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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.adsabs.solr.AdsConfig.F;

/**
 * 
 * Tests for all the author_ types defined in schema.xml
 * See:
 * http://labs.adsabs.harvard.edu/trac/ads-invenio/ticket/131
 * http://labs.adsabs.harvard.edu/trac/ads-invenio/ticket/156
 * 
 * I would like to see a token processing which is crazier...
 * 
 * IMPORTANT: this unittest was reviewed on 11-12-2012 by Alberto
 * and he found 1 (in words "ONE") problem, everything else was
 * fine. The problem is easily fixable, right now the 
 * "synonym-upgrade" considers only names with initials for
 * expansion. Ie. 
 *  
 *  "jones, c" =&gt; jones, christine; forman, c; forman, christine
 *  
 *  But Alberto wants that any short form produces the same effect,
 *  ie. 
 *  
 *  "jones," =&gt; jones, christine; forman, c; forman, christine
 *  "jones, c" =&gt; jones, christine; forman, c; forman, christine
 *  
 *  12-12-2012: Finished (I told Alberto, but we didn't review it again)
 * 
 */
public class TestAdsabsTypeAuthorParsing extends MontySolrQueryTestCase {


	private String author_field = "author";
	

	@BeforeClass
	public static void beforeClass() throws Exception {
		
		makeResourcesVisible(Thread.currentThread().getContextClassLoader(), new String[] {
			MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/server/solr/collection1/conf",
		  MontySolrSetup.getSolrHome() + "/example/solr/collection1"
		});
				
		System.setProperty("solr.allow.unsafe.resourceloading", "true");
		schemaString = getSchemaFile();

		
		configString = MontySolrSetup.getMontySolrHome()
			    + "/contrib/examples/adsabs/server/solr/collection1/conf/solrconfig.xml";
		
		initCore(configString, schemaString, MontySolrSetup.getSolrHome()
			    + "/example/solr");
	}
	
