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
import monty.solr.util.SolrTestSetup;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

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
        System.setProperty("solr.directoryFactory", "solr.StandardDirectoryFactory");
        System.setProperty("solr.allow.unsafe.resourceloading", "true");

        schemaString = getSchemaFile();

        configString = getConfigFile();

        SolrTestSetup.initCore(configString, schemaString);
    }

    public static String getConfigFile() {
        String configFile = null;
        try {
            configFile = SolrTestSetup
                    .getRepoUrl(Paths.get("deploy/adsabs/server/solr/collection1/conf/solrconfig.xml"))
                    .getFile();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

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

        String configFile = null;
        try {
            configFile = SolrTestSetup
                    .getRepoUrl(Paths.get("deploy/adsabs/server/solr/collection1/conf/schema.xml"))
                    .getFile();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

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
                            "fermi, fermilab => fermi\n" +
                            "galactic=>galaxies\n" +
                            "central,centre=>central\n" +
                            "special=>especially\n" +
                            "relativistic=>relativity\n" +
                            "theory=>theoretical\n" +
                            "locally=>local\n" +
                            "anisotropic=>anisotropy\n" +
                            "spacetime=>time\n",
                    "space => universe\n"
            });
            File indexSimpleTokenSynonymsFile = duplicateFile(simpleTokenSynonymsFile);
            replaceInFile(indexSimpleTokenSynonymsFile, "special=>especially\n", "");
            replaceInFile(indexSimpleTokenSynonymsFile, "relativistic=>relativity\n", "");
            replaceInFile(indexSimpleTokenSynonymsFile, "theory=>theoretical\n", "");
            replaceInFile(indexSimpleTokenSynonymsFile, "locally=>local\n", "");
            replaceInFile(indexSimpleTokenSynonymsFile, "anisotropic=>anisotropy\n", "");
            replaceInFile(indexSimpleTokenSynonymsFile, "spacetime=>time\n", "");

            File multiTokenSynonymsFile = createTempFile("dynamics\0hubble,dyhu\n" +
                    "hubble\0space\0telescope,HST\n" +
                    "NuSTAR,nuclear\0spectroscopic\0telescope\0array\n" +
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
                    "galactic\0centre,galaxies\0central,milky\0way\0galaxy\0nucleus\n" +
                    "galactic\0center,galaxies\0central,milky\0way\0galaxy\0nucleus\n" +
                    "central\0molecular\0zone,galactic\0centre\n" +
                    "star\0formation,stars\0formation\n" +
                    "space,universe"

                    // and this is how it would be if it was one line
                    //"ADS,aitken\0double\0stars,astrophysics\0data\0system,anti\0de\0sitter\0space,antidesitter\0spacetime\n"
            );

            replaceInFile(newConfig,
                    Pattern.compile("(?s)(<!-- MOND => \\[\\] mond.*?synonyms=\")ads_text_simple\\.synonyms"),
                    "$1ISSUE170_QUERY_SYNONYMS");
            replaceInFile(newConfig, "synonyms=\"ads_text_multi.synonyms\"", "synonyms=\"" + multiTokenSynonymsFile.getAbsolutePath() + "\"");
            replaceInFile(newConfig, "synonyms=\"ads_text_simple.synonyms\"", "synonyms=\"" + indexSimpleTokenSynonymsFile.getAbsolutePath() + "\"");
            replaceInFile(newConfig, "synonyms=\"ISSUE170_QUERY_SYNONYMS\"", "synonyms=\"" + simpleTokenSynonymsFile.getAbsolutePath() + "\"");

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

        assertU(adoc("id", "318", "bibcode", "xxxxxxxxxx318", "title", "creation of a thesaurus", "pub_raw", "creation of a thesaurus"));
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
        assertU(adoc("id", "1051", "bibcode", "xxxxxxxxxx1051", "title", "NuSTAR"));
        assertU(adoc("id", "1052", "bibcode", "xxxxxxxxxx1052", "title", "NuStar"));
        assertU(adoc("id", "1053", "bibcode", "xxxxxxxxxx1053", "title", "nuclear spectroscopic telescope array"));
        assertU(adoc("id", "1054", "bibcode", "xxxxxxxxxx1054", "title", "nuclear spectroscopic telescope"));
        assertU(adoc("id", "700", "bibcode", "xxxxxxxxxx700",
                "title", "H2O+ CNO-SI+ PSI+O3-"));
        assertU(adoc("id", "701", "bibcode", "xxxxxxxxxx701",
                "title", "H2O CNO-SI PSI O3"));
        assertU(adoc("id", "702", "bibcode", "xxxxxxxxxx702",
                "title", "H<SUB>2</SUB>O+"));
        assertU(adoc("id", "703", "bibcode", "xxxxxxxxxx703",
                "title", "CO<SUB>2</SUB><SUP>+</SUP>"));
        assertU(adoc("id", "704", "bibcode", "xxxxxxxxxx704",
                "title", "CO<SUB>2</SUB>"));
        assertU(adoc("id", "705", "bibcode", "xxxxxxxxxx705",
                "title", "H&alpha;+"));
        assertU(adoc("id", "706", "bibcode", "xxxxxxxxxx706",
                "title", "H<SUB>&alpha;</SUB>+"));
        assertU(adoc("id", "707", "bibcode", "xxxxxxxxxx707",
                "title", "H&alpha;"));
        assertU(adoc("id", "708", "bibcode", "xxxxxxxxxx708",
                "title", "H<SUB>&alpha;</SUB>"));
        assertU(adoc("id", "709", "bibcode", "xxxxxxxxxx709",
                "title", "H2O+CO2"));
        assertU(adoc("id", "710", "bibcode", "xxxxxxxxxx710",
                "title", "J1234+5678"));
        assertU(adoc("id", "711", "bibcode", "xxxxxxxxxx711",
                "title", "J1234-5678"));
        assertU(adoc("id", "712", "bibcode", "xxxxxxxxxx712",
                "title", "J1234 5678"));
        assertU(adoc("id", "713", "bibcode", "xxxxxxxxxx713",
                "title", "He+"));
        assertU(adoc("id", "714", "bibcode", "xxxxxxxxxx714",
                "title", "He"));
        assertU(adoc("id", "715", "bibcode", "xxxxxxxxxx715",
                "title", "foo+123"));
        assertU(adoc("id", "716", "bibcode", "xxxxxxxxxx716",
                "title", "well-known+"));
        assertU(adoc("id", "717", "bibcode", "xxxxxxxxxx717",
                "title", "TB+ TM+"));
        assertU(commit());
    }


    public void testIssue171PhraseAlternativesRemainSearchable() throws Exception {
        assertU(adoc("id", "1710", "bibcode", "xxxxxxxxxx1710",
                "abstract", "A study of the galactic centre and star formation"));
        assertU(adoc("id", "1711", "bibcode", "xxxxxxxxxx1711",
                "abstract", "A study of the galactic center and star formation"));
        assertU(adoc("id", "1712", "bibcode", "xxxxxxxxxx1712",
                "abstract", "A study of the central molecular zone and star formation"));
        assertU(adoc("id", "1713", "bibcode", "xxxxxxxxxx1713",
                "abstract", "A study of the galactic distant object central and star formation"));
        assertU(commit());

        assertQ(req("q", "abstract:((\"galactic centre\" OR \"galactic center\" OR \"central molecular zone\") AND (\"star formation\"))"),
                "//*[@numFound='3']",
                "//doc/str[@name='id'][.='1710']",
                "//doc/str[@name='id'][.='1711']",
                "//doc/str[@name='id'][.='1712']",
                "not(//doc/str[@name='id'][.='1713'])");
        assertQ(req("q", "abstract:((\"galactic    centre\" OR \"galactic   center\" OR \"central   molecular  zone\") AND (\"star    formation\"))"),
                "//*[@numFound='3']",
                "//doc/str[@name='id'][.='1710']",
                "//doc/str[@name='id'][.='1711']",
                "//doc/str[@name='id'][.='1712']",
                "not(//doc/str[@name='id'][.='1713'])");
    }

    public void testRepeatedWhitespaceDoesNotWidenPhrase() throws Exception {
        try {
            assertU(adoc("id", "17140", "bibcode", "b17140",
                    "abstract", "galactic centre and star formation"));
            assertU(adoc("id", "17141", "bibcode", "b17141",
                    "abstract", "galactic centre gapone gaptwo gapthree gapfour gapfive and star formation"));
            assertU(commit());

            assertQ(req("q", "abstract:\"galactic centre and star formation\"",
                            "fq", "{!terms f=id}17140,17141"),
                    "//*[@numFound='1']", "//doc/str[@name='id'][.='17140']",
                    "not(//doc/str[@name='id'][.='17141'])");
            assertQ(req("q", "abstract:\"galactic        centre and star formation\"",
                            "fq", "{!terms f=id}17140,17141"),
                    "//*[@numFound='1']", "//doc/str[@name='id'][.='17140']",
                    "not(//doc/str[@name='id'][.='17141'])");
        } finally {
            assertU(delI("17140"));
            assertU(delI("17141"));
            assertU(commit());
        }
    }

    public void testIssue170PhraseRetainsLiteralPath() throws Exception {
        assertU(adoc("id", "1700", "bibcode", "xxxxxxxxxx1700",
                "title", "A special-relativistic theory of the locally anisotropic spacetime"));
        assertU(adoc("id", "1701", "bibcode", "xxxxxxxxxx1701",
                "title", "A special-relativistic filler theory of the locally anisotropic spacetime"));
        assertU(adoc("id", "1702", "bibcode", "xxxxxxxxxx1702",
                "title", "special filler theory"));
        assertU(adoc("id", "1703", "bibcode", "xxxxxxxxxx1703",
                "title", "special filler one theory"));
        assertU(adoc("id", "1704", "bibcode", "xxxxxxxxxx1704",
                "title", "special theory zzanchor"));
        assertU(adoc("id", "1705", "bibcode", "xxxxxxxxxx1705",
                "title", "special theory zzdifferent"));
        assertU(commit());

        assertQ(req("q", "title:\"special theory zzanchor\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='1704']",
                "not(//doc/str[@name='id'][.='1705'])");

        assertQ(req("q", "title:\"A special-relativistic theory of the locally anisotropic spacetime\""),
                "//doc/str[@name='id'][.='1700']",
                "not(//doc/str[@name='id'][.='1705'])");
        assertQ(req("q", "title:\"A special-relativistic theory of the locally anisotropic spacetime\"~0"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='1700']");
        assertQ(req("q", "title:\"special theory\"~1",
                "fq", "id:(1700 OR 1701 OR 1702 OR 1703)"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='1700']",
                "//doc/str[@name='id'][.='1702']",
                "not(//doc/str[@name='id'][.='1701'])",
                "not(//doc/str[@name='id'][.='1703'])");
        Object phraseQuery = getParser(req("q", "title:\"special theory\"~1",
                "aqp.multiphrase.keep_one", "SYNONYM")).parse();
        assertTrue("Expected synonym disjunction, got " + phraseQuery.getClass()
                + ": " + phraseQuery, phraseQuery instanceof DisjunctionMaxQuery);
    }

    public void testPhraseWithoutLiteralKeepsOneTokenPerPosition() throws Exception {
        // Listing the original token's type in keep_one leaves the stacked position without a literal token.
        Query phraseQuery = getParser(req("q", "title:\"deep space\"",
                "aqp.multiphrase.keep_one", "SYNONYM,word")).parse();
        assertTrue("Expected one phrase, got " + phraseQuery, phraseQuery instanceof MultiPhraseQuery);
        for (Term[] position : ((MultiPhraseQuery) phraseQuery).getTermArrays()) {
            assertEquals(phraseQuery.toString(), 1, position.length);
        }
    }


    public void testMultiTokens() throws Exception {

        // this multitoken is onthe first position
        assertQ(req("q", "title:\"very long baseline array\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='602']"
        );
        assertQ(req("q", "title:NuSTAR"),
                "//*[@numFound='3']",
                "//doc/str[@name='id'][.='1051']",
                "//doc/str[@name='id'][.='1052']",
                "//doc/str[@name='id'][.='1053']",
                "//doc[not(str[@name='id']='1054')]");
        assertQ(req("q", "title:NuStar"),
                "//*[@numFound='3']",
                "//doc/str[@name='id'][.='1051']",
                "//doc/str[@name='id'][.='1052']",
                "//doc/str[@name='id'][.='1053']",
                "//doc[not(str[@name='id']='1054')]");

        // now add some docfreq
        assertU(adoc("id", "1000", "bibcode", "xxxxxxxxxx1000",
                "title", "antidesitter spacetime application"));
        assertU(adoc("id", "1001", "bibcode", "xxxxxxxxxx1001",
                "title", "anti de sitter space application"));
        assertU(adoc("id", "1002", "bibcode", "xxxxxxxxxx1002",
                "title", "NASA ADS"));
        assertU(commit());

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
        assertQ(req("q", "title:\"black hole\"",
                        "aqp.multiphrase.keep_one", "SYNONYM",
                        "aqp.multiphrase.fields", "title"
                ),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='500']",
                "//doc/str[@name='id'][.='501']"
        );

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

        assertQ(req("q", "title:\"BH\"", "aqp.multiphrase.keep_one", "SYNONYM"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='500']",
                "//doc/str[@name='id'][.='501']"
        );

        assertQ(req("q", "title:\"observations BH\"", "aqp.multiphrase.keep_one", "SYNONYM"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='500']",
                "//doc/str[@name='id'][.='501']"
        );

        assertQ(req("q", "title:\"BH event\"", "aqp.multiphrase.keep_one", "SYNONYM"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='500']",
                "//doc/str[@name='id'][.='501']"
        );

        assertQ(req("q", "title:\"BH\"", "aqp.multiphrase.keep_one", "SYNONYM"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='500']",
                "//doc/str[@name='id'][.='501']"
        );

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
        assertQ(req("q", "NAG5-ABCD", "df", "title"),
                "//*[@numFound='3']",
                "//doc/str[@name='id'][.='147']",
                "//doc/str[@name='id'][.='148']",
                "//doc/str[@name='id'][.='149']"
        );
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
        // pub is a normalized_string for exact publication matching.  The full-text
        // behavior exercised here belongs to pub_raw, whose current type is ads_text.
        assertQ(req("q", "pub_raw:creation AND pub_raw:thesaurus"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='318']"
        );
        // Stop words are removed by the ads_text index analyzer, but they do not
        // prevent the surrounding terms from matching.
        assertQ(req("q", "pub_raw:of AND pub_raw:a"), "//*[@numFound='0']");
        assertQ(req("q", "creation of a thesaurus", "defType", "aqp", "qf", "pub_raw"),
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
        assertQueryEquals(req("q", "title:mond", "defType", "aqp"),
                "Synonym(title:mond title:syn::lunar)", SynonymQuery.class);
        assertQueryEquals(req("q", "title:Mond", "defType", "aqp"),
                "Synonym(title:mond title:syn::lunar)", SynonymQuery.class);

        // unfielded simple token
        
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
        
        assertQ(req("q", "title" + ":\"bubble pace telescope multi-pace foobar\""), "//*[@numFound='1']",
                "//doc/str[@name='id'][.='17']");


        // now the same thing, but not using phrases
        
        assertQ(req("q", "title" + ":modified\\ newtonian\\ dynamics"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='14']",
                "//doc/str[@name='id'][.='15']");


        // and even unfielded!
        

        assertQ(req("q", "modified\\ newtonian\\ dynamics", "defType", "aqp", "df", "title"), "//*[@numFound='2']",
                "//doc/str[@name='id'][.='14']",
                "//doc/str[@name='id'][.='15']");


        // lastly - unfielded phrase
        
        assertQ(req("q", "\"modified newtonian dynamics\"", "qf", "title^2.0 all^1.5"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='14']",
                "//doc/str[@name='id'][.='15']");


        // test of the multi-synonym replacement, phrase handling etc
        //dumpDoc(null, "title", "recid");
        
        assertQ(req("q", "title:\"bubble pace telescope multi-foo\"", "defType", "aqp", "df", "title"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='20']"
        );

        // wow! this works correctly
        
        assertQ(req("q", "bubble\\ pace\\ telescope\\ and\\ MIT", "defType", "aqp", "df", "title"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='19']"
        );

    }

    private static List<String> analyzedTokens(Analyzer analyzer, String value) throws IOException {
        TokenStream stream = analyzer.tokenStream("title", new StringReader(value));
        CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
        PositionIncrementAttribute increment = stream.addAttribute(PositionIncrementAttribute.class);
        List<String> tokens = new ArrayList<>();
        int position = 0;
        stream.reset();
        while (stream.incrementToken()) {
            position += increment.getPositionIncrement();
            tokens.add(position + ":" + term.toString());
        }
        stream.end();
        stream.close();
        return tokens;
    }

    private static boolean hasTokenAt(List<String> tokens, int position, String expected) {
        String prefix = position + ":";
        for (String token : tokens) {
            if (token.startsWith(prefix) && token.substring(prefix.length()).equalsIgnoreCase(expected)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasToken(List<String> tokens, String expected) {
        for (String token : tokens) {
            int separator = token.indexOf(':');
            if (separator >= 0 && token.substring(separator + 1).equalsIgnoreCase(expected)) {
                return true;
            }
        }
        return false;
    }

    public void testSignedChemicalFormulaAnalyzerGraphs() throws Exception {
        Analyzer indexAnalyzer = h.getCore().getLatestSchema().getField("title").getType().getIndexAnalyzer();
        Analyzer queryAnalyzer = h.getCore().getLatestSchema().getField("title").getType().getQueryAnalyzer();

        List<String> indexedCno = analyzedTokens(indexAnalyzer, "CNO-SI+");
        assertTrue(indexedCno.toString(), hasTokenAt(indexedCno, 1, "CNO-SI+"));
        assertTrue(indexedCno.toString(), hasTokenAt(indexedCno, 1, "acr::cno-"));
        assertTrue(indexedCno.toString(), hasTokenAt(indexedCno, 2, "SI+"));

        List<String> indexedPsi = analyzedTokens(indexAnalyzer, "PSI+O3-");
        assertTrue(indexedPsi.toString(), hasTokenAt(indexedPsi, 1, "PSI+O3-"));
        assertTrue(indexedPsi.toString(), hasTokenAt(indexedPsi, 1, "acr::psi+"));
        assertTrue(indexedPsi.toString(), hasTokenAt(indexedPsi, 2, "O3-"));

        List<String> queriedCno = analyzedTokens(queryAnalyzer, "CNO-SI+");
        assertTrue(queriedCno.toString(), hasTokenAt(queriedCno, 1, "CNO-SI+"));
        assertTrue(queriedCno.toString(), hasTokenAt(queriedCno, 1, "acr::cno-"));
        assertTrue(queriedCno.toString(), hasTokenAt(queriedCno, 2, "SI+"));

        List<String> queriedPsi = analyzedTokens(queryAnalyzer, "PSI+O3-");
        assertTrue(queriedPsi.toString(), hasTokenAt(queriedPsi, 1, "PSI+O3-"));
        assertTrue(queriedPsi.toString(), hasTokenAt(queriedPsi, 1, "acr::psi+"));
        assertTrue(queriedPsi.toString(), hasTokenAt(queriedPsi, 2, "O3-"));

        List<String> ordinaryIndexed = analyzedTokens(indexAnalyzer, "zzword+ zzword-");
        List<String> ordinaryQueried = analyzedTokens(queryAnalyzer, "zzword+ zzword-");
        assertTrue(ordinaryIndexed.toString(), hasToken(ordinaryIndexed, "zzword"));
        assertFalse(ordinaryIndexed.toString(), hasToken(ordinaryIndexed, "zzword+"));
        assertFalse(ordinaryIndexed.toString(), hasToken(ordinaryIndexed, "zzword-"));
        assertTrue(ordinaryQueried.toString(), hasToken(ordinaryQueried, "zzword"));
        assertFalse(ordinaryQueried.toString(), hasToken(ordinaryQueried, "zzword+"));
        assertFalse(ordinaryQueried.toString(), hasToken(ordinaryQueried, "zzword-"));
        Analyzer exactIndexAnalyzer = h.getCore().getLatestSchema()
                .getField("title_nosyn").getType().getIndexAnalyzer();
        Analyzer exactQueryAnalyzer = h.getCore().getLatestSchema()
                .getField("title_nosyn").getType().getQueryAnalyzer();
        List<String> exactIndexedCno = analyzedTokens(exactIndexAnalyzer, "CNO-SI+");
        List<String> exactQueriedCno = analyzedTokens(exactQueryAnalyzer, "CNO-SI+");
        assertTrue(exactIndexedCno.toString(), hasTokenAt(exactIndexedCno, 1, "CNO-"));
        assertTrue(exactIndexedCno.toString(), hasTokenAt(exactIndexedCno, 2, "SI+"));
        assertTrue(exactQueriedCno.toString(), hasTokenAt(exactQueriedCno, 1, "CNO-"));
        assertTrue(exactQueriedCno.toString(), hasTokenAt(exactQueriedCno, 2, "SI+"));
        assertFalse(exactIndexedCno.toString(), hasToken(exactIndexedCno, "CNO-SI"));

        List<String> exactIndexedPsi = analyzedTokens(exactIndexAnalyzer, "PSI+O3-");
        List<String> exactQueriedPsi = analyzedTokens(exactQueryAnalyzer, "PSI+O3-");
        assertTrue(exactIndexedPsi.toString(), hasTokenAt(exactIndexedPsi, 1, "PSI+"));
        assertTrue(exactIndexedPsi.toString(), hasTokenAt(exactIndexedPsi, 2, "O3-"));
        assertTrue(exactQueriedPsi.toString(), hasTokenAt(exactQueriedPsi, 1, "PSI+"));
        assertTrue(exactQueriedPsi.toString(), hasTokenAt(exactQueriedPsi, 2, "O3-"));
    }

    public void testSignedChemicalFormulae() throws Exception {
        assertQ(req("q", "title:\"H2O+\""),
                "//*[@numFound='3']",
                "//doc/str[@name='id'][.='700']",
                "//doc/str[@name='id'][.='702']",
                "//doc/str[@name='id'][.='709']"
        );
        assertQ(req("q", "title:\"CNO-SI+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='700']"
        );
        assertQ(req("q", "title:\"PSI+O3-\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='700']"
        );
        assertQ(req("q", "title:\"PSI+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='700']"
        );
        assertQ(req("q", "title:\"O3-\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='700']"
        );
        assertQ(req("q", "title:\"CNO-\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='700']"
        );
        assertQ(req("q", "title:\"SI+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='700']"
        );
        assertQ(req("q", "title:\"si+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='700']",
                "not(//doc/str[@name='id'][.='701'])");
        assertQ(req("q", "title:\"cno-si+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='700']",
                "not(//doc/str[@name='id'][.='701'])");
        assertQ(req("q", "title:\"CO2+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='703']"
        );
        assertQ(req("q", "title:\"co2+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='703']",
                "not(//doc/str[@name='id'][.='704'])");
        assertQ(req("defType", "aqp", "q", "=title:\"co2+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='703']",
                "not(//doc/str[@name='id'][.='704'])");
        assertQ(req("defType", "aqp", "q", "=title:\"si+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='700']",
                "not(//doc/str[@name='id'][.='701'])");
        assertQ(req("defType", "aqp", "q", "=title:\"cno-si+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='700']",
                "not(//doc/str[@name='id'][.='701'])");
        assertQ(req("defType", "aqp", "q", "=title:\"H2O+\""),
                "//doc/str[@name='id'][.='709']");
        assertQ(req("q", "title:\"foo+123\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='715']");
        assertQ(req("defType", "aqp", "q", "=title:\"foo+123\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='715']");
        assertQ(req("q", "title:\"well-known+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='716']");
        assertQ(req("defType", "aqp", "q", "=title:\"well-known+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='716']");
        assertQ(req("q", "title:\"tb+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='717']");
        assertQ(req("defType", "aqp", "q", "=title:\"tb+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='717']");
        assertQ(req("q", "title:\"tm+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='717']");
        assertQ(req("defType", "aqp", "q", "=title:\"tm+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='717']");
        assertQ(req("q", "title:\"CO2\""),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='704']",
                "//doc/str[@name='id'][.='709']"
        );
        assertQ(req("q", "title:\"Hα+\""),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='705']",
                "//doc/str[@name='id'][.='706']"
        );
        assertQ(req("q", "title:\"Hα\""),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='707']",
                "//doc/str[@name='id'][.='708']"
        );
    }

    public void testSignedAstronomicalObjectNames() throws Exception {
        // A coordinate written with a space is a meaningful negative control.
        assertQ(req("q", "title:\"J1234+5678\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='710']",
                "not(//doc/str[@name='id'][.='712'])"
        );
        assertQ(req("q", "title:\"j1234+5678\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='710']",
                "not(//doc/str[@name='id'][.='712'])"
        );
        assertQ(req("q", "title:\"J1234-5678\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='711']"
        );
        assertQ(req("q", "title:\"j1234-5678\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='711']"
        );
        assertQ(req("defType", "aqp", "q", "=title:\"j1234+5678\""),
                "//doc/str[@name='id'][.='710']");
        assertQ(req("q", "title:\"J1234 5678\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='712']"
        );
    }

    public void testSignedChemicalFormulaNames() throws Exception {
        assertQ(req("q", "title:\"He+\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='713']",
                "not(//doc/str[@name='id'][.='714'])"
        );
    }

    public void unfieldedSearch() throws Exception {
        // non-phrase: by default do span search
        //setDebug(true);
        
        assertQ(req("q", "hubble space telescope"),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='4']",
                "//doc/str[@name='id'][.='5']",
                "//doc/str[@name='id'][.='17']", // to go away after #147
                "//doc/str[@name='id'][.='18']"
        );

        // make sure the unfielded search is expanded properly (by edismax) - we use it just here
        // HOWEVER: maybe it should do expansion inside each clause? now it favors docs with matches in all fields (which is fine)
        

        
        assertQ(req("q", "title:(hubble space telescope goes home)"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='4']"
        );

        // surrounded by stop words
        
        assertQ(req("q", "title:(mirrors of the hubble space telescope goes home)"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='4']"
        );

        // surrounded - change default operator (many matches)
        // TODO: #147
        
        assertQ(req("q", "title:(mirrors of the hubble space telescope start home)", "q.op", "OR"),
                "//*[@numFound='6']",
                "//doc[1]/str[@name='id'][.='4']", // this one is the best match
                "//doc/str[@name='id'][.='18']",
                "//doc/str[@name='id'][.='5']",
                "//doc/str[@name='id'][.='6']",
                "//doc/str[@name='id'][.='7']",
                "//doc/str[@name='id'][.='17']"
        );

        
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
        

        assertQ(req("q", "title:\"hubble space telescope\""),
                "//*[@numFound='5']",
                "//doc/str[@name='id'][.='4']",
                "//doc/str[@name='id'][.='5']",
                "//doc/str[@name='id'][.='600']",
                "//doc/str[@name='id'][.='601']",
                "//doc/str[@name='id'][.='603']",
                "not(//doc/str[@name='id'][.='6'])",
                "not(//doc/str[@name='id'][.='18'])"
        );
        assertQ(req("q", "title:\"hubble space telescope\"~1", "fq", "id:18"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='18']");


        // preceded by something
        // TODO: remove 'title:' after #147 is solved
        

        // An internal gap requires explicit slop; acronym case remains significant.
        assertQ(req("q", "title:\"mirrors hubble space telescope\"", "defType", "aqp"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='4']",
                "//doc/str[@name='id'][.='5']",
                "not(//doc/str[@name='id'][.='6'])",
                "not(//doc/str[@name='id'][.='18'])"
        );
        assertQ(req("q", "title:\"mirrors of the hubble space telescope\""),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='4']",
                "//doc/str[@name='id'][.='5']",
                "not(//doc/str[@name='id'][.='6'])",
                "not(//doc/str[@name='id'][.='18'])"
        );
        assertQ(req("q", "title:\"mirrors of the hubble space scope\""),
                "//*[@numFound='0']"
        );

        // query followed by something
        
        assertQ(req("q", "title:\"hubble space telescope goes home\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='4']"
        );


        // surrounded by something
        
        assertQ(req("q", "title:\"mirrors of the hubble space telescope goes home\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='4']"
        );





        /*
         * Synonym expansion 1token->many
         */
        assertQ(req("q", "title:HST"),
                "//*[@numFound='5']",
                "//doc/str[@name='id'][.='4']",
                "//doc/str[@name='id'][.='5']");


        


        /*
         * many token -> 1
         */


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
        
        //one-token word one-token
        
        //one-token word multi-token
        
        //multi-token stopword single-token
        
        //multi-token word single-token
        


        // synonyms hidden inside other words:
        //word one-token stopword one-token word
        
        //word one-token word one-token word
        
        //word one-token stopword multi-token word
        
        //word one-token word multi-token word
        
        //word multi-token stopword single-token word
        
        //word multi-token word single-token word
        


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
        
        
        

        

        /*
         * *QUERY* synonym expansion is case sensitive for single tokens,
         * but case-insensitive for multi-tokens (yes, your developer went through some extreme pain ;)))
         */
        assertQueryEquals(req("q", "Hst", "defType", "aqp"),
                "all:hst", TermQuery.class);

        assertQueryEquals(req("q", "hst", "defType", "aqp"),
                "all:hst", TermQuery.class);

        


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
                "title:\"ngc 1\"",
                PhraseQuery.class);
        assertQ(req("q", "=title" + ":NGC 1"),
                "//*[@numFound='4']",
                "//doc/str[@name='id'][.='153']",
                "//doc/str[@name='id'][.='154']",
                "//doc/str[@name='id'][.='155']",
                "//doc/str[@name='id'][.='156']",
                "//doc/str[@name='id'][.!='157']"
        );




        // the ascii folding filter emits both unicode and the ascii version
        assertQ(req("q", "title" + ":Bílá"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
        assertQ(req("q", "title" + ":Bila"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");
        assertQ(req("q", "title" + ":bila"), "//*[@numFound='1']", "//doc[1]/str[@name='id'][.='1']");

        // test that the two lines in the synonym file get merged and produce correct synonym expansion
        
        // rca: 07/09/2019 - discovered, that when you search for ABC, it produces the correct output
        //      but if you were to search for full-name, it only uses the synonyms that were present
        //      on that line in the synonym input; so in this case the 'astrophysics business commons'
        //      is completely ignored; that's a feature that we cat take advantage of!
        


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
