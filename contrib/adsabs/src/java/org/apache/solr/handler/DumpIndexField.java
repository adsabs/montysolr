package org.apache.solr.handler;

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
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.NamedList.NamedListEntry;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.SolrIndexSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;

public class DumpIndexField extends RequestHandlerBase {

  public static final Logger log = LoggerFactory.getLogger(DumpIndexField.class);

  RequestQueue queue = new RequestQueue();

  private volatile int counter = 0;
  private boolean asynchronous = true;
  private volatile String workerMessage = "";
  private volatile String tokenMessage = "";

  private Pattern allowed;



  class RequestData {
    private SolrParams params;
    public String sourceField;
    public String targetField;
    public String msg = "";

    public RequestData(SolrParams params) {
      this.params = params;
      sourceField = params.get("sourceField");
      targetField = params.get("targetField");
    }
    public SolrParams getReqParams() {
      return this.params;
    }
    public String toString() {
      return "from:" + sourceField + "->" + targetField;
    }
    public void msg(String string) {
      this.msg += string + " ";
    }
  }

  class RequestQueue {

    Map<String, RequestData>tbdQueue = Collections.synchronizedMap(new LinkedHashMap<String, RequestData>());
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
      queuedIn = 0;
      queuedOut = 0;
    }
    

    public void registerNewRequest(SolrParams params) {
      RequestData rd = new RequestData(params);
      if (!tbdQueue.containsKey(rd.toString())) {
        queuedIn++;
        tbdQueue.put(rd.toString(), rd);
      }
    }

    public void registerFailedBatch(RequestData rd) {
      if (!failedQueue.containsKey(rd.toString())) {
        failedQueue.put(rd.toString(), rd);
      }
    }
    
    public int docsToGo() {
      // TODO Auto-generated method stub
      return 0;
    }
    
  }


  @SuppressWarnings("rawtypes")
  public void init(NamedList args) {
    super.init(args);
    if (args.get("defaults") == null) {
      return;
    }
    NamedList defs = (NamedList) args.get("defaults");
    allowed  = Pattern.compile(".*");
    
    if (defs.get("allowed") != null) {
      allowed  = Pattern.compile((String)defs.get("allowed"));    
    }
    
  }

  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
  throws IOException, InterruptedException {
    
    SolrParams params = req.getParams();
    String command = params.get("command","info");
    
    if (command.equals("dump")) {
      queue.registerNewRequest(params);
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
    else if(command.equals("detailedInfo")) {
      printDetailedInfo(rsp);
    }
    else if(command.equals("start")) {
      if (isBusy()) {
        rsp.add("message", "Operation is already running...");
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
      printInfo(rsp);
    }
    
    
    rsp.add("status", isBusy() ? "busy" : "idle");
    //setToken("");

  }

  private void printInfo(SolrQueryResponse rsp) {
    Map<String, String> rows = new LinkedHashMap<String, String>();
    
    rows.put("queueSize", Integer.toString(queue.tbdQueue.size()));
    
    rows.put("docsToGo", Integer.toString(queue.docsToGo()));
    
    rsp.add("lastWorkerMessage", getWorkerMessage());
    
    rsp.add("info", rows);
  }
  

  private void printDetailedInfo(SolrQueryResponse rsp) {
    printInfo(rsp);
    
    
    List<String> tbd = new ArrayList<String>();
    rsp.add("toBeDone", tbd);
    for (RequestData rd: queue.tbdQueue.values()) {
      tbd.add(rd.toString());
    }
    
    List<String> fb = new ArrayList<String>();
    rsp.add("failedBatches", fb);
    for (RequestData rd: queue.failedQueue.values()) {
      tbd.add(rd.toString() + "(" + rd.msg + ")");
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
    IndexSchema schema = req.getSchema();
    
    RequestData data = queue.pop();
    
    if (!allowed.matcher(data.sourceField).matches()) {
      data.msg("Export of this field is not allowed: " + data.sourceField);
      queue.registerFailedBatch(data);
      return;
    }
    SchemaField field = core.getLatestSchema().getFieldOrNull(data.sourceField);
    
    if (field==null || !field.stored()) {
      data.msg("We cannot dump fields that are not stored: " + data.sourceField);
      queue.registerFailedBatch(data);
      return;
    }
    
    final Analyzer analyzer = core.getLatestSchema().getQueryAnalyzer();
    
    SchemaField targetField = core.getLatestSchema().getFieldOrNull(data.targetField);
    
    if (targetField == null) {
      data.msg("We cannot find analyzer for: " + data.targetField);
      queue.registerFailedBatch(data);
      return;
    }
    
    final String targetAnalyzer = data.targetField;
    
    DirectoryReader ir = req.getSearcher().getIndexReader();
    SolrIndexSearcher se = req.getSearcher();
    
    final HashSet<String> fieldsToLoad = new HashSet<String>();
    fieldsToLoad.add(data.sourceField);
    
    se.search(new MatchAllDocsQuery(), new Collector() {
      private AtomicReader reader;
      private int i = 0;
      
      @Override
      public boolean acceptsDocsOutOfOrder() {
        return true;
      }

      @Override
      public void collect(int i) {
        Document d;
        try {
          d = reader.document(i, fieldsToLoad);
          for (String f: fieldsToLoad) {
            String[] vals = d.getValues(f);
            for (String s: vals) {
              TokenStream ts = analyzer.tokenStream(targetAnalyzer, new StringReader(s));
              ts.reset();
              while (ts.incrementToken()) {
                //pass
              }
            }
          }
        } catch (IOException e) {
          // pass
        }
      }
      @Override
      public void setNextReader(AtomicReaderContext context) {
        this.reader = context.reader();
      }
      @Override
      public void setScorer(org.apache.lucene.search.Scorer scorer) {
        // Do Nothing
      }
    });
    
    // persist the data
    TokenStream ts = analyzer.tokenStream(data.targetField, new StringReader("xxx"));
    ts.reset();
    ts.reset();
    ts.reset();
    
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
