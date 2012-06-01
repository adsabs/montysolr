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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.util.MontySolrAbstractTestCase;
import invenio.montysolr.util.MontySolrSetup;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.dataimport.WaitingDataImportHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.BlackBoxKeepRecidUpdated.MyInvenioKeepRecidUpdated;
import org.junit.BeforeClass;

/**
 * Tests that the dataimport handler does really wait and does not
 * return immediately. Also, one of the fields is fetched by Python.
 * 
 */
public class TestInvenioKeepRecidUpdated extends MontySolrAbstractTestCase {
	
	private String importurl = "http://localhost:8983/solr/import-dataimport";
	private String updateurl = "http://localhost:8983/solr/update-dataimport&dirs=x";
	private String deleteurl = "http://localhost:8983/solr/delete-dataimport";
	private String inveniourl = "http://inspirebeta.net/search";
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		envInit();
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() 
				+ "/contrib/invenio/src/python");
		MontySolrSetup.addTargetsToHandler("monty_invenio.tests.fake_import");
	}
	
	@Override
	public String getSchemaFile() {
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/invenio/src/test-files/solr/conf/schema-invenio-keeprecid-updater.xml";
	}

	@Override
	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/invenio/src/test-files/solr/conf/solrconfig-invenio-keeprecid-updater.xml";
	}

	public String getSolrHome() {
		return MontySolrSetup.getSolrHome() + "/example/solr";
	}
	
	

	
	public void testImport() throws InterruptedException {
		
		
		SolrCore core = h.getCore();
		
		InvenioKeepRecidUpdated lazy = (InvenioKeepRecidUpdated) core.getRequestHandler("invenio_update.mock");
		assertEquals("get_changed_recids_mock", lazy.getPythonFunctionName());
		
		MockInvenioKeepRecidUpdated handler = new MockInvenioKeepRecidUpdated();
		handler.setPythonFunctionName("get_changed_recids_mock");
		handler.setName("h-00");
		
		SolrQueryResponse rsp = new SolrQueryResponse();
		
		PythonMessage message = MontySolrVM.INSTANCE
			.createMessage("set_changed_recids_mock")
			.setParam("ADDED", "1,2,3")
			.setParam("UPDATED", "4,6")
			.setParam("DELETED", "5")
			.setParam("last_recid", "-1")
			.setParam("mod_date", "xxxx");
		MontySolrVM.INSTANCE.sendMessage(message);
		
		// fresh state, everything reset - we are not committing anything
		// and no upload is done (but we'll check the correct urls were
		// generated)
		
		
		// extected: everything is retrieved as new
		
		core.execute(handler, req("last_recid", "-1", 
					    "inveniourl", inveniourl,
						"importurl", importurl,
						"updateurl", updateurl,
						"deleteurl", deleteurl), rsp);
	
		// must wait for the landler to finish his threads
		while (handler.isBusy()) {
			Thread.sleep(10);
		}
		
		int[] added = handler.retrievedRecIds.get("ADDED");
		int[] updated = handler.retrievedRecIds.get("UPDATED");
		int[] deleted = handler.retrievedRecIds.get("DELETED");
		
		assertTrue(handler.lastRecId == -1); 
		assertTrue(added.length == 3);
		assertTrue(updated.length == 2);
		assertTrue(deleted.length == 1);
		
		
		rsp = new SolrQueryResponse();
		SolrQueryResponse rsp2 = new SolrQueryResponse();
		
		handler.testUpload = false;  // needed to avoid clashing out of bound index
		handler.setName("h-01");
		core.execute(handler, req("last_recid", "-1", 
			    "inveniourl", inveniourl,
				"importurl", importurl,
				"updateurl", updateurl,
				"deleteurl", deleteurl,
				"idtoken", "#2"), rsp);
		
		
		handler.setName("h-02");
		core.execute(handler, req("last_recid", "-1", 
			    "inveniourl", inveniourl,
				"importurl", importurl,
				"updateurl", updateurl,
				"deleteurl", deleteurl,
				"idtoken", "#3"), rsp2);
		
		
		
		handler.testUpload = false;
		
		assertTrue(rsp.getValues().get("importStatus").equals("busy"));
		assertTrue(rsp2.getValues().get("importStatus").equals("busy"));
		assertTrue( rsp.getValues().get("idtoken").equals("#2"));
		assertTrue( !rsp2.getValues().get("idtoken").equals("#3"));
		
		while (handler.isBusy()) {
			Thread.sleep(10);
		}
	}
	
	
	public class MockInvenioKeepRecidUpdated extends InvenioKeepRecidUpdated {
		
		public Map<String, int[]> retrievedRecIds;
		public Integer lastRecId;
		public Integer lastUpdatedRecId;
		private int stage = 0;
		private String[] stages = new String[]{importurl, updateurl, deleteurl};
		public int[] added = null;
		public int[] updated = null;
		public int[] deleted = null;
		public String tName = null;
		public boolean testUpload = true;
		
		public void setName(String name) {
			tName = name;
		}
		public void skipStage() {
			stage++;
		}
		
		@SuppressWarnings("unchecked")
		protected Map<String, Object> retrieveRecids(Properties prop, 
				SolrQueryRequest req, SolrQueryResponse rsp) {
			
			if (prop.containsKey("last_recid")) {
				lastRecId = Integer.valueOf(prop.getProperty("last_recid"));
			}
			
		    Map<String, Object> ret = super.retrieveRecids(prop, req, rsp);
		    if (ret != null) {
		    	retrievedRecIds = (Map<String, int[]>) ret.get("dictData");
		    	lastUpdatedRecId = (Integer) ret.get("last_recid");
		    }
		    return ret;
		}
		
		@Override
		protected void runUpload(List<String> urlsToFetch) throws MalformedURLException, IOException, InterruptedException {
			if (!testUpload) {
				return;
			}
			while (urlsToFetch.size() > 0) {
				String url = urlsToFetch.remove(0);
				assertTrue("Name: " + tName + "\nStage: " + stage + "\nUrl: " + url + "\nDoest not contain: " + inveniourl,
						url.contains(java.net.URLEncoder.encode(inveniourl, "UTF-8")));
				assertTrue("Name: " + tName + "\nStage: " + stage + "\nUrl: " + url + "\nDoest not contain: " + stages[stage],
						url.contains(stages[stage]));
				urlsToFetch.clear(); // we want just once
			}
			stage++;
		}
		
		@Override
		protected void runProcessingAdded(int[] recids, SolrQueryRequest req) throws IOException {
			added = recids;
			super.runProcessingAdded(recids, req);
		}
		@Override
		protected void runProcessingDeleted(int[] recids, SolrQueryRequest req) throws IOException {
			deleted = recids;
			super.runProcessingDeleted(recids, req);
		}
		@Override
		protected void runProcessingUpdated(int[] recids, SolrQueryRequest req) throws IOException {
			updated = recids;
			super.runProcessingUpdated(recids, req);
		}
		
	}
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(BlackBoxKeepRecidUpdated.class);
    }
}
