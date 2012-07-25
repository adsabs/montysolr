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

import montysolr.util.MontySolrSetup;

import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.dataimport.WaitingDataImportHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.util.AbstractSolrTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Tests that the dataimport handler does really wait and does not
 * return immediately. Also, one of the fields is fetched by Python.
 * 
 */
public class TestWaitingDataimportHandler extends AbstractSolrTestCase {
	
	
  @BeforeClass
  public static void beforeTestWaitingDataimportHandler() throws Exception {
    System.setProperty("montysolr.home", MontySolrSetup.getMontySolrHome());
  }
  
  @AfterClass
  public static void afterTestWaitingDataimportHandler() throws Exception {
    System.clearProperty("montysolr.home");
  }
  
	public String getSchemaFile() {
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/invenio/src/test-files/solr/collection1/conf/schema-minimal.xml";
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/invenio/src/test-files/solr/collection1/conf/solrconfig-invenio-keeprecid-updater.xml";
	}

	public String getSolrHome() {
	  System.clearProperty("solr.solr.home"); //always force field re-computation
		return MontySolrSetup.getSolrHome() + "/example/solr";
	}
	

	
	public void testImport() throws Exception {
		
		
		String testDir = MontySolrSetup.getMontySolrHome() + "/src/test-files/data/";
		
		WaitingDataImportHandler handler = (WaitingDataImportHandler) h.getCore().getRequestHandler("/waiting-dataimport");
		
		SolrCore core = h.getCore();
		
		SolrQueryRequest req = req("command", "full-import",
				"dirs", testDir,
				"commit", "true",
				"url", "file://" + MontySolrSetup.getMontySolrHome() + "/contrib/invenio/src/test-files/data/demo-site.xml"
				);
		SolrQueryResponse rsp = new SolrQueryResponse();
		
		core.execute(handler, req, rsp);
		
		assertU(commit());
		//commit("waitFlush", "true", "waitSearcher", "true");
		
		
		assertQ(req("q", "*:*"), "//*[@numFound='104']");
		assertQ(req("q", "id:53"), "//*[@numFound='1']");
		assertQ(req("q", "id:54"), "//*[@numFound='1']");
		assertQ(req("q", "id:55"), "//*[@numFound='1']");
		assertQ(req("q", "abstract:\"Higgs boson\""), "//*[@numFound='1']");
		assertQ(req("q", "author:photolab"), "//*[@numFound='1']");
		
		assertQ(req("q", "id:100"), "//*[@numFound='1']");
		
		
		
	}
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestWaitingDataimportHandler.class);
    }
}
