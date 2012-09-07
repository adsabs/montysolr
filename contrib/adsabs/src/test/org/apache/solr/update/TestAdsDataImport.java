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


import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.dataimport.DataImporter;
import org.apache.solr.handler.dataimport.config.DIHConfiguration;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.TextField;
import org.apache.solr.servlet.DirectSolrConnection;
import org.junit.BeforeClass;

import java.lang.reflect.Field; 
import java.util.Map;

/**
 * Tests that the dataimport handler does really wait and does not
 * return immediately. Also, one of the fields is fetched by Python.
 * 
 */
public class TestAdsDataImport extends MontySolrAbstractTestCase {
	
	
	@BeforeClass
	public static void beforeTestAdsDataImport() throws Exception {
		// to use filesystem instead of ram
		//System.setProperty("solr.directoryFactory","solr.SimpleFSDirectoryFactory");
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() 
				+ "/contrib/invenio/src/python");
		MontySolrSetup.addTargetsToHandler("monty_invenio.schema.tests.targets");
		
	}
	
	@Override
	public String getSchemaFile() {
		makeResourcesVisible(this.solrConfig.getResourceLoader(),
	    		new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
	    				      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
	    	});
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";
	}

	@Override
	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
	}


	public void mockHandler(SolrRequestHandler handler) throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException {
		
		DIHConfiguration config = null;
		
		Field fields[] = handler.getClass().getDeclaredFields();
		for (Field f: fields) {
			if (f.toString().contains("importer")) {
				f.setAccessible(true);
				DataImporter importer = (DataImporter) f.get(handler);
				
				Field fields2[] = DataImporter.class.getDeclaredFields();
				for (Field f2: fields2) {
					if (f2.toString().endsWith("config")) {
						f2.setAccessible(true);
						config = (DIHConfiguration) f2.get(importer);
					}
				
				}
			}
		}
		
		config.getDataSources().get(null).put("type", "InvenioDataSource");
		
		
		// HACK to prevent DIH from running mongoDB types
		Field schema = config.getClass().getDeclaredField("lowerNameVsSchemaField");
		schema.setAccessible(true);
		
		@SuppressWarnings("unchecked")
		Map<String, SchemaField> lowerNameVsSchemaField = (Map<String, SchemaField>) schema.get(config);
		
		
		SchemaField full = lowerNameVsSchemaField.get("full");
//		System.out.println(full);
		
		FieldType type = null;
		Field fields3[] = full.getClass().getDeclaredFields();
		for (Field f: fields3) {
			if (f.toString().endsWith("type")) {
				f.setAccessible(true);
				type = (FieldType) f.get(full);
			}
		}
		System.out.println(type.toString());
		
		//schema = h.getCore().getSchema();
		TextField tf = new TextField();
		//tf.init(h.getCore().getSchema(), null);
		
		TextField x = (TextField) type;
		
		lowerNameVsSchemaField.remove("full");
		lowerNameVsSchemaField.remove("ack");
		lowerNameVsSchemaField.remove("reader");
		
	}
	
	public void testImport() throws Exception {
		
		
		String testDir = MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/";
		
		SolrRequestHandler handler = h.getCore().getRequestHandler("/invenio/import");
		
		//mockHandler(handler);
		
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
		assertQ(req("q", "author:\"Rentzsch Holm, Inga\""), "//*[@numFound='1']");
		
		assertQ(req("q", "aff:46556"), "//*[@numFound='1']");
		assertQ(req("q", "aff:\"Notre Dame\""), "//*[@numFound='1']");
		
		assertQ(req("q", "page:2056 AND recid:9218920"), "//*[@numFound='1']");
		assertQ(req("q", "page:2056-2059 AND recid:9218920"), "//*[@numFound='1']");
		assertQ(req("q", "page:a056"), "//*[@numFound='1']");
		assertQ(req("q", "page:a056-"), "//*[@numFound='1']");
		assertQ(req("q", "page:a056-2059 AND recid:2"), "//*[@numFound='1']");
		
		assertQ(req("q", "volume:l219"), "//*[@numFound='1']");
		assertQ(req("q", "volume:L219"), "//*[@numFound='1']");
		assertQ(req("q", "issue:4"), "//*[@numFound='1']");
		assertQ(req("q", "aff:nasa"), "//*[@numFound='1']"); // should find acronym...
		assertQ(req("q", "aff:NASA"), "//*[@numFound='1']"); // regardless of case
		assertQ(req("q", "aff:SPACE"), "//*[@numFound='0']"); // be case sensitive with uppercased query terms
		assertQ(req("q", "aff:KAVLI"), "//*[@numFound='0']"); // same here
		assertQ(req("q", "aff:kavli"), "//*[@numFound='1']"); // otherwise case-insensitive
		
		
		/*
		 * email
		 */
		assertQ(req("q", "email:rcutri@example.edu"), "//*[@numFound='1']");
		assertQ(req("q", "email:rCutri@example.edu"), "//*[@numFound='1']");
		assertQ(req("q", "email:rcutri@example*"), "//*[@numFound='1']");
		

		/*
		 * database & bibgroup
		 */
		assertQ(req("q", "database:astronomy"), "//*[@numFound='8']");
		assertQ(req("q", "database:Astronomy"), "//*[@numFound='8']");
		assertQ(req("q", "database:ASTRONOMY"), "//*[@numFound='8']");
		assertQ(req("q", "database:astronom*"), "//*[@numFound='8']");
		assertQ(req("q", "database:astronom?"), "//*[@numFound='8']");
		
		assertQ(req("q", "bibgroup:cfa"), "//*[@numFound='2']");
		assertQ(req("q", "bibgroup:CFA"), "//*[@numFound='2']");
		assertQ(req("q", "bibgroup:CF*"), "//*[@numFound='2']");
		assertQ(req("q", "bibgroup:?FA"), "//*[@numFound='2']");
		
		assertQ(req("q", "property:catalog AND property:nonarticle"), "//*[@numFound='4']");
		assertQ(req("q", "property:Catalog AND property:Nonarticle"), "//*[@numFound='4']");
		assertQ(req("q", "property:CATALOG AND property:nonarticle"), "//*[@numFound='4']");
		assertQ(req("q", "property:catalog AND property:NONARTICLE"), "//*[@numFound='4']");
		
		
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
		 * id - str type, the unique id key, we do no processing
		 */
		
		assertQ(req("q", "id:2"), "//*[@numFound='1']");
    assertQ(req("q", "id:9218605"), "//*[@numFound='1']");
    assertQ(req("q", "id:002"), "//*[@numFound='0']");
    
    
		/*
		 * recid - recid is a int field, in the previous versions
		 * this worker, but now the value must be of numeric type
		 * to find something. The question is how to get that value
		 * into the term query when fieldType=int 
		 */
		
//		assertQ(req("q", "recid:2"), "//*[@numFound='1']");
//		assertQ(req("q", "recid:9106442"), "//*[@numFound='1']");
//		assertQ(req("q", "recid:002"), "//*[@numFound='1']");
		
		
		
		
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
		assertQ(req("q", "keyword:\"WORLD WIDE WEB\""), "//*[@numFound='1']"); // should be case-insensitive
		assertQ(req("q", "keyword:\"fluid dynamics\""), "//*[@numFound='1']"); // should get both 695$a ...
		assertQ(req("q", "keyword:\"methods numerical\""), "//*[@numFound='1']"); // ... and 695$b 
		assertQ(req("q", "keyword:WISE"), "//*[@numFound='1']"); // ... and 653$a
		assertQ(req("q", "keyword:\"planets and satellites\""), "//*[@numFound='1']"); // .. and 653$b
		assertQ(req("q", "keyword:spectroscopy"), "//*[@numFound='1']");
		assertQ(req("q", "keyword_norm:HYDRODYNAMICS"), "//*[@numFound='1']"); // case-insensitive
		assertQ(req("q", "keyword_norm:Magnitudes"), "//*[@numFound='0']"); // should not get 695$a 
		assertQ(req("q", "keyword_norm:WISE"), "//*[@numFound='0']"); // ... or 653$a 
		assertQ(req("q", "keyword_norm:\"methods numerical\""), "//*[@numFound='1']"); // should get 695$b 
		assertQ(req("q", "keyword_facet:\"world wide web\""), "//*[@numFound='0']"); // case-sensitive
		assertQ(req("q", "keyword_facet:planets"), "//*[@numFound='0']"); // not tokenized
		assertQ(req("q", "keyword_facet:\"planets and satellites\""), "//*[@numFound='1']"); // should get 653$b
		assertQ(req("q", "keyword_facet:\"methods numerical\""), "//*[@numFound='1']"); // should get 695$b
		
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
		assertQ(req("q", "title:KEPLER"), "//*[@numFound='0']"); // shouldn't lowercase all-caps (acronym) terms
		assertQ(req("q", "title:probing"), "//*[@numFound='0']"); // alternate titles shouldn't go in main title field
		assertQ(req("q", "alternate_title:probing"), "//*[@numFound='1']"); // they should go in the alternate_title field
		
		
		/*
		 * abstract
		 * 
		 */
		assertQ(req("q", "abstract:abstract"), "//*[@numFound='1']"); // everything besides title is stopword
		assertQ(req("q", "abstract:No-SKy"), "//*[@numFound='2']"); //becomes: title:no-sky title:sky title:no-sky
		assertQ(req("q", "abstract:nosky"), "//*[@numFound='1']");
		assertQ(req("q", "abstract:sph"), "//*[@numFound='1']");
		assertQ(req("q", "abstract:SPH"), "//*[@numFound='1']");
		assertQ(req("q", "abstract:PARTICLE"), "//*[@numFound='0']"); // acronyms shouldn't get lowercased
		
		//becomes: abstract:q'i abstract:q abstract:i abstract:qi
		assertQ(req("q", "abstract:q\\'i", "fl", "recid,abstract,title"), "//*[@numFound='4']");
		
		
		assertQ(req("q", "abstract:ABSTRACT", "fl", "recid,abstract,title"), "//*[@numFound='0']"); // is considered acronym
		
		assertQ(req("q", "reference:2001nlin......4016H"), "//*[@numFound='1']");
		//assertQ(req("q", "reference:1998Sci...280...98L"), "//*[@numFound='0']");
		//assertQ(req("qt", "/admin/luke"), "//*[@numFound='0']");
		
		
		/*
		 * Cites/refersto queries (use special dummy records, field 999i)
		 */
//		assertQ(req("q", "recid:12"), "//*[@numFound='1']");
//		assertQ(req("q", "recid:16"), "//*[@numFound='1']");
		assertQ(req("q", "id:12"), "//*[@numFound='1']");
		assertQ(req("q", "id:16"), "//*[@numFound='1']");
		assertQ(req("q", "refersto(id:12)"), "//*[@numFound='3']");
		assertQ(req("q", "refersto(title:test)"), "//*[@numFound='4']");
		
//		System.out.println(h.query(req("q", "cites(id:12)")));
//		System.out.println(h.query(req("q", "cites(title:test)")));
		
		assertQ(req("q", "cites(id:12)"), "//*[@numFound='0']");
		assertQ(req("q", "cites(id:13)"), "//*[@numFound='2']");
		assertQ(req("q", "cites(id:12 OR id:13)"), "//*[@numFound='2']");
		assertQ(req("q", "cites(title:test)"), "//*[@numFound='5']");
		
		
		//TODO - this query finds nothing (recid:12) but id:12 does
		//the problem must be with the analyzer (because i see the TermQuery has the binary
		// value of the integer, whilst cites() query uses the string
		//assertQ(req("q", "refersto(recid:12)"), "//*[@numFound='1']", "debugQuery", "true");cite_read_boost
		
		
		assertQ(req("q", "read_count:1 AND bibcode:1976NASSP.389..293M"), "//*[@numFound='1']");
	}
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAdsDataImport.class);
    }
}
