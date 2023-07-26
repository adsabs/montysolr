/*
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
package org.apache.solr.search;

import java.io.IOException;

import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.search.Query;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.request.SolrQueryRequest;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestAqpLuceneQParserPlugin extends MontySolrAbstractTestCase {
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		
		makeResourcesVisible(Thread.currentThread().getContextClassLoader(), new String[] {
			    MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/server/solr/collection1/conf",
		      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
		    });
				
		System.setProperty("solr.allow.unsafe.resourceloading", "true");
		schemaString = MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1/conf/" 
		      + "schema-minimal.xml";
		
		configString = MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1/conf/" 
					+ "solrconfig-extended-lucene-qparser.xml";
		
		initCore(configString, schemaString, MontySolrSetup.getSolrHome()
			    + "/example/solr");
	}
	
	

	public void createIndex() {
		assertU(adoc("id","1", "text", "who"));
		assertU(adoc("id","2", "text", "is stopword"));
		assertU(adoc("id","3", "text", "able"));
		assertU(adoc("id","4", "text", "to stopword"));
		assertU(adoc("id","5", "text", "exchange"));
		assertU(adoc("id","16", "text", "liberty"));
		assertU(adoc("id","17", "text", "for stopword"));
		assertU(adoc("id","18", "text", "safety"));
		assertU(adoc("id","19", "text", "deserves"));
		assertU(adoc("id","20", "text", "neither"));
		assertU(adoc("id","21", "text", "Benjamin Franklin must not be a hero of NSA officers!"));
		assertU(commit());
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		createIndex();
	}

	
	@Test
	public void test() throws IOException, Exception {
		
		AqpLuceneQParserPlugin a = new AqpLuceneQParserPlugin();
		SolrQueryRequest r = req(CommonParams.Q, "franklin NEAR hero", CommonParams.DF, "text");
		QParser parser = a.createParser("franklin NEAR hero", r.getParams(), r.getParams(), r);
		Query query = parser.parse();
		assertEquals("spanNear([text:franklin, text:hero], 5, false)", query.toString());
		r.close();
		
		
		QParserPlugin qp = h.getCore().getQueryPlugin("lucene2");
		assertTrue("parserPlugin is not an instanceof " + AqpLuceneQParserPlugin.class, qp instanceof AqpLuceneQParserPlugin);
		
		
		assertQ(req("q", "*:*"),
				"//*[@numFound='11']"
		);
		assertQ(req("q", "text:\"benjamin franklin\""),
				"//*[@numFound='1']"
		);
		
		assertQ(req("q", "{!lucene2}franklin NEAR hero"),
				"//*[@numFound='1']",
				"//doc/str[@name='id'][.='21']"
		);
		
		assertQEx("Configuration for proximity search not heeded", 
				req("q", "{!lucene2} franklin NEAR10 officers"),
				SolrException.ErrorCode.BAD_REQUEST);
				
		
		assertQ(req("q", "{!lucene2} benjamin NEAR2 nsa"),
				"//*[@numFound='0']"
		);
		
		assertQ(req("q", "{!lucene2} benjamin NEAR1 franklin"),
				"//*[@numFound='1']",
				"//doc/str[@name='id'][.='21']"
		);

	}

}


