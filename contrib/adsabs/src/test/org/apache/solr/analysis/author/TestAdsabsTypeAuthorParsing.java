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
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.SchemaField;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

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
          "ADAMŠUK, K;ADAMGUK, K;ADAMČUK, K"  // hand-made additions
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
     *   that way)
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
     *   kurtz, michael ->       kurtz, michael
     *                           kurtz, michael *
     *                           kurtz, m
     *                           kurtz,
     *   
     *   kurtz, m       ->       kurtz, m
     *                           kurtz, m* (in fact, these two can become just: kurtz, m*)
     *                           kurtz, 
     *   
     *   kurtz, mi*     ->       kurtz, mi*
     *                           kurtz,
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


		 - transliteration: adamčuk, k --> adamchuk, k;adamcuk, k
     - synonym expansion for: ADAMŠUK, K;ADAMGUK, K;ADAMČUK, K

     */

    String expected;
    
    expected = "author:adamčuk, author:adamchuk, author:adamcuk,";
    
    //<surname>
    // upgraded && transliterated 
    // synonym adamšuk IS NOT FOUND because there is no  entry for "adam(č|c|ch)uk" the syn list
    testAuthorQuery(
        "adamčuk", expected, "//*[@numFound='']",
        "adamcuk", expected, "//*[@numFound='']",
        "adamchuk", expected, "//*[@numFound='']",
        "adamczuk", expected, "//*[@numFound='']",
        "adamšuk", "author:adamšuk, author:adamshuk, author:adamsuk,", "//*[@numFound='']",
        "adamguk", "author:adamguk,", "//*[@numFound='']"
    );

    
    //<surname>,
    // upgraded && transliterated 
    // synonym adamšuk IS NOT FOUND because there is no  entry for "adam(č|c|ch)uk" the syn list
    testAuthorQuery(
        "\"adamčuk,\"", "author:adamčuk, author:adamchuk, author:adamcuk,", "//*[@numFound='']",
        "\"adamcuk,\"", "author:adamčuk, author:adamchuk, author:adamcuk,", "//*[@numFound='']",
        "\"adamchuk,\"", "author:adamčuk, author:adamchuk, author:adamcuk,", "//*[@numFound='']",
        "\"adamczuk,\"", "author:adamčuk, author:adamchuk, author:adamcuk,", "//*[@numFound='']",
        "\"adamšuk,\"", "author:adamšuk, author:adamshuk, author:adamsuk,", "//*[@numFound='']",
        "\"adamguk,\"", "author:adamguk,", "//*[@numFound='']"
    );


    //<surname>, <1>
    // expanded && upgraded && transliterated && expanded
    // synonym adamšuk IS FOUND because there is entry for "adamčuk, k" the syn list, notice that
    // this works even if we type "adamchuk, k" or "adamcuk, k"
    //
    // question: the chain correctly finds the synonym "adamšuk, k", and this synonym is
    // then transliterated: adamshuk, k;adamsuk, k (is this desirable?) I think yes.
    expected = "author:adamšuk, k author:adamšuk, author:adamsuk, k " + 
               "author:adamsuk, author:adamshuk, k author:adamshuk, author:adamguk, k " + 
               "author:adamguk, author:adamčuk, k author:adamčuk, author:adamchuk, k " + 
               "author:adamchuk, author:adamcuk, k author:adamcuk,";
    
    
    testAuthorQuery(
        "\"adamčuk, k\"", expected, "//*[@numFound='']",
        "\"adamcuk, k\"", expected, "//*[@numFound='']",
        "\"adamchuk, k\"", expected, "//*[@numFound='']",
        "\"adamczuk, k\"", expected, "//*[@numFound='']",
        "\"adamšuk, k\"", expected, "//*[@numFound='']",
        "\"adamguk, k\"", expected, "//*[@numFound='']",
        
        "\"AdAmČuk, K\"", expected, "//*[@numFound='']", // just for fun
        "\"ADAMČUK, K\"", expected, "//*[@numFound='']",
        "\"AdAmCHuk, K\"", expected, "//*[@numFound='']"
    );


    //setDebug(true);
    //<surname>, <1name>
    // upgraded && transliterated && expanded
    // synonym "adamšuk, k" IS FOUND because of the query variation for "adamčuk, k" the syn list
    expected = "author:adamčuk, kolja author:adamčuk, k author:adamčuk, " +
    		       "author:adamcuk, kolja author:adamcuk, k author:adamcuk, " +
    		       "author:adamchuk, kolja author:adamchuk, k author:adamchuk, " +
               "author:adamšuk, k author:adamšuk, " +
               "author:adamsuk, k author:adamsuk, " +
               "author:adamshuk, k author:adamshuk, " +
               "author:adamguk, k author:adamguk,";
    
    testAuthorQuery(
        // exactly the same as expected (17), but in diff order
        // the order is such because input does't need to be upgraded before processing
        "\"adamčuk, kolja\"", "author:adamčuk, kolja author:adamčuk, k author:adamčuk, " +
        		                  "author:adamšuk, k author:adamšuk, " +
        		                  "author:adamsuk, k author:adamsuk, " +
        		                  "author:adamshuk, k author:adamshuk, " +
        		                  "author:adamguk, k author:adamguk, " +
        		                  "author:adamchuk, k author:adamchuk, " +
        		                  "author:adamcuk, k author:adamcuk, " +
        		                  "author:adamcuk, kolja author:adamchuk, kolja", "//*[@numFound='']",
        "\"adamcuk, kolja\"", expected, "//*[@numFound='']",
        "\"adamchuk, kolja\"", expected, "//*[@numFound='']",
        "\"adamczuk, kolja\"", expected, "//*[@numFound='']",
        // the same number as expected (17), but diff is there:
        // "adamčuk, kolja" is not there (and cannot be, because it is not in
        // synonym map, but "adamšuk, k" is found correctly)
        "\"adamšuk, kolja\"", "author:adamšuk, kolja author:adamšuk, k author:adamšuk, " +
        		                  "author:adamsuk, k author:adamsuk, " +
        		                  "author:adamshuk, k author:adamshuk, " +
        		                  "author:adamguk, k author:adamguk, " +
        		                  "author:adamčuk, k author:adamčuk, " +
        		                  "author:adamchuk, k author:adamchuk, " +
        		                  "author:adamcuk, k author:adamcuk, " +
        		                  "author:adamshuk, kolja author:adamsuk, kolja", "//*[@numFound='']",
        // shorter by two variants (15), because "adamguk, kolja" is already ascii form
        // it doesn't generate: "author:adamshuk, kolja author:adamsuk, kolja"
        // that is correct, because "adamšuk, k" is found and transliterated
        // "adamšuk, kolja" simply isn't in any synonym list and we tehrefore cannot have it 
        "\"adamguk, kolja\"", "author:adamguk, kolja author:adamguk, k author:adamguk, " +
        		                  "author:adamšuk, k author:adamšuk, " +
        		                  "author:adamsuk, k author:adamsuk, " +
        		                  "author:adamshuk, k author:adamshuk, " +
        		                  "author:adamčuk, k author:adamčuk, " +
        		                  "author:adamchuk, k author:adamchuk, " +
        		                  "author:adamcuk, k author:adamcuk,", "//*[@numFound='']"
    );
    
    
    //<surname>, <1name> <2>
    // upgraded && transliterated && expanded
    // synonym adamšuk IS NOT FOUND because there is no entry for "adamčuk, kolja k" nor 
    // there is any "adamčuk, k k" in the syn list
    //
    // NOTE: if you think that "adamšuk" should be found in our model, then you are wrong
    // because "adamcuk, k k" is a different name than "adamcuk, k"
    // We are not goign to do any magic to find the surname mapping, in other words:
    // we are not going to replace defficient synonym file. The correct translation CAN
    // WORK if the synonym list has both "adamcuk, k k" and "adamcuk, k"
    expected = "author:adamčuk, kolja k author:adamčuk, k k author:adamčuk, " +
               "author:adamcuk, kolja k author:adamcuk, k k author:adamcuk, " + 
               "author:adamchuk, kolja k author:adamchuk, k k author:adamchuk,";
    
    testAuthorQuery(
        "\"adamčuk, kolja k\"", expected, "//*[@numFound='']",
        "\"adamcuk, kolja k\"", "author:adamcuk, kolja k author:adamcuk, k k author:adamcuk,", "//*[@numFound='']",
        "\"adamchuk, kolja k\"", "author:adamchuk, kolja k author:adamchuk, k k author:adamchuk,", "//*[@numFound='']",
        "\"adamczuk, kolja k\"", "author:adamczuk, kolja k author:adamczuk, k k author:adamczuk,", "//*[@numFound='']",
        "\"adamšuk, kolja k\"", "author:adamšuk, kolja k author:adamšuk, k k author:adamšuk, " +
        		                    "author:adamsuk, kolja k author:adamsuk, k k author:adamsuk, " +
        		                    "author:adamshuk, kolja k author:adamshuk, k k author:adamshuk,", "//*[@numFound='']",
        "\"adamguk, kolja k\"", "author:adamguk, kolja k author:adamguk, k k author:adamguk,", "//*[@numFound='']"
    );

    
    //TODO: show that the translation works properly when the synonym is in the synonym list
    // ie "adamčuk, k k;adamšuk, k k"
    
    
    fail("Hey!!! We have move great chunk of road forward! Yet author parsing still needs attention");
    
    // wildcard searches are expanded only for synonyms
    assertQueryEquals(req("qt", "aqp", "q", "author:\"adamčuk, kol*\""), 
        //"author:adamčuk, k author:adamcuk, k author:adamchuk, k author:adamčuk, author:adamčuk, k* author:adamchuk, karel author:adamčuk, karel author:adamcuk, kolja author:adamcuk, karel author:adamčuk, kolja author:adamchuk, kolja author:adamchuk, k* author:adamchuk, author:adamcuk, author:adamcuk, k*",
        "author:adamčuk, kol*",
        BooleanQuery.class);


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
    
    //assertQueryEquals(req("qt", "aqp", "q", "author:\"adamčuk, kolja\""), 
    //    "author:adamčuk, kolja author:adamcuk, kolja author:adamchuk, kolja author:/adamčuk, kolja\\b.*/ author:adamčuk, author:/adamčuk, k\\b.*/ author:adamcuk, k author:adamchuk, k author:adamčuk, k author:/adamchuk, k\\b.*/ author:/adamchuk, kolja\\b.*/ author:adamchuk, author:/adamcuk, k\\b.*/ author:adamcuk, author:/adamcuk, kolja\\b.*/",
    //    BooleanQuery.class);
    
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


  private void testAuthorQuery(String...vals) throws Exception {
    assert vals.length%3==0;
    for (int i=0;i<vals.length;i=i+3) {
      System.out.println(String.format("author:%s", vals[i]));
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
}
