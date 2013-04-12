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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;
import org.apache.noggit.JSONUtil;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.MultiMapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.RawResponseWriter;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.QueryParsing;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.servlet.SolrRequestParsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;

/*
 * This is a handler to execute batch-jobs, it provides certain (internal)
 * commands, but I plan to make it extendible. Ie. to execute providers 
 * of commands.
 * 
 * The handler must be registered at certain url and invoked through a
 * sequence of commands:
 *   1. command=<your-command>&<param1>=<param1-value>....
 *   2. command=start
 *       
 *      executor starts running, you can inquire about its status
 *      using command=info or do "command=stop"
 *      
 *   3. command=<your-other-command> -- for example to retrieve data
 *      provided by result of command (1)
 *      
 */
public class BatchHandler extends RequestHandlerBase {

  public static final Logger log = LoggerFactory.getLogger(BatchHandler.class);

  RequestQueue queue;
  private volatile int counter;
  private boolean asynchronous;
  private volatile List<String> workerMessage;
  private long sleepTime;
  private Map<String, Provider> providers;
	private Thread thread;
	private Path tmpDir;
  
  public BatchHandler() {
  	queue = new RequestQueue();
  	workerMessage = new ArrayList<String>();
  	providers = new HashMap<String, Provider>();
  	counter = 0;
  	asynchronous = true;
  	sleepTime = 300;
  	populateDefaultProviders();
  }
  
  class Provider {
  	public String name;
  	public String solrHandler;
  	
  	public Provider(String name) {
  		this.name = name;
  	}
  	
  	public Provider(String name, String solrHandler) {
  		this.name = name;
  		this.solrHandler = solrHandler;
  	}

		public void run(SolrQueryRequest locReq) throws Exception {
	    // TODO Auto-generated method stub
    }
  }
  
  class RequestData {

    public String url;
    public int count;
    private SolrParams params;
    public Provider handler;

    public RequestData(Provider handler, SolrParams params) {
      this.url = params.toString();
      this.params = params;
      this.handler = handler;
    }
    
    public RequestData(Provider handler, String url) throws UnsupportedEncodingException {
      this.url = url;
      this.params = SolrRequestParsers.parseQueryString(this.url);
      this.handler = handler;
    }
    
    public SolrParams getReqParams() {
      return this.params;
    }
    
    public String toString() {
      return handler + "::" + url;
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

		public Provider getProvider() {
	    return handler;
    }
  }

  class RequestQueue {

    Map<String, RequestData>tbdQueue = Collections.synchronizedMap(new LinkedHashMap<String, RequestData>());
    Map<String, RequestData>failedQueue = Collections.synchronizedMap(new LinkedHashMap<String, RequestData>());
    Integer queuedIn = 0;
    Integer queuedOut = 0;
    
    private volatile boolean stopped;

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

    public void registerFailedBatch(RequestData data) {
      if (!failedQueue.containsKey(data.url)) {
      	RequestData rd = new RequestData(providers.get("#failed"), data.getReqParams());
        failedQueue.put(rd.url, rd);
      }
    }

    public void registerNewBatch(Provider handler, SolrParams params)  {
      RequestData rd = new RequestData(handler, params);
      if (!tbdQueue.containsKey(rd.url)) {
        queuedIn++;
        tbdQueue.put(rd.url, rd);
      }
    }
    
    public void registerNewBatchx(Provider handler, String url) throws UnsupportedEncodingException {
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
      queuedIn = 0;
      queuedOut = 0;
    }
    

    public void reInsert(RequestData rd) {
      if (!tbdQueue.containsKey(rd.url)) {
        queuedIn++;
        tbdQueue.put(rd.url, rd);
      }
    }
    
    public boolean isStopped() {
    	return stopped;
    }
    
  }


  @SuppressWarnings("rawtypes")
  public void init(NamedList args) {
    super.init(args);
    
    // TODO: this sucks and files will stall there
    try {
	    tmpDir = Files.createTempDirectory("montysolr-batch-handler", 
	    		PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr-----")));
    } catch (IOException e) {
    	// pass
    }
    
    
    if (args.get("defaults") == null) {
      return;
    }
    NamedList defs = (NamedList) args.get("defaults");
    
    if (defs.get("providers") != null) {
      //TODO
    }
    
    if (defs.get("synchronous") != null) {
      asynchronous = ((String) defs.get("synchronous")).toLowerCase().equals("true") ? false : true;
    }
    
    if (defs.get("sleepTime") != null) {
      sleepTime  = Long.parseLong((String) defs.get("sleepTime"));
    }
  }

  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
  throws IOException, InterruptedException {
    
    SolrParams params = req.getParams();
    String command = params.get("command","info");
    
    
    if (command.equals("stop")) {
      queue.stop();
      new Thread(new Runnable() {
      	public void run() {
      	  // give the worker some time to interrupt itself properly
      		try {
	          Thread.sleep(10000);
          } catch (InterruptedException e) {
	          e.printStackTrace();
          } 
      		if (thread != null && thread.isAlive()) {
      			setWorkerMessage("Interrupting thread!");
      			thread.interrupt();
      		}
      	}
      }).start();
    }
    else if(command.equals("reset")) {
      queue.reset();
    }
    else if(command.equals("detailed-info")) {
      printDetailedInfo(rsp);
    }
    else if(command.equals("start")) {
      if (isBusy()) {
        rsp.add("message", "Batch processing is already running...");
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
    else if(command.equals("get-results")) {
      getResults(req, rsp);
      return;
    }
    else if(providers.containsKey(command)) {
    	if (params.get("jobid", null) == null) {
    		ModifiableSolrParams mParams = new ModifiableSolrParams( req.getParams() );
        mParams.set( "jobid", UUID.randomUUID().toString());
        req.setParams(mParams);
    	}
    	Provider provider = providers.get(command);
      queue.registerNewBatch(providers.get(command), req.getParams());
      rsp.add("jobid", req.getParams().get("jobid"));
    }
    else {
      rsp.add("message", "Unknown command: " + command);
      List<String> commands = new ArrayList<String>(Arrays.asList("start", "stop", "reset", "info", "detailed-info"));
      for (Provider p: providers.values()) {
      	commands.add(p.name);
      }
      rsp.add("availableCommands", commands);
    }
    
    
    rsp.add("status", isBusy() ? "busy" : "idle");
    printInfo(rsp);

  }

  private void printInfo(SolrQueryResponse rsp) {
    Map<String, String> rows = new LinkedHashMap<String, String>();
    
    
    rows.put("queueSize", Integer.toString(queue.tbdQueue.size()));
    rows.put("failedBatches", Integer.toString(queue.failedQueue.size()));
    
    rows.put("registeredRequests", Integer.toString(queue.queuedIn));
    rows.put("restartedRequests", Integer.toString(queue.queuedOut));
    
    rsp.add("lastWorkerMessage", getLastWorkerMessage());
    
    rsp.add("info", rows);
  }
  

  private String getLastWorkerMessage() {
    if (workerMessage.size() > 0) return workerMessage.get(0);
    return "<no message yet>";
  }

  
  private void printDetailedInfo(SolrQueryResponse rsp) {
    
    
    List<String> tbd = new ArrayList<String>();
    rsp.add("toBeDone", tbd);
    for (RequestData rd: queue.tbdQueue.values()) {
      tbd.add(rd.toString());
    }
    
    
    List<String> fb = new ArrayList<String>();
    rsp.add("failedBatches", fb);
    for (RequestData rd: queue.failedQueue.values()) {
      fb.add(rd.toString());
    }
    
    rsp.add("allMessages", getWorkerMessage());
  }


  private void runAsynchronously(SolrQueryRequest req) {

    final SolrQueryRequest request = req;

    thread = new Thread(new Runnable() {

      public void run() {
        setWorkerMessage("I am idle");
        try {
          while (queue.hasMore()) {
            setWorkerMessage("Running in the background... (" + queue.getNext() + ")");
            runSynchronously(queue, request);
          }
        } catch (Exception e) {
          setWorkerMessage("Worker error..." + e.getLocalizedMessage());
          log.error(e.getLocalizedMessage());
          log.error(e.getStackTrace().toString());
        } finally {
          setBusy(false);
          request.close();
        }
      }
    });
    
    thread.start();
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
  private void runSynchronously(RequestQueue queue, SolrQueryRequest req) {

    SolrCore core = req.getCore();

    RequestData data = queue.pop();
    SolrParams params = data.getReqParams();
    Provider provider = data.getProvider();
    
    SolrQueryRequest locReq = new LocalSolrQueryRequest(req.getCore(), params);
    try {
    	setWorkerMessage("Executing :" + provider);
    	provider.run(locReq);
    }
    catch(Exception e) {
      queue.registerFailedBatch(data);
      log.error(e.getLocalizedMessage());
    }
    finally {
    	locReq.close();
    }
  }
  



  public String getVersion() {
    return "";
  }

  public String getDescription() {
    return "Executes long-running commands in the background";
  }

  public String getSourceId() {
    return "";
  }

  public String getSource() {
    return "";
  }

  
  private void getResults(SolrQueryRequest req, SolrQueryResponse rsp) {
			
			SolrParams params = req.getParams();
			String jobid = params.get("jobid", null);
			if (jobid == null) {
				throw new SolrException(ErrorCode.BAD_REQUEST, "The 'jobid' parameter is missing");
			}
			
			File jobFile = new File(tmpDir + "/" + jobid);
			if (!jobFile.exists()) {
				throw new SolrException(ErrorCode.BAD_REQUEST, "No results available (yet) for: " + jobid);
			}
			
  		// Include the file contents
      //The file logic depends on RawResponseWriter, so force its use.
      ModifiableSolrParams mParams = new ModifiableSolrParams( req.getParams() );
      mParams.set( CommonParams.WT, "raw" );
      req.setParams(mParams);

      ContentStreamBase content = new ContentStreamBase.FileStream( jobFile );
      content.setContentType( req.getParams().get( "contentType" ) );

      rsp.add(RawResponseWriter.CONTENT, content);
      rsp.setHttpCaching(false);
  }
  
  private void populateDefaultProviders() {
  	
  	providers.put("dump-freqs", new Provider("dump-freqs") {
  		public void run(SolrQueryRequest req) throws Exception {
  			
  			SolrCore core = req.getCore();
  			SolrParams params = req.getParams();
  			IndexSchema schema = core.getSchema();
  			final HashSet<String> fieldsToLoad = new HashSet<String>();
  			
  			String[] fields = params.getParams("fields");
  			for (String f: fields) {
  				for (String ff: f.split("( |,)")) {
		  			SchemaField field = schema.getFieldOrNull(ff);
		  	    if (field==null || !field.indexed()) {
		  	      throw new SolrException(ErrorCode.BAD_REQUEST, "We cannot dump fields that do not exist or are not indexed: " + ff);
		  	    }
		  	    fieldsToLoad.add(ff);
  				}
  			}
  			
  			File jobFile = new File(tmpDir + "/" + params.get("jobid"));
  	    final BufferedWriter out = new BufferedWriter(new FileWriter(jobFile), 1024*256);
  	    out.write("term");
				out.write("\t");
				out.write("termFreq");
				out.write("\t");
				out.write("docFreq");
  	    
  			DirectoryReader ir = req.getSearcher().getIndexReader();
  			TermsEnum reuse = null;
  			int processed = 0;
  			for (String f: fieldsToLoad) {
	  	    
  				out.write("\n\n# " + f + "\n");
  				
	  	    Terms te = MultiFields.getTerms(ir, f);
	  	    if (te == null) {
	  	    	out.write("# term stats is not available for this field");
	  	    	continue;
	  	    }
	  	    reuse = te.iterator(reuse);
	  	    
	  	    BytesRef term;
					while((term = reuse.next()) != null) {
						out.write(term.utf8ToString());
						out.write("\t");
						out.write(Long.toString(reuse.totalTermFreq()));
						out.write("\t");
						out.write(Long.toString(reuse.docFreq()));
						out.write("\n");
						
						processed++;
						if (processed % 10000 == 0) {
	          	if(queue.isStopped()) { // inside, because queue is synchronized
	          		throw new IOException("Collector interrupted - stopping");
	          	}
	          }
	  	    }
  			}
  	    out.close();
  	    
  		}
  	});
  	
  	
  	providers.put("dump-index", new Provider("dump-index") {
  		public void run(SolrQueryRequest req) throws Exception {
  			
  			SolrCore core = req.getCore();
  			SolrParams params = req.getParams();
  			IndexSchema schema = core.getSchema();
  			final HashSet<String> fieldsToLoad = new HashSet<String>();
  			final Analyzer analyzer = core.getSchema().getQueryAnalyzer();
  			
  			String q = params.get(CommonParams.Q, null);
  			if (q == null) {
  				throw new SolrException(ErrorCode.BAD_REQUEST, "The 'q' parameter is missing, but we must know how to select docs");
  			}
  			
  			String[] fields = params.getParams("fields");
  			for (String f: fields) {
  				for (String ff: f.split("( |,)")) {
		  			SchemaField field = schema.getFieldOrNull(ff);
		  	    if (field==null || !field.stored()) {
		  	      throw new SolrException(ErrorCode.BAD_REQUEST, "We cannot dump fields that do not exist or are not stored: " + ff);
		  	    }
		  	    fieldsToLoad.add(ff);
  				}
  			}
  			
  			String defType = params.get(QueryParsing.DEFTYPE,QParserPlugin.DEFAULT_QTYPE);
  			QParser parser = QParser.getParser(q, defType, req);
        Query query = parser.getQuery();
        
  	    SolrIndexSearcher se = req.getSearcher();
  	    
  	    HashMap<String, String> descr = new HashMap<String, String>();
  	    descr.put("query", query.toString());
  	    descr.put("indexDir", se.getIndexDir());
  	    descr.put("indexVersion", se.getVersion());
  	    descr.put("maxDoc", Integer.toString(se.maxDoc()));
  	    descr.put("date", new Date().toString()); 
  	    
  	    File jobFile = new File(tmpDir + "/" + params.get("jobid"));
  	    final BufferedWriter out = new BufferedWriter(new FileWriter(jobFile), 1024*256);
  	    out.write("{\n");
  	    out.write("\"description\": " + JSONUtil.toJSON(descr).replace("\n", " ") + ",\n");
  	    out.write("\"data\" : [\n");
  	    se.search(query, new Collector() {
  	      private AtomicReader reader;
  	      private int processed = 0;
  	      private CharTermAttribute termAtt;
  	      private PositionIncrementAttribute posIncrAtt;
  	      private Map<String, List<String>>document = new HashMap<String, List<String>>();
  	      
  	      @Override
  	      public boolean acceptsDocsOutOfOrder() {
  	        return true;
  	      }

  	      @Override
  	      public void collect(int i) throws IOException {
  	        Document d;
	          d = reader.document(i, fieldsToLoad);
	          processed++;
	          document.clear();
	          
	          if (processed % 10000 == 0) {
	          	if(queue.isStopped()) { // inside, because queue is synchronized
	          		throw new IOException("Collector interrupted - stopping");
	          	}
	          }
	          
	          
	          for (String f: fieldsToLoad) {
	          	List<String> tokens = new ArrayList<String>(500);
	          	document.put(f, tokens);
	            String[] vals = d.getValues(f);
	            posIncrAtt = null;
	            for (String s: vals) {
	              TokenStream buffer = analyzer.tokenStream(f, new StringReader(s));
	              
	              if (!buffer.hasAttribute(CharTermAttribute.class)) {
	                continue; // empty stream
	              }

	              termAtt = buffer.getAttribute(CharTermAttribute.class);
	              
	              if (buffer.hasAttribute(PositionIncrementAttribute.class)) {
  	              posIncrAtt = buffer.getAttribute(PositionIncrementAttribute.class);
  	            }
	              buffer.reset();
	              
	              if (posIncrAtt != null) {
  	              while (buffer.incrementToken()) {
  	              	if (posIncrAtt.getPositionIncrement() == 0) {
  	              		tokens.set(tokens.size()-1, tokens.get(tokens.size()-1) + "|" + termAtt.toString());
  	              	}
  	              	else {
  	              		tokens.add(termAtt.toString());
  	              	}
  	              }
	              }
	              else {
	              	while (buffer.incrementToken()) {
	              		tokens.add(termAtt.toString());
  	              }
	              }
	            }
	          }
	          // bummer, it doesn't have api for newlines - according to quick googling
	          // control chars should be escaped in JSON, so this should be safe
	          out.write(JSONUtil.toJSON(document).replace("\n", " "));
	          out.write(",\n");
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
  	    //System.out.println("written + " + jobFile);
  	    out.write("]\n");
  	    out.write("}");
  	    out.close();
  		}
  	});
  	
  }
  
  public static boolean recurseDelete(File f) {
    if (f.isDirectory()) {
      for (File sub : f.listFiles()) {
        if (!recurseDelete(sub)) {
          System.err.println("!!!! WARNING: best effort to remove " + sub.getAbsolutePath() + " FAILED !!!!!");
          return false;
        }
      }
    }
    return f.delete();
  }
}
