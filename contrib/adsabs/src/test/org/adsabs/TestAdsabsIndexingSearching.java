package examples.adsabs;

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

	public String getSchemaFile() {
		
	    makeResourcesVisible(this.solrConfig.getResourceLoader(),
	    		new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
	    				      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
	    	});
		
		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
	}

	public void test() throws Exception {
		
		DirectSolrConnection direct = getDirectServer();
		EmbeddedSolrServer embedded = getEmbeddedServer();
		
		// checking the schema
		IndexSchema schema = h.getCore().getSchema();
		
		
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
		assertU(adoc("id", "0", "bibcode", "b1", F.AUTHOR, "Dall'oglio, Antonella"));
		assertU(adoc("id", "1", "bibcode", "b2", F.AUTHOR, "VAN DER KAMP, A; Von Accomazzi, Alberto, III, Dr.;Kao, P'ing-Tzu"));
		assertU(adoc("id", "2", "bibcode", "b3", F.AUTHOR, "'t Hooft, Furst Middle"));
		assertU(adoc("id", "3", "bibcode", "b4", F.AUTHOR, "O, Paul S.; Last, Furst Middle More"));
		assertU(adoc("id", "4", "bibcode", "b5", F.AUTHOR, "O, Paul S.", F.AUTHOR, "Last, Furst Middle More"));
		assertU(adoc("id", "5", "bibcode", "b6", F.AUTHOR, "van Tiggelen, Bart A., Jr."));
		assertU(adoc("id", "6", "bibcode", "b7", F.AUTHOR, "Łuczak, Andrzej;John Doe Jr;Mac Low, Furst Middle;'t Hooft, Furst Middle"));
		assertU(adoc("id", "7", "bibcode", "b8", F.AUTHOR, "Łuczak, Andrzej", F.AUTHOR, "John Doe Jr", 
				F.AUTHOR, "Mac Low, Furst Middle", F.AUTHOR, "'t Hooft, Furst Middle"));
		
		
		assertU(commit("waitSearcher", "true"));
		
		assertQ("should find one", req("defType", "aqp", "debugQuery", "true",
        "q", "author:Last") ,"//result[@numFound=1]" );
		
		assertQ("should find one", req("defType", "aqp", "debugQuery", "true",
				"q", "author:Antonella Dall'oglio") ,"//result[@numFound=1]" );
		
		
	}
}