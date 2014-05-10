package org.apache.solr.handler.batch;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldCache.Ints;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.update.InvenioDB;
import org.apache.solr.update.InvenioDB.BatchOfInvenioIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchProviderIndexInvenio extends BatchProvider {
	
	public static final Logger log = LoggerFactory
		.getLogger(BatchProviderIndexInvenio.class);
	
	private BitSet toAdd = new BitSet();
	private BitSet toDelete = new BitSet();
	private BitSet toDoNothing = new BitSet();
	
	@Override
	public void run(SolrQueryRequest locReq, BatchHandlerRequestQueue queue)
	    throws Exception {
		
		SolrParams params = locReq.getParams();
		if (params.get("last_recid", null) == null) {
			IndexSchema schema = locReq.getSchema();
			assert schema.getFieldOrNull("indexstamp") != null;
			
			SolrIndexSearcher searcher = locReq.getSearcher();
			ScoreDoc[] hits = null;
      Sort sort = new Sort(SortField.FIELD_SCORE,
                           new SortField("indexstamp", SortField.Type.LONG, true));
      hits = searcher.search(new MatchAllDocsQuery(), 1, sort).scoreDocs;
      
      if (hits.length == 0) {
      	toAdd.clear();
        toDelete.clear();
        toDoNothing.clear();
      }
      else {
      	Document doc = searcher.getIndexReader().document(hits[0].doc);
      	String timestamp = doc.get("indexstamp"); // "2013-07-08T21:37:48.834Z" 
        // we need "%Y-%m-%d %H:%M:%S"
      	String simpleTimestamp = timestamp.substring(0, timestamp.indexOf('.')).replace("T", " "); 
      	ModifiableSolrParams mParams = new ModifiableSolrParams(params);
      	mParams.add("mod_date", simpleTimestamp);
      	locReq.setParams(mParams);
      }
      
		}
		else if (params.get("last_recid", null) != null || params.getInt("last_recid", 0) == -1) {
      toAdd.clear();
      toDelete.clear();
      toDoNothing.clear();
    }
    
    discoverMissingRecords(locReq, queue);

	}

	@Override
	public String getDescription() {
		return "Re-index Invenio documents";
	}
	
	private Map<Integer, Map<Integer, Integer>> tmpMap = new HashMap<Integer, Map<Integer,Integer>>();
  private void discoverMissingRecords(SolrQueryRequest req, BatchHandlerRequestQueue queue) throws IOException, SQLException, ParseException {
  	
    // get recids from Invenio {'ADDED': int, 'UPDATED': int, 'DELETED':
    // int }
    SolrQueryResponse rsp = new SolrQueryResponse();
    
    HashMap<String, int[]> dictData = null;
    
    SolrParams params = req.getParams();
    String field = params.get("field", "recid");
    Integer lastRecid = params.getInt("last_recid", -1);
    String modDate = params.get("mod_date", null);
    Integer fetchSize = Math.min(params.getInt("fetch_size", 100000), 100000);
    // setting maxRecs to very large value means the worker cannot be stopped in time
    int maxRecs = Math.min(params.getInt("max_records", 100000), 1000000);
    
    
    Ints existingRecs = FieldCache.DEFAULT.getInts(req.getSearcher().getAtomicReader(), field, false);
    Map<Integer, Integer> idToLuceneId;
    
    if (tmpMap.containsKey(existingRecs.hashCode())) {
    	idToLuceneId = tmpMap.get(existingRecs.hashCode());
    }
    else {
    	tmpMap.clear();
    	int m = req.getSearcher().maxDoc();
    	idToLuceneId = new HashMap<Integer, Integer>(m);
    	for (int i=0;i<m;i++) {
        idToLuceneId.put(existingRecs.get(i), i);
      }
    }
    
    
    
    int doneSoFar = 0;
    boolean finished = false;
    
    log.info("Checking database for changed records; last_recid={}", lastRecid);
    
    while (doneSoFar<maxRecs) {

      
      BatchOfInvenioIds results = InvenioDB.INSTANCE.getRecidsChanges(lastRecid, fetchSize, modDate);
      
      if (results == null) {
        finished = true;
        tmpMap.clear();
        break;
      }
      
      if (results.added != null) {
        doneSoFar += results.added.size();
        for (Integer x: results.added) {
          if (idToLuceneId.containsKey(x)) {
            toDoNothing.set(x);
          }
          else {
            toAdd.set(x);
          }
        }
      }
      
      if (results.updated != null) {
        doneSoFar += results.updated.size();
        for (Integer x: results.updated) {
          if (idToLuceneId.containsKey(x)) {
            toDoNothing.set(x);
          }
          else {
            toAdd.set(x);
          }
        }
      }
      
      if (results.deleted != null) {
        doneSoFar += results.deleted.size();
        for (int x: results.deleted) {
        	if (idToLuceneId.containsKey(x)) {
        		toDelete.set(x);
        	}
        }
      }
      
      lastRecid = results.lastRecid;
      modDate = results.lastModDate;
      
      log.info("Checking database; restart_from={}; found={}", lastRecid, doneSoFar);
      
    }
    
    if (!finished) {
      ModifiableSolrParams mp = new ModifiableSolrParams(params);
      mp.set("last_recid", lastRecid);
      mp.set("mod_date", modDate);
      mp.remove(this.getName());
      queue.registerNewBatch(this, mp);
    }
    else {
      //queue.registerNewBatch("#index-discovered", "index-discovered");
    }
    
  }

}
