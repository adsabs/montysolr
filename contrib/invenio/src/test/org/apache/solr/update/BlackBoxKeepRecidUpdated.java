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
import invenio.montysolr.util.MontySolrSetup;
import invenio.montysolr.util.MontySolrTestCaseJ4;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.junit.BeforeClass;

/**
 * This extensive suite verifies that we are able to pick changes
 * that are happening inside Invenio. We are not running any actual
 * indexing, but we do test whether the handlers *would* be called.
 * We do not change anything in the Invenio DB, not Solr index.
 * 
 * This test requires access to Invenio demo-site.
 * 
 */
public class BlackBoxKeepRecidUpdated extends MontySolrAbstractTestCase {
	
	private String importurl = "http://localhost:8983/solr/import-dataimport";
	private String updateurl = "http://localhost:8983/solr/update-dataimport&dirs=x";
	private String deleteurl = "http://localhost:8983/solr/delete-dataimport";
	private String inveniourl = "http://inspirebeta.net/search";
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", 
				MontySolrSetup.getMontySolrHome() + "/src/python");
		MontySolrSetup.addBuildProperties("contrib/invenio");
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() + "/contrib/invenio/src/python");
		MontySolrSetup.addTargetsToHandler("monty_invenio.targets");
		MontySolrSetup.addTargetsToHandler("monty_invenio.tests.demotest_updating");
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
		return MontySolrTestCaseJ4.EXAMPLE_HOME; 
		
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		PythonMessage message = MontySolrVM.INSTANCE.createMessage("reset_records");
		MontySolrVM.INSTANCE.sendMessage(message);
		
	}

	

	
	public void testIndexing() throws Exception {
		SolrCore core = h.getCore();
		MyInvenioKeepRecidUpdated handler = new MyInvenioKeepRecidUpdated();
		handler.setName("h-00");
		
		SolrQueryResponse rsp = new SolrQueryResponse();
		
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
		
		assertTrue(handler.lastRecId == -1); // cause we havent indexed them
		assertTrue(added.length == 104);
		assertTrue(updated.length == 0);
		assertTrue(deleted.length == 0); // this can work only for fresh demo
		
		
		
		
		
		// let's update every record (we are still in the fresh, reset state)
		// expected: added will be zero, but updates will have everything and 
		// the LAST will contain the last id of recids (as it is sorted by
		// modification date and by id then)
		
		int[] recids = new int[104];
		for (int i=0;i<104;i++) {
			recids[i] = i+1;
		}
		PythonMessage message = MontySolrVM.INSTANCE
			.createMessage("change_records")
			.setParam("recids", recids)
			.setParam("diff", 5);
		MontySolrVM.INSTANCE.sendMessage(message);
		
		
		handler = new MyInvenioKeepRecidUpdated();
		handler.setName("h-00-b");
		handler.skipStage(); // because there is no added rec
		core.execute(handler, req("last_recid", "-1", 
					    "inveniourl", inveniourl,
						"importurl", importurl,
						"updateurl", updateurl,
						"deleteurl", deleteurl), rsp);
		while (handler.isBusy()) {
			Thread.sleep(10);
		}
		
		added = handler.retrievedRecIds.get("ADDED");
		updated = handler.retrievedRecIds.get("UPDATED");
		deleted = handler.retrievedRecIds.get("DELETED");
		
		assertTrue(handler.lastRecId == -1);
		assertTrue(added.length == 0);
		assertTrue(updated.length == 104);
		assertTrue(deleted.length == 0); // will work on fresh demo
		assertTrue(handler.lastUpdatedRecId.equals(recids[recids.length-1]));
		
		
		
		// now test additions/changes/updates - we'll create 2 new
		// records; change 9 recs; delete one of the two new ones
		
		message = MontySolrVM.INSTANCE
			.createMessage("reset_records");
		MontySolrVM.INSTANCE.sendMessage(message);
		
		
		message = MontySolrVM.INSTANCE.createMessage("create_record")
		.setParam("diff", 5);
		MontySolrVM.INSTANCE.sendMessage(message);
		Integer firstAdded = (Integer) message.getResults();
		
		message = MontySolrVM.INSTANCE.createMessage("create_record")
			.setParam("diff", 5);
		MontySolrVM.INSTANCE.sendMessage(message);
		Integer secondAdded = (Integer) message.getResults();
		
		message = MontySolrVM.INSTANCE.createMessage("change_records")
		.setParam("recids", new int[]{1,2,3,4,5,6,7,8,9})
		.setParam("diff", 5);
		MontySolrVM.INSTANCE.sendMessage(message);
		
		message = MontySolrVM.INSTANCE.createMessage("delete_record")
		.setParam("recid", firstAdded)
		.setParam("diff", 7);
		MontySolrVM.INSTANCE.sendMessage(message);
		
		
		
		
		// expected: 1 added, 9 changed, 1 deleted AND lastid will 
		// be the deleted recs
		handler = new MyInvenioKeepRecidUpdated();
		handler.setName("h-00-c");
		core.execute(handler, req("last_recid", "10", 
					    "inveniourl", inveniourl,
						"importurl", importurl,
						"updateurl", updateurl,
						"deleteurl", deleteurl), rsp);
		while (handler.isBusy()) {
			Thread.sleep(10);
		}
		
		added = handler.retrievedRecIds.get("ADDED");
		updated = handler.retrievedRecIds.get("UPDATED");
		deleted = handler.retrievedRecIds.get("DELETED");
		
		assertTrue(handler.lastRecId == 10); // cause we use test class
		
		assertTrue(added.length == 1);
		assertTrue(updated.length == 9);
		assertTrue(deleted.length == 1);
		assertTrue(handler.lastUpdatedRecId.equals(firstAdded));
		assertTrue(handler.added == null); // these are there when processing runs
		
		
		
		
		
		
		
		// now we test the same situation, but using a different
		// ID as a reference point (we are forcing the updater
		// to use our last_recid, but int this situation the results
		// should be the same)
		
		// expected: added=1, updated=9, deleted=1
		
		handler = new MyInvenioKeepRecidUpdated();
		handler.setName("h-01");
		
		core.execute(handler, req("last_recid", "30", 
			    "inveniourl", inveniourl,
				"importurl", importurl,
				"updateurl", updateurl,
				"deleteurl", deleteurl), rsp);
		while (handler.isBusy()) {
			Thread.sleep(10);
		}
		
		added = handler.retrievedRecIds.get("ADDED");
		updated = handler.retrievedRecIds.get("UPDATED");
		deleted = handler.retrievedRecIds.get("DELETED");
		
		
		assertTrue(handler.lastRecId.equals(30));
		assertTrue(added.length == 1);
		assertTrue(updated.length == 9);
		assertTrue(deleted.length == 1);
		assertTrue(handler.lastUpdatedRecId.equals(firstAdded));
		assertTrue(handler.added == null); // these are there only when processing runs
		
		
		
		
		// internal processing (now we will index the docs
		// using the blankrecords -- meaning that the blank
		// records are created for each external document)
		
		// expected: added=1, updated=9, deleted=1
		//           LAST=lastDeleted
		//           index contains 9+1 documents
		
		
		handler = new MyInvenioKeepRecidUpdated();
		handler.setName("h-02");
		
		core.execute(handler, req("last_recid", "30", 
			    "inveniourl", inveniourl,
				"importurl", "blankrecords",
				"updateurl", "blankrecords",
				"deleteurl", "blankrecords",
				"commit", "true"), rsp);
		while (handler.isBusy()) {
			Thread.sleep(10);
		}
		
		added = handler.retrievedRecIds.get("ADDED");
		updated = handler.retrievedRecIds.get("UPDATED");
		deleted = handler.retrievedRecIds.get("DELETED");
		
		assertTrue(handler.lastRecId.equals(30));
		assertTrue(added.length == 1);
		assertTrue(updated.length == 9);
		assertTrue(deleted.length == 1);
		assertTrue(handler.lastUpdatedRecId.equals(firstAdded));
		assertTrue(handler.added != null); // these are there only when processing runs
		
		assertQ(req("q", "*:*"), "//*[@numFound='10']");
		
		
		
		// after this step the last update should be stored in 
		// a file, so if we run the handler again nothing should be 
		// found
		
		// expected: nothing new, but lastRecid contains correct value
		// (nothing, because we use mod_date now from the config)
		handler = new MyInvenioKeepRecidUpdated();
		handler.setName("h-03");
		
		core.execute(handler, req( 
			    "inveniourl", inveniourl,
				"importurl", "blankrecords",
				"updateurl", "blankrecords",
				"deleteurl", "blankrecords"
				), rsp);
		while (handler.isBusy()) {
			Thread.sleep(10);
		}
		assertTrue(handler.lastRecId == null);
		assertTrue(handler.added == null); // these are there only when processing runs
		
		
		
		// now we'll delete the second record (but not commit it to the index)
		
		// expected: deleted=1 & lastUpdatedRecid=secondAdded & lastRecId == null
		//           index still has 10 docs
		//Thread.sleep(500);
		message = MontySolrVM.INSTANCE.createMessage("delete_record")
			.setParam("recid", secondAdded)
			.setParam("diff", 10);
		MontySolrVM.INSTANCE.sendMessage(message);
		
		
		handler = new MyInvenioKeepRecidUpdated();
		handler.setName("h-04");
		
		core.execute(handler, req( 
			    "inveniourl", inveniourl,
				"importurl", "blankrecords",
				"updateurl", "blankrecords",
				"deleteurl", "blankrecords"), rsp);
		while (handler.isBusy()) {
			Thread.sleep(10);
		}
		
		added = handler.retrievedRecIds.get("ADDED");
		updated = handler.retrievedRecIds.get("UPDATED");
		deleted = handler.retrievedRecIds.get("DELETED");
		
		
		
		assertTrue(handler.lastRecId == null);
		assertTrue(added.length == 0);
		assertTrue(updated.length == 0);
		assertTrue(deleted.length == 1);
		assertTrue(handler.lastUpdatedRecId.equals(secondAdded));
		assertQ(req("q", "*:*"), "//*[@numFound='10']");

		
		
		// now commit the changes, even if nothing new 
		// was added, the index should not be updated cause
		// updater will give up after it realized there is nothing
		// new
		
		// extected: nothing is found, index is not changed
		//           lastRecid=null (retrieved by handler)
		
		handler = new MyInvenioKeepRecidUpdated();
		handler.setName("h-05");
		
		core.execute(handler, req(
			    "inveniourl", inveniourl,
				"importurl", "blankrecords",
				"updateurl", "blankrecords",
				"deleteurl", "blankrecords",
				"commit", "true"), rsp);
		while (handler.isBusy()) {
			Thread.sleep(10);
		}
		
		assertTrue(handler.lastRecId == null);
		assertTrue(handler.added == null); // these are there only when processing runs
		assertQ(req("q", "*:*"), "//*[@numFound='10']");
		
		
		
		// let's now repeat the same operation, but setting 
		// the last_recid=30, which should retrieve the changes
		// we have just made
		
		// expected: added=1, updated=9, deleted=2
		//           LAST=secondAdded
		//           index contains 9 docs
		
		handler = new MyInvenioKeepRecidUpdated();
		handler.setName("h-06");
		
		core.execute(handler, req("last_recid", "30", 
			    "inveniourl", inveniourl,
				"importurl", "blankrecords",
				"updateurl", "blankrecords",
				"deleteurl", "blankrecords",
				"commit", "true"), rsp);

		while (handler.isBusy()) {
			Thread.sleep(10);
		}
		
		added = handler.retrievedRecIds.get("ADDED");
		updated = handler.retrievedRecIds.get("UPDATED");
		deleted = handler.retrievedRecIds.get("DELETED");
		
		
		assertTrue(handler.lastRecId.equals(30));
		assertTrue(added.length == 0);
		assertTrue(updated.length == 9);
		assertTrue(deleted.length == 2);
		assertTrue(handler.lastUpdatedRecId.equals(secondAdded));
		assertQ(req("q", "*:*"), "//*[@numFound='9']");
		
		
		
		
		
		
		
		// now create a third new record and run the updater
		
		// expected: added=1, the rest empty
		//           LAST=thirdAdded
		//           index contains 9+1 docs
		
		message = MontySolrVM.INSTANCE.createMessage("create_record")
			.setParam("diff", 15);
		MontySolrVM.INSTANCE.sendMessage(message);
		Integer thirdAdded = (Integer) message.getResults();
		
		handler = new MyInvenioKeepRecidUpdated();
		handler.setName("h-07");
		
		core.execute(handler, req( 
			    "inveniourl", inveniourl,
				"importurl", "blankrecords",
				"updateurl", "blankrecords",
				"deleteurl", "blankrecords",
				"commit", "true"), rsp);
		
		while (handler.isBusy()) {
			Thread.sleep(10);
		}
		
		added = handler.retrievedRecIds.get("ADDED");
		updated = handler.retrievedRecIds.get("UPDATED");
		deleted = handler.retrievedRecIds.get("DELETED");
		
		
		assertTrue(added.length == 1);
		assertTrue(updated.length == 0);
		assertTrue(deleted.length == 0);
		assertTrue(handler.lastUpdatedRecId.equals(thirdAdded));
		assertQ(req("q", "*:*"), "//*[@numFound='10']");

		
		
		
		
		
		
		// and delete the new record again (but must set the 
		// modification date after the record -- unfortunately,
		// invenio has very low granularity, understands only 
		// changes in intervals of seconds)
		
		
		message = MontySolrVM.INSTANCE.createMessage("delete_record")
			.setParam("recid", thirdAdded)
			.setParam("diff", 100);
		MontySolrVM.INSTANCE.sendMessage(message);
		
		
		// expected: added=0, updated=0, deleted=1
		//           LAST=thirdAdded & lastRecid=null
		//           index has 9 docs
		
		handler = new MyInvenioKeepRecidUpdated();
		handler.setName("h-08");
		
		core.execute(handler, req( 
			    "inveniourl", inveniourl,
				"importurl", "blankrecords",
				"updateurl", "blankrecords",
				"deleteurl", "blankrecords",
				"commit", "true"), rsp);

		while (handler.isBusy()) {
			Thread.sleep(10);
		}
		
		added = handler.retrievedRecIds.get("ADDED");
		updated = handler.retrievedRecIds.get("UPDATED");
		deleted = handler.retrievedRecIds.get("DELETED");
		
		
		assertTrue(handler.lastRecId == null);
		assertTrue(handler.lastUpdatedRecId.equals(thirdAdded));
		assertTrue(added.length == 0);
		assertTrue(updated.length == 0);
		assertTrue(deleted.length == 1);
		assertQ(req("q", "*:*"), "//*[@numFound='9']");
		
		
		
		
		// clean up after, delete the added recs
		
		message = MontySolrVM.INSTANCE.createMessage("wipeout_record")
			.setParam("recid", firstAdded);
		MontySolrVM.INSTANCE.sendMessage(message);
		
		message.setParam("recid", secondAdded);
		MontySolrVM.INSTANCE.sendMessage(message);
		
		message.setParam("recid", thirdAdded);
		MontySolrVM.INSTANCE.sendMessage(message);
	}
	
	
	public class MyInvenioKeepRecidUpdated extends InvenioKeepRecidUpdated {
		
		public Map<String, int[]> retrievedRecIds;
		public Integer lastRecId;
		public Integer lastUpdatedRecId;
		private int stage = 0;
		private String[] stages = new String[]{importurl, updateurl, deleteurl};
		public int[] added = null;
		public int[] updated = null;
		public int[] deleted = null;
		public String tName = null;
		
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
