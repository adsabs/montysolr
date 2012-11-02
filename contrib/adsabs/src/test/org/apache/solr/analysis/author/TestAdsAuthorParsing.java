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

import org.adsabs.solr.AdsConfig.F;

/**
 * 
 * Tests for all the author_ types defined in schema.xml
 * 
 */
public class TestAdsAuthorParsing extends MontySolrQueryTestCase {
	
	
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
		 * Make a copy of the schema.xml, and create our own synonym translation rules
		 */
		
		String schemaConfig = MontySolrSetup.getMontySolrHome()
			+ "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";
		
		File newConfig;
		try {
			
		  // hand-curated synonyms
			File curatedSynonyms = createTempFile(formatSynonyms(new String[]{
			    "ABBOT, CHARLES GREELEY;ABBOTT, CHARLES GREELEY",
			    "ABDEL AZIZ BAKRY, A;BAKRY, A",
			    "ACHUTBHAN, P;ACHUTHAN, P",
			    "ADAMUT, I A;ADAMUTI, I A",
			    "ADJABSCHIRZADEH, A;ADJABSHIRZADEH, A",
			    "AGARWAL, S;AGGARWAL, S",
			    "AGUILAR CHIU, L A;AGUILAR, L A",
			    "AITMUHAMBETOV, A A;AITMUKHAMBETOV, A A",
			    "AL MLEAKY, Y M;ALMLEAKY, Y M",
			    "ALEXEENKO, V V;ALEXEYENKO, V V",
			    "ALFONSO, JULIA;ALFONSO-GARZON, JULIA",
			    "ALLEN, LYNNE;ALLEN, R LYNNE;JONES, LYNNE;JONES, R LYNNE", // until here copied from: /proj/ads/abstracts/config/author.syn.new
			    "ARAGON SALAMANCA, A;ARAGON-SALAMANCA, A;ARAGON, A;SALAMANCA, A" // copied from: /proj/ads/abstracts/config/author.syn
					}));
			
			// automatically harvested variations of author names (collected during indexing)
			// it will be enriched by the indexing
			File generatedTransliterations = createTempFile(formatSynonyms(new String[]{
          "ADAMCHUK, K => ADAMČUK, K",
          "ADAMCUK, K => ADAMČUK, K",
          "ADAMCZUK, K => ADAMČUK, K",
          "ADAMCHUK, KOLJA => ADAMČUK, KOLJA",
          "ADAMCUK, KOLJA => ADAMČUK, KOLJA",
          "ADAMCZUK, KOLJA => ADAMČUK, KOLJA",
          "MULLER, WILLIAM => MÜLLER, WILLIAM",
          "MUELLER, WILLIAM => MÜLLER, WILLIAM",
          }
      ));
			
			
			File newSchema = duplicateModify(new File(schemaConfig), 
			    "synonyms=\"author_curated.synonyms\"", "synonyms=\"" + curatedSynonyms.getAbsolutePath() + "\"",
			    "synonyms=\"author_generated.translit\"", "synonyms=\"" + generatedTransliterations.getAbsolutePath() + "\"",
			    "outFile=\"author_generated.translit\"", "outFile=\"" + generatedTransliterations.getAbsolutePath() + "\""
			    );
			return newSchema.getAbsolutePath();
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
		
	}

	private String[] formatSynonyms(String[] strings) {
    String[] newLines = new String[strings.length];
    int nl = 0;
    for (String line : strings) {
      StringBuilder out = new StringBuilder();
      String[] kv = line.split("=>");
      for (int i=0;i<kv.length;i++) {
        if (i>0) out.append("=>");
        String[] names = kv[i].split(";");
        for (int j=0;j<names.length;j++) {
          if (j>0) out.append(",");
          out.append(names[j].trim().replace(" ", "\\ ").replace(",", "\\,"));
        }
      }
      newLines[nl++] = out.toString();
    }
    return newLines;
  }

  @Override
	public String getSolrConfigFile() {
		
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
		
	}

	public void testAuthorParsing() throws Exception {

	  /*
	   * For ADS there are these rules:
     *   What gets indexed: Normalized author name (always lowercase!)
     *   What gets searched: By default, the author name is
     *       
     *       example: Štaufčik, Piotr
     *       
     *       1. normalized (sztaufczik, piotr)
     *         2. enriched with name variants (sztaufczik, pjotr)
     *           3. enriched with synonyms (konrad, pjotr)
     *           
     *   The different tokenizer chains serve for situations, when we want
     *   to search for the author name but DE-activate some of the steps
     *   above. The NORMALIZATION happens ALWAYS (because we index things
     *   that way)
     *   
     *     author_exact = 1 + 3
     *     author_nosyn = 1 + 2
     *     author_exact_nosyn = 1  
     */
	  
	  
		assertU(adoc(F.ID, "1", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, K"));
		assertU(adoc(F.ID, "2", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Karel"));
		assertU(adoc(F.ID, "3", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Kolja"));
		
		assertU(adoc(F.ID, "4", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Müller, William"));
		assertU(adoc(F.ID, "5", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Mueller, William"));
		assertU(commit());

    // TODO: persist the transliteration map, restart the core (or call the synonym filter somehow)
    // to reload synonym chain harvested during indexing

		
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
