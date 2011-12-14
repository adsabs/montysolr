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

import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.jni.MontySolrVM;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.DictionaryRecIdCache;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.InvenioRequestHandler;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.handler.dataimport.DataImportHandler;
import org.apache.solr.handler.dataimport.SolrWriter;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This handler keeps Solr index in sync with the Invenio database.
 * Basically, on every invocation it calls Invenio to retrieve set
 * of added/updated/deleted document recids.
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
public class InvenioKeepRecidUpdated extends RequestHandlerBase {
	
	public static final Logger log = LoggerFactory
		.getLogger(InvenioRequestHandler.class);
	
	
	protected volatile List<String> urlsToFetch = new ArrayList<String>();
	private volatile int counter = 0;
	private boolean asynchronous = true;

	@Override
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
		SolrParams required = params.required();
		SolrCore core = req.getCore();
		IndexSchema schema = req.getSchema();
		UpdateHandler updateHandler = core.getUpdateHandler();

		long start = System.currentTimeMillis();
		
		int last_recid = getLastRecid(params, req);
		rsp.add("lastRecid", last_recid);

		
		Map<String, int[]> dictData = retrieveRecids(last_recid, params, req, rsp);
		if (dictData == null) {
			setBusy(false);
			return;
		}

		
		if (isAsynchronous()) {
			runAsynchronously(params, dictData, schema, updateHandler);
		}
		else {
			runSynchronously(params, dictData, schema, updateHandler);
		}
		

		long end = System.currentTimeMillis();

		rsp.add("importStatus", isBusy() ? "busy" : "OK");
		rsp.add("QTime", end - start);
	}

	
	private void runSynchronously(SolrParams params, Map<String, int[]> dictData, 
			IndexSchema schema, UpdateHandler updateHandler) 
			throws MalformedURLException, IOException, InterruptedException {
		
		String inveniourl = params.get("inveniourl", null);
		String importurl = params.get("importurl", null);
		String updateurl = params.get("updateurl", importurl);
		String deleteurl = params.get("deleteurl", importurl);
		Integer maximport = params.getInt("maximport", 200);
		Boolean commit = params.getBool("commit", false);
		
		List<String> queryParts;

		if (dictData.containsKey("ADDED") ) {
			if (importurl != null) {
				queryParts = getQueryIds(maximport, dictData.get("ADDED"));
				for (String queryPart : queryParts) {
					urlsToFetch.add(getFetchURL(importurl, inveniourl,
							queryPart, maximport));
				}
				runUpload();
			}
			else {
				runProcessingAdded(dictData.get("ADDED"), schema, commit, updateHandler);
			}
			
		}
		
		if (dictData.containsKey("UPDATED") ) {
			if (updateurl != null) {
				queryParts = getQueryIds(maximport, dictData.get("UPDATED"));
				for (String queryPart : queryParts) {
					urlsToFetch.add(getFetchURL(updateurl, inveniourl,
							queryPart, maximport));
				}
				runUpload();
			}
			else {
				runProcessingUpdated(dictData.get("UPDATED"), schema, commit, updateHandler);
			}
		}

		if (dictData.containsKey("DELETED") ) {
			if (deleteurl != null) {
				queryParts = getQueryIds(maximport, dictData.get("DELETED"));
				for (String queryPart : queryParts) {
					urlsToFetch.add(getFetchURL(updateurl, deleteurl,
							queryPart, maximport));
				}
				runUpload();
			}
			else {
				runProcessingDeleted(dictData.get("DELETED"), schema, commit, updateHandler);
			}
		}
		
		if (commit) {
			CommitUpdateCommand updateCmd = new CommitUpdateCommand(commit);
			updateHandler.commit(updateCmd);
		}
	    
    }


	private void runAsynchronously(SolrParams solrParams, Map<String, int[]> dictData, IndexSchema schema, UpdateHandler updateHandler) {
		
		final SolrParams params = solrParams;
		final Map<String, int[]> dataToProcess = dictData;
		final IndexSchema indexSchema = schema;
		final UpdateHandler uHandler = updateHandler;
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
	                runSynchronously(params, dataToProcess, indexSchema, uHandler);
                } catch (IOException e) {
                	log.error(e.getLocalizedMessage());
                } catch (InterruptedException e) {
                	log.error(e.getLocalizedMessage());
				} finally {
                	setBusy(false);
                }
			}
		}).start();
    }


	protected void setAsynchronous(boolean val) {
		asynchronous = val;
	}
	
	
	protected boolean isAsynchronous() {
		return asynchronous;
	}
	
	protected Map<String, int[]> retrieveRecids(int lastRecid, SolrParams params, SolrQueryRequest req,
            SolrQueryResponse rsp) {
		
		Map<String, int[]> dictData;
		// we'll generate empty records (good just to have a mapping between invenio
		// and lucene docids; necessary for search operations)
		if (params.getBool("generate", false)) {
			Integer max_recid = params.getInt("max_recid", 0);
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
		} else {
			// get recids from Invenio {'ADDED': int, 'UPDATED': int, 'DELETED':
			// int }
			PythonMessage message = MontySolrVM.INSTANCE
					.createMessage("get_recids_changes")
					.setSender(this.getClass().getSimpleName())
					.setSolrQueryRequest(req).setSolrQueryResponse(rsp)
					.setParam("last_recid", lastRecid);
			MontySolrVM.INSTANCE.sendMessage(message);

			Object results = message.getResults();
			if (results == null) {
				rsp.add("message",
						"No new/updated/deleted records according to Invenio.");
				return null;
			}
			dictData = (HashMap<String, int[]>) results;
		}
		return dictData;
    }

	
	
	protected int getLastRecid(SolrParams params, SolrQueryRequest req) throws IOException {
		int last_recid = -1; // -1 means get the first created doc

		// Either generate or retrieve the docid of the last-indexed record
		// TODO: this considers only the highest id, but we should get the
		// oldest
		if (params.getInt("last_recid") != null) {
			last_recid = params.getInt("last_recid");
		} else {
			int[] ids = DictionaryRecIdCache.INSTANCE.getLuceneCache(req
					.getSearcher().getReader(), req.getSchema().getUniqueKeyField()
					.getName());
			for (int m : ids) {
				if (m > last_recid) {
					last_recid = m;
				}
			}
		}
		return last_recid;

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
	
	
	protected void runProcessingAdded(int[] recids, IndexSchema schema,
			boolean commit, UpdateHandler updateHandler) throws IOException {
		
		AddUpdateCommand addCmd = new AddUpdateCommand();
		addCmd.allowDups = false;
		addCmd.overwriteCommitted = false;
		addCmd.overwritePending = false;
		
		if (recids.length > 0) {
			SolrInputDocument doc = null;
			for (int i = 0; i < recids.length; i++) {
				doc = new SolrInputDocument();
				doc.addField(schema.getUniqueKeyField().getName(),
						recids[i]);
				addCmd.doc = DocumentBuilder.toDocument(doc, schema);
				updateHandler.addDoc(addCmd);
			}
		}
			
	}
	
	protected void runProcessingUpdated(int[] recids, IndexSchema schema,
			boolean commit, UpdateHandler updateHandler) throws IOException {
		runProcessingAdded(recids, schema, commit, updateHandler);
	}
	
	protected void runProcessingDeleted(int[] recids, IndexSchema schema,
			boolean commit, UpdateHandler updateHandler) throws IOException {
		
		DeleteUpdateCommand delCmd = new DeleteUpdateCommand();
		delCmd.fromCommitted = true;
        delCmd.fromPending = true;

        if (recids.length > 0) {
			
			Map<Integer, Integer> map = DictionaryRecIdCache.INSTANCE.getTranslationCache(null, 
					schema.getUniqueKeyField().getName());
			
			for (int i = 0; i < recids.length; i++) {
				delCmd.id = map.get(recids[i]).toString();
				updateHandler.delete(delCmd);
			}
		}
	}
	
	
	
	private void runAsyncUpload() {
		new Thread() {
			@Override
			public void run() {
				try {
					runUpload();
				} catch (Exception e) {
					// pass
				}
				setBusy(false);
			}
		}.start();
	}

	
	protected void runUpload() throws MalformedURLException, IOException,
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
		return importurl
				+ "&url="
				+ java.net.URLEncoder.encode(
					inveniourl + "?p=" + queryPart + "&rg=" + maximport + "&of=xm",
					"UTF-8");

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
	
	@Override
	public String getVersion() {
		return "";
	}

	@Override
	public String getDescription() {
		return "Updates the Invenio recid with the missing/new docs (if any)";
	}

	@Override
	public String getSourceId() {
		return "";
	}

	@Override
	public String getSource() {
		return "";
	}
	
}
