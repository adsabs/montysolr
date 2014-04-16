package org.apache.solr.update;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.zip.DeflaterInputStream;
import java.util.zip.Inflater;

import org.apache.commons.lang.StringEscapeUtils;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * this is a <b>throw away!</b> code - just a temporary replacement of the 
 * python functionality; the whole workflow of pulling data from the db
 * should be converted into queue/pushing -- so this will get removed!!!
 */

public enum InvenioDB {
  INSTANCE;
  
  private Connection connection = null;
  private String tableToQuery;
  
  public void setBibRecTableName(String table) {
    this.tableToQuery = table;
  }
  
  public String getBibRecTableName() {
    return this.tableToQuery;
  }
  
  /**
   *  
    Retrieves the sets of records that were added/updated/deleted
    
    Reference python impl:
    
    def get_recids_changes(last_recid, max_recs=10000, mod_date=None, table='bibrec'):
    """
    Retrieves the sets of records that were added/updated/deleted
    
    The time is selected according to some know recid, ie. 
    we retrieve the modification time of one record and look
    at those that are older. 
    
        OR
        
    You can pass in the date that you are interested in, in the 
    format: YYYY-MM-DD HH:MM:SS
    
    added => bibrec.modification_date == bibrec.creation_date
    updated => bibrec.modification_date >= bibrec.creation_date
    deleted => bibrec.status == DELETED
    
    We usually return list of max_recs size, however if there 
    are records past max_recs size and their modification date 
    is the same, we'll add them too. This is a workaround and 
    necessary because Invenio doesn't use timestamp with high
    enough granularity
    """
    
    table = dbquery.real_escape_string(table)
    search_op = '>'    
    if not mod_date:
        if last_recid == -1:
            l = list(dbquery.run_sql("SELECT modification_date FROM `%s` ORDER BY modification_date ASC LIMIT 1" % (table,)))
            mod_date = l[0][0].strftime(format="%Y-%m-%d %H:%M:%S")
            search_op = '>='
        else:
            l = list(dbquery.run_sql("SELECT id, modification_date FROM `" + table + "` WHERE id = %s LIMIT 1", (last_recid,)))
            if not len(l):
                # let's make sure we have a valid recid (or get the close valid one) BUT this could mean we get the wrong
                # modification date! (and could result in non-ending loop!)
                l = list(dbquery.run_sql("SELECT id, modification_date FROM `" + table + "` WHERE id >= %s LIMIT 1", (last_recid,)))
                if len(l):
                    sys.stderr.write("Warning, the last_recid %s does not exist, we have found the closest higher id %s with mod_date: %s\n" %
                           (l[0][0], l[0][1].strftime(format="%Y-%m-%d %H:%M:%S")))
            if not len(l):
                return
            last_recid = l[0][0]
            mod_date = l[0][1].strftime(format="%Y-%m-%d %H:%M:%S")
            
            # there is not api to get this (at least i haven't found it)
            #mod_date = search_engine.get_modification_date(last_recid, fmt="%Y-%m-%d %H:%i:%S")
            #if not mod_date:
            #    return
        
    modified_records = list(dbquery.run_sql("SELECT id,modification_date, creation_date FROM `" + table +
                    "` WHERE modification_date " + search_op + " '%s' ORDER BY modification_date ASC, id ASC LIMIT %s" %
                    (mod_date, max_recs )))
    
    #sys.stderr.write(str(("SELECT id,modification_date, creation_date FROM bibrec "
    #                "WHERE modification_date " + search_op + "\"%s\" ORDER BY modification_date ASC, id ASC LIMIT %s" %
    #                (mod_date, max_recs ))) + "\n")
    #print len(modified_records)
    
    if not len(modified_records):
        return
    
    # because invenio understands regularity of only one sec (which is very stupid) we must make sure
    # that we include all records that were modified in that one second to close the group
    #if modified_records[-1][1].strftime(format="%Y-%m-%d %H:%M:%S") == mod_date:
    for to_add in list(dbquery.run_sql("SELECT id,modification_date, creation_date FROM `" + table +
                    "` WHERE modification_date = '%s' AND id > %s ORDER BY id ASC" %
                    (modified_records[-1][1].strftime(format="%Y-%m-%d %H:%M:%S"), modified_records[-1][0]))):
            modified_records.append(to_add)
            
    
    added = []
    updated = []
    deleted = []
    

    # find records that are marked as deleted, this can select lots of recs (in the worst case
    # all deleted recs) but usually it will work with a subrange
    rec_ids = sorted([r[0] for r in modified_records])
    local_min = rec_ids[0]
    local_max = rec_ids[-1]

    dels = {}
    for x in list(dbquery.run_sql("""SELECT distinct(id_bibrec) FROM bibrec_bib98x WHERE 
        (id_bibrec >= %s AND id_bibrec <= %s) AND 
        id_bibxxx=(SELECT id FROM bib98x WHERE VALUE='DELETED')""" % (local_min, local_max))):
        dels[int(x[0])] = 1
    #print "delets local min=%s max=%s found=%s" % (local_min, local_max, len(dels))
    
    
    for recid, mod_date, create_date in modified_records:
        recid = int(recid)
        
        # this is AWFULLY slow! 100x times
        #status = search_engine.record_exists(recid)
        
        if recid in dels:
        #if status == -1:
            deleted.append(recid)
        elif mod_date == create_date:
            added.append(recid)
        else:
            updated.append(recid)
    
    return {'DELETED': deleted, 'UPDATED': updated, 'ADDED': added}, recid, mod_date.strftime(format="%Y-%m-%d %H:%M:%S")

    
   * @param lastRecid
   * @param maxRecords
   * @param lastModDate
   * @param table
   * @throws SQLException 
   */
  
  public BatchOfInvenioIds getRecidsChanges(Integer last_recid, Integer max_recs, String mod_date) throws SQLException {
    return this.getRecidsChanges(last_recid, max_recs, mod_date, this.getBibRecTableName());
  }
  
  public BatchOfInvenioIds getRecidsChanges(Integer last_recid, Integer max_recs, String mod_date, String table) throws SQLException {
    
    if (table == null) 
      table = "bibrec";
    
    if (last_recid == null)
      last_recid = -1;
    
    table = StringEscapeUtils.escapeSql(table);
    String search_op = ">";
    DateFormat df = new SimpleDateFormat("%Y-%m-%d %H:%M:%S");
    
    
    if (mod_date == null) {
      if (last_recid == -1) {
        ResultSet mDate = getResultSet("SELECT modification_date FROM `" + table + "` ORDER BY modification_date ASC LIMIT 1");
        if (mDate.first()) {
          mod_date = df.format(mDate.getDate("modification_date"));
          search_op = ">=";
          closeResultSet(mDate);
        }
        else {
          closeResultSet(mDate);
          throw new SQLException("Empty table: " + table);
        }
      }
      else {
        ResultSet recDate = getResultSet("SELECT id, modification_date FROM `" + table + "` WHERE id = " + last_recid + " LIMIT 1");
        if (recDate.first()) {
          last_recid = recDate.getInt("id");
          mod_date = df.format(recDate.getDate("modification_date"));
        }
        else {
          // find the closes match
          closeResultSet(recDate);
          
          //# let's make sure we have a valid recid (or get the close valid one) BUT this could mean we get the wrong
          //# modification date! (and could result in non-ending loop!)
          
          recDate = getResultSet("SELECT id, modification_date FROM `" + table + "` WHERE id >= " + last_recid + " LIMIT 1");
          if (recDate.first()) {
            System.err.println(String.format("Warning, the last_recid %s does not exist, we have found the closest higher id %s with mod_date: %s\n", last_recid, df.format(recDate.getDate("modification_date"))));
          }
          else {
            // nothing is there
            return null;
          }
          
          last_recid = recDate.getInt("id");
          mod_date = df.format(recDate.getDate("modification_date"));
          closeResultSet(recDate);
        }
        
      }
    }
    
    ResultSet mRecs = getResultSet("SELECT id,modification_date, creation_date FROM `" + table +
        "` WHERE modification_date " + search_op + " '" + mod_date + "' ORDER BY modification_date ASC, id ASC LIMIT " + max_recs);
    if (mRecs == null)
      return null;
    
    ArrayList<ModRec> modified_records = new ArrayList<ModRec>(mRecs.getFetchSize());
    while (mRecs.next()) {
      modified_records.add(new ModRec(mRecs.getInt("id"), mRecs.getDate("creation_date"), mRecs.getDate("modification_date")));
    }
    closeResultSet(mRecs);
    
    if (modified_records.size() == 0) {
      return null;
    }
    
    //# because invenio understands regularity of only one sec (which is very stupid) we must make sure
    //# that we include all records that were modified in that one second to close the group
    //#if modified_records[-1][1].strftime(format="%Y-%m-%d %H:%M:%S") == mod_date:
    mRecs = getResultSet("SELECT id,modification_date, creation_date FROM `" + table +
                    "` WHERE modification_date = '" + df.format(modified_records.get(modified_records.size()-1).modDate) + 
                    "' AND id > " + modified_records.get(modified_records.size()-1).recid + " ORDER BY id ASC");
    if (mRecs != null) {
      while (mRecs.next()) {
        modified_records.add(new ModRec(mRecs.getInt("id"), mRecs.getDate("creation_date"), mRecs.getDate("modification_date")));
      }
      closeResultSet(mRecs);
    }
    
    int local_max = modified_records.get(0).recid;
    int local_min = local_max;
    
    for (ModRec mr: modified_records) {
      if (mr.recid > local_max) {
        local_max = mr.recid;
      }
      if (mr.recid < local_min) {
        local_min = mr.recid;
      }
    }
    
    //# find records that are marked as deleted, this can select lots of recs (in the worst case
    //# all deleted recs) but usually it will work with a subrange
    
    HashSet<Integer> dels = new HashSet<Integer>();
    ResultSet delRecs = getResultSet("SELECT distinct(id_bibrec) FROM bibrec_bib98x WHERE " +
        "(id_bibrec >= " + local_min + " AND id_bibrec <= " + local_max + ") AND " +
        "id_bibxxx=(SELECT id FROM bib98x WHERE VALUE='DELETED')");
    
    while(delRecs.next()) {
      dels.add(delRecs.getInt(0));
    }
    closeResultSet(delRecs);
    
    ArrayList<Integer> added = new ArrayList<Integer>();
    ArrayList<Integer> updated = new ArrayList<Integer>();
    ArrayList<Integer> deleted = new ArrayList<Integer>();
    
    for (ModRec mr: modified_records) {
      Integer recid = new Integer(mr.recid);
      if (dels.contains(recid)) {
        deleted.add(recid);
      }
      else if (mr.modDate.equals(mr.createDate)) {
        added.add(recid);
      }
      else {
        updated.add(recid);
      }
    }
    
    String lastModDate = df.format(modified_records.get(modified_records.size()-1));
    int lastRecid = modified_records.get(modified_records.size()-1).recid;
    
    
    return new BatchOfInvenioIds(lastRecid, lastModDate, added, updated, deleted);
                      
  }
  
