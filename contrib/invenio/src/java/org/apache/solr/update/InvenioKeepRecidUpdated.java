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

import invenio.montysolr.PythonCall;
import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.search.DictionaryRecIdCache;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.InvenioRequestHandler;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.handler.dataimport.DataImportHandler;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This handler keeps Solr index in sync with the Invenio database.
 * Basically, on every invocation it calls Invenio to retrieve set
 * of added/updated/deleted document recids.
 * 
 * Note from the author: I don't like my code at all, it should be simpler
 * 
 * When we have these ids, we'll call the respective handlers and
 * pass them recids. This implementation extends {@link DataImportHandler}
 * therefore it is sequential. While one import is running, consecutive
 * requests to the same import handler class will respond with 
 * importStatus <b>busy</b>
 * 
 * 	@param request parameters
 * 		<p>
 * 		- <b>last_recid</b>: the recid of the reference record, it will be the
 * 			orientation point to find all newer changed/added/deleted recs
 * 			
 * 			If null, we find the highest <code>recid</code> from
 * 		    {@link DictionaryRecIdCache} using {@link IndexSchema}#getUniqueKeyField()
 * 			
 * 			If last_recid == -1, we start from the first document
 * 		<p>
 * 		- <b>generate</b>: boolean parameter which means empty lucene documents
 * 			should be generated in the range <b>{last_recid, max_recid}</b>
 * 
 *  		- <b>max_recid</b>: integer, marks the end of the interval, must be
 *  			supplied when using generate
 *  		If <b>generate</b> is false, then we will try to retrieve recids
 *  		from invenio and start the indexing/updates
 *  
 * 		<p>
 *  	- <b>inveniourl</b> : complete url to the Invenio search (we'll prepend query
 *        		parameters, eg. inveniourl?p=recid:x->y)
 *      <p>
 *  	- <b>updateurl</b> : complete url to the Solr update handler (this handler
 *              should fetch <b>updated</b> source documents and index them)
 *      <p>
 *  	- <b>importurl</b> : complete url to the Solr update handler (this handler
 *              should fetch <b>new</b> source documents and index them)
 *      <p>
 *  	- <b>deleteurl</b> : complete url to the Solr update handler (this handler
 *              should remove <b>deleted</b> documents from Solr index)
 *              
 *              
 *  <p>            
 *  Example configuration:
 *  <pre>
 *    last_recid: 90
 *    inveniourl: http://invenio-server/search
 *    updateurl: http://localhost:8983/solr/update-dataimport?command=full-import&dirs=/proj/fulltext/extracted    
 *    importurl: http://localhost:8983/solr/waiting-dataimport?command=full-import&arg1=val1&arg2=val2
 *    deleteurl: http://localhost:8983/solr/delete-dataimport?command=full-import
 *    maximport: 200
 *  
 *  using modification date of the recid 90 we discover...
 *  
 *    updated records: 53, 54, 55, 100
 *    added records: 101,103
 *    deleted records: 91,92,93,102
 *  
 *  ...which results in 3 requests (newline breaks added for readability):
 *  
 *  
 *  1. http://localhost:8983/solr/update-dataimport?command=full-import&dirs=/proj/fulltext/extracted
 *     &url=http://invenio-server/search?p=recid:53->55 OR recid:100&rg=200&of=xm
 *     
 *  2. http://localhost:8983/solr/waiting-dataimport?command=full-import&arg1=val1&arg2=val2
 *     &url=http://invenio-server/search?p=recid:101 OR recid:103&rg=200&of=xm
 *  
 *  3. http://localhost:8983/solr/delete-dataimport?command=full-import
 *     &url=http://invenio-server/search?p=recid:91-93 OR recid:102&rg=200&of=xm
 *  
 *  </pre>
 *  
 *  NOTE: the url parameter <b>url</b> is url-encoded (it is here in plain form for readability)
 *  
 *  <p>
 *  Also, if you want to try the update handler manually, you must encode the parameters, eg:
 *  
 *  <code>
 *  http://localhost:8983/solr/invenio_update?last_recid=100&index=true
 *    &inveniourl=http%3A%2F%2Finvenio-server%2Fsearch
 *    &importurl=http%3A%2F%2Flocalhost%3A8983%2Fsolr%2Fwaiting-dataimport%3Fcommand%3Dfull-import%26dirs%3D%2Fproj%2Fadsx%2Ffulltext%2Fextracted
 *  </code>   
 *  
 */
