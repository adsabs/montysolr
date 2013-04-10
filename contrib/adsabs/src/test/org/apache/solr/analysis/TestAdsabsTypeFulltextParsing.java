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
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spans.SpanNearQuery;

import java.io.File;
import java.io.IOException;
import org.adsabs.solr.AdsConfig.F;

/**
 * Tests that the fulltext is parsed properly, the ads_text type
 * is not as simple as it seems
 * 
 * The ads_text has several tasks to do:
 * 
 *    1) normalize the input text, ie. token -foo => token-foo
 *       this is done through a series of pattern replace filters
 *    2) use WordDelimiterFilterFactory to split words (ie. all-sky)
 *    3) discover synonyms (and we have several families of synonyms)
 *       - multi-token: search case insensitively
 *       - acronyms: search case sensitively
 *       - single-token: search case insensitively
 *       Each of the newly discovered tokens is *inserted* into the
 *       document, we take care to preserve also the original token
 *       Synonyms have prefix 'syn::' and acronyms 'acr::'
 *    4) remove stopwords
 *    5) normalization (lowercase etc)
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
      
      // notice 'mond' is a synonym in both synonym files
      // notice two rows point into 'lunar' - they should be merged, which means
      // if you searched for 'mond' or 'space', it resolves to 'syn:lunar' 
      // but if you search for lunar, you WILL NOT find 'mond'
      File simpleTokenSynonymsFile = createTempFile(new String[]{
      		"moon,moons,luna,lune,mond=>lunar\n" +
      		"stetoscope=>glass\n" +
      		"pace=> lunar\n"
      });
      
      File multiTokenSynonymsFile = createTempFile(new String[]{
      		"dynamics\0hubble,dyhu\n" +
          "hubble\0space\0telescope,HST\n" +
          "Massachusets\0Institute\0of\0Technology, MIT\n" +
          "Hubble\0Space\0Microscope, HSM\n" +
          "ABC,Astrophysics\0Business\0Center\n" +
          "Astrophysics\0Business\0Commons, ABC\n" + 
          "MOND,modified\0newtonian\0dynamics\n" +
          "bubble\0pace\0telescope,BPT\n"
      });
      
      replaceInFile(newConfig, "synonyms=\"ads_text_multi.synonyms\"", "synonyms=\"" + multiTokenSynonymsFile.getAbsolutePath() + "\"");
      replaceInFile(newConfig, "synonyms=\"ads_text_simple.synonyms\"", "synonyms=\"" + simpleTokenSynonymsFile.getAbsolutePath() + "\"");

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
     *     0: massachusets|mit|massachusets institute of technology
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
    assertU(adoc(F.ID, "4", F.BIBCODE, "xxxxxxxxxxxx4", F.TYPE_ADS_TEXT, "Mirrors of the hubble space telescope goes home"));
    assertU(adoc(F.ID, "5", F.BIBCODE, "xxxxxxxxxxxx5", F.TYPE_ADS_TEXT, "Mirrors of the HST second"));
    assertU(adoc(F.ID, "6", F.BIBCODE, "xxxxxxxxxxxx6", F.TYPE_ADS_TEXT, "Mirrors of the Hst third"));
    assertU(adoc(F.ID, "7", F.BIBCODE, "xxxxxxxxxxxx7", F.TYPE_ADS_TEXT, "Mirrors of the HubbleSpaceTelescope fourth"));
    assertU(adoc(F.ID, "8", F.BIBCODE, "xxxxxxxxxxxx8", F.TYPE_ADS_TEXT, "Take Massachusets Institute of Technology (MIT)"));
    assertU(adoc(F.ID, "9", F.BIBCODE, "xxxxxxxxxxxx9", F.TYPE_ADS_TEXT, "MIT developed new network protocols"));
    assertU(adoc(F.ID, "10", F.BIBCODE, "xxxxxxxxxxx10", F.TYPE_ADS_TEXT, "No-sky data survey"));
    assertU(adoc(F.ID, "11", F.BIBCODE, "xxxxxxxxxxx11", F.TYPE_ADS_TEXT, "All-sky data survey"));
    assertU(adoc(F.ID, "12", F.BIBCODE, "xxxxxxxxxxx12", F.TYPE_ADS_TEXT, "NoSky data survey"));
    assertU(adoc(F.ID, "13", F.BIBCODE, "xxxxxxxxxxx13", F.TYPE_ADS_TEXT, "AllSky data survey"));
    assertU(adoc(F.ID, "14", F.BIBCODE, "xxxxxxxxxxx14", F.TYPE_ADS_TEXT, "Modified Newtonian Dynamics (MOND): Observational Phenomenology and Relativistic Extensions"));
    assertU(adoc(F.ID, "15", F.BIBCODE, "xxxxxxxxxxx15", F.TYPE_ADS_TEXT, "MOND test"));
    assertU(adoc(F.ID, "16", F.BIBCODE, "xxxxxxxxxxx16", F.TYPE_ADS_TEXT, "mond test"));
    assertU(adoc(F.ID, "17", F.BIBCODE, "xxxxxxxxxxx17", F.TYPE_ADS_TEXT, "bubble pace telescope multi-pace foobar"));
    assertU(adoc(F.ID, "18", F.BIBCODE, "xxxxxxxxxxx18", F.TYPE_ADS_TEXT, "Mirrors of the Hubble fooox Space Telescope"));
    
    assertU(adoc(F.ID, "147", F.BIBCODE, "xxxxxxxxxx147", F.TYPE_ADS_TEXT, "NAG5-5269"));
    assertU(adoc(F.ID, "148", F.BIBCODE, "xxxxxxxxxx148", F.TYPE_ADS_TEXT, "NAG55269"));
    assertU(adoc(F.ID, "149", F.BIBCODE, "xxxxxxxxxx149", F.TYPE_ADS_TEXT, "NAG5 5269"));
    assertU(adoc(F.ID, "150", F.BIBCODE, "xxxxxxxxxx150", F.TYPE_ADS_TEXT, "nag5-5269"));
    assertU(adoc(F.ID, "151", F.BIBCODE, "xxxxxxxxxx151", F.TYPE_ADS_TEXT, "nag55269"));
    assertU(adoc(F.ID, "152", F.BIBCODE, "xxxxxxxxxx152", F.TYPE_ADS_TEXT, "nag5 5269"));
    
    assertU(adoc(F.ID, "318", F.BIBCODE, "xxxxxxxxxx318", F.TYPE_ADS_TEXT, "creation of a thesaurus"));
    
    
    
    assertU(commit());

    
    
    dumpDoc(null, F.ID, F.TYPE_ADS_TEXT);
    
    
//    assertQueryEquals(req("q", "\"NASA grant\"~3 NEAR N*", "qt", "aqp", "qf", "author^1.5 title^1.4 abstract^1.3 all"), 
//        "(((spanNear([abstract:acr::nag5, abstract:5269], 5, true) abstract:acr::nag55269)^1.3) | ((author:nag5 5269, author:nag5 5269, * author:nag5 5 author:nag5 5 * author:nag5)^1.5) | ((spanNear([title:acr::nag5, title:5269], 5, true) title:acr::nag55269)^1.4) | (spanNear([all:acr::nag5, all:5269], 5, true) all:acr::nag55269))", 
//        DisjunctionMaxQuery.class);
//    assertQ(req("q", "NAG5-5269", "qf", "author^1.5 title^1.4 abstract^1.3 all"), 
//    		"//*[@numFound='3']",
//        "//doc/str[@name='id'][.='147']",
//        "//doc/str[@name='id'][.='148']",
//        "//doc/str[@name='id'][.='149']"
//        );
    
    // related to ticket #147
    assertQueryEquals(req("q", "NAG5-5269", "qt", "aqp", "qf", "author^1.5 title^1.4 abstract^1.3 all", "aqp.uop", "span"), 
        "(((spanNear([abstract:acr::nag5, abstract:5269], 5, true) abstract:acr::nag55269)^1.3) | ((author:nag5 5269, author:nag5 5269, * author:nag5 5 author:nag5 5 * author:nag5)^1.5) | ((spanNear([title:acr::nag5, title:5269], 5, true) title:acr::nag55269)^1.4) | (spanNear([all:acr::nag5, all:5269], 5, true) all:acr::nag55269))", 
        DisjunctionMaxQuery.class);
    assertQueryEquals(req("q", "NAG5-5269", "qt", "aqp", "qf", "author^1.5 title^1.4 abstract^1.3 all"), 
        "(((spanNear([abstract:acr::nag5, abstract:5269], 5, true) abstract:acr::nag55269)^1.3) | ((author:nag5 5269, author:nag5 5269, * author:nag5 5 author:nag5 5 * author:nag5)^1.5) | ((spanNear([title:acr::nag5, title:5269], 5, true) title:acr::nag55269)^1.4) | (spanNear([all:acr::nag5, all:5269], 5, true) all:acr::nag55269))", 
        DisjunctionMaxQuery.class);
    assertQ(req("q", "NAG5-5269", "qf", "author^1.5 title^1.4 abstract^1.3 all"), 
    		"//*[@numFound='3']",
        "//doc/str[@name='id'][.='147']",
        "//doc/str[@name='id'][.='148']",
        "//doc/str[@name='id'][.='149']"
        );
    
    assertQueryEquals(req("q", "nag5-5269", "qt", "aqp", "qf", "author^1.5 title^1.4 abstract^1.3 all"), 
        "(((spanNear([abstract:nag5, abstract:5269], 5, true) abstract:nag55269)^1.3) | ((author:nag5 5269, author:nag5 5269, * author:nag5 5 author:nag5 5 * author:nag5)^1.5) | ((spanNear([title:nag5, title:5269], 5, true) title:nag55269)^1.4) | (spanNear([all:nag5, all:5269], 5, true) all:nag55269))", 
        DisjunctionMaxQuery.class);
    assertQ(req("q", "nag5-5269", "qf", "author^1.5 title^1.4 abstract^1.3 all"), 
    		"//*[@numFound='6']",
    		"//doc/str[@name='id'][.='147']",
        "//doc/str[@name='id'][.='148']",
        "//doc/str[@name='id'][.='149']",
        "//doc/str[@name='id'][.='150']",
        "//doc/str[@name='id'][.='151']",
        "//doc/str[@name='id'][.='152']"
        );
    
    
    // ticket #318
    assertQueryEquals(req("q", "creation of a thesaurus", "qt", "aqp", "qf", "author^1.5 title^1.4 abstract^1.3 all"), 
        "(spanNear([abstract:creation, abstract:thesaurus], 5, true)^1.3 | ((author:creation of a thesaurus, author:creation of a thesaurus, * author:/creation of a[^\\s]+ thesaurus,/ author:/creation of a[^\\s]+ thesaurus, .*/ author:creation o a thesaurus, author:creation o a thesaurus, * author:/creation o a[^\\s]+ thesaurus,/ author:/creation o a[^\\s]+ thesaurus, .*/ author:creation of a t author:creation of a t * author:/creation of a[^\\s]+ t/ author:/creation of a[^\\s]+ t .*/ author:creation o a t author:creation o a t * author:/creation o a[^\\s]+ t/ author:/creation o a[^\\s]+ t .*/ author:creation of author:creation o author:creation)^1.5) | spanNear([title:creation, title:thesaurus], 5, true)^1.4 | spanNear([all:creation, all:thesaurus], 5, true))", 
        DisjunctionMaxQuery.class);
    assertQ(req("q", "creation of a thesaurus", "qf", "author^1.5 title^1.4 abstract^1.3 all"), 
    		"//*[@numFound='1']",
        "//doc/str[@name='id'][.='318']");
    
    
    
    // ticket #320
    // in natural language: when searching for MOND, we'll first find the multi-token synonyms
    // ie. MOND, modified newtonina dynamics
    // then search for simple synonymes: <find nothing, ie. ignore 'mond'>
    // MOND is caught by acronym filter, which is configured to eat the original
    // and the result is made of acronym + synonym + multi-token-synonym
    
    // test with a field
    assertQueryEquals(req("q", "title:MOND", "qt", "aqp"), 
        "title:acr::mond title:syn::acr::mond title:syn::modified newtonian dynamics", BooleanQuery.class);
    assertQueryEquals(req("q", "title:mond", "qt", "aqp"), 
        "title:mond title:syn::lunar", BooleanQuery.class);
    assertQueryEquals(req("q", "title:Mond", "qt", "aqp"), 
        "title:mond title:syn::lunar", BooleanQuery.class);
    
    // unfielded simple token
    assertQueryEquals(req("q", "MOND", "qt", "aqp"), 
        "all:acr::mond all:syn::acr::mond all:syn::modified newtonian dynamics", BooleanQuery.class);
    assertQ(req("q", F.TYPE_ADS_TEXT + ":MOND"), "//*[@numFound='2']",
    		"//doc/str[@name='id'][.='14']",
        "//doc/str[@name='id'][.='15']");
    
    assertQueryEquals(req("q", "mond", "qt", "aqp"), 
        "all:mond all:syn::lunar", BooleanQuery.class);
    assertQ(req("q", F.TYPE_ADS_TEXT + ":mond", "explain", "true"), "//*[@numFound='4']",
    		"//doc/str[@name='id'][.='14']",
    		"//doc/str[@name='id'][.='15']",
        "//doc/str[@name='id'][.='16']",
        "//doc/str[@name='id'][.='17']");
    
    assertQueryEquals(req("q", "Mond", "qt", "aqp"), 
        "all:mond all:syn::lunar", BooleanQuery.class);
    assertQ(req("q", F.TYPE_ADS_TEXT + ":Mond"), "//*[@numFound='4']",
    		"//doc/str[@name='id'][.='17']", // orig 'space' -> syn:lunar; look at the synonym file to understand
    		"//doc/str[@name='id'][.='14']",
    		"//doc/str[@name='id'][.='15']",
        "//doc/str[@name='id'][.='16']");
    
    // search for 'pace' and find 'mond' (there is intentional error/duplication
    // in our synonym files - look above)
    assertQueryEquals(req("q", "pace", "qt", "aqp"), 
        "all:pace all:syn::lunar", BooleanQuery.class);
    assertQ(req("q", F.TYPE_ADS_TEXT + ":pace"), "//*[@numFound='2']",
    		"//doc/str[@name='id'][.='17']",
        "//doc/str[@name='id'][.='16']");
    
    // search for 'lunar' MUST NOT return 'mond' (because synonyms are explicit =>)
    // and 'lunar' is not on the left hand side
    assertQueryEquals(req("q", "lunar", "qt", "aqp"), 
        "all:lunar", TermQuery.class);
    assertQ(req("q", F.TYPE_ADS_TEXT + ":lunar"), "//*[@numFound='0']");
    
    // but 'luna' is a synonym (syn::lunar)
    assertQueryEquals(req("q", "luna", "qt", "aqp"), 
        "all:luna all:syn::lunar", BooleanQuery.class);
    assertQ(req("q", F.TYPE_ADS_TEXT + ":luna"), "//*[@numFound='2']",
    		"//doc/str[@name='id'][.='17']",
        "//doc/str[@name='id'][.='16']");

    
    // now the multi-token version
    assertQueryEquals(req("q", "title:\"modified newtonian dynamics\"", "qt", "aqp"), 
        "title:\"modified newtonian dynamics\"" +
        " (title:syn::acr::mond title:syn::modified newtonian dynamics)", 
        BooleanQuery.class);
    assertQ(req("q", F.TYPE_ADS_TEXT + ":\"modified newtonian dynamics\""), "//*[@numFound='2']",
    		"//doc/str[@name='id'][.='14']",
        "//doc/str[@name='id'][.='15']");
    

    // unfielded multi-token. this is truly crazy (several synonyms overlap)
    // 'bubble pace telescope' is a synonym
    // 'pace' is a synonym
    // multi-pace is split by WDFF and expanded with a synonym
    assertQueryEquals(req("q", "title:\"bubble pace telescope multi-pace foobar\"", "qt", "aqp"), 
        "title:\"bubble (pace syn::lunar) telescope multi (pace syn::lunar) foobar\"" +
        " title:\"bubble (pace syn::lunar) telescope ? multipace foobar\"" +
        " title:\"(syn::bubble pace telescope syn::acr::bpt) ? ? multi (pace syn::lunar) foobar\"" +
        " title:\"(syn::bubble pace telescope syn::acr::bpt) ? ? ? multipace foobar\"",
        BooleanQuery.class);
    assertQ(req("q", F.TYPE_ADS_TEXT + ":\"bubble pace telescope multi-pace foobar\""), "//*[@numFound='1']",
        "//doc/str[@name='id'][.='17']");
    
    
    // now the same thing, but not using phrases
    assertQueryEquals(req("q", "title:modified\\ newtonian\\ dynamics", "qt", "aqp"), 
        "spanNear([title:modified, title:newtonian, title:dynamics], 5, true)" +
        " (title:syn::acr::mond title:syn::modified newtonian dynamics)", 
        BooleanQuery.class);
    assertQ(req("q", F.TYPE_ADS_TEXT + ":modified\\ newtonian\\ dynamics"), "//*[@numFound='2']",
    		"//doc/str[@name='id'][.='14']",
        "//doc/str[@name='id'][.='15']");
    
    assertQueryEquals(req("q", "title:modified newtonian dynamics", "qt", "aqp"), 
    		"spanNear([title:modified, title:newtonian, title:dynamics], 5, true)" +
    		" (title:syn::acr::mond title:syn::modified newtonian dynamics)", BooleanQuery.class);
    assertQ(req("q", F.TYPE_ADS_TEXT + ":modified newtonian dynamics"), "//*[@numFound='2']",
    		"//doc/str[@name='id'][.='14']",
        "//doc/str[@name='id'][.='15']");
    
    
    // and even unfielded!
    assertQueryEquals(req("q", "modified\\ newtonian\\ dynamics", "qt", "aqp", "qf", ""), 
        "spanNear([all:modified, all:newtonian, all:dynamics], 5, true)" +
        " (all:syn::acr::mond all:syn::modified newtonian dynamics)", 
        BooleanQuery.class);
    assertQ(req("q", "modified\\ newtonian\\ dynamics"), "//*[@numFound='2']",
    		"//doc/str[@name='id'][.='14']",
        "//doc/str[@name='id'][.='15']");
    
    assertQueryEquals(req("q", "modified newtonian dynamics", "qt", "aqp", "qf", ""), 
        "spanNear([all:modified, all:newtonian, all:dynamics], 5, true)" +
        " (all:syn::acr::mond all:syn::modified newtonian dynamics)", 
        BooleanQuery.class);
    assertQ(req("q", "modified newtonian dynamics"), "//*[@numFound='2']",
    		"//doc/str[@name='id'][.='14']",
        "//doc/str[@name='id'][.='15']");
    
    assertQueryEquals(req("q", "modified\\ newtonian\\ dynamics", "qt", "aqp", "qf", "title^2.0 all^1.5"), 
        "(((spanNear([title:modified, title:newtonian, title:dynamics], 5, true)" +
        " (title:syn::acr::mond title:syn::modified newtonian dynamics))^2.0)" +
        " | ((spanNear([all:modified, all:newtonian, all:dynamics], 5, true)" +
        " (all:syn::acr::mond all:syn::modified newtonian dynamics))^1.5))", 
        DisjunctionMaxQuery.class);
    assertQ(req("q", "modified\\ newtonian\\ dynamics", "qf", "title^2.0 all^1.5"), 
    		"//*[@numFound='2']",
    		"//doc/str[@name='id'][.='14']",
        "//doc/str[@name='id'][.='15']");
    
    assertQueryEquals(req("q", "modified newtonian dynamics", "qt", "aqp", "qf", "title^2.0 all^1.5"), 
    		"(((spanNear([title:modified, title:newtonian, title:dynamics], 5, true)" +
    		" (title:syn::acr::mond title:syn::modified newtonian dynamics))^2.0)" +
    		" | ((spanNear([all:modified, all:newtonian, all:dynamics], 5, true)" +
    		" (all:syn::acr::mond all:syn::modified newtonian dynamics))^1.5))", 
    		DisjunctionMaxQuery.class);
    assertQ(req("q", "modified newtonian dynamics", "qf", "title^2.0 all^1.5"), 
    		"//*[@numFound='2']",
    		"//doc/str[@name='id'][.='14']",
        "//doc/str[@name='id'][.='15']");
    
    // lastly - unfielded phrase
    assertQueryEquals(req("q", "\"modified newtonian dynamics\"", "qt", "aqp", "qf", "title^2.0 all^1.5"), 
    		"(((title:\"modified newtonian dynamics\" (title:syn::acr::mond title:syn::modified newtonian dynamics))^2.0)" +
    		" | ((all:\"modified newtonian dynamics\" (all:syn::acr::mond all:syn::modified newtonian dynamics))^1.5))", 
    		DisjunctionMaxQuery.class);
    assertQ(req("q", "\"modified newtonian dynamics\"", "qf", "title^2.0 all^1.5"), 
    		"//*[@numFound='2']",
    		"//doc/str[@name='id'][.='14']",
        "//doc/str[@name='id'][.='15']");

    
    // test of the multi-synonym replacement, phrase handling etc
    assertQueryEquals(req("q", "\"bubble pace telescope multi-pace query\"", "qt", "aqp"), 
        "all:\"bubble (pace syn::lunar) telescope multi (pace syn::lunar)\"" +
        " all:\"bubble (pace syn::lunar) telescope ? multipace\"" +
        " all:\"(syn::bubble pace telescope syn::acr::bpt) ? ? multi (pace syn::lunar)\"" +
        " all:\"(syn::bubble pace telescope syn::acr::bpt) ? ? ? multipace\"", 
        BooleanQuery.class);
    
    // wow! this works correctly - but it is wrong still
    // the AND operator has precedence, but I guess we should merge
    // the previous tokens to form AND operator; but the query
    // as it stands, is doing the right thing!
    assertQueryEquals(req("q", "bubble pace telescope and MIT", "qt", "aqp"), 
        "+(+all:telescope +(all:acr::mit all:syn::massachusets institute of technology all:syn::acr::mit)) " +
        "+spanNear([all:bubble, spanOr([all:pace, all:syn::lunar])], 5, true)", BooleanQuery.class);
    
    
    
    
    /*
     * Test multi-token translation, the chain is set to recognize
     * synonyms. So even if the query string is split into 3 tokens,
     * we are able to join them and find their synonym (HST)
     * 
     */
    
    //setDebug(true);
    // simple case
    assertQueryEquals(req("q", "\"hubble space telescope\"", "qt", "aqp"), 
        "all:\"hubble space telescope\"" +
        " (all:syn::hubble space telescope all:syn::acr::hst)", 
        BooleanQuery.class);
    assertQ(req("q", "\"hubble space telescope\""), 
    		"//*[@numFound='2']",
    		"//doc/str[@name='id'][.='4']",
        "//doc/str[@name='id'][.='5']");
    
    // non-phrase: by default do span search
    assertQueryEquals(req("q", "hubble space telescope", "qt", "aqp"), 
        "spanNear([all:hubble, all:space, all:telescope], 5, true)" +
        " (all:syn::hubble space telescope all:syn::acr::hst)", 
        BooleanQuery.class);
    assertQ(req("q", "hubble space telescope"), 
    		"//*[@numFound='3']",
    		"//doc/str[@name='id'][.='4']",
        "//doc/str[@name='id'][.='5']",
        "//doc/str[@name='id'][.='18']"
        );
    
    // make sure the unfielded search is expanded properly (by edismax) - we use it just here
    // HOWEVER: maybe it should do expansion inside each clause? now it favors docs with matches in all fields (which is fine)
    assertQueryEquals(req("q", "hubble space telescope", "qt", "aqp", "qf", "title^2.0 keyword^1.5"), 
        "(((spanNear([title:hubble, title:space, title:telescope], 5, true)" +
        " (title:syn::hubble space telescope title:syn::acr::hst))^2.0)" +
        " | spanNear([keyword:hubble, keyword:space, keyword:telescope], 5, true)^1.5)", 
        DisjunctionMaxQuery.class);
    
        
    // preceded by something
    assertQueryEquals(req("q", "\"mirrors of the hubble space telescope\"", "qt", "aqp"), 
        "all:\"mirrors hubble space telescope\"" +
        " all:\"mirrors (syn::hubble space telescope syn::acr::hst)\"", 
        BooleanQuery.class);
    
    assertQ(req("q", "\"mirrors hubble space telescope\""), 
    		"//*[@numFound='2']",
    		"//doc/str[@name='id'][.='4']",
    		"//doc/str[@name='id'][.='5']"
        );
    assertQ(req("q", "\"mirrors of the hubble space telescope\""), 
    		"//*[@numFound='2']",
    		"//doc/str[@name='id'][.='4']",
    		"//doc/str[@name='id'][.='5']"
        );
    assertQ(req("q", "\"mirrors of the hubble space scope\""), 
    		"//*[@numFound='0']"
        );
    
    // query followed by something
    assertQueryEquals(req("q", "\"hubble space telescope goes home\"", "qt", "aqp"), 
        "all:\"hubble space telescope goes home\"" +
        " all:\"(syn::hubble space telescope syn::acr::hst) ? ? goes home\"", 
        BooleanQuery.class);
    assertQ(req("q", "\"hubble space telescope goes home\""), 
    		"//*[@numFound='1']",
    		"//doc/str[@name='id'][.='4']"
        );
    
    assertQueryEquals(req("q", "hubble space telescope goes home", "qt", "aqp"), 
        "spanNear([all:hubble, all:space, all:telescope, all:goes, all:home], 5, true) spanNear([spanOr([all:syn::hubble space telescope, all:syn::acr::hst]), all:goes, all:home], 5, true)", BooleanQuery.class);
    assertQ(req("q", "hubble space telescope goes home"), 
    		"//*[@numFound='1']",
    		"//doc/str[@name='id'][.='4']"
        );

    
    // surrounded by something
    assertQueryEquals(req("q", "\"mirrors of the hubble space telescope goes home\"", "qt", "aqp"), 
        "all:\"mirrors hubble space telescope goes home\"" +
        " all:\"mirrors (syn::hubble space telescope syn::acr::hst) ? ? goes home\"", 
        BooleanQuery.class);
    assertQ(req("q", "\"mirrors of the hubble space telescope goes home\""), 
    		"//*[@numFound='1']",
    		"//doc/str[@name='id'][.='4']"
        );
    assertQueryEquals(req("q", "mirrors of the hubble space telescope goes home", "qt", "aqp"), 
        "spanNear([all:mirrors, all:hubble, all:space, all:telescope, all:goes, all:home], 5, true)" +
        " spanNear([all:mirrors, spanOr([all:syn::hubble space telescope, all:syn::acr::hst]), all:goes, all:home], 5, true)", 
        BooleanQuery.class);
    assertQ(req("q", "mirrors of the hubble space telescope goes home"), 
    		"//*[@numFound='1']",
    		"//doc/str[@name='id'][.='4']"
        );

    // surrounded by stop words
    assertQueryEquals(req("q", "mirrors of the hubble space telescope the goes home", "qt", "aqp"), 
        "spanNear([all:mirrors, all:hubble, all:space, all:telescope, all:goes, all:home], 5, true)" +
        " spanNear([all:mirrors, spanOr([all:syn::hubble space telescope, all:syn::acr::hst]), all:goes, all:home], 5, true)", 
        BooleanQuery.class);
    assertQ(req("q", "mirrors of the hubble space telescope goes home"), 
    		"//*[@numFound='1']",
    		"//doc/str[@name='id'][.='4']"
        );
    
    // surrounded - change default operator (many matches)
    assertQueryEquals(req("q", "mirrors of the hubble space telescope start home", "qt", "aqp", "q.op", "OR"), 
        "(all:mirrors all:hubble all:space all:telescope all:start all:home)" +
        " (all:mirrors (all:syn::hubble space telescope all:syn::acr::hst) all:start all:home)", 
        BooleanQuery.class);
    assertQ(req("q", "mirrors of the hubble space telescope start home", "q.op", "OR"), 
    		"//*[@numFound='6']",
    		"//doc[1]/str[@name='id'][.='4']", // this one is the best match
    		"//doc/str[@name='id'][.='18']",
    		"//doc/str[@name='id'][.='5']",
    		"//doc/str[@name='id'][.='6']",
    		"//doc/str[@name='id'][.='7']",
    		"//doc/str[@name='id'][.='17']"
        );
    
    // different modifier (synonym must not be found)
    setDebug(true);
    assertQueryEquals(req("q", "hubble space -telescope", "qt", "aqp"), 
        "+all:hubble +all:space -all:telescope", BooleanQuery.class);
    assertQueryEquals(req("q", "hubble space -telescope", "qt", "aqp"), 
        "+all:hubble +all:space -all:telescope", BooleanQuery.class);
    assertQueryEquals(req("q", "hubble space -telescope", "qt", "aqp"), 
        "+all:hubble +all:space -all:telescope", BooleanQuery.class);
    assertQueryEquals(req("q", "hubble space -telescope", "qt", "aqp"), 
        "+all:hubble +all:space -all:telescope", BooleanQuery.class);
    
    // different field
    assertQueryEquals(req("q", "hubble space title:telescope", "qt", "aqp"), 
        "+all:hubble +all:space +title:telescope", BooleanQuery.class);

    assertQueryEquals(req("q", "hubble space telescope +star", "qt", "aqp"), 
        "+(all:hubble space telescope all:acr::hst) +all:star", BooleanQuery.class);

    /*
     * Synonym expansion 1token->many
     */
    assertQueryEquals(req("q", "HST", "qt", "aqp"), 
        "all:hubble space telescope all:acr::hst", BooleanQuery.class);
    
    
    // XXX: note the acronym is not present (that is because the synonym processor
    // outputs only tokens type=SYNONYM, but if we catch all tokens with posIncr=0
    // it could work)
    // +(all:hubble space telescope all:acr::hst all:acr::hst) +all:goes +all:home
    assertQueryEquals(req("q", "HST goes home", "qt", "aqp"), 
        "+(all:hubble space telescope all:acr::hst) +all:goes +all:home", BooleanQuery.class);

    
    // MIT 
    assertQueryEquals(req("q", "Massachusets Institute of Technology", "qt", "aqp"), 
        "all:massachusets institute of technology all:acr::mit", BooleanQuery.class);
    // XXX: make ignore case for multi-tokens
    //assertQueryEquals(req("q", "massachusets institute of technology", "qt", "aqp"), 
    //    "all:massachusets institute of technology all:acr::mit", BooleanQuery.class);
    //assertQueryEquals(req("q", "Massachusets Institute Technology", "qt", "aqp"), 
    //    "all:massachusets institute of technology all:acr::mit", BooleanQuery.class);
    
    //setDebug(true);

    /*
     * Case (In)Sensitivity 
     * 
     * It shoulb be ase sensitive for single tokens, and case-insensitive
     * for multi-tokens
     */
    
    assertQueryEquals(req("q", "hst", "qt", "aqp"), 
        "all:hst", TermQuery.class);
    assertQueryEquals(req("q", "HSt", "qt", "aqp"), 
        "all:hst", TermQuery.class);
    assertQueryEquals(req("q", "Hubble Space Telescope +star", "qt", "aqp"), 
        "+(all:hubble space telescope all:acr::hst) +all:star", BooleanQuery.class);
    
    
    /*
     * alternation of synonym groups:
     * =============================
     */
    
    
    //synonym at extremities (end-end):
    
    //one-token stopword one-token
    assertQueryEquals(req("q", "HST at MIT", "qt", "aqp"), 
        "+(all:hubble space telescope all:acr::hst) +(all:massachusets institute of technology all:acr::mit)", BooleanQuery.class);
    //one-token word one-token
    assertQueryEquals(req("q", "HST bum MIT", "qt", "aqp"), 
        "+(all:hubble space telescope all:acr::hst) +all:bum +(all:massachusets institute of technology all:acr::mit)", BooleanQuery.class);
    //one-token stopword multi-token
    assertQueryEquals(req("q", "HST at Massachusets Institute of Technology", "qt", "aqp"), 
        "+(all:hubble space telescope all:acr::hst) +(all:massachusets institute of technology all:acr::mit)", BooleanQuery.class);
    //one-token word multi-token
    assertQueryEquals(req("q", "HST bum Massachusets Institute of Technology", "qt", "aqp"), 
        "+(all:hubble space telescope all:acr::hst) +all:bum +(all:massachusets institute of technology all:acr::mit)", BooleanQuery.class);
    //multi-token stopword single-token
    assertQueryEquals(req("q", "hubble space telescope at MIT", "qt", "aqp"), 
        "+(all:hubble space telescope all:acr::hst) +(all:massachusets institute of technology all:acr::mit)", BooleanQuery.class);
    //multi-token word single-token
    assertQueryEquals(req("q", "hubble space telescope bum MIT", "qt", "aqp"), 
        "+(all:hubble space telescope all:acr::hst) +all:bum +(all:massachusets institute of technology all:acr::mit)", BooleanQuery.class);
    
    
    // synonyms hidden inside other words:
    
    //word one-token stopword one-token word
    assertQueryEquals(req("q", "foo HST at MIT bar", "qt", "aqp"), 
        "+all:foo +(all:hubble space telescope all:acr::hst) +(all:massachusets institute of technology all:acr::mit) +all:bar", BooleanQuery.class);
    //word one-token word one-token word
    assertQueryEquals(req("q", "foo HST bum MIT bar", "qt", "aqp"), 
        "+all:foo +(all:hubble space telescope all:acr::hst) +all:bum +(all:massachusets institute of technology all:acr::mit) +all:bar", BooleanQuery.class);
    //word one-token stopword multi-token word
    assertQueryEquals(req("q", "foo HST at Massachusets Institute of Technology bar", "qt", "aqp"), 
        "+all:foo +(all:hubble space telescope all:acr::hst) +(all:massachusets institute of technology all:acr::mit) +all:bar", BooleanQuery.class);
    //word one-token word multi-token word
    assertQueryEquals(req("q", "foo HST bum Massachusets Institute of Technology bar", "qt", "aqp"), 
        "+all:foo +(all:hubble space telescope all:acr::hst) +all:bum +(all:massachusets institute of technology all:acr::mit) +all:bar", BooleanQuery.class);
    //word multi-token stopword single-token word
    assertQueryEquals(req("q", "foo hubble space telescope at MIT bar", "qt", "aqp"), 
        "+all:foo +(all:hubble space telescope all:acr::hst) +(all:massachusets institute of technology all:acr::mit) +all:bar", BooleanQuery.class);
    //word multi-token word single-token word
    assertQueryEquals(req("q", "foo hubble space telescope bum MIT bar", "qt", "aqp"), 
        "+all:foo +(all:hubble space telescope all:acr::hst) +all:bum +(all:massachusets institute of technology all:acr::mit) +all:bar", BooleanQuery.class);
    
    
    /**
     * WordDelimiterFactory + synonym expansion craziness
     */
    
    /*
     * Example of the CamelCase - because the WordDelimiterFactory is before the synonym filter
     * these token are first split and then matched. HOWEVER, the case is important!!
     * 
     * So, Hubble.Space.Microscope is split into: Hubble, Space, Microscope
     * 
     * Which will be found only if the synonym file contains the same case (OR: if we enable the
     * case insensitive search, which is on my TODO list)
     * 
     */
    assertQueryEquals(req("q", "HubbleSpaceMicroscope bum MIT BX", "qt", "aqp"), 
        "+all:hubblespacemicroscope +all:bum +(all:massachusets institute of technology all:acr::mit) +all:acr::bx", 
        BooleanQuery.class);
    assertQueryEquals(req("q", "Hubble.Space.Microscope -bum MIT BX", "qt", "aqp"), 
        "+(all:hubble space microscope all:acr::hsm) -all:bum +(all:massachusets institute of technology all:acr::mit) +all:acr::bx",
        BooleanQuery.class);

    assertQueryEquals(req("q", "Hubble-Space-Microscope bum MIT BX", "qt", "aqp"), 
        "+(all:hubble space microscope all:acr::hsm) +all:bum +(all:massachusets institute of technology all:acr::mit) +all:acr::bx", 
        BooleanQuery.class);
    
    /*
     * *QUERY* synonym expansion is case sensitive for single tokens,
     * but case-insensitive for multi-tokens (yes, the developer went through some extreme pain ;)))
     */
    assertQueryEquals(req("q", "Hst", "qt", "aqp"), 
        "all:hst", TermQuery.class);

    assertQueryEquals(req("q", "hst", "qt", "aqp"), 
        "all:hst", TermQuery.class);

    assertQueryEquals(req("q", "HST OR Hst", "qt", "aqp"), 
        "(all:hubble space telescope all:acr::hst) all:hst", BooleanQuery.class);


    //TODO: add the corresponding searches, but this shows we are indexing  properly
    //dumpDoc(null, F.ID, F.ADS_TEXT_TYPE);


    // the ascii folding filter emits both unicode and the ascii version
    assertQ(req("q", F.TYPE_ADS_TEXT + ":Bílá"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
    assertQ(req("q", F.TYPE_ADS_TEXT + ":Bila"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
    assertQ(req("q", F.TYPE_ADS_TEXT + ":bila"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
    
    // test that the two lines in the synonym file get merged and produce correct synonym expansion
    assertQueryEquals(req("q", "ABC", "qt", "aqp"), 
        "all:acr::abc all:astrophysics business center all:astrophysics business commons", 
        BooleanQuery.class);
    
    
    // "all-sky" is indexed as "all", "sky", "all-sky"
    // we could achieve higher precision if WDDF generateWordParts=0
    // but that would cause "some-other-hyphenated" tokens to be missed
    assertQ(req("q", F.TYPE_ADS_TEXT + ":no-sky"), "//*[@numFound='3']", 
        "//doc/str[@name='id'][.='10']",
        "//doc/str[@name='id'][.='11']",
        "//doc/str[@name='id'][.='12']");
    assertQ(req("q", F.TYPE_ADS_TEXT + ":nosky"), "//*[@numFound='2']", 
        "//doc/str[@name='id'][.='10']",
        "//doc/str[@name='id'][.='12']");
    assertQ(req("q", F.TYPE_ADS_TEXT + ":all-sky"), "//*[@numFound='3']", 
        "//doc/str[@name='id'][.='10']",
        "//doc/str[@name='id'][.='11']",
        "//doc/str[@name='id'][.='13']");
    assertQ(req("q", F.TYPE_ADS_TEXT + ":allsky"), "//*[@numFound='2']", 
        "//doc/str[@name='id'][.='11']",
        "//doc/str[@name='id'][.='13']");
  }


  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeFulltextParsing.class);
  }
}