  /**
   * Retrieves data from Invenio; the pythonic reference implementation was:
   * 
   * def invenio_search_xml2(kwargs):
    """Simple version which just fetches XML records
    from Invenio. It only understands query of type:
    p=recid:1->50 OR recid:50 OR recid:....
    
    This is an optimized version
    
    """
    out = []
    p = kwargs['p']
    of = 'xm'
    if 'of' in kwargs:
        of = kwargs['of']

    if of == 'xm':
        out.append('<?xml version="1.0" encoding="UTF-8"?>')
        out.append('<collection xmlns="http://www.loc.gov/MARC21/slim">')
    
    query = ['SELECT id_bibrec, `value` FROM bibfmt WHERE `format`=\'xm\' AND']
    
    # first expand the recids
    clauses = p.split(' OR ')
    ccs = []
    for c in clauses:
        c = c.replace('recid:', '')
        if '->' in c:
            ints = c.split('->')
            ccs.append('(id_bibrec>=%s AND id_bibrec<=%s)' % tuple(ints))
        else:
            ccs.append('(id_bibrec=%s)' % c)
    
    query.append('(')
    query.append(' OR '.join(ccs))
    query.append(')')
    
    
    query = ' '.join(query)    
    # now retrieve the record xml
    decompress = zlib.decompress
    for value in dbquery.run_sql(query):
        ### In case of corruption, we fail (intentionally)
        out.append(decompress(value[1]))
            
    
    if of == 'xm':
        out.append('</collection>')
    
    return '\n'.join(out)
   * @param recids
   * @return
   * @throws SQLException 
   * @throws IOException 
   */
  
