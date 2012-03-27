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

import invenio.montysolr.util.MontySolrAbstractTestCase;
import invenio.montysolr.util.MontySolrSetup;


import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.dataimport.WaitingDataImportHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.junit.BeforeClass;

/**
 * Most of the tests for StandardRequestHandler are in ConvertedLegacyTest
 * 
 */
public class TestWaitingDataimportHandler extends MontySolrAbstractTestCase {
	
	private String inveniourl = "http://inspirehep.net/search";
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", 
				MontySolrSetup.getMontySolrHome() + "/src/python");
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() + "/contrib/invenio/src/python");
		MontySolrSetup.addTargetsToHandler("monty_invenio.schema.targets");
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


	

	private String esc(String...params) {
		StringBuffer out = new StringBuffer();
		assert params.length % 2 == 0;
		out.append(inveniourl);
		out.append("?");
		for (int i=0;i<params.length;i=i+2) {
			out.append(params[i]);
			out.append("=");
			out.append(java.net.URLEncoder.encode(params[i+1]));
			out.append("&");
		}
		return out.toString();
	}
	
	public void testImport() throws InterruptedException {
		
		// TODO: this test belongs to the contrib/examples and requires 
		// running web service
		if (true) {
			return;
		}
		
		String testDir = MontySolrSetup.getMontySolrHome() + "/src/test-files/data/";
		
		indexDoc("/waiting-dataimport", 
				req("command", "full-import",
					"dirs", testDir,
					"commit", "true",
					"url", esc("p", "recid:53->55 OR recid:100", 
							"rg", "200", "of", "xm")
							
					));
		
		assertQ(req("q", "recid:53"), "//*[@numFound='1']");
		assertQ(req("q", "recid:54"), "//*[@numFound='1']");
		assertQ(req("q", "recid:55"), "//*[@numFound='1']");
		assertQ(req("q", "title:annihilation"), "//*[@numFound='1']");
		assertQ(req("q", "author:Bander"), "//*[@numFound='1']");
		
		assertQ(req("q", "recid:100"), "//*[@numFound='1']");
		
		
		
	}
	
	public SolrQueryResponse indexDoc(SolrQueryRequest req) throws InterruptedException {
		SolrCore core = h.getCore();
		WaitingDataImportHandler handler = new WaitingDataImportHandler();
		handler.inform(core);
		return indexDoc(handler, req);
	}
	
	public SolrQueryResponse indexDoc(String handlerName, SolrQueryRequest req) throws InterruptedException {
		SolrCore core = h.getCore();
		SolrRequestHandler handler = core.getRequestHandler(handlerName);
		return indexDoc(handler, req);
		
	}
	
	private SolrQueryResponse indexDoc(SolrRequestHandler handler, SolrQueryRequest req) {
		SolrCore core = req.getCore();
		SolrQueryResponse rsp = new SolrQueryResponse();
		
		core.execute(handler, req, rsp);
		// we must give handler time to do the indexing
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		/*
		// must wait for the landler to finish his threads
		WaitingDataImportHandler wih = (WaitingDataImportHandler) handler;
		while (wih.isBusy()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		*/
		return rsp;
	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestWaitingDataimportHandler.class);
    }
}
