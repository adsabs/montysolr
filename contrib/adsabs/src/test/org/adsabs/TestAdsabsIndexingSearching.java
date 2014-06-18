package org.adsabs;

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


import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.TrieIntField;
import org.apache.solr.servlet.DirectSolrConnection;
import org.adsabs.solr.AdsConfig.F;
import org.junit.BeforeClass;


/**
 * This test encompassess both indexing and searching, as configured
 * for the ADS. The test does not need a working solr installation,
 * it is using both solr example config and the specific ads config.
 * 
 *    KEEP IT FREE FROM DEPENDENCIES!!!
 * 
 * HOWEVER, for config it is wise to use filenames that are not present in the
 * default solr example (to avoid confusion)
 *
 **/
public class TestAdsabsIndexingSearching extends MontySolrQueryTestCase {

  @BeforeClass
  public static void beforeClass() throws Exception {
  	makeResourcesVisible(Thread.currentThread().getContextClassLoader(), new String[] {
			MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
		  MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
		});
  	
    System.setProperty("solr.allow.unsafe.resourceloading", "true");
    schemaString = MontySolrSetup.getMontySolrHome()
        + "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";
      
    configString = MontySolrSetup.getMontySolrHome()
        + "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
    
    initCore(configString, schemaString, MontySolrSetup.getSolrHome()
			    + "/example/solr");
  }
  

	public void test() throws Exception {
		
		DirectSolrConnection direct = getDirectServer();
		EmbeddedSolrServer embedded = getEmbeddedServer();
		
		// checking the schema
		IndexSchema schema = h.getCore().getLatestSchema();
		
		
		SchemaField field = schema.getField("id");
		assertTrue(field.indexed() == true && field.stored() == true && field.isRequired() == true
				&& field.multiValued() == false);
		
		field = schema.getUniqueKeyField();
		field.getName().equals("id");
		
		field = schema.getField(F.BIBCODE);
		assertTrue(field.indexed() == true && field.stored() == true && field.isRequired() == true 
				&& field.multiValued() == false);
		field.checkSortability();
		
		field = schema.getField(F.RECID);
		assertTrue(field.indexed() == true && field.stored() == true && field.isRequired() == true 
				&& field.multiValued() == false);
		field.checkSortability();
		assertTrue(field.getType().getClass().isAssignableFrom(TrieIntField.class));
		
		// check field ID is copied to field RECID
//		List<CopyField> copyFields = schema.getCopyFieldsList("id");
//		assertTrue(copyFields.size() == 1);
//		CopyField cField = copyFields.get(0);
//		cField.getSource().getName().equals("id");
//		cField.getDestination().getName().equals(F.RECID);
//		field = cField.getDestination();
		
		
		
		
		
		// check authors are correctly indexed/searched
		assertU(adoc("id", "0", "bibcode", "b1", "author", "Dall'oglio, Antonella"));
		assertU(adoc("id", "1", "bibcode", "b2", "author", "VAN DER KAMP, A; Von Accomazzi, Alberto, III, Dr.;Kao, P'ing-Tzu"));
		assertU(adoc("id", "2", "bibcode", "b3", "author", "'t Hooft, Furst Middle"));
		assertU(adoc("id", "3", "bibcode", "b4", "author", "O, Paul S.; Last, Furst Middle More"));
		assertU(adoc("id", "4", "bibcode", "b5", "author", "O, Paul S.", "author", "Last, Furst Middle More"));
		assertU(adoc("id", "5", "bibcode", "b6", "author", "van Tiggelen, Bart A., Jr."));
		assertU(adoc("id", "6", "bibcode", "b7", "author", "Łuczak, Andrzej;John Doe Jr;Mac Low, Furst Middle;'t Hooft, Furst Middle"));
		assertU(adoc("id", "7", "bibcode", "b8", "author", "Łuczak, Andrzej", "author", "John Doe Jr", 
				"author", "Mac Low, Furst Middle", "author", "'t Hooft, Furst Middle"));
		
		assertU(adoc("id", "8", "bibcode", "b9", 
				"author", "t' Hooft, van X",
				"author", "Anders, John Michael",
				"author", "Einstein, A",
				"author_facet_hier", "0/T Hooft, V", 
				"author_facet_hier", "1/T Hooft, V/T Hooft, Van X",
				"author_facet_hier", "0/Anders, J M", 
				"author_facet_hier", "1/Anders, J M/Anders, John Michael",
				"author_facet_hier", "0/Einstein, A", 
				"aff", "Hooft affiliation",
				"aff", "-",
				"aff", "Einstein affiliation, Zurych",
				"email", "-",
				"email", "anders@email.com",
				"email", "-"
				));
		
		String json = "{\"add\": {"
		+ "\"doc\": {" +
				"\"id\": 100" +
			  ", \"recid\": 100" +
				", \"bibcode\": \"2014JNuM..455...10B\"" +
			  
				// order and length must be the same for author,aff, email
				// missing value must be indicated by '-'
				", \"author\": [\"t' Hooft, van X\", \"Anders, John Michael\", \"Einstein, A\"]" +
				", \"aff\": [\"Hoof affiliation\", \"-\", \"Einstein institute, Zurych, Switzerland\"]" +
				", \"email\": [\"-\", \"anders@email.com\", \"-\"]" +
				
				// author_facet_hier must be generated (solr doesn't modify it)
				", \"author_facet_hier\": [\"0/T Hooft, V\", \"1/T Hooft, V/T Hooft, Van X\", \"0/Anders, J M\", \"1/Anders, J M/Anders, John Michael\", \"0/Einstein, A\"]" +
				
				// must be: "yyyy-MM-dd|yyyy-MM|yyyy"
				", \"pubdate\": \"2013-08-05\"" +
				
				// Field that contains both grant ids and grant agencies.
				", \"grant\": [\"NASA\", \"123456-78\"]" +
				// grant_agency/grant_id
				", \"grant_facet_hier\": [\"0/NASA\", \"1/NASA/123456-78\"]" +
			"}" +
		"}}";
		updateJ(json, null);
		assertU(commit("waitSearcher", "true"));
		
		
		assertQ("should find one", req("defType", "aqp",
        "q", "bibcode:2014JNuM..455...10B") ,"//result[@numFound=1]" );
		
		assertQ("should find one", req("defType", "aqp",
        "q", "author:Last") ,"//result[@numFound=1]" );
		
		assertQ("should find one", req("defType", "aqp",
				"q", "author:Antonella Dall'oglio") ,"//result[@numFound=1]" );
		
		
	}
}