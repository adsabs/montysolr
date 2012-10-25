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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.NamedList.NamedListEntry;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.servlet.SolrRequestParsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;

public class InvenioImportBackup extends RequestHandlerBase {

  public static final Logger log = LoggerFactory.getLogger(InvenioImportBackup.class);

  RequestQueue queue = new RequestQueue();

  private volatile int counter = 0;
  private boolean asynchronous = true;
  private volatile String workerMessage = "";
  private volatile String tokenMessage = "";

  public static String handlerName = "/import";

  class RequestData {

    public String url;
    public int count;

    public RequestData(String url) {
      this.url = url; // the URL is UTF-8 encoded
      this.count = countIds(url);
    }
    
    private int countIds(String url) {
      int count = 0;
      
      for (String arg: url.split("&")) {
      
        String[] kv = arg.split("=");
        if (!kv[0].equals("url")) continue;
        
        String invenioUrl;
        try {
          invenioUrl = URLDecoder.decode( kv[1], "UTF-8" );
        } catch (UnsupportedEncodingException e) {
          throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, e);
        }
        
        String[] urlParts = invenioUrl.split("\\?");
        for (String param : urlParts[1].split("&")) {
          String[] pair = param.split("=");
          if (pair[0].equals("p")) {
            for (String s: pair[1].split(" OR ")) {
              System.out.println(s);
              s = s.replace("recid:", "");
              if (s.indexOf("->") > -1) {
                String[] range = s.split("->");
                count = count + (Integer.parseInt(range[1]) - Integer.parseInt(range[0])); 
              }
              else {
                count++;
              }
            }
          }
        }     
      }
      return count;
    }

    public SolrParams getReqParams() {
      return SolrRequestParsers.parseQueryString(this.url);
    }
    
    public String toString() {
      return "(" + count + ") " + url;
    }
  }

  class RequestQueue {

    Map<String, RequestData>tbdQueue = Collections.synchronizedMap(new LinkedHashMap<String, RequestData>());
    Set<String> failedIds = Collections.synchronizedSet(new HashSet<String>());
    Map<String, RequestData>failedQueue = Collections.synchronizedMap(new LinkedHashMap<String, RequestData>());
    Integer queuedIn = 0;
    Integer queuedOut = 0;
    
    private volatile boolean stopped;

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

    public void registerFailedBatch(String url) {
      if (!failedQueue.containsKey(url)) {
        failedQueue.put(url, new RequestData(url));
      }
    }

    public void registerNewBatch(String url) {
      if (!tbdQueue.containsKey(url)) {
        queuedIn++;
        tbdQueue.put(url, new RequestData(url));
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
  }

  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
  throws IOException, InterruptedException {
    
    SolrParams params = req.getParams();
    String command = params.get("command");
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
    else if(command.equals("info")) {
      printInfo(rsp);
    }
    else if(command.equals("detailed-info")) {
      printDetailedInfo(rsp);
    }
    else if(command.equals("start")) {
      if (isBusy()) {
        rsp.add("message", "Import is already running...");
        rsp.add("status", "busy");
        return;
      }
      setBusy(true);
      if (isAsynchronous()) {
        runAsynchronously(req);
      }
      else {
        runSynchronously(queue, req);
        setBusy(false);
      }
    }
    else {
      rsp.add("message", "Unknown command: " + command);
      rsp.add("message", "Allowed: start,stop,reset,info,detailed-info");
    }
    

    rsp.add("status", isBusy() ? "busy" : "idle");
    //setToken("");

  }

  private void printInfo(SolrQueryResponse rsp) {
    Map<String, String> rows = new LinkedHashMap<String, String>();
    
    rows.put("queueSize", Integer.toString(queue.tbdQueue.size()));
    rows.put("failedRecs", Integer.toString(queue.failedIds.size()));
    rows.put("failedBatches", Integer.toString(queue.failedQueue.size()));
    rows.put("failedTotal", Integer.toString(queue.countDocs(queue.failedQueue) + queue.failedIds.size()));
    
    rows.put("registeredRequests", Integer.toString(queue.queuedIn));
    rows.put("restartedRequests", Integer.toString(queue.queuedOut));
    rows.put("docsToCheck", Integer.toString(queue.countDocs(queue.tbdQueue)));
    
    if (isBusy()) {
      rsp.add("workerMessage", getWorkerMessage());
    }
    
    rsp.add("info", rows);
  }
  

  private void printDetailedInfo(SolrQueryResponse rsp) {
    printInfo(rsp);
    
    
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
            setWorkerMessage("Running in the background...");
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
    log.info(msg);
    workerMessage = msg;
  }

  public String getWorkerMessage() {
    return workerMessage;
  }

  /*
   * The main call
   */
  private void runSynchronously(RequestQueue queue, SolrQueryRequest req)
  throws MalformedURLException, IOException, InterruptedException {

    SolrCore core = req.getCore();

    SolrRequestHandler handler = req.getCore().getRequestHandler(handlerName);
    SolrQueryResponse rsp = new SolrQueryResponse();
    
    
    RequestData data = queue.pop();
    
    LocalSolrQueryRequest locReq = new LocalSolrQueryRequest(req.getCore(), data.getReqParams());
    
    core.execute(handler, locReq, rsp);

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

}
