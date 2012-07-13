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

import java.util.List;

import montysolr.util.MontySolrAbstractTestCase;
import montysolr.util.MontySolrSetup;

import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.schema.CopyField;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.TrieIntField;
import org.apache.solr.servlet.DirectSolrConnection;


import examples.adsabs.AdsConfig;

/**
 * This test encompassess both indexing and searching, as configured
 * for the ADS. The test does not need a working solr installation,
 * it is using both solr example config and the specific ads config.
 * 
 * HOWEVER, for config it is wise to use filenames that are not present in the
 * default solr example (to avoid confusion)
 *
 **/
public class AdsabsBigTestIndexingSearching extends MontySolrAbstractTestCase {

	public String getSchemaFile() {
		
	    makeResourcesVisible(this.solrConfig.getResourceLoader(),
	    		new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/conf",
	    				      MontySolrSetup.getSolrHome() + "/example/solr/conf"
	    	});
		
		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/examples/adsabs/solr/conf/schema.xml";
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/examples/adsabs/solr/conf/solrconfig.xml";
	}

	public void test() throws Exception {
		
		DirectSolrConnection direct = getDirectServer();
		EmbeddedSolrServer embedded = getEmbeddedServer();
		
		// checking the schema
		IndexSchema schema = h.getCore().getSchema();
		
		
		SchemaField field = schema.getField(AdsConfig.FIELD_ID);
		assertTrue(field.indexed() == true && field.stored() == true && field.isRequired() == true
				&& field.multiValued() == false);
		
		field = schema.getUniqueKeyField();
		field.getName().equals(AdsConfig.FIELD_ID);
		
		field = schema.getField(AdsConfig.FIELD_BIBCODE);
		assertTrue(field.indexed() == true && field.stored() == true && field.isRequired() == true 
				&& field.multiValued() == false);
		field.checkSortability();
		
		field = schema.getField(AdsConfig.FIELD_RECID);
		assertTrue(field.indexed() == true && field.stored() == true && field.isRequired() == true 
				&& field.multiValued() == false);
		field.checkSortability();
		assertTrue(field.getType().getClass().isAssignableFrom(TrieIntField.class));
		
		// check field ID is copied to field RECID
//		List<CopyField> copyFields = schema.getCopyFieldsList(AdsConfig.FIELD_ID);
//		assertTrue(copyFields.size() == 1);
//		CopyField cField = copyFields.get(0);
//		cField.getSource().getName().equals(AdsConfig.FIELD_ID);
//		cField.getDestination().getName().equals(AdsConfig.FIELD_RECID);
//		field = cField.getDestination();
		
		
		
		
		
		// check authors are correctly indexed/searched
		adoc(AdsConfig.FIELD_ID, "0", AdsConfig.FIELD_AUTHOR, "Antonella Dall'oglio; P'ING-TZU KAO; A VAN DER KAMP");
		adoc(AdsConfig.FIELD_ID, "1", AdsConfig.FIELD_AUTHOR, "VAN DER KAMP, A; Von Accomazzi, Alberto, III, Dr.;Kao, P'ing-Tzu");
		adoc(AdsConfig.FIELD_ID, "2", AdsConfig.FIELD_AUTHOR, "Paul S O; Last, Furst Middle;'t Hooft, Furst Middle");
		adoc(AdsConfig.FIELD_ID, "3", AdsConfig.FIELD_AUTHOR, "O, Paul S.; Last, Furst Middle More");
		adoc(AdsConfig.FIELD_ID, "4", AdsConfig.FIELD_AUTHOR, "O, Paul S.", AdsConfig.FIELD_AUTHOR, "Last, Furst Middle More");
		adoc(AdsConfig.FIELD_ID, "5", AdsConfig.FIELD_AUTHOR, "van Tiggelen, Bart A., Jr.");
		adoc(AdsConfig.FIELD_ID, "6", AdsConfig.FIELD_AUTHOR, "Łuczak, Andrzej;John Doe Jr;Mac Low, Furst Middle;'t Hooft, Furst Middle");
		adoc(AdsConfig.FIELD_ID, "7", AdsConfig.FIELD_AUTHOR, "Łuczak, Andrzej", AdsConfig.FIELD_AUTHOR, "John Doe Jr", 
				AdsConfig.FIELD_AUTHOR, "Mac Low, Furst Middle", AdsConfig.FIELD_AUTHOR, "'t Hooft, Furst Middle");
		
		//TODO: this should not succeed, cause BIBCODE is missing
		
		assertU(commit());
		
		assertQ("should find one", req("defType", AdsConfig.DEF_TYPE, 
				"q", AdsConfig.FIELD_AUTHOR + ":Antonella") ,"//result[@numFound=1]" );
		
		assertQ("should find one", req("defType", AdsConfig.DEF_TYPE, 
				"q", AdsConfig.FIELD_AUTHOR + ":Antonella") ,"//result[@numFound=1]" );
		
	}
}