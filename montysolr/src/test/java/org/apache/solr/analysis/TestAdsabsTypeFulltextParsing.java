/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.analysis;


import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;
import org.apache.lucene.search.*;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;

/**
 * Tests that the fulltext is parsed properly, the ads_text type
 * is not as simple as it seems
 *
 * The ads_text has several tasks to do:
 *
 *    1) normalize the input text, ie. token -foo becomes token-foo
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
 *
 *
 * The difficult part with this token type is the presence of synonyms (besides other things)
 * So, for example in the sentence:
 *
 *   Mirrors of the hubble space telescope
 *
 * We must do different things during indexing and querying
 *
 *  indexing: mirrors,hubble|syn::hubble space telescope|syn::hst,space,telescope
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
 *     0: massachusets|syn::mit|syn::massachusets institute of technology
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

public class TestAdsabsTypeFulltextParsing extends MontySolrQueryTestCase {


    @BeforeClass
    public static void beforeClass() throws Exception {

        makeResourcesVisible(Thread.currentThread().getContextClassLoader(),
                MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/server/solr/collection1/conf",
                MontySolrSetup.getSolrHome() + "/example/solr/collection1");

        System.setProperty("solr.directoryFactory", "solr.StandardDirectoryFactory");
        System.setProperty("solr.allow.unsafe.resourceloading", "true");
        schemaString = getSchemaFile();


        configString = getConfigFile();

        initCore(configString, schemaString, MontySolrSetup.getSolrHome() + "/example/solr");
    }

    public static String getConfigFile() {
        String configFile = MontySolrSetup.getMontySolrHome()
                + "/contrib/examples/adsabs/server/solr/collection1/conf/solrconfig.xml";

        File newConfig;
        try {

            newConfig = duplicateFile(new File(configFile));

            replaceInFile(newConfig, "solr.SchemaCodecFactory", "solr.SimpleTextCodecFactory");

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }

        return newConfig.getAbsolutePath();

    }

    public static String getSchemaFile() {

        /*
         * For purposes of the test, we make a copy of the schema.xml,
         * and create our own synonym files
         */

        String configFile = MontySolrSetup.getMontySolrHome()
                + "/contrib/examples/adsabs/server/solr/collection1/conf/schema.xml";

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
                            "pace=> lunar\n" +
                            "mhz, khz, terahertz, hertz, gigahertz, kilohertz, megahertz, hertzian, millihertz, microhz, microhertz, submegahertz, millihz, gigahertzs, microherz => mhz\n" +
                            "survey, surveys, surveyed, surveyor, surveying, durchmusterung, surveyors, resurveyed, resurvey, minisurvey, survery, durchmusterungen, nonsurvey, surveyable, relevamientos, surveyof, serveying, unsurveyable, surfey, servey => survey\n" +
                            "source, sources, multisource, sourcing, sourceless, quellen, souce, subsources, radioquellen, souces, soruce, circumsource, soruces, sourse, sourses, subsource, pseudosource, surces, cources, intersource, sourcers, intrasource, sourcefile, scource, souarce, sourceat => source\n" +
                            "faint, fainter, faintest, faintness, faintly, faintward, faintwards, faintening, fiant => faint\n" +
                            "gamma, gammas, amma, gam, gama, gamm, gammar, gammma, gramma, gammaisation => gamma\n" +
                            "radio, radios, nonradio, radioed, radiobereich, adio, miniradio, radido => radio\n" +
                            "pulsars, pulsar, psr, pulser, psrs, pulsare, pulsares, pulars, pulsary, puslsar, interpulsars, pusar, nonpulsar, psro, rontgenpulsare, pulsarlike, pulsarpsr => pulsars\n" +
                            "millisecond, milliseconds, submillisecond, millisec, milliseconde, millesecond, millisekunden, milliseond, millisecnd => millisecond\n" +
                            "fermi, fermilab => fermi\n",
                    "space => universe\n"
            });

            File multiTokenSynonymsFile = createTempFile("dynamics\0hubble,dyhu\n" +
                    "hubble\0space\0telescope,HST\n" +
                    "Massachusets\0Institute\0of\0Technology, MIT\n" +
                    "Hubble\0Space\0Microscope, HSM\n" +
                    "ABC,Astrophysics\0Business\0Center\n" +
                    "Astrophysics\0Business\0Commons, ABC\n" +
                    "MOND,modified\0newtonian\0dynamics\n" +
                    "bubble\0pace\0telescope,BPT\n" +
                    "GBT,Green\0bank\0telescope\n" +
                    "gamma\0ray,gammaray,gamma\0rays,gammarays\n" +
                    "black\0hole,BH\n" +
                    // this is from ads synonyms
                    "ADS,aitken\0double\0stars\n" +
                    "ADS,astrophysics\0data\0system\n" +
                    "ADS,anti\0de\0sitter\0space,antidesitter\0spacetime,antidesitter\0space\n" +
                    "ADS,astrophysics\0data\0system\n" +
                    "VLBA,very\0long\0baseline\0array\n" +
                    "space,universe"

                    // and this is how it would be if it was one line
                    //"ADS,aitken\0double\0stars,astrophysics\0data\0system,anti\0de\0sitter\0space,antidesitter\0spacetime\n"
            );

            replaceInFile(newConfig, "synonyms=\"ads_text_multi.synonyms\"", "synonyms=\"" + multiTokenSynonymsFile.getAbsolutePath() + "\"");
            replaceInFile(newConfig, "synonyms=\"ads_text_simple.synonyms\"", "synonyms=\"" + simpleTokenSynonymsFile.getAbsolutePath() + "\"");

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }

        return newConfig.getAbsolutePath();
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();
//  	assertU(adoc("id", "603", "bibcode", "xxxxxxxxxx603",
//        "title", "THE HUBBLE constant: a summary of the Hubble Space Telescope program"));
//  	assertU(adoc("id", "605", "bibcode", "xxxxxxxxxx604",
//        "title", "MIT and anti de sitter space-time"));
        assertU(adoc("id", "11", "bibcode", "xxxxxxxxxxx11", "title", "All-sky"));

        assertU(adoc("id", "1", "bibcode", "xxxxxxxxxxxx1", "title", "Bílá kobyla skočila přes čtyřista"));
        assertU(adoc("id", "2", "bibcode", "xxxxxxxxxxxx2", "title", "třicet-tři stříbrných střech"));
        assertU(adoc("id", "3", "bibcode", "xxxxxxxxxxxx3", "title", "A ještě TřistaTřicetTři stříbrných křepeliček"));
        assertU(adoc("id", "4", "bibcode", "xxxxxxxxxxxx4", "title", "Mirrors of the hubble space telescope goes home"));
        assertU(adoc("id", "5", "bibcode", "xxxxxxxxxxxx5", "title", "Mirrors of the HST second"));
        assertU(adoc("id", "6", "bibcode", "xxxxxxxxxxxx6", "title", "Mirrors of the Hst third"));
        assertU(adoc("id", "7", "bibcode", "xxxxxxxxxxxx7", "title", "Mirrors of the HubbleSpaceTelescope fourth"));
        assertU(adoc("id", "8", "bibcode", "xxxxxxxxxxxx8", "title", "Take Massachusets Institute of Technology (MIT)"));
        assertU(adoc("id", "9", "bibcode", "xxxxxxxxxxxx9", "title", "MIT developed new network protocols"));
        assertU(adoc("id", "10", "bibcode", "xxxxxxxxxxx10", "title", "No-sky data survey"));
        assertU(adoc("id", "11", "bibcode", "xxxxxxxxxxx11", "title", "All-sky data survey"));
        assertU(adoc("id", "12", "bibcode", "xxxxxxxxxxx12", "title", "NoSky data survey"));
        assertU(adoc("id", "13", "bibcode", "xxxxxxxxxxx13", "title", "AllSky data survey"));
        assertU(adoc("id", "14", "bibcode", "xxxxxxxxxxx14", "title", "Modified Newtonian Dynamics: Observational Phenomenology and Relativistic Extensions"));
        assertU(adoc("id", "15", "bibcode", "xxxxxxxxxxx15", "title", "MOND test"));
        assertU(adoc("id", "16", "bibcode", "xxxxxxxxxxx16", "title", "mond test"));
        assertU(adoc("id", "17", "bibcode", "xxxxxxxxxxx17", "title", "bubble pace telescope multi-pace foobar"));
        assertU(adoc("id", "18", "bibcode", "xxxxxxxxxxx18", "title", "Mirrors of the Hubble fooox Space Telescope"));
        assertU(adoc("id", "19", "bibcode", "xxxxxxxxxxx19", "title", "BPT MIT"));
        assertU(adoc("id", "20", "bibcode", "xxxxxxxxxxx20", "title", "bubble pace telescope multi-foo"));
        assertU(adoc("id", "21", "bibcode", "xxxxxxxxxxx21", "title", "BPT multi-foo"));
        assertU(adoc("id", "21", "bibcode", "xxxxxxxxxxx21", "title", "multi-foo"));

        assertU(adoc("id", "147", "bibcode", "xxxxxxxxxx147", "title", "NAG5-ABCD"));
        assertU(adoc("id", "148", "bibcode", "xxxxxxxxxx148", "title", "NAG5ABCD"));
        assertU(adoc("id", "149", "bibcode", "xxxxxxxxxx149", "title", "NAG5 ABCD"));
        assertU(adoc("id", "150", "bibcode", "xxxxxxxxxx150", "title", "nag5-abcd"));
        assertU(adoc("id", "151", "bibcode", "xxxxxxxxxx151", "title", "nag5abcd"));
        assertU(adoc("id", "152", "bibcode", "xxxxxxxxxx152", "title", "nag5 abcd"));
        assertU(adoc("id", "153", "bibcode", "xxxxxxxxxx153", "title", "NGC 1"));
        assertU(adoc("id", "154", "bibcode", "xxxxxxxxxx154", "title", "NGC-1"));
        assertU(adoc("id", "155", "bibcode", "xxxxxxxxxx155", "title", "N-1"));
        assertU(adoc("id", "156", "bibcode", "xxxxxxxxxx156", "title", "N 1"));
        assertU(adoc("id", "157", "bibcode", "xxxxxxxxxx157", "title", "NGC1"));

        assertU(adoc("id", "318", "bibcode", "xxxxxxxxxx318", "title", "creation of a thesaurus", "pub", "creation of a thesaurus"));
        assertU(adoc("id", "382", "bibcode", "xxxxxxxxxx382", "title", "xhtml <tags> should be <SUB>fooxx</SUB> <xremoved>"));

        // greek letter should not be a problem, #604
        assertU(adoc("id", "400", "bibcode", "xxxxxxxxxx400", "title", "A 350-MHz GBT Survey of 50 Faint Fermi $\\gamma$-ray Sources for Radio Millisecond Pulsars"));
        assertU(adoc("id", "401", "bibcode", "xxxxxxxxxx401", "title", "A 350-MHz GBT Survey of 50 Faint Fermi γ-ray Sources for Radio Millisecond Pulsars"));
        assertU(adoc("id", "402", "bibcode", "xxxxxxxxxx402", "title", "A 350-MHz GBT Survey of 50 Faint Fermi $\\gamma$ ray Sources for Radio Millisecond Pulsars"));
        assertU(adoc("id", "403", "bibcode", "xxxxxxxxxx403", "title", "A 350-MHz GBT Survey of 50 Faint Fermi γ ray Sources for Radio Millisecond Pulsars"));

        assertU(adoc("id", "500", "bibcode", "xxxxxxxxxx500", "title", "Observations of a BH event horizon",
                "keyword", "one ADS two"));
        assertU(adoc("id", "501", "bibcode", "xxxxxxxxxx501", "title", "Observations of a black hole event horizon",
                "keyword", "one Astrophysics Data System two"));
        assertU(adoc("id", "502", "bibcode", "xxxxxxxxxx502",
                "keyword", "one ads two"));
        assertU(adoc("id", "600", "bibcode", "xxxxxxxxxx600",
                "title", "THE HUBBLE constant: A SUMMARY OF THE HST PROGRAM FOR THE LUMINOSITY CALIBRATION OF TYPE Ia SUPERNOVAE BY MEANS OF CEPHEIDS"));
        assertU(adoc("id", "601", "bibcode", "xxxxxxxxxx601",
                "title", "the hubble constant: a summary of the HST program for the luminosity calibration of type Ia supernovae by means of cepheids"));

        assertU(adoc("id", "602", "bibcode", "xxxxxxxxxx602",
                "title", "Very Long Baseline Array (VLBA) is a ten-antennaaaah"));
        assertU(adoc("id", "603", "bibcode", "xxxxxxxxxx603",
                "title", "THE HUBBLE constant: a summary of the hubble space telescope program"));
        assertU(adoc("id", "604", "bibcode", "xxxxxxxxxx604",
                "title", "MIT and antidesitter space-time"));
        assertU(adoc("id", "605", "bibcode", "xxxxxxxxxx604",
                "title", "MIT and anti de sitter space-time"));
        assertU(adoc("id", "606", "bibcode", "xxxxxxxxxx604",
                "title", "Massachusets Institute of Technology and antidesitter space-time"));

        assertU(commit());
    }


    public void testMultiTokens() throws Exception {

        // this multitoken is onthe first position
        assertQ(req("q", "title:\"very long baseline array\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='602']"
        );

        // make sure the correct synonym is picked in absence of docfreq info
        assertQueryEquals(req("q", "title:(\"antidesitter spacetime\" application)",
                        "aqp.multiphrase.keep_one", "SYNONYM",
                        "aqp.multiphrase.fields", "title"),
                "+(title:\"antidesitter spacetime\" | Synonym(title:syn::antidesitter spacetime)) +title:application",
                BooleanQuery.class);

        // now add some docfreq
        assertU(adoc("id", "1000", "bibcode", "xxxxxxxxxx1000",
                "title", "antidesitter spacetime application"));
        assertU(adoc("id", "1001", "bibcode", "xxxxxxxxxx1001",
                "title", "anti de sitter space application"));
        assertU(adoc("id", "1002", "bibcode", "xxxxxxxxxx1002",
                "title", "NASA ADS"));
        assertU(commit());

        assertQueryEquals(req("q", "title:(\"antidesitter spacetime\" application)",
                        "aqp.multiphrase.keep_one", "SYNONYM",
                        "aqp.multiphrase.fields", "title"),
                "+(title:\"antidesitter spacetime\" | Synonym(title:syn::antidesitter spacetime)) +title:application",
                BooleanQuery.class);


        // for relevancy scoring we want to avoid double-counting
        // so all of these below will use new aqp.multiphrase.keep parameter
    
    /*
      Given the input document that contains:
        ```observations of a BH event horizon```
       
        Presumably we want this to be found when somebody searches for the following:
        1. "black hole"
        2. "observations black hole"
        3. "black hole event"
        4. "observations black hole event"
        Do we also need to get the hit of somebody searches for:
        1. "hole event"
        2. "observation black"
        My feeling is no, i.e. we don't need to allow for partial phrase matching generated by acronym expansions.  We should also consider the reverse case, where the text is:
        ```observations of a black hole event horizon```
        And we want to match:
        1. BH
        2. "BH event"
        3. "observations BH"
        4. "observations BH event"
     */
        //setDebug(true);
        assertQueryEquals(req("q", "title:\"black hole\"",
                        "aqp.multiphrase.keep_one", "SYNONYM",
                        "aqp.multiphrase.fields", "title"),
                "(title:\"black hole\" | Synonym(title:syn::black hole))",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "title:\"black hole\"",
                        "aqp.multiphrase.keep_one", "SYNONYM",
                        "aqp.multiphrase.fields", "title"
                ),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='500']",
                "//doc/str[@name='id'][.='501']"
        );

        assertQueryEquals(req("q", "title:\"observations black hole\"",
                        "aqp.multiphrase.keep_one", "SYNONYM",
                        "aqp.multiphrase.fields", "title"
                ),
                "(title:\"observations black hole\" | title:\"observations syn::black hole\"~2)",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "title:\"observations black hole\"",
                        "aqp.multiphrase.keep_one", "SYNONYM",
                        "aqp.multiphrase.fields", "title"
                ),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='500']",
                "//doc/str[@name='id'][.='501']"
        );

        // default (just to show the difference in treatment)
        assertQueryEquals(req("q", "title:\"observations black hole\""),
                "(title:\"observations black hole\" | title:\"observations (syn::black hole syn::bh acr::bh)\"~2)",
                DisjunctionMaxQuery.class);
        assertQueryEquals(req("q", "title:\"observations BH\""),
                "title:\"observations (acr::bh syn::black hole syn::bh)\"~2",
                MultiPhraseQuery.class);

        // btw our analyzer chain outputs all multi synonyms during indexing (but only canonical synonym for single ones)
        // that makes sense because we don't know how the user/author are writing them; but because we output all of them
        // if we pick just one at the query time, we are still good (it's going to be there - and their frequencies are
        // essentially identical) - all is good...almost, because 'BH event' won't get matched
        // because it is index (in doc 501) next to each other; and we want them exactly next to each other
        // TODO: has to add slope (with slope~2 the query below works)

        assertQueryEquals(req("q", "title:\"black hole event\"",
                        "aqp.multiphrase.keep_one", "SYNONYM"),
                "(title:\"black hole event\" | title:\"syn::black hole ? event\"~2)",
                DisjunctionMaxQuery.class);

        assertQ(req("q", "title:\"black hole event\"", "aqp.multiphrase.keep_one", "SYNONYM"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='500']",
                "//doc/str[@name='id'][.='501']"
        );

        assertQueryEquals(req("q", "title:\"observations black hole event\"",
                        "aqp.multiphrase.keep_one", "SYNONYM"),
                "(title:\"observations black hole event\" | title:\"observations syn::black hole ? event\"~2)",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "title:\"black hole event\"", "aqp.multiphrase.keep_one", "SYNONYM"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='500']",
                "//doc/str[@name='id'][.='501']"
        );

        assertQ(req("q", "title:\"observations black hole event\"", "aqp.multiphrase.keep_one", "SYNONYM"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='500']",
                "//doc/str[@name='id'][.='501']"
        );


        assertQueryEquals(req("q", "title:\"BH\"",
                        "aqp.multiphrase.keep_one", "SYNONYM"),
                "Synonym(title:acr::bh title:syn::bh title:syn::black hole)",
                SynonymQuery.class);
        assertQ(req("q", "title:\"BH\"", "aqp.multiphrase.keep_one", "SYNONYM"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='500']",
                "//doc/str[@name='id'][.='501']"
        );

        assertQueryEquals(req("q", "title:\"observations BH\"",
                        "aqp.multiphrase.keep_one", "SYNONYM"),
                "title:\"observations syn::bh\"~2",
                MultiPhraseQuery.class);
        assertQ(req("q", "title:\"observations BH\"", "aqp.multiphrase.keep_one", "SYNONYM"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='500']",
                "//doc/str[@name='id'][.='501']"
        );

        assertQueryEquals(req("q", "title:\"BH event\""),
                "title:\"(acr::bh syn::black hole syn::bh) event\"~2",
                MultiPhraseQuery.class);
        assertQ(req("q", "title:\"BH event\"", "aqp.multiphrase.keep_one", "SYNONYM"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='500']",
                "//doc/str[@name='id'][.='501']"
        );

        assertQueryEquals(req("q", "title:\"observations BH event\"",
                        "aqp.multiphrase.keep_one", "SYNONYM"),
                "title:\"observations syn::bh event\"~2",
                MultiPhraseQuery.class);
        assertQ(req("q", "title:\"BH\"", "aqp.multiphrase.keep_one", "SYNONYM"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='500']",
                "//doc/str[@name='id'][.='501']"
        );
    
    /*
     Do we also need to get the hit of somebody searches for:
      1. "hole event"
      2. "observation black"
      
      AA thinks not, RCA thinks 'geese, i hope he wasn't thinking to match part of a synonym!'
     */
        assertQueryEquals(req("q", "title:\"hole event\"",
                        "aqp.multiphrase.keep_one", "SYNONYM"),
                "title:\"hole event\"",
                PhraseQuery.class);
        assertQ(req("q", "title:\"hole event\"", "aqp.multiphrase.keep_one", "SYNONYM"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='501']"
        );
        assertQueryEquals(req("q", "title:\"observation black\"",
                        "aqp.multiphrase.keep_one", "SYNONYM"),
                "title:\"observation black\"",
                PhraseQuery.class);
        assertQ(req("q", "title:\"observations black\"", "aqp.multiphrase.keep_one", "SYNONYM"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='501']"
        );


        // default behaviour, all synonyms in multi-phrase query
//    assertQueryEquals(req("q", "title:\"bubble pace telescope multi-pace foobar\"", "defType", "aqp"), 
//        "(title:\"bubble (pace syn::lunar) telescope multi (pace syn::lunar) foobar\"~3 "
//        + "| title:\"bubble (pace syn::lunar) telescope ? multipace foobar\"~3 "
//        + "| title:\"(syn::bubble pace telescope syn::bpt acr::bpt) ? ? multi (pace syn::lunar) foobar\"~3 "
//        + "| title:\"(syn::bubble pace telescope syn::bpt acr::bpt) ? ? ? multipace foobar\"~3)",
//        DisjunctionMaxQuery.class);
        assertQ(req("q", "title" + ":\"bubble pace telescope multi-pace foobar\""), "//*[@numFound='1']",
                "//doc/str[@name='id'][.='17']");

        //assertQueryEquals(req("q", "\"NASA grant\"~3 NEAR N*", "defType", "aqp", "qf", "author^1.5 title^1.4 abstract^1.3 all"),
        //    "(((spanNear([abstract:acr::nag5, abstract:5269], 5, true) abstract:acr::nag55269)^1.3) | ((author:nag5 5269, author:nag5 5269, * author:nag5 5 author:nag5 5 * author:nag5)^1.5) | ((spanNear([title:acr::nag5, title:5269], 5, true) title:acr::nag55269)^1.4) | (spanNear([all:acr::nag5, all:5269], 5, true) all:acr::nag55269))",
        //    DisjunctionMaxQuery.class);

        // UPPER-CASE vs lower-case
        assertQueryEquals(req("q", "NAG5-ABCD", "defType", "aqp", "df", "title", "fl", "id,title"),
                "(title:acr::nag5abcd | (+title:acr::nag5 +title:acr::abcd))",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "NAG5-ABCD", "df", "title"),
                "//*[@numFound='3']",
                "//doc/str[@name='id'][.='147']",
                "//doc/str[@name='id'][.='148']",
                "//doc/str[@name='id'][.='149']"
        );
        assertQueryEquals(req("q", "nag5-abcd", "defType", "aqp", "df", "title"),
                "(title:nag5abcd | (+title:nag5 +title:abcd))",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "nag5-abcd", "df", "title"),
                "//*[@numFound='6']",
                "//doc/str[@name='id'][.='147']",
                "//doc/str[@name='id'][.='148']",
                "//doc/str[@name='id'][.='149']",
                "//doc/str[@name='id'][.='150']",
                "//doc/str[@name='id'][.='151']",
                "//doc/str[@name='id'][.='152']"
        );

        // ticket #318
        assertQueryEquals(req("q", "creation of a thesaurus", "defType", "aqp", "qf", "all title^1.4 pub"),
                "+(all:creation | pub:creation | (title:creation)^1.4) +pub:of +pub:a +(all:thesaurus | pub:thesaurus | (title:thesaurus)^1.4)",
                BooleanQuery.class);
        assertQ(req("q", "pub:of AND pub:a"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='318']"
        );
        assertQ(req("q", "creation of a thesaurus", "defType", "aqp", "qf", "title^1.4 all pub"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='318']"
        );


        // ticket #320
        // in natural language: when searching for MOND, we'll first find the multi-token synonyms
        // ie. MOND, modified newtonina dynamics
        // then search for simple synonymes: <find nothing, ie. ignore 'mond'>
        // MOND is caught by acronym filter, which is configured to eat the original
        // and the result is made of acronym + synonym + multi-token-synonym

        // test with a field
        assertQueryEquals(req("q", "title:MOND", "defType", "aqp"),
                "Synonym(title:acr::mond title:syn::modified newtonian dynamics title:syn::mond)",
                SynonymQuery.class);
        assertQueryEquals(req("q", "title:mond", "defType", "aqp"),
                "Synonym(title:mond title:syn::lunar)", SynonymQuery.class);
        assertQueryEquals(req("q", "title:Mond", "defType", "aqp"),
                "Synonym(title:mond title:syn::lunar)", SynonymQuery.class);

        // unfielded simple token
        assertQueryEquals(req("q", "MOND", "defType", "aqp"),
                "Synonym(all:acr::mond all:syn::modified newtonian dynamics all:syn::mond)",
                SynonymQuery.class);
        assertQ(req("q", "title" + ":MOND"), "//*[@numFound='2']",
                "//doc/str[@name='id'][.='14']",
                "//doc/str[@name='id'][.='15']");

        assertQueryEquals(req("q", "mond", "defType", "aqp"),
                "Synonym(all:mond all:syn::lunar)",
                SynonymQuery.class);
        assertQ(req("q", "title" + ":mond"),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='15']",
                "//doc/str[@name='id'][.='16']",
                "//doc/str[@name='id'][.='17']",
                "//doc/str[@name='id'][.='20']");

        assertQueryEquals(req("q", "Mond", "defType", "aqp"),
                "Synonym(all:mond all:syn::lunar)",
                SynonymQuery.class);
        assertQ(req("q", "title" + ":Mond"),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='17']", // orig 'space' -> syn:lunar; look at the synonym file to understand
                "//doc/str[@name='id'][.='15']",
                "//doc/str[@name='id'][.='16']",
                "//doc/str[@name='id'][.='20']");

        // search for 'pace' and find 'mond' (there is intentional error/duplication
        // in our synonym files - look above)
        assertQueryEquals(req("q", "title:pace", "defType", "aqp"),
                "Synonym(title:pace title:syn::lunar)",
                SynonymQuery.class);
        assertQ(req("q", "title" + ":pace"),
                "//*[@numFound='3']",
                "//doc/str[@name='id'][.='17']",
                "//doc/str[@name='id'][.='16']",
                "//doc/str[@name='id'][.='20']");

        // search for 'lunar' MUST NOT return 'mond' (because synonyms are explicit =>)
        // and 'lunar' is not on the left hand side
        assertQueryEquals(req("q", "title:lunar", "defType", "aqp"),
                "title:lunar",
                TermQuery.class);
        assertQ(req("q", "title" + ":lunar"), "//*[@numFound='0']");

        // but 'luna' is a synonym (syn::lunar)
        assertQueryEquals(req("q", "title:luna", "defType", "aqp"),
                "Synonym(title:luna title:syn::lunar)",
                SynonymQuery.class);
        assertQ(req("q", "title" + ":luna"),
                "//*[@numFound='3']",
                "//doc/str[@name='id'][.='17']",
                "//doc/str[@name='id'][.='16']",
                "//doc/str[@name='id'][.='20']");


        // now the multi-token version

        assertQueryEquals(req("q", "title:\"modified newtonian dynamics\"", "defType", "aqp"),
                "(title:\"modified newtonian dynamics\" "
                        + "| Synonym(title:acr::mond title:syn::modified newtonian dynamics title:syn::mond))",
                DisjunctionMaxQuery.class);
        assertQueryEquals(req("q", "title:\"MOND\"", "defType", "aqp"),
                "Synonym(title:acr::mond title:syn::modified newtonian dynamics title:syn::mond)",
                SynonymQuery.class);
        assertQ(req("q", "title" + ":\"modified newtonian dynamics\""), "//*[@numFound='2']",
                "//doc/str[@name='id'][.='14']",
                "//doc/str[@name='id'][.='15']");
        assertQ(req("q", "title" + ":\"MOND\""), "//*[@numFound='2']",
                "//doc/str[@name='id'][.='14']",
                "//doc/str[@name='id'][.='15']");


        // multi-token. this is truly crazy (several synonyms overlap)
        // 'bubble pace telescope' is a synonym
        // 'pace' is a synonym
        // multi-pace is split by WDFF and expanded with a synonym
        assertQueryEquals(req("q", "title:\"bubble pace telescope multi-pace foobar\"", "defType", "aqp"),
                "(title:\"bubble (pace syn::lunar) telescope multipace ? foobar\"~3 | title:\"bubble (pace syn::lunar) telescope multi (pace syn::lunar) foobar\"~3 | title:\"(syn::bubble pace telescope syn::bpt acr::bpt) ? ? multipace ? foobar\"~3 | title:\"(syn::bubble pace telescope syn::bpt acr::bpt) ? ? multi (pace syn::lunar) foobar\"~3)",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "title" + ":\"bubble pace telescope multi-pace foobar\""), "//*[@numFound='1']",
                "//doc/str[@name='id'][.='17']");


        // now the same thing, but not using phrases
        assertQueryEquals(req("q", "title:modified\\ newtonian\\ dynamics", "defType", "aqp"),
                "((+title:modified +title:newtonian +title:dynamics) | Synonym(title:acr::mond title:syn::modified newtonian dynamics title:syn::mond))",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "title" + ":modified\\ newtonian\\ dynamics"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='14']",
                "//doc/str[@name='id'][.='15']");


        // and even unfielded!
        assertQueryEquals(req("q", "modified\\ newtonian\\ dynamics", "defType", "aqp", "df", "title"),
                "((+title:modified +title:newtonian +title:dynamics) | Synonym(title:acr::mond title:syn::modified newtonian dynamics title:syn::mond))",
                DisjunctionMaxQuery.class);

        assertQ(req("q", "modified\\ newtonian\\ dynamics", "defType", "aqp", "df", "title"), "//*[@numFound='2']",
                "//doc/str[@name='id'][.='14']",
                "//doc/str[@name='id'][.='15']");


        // lastly - unfielded phrase
        assertQueryEquals(req("q", "\"modified newtonian dynamics\"", "defType", "aqp", "qf", "title^2.0 all^1.5"),
                "(((all:\"modified newtonian dynamics\" | Synonym(all:acr::mond all:syn::modified newtonian dynamics all:syn::mond)))^1.5 | ((title:\"modified newtonian dynamics\" | Synonym(title:acr::mond title:syn::modified newtonian dynamics title:syn::mond)))^2.0)",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "\"modified newtonian dynamics\"", "qf", "title^2.0 all^1.5"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='14']",
                "//doc/str[@name='id'][.='15']");


        // test of the multi-synonym replacement, phrase handling etc
        //dumpDoc(null, "title", "recid");
        assertQueryEquals(req("q", "title:\"bubble pace telescope multi-foo\"", "defType", "aqp", "df", "title"),
                "(title:\"bubble (pace syn::lunar) telescope multifoo\"~3 | title:\"bubble (pace syn::lunar) telescope multi foo\"~3 | title:\"(syn::bubble pace telescope syn::bpt acr::bpt) ? ? multifoo\"~3 | title:\"(syn::bubble pace telescope syn::bpt acr::bpt) ? ? multi foo\"~3)",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "title:\"bubble pace telescope multi-foo\"", "defType", "aqp", "df", "title"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='20']"
        );

        // wow! this works correctly
        assertQueryEquals(req("q", "bubble\\ pace\\ telescope\\ and\\ MIT", "defType", "aqp", "df", "title"),
                "((+title:bubble +Synonym(title:pace title:syn::lunar) +title:telescope +Synonym(title:acr::mit title:syn::massachusets institute of technology title:syn::mit)) | (+Synonym(title:acr::bpt title:syn::bpt title:syn::bubble pace telescope) +Synonym(title:acr::mit title:syn::massachusets institute of technology title:syn::mit)))",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "bubble\\ pace\\ telescope\\ and\\ MIT", "defType", "aqp", "df", "title"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='19']"
        );

    }

    public void unfieldedSearch() throws Exception {
        // non-phrase: by default do span search
        //setDebug(true);
        assertQueryEquals(req("q", "hubble space telescope", "defType", "aqp",
                        "aqp.unfielded.tokens.strategy", "join",
                        "df", "all"),
                "all:hubble all:syn::hubble space telescope all:acr::hst all:space all:telescope",
                BooleanQuery.class);
        assertQ(req("q", "hubble space telescope"),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='4']",
                "//doc/str[@name='id'][.='5']",
                "//doc/str[@name='id'][.='17']", // to go away after #147
                "//doc/str[@name='id'][.='18']"
        );

        // make sure the unfielded search is expanded properly (by edismax) - we use it just here
        // HOWEVER: maybe it should do expansion inside each clause? now it favors docs with matches in all fields (which is fine)
        assertQueryEquals(req("q", "hubble space telescope", "defType", "aqp", "qf", "title^2.0 keyword^1.5"),
                "(((title:hubble title:syn::hubble space telescope title:acr::hst title:space title:telescope)^2.0) " +
                        "| ((keyword:hubble keyword:space keyword:telescope)^1.5))",
                DisjunctionMaxQuery.class);

        assertQueryEquals(req("q", "title:(hubble space telescope goes home)", "defType", "aqp", "fl", "recid,title"),
                //"spanNear([all:hubble, all:space, all:telescope, all:goes, all:home], 5, true) spanNear([spanOr([all:syn::hubble space telescope, all:acr::hst]), all:goes, all:home], 5, true)",
                "(+title:hubble +title:space +title:telescope +title:goes +title:home) (+(title:syn::hubble space telescope title:acr::hst) +title:goes +title:home)",
                BooleanQuery.class);
        assertQ(req("q", "title:(hubble space telescope goes home)"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='4']"
        );

        // surrounded by stop words
        assertQueryEquals(req("q", "title:(mirrors of the hubble space telescope the goes home)", "defType", "aqp"),
                //"spanNear([all:mirrors, all:hubble, all:space, all:telescope, all:goes, all:home], 5, true)" +
                //" spanNear([all:mirrors, spanOr([all:syn::hubble space telescope, all:acr::hst]), all:goes, all:home], 5, true)",
                "(+title:mirrors +title:hubble +title:space +title:telescope +title:goes +title:home) " +
                        "(+title:mirrors +(title:syn::hubble space telescope title:acr::hst) +title:goes +title:home)",
                BooleanQuery.class);
        assertQ(req("q", "title:(mirrors of the hubble space telescope goes home)"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='4']"
        );

        // surrounded - change default operator (many matches)
        // TODO: #147
        assertQueryEquals(req("q", "title:(mirrors of the hubble space telescope start home)", "defType", "aqp", "q.op", "OR"),
                "(title:mirrors title:hubble title:space title:telescope title:start title:home) " +
                        "(title:mirrors (title:syn::hubble space telescope title:acr::hst) title:start title:home)",
                BooleanQuery.class);
        assertQ(req("q", "title:(mirrors of the hubble space telescope start home)", "q.op", "OR"),
                "//*[@numFound='6']",
                "//doc[1]/str[@name='id'][.='4']", // this one is the best match
                "//doc/str[@name='id'][.='18']",
                "//doc/str[@name='id'][.='5']",
                "//doc/str[@name='id'][.='6']",
                "//doc/str[@name='id'][.='7']",
                "//doc/str[@name='id'][.='17']"
        );

        assertQueryEquals(req("q", "title:(mirrors of the hubble space telescope goes home)", "defType", "aqp"),
                //"spanNear([all:mirrors, all:hubble, all:space, all:telescope, all:goes, all:home], 5, true)" +
                //" spanNear([all:mirrors, spanOr([all:syn::hubble space telescope, all:acr::hst]), all:goes, all:home], 5, true)",
                "(+title:mirrors +title:hubble +title:space +title:telescope +title:goes +title:home) " +
                        "(+title:mirrors +(title:syn::hubble space telescope title:acr::hst) +title:goes +title:home)",
                BooleanQuery.class);
        assertQ(req("q", "title:(mirrors of the hubble space telescope goes home)"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='4']"
        );

        // different modifier (synonym must not be found)
        assertQueryEquals(req("q", "hubble space -telescope", "defType", "aqp"),
                "+(all:hubble all:space) -all:telescope", BooleanQuery.class);

        // different field
        assertQueryEquals(req("q", "hubble space title:telescope", "defType", "aqp"),
                "+(all:hubble all:space) +title:telescope", BooleanQuery.class);

        assertQueryEquals(req("q", "hubble space telescope +star", "defType", "aqp"),
                //"+(all:hubble space telescope all:acr::hst) +all:star",
                "+(all:hubble all:syn::hubble space telescope all:acr::hst all:space all:telescope) +all:star",
                BooleanQuery.class);


    }


    public void testNoSynChain() throws Exception {


        // simple case: synonyms deactivated
        assertQueryEquals(req("q", "=title:\"Hubble Space Telescope\"", "defType", "aqp"),
                "title:\"hubble space telescope\"",
                PhraseQuery.class);
        assertQ(req("q", "=title:\"Hubble Space Telescope\""),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='4']"
        );
        //setDebug(true);
        assertQueryEquals(req("q", "=\"Hubble Space Telescope\"", "defType", "aqp", "qf", "body title"),
                "(body:\"hubble space telescope\" | title:\"hubble space telescope\")",
                DisjunctionMaxQuery.class);
    }


    public void testSynonyms() throws Exception {


        /*
         * Test multi-token translation, the chain is set to recognize
         * synonyms. So even if the query string is split into 3 tokens,
         * we are able to join them and find their synonym (HST)
         *
         */

        // simple case
        assertQueryEquals(req("q", "title:\"hubble space telescope\"", "defType", "aqp"),
                "(title:\"hubble (space syn::universe) telescope\"~3 | Synonym(title:acr::hst title:syn::hst title:syn::hubble space telescope))",
                DisjunctionMaxQuery.class);

        assertQ(req("q", "title:\"hubble space telescope\""),
                "//*[@numFound='6']",
                "//doc/str[@name='id'][.='4']",
                "//doc/str[@name='id'][.='5']",
                "//doc/str[@name='id'][.='600']",
                "//doc/str[@name='id'][.='601']"
        );


        // preceded by something
        // TODO: remove 'title:' after #147 is solved
        assertQueryEquals(req("q", "title:\"mirrors of the hubble space telescope\"", "defType", "aqp"),
                "(title:\"mirrors hubble (space syn::universe) telescope\"~3 | title:\"mirrors (syn::hubble space telescope syn::hst acr::hst)\"~3)",
                DisjunctionMaxQuery.class);

        // TODO:rca - this finds too much because of the higher slope; a potential solution
        // is to compare the max branch size and if we discover that it has covered the full
        // length then no slope is needed for this multiphrase; see AqpSlopQueryNodeBuilder
        // for possible implementation; when accepted then the numFound=2 is correct here
        assertQ(req("q", "title:\"mirrors hubble space telescope\"", "defType", "aqp"),
                "//*[@numFound='3']",
                "//doc/str[@name='id'][.='4']",
                "//doc/str[@name='id'][.='5']"
        );
        assertQ(req("q", "title:\"mirrors of the hubble space telescope\""),
                "//*[@numFound='3']",
                "//doc/str[@name='id'][.='4']",
                "//doc/str[@name='id'][.='5']"
        );
        assertQ(req("q", "title:\"mirrors of the hubble space scope\""),
                "//*[@numFound='0']"
        );

        // query followed by something
        assertQueryEquals(req("q", "title:\"hubble space telescope goes home\"", "defType", "aqp"),
                "(title:\"hubble (space syn::universe) telescope goes home\"~3 | title:\"(syn::hubble space telescope syn::hst acr::hst) ? ? goes home\"~3)",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "title:\"hubble space telescope goes home\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='4']"
        );


        // surrounded by something
        assertQueryEquals(req("q", "title:\"mirrors of the hubble space telescope goes home\"", "defType", "aqp"),
                "(title:\"mirrors hubble (space syn::universe) telescope goes home\"~3 | title:\"mirrors (syn::hubble space telescope syn::hst acr::hst) ? ? goes home\"~3)",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "title:\"mirrors of the hubble space telescope goes home\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='4']"
        );





        /*
         * Synonym expansion 1token->many
         */
        assertQueryEquals(req("q", "title:HST", "defType", "aqp"),
                "Synonym(title:acr::hst title:syn::hst title:syn::hubble space telescope)",
                SynonymQuery.class);
        assertQ(req("q", "title:HST"),
                "//*[@numFound='5']",
                "//doc/str[@name='id'][.='4']",
                "//doc/str[@name='id'][.='5']");


        assertQueryEquals(req("q", "HST goes home", "defType", "aqp"),
                "+Synonym(all:acr::hst all:syn::hst all:syn::hubble space telescope) +all:goes +all:home",
                BooleanQuery.class);


        /*
         * many token -> 1
         */

        assertQueryEquals(req("q", "\"Massachusets Institute of Technology\"", "defType", "aqp"),
                "Synonym(all:acr::mit all:syn::massachusets institute of technology all:syn::mit)",
                SynonymQuery.class);
        assertQueryEquals(req("q", "\"massachusets institute of technology\"", "defType", "aqp"),
                "Synonym(all:acr::mit all:syn::massachusets institute of technology all:syn::mit)",
                SynonymQuery.class);

        //TODO: this doesn't work because stop filter is at the end of the chain, move it up?
        //    assertQueryEquals(req("q", "\"Massachusets Institute of the Technology\"", "defType", "aqp"),
        //    		"(all:syn::massachusets institute of technology all:acr::mit)",
        //    		BooleanQuery.class);
        //    assertQueryEquals(req("q", "\"Massachusets Institute Technology\"", "defType", "aqp"),
        //    		"(all:syn::massachusets institute of technology all:acr::mit)",
        //        BooleanQuery.class);


        /*
         * Case (In)Sensitivity
         *
         * It shoulb be ase sensitive for single tokens, and case-insensitive
         * for multi-tokens
         */
        assertQueryEquals(req("q", "hst", "defType", "aqp"),
                "all:hst", TermQuery.class);
        assertQueryEquals(req("q", "HSt", "defType", "aqp"),
                "all:hst", TermQuery.class);

        /*
         * alternation of synonym groups:
         * =============================
         */


        //synonym at extremities (end-end):

        //one-token stopword one-token
        assertQueryEquals(req("q", "HST at MIT", "defType", "aqp"),
                "+Synonym(all:acr::hst all:syn::hst all:syn::hubble space telescope) +Synonym(all:acr::mit all:syn::massachusets institute of technology all:syn::mit)",
                BooleanQuery.class);
        //one-token word one-token
        assertQueryEquals(req("q", "HST bum MIT", "defType", "aqp"),
                "+Synonym(all:acr::hst all:syn::hst all:syn::hubble space telescope) +all:bum +Synonym(all:acr::mit all:syn::massachusets institute of technology all:syn::mit)",
                BooleanQuery.class);
        //one-token stopword multi-token
        assertQueryEquals(req("q", "\"HST at Massachusets Institute of Technology\"", "defType", "aqp"),
                "all:\"(acr::hst syn::hubble space telescope syn::hst) (syn::massachusets institute of technology syn::mit acr::mit)\"~4",
                MultiPhraseQuery.class);
        //one-token word multi-token
        assertQueryEquals(req("q", "\"HST bum Massachusets Institute of Technology\"", "defType", "aqp"),
                "all:\"(acr::hst syn::hubble space telescope syn::hst) bum (syn::massachusets institute of technology syn::mit acr::mit)\"~4",
                MultiPhraseQuery.class);
        //multi-token stopword single-token
        assertQueryEquals(req("q", "\"hubble space telescope at MIT\"", "defType", "aqp"),
                "(all:\"hubble (space syn::universe) telescope (acr::mit syn::massachusets institute of technology syn::mit)\"~4 | all:\"(syn::hubble space telescope syn::hst acr::hst) ? ? (acr::mit syn::massachusets institute of technology syn::mit)\"~4)",
                DisjunctionMaxQuery.class);
        //multi-token word single-token
        assertQueryEquals(req("q", "\"hubble space telescope bum MIT\"", "defType", "aqp"),
                "(all:\"hubble (space syn::universe) telescope bum (acr::mit syn::massachusets institute of technology syn::mit)\"~4 | all:\"(syn::hubble space telescope syn::hst acr::hst) ? ? bum (acr::mit syn::massachusets institute of technology syn::mit)\"~4)",
                DisjunctionMaxQuery.class);


        // synonyms hidden inside other words:
        //word one-token stopword one-token word
        assertQueryEquals(req("q", "\"foo HST at MIT bar\"", "defType", "aqp"),
                //"+all:foo +(all:hubble space telescope all:acr::hst) +(all:massachusets institute of technology all:acr::mit) +all:bar",
                "all:\"foo (acr::hst syn::hubble space telescope syn::hst) (acr::mit syn::massachusets institute of technology syn::mit) bar\"~4",
                MultiPhraseQuery.class);
        //word one-token word one-token word
        assertQueryEquals(req("q", "\"foo HST bum MIT bar\"", "defType", "aqp"),
                "all:\"foo (acr::hst syn::hubble space telescope syn::hst) bum (acr::mit syn::massachusets institute of technology syn::mit) bar\"~4",
                MultiPhraseQuery.class);
        //word one-token stopword multi-token word
        assertQueryEquals(req("q", "\"foo HST at Massachusets Institute of Technology bar\"", "defType", "aqp"),
                "all:\"foo (acr::hst syn::hubble space telescope syn::hst) (syn::massachusets institute of technology syn::mit acr::mit) ? ? bar\"~4",
                MultiPhraseQuery.class);
        //word one-token word multi-token word
        assertQueryEquals(req("q", "\"foo HST bum Massachusets Institute of Technology bar\"", "defType", "aqp"),
                "all:\"foo (acr::hst syn::hubble space telescope syn::hst) bum (syn::massachusets institute of technology syn::mit acr::mit) ? ? bar\"~4",
                MultiPhraseQuery.class);
        //word multi-token stopword single-token word
        assertQueryEquals(req("q", "\"foo hubble space telescope at MIT bar\"", "defType", "aqp"),
                "(all:\"foo hubble (space syn::universe) telescope (acr::mit syn::massachusets institute of technology syn::mit) bar\"~4 | all:\"foo (syn::hubble space telescope syn::hst acr::hst) ? ? (acr::mit syn::massachusets institute of technology syn::mit) bar\"~4)",
                DisjunctionMaxQuery.class);
        //word multi-token word single-token word
        assertQueryEquals(req("q", "\"foo hubble space telescope bum MIT bar\"", "defType", "aqp"),
                "(all:\"foo hubble (space syn::universe) telescope bum (acr::mit syn::massachusets institute of technology syn::mit) bar\"~4 | all:\"foo (syn::hubble space telescope syn::hst acr::hst) ? ? bum (acr::mit syn::massachusets institute of technology syn::mit) bar\"~4)",
                DisjunctionMaxQuery.class);


        /**
         * WordDelimiterFactory + synonym expansion craziness
         */
        /*
         * Example of the CamelCase ignored, but other WordDelimiterFactory matched.
         * Because WDFF is before the synonym filter these token are first split
         * and then matched. HOWEVER, the case is important!!
         *
         * So, Hubble.Space.Microscope is split into: Hubble, Space, Microscope
         *
         * Which will be found only if the synonym file contains the same case (OR: if we enable the
         * case insensitive search, which is on my TODO list)
         *
         */
        assertQueryEquals(req("q", "HubbleSpaceMicroscope bum MIT BX", "defType", "aqp"),
                "+all:hubblespacemicroscope +all:bum +Synonym(all:acr::mit all:syn::massachusets institute of technology all:syn::mit) +all:acr::bx",
                BooleanQuery.class);
        assertQueryEquals(req("q", "Hubble.Space.Microscope -bum MIT BX", "defType", "aqp"),
                "+(Synonym(all:acr::hsm all:hubblespacemicroscope all:syn::hsm all:syn::hubble space microscope) | (+all:hubble +Synonym(all:space all:syn::universe) +all:microscope)) -all:bum +Synonym(all:acr::mit all:syn::massachusets institute of technology all:syn::mit) +all:acr::bx",
                BooleanQuery.class);
        assertQueryEquals(req("q", "Hubble.Space.Microscope -bum MIT BX", "defType", "aqp"),
                "+(Synonym(all:acr::hsm all:hubblespacemicroscope all:syn::hsm all:syn::hubble space microscope) | (+all:hubble +Synonym(all:space all:syn::universe) +all:microscope)) -all:bum +Synonym(all:acr::mit all:syn::massachusets institute of technology all:syn::mit) +all:acr::bx",
                BooleanQuery.class);

        assertQueryEquals(req("q", "Hubble-Space-Microscope bum MIT BX", "defType", "aqp"),
                "+(Synonym(all:acr::hsm all:hubblespacemicroscope all:syn::hsm all:syn::hubble space microscope) | (+all:hubble +Synonym(all:space all:syn::universe) +all:microscope)) +all:bum +Synonym(all:acr::mit all:syn::massachusets institute of technology all:syn::mit) +all:acr::bx",
                BooleanQuery.class);

        /*
         * *QUERY* synonym expansion is case sensitive for single tokens,
         * but case-insensitive for multi-tokens (yes, your developer went through some extreme pain ;)))
         */
        assertQueryEquals(req("q", "Hst", "defType", "aqp"),
                "all:hst", TermQuery.class);

        assertQueryEquals(req("q", "hst", "defType", "aqp"),
                "all:hst", TermQuery.class);

        assertQueryEquals(req("q", "HST OR Hst", "defType", "aqp"),
                "Synonym(all:acr::hst all:syn::hst all:syn::hubble space telescope) all:hst",
                BooleanQuery.class);


        //TODO: add the corresponding searches, but this shows we are indexing  properly
        //dumpDoc(null, "id", "title");
    }

    public void testOtherCases() throws Exception {


        /**
         *
         * input:
         *
         * THE HUBBLE constant: A SUMMARY OF THE HST PROGRAM FOR THE LUMINOSITY CALIBRATION OF TYPE Ia SUPERNOVAE BY MEANS OF CEPHEIDS
         *
         * this is how it gets indexed internally (before moving acronym filter after stop filters):
         *
         * [(0, ['acr::the']),
         (1, ['acr::hubble', 'hubble']),
         (2, ['constant']),
         (3, ['acr::of', 'acr::summary', 'acr::the', 'summary']),
         (4, ['acr::hst', 'hst', 'syn::hst', 'syn::hubble space telescope']),
         (5, ['acr::for', 'acr::program', 'acr::the', 'program']),
         (6, ['acr::luminosity', 'luminosity']),
         (7, ['acr::calibration', 'acr::of', 'calibration']),
         (8, ['acr::type', 'type']),
         (9, ['ia']),
         (10, ['acr::supernovae', 'supernovae']),
         (11, ['acr::by', 'by']),
         (12, ['acr::means', 'acr::of', 'means']),
         (13, ['acr::cepheids', 'cepheids']),
         (14, []),
         (15, []),
         (16, []),
         (17, []),
         (18, []),
         (19, [])]

         with stop filters before acronyms

         [(0, []),
         (1, ['acr::hubble']),
         (2, ['constant']),
         (3, ['acr::summary', 'summary']),
         (4, []),
         (5, ['acr::program', 'program']),
         (6, ['acr::luminosity', 'luminosity']),
         (7, ['acr::calibration', 'calibration']),
         (8, ['acr::type', 'type']),
         (9, ['ia']),
         (10, ['acr::supernovae', 'supernovae']),
         (11, ['acr::by', 'by']),
         (12, ['acr::means', 'means']),
         (13, ['acr::cepheids', 'cepheids']),

         and this how it got parsed before the change:

         title:"acr::the acr::hubble constant acr::summary acr::of acr::the (acr::hst syn::hubble space telescope syn::hst) acr::program acr::for acr::the acr::luminosity acr::calibration acr::of acr::type ia acr::supernovae acr::by acr::means acr::of acr::cepheids"~3

         [(0, 'acr::the'),
         (1, 'acr::hubble'),
         (2, 'constant'),
         (3, 'acr::summary'),
         (4, 'acr::of'),
         (5, 'acr::the'),
         (6, '(acr::hst syn::hubble space telescope syn::hst)'),
         (7, 'acr::program'),
         (8, 'acr::for'),
         (9, 'acr::the'),
         (10, 'acr::luminosity'),
         (11, 'acr::calibration'),
         (12, 'acr::of'),
         (13, 'acr::type'),
         (14, 'ia'),
         (15, 'acr::supernovae'),
         (16, 'acr::by'),
         (17, 'acr::means'),
         (18, 'acr::of'),
         (19, 'acr::cepheids')]


         */
        assertQueryEquals(req("q", "title:\"THE HUBBLE constant: A SUMMARY OF THE HST PROGRAM FOR THE LUMINOSITY CALIBRATION OF TYPE Ia SUPERNOVAE BY MEANS OF CEPHEIDS\""),
                "title:\"acr::hubble constant acr::summary (acr::hst syn::hubble space telescope syn::hst) acr::program acr::luminosity acr::calibration acr::type ia acr::supernovae acr::by acr::means acr::cepheids\"~3",
                MultiPhraseQuery.class);
        assertQ(req("q", "title:\"THE HUBBLE constant: A SUMMARY OF THE HST PROGRAM FOR THE LUMINOSITY CALIBRATION OF TYPE Ia SUPERNOVAE BY MEANS OF CEPHEIDS\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='600']");
        assertQ(req("q", "title:\"the hubble constant: a summary of the HST program for the luminosity calibration of type Ia supernovae by means of cepheids\""),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='600']",
                "//doc/str[@name='id'][.='601']");

        // change to NGC tokenizer in the schema; we want to index both
        // variants, but during search time only query for the concat version

        assertQ(req("q", "title" + ":NGC"),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='153']", //NGC 1
                "//doc/str[@name='id'][.='154']", //NGC-1
                "//doc/str[@name='id'][.='155']", //N-1
                "//doc/str[@name='id'][.='156']"  //N 1
                //"//doc/str[@name='id'][.='157']" //NGC1
        );

        assertQueryEquals(req("q", "title:\"NGC 1\"", "defType", "aqp"),
                "(title:acr::ngc1 | title:\"acr::ngc 1\")",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "title" + ":NGC 1", "indent", "true"),
                "//*[@numFound='5']",
                "//doc/str[@name='id'][.='153']",
                "//doc/str[@name='id'][.='154']",
                "//doc/str[@name='id'][.='155']",
                "//doc/str[@name='id'][.='156']",
                "//doc/str[@name='id'][.='157']"
        );


        assertQueryEquals(req("q", "title:\"NGC-1\"", "defType", "aqp"),
                "(title:acr::ngc1 | title:\"acr::ngc 1\")",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "title" + ":NGC-1"),
                "//*[@numFound='5']",
                "//doc/str[@name='id'][.='153']",
                "//doc/str[@name='id'][.='154']",
                "//doc/str[@name='id'][.='155']",
                "//doc/str[@name='id'][.='156']",
                "//doc/str[@name='id'][.='157']" //NGC1
        );

        assertQueryEquals(req("q", "title:\"N-1\"", "defType", "aqp"),
                "(title:n1 | title:\"n 1\")",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "title" + ":N-1"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.!='153']",
                "//doc/str[@name='id'][.!='154']",
                "//doc/str[@name='id'][.='155']",
                "//doc/str[@name='id'][.='156']",
                "//doc/str[@name='id'][.!='157']"
        );

        // this finds 0 because during indexing, we'd turn the two
        // tokens into 'n1' - and this search
        assertQueryEquals(req("q", "title:\"N 1\"", "defType", "aqp"),
                "(title:n1 | title:\"n 1\")",
                DisjunctionMaxQuery.class);
        assertQ(req("q", "title" + ":\"N 1\""),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.!='153']",
                "//doc/str[@name='id'][.!='154']",
                "//doc/str[@name='id'][.='155']",
                "//doc/str[@name='id'][.='156']",
                "//doc/str[@name='id'][.!='157']" //NGC1
        );

        assertQueryEquals(req("q", "title:\"NGC1\"", "defType", "aqp"),
                "title:acr::ngc1",
                TermQuery.class);
        assertQ(req("q", "title" + ":NGC1"),
                "//*[@numFound='5']",
                "//doc/str[@name='id'][.='153']",
                "//doc/str[@name='id'][.='154']",
                "//doc/str[@name='id'][.='155']",
                "//doc/str[@name='id'][.='156']",
                "//doc/str[@name='id'][.='157']"
        );

        assertQueryEquals(req("q", "=title:\"NGC 1\"", "defType", "aqp"),
                "title:\"acr::ngc 1\"",
                PhraseQuery.class);
        assertQ(req("q", "=title" + ":NGC 1"),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='153']",
                "//doc/str[@name='id'][.='154']",
                "//doc/str[@name='id'][.='155']",
                "//doc/str[@name='id'][.='156']",
                "//doc/str[@name='id'][.!='157']"
        );


        // #147 - parsing of WDDF tokens
        // analyzer operation. eg. XXX-YYYY => (XXX AND YYY) OR XXXYYY
        assertQueryEquals(req("q", "NAG5-ABCD", "defType", "aqp"),
                "(all:acr::nag5abcd | (+all:acr::nag5 +all:acr::abcd))",
                DisjunctionMaxQuery.class);


        // the ascii folding filter emits both unicode and the ascii version
        assertQ(req("q", "title" + ":Bílá"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
        assertQ(req("q", "title" + ":Bila"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
        assertQ(req("q", "title" + ":bila"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");

        // test that the two lines in the synonym file get merged and produce correct synonym expansion
        assertQueryEquals(req("q", "ABC", "defType", "aqp"),
                "Synonym(all:acr::abc all:syn::abc all:syn::astrophysics business center all:syn::astrophysics business commons)",
                SynonymQuery.class);
        // rca: 07/09/2019 - discovered, that when you search for ABC, it produces the correct output
        //      but if you were to search for full-name, it only uses the synonyms that were present
        //      on that line in the synonym input; so in this case the 'astrophysics business commons'
        //      is completely ignored; that's a feature that we cat take advantage of!
        assertQueryEquals(req("q", "\"astrophysics business center\"", "defType", "aqp"),
                "(all:\"astrophysics business center\" | Synonym(all:acr::abc all:syn::abc all:syn::astrophysics business center))",
                DisjunctionMaxQuery.class);


        // "all-sky" is indexed as "all", "sky", "all-sky"
        // we could achieve higher precision if WDDF generateWordParts=0
        // but that would cause "some-other-hyphenated" tokens to be missed
        assertQ(req("q", "title" + ":no-sky"), "//*[@numFound='2']",
                "//doc/str[@name='id'][.='10']",
                "//doc/str[@name='id'][.='12']");
        assertQ(req("q", "title" + ":nosky"), "//*[@numFound='2']",
                "//doc/str[@name='id'][.='10']",
                "//doc/str[@name='id'][.='12']");
        assertQ(req("q", "title" + ":all-sky"), "//*[@numFound='2']",
                "//doc/str[@name='id'][.='11']",
                "//doc/str[@name='id'][.='13']");
        assertQ(req("q", "title" + ":allsky"), "//*[@numFound='2']",
                "//doc/str[@name='id'][.='11']",
                "//doc/str[@name='id'][.='13']");
        assertQ(req("q", "title" + ":sky"), "//*[@numFound='2']",
                "//doc/str[@name='id'][.='10']",
                "//doc/str[@name='id'][.='11']"
        );

        assertQ(req("qt", "/query",
                        "aqp.allow.leading_wildcard", "true",
                        "q", "title" + ":*sky"), "//*[@numFound='4']",
                "//doc/str[@name='id'][.='10']",
                "//doc/str[@name='id'][.='11']",
                "//doc/str[@name='id'][.='12']",
                "//doc/str[@name='id'][.='13']");

        /*
         * Html tags should be removed
         */

        assertQ(req("q", "title" + ":xremoved"), "//*[@numFound='0']");
        assertQ(req("q", "title" + ":xhtml"), "//*[@numFound='1']",
                "//doc/str[@name='id'][.='382']");

        /**
         * Latex symbols should simply be converted to ascii
         */

        assertQ(req("q", "title:\"$\\gamma$-ray\""),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='400']",
                "//doc/str[@name='id'][.='401']",
                "//doc/str[@name='id'][.='402']",
                "//doc/str[@name='id'][.='403']"
        );
        assertQ(req("q", "title:\"$\\gamma$ ray\""),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='400']",
                "//doc/str[@name='id'][.='401']",
                "//doc/str[@name='id'][.='402']",
                "//doc/str[@name='id'][.='403']"
        );
        assertQ(req("q", "title:\"γ-ray\""),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='400']",
                "//doc/str[@name='id'][.='401']",
                "//doc/str[@name='id'][.='402']",
                "//doc/str[@name='id'][.='403']"
        );
        assertQ(req("q", "title:\"γ ray\""),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='400']",
                "//doc/str[@name='id'][.='401']",
                "//doc/str[@name='id'][.='402']",
                "//doc/str[@name='id'][.='403']"
        );

        assertQueryEquals(req(
                        "q", "title:\"A 350-MHz GBT Survey of 50 Faint Fermi $\\gamma$ ray Sources for Radio Millisecond Pulsars\"",
                        "defType", "aqp"),
                "(title:\"350mhz ? (acr::gbt syn::gbt syn::green bank telescope) (survey syn::survey) 50 (faint syn::faint) (fermi syn::fermi) (gamma syn::gamma) ray (sources syn::source) (radio syn::radio) (millisecond syn::millisecond) (pulsars syn::pulsars)\"~3 | title:\"350mhz ? (acr::gbt syn::gbt syn::green bank telescope) (survey syn::survey) 50 (faint syn::faint) (fermi syn::fermi) (syn::gamma ray syn::gammaray syn::gamma rays syn::gammarays) ? (sources syn::source) (radio syn::radio) (millisecond syn::millisecond) (pulsars syn::pulsars)\"~3 | title:\"350 (mhz syn::mhz) (acr::gbt syn::gbt syn::green bank telescope) (survey syn::survey) 50 (faint syn::faint) (fermi syn::fermi) (gamma syn::gamma) ray (sources syn::source) (radio syn::radio) (millisecond syn::millisecond) (pulsars syn::pulsars)\"~3 | title:\"350 (mhz syn::mhz) (acr::gbt syn::gbt syn::green bank telescope) (survey syn::survey) 50 (faint syn::faint) (fermi syn::fermi) (syn::gamma ray syn::gammaray syn::gamma rays syn::gammarays) ? (sources syn::source) (radio syn::radio) (millisecond syn::millisecond) (pulsars syn::pulsars)\"~3)",
                DisjunctionMaxQuery.class);

        assertQueryEquals(req(
                        "q", "title:\"A 350-MHz GBT Survey of 50 Faint Fermi γ-ray Sources for Radio Millisecond Pulsars\"",
                        "defType", "aqp"),
                "(title:\"350mhz ? (acr::gbt syn::gbt syn::green bank telescope) (survey syn::survey) 50 (faint syn::faint) (fermi syn::fermi) (gammaray syn::gamma ray syn::gammaray syn::gamma rays syn::gammarays gamma syn::gamma) ray (sources syn::source) (radio syn::radio) (millisecond syn::millisecond) (pulsars syn::pulsars)\"~3 | title:\"350 (mhz syn::mhz) (acr::gbt syn::gbt syn::green bank telescope) (survey syn::survey) 50 (faint syn::faint) (fermi syn::fermi) (gammaray syn::gamma ray syn::gammaray syn::gamma rays syn::gammarays gamma syn::gamma) ray (sources syn::source) (radio syn::radio) (millisecond syn::millisecond) (pulsars syn::pulsars)\"~3)",
                DisjunctionMaxQuery.class);

        //dumpDoc(null, "title");
        assertQ(req("q", "title:\"A 350-MHz GBT Survey of 50 Faint Fermi $\\gamma$ ray Sources for Radio Millisecond Pulsars\""),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='400']",
                "//doc/str[@name='id'][.='401']",
                "//doc/str[@name='id'][.='402']",
                "//doc/str[@name='id'][.='403']");
        assertQ(req("q", "title:\"A 350-MHz GBT Survey of 50 Faint Fermi $\\gamma$-ray Sources for Radio Millisecond Pulsars\""),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='400']",
                "//doc/str[@name='id'][.='401']",
                "//doc/str[@name='id'][.='402']",
                "//doc/str[@name='id'][.='403']");


        assertQ(req("q", "title:\"Survey\""),
                "//*[@numFound>='4']");
        assertQ(req("q", "title:\"Faint Fermi\""),
                "//*[@numFound>='4']");
        assertQ(req("q", "title:\"GBT Survey\""),
                "//*[@numFound>='4']");
        assertQ(req("q", "title:\"GBT Survey of 50 Faint Fermi\"~2"),
                "//*[@numFound>='4']");

        //TODO: this test is intentionally left failing; it used to work until the scoring changes (i'd like to
        // investigate more how the multi-token affects recall)
        /**
         * 1. A 350-MHz GBT Survey of 50 Faint Fermi γ-ray
         *
         * gets indexed as:
         *
         * [(0, []),
         (1, []),
         (2, ['350mhz', 'syn::mhz']),
         (3, ['gbt', 'syn::gbt', 'syn::green bank telescope']),
         (4, ['syn::survey']),
         (5, ['50']),
         (6, ['faint', 'syn::faint']),
         (7, ['fermi', 'syn::fermi']),
         (8,
         ['syn::gamma',
         'syn::gamma ray',
         'syn::gamma rays',
         'syn::gammaray',
         'syn::gammarays']),
         (9,
         ['gammaray',
         'ray',
         'syn::gamma ray',
         'syn::gamma rays',
         'syn::gammaray',
         'syn::gammarays']),
         (10, ['syn::source']),
         (11, ['syn::radio']),
         (12, ['millisecond', 'syn::millisecond'])]

         while 

         2. A 350-MHz GBT Survey of 50 Faint Fermi γ ray

         gets indexed as:

         [(0, []),
         (1, []),
         (2, ['350mhz', 'syn::mhz']),
         (3, ['syn::gbt', 'syn::green bank telescope']),
         (4, ['syn::survey']),
         (5, ['50']),
         (6, ['faint', 'syn::faint']),
         (7, ['fermi', 'syn::fermi']),
         (8,
         ['syn::gamma',
         'syn::gamma ray',
         'syn::gamma rays',
         'syn::gammaray',
         'syn::gammarays']),
         (9, ['ray']),
         (10, ['syn::source']),
         (11, ['syn::radio']),
         (12, ['millisecond', 'syn::millisecond'])]
         */

        assertQueryEquals(req("q", "title:\"γ ray Sources\""),
                "(title:\"(gamma syn::gamma) ray (sources syn::source)\"~2 | title:\"(syn::gamma ray syn::gammaray syn::gamma rays syn::gammarays) ? (sources syn::source)\"~2)",
                DisjunctionMaxQuery.class);
        assertQueryEquals(req("q", "title:\"γ-ray Sources\""),
                "title:\"(gammaray syn::gamma ray syn::gammaray syn::gamma rays syn::gammarays gamma syn::gamma) ray (sources syn::source)\"~2",
                MultiPhraseQuery.class);

        assertQ(req("q", "title:\"γ ray Sources\"",
                        "indent", "true",
                        "debugQuery", "true"),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='400']",
                "//doc/str[@name='id'][.='401']",
                "//doc/str[@name='id'][.='402']",
                "//doc/str[@name='id'][.='403']"
        );
        assertQ(req("q", "title:\"$\\gamma$ ray Sources\"",
                        "indent", "true",
                        "debugQuery", "true"),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='400']",
                "//doc/str[@name='id'][.='401']",
                "//doc/str[@name='id'][.='402']",
                "//doc/str[@name='id'][.='403']"
        );
        assertQ(req("q", "title:\"γ-ray Sources\"",
                        "indent", "true",
                        "debugQuery", "true"),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='400']",
                "//doc/str[@name='id'][.='401']",
                "//doc/str[@name='id'][.='402']",
                "//doc/str[@name='id'][.='403']"
        );

        assertQ(req("q", "title:\"A 350-MHz GBT Survey of 50 Faint Fermi γ-ray Sources for Radio Millisecond Pulsars\"",
                        "indent", "true",
                        "debugQuery", "true"),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='400']",
                "//doc/str[@name='id'][.='401']",
                "//doc/str[@name='id'][.='402']",
                "//doc/str[@name='id'][.='403']"
        );
        assertQ(req("q", "title:\"A 350-MHz GBT Survey of 50 Faint Fermi γ ray Sources for Radio Millisecond Pulsars\""),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='400']",
                "//doc/str[@name='id'][.='401']",
                "//doc/str[@name='id'][.='402']",
                "//doc/str[@name='id'][.='403']");


        //assertU(adoc("id", "402", "bibcode", "xxxxxxxxxx402", "title",
        //"A 350-MHz GBT Survey of 50 Faint Fermi $\\gamma$ ray Sources for Radio Millisecond Pulsars"));
        //assertU(adoc("id", "403", "bibcode", "xxxxxxxxxx403", "title",
        //"A 350-MHz GBT Survey of 50 Faint Fermi γ ray Sources for Radio Millisecond Pulsars"));


    }


    // Uniquely for Junit 3
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeFulltextParsing.class);
    }
}
