package org.apache.solr.update;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.InflaterInputStream;

import org.adsabs.InvenioBitSet;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.solr.core.CloseHook;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.dataimport.Context;


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
 * 
 * NOTE: I HAVE to implement connection pool; current situation is not
 * thread safe!
 */

public enum InvenioDB {
  INSTANCE;
  
  private volatile Connection connection = null;
  private String tableToQuery;
  private int openedStmts;
  private int errors;
  
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
   * @throws ParseException 
   */
  
  public BatchOfInvenioIds getRecidsChanges(Integer last_recid, Integer max_recs, String mod_date) throws SQLException, ParseException {
    return this.getRecidsChanges(last_recid, max_recs, mod_date, this.getBibRecTableName());
  }
  
  public BatchOfInvenioIds getRecidsChanges(Integer last_recid, Integer max_recs, String mod_date, String table) throws SQLException, ParseException {
    
    if (table == null) 
      table = "bibrec";
    
    if (last_recid == null)
      last_recid = -1;
    
    if (max_recs == null)
      max_recs = 10000;
    
    table = StringEscapeUtils.escapeSql(table);
    String search_op = ">";
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    
    if (mod_date == null || mod_date.trim() == "") {
      if (last_recid == -1) {
        ResultSet mDate = getResultSet("SELECT modification_date FROM `" + table + "` ORDER BY modification_date ASC LIMIT 1");
        if (mDate.first()) {
          mod_date = df.format(df.parse(mDate.getString("modification_date")));
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
          mod_date = df.format(df.parse(recDate.getString("modification_date")));
        }
        else {
          // find the closes match
          closeResultSet(recDate);
          
          //# let's make sure we have a valid recid (or get the close valid one) BUT this could mean we get the wrong
          //# modification date! (and could result in non-ending loop!)
          
          recDate = getResultSet("SELECT id, modification_date FROM `" + table + "` WHERE id >= " + last_recid + " LIMIT 1");
          if (recDate.first()) {
            System.err.println(String.format("Warning, the last_recid %s does not exist, we have found the closest higher id %s with mod_date: %s\n", last_recid, df.format(df.parse(recDate.getString("modification_date")))));
          }
          else {
            // nothing is there
            closeResultSet(recDate);
            return null;
          }
          
          last_recid = recDate.getInt("id");
          mod_date = df.format(df.parse(recDate.getString("modification_date")));
          closeResultSet(recDate);
        }
        
      }
    }
    
    // now we have mod_date and/or last_recid and we'll retrieve changed recs since x....
    ResultSet mRecs = getResultSet("SELECT id,modification_date, creation_date FROM `" + table +
        "` WHERE modification_date " + search_op + " '" + mod_date + "' ORDER BY modification_date ASC, id ASC LIMIT " + max_recs);
    if (mRecs == null)
      return null;
    
    ArrayList<ModRec> modified_records = new ArrayList<ModRec>(mRecs.getFetchSize());
    while (mRecs.next()) {
      modified_records.add(new ModRec(mRecs.getInt("id"), 
          df.parse(mRecs.getString("creation_date")), 
          df.parse(mRecs.getString("modification_date"))));
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
        modified_records.add(new ModRec(mRecs.getInt("id"), df.parse(mRecs.getString("creation_date")), df.parse(mRecs.getString("modification_date"))));
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
      dels.add(delRecs.getInt("id_bibrec"));
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
    
    String lastModDate = df.format(modified_records.get(modified_records.size()-1).modDate);
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
          ccs.add("(id_bibrec>=" + StringEscapeUtils.escapeSql(ints[0]) + " AND id_bibrec<=" + StringEscapeUtils.escapeSql(ints[1]) + ")");
        }
        else {
          ccs.add("(id_bibrec=" + StringEscapeUtils.escapeSql(c) + ")");
        }
      }
      
      query.append(" (");
      boolean isFirst = true;
      for (String c: ccs) {
        if (isFirst) {
          query.append(c);
          isFirst = false;
        }
        else {
          query.append(" OR ");
          query.append(c);
        }
      }
      query.append(")");
      
