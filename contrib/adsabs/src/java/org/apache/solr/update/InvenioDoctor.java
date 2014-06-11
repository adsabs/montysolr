package org.apache.solr.update;

/**
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.lucene.search.FieldCache;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.MultiMapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.servlet.SolrRequestParsers;
import org.apache.solr.update.InvenioDB.BatchOfInvenioIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvenioDoctor extends RequestHandlerBase {

  public static final Logger log = LoggerFactory.getLogger(InvenioDoctor.class);

  RequestQueue queue = new RequestQueue();

  private volatile int counter = 0;
  private boolean asynchronous = true;
  private List<String> workerMessage = new ArrayList<String>();

  private long sleepTime = 300;

  public static String handlerName = "/import";
  private String handlerParams = "commit=false&command=full-import&url=";
	private String deleteHandlerName = "/delete";

	private boolean strictMode;

  
  class RequestData {

    public String url;
    public int count;
    private MultiMapSolrParams params;
    public String handler;

    public RequestData(String handler, String url) throws UnsupportedEncodingException {
      this.url = url;
      this.params = SolrRequestParsers.parseQueryString(this.url);
      this.count = getIds(params.get("url")).size();
      this.handler = handler;
    }
    
    public List<Integer> getIds(String url) {
    	List<Integer> ids = new ArrayList<Integer>();
      if (url == null) return ids;
      
      String[] urlParts = url.split("\\?");
      if (urlParts.length>1) {
        MultiMapSolrParams q = SolrRequestParsers.parseQueryString(urlParts[1]);
        if (q.get("p", null) != null) {
          String s = q.get("p");
          for (String t: s.split(" OR ")) {
            t = t.replace("recid:", "");
            if (t.indexOf("->") > -1) {
              String[] range = t.split("->");
              int start = Integer.parseInt(range[0]);
              int end = Integer.parseInt(range[1]);
              ids.add(start);
              while (start < end) {
              	ids.add(++start);
              }
              //ids = ids + (Integer.parseInt(range[1]) - Integer.parseInt(range[0])) + 1; 
            }
            else {
              //ids++;
            	ids.add(Integer.parseInt(t));
            }
          }
        }
      }
      return ids;
    }

    public SolrParams getReqParams() {
      return this.params;
    }
    
    public String toString() {
      return handler + " (" + count + ") " + url;
    }
    
    /*
     * we cannot use: SolrRequestParsers.parseQueryString(this.url);
     * because it will unencode the params, which is bad for us
     */
    private MultiMapSolrParams parseQueryString(String queryString) 
    {
      Map<String,String[]> map = new HashMap<String, String[]>();
      if( queryString != null && queryString.length() > 0 ) {
        for( String kv : queryString.split( "&" ) ) {
          int idx = kv.indexOf( '=' );
          if( idx > 0 ) {
            String name = kv.substring( 0, idx );
            String value = kv.substring( idx+1 );
            MultiMapSolrParams.addParam( name, value, map );
          }
          else {
            MultiMapSolrParams.addParam( kv, "", map );
          }
        }
      }
      return new MultiMapSolrParams( map );
    }
  }

  class RequestQueue {

    Map<String, RequestData>tbdQueue = Collections.synchronizedMap(new LinkedHashMap<String, RequestData>());
    Set<String> failedIds = Collections.synchronizedSet(new HashSet<String>());
    Map<String, RequestData>failedQueue = Collections.synchronizedMap(new LinkedHashMap<String, RequestData>());
    Integer queuedIn = 0;
    Integer queuedOut = 0;
    
    private volatile boolean stopped;
    private BitSet missingRecs = null;
    private BitSet presentRecs;
    private BitSet toDeleteRecs;

    public RequestData getNext() {
      for (Entry e: tbdQueue.entrySet()) {
        return tbdQueue.get(e.getKey());
      }
      return null;
    }
    public RequestData pop() {
      for (Entry e: tbdQueue.entrySet()) {
        queuedOut++;
        return tbdQueue.remove(e.getKey());
      }
      return null;
    }

    public void registerFailedDoc(String recid) {
      failedIds.add(recid);
    }

    public void registerFailedBatch(String url) throws UnsupportedEncodingException {
      RequestData rd = new RequestData("#failed", url);
      if (!failedQueue.containsKey(rd.url)) {
        failedQueue.put(rd.url, rd);
      }
    }

    public void registerNewBatch(String handler, String url) throws UnsupportedEncodingException {
      RequestData rd = new RequestData(handler, url);
      if (!tbdQueue.containsKey(rd.url)) {
        queuedIn++;
        tbdQueue.put(rd.url, rd);
      }
    }

    public boolean hasMore() {
      return tbdQueue.size() > 0 && stopped==false;
    }

    public void stop() {
      stopped = true;
    }
    
    public void start() {
      stopped = false;
    }

    public void reset() {
      tbdQueue.clear();
      failedQueue.clear();
      failedIds.clear();
      queuedIn = 0;
      queuedOut = 0;
    }
    
    public int countDocs (Map<String, RequestData> theQueue) {
      int i =0;
      for (RequestData rd: theQueue.values()) {
        i += rd.count;
      }
      return i;
    }

    public void setMissing(BitSet bitSet) {
      missingRecs = bitSet;
    }
    
    public BitSet getMissing() {
      return missingRecs;
    }
    
    public void setPresent(BitSet bitSet) {
      presentRecs = bitSet;
    }

    public BitSet getPresent() {
      return presentRecs;
    }
    
    public void reInsert(RequestData rd) {
      if (!tbdQueue.containsKey(rd.url)) {
        queuedIn++;
        tbdQueue.put(rd.url, rd);
      }
    }
		public BitSet getToDelete() {
	    return toDeleteRecs;
    }
		public void setToDelete(BitSet bitSet) {
	    toDeleteRecs = bitSet;
    }

  }


  @SuppressWarnings("rawtypes")
  public void init(NamedList args) {
    super.init(args);
    if (args.get("defaults") == null) {
      return;
    }
    NamedList defs = (NamedList) args.get("defaults");
    if (defs.get("handler") != null) {
      handlerName  = (String) defs.get("handler");
    }
    
    if (defs.get("deleteHandler") != null) {
      deleteHandlerName  = (String) defs.get("deleteHandler");
    }
    
    if (defs.get("tableToQuery") != null) {
      InvenioDB.INSTANCE.setBibRecTableName(((String) defs.get("tableToQuery")));
    }
    
    if (defs.get("handlerParams") != null) {
      handlerParams  = ((String) defs.get("handlerParams"));
    }
    
    if (defs.get("sleepTime") != null) {
      sleepTime  = Long.parseLong((String) defs.get("sleepTime"));
    }
    
    if (defs.get("strict") != null) {
      strictMode  = Boolean.parseBoolean((String) defs.get("strict"));
    }
  }

  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
  throws IOException, InterruptedException, SQLException, ParseException {
    
    SolrParams params = req.getParams();
    String command = params.get("command","info");
    if (command.equals("register-failed-doc")) {
      queue.registerFailedDoc(params.get("recid"));
    }
    else if(command.equals("register-failed-batch")) {
      queue.registerFailedBatch(params.get("url"));
    }
    else if(command.equals("register-new-batch")) {
      queue.registerNewBatch(handlerName, params.get("url"));
    }
    else if(command.equals("stop")) {
      queue.stop();
    }
    else if(command.equals("reset")) {
      queue.reset();
    }
    else if(command.equals("detailed-info")) {
      printDetailedInfo(rsp);
    }
    else if(command.equals("show-missing")) {
      printMissingRecs(rsp);
    }
    else if(command.equals("start")) {
      if (isBusy()) {
        rsp.add("message", "Import is already running...");
        rsp.add("status", "busy");
        printInfo(rsp);
        return;
      }
      queue.start();
      setBusy(true);
      if (isAsynchronous()) {
        runAsynchronously(req);
      }
      else {
        runSynchronously(queue, req);
        setBusy(false);
      }
    }
    else if(command.equals("discover")) {
      queue.registerNewBatch("#discover", params.get("params", ""));
    }
    else if(command.equals("force-reindexing")) {
      queue.registerNewBatch("#discover", "force_reindexing=true");
    }
    else {
      rsp.add("message", "Unknown command: " + command);
      rsp.add("message", "Allowed: start,stop,reset,info,detailed-info");
    }
    
    
    rsp.add("status", isBusy() ? "busy" : "idle");
    printInfo(rsp);

  }

  private void printInfo(SolrQueryResponse rsp) {
    Map<String, String> rows = new LinkedHashMap<String, String>();
    
    rows.put("handlerToCall", handlerName);
    
    rows.put("queueSize", Integer.toString(queue.tbdQueue.size()));
    rows.put("failedRecs", Integer.toString(queue.failedIds.size()));
    rows.put("failedBatches", Integer.toString(queue.failedQueue.size()));
    rows.put("failedTotal", Integer.toString(queue.countDocs(queue.failedQueue) + queue.failedIds.size()));
    
    rows.put("registeredRequests", Integer.toString(queue.queuedIn));
    rows.put("restartedRequests", Integer.toString(queue.queuedOut));
    rows.put("docsToCheck", Integer.toString(queue.countDocs(queue.tbdQueue)));
    
    rsp.add("lastWorkerMessage", getLastWorkerMessage());
    
    rsp.add("info", rows);
  }
  

  private String getLastWorkerMessage() {
    if (workerMessage.size() > 0) return workerMessage.get(0);
    return "<no message yet>";
  }

  private void printMissingRecs(SolrQueryResponse rsp) {
    
    BitSet missing = queue.getMissing();
    BitSet present = queue.getPresent();
    BitSet toDelete = queue.getToDelete();
    
    if (missing == null) {
      rsp.add("message", "We have no data yet, please run command=discover");
      return;
    }
    else {
      rsp.add("message", "These are the records that were not present in the index at the time the doctor was last started. The current index may have already changed by now...");
    }
    
    rsp.add("totalPresent", present.cardinality());
    rsp.add("totalMissing", missing.cardinality());
    rsp.add("totalToDelete", toDelete.cardinality());
    
    ArrayList<Integer> tbd = new ArrayList<Integer>(missing.cardinality());
    rsp.add("missingRecs", tbd);
    
    for (int i = missing.nextSetBit(0); i >= 0; i = missing.nextSetBit(i+1)) {
      tbd.add(i);
    }
    
    ArrayList<Integer> tbdel = new ArrayList<Integer>(toDelete.cardinality());
    rsp.add("toDeleteRecs", tbdel);
    int j = 0;
    for (int i = toDelete.nextSetBit(0); i >= 0; i = toDelete.nextSetBit(i+1)) {
    	tbdel.add(i);
    }
  }
  
  private void printDetailedInfo(SolrQueryResponse rsp) {
    
    
    List<String> tbd = new ArrayList<String>();
    rsp.add("toBeDone", tbd);
    for (RequestData rd: queue.tbdQueue.values()) {
      tbd.add(rd.toString());
    }
    
    
    rsp.add("failedRecs", queue.failedIds);
    
    List<String> fb = new ArrayList<String>();
    rsp.add("failedBatches", fb);
    for (RequestData rd: queue.failedQueue.values()) {
      fb.add(rd.toString());
    }
    
    rsp.add("allMessages", getWorkerMessage());
  }


  private void runAsynchronously(SolrQueryRequest req) {

    final SolrQueryRequest request = req;

    new Thread(new Runnable() {

      public void run() {
        setWorkerMessage("I am idle");
        try {
          while (queue.hasMore()) {
            setWorkerMessage("Running in the background... (" + queue.getNext() + ")");
            runSynchronously(queue, request);
          }
        } catch (IOException e) {
        	if (strictMode) {
        		throw new SolrException(ErrorCode.SERVER_ERROR, e);
        	}
          setWorkerMessage("Worker error..." + e.getMessage());
          log.error(e.getMessage());
          log.error(e.getStackTrace().toString());
        } catch (InterruptedException e) {
          setWorkerMessage("Interrupted..." + e.getMessage());
          log.error(e.getMessage());
          log.error(e.getStackTrace().toString());
        } catch (SQLException e) {
          if (strictMode) {
            throw new SolrException(ErrorCode.SERVER_ERROR, e);
          }
          setWorkerMessage("Worker error..." + e.getMessage());
          log.error(e.getMessage());
          log.error(e.getStackTrace().toString());
        } catch (ParseException e) {
          if (strictMode) {
            throw new SolrException(ErrorCode.SERVER_ERROR, e);
          }
          setWorkerMessage("Worker error..." + e.getMessage());
          log.error(e.getMessage());
          log.error(e.getStackTrace().toString());
        } finally {
          setBusy(false);
          request.close();
        }
      }
    }).start();
  }

  public void setAsynchronous(boolean val) {
    asynchronous = val;
  }

  public boolean isAsynchronous() {
    return asynchronous;
  }

  private void setBusy(boolean b) {
    if (b == true) {
      counter++;
    } else {
      counter--;
    }
  }

  public boolean isBusy() {
    if (counter < 0) {
      throw new IllegalStateException(
      "Huh, 2+2 is not 4?! Should never happen.");
    }
    return counter > 0;
  }

  public void setWorkerMessage(String msg) {
  	synchronized (workerMessage) {
  		workerMessage.add(0, msg);
    }
  }

  public List<String> getWorkerMessage() {
    if (workerMessage.size() > 100) {
    	synchronized (workerMessage) {
    		for (int i=100; i<workerMessage.size();i++) {
      		workerMessage.remove(i);
      	}
      }
    };
    return workerMessage;
  }

  /*
   * The main call
   */
  private void runSynchronously(RequestQueue queue, SolrQueryRequest req) throws InterruptedException, IOException, SQLException, ParseException {

    SolrCore core = req.getCore();

    RequestData data = queue.pop();
    
    SolrParams params = data.getReqParams();
    
    LocalSolrQueryRequest locReq = new LocalSolrQueryRequest(req.getCore(), params);
    
    long startTime = System.currentTimeMillis();
    
    try {
	    if (data.handler.equals("#discover")) {
	    	runDiscovery(locReq);
	    	return;
	    }
	    else if (data.handler.equals("#index-discovered")) {
	    	runIndexingOfDiscovered(locReq);
	    	return;
	    }
	    else if (data.handler.equals("#delete")) {
	    	runDeleteRecords(locReq, data);
	    	return;
	    }
	    
	    
	    SolrRequestHandler handler = req.getCore().getRequestHandler(data.handler);
	    setWorkerMessage("Executing :" + data.handler + " with params: " + locReq.getParamString());
	    
	    SolrQueryResponse rsp;
	    
	    boolean repeat = false;
	    int maxRepeat = 10;
	    do {
	    	
	      rsp = new SolrQueryResponse();	      
	      core.execute(handler, locReq, rsp);
	      String is = (String) rsp.getValues().get("status");
	      
	      if (is == null) {
	      	setWorkerMessage("Executed :" + data.handler + " result: " + rsp.getValues().toString());
	      	break; // some unknown handler
	      }
	      
	      if (is.equals("busy")) {
	        repeat = true;
	        try {
	          Thread.sleep(sleepTime );
	        } catch (InterruptedException e) {
	          queue.reInsert(data);
	          throw e;
	        } 
	        setWorkerMessage("Waiting for handler to be idle: " + data.handler);
	      }
	      else {
	        repeat = false;
	      }
	      
	      setWorkerMessage("Executed :" + data.handler + " result: " + rsp.getValues().toString());
	      
	      if (maxRepeat-- < 0) {
	        setWorkerMessage("Batch failed, worker is too busy, max waiting time exhausted");
	        try {
	          queue.registerFailedBatch(req.getParamString());
	        } catch (UnsupportedEncodingException e) {
	          log.error(e.getMessage());
	        }
	        repeat = false;
	      }
	      else {
		      locReq.close();
		      locReq = new LocalSolrQueryRequest(req.getCore(), params);
	      }
	      
	    } while (repeat);
    }
    finally {
    	locReq.close();
    	setWorkerMessage("Execution time: " + (System.currentTimeMillis() - startTime) + " ms.");
    }
    
  }
  
  // XXX: to remove, it should be done via xml messages to the appropriate handle
  // but i am tired and lazy now
  private void runDeleteRecords(LocalSolrQueryRequest locReq, RequestData data) 
  		throws IOException {
		UpdateHandler updateHandler = locReq.getCore().getUpdateHandler();
		DeleteUpdateCommand delCmd = new DeleteUpdateCommand(locReq);
		
		List<Integer> recids = data.getIds(data.params.get("url"));
	  if (recids.size() > 0) {
			for (int i : recids) {
			  delCmd.clear();
				delCmd.id = Integer.toString(i);
				updateHandler.delete(delCmd);
			}
		}
  }

	private void runDiscovery(SolrQueryRequest req) throws IOException, SQLException, ParseException {
    SolrParams params = req.getParams();
    if (params.get("last_recid", null) == null || params.getInt("last_recid", 0) == -1) {
      queue.setPresent(new BitSet());
      queue.setMissing(new BitSet());
      queue.setToDelete(new BitSet());
      setWorkerMessage("Resetting list of missing records (new search will be done)");
    }
    
    BitSet[] data = discoverMissingRecords(queue.getPresent(), queue.getMissing(), 
    		queue.getToDelete(), req);
    queue.setPresent(data[0]);
    queue.setMissing(data[1]);
    queue.setToDelete(data[2]);
  }
  
  private void runIndexingOfDiscovered(SolrQueryRequest req) throws UnsupportedEncodingException {
  	ModifiableSolrParams rParam = new ModifiableSolrParams(SolrRequestParsers.parseQueryString(handlerParams));
  	if (queue.getMissing().cardinality() > 0) {
  		registerReindexingOfRecords(handlerName, req, rParam, queue.getMissing());
  	}
  	
    if (queue.getToDelete().cardinality() > 0) {
    	registerReindexingOfRecords("#delete" , req, rParam, queue.getToDelete());
    }
    queue.registerNewBatch("/update", "commit=true");
  }

  private Map<Integer, Map<Integer, Integer>> tmpMap = new HashMap<Integer, Map<Integer,Integer>>();
  private BitSet[] discoverMissingRecords(BitSet present, BitSet missing, BitSet toDelete, 
  		SolrQueryRequest req) throws IOException, SQLException, ParseException {
    
    SolrParams params = req.getParams();
    String field = params.get("field", "recid");
    Integer lastRecid = params.getInt("last_recid", -1);
    String modDate = params.get("mod_date", null);
    Boolean forceReindexing = params.getBool("force_reindexing", false);
    Integer fetchSize = Math.min(params.getInt("fetch_size", 100000), 100000);
    // setting maxRecs to very large value means the worker cannot be stopped in time
    int maxRecs = Math.min(params.getInt("max_records", 100000), 1000000);
    
    
    int[] existingRecs = FieldCache.DEFAULT.getInts(req.getSearcher().getAtomicReader(), field, false);
    Map<Integer, Integer> idToLuceneId;
    
    if (tmpMap.containsKey(existingRecs.hashCode())) {
    	idToLuceneId = tmpMap.get(existingRecs.hashCode());
    }
    else {
    	tmpMap.clear();
    	idToLuceneId = new HashMap<Integer, Integer>(existingRecs.length);
    	for (int i=0;i<existingRecs.length;i++) {
        idToLuceneId.put(existingRecs[i], i);
      }
    }
    
    
    if (present == null) present = new BitSet(existingRecs.length);
    if (missing == null) missing = new BitSet(existingRecs.length);
    
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
      
      Object[] data = new List[]{results.added, results.updated};
      
      for (Object o:  data) {
        @SuppressWarnings("unchecked")
        List<Integer> coll = (List<Integer>) o;
        doneSoFar += coll.size();
        for (int x: coll) {
          if (!forceReindexing && idToLuceneId.containsKey(x)) {
            present.set(x);
          }
          else {
            missing.set(x);
          }
        }
      }
      
      doneSoFar += results.deleted.size();
      
      for (int x: results.deleted) {
      	if (idToLuceneId.containsKey(x)) {
      		toDelete.set(x);
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
      mp.remove("discover");
      queue.registerNewBatch("#discover", mp.toString());
    }
    else {
      queue.registerNewBatch("#index-discovered", "index-discovered");
    }
    return new BitSet[]{present, missing, toDelete};
    
  }
  
  private void registerReindexingOfRecords(String handler, SolrQueryRequest req, 
  		ModifiableSolrParams rParam, BitSet records) 
  		throws UnsupportedEncodingException {
    
    int[] ids = new int[records.cardinality()];
    int j = 0;
    for (int i = records.nextSetBit(0); i >= 0; i = records.nextSetBit(i+1)) {
      ids[j++] = i;
    }
    
    // for security reason, only certain params can be supplied by user
    SolrParams params = req.getParams();
    int batchSize = Math.max(Math.min(params.getInt("batchSize", 200), 2000), 1);
    int maxRecords = Math.max(Math.min(params.getInt("maxRecords", 10000), 100000), 1000);
    
    List<String> queryParts = InvenioKeepRecidUpdated.getQueryIds(batchSize, ids);
    for (String queryPart : queryParts) {
      String url = InvenioKeepRecidUpdated.getInternalURL("python://search", queryPart, maxRecords);
      rParam.set("url", url);
      queue.registerNewBatch(handler, rParam.toString());
    }
    
    // invoke handler to save the last modified recid
    if (ids.length > 0)
      queue.registerNewBatch("/invenio/update", "last_recid="+ids[ids.length-1]+"&batchsize=1");
  }




  public String getVersion() {
    return "";
  }

  public String getDescription() {
    return "Registers the failed imports and calls them again";
  }

  public String getSourceId() {
    return "";
  }

  public String getSource() {
    return "";
  }

}
