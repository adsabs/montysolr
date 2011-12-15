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

package org.apache.solr.update;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.util.MontySolrAbstractTestCase;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.search.DocSlice;
import org.apache.solr.update.TestInvenioKeepRecidUpdated.MyInvenioKeepRecidUpdated;

/**
 * Most of the tests for StandardRequestHandler are in ConvertedLegacyTest
 * 
 */
public class TestInvenioKeepRecidUpdated extends MontySolrAbstractTestCase {
	
	//TODO: dynamically retrieve these values
	private String importurl = "http://localhost:8983/solr/import-dataimport";
	private String updateurl = "http://localhost:8983/solr/update-dataimport&dirs=x";
	private String deleteurl = "http://localhost:8983/solr/delete-dataimport";
	private String inveniourl = "http://insdev01.cern.ch/search";
	
	
	@Override
	public String getSchemaFile() {
		return getMontySolrHome()
		+ "/contrib/invenio/src/test-files/solr/conf/schema-invenio-keeprecid-updater.xml";
	}

	@Override
	public String getSolrConfigFile() {
		return getMontySolrHome()
				+ "/contrib/invenio/src/test-files/solr/conf/solrconfig-invenio-keeprecid-updater.xml";
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		addToSysPath(getMontySolrHome() + "/contrib/invenio/src/python");
		
		addTargetsToHandler("monty_invenio.targets");
		addTargetsToHandler("monty_invenio.tests.unittest_updating");
		
		PythonMessage message = MontySolrVM.INSTANCE.createMessage("reset_records");
		MontySolrVM.INSTANCE.sendMessage(message);
		
	}

	
	@Override
	public String getModuleName() {
		return "montysolr.java_bridge.SimpleBridge";
	}

	
	public void testIndexing() throws Exception {
		SolrCore core = h.getCore();
		MyInvenioKeepRecidUpdated handler = new MyInvenioKeepRecidUpdated();
		SolrQueryResponse rsp = new SolrQueryResponse();
		
		int[] recids = new int[104];
		for (int i=0;i<104;i++) {
			recids[i] = i+1;
		}
		
		
		// fresh state, everything reset
		core.execute(handler, req("last_recid", "-1", 
					    "inveniourl", inveniourl,
						"importurl", importurl,
						"updateurl", updateurl,
						"deleteurl", deleteurl), rsp);
	
		// must wait for the landler to finish his threads
		while (handler.isBusy()) {
			Thread.sleep(100);
		}
		
		int[] added = handler.retrievedRecIds.get("ADDED");
		int[] updated = handler.retrievedRecIds.get("UPDATED");
		int[] deleted = handler.retrievedRecIds.get("DELETED");
		
		assertTrue(handler.lastRecId == -1);
		assertTrue(added.length > 104);
	
	
	
		// changes made
	
		PythonMessage message = MontySolrVM.INSTANCE
			.createMessage("change_records")
			.setParam("recids", recids)
			.setParam("diff", 5);
		MontySolrVM.INSTANCE.sendMessage(message);
		
		
		core.execute(handler, req("last_recid", "-1", 
					    "inveniourl", inveniourl,
						"importurl", importurl,
						"updateurl", updateurl,
						"deleteurl", deleteurl), rsp);

		// must wait for the landler to finish his threads
		while (handler.isBusy()) {
			Thread.sleep(100);
		}
		
		added = handler.retrievedRecIds.get("ADDED");
		updated = handler.retrievedRecIds.get("UPDATED");
		deleted = handler.retrievedRecIds.get("DELETED");
		
		assertTrue(handler.lastRecId == -1);
		assertTrue(added.length == 0);
		assertTrue(updated.length == 104);
		assertTrue(deleted.length == 0);
		
		
		
		
		// now test additions/changes/updates
		
		message = MontySolrVM.INSTANCE
			.createMessage("reset_records");
		MontySolrVM.INSTANCE.sendMessage(message);
		
		
		message = MontySolrVM.INSTANCE.createMessage("create_record")
		.setParam("diff", 5);
		MontySolrVM.INSTANCE.sendMessage(message);
		Integer firstAdded = (Integer) message.getResults();
		
		message = MontySolrVM.INSTANCE.createMessage("create_record")
			.setParam("diff", 5);;
		MontySolrVM.INSTANCE.sendMessage(message);
		Integer secondAdded = (Integer) message.getResults();
		
		message = MontySolrVM.INSTANCE.createMessage("change_records")
		.setParam("recids", new int[]{1,2,3,4,5,6,7,8,9})
		.setParam("diff", 5);;
		MontySolrVM.INSTANCE.sendMessage(message);
		
		message = MontySolrVM.INSTANCE.createMessage("delete_record")
		.setParam("recid", firstAdded)
		.setParam("diff", 5);;
		MontySolrVM.INSTANCE.sendMessage(message);
		
		// extra query needed as invenio sets modification date (that will be lower than creation)
		message = MontySolrVM.INSTANCE.createMessage("change_records")
		.setParam("recids", new int[]{firstAdded})
		.setParam("diff", 5);;
		MontySolrVM.INSTANCE.sendMessage(message);
		
		
		core.execute(handler, req("last_recid", "-1", 
					    "inveniourl", inveniourl,
						"importurl", importurl,
						"updateurl", updateurl,
						"deleteurl", deleteurl), rsp);

		// must wait for the landler to finish his threads
		while (handler.isBusy()) {
			Thread.sleep(100);
		}
		
		added = handler.retrievedRecIds.get("ADDED");
		updated = handler.retrievedRecIds.get("UPDATED");
		deleted = handler.retrievedRecIds.get("DELETED");
		
		List<String> urlsToFetch = handler.urlsToFetch;
		
		assertTrue(handler.lastRecId == -1);
		
		assertTrue(added.length == 1);
		assertTrue(updated.length == 9);
		assertTrue(deleted.length == 1);
		assertTrue(handler.added == null);
		
		
		
		// different starting point
		
		handler = new MyInvenioKeepRecidUpdated();
		core.execute(handler, req("last_recid", "30", 
			    "inveniourl", inveniourl,
				"importurl", importurl,
				"updateurl", updateurl,
				"deleteurl", deleteurl), rsp);

		while (handler.isBusy()) {
			Thread.sleep(100);
		}
		
		added = handler.retrievedRecIds.get("ADDED");
		updated = handler.retrievedRecIds.get("UPDATED");
		deleted = handler.retrievedRecIds.get("DELETED");
		
		urlsToFetch = handler.urlsToFetch;
		
		assertTrue(handler.lastRecId == 30);
		
		assertTrue(added.length == 1);
		assertTrue(updated.length == 9);
		assertTrue(deleted.length == 1);
		assertTrue(handler.added == null);
		
		
		// internal processing (now with index)
		
		
		handler = new MyInvenioKeepRecidUpdated();
		core.execute(handler, req("last_recid", "30", 
			    "inveniourl", inveniourl,
				"importurl", "blankrecords",
				"updateurl", "blankrecords",
				"deleteurl", "blankrecords",
				"commit", "true"), rsp);

		while (handler.isBusy()) {
			Thread.sleep(100);
		}
		
		added = handler.retrievedRecIds.get("ADDED");
		updated = handler.retrievedRecIds.get("UPDATED");
		deleted = handler.retrievedRecIds.get("DELETED");
		
		assertTrue(handler.lastRecId == 30);
		assertTrue(added.length == 1);
		assertTrue(updated.length == 9);
		assertTrue(deleted.length == 1);
		
		assertQ(req("q", "*:*"), "//*[@numFound='10']");
		
		
		
		
		// delete one record (but not commit)
		
		handler = new MyInvenioKeepRecidUpdated();
		core.execute(handler, req("last_recid", "30", 
			    "inveniourl", inveniourl,
				"importurl", "blankrecords",
				"updateurl", "blankrecords",
				"deleteurl", "blankrecords"), rsp);

		while (handler.isBusy()) {
			Thread.sleep(100);
		}
		
		message = MontySolrVM.INSTANCE.createMessage("delete_record")
			.setParam("recid", secondAdded)
			.setParam("diff", 5);
		MontySolrVM.INSTANCE.sendMessage(message);
		
		// extra query needed as invenio sets modification date (that will be lower than creation)
		message = MontySolrVM.INSTANCE.createMessage("change_records")
		.setParam("recids", new int[]{firstAdded})
		.setParam("diff", 5);;
		MontySolrVM.INSTANCE.sendMessage(message);
		
		assertQ(req("q", "*:*"), "//*[@numFound='10']");
		
		
		// now commit
		
		handler = new MyInvenioKeepRecidUpdated();
		core.execute(handler, req("last_recid", "30", 
			    "inveniourl", inveniourl,
				"importurl", "barerecords",
				"updateurl", "barerecords",
				"deleteurl", "barerecords",
				"commit", "true"), rsp);

		while (handler.isBusy()) {
			Thread.sleep(100);
		}
		
		added = handler.retrievedRecIds.get("ADDED");
		updated = handler.retrievedRecIds.get("UPDATED");
		deleted = handler.retrievedRecIds.get("DELETED");
		
		assertTrue(handler.lastRecId == 30);
		assertTrue(added.length == 1);
		assertTrue(updated.length == 9);
		assertTrue(deleted.length == 1);
		
		assertQ(req("q", "*:*"), "//*[@numFound='9']");
		
		
		// dataimport processing
		
		
		handler = new MyInvenioKeepRecidUpdated();
		core.execute(handler, req("last_recid", "30", 
			    "inveniourl", inveniourl,
				"importurl", "dataimport",
				"updateurl", "dataimport",
				"deleteurl", "dataimport"), rsp);

		while (handler.isBusy()) {
			Thread.sleep(100);
		}
		
		added = handler.retrievedRecIds.get("ADDED");
		updated = handler.retrievedRecIds.get("UPDATED");
		deleted = handler.retrievedRecIds.get("DELETED");
		
		urlsToFetch = handler.urlsToFetch;
		
		assertTrue(handler.lastRecId == 30);
		
		assertTrue(added.length == 1);
		assertTrue(updated.length == 9);
		assertTrue(deleted.length == 1);
		

		
		// clean up after us
		message = MontySolrVM.INSTANCE.createMessage("wipeout_record")
			.setParam("recid", firstAdded);
		MontySolrVM.INSTANCE.sendMessage(message);
		
		message.setParam("recid", secondAdded);
		MontySolrVM.INSTANCE.sendMessage(message);
	}
	
	
	public class MyInvenioKeepRecidUpdated extends InvenioKeepRecidUpdated {
		
