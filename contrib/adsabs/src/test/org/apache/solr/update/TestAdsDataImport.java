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
		System.setProperty("solr.directoryFactory","solr.SimpleFSDirectoryFactory");
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
		
		assertQ(req("q", "author:\"Albert, R\""), "//*[@numFound='1']");
		assertQ(req("q", "author:\"Albert, Reeka\""), "//*[@numFound='1']");
		assertQ(req("q", "author:\"Barabási, A\""), "//*[@numFound='1']");
		assertQ(req("q", "author:\"Barabaesi, A\""), "//*[@numFound='1']");
		assertQ(req("q", "author:\"Barabási, Albert-László\""), "//*[@numFound='1']");
		assertQ(req("q", "author:\"Barabasi, Albert-Laszlo\""), "//*[@numFound='1']");
		assertQ(req("q", "author:Sellgren"), "//*[@numFound='1']");
		assertQ(req("q", "author:\"Dwek, E P\""), "//*[@numFound='1']");
		assertQ(req("q", "author:\"Dwek, E.\""), "//*[@numFound='1']");
		assertQ(req("q", "author:\"Dwek, Edgar\""), "//*[@numFound='1']");
		assertQ(req("q", "author:\"Dwek, E. P.\""), "//*[@numFound='1']");
		
		assertQ(req("q", "aff:46556"), "//*[@numFound='1']");
		assertQ(req("q", "aff:\"Notre Dame\""), "//*[@numFound='1']");
		
		assertQ(req("q", "page:2056 AND recid:9106451"), "//*[@numFound='1']");
		assertQ(req("q", "page:2056-2059 AND recid:9106451"), "//*[@numFound='1']");
		assertQ(req("q", "page:a056"), "//*[@numFound='1']");
		assertQ(req("q", "page:a056-"), "//*[@numFound='1']");
		assertQ(req("q", "page:a056-2059 AND recid:2"), "//*[@numFound='1']");
		
		assertQ(req("q", "volume:l219"), "//*[@numFound='1']");
		assertQ(req("q", "volume:L219"), "//*[@numFound='1']");
		assertQ(req("q", "issue:4"), "//*[@numFound='1']");
		assertQ(req("q", "aff:nasa"), "//*[@numFound='1']");
		assertQ(req("q", "aff:KAVLI"), "//*[@numFound='0']");
		assertQ(req("q", "aff:kavli"), "//*[@numFound='1']");
		
		
		/*
		 * email
		 */
		assertQ(req("q", "email:rcutri@example.edu"), "//*[@numFound='1']");
		assertQ(req("q", "email:rCutri@example.edu"), "//*[@numFound='1']");
		assertQ(req("q", "email:rcutri@example*"), "//*[@numFound='1']");
		

		/*
		 * database & bibgroup
		 */
		assertQ(req("q", "database:astronomy"), "//*[@numFound='7']");
		assertQ(req("q", "database:Astronomy"), "//*[@numFound='7']");
		assertQ(req("q", "database:ASTRONOMY"), "//*[@numFound='7']");
		assertQ(req("q", "database:astronom*"), "//*[@numFound='7']");
		assertQ(req("q", "database:astronom?"), "//*[@numFound='7']");
		
		assertQ(req("q", "bibgroup:cfa"), "//*[@numFound='2']");
		assertQ(req("q", "bibgroup:CFA"), "//*[@numFound='2']");
		assertQ(req("q", "bibgroup:CF*"), "//*[@numFound='2']");
		assertQ(req("q", "bibgroup:?FA"), "//*[@numFound='2']");
		
		assertQ(req("q", "property:catalog AND property:photos"), "//*[@numFound='1']");
		assertQ(req("q", "property:Catalog AND property:Photos"), "//*[@numFound='1']");
		assertQ(req("q", "property:CATALOG AND property:photos"), "//*[@numFound='1']");
		assertQ(req("q", "property:catalog AND property:PHOTOS"), "//*[@numFound='1']");
		
		
		/*
		 * Bibcodes
		 */
		
		assertQ(req("q", "bibcode:2012yCat..35409143M"), "//*[@numFound='1']");
		assertQ(req("q", "bibcode:2012ycat..35409143m"), "//*[@numFound='1']");
		assertQ(req("q", "bibcode:2012YCAT..35409143M"), "//*[@numFound='1']");
		assertQ(req("q", "bibcode:2012YCAT..*"), "//*[@numFound='4']");
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
		 * Bibstem is not case sensitive (at least for now, so the above values
		 * are lowercased)
		 * 
		 */
		//System.out.println(direct.request("/select?q=*:*&fl=bibcode,bibstem,recid,title", null).replace("</", "\n</"));
		assertQ(req("q", "bibstem:YCAT"), "//*[@numFound='5']");
		assertQ(req("q", "bibstem:yCat"), "//*[@numFound='5']");
		assertQ(req("q", "bibstem:ycat"), "//*[@numFound='5']");

		assertQ(req("q", "bibstem:yCat..35a"), "//*[@numFound='1']");
		assertQ(req("q", "bibstem:yCat..35*"), "//*[@numFound='3']");
		assertQ(req("q", "bibstem:yCat..35?"), "//*[@numFound='3']");
		
		assertQ(req("q", "bibstem:apj.."), "//*[@numFound='1']");

		assertQ(req("q", "bibstem:yCat..*"), "//*[@numFound='4']");
		assertQ(req("q", "bibstem:yCat.*"), "//*[@numFound='5']");
		assertQ(req("q", "bibstem:yCat*"), "//*[@numFound='5']");
		assertQ(req("q", "bibstem:stat.conf"), "//*[@numFound='1']");
		assertQ(req("q", "bibstem:STAT.CONF"), "//*[@numFound='1']");
		
		/*
		 * recid
		 */
		
		assertQ(req("q", "recid:2"), "//*[@numFound='1']");
		assertQ(req("q", "recid:9106442"), "//*[@numFound='1']");
		assertQ(req("q", "recid:002"), "//*[@numFound='1']");
		
		
		/*
		 * doi:
		 * 
		 * According to the standard, doi can contain almost any utf-8
		 * char
		 */
		
		assertQ(req("q", "doi:abcds/esdfs.123045"), "//*[@numFound='1']");
		assertQ(req("q", "doi:doi\\:abcds/esdfs.123045"), "//*[@numFound='1']");
		assertQ(req("q", "doi:\"doi:ŽŠČŘĎŤŇ:123456789\""), "//*[@numFound='1']");
		assertQ(req("q", "doi:\"doi:ŽŠČŘĎŤŇ:123456789\""), "//*[@numFound='1']");
		assertQ(req("q", "doi:\"doi:ŽŠČŘĎŤŇ.123456789\""), "//*[@numFound='1']");
		assertQ(req("q", "doi:\"doi:žščřďťň.123456789\""), "//*[@numFound='1']");
		assertQ(req("q", "doi:\"doi:žščŘĎŤŇ\\?123456789\""), "//*[@numFound='2']");
		assertQ(req("q", "doi:\"doi:žščŘĎŤŇ\\?123456789\""), "//*[@numFound='2']");
		
		/*
		 * keywords
		 */
		assertQ(req("q", "keyword:\"classical statistical mechanics\""), "//*[@numFound='1']");
		assertQ(req("q", "keyword:\"WORLD WIDE WEB\""), "//*[@numFound='1']");
		assertQ(req("q", "keyword:HYDRODYNAMICS"), "//*[@numFound='1']");
		assertQ(req("q", "keyword_norm:HYDRODYNAMICS"), "//*[@numFound='1']");
		assertQ(req("q", "keyword_norm:\"methods numerical\""), "//*[@numFound='1']");
		assertQ(req("q", "keyword_facet:\"World Wide Web\""), "//*[@numFound='1']");
		assertQ(req("q", "keyword_facet:\"world wide web\""), "//*[@numFound='0']");
		
		/*
		 * identifier
		 * should be translated into the correct field (currently, the grammar 
		 * understands only arxiv: and doi: (and doi gets handled separately)
		 */
		assertQ(req("q", "arxiv:1234.5678"), "//*[@numFound='1']");
		assertQ(req("q", "arxiv:\"arXiv:1234.5678\""), "//*[@numFound='1']");
		assertQ(req("q", "arXiv:1234.5678"), "//*[@numFound='1']");
		assertQ(req("q", "identifier:1234.5678"), "//*[@numFound='1']");
		assertQ(req("q", "arXiv:hep-ph/1234"), "//*[@numFound='1']");
		assertQ(req("q", "arxiv:\"ARXIV:hep-ph/1234\""), "//*[@numFound='1']");
		assertQ(req("q", "arxiv:hep-ph/1234"), "//*[@numFound='1']");
		assertQ(req("q", "identifier:hep-ph/1234"), "//*[@numFound='1']");
		
		/*
		 * title
		 * 
		 * TODO: I feel we need to enrich this suite with more tests
		 * but we need more examples and my head is quite tired to think
		 * of something comprehensive 
		 */
		assertQ(req("q", "title:\"title is not available\""), "//*[@numFound='1']"); // everything besides title is stopword
		assertQ(req("q", "title:no-sky"), "//*[@numFound='2']"); //becomes: title:no-sky title:sky title:no-sky
		assertQ(req("q", "title:nosky"), "//*[@numFound='1']");
		assertQ(req("q", "title:q\\'i"), "//*[@numFound='2']");
		
		
		/*
		 * abstract
		 * 
		 */
		assertQ(req("q", "abstract:abstract"), "//*[@numFound='1']"); // everything besides title is stopword
		assertQ(req("q", "abstract:No-SKy"), "//*[@numFound='2']"); //becomes: title:no-sky title:sky title:no-sky
		assertQ(req("q", "abstract:nosky"), "//*[@numFound='1']");
		
		//becomes: abstract:q'i abstract:q abstract:i abstract:qi
		assertQ(req("q", "abstract:q\\'i", "fl", "recid,abstract,title"), "//*[@numFound='4']");
		
		
		assertQ(req("q", "abstract:ABSTRACT", "fl", "recid,abstract,title"), "//*[@numFound='0']"); // is considered acronym
		
		//assertQ(req("qt", "/admin/luke"), "//*[@numFound='0']");
	}
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAdsDataImport.class);
    }
}
