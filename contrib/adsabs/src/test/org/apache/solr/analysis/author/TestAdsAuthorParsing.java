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
import org.apache.solr.analysis.WriteableExplicitSynonymMap;
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
//			File synonymsFile = createTempFile(new String[]{
//					"ADAMUT\\, I, ADAMUTI\\, I",
//					"ADAMCHUK\\, K, ADAMCZUK\\, K",
//				    "ADJABSCHIRZADEH\\, A, ADJABSHIRZADEH\\, A",
//				    "KUEHN\\, J, KUHN\\, J",
//				    "KUEHN\\, M, KUHN\\, M",
//				    "KUEHNEL\\, W, KUHNEL\\, W",
//			});
//			replaceInFile(newConfig, "synonyms=\"author.synonyms\"", "synonyms=\"" + synonymsFile.getAbsolutePath() + "\"");
			
			
			
			File synonymsFile = createTempFile(new String[]{
					"ZELENYI\\,\\ L\\ M=>ZELENY\\,,ZELENY\\,\\ L\\w*\\ M.*,ZELENY\\,\\ L\\ M,ZELENY\\,\\ L\\w*,",
					"TELNIUK\\ ADAMCHUK\\,\\ V\\ V=>TELNYUK\\ ADAMCHUK\\,,TELNYUK\\ ADAMCHUK\\,\\ V\\ V,TELNYUK\\ ADAMCHUK\\,\\ V\\w*\\ V.*,TELNYUK\\ ADAMCHUK\\,\\ V\\w*,",
					"GORKAVII\\,\\ N\\ N=>GORKAVYY\\,\\ N\\w*,GORKAVYY\\,,GORKAVYY\\,\\ N\\ N,GORKAVYY\\,\\ N\\w*\\ N.*,",
					});
			replaceInFile(newConfig, "synonyms=\"author_curated.synonyms\"", "synonyms=\"" + synonymsFile.getAbsolutePath() + "\"");
			
			
			
			synonymsFile = createTempFile(new String[]{
					"ADAMČUK\\,\\ K,ADAMCUK\\,\\ K,ADAMCHUK\\,\\ K,",
					"ADAMČUK\\,\\ KAREL,ADAMCUK\\,\\ KAREL,ADAMCHUK\\,\\ KAREL,",
					"ADAMČUK\\,\\ KOLJA,ADAMCUK\\,\\ KOLJA,ADAMCHUK\\,\\ KOLJA,",
					"MÜLLER\\,\\ WILLIAM,MULLER\\,\\ WILLIAM,MUELLER\\,\\ WILLIAM",
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
		assertU(adoc(FID, "5", FBIBCODE, "xxxxxxxxxxxxx", FAUTHOR, "Mueller, William"));
		assertU(commit());
		
		//setDebug(true);
		
		//XXX: I want to finish this!!!
		// testcases:
		// adamčuk, kol*
		
		fail("Author parsing needs attention!");
		
		// 1. transliteration: adamčuk, k --> adamchuk, k
		// 2. synonym expansion: adamchuk, k --> adamchuk, k; adamczuk, k
		// 3. query expansion: for each...
		
		
		// simply by replacing .* with * we limit considerably the returned values here
		assertQueryEquals(req("qt", "aqp", "q", "author:\"adamčuk, kol*\""), 
        //"author:adamčuk, k author:adamcuk, k author:adamchuk, k author:adamčuk, author:adamčuk, k* author:adamchuk, karel author:adamčuk, karel author:adamcuk, kolja author:adamcuk, karel author:adamčuk, kolja author:adamchuk, kolja author:adamchuk, k* author:adamchuk, author:adamcuk, author:adamcuk, k*",
        "author:adamčuk, k author:adamcuk, k author:adamchuk, k author:adamčuk, author:adamčuk, k* author:adamchuk, k* author:adamchuk, author:adamcuk, author:adamcuk, k*",
        BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "author:\"adamčuk, k\""), 
				//"author:adamčuk, k author:adamcuk, k author:adamchuk, k author:adamčuk, author:adamčuk, k* author:adamchuk, karel author:adamčuk, karel author:adamcuk, kolja author:adamcuk, karel author:adamčuk, kolja author:adamchuk, kolja author:adamchuk, k* author:adamchuk, author:adamcuk, author:adamcuk, k*",
		    "author:adamčuk, k author:adamcuk, k author:adamchuk, k author:adamčuk, author:adamčuk, k* author:adamchuk, k* author:adamchuk, author:adamcuk, author:adamcuk, k*",
				BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "author:\"Adamčuk, K\""), 
				//"author:adamčuk, k author:adamcuk, k author:adamchuk, k author:adamčuk, author:adamčuk, k* author:adamchuk, karel author:adamčuk, karel author:adamcuk, kolja author:adamcuk, karel author:adamčuk, kolja author:adamchuk, kolja author:adamchuk, k* author:adamchuk, author:adamcuk, author:adamcuk, k*",
		    "author:adamčuk, k author:adamcuk, k author:adamchuk, k author:adamčuk, author:adamčuk, k* author:adamchuk, k* author:adamchuk, author:adamcuk, author:adamcuk, k*",
				BooleanQuery.class);

		assertQueryEquals(req("qt", "aqp", "q", "author:\"ADAMČUK, K\""), 
				//"author:adamčuk, k author:adamcuk, k author:adamchuk, k author:adamčuk, author:adamčuk, k* author:adamchuk, karel author:adamčuk, karel author:adamcuk, kolja author:adamcuk, karel author:adamčuk, kolja author:adamchuk, kolja author:adamchuk, k* author:adamchuk, author:adamcuk, author:adamcuk, k*",
		    "author:adamčuk, k author:adamcuk, k author:adamchuk, k author:adamčuk, author:adamčuk, k* author:adamchuk, k* author:adamchuk, author:adamcuk, author:adamcuk, k*",
				BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "author:\"adamchuk, k\""), 
				//"author:adamchuk, k author:adamcuk, k author:adamčuk, k author:adamchuk, k* author:adamchuk, karel author:adamčuk, karel author:adamcuk, kolja author:adamcuk, karel author:adamchuk, kolja author:adamčuk, kolja author:adamchuk,",
		    "author:adamchuk, k author:adamcuk, k author:adamčuk, k author:adamchuk, k* author:adamchuk,",
				BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "author:\"adamčuk, kolja\""), 
				"author:adamčuk, kolja author:adamcuk, kolja author:adamchuk, kolja author:/adamčuk, kolja\\b.*/ author:adamčuk, author:/adamčuk, k\\b.*/ author:adamcuk, k author:adamchuk, k author:adamčuk, k author:/adamchuk, k\\b.*/ author:/adamchuk, kolja\\b.*/ author:adamchuk, author:/adamcuk, k\\b.*/ author:adamcuk, author:/adamcuk, kolja\\b.*/",
				BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "author:\"Muller, W\""),
				"author:muller, w author:muller, w* author:muller, william author:müller, william author:mueller, william author:muller,",
				BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "author:\"Müller, William\""),
				"author:müller, william author:muller, william author:mueller, william author:müller, w author:/müller, w\\b.*/ author:müller, author:/müller, william\\b.*/ author:/mueller, w\\b.*/ author:/mueller, william\\b.*/ author:mueller, w author:mueller, author:/muller, w\\b.*/ author:muller, w author:/muller, william\\b.*/ author:muller,",
				BooleanQuery.class);
		
		assertQ(req("q", "author:\"ADAMČUK, K\""), "//*[@numFound='3']");
		assertQ(req("q", "author:\"adamčuk, k\""), "//*[@numFound='3']");
		assertQ(req("q", "author:\"adamchuk, k\""), "//*[@numFound='3']");
		assertQ(req("q", "author:\"adamcuk, k\""), "//*[@numFound='3']");
		assertQ(req("q", "author:\"adamčuk, kolja\""), "//*[@numFound='2']"); // should not match record with Adamčuk, Karel
		assertQ(req("q", "author:\"müller, w\""), "//*[@numFound='2']");
		assertQ(req("q", "author:\"mueller, w\""), "//*[@numFound='2']");
		assertQ(req("q", "author:\"muller, w\""), "//*[@numFound='2']");
		
		// not working because: adamczuk, k is  not in synonym file
		//assertQ(req("q", "author:\"adamczuk, k\""), "//*[@numFound='3']");
		
	}
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAdsAuthorParsing.class);
    }
}
