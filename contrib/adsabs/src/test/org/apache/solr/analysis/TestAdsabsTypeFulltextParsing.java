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

package org.apache.solr.analysis;


import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;

/**
 * Tests that the fulltext is parsed properly, the ads_text type
 * is not as simple as it seems
 * 
 */
public class TestAdsabsTypeFulltextParsing extends MontySolrQueryTestCase {

	@Override
	public String getSchemaFile() {
		makeResourcesVisible(this.solrConfig.getResourceLoader(),
	    		new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
	    				      MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
	    	});
		
		/*
		 * For purposes of the test, we make a copy of the schema.xml,
		 * and create our own synonym files
		 */
		
		String configFile = MontySolrSetup.getMontySolrHome()
			+ "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";
		
		File newConfig;
		try {
			
			newConfig = duplicateFile(new File(configFile));
			
			File synonymsFile = createTempFile(new String[]{
					"Hst, hubble\\ space\\ telescope, HST",
			});
			replaceInFile(newConfig, "synonyms=\"ads_text.synonyms\"", "synonyms=\"" + synonymsFile.getAbsolutePath() + "\"");
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
		
		return newConfig.getAbsolutePath();
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
	}

	
	public void test() throws Exception {
		
		//setDebug(true);
		
		/*
		 * Test multi-token translation, the chain is set to recognize
		 * synonyms. So even if the query string is split into 3 tokens,
		 * we are able to join them and find their synonym (HST)
		 */
		assertQueryEquals(req("q", "hubble space telescope", "qt", "aqp"), 
				"all:Hst all:hubble space telescope all:HST", BooleanQuery.class);
		
		assertQueryEquals(req("q", "hubble space -telescope", "qt", "aqp"), 
				"all:hubble all:space -all:telescope", BooleanQuery.class);
		
		assertQueryEquals(req("q", "hubble space title:telescope", "qt", "aqp"), 
				"all:hubble all:space title:telescope", BooleanQuery.class);
		
		/*
		 * Synonym expansion 1token->many
		 */
		
		assertQueryEquals(req("q", "HST", "qt", "aqp"), 
				"all:Hst all:hubble space telescope all:HST", BooleanQuery.class);
		
		/*
		 * But right now, synonym expansion is case sensitive, so these will
		 * fail (even if 'Hst' is in the synonym list). The Acronym filter
		 * causes this behaviour. We should change that (everything must be 
		 * indexed/searched lowercase)
		 */
		assertQueryEquals(req("q", "Hst", "qt", "aqp"), 
				"all:hst", TermQuery.class);
		
		assertQueryEquals(req("q", "hst", "qt", "aqp"), 
				"all:hst", TermQuery.class);
		
		assertQueryEquals(req("q", "HST OR Hst", "qt", "aqp"), 
				"(all:Hst all:hubble space telescope all:HST) all:hst", BooleanQuery.class);
		
		
		
		
		// TODO
		//assertQueryEquals(req("q", "hubble space telescope +star", "qt", "aqp"), 
		//		"(all:Hst all:hubble space telescope all:HST) +star", BooleanQuery.class);
	}
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeFulltextParsing.class);
    }
}
