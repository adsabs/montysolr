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
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.handler.dataimport.SolrWriter;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.util.DictionaryCache;

/**
 * Ping solr core
 *
 * @since solr 1.3
 */
public class InvenioKeepRecidUpdated extends RequestHandlerBase {
	private volatile List<String> urlsToFetch = new ArrayList<String>();
	private volatile boolean busy = false;

	@Override
	public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
			throws Exception {

		if (isBusy()) {
			rsp.add("message",
					"Import is already running, please retry later...");
			rsp.add("status", "busy");
			return;
		}

		setBusy(true);

		SolrParams params = req.getParams();
		SolrParams required = params.required();
		SolrCore core = req.getCore();
		IndexSchema schema = req.getSchema();

		UpdateHandler updateHandler = core.getUpdateHandler();

		long start = System.currentTimeMillis();

		AddUpdateCommand addCmd = new AddUpdateCommand();
		addCmd.allowDups = false;
		addCmd.overwriteCommitted = false;
		addCmd.overwritePending = false;

		int last_recid = -1; // -1 means get the first created doc

		// Either generate or retrieve the docid of the last-indexed record
		// TODO: this considers only the highest id, but we should get the
		// oldest
		if (params.getInt("last_recid") != null) {
			last_recid = params.getInt("last_recid");
		} else {
			int[] ids = DictionaryCache.INSTANCE.getLuceneCache(req
					.getSearcher().getReader(), schema.getUniqueKeyField()
					.getName());
			for (int m : ids) {
				if (m > last_recid) {
					last_recid = m;
				}
			}
		}

		rsp.add("last_recid", last_recid);

		Map<String, int[]> dictData;

		if (params.getBool("generate", false)) {
			Integer max_recid = params.getInt("max_recid", 0);
			if (max_recid == 0 || max_recid < last_recid) {
				throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
						"The max_recid parameter missing!");
			}

			dictData = new HashMap<String, int[]>();
			int[] a = new int[max_recid - last_recid];
			for (int i = 0, ii = last_recid + 1; ii < max_recid + 1; i++, ii++) {
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
					.setParam("last_recid", last_recid);
			MontySolrVM.INSTANCE.sendMessage(message);

			Object results = message.getResults();
			if (results == null) {
				rsp.add("message",
						"Invenio returned null. We cannot update anything.");
				return;
			}
			dictData = (HashMap<String, int[]>) results;
		}

		Boolean index = params.getBool("index", false);
		String datasource = params.get("datasource", null);
		String importurl = params.get("importurl", null);
		Integer maximport = params.getInt("maximport", 200);

		if (index && datasource != null && importurl != null) {
			rsp.add("message", "Fetching recids from: " + importurl
					+ " Using url: " + datasource);
			List<String> queryParts;

			// let's start with the updated records
			if (dictData.containsKey("UPDATED")) {
				queryParts = getQueryIds(maximport, dictData.get("UPDATED"));
				for (String queryPart : queryParts) {
					urlsToFetch.add(getFetchURL(importurl, datasource,
							queryPart, maximport));
				}
			}

			if (dictData.containsKey("ADDED")) {
				queryParts = getQueryIds(maximport, dictData.get("ADDED"));
				for (String queryPart : queryParts) {
					urlsToFetch.add(getFetchURL(importurl, datasource,
							queryPart, maximport));
				}
			}

			runAsyncUpload();
		} else {
			rsp.add("message", "Generating empty records");

			if (dictData.containsKey("ADDED")) {
				int[] recids = dictData.get("ADDED");
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
				CommitUpdateCommand updateCmd = new CommitUpdateCommand(params.getBool("commit", false));
				updateHandler.commit(updateCmd);
				rsp.add("added", recids.length);
			}
			setBusy(false);
		}



		long end = System.currentTimeMillis();

		rsp.add("status", isBusy() ? "busy" : "OK");
		rsp.add("QTime", end - start);
	}

	private void setBusy(boolean b) {
		busy = b;
	}

	private boolean isBusy() {
		return busy;
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

	private void runUpload() throws MalformedURLException, IOException,
			InterruptedException {
		while (urlsToFetch.size() > 0) {
			String url = urlsToFetch.remove(0);
			String html = IOUtils.toString(new URL(url).openStream());
			while (html.contains("busy")) {
				Thread.sleep(200);
			}
		}

	}

	private String getFetchURL(String importurl, String datasource,
			String queryPart, Integer maximport)
			throws UnsupportedEncodingException {
		return importurl
				+ "&url="
				+ java.net.URLEncoder.encode(
					datasource + "?p=" + queryPart + "&rm=" + maximport + "&of=xm",
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
				if (delta > maxspan) {
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