public class InvenioKeepRecidUpdated extends RequestHandlerBase implements PythonCall {
	
	public static final Logger log = LoggerFactory
		.getLogger(InvenioRequestHandler.class);
	
	
	
	private volatile int counter = 0;
	private boolean asynchronous = true;
	
	static final String IKRU_PROPERTIES = "invenio_ikru.properties"; // will be put into context
	static final String LAST_RECID = "last_recid"; // name of the param from url and also what is passed to python
	static final String LAST_UPDATE = "mod_date"; // name of the param from url and also what is passed to python
	
	static final String ADDED = "ADDED"; // datastructure returned from python with recids is keyed
	static final String UPDATED = "UPDATED";
	static final String DELETED = "DELETED";
	
	static final String PARAM_INVENIO = "inveniourl"; // url params that influence processing
	static final String PARAM_IMPORT = "importurl";
	static final String PARAM_UPDATE = "updateurl";
	static final String PARAM_DELETE = "deleteurl";
	static final String PARAM_MAXIMPORT = "maximport";
	static final String PARAM_COMMIT = "commit";
	static final String PARAM_MAX_RECID = "max_recid";

	
	private String pythonFunctionName = "get_recids_changes";
	

	
	public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) 
		throws IOException, InterruptedException
			 {

		if (isBusy()) {
			rsp.add("message",
					"Import is already running, please retry later...");
			rsp.add("importStatus", "busy");
			return;
		}

		setBusy(true);

		SolrParams params = req.getParams();
		long start = System.currentTimeMillis();
		
		
		Properties prop = loadProperties(params);
		
		Map<String, Object> dictData = null;
		
		try {
			dictData = retrieveRecids(prop, req, rsp);
		}
		catch (RuntimeException e) {
			setBusy(false);
			throw e;
		}
		
		if (dictData == null) {
			setBusy(false);
			return;
		}

		LocalSolrQueryRequest locReq = new LocalSolrQueryRequest(req.getCore(), params);
		
		locReq.getContext().put(IKRU_PROPERTIES, prop);
		
		if (isAsynchronous()) {
			runAsynchronously(dictData, locReq);
		}
		else {
			runSynchronously(dictData, locReq);
			locReq.close();
			setBusy(false);
		}
		

		long end = System.currentTimeMillis();

		rsp.add("importStatus", isBusy() ? "busy" : "idle");
		rsp.add("QTime", end - start);
	}
	
	/*
	 * The method that discovers what was changed in Invenio DB
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Object> retrieveRecids(Properties prop,
			SolrQueryRequest req,
            SolrQueryResponse rsp) {
		
		HashMap<String, Object> retData = new HashMap<String, Object>();
		
		SolrParams params = req.getParams();
		
		Integer lastRecid = null;
		String lastUpdate = null;
		if (prop.containsKey(LAST_RECID)) {
			lastRecid = Integer.valueOf(prop.getProperty(LAST_RECID));
		}
		if (prop.containsKey(LAST_UPDATE)) {
			lastUpdate = prop.getProperty(LAST_UPDATE);
		}
		
		Map<String, int[]> dictData;
		// we'll generate empty records (good just to have a mapping between invenio
		// and lucene docids; necessary for search operations)
		if (params.getBool("generate", false)) {
			Integer max_recid = params.getInt(PARAM_MAX_RECID, 0);
			if (max_recid == 0 || max_recid < lastRecid) {
				throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
						"The max_recid parameter missing!");
			}

			dictData = new HashMap<String, int[]>();
			int[] a = new int[max_recid - lastRecid];
			for (int i = 0, ii = lastRecid + 1; ii < max_recid + 1; i++, ii++) {
				a[i] = ii;
			}
			dictData.put("ADDED", a);
			retData.put(LAST_UPDATE, null);
			retData.put(LAST_RECID, max_recid);
			
		} else {
			// get recids from Invenio {'ADDED': int, 'UPDATED': int, 'DELETED':
			// int }
			PythonMessage message = MontySolrVM.INSTANCE
					.createMessage(pythonFunctionName)
					.setSender(this.getClass().getSimpleName())
					.setParam("request", req)
					.setParam("response", rsp);
			
			if (lastRecid != null) message.setParam(LAST_RECID, lastRecid);
			if (lastUpdate != null) message.setParam(LAST_UPDATE, lastUpdate);
			
			MontySolrVM.INSTANCE.sendMessage(message);

			Object results = message.getResults();
			if (results == null) {
				rsp.add("message",
						"No new/updated/deleted records inside Invenio.");
				rsp.add("importStatus", "idle");
				return null;
			}
			dictData = (HashMap<String, int[]>) results;
			retData.put(LAST_UPDATE, (String) message.getParam(LAST_UPDATE));
			retData.put(LAST_RECID, (Integer) message.getParam(LAST_RECID));
		}
		retData.put("dictData", dictData);
		return retData;
    }

	
	public void setPythonFunctionName(String name) {
		pythonFunctionName = name;
	}
	
	public String getPythonFunctionName() {
		return pythonFunctionName;
	}

	
	private void runAsynchronously(Map<String, Object> dictData, 
			SolrQueryRequest req) {
		
		final Map<String, Object> dataToProcess = dictData;
		final SolrQueryRequest request = req;
		
		new Thread(new Runnable() {
			
			public void run() {
				try {
	                runSynchronously(dataToProcess, request);
                } catch (IOException e) {
                	log.error(e.getLocalizedMessage());
                } catch (InterruptedException e) {
                	log.error(e.getLocalizedMessage());
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
		}
		else {
			counter--;
		}
	}

	public boolean isBusy() {
		if (counter<0) {
			throw new IllegalStateException("Huh, 2+2 is not 4?! Should never happen.");
		}
		return counter > 0;
	}

	
	
	/*
	 * The main method/logic
	 */
	private void runSynchronously(Map<String, Object> data, 
			SolrQueryRequest req) 
			throws MalformedURLException, IOException, InterruptedException {
		
		
		SolrParams params = req.getParams();
		SolrCore core = req.getCore();
		
		String importurl = params.get(PARAM_IMPORT, null);
		String updateurl = params.get(PARAM_UPDATE, null);
		String deleteurl = params.get(PARAM_DELETE, null);
		
		
		@SuppressWarnings("unchecked")
		HashMap<String, int[]> dictData = (HashMap<String, int[]>) data.get("dictData");
		Properties prop = (Properties) req.getContext().get(IKRU_PROPERTIES);
		

		if (dictData.containsKey(ADDED) && dictData.get(ADDED).length > 0) {
			if (importurl != null) {
				if (importurl.equals("blankrecords")) {
					runProcessingAdded(dictData.get(ADDED), req);
				}
				else {
					runProcessing(core, importurl, dictData.get(ADDED), req);
				}
			}
		}
		
		if (dictData.containsKey(UPDATED) && dictData.get(UPDATED).length > 0) {
			if (updateurl != null) {
				if (updateurl.equals("blankrecords")) {
					runProcessingUpdated(dictData.get(UPDATED), req);
				}
				else {
					runProcessing(core, updateurl, dictData.get(UPDATED), req);
				}
			}
		}

		if (dictData.containsKey(DELETED) && dictData.get(DELETED).length > 0 ) {
			if (deleteurl != null) {
				if (deleteurl.equals("blankrecords")) {
					runProcessingDeleted(dictData.get(DELETED), req);
				}
				else {
					runProcessing(core, deleteurl, dictData.get(DELETED), req);
				}
			}
		}

		// save the state into the properties (the modification date must be there
		// in all situations 
		prop.put(LAST_UPDATE, (String) data.get(LAST_UPDATE));
		prop.put(LAST_RECID, String.valueOf((Integer) data.get(LAST_RECID)));
		saveProperties(prop);
		
		
		if (params.getBool(PARAM_COMMIT, false)) {
			CommitUpdateCommand updateCmd = new CommitUpdateCommand(true);
			req.getCore().getUpdateHandler().commit(updateCmd);
		}
	    
    }


		
	private void runProcessing(SolrCore core, String handlerUrl, int[] recids,
			SolrQueryRequest req) throws MalformedURLException, IOException, InterruptedException {
		
		SolrParams params = req.getParams();
		Integer maximport = params.getInt(PARAM_MAXIMPORT, 200);
		String inveniourl = params.get(PARAM_INVENIO, null);
		
		List<String> queryParts = getQueryIds(maximport, recids);
		List<String> urlsToFetch = new ArrayList<String>();
		
		URI u = null;
		SolrRequestHandler handler = null;
		try {
			u = new URI(handlerUrl);
			String p = u.getPath();
			if (u.getHost() == null || u.getHost() == "") {
				if (core.getRequestHandler(p) != null) {
					handler = core.getRequestHandler(p);
				}
				else if (!p.startsWith("/") && core.getRequestHandler("/" + p) != null) {
					handler = core.getRequestHandler("/" + p);
				}
				
				if (handler != null) {
					Map<String, List<String>> handlerParams = WebUtils.parseQuery(u.getQuery());
					HashMap<String, String[]> hParams = new HashMap<String, String[]>();
					for (String val: handlerParams.keySet()) {
						String[] nV = new String[handlerParams.get(val).size()];
						int i = 0;
						for (String v: handlerParams.get(val)) {
							nV[i] = v;
							i++;
						}
						hParams.put(val, nV);
					}
					
					LocalSolrQueryRequest localReq = null;
					for (String queryPart : queryParts) {
						
						String[] invP = new String[1];
						invP[0] = getInternalURL(inveniourl, queryPart, maximport);
						hParams.put("url", invP);
						localReq = new LocalSolrQueryRequest(core, hParams);
						SolrQueryResponse rsp = new SolrQueryResponse();
						core.execute(handler, localReq, rsp);
					}
					return;
				}
			}
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		
		for (String queryPart : queryParts) {
			urlsToFetch.add(getFetchURL(handlerUrl, inveniourl,
					queryPart, maximport));
		}
		runUpload(urlsToFetch);
	}

	private File getPropertyFile() {
		return new File("invenio.properties");
	}
	
	private Properties loadProperties(SolrParams params) throws FileNotFoundException, IOException {
	    	Properties prop = new Properties();
	    	
			
	    	File f = getPropertyFile();
	    	if (f.exists()) {
	    		prop.load(new FileInputStream(f));
	    	}
	    	
	    	String prop_recid = null;
	    	if (prop.containsKey(LAST_RECID)) {
	    		prop_recid = (String) prop.remove(LAST_RECID);
	    	}
	    	
	    	String prop_mod_date = null;
	    	if (prop.containsKey(LAST_UPDATE)) {
	    		prop_mod_date = (String) prop.remove(LAST_UPDATE);
	    	}
	    	
	    	boolean userParam = false;
	    	
	    	// parameters in url have always precedence (if both set
	    	// it is up to the python to figure out who has precedence
			if (params.getInt(LAST_RECID) != null) {
				prop.put(LAST_RECID, params.get(LAST_RECID));
				userParam = true;
			} 
			
			if (params.get(LAST_UPDATE, null) != null) {
				prop.put(LAST_UPDATE, params.get(LAST_UPDATE));
				userParam = true;
			}
			
			if (!userParam) {
				// when no user params were supplied, prefer the mod_date over recid
				if (prop_mod_date != null) {
					prop.put(LAST_UPDATE, prop_mod_date);
				}
				else if (prop_recid != null) {
					prop.put(LAST_RECID, prop_recid);
				}
			}
			
	    	return prop;
	}
	
	private void saveProperties(Properties prop) throws IOException {
		File f = getPropertyFile();
		FileOutputStream out = new FileOutputStream(f);
		prop.store(out, null);
	}
	
	

	
	/*
	 * When method-blankrecords we are creating/adding empty docs
	 */
	
	protected void runProcessingAdded(int[] recids, SolrQueryRequest req) throws IOException {
		
		IndexSchema schema = req.getSchema();
		UpdateHandler updateHandler = req.getCore().getUpdateHandler();
		String uniqField = schema.getUniqueKeyField().getName();
		
		AddUpdateCommand addCmd = new AddUpdateCommand();
		addCmd.allowDups = false;
		addCmd.overwriteCommitted = false;
		addCmd.overwritePending = false;
		
		if (recids.length > 0) {
			SolrInputDocument doc = new SolrInputDocument();
			for (int i = 0; i < recids.length; i++) {
				doc.clear();
				doc.addField(uniqField,	recids[i]);
				addCmd.doc = DocumentBuilder.toDocument(doc, schema);
				updateHandler.addDoc(addCmd);
			}
		}
			
	}
	
	protected void runProcessingUpdated(int[] recids, SolrQueryRequest req) throws IOException {
		IndexSchema schema = req.getSchema();
		UpdateHandler updateHandler = req.getCore().getUpdateHandler();
		String uniqField = schema.getUniqueKeyField().getName();
		
		AddUpdateCommand addCmd = new AddUpdateCommand();
		addCmd.allowDups = false;
		addCmd.overwriteCommitted = false;
		addCmd.overwritePending = false;

        if (recids.length > 0) {
			
			Map<Integer, Integer> map = DictionaryRecIdCache.INSTANCE
					.getTranslationCache(req.getSearcher().getReader(), 
							uniqField);
			SolrInputDocument doc = new SolrInputDocument();
			
			for (int i = 0; i < recids.length; i++) {
				if (!map.containsKey(recids[i])) {
					doc.clear();
					doc.addField(uniqField,	recids[i]);
					addCmd.doc = DocumentBuilder.toDocument(doc, schema);
					updateHandler.addDoc(addCmd);
				}
			}
		}
	}
	
	protected void runProcessingDeleted(int[] recids, SolrQueryRequest req) throws IOException {
		IndexSchema schema = req.getSchema();
		UpdateHandler updateHandler = req.getCore().getUpdateHandler();
		
		DeleteUpdateCommand delCmd = new DeleteUpdateCommand();
		delCmd.fromCommitted = true;
        delCmd.fromPending = true;

        if (recids.length > 0) {
			
			for (int i = 0; i < recids.length; i++) {
				delCmd.id = Integer.toString(recids[i]);
				updateHandler.delete(delCmd);
			}
		}
	}
	
	
	/*
	 * Internally calling dataimport handler
	 */
	protected void runProcessingInternally(SolrRequestHandler handler, 
			int[] recids, SolrQueryRequest req) throws IOException {
		SolrCore core = req.getCore();
		SolrParams params = req.getParams();
		
		
		
		SolrQueryResponse rsp = new SolrQueryResponse();
		//core.execute(handler, req, rsp);
	}
	
	
	/*
	 * Calling external URL's
	 */
	protected void runUpload(List<String> urlsToFetch) throws MalformedURLException, IOException,
			InterruptedException {
		while (urlsToFetch.size() > 0) {
			String url = urlsToFetch.remove(0);
			String html = IOUtils.toString(new URL(url).openStream());
			while (html.contains("busy")) {
				Thread.sleep(200);
			}
		}

	}

	protected String getFetchURL(String importurl, String inveniourl,
			String queryPart, Integer maximport)
			throws UnsupportedEncodingException {
		String sign = importurl.contains("?") ? "&" : "?";
		String sign2 = inveniourl.contains("?") ? "&" : "?";
		return importurl
				+ sign
				+ "url="
				+ java.net.URLEncoder.encode(
					inveniourl + sign2 + "p=" + java.net.URLEncoder.encode(queryPart, "UTF-8") + "&rg=" + maximport + "&of=xm",
					"UTF-8");
	}
	
	protected String getInternalURL(String sourceUrl,
			String queryPart, Integer maximport)
			throws UnsupportedEncodingException {
		String sign = sourceUrl.contains("?") ? "&" : "?";
		return sourceUrl
				+ sign
				+ "p=" + java.net.URLEncoder.encode(queryPart, "UTF-8") 
				+ "&rg=" + maximport + "&of=xm";
	}

	// ////////////////////// SolrInfoMBeans methods //////////////////////

	/**
	 * Will split array of intgs into query "recid:4->15 OR recid:78 OR recid:80->82"
	 */
	public static List<String> getQueryIds(int maxspan, int[] recids) {

		Arrays.sort(recids);
		List<String> ret = new ArrayList<String>();
		StringBuilder query;

		int i = 0;

		while (i < recids.length) {
			int delta = 1;
			int last_id = 0;
			query = new StringBuilder();
			query.append("recid:" + recids[i]);
			for (i++; i < recids.length; i++) {
				if (delta >= maxspan) {
					break;
				}
				if (recids[i] - 1 == recids[i - 1]) {
					last_id = recids[i];
					delta += 1;
					continue;
				}
				if (last_id > 0) {
					query.append("->" + last_id);
					last_id = 0;
				}
				query.append(" OR recid:" + recids[i]);
				delta += 1;
			}

			if (last_id > 0) {
				query.append("->" + last_id);
			}

			ret.add(query.toString());
		}
		return ret;

	}
	
	
	public String getVersion() {
		return "";
	}

	
	public String getDescription() {
		return "Updates the Invenio recid with the missing/new docs (if any)";
	}

	
	public String getSourceId() {
		return "";
	}

	
	public String getSource() {
		return "";
	}
	
	
}