  public static String getSchemaFile() {
    
    /*
     * Make a copy of the schema.xml, and create our own synonym translation rules
     */

    String schemaConfig = MontySolrSetup.getMontySolrHome()
    + "/contrib/examples/adsabs/server/solr/collection1/conf/schema.xml";

    File newConfig;
    try {

      // hand-curated synonyms
      File curatedSynonyms = createTempFile(new String[]{
          "ABBOT, CHARLES GREELEY;ABBOTT, CHARLES GREELEY",
          "ABDEL AZIZ BAKRY, A;BAKRY, A",
          "ACHUTBHAN, P;ACHUTHAN, P",
          "ADAMUT, I A;ADAMUTI, I A",
          "ADJABSCHIRZADEH, A;ADJABSHIRZADEH, A",
          "AGARWAL, S;AGGARWAL, S",
          "AGUILAR CHIU, L A;AGUILAR, L A",
          "AITMUHAMBETOV, A A;AITMUKHAMBETOV, A A",
          "AL MLEAKY, Y M; ALMLEAKY, Y M",
          "ALEXEENKO, V V;ALEXEYENKO, V V",
          "ALFONSO, JULIA;ALFONSO-GARZON, JULIA",
          "ALLEN, LYNNE;ALLEN, R LYNNE;JONES, LYNNE;JONES, R LYNNE", // until here copied from: /proj/ads/abstracts/config/author.syn.new
          "ARAGON SALAMANCA, A;ARAGON-SALAMANCA, A;ARAGON, A;SALAMANCA, A", // copied from: /proj/ads/abstracts/config/author.syn
          "ADAMŠuk, m; ADAMGuk, m;ADAMČuk, m",  // hand-made additions
          "MÜLLER, A WILLIAM;MÜLLER, A BILL",
          "MÜLLER, WILLIAM;MÜLLER, BILL",
          "JONES, CHRISTINE;FORMAN, CHRISTINE", // the famous post-synonym expansion
          "DE ZEEUW, TIM=>DE ZEEUW, P TIM",
          "DE ZEEUW, P TIM=>DE ZEEUW, TIM;DE ZEEUW,",
          "grant, carolyn s; stern grant, carolyn; stern, carolyn p",
          "orlitova, ivana; stoklasova, ivana",
          "orlitova,; stoklasova,"
      });

      // automatically harvested variations of author names (collected during indexing)
      // it will be enriched by the indexing
      File generatedTransliterations = createTempFile(formatSynonyms(new String[]{
          "wyrzykowsky, l=>wyrzykowski, l;wyrzykowski, ł",
          "ADAMCHuk, m => ADAMČuk, m",
          "ADAMCuk, m => ADAMČuk, m",
          "ADAMCZuk, m => ADAMČuk, m",
          //"ADAMCHuk, m K=> ADAMČuk, m K",  => deactivated for test purposes, see <surname>, <1> <2> use case
          //"ADAMCuk, m K=> ADAMČuk, m K", => deactivated for test purposes, see <surname>, <1> <2> use case
          "ADAMCUK, A B=> ADAMČUK, A B",
          "ADAMCHUK, A B=> ADAMČUK, A B",
          "ADAMCZUK, A B=> ADAMČUK, A B",
          "ADAMCHuk, mOLJA => ADAMČuk, mOLJA",
          "ADAMCuk, mOLJA => ADAMČuk, mOLJA",
          "ADAMCZuk, mOLJA => ADAMČuk, mOLJA",
          "ADAMCHuk, mOLJA K=> ADAMČuk, mOLJA K",
          "ADAMCuk, mOLJA K=> ADAMČuk, mOLJA K",
          "ADAMCZuk, mOLJA K=> ADAMČuk, mOLJA K",
          "ADAMCHUK, => ADAMČUK,",
          "ADAMCUK,=> ADAMČUK,",
          "ADAMCZUK, => ADAMČUK,", // this one is added by hand (no automated transliteration)
          "MULLER, WILLIAM => MÜLLER, WILLIAM",
          "MUELLER, WILLIAM => MÜLLER, WILLIAM",
          "Boser,=>Böser,",
          "Boser, S=>Böser, S",
          "Gonzaelez Alfonso,=>González Alfonso,",
          "Gonzaelez Alfonso, E=>González Alfonso, E",
          "Gonzalez Alfonso, E=>González Alfonso, E",
          "Chyelkovae,=>Chýlková,",
          "stoklasova,=>stoklasová,",
          "orlitova,=>orlitová,"
      }
      ));


      File newSchema = duplicateModify(new File(schemaConfig), 
          "synonyms=\"author_curated.synonyms\"", "synonyms=\"" + curatedSynonyms.getAbsolutePath().replace('\\', '/') + "\"",
          "synonyms=\"author_generated.translit\"", "synonyms=\"" + generatedTransliterations.getAbsolutePath().replace('\\', '/') + "\""
      );
      return newSchema.getAbsolutePath();

    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException(e.getMessage());
    }

  }

  
  @Override 
  public void setUp() throws Exception {
  	super.setUp();
  	
  	
    assertU(adoc(F.ID, "1", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk,"));
    assertU(adoc(F.ID, "2", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, M."));
    assertU(adoc(F.ID, "3", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Marel"));
    assertU(adoc(F.ID, "4", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja"));
    assertU(adoc(F.ID, "5", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja Karel"));
    assertU(adoc(F.ID, "6", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, M Karel"));
    assertU(adoc(F.ID, "7", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Molja K"));
    assertU(adoc(F.ID, "8", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, M K"));
    assertU(adoc(F.ID, "9", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Karel Molja"));
    assertU(adoc(F.ID, "10", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Karel M"));
    assertU(adoc(F.ID, "11", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, K Molja"));
    
    assertU(adoc(F.ID, "20", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamcuk,"));
    assertU(adoc(F.ID, "21", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamcuk, M."));
    assertU(adoc(F.ID, "22", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamcuk, Marel"));
    assertU(adoc(F.ID, "23", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamcuk, Molja"));
    assertU(adoc(F.ID, "24", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamcuk, Molja Karel"));
    assertU(adoc(F.ID, "25", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamcuk, M Karel"));
    assertU(adoc(F.ID, "26", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamcuk, Molja K"));
    assertU(adoc(F.ID, "27", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamcuk, M K"));
    assertU(adoc(F.ID, "28", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamcuk, Karel Molja"));
    assertU(adoc(F.ID, "29", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamcuk, Karel M"));
    assertU(adoc(F.ID, "30", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamcuk, K Molja"));
    
    assertU(adoc(F.ID, "40", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamchuk,"));
    assertU(adoc(F.ID, "41", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamchuk, M."));
    assertU(adoc(F.ID, "42", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamchuk, Marel"));
    assertU(adoc(F.ID, "43", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamchuk, Molja"));
    assertU(adoc(F.ID, "44", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamchuk, Molja Karel"));
    assertU(adoc(F.ID, "45", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamchuk, M Karel"));
    assertU(adoc(F.ID, "46", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamchuk, Molja K"));
    assertU(adoc(F.ID, "47", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamchuk, M K"));
    assertU(adoc(F.ID, "48", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamchuk, Karel Molja"));
    assertU(adoc(F.ID, "49", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamchuk, Karel M"));
    assertU(adoc(F.ID, "50", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamchuk, K Molja"));
    
    assertU(adoc(F.ID, "60", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamguk,"));
    assertU(adoc(F.ID, "61", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamguk, M."));
    assertU(adoc(F.ID, "62", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamguk, Marel"));
    assertU(adoc(F.ID, "63", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamguk, Molja"));
    assertU(adoc(F.ID, "64", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamguk, Molja Karel"));
    assertU(adoc(F.ID, "65", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamguk, M Karel"));
    assertU(adoc(F.ID, "66", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamguk, Molja K"));
    assertU(adoc(F.ID, "67", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamguk, M K"));
    assertU(adoc(F.ID, "68", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamguk, Karel Molja"));
    assertU(adoc(F.ID, "69", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamguk, Karel M"));
    assertU(adoc(F.ID, "70", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamguk, K Molja"));
    
    assertU(adoc(F.ID, "80", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamshuk,"));
    assertU(adoc(F.ID, "81", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamshuk, M."));
    assertU(adoc(F.ID, "82", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamshuk, Marel"));
    assertU(adoc(F.ID, "83", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamshuk, Molja"));
    assertU(adoc(F.ID, "84", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamshuk, Molja Karel"));
    assertU(adoc(F.ID, "85", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamshuk, M Karel"));
    assertU(adoc(F.ID, "86", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamshuk, Molja K"));
    assertU(adoc(F.ID, "87", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamshuk, M K"));
    assertU(adoc(F.ID, "88", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamshuk, Karel Molja"));
    assertU(adoc(F.ID, "89", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamshuk, Karel M"));
    assertU(adoc(F.ID, "90", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamshuk, K Molja"));

    assertU(adoc(F.ID, "100", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Müller, William"));
    assertU(adoc(F.ID, "101", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Mueller, William"));
    
    assertU(adoc(F.ID, "110", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Jones, Christine"));
    assertU(adoc(F.ID, "111", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Jones, C"));
    assertU(adoc(F.ID, "112", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Forman, Christine"));
    assertU(adoc(F.ID, "113", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Forman, C"));
    assertU(adoc(F.ID, "114", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Jones, Christopher"));
    assertU(adoc(F.ID, "115", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Jones, C"));
    assertU(adoc(F.ID, "116", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Forman, Christopher"));
    assertU(adoc(F.ID, "117", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Forman, C"));
    //"ALLEN, LYNNE;ALLEN, R LYNNE;JONES, LYNNE;JONES, R LYNNE"
    assertU(adoc(F.ID, "120", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Allen, Lynne"));
    assertU(adoc(F.ID, "121", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Allen, L"));
    assertU(adoc(F.ID, "122", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Allen, R Lynne"));
    assertU(adoc(F.ID, "123", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Allen, R L"));
    assertU(adoc(F.ID, "124", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Jones, Lynne"));
    assertU(adoc(F.ID, "125", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Jones, L"));
    assertU(adoc(F.ID, "126", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Jones, R Lynne"));
    assertU(adoc(F.ID, "127", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Jones, R L"));
    
    assertU(adoc(F.ID, "130", F.BIBCODE, "xxxxxxxxxxxxx", 
        F.AUTHOR, "Author, A",
        F.AUTHOR, "Author, B",
        F.AUTHOR, "Author, C"
    ));
    
    assertU(adoc(F.ID, "200", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Lee, H C"));
    assertU(adoc(F.ID, "201", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Lee, H-C"));
    assertU(adoc(F.ID, "202", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Lee, Harwin-C"));
    assertU(adoc(F.ID, "203", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Lee, Harwin-Costa"));
    
    assertU(adoc(F.ID, "210", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Pinilla-Alonso")); // just surname
    assertU(adoc(F.ID, "211", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Pinilla-Alonso,"));
    assertU(adoc(F.ID, "212", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Pinilla-Alonso, B"));
    assertU(adoc(F.ID, "213", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Pinilla-Alonso, Brava"));
    assertU(adoc(F.ID, "214", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Pinilla-Alonso, Borat"));
    assertU(adoc(F.ID, "215", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Pinilla-Alonso, Amer"));
    
    assertU(adoc(F.ID, "220", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "van Dokkum"));
    assertU(adoc(F.ID, "221", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "van Dokkum,"));
    assertU(adoc(F.ID, "222", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "van Dokkum, H"));
    assertU(adoc(F.ID, "223", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "van Dokkum, Hector"));
    assertU(adoc(F.ID, "224", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "van Dokkum, Hiatus"));
    assertU(adoc(F.ID, "225", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "van Dokkum, Romulus"));
    
    assertU(adoc(F.ID, "230", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Böser", "first_author", "Böser, S"));
    assertU(adoc(F.ID, "231", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Böser, S"));
    assertU(adoc(F.ID, "232", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Boser, S"));
    assertU(adoc(F.ID, "233", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Boser,"));
    
    assertU(adoc(F.ID, "300", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Gopal-Krishna,"));
    assertU(adoc(F.ID, "301", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Gopal-Krishna, Jewell"));
    assertU(adoc(F.ID, "302", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Gopal-Krishna, J"));
    
    assertU(adoc(F.ID, "400", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Moon, Dae-Sik"));
    assertU(adoc(F.ID, "401", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Moon, Dae- Sik"));
    assertU(adoc(F.ID, "402", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Moon, D. -S."));
    assertU(adoc(F.ID, "403", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Moon, D."));
    assertU(adoc(F.ID, "404", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Moon, D"));
    assertU(adoc(F.ID, "405", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Moon, D. S."));
    assertU(adoc(F.ID, "406", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Moon, Dae S."));
    assertU(adoc(F.ID, "407", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Moon, Dae S"));
    assertU(adoc(F.ID, "408", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Moon, D Sik"));
    assertU(adoc(F.ID, "409", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Moon, D-Sik"));
    
    
    assertU(adoc(F.ID, "500", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "González Alfonso, E"));
    assertU(adoc(F.ID, "501", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Gonzaelez Alfonso, E"));
    assertU(adoc(F.ID, "502", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Gonzalez Alfonso, E"));
    assertU(adoc(F.ID, "503", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "González-Alfonso, E"));
    assertU(adoc(F.ID, "504", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Gonzaelez-Alfonso, E"));
    assertU(adoc(F.ID, "505", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Gonzalez-Alfonso, E"));
    assertU(adoc(F.ID, "506", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "González, Alfonso"));
    assertU(adoc(F.ID, "507", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Gonzaelez, Alfonso"));
    assertU(adoc(F.ID, "508", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Gonzalez, Alfonso"));
    
    assertU(adoc(F.ID, "600", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Foo, Bar|Other, Name|" + '\u8349',
                          F.AUTHOR, "Baz, Baz|\\u8349")); // 草
    
    assertU(adoc(F.ID, "601", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Wyrzykowski, Ł"));
    
    assertU(commit());
    
    //dumpDoc(null, "id", "author");

    // persist the transliteration map after new docs were indexed
    // and reload synonym chain harvested during indexing
    Analyzer iAnalyzer = h.getCore().getLatestSchema().getIndexAnalyzer();
    Analyzer qAnalyzer = h.getCore().getLatestSchema().getQueryAnalyzer();

    TokenStream iAuthor = iAnalyzer.tokenStream("author", new StringReader(""));
    TokenStream qAuthor = qAnalyzer.tokenStream("author", new StringReader(""));


    iAuthor.close();
    qAuthor.close();
    
    // TODO: force reload of the synonym map
    //h.getCoreContainer().reload("collection1");

  }

  
  public void xtestX() throws Exception {
  	String expected = "author:adamčuk, molja k | author:adamčuk, molja k* " +
    		       "author:adamčuk, m k | author:adamčuk, m k* " +
    		       "author:adamčuk, molja " + // ! | author:adamčuk, molja *
    		       "author:adamčuk, m " + // ! | author:adamčuk, m*
    		       "author:adamčuk, " +
    		       "author:adamcuk, molja k | author:adamcuk, molja k* " +
    		       "author:adamcuk, m k | author:adamcuk, m k* " +
    		       "author:adamcuk, molja " + // ! | author:adamcuk, molja * 
    		       "author:adamcuk, m " + // ! | author:adamcuk, m* 
    		       "author:adamcuk, " +
    		       "author:adamchuk, molja k | author:adamchuk, molja k* " +
    		       "author:adamchuk, m k | author:adamchuk, m k* " +
    		       "author:adamchuk, molja " + // ! | author:adamchuk, molja * 
    		       "author:adamchuk, m " + // ! | author:adamchuk, m*
    		       "author:adamchuk,";
  	
    testAuthorQuery("\"adamczuk, molja k\"", expected + 
                          " | author:adamczuk, molja k | author:adamczuk, molja k* | author:adamczuk, m k | author:adamczuk, m k* | author:adamczuk, molja | author:adamczuk, m | author:adamczuk,", 
                          "//*[@numFound='21']");
    
  }
  
  public void testAuthorParsingUseCases() throws Exception {
  	
  	assertQueryEquals(req("q", "author:acco*"), "author:acco*", WildcardQuery.class);
  	assertQueryEquals(req("q", "author:Adamč*"), "author:adamč*", WildcardQuery.class);
  	
  	testAuthorQuery("Adamč*",
  			"adamč*",
  			"//*[@numFound='11']");
    
    // multiple synonyms in the file are separated with semicolon
    testAuthorQuery("\"wyrzykowsky, l\"",
        "wyrzykowski, | wyrzykowski, l | wyrzykowski, l* | wyrzykowski, ł | wyrzykowski, ł* | wyrzykowskii, | wyrzykowskii, l | wyrzykowskii, l* | wyrzykowskii, ł | wyrzykowskii, ł* | wyrzykowskij, | wyrzykowskij, l | wyrzykowskij, l* | wyrzykowskij, ł | wyrzykowskij, ł* | wyrzykowskiy, | wyrzykowskiy, l | wyrzykowskiy, l* | wyrzykowskiy, ł | wyrzykowskiy, ł* | wyrzykowsky, | wyrzykowsky, l | wyrzykowsky, l* | wyrzykowsky, ł | wyrzykowsky, ł* | wyrzykowskyi, | wyrzykowskyi, l | wyrzykowskyi, l* | wyrzykowskyi, ł | wyrzykowskyi, ł*",
        "//*[@numFound='1']");
    
    // multiple names
    testAuthorQuery("\"other, name\"",
        "author:other, name | author:other, name * | author:other, n | author:other, n * | author:other,",
        "//*[@numFound='1']");
    testAuthorQuery("\u8349",
        "author:cao,* | author: cao, | author:\u8349, | author:\u8349,*", // | author:草, | author:草,*
        "//*[@numFound='1']");
    testAuthorQuery("\"baz, baz\"",
        "author:baz, baz | author:baz, baz * | author:baz, b | author:baz, b * | author:baz,",
        "//*[@numFound='1']");
    
    // should not find anything, even though the names are there indexed next to each other
    assertQ(req("q", "author:\"foo, * other, *\""),
        "//*[@numFound='0']"
    );
    assertQ(req("q", "author:\"foo, *\""),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='600']"
    );
    assertQ(req("q", "author:\"other, *\""),
        "//*[@numFound='1']",
        "//doc/int[@name='recid'][.='600']"
    );
    
    
    // 6-length author names; and in the second case of 'hillary' we should not allow m[^ ]* h[^ ]* d.*
    // but only m[^ ]* hillary d.*
    testAuthorQuery("\"van der Wiel, M. H. D.\"",
        "author:van der wiel, m h d | author:van der wiel, m h d* | author:/van der wiel, m[^ ]*/ | author:/van der wiel, m[^ ]* h[^ ]*/ | author:/van der wiel, m[^ ]* h[^ ]* d.*/ | author:van der wiel, m | author:van der wiel,",
        "//*[@numFound='0']");
    testAuthorQuery("\"van der Wiel, M. Hillary D.\"",
        "author:van der wiel, m hillary d | author:van der wiel, m hillary d* | author:/van der wiel, m[^ ]*/ | author:/van der wiel, m[^ ]* hillary d.*/ | author:van der wiel, m h d | author:van der wiel, m h d* | author:/van der wiel, m[^ ]* h d.*/ | author:van der wiel, m | author:van der wiel,",
        "//*[@numFound='0']");
    
    // stoklasova == orlitova == orlitová == stoklasová; it should produce the same query
    // wrong output (missing is "orlitová, *")
    // | author:stoklasova, | author:orlitova, ivana | author:stoklasova, i | author:stoklasova, ivana | author:orlitova, i | author:stoklasova,* | author:stoklasová, | author:stoklasová,* | author:stoklasovae, | author:stoklasovae,*",
    // expected:
    // | author:orlitova, | author:stoklasová,* | author:orlitova, ivana | author:orlitova, ivana * | author:stoklasova, i | author:stoklasova, i * | author:stoklasova, ivana | author:stoklasova, ivana * | author:orlitova, i | author:orlitova, i * | author:orlitova,* | author:stoklasova, | author:stoklasova,* | author:orlitová, | author:orlitová,* | author:orlitovae, | author:orlitovae,* | author:stoklasová, | author:stoklasovae, | author:stoklasovae,*
    // TODO: optimize the query, remove the clauses that match the doc twice
    
    testAuthorQuery("\"stoklasova\"", 
        "author:orlitova, | author:stoklasová, | author:orlitova, ivana | author:stoklasova, i | author:stoklasova, ivana | author:orlitova, i | author:orlitova,* | author:stoklasova, | author:stoklasova,* | author:orlitová, | author:orlitová,* | author:orlitovae, | author:orlitovae,* | author:stoklasová,* | author:stoklasovae, | author:stoklasovae,*", 
        "//*[@numFound='0']");
    testAuthorQuery("\"orlitova\"", 
        "author:orlitova, | author:stoklasová, | author:orlitova, ivana | author:stoklasova, i | author:stoklasova, ivana | author:orlitova, i | author:orlitova,* | author:stoklasova, | author:stoklasova,* | author:orlitová, | author:orlitová,* | author:orlitovae, | author:orlitovae,* | author:stoklasová,* | author:stoklasovae, | author:stoklasovae,*", 
        "//*[@numFound='0']");
    testAuthorQuery("\"orlitová\"", 
        "author:orlitova, | author:stoklasová, | author:orlitova, ivana | author:stoklasova, i | author:stoklasova, ivana | author:orlitova, i | author:orlitova,* | author:stoklasova, | author:stoklasova,* | author:orlitová, | author:orlitová,* | author:orlitovae, | author:orlitovae,* | author:stoklasová,* | author:stoklasovae, | author:stoklasovae,*", 
        "//*[@numFound='0']");
    testAuthorQuery("\"stoklasová\"", 
        "author:orlitova, | author:stoklasová, | author:orlitova, ivana | author:stoklasova, i | author:stoklasova, ivana | author:orlitova, i | author:orlitova,* | author:stoklasova, | author:stoklasova,* | author:orlitová, | author:orlitová,* | author:orlitovae, | author:orlitovae,* | author:stoklasová,* | author:stoklasovae, | author:stoklasovae,*", 
        "//*[@numFound='0']");
    
    // searching for ascii version finds also the utf (for hyphenated names)
    testAuthorQuery("\"chyelkovae\"", 
        "author:chyelkovae, | author:chyelkovae,* | author:chýlková, | author:chýlková,* | author:chylkova, | author:chylkova,*", 
        "//*[@numFound='0']");
    testAuthorQuery("\"Gonzalez-Alfonso, E\"", 
        "author:gonzalez alfonso, e | author:gonzalez alfonso, e* | author:gonzalez alfonso, | author:gonzález alfonso, e | author:gonzález alfonso, e* | author:gonzález alfonso, | author:gonzaelez alfonso, e | author:gonzaelez alfonso, e* | author:gonzaelez alfonso,", 
        "//*[@numFound='6']");
    testAuthorQuery("\"Gonzalez Alfonso, E\"", 
        "author:gonzalez alfonso, e | author:gonzalez alfonso, e* | author:gonzalez alfonso, | author:gonzález alfonso, e | author:gonzález alfonso, e* | author:gonzález alfonso, | author:gonzaelez alfonso, e | author:gonzaelez alfonso, e* | author:gonzaelez alfonso,", 
        "//*[@numFound='6']");
    
    // issue #57: https://github.com/romanchyla/montysolr/issues/57
    testAuthorQuery("\"Moon, Dae-Sik\"", 
        "author:moon, dae sik | author:moon, dae sik * | author:moon, d sik | author:moon, d sik * | author:moon, dae s | author:moon, dae s * | author:moon, d s | author:moon, d s * | author:moon, dae | author:moon, d | author:moon,", 
        "//*[@numFound='10']");
    
    /**
     * will miss: Moon, Dae-Sik; Moon, Dae -Sik
     * 
     * ie where both parts are fully spelled; but it will find 'dae, s' and 'd sik'
     * this logic seems defficient
     * */
    testAuthorQuery("\"Moon, D. -S.\"", 
        //"author:moon, d s | author:moon, d s* | author:/moon, d[^ ]* s/ | author:/moon, d[^ ]* s .*/ | author:moon, d | author:moon,",
        "author:moon, d s | author:moon, d s* | author:/moon, d[^ ]*/ | author:/moon, d[^ ]* s.*/ | author:moon, d | author:moon,",
        "//*[@numFound='10']");
    
    
  	// test the definition that is in the live synonym file
  	// we use this for blackbox - to verify deployment is using
  	// synonym translation
  	testAuthorQuery(
        "\"grant, carolyn s\"", 
        		"author:grant, carolyn s " +
        		"author:grant, carolyn s* " +
        		"author:grant, c s " +
        		"author:grant, c s* " +
        		"author:grant, carolyn " +
        		"author:grant, c " +
        		"author:grant, " +
        		"author:stern grant, carolyn " +
        		"author:stern grant, c " +
        		"author:stern grant, " +
        		"author:stern, carolyn p " +
        		"author:stern, carolyn p* " +
        		"author:stern, c p " +
        		"author:stern, c p* " +
        		"author:stern, carolyn " +
        		"author:stern, c " +
        		"author:stern,",
        		"//*[@numFound='0']"
        		);
        		
  	
  	testAuthorQuery(
        "Gopal-Krishna", 
        		"author:gopal krishna, | author:gopal krishna,*",
        		"//*[@numFound='3']",
    		"\"Gopal Krishna,\"",
        		"author:gopal krishna, | author:gopal krishna,*",
        		"//*[@numFound='3']",
    		"\"Gopal Krishna\"",
        		"author:gopal krishna, | author:gopal krishna,* | author:krishna, gopal | author:krishna, gopal * | author:krishna, g | author:krishna, g * | author:krishna, | author:krishna,*",
        		"//*[@numFound='3']"
        		);
  	
  	//#487 - these author names should parse the same; Maestro, V was
  	// picked by the python name parser (V removed); Boyjian had problems
  	// with expansion (python name parser was not applied there)
  	testAuthorQuery(
        "Maestro\\,\\ V", 
        		"author:maestro, v | author:maestro, v* | author:maestro,",
        		"//*[@numFound='0']",
    		"V\\ Maestro", 
            "author:v maestro, | author:v maestro,* | author:maestro, v | author:maestro, v* | author:maestro, v * | author:maestro, | author:maestro,*",
        		//"author:maestro, v | author:maestro, v* | author:maestro,",
        		"//*[@numFound='0']"
        		);
  	testAuthorQuery(
        "Boyajian\\,\\ T", 
        		"author:boyajian, t | author:boyajian, t* | author:boyajian,",
        		"//*[@numFound='0']",
    		"T\\ Boyajian", 
        		"author:t boyajian, | author:t boyajian,* | author:boyajian, t | author:boyajian, t* | author:boyajian, t * | author:boyajian, | author:boyajian,*",
        		"//*[@numFound='0']"
        		);
  	
  	
  	
  	// first is considered a title (but when the only thing we have, it will be searched as surname)
  	testAuthorQuery(
        "first", 
        		"author:first, | author:first,*",
        		"//*[@numFound='0']"
        		);
  	testAuthorQuery(
        "goodman", 
            "author:goodman, | author:goodman,*",
            "//*[@numFound='0']"
            );
  	
    // 'xxx' will be removed from the author (at least in the modified version)
	  assertQueryEquals(req("defType", "aqp", "q", "author:\"accomazzi, alberto, xxx.\""), 
        "author:accomazzi, alberto, xxx | author:accomazzi, alberto, xxx * | author:accomazzi, alberto | author:accomazzi, alberto * | author:accomazzi, a xxx | author:accomazzi, a xxx * | author:accomazzi, alberto, x | author:accomazzi, alberto, x * | author:accomazzi, a x | author:accomazzi, a x * | author:accomazzi, alberto, | author:accomazzi, alberto, * | author:accomazzi, a | author:accomazzi, a * | author:accomazzi,",
        DisjunctionMaxQuery.class);
    
    
    
  	// #362 - smartly handle o' sulliva (done in the Pythonic name parser)
  	// I'm not sure whether we should index the apostrophe, maybe it should
  	// be replaced by space ?
  	testAuthorQuery(
        "\"o' sullivan\"", 
        		"author:o sullivan, | author:o sullivan,*",
        		"//*[@numFound='0']",
    		"\"o'sullivan\"", 
        		"author:o sullivan, | author:o sullivan,*",
        		"//*[@numFound='0']",
    		"\"o' sullivan, ji\"", 
        		"author:o sullivan, ji | author:o sullivan, ji * | author:o sullivan, j | author:o sullivan, j * | author:o sullivan,",
        		"//*[@numFound='0']"
        		);
  	// funny author names
  	testAuthorQuery(
    		"\"o'sullivan\"", 
        		"author:o sullivan, | author:o sullivan,*",
        		"//*[@numFound='0']",
    		"\"o' sullivan\"",
        		"author:o sullivan, | author:o sullivan,*",
        		"//*[@numFound='0']"
        		);
  	
  	testAuthorQuery(
    		"Dall\\'oglio",
        		"author:dall oglio, | author:dall oglio,*",
        		"//*[@numFound='0']",
    		"Antonella\\ Dall\\'Oglio",
        		"author:antonella dall oglio, | author:antonella dall oglio,* | author:dall oglio, antonella | author:dall oglio, antonella * | author:dall oglio, a | author:dall oglio, a * | author:dall oglio, | author:dall oglio,*",
        		"//*[@numFound='0']"
        		);
  	
  	testAuthorQuery(
    		"\"t' Hooft, Sullivan\"",
        		"author:t hooft, sullivan | author:t hooft, sullivan * | author:t hooft, s | author:t hooft, s * | author:t hooft,",
        		"//*[@numFound='0']"
        		);

  	// hmmm.. these regexes must be slow; we should not generate them
  	// also, before #487, the first query would generate:
    //"author:kao, p ing tzu | author:kao, p ing tzu * | author:kao, p i tzu | author:kao, p i tzu * | author:kao, p ing t | author:kao, p ing t * | author:kao, p i t | author:kao, p i t * | author:kao, p | author:kao,",
  	testAuthorQuery(
    		"\"P'ING-TZU KAO\"",
            "author:p ing tzu kao, "
            + "author:p ing tzu kao,* "
            + "author:kao, p ing tzu "
            + "author:kao, p ing tzu * "
            + "author:/kao, p[^ ]*/ "
            + "author:/kao, p[^ ]* ing tzu/ "
            + "author:/kao, p[^ ]* ing tzu .*/ "
            + "author:kao, p i tzu "
            + "author:kao, p i tzu * "
            + "author:/kao, p[^ ]* i tzu/ "
            + "author:/kao, p[^ ]* i tzu .*/ "
            + "author:kao, p ing t "
            + "author:kao, p ing t * "
            + "author:/kao, p[^ ]* ing t/ "
            + "author:/kao, p[^ ]* ing t .*/ "
            + "author:kao, p i t "
            + "author:kao, p i t * "
            + "author:/kao, p[^ ]* i t/ "
            + "author:/kao, p[^ ]* i t .*/ "
            + "author:kao, p "
            + "author:kao, p * "
            + "author:kao, "
            + "author:kao,*",
        		"//*[@numFound='0']"
            );
  	testAuthorQuery(
    		"\"Kao, P'ing-Tzu\"",
        		"author:kao, p ing tzu "
        		+ "author:kao, p ing tzu * "
        		+ "author:/kao, p[^ ]*/ "
        		+ "author:/kao, p[^ ]* ing tzu/ "
        		+ "author:/kao, p[^ ]* ing tzu .*/ "
        		+ "author:kao, p i tzu "
        		+ "author:kao, p i tzu * "
        		+ "author:/kao, p[^ ]* i tzu/ "
        		+ "author:/kao, p[^ ]* i tzu .*/ "
        		+ "author:kao, p ing t "
        		+ "author:kao, p ing t * "
        		+ "author:/kao, p[^ ]* ing t/ "
        		+ "author:/kao, p[^ ]* ing t .*/ "
        		+ "author:kao, p i t "
        		+ "author:kao, p i t * "
        		+ "author:/kao, p[^ ]* i t/ "
        		+ "author:/kao, p[^ ]* i t .*/ "
        		+ "author:kao, p | author:kao,",
        		"//*[@numFound='0']"
        		);
  	
  	
  	
  	// what happens we receive very long string (non-author thing)
  	testAuthorQuery(
        "\"purpose of this review is to bridge the gap between\"", 
        		"MatchNoDocsQuery(\"\")",
        		"//*[@numFound='0']"
        		);
  	
  	// making sure also other fields are being parsed properly
  	author_field = "first_author";
  	testAuthorQuery(
        "\"Boser, S\"", 
        		"first_author:boser, s | first_author:boser, s* | first_author:boser, | first_author:böser, s | first_author:böser, s* | first_author:böser, | first_author:boeser, s | first_author:boeser, s* | first_author:boeser,",
        		"//*[@numFound='1']",
    		"\"Böser, S\"", 
        		"first_author:böser, s | first_author:böser, s* | first_author:böser, | first_author:boeser, s | first_author:boeser, s* | first_author:boeser, | first_author:boser, s | first_author:boser, s* | first_author:boser,",
        		"//*[@numFound='1']"
        		);
  	
  	
  	// back to the standard: author
  	author_field = "author";
  	testAuthorQuery(
        "\"Boser, S\"", 
        		"author:böser, s | author:böser, s* | author:böser, | author:boeser, s | author:boeser, s* | author:boeser, | author:boser, s | author:boser, s* | author:boser,",
        		"//*[@numFound='4']",
    		"\"Böser, S\"", 
        		"author:böser, s | author:böser, s* | author:böser, | author:boeser, s | author:boeser, s* | author:boeser, | author:boser, s | author:boser, s* | author:boser,",
        		"//*[@numFound='4']"
        		);
        
    
  	// reported by Alex
  	// [author:"van Dokkum" bibstem:"Natur" | author:"Conroy" ]
    // doesn't return any results, even though it should yield 2010Natur.468..940V.
    testAuthorQuery(
        "\"van Dokkum\"", 
        				   "author:van dokkum, | author:van dokkum,*",
                   "//*[@numFound='6']",
                   // "van Dokkum" numFound=6
                   // 220	van Dokkum             221	van Dokkum,            222	van Dokkum, H          
                   // 223	van Dokkum, Hector     224	van Dokkum, Hiatus     225	van Dokkum, Romulus    
        "\"van Dokkum,\"", 
        				   "author:van dokkum, | author:van dokkum,*",
                   "//*[@numFound='6']",
                   // "van Dokkum," numFound=6
                   // 220	van Dokkum             221	van Dokkum,            222	van Dokkum, H          
                   // 223	van Dokkum, Hector     224	van Dokkum, Hiatus     225	van Dokkum, Romulus    
			   "\"van Dokkum, H\"", 
       				   "author:van dokkum, h | author:van dokkum, h* | author:van dokkum,",
                  "//*[@numFound='5']",
                  // "van Dokkum, H" numFound=5
                  // 220	van Dokkum             221	van Dokkum,            222	van Dokkum, H          
                  // 223	van Dokkum, Hector     224	van Dokkum, Hiatus        
        "\"van Dokkum, H.\"", 
       				   "author:van dokkum, h | author:van dokkum, h* | author:van dokkum,",
                  "//*[@numFound='5']",
                  // "van Dokkum, H." numFound=5
                  // 220	van Dokkum             221	van Dokkum,            222	van Dokkum, H          
                  // 223	van Dokkum, Hector     224	van Dokkum, Hiatus                       
        "\"van Dokkum, Romulus\"", 
                  "author:van dokkum, romulus | author:van dokkum, romulus * | author:van dokkum, r | author:van dokkum, r * | author:van dokkum,",
                  "//*[@numFound='3']"
                  // "van Dokkum, Romulus" numFound=3
                  // 220	van Dokkum             221	van Dokkum,            225	van Dokkum, Romulus             				   
       );

    
    //bug #324
    testAuthorQuery(
         "Pinilla-Alonso", 
        				   "author:pinilla alonso, | author:pinilla alonso,*",
                   "//*[@numFound='6']",
                   // Pinilla-Alonso numFound=6
                   // 210	Pinilla-Alonso         211	Pinilla-Alonso,        212	Pinilla-Alonso, B      
                   // 213	Pinilla-Alonso, Brava  214	Pinilla-Alonso, Borat  215	Pinilla-Alonso, Amer  
         "\"Pinilla Alonso\"", 
         						"author:pinilla alonso, | author:pinilla alonso,* | author:alonso, pinilla | author:alonso, pinilla * | author:alonso, p | author:alonso, p * | author:alonso, | author:alonso,*",
                    "//*[@numFound='6']",
                    // Pinilla-Alonso numFound=6
                    // 210	Pinilla-Alonso         211	Pinilla-Alonso,        212	Pinilla-Alonso, B      
                    // 213	Pinilla-Alonso, Brava  214	Pinilla-Alonso, Borat  215	Pinilla-Alonso, Amer
         "\"Pinilla Alonso,\"", 
         				   "author:pinilla alonso, | author:pinilla alonso,*",
                    "//*[@numFound='6']",
                    // Pinilla-Alonso numFound=6
                    // 210	Pinilla-Alonso         211	Pinilla-Alonso,        212	Pinilla-Alonso, B      
                    // 213	Pinilla-Alonso, Brava  214	Pinilla-Alonso, Borat  215	Pinilla-Alonso, Amer
			   "\"Pinilla-Alonso, B\"", 
        				   "author:pinilla alonso, b | author:pinilla alonso, b* | author:pinilla alonso,",
                   "//*[@numFound='5']",
                   // Pinilla-Alonso numFound=6
                   // 210	Pinilla-Alonso         211	Pinilla-Alonso,        212	Pinilla-Alonso, B      
                   // 213	Pinilla-Alonso, Brava  214	Pinilla-Alonso, Borat   
         "\"Pinilla Alonso, B.\"", 
        				   "author:pinilla alonso, b | author:pinilla alonso, b* | author:pinilla alonso,",
                   "//*[@numFound='5']",
                   // Pinilla-Alonso numFound=6
                   // 210	Pinilla-Alonso         211	Pinilla-Alonso,        212	Pinilla-Alonso, B      
                   // 213	Pinilla-Alonso, Brava  214	Pinilla-Alonso, Borat                      
         "\"Pinilla-Alonso, Brava\"", 
                   "author:pinilla alonso, brava | author:pinilla alonso, brava * | author:pinilla alonso, b | author:pinilla alonso, b * | author:pinilla alonso,",
                   "//*[@numFound='4']"
                   // Pinilla-Alonso, Brava numFound=4
                   // 210	Pinilla-Alonso         211	Pinilla-Alonso,        212	Pinilla-Alonso, B      
                   // 213	Pinilla-Alonso, Brava         				   
        );

    
    
    // bug: #255
    testAuthorQuery(
        "\"Lee, H-C\"", "author:lee, h c | author:lee, h c* | author:/lee, h[^ ]*/ | author:/lee, h[^ ]* c.*/ | author:lee, h | author:lee,",
                    "//*[@numFound='4']",
                    // Lee, H-C numFound=4
                    // 200 Lee, H C               201  Lee, H-C               202  Lee, Harwin-C                            
                    // 203 Lee, Harwin-Costa  
        "\"Lee, H C\"", "author:lee, h c | author:lee, h c* | author:/lee, h[^ ]*/ | author:/lee, h[^ ]* c.*/ | author:lee, h | author:lee,",
                    "//*[@numFound='4']",
                    // "Lee, H-C" numFound=4
                    // 200 Lee, H C               201  Lee, H-C               202  Lee, Harwin-C
                    // 203 Lee, Harwin-Costa  
        "\"Lee, Harwin C\"", "author:lee, harwin c | author:lee, harwin c* | author:lee, h c | author:lee, h c* | author:lee, harwin | author:lee, h | author:lee,",
                    "//*[@numFound='4']",
                    // Lee, Harwin C numFound=4
                    // 200 Lee, H C               201  Lee, H-C               202  Lee, Harwin-C          
                    // 203 Lee, Harwin-Costa                    
        "\"Lee, Harwin-*\"", "author:lee, harwin-*",
                    "//*[@numFound='0']",
                    // Lee, Harwin-* numFound=0
        "\"Lee, Harwin*\"", "author:lee, harwin*",
                    "//*[@numFound='2']",
                     // Lee, Harwin* numFound=2
                     // 202 Lee, Harwin-C          203  Lee, Harwin-Costa
        "\"Lee, H*\"", "author:lee, h | author:lee, h* | author:lee,",
                    "//*[@numFound='4']"
                     // Lee, Harwin-C numFound=4
                     // 200 Lee, H C               201  Lee, H-C               202  Lee, Harwin-C          
                     // 203 Lee, Harwin-Costa 
    );    
    
    
    // test proper order of authors - ticket: #98
    //System.out.println(h.query(req("q", String.format("%s:130", F.ID))));
    assertQ(req("q", String.format("%s:130", F.ID), "fl", "author"), "//*[@numFound='1']");
    assert h.query(req("q", String.format("%s:130", F.ID), "indent", "false"))
      .contains("<arr name=\"author\"><str>Author, A</str><str>Author, B</str><str>Author, C</str></arr>");
    

  }
  
  public void testAuthorParsingMainLogic() throws Exception {
    /**
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
     *   that way). Combinations are:
     *   
     *     author_exact = 1 + 3
     *     author_nosyn = 1 + 2
     *     author_exact_nosyn = 1  
     *     
     *     
     *   As a general rule, the ADS is trying to get more rather than less.
     *   Here are the examples:
     *     
     * <pre>
     *   query:                  expanded into:
     *   ===============================================================
     *   
     *   kurtz, michael julian -> kurtz, michael julian
     *                            kurtz, michael julian *
     *                            kurtz, michael j
     *                            kurtz, michael j *
     *                            kurtz, m j
     *                            kurtz, m j *
     *                            kurtz, m julian
     *                            kurtz, m julian *
     *                            kurtz, michael  (<- libation to gods of recall)
     *                            kurtz, m        (<- dtto)
     *                            kurtz,          (<- libation #2)
     *   
     *   kurtz, michael j      -> kurtz, michael j*
     *                            kurtz, michael j *
     *                            kurtz, m j*
     *                            kurtz,
     *                            kurtz, michael
     *                            kurtz, m
     *   
     *   kurtz, m julian       -> kurtz, m julian
     *                            kurtz, m julian *
     *                            kurtz, m j *
     *                            kurtz, m j
     *                            kurtz, m
     *                            kurtz,
     *                            kurtz, m\w* julian    (<- happens only for one-letter initials)
     *                            kurtz, m\w* julian .* (dtto)
     *                            kurtz, m\w* j         (dtto)
     *                            kurtz, m\w* j .*      (dtto)
     *   
     *   kurtz, michael        -> kurtz, michael
     *                            kurtz, michael *
     *                            kurtz, m
     *                            kurtz, m *
     *                            kurtz,
     *   
     *   kurtz, m              -> kurtz, m
     *                            kurtz, m* (in fact, these two can become just: kurtz, m*)
     *                            kurtz, 
     *   
     *   kurtz, mi*            -> kurtz, mi*
     *                            kurtz,
     *                           
     *                           
     *                   
     * </pre>
     */



    
    
    
    /*
     * ============================================================
     * Here comes the bloodiest part of the author parsing unittest
     * ============================================================
     * 
     * 
		 Each test case has two branches, one representing the full utf-8 form (with ascii chars),
		 the other the ascii downgraded form. No matter which, the query must be expanded in both
		 cases equally for each testcase

		 Test-cases:

		   <surname>
		   <surname>,
		   <surname>, <1>
		   <surname>, <1name>
		   <surname>, <1name> <2>
		   <surname>, <1name> <2name>
		   <surname>, <1> <2name>
		   <surname>, <1> <2>
		   <surname>, <2>
       <surname>, <2name>
       <surname>, <2name> <1>
       <surname>, <2name> <1name>
       <surname>, <2> <1name>
       <surname>, <2> <1>
       
       <surname>, <1n*>
       <surname>, <1*>
       <surname>, <2n*>
       <surname>, <2*>

		 - transliteration: adamčuk, m --> adamchuk, m;adamcuk, m
     - synonym expansion for: ADAMŠuk, m;ADAMGuk, m;ADAMČuk, m

     */
    
    //testAuthorQuery("\"allen, lynne\"", "xxx", "//*[@numFound='']");
    
    String expected;
    String expected0;

    expected = "author:adamčuk, | author:adamčuk,* " + // query variants added by parser
               "| author:adamchuk, | author:adamchuk,* " +
               "| author:adamcuk, | author:adamcuk,*";


    /**
     * <surname>
     * 
     * upgraded && transliterated 
     * synonym adamšuk IS NOT FOUND because there is no  entry for "adam(č|c|ch)uk" the syn list
     */
    
    testAuthorQuery(
        //"adAMčuk"
    		"adAM\u010duk", expected + " | author:adamguk, m | author:adamčuk, m | author:adamšuk, m", 
        "//*[@numFound='34']"
        // adamčuk numFound=34
        //   1 Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
        //   4 Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
        //   7 Adamčuk, Molja K         8  Adamčuk, M K             9  Adamčuk, Karel Molja   
        //  10 Adamčuk, Karel M        11  Adamčuk, K Molja        20  Adamcuk,               
        //  21 Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
        //  24 Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
        //  27 Adamcuk, M K            28  Adamcuk, Karel Molja    29  Adamcuk, Karel M       
        //  30 Adamcuk, K Molja        40  Adamchuk,               41  Adamchuk, M.           
        //  42 Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
        //  45 Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
        //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M       50  Adamchuk, K Molja      
        //  61 Adamguk, M.
        );
    testAuthorQuery(
        "adAMcuk", expected, "//*[@numFound='33']"
        // adamcuk numFound=33
        //   1 Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
        //   4 Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
        //   7 Adamčuk, Molja K         8  Adamčuk, M K             9  Adamčuk, Karel Molja   
        //  10 Adamčuk, Karel M        11  Adamčuk, K Molja        20  Adamcuk,               
        //  21 Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
        //  24 Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
        //  27 Adamcuk, M K            28  Adamcuk, Karel Molja    29  Adamcuk, Karel M       
        //  30 Adamcuk, K Molja        40  Adamchuk,               41  Adamchuk, M.           
        //  42 Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
        //  45 Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
        //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M       50  Adamchuk, K Molja
        );
    testAuthorQuery(
        "adAMchuk", expected , "//*[@numFound='33']"
        // adamchuk numFound=33
        //   1 Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
        //   4 Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
        //   7 Adamčuk, Molja K         8  Adamčuk, M K             9  Adamčuk, Karel Molja   
        //  10 Adamčuk, Karel M        11  Adamčuk, K Molja        20  Adamcuk,               
        //  21 Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
        //  24 Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
        //  27 Adamcuk, M K            28  Adamcuk, Karel Molja    29  Adamcuk, Karel M       
        //  30 Adamcuk, K Molja        40  Adamchuk,               41  Adamchuk, M.           
        //  42 Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
        //  45 Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
        //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M       50  Adamchuk, K Molja
        );
    testAuthorQuery(
        "adAMczuk", expected + " | author:adamczuk, | author:adamczuk,*", "//*[@numFound='33']"
        // adamczuk numFound=33
        //   1 Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
        //   4 Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
        //   7 Adamčuk, Molja K         8  Adamčuk, M K             9  Adamčuk, Karel Molja   
        //  10 Adamčuk, Karel M        11  Adamčuk, K Molja        20  Adamcuk,               
        //  21 Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
        //  24 Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
        //  27 Adamcuk, M K            28  Adamcuk, Karel Molja    29  Adamcuk, Karel M       
        //  30 Adamcuk, K Molja        40  Adamchuk,               41  Adamchuk, M.           
        //  42 Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
        //  45 Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
        //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M       50  Adamchuk, K Molja      
        );
    testAuthorQuery(
        //"adAMšuk"
        "adAM\u0161uk", "author:adamšuk, | author:adamšuk,* " +
                   "| author:adamshuk, | author:adamshuk,* " +
                   "| author:adamsuk, | author:adamsuk,* " +
                   "| author:adamguk, m | author:adamčuk, m | author:adamšuk, m", 
                   "//*[@numFound='13']"
                   // adamšuk numFound=13
                   //   2 Adamčuk, M.             61  Adamguk, M.             80  Adamshuk,              
                   //  81 Adamshuk, M.            82  Adamshuk, Marel         83  Adamshuk, Molja        
                   //  84 Adamshuk, Molja Karel   85  Adamshuk, M Karel       86  Adamshuk, Molja K      
                   //  87 Adamshuk, M K           88  Adamshuk, Karel Molja   89  Adamshuk, Karel M      
                   //  90 Adamshuk, K Molja   
       );
     testAuthorQuery(
        "adAMguk", "(author:adamguk, | author:adamguk,* " +
                   "| author:adamguk, m | author:adamčuk, m | author:adamšuk, m)", 
                   "//*[@numFound='12']"
                   // adamguk numFound=12
                   //   2 Adamčuk, M.             60  Adamguk,                61  Adamguk, M.            
                   //  62 Adamguk, Marel          63  Adamguk, Molja          64  Adamguk, Molja Karel   
                   //  65 Adamguk, M Karel        66  Adamguk, Molja K        67  Adamguk, M K           
                   //  68 Adamguk, Karel Molja    69  Adamguk, Karel M        70  Adamguk, K Molja                         
    );

    /**
     * <surname>,
     * 
     * upgraded && transliterated 
     * synonym adamšuk IS NOT FOUND because there is no  entry for "adam(č|c|ch)uk" the syn list
     */
    testAuthorQuery(
        "\"adamčuk,\"", expected + " | author:adamguk, m | author:adamčuk, m | author:adamšuk, m", 
        "//*[@numFound='34']",
        // adamčuk numFound=34
        //   1 Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
        //   4 Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
        //   7 Adamčuk, Molja K         8  Adamčuk, M K             9  Adamčuk, Karel Molja   
        //  10 Adamčuk, Karel M        11  Adamčuk, K Molja        20  Adamcuk,               
        //  21 Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
        //  24 Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
        //  27 Adamcuk, M K            28  Adamcuk, Karel Molja    29  Adamcuk, Karel M       
        //  30 Adamcuk, K Molja        40  Adamchuk,               41  Adamchuk, M.           
        //  42 Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
        //  45 Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
        //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M       50  Adamchuk, K Molja      
        //  61 Adamguk, M.     
        "\"adamcuk,\"", expected, "//*[@numFound='33']",
        // adamcuk numFound=33
        //   1 Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
        //   4 Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
        //   7 Adamčuk, Molja K         8  Adamčuk, M K             9  Adamčuk, Karel Molja   
        //  10 Adamčuk, Karel M        11  Adamčuk, K Molja        20  Adamcuk,               
        //  21 Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
        //  24 Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
        //  27 Adamcuk, M K            28  Adamcuk, Karel Molja    29  Adamcuk, Karel M       
        //  30 Adamcuk, K Molja        40  Adamchuk,               41  Adamchuk, M.           
        //  42 Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
        //  45 Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
        //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M       50  Adamchuk, K Molja      
        "\"adamchuk,\"", expected, "//*[@numFound='33']",
        // adamchuk numFound=33
        //   1 Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
        //   4 Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
        //   7 Adamčuk, Molja K         8  Adamčuk, M K             9  Adamčuk, Karel Molja   
        //  10 Adamčuk, Karel M        11  Adamčuk, K Molja        20  Adamcuk,               
        //  21 Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
        //  24 Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
        //  27 Adamcuk, M K            28  Adamcuk, Karel Molja    29  Adamcuk, Karel M       
        //  30 Adamcuk, K Molja        40  Adamchuk,               41  Adamchuk, M.           
        //  42 Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
        //  45 Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
        //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M       50  Adamchuk, K Molja      
        "\"adamczuk,\"", expected + "author:adamczuk, | author:adamczuk,*", "//*[@numFound='33']",
        // adamczuk numFound=33
        //   1 Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
        //   4 Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
        //   7 Adamčuk, Molja K         8  Adamčuk, M K             9  Adamčuk, Karel Molja   
        //  10 Adamčuk, Karel M        11  Adamčuk, K Molja        20  Adamcuk,               
        //  21 Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
        //  24 Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
        //  27 Adamcuk, M K            28  Adamcuk, Karel Molja    29  Adamcuk, Karel M       
        //  30 Adamcuk, K Molja        40  Adamchuk,               41  Adamchuk, M.           
        //  42 Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
        //  45 Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
        //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M       50  Adamchuk, K Molja      
        "\"adamšuk,\"", "author:adamšuk, | author:adamšuk,* " +
                   "author:adamshuk, | author:adamshuk,* " +
                   "author:adamsuk, | author:adamsuk,* " +
                   "author:adamguk, m | author:adamčuk, m | author:adamšuk, m", 
                   "//*[@numFound='13']",
                   // adamšuk numFound=13
                   //   2 Adamčuk, M.             61  Adamguk, M.             80  Adamshuk,              
                   //  81 Adamshuk, M.            82  Adamshuk, Marel         83  Adamshuk, Molja        
                   //  84 Adamshuk, Molja Karel   85  Adamshuk, M Karel       86  Adamshuk, Molja K      
                   //  87 Adamshuk, M K           88  Adamshuk, Karel Molja   89  Adamshuk, Karel M      
                   //  90 Adamshuk, K Molja                               
        "\"adamguk,\"", "author:adamguk, | author:adamguk,* " +
                   "author:adamguk, m | author:adamčuk, m | author:adamšuk, m", 
                   "//*[@numFound='12']"
                   // adamguk numFound=12
                   //   2 Adamčuk, M.             60  Adamguk,                61  Adamguk, M.            
                   //  62 Adamguk, Marel          63  Adamguk, Molja          64  Adamguk, Molja Karel   
                   //  65 Adamguk, M Karel        66  Adamguk, Molja K        67  Adamguk, M K           
                   //  68 Adamguk, Karel Molja    69  Adamguk, Karel M        70  Adamguk, K Molja       
    );


    /**
     * <surname>, <1>
     * 
     *  expanded && upgraded && transliterated && expanded
     *  synonym "adamšuk, m" IS FOUND because there is entry for "adamčuk, m" the syn list, notice
     *  this works even if we type "adamchuk, m" or "adamcuk, m"
     * 
     *  question: the chain correctly finds the synonym "adamšuk, m", and this synonym is
     *  then transliterated: adamshuk, m;adamsuk, m (is this desirable?) I think yes.
     */
    expected = "author:adamšuk, m | author:adamšuk, m* | author:adamšuk, " +
                "| author:adamsuk, m | author:adamsuk, m* | author:adamsuk, " +
                "| author:adamshuk, m | author:adamshuk, m* | author:adamshuk, " +
                "| author:adamguk, m | author:adamguk, m* | author:adamguk, " +
                "| author:adamčuk, m | author:adamčuk, m* | author:adamčuk, " +
                "| author:adamchuk, m | author:adamchuk, m* | author:adamchuk, " +
                "| author:adamcuk, m | author:adamcuk, m* | author:adamcuk,"; 
    testAuthorQuery(
        "\"adamčuk,    m\"", expected, "//*[@numFound='40']",
               // "adamčuk, m" numFound=40
          //      1  Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
          //      4  Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
          //      7  Adamčuk, Molja K         8  Adamčuk, M K            20  Adamcuk,               
          //     21  Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
          //     24  Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
          //     27  Adamcuk, M K            40  Adamchuk,               41  Adamchuk, M.           
          //     42  Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
          //     45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
          //     60  Adamguk,                61  Adamguk, M.             62  Adamguk, Marel         
          //     63  Adamguk, Molja          64  Adamguk, Molja Karel    65  Adamguk, M Karel       
          //     66  Adamguk, Molja K        67  Adamguk, M K            80  Adamshuk,              
          //     81  Adamshuk, M.            82  Adamshuk, Marel         83  Adamshuk, Molja        
          //     84  Adamshuk, Molja Karel   85  Adamshuk, M Karel       86  Adamshuk, Molja K      
          //     87  Adamshuk, M K          
        "\"adamcuk, m\"", expected, "//*[@numFound='40']",
               // "adamcuk, m" numFound=40
          //      1  Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
          //      4  Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
          //      7  Adamčuk, Molja K         8  Adamčuk, M K            20  Adamcuk,               
          //     21  Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
          //     24  Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
          //     27  Adamcuk, M K            40  Adamchuk,               41  Adamchuk, M.           
          //     42  Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
          //     45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
          //     60  Adamguk,                61  Adamguk, M.             62  Adamguk, Marel         
          //     63  Adamguk, Molja          64  Adamguk, Molja Karel    65  Adamguk, M Karel       
          //     66  Adamguk, Molja K        67  Adamguk, M K            80  Adamshuk,              
          //     81  Adamshuk, M.            82  Adamshuk, Marel         83  Adamshuk, Molja        
          //     84  Adamshuk, Molja Karel   85  Adamshuk, M Karel       86  Adamshuk, Molja K      
          //     87  Adamshuk, M K          
        "\"adamchuk, m\"", expected, "//*[@numFound='40']",
               // "adamchuk, m" numFound=40
          //      1  Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
          //      4  Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
          //      7  Adamčuk, Molja K         8  Adamčuk, M K            20  Adamcuk,               
          //     21  Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
          //     24  Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
          //     27  Adamcuk, M K            40  Adamchuk,               41  Adamchuk, M.           
          //     42  Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
          //     45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
          //     60  Adamguk,                61  Adamguk, M.             62  Adamguk, Marel         
          //     63  Adamguk, Molja          64  Adamguk, Molja Karel    65  Adamguk, M Karel       
          //     66  Adamguk, Molja K        67  Adamguk, M K            80  Adamshuk,              
          //     81  Adamshuk, M.            82  Adamshuk, Marel         83  Adamshuk, Molja        
          //     84  Adamshuk, Molja Karel   85  Adamshuk, M Karel       86  Adamshuk, Molja K      
          //     87  Adamshuk, M K     
        "\"adamczuk, m\"", expected + "author:adamczuk, m | author:adamczuk, m* | author:adamczuk,", 
        	    "//*[@numFound='40']",
               // "adamczuk, m" numFound=40
          //      1  Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
          //      4  Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
          //      7  Adamčuk, Molja K         8  Adamčuk, M K            20  Adamcuk,               
          //     21  Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
          //     24  Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
          //     27  Adamcuk, M K            40  Adamchuk,               41  Adamchuk, M.           
          //     42  Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
          //     45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
          //     60  Adamguk,                61  Adamguk, M.             62  Adamguk, Marel         
          //     63  Adamguk, Molja          64  Adamguk, Molja Karel    65  Adamguk, M Karel       
          //     66  Adamguk, Molja K        67  Adamguk, M K            80  Adamshuk,              
          //     81  Adamshuk, M.            82  Adamshuk, Marel         83  Adamshuk, Molja        
          //     84  Adamshuk, Molja Karel   85  Adamshuk, M Karel       86  Adamshuk, Molja K      
          //     87  Adamshuk, M K   
        "\"adamšuk, m\"", expected, "//*[@numFound='40']",
               // "adamšuk, m" numFound=40
          //      1  Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
          //      4  Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
          //      7  Adamčuk, Molja K         8  Adamčuk, M K            20  Adamcuk,               
          //     21  Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
          //     24  Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
          //     27  Adamcuk, M K            40  Adamchuk,               41  Adamchuk, M.           
          //     42  Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
          //     45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
          //     60  Adamguk,                61  Adamguk, M.             62  Adamguk, Marel         
          //     63  Adamguk, Molja          64  Adamguk, Molja Karel    65  Adamguk, M Karel       
          //     66  Adamguk, Molja K        67  Adamguk, M K            80  Adamshuk,              
          //     81  Adamshuk, M.            82  Adamshuk, Marel         83  Adamshuk, Molja        
          //     84  Adamshuk, Molja Karel   85  Adamshuk, M Karel       86  Adamshuk, Molja K      
          //     87  Adamshuk, M K          
        "\"adamguk, m\"", expected, "//*[@numFound='40']",
             // "adamguk, m" numFound=40
          //      1  Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
          //      4  Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
          //      7  Adamčuk, Molja K         8  Adamčuk, M K            20  Adamcuk,               
          //     21  Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
          //     24  Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
          //     27  Adamcuk, M K            40  Adamchuk,               41  Adamchuk, M.           
          //     42  Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
          //     45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
          //     60  Adamguk,                61  Adamguk, M.             62  Adamguk, Marel         
          //     63  Adamguk, Molja          64  Adamguk, Molja Karel    65  Adamguk, M Karel       
          //     66  Adamguk, Molja K        67  Adamguk, M K            80  Adamshuk,              
          //     81  Adamshuk, M.            82  Adamshuk, Marel         83  Adamshuk, Molja        
          //     84  Adamshuk, Molja Karel   85  Adamshuk, M Karel       86  Adamshuk, Molja K      
          //     87  Adamshuk, M K     
        "\"AdAmČuk, m\"", expected, "//*[@numFound='40']", // just for fun
        "\"ADAMŠuk, m\"", expected, "//*[@numFound='40']",
        "\"AdAmGuk,    M\"", expected, "//*[@numFound='40']"
    );


    /**
     * <surname>, <1name>
     * 
     *  upgraded && transliterated && expanded
     *  synonym "adamšuk, m" IS FOUND because of the query variation for "adamčuk, m" the syn list
     */
    
    // base part, must be present in all
    expected0 = 
    		       "author:adamčuk, m | author:adamčuk, m * | author:adamčuk, " +
    		       "author:adamcuk, m | author:adamcuk, m * | author:adamcuk, " +
    		       "author:adamchuk, m | author:adamchuk, m * | author:adamchuk, " +
    		       "author:adamšuk, m | author:adamšuk, m * | author:adamšuk, " +
    		       "author:adamsuk, m | author:adamsuk, m * | author:adamsuk, " +
    		       "author:adamshuk, m | author:adamshuk, m * | author:adamshuk, " +
    		       "author:adamguk, m | author:adamguk, m * | author:adamguk, ";

    expected = expected0 +
               "author:adamčuk, molja | author:adamčuk, molja * " +
               "author:adamchuk, molja | author:adamchuk, molja * " +
               "author:adamcuk, molja | author:adamcuk, molja *"
               ;
    
    
    testAuthorQuery(
        "\"adamčuk, molja\"", expected, "//*[@numFound='29']",
               // "adamčuk, molja" numFound=29
          //      1  Adamčuk,                 2  Adamčuk, M.              4  Adamčuk, Molja         
          //      5  Adamčuk, Molja Karel     6  Adamčuk, M Karel         7  Adamčuk, Molja K       
          //      8  Adamčuk, M K            20  Adamcuk,                21  Adamcuk, M.            
          //     23  Adamcuk, Molja          24  Adamcuk, Molja Karel    25  Adamcuk, M Karel       
          //     26  Adamcuk, Molja K        27  Adamcuk, M K            40  Adamchuk,              
          //     41  Adamchuk, M.            43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
          //     45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
          //     60  Adamguk,                61  Adamguk, M.             65  Adamguk, M Karel       
          //     67  Adamguk, M K            80  Adamshuk,               81  Adamshuk, M.           
          //     85  Adamshuk, M Karel       87  Adamshuk, M K        
        "\"adamcuk, molja\"", expected, "//*[@numFound='29']",
               // "adamcuk, molja" numFound=29
          //      1  Adamčuk,                 2  Adamčuk, M.              4  Adamčuk, Molja         
          //      5  Adamčuk, Molja Karel     6  Adamčuk, M Karel         7  Adamčuk, Molja K       
          //      8  Adamčuk, M K            20  Adamcuk,                21  Adamcuk, M.            
          //     23  Adamcuk, Molja          24  Adamcuk, Molja Karel    25  Adamcuk, M Karel       
          //     26  Adamcuk, Molja K        27  Adamcuk, M K            40  Adamchuk,              
          //     41  Adamchuk, M.            43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
          //     45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
          //     60  Adamguk,                61  Adamguk, M.             65  Adamguk, M Karel       
          //     67  Adamguk, M K            80  Adamshuk,               81  Adamshuk, M.           
          //     85  Adamshuk, M Karel       87  Adamshuk, M K   
        "\"adamchuk, molja\"", expected, "//*[@numFound='29']",
               // "adamchuk, molja" numFound=29
          //      1  Adamčuk,                 2  Adamčuk, M.              4  Adamčuk, Molja         
          //      5  Adamčuk, Molja Karel     6  Adamčuk, M Karel         7  Adamčuk, Molja K       
          //      8  Adamčuk, M K            20  Adamcuk,                21  Adamcuk, M.            
          //     23  Adamcuk, Molja          24  Adamcuk, Molja Karel    25  Adamcuk, M Karel       
          //     26  Adamcuk, Molja K        27  Adamcuk, M K            40  Adamchuk,              
          //     41  Adamchuk, M.            43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
          //     45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
          //     60  Adamguk,                61  Adamguk, M.             65  Adamguk, M Karel       
          //     67  Adamguk, M K            80  Adamshuk,               81  Adamshuk, M.           
          //     85  Adamshuk, M Karel       87  Adamshuk, M K  
        "\"adamczuk, molja\"", expected + "author:adamczuk, molja | author:adamczuk, molja * | author:adamczuk, m | author:adamczuk, m * | author:adamczuk,", 
        		"//*[@numFound='29']",
               // "adamczuk, molja" numFound=29
          //      1  Adamčuk,                 2  Adamčuk, M.              4  Adamčuk, Molja         
          //      5  Adamčuk, Molja Karel     6  Adamčuk, M Karel         7  Adamčuk, Molja K       
          //      8  Adamčuk, M K            20  Adamcuk,                21  Adamcuk, M.            
          //     23  Adamcuk, Molja          24  Adamcuk, Molja Karel    25  Adamcuk, M Karel       
          //     26  Adamcuk, Molja K        27  Adamcuk, M K            40  Adamchuk,              
          //     41  Adamchuk, M.            43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
          //     45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
          //     60  Adamguk,                61  Adamguk, M.             65  Adamguk, M Karel       
          //     67  Adamguk, M K            80  Adamshuk,               81  Adamshuk, M.           
          //     85  Adamshuk, M Karel       87  Adamshuk, M K     
        // "adamčuk, molja" is not there (and cannot be, because it is not in
        // synonym map, but synonym "adamšuk, m" is found correctly)

        "\"adamšuk, molja\"", expected0 +
                              "author:adamšuk, molja | author:adamšuk, molja * " +
                              "author:adamshuk, molja | author:adamshuk, molja * " +
                              "author:adamsuk, molja | author:adamsuk, molja *", "//*[@numFound='23']",
          // shorter by two variants, because "adamguk, molja" is already ascii form
          // it doesn't generate: "author:adamshuk, molja | author:adamsuk, molja"
          // that is correct, because "adamšuk, m" is found and transliterated
          // "adamšuk, molja" simply isn't in any synonym list and we tehrefore cannot have it                              
                  // "adamšuk, molja" numFound=23
          //      1  Adamčuk,                 2  Adamčuk, M.              6  Adamčuk, M Karel       
          //      8  Adamčuk, M K            20  Adamcuk,                21  Adamcuk, M.            
          //     25  Adamcuk, M Karel        27  Adamcuk, M K            40  Adamchuk,              
          //     41  Adamchuk, M.            45  Adamchuk, M Karel       47  Adamchuk, M K          
          //     60  Adamguk,                61  Adamguk, M.             65  Adamguk, M Karel       
          //     67  Adamguk, M K            80  Adamshuk,               81  Adamshuk, M.           
          //     83  Adamshuk, Molja         84  Adamshuk, Molja Karel   85  Adamshuk, M Karel      
          //     86  Adamshuk, Molja K       87  Adamshuk, M K   
                              
                              
 
        "\"adamguk, molja\"", expected0 +
                              "author:adamguk, molja | author:adamguk, molja *",  "//*[@numFound='23']"
               // "adamguk, molja" numFound=23
          //      1  Adamčuk,                 2  Adamčuk, M.              6  Adamčuk, M Karel       
          //      8  Adamčuk, M K            20  Adamcuk,                21  Adamcuk, M.            
          //     25  Adamcuk, M Karel        27  Adamcuk, M K            40  Adamchuk,              
          //     41  Adamchuk, M.            45  Adamchuk, M Karel       47  Adamchuk, M K          
          //     60  Adamguk,                61  Adamguk, M.             63  Adamguk, Molja         
          //     64  Adamguk, Molja Karel    65  Adamguk, M Karel        66  Adamguk, Molja K       
          //     67  Adamguk, M K            80  Adamshuk,               81  Adamshuk, M.           
          //     85  Adamshuk, M Karel       87  Adamshuk, M K         
                              
    );



    /**
     * <surname>, <1name> <2>
     * 
     * upgraded && transliterated && expanded
     * synonym adamšuk IS NOT FOUND because there is no entry for "adamčuk, molja k" nor 
     * there is any "adamčuk, m k" in the syn list
     * 
     * NOTE: if you think that "adamšuk" should be found in our model, then you are wrong
     * because "adamcuk, m k" is a different name than "adamcuk, m"
     * We are not goign to do any magic to find the surname mapping, in other words:
     * we are not going to replace defficient synonym file. Because the correct translation 
     * CAN WORK if "adamcuk, m k" and "adamcuk, m" are named as synonymous (see the example
     * case of "adamczuk, m k k")
     */
    
    expected = "author:adamčuk, molja k | author:adamčuk, molja k* " +
    		       "author:adamčuk, m k | author:adamčuk, m k* " +
    		       "author:adamčuk, molja " + // ! | author:adamčuk, molja *
    		       "author:adamčuk, m " + // ! | author:adamčuk, m*
    		       "author:adamčuk, " +
    		       "author:adamcuk, molja k | author:adamcuk, molja k* " +
    		       "author:adamcuk, m k | author:adamcuk, m k* " +
    		       "author:adamcuk, molja " + // ! | author:adamcuk, molja * 
    		       "author:adamcuk, m " + // ! | author:adamcuk, m* 
    		       "author:adamcuk, " +
    		       "author:adamchuk, molja k | author:adamchuk, molja k* " +
    		       "author:adamchuk, m k | author:adamchuk, m k* " +
    		       "author:adamchuk, molja " + // ! | author:adamchuk, molja * 
    		       "author:adamchuk, m " + // ! | author:adamchuk, m*
    		       "author:adamchuk,";
      

    testAuthorQuery(
        "\"adamčuk, molja k\"", expected, "//*[@numFound='21']",
             // "adamčuk, molja k" numFound=21
        //      1  Adamčuk,                 2  Adamčuk, M.              4  Adamčuk, Molja         
        //      5  Adamčuk, Molja Karel     6  Adamčuk, M Karel         7  Adamčuk, Molja K       
        //      8  Adamčuk, M K            20  Adamcuk,                21  Adamcuk, M.            
        //     23  Adamcuk, Molja          24  Adamcuk, Molja Karel    25  Adamcuk, M Karel       
        //     26  Adamcuk, Molja K        27  Adamcuk, M K            40  Adamchuk,              
        //     41  Adamchuk, M.            43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
        //     45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K       
        "\"adamcuk, molja k\"", expected, "//*[@numFound='21']",
             // "adamcuk, molja k" numFound=21
        //      1  Adamčuk,                 2  Adamčuk, M.              4  Adamčuk, Molja         
        //      5  Adamčuk, Molja Karel     6  Adamčuk, M Karel         7  Adamčuk, Molja K       
        //      8  Adamčuk, M K            20  Adamcuk,                21  Adamcuk, M.            
        //     23  Adamcuk, Molja          24  Adamcuk, Molja Karel    25  Adamcuk, M Karel       
        //     26  Adamcuk, Molja K        27  Adamcuk, M K            40  Adamchuk,              
        //     41  Adamchuk, M.            43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
        //     45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
        "\"adamchuk, molja k\"", expected, "//*[@numFound='21']",
        // this contains 4 more entries because by default, the
        // transliteration produces only adam(c|ch)uk
             // "adamchuk, molja k" numFound=21
        //      1  Adamčuk,                 2  Adamčuk, M.              4  Adamčuk, Molja         
        //      5  Adamčuk, Molja Karel     6  Adamčuk, M Karel         7  Adamčuk, Molja K       
        //      8  Adamčuk, M K            20  Adamcuk,                21  Adamcuk, M.            
        //     23  Adamcuk, Molja          24  Adamcuk, Molja Karel    25  Adamcuk, M Karel       
        //     26  Adamcuk, Molja K        27  Adamcuk, M K            40  Adamchuk,              
        //     41  Adamchuk, M.            43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
        //     45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K     
        "\"adamczuk, molja k\"", expected + " | author:adamczuk, molja k | author:adamczuk, molja k* | author:adamczuk, m k | author:adamczuk, m k* | author:adamczuk, molja | author:adamczuk, m | author:adamczuk,",  
                          "//*[@numFound='21']",
              // "adamczuk, molja k" numFound=21
        //       1  Adamčuk,                 2  Adamčuk, M.              4  Adamčuk, Molja         
        //       5  Adamčuk, Molja Karel     6  Adamčuk, M Karel         7  Adamčuk, Molja K       
        //       8  Adamčuk, M K            20  Adamcuk,                21  Adamcuk, M.            
        //      23  Adamcuk, Molja          24  Adamcuk, Molja Karel    25  Adamcuk, M Karel       
        //      26  Adamcuk, Molja K        27  Adamcuk, M K            40  Adamchuk,              
        //      41  Adamchuk, M.            43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
        //      45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K     
        "\"adamšuk, molja k\"", 
            "author:adamšuk, molja k | author:adamšuk, molja k* " +
            "author:adamšuk, m k | author:adamšuk, m k* " +
            "author:adamšuk, molja " +
            "author:adamšuk, m " +
            "author:adamšuk, " +
            "author:adamsuk, molja k | author:adamsuk, molja k* " +
            "author:adamsuk, m k | author:adamsuk, m k* " +
            "author:adamsuk, molja " +
            "author:adamsuk, m " +
            "author:adamsuk, " +
            "author:adamshuk, molja k | author:adamshuk, molja k* " +
            "author:adamshuk, m k | author:adamshuk, m k* " +
            "author:adamshuk, molja " +
            "author:adamshuk, m " +
            "author:adamshuk,", 
              "//*[@numFound='7']",
              // "adamšuk, molja k" numFound=7
        //       80  Adamshuk,               81  Adamshuk, M.            83  Adamshuk, Molja        
        //       84  Adamshuk, Molja Karel   85  Adamshuk, M Karel       86  Adamshuk, Molja K      
        //       87  Adamshuk, M K          
              
        "\"adamguk, molja k\"", 
            "author:adamguk, molja k | author:adamguk, molja k* " +
            "author:adamguk, m k | author:adamguk, m k* " +
            "author:adamguk, molja " +
            "author:adamguk, m " +
            "author:adamguk,", 
            "//*[@numFound='7']"
           // "adamguk, molja k" numFound=7
        // 60  Adamguk,                61  Adamguk, M.             63  Adamguk, Molja         
        // 64  Adamguk, Molja Karel    65  Adamguk, M Karel        66  Adamguk, Molja K       
        // 67  Adamguk, M K   
    );



    /**
     * <surname>, <1name> <2name>
     * 
     * It works as above with the addition that the VARIATIONS of the initials/full names
     * are produced, ie. Aaaa B Ccccc will produce
     *    Aaaa B C
     *    A B C, A B Cccc
     *    Aaaa B Cccc
     *    
     * And through these variations, we find the upgraded form "adamčuk, molja k"
     * 
     * This has the benefit of us finding the combination of name/initials even if 
     * we didn't encounter them during indexing. HOWEVER, to avoid false hits these
     * combinations are found only for names that have certain number of parts,
     * default >= 3 
     */

    // we expect the same results as above (the difference is in the "..., k k *")
    // plus whathever comes out of the original input transliteration/combination
    expected0 = "author:adamčuk, molja k | author:adamčuk, molja k * " +
                "author:adamčuk, m k | author:adamčuk, m k * " +
                "author:adamčuk, molja " + // <- in my opinion this is wrong (too much recall), but it was requested
                "author:adamčuk, m " + 
                "author:adamčuk, " +
                "author:adamcuk, molja k | author:adamcuk, molja k * " +
                "author:adamcuk, m k | author:adamcuk, m k * " +
                "author:adamcuk, molja " +  // dtto
                "author:adamcuk, m " +  
                "author:adamcuk, " +
                "author:adamchuk, molja k | author:adamchuk, molja k * " +
                "author:adamchuk, m k | author:adamchuk, m k * " +
                "author:adamchuk, molja " +  //dtto
                "author:adamchuk, m " + 
                "author:adamchuk,";
    
    //dumpDoc(null, "id", "author");
    
    testAuthorQuery(
        "\"adamčuk, molja karel\"", expected0 + " " + 
                                    "author:adamčuk, molja karel | author:adamčuk, molja karel * " +
                                    "author:adamčuk, m karel | author:adamčuk, m karel * " +
                                    "author:adamchuk, molja karel | author:adamchuk, molja karel * " +
                                    "author:adamchuk, m karel | author:adamchuk, m karel * " +
                                    "author:adamcuk, molja karel | author:adamcuk, molja karel * " +
                                    "author:adamcuk, m karel | author:adamcuk, m karel *", 
                                    "//*[@numFound='21']",
             // "adamčuk, molja karel" numFound=21
        //      1  Adamčuk,                 2  Adamčuk, M.              4  Adamčuk, Molja         
        //      5  Adamčuk, Molja Karel     6  Adamčuk, M Karel         7  Adamčuk, Molja K       
        //      8  Adamčuk, M K            20  Adamcuk,                21  Adamcuk, M.            
        //     23  Adamcuk, Molja          24  Adamcuk, Molja Karel    25  Adamcuk, M Karel       
        //     26  Adamcuk, Molja K        27  Adamcuk, M K            40  Adamchuk,              
        //     41  Adamchuk, M.            43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
        //     45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K    
                                    
        "\"adamcuk, molja karel\"", expected0 + " " + 
                                    "author:adamcuk, molja karel | author:adamcuk, molja karel * " +
                                    "author:adamcuk, m karel | author:adamcuk, m karel *",
                                    "//*[@numFound='17']", // because adamcuk, m\w* k\w* is not searched
             // "adamcuk, molja karel" numFound=17
        //      1  Adamčuk,                 2  Adamčuk, M.              4  Adamčuk, Molja         
        //      7  Adamčuk, Molja K         8  Adamčuk, M K            20  Adamcuk,               
        //     21  Adamcuk, M.             23  Adamcuk, Molja          24  Adamcuk, Molja Karel   
        //     25  Adamcuk, M Karel        26  Adamcuk, Molja K        27  Adamcuk, M K           
        //     40  Adamchuk,               41  Adamchuk, M.            43  Adamchuk, Molja        
        //     46  Adamchuk, Molja K       47  Adamchuk, M K         
                                    
        "\"adamchuk, molja karel\"", expected0 + " " + 
                                    "author:adamchuk, molja karel | author:adamchuk, molja karel * " +
                                    "author:adamchuk, m karel | author:adamchuk, m karel *",
                                    "//*[@numFound='17']",
             // "adamchuk, molja karel" numFound=17
        //      1  Adamčuk,                 2  Adamčuk, M.              4  Adamčuk, Molja         
        //      7  Adamčuk, Molja K         8  Adamčuk, M K            20  Adamcuk,               
        //     21  Adamcuk, M.             23  Adamcuk, Molja          26  Adamcuk, Molja K       
        //     27  Adamcuk, M K            40  Adamchuk,               41  Adamchuk, M.           
        //     43  Adamchuk, Molja         44  Adamchuk, Molja Karel   45  Adamchuk, M Karel      
        //     46  Adamchuk, Molja K       47  Adamchuk, M K    
                                    
        "\"adamczuk, molja karel\"", expected0 + " " + 
                                    "author:adamczuk, molja karel | author:adamczuk, molja karel * " +
                                    "author:adamczuk, m karel | author:adamczuk, m karel * " +
                                    "author:adamczuk, molja k | author:adamczuk, molja k * " +
                                    "author:adamczuk, m k | author:adamczuk, m k * " +
                                    "author:adamczuk, molja | author:adamczuk, m " +
                                    "author:adamczuk,",
                                    "//*[@numFound='15']",//-3 because "č"->"cz" normally doesn't exist
             // "adamczuk, molja karel" numFound=15
        //      1  Adamčuk,                 2  Adamčuk, M.              4  Adamčuk, Molja         
        //      7  Adamčuk, Molja K         8  Adamčuk, M K            20  Adamcuk,               
        //     21  Adamcuk, M.             23  Adamcuk, Molja          26  Adamcuk, Molja K       
        //     27  Adamcuk, M K            40  Adamchuk,               41  Adamchuk, M.           
        //     43  Adamchuk, Molja         46  Adamchuk, Molja K       47  Adamchuk, M K
                                    
        // almost exactly the same as above, the only difference must be the space before *
        "\"adamšuk, molja karel\"", "author:adamšuk, molja k | author:adamšuk, molja k * " +
                                    "author:adamšuk, m k | author:adamšuk, m k * " +
                                    "author:adamšuk, molja " +
                                    "author:adamšuk, m " +
                                    "author:adamšuk, " +
                                    "author:adamsuk, molja k | author:adamsuk, molja k * " +
                                    "author:adamsuk, m k | author:adamsuk, m k * " +
                                    "author:adamsuk, molja " +
                                    "author:adamsuk, m " +
                                    "author:adamsuk, " +
                                    "author:adamshuk, molja k | author:adamshuk, molja k * " +
                                    "author:adamshuk, m k | author:adamshuk, m k * " +
                                    "author:adamshuk, molja " +
                                    "author:adamshuk, m " +
                                    "author:adamshuk, " + 
                                    // plus variants with karel
                                    "author:adamšuk, molja karel | author:adamšuk, molja karel * " +
                                    "author:adamšuk, m karel | author:adamšuk, m karel * " + 
                                    "author:adamshuk, molja karel | author:adamshuk, molja karel * " +
                                    "author:adamshuk, m karel | author:adamshuk, m karel * " +
                                    "author:adamsuk, molja karel | author:adamsuk, molja karel * " +
                                    "author:adamsuk, m karel | author:adamsuk, m karel *",
                                    "//*[@numFound='7']",
                 // "adamšuk, molja karel" numFound=7
          //        80  Adamshuk,               81  Adamshuk, M.            83  Adamshuk, Molja        
          //        84  Adamshuk, Molja Karel   85  Adamshuk, M Karel       86  Adamshuk, Molja K      
          //        87  Adamshuk, M K     
                                    
        "\"adamguk, molja karel\"", "author:adamguk, molja k | author:adamguk, molja k * " +
                                    "author:adamguk, m k | author:adamguk, m k * " +
                                    "author:adamguk, molja " +
                                    "author:adamguk, m " +
                                    "author:adamguk, " + 
                                    // plus variants with karel
                                    "author:adamguk, molja karel | author:adamguk, molja karel * " +
                                    "author:adamguk, m karel | author:adamguk, m karel *",
                                    "//*[@numFound='7']"
             // "adamguk, molja karel" numFound=7
        //      60  Adamguk,                61  Adamguk, M.             63  Adamguk, Molja         
        //      64  Adamguk, Molja Karel    65  Adamguk, M Karel        66  Adamguk, Molja K       
        //      67  Adamguk, M K 
        
    );
    
    
    /*
     * TODO:
     * 
     * Also make sure we test that the expanding algorithm doesn't have unwanted consequences
     * and doesn't include too much, ie. that search for "adamčuk, mos" doesn't get
     * transformed into "adam(c|ch)uk, m"
     */
    
    //TODO: show that the translation works properly when the synonym is in the synonym list
    // ie "adamčuk, m k;adamšuk, m k"

    
    /**
     * <surname>, <1> <2name>
     * 
     * Speciality of this patter is that we want to search for regular
     * expression
     * 
     *    <surname>, <1>\w* <2>
     *    <surname>, <1>\w* <2name>
     * 
     * The following expansion will not find the synonyms and will not find 
     * the upgrade. I am listing this example here specifically to show what
     * happens when the synonym list is missing some values (in real life,
     * the correct mapping will be generated IFF we encounter one of these
     * during indexing:
     *   
     *    adamčuk, m karel
     *    adamčuk, mxxxx karel
     * 
     * 
     */

    
    //dumpDoc(null, "id", "author");
    testAuthorQuery(
        "\"adamčuk, m karel\"", "author:adamčuk, m karel | author:adamčuk, m karel * " +
                                "author:/adamčuk, m[^ ]*/ " +
                                "author:adamčuk, m k | author:adamčuk, m k * " +
                                "author:adamčuk, m " + 
                                "author:adamčuk, " +
                                "author:adamcuk, m karel | author:adamcuk, m karel * " +
                                "author:adamcuk, m k | author:adamcuk, m k * " +
                                "author:adamcuk, m " +  
                                "author:adamcuk, " +
                                "author:adamchuk, m karel | author:adamchuk, m karel * " +
                                "author:adamchuk, m k | author:adamchuk, m k * " +
                                "author:adamchuk, m " + 
                                "author:adamchuk, " +
                                "author:/adamčuk, m[^ ]* karel/ " +
                                "author:/adamčuk, m[^ ]* karel .*/ " +
                                "author:/adamčuk, m[^ ]* k/ " +
                                "author:/adamčuk, m[^ ]* k .*/ " +
                                "author:/adamcuk, m[^ ]* karel/ " +
                                "author:/adamcuk, m[^ ]* karel .*/ " +
                                "author:/adamcuk, m[^ ]* k/ " +
                                "author:/adamcuk, m[^ ]* k .*/ " +
                                "author:/adamchuk, m[^ ]* karel/ " +
                                "author:/adamchuk, m[^ ]* karel .*/ " +
                                "author:/adamchuk, m[^ ]* k/ " +
                                "author:/adamchuk, m[^ ]* k .*/ " +
                                "author:/adamchuk, m[^ ]*/ | author:/adamcuk, m[^ ]*/",
                                 "//*[@numFound='24']"        
        
         // "adamčuk, m karel" numFound=24
         //   1 Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
         //   4 Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
         //   7 Adamčuk, Molja K         8  Adamčuk, M K            20  Adamcuk,               
         //  21 Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
         //  24 Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
         //  27 Adamcuk, M K            40  Adamchuk,               41  Adamchuk, M.           
         //  42 Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
         //  45 Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K 
    );
    testAuthorQuery(
        "\"adamcuk, m karel\"", "author:adamcuk, m karel | author:adamcuk, m karel * " +
                                "author:/adamcuk, m[^ ]*/ " +
                                "author:/adamcuk, m[^ ]* karel/ | author:/adamcuk, m[^ ]* karel .*/ " +
                                "author:adamcuk, m k | author:adamcuk, m k * " +
                                "author:/adamcuk, m[^ ]* k/ | author:/adamcuk, m[^ ]* k .*/ " +
                                "author:adamcuk, m | author:adamcuk," ,
                                "//*[@numFound='8']"
               // If you wonder why it is not the same as above, then know it is because of the
               // special setup - we are testing various situations (study the synonym and ascii
               // upgrade setup to understand details)
                                
               // "adamcuk, m karel" numFound=8
        //        20  Adamcuk,                21  Adamcuk, M.             24  Adamcuk, Molja Karel   
        //        25  Adamcuk, M Karel        26  Adamcuk, Molja K        27  Adamcuk, M K   
    );
    testAuthorQuery(
        "\"adamchuk, m karel\"", "author:adamchuk, m karel | author:adamchuk, m karel * " +
                                 "author:/adamchuk, m[^ ]*/ " +
                                 "author:/adamchuk, m[^ ]* karel/ | author:/adamchuk, m[^ ]* karel .*/ " +
                                 "author:adamchuk, m k | author:adamchuk, m k * " +
                                 "author:/adamchuk, m[^ ]* k/ | author:/adamchuk, m[^ ]* k .*/ " +
                                 "author:adamchuk, m | author:adamchuk," ,
                                 "//*[@numFound='8']"
                // "adamchuk, m karel" numFound=8
        //         40  Adamchuk,               41  Adamchuk, M.            44  Adamchuk, Molja Karel  
        //         45  Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K   
    );
    testAuthorQuery(
        "\"adamczuk, m karel\"", "author:adamczuk, m karel | author:adamczuk, m karel * " +
                                 "author:/adamczuk, m[^ ]*/ " +
                                 "author:/adamczuk, m[^ ]* karel/ | author:/adamczuk, m[^ ]* karel .*/ " +
                                 "author:adamczuk, m k | author:adamczuk, m k * " +
                                 "author:/adamczuk, m[^ ]* k/ | author:/adamczuk, m[^ ]* k .*/ " +
                                 "author:adamczuk, m | author:adamczuk," ,
                                 "//*[@numFound='0']"
    );
    testAuthorQuery(
        "\"adamšuk, m karel\"", "author:adamšuk, m karel | author:adamšuk, m karel * " +
                                "author:/adamšuk, m[^ ]*/ | author:/adamshuk, m[^ ]*/ | author:/adamsuk, m[^ ]*/" +
        		                    "author:/adamšuk, m[^ ]* karel/ | author:/adamšuk, m[^ ]* karel .*/ " +
        		                    "author:adamšuk, m k | author:adamšuk, m k * " +
        		                    "author:/adamšuk, m[^ ]* k/ | author:/adamšuk, m[^ ]* k .*/ " +
        		                    "author:adamšuk, m " +
        		                    "author:adamšuk, " +
        		                    "author:adamsuk, m karel | author:adamsuk, m karel * " +
        		                    "author:/adamsuk, m[^ ]* karel/ | author:/adamsuk, m[^ ]* karel .*/ " +
        		                    "author:adamsuk, m k | author:adamsuk, m k * " +
        		                    "author:/adamsuk, m[^ ]* k/ | author:/adamsuk, m[^ ]* k .*/ " +
        		                    "author:adamsuk, m " +
        		                    "author:adamsuk, " +
        		                    "author:adamshuk, m karel | author:adamshuk, m karel * " +
        		                    "author:/adamshuk, m[^ ]* karel/ | author:/adamshuk, m[^ ]* karel .*/ " +
        		                    "author:adamshuk, m k | author:adamshuk, m k * " +
        		                    "author:/adamshuk, m[^ ]* k/ | author:/adamshuk, m[^ ]* k .*/ " +
        		                    "author:adamshuk, m " +
        		                    "author:adamshuk,",
                                "//*[@numFound='8']"
             // "adamšuk, m karel" numFound=8
                //  80 Adamshuk,               81  Adamshuk, M.            82  Adamshuk, Marel        
                //  83 Adamshuk, Molja         84  Adamshuk, Molja Karel   85  Adamshuk, M Karel      
                //  86 Adamshuk, Molja K       87  Adamshuk, M K                  		                    
                                
    );
    testAuthorQuery(
        "\"adamguk, m karel\"", "author:adamguk, m karel | author:adamguk, m karel * " +
                                "author:/adamguk, m[^ ]*/ " +
                                "author:/adamguk, m[^ ]* karel/ | author:/adamguk, m[^ ]* karel .*/ " +
                                "author:adamguk, m k | author:adamguk, m k * " +
                                "author:/adamguk, m[^ ]* k/ | author:/adamguk, m[^ ]* k .*/ " +
                                "author:adamguk, m | author:adamguk," ,
                                "//*[@numFound='8']"
               // "adamguk, m karel" numFound=8
                  //  60 Adamguk,                61  Adamguk, M.             62  Adamguk, Marel         
                  //  63 Adamguk, Molja          64  Adamguk, Molja Karel    65  Adamguk, M Karel       
                  //  66 Adamguk, Molja K        67  Adamguk, M K 
        
    );
    
    
    
    /**
     * <surname>, <1> <2>
     * 
     * Speciality of this patter is that we want to search for regular
     * expression
     * 
     *    <surname>, <1>\w* <2>
     * 
     * The following expansion will not find the synonyms and will not find 
     * the upgrade. I am listing this example here specifically to show what
     * happens when the synonym list is missing some values (in real life,
     * the correct mapping will be generated IFF we encounter one of these
     * during indexing:
     *   
     *    adamčuk, m karel
     *    adamčuk, mxxxx karel
     * 
     * 
     */

    expected = "author:adamčuk, a b | author:adamčuk, a b* " +
    		        "author:/adamčuk, a[^ ]*/ | author:/adamčuk, a[^ ]* b.*/ " +
    		        "author:adamčuk, a " +
    		        "author:adamčuk, " +
    		        "author:adamchuk, a b | author:adamchuk, a b* " +
    		        "author:/adamchuk, a[^ ]*/ | author:/adamchuk, a[^ ]* b.*/ " +
    		        "author:adamchuk, a " +
    		        "author:adamchuk, " +
    		        "author:adamcuk, a b | author:adamcuk, a b* " +
    		        "author:/adamcuk, a[^ ]*/ | author:/adamcuk, a[^ ]* b.*/ " +
    		        "author:adamcuk, a " +
    		        "author:adamcuk,"
                ;
    
    testAuthorQuery(
        "\"adamčuk, a b\"", expected ,
                            "//*[@numFound='3']",
                            // "adamčuk, a b" numFound=3
                            //   1 Adamčuk,                20  Adamcuk,                40  Adamchuk,                                          
                            
        "\"adamcuk, a b\"", expected ,
                            "//*[@numFound='3']",
                            // "adamcuk, a b" numFound=3
                            //   1 Adamčuk,                20  Adamcuk,                40  Adamchuk,              
                            
        "\"adamchuk, a b\"", expected ,
                            "//*[@numFound='3']",
                            // "adamchuk, a b" numFound=3
                            //   1 Adamčuk,                20  Adamcuk,                40  Adamchuk,              
                            
        "\"adamczuk, a b\"", expected + "author:adamczuk, a b | author:adamczuk, a b* | author:/adamczuk, a[^ ]*/ | author:/adamczuk, a[^ ]* b.*/ | author:adamczuk, a | author:adamczuk,",
                             "//*[@numFound='3']",
                             // "adamczuk, a b" numFound=3
                             //   1 Adamčuk,                20  Adamcuk,                40  Adamchuk,              
                             
        "\"adamšuk, m k\"", 
                          "author:adam\u0161uk, m k | author:adam\u0161uk, m k* "
                          + "author:/adam\u0161uk, m[^ ]*/ | author:/adam\u0161uk, m[^ ]* k.*/ "
                          + "author:adam\u0161uk, m "
                          + "author:adam\u0161uk, "
                          + "author:adamsuk, m k | author:adamsuk, m k* "
                          + "author:/adamsuk, m[^ ]*/ | author:/adamsuk, m[^ ]* k.*/ "
                          + "author:adamsuk, m "
                          + "author:adamsuk, "
                          + "author:adamshuk, m k | author:adamshuk, m k* "
                          + "author:/adamshuk, m[^ ]*/ | author:/adamshuk, m[^ ]* k.*/ "
                          + "author:adamshuk, m "
                          + "author:adamshuk,",         
                            "//*[@numFound='8']",
                            //   "adamšuk, m k" numFound=8
                            //  80 Adamshuk,               81  Adamshuk, M.            82  Adamshuk, Marel        
                            //  83 Adamshuk, Molja         84  Adamshuk, Molja Karel   85  Adamshuk, M Karel      
                            //  86 Adamshuk, Molja K       87  Adamshuk, M K          
                            
        "\"adamguk, m k\"", "author:adamguk, m k | author:adamguk, m k* " +
        		                "author:/adamguk, m[^ ]*/ | author:/adamguk, m[^ ]* k.*/ " +
        		                "author:adamguk, m " +
        		                "author:adamguk," ,
                            "//*[@numFound='8']"
        		                // "adamguk, m k" numFound=8
                            //  60 Adamguk,                61  Adamguk, M.             62  Adamguk, Marel         
                            //  63 Adamguk, Molja          64  Adamguk, Molja Karel    65  Adamguk, M Karel       
                            //  66 Adamguk, Molja K        67  Adamguk, M K
    );
    
    
    /**
     * <surname>, <2>
     * 
     * No expansion, because of the gap. Only transliteration
     * 
     */
    
    
    testAuthorQuery(
            "\"adamčuk, k\"", "author:adamčuk, k | author:adamčuk, k* | author:adamčuk, " +
                               "author:adamchuk, k | author:adamchuk, k* | author:adamchuk, " +
                               "author:adamcuk, k | author:adamcuk, k* | author:adamcuk,", 
                               "//*[@numFound='12']",
                               // "adamčuk, k" numFound=12
                               //   1 Adamčuk,                 9  Adamčuk, Karel Molja    10  Adamčuk, Karel M       
                               //  11 Adamčuk, K Molja        20  Adamcuk,                28  Adamcuk, Karel Molja   
                               //  29 Adamcuk, Karel M        30  Adamcuk, K Molja        40  Adamchuk,              
                               //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M       50  Adamchuk, K Molja      
                               
            "\"adamcuk, k\"", "author:adamcuk, k | author:adamcuk, k* | author:adamcuk,", 
                               "//*[@numFound='4']",
                               // "adamcuk, k" numFound=4
                               //  20 Adamcuk,                28  Adamcuk, Karel Molja    29  Adamcuk, Karel M       
                               //  30 Adamcuk, K Molja       
             
            "\"adamchuk, k\"", "author:adamchuk, k | author:adamchuk, k* | author:adamchuk,", 
                                "//*[@numFound='4']",
                                // "adamchuk, k" numFound=4
                                //  40 Adamchuk,               48  Adamchuk, Karel Molja   49  Adamchuk, Karel M      
                                //  50 Adamchuk, K Molja                                      
             
            "\"adamczuk, k\"", "author:adamczuk, k | author:adamczuk, k* | author:adamczuk,", 
                                "//*[@numFound='0']",
                                // "adamczuk, k" numFound=0                              
                              
            "\"adamšuk, k\"", "author:adamšuk, k | author:adamšuk, k* | author:adamšuk, " +
                               "author:adamsuk, k | author:adamsuk, k* | author:adamsuk, " +
                               "author:adamshuk, k | author:adamshuk, k* | author:adamshuk,", 
                               "//*[@numFound='4']"
                               // "adamšuk, k" numFound=4
                               //  80 Adamshuk,               88  Adamshuk, Karel Molja   89  Adamshuk, Karel M      
                               //  90 Adamshuk, K Molja      
                               ,
            "\"adamguk, k\"", "author:adamguk, k | author:adamguk, k* | author:adamguk,", 
                               "//*[@numFound='4']"
                              // "adamguk, k" numFound=4
                              //  60 Adamguk,                68  Adamguk, Karel Molja    69  Adamguk, Karel M       
                              //  70 Adamguk, K Molja    
    );
    
    
    /**
     * <surname>, <2name>
     * 
     * No expansion, because of the gap. Only transliteration
     * 
     */
    
    
    testAuthorQuery(
            "\"adamčuk, karel\"", "author:adamčuk, karel | author:adamčuk, karel * " +
            		                  "author:adamčuk, k | author:adamčuk, k * | author:adamčuk, " +
            		                  "author:adamcuk, karel | author:adamcuk, karel * " +
            		                  "author:adamcuk, k | author:adamcuk, k * | author:adamcuk, " +
            		                  "author:adamchuk, karel | author:adamchuk, karel * " +
            		                  "author:adamchuk, k | author:adamchuk, k * | author:adamchuk,", 
                               "//*[@numFound='12']",
                               // "adamčuk, karel" numFound=12
                               //   1 Adamčuk,                 9  Adamčuk, Karel Molja    10  Adamčuk, Karel M       
                               //  11 Adamčuk, K Molja        20  Adamcuk,                28  Adamcuk, Karel Molja   
                               //  29 Adamcuk, Karel M        30  Adamcuk, K Molja        40  Adamchuk,              
                               //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M       50  Adamchuk, K Molja
                               
            "\"adamcuk, karel\"", "author:adamcuk, karel | author:adamcuk, karel * " +
                                  "author:adamcuk, k | author:adamcuk, k * | author:adamcuk,",
                               "//*[@numFound='4']",
                               // "adamcuk, karel" numFound=4
                               //  20 Adamcuk,                28  Adamcuk, Karel Molja    29  Adamcuk, Karel M       
                               //  30 Adamcuk, K Molja
                               
            "\"adamchuk, karel\"", "author:adamchuk, karel | author:adamchuk, karel * " +
                                   "author:adamchuk, k | author:adamchuk, k * | author:adamchuk,", 
                                "//*[@numFound='4']",
                                // "adamchuk, karel" numFound=4
                                //  40 Adamchuk,               48  Adamchuk, Karel Molja   49  Adamchuk, Karel M      
                                //  50 Adamchuk, K Molja
                                
            "\"adamczuk, karel\"", "author:adamczuk, karel | author:adamczuk, karel * " +
                                   "author:adamczuk, k | author:adamczuk, k * | author:adamczuk,", 
                                "//*[@numFound='0']",
                                // "adamczuk, karel" numFound=0
                                
            "\"adamšuk, karel\"", "author:adamšuk, karel | author:adamšuk, karel * " +
            		                  "author:adamšuk, k | author:adamšuk, k * | author:adamšuk, " +
            		                  "author:adamshuk, karel | author:adamshuk, karel * " +
            		                  "author:adamshuk, k | author:adamshuk, k * | author:adamshuk, " +
            		                  "author:adamsuk, karel | author:adamsuk, karel * " +
            		                  "author:adamsuk, k | author:adamsuk, k * | author:adamsuk,", 
                               "//*[@numFound='4']",
                               // "adamšuk, karel" numFound=4
                               //  80 Adamshuk,               88  Adamshuk, Karel Molja   89  Adamshuk, Karel M      
                               //  90 Adamshuk, K Molja     
                               
            "\"adamguk, karel\"", "author:adamguk, karel | author:adamguk, karel * " +
                                  "author:adamguk, k | author:adamguk, k * | author:adamguk,", 
                               "//*[@numFound='4']"
                                  // "adamguk, karel" numFound=4
                                  //  60 Adamguk,                68  Adamguk, Karel Molja    69  Adamguk, Karel M       
                                  //  70 Adamguk, K Molja                                         
    );
    
    
    /**
     * <surname>, <2name> <1>
     * 
     * The order is not correct, therefore no expansion. Only transliteration
     * 
     */
    
    
    testAuthorQuery(
            "\"adamčuk, karel m\"", "author:adamčuk, karel m | author:adamčuk, karel m* " +
                                		"author:adamčuk, k m | author:adamčuk, k m* | author:adamčuk, karel " +
                                		"author:adamčuk, k | author:adamčuk, | author:adamchuk, karel m " +
                                		"author:adamchuk, karel m* | author:adamchuk, k m " +
                                		"author:adamchuk, k m* | author:adamchuk, karel | author:adamchuk, k " +
                                		"author:adamchuk, | author:adamcuk, karel m | author:adamcuk, karel m* " +
                                		"author:adamcuk, k m | author:adamcuk, k m* | author:adamcuk, karel " +
                                		"author:adamcuk, k | author:adamcuk,", 
                               "//*[@numFound='12']",
                               // "adamčuk, karel m" numFound=12
                               //   1 Adamčuk,                 9  Adamčuk, Karel Molja    10  Adamčuk, Karel M       
                               //  11 Adamčuk, K Molja        20  Adamcuk,                28  Adamcuk, Karel Molja   
                               //  29 Adamcuk, Karel M        30  Adamcuk, K Molja        40  Adamchuk,              
                               //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M       50  Adamchuk, K Molja     
                               
            "\"adamcuk, karel m\"", "author:adamcuk, karel m | author:adamcuk, karel m* | author:adamcuk, k m " +
            		                    "author:adamcuk, k m* | author:adamcuk, karel | author:adamcuk, k | author:adamcuk,",
                               "//*[@numFound='4']",
                               // "adamcuk, karel m" numFound=4
                               //  20 Adamcuk,                28  Adamcuk, Karel Molja    29  Adamcuk, Karel M       
                               //  30 Adamcuk, K Molja          
                               
            "\"adamchuk, karel m\"", 
                                    "author:adamchuk, karel m | author:adamchuk, karel m* | author:adamchuk, k m " +
                                    "author:adamchuk, k m* | author:adamchuk, karel | author:adamchuk, k | author:adamchuk,", 
                                "//*[@numFound='4']",
                                // "adamchuk, karel m" numFound=4
                                //  40 Adamchuk,               48  Adamchuk, Karel Molja   49  Adamchuk, Karel M      
                                //  50 Adamchuk, K Molja      
                                
            "\"adamczuk, karel m\"", 
                                    "author:adamczuk, karel m | author:adamczuk, karel m* | author:adamczuk, k m " +
                                    "author:adamczuk, k m* | author:adamczuk, karel | author:adamczuk, k | author:adamczuk,", 
                                "//*[@numFound='0']",
                                // "adamczuk, karel m" numFound=0
                                
            "\"adamšuk, karel m\"", "author:adamšuk, karel m | author:adamšuk, karel m* | author:adamšuk, k m " +
            		                    "author:adamšuk, k m* | author:adamšuk, karel | author:adamšuk, k | author:adamšuk, " +
            		                    "author:adamsuk, karel m | author:adamsuk, karel m* | author:adamsuk, k m " +
            		                    "author:adamsuk, k m* | author:adamsuk, karel | author:adamsuk, k | author:adamsuk, " +
            		                    "author:adamshuk, karel m | author:adamshuk, karel m* | author:adamshuk, k m " +
            		                    "author:adamshuk, k m* | author:adamshuk, karel | author:adamshuk, k " +
            		                    "author:adamshuk,", 
                               "//*[@numFound='4']",
                               // "adamšuk, karel m" numFound=4
                               //  80 Adamshuk,               88  Adamshuk, Karel Molja   89  Adamshuk, Karel M      
                               //  90 Adamshuk, K Molja                                
                               
            "\"adamguk, karel m\"", "author:adamguk, karel m | author:adamguk, karel m* | author:adamguk, k m " +
            		                    "author:adamguk, k m* | author:adamguk, karel | author:adamguk, k | author:adamguk,", 
                               "//*[@numFound='4']"
                                    // "adamguk, karel m" numFound=4
                                    //  60 Adamguk,                68  Adamguk, Karel Molja    69  Adamguk, Karel M       
                                    //  70 Adamguk, K Molja                		                    
    );
    
    
    /**
     * <surname>, <2name> <1name>
     * 
     * The order is not correct. Only transliteration
     * 
     */
    
    
    testAuthorQuery(
            "\"adamčuk, karel molja\"", "author:adamčuk, karel molja | author:adamčuk, karel molja * " +
            		                        "author:adamčuk, k molja | author:adamčuk, k molja * | author:adamčuk, karel m " +
            		                        "author:adamčuk, karel m * | author:adamčuk, k m | author:adamčuk, k m * " +
            		                        "author:adamčuk, karel | author:adamčuk, k | author:adamčuk, " +
            		                        "author:adamcuk, karel molja | author:adamcuk, karel molja * " +
            		                        "author:adamcuk, k molja | author:adamcuk, k molja * | author:adamcuk, karel m " +
            		                        "author:adamcuk, karel m * | author:adamcuk, k m | author:adamcuk, k m * " +
            		                        "author:adamcuk, karel | author:adamcuk, k | author:adamcuk, " +
            		                        "author:adamchuk, karel molja | author:adamchuk, karel molja * " +
            		                        "author:adamchuk, k molja | author:adamchuk, k molja * " +
            		                        "author:adamchuk, karel m | author:adamchuk, karel m * " +
            		                        "author:adamchuk, k m | author:adamchuk, k m * " +
            		                        "author:adamchuk, karel | author:adamchuk, k | author:adamchuk,", 
                               "//*[@numFound='12']",
                               // "adamčuk, karel molja" numFound=12
                               //   1 Adamčuk,                 9  Adamčuk, Karel Molja    10  Adamčuk, Karel M       
                               //  11 Adamčuk, K Molja        20  Adamcuk,                28  Adamcuk, Karel Molja   
                               //  29 Adamcuk, Karel M        30  Adamcuk, K Molja        40  Adamchuk,              
                               //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M       50  Adamchuk, K Molja   
                               
            "\"adamcuk, karel molja\"", "author:adamcuk, karel molja | author:adamcuk, karel molja * " +
            		                        "author:adamcuk, k molja | author:adamcuk, k molja * " +
            		                        "author:adamcuk, karel m | author:adamcuk, karel m * " +
            		                        "author:adamcuk, k m | author:adamcuk, k m * | author:adamcuk, karel " +
            		                        "author:adamcuk, k | author:adamcuk,",
                               "//*[@numFound='4']",
                               // "adamcuk, karel molja" numFound=4
                               //  20 Adamcuk,                28  Adamcuk, Karel Molja    29  Adamcuk, Karel M       
                               //  30 Adamcuk, K Molja
                               
            "\"adamchuk, karel molja\"", "author:adamchuk, karel molja | author:adamchuk, karel molja * " +
            		                         "author:adamchuk, k molja | author:adamchuk, k molja * " +
            		                         "author:adamchuk, karel m | author:adamchuk, karel m * " +
            		                         "author:adamchuk, k m | author:adamchuk, k m * | author:adamchuk, karel " +
            		                         "author:adamchuk, k | author:adamchuk,", 
                                "//*[@numFound='4']",
                                // "adamchuk, karel molja" numFound=4
                                //  40 Adamchuk,               48  Adamchuk, Karel Molja   49  Adamchuk, Karel M      
                                //  50 Adamchuk, K Molja    
                                
            "\"adamczuk, karel molja\"", "author:adamczuk, karel molja | author:adamczuk, karel molja * " +
            		                         "author:adamczuk, k molja | author:adamczuk, k molja * " +
            		                         "author:adamczuk, karel m | author:adamczuk, karel m * " +
            		                         "author:adamczuk, k m | author:adamczuk, k m * " +
            		                         "author:adamczuk, karel | author:adamczuk, k | author:adamczuk,", 
                                "//*[@numFound='0']",
                                
            "\"adamšuk, karel molja\"", "author:adamšuk, karel molja | author:adamšuk, karel molja * " +
            		                        "author:adamšuk, k molja | author:adamšuk, k molja * " +
            		                        "author:adamšuk, karel m | author:adamšuk, karel m * " +
            		                        "author:adamšuk, k m | author:adamšuk, k m * " +
            		                        "author:adamšuk, karel | author:adamšuk, k | author:adamšuk, " +
            		                        "author:adamsuk, karel molja | author:adamsuk, karel molja * " +
            		                        "author:adamsuk, k molja | author:adamsuk, k molja * " +
            		                        "author:adamsuk, karel m | author:adamsuk, karel m * " +
            		                        "author:adamsuk, k m | author:adamsuk, k m * | author:adamsuk, karel " +
            		                        "author:adamsuk, k | author:adamsuk, | author:adamshuk, karel molja " +
            		                        "author:adamshuk, karel molja * | author:adamshuk, k molja " +
            		                        "author:adamshuk, k molja * | author:adamshuk, karel m " +
            		                        "author:adamshuk, karel m * | author:adamshuk, k m " +
            		                        "author:adamshuk, k m * | author:adamshuk, karel " +
            		                        "author:adamshuk, k | author:adamshuk,", 
                               "//*[@numFound='4']",
                               // "adamšuk, karel molja" numFound=4
                               //  80 Adamshuk,               88  Adamshuk, Karel Molja   89  Adamshuk, Karel M      
                               //  90 Adamshuk, K Molja  
                               
            "\"adamguk, karel molja\"", "author:adamguk, karel molja | author:adamguk, karel molja * " +
            		                        "author:adamguk, k molja | author:adamguk, k molja * " +
            		                        "author:adamguk, karel m | author:adamguk, karel m * " +
            		                        "author:adamguk, k m | author:adamguk, k m * " +
            		                        "author:adamguk, karel | author:adamguk, k " +
            		                        "author:adamguk,", 
                               "//*[@numFound='4']"
                                        // "adamguk, karel molja" numFound=4
                                        //  60 Adamguk,                68  Adamguk, Karel Molja    69  Adamguk, Karel M       
                                        //  70 Adamguk, K Molja             		                        
    );
    
    
    /**
     * <surname>, <2> <1>
     * 
     * The order is not correct, therefore no expansion. Only transliteration
     * 
     */
    
    testAuthorQuery(
            "\"adamčuk, k m\"", "author:adamčuk, k m | author:adamčuk, k m* " +
            		                "author:/adamčuk, k[^ ]*/ | author:/adamčuk, k[^ ]* m.*/ " +
            		                "author:adamčuk, k | author:adamčuk, "
            		                + "author:adamchuk, k m | author:adamchuk, k m* " +
            		                "author:/adamchuk, k[^ ]*/ | author:/adamchuk, k[^ ]* m.*/ " +
            		                "author:adamchuk, k | author:adamchuk, "
            		                + "author:adamcuk, k m | author:adamcuk, k m* " +
            		                "author:/adamcuk, k[^ ]*/ | author:/adamcuk, k[^ ]* m.*/ " +
            		                "author:adamcuk, k | author:adamcuk,", 
                               "//*[@numFound='12']"
                                // "adamčuk, k m" numFound=12
                                //   1 Adamčuk,                 9  Adamčuk, Karel Molja    10  Adamčuk, Karel M       
                                //  11 Adamčuk, K Molja        20  Adamcuk,                28  Adamcuk, Karel Molja   
                                //  29 Adamcuk, Karel M        30  Adamcuk, K Molja        40  Adamchuk,              
                                //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M       50  Adamchuk, K Molja   
                               );
    testAuthorQuery(
            "\"adamcuk, k m\"", "author:adamcuk, k m | author:adamcuk, k m* " +
            		                "author:/adamcuk, k[^ ]*/ | author:/adamcuk, k[^ ]* m.*/ " +
                                "author:adamcuk, k | author:adamcuk,", 
                               "//*[@numFound='4']"
                               // "adamcuk, k m" numFound=4
                               //  20 Adamcuk,                29  Adamcuk, Karel M        30  Adamcuk, K Molja   
                               //  28 Adamcuk, Karel Molja
                               );
    testAuthorQuery(
            "\"adamchuk, k m\"", "author:adamchuk, k m | author:adamchuk, k m* " +
            		                 "author:/adamchuk, k[^ ]*/ | author:/adamchuk, k[^ ]* m.*/ " +
                                 "author:adamchuk, k | author:adamchuk,", 
                                "//*[@numFound='4']"
                                // "adamchuk, k m" numFound=4
                                //  40 Adamchuk,               49  Adamchuk, Karel M       50  Adamchuk, K Molja
                                //  xx Adamchuk, Karel Molja
                                );
    testAuthorQuery(
            "\"adamczuk, k m\"", "author:adamczuk, k m | author:adamczuk, k m* " +
            		                 "author:/adamczuk, k[^ ]*/ | author:/adamczuk, k[^ ]* m.*/ " +
                                 "author:adamczuk, k | author:adamczuk,", 
                                 "//*[@numFound='0']"
                                 // "adamczuk, k m" numFound=0
                                 );
    testAuthorQuery(
            "\"adamšuk, k m\"", "author:adamšuk, k m | author:adamšuk, k m* " +
            		                "author:/adamšuk, k[^ ]*/ | author:/adamšuk, k[^ ]* m.*/ " +
            		                "author:adamšuk, k | author:adamšuk, " +
            		                "author:adamshuk, k m | author:adamshuk, k m* " +
            		                "author:/adamshuk, k[^ ]*/ | author:/adamshuk, k[^ ]* m.*/ " +
            		                "author:adamshuk, k | author:adamshuk, " +
            		                "author:adamsuk, k m | author:adamsuk, k m* " +
            		                "author:/adamsuk, k[^ ]*/ | author:/adamsuk, k[^ ]* m.*/ " +
            		                "author:adamsuk, k | author:adamsuk,", 
                               "//*[@numFound='4']"
                               // "adamšuk, k m" numFound=4
                               //  80 Adamshuk,               89  Adamshuk, Karel M       90  Adamshuk, K Molja
            		               //  xx Adamshuk, Karel Molja
                               );
    testAuthorQuery(
            "\"adamguk, k m\"", "author:adamguk, k m | author:adamguk, k m* " +
                                "author:/adamguk, k[^ ]*/ | author:/adamguk, k[^ ]* m.*/ " +
                                "author:adamguk, k | author:adamguk,", 
                               "//*[@numFound='4']"
                                // "adamguk, k m" numFound=4
                                //  60 Adamguk,                69  Adamguk, Karel M        70  Adamguk, K Molja       
                                //  xx Adamguk, Karel Molja
    );
    
    
    /**
     * <surname>, <2> <1name>
     * 
     * The order is not correct, therefore no expansion. Only transliteration
     * 
     */
    
    
    testAuthorQuery(
            "\"adamčuk, k molja\"", "author:adamčuk, k molja | author:adamčuk, k molja * "
                + "author:/adamčuk, k[^ ]*/ | author:/adamčuk, k[^ ]* molja/ | author:/adamčuk, k[^ ]* molja .*/ "
                + "author:adamčuk, k m | author:adamčuk, k m * "
                + "author:/adamčuk, k[^ ]* m/ | author:/adamčuk, k[^ ]* m .*/ "
                + "author:adamčuk, k | author:adamčuk, | author:adamchuk, k molja | author:adamchuk, k molja * "
                + "author:/adamchuk, k[^ ]*/ | author:/adamchuk, k[^ ]* molja/ | author:/adamchuk, k[^ ]* molja .*/ "
                + "author:adamchuk, k m | author:adamchuk, k m * "
                + "author:/adamchuk, k[^ ]* m/ | author:/adamchuk, k[^ ]* m .*/ "
                + "author:adamchuk, k | author:adamchuk, | author:adamcuk, k molja | author:adamcuk, k molja * "
                + "author:/adamcuk, k[^ ]*/ | author:/adamcuk, k[^ ]* molja/ | author:/adamcuk, k[^ ]* molja .*/ "
                + "author:adamcuk, k m | author:adamcuk, k m * "
                + "author:/adamcuk, k[^ ]* m/ | author:/adamcuk, k[^ ]* m .*/ "
                + "author:adamcuk, k | author:adamcuk,", 
                               "//*[@numFound='12']"
                               // "adamčuk, k molja" numFound=12
                               //   1 Adamčuk,                 9  Adamčuk, Karel Molja    10  Adamčuk, Karel M       
                               //  11 Adamčuk, K Molja        20  Adamcuk,                28  Adamcuk, Karel Molja   
                               //  29 Adamcuk, Karel M        30  Adamcuk, K Molja        40  Adamchuk,              
                               //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M       50  Adamchuk, K Molja      
    );
    testAuthorQuery(
            "\"adamcuk, k molja\"", 
                                    "author:adamcuk, k molja | author:adamcuk, k molja * " +
                                    "author:/adamcuk, k[^ ]*/ | author:/adamcuk, k[^ ]* molja/ | author:/adamcuk, k[^ ]* molja .*/ " +
                                    "author:adamcuk, k m | author:adamcuk, k m * " +
                                    "author:/adamcuk, k[^ ]* m/ | author:/adamcuk, k[^ ]* m .*/ " +
                                    "author:adamcuk, k | author:adamcuk,", 
                               "//*[@numFound='4']"
                               // "adamcuk, k molja" numFound=4
                               //  20 Adamcuk,                28  Adamcuk, Karel Molja    29  Adamcuk, Karel M       
                               //  30 Adamcuk, K Molja       
    );
    testAuthorQuery(                               
            "\"adamchuk, k molja\"", "author:adamchuk, k molja | author:adamchuk, k molja * " +
                                    "author:/adamchuk, k[^ ]*/ | author:/adamchuk, k[^ ]* molja/ | author:/adamchuk, k[^ ]* molja .*/ " +
                                    "author:adamchuk, k m | author:adamchuk, k m * " +
                                    "author:/adamchuk, k[^ ]* m/ | author:/adamchuk, k[^ ]* m .*/ " +
                                    "author:adamchuk, k | author:adamchuk,", 
                                "//*[@numFound='4']"
                                // "adamchuk, k molja" numFound=4
                                //  40 Adamchuk,               48  Adamchuk, Karel Molja   49  Adamchuk, Karel M      
                                //  50 Adamchuk, K Molja   
        );
    testAuthorQuery(                                
            "\"adamczuk, k molja\"", "author:adamczuk, k molja | author:adamczuk, k molja * " +
                                    "author:/adamczuk, k[^ ]*/ | author:/adamczuk, k[^ ]* molja/ | author:/adamczuk, k[^ ]* molja .*/ " +
                                    "author:adamczuk, k m | author:adamczuk, k m * " +
                                    "author:/adamczuk, k[^ ]* m/ | author:/adamczuk, k[^ ]* m .*/ " +
                                    "author:adamczuk, k | author:adamczuk,", 
                                 "//*[@numFound='0']"
                                 // "adamczuk, k molja" numFound=0
            );
    testAuthorQuery(         
            "\"adamšuk, k molja\"", "author:adamšuk, k molja | author:adamšuk, k molja * " +
                                		"author:/adamšuk, k[^ ]*/ | author:/adamšuk, k[^ ]* molja/ | author:/adamšuk, k[^ ]* molja .*/ " +
                                		"author:adamšuk, k m | author:adamšuk, k m * | author:/adamšuk, k[^ ]* m/ " +
                                		"author:/adamšuk, k[^ ]* m .*/ | author:adamšuk, k | author:adamšuk, " +
                                		"author:adamsuk, k molja | author:adamsuk, k molja * " +
                                		"author:/adamsuk, k[^ ]*/ | author:/adamsuk, k[^ ]* molja/ | author:/adamsuk, k[^ ]* molja .*/ " +
                                		"author:adamsuk, k m | author:adamsuk, k m * | author:/adamsuk, k[^ ]* m/ " +
                                		"author:/adamsuk, k[^ ]* m .*/ | author:adamsuk, k | author:adamsuk, " +
                                		"author:adamshuk, k molja | author:adamshuk, k molja * " +
                                		"author:/adamshuk, k[^ ]*/ | author:/adamshuk, k[^ ]* molja/ | author:/adamshuk, k[^ ]* molja .*/ " +
                                		"author:adamshuk, k m | author:adamshuk, k m * | author:/adamshuk, k[^ ]* m/ " +
                                		"author:/adamshuk, k[^ ]* m .*/ | author:adamshuk, k | author:adamshuk,", 
                               "//*[@numFound='4']"
                               // "adamšuk, k molja" numFound=4
                               //  80 Adamshuk,               88  Adamshuk, Karel Molja   89  Adamshuk, Karel M      
                               //  90 Adamshuk, K Molja      
            );
    testAuthorQuery(       
            "\"adamguk, k molja\"", "author:adamguk, k molja | author:adamguk, k molja * " +
                                		"author:/adamguk, k[^ ]*/ | author:/adamguk, k[^ ]* molja/ | author:/adamguk, k[^ ]* molja .*/ " +
                                		"author:adamguk, k m | author:adamguk, k m * " +
                                		"author:/adamguk, k[^ ]* m/ | author:/adamguk, k[^ ]* m .*/ " +
                                		"author:adamguk, k | author:adamguk,", 
                               "//*[@numFound='4']"
                                    // "adamguk, k molja" numFound=4
                                    //  60 Adamguk,                68  Adamguk, Karel Molja    69  Adamguk, Karel M       
                                    //  70 Adamguk, K Molja                                       		
    );
    
    /**
     * <surname>, <1*>
     * <surname>, <1n*>
     * 
     * No expansion should happen if the <part*> has more than 2 characters, otherwise
     * it should work as if <surname>, <1> was specified
     * 
     */
    
    expected = "author:adamšuk, m | author:adamšuk, m* | author:adamšuk, " +
              "author:adamsuk, m | author:adamsuk, m* | author:adamsuk, " +
              "author:adamshuk, m | author:adamshuk, m* | author:adamshuk, " +
              "author:adamguk, m | author:adamguk, m* | author:adamguk, " +
              "author:adamčuk, m | author:adamčuk, m* | author:adamčuk, " +
              "author:adamchuk, m | author:adamchuk, m* | author:adamchuk, " +
              "author:adamcuk, m | author:adamcuk, m* | author:adamcuk,";
    
    testAuthorQuery(
            "\"adamčuk, m*\"", expected, 
            "//*[@numFound='40']"
            // "adamčuk, m*" numFound=40
            //   1 Adamčuk,                 2  Adamčuk, M.              3  Adamčuk, Marel         
            //   4 Adamčuk, Molja           5  Adamčuk, Molja Karel     6  Adamčuk, M Karel       
            //   7 Adamčuk, Molja K         8  Adamčuk, M K            20  Adamcuk,               
            //  21 Adamcuk, M.             22  Adamcuk, Marel          23  Adamcuk, Molja         
            //  24 Adamcuk, Molja Karel    25  Adamcuk, M Karel        26  Adamcuk, Molja K       
            //  27 Adamcuk, M K            40  Adamchuk,               41  Adamchuk, M.           
            //  42 Adamchuk, Marel         43  Adamchuk, Molja         44  Adamchuk, Molja Karel  
            //  45 Adamchuk, M Karel       46  Adamchuk, Molja K       47  Adamchuk, M K          
            //  60 Adamguk,                61  Adamguk, M.             62  Adamguk, Marel         
            //  63 Adamguk, Molja          64  Adamguk, Molja Karel    65  Adamguk, M Karel       
            //  66 Adamguk, Molja K        67  Adamguk, M K            80  Adamshuk,              
            //  81 Adamshuk, M.            82  Adamshuk, Marel         83  Adamshuk, Molja        
            //  84 Adamshuk, Molja Karel   85  Adamshuk, M Karel       86  Adamshuk, Molja K      
            //  87 Adamshuk, M K
    );
    testAuthorQuery(
            "\"adamcuk, m*\"", expected, "//*[@numFound='40']",
            "\"adamchuk, m*\"", expected, "//*[@numFound='40']"
    );
    testAuthorQuery(            
            "\"adamczuk, m*\"", expected + " | author:adamczuk, m | author:adamczuk, m* | author:adamczuk,", "//*[@numFound='40']",
            "\"adamšuk, m*\"", expected, "//*[@numFound='40']",
            "\"adamguk, m*\"", expected, "//*[@numFound='40']",
            
            "\"adamčuk, mo*\"", "author:adamčuk, mo*", "//*[@numFound='3']",
            // "adamčuk, mo*" numFound=3
            //   4 Adamčuk, Molja           5  Adamčuk, Molja Karel     7  Adamčuk, Molja K       
            
            "\"adamcuk, mo*\"", "author:adamcuk, mo*", "//*[@numFound='3']",
            // "adamcuk, mo*" numFound=3
            //  23 Adamcuk, Molja          24  Adamcuk, Molja Karel    26  Adamcuk, Molja K            
            "\"adamchuk, mo*\"", "author:adamchuk, mo*", "//*[@numFound='3']",
            // "adamchuk, mo*" numFound=3
            //  43 Adamchuk, Molja         44  Adamchuk, Molja Karel   46  Adamchuk, Molja K             
            "\"adamczuk, mo*\"", "author:adamczuk, mo*", "//*[@numFound='0']",
            // "adamczuk, mo*" numFound=0            
            "\"adamšuk, mo*\"", "author:adamšuk, mo*", "//*[@numFound='0']",
            // "adamšuk, mo*" numFound=0            
            "\"adamguk, mo*\"", "author:adamguk, mo*", "//*[@numFound='3']"
            // "adamguk, mo*" numFound=3
            //  63 Adamguk, Molja          64  Adamguk, Molja Karel    66  Adamguk, Molja K             
    );

    
    /**
     * <surname>, <2*>
     * <surname>, <2n*>
     * 
     * No expansion should happen if the <part*> has more than 2 characters, otherwise
     * it should work only if such a patter is in the synonym list (and there is none)
     * 
     */
    
    
    testAuthorQuery(
            "\"adamčuk, k*\"", "author:adamčuk, k | author:adamčuk, k* | author:adamčuk, " +
            		               "author:adamchuk, k | author:adamchuk, k* | author:adamchuk, " +
            		               "author:adamcuk, k | author:adamcuk, k* | author:adamcuk,", 
            		               "//*[@numFound='12']",
                               // "adamčuk, k*" numFound=12
                               //   1 Adamčuk,                 9  Adamčuk, Karel Molja    10  Adamčuk, Karel M       
                               //  11 Adamčuk, K Molja        20  Adamcuk,                28  Adamcuk, Karel Molja   
                               //  29 Adamcuk, Karel M        30  Adamcuk, K Molja        40  Adamchuk,              
                               //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M       50  Adamchuk, K Molja             		               
            "\"adamcuk, k*\"", "author:adamcuk, k | author:adamcuk, k* | author:adamcuk,", 
                               "//*[@numFound='4']",
                               // because there is no synonym mapping for "a, k" (but there is one for "a, m"!)
                               // "adamcuk, k*" numFound=4
                               //  20 Adamcuk,                28  Adamcuk, Karel Molja    29  Adamcuk, Karel M       
                               //  30 Adamcuk, K Molja  
                               
            "\"adamchuk, k*\"", "author:adamchuk, k | author:adamchuk, k* | author:adamchuk,", 
                                "//*[@numFound='4']",
                                // "adamchuk, k*" numFound=4
                                //  40 Adamchuk,               48  Adamchuk, Karel Molja   49  Adamchuk, Karel M      
                                //  50 Adamchuk, K Molja  
                                
            "\"adamczuk, k*\"", "author:adamczuk, k | author:adamczuk, k* | author:adamczuk,", 
                                "//*[@numFound='0']",
            "\"adamšuk, k*\"", "author:adamšuk, k | author:adamšuk, k* | author:adamšuk, " +
                               "author:adamsuk, k | author:adamsuk, k* | author:adamsuk, " +
                               "author:adamshuk, k | author:adamshuk, k* | author:adamshuk,", 
                               "//*[@numFound='4']",
                               // "adamšuk, k*" numFound=4
                               //  80 Adamshuk,               88  Adamshuk, Karel Molja   89  Adamshuk, Karel M      
                               //  90 Adamshuk, K Molja 
                               
            "\"adamguk, k*\"", "author:adamguk, k | author:adamguk, k* | author:adamguk,", 
                               "//*[@numFound='4']",
                               // "adamguk, k*" numFound=4
                               //  60 Adamguk,                68  Adamguk, Karel Molja    69  Adamguk, Karel M       
                               //  70 Adamguk, K Molja  
                               
            "\"adamčuk, ka*\"", "author:adamčuk, ka*", "//*[@numFound='2']",
            //   9 Adamčuk, Karel Molja    10  Adamčuk, Karel M             
            "\"adamcuk, ka*\"", "author:adamcuk, ka*", "//*[@numFound='2']",
            // "adamcuk, ka*" numFound=2
            //  28 Adamcuk, Karel Molja    29  Adamcuk, Karel M                 
            "\"adamchuk, ka*\"", "author:adamchuk, ka*", "//*[@numFound='2']",
            // "adamchuk, ka*" numFound=2
            //  48 Adamchuk, Karel Molja   49  Adamchuk, Karel M              
            "\"adamczuk, ka*\"", "author:adamczuk, ka*", "//*[@numFound='0']",
            "\"adamšuk, ka*\"", "author:adamšuk, ka*", "//*[@numFound='0']",
            // "adamšuk, ka*" numFound=0            
            "\"adamguk, ka*\"", "author:adamguk, ka*", "//*[@numFound='2']"
            // "adamguk, ka*" numFound=2
            //  28 Adamguk, Karel Molja    29  Adamguk, Karel M                 
    );

    
    /**
     * 
     * The special case of synonym expansion called "semantic upgrade"
     * Basically, if the user input is too short - eg. "jones, c"
     * and our synonym file contains only these entries
     * "jones, christine; forman,christine"
     * 
     * Then we want to be able to find that "jones, c" corresponds to 
     * "jones, christine" and add the "forman, christine" and
     * "forman, c" to the expanded synonyms. However, WE DO NOT want
     * "forman, c*" search, but we want "jones, c*" search
     * 
     */
    
    testAuthorQuery(
        //must NOT have "jones*", must have "jones, c;jones, christine"
        "forman", "author:forman, | author:forman, c | author:jones, christine | author:jones, c " +
        		      "author:forman, christine | author:forman,*",
                         "//*[@numFound='7']",
                         // forman numFound=7
                         // 110 Jones, Christine       111  Jones, C               112  Forman, Christine      
                         // 113 Forman, C              115  Jones, C               116  Forman, Christopher    
                         // 117 Forman, C  
        //must NOT have "forman*", must have "forman, c;forman, christine"
        // PLUS - must have other jones's and allen's
        "jones", "author:jones, | author:jones, l | author:allen, l | author:allen, r l " +
        		     "author:allen, lynne | author:jones, r l | author:jones, r lynne | author:jones, lynne " +
        		     "author:allen, r lynne | author:forman, c | author:jones, christine | author:jones, c " +
        		     "author:forman, christine | author:jones,*",
                          "//*[@numFound='15']",
                          // jones numFound=15
                          // 110 Jones, Christine       111  Jones, C               112  Forman, Christine      
                          // 113 Forman, C              114  Jones, Christopher     115  Jones, C               
                          // 117 Forman, C              120  Allen, Lynne           121  Allen, L               
                          // 122 Allen, R Lynne         123  Allen, R L             124  Jones, Lynne           
                          // 125 Jones, L               126  Jones, R Lynne         127  Jones, R L             
        //must NOT have "jones, c*", must have "jones, christine"
        "\"forman, c\"", "author:forman, c | author:forman, christine | author:forman, c* | author:forman," +
        		             "author:jones, christine | author:jones, c",
                         "//*[@numFound='7']",
                         // "forman, c" numFound=7
                         // 110 Jones, Christine       111  Jones, C               112  Forman, Christine      
                         // 113 Forman, C              115  Jones, C               116  Forman, Christopher    
                         // 117 Forman, C   
                         
        //must NOT have "forman, c*", must have "forman, christine"
        "\"jones, c\"", "author:jones, c | author:jones, christine | author:jones, c* | author:jones," +
        		            "author:forman, christine | author:forman, c",
                         "//*[@numFound='7']",
                         // "jones, c" numFound=7
                         // 110 Jones, Christine       111  Jones, C               112  Forman, Christine      
                         // 113 Forman, C              114  Jones, Christopher     115  Jones, C               
                         // 117 Forman, C                          
                         
        "\"jones, christine\"", 
                        "author:jones, christine | author:jones, christine * | author:jones, c " +
                        "author:jones, c * | author:jones, | author:forman, christine " +
                        "author:forman, christine * | author:forman, c | author:forman, c * " +
                        "author:forman,", 
                        "//*[@numFound='6']",
                        // "jones, christine" numFound=6
                        // 110 Jones, Christine       111  Jones, C               112  Forman, Christine      
                        // 113 Forman, C              115  Jones, C               117  Forman, C 
                        
        "\"forman, christine\"", "author:jones, christine | author:jones, christine * | author:jones, c " +
        		            "author:jones, c * | author:jones, | author:forman, christine | author:forman, christine * " +
        		            "author:forman, c | author:forman, c * | author:forman,", 
        		            "//*[@numFound='6']"
    );
    
    

    /**
     * THE OLD STYLE, SO THAT I CAN COMPARE
    assertQueryEquals(req("qt", "aqp", "q", "author:\"Adamčuk, m\""), 
        //"author:adamčuk, m | author:adamcuk, m | author:adamchuk, m | author:adamčuk, | author:adamčuk, m* | author:adamchuk, marel | author:adamčuk, marel | author:adamcuk, molja | author:adamcuk, marel | author:adamčuk, molja | author:adamchuk, molja | author:adamchuk, m* | author:adamchuk, | author:adamcuk, | author:adamcuk, m*",
        "author:adamčuk, m | author:adamcuk, m | author:adamchuk, m | author:adamčuk, | author:adamčuk, m* | author:adamchuk, m* | author:adamchuk, | author:adamcuk, | author:adamcuk, m*",
        BooleanQuery.class);

    assertQueryEquals(req("qt", "aqp", "q", "author:\"ADAMČuk, m\""), 
        //"author:adamčuk, m | author:adamcuk, m | author:adamchuk, m | author:adamčuk, | author:adamčuk, m* | author:adamchuk, marel | author:adamčuk, marel | author:adamcuk, molja | author:adamcuk, marel | author:adamčuk, molja | author:adamchuk, molja | author:adamchuk, m* | author:adamchuk, | author:adamcuk, | author:adamcuk, m*",
        "author:adamčuk, m | author:adamcuk, m | author:adamchuk, m | author:adamčuk, | author:adamčuk, m* | author:adamchuk, m* | author:adamchuk, | author:adamcuk, | author:adamcuk, m*",
        BooleanQuery.class);

    assertQueryEquals(req("qt", "aqp", "q", "author:\"adamchuk, m\""), 
        //"author:adamchuk, m | author:adamcuk, m | author:adamčuk, m | author:adamchuk, m* | author:adamchuk, marel | author:adamčuk, marel | author:adamcuk, molja | author:adamcuk, marel | author:adamchuk, molja | author:adamčuk, molja | author:adamchuk,",
        "author:adamchuk, m | author:adamcuk, m | author:adamčuk, m | author:adamchuk, m* | author:adamchuk,",
        BooleanQuery.class);
     **/


    assertQueryEquals(req("defType", "aqp", "q", "author:\"Muller, William\""),
        // this was the old-style result, note "muller, w*"
        //"author:muller, w | author:muller, w* | author:muller, william | author:müller, william | author:mueller, william | author:muller,",
        "author:müller, william | author:müller, william * " +
        "| author:müller, w | author:müller, w * " +
        "| author:müller, " +
        "| author:muller, william | author:muller, william * " +
        "| author:muller, w | author:muller, w * " +
        "| author:muller, " +
        "| author:mueller, william | author:mueller, william * " +
        "| author:mueller, w | author:mueller, w * " +
        "| author:mueller, " +
        "| author:müller, bill | author:müller, bill * " +
        "| author:müller, b | author:müller, b * " +
        "| author:mueller, bill | author:mueller, bill * " +
        "| author:mueller, b | author:mueller, b * " +
        "| author:muller, bill | author:muller, bill * " +
        "| author:muller, b | author:muller, b *",
        DisjunctionMaxQuery.class);

    
    
    /*
     * 
    TODO: 
    
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

    */
    
    
    /*
     * Test we are not mixing/concatenating fields - Ticket #346 
     */
    assertQueryEquals(req("q", "author:\"obama,\" boooo", "df", "all"), "+(author:obama, | author:obama,*) +all:boooo", BooleanQuery.class);
    
  }


  private void testAuthorQuery(String...vals) throws Exception {
    assert vals.length%3==0;
    for (int i=0;i<vals.length;i=i+3) {
      
      System.out.println(escapeUnicode(vals[i]));
      
      
      boolean failed = true;
      try {
	      assertQueryEquals(req("defType", "aqp", "q", author_field + ":" + vals[i]),
	          vals[i+1],
	          null);
	      assertQ(req("fl", "id," + author_field, "rows", "100", "q", author_field + ":" + vals[i]), vals[i+2].split(";"));
	      failed = false;
      }
      finally {
      	if (failed) {
      	  
          System.out.println(escapeUnicode(vals[i]));
          System.out.println("Running test for " + author_field + ":" + vals[i]);
          String response = h.query(req("fl", "id,author", "rows", "100", "defType", "aqp", "q", String.format("%s:%s", author_field, vals[i])));
          
          ArrayList<String> out = new ArrayList<String>();
          Matcher m = Pattern.compile("numFound=\\\"(\\d+)").matcher(response);
          Matcher m2 = Pattern.compile("<doc><str name=\\\"id\\\">(\\d+)</str><arr name=\\\"" + author_field + "\\\"><str>([^<]*)</str></arr></doc>").matcher(response);
          m.find();
          String numFound = m.group(1);
          while (m2.find()) {
            out.add(String.format("%0$3s\t%2$-23s", m2.group(1), m2.group(2)));
          }
          Collections.sort(out);
          System.out.print("                             // " + vals[i] + " numFound=" + numFound);
          int j=0;
          for (String s: out) {
            if (j%3==0) {
              System.out.print("\n                             // ");
            }
            System.out.print(s);
            j++;
          }
          System.out.println();
      	  
      	  QParser qParser = getParser(req("fl", "id," + author_field, "rows", "100", "q", author_field + ":" + vals[i]));
          Query q = qParser.parse();
          String actual = q.toString("field");
      		System.out.println("Offending test case: " + escapeUnicode(vals[i]) + "\nexpected vs actual: \n" + escapeUnicode(vals[i+1]) + "\n" + escapeUnicode(actual));
      		
      		
      	}
      }
    }
  }

  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeAuthorParsing.class);
  }

  /* XXX:rca - it was not used, to remove?
   * 
   *
  public void assertQ(String message, SolrQueryRequest req, String... tests) {
    try {
      String m = (null == message) ? "" : message + " ";
      String response = h.query(req);
      String results = h.validateXPath(response, tests);
      if (null != results) {
        tp.debugFail(m + "query failed XPath: " + results +
            "\n xml response was: " + response +
            "\n request was: " + req.getParamString());
      }
    } catch (XPathExpressionException e1) {
      throw new RuntimeException("XPath is invalid", e1);
    } catch (Exception e2) {
      throw new RuntimeException("Exception during query", e2);
    }
  }
  */

  public Query assertQueryEquals(SolrQueryRequest req, String expected, Class<?> clazz)
  throws Exception {

    QParser qParser = getParser(req);
    String query = req.getParams().get(CommonParams.Q);
    Query q = qParser.parse();
    
    String actual = q.toString("field");
    
    if (expected.startsWith("("))
      expected = expected.substring(1, expected.length()-1);
    if (actual.startsWith("("))
      actual = actual.substring(1, actual.length()-1);
    
    String[] ex = expected.split("(\\s\\|\\s|\\s)*[a-z]+\\:");
    Arrays.sort(ex);
    String[] ac = actual.split("(\\s\\|\\s|\\s)*[a-z]+\\:");
    Arrays.sort(ac);
    StringBuffer exs = new StringBuffer();
    for (String s: ex) {
    	if (s.trim().equals(""))
    		continue;
    	if (exs.length() > 0)
    		exs.append(" | ");
      exs.append(s.trim());
    }
    StringBuffer acs = new StringBuffer();
    for (String s: ac) {
    	if (s.trim().equals(""))
    		continue;
    	if (acs.length() > 0)
    		acs.append(" | ");
      acs.append(s.trim());
    }
    
    if (!acs.toString().equals(exs.toString())) {
      //assertArrayEquals(ac, ex);
      //tp.debugFail(query, expected, actual);
      tp.debugFail(query, exs.toString(), acs.toString());
    }

    if (clazz != null) {
      if (!q.getClass().isAssignableFrom(clazz)) {
        tp.debugFail("Query is not: " + clazz + " but: " + q.getClass());
      }
    }

    return q;
  }
  
  public String escapeUnicode(String input) {
    StringBuilder b = new StringBuilder(input.length());
    Formatter f = new Formatter(b);
    for (char c : input.toCharArray()) {
      if (c < 128) {
        b.append(c);
      } else {
        f.format("\\u%04x", (int) c);
      }
    }
    return b.toString();
  }
}
