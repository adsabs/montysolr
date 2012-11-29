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

import javax.xml.xpath.XPathExpressionException;

import org.adsabs.solr.AdsConfig.F;

/**
 * 
 * Tests for all the author_ types defined in schema.xml
 * See:
 * http://labs.adsabs.harvard.edu/trac/ads-invenio/ticket/131
 * http://labs.adsabs.harvard.edu/trac/ads-invenio/ticket/156
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
          "ADAMŠUK, K;ADAMGUK, K;ADAMČUK, K",  // hand-made additions
          "MÜLLER, A WILLIAM;MÜLLER, A BILL",
          "MÜLLER, WILLIAM;MÜLLER, BILL",
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
          "ADAMCHUK, KOLJA K=> ADAMČUK, KOLJA K",
          "ADAMCUK, KOLJA K=> ADAMČUK, KOLJA K",
          "ADAMCZUK, KOLJA K=> ADAMČUK, KOLJA K",
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


    assertU(adoc(F.ID, "1", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, K"));
    assertU(adoc(F.ID, "2", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Karel"));
    assertU(adoc(F.ID, "3", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Adamčuk, Kolja"));

    assertU(adoc(F.ID, "4", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Müller, William"));
    assertU(adoc(F.ID, "5", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Mueller, William"));
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
       <surname>, <2name> <3>
       <surname>, <2name> <3name>
       <surname>, <2> <3name>
       <surname>, <2> <3>
       
       <surname>, <1n*>
       <surname>, <1*>
       <surname>, <2n*>
       <surname>, <2*>

		 - transliteration: adamčuk, k --> adamchuk, k;adamcuk, k
     - synonym expansion for: ADAMŠUK, K;ADAMGUK, K;ADAMČUK, K

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
        "adamčuk", expected, "//*[@numFound='']",
        "adamcuk", expected, "//*[@numFound='']",
        "adamchuk", expected, "//*[@numFound='']",
        "adamczuk", expected, "//*[@numFound='']",
        "adamšuk", "author:adamšuk, author:adamšuk,* " +
                   "author:adamshuk, author:adamshuk,* " +
                   "author:adamsuk, author:adamsuk,*", "//*[@numFound='']",
        "adamguk", "author:adamguk, author:adamguk,*", "//*[@numFound='']"
    );

    //setDebug(true);
    /**
     * <surname>,
     * 
     * upgraded && transliterated 
     * synonym adamšuk IS NOT FOUND because there is no  entry for "adam(č|c|ch)uk" the syn list
     */
    testAuthorQuery(
        "\"adamčuk,\"", expected, "//*[@numFound='']",
        "\"adamcuk,\"", expected, "//*[@numFound='']",
        "\"adamchuk,\"", expected, "//*[@numFound='']",
        "\"adamczuk,\"", expected, "//*[@numFound='']",
        "\"adamšuk,\"", "author:adamšuk, author:adamšuk,* " +
                        "author:adamshuk, author:adamshuk,* " +
                        "author:adamsuk, author:adamsuk,*", "//*[@numFound='']",
        "\"adamguk,\"", "author:adamguk, author:adamguk,*", "//*[@numFound='']"
    );


    /**
     * <surname>, <1>
     * 
     *  expanded && upgraded && transliterated && expanded
     *  synonym "adamšuk, k" IS FOUND because there is entry for "adamčuk, k" the syn list, notice
     *  this works even if we type "adamchuk, k" or "adamcuk, k"
     * 
     *  question: the chain correctly finds the synonym "adamšuk, k", and this synonym is
     *  then transliterated: adamshuk, k;adamsuk, k (is this desirable?) I think yes.
     */
    expected = "author:adamšuk, k author:adamšuk, k* author:adamšuk, " +
                "author:adamsuk, k author:adamsuk, k* author:adamsuk, " +
                "author:adamshuk, k author:adamshuk, k* author:adamshuk, " +
                "author:adamguk, k author:adamguk, k* author:adamguk, " +
                "author:adamčuk, k author:adamčuk, k* author:adamčuk, " +
                "author:adamchuk, k author:adamchuk, k* author:adamchuk, " +
                "author:adamcuk, k author:adamcuk, k* author:adamcuk,";

    testAuthorQuery(
        "\"adamčuk, k\"", expected, "//*[@numFound='']",
        "\"adamcuk, k\"", expected, "//*[@numFound='']",
        "\"adamchuk, k\"", expected, "//*[@numFound='']",
        "\"adamczuk, k\"", expected, "//*[@numFound='']",
        "\"adamšuk, k\"", expected, "//*[@numFound='']",
        "\"adamguk, k\"", expected, "//*[@numFound='']",

        "\"AdAmČuk, K\"", expected, "//*[@numFound='']", // just for fun
        "\"ADAMČUK, k\"", expected, "//*[@numFound='']",
        "\"AdAmCHuk, K\"", expected, "//*[@numFound='']"
    );


    /**
     * <surname>, <1name>
     * 
     *  upgraded && transliterated && expanded
     *  synonym "adamšuk, k" IS FOUND because of the query variation for "adamčuk, k" the syn list
     */
    
    // base part, must be present in all
    expected0 = 
    		       "author:adamčuk, k author:adamčuk, k * author:adamčuk, " +
    		       "author:adamcuk, k author:adamcuk, k * author:adamcuk, " +
    		       "author:adamchuk, k author:adamchuk, k * author:adamchuk, " +
    		       "author:adamšuk, k author:adamšuk, k * author:adamšuk, " +
    		       "author:adamsuk, k author:adamsuk, k * author:adamsuk, " +
    		       "author:adamshuk, k author:adamshuk, k * author:adamshuk, " +
    		       "author:adamguk, k author:adamguk, k * author:adamguk, ";

    expected = expected0 +
               "author:adamčuk, kolja author:adamčuk, kolja * " +
               "author:adamchuk, kolja author:adamchuk, kolja * " +
               "author:adamcuk, kolja author:adamcuk, kolja *"
               ;
    
    
    testAuthorQuery(
        "\"adamčuk, kolja\"", expected, "//*[@numFound='']",
        "\"adamcuk, kolja\"", expected, "//*[@numFound='']",
        "\"adamchuk, kolja\"", expected, "//*[@numFound='']",
        "\"adamczuk, kolja\"", expected, "//*[@numFound='']",
        // "adamčuk, kolja" is not there (and cannot be, because it is not in
        // synonym map, but synonym "adamšuk, k" is found correctly)
        "\"adamšuk, kolja\"", expected0 +
                              "author:adamšuk, kolja author:adamšuk, kolja * " +
                              "author:adamshuk, kolja author:adamshuk, kolja * " +
                              "author:adamsuk, kolja author:adamsuk, kolja *", 
                              "//*[@numFound='']",
        // shorter by two variants, because "adamguk, kolja" is already ascii form
        // it doesn't generate: "author:adamshuk, kolja author:adamsuk, kolja"
        // that is correct, because "adamšuk, k" is found and transliterated
        // "adamšuk, kolja" simply isn't in any synonym list and we tehrefore cannot have it 
        "\"adamguk, kolja\"", expected0 +
                              "author:adamguk, kolja author:adamguk, kolja *",
                              "//*[@numFound='']"
    );



    /**
     * <surname>, <1name> <2>
     * 
     * upgraded && transliterated && expanded
     * synonym adamšuk IS NOT FOUND because there is no entry for "adamčuk, kolja k" nor 
     * there is any "adamčuk, k k" in the syn list
     * 
     * NOTE: if you think that "adamšuk" should be found in our model, then you are wrong
     * because "adamcuk, k k" is a different name than "adamcuk, k"
     * We are not goign to do any magic to find the surname mapping, in other words:
     * we are not going to replace defficient synonym file. Because the correct translation 
     * CAN WORK if "adamcuk, k k" and "adamcuk, k" are named as synonymous (see the example
     * case of "adamczuk, k k k")
     */
    
    expected = "author:adamčuk, kolja k author:adamčuk, kolja k* " +
    		       "author:adamčuk, k k author:adamčuk, k k* " +
    		       "author:adamčuk, kolja " + // ! author:adamčuk, kolja *
    		       "author:adamčuk, k " + // ! author:adamčuk, k*
    		       "author:adamčuk, " +
    		       "author:adamcuk, kolja k author:adamcuk, kolja k* " +
    		       "author:adamcuk, k k author:adamcuk, k k* " +
    		       "author:adamcuk, kolja " + // ! author:adamcuk, kolja * 
    		       "author:adamcuk, k " + // ! author:adamcuk, k* 
    		       "author:adamcuk, " +
    		       "author:adamchuk, kolja k author:adamchuk, kolja k* " +
    		       "author:adamchuk, k k author:adamchuk, k k* " +
    		       "author:adamchuk, kolja " + // ! author:adamchuk, kolja * 
    		       "author:adamchuk, k " + // ! author:adamchuk, k*
    		       "author:adamchuk,";
      

    testAuthorQuery(
        "\"adamčuk, kolja k\"", expected, "//*[@numFound='']",
        "\"adamcuk, kolja k\"", expected, "//*[@numFound='']",
        "\"adamchuk, kolja k\"", expected, "//*[@numFound='']",
        // this contains 4 more entries because by default, the
        // transliteration produces only adam(c|ch)uk
        "\"adamczuk, kolja k\"", expected + 
                          " author:adamczuk, k k author:adamczuk, k k* author:adamczuk, k author:adamczuk,", 
                          "//*[@numFound='']",
        "\"adamšuk, kolja k\"", 
            "author:adamšuk, kolja k author:adamšuk, kolja k* " +
            "author:adamšuk, k k author:adamšuk, k k* " +
            "author:adamšuk, kolja " +
            "author:adamšuk, k " +
            "author:adamšuk, " +
            "author:adamsuk, kolja k author:adamsuk, kolja k* " +
            "author:adamsuk, k k author:adamsuk, k k* " +
            "author:adamsuk, kolja " +
            "author:adamsuk, k " +
            "author:adamsuk, " +
            "author:adamshuk, kolja k author:adamshuk, kolja k* " +
            "author:adamshuk, k k author:adamshuk, k k* " +
            "author:adamshuk, kolja " +
            "author:adamshuk, k " +
            "author:adamshuk,", 
              "//*[@numFound='']",
        "\"adamguk, kolja k\"", 
            "author:adamguk, kolja k author:adamguk, kolja k* " +
            "author:adamguk, k k author:adamguk, k k* " +
            "author:adamguk, kolja " +
            "author:adamguk, k " +
            "author:adamguk,", 
            "//*[@numFound='']"
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
     * And through these variations, we find the upgraded form "adamčuk, kolja k"
     * 
     * This has the benefit of us finding the combination of name/initials even if 
     * we didn't encounter them during indexing. HOWEVER, to avoid false hits these
     * combinations are found only for names that have certain number of parts,
     * default >= 3 
     */

    // we expect the same results as above (the difference is in the "..., k k *")
    // plus whathever comes out of the original input transliteration/combination
    expected0 = "author:adamčuk, kolja k author:adamčuk, kolja k * " +
                "author:adamčuk, k k author:adamčuk, k k * " +
                "author:adamčuk, kolja " + 
                "author:adamčuk, k " + 
                "author:adamčuk, " +
                "author:adamcuk, kolja k author:adamcuk, kolja k * " +
                "author:adamcuk, k k author:adamcuk, k k * " +
                "author:adamcuk, kolja " +  
                "author:adamcuk, k " +  
                "author:adamcuk, " +
                "author:adamchuk, kolja k author:adamchuk, kolja k * " +
                "author:adamchuk, k k author:adamchuk, k k * " +
                "author:adamchuk, kolja " +  
                "author:adamchuk, k " + 
                "author:adamchuk,";
    
    
    testAuthorQuery(
        "\"adamčuk, kolja karel\"", expected0 + " " + 
                                    "author:adamčuk, kolja karel author:adamčuk, kolja karel * " +
                                    "author:adamčuk, k karel author:adamčuk, k karel * " +
                                    "author:adamchuk, kolja karel author:adamchuk, kolja karel * " +
                                    "author:adamchuk, k karel author:adamchuk, k karel * " +
                                    "author:adamcuk, kolja karel author:adamcuk, kolja karel * " +
                                    "author:adamcuk, k karel author:adamcuk, k karel *", 
                                    "//*[@numFound='']",
        "\"adamcuk, kolja karel\"", expected0 + " " + 
                                    "author:adamcuk, kolja karel author:adamcuk, kolja karel * " +
                                    "author:adamcuk, k karel author:adamcuk, k karel *",
                                    "//*[@numFound='']",
        "\"adamchuk, kolja karel\"", expected0 + " " + 
                                    "author:adamchuk, kolja karel author:adamchuk, kolja karel * " +
                                    "author:adamchuk, k karel author:adamchuk, k karel *",
                                    "//*[@numFound='']",
        "\"adamczuk, kolja karel\"", expected0 + " " + 
                                    "author:adamczuk, kolja karel author:adamczuk, kolja karel * " +
                                    "author:adamczuk, k karel author:adamczuk, k karel * " +
                                    "author:adamczuk, kolja k author:adamczuk, kolja k * " +
                                    "author:adamczuk, k k author:adamczuk, k k * " +
                                    "author:adamczuk, kolja author:adamczuk, k " +
                                    "author:adamczuk,",
                                    "//*[@numFound='']",
        // almost exactly the same as above, the only difference must be the space before *
        "\"adamšuk, kolja karel\"", "author:adamšuk, kolja k author:adamšuk, kolja k * " +
                                    "author:adamšuk, k k author:adamšuk, k k * " +
                                    "author:adamšuk, kolja " +
                                    "author:adamšuk, k " +
                                    "author:adamšuk, " +
                                    "author:adamsuk, kolja k author:adamsuk, kolja k * " +
                                    "author:adamsuk, k k author:adamsuk, k k * " +
                                    "author:adamsuk, kolja " +
                                    "author:adamsuk, k " +
                                    "author:adamsuk, " +
                                    "author:adamshuk, kolja k author:adamshuk, kolja k * " +
                                    "author:adamshuk, k k author:adamshuk, k k * " +
                                    "author:adamshuk, kolja " +
                                    "author:adamshuk, k " +
                                    "author:adamshuk, " + 
                                    // plus variants with karel
                                    "author:adamšuk, kolja karel author:adamšuk, kolja karel * " +
                                    "author:adamšuk, k karel author:adamšuk, k karel * " + 
                                    "author:adamshuk, kolja karel author:adamshuk, kolja karel * " +
                                    "author:adamshuk, k karel author:adamshuk, k karel * " +
                                    "author:adamsuk, kolja karel author:adamsuk, kolja karel * " +
                                    "author:adamsuk, k karel author:adamsuk, k karel *",
                                    "//*[@numFound='']",
        "\"adamguk, kolja karel\"", "author:adamguk, kolja k author:adamguk, kolja k * " +
                                    "author:adamguk, k k author:adamguk, k k * " +
                                    "author:adamguk, kolja " +
                                    "author:adamguk, k " +
                                    "author:adamguk, " + 
                                    // plus variants with karel
                                    "author:adamguk, kolja karel author:adamguk, kolja karel * " +
                                    "author:adamguk, k karel author:adamguk, k karel *",
                                    "//*[@numFound='']"
        
    );
    
    
    /*
     * TODO:
     * 
     * Also make sure we test that the expanding algorithm doesn't have unwanted consequences
     * and doesn't include too much, ie. that search for "adamčuk, kos" doesn't get
     * transformed into "adam(c|ch)uk, k"
     */
    
    //TODO: show that the translation works properly when the synonym is in the synonym list
    // ie "adamčuk, k k;adamšuk, k k"

    
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
     *    adamčuk, k karel
     *    adamčuk, kxxxx karel
     * 
     * 
     */

    expected0 = "author:adamčuk, k karel author:adamčuk, k karel * " +
                "author:adamčuk, k k author:adamčuk, k k * " +
                "author:adamčuk, k " + 
                "author:adamčuk, " +
                "author:adamcuk, k karel author:adamcuk, k karel * " +
                "author:adamcuk, k k author:adamcuk, k k * " +
                "author:adamcuk, k " +  
                "author:adamcuk, " +
                "author:adamchuk, k karel author:adamchuk, k karel * " +
                "author:adamchuk, k k author:adamchuk, k k * " +
                "author:adamchuk, k " + 
                "author:adamchuk, " +
                "author:/adamčuk,k\\w* karel/ " +
                "author:/adamčuk,k\\w* karel .*/ " +
                "author:/adamčuk,k\\w* k/ " +
                "author:/adamčuk,k\\w* k .*/ " +
                "author:/adamcuk,k\\w* karel/ " +
                "author:/adamcuk,k\\w* karel .*/ " +
                "author:/adamcuk,k\\w* k/ " +
                "author:/adamcuk,k\\w* k .*/ " +
                "author:/adamchuk,k\\w* karel/ " +
                "author:/adamchuk,k\\w* karel .*/ " +
                "author:/adamchuk,k\\w* k/ " +
                "author:/adamchuk,k\\w* k .*/"
                ;
    
    
    //setDebug(true);
    testAuthorQuery(
        "\"adamčuk, k karel\"", expected0 ,
                                    "//*[@numFound='']",
        "\"adamcuk, k karel\"", "author:adamcuk, k karel author:adamcuk, k karel * " +
                                "author:/adamcuk,k\\w* karel/ author:/adamcuk,k\\w* karel .*/ " +
                                "author:adamcuk, k k author:adamcuk, k k * " +
                                "author:/adamcuk,k\\w* k/ author:/adamcuk,k\\w* k .*/ " +
                                "author:adamcuk, k author:adamcuk," ,
                                "//*[@numFound='']",
        "\"adamchuk, k karel\"", "author:adamchuk, k karel author:adamchuk, k karel * " +
                                 "author:/adamchuk,k\\w* karel/ author:/adamchuk,k\\w* karel .*/ " +
                                 "author:adamchuk, k k author:adamchuk, k k * " +
                                 "author:/adamchuk,k\\w* k/ author:/adamchuk,k\\w* k .*/ " +
                                 "author:adamchuk, k author:adamchuk," ,
                                 "//*[@numFound='']",
        "\"adamczuk, k karel\"", "author:adamczuk, k karel author:adamczuk, k karel * " +
                                 "author:/adamczuk,k\\w* karel/ author:/adamczuk,k\\w* karel .*/ " +
                                 "author:adamczuk, k k author:adamczuk, k k * " +
                                 "author:/adamczuk,k\\w* k/ author:/adamczuk,k\\w* k .*/ " +
                                 "author:adamczuk, k author:adamczuk," ,
                                 "//*[@numFound='']",
        "\"adamšuk, k karel\"", "author:adamšuk, k karel author:adamšuk, k karel * " +
        		                    "author:/adamšuk,k\\w* karel/ author:/adamšuk,k\\w* karel .*/ " +
        		                    "author:adamšuk, k k author:adamšuk, k k * " +
        		                    "author:/adamšuk,k\\w* k/ author:/adamšuk,k\\w* k .*/ " +
        		                    "author:adamšuk, k " +
        		                    "author:adamšuk, " +
        		                    "author:adamsuk, k karel author:adamsuk, k karel * " +
        		                    "author:/adamsuk,k\\w* karel/ author:/adamsuk,k\\w* karel .*/ " +
        		                    "author:adamsuk, k k author:adamsuk, k k * " +
        		                    "author:/adamsuk,k\\w* k/ author:/adamsuk,k\\w* k .*/ " +
        		                    "author:adamsuk, k " +
        		                    "author:adamsuk, " +
        		                    "author:adamshuk, k karel author:adamshuk, k karel * " +
        		                    "author:/adamshuk,k\\w* karel/ author:/adamshuk,k\\w* karel .*/ " +
        		                    "author:adamshuk, k k author:adamshuk, k k * " +
        		                    "author:/adamshuk,k\\w* k/ author:/adamshuk,k\\w* k .*/ " +
        		                    "author:adamshuk, k " +
        		                    "author:adamshuk,",
                                "//*[@numFound='']",
        "\"adamguk, k karel\"", "author:adamguk, k karel author:adamguk, k karel * " +
                                "author:/adamguk,k\\w* karel/ author:/adamguk,k\\w* karel .*/ " +
                                "author:adamguk, k k author:adamguk, k k * " +
                                "author:/adamguk,k\\w* k/ author:/adamguk,k\\w* k .*/ " +
                                "author:adamguk, k author:adamguk," ,
                                "//*[@numFound='']"
        
    );
    
    /**
     * <surname>, <part*>
     * 
     * No expansion should happen if the <part*> has more than 2 characters, otherwise
     * it should work as if <surname>, <1> was specified
     * 
     */
    
    expected = "author:adamšuk, k author:adamšuk, k* author:adamšuk, " +
              "author:adamsuk, k author:adamsuk, k* author:adamsuk, " +
              "author:adamshuk, k author:adamshuk, k* author:adamshuk, " +
              "author:adamguk, k author:adamguk, k* author:adamguk, " +
              "author:adamčuk, k author:adamčuk, k* author:adamčuk, " +
              "author:adamchuk, k author:adamchuk, k* author:adamchuk, " +
              "author:adamcuk, k author:adamcuk, k* author:adamcuk,";
    
    //setDebug(true);
    testAuthorQuery(
            "\"adamčuk, k*\"", expected, "//*[@numFound='']",
            "\"adamcuk, k*\"", expected, "//*[@numFound='']",
            "\"adamchuk, k*\"", expected, "//*[@numFound='']",
            "\"adamczuk, k*\"", expected, "//*[@numFound='']",
            "\"adamšuk, k*\"", expected, "//*[@numFound='']",
            "\"adamguk, k*\"", expected, "//*[@numFound='']",
            
            "\"adamčuk, ko*\"", "author:adamčuk, ko*", "//*[@numFound='']",
            "\"adamcuk, ko*\"", "author:adamcuk, ko*", "//*[@numFound='']",
            "\"adamchuk, ko*\"", "author:adamchuk, ko*", "//*[@numFound='']",
            "\"adamczuk, ko*\"", "author:adamczuk, ko*", "//*[@numFound='']",
            "\"adamšuk, ko*\"", "author:adamšuk, ko*", "//*[@numFound='']",
            "\"adamguk, ko*\"", "author:adamguk, ko*", "//*[@numFound='']"
    );



    /**
     * THE OLD STYLE, SO THAT I CAN COMPARE
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
    assertQ(req("q", "author:\"ADAMČUK, K\""), "//*[@numFound='3']");
    assertQ(req("q", "author:\"adamčuk, k\""), "//*[@numFound='3']");
    assertQ(req("q", "author:\"adamchuk, k\""), "//*[@numFound='3']");
    assertQ(req("q", "author:\"adamcuk, k\""), "//*[@numFound='3']");
    assertQ(req("q", "author:\"adamčuk, kolja\""), "//*[@numFound='2']"); // should not match record with Adamčuk, Karel
    assertQ(req("q", "author:\"müller, w\""), "//*[@numFound='2']");
    assertQ(req("q", "author:\"mueller, w\""), "//*[@numFound='2']");
    assertQ(req("q", "author:\"muller, w\""), "//*[@numFound='2']");

    */
    
    // not working because: adamczuk, k is  not in synonym file
    //assertQ(req("q", "author:\"adamczuk, k\""), "//*[@numFound='3']");

  }


  private void testAuthorQuery(String...vals) throws Exception {
    assert vals.length%3==0;
    for (int i=0;i<vals.length;i=i+3) {
      System.out.println("Running test for " + String.format("author:%s", vals[i]));
      assertQueryEquals(req("qt", "aqp", "q", String.format("author:%s", vals[i])),
          vals[i+1],
          null);
      //assertQ(req("q", String.format("author:%s", vals[i])), vals[i+2].split(";"));
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
