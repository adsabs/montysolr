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
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathExpressionException;

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
 */
public class TestAdsabsTypeAuthorParsing extends MontySolrQueryTestCase {


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
          "ARAGON SALAMANCA, A;ARAGON-SALAMANCA, A;ARAGON, A;SALAMANCA, A", // copied from: /proj/ads/abstracts/config/author.syn
          "ADAMŠuk, m;ADAMGuk, m;ADAMČuk, m",  // hand-made additions
          "MÜLLER, A WILLIAM;MÜLLER, A BILL",
          "MÜLLER, WILLIAM;MÜLLER, BILL",
          "JONES, CHRISTINE;FORMAN, CHRISTINE" // the famous post-synonym expansion
      }));

      // automatically harvested variations of author names (collected during indexing)
      // it will be enriched by the indexing
      File generatedTransliterations = createTempFile(formatSynonyms(new String[]{
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
    assertU(commit());

    // persist the transliteration map after new docs were indexed
    // and reload synonym chain harvested during indexing
    Analyzer iAnalyzer = h.getCore().getSchema().getAnalyzer();
    Analyzer qAnalyzer = h.getCore().getSchema().getQueryAnalyzer();

    TokenStream iAuthor = iAnalyzer.tokenStream("author", new StringReader(""));
    TokenStream qAuthor = qAnalyzer.tokenStream("author", new StringReader(""));

    iAuthor.reset();
    iAuthor.reset();
    iAuthor.reset();

    qAuthor.reset();
    qAuthor.reset();
    qAuthor.reset();

    // TODO: force reload of the synonym map
    //h.getCoreContainer().reload("collection1");

    /*
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

    expected = "author:adamčuk, author:adamčuk,* " + // query variants added by parser
               "author:adamchuk, author:adamchuk,* " +
               "author:adamcuk, author:adamcuk,*";


    /**
     * <surname>
     * 
     * upgraded && transliterated 
     * synonym adamšuk IS NOT FOUND because there is no  entry for "adam(č|c|ch)uk" the syn list
     */
    testAuthorQuery(
        "adamčuk", expected, "//*[@numFound='33']",
        "adamcuk", expected, "//*[@numFound='33']",
        "adamchuk", expected, "//*[@numFound='33']",
        "adamczuk", expected, "//*[@numFound='33']",
        "adamšuk", "author:adamšuk, author:adamšuk,* " +
                   "author:adamshuk, author:adamshuk,* " +
                   "author:adamsuk, author:adamsuk,*", "//*[@numFound='11']",
        "adamguk", "author:adamguk, author:adamguk,*", "//*[@numFound='11']"
    );

    //setDebug(true);
    /**
     * <surname>,
     * 
     * upgraded && transliterated 
     * synonym adamšuk IS NOT FOUND because there is no  entry for "adam(č|c|ch)uk" the syn list
     */
    testAuthorQuery(
        "\"adamčuk,\"", expected, "//*[@numFound='33']",
        "\"adamcuk,\"", expected, "//*[@numFound='33']",
        "\"adamchuk,\"", expected, "//*[@numFound='33']",
        "\"adamczuk,\"", expected, "//*[@numFound='33']",
        "\"adamšuk,\"", "author:adamšuk, author:adamšuk,* " +
                        "author:adamshuk, author:adamshuk,* " +
                        "author:adamsuk, author:adamsuk,*", "//*[@numFound='11']",
        "\"adamguk,\"", "author:adamguk, author:adamguk,*", "//*[@numFound='11']"
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
    expected = "author:adamšuk, m author:adamšuk, m* author:adamšuk, " +
                "author:adamsuk, m author:adamsuk, m* author:adamsuk, " +
                "author:adamshuk, m author:adamshuk, m* author:adamshuk, " +
                "author:adamguk, m author:adamguk, m* author:adamguk, " +
                "author:adamčuk, m author:adamčuk, m* author:adamčuk, " +
                "author:adamchuk, m author:adamchuk, m* author:adamchuk, " +
                "author:adamcuk, m author:adamcuk, m* author:adamcuk,";

    testAuthorQuery(
        "\"adamčuk, m\"", expected, "//*[@numFound='40']",
        "\"adamcuk, m\"", expected, "//*[@numFound='40']",
        "\"adamchuk, m\"", expected, "//*[@numFound='40']",
        "\"adamczuk, m\"", expected, "//*[@numFound='40']",
        "\"adamšuk, m\"", expected, "//*[@numFound='40']",
        "\"adamguk, m\"", expected, "//*[@numFound='40']",

        "\"AdAmČuk, m\"", expected, "//*[@numFound='40']", // just for fun
        "\"ADAMČuk, m\"", expected, "//*[@numFound='40']",
        "\"AdAmCHuk, m\"", expected, "//*[@numFound='40']"
    );


    /**
     * <surname>, <1name>
     * 
     *  upgraded && transliterated && expanded
     *  synonym "adamšuk, m" IS FOUND because of the query variation for "adamčuk, m" the syn list
     */
    
    // base part, must be present in all
    expected0 = 
    		       "author:adamčuk, m author:adamčuk, m * author:adamčuk, " +
    		       "author:adamcuk, m author:adamcuk, m * author:adamcuk, " +
    		       "author:adamchuk, m author:adamchuk, m * author:adamchuk, " +
    		       "author:adamšuk, m author:adamšuk, m * author:adamšuk, " +
    		       "author:adamsuk, m author:adamsuk, m * author:adamsuk, " +
    		       "author:adamshuk, m author:adamshuk, m * author:adamshuk, " +
    		       "author:adamguk, m author:adamguk, m * author:adamguk, ";

    expected = expected0 +
               "author:adamčuk, molja author:adamčuk, molja * " +
               "author:adamchuk, molja author:adamchuk, molja * " +
               "author:adamcuk, molja author:adamcuk, molja *"
               ;
    
    
    testAuthorQuery(
        "\"adamčuk, molja\"", expected, "//*[@numFound='29']",
        "\"adamcuk, molja\"", expected, "//*[@numFound='29']",
        "\"adamchuk, molja\"", expected, "//*[@numFound='29']",
        "\"adamczuk, molja\"", expected, "//*[@numFound='29']",
        // "adamčuk, molja" is not there (and cannot be, because it is not in
        // synonym map, but synonym "adamšuk, m" is found correctly)
        "\"adamšuk, molja\"", expected0 +
                              "author:adamšuk, molja author:adamšuk, molja * " +
                              "author:adamshuk, molja author:adamshuk, molja * " +
                              "author:adamsuk, molja author:adamsuk, molja *", 
                              "//*[@numFound='23']",
        // shorter by two variants, because "adamguk, molja" is already ascii form
        // it doesn't generate: "author:adamshuk, molja author:adamsuk, molja"
        // that is correct, because "adamšuk, m" is found and transliterated
        // "adamšuk, molja" simply isn't in any synonym list and we tehrefore cannot have it 
        "\"adamguk, molja\"", expected0 +
                              "author:adamguk, molja author:adamguk, molja *",
                              "//*[@numFound='23']"
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
    
    expected = "author:adamčuk, molja k author:adamčuk, molja k* " +
    		       "author:adamčuk, m k author:adamčuk, m k* " +
    		       "author:adamčuk, molja " + // ! author:adamčuk, molja *
    		       "author:adamčuk, m " + // ! author:adamčuk, m*
    		       "author:adamčuk, " +
    		       "author:adamcuk, molja k author:adamcuk, molja k* " +
    		       "author:adamcuk, m k author:adamcuk, m k* " +
    		       "author:adamcuk, molja " + // ! author:adamcuk, molja * 
    		       "author:adamcuk, m " + // ! author:adamcuk, m* 
    		       "author:adamcuk, " +
    		       "author:adamchuk, molja k author:adamchuk, molja k* " +
    		       "author:adamchuk, m k author:adamchuk, m k* " +
    		       "author:adamchuk, molja " + // ! author:adamchuk, molja * 
    		       "author:adamchuk, m " + // ! author:adamchuk, m*
    		       "author:adamchuk,";
      

    testAuthorQuery(
        "\"adamčuk, molja k\"", expected, "//*[@numFound='21']",
        "\"adamcuk, molja k\"", expected, "//*[@numFound='21']",
        "\"adamchuk, molja k\"", expected, "//*[@numFound='21']",
        // this contains 4 more entries because by default, the
        // transliteration produces only adam(c|ch)uk
        "\"adamczuk, molja k\"", expected + 
                          " author:adamczuk, m k author:adamczuk, m k* author:adamczuk, m author:adamczuk,", 
                          "//*[@numFound='21']",
        "\"adamšuk, molja k\"", 
            "author:adamšuk, molja k author:adamšuk, molja k* " +
            "author:adamšuk, m k author:adamšuk, m k* " +
            "author:adamšuk, molja " +
            "author:adamšuk, m " +
            "author:adamšuk, " +
            "author:adamsuk, molja k author:adamsuk, molja k* " +
            "author:adamsuk, m k author:adamsuk, m k* " +
            "author:adamsuk, molja " +
            "author:adamsuk, m " +
            "author:adamsuk, " +
            "author:adamshuk, molja k author:adamshuk, molja k* " +
            "author:adamshuk, m k author:adamshuk, m k* " +
            "author:adamshuk, molja " +
            "author:adamshuk, m " +
            "author:adamshuk,", 
              "//*[@numFound='7']",
        "\"adamguk, molja k\"", 
            "author:adamguk, molja k author:adamguk, molja k* " +
            "author:adamguk, m k author:adamguk, m k* " +
            "author:adamguk, molja " +
            "author:adamguk, m " +
            "author:adamguk,", 
            "//*[@numFound='7']"
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
    expected0 = "author:adamčuk, molja k author:adamčuk, molja k * " +
                "author:adamčuk, m k author:adamčuk, m k * " +
                "author:adamčuk, molja " + // <- in my opinion this is wrong (too much recall), but it was requested
                "author:adamčuk, m " + 
                "author:adamčuk, " +
                "author:adamcuk, molja k author:adamcuk, molja k * " +
                "author:adamcuk, m k author:adamcuk, m k * " +
                "author:adamcuk, molja " +  // dtto
                "author:adamcuk, m " +  
                "author:adamcuk, " +
                "author:adamchuk, molja k author:adamchuk, molja k * " +
                "author:adamchuk, m k author:adamchuk, m k * " +
                "author:adamchuk, molja " +  //dtto
                "author:adamchuk, m " + 
                "author:adamchuk,";
    
    //dumpDoc(null, "id", "author");
    
    testAuthorQuery(
        "\"adamčuk, molja karel\"", expected0 + " " + 
                                    "author:adamčuk, molja karel author:adamčuk, molja karel * " +
                                    "author:adamčuk, m karel author:adamčuk, m karel * " +
                                    "author:adamchuk, molja karel author:adamchuk, molja karel * " +
                                    "author:adamchuk, m karel author:adamchuk, m karel * " +
                                    "author:adamcuk, molja karel author:adamcuk, molja karel * " +
                                    "author:adamcuk, m karel author:adamcuk, m karel *", 
                                    "//*[@numFound='21']",
        "\"adamcuk, molja karel\"", expected0 + " " + 
                                    "author:adamcuk, molja karel author:adamcuk, molja karel * " +
                                    "author:adamcuk, m karel author:adamcuk, m karel *",
                                    "//*[@numFound='17']", // because adamcuk, m\w* k\w* is not searched
        "\"adamchuk, molja karel\"", expected0 + " " + 
                                    "author:adamchuk, molja karel author:adamchuk, molja karel * " +
                                    "author:adamchuk, m karel author:adamchuk, m karel *",
                                    "//*[@numFound='17']",
        "\"adamczuk, molja karel\"", expected0 + " " + 
                                    "author:adamczuk, molja karel author:adamczuk, molja karel * " +
                                    "author:adamczuk, m karel author:adamczuk, m karel * " +
                                    "author:adamczuk, molja k author:adamczuk, molja k * " +
                                    "author:adamczuk, m k author:adamczuk, m k * " +
                                    "author:adamczuk, molja author:adamczuk, m " +
                                    "author:adamczuk,",
                                    "//*[@numFound='15']",//-3 because "č"->"cz" normally doesn't exist
        // almost exactly the same as above, the only difference must be the space before *
        "\"adamšuk, molja karel\"", "author:adamšuk, molja k author:adamšuk, molja k * " +
                                    "author:adamšuk, m k author:adamšuk, m k * " +
                                    "author:adamšuk, molja " +
                                    "author:adamšuk, m " +
                                    "author:adamšuk, " +
                                    "author:adamsuk, molja k author:adamsuk, molja k * " +
                                    "author:adamsuk, m k author:adamsuk, m k * " +
                                    "author:adamsuk, molja " +
                                    "author:adamsuk, m " +
                                    "author:adamsuk, " +
                                    "author:adamshuk, molja k author:adamshuk, molja k * " +
                                    "author:adamshuk, m k author:adamshuk, m k * " +
                                    "author:adamshuk, molja " +
                                    "author:adamshuk, m " +
                                    "author:adamshuk, " + 
                                    // plus variants with karel
                                    "author:adamšuk, molja karel author:adamšuk, molja karel * " +
                                    "author:adamšuk, m karel author:adamšuk, m karel * " + 
                                    "author:adamshuk, molja karel author:adamshuk, molja karel * " +
                                    "author:adamshuk, m karel author:adamshuk, m karel * " +
                                    "author:adamsuk, molja karel author:adamsuk, molja karel * " +
                                    "author:adamsuk, m karel author:adamsuk, m karel *",
                                    "//*[@numFound='7']",
        "\"adamguk, molja karel\"", "author:adamguk, molja k author:adamguk, molja k * " +
                                    "author:adamguk, m k author:adamguk, m k * " +
                                    "author:adamguk, molja " +
                                    "author:adamguk, m " +
                                    "author:adamguk, " + 
                                    // plus variants with karel
                                    "author:adamguk, molja karel author:adamguk, molja karel * " +
                                    "author:adamguk, m karel author:adamguk, m karel *",
                                    "//*[@numFound='7']"
        
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

    
    dumpDoc(null, "id", "author");
    //setDebug(true);
    testAuthorQuery(
        "\"adamčuk, m karel\"", "author:adamčuk, m karel author:adamčuk, m karel * " +
                                "author:adamčuk, m k author:adamčuk, m k * " +
                                "author:adamčuk, m " + 
                                "author:adamčuk, " +
                                "author:adamcuk, m karel author:adamcuk, m karel * " +
                                "author:adamcuk, m k author:adamcuk, m k * " +
                                "author:adamcuk, m " +  
                                "author:adamcuk, " +
                                "author:adamchuk, m karel author:adamchuk, m karel * " +
                                "author:adamchuk, m k author:adamchuk, m k * " +
                                "author:adamchuk, m " + 
                                "author:adamchuk, " +
                                "author:/adamčuk, m[^ ]+ karel/ " +
                                "author:/adamčuk, m[^ ]+ karel .*/ " +
                                "author:/adamčuk, m[^ ]+ k/ " +
                                "author:/adamčuk, m[^ ]+ k .*/ " +
                                "author:/adamcuk, m[^ ]+ karel/ " +
                                "author:/adamcuk, m[^ ]+ karel .*/ " +
                                "author:/adamcuk, m[^ ]+ k/ " +
                                "author:/adamcuk, m[^ ]+ k .*/ " +
                                "author:/adamchuk, m[^ ]+ karel/ " +
                                "author:/adamchuk, m[^ ]+ karel .*/ " +
                                "author:/adamchuk, m[^ ]+ k/ " +
                                "author:/adamchuk, m[^ ]+ k .*/" ,
                                 "//*[@numFound='18']",
        "\"adamcuk, m karel\"", "author:adamcuk, m karel author:adamcuk, m karel * " +
                                "author:/adamcuk, m[^ ]+ karel/ author:/adamcuk, m[^ ]+ karel .*/ " +
                                "author:adamcuk, m k author:adamcuk, m k * " +
                                "author:/adamcuk, m[^ ]+ k/ author:/adamcuk, m[^ ]+ k .*/ " +
                                "author:adamcuk, m author:adamcuk," ,
                                "//*[@numFound='6']",
        "\"adamchuk, m karel\"", "author:adamchuk, m karel author:adamchuk, m karel * " +
                                 "author:/adamchuk, m[^ ]+ karel/ author:/adamchuk, m[^ ]+ karel .*/ " +
                                 "author:adamchuk, m k author:adamchuk, m k * " +
                                 "author:/adamchuk, m[^ ]+ k/ author:/adamchuk, m[^ ]+ k .*/ " +
                                 "author:adamchuk, m author:adamchuk," ,
                                 "//*[@numFound='6']",
        "\"adamczuk, m karel\"", "author:adamczuk, m karel author:adamczuk, m karel * " +
                                 "author:/adamczuk, m[^ ]+ karel/ author:/adamczuk, m[^ ]+ karel .*/ " +
                                 "author:adamczuk, m k author:adamczuk, m k * " +
                                 "author:/adamczuk, m[^ ]+ k/ author:/adamczuk, m[^ ]+ k .*/ " +
                                 "author:adamczuk, m author:adamczuk," ,
                                 "//*[@numFound='0']",
        "\"adamšuk, m karel\"", "author:adamšuk, m karel author:adamšuk, m karel * " +
        		                    "author:/adamšuk, m[^ ]+ karel/ author:/adamšuk, m[^ ]+ karel .*/ " +
        		                    "author:adamšuk, m k author:adamšuk, m k * " +
        		                    "author:/adamšuk, m[^ ]+ k/ author:/adamšuk, m[^ ]+ k .*/ " +
        		                    "author:adamšuk, m " +
        		                    "author:adamšuk, " +
        		                    "author:adamsuk, m karel author:adamsuk, m karel * " +
        		                    "author:/adamsuk, m[^ ]+ karel/ author:/adamsuk, m[^ ]+ karel .*/ " +
        		                    "author:adamsuk, m k author:adamsuk, m k * " +
        		                    "author:/adamsuk, m[^ ]+ k/ author:/adamsuk, m[^ ]+ k .*/ " +
        		                    "author:adamsuk, m " +
        		                    "author:adamsuk, " +
        		                    "author:adamshuk, m karel author:adamshuk, m karel * " +
        		                    "author:/adamshuk, m[^ ]+ karel/ author:/adamshuk, m[^ ]+ karel .*/ " +
        		                    "author:adamshuk, m k author:adamshuk, m k * " +
        		                    "author:/adamshuk, m[^ ]+ k/ author:/adamshuk, m[^ ]+ k .*/ " +
        		                    "author:adamshuk, m " +
        		                    "author:adamshuk,",
                                "//*[@numFound='6']",
        "\"adamguk, m karel\"", "author:adamguk, m karel author:adamguk, m karel * " +
                                "author:/adamguk, m[^ ]+ karel/ author:/adamguk, m[^ ]+ karel .*/ " +
                                "author:adamguk, m k author:adamguk, m k * " +
                                "author:/adamguk, m[^ ]+ k/ author:/adamguk, m[^ ]+ k .*/ " +
                                "author:adamguk, m author:adamguk," ,
                                "//*[@numFound='6']"
        
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

    expected = "author:adamčuk, a b author:adamčuk, a b* " +
    		        "author:/adamčuk, a[^ ]+ b/ author:/adamčuk, a[^ ]+ b .*/ " +
    		        "author:adamčuk, a " +
    		        "author:adamčuk, " +
    		        "author:adamchuk, a b author:adamchuk, a b* " +
    		        "author:/adamchuk, a[^ ]+ b/ author:/adamchuk, a[^ ]+ b .*/ " +
    		        "author:adamchuk, a " +
    		        "author:adamchuk, " +
    		        "author:adamcuk, a b author:adamcuk, a b* " +
    		        "author:/adamcuk, a[^ ]+ b/ author:/adamcuk, a[^ ]+ b .*/ " +
    		        "author:adamcuk, a " +
    		        "author:adamcuk,"
                ;
    
    
    testAuthorQuery(
         // 1  Adamčuk,
         // 20  Adamcuk,
         // 40  Adamchuk,
        "\"adamčuk, a b\"", expected ,
                            "//*[@numFound='3']",
                            
        "\"adamcuk, a b\"", expected ,
                            "//*[@numFound='3']",
        "\"adamchuk, a b\"", expected ,
                            "//*[@numFound='3']",
        "\"adamczuk, a b\"", expected ,
                             "//*[@numFound='3']",
                             
        "\"adamšuk, m k\"", "author:adamšuk, m k author:adamšuk, m k* " +
        		                "author:/adamšuk, m[^ ]+ k/ author:/adamšuk, m[^ ]+ k .*/ " +
        		                "author:adamšuk, m " +
        		                "author:adamšuk, " +
        		                "author:adamshuk, m k author:adamshuk, m k* " +
        		                "author:/adamshuk, m[^ ]+ k/ author:/adamshuk, m[^ ]+ k .*/ " +
        		                "author:adamshuk, m " +
        		                "author:adamshuk, " +
        		                "author:adamsuk, m k author:adamsuk, m k* " +
        		                "author:/adamsuk, m[^ ]+ k/ author:/adamsuk, m[^ ]+ k .*/ " +
        		                "author:adamsuk, m " +
        		                "author:adamsuk,",
                            "//*[@numFound='5']"
            		             // 87  Adamshuk, M K
            		             // 80  Adamshuk,
            		             // 81  Adamshuk, M.
            		             // 85  Adamshuk, M Karel
            		             // 86  Adamshuk, Molja K

        		                ,
        "\"adamguk, m k\"", "author:adamguk, m k author:adamguk, m k* " +
        		                "author:/adamguk, m[^ ]+ k/ author:/adamguk, m[^ ]+ k .*/ " +
        		                "author:adamguk, m " +
        		                "author:adamguk," ,
                            "//*[@numFound='5']"
            		             // 67  Adamguk, M K
            		             // 60  Adamguk,
            		             // 61  Adamguk, M.
            		             // 65  Adamguk, M Karel
            		             // 66  Adamguk, Molja K
        
    );
    
    
    /**
     * <surname>, <2>
     * 
     * No expansion, because of the gap. Only transliteration
     * 
     */
    
    
    testAuthorQuery(
            "\"adamčuk, k\"", "author:adamčuk, k author:adamčuk, k* author:adamčuk, " +
                               "author:adamchuk, k author:adamchuk, k* author:adamchuk, " +
                               "author:adamcuk, k author:adamcuk, k* author:adamcuk,", 
                               "//*[@numFound='12']"
                            // 1  Adamčuk,
                            // 20  Adamcuk,
                            // 40  Adamchuk,
                            // 9  Adamčuk, Karel Molja
                            // 10  Adamčuk, Karel M
                            // 11  Adamčuk, K Molja
                            // 28  Adamcuk, Karel Molja
                            // 29  Adamcuk, Karel M
                            // 30  Adamcuk, K Molja
                            // 48  Adamchuk, Karel Molja
                            // 49  Adamchuk, Karel M
                            // 50  Adamchuk, K Molja
                               ,
            "\"adamcuk, k\"", "author:adamcuk, k author:adamcuk, k* author:adamcuk,", 
                               "//*[@numFound='4']"
                           // 20  Adamcuk,
                           // 28  Adamcuk, Karel Molja
                           // 29  Adamcuk, Karel M
                           // 30  Adamcuk, K Molja
                              ,
            "\"adamchuk, k\"", "author:adamchuk, k author:adamchuk, k* author:adamchuk,", 
                                "//*[@numFound='4']"
                           // 40  Adamchuk,
                           // 48  Adamchuk, Karel Molja
                           // 49  Adamchuk, Karel M
                           // 50  Adamchuk, K Molja
                             ,
            "\"adamczuk, k\"", "author:adamczuk, k author:adamczuk, k* author:adamczuk,", 
                                "//*[@numFound='0']"
                              ,
            "\"adamšuk, k\"", "author:adamšuk, k author:adamšuk, k* author:adamšuk, " +
                               "author:adamsuk, k author:adamsuk, k* author:adamsuk, " +
                               "author:adamshuk, k author:adamshuk, k* author:adamshuk,", 
                               "//*[@numFound='4']"
                               // 80  Adamshuk,
                               // 88  Adamshuk, Karel Molja
                               // 89  Adamshuk, Karel M
                               // 90  Adamshuk, K Molja
                               ,
            "\"adamguk, k\"", "author:adamguk, k author:adamguk, k* author:adamguk,", 
                               "//*[@numFound='4']"
                               // 60  Adamguk,
                               // 68  Adamguk, Karel Molja
                               // 69  Adamguk, Karel M
                               // 70  Adamguk, K Molja
    );
    
    
    /**
     * <surname>, <2name>
     * 
     * No expansion, because of the gap. Only transliteration
     * 
     */
    
    
    testAuthorQuery(
            "\"adamčuk, karel\"", "author:adamčuk, karel author:adamčuk, karel * " +
            		                  "author:adamčuk, k author:adamčuk, k * author:adamčuk, " +
            		                  "author:adamcuk, karel author:adamcuk, karel * " +
            		                  "author:adamcuk, k author:adamcuk, k * author:adamcuk, " +
            		                  "author:adamchuk, karel author:adamchuk, karel * " +
            		                  "author:adamchuk, k author:adamchuk, k * author:adamchuk,", 
                               "//*[@numFound='12']"
            		               // 1  Adamčuk,
            		               // 20  Adamcuk,
            		               // 40  Adamchuk,
            		               // 9  Adamčuk, Karel Molja
            		               // 10  Adamčuk, Karel M
            		               // 11  Adamčuk, K Molja
            		               // 28  Adamcuk, Karel Molja
            		               // 29  Adamcuk, Karel M
            		               // 30  Adamcuk, K Molja
            		               // 48  Adamchuk, Karel Molja
            		               // 49  Adamchuk, Karel M
            		               // 50  Adamchuk, K Molja
            		                  ,
            "\"adamcuk, karel\"", "author:adamcuk, karel author:adamcuk, karel * " +
                                  "author:adamcuk, k author:adamcuk, k * author:adamcuk,",
                               "//*[@numFound='']",
            "\"adamchuk, karel\"", "author:adamchuk, karel author:adamchuk, karel * " +
                                   "author:adamchuk, k author:adamchuk, k * author:adamchuk,", 
                                "//*[@numFound='']",
            "\"adamczuk, karel\"", "author:adamczuk, karel author:adamczuk, karel * " +
                                   "author:adamczuk, k author:adamczuk, k * author:adamczuk,", 
                                "//*[@numFound='']",
            "\"adamšuk, karel\"", "author:adamšuk, karel author:adamšuk, karel * " +
            		                  "author:adamšuk, k author:adamšuk, k * author:adamšuk, " +
            		                  "author:adamshuk, karel author:adamshuk, karel * " +
            		                  "author:adamshuk, k author:adamshuk, k * author:adamshuk, " +
            		                  "author:adamsuk, karel author:adamsuk, karel * " +
            		                  "author:adamsuk, k author:adamsuk, k * author:adamsuk,", 
                               "//*[@numFound='']",
            "\"adamguk, karel\"", "author:adamguk, karel author:adamguk, karel * " +
                                  "author:adamguk, k author:adamguk, k * author:adamguk,", 
                               "//*[@numFound='']"
    );
    
    
    /**
     * <surname>, <2name> <1>
     * 
     * The order is not correct, therefore no expansion. Only transliteration
     * 
     */
    
    
    testAuthorQuery(
            "\"adamčuk, karel m\"", "author:adamčuk, karel m author:adamčuk, karel m* " +
                                		"author:adamčuk, k m author:adamčuk, k m* author:adamčuk, karel " +
                                		"author:adamčuk, k author:adamčuk, author:adamchuk, karel m " +
                                		"author:adamchuk, karel m* author:adamchuk, k m " +
                                		"author:adamchuk, k m* author:adamchuk, karel author:adamchuk, k " +
                                		"author:adamchuk, author:adamcuk, karel m author:adamcuk, karel m* " +
                                		"author:adamcuk, k m author:adamcuk, k m* author:adamcuk, karel " +
                                		"author:adamcuk, k author:adamcuk,", 
                               "//*[@numFound='']",
            "\"adamcuk, karel m\"", "author:adamcuk, karel m author:adamcuk, karel m* author:adamcuk, k m " +
            		                    "author:adamcuk, k m* author:adamcuk, karel author:adamcuk, k author:adamcuk,",
                               "//*[@numFound='']",
            "\"adamchuk, karel m\"", 
                                    "author:adamchuk, karel m author:adamchuk, karel m* author:adamchuk, k m " +
                                    "author:adamchuk, k m* author:adamchuk, karel author:adamchuk, k author:adamchuk,", 
                                "//*[@numFound='']",
            "\"adamczuk, karel m\"", 
                                    "author:adamczuk, karel m author:adamczuk, karel m* author:adamczuk, k m " +
                                    "author:adamczuk, k m* author:adamczuk, karel author:adamczuk, k author:adamczuk,", 
                                "//*[@numFound='']",
            "\"adamšuk, karel m\"", "author:adamšuk, karel m author:adamšuk, karel m* author:adamšuk, k m " +
            		                    "author:adamšuk, k m* author:adamšuk, karel author:adamšuk, k author:adamšuk, " +
            		                    "author:adamsuk, karel m author:adamsuk, karel m* author:adamsuk, k m " +
            		                    "author:adamsuk, k m* author:adamsuk, karel author:adamsuk, k author:adamsuk, " +
            		                    "author:adamshuk, karel m author:adamshuk, karel m* author:adamshuk, k m " +
            		                    "author:adamshuk, k m* author:adamshuk, karel author:adamshuk, k " +
            		                    "author:adamshuk,", 
                               "//*[@numFound='']",
            "\"adamguk, karel m\"", "author:adamguk, karel m author:adamguk, karel m* author:adamguk, k m " +
            		                    "author:adamguk, k m* author:adamguk, karel author:adamguk, k author:adamguk,", 
                               "//*[@numFound='']"
    );
    
    
    /**
     * <surname>, <2name> <1name>
     * 
     * The order is not correct. Only transliteration
     * 
     */
    
    
    testAuthorQuery(
            "\"adamčuk, karel molja\"", "author:adamčuk, karel molja author:adamčuk, karel molja * " +
            		                        "author:adamčuk, k molja author:adamčuk, k molja * author:adamčuk, karel m " +
            		                        "author:adamčuk, karel m * author:adamčuk, k m author:adamčuk, k m * " +
            		                        "author:adamčuk, karel author:adamčuk, k author:adamčuk, " +
            		                        "author:adamcuk, karel molja author:adamcuk, karel molja * " +
            		                        "author:adamcuk, k molja author:adamcuk, k molja * author:adamcuk, karel m " +
            		                        "author:adamcuk, karel m * author:adamcuk, k m author:adamcuk, k m * " +
            		                        "author:adamcuk, karel author:adamcuk, k author:adamcuk, " +
            		                        "author:adamchuk, karel molja author:adamchuk, karel molja * " +
            		                        "author:adamchuk, k molja author:adamchuk, k molja * " +
            		                        "author:adamchuk, karel m author:adamchuk, karel m * " +
            		                        "author:adamchuk, k m author:adamchuk, k m * " +
            		                        "author:adamchuk, karel author:adamchuk, k author:adamchuk,", 
                               "//*[@numFound='']",
            "\"adamcuk, karel molja\"", "author:adamcuk, karel molja author:adamcuk, karel molja * " +
            		                        "author:adamcuk, k molja author:adamcuk, k molja * " +
            		                        "author:adamcuk, karel m author:adamcuk, karel m * " +
            		                        "author:adamcuk, k m author:adamcuk, k m * author:adamcuk, karel " +
            		                        "author:adamcuk, k author:adamcuk,",
                               "//*[@numFound='']",
            "\"adamchuk, karel molja\"", "author:adamchuk, karel molja author:adamchuk, karel molja * " +
            		                         "author:adamchuk, k molja author:adamchuk, k molja * " +
            		                         "author:adamchuk, karel m author:adamchuk, karel m * " +
            		                         "author:adamchuk, k m author:adamchuk, k m * author:adamchuk, karel " +
            		                         "author:adamchuk, k author:adamchuk,", 
                                "//*[@numFound='']",
            "\"adamczuk, karel molja\"", "author:adamczuk, karel molja author:adamczuk, karel molja * " +
            		                         "author:adamczuk, k molja author:adamczuk, k molja * " +
            		                         "author:adamczuk, karel m author:adamczuk, karel m * " +
            		                         "author:adamczuk, k m author:adamczuk, k m * " +
            		                         "author:adamczuk, karel author:adamczuk, k author:adamczuk,", 
                                "//*[@numFound='']",
            "\"adamšuk, karel molja\"", "author:adamšuk, karel molja author:adamšuk, karel molja * " +
            		                        "author:adamšuk, k molja author:adamšuk, k molja * " +
            		                        "author:adamšuk, karel m author:adamšuk, karel m * " +
            		                        "author:adamšuk, k m author:adamšuk, k m * " +
            		                        "author:adamšuk, karel author:adamšuk, k author:adamšuk, " +
            		                        "author:adamsuk, karel molja author:adamsuk, karel molja * " +
            		                        "author:adamsuk, k molja author:adamsuk, k molja * " +
            		                        "author:adamsuk, karel m author:adamsuk, karel m * " +
            		                        "author:adamsuk, k m author:adamsuk, k m * author:adamsuk, karel " +
            		                        "author:adamsuk, k author:adamsuk, author:adamshuk, karel molja " +
            		                        "author:adamshuk, karel molja * author:adamshuk, k molja " +
            		                        "author:adamshuk, k molja * author:adamshuk, karel m " +
            		                        "author:adamshuk, karel m * author:adamshuk, k m " +
            		                        "author:adamshuk, k m * author:adamshuk, karel " +
            		                        "author:adamshuk, k author:adamshuk,", 
                               "//*[@numFound='']",
            "\"adamguk, karel molja\"", "author:adamguk, karel molja author:adamguk, karel molja * " +
            		                        "author:adamguk, k molja author:adamguk, k molja * " +
            		                        "author:adamguk, karel m author:adamguk, karel m * " +
            		                        "author:adamguk, k m author:adamguk, k m * " +
            		                        "author:adamguk, karel author:adamguk, k " +
            		                        "author:adamguk,", 
                               "//*[@numFound='']"
    );
    
    
    /**
     * <surname>, <2> <1>
     * 
     * The order is not correct, therefore no expansion. Only transliteration
     * 
     */
    
    
    testAuthorQuery(
            "\"adamčuk, k m\"", "author:adamčuk, k m author:adamčuk, k m* " +
            		                "author:/adamčuk, k[^ ]+ m/ author:/adamčuk, k[^ ]+ m .*/ " +
            		                "author:adamčuk, k author:adamčuk, author:adamchuk, k m " +
            		                "author:adamchuk, k m* author:/adamchuk, k[^ ]+ m/ " +
            		                "author:/adamchuk, k[^ ]+ m .*/ author:adamchuk, k " +
            		                "author:adamchuk, author:adamcuk, k m author:adamcuk, k m* " +
            		                "author:/adamcuk, k[^ ]+ m/ author:/adamcuk, k[^ ]+ m .*/ " +
            		                "author:adamcuk, k author:adamcuk,", 
                               "//*[@numFound='']",
            "\"adamcuk, k m\"", "author:adamcuk, k m author:adamcuk, k m* " +
            		                "author:/adamcuk, k[^ ]+ m/ author:/adamcuk, k[^ ]+ m .*/ " +
                                "author:adamcuk, k author:adamcuk,", 
                               "//*[@numFound='']",
            "\"adamchuk, k m\"", "author:adamchuk, k m author:adamchuk, k m* " +
            		                 "author:/adamchuk, k[^ ]+ m/ author:/adamchuk, k[^ ]+ m .*/ " +
                                 "author:adamchuk, k author:adamchuk,", 
                                "//*[@numFound='']",
            "\"adamczuk, k m\"", "author:adamczuk, k m author:adamczuk, k m* " +
            		                 "author:/adamczuk, k[^ ]+ m/ author:/adamczuk, k[^ ]+ m .*/ " +
                                 "author:adamczuk, k author:adamczuk,", 
                                 "//*[@numFound='']",
            "\"adamšuk, k m\"", "author:adamšuk, k m author:adamšuk, k m* " +
            		                "author:/adamšuk, k[^ ]+ m/ author:/adamšuk, k[^ ]+ m .*/ " +
            		                "author:adamšuk, k author:adamšuk, " +
            		                "author:adamshuk, k m author:adamshuk, k m* " +
            		                "author:/adamshuk, k[^ ]+ m/ author:/adamshuk, k[^ ]+ m .*/ " +
            		                "author:adamshuk, k author:adamshuk, " +
            		                "author:adamsuk, k m author:adamsuk, k m* " +
            		                "author:/adamsuk, k[^ ]+ m/ author:/adamsuk, k[^ ]+ m .*/ " +
            		                "author:adamsuk, k author:adamsuk,", 
                               "//*[@numFound='']",
            "\"adamguk, k m\"", "author:adamguk, k m author:adamguk, k m* " +
                                "author:/adamguk, k[^ ]+ m/ author:/adamguk, k[^ ]+ m .*/ " +
                                "author:adamguk, k author:adamguk,", 
                               "//*[@numFound='']"
    );
    
    
    /**
     * <surname>, <2> <1name>
     * 
     * The order is not correct, therefore no expansion. Only transliteration
     * 
     */
    
    
    testAuthorQuery(
            "\"adamčuk, k molja\"", "author:adamčuk, k molja author:adamčuk, k molja * " +
                                		"author:/adamčuk, k[^ ]+ molja/ author:/adamčuk, k[^ ]+ molja .*/ " +
                                		"author:adamčuk, k m author:adamčuk, k m * " +
                                		"author:/adamčuk, k[^ ]+ m/ author:/adamčuk, k[^ ]+ m .*/ " +
                                		"author:adamčuk, k author:adamčuk, " +
                                		"author:adamcuk, k molja author:adamcuk, k molja * " +
                                		"author:/adamcuk, k[^ ]+ molja/ author:/adamcuk, k[^ ]+ molja .*/ " +
                                		"author:adamcuk, k m author:adamcuk, k m * " +
                                		"author:/adamcuk, k[^ ]+ m/ author:/adamcuk, k[^ ]+ m .*/ " +
                                		"author:adamcuk, k author:adamcuk, " +
                                		"author:adamchuk, k molja author:adamchuk, k molja * " +
                                		"author:/adamchuk, k[^ ]+ molja/ author:/adamchuk, k[^ ]+ molja .*/ " +
                                		"author:adamchuk, k m author:adamchuk, k m * " +
                                		"author:/adamchuk, k[^ ]+ m/ author:/adamchuk, k[^ ]+ m .*/ " +
                                		"author:adamchuk, k author:adamchuk,", 
                               "//*[@numFound='']",
            "\"adamcuk, k molja\"", 
                                    "author:adamcuk, k molja author:adamcuk, k molja * " +
                                    "author:/adamcuk, k[^ ]+ molja/ author:/adamcuk, k[^ ]+ molja .*/ " +
                                    "author:adamcuk, k m author:adamcuk, k m * " +
                                    "author:/adamcuk, k[^ ]+ m/ author:/adamcuk, k[^ ]+ m .*/ " +
                                    "author:adamcuk, k author:adamcuk,", 
                               "//*[@numFound='']",
            "\"adamchuk, k molja\"", "author:adamchuk, k molja author:adamchuk, k molja * " +
                                    "author:/adamchuk, k[^ ]+ molja/ author:/adamchuk, k[^ ]+ molja .*/ " +
                                    "author:adamchuk, k m author:adamchuk, k m * " +
                                    "author:/adamchuk, k[^ ]+ m/ author:/adamchuk, k[^ ]+ m .*/ " +
                                    "author:adamchuk, k author:adamchuk,", 
                                "//*[@numFound='']",
            "\"adamczuk, k molja\"", "author:adamczuk, k molja author:adamczuk, k molja * " +
                                    "author:/adamczuk, k[^ ]+ molja/ author:/adamczuk, k[^ ]+ molja .*/ " +
                                    "author:adamczuk, k m author:adamczuk, k m * " +
                                    "author:/adamczuk, k[^ ]+ m/ author:/adamczuk, k[^ ]+ m .*/ " +
                                    "author:adamczuk, k author:adamczuk,", 
                                 "//*[@numFound='']",
            "\"adamšuk, k molja\"", "author:adamšuk, k molja author:adamšuk, k molja * " +
                                		"author:/adamšuk, k[^ ]+ molja/ author:/adamšuk, k[^ ]+ molja .*/ " +
                                		"author:adamšuk, k m author:adamšuk, k m * author:/adamšuk, k[^ ]+ m/ " +
                                		"author:/adamšuk, k[^ ]+ m .*/ author:adamšuk, k author:adamšuk, " +
                                		"author:adamsuk, k molja author:adamsuk, k molja * " +
                                		"author:/adamsuk, k[^ ]+ molja/ author:/adamsuk, k[^ ]+ molja .*/ " +
                                		"author:adamsuk, k m author:adamsuk, k m * author:/adamsuk, k[^ ]+ m/ " +
                                		"author:/adamsuk, k[^ ]+ m .*/ author:adamsuk, k author:adamsuk, " +
                                		"author:adamshuk, k molja author:adamshuk, k molja * " +
                                		"author:/adamshuk, k[^ ]+ molja/ author:/adamshuk, k[^ ]+ molja .*/ " +
                                		"author:adamshuk, k m author:adamshuk, k m * author:/adamshuk, k[^ ]+ m/ " +
                                		"author:/adamshuk, k[^ ]+ m .*/ author:adamshuk, k author:adamshuk,", 
                               "//*[@numFound='']",
            "\"adamguk, k molja\"", "author:adamguk, k molja author:adamguk, k molja * " +
                                		"author:/adamguk, k[^ ]+ molja/ author:/adamguk, k[^ ]+ molja .*/ " +
                                		"author:adamguk, k m author:adamguk, k m * " +
                                		"author:/adamguk, k[^ ]+ m/ author:/adamguk, k[^ ]+ m .*/ " +
                                		"author:adamguk, k author:adamguk,", 
                               "//*[@numFound='']"
    );
    
    /**
     * <surname>, <1*>
     * <surname>, <1n*>
     * 
     * No expansion should happen if the <part*> has more than 2 characters, otherwise
     * it should work as if <surname>, <1> was specified
     * 
     */
    
    expected = "author:adamšuk, m author:adamšuk, m* author:adamšuk, " +
              "author:adamsuk, m author:adamsuk, m* author:adamsuk, " +
              "author:adamshuk, m author:adamshuk, m* author:adamshuk, " +
              "author:adamguk, m author:adamguk, m* author:adamguk, " +
              "author:adamčuk, m author:adamčuk, m* author:adamčuk, " +
              "author:adamchuk, m author:adamchuk, m* author:adamchuk, " +
              "author:adamcuk, m author:adamcuk, m* author:adamcuk,";
    
    //setDebug(true);
    testAuthorQuery(
            "\"adamčuk, m*\"", expected, "//*[@numFound='']",
            "\"adamcuk, m*\"", expected, "//*[@numFound='']",
            "\"adamchuk, m*\"", expected, "//*[@numFound='']",
            "\"adamczuk, m*\"", expected, "//*[@numFound='']",
            "\"adamšuk, m*\"", expected, "//*[@numFound='']",
            "\"adamguk, m*\"", expected, "//*[@numFound='']",
            
            "\"adamčuk, mo*\"", "author:adamčuk, mo*", "//*[@numFound='']",
            "\"adamcuk, mo*\"", "author:adamcuk, mo*", "//*[@numFound='']",
            "\"adamchuk, mo*\"", "author:adamchuk, mo*", "//*[@numFound='']",
            "\"adamczuk, mo*\"", "author:adamczuk, mo*", "//*[@numFound='']",
            "\"adamšuk, mo*\"", "author:adamšuk, mo*", "//*[@numFound='']",
            "\"adamguk, mo*\"", "author:adamguk, mo*", "//*[@numFound='']"
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
            "\"adamčuk, k*\"", "author:adamčuk, k author:adamčuk, k* author:adamčuk, " +
            		               "author:adamchuk, k author:adamchuk, k* author:adamchuk, " +
            		               "author:adamcuk, k author:adamcuk, k* author:adamcuk,", 
            		               "//*[@numFound='']",
            "\"adamcuk, k*\"", "author:adamcuk, k author:adamcuk, k* author:adamcuk,", 
                               "//*[@numFound='']",
            "\"adamchuk, k*\"", "author:adamchuk, k author:adamchuk, k* author:adamchuk,", 
                                "//*[@numFound='']",
            "\"adamczuk, k*\"", "author:adamczuk, k author:adamczuk, k* author:adamczuk,", 
                                "//*[@numFound='']",
            "\"adamšuk, k*\"", "author:adamšuk, k author:adamšuk, k* author:adamšuk, " +
                               "author:adamsuk, k author:adamsuk, k* author:adamsuk, " +
                               "author:adamshuk, k author:adamshuk, k* author:adamshuk,", 
                               "//*[@numFound='']",
            "\"adamguk, k*\"", "author:adamguk, k author:adamguk, k* author:adamguk,", 
                               "//*[@numFound='']",
            
            "\"adamčuk, ko*\"", "author:adamčuk, ko*", "//*[@numFound='']",
            "\"adamcuk, ko*\"", "author:adamcuk, ko*", "//*[@numFound='']",
            "\"adamchuk, ko*\"", "author:adamchuk, ko*", "//*[@numFound='']",
            "\"adamczuk, ko*\"", "author:adamczuk, ko*", "//*[@numFound='']",
            "\"adamšuk, ko*\"", "author:adamšuk, ko*", "//*[@numFound='']",
            "\"adamguk, ko*\"", "author:adamguk, ko*", "//*[@numFound='']"
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
        //must NOT have "jones, c*", must have "jones, christine"
        "\"forman, c\"", "author:forman, c author:forman, christine author:forman, c* author:forman," +
        		             "author:jones, christine author:jones, c",
                         "//*[@numFound='']",
        //must NOT have "forman, c*", must have "forman, christine"
        "\"jones, c\"", "author:jones, c author:jones, christine author:jones, c* author:jones," +
        		            "author:forman, christine author:forman, c",
                         "//*[@numFound='']",
        "\"jones, christine\"", 
                        "author:jones, christine author:jones, christine * author:jones, c " +
                        "author:jones, c * author:jones, author:forman, christine " +
                        "author:forman, christine * author:forman, c author:forman, c * " +
                        "author:forman,", 
                        "//*[@numFound='']",
        "\"forman, christine\"", "author:jones, christine author:jones, christine * author:jones, c " +
        		            "author:jones, c * author:jones, author:forman, christine author:forman, christine * " +
        		            "author:forman, c author:forman, c * author:forman,", 
        		            "//*[@numFound='']"
    );
    
    

    /**
     * THE OLD STYLE, SO THAT I CAN COMPARE
    assertQueryEquals(req("qt", "aqp", "q", "author:\"Adamčuk, m\""), 
        //"author:adamčuk, m author:adamcuk, m author:adamchuk, m author:adamčuk, author:adamčuk, m* author:adamchuk, marel author:adamčuk, marel author:adamcuk, molja author:adamcuk, marel author:adamčuk, molja author:adamchuk, molja author:adamchuk, m* author:adamchuk, author:adamcuk, author:adamcuk, m*",
        "author:adamčuk, m author:adamcuk, m author:adamchuk, m author:adamčuk, author:adamčuk, m* author:adamchuk, m* author:adamchuk, author:adamcuk, author:adamcuk, m*",
        BooleanQuery.class);

    assertQueryEquals(req("qt", "aqp", "q", "author:\"ADAMČuk, m\""), 
        //"author:adamčuk, m author:adamcuk, m author:adamchuk, m author:adamčuk, author:adamčuk, m* author:adamchuk, marel author:adamčuk, marel author:adamcuk, molja author:adamcuk, marel author:adamčuk, molja author:adamchuk, molja author:adamchuk, m* author:adamchuk, author:adamcuk, author:adamcuk, m*",
        "author:adamčuk, m author:adamcuk, m author:adamchuk, m author:adamčuk, author:adamčuk, m* author:adamchuk, m* author:adamchuk, author:adamcuk, author:adamcuk, m*",
        BooleanQuery.class);

    assertQueryEquals(req("qt", "aqp", "q", "author:\"adamchuk, m\""), 
        //"author:adamchuk, m author:adamcuk, m author:adamčuk, m author:adamchuk, m* author:adamchuk, marel author:adamčuk, marel author:adamcuk, molja author:adamcuk, marel author:adamchuk, molja author:adamčuk, molja author:adamchuk,",
        "author:adamchuk, m author:adamcuk, m author:adamčuk, m author:adamchuk, m* author:adamchuk,",
        BooleanQuery.class);
     **/


    assertQueryEquals(req("qt", "aqp", "q", "author:\"Muller, William\""),
        // this was the old-style result, note "muller, w*"
        //"author:muller, w author:muller, w* author:muller, william author:müller, william author:mueller, william author:muller,",
        "author:müller, william author:müller, william * " +
        "author:müller, w author:müller, w * " +
        "author:müller, " +
        "author:muller, william author:muller, william * " +
        "author:muller, w author:muller, w * " +
        "author:muller, " +
        "author:mueller, william author:mueller, william * " +
        "author:mueller, w author:mueller, w * " +
        "author:mueller, " +
        "author:müller, bill author:müller, bill * " +
        "author:müller, b author:müller, b * " +
        "author:mueller, bill author:mueller, bill * " +
        "author:mueller, b author:mueller, b * " +
        "author:muller, bill author:muller, bill * " +
        "author:muller, b author:muller, b *",
        BooleanQuery.class);

    
    /*
     * 
    TODO: 
    assertQ(req("q", "author:\"ADAMČuk, m\""), "//*[@numFound='3']");
    assertQ(req("q", "author:\"adamčuk, m\""), "//*[@numFound='3']");
    assertQ(req("q", "author:\"adamchuk, m\""), "//*[@numFound='3']");
    assertQ(req("q", "author:\"adamcuk, m\""), "//*[@numFound='3']");
    assertQ(req("q", "author:\"adamčuk, molja\""), "//*[@numFound='2']"); // should not match record with Adamčuk, marel
    assertQ(req("q", "author:\"müller, w\""), "//*[@numFound='2']");
    assertQ(req("q", "author:\"mueller, w\""), "//*[@numFound='2']");
    assertQ(req("q", "author:\"muller, w\""), "//*[@numFound='2']");
    
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
    
    // not working because: adamczuk, m is  not in synonym file
    //assertQ(req("q", "author:\"adamczuk, m\""), "//*[@numFound='3']");

  }


  private void testAuthorQuery(String...vals) throws Exception {
    assert vals.length%3==0;
    for (int i=0;i<vals.length;i=i+3) {
      System.out.println("Running test for " + String.format("author:%s", vals[i]));
      String response = h.query(req("fl", "id,author", "rows", "100", "defType", "aqp", "q", String.format("author:%s", vals[i])));
      
      StringBuffer out = new StringBuffer();
      Matcher m = Pattern.compile("numFound=\\\"(\\d+)").matcher(response);
      Matcher m2 = Pattern.compile("<doc><str name=\\\"id\\\">(\\d+)</str><arr name=\\\"author\\\"><str>([^<]*)</str></arr></doc>").matcher(response);
      m.find();
      out.append("numFound " + m.group(1) + "\n");
      while (m2.find()) {
        out.append("// " + m2.group(1) + "\t" + m2.group(2) + "\n");
      }
      System.out.println(out.toString());
      
      assertQueryEquals(req("qt", "aqp", "q", String.format("author:%s", vals[i])),
          vals[i+1],
          null);
      assertQ(req("fl", "id,author", "rows", "100", "q", String.format("author:%s", vals[i])), vals[i+2].split(";"));
    }

  }

  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeAuthorParsing.class);
  }

  /** Validates a query matches some XPath test expressions and closes the query */
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

  public Query assertQueryEquals(SolrQueryRequest req, String expected, Class<?> clazz)
  throws Exception {

    QParser qParser = getParser(req);
    String query = req.getParams().get(CommonParams.Q);
    Query q = qParser.parse();

    String actual = q.toString("field");
    
    String[] ex = expected.split("\\s*[a-z]+:");
    Arrays.sort(ex);
    String[] ac = actual.split("\\s*[a-z]+:");
    Arrays.sort(ac);
    StringBuffer exs = new StringBuffer();
    for (String s: ex) {
      exs.append(s.trim());
    }
    StringBuffer acs = new StringBuffer();
    for (String s: ac) {
      acs.append(s.trim());
    }
    
    if (!acs.toString().equals(exs.toString())) {
      //assertArrayEquals(ac, ex);
      tp.debugFail(query, expected, actual);
    }

    if (clazz != null) {
      if (!q.getClass().isAssignableFrom(clazz)) {
        tp.debugFail("Query is not: " + clazz + " but: " + q.getClass(), expected, "-->" + q.toString());
      }
    }

    return q;
  }
}