      // now retrieve the record xml
      ResultSet rs = getResultSet(query.toString());
      if (rs != null) {
        while (rs.next()) {
          
          //IOUtils.copy(rs.getBinaryStream("value"), new FileOutputStream("/tmp/from.java"));
          
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
    InflaterInputStream zipStream = new InflaterInputStream(data);
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
  
  public void close() {
    if (this.connection != null) {
      try {
        this.connection.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
      this.connection = null;
    }
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
      this.connection = conn;
    }
    return this.connection;
  }
  
  private Connection getConnection(String url) throws SQLException {
    Connection conn = DriverManager.getConnection(
        url != null ?
            url 
            :
              "jdbc:drizzle://localhost/invenio?" +
        "user=invenio&password=invenio");
    Statement stmt = conn.createStatement();
    stmt.execute("select count(*) from bibrec");
    ResultSet rs = stmt.getResultSet();
    while (rs.next()) {
      //System.out.println("connected to db with: " + rs.getInt(1));
    }
    closeResultSet(rs);
    
    return conn;
  }
  
  private void loadDriver(String name) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    // mysql driver has a bug: http://bugs.mysql.com/bug.php?id=36565 (leaking threads)
    //Class<?> clazz = Class.forName(name == null ? "com.mysql.jdbc.Driver" : name);
    Class<?> clazz = Class.forName(name == null ? "org.drizzle.jdbc.DrizzleDriver" : name);
    Object inst = clazz.newInstance();
  }
  
  private ResultSet getResultSet(String query, Object...args) throws SQLException {
    System.err.println(query);
    try {
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
        openedStmts += 1;
        this.errors = 0;
        return stmt.getResultSet();
      }
      else {
        if (stmt != null) {
          stmt.close();
        }
        return null;
      }
    }
    catch (SQLNonTransientConnectionException e) {
      if (this.errors > 5)
        throw e;
      
      this.errors += 1;
      this.close();       // try reconnecting
      return getResultSet(query, args);
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
    openedStmts -= 1;
  }
  
  class ModRec {
    private int recid;
    private java.util.Date createDate;
    private java.util.Date modDate;
    
    public ModRec(int recid, java.util.Date date, java.util.Date date2) {
      this.recid = recid;
      this.createDate = date;
      this.modDate = date2;
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
  
  public void init(Context context) {
    context.getSolrCore().addCloseHook(new CloseHook() {
      @Override
      public void preClose(SolrCore core) {
        InvenioDB.INSTANCE.close();
      }
      @Override
      public void postClose(SolrCore core) {
      }
    });
  }
  
  /**
   * 
   * def test_get_recids():
    """This is used to manually run and compare results against the DB
    
    note: to get mapping from recids into bibcodes:
    
    allinv = {}
    for x in dbquery.run_sql('select t1.value,t2.id_bibrec from bib97x as t1 inner join bibrec_bib97x as t2 ON t1.id=t2.id_bibxxx WHERE t1.tag="970__a"'):    
      allinv[int(x[1])] = x[0].lower()

    """
    
    total = 0
    lastid=-1
    moddate=None
    dtotal=0
    utotal=0
    atotal=0
    set_added = set()
    set_deleted = set()
    set_updated = set()
    
    while lastid is not None:
        print lastid, moddate
        if lastid == -1:
            total = 0
            row = 0
        results = get_recids_changes(lastid, max_recs=1000000)
        if results is None:
            break
        d, lastid, moddate = results
        row = sum(len(x) for x in d.values())
        total += row
        deleted, updated, added = len(d['DELETED']), len(d['UPDATED']), len(d['ADDED'])
        dtotal += deleted
        utotal += updated
        atotal += added
        print "del=%s (%s) upd=%s (%s) add=%s (%s) row=%s total=%s" % (deleted, dtotal, updated, utotal, added, atotal, row, total)
        set_updated.update(d['UPDATED'])
        set_added.update(d['ADDED'])
        set_deleted.update(d['DELETED'])
    
    totalrecs = dbquery.run_sql("select count(id) from bibrec")[0][0]
    deleted_ids = [a[0] for a in list(dbquery.run_sql("""SELECT distinct(id_bibrec) FROM bibrec_bib98x WHERE id_bibxxx='%s' ORDER BY id_bibrec""", (197,)))]
    
    print "\ndatabse:"
    print "totalrecs=", totalrecs
    print "liverecs (%s-%s)= %s" % (totalrecs, len(deleted_ids), totalrecs-len(deleted_ids))
    
    print "\ndiscovered:"
    trex = (sum((dtotal, utotal, atotal)))
    print "totalrecs=%s (vefification=%s)" % (trex, len(set_updated) + len(set_added) + len(set_deleted))
    print "liverecs (%s-%s)= %s" % (trex, dtotal, trex-dtotal)
    
    
    print "\ndifference %s-%s=%s" % (totalrecs-len(deleted_ids), trex-dtotal, totalrecs-len(deleted_ids)-(trex-dtotal))
    
    deleted_ids = set(deleted_ids)
    
    lost_recs = {'in_del':[], 'in_upd':[], 'in_add':[], 'present_in_one':[], 'present_in_many':[], 'present_in_none':[]}
    for r_del in deleted_ids:
      counter = 0
      if r_del in set_deleted:
        lost_recs['in_del'].append(r_del)
        counter += 1
      if r_del in set_updated:
        lost_recs['in_upd'].append(r_del)
        counter += 1
      if r_del in set_added:
        lost_recs['in_add'].append(r_del)
        counter += 1
      if counter == 1:
        lost_recs['present_in_one'].append(r_del)
      elif counter > 1:
        lost_recs['present_in_many'].append(r_del)
      else:
        lost_recs['present_in_none'].append(r_del)
      
    for k, v in lost_recs.items():
      print "%s=%s" % (k, len(v))
   * @throws SQLException 
   * @throws ParseException 
   */
  public void testGetChangedRecids() throws SQLException, ParseException {
    Integer total = 1;
    Integer lastid =-1;
    String moddate = null;
    Integer dtotal = 0;
    Integer utotal = 0;
    Integer atotal=0;
    HashSet<Integer> set_added = new HashSet<Integer>();
    HashSet<Integer> set_deleted = new HashSet<Integer>();
    HashSet<Integer> set_updated = new HashSet<Integer>();
    
    Integer row = null;
    BatchOfInvenioIds results;
    Integer deleted, updated, added;
    while (lastid != null) {
      System.out.println(lastid + ", " + moddate);
      if (lastid == -1) {
        total = 0;
        row = 0;
      }
      
      results = getRecidsChanges(lastid, 100000, moddate);
      
      if (results == null)
        break;
      
      deleted = (results.deleted != null ? results.deleted.size() : 0);
      updated = (results.added != null ? results.added.size() : 0);
      added  = (results.updated != null ? results.updated.size() : 0);
      
      row = deleted + updated + added;
      total += row;
      
      dtotal += deleted;
      atotal += added;
      utotal += updated;
      
      System.out.println(String.format("del=%s (%s) upd=%s (%s) add=%s (%s) row=%s total=%s", 
          deleted, dtotal, updated, utotal, added, atotal, row, total));
    }
    
    ResultSet r = getResultSet("select count(id) from bibrec");
    int totalrecs = r.getInt(1);
    closeResultSet(r);
    
    HashSet<Integer> deleted_ids = new HashSet<Integer>();
    r = getResultSet("select distinct(id_bibrec) from bibrec_bib98x where id_bibxxx=197 order by id_bibrec");
    while (r.next()) {
      deleted_ids.add(r.getInt(1));
    }
    closeResultSet(r);
    
    System.out.println("\ndatabase:");
    System.out.println("totalrecs=" + totalrecs);
    System.out.println(String.format("liverecs (%s-%s)= %s", totalrecs, deleted_ids.size(), totalrecs-deleted_ids.size()));
    
    int trex = dtotal + utotal + atotal;
    System.out.println("\ndiscovered:");
    System.out.println(String.format("totalrecs=%s (vefification=%s)", trex, set_updated.size() + set_added.size() + set_deleted.size()));
    System.out.println(String.format("liverecs (%s-%s)= %s", trex, dtotal, trex-total));
    
    System.out.println(String.format("\ndifference %s-%s=%s", totalrecs-deleted_ids.size(), trex-dtotal, totalrecs-deleted_ids.size()-trex-dtotal));
    
    HashMap<String,List<Integer>> lost_recs = new HashMap<String, List<Integer>>();
    for (String v: new String[]{"ind_del", "in_upd", "in_add", "present_in_one", "present_in_many", "present_in_none"}) {
      lost_recs.put(v, new ArrayList<Integer>());
    }
    
    for (Integer r_del: deleted_ids) {
      int counter = 0;
      if (set_deleted.contains(r_del)) {
        lost_recs.get("in_del").add(r_del);
        counter += 1;
      }
      if (set_deleted.contains(r_del)) {
        lost_recs.get("in_upd").add(r_del);
        counter += 1;
      }
      if (set_deleted.contains(r_del)) {
        lost_recs.get("in_add").add(r_del);
        counter += 1;
      }
      if (counter == 1) {
        lost_recs.get("present_in_one").add(r_del);
      }
      else if (counter > 1) {
        lost_recs.get("present_in_many").add(r_del);
      }
      else {
        lost_recs.get("present_in_none").add(r_del);
      }
    }
    
    for (Entry<String,List<Integer>> es: lost_recs.entrySet()) {
      System.out.println(String.format("%s=%s", es.getKey(), es.getValue().size()));
    }
  }
  
  
  /**
   * A helper function to create a tmp table that stores records we
   * want to index; python ref impl:
   * 
   * def create_collection_bibrec(table_name, coll_name, step_size=10000, max_size=-1):
    if table_name[0] != '_':
        raise Exception("By convention, temporary tables must begin with '_'. I don't want to give you tools to screw st important")
    
    create_stmt = dbquery.run_sql("SHOW CREATE TABLE bibrec")[0][1].replace('bibrec', dbquery.real_escape_string(table_name))
    dbquery.run_sql("DROP TABLE IF EXISTS `%s`" % dbquery.real_escape_string(table_name))
    dbquery.run_sql(create_stmt)
    
    # now retrieve the collection
    c = search_engine.get_collection_reclist(coll_name)
    # reverse sort it
    c = sorted(c, reverse=True)
    
    if len(c) < 0:
        sys.stderr.write("The collection %s is empty!\n" % coll_name)
    
    c = list(c)
    l = len(c)
    if max_size > 0:
        l = max_size
    i = 0
    sys.stderr.write("Copying bibrec data, patience please...\n")
    while i < l:
        dbquery.run_sql("INSERT INTO `%s` SELECT * FROM `bibrec` WHERE bibrec.id IN (%s)" % 
                             (dbquery.real_escape_string(table_name), ','.join(map(str, c[i:i+step_size]))))
        i = i + len(c[i:i+step_size])
        #sys.stderr.write("%s\n" % i)
        
    sys.stderr.write("Total number of records: %s Copied: %s\n" % (len(c), min(l, len(c))))
   * @throws Exception 
   **/
  public void create_collection_bibrec(String table_name, String coll_name, Integer step_size, Integer max_size) throws Exception {
    
    if (step_size == null)
      step_size = 1000;
    if (max_size == null)
      max_size = -1;
    
    if (table_name.substring(0, 1).equals("_")) {
      throw new Exception("By convention, temporary tables must begin with '_'. I don't want to give you tools to screw st important");
    }
    
    ResultSet r = getResultSet("SHOW CREATE TABLE bibrec");
    if (!r.first()) {
      throw new SQLException("Missing table bibrec");
    }
    String create_stmt = r.getString(1).replace("bibrec", StringEscapeUtils.escapeSql(table_name));
    closeResultSet(r);
    
    r = getResultSet("DROP TABLE IF EXISTS `" + StringEscapeUtils.escapeSql(table_name) + "`");
    if (r != null) {
      closeResultSet(r);
    }
    
    r = getResultSet(create_stmt);
    if (r != null) {
      closeResultSet(r);
    }
    
    List<Integer> c = getCollectionRecids(coll_name);
    if (c == null || c.size() == 0) {
      throw new SQLException("No data for colleciton: " + coll_name);
    }
    
    Collections.reverse(c);
    
    int l = max_size > 0 ? max_size : c.size();
    int i = 0;
    
    System.err.println("Copying data, patience please...");
    
    while (i < l) {
      StringBuilder ids = new StringBuilder();
      boolean first = true;
      int j = 0;
      for (; j<i+step_size; j++) {
        if (first) {
          ids.append(c.get(j));
          first = false;
        }
        else {
          ids.append(",");
          ids.append(c.get(j));
        }
      }
      String q = String.format("INSERT INTO `%s` SELECT * FROM `bibrec` WHERE bibrec.id IN (%s)",
          StringEscapeUtils.escapeSql(table_name),
          ids.toString());
      
      r = getResultSet(q);
      if (r != null) {
        closeResultSet(r);
      }
      
      i = i + j;
    }
    
  }
  
  private List<Integer> getCollectionRecids(String coll) throws SQLException {
    ResultSet rs = getResultSet("SELECT nbrecs,reclist FROM collection WHERE name='" + StringEscapeUtils.escapeSql(coll) + "'");
    try {
      if (rs.next()) {
        InvenioBitSet data = new InvenioBitSet(rs.getBytes("reclist"));
        ArrayList<Integer> out = new ArrayList<Integer>(data.cardinality());
        for (int i = data.nextSetBit(0); i >= 0; i = data.nextSetBit(i+1)) {
          out.add(i);
        }
        return out;
      }
      return null;
    }
    finally {
      closeResultSet(rs);
    }
  }

}
