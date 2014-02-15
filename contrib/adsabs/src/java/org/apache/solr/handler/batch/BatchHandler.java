package org.apache.solr.handler.batch;

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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.RawResponseWriter;
import org.apache.solr.response.SolrQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	BatchHandlerRequestQueue queue;
	private volatile int counter;
	private boolean asynchronous;
	private volatile List<String> workerMessage;
	private long sleepTime;
	private Map<String, BatchProvider> providers;
	private Thread thread;
	private File tmpDir;

	
	public BatchHandler() {
		queue = new BatchHandlerRequestQueue();
		workerMessage = new ArrayList<String>();
		providers = new HashMap<String, BatchProvider>();
		counter = 0;
		asynchronous = true;
		sleepTime = 300;
	}
	

	@SuppressWarnings("rawtypes")
	public void init(NamedList args) {
		super.init(args);


		NamedList defs = (NamedList) args.get("defaults");
		if (defs == null) {
			defs = new NamedList();
		}

		if (args.get("providers") != null) {
			NamedList<String> provs = (NamedList<String>) args.get("providers");
			Iterator<Entry<String, String>> it = provs.iterator();
			while (it.hasNext()) {
				Entry<String, String> p = it.next();
				BatchProviderLazyLoader bp = new BatchProviderLazyLoader();
				bp.setName(p.getValue().toString());
				bp.setQueue(queue);

				providers.put(p.getKey(), bp);
			}
		}

		if (defs.get("asynchronous") != null) {
			asynchronous = ((Boolean) defs.get("asynchronous") == true ) ? true : false;
		}

		if (defs.get("sleepTime") != null) {
			sleepTime  = Long.parseLong((String) defs.get("sleepTime"));
		}

		// we cannot get the solr indexdir at this point, so let's hope for best :)
		String startDir = System.getProperty("user.dir");

		if (defs.get("workdir") != null) {
			File wdir = new File((String) defs.get("workdir"));
			if (wdir.isAbsolute() && wdir.canWrite()) {
				tmpDir = wdir;
			}
			else if (!wdir.isAbsolute() && wdir.canWrite()) {
				log.info("Batch handler will write into: {}", wdir.getAbsolutePath());
				tmpDir = wdir;
			}
			else if (!wdir.exists()) {
				if (!wdir.isAbsolute()) {
					wdir = new File(startDir + "/" + wdir.toString());
				}
				// sadly, available only for java 7
        //Files.createDirectory(wdir.toPath(), 
        //		PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr-----")));
        wdir.mkdir();
				tmpDir = wdir;
			}
			else {
				throw new RuntimeException("The folder is not readable: " + wdir.toString());
			}
		}
		else {
			//tmpDir = Files.createTempDirectory("montysolr-batch-handler", 
      //		PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr-----")));
      createTempDir("montysolr-batch-handler");
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
					setBusy(false);
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
		else if(command.equals("receive-data")) {
			receiveData(req, rsp);
		}
		else if(command.equals("status")) {
			if (params.get("jobid", null) == null) {
				throw new SolrException(ErrorCode.BAD_REQUEST, "I need 'jobid' parameter");
			}
			String jobid = params.get("jobid");
			if (queue.isJobidRegistered(jobid)) {
				if (queue.isJobidFailed(jobid)) {
					rsp.add("job-status", "failed");
					rsp.add("error", queue.getErrorMessage(jobid));
				}
				else if (queue.isJobidFinished(jobid)) {
					rsp.add("job-status", "finished");
				}
				else if (queue.isJobidRunning(jobid)) {
					rsp.add("job-status", "running");
				}
				else {
					rsp.add("job-status", "waiting");
				}
			}
			else {
				rsp.add("job-status", "no-such-job");
			}
		}
		else if(providers.containsKey(command)) {
			ModifiableSolrParams mParams = new ModifiableSolrParams( req.getParams() );
			req.setParams(mParams);
			
			if (mParams.get("jobid", null) == null) {
				mParams.set( "jobid", UUID.randomUUID().toString());
			}
			mParams.set("#workdir", tmpDir.getAbsolutePath());
			
			queue.registerNewBatch(providers.get(command), req.getParams());
			rsp.add("jobid", req.getParams().get("jobid"));
		}
		else {
			rsp.add("message", "Unknown command: " + command);
			List<String> commands = new ArrayList<String>(Arrays.asList(
					"start", "stop", "reset", "info", "detailed-info",
					"get-results", "receive-data", "status"));
			for (String p: providers.keySet()) {
				commands.add(p);
			}
			rsp.add("availableCommands", commands);
		}


		rsp.add("status", isBusy() ? "busy" : "idle");
		printInfo(rsp);

	}
	
	


	

	/*
	 * Writes whatever data was sent through the request into a file,
	 * we do not care for the content type, we dump it there as it is
	 * received.
	 */
	private void receiveData(SolrQueryRequest req, SolrQueryResponse rsp) throws IOException {
		SolrParams params = req.getParams();
		String jobid = null;
		if (params.get("jobid", null) == null) {
			throw new SolrException(ErrorCode.BAD_REQUEST, "The 'jobid' parameter is missing");
		}
		
		jobid = params.get("jobid");
		if (!queue.isJobidRegistered(jobid)) {
			throw new SolrException(ErrorCode.BAD_REQUEST, "Unknown 'jobid' - you must create a task first");
		}

		File jobFile = new File(tmpDir + "/" + jobid + ".input");

		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(jobFile));

			for (ContentStream cs: req.getContentStreams()) {
				InputStream is = cs.getStream();
				try {  
					byte[] buffer = new byte[4096];  
					for (int n; (n = is.read(buffer)) != -1; )   
						os.write(buffer, 0, n); 
				} 
				finally { 
					is.close(); 
				}
			}
		}
		finally {
			if (os != null)
				os.close();
		}
	}

	private void printInfo(SolrQueryResponse rsp) {
		Map<String, String> rows = new LinkedHashMap<String, String>();


		rows.put("queueSize", Integer.toString(queue.getTbdQueueSize()));
		rows.put("failedBatches", Integer.toString(queue.getFailedQueueSize()));

		rows.put("registeredRequests", Integer.toString(queue.getTotalQueueSize()));
		rows.put("restartedRequests", Integer.toString(queue.getTotalFinishedSize()));

		rsp.add("lastWorkerMessage", getLastWorkerMessage());

		rsp.add("info", rows);
	}


	private String getLastWorkerMessage() {
		if (workerMessage.size() > 0) return workerMessage.get(0);
		return "<no message yet>";
	}


	private void printDetailedInfo(SolrQueryResponse rsp) {

		rsp.add("toBeDone", queue.getQueueDetails(10, 1));

		rsp.add("failedBatches", queue.getQueueDetails(10, 0));

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
					log.error(getErrorStackTrace(e));
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
	private void runSynchronously(BatchHandlerRequestQueue queue, SolrQueryRequest req) {

		SolrCore core = req.getCore();

		BatchHandlerRequestData data = queue.pop();
		SolrParams params = data.getReqParams();
		BatchProvider provider = data.getProvider();

		SolrQueryRequest locReq = new LocalSolrQueryRequest(req.getCore(), params);
		try {
			setWorkerMessage("Executing :" + provider);
			provider.run(locReq, queue);
			queue.registerFinishedBatch(data);
		}
		catch(Exception e) {
			String trace = getErrorStackTrace(e);
			data.setMsg(trace);
			queue.registerFailedBatch(providers.get("#failed"), data);
			log.error("Error executing: " + locReq);
			log.error(trace);
		}
		finally {
			locReq.close();
		}
	}



	private String getErrorStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
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

	public class BatchProviderLazyLoader extends BatchProvider {

		BatchProviderI _runner = null;
		BatchHandlerRequestQueue _queue = null;
		
		private void initialize(SolrQueryRequest req) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
			Class clazz = loadClass(name, req.getCore());
			if (BatchProviderI.class.isAssignableFrom(clazz)) {
				_runner = (BatchProviderI) clazz.newInstance();
				_runner.setName(name);
			}
			else {
				throw new RuntimeException("The class does not provide BatchProviderI interface: " + this.name);
			}
		}

		@Override
		public void run(SolrQueryRequest locReq, BatchHandlerRequestQueue queue) throws Exception {
			if (_runner == null) {
				initialize(locReq);
			}
			_runner.run(locReq, _queue);
		}

		public void setQueue(BatchHandlerRequestQueue queue) {
	    _queue = queue;
    }

		@SuppressWarnings("unchecked")
		private Class loadClass(String name, SolrCore core) throws ClassNotFoundException {
			try {
				return core != null ?
						core.getResourceLoader().findClass(name, Object.class) :
							Class.forName(name);
			} catch (Exception e) {
				try {
					String n = BatchHandler.class.getPackage().getName() + "." + name;
					return core != null ?
							core.getResourceLoader().findClass(n, Object.class) :
								Class.forName(n);
				} catch (Exception e1) {
					throw new ClassNotFoundException("Unable to load " + name + " or " + BatchHandler.class.getPackage().getName() + "." + name, e);
				}
			}
		}

		public String toString() {
			return "LazyBatchProvider:(" + this.name + ")";
		}

		@Override
    public String getDescription() {
	    if (_runner == null) {
	    	return "Lazy loader for a provider: " + name + " I don't know what I hold :-)";
	    }
	    else {
	    	return _runner.getDescription();
	    }
    }
	}
	
	/*
	 * A copy from the Google Guava, as Java 6 is missing this 
	 * functionality
	 */
	public static File createTempDir(String baseName) {
	  int TEMP_DIR_ATTEMPTS =  10000;
	  File baseDir = new File(System.getProperty("java.io.tmpdir"));
	  baseName = baseName + "-" + System.currentTimeMillis() + "-";

	  for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
	    File tempDir = new File(baseDir, baseName + counter);
	    if (tempDir.mkdir()) {
	      return tempDir;
	    }
	  }
	  throw new IllegalStateException("Failed to create directory within "
	      + TEMP_DIR_ATTEMPTS + " attempts (tried "
	      + baseName + "0 to " + baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
	}
}

