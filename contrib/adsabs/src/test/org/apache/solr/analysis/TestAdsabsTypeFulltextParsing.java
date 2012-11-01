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
import java.io.File;
import java.io.IOException;
import org.adsabs.solr.AdsConfig.F;

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
          "hubble\0space\0telescope, HST\n" +
          "Massachusets\0Institute\0of\0Technology, MIT\n" +
          "Hubble\0Space\0Microscope, HSM"
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

    /*
     * The difficult part with this token type is the presence of synonyms (besides other things)
     * So, for example in the sentence:
     * 
     *   Mirrors of the hubble space telescope
     *   
     * We must do different things during indexing and querying
     * 
     *  indexing: mirrors,hubble|hubble space telescope|hst,space,telescope
     *  querying: +mirrors +(hubble space telescope | hst)
     *  
     * 
     * During the indexing we want to output BOTH the original tokens, as well as their
     * synonyms. But in the search phase, we only want the synonyms. HOWEVER, we need
     * the original tokens for the proximity queries, if we indexed 'hubble space telescope'
     * as one token, we cannot search for 'hubble NEAR telescope'
     * 
     * The default solr synonym filter is configured for indexing, but it has the ability
     * to do what we want. Unfortunately, the public API does not allow us to configure
     * its behaviour (so I made a custom factory, hopefully that can go away).
     * 
     * 
     * ACRONYMS:
     *   Acronyms are identified IFF they were all UPPERCASE and were present in the 
     *   source text. Acronym is indexed in the original form, as well as with prefix 'acr::'
     *   
     *   Example: MIT
     *   Indexed: mit|acr::mit
     *   
     *   But if the source text contains:
     *   
     *     Massachusets Institute of Technology
     *     
     *   It is expanded into:
     *     0: masschusets|mit|massachusets institute of technology
     *     1: institute
     *     2: (null, removed by the stop filter)
     *     3: technology
     *     
     *   Because the synonym filters IGNORE case, the synonym MIT is emitted as 'mit'
     *   Therefore it cannot be recognized by the Acronym filter (even it it sits after the
     *   synonym filter)
     *   
     *   This has the effect that 'acr::*' will find only documents where the acronym 
     *   was in the source (as opposed to synonym expansion)
     *   
     *   
     *   TODO: maybe we can make the FST search with ignoreCase=true, but emit UpperCase
     *   TODO: the analyzer for the synonyms must use the same StopFilters as the query chain
     * 
     */
    assertU(adoc(F.ID, "1", F.BIBCODE, "xxxxxxxxxxxx1", F.TYPE_ADS_TEXT, "Bílá kobyla skočila přes čtyřista"));
    assertU(adoc(F.ID, "2", F.BIBCODE, "xxxxxxxxxxxx2", F.TYPE_ADS_TEXT, "třicet-tři stříbrných střech"));
    assertU(adoc(F.ID, "3", F.BIBCODE, "xxxxxxxxxxxx3", F.TYPE_ADS_TEXT, "A ještě TřistaTřicetTři stříbrných křepeliček"));
    assertU(adoc(F.ID, "4", F.BIBCODE, "xxxxxxxxxxxx4", F.TYPE_ADS_TEXT, "Mirrors of the hubble space telescope first"));
    assertU(adoc(F.ID, "5", F.BIBCODE, "xxxxxxxxxxxx5", F.TYPE_ADS_TEXT, "Mirrors of the HST second"));
    assertU(adoc(F.ID, "6", F.BIBCODE, "xxxxxxxxxxxx6", F.TYPE_ADS_TEXT, "Mirrors of the Hst third"));
    assertU(adoc(F.ID, "7", F.BIBCODE, "xxxxxxxxxxxx7", F.TYPE_ADS_TEXT, "Mirrors of the HubbleSpaceTelescope fourth"));
    assertU(adoc(F.ID, "8", F.BIBCODE, "xxxxxxxxxxxx8", F.TYPE_ADS_TEXT, "Take Massachusets Institute of Technology (MIT)"));
    assertU(adoc(F.ID, "9", F.BIBCODE, "xxxxxxxxxxxx9", F.TYPE_ADS_TEXT, "MIT developed new network protocols"));

    assertU(commit());



    /*
     * Test multi-token translation, the chain is set to recognize
     * synonyms. So even if the query string is split into 3 tokens,
     * we are able to join them and find their synonym (HST)
     */

    // simple case
    assertQueryEquals(req("q", "hubble space telescope", "qt", "aqp"), 
        "all:hubble space telescope all:hst", BooleanQuery.class);
    // followed by something
    assertQueryEquals(req("q", "hubble space telescope goes home", "qt", "aqp"), 
        "+(all:hubble space telescope all:hst) +all:goes +all:home", BooleanQuery.class);
    // preceded by something
    assertQueryEquals(req("q", "mirrors hubble space telescope start home", "qt", "aqp"), 
        "+all:mirrors +(all:hubble space telescope all:hst) +all:start +all:home", BooleanQuery.class);
    // surrounded by something
    assertQueryEquals(req("q", "mirrors of the hubble space telescope start home", "qt", "aqp"), 
        "+all:mirrors +(all:hubble space telescope all:hst) +all:start +all:home", BooleanQuery.class);
    // surrounded by stop words
    assertQueryEquals(req("q", "mirrors of the hubble space telescope the start home", "qt", "aqp"), 
        "+all:mirrors +(all:hubble space telescope all:hst) +all:start +all:home", BooleanQuery.class);
    // surrounded - change default operator
    assertQueryEquals(req("q", "mirrors of the hubble space telescope start home", "qt", "aqp", "q.op", "OR"), 
        "all:mirrors (all:hubble space telescope all:hst) all:start all:home", BooleanQuery.class);
    // different modifier (synonym must not be found)
    assertQueryEquals(req("q", "hubble space -telescope", "qt", "aqp"), 
        "+all:hubble +all:space -all:telescope", BooleanQuery.class);
    // different field
    assertQueryEquals(req("q", "hubble space title:telescope", "qt", "aqp"), 
        "+all:hubble +all:space +title:telescope", BooleanQuery.class);

    assertQueryEquals(req("q", "hubble space telescope +star", "qt", "aqp"), 
        "+(all:hubble space telescope all:hst) +all:star", BooleanQuery.class);

    /*
     * Synonym expansion 1token->many
     */

    assertQueryEquals(req("q", "HST", "qt", "aqp"), 
        "all:hubble space telescope all:hst all:acr::hst", BooleanQuery.class);

    // XXX: note the acronym is not present (that is because the synonym processor
    // outputs only tokens type=SYNONYM, but if we catch all tokens with posIncr=0
    // it could work)
    // +(all:hubble space telescope all:hst all:acr::hst) +all:goes +all:home
    assertQueryEquals(req("q", "HST goes home", "qt", "aqp"), 
        "+(all:hubble space telescope all:hst) +all:goes +all:home", BooleanQuery.class);

    
    // MIT 
    assertQueryEquals(req("q", "Massachusets Institute of Technology", "qt", "aqp"), 
        "all:massachusets institute of technology all:mit", BooleanQuery.class);
    // XXX: make ignore case for multi-tokens
    //assertQueryEquals(req("q", "massachusets institute of technology", "qt", "aqp"), 
    //    "all:massachusets institute of technology all:mit", BooleanQuery.class);
    //assertQueryEquals(req("q", "Massachusets Institute Technology", "qt", "aqp"), 
    //    "all:massachusets institute of technology all:mit", BooleanQuery.class);
    
    //setDebug(true);

    
    /*
     * alternation of synonym groups:
     * =============================
     */
    
    
    //synonym at extremities (end-end):
    
    //one-token stopword one-token
    assertQueryEquals(req("q", "HST at MIT", "qt", "aqp"), 
        "+(all:hubble space telescope all:hst) +(all:massachusets institute of technology all:mit)", BooleanQuery.class);
    //one-token word one-token
    assertQueryEquals(req("q", "HST bum MIT", "qt", "aqp"), 
        "+(all:hubble space telescope all:hst) +all:bum +(all:massachusets institute of technology all:mit)", BooleanQuery.class);
    //one-token stopword multi-token
    assertQueryEquals(req("q", "HST at Massachusets Institute of Technology", "qt", "aqp"), 
        "+(all:hubble space telescope all:hst) +(all:massachusets institute of technology all:mit)", BooleanQuery.class);
    //one-token word multi-token
    assertQueryEquals(req("q", "HST bum Massachusets Institute of Technology", "qt", "aqp"), 
        "+(all:hubble space telescope all:hst) +all:bum +(all:massachusets institute of technology all:mit)", BooleanQuery.class);
    //multi-token stopword single-token
    assertQueryEquals(req("q", "hubble space telescope at MIT", "qt", "aqp"), 
        "+(all:hubble space telescope all:hst) +(all:massachusets institute of technology all:mit)", BooleanQuery.class);
    //multi-token word single-token
    assertQueryEquals(req("q", "hubble space telescope bum MIT", "qt", "aqp"), 
        "+(all:hubble space telescope all:hst) +all:bum +(all:massachusets institute of technology all:mit)", BooleanQuery.class);
    
    
    // synonyms hidden inside other words:
    
    //word one-token stopword one-token word
    assertQueryEquals(req("q", "foo HST at MIT bar", "qt", "aqp"), 
        "+all:foo +(all:hubble space telescope all:hst) +(all:massachusets institute of technology all:mit) +all:bar", BooleanQuery.class);
    //word one-token word one-token word
    assertQueryEquals(req("q", "foo HST bum MIT bar", "qt", "aqp"), 
        "+all:foo +(all:hubble space telescope all:hst) +all:bum +(all:massachusets institute of technology all:mit) +all:bar", BooleanQuery.class);
    //word one-token stopword multi-token word
    assertQueryEquals(req("q", "foo HST at Massachusets Institute of Technology bar", "qt", "aqp"), 
        "+all:foo +(all:hubble space telescope all:hst) +(all:massachusets institute of technology all:mit) +all:bar", BooleanQuery.class);
    //word one-token word multi-token word
    assertQueryEquals(req("q", "foo HST bum Massachusets Institute of Technology bar", "qt", "aqp"), 
        "+all:foo +(all:hubble space telescope all:hst) +all:bum +(all:massachusets institute of technology all:mit) +all:bar", BooleanQuery.class);
    //word multi-token stopword single-token word
    assertQueryEquals(req("q", "foo hubble space telescope at MIT bar", "qt", "aqp"), 
        "+all:foo +(all:hubble space telescope all:hst) +(all:massachusets institute of technology all:mit) +all:bar", BooleanQuery.class);
    //word multi-token word single-token word
    assertQueryEquals(req("q", "foo hubble space telescope bum MIT bar", "qt", "aqp"), 
        "+all:foo +(all:hubble space telescope all:hst) +all:bum +(all:massachusets institute of technology all:mit) +all:bar", BooleanQuery.class);
    
    
    /**
     * WordDelimiterFactory + synonym expansion craziness
     */
    
    /*
     * Example of the CamelCase - because the WordDelimiterFactory is before the synonym filter
     * these token are first split and then matched. HOWEVER, the case is important!!
     * 
     * So, HubbleSpaceMicroscope is split into: Hubble, Space, Microscope
     * 
     * Which will be found only if the synonym file contains the same case (OR: if we enable the
     * case insensitive search, which is on my TODO list)
     * 
     */
    assertQueryEquals(req("q", "HubbleSpaceMicroscope bum MIT BX", "qt", "aqp"), 
        "+(all:hubble space microscope all:hsm) +all:bum +(all:massachusets institute of technology all:mit) +(all:bx all:acr::bx)", 
        BooleanQuery.class);
    assertQueryEquals(req("q", "HubbleSpaceMicroscope -bum MIT BX", "qt", "aqp"), 
        "+(all:hubble space microscope all:hsm) -all:bum +(all:massachusets institute of technology all:mit) +(all:bx all:acr::bx)",
        BooleanQuery.class);

    assertQueryEquals(req("q", "Hubble-Space-Microscope bum MIT BX", "qt", "aqp"), 
        "+(all:hubble space microscope all:hsm) +all:bum +(all:massachusets institute of technology all:mit) +(all:bx all:acr::bx)", 
        BooleanQuery.class);
    
    /*
     * But right now, *QUERY* synonym expansion is case sensitive, so these will
     * not find anything (even if 'Hst' was in the synonym list). 
     */
    assertQueryEquals(req("q", "Hst", "qt", "aqp"), 
        "all:hst", TermQuery.class);

    assertQueryEquals(req("q", "hst", "qt", "aqp"), 
        "all:hst", TermQuery.class);

    assertQueryEquals(req("q", "HST OR Hst", "qt", "aqp"), 
        "(all:hubble space telescope all:hst all:acr::hst) all:hst", BooleanQuery.class);


    //TODO: add the corresponding searches, but this shows we are indexing  properly
    //dumpDoc(null, F.ID, F.ADS_TEXT_TYPE);


    // the ascii folding filter emits both unicode and the ascii version
    assertQ(req("q", F.TYPE_ADS_TEXT + ":Bílá"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
    assertQ(req("q", F.TYPE_ADS_TEXT + ":Bila"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
    assertQ(req("q", F.TYPE_ADS_TEXT + ":bila"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
  }


  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeFulltextParsing.class);
  }
}
