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

package org.apache.solr.analysis.author;


import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.search.BooleanQuery;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;

/**
 * Tests that the dataimport handler does really wait and does not
 * return immediately. Also, one of the fields is fetched by Python.
 * 
 */
public class TestAdsAuthorParsing extends MontySolrQueryTestCase {
	
	String FID = "id";
	String FBIBCODE = "bibcode";
	String FAUTHOR = "author";
	
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
					"ADAMUT\\, I, ADAMUTI\\, I",
					"ADAMCHUK\\, K, ADAMCZUK\\, K",
				    "ADJABSCHIRZADEH\\, A, ADJABSHIRZADEH\\, A",
				    "KUEHN\\, J, KUHN\\, J",
				    "KUEHN\\, M, KUHN\\, M",
				    "KUEHNEL\\, W, KUHNEL\\, W",
			});
			replaceInFile(newConfig, "synonyms=\"author.synonyms\"", "synonyms=\"" + synonymsFile.getAbsolutePath() + "\"");
			
			
			
			synonymsFile = createTempFile(new String[]{
					"ZELENYI\\,\\ L\\ M=>ZELENY\\,,ZELENY\\,\\ L\\w*\\ M.*,ZELENY\\,\\ L\\ M,ZELENY\\,\\ L\\w*,",
					"TELNIUK\\ ADAMCHUK\\,\\ V\\ V=>TELNYUK\\ ADAMCHUK\\,,TELNYUK\\ ADAMCHUK\\,\\ V\\ V,TELNYUK\\ ADAMCHUK\\,\\ V\\w*\\ V.*,TELNYUK\\ ADAMCHUK\\,\\ V\\w*,",
					"GORKAVII\\,\\ N\\ N=>GORKAVYY\\,\\ N\\w*,GORKAVYY\\,,GORKAVYY\\,\\ N\\ N,GORKAVYY\\,\\ N\\w*\\ N.*,",
					});
			replaceInFile(newConfig, "synonyms=\"author_curated.synonyms\"", "synonyms=\"" + synonymsFile.getAbsolutePath() + "\"");
			
			
			
			synonymsFile = createTempFile(new String[]{
					//"ADAMČUK\\,\\ K=>ADAMCUK\\,\\ K,ADAMCHUK\\,\\ K,"
					}
			);
			replaceInFile(newConfig, "synonyms=\"author_generated.translit\"", "synonyms=\"" + synonymsFile.getAbsolutePath() + "\"");
			replaceInFile(newConfig, "outFile=\"author_generated.translit\"", "outFile=\"" + synonymsFile.getAbsolutePath() + "\"");
			
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
		
		return newConfig.getAbsolutePath();
	}

	@Override
	public String getSolrConfigFile() {
		
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
		
	}


	
	public void testAuthorParsing() throws Exception {

		assertU(adoc(FID, "1", FBIBCODE, "xxxxxxxxxxxxx", FAUTHOR, "Adamčuk, K"));
		assertU(adoc(FID, "2", FBIBCODE, "xxxxxxxxxxxxx", FAUTHOR, "Adamčuk, Karel"));
		assertU(adoc(FID, "3", FBIBCODE, "xxxxxxxxxxxxx", FAUTHOR, "Adamčuk, Kolja"));
		assertU(adoc(FID, "4", FBIBCODE, "xxxxxxxxxxxxx", FAUTHOR, "Müller, William"));
		assertU(commit());
		
		//setDebug(true);
		
		// 1. transliteration: adamčuk, k --> adamchuk, k
		// 2. synonym expansion: adamchuk, k --> adamchuk, k; adamczuk, k
		// 3. query expansion: for each...
		assertQueryEquals(req("qt", "aqp", "q", "author:\"adamčuk, k\""), 
				"author:adamčuk, k author:adamčuk, author:adamčuk, k* author:adamchuk, k author:adamchuk, k* author:adamchuk, author:adamczuk, k author:adamczuk, author:adamczuk, k* author:adamcuk, k author:adamcuk, author:adamcuk, k*", 
				BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "author:\"Adamčuk, K\""), 
				"author:adamčuk, k author:adamčuk, author:adamčuk, k* author:adamchuk, k author:adamchuk, k* author:adamchuk, author:adamczuk, k author:adamczuk, author:adamczuk, k* author:adamcuk, k author:adamcuk, author:adamcuk, k*", 
				BooleanQuery.class);

		assertQueryEquals(req("qt", "aqp", "q", "author:\"ADAMČUK, K\""), 
				"author:adamčuk, k author:adamčuk, author:adamčuk, k* author:adamchuk, k author:adamchuk, k* author:adamchuk, author:adamczuk, k author:adamczuk, author:adamczuk, k* author:adamcuk, k author:adamcuk, author:adamcuk, k*", 
				BooleanQuery.class);
		
		
		// this is not possible without synonym mapping (and the mechanism of collecting terms while
		// indexing is not set up to handle this case), it creates this:
		// ADAMČUK\,\ K=>ADAMCUK\,\ K,ADAMCHUK\,\ K,
		//assertQueryEquals(req("qt", "aqp", "q", "author:\"adamchuk, k\""), 
		//		"author:adamčuk, k author:adamčuk, author:adamčuk, k* author:adamchuk, k author:adamchuk, k* author:adamchuk, author:adamczuk, k author:adamczuk, author:adamczuk, k* author:adamcuk, k author:adamcuk, author:adamcuk, k*", 
		//		BooleanQuery.class);
		
		assertQ(req("q", "author:\"ADAMČUK, K\""), "//*[@numFound='3']");
		assertQ(req("q", "author:\"adamčuk, k\""), "//*[@numFound='3']");
		
		// not working because: adamczuk, k is  not in synonym file
		//assertQ(req("q", "author:\"adamczuk, k\""), "//*[@numFound='3']");
		
	}
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAdsAuthorParsing.class);
    }
}