		public Map<String, int[]> retrievedRecIds;
		public int lastRecId;
		private int stage = 0;
		private String[] stages = new String[]{importurl + "?", updateurl + "&", deleteurl};
		public int[] added = null;
		public int[] updated = null;
		public int[] deleted = null;
		
		
		@Override
		protected int getLastRecid(SolrParams params, SolrQueryRequest req) throws IOException {
		     lastRecId = super.getLastRecid(params, req);
		     return lastRecId;
		}
		
		@Override
		protected Map<String, int[]> retrieveRecids(int lastRecid, SolrParams params,
		        SolrQueryRequest req, SolrQueryResponse rsp) {
			
		    retrievedRecIds = super.retrieveRecids(lastRecid, params, req, rsp);
		    return retrievedRecIds;
		}
		
		@Override
		protected void runUpload() throws MalformedURLException, IOException, InterruptedException {
			while (urlsToFetch.size() > 0) {
				String url = urlsToFetch.remove(0);
				assert url.contains(java.net.URLEncoder.encode(inveniourl, "UTF-8"));
				assert url.contains(stages[stage]);
			}
			stage++;
		}
		
		@Override
		protected void runProcessingAdded(int[] recids, SolrParams params) throws IOException {
			added = recids;
			super.runProcessingAdded(recids, params);
		}
		
		@Override
		protected void runProcessingUpdated(int[] recids, SolrParams params) throws IOException {
			updated = recids;
			super.runProcessingUpdated(recids, params);
		}
		@Override
		protected void runProcessingDeleted(int[] recids, SolrParams params) throws IOException {
			deleted = recids;
			super.runProcessingDeleted(recids, params);
		}
	}
}
