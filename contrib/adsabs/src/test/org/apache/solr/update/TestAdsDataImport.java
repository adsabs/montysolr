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

import org.adsabs.mongodb.MongoConnection;
import org.apache.lucene.queryParser.standard.processors.LowercaseExpandedTermsQueryNodeProcessor;
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
	

	public void tearDown() throws Exception {
		MongoConnection.INSTANCE.close();
		super.tearDown();
	}
	
	public void testImport() throws Exception {
		
		
		String testDir = MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/";
		
		SolrRequestHandler handler = h.getCore().getRequestHandler("/invenio/import");
		
		SolrCore core = h.getCore();
		
		String url = "file://" + MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/ads-demo-records.xml";
		
		SolrQueryRequest req = req("command", "full-import",
				"dirs", testDir,
				"commit", "true",
				"url", url
				);
		SolrQueryResponse rsp = new SolrQueryResponse();
		
		core.execute(handler, req, rsp);
		
		commit("waitFlush", "true", "waitSearcher", "true");
		
		DirectSolrConnection direct = getDirectServer();
		
		
		/*
		 * For the reference resolver, the field which contains only the last
		 * name of the first author 
		 */
		
		assertQ(req("q", "first_author_surname:cutri"), "//*[@numFound='1']");
		assertQ(req("q", "first_author_surname:Cutri"), "//*[@numFound='1']");
		assertQ(req("q", "first_author_surname:\"Cutri,\""), "//*[@numFound='1']");
		assertQ(req("q", "first_author_surname:\"Cutri,R\""), "//*[@numFound='1']");
		assertQ(req("q", "first_author_surname:\"CUTRI\""), "//*[@numFound='1']");
		assertQ(req("q", "author_norm:\"tenenbaum, p\""), "//*[@numFound='1']");
		assertQ(req("q", "author_norm:\"mosser, b\""), "//*[@numFound='1']");
		assertQ(req("q", "author_facet:\"Tenenbaum, P\""), "//*[@numFound='1']");
		assertQ(req("q", "author_facet:\"Mosser, B\""), "//*[@numFound='1']");
		assertQ(req("q", "page:2056 AND recid:9106451"), "//*[@numFound='1']");
		assertQ(req("q", "volume:l219"), "//*[@numFound='1']");
		assertQ(req("q", "volume:L219"), "//*[@numFound='1']");
		assertQ(req("q", "issue:4"), "//*[@numFound='1']");
		assertQ(req("q", "aff:nasa"), "//*[@numFound='1']");
		assertQ(req("q", "aff:KAVLI"), "//*[@numFound='0']");
		assertQ(req("q", "aff:kavli"), "//*[@numFound='1']");
		assertQ(req("q", "email:rcutri@example.edu"), "//*[@numFound='1']");
		assertQ(req("q", "bibstem:ycat"), "//*[@numFound='4']");
		assertQ(req("q", "bibstem:stat.conf"), "//*[@numFound='1']");

		
		/*
		 * These aren't working as of yet, possibly due to the data import
		 * not supporting the xpath syntax being used in data-config.xml for these fields
		 */
//		assertQ(req("q", "database:astronomy"), "//*[@numFound='5']");
//		assertQ(req("q", "bibgroup:cfa"), "//*[@numFound='2']");
		
		
		/*
		 * Bibcodes
		 */
		
		assertQ(req("q", "bibcode:2012yCat..35409143M"), "//*[@numFound='1']");
		assertQ(req("q", "bibcode:2012ycat..35409143m"), "//*[@numFound='1']");
		assertQ(req("q", "bibcode:2012YCAT..35409143M"), "//*[@numFound='1']");
		assertQ(req("q", "bibcode:2012YCAT..*", "debugQuery", "true"), "//*[@numFound='4']");
		assertQ(req("q", "bibcode:201?YCAT..35409143M"), "//*[@numFound='1']");
		assertQ(req("q", "bibcode:*YCAT..35409143M"), "//*[@numFound='1']");
		
		
		/*
		 * Bibstem is derived from bibcode, it is either the bibcode[4:9] OR
		 * bibcode[4:13] when the volume information is NOT present
		 * 
		 * So this bibcode: 2012yCat..35a09143M
		 * has bibstem:     yCat..35a
		 * 
		 * But this bicode: 2012yCat..35009143M
		 * has bibstem:     yCat
		 * 
		 * Bibstem is case sensitive (at least for now)
		 * 
		 * However! This also means that search for bibstem:yCat
		 * will not find the first record (!!)
		 * 
		 */
		assertQ(req("q", "bibstem:YCAT"), "//*[@numFound='0']");
		assertQ(req("q", "bibstem:yCat"), "//*[@numFound='4']");
		assertQ(req("q", "bibstem:yCat..35a"), "//*[@numFound='1']");
		
		
		// this works because we use the solr analyzer when available
		// (not the default action which is to lowercase wildcards)
		assertQ(req("q", "bibstem:yCat..*", "debugQuery", "true"), "//*[@numFound='1']");
		
		//System.out.println(direct.request("/select?q=*:*&fl=bibcode,recid,title", null).replace("</", "\n</"));
	}
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAdsDataImport.class);
    }
}
