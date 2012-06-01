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
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.servlet.DirectSolrConnection;
import org.junit.BeforeClass;

/**
 * Tests that the dataimport handler does really wait and does not
 * return immediately. Also, one of the fields is fetched by Python.
 * 
 */
public class TestAdsDataImport extends MontySolrAbstractTestCase {
	
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		envInit();
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() 
				+ "/contrib/invenio/src/python");
		MontySolrSetup.addTargetsToHandler("monty_invenio.schema.tests.targets");
	}
	
	@Override
	public String getSchemaFile() {
		makeResourcesVisible(this.solrConfig.getResourceLoader(),
	    		new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/conf",
	    				      MontySolrSetup.getSolrHome() + "/example/solr/conf"
	    	});
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/examples/adsabs/solr/conf/schema.xml";
	}

	@Override
	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/examples/adsabs/solr/conf/solrconfig.xml";
	}

	public String getSolrHome() {
		return MontySolrSetup.getSolrHome() + "/example/solr";
	}
	

	
	public void testImport() throws Exception {
		
		
		String testDir = MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/";
		
		SolrRequestHandler handler = h.getCore().getRequestHandler("/invenio/import");
		
		SolrCore core = h.getCore();
		
		SolrQueryRequest req = req("command", "full-import",
				"dirs", testDir,
				"commit", "true",
				"url", "file://" + MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/ads-demo-records.xml"
				);
		SolrQueryResponse rsp = new SolrQueryResponse();
		
		core.execute(handler, req, rsp);
		
		commit("waitFlush", "true", "waitSearcher", "true");
		
		DirectSolrConnection direct = getDirectServer();
		System.out.println(direct.request("/select?q=*:*", null).replace("</", "\n</"));
		//assertQ(req("q", "*:*"), "//*[@numFound='1']");
		
	}
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAdsDataImport.class);
    }
}