  public String getMarcXML(Map<String, List<String>> params) throws SQLException, IOException {
    StringBuilder out = new StringBuilder();
    
    
    List<String> p = params.get("p");
    
    out.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    out.append("<collection xmlns=\"http://www.loc.gov/MARC21/slim\">");
    
    
    for (String pVal: p) {
      StringBuilder query = new StringBuilder();
      query.append("SELECT id_bibrec, `value` FROM bibfmt WHERE `format`=\'xm\' AND ");
      
      String[] clauses = pVal.split(" OR ");
      ArrayList<String> ccs = new ArrayList<String>();
      for (String c: clauses) {
        c = c.replace("recid:", "");
        if (c.contains("->")) {
          String[] ints = c.split("->");
          ccs.add("(id_bibrec>=" + StringEscapeUtils.escapeSql(ints[0]) + " AND id_bibrec<=" + StringEscapeUtils.escapeSql(ints[0]) + ")");
        }
        else {
          ccs.add("(id_bibrec=" + StringEscapeUtils.escapeSql(c) + ")");
        }
      }
      
      query.append(" (");
      boolean isFirst = true;
      for (String c: ccs) {
        if (!isFirst) {
          query.append(c);
        }
        query.append(" OR ");
        query.append(c);
        isFirst = false;
      }
      query.append(")");
      
      // now retrieve the record xml
      ResultSet rs = getResultSet(query.toString());
      if (rs != null) {
        while (rs.next()) {
          
          out.append(unZip(rs.getBinaryStream("value")));
          out.append("\n");
        }
        closeResultSet(rs);
      }
    }
    
    out.append("</collection>");
    return out.toString();
  }
  
  private String unZip(InputStream data) throws IOException {
    DeflaterInputStream zipStream = new DeflaterInputStream(data);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    byte[] buffer = new byte[1024];
    int len;
    while ((len = zipStream.read(buffer)) > 0) {
      baos.write(buffer, 0, len);
    }
    data.close();
    zipStream.close();
    return baos.toString("UTF-8");
  }
  
  private Connection connect() throws SQLException {
    if (this.connection == null) {
      try {
        loadDriver(System.getProperty("montySolrInvenioDriver", null));
      } catch (InstantiationException e) {
        throw new SQLException(e);
      } catch (IllegalAccessException e) {
        throw new SQLException(e);
      } catch (ClassNotFoundException e) {
        throw new SQLException(e);
      }
      Connection conn = this.getConnection(System.getProperty("montySolrInvenio"));
      if (conn == null) {
        throw new SQLException("Cannot connect to Invenio DB: " + System.getProperty("montySolrInvenio"));
      }
    }
    return this.connection;
  }
  
  private Connection getConnection(String url) throws SQLException {
    Connection conn = DriverManager.getConnection(
        url != null ?
        url 
        :
        "jdbc:mysql://localhost/test?" +
        "user=monty&password=greatsqldb");
    return conn;
  }
  
  private void loadDriver(String name) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      Class.forName(name != null ? "com.mysql.jdbc.Driver" : name).newInstance();
  }
  
  private ResultSet getResultSet(String query, Object...args) throws SQLException {
    
    Connection conn = connect();
    Statement stmt = null;
    
    if (args.length > 0) {
      PreparedStatement stmtx = conn.prepareStatement(query);
      for (int i=0; i<args.length;i++) {
        stmtx.setObject(i, args[i]);
      }
      stmt = stmtx;
    }
    else {
      stmt = conn.createStatement();
    }
    
    if (stmt.execute(query)) {
        return stmt.getResultSet();
    }
    else {
      if (stmt != null) {
        stmt.close();
      }
      return null;
    }
  }
  
  private void closeResultSet(ResultSet rs) throws SQLException {
    Statement stmt = rs.getStatement();
    try {
      rs.close();
    }
    catch (SQLException sqlEx) { } // ignore
    
    if (stmt != null) {
      try {
        stmt.close();
      } catch (SQLException sqlEx) { } // ignore
      stmt = null;
    }
  }
  
  class ModRec {
    private int recid;
    private Date createDate;
    private Object modDate;

    public ModRec(int recid, Date cDate, Date mDate) {
      this.recid = recid;
      this.createDate = cDate;
      this.modDate = mDate;
    }
  }
  
  public static class BatchOfInvenioIds {
    public int lastRecid;
    public String lastModDate;
    public List<Integer> added;
    public List<Integer> updated;
    public List<Integer> deleted;

    public BatchOfInvenioIds(int lastRecid, String lastModDate, List<Integer> added, List<Integer> updated, List<Integer> deleted) {
      this.lastRecid = lastRecid;
      this.lastModDate = lastModDate;
      this.added = added;
      this.updated = updated;
      this.deleted = deleted;
    }
  }
}
