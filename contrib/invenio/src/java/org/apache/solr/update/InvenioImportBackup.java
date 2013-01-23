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
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import monty.solr.jni.MontySolrVM;
import monty.solr.jni.PythonCall;
import monty.solr.jni.PythonMessage;

import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldCacheDocIdSet;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.MultiMapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.NamedList.NamedListEntry;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.handler.dataimport.WaitingDataImportHandler;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.servlet.SolrRequestParsers;
import org.apache.solr.update.InvenioImportBackup.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;

public class InvenioImportBackup extends RequestHandlerBase implements PythonCall {

  public static final Logger log = LoggerFactory.getLogger(InvenioImportBackup.class);

  RequestQueue queue = new RequestQueue();

  private volatile int counter = 0;
  private boolean asynchronous = true;
  private volatile List<String> workerMessage = new ArrayList<String>();
  private volatile String tokenMessage = "";

  private long sleepTime = 300;

  public static String handlerName = "/import";
  private String pythonFunctionName = "get_recids_changes";
  private String handlerParams = "commit=false&command=full-import&url=";
  
  class RequestData {

    public String url;
    public int count;
    private MultiMapSolrParams params;

    public RequestData(String url) throws UnsupportedEncodingException {
      this.url = url;
      this.params = SolrRequestParsers.parseQueryString(this.url);
      this.count = countIds(params.get("url"));
    }
    
    private int countIds(String url) {
      int count = 0;
      if (url == null) return count;
      
      String[] urlParts = url.split("\\?");
      if (urlParts.length>1) {
        MultiMapSolrParams q = SolrRequestParsers.parseQueryString(urlParts[1]);
        if (q.get("p", null) != null) {
          String s = q.get("p");
          for (String t: s.split(" OR ")) {
            t = t.replace("recid:", "");
            if (t.indexOf("->") > -1) {
              String[] range = t.split("->");
              count = count + (Integer.parseInt(range[1]) - Integer.parseInt(range[0])) + 1; 
            }
            else {
              count++;
            }
          }
        }
      }
      return count;
    }

    public SolrParams getReqParams() {
      return this.params;
    }
    
    public String toString() {
      return "(" + count + ") " + url;
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
      RequestData rd = new RequestData(url);
      if (!failedQueue.containsKey(rd.url)) {
        failedQueue.put(rd.url, rd);
      }
    }

    public void registerNewBatch(String url) throws UnsupportedEncodingException {
      RequestData rd = new RequestData(url);
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
    
    if (defs.get("pythonFunctionName") != null) {
      setPythonFunctionName((String) defs.get("pythonFunctionName"));
    }
    
    if (defs.get("handlerParams") != null) {
      handlerParams  = ((String) defs.get("handlerParams"));
    }
    
    if (defs.get("sleepTime") != null) {
      sleepTime  = Long.parseLong((String) defs.get("sleepTime"));
    }
  }

  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
  throws IOException, InterruptedException {
    
    SolrParams params = req.getParams();
    String command = params.get("command","info");
    if (command.equals("register-failed-doc")) {
      queue.registerFailedDoc(params.get("recid"));
    }
    else if(command.equals("register-failed-batch")) {
      queue.registerFailedBatch(params.get("url"));
    }
    else if(command.equals("register-new-batch")) {
      queue.registerNewBatch(params.get("url"));
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
      workerMessage.clear();
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
      queue.registerNewBatch("discover=1&"+params.get("params", ""));
    }
    else {
      rsp.add("message", "Unknown command: " + command);
      rsp.add("message", "Allowed: start,stop,reset,info,detailed-info");
    }
    
    
    rsp.add("status", isBusy() ? "busy" : "idle");
    printInfo(rsp);
    //setToken("");

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
    
    if (missing == null) {
      rsp.add("message", "We have no data yet, please run command=discover");
      return;
    }
    else {
      rsp.add("message", "These are the records that were not present in the index at the time the doctor was last started. The current index may have already changed by now...");
    }
    
    rsp.add("totalPresent", present.cardinality());
    rsp.add("totalMissing", missing.cardinality());
    
    ArrayList<Integer> tbd = new ArrayList<Integer>(missing.cardinality());
    rsp.add("missingRecs", tbd);
    
    int j = 0;
    for (int i = missing.nextSetBit(0); i >= 0; i = missing.nextSetBit(i+1)) {
      tbd.add(i);
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

  private void setToken(String string) {
    tokenMessage = string;
  }

  private String getToken() {
    return tokenMessage;
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
          setWorkerMessage("Worker error..." + e.getLocalizedMessage());
          log.error(e.getLocalizedMessage());
          log.error(e.getStackTrace().toString());
        } catch (InterruptedException e) {
          setWorkerMessage("Worker error..." + e.getLocalizedMessage());
          log.error(e.getLocalizedMessage());
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
    workerMessage.add(0, msg);
  }

  public String getWorkerMessage() {
    StringBuilder out = new StringBuilder();
    for (String msg: workerMessage) {
      out.append(msg);
      out.append("\n");
    }
    workerMessage.clear();
    return out.toString();
  }

  /*
   * The main call
   */
  private void runSynchronously(RequestQueue queue, SolrQueryRequest req) throws InterruptedException, IOException {

    SolrCore core = req.getCore();

    SolrRequestHandler handler = req.getCore().getRequestHandler(handlerName);
    RequestData data = queue.pop();
    SolrParams params = data.getReqParams();
    
    LocalSolrQueryRequest locReq = new LocalSolrQueryRequest(req.getCore(), params);
    
    if (data.url.substring(0, 8).equals("discover")) {
      try {
        runDiscoveryReindexing(locReq);
      } catch (IOException e) {
        throw e;
      } finally {
        locReq.close();
      }
      return;
    }
    
    
    setWorkerMessage("Executing :" + handlerName + " with params: " + locReq.getParamString());
    
    SolrQueryResponse rsp;
    locReq.close();
    
    boolean repeat = false;
    int maxRepeat = 10;
    do {
      locReq = new LocalSolrQueryRequest(req.getCore(), params);
      rsp = new SolrQueryResponse();
      
      core.execute(handler, locReq, rsp);
      String is = (String) rsp.getValues().get("status");
      
      if (is.equals("busy")) {
        repeat = true;
        try {
          Thread.sleep(sleepTime );
        } catch (InterruptedException e) {
          queue.reInsert(data);
          throw e;
        } finally {
          locReq.close();
        }
        setWorkerMessage("Waiting for handler to be idle: " + handlerName);
      }
      else {
        repeat = false;
      }
      
      setWorkerMessage("Executed :" + handlerName + " result: " + rsp.getValues().toString());
      locReq.close();
      
      if (maxRepeat-- < 0) {
        setWorkerMessage("Batch failed, worker is too busy, max waiting time exhausted");
        try {
          queue.registerFailedBatch(req.getParamString());
        } catch (UnsupportedEncodingException e) {
          log.error(e.getMessage());
        }
        repeat = false;
      }
    } while (repeat);
    
  }
  
  private void runDiscoveryReindexing(SolrQueryRequest req) throws IOException {
    
    SolrParams params = req.getParams();
    if (params.get("last_recid", null) == null || params.getInt("last_recid", 0) == -1) {
      queue.setMissing(new BitSet());
      queue.setMissing(new BitSet());
      setWorkerMessage("Resetting list of missing records (new search will be done)");
    }
    
    BitSet[] data = discoverMissingRecords(queue.getPresent(), queue.getMissing(), req);
    queue.setPresent(data[0]);
    queue.setMissing(data[1]);
    registerReindexingOfMissed(req, data[1]);
  }

  private BitSet[] discoverMissingRecords(BitSet present, BitSet missing, SolrQueryRequest req) throws IOException {
    // get recids from Invenio {'ADDED': int, 'UPDATED': int, 'DELETED':
    // int }
    SolrQueryResponse rsp = new SolrQueryResponse();
    
    HashMap<String, int[]> dictData = null;
    
    SolrParams params = req.getParams();
    String field = params.get("field", "recid");
    Integer lastRecid = params.getInt("last_recid", -1);
    Integer fetchSize = Math.min(params.getInt("fetch_size", 100000), 100000);
    // setting maxRecs to very large value means the worker cannot be stopped in time
    int maxRecs = Math.min(params.getInt("max_records", 100000), 1000000);
    
    int[] existingRecs = FieldCache.DEFAULT.getInts(req.getSearcher().getAtomicReader(), field, false);
    HashMap<Integer, Integer> idToLuceneId = new HashMap<Integer, Integer>(existingRecs.length);
    for (int i=0;i<existingRecs.length;i++) {
      idToLuceneId.put(existingRecs[i], i);
    }
    
    if (present == null) present = new BitSet(existingRecs.length);
    if (missing == null) missing = new BitSet(existingRecs.length);
    
    int doneSoFar = 0;
    
    boolean finished = false;
    
    while (doneSoFar<maxRecs) {
      PythonMessage message = MontySolrVM.INSTANCE
        .createMessage(pythonFunctionName)
        .setSender("InvenioKeepRecidUpdated")
        .setParam("max_records", fetchSize)
        .setParam("request", req)
        .setParam("response", rsp)
        .setParam("last_recid", lastRecid);
    
      MontySolrVM.INSTANCE.sendMessage(message);

      Object results = message.getResults();
      if (results == null) {
        finished = true;
        break;
      }
      dictData = (HashMap<String, int[]>) results;
      
      for (String name: new String[]{"ADDED", "UPDATED"}) {
        int[] coll = dictData.get(name);
        doneSoFar += coll.length;
        for (int x: coll) {
          if (idToLuceneId.containsKey(x)) {
            present.set(x);
          }
          else {
            missing.set(x);
          }
        }
      }
      
      int[] deleted = dictData.get("DELETED"); //TODO
      doneSoFar += deleted.length;
      lastRecid = (Integer) message.getParam("last_recid");
      
      log.info("Checking database; last_recid={}; found={}", lastRecid, doneSoFar);
    }
    
    if (!finished) {
      ModifiableSolrParams mp = new ModifiableSolrParams(params);
      mp.set("last_recid", lastRecid);
      mp.remove("discover");
      queue.registerNewBatch("discover=1&"+mp.toString());
    }
    return new BitSet[]{present, missing};
    
  }
  
  private void registerReindexingOfMissed(SolrQueryRequest req, BitSet missing) throws UnsupportedEncodingException {
    
    int[] ids = new int[missing.cardinality()];
    int j = 0;
    for (int i = missing.nextSetBit(0); i >= 0; i = missing.nextSetBit(i+1)) {
      ids[j++] = i;
    }
    
    ModifiableSolrParams rParam = new ModifiableSolrParams(SolrRequestParsers.parseQueryString(handlerParams));
    
    // for security reason, only certain params can be supplied by user
    SolrParams params = req.getParams();
    int batchSize = Math.max(Math.min(params.getInt("batchSize", 200), 200), 1);
    int maxRecords = Math.max(Math.min(params.getInt("maxRecords", 10000), 10000), 1000);
    
    List<String> queryParts = InvenioKeepRecidUpdated.getQueryIds(batchSize, ids);
    for (String queryPart : queryParts) {
      String url = InvenioKeepRecidUpdated.getInternalURL("python://search", queryPart, maxRecords);
      rParam.set("url", url);
      queue.registerNewBatch(rParam.toString());
    }
    
  }

  public SolrQueryRequest req(SolrQueryRequest req, String ... q) {
    if (q.length%2 != 0) { 
      throw new RuntimeException("The length of the string array (query arguments) needs to be even");
    }
    Map.Entry<String, String> [] entries = new NamedListEntry[q.length / 2];
    for (int i = 0; i < q.length; i += 2) {
      entries[i/2] = new NamedListEntry<String>(q[i], q[i+1]);
    }
    return new LocalSolrQueryRequest(req.getCore(), new NamedList(entries));
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

  public void setPythonFunctionName(String name) {
    pythonFunctionName = name;
  }

  public String getPythonFunctionName() {
    return pythonFunctionName;
  }

}
