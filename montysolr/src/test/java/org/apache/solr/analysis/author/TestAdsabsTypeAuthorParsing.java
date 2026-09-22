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

package org.apache.solr.analysis.author;


import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;
import monty.solr.util.SolrTestSetup;
import org.adsabs.solr.AdsConfig.F;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

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
        schemaString = getSchemaFile();

        configString = "solrconfig.xml";

        SolrTestSetup.initCore(configString, schemaString);
    }

    public static String getSchemaFile() {

        /*
         * Make a copy of the schema.xml, and create our own synonym translation rules
         */

        String schemaConfig = null;
        try {
            schemaConfig = SolrTestSetup
                    .getRepoUrl(Paths.get("deploy/adsabs/server/solr/collection1/conf/schema.xml"))
                    .getFile();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

        File newConfig;
        try {

            // hand-curated synonyms
            File curatedSynonyms = createTempFile("ABBOT, CHARLES GREELEY;ABBOTT, CHARLES GREELEY",
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
                    "orlitova,; stoklasova,");

            // automatically harvested variations of author names (collected during indexing)
            // it will be enriched by the indexing
            File generatedTransliterations = createTempFile(formatSynonyms(new String[]{
                            "wyrzykowskij, l=>wyrzykowski, l;wyrzykowski, ł",
                            "ADAMCuk, m => ADAMČuk, m",
                            "ADAMCZuk, m => ADAMČuk, m",
                            //"ADAMCHuk, m K=> ADAMČuk, m K",  => deactivated for test purposes, see <surname>, <1> <2> use case
                            //"ADAMCuk, m K=> ADAMČuk, m K", => deactivated for test purposes, see <surname>, <1> <2> use case
                            "ADAMCUK, A B=> ADAMČUK, A B",
                            "ADAMCZUK, A B=> ADAMČUK, A B",
                            "ADAMCuk, mOLJA => ADAMČuk, mOLJA",
                            "ADAMCZuk, mOLJA => ADAMČuk, mOLJA",
                            "ADAMCuk, mOLJA K=> ADAMČuk, mOLJA K",
                            "ADAMCZuk, mOLJA K=> ADAMČuk, mOLJA K",
                            "ADAMCUK,=> ADAMČUK,",
                            "ADAMCZUK, => ADAMČUK,", // this one is added by hand (no automated transliteration)
                            "MULLER, WILLIAM => MÜLLER, WILLIAM",
                            "MUELLER, WILLIAM => MÜLLER, WILLIAM",
                            "Boser,=>Böser,",
                            "Boser, S=>Böser, S",
                            "Gonzalez Alfonso,=>González Alfonso,",
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

        assertU(adoc(F.ID, "100", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Müller, William", "all", "William"));
        assertU(adoc(F.ID, "101", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Mueller, William", "all", "William"));
        assertU(adoc(F.ID, "102", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Müller, Bill", "all", "Bill"));
        assertU(adoc(F.ID, "103", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Müller", "all", "Müller"));

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
        assertAuthorResults("\"adamczuk, molja k\"", "21");

    }

    public void testAuthorParsingUseCases() throws Exception {
        assertU(adoc(F.ID, "700", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Krivodubski, V"));
        assertU(adoc(F.ID, "701", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Krivodubski,"));
        assertU(adoc(F.ID, "702", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Herrera-Camus, Ana"));
        assertU(adoc(F.ID, "703", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Camus Herrera, Ana"));
        assertU(adoc(F.ID, "704", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Prefix Herrera-Camus, Ana"));
        assertU(adoc(F.ID, "705", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Accomazzi, Alberto"));
        assertU(adoc(F.ID, "706", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Accomazzi,"));
        assertU(adoc(F.ID, "707", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Kao, P'ing-Tzu"));
        assertU(adoc(F.ID, "708", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Kao,"));
        assertU(adoc(F.ID, "709", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Maestro, V"));
        assertU(adoc(F.ID, "710", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Boyajian, T"));
        assertU(commit());

        // A multipart author query must retain the normalized surname and initial,
        // but must not broaden to the surname-only record.
        assertAuthorResults("\"krivodubski, v\"", "1", "700");
        assertAuthorResults("\"krivodubskij, v\"", "1", "700");

        // Wildcards and a leading-position query must operate on the indexed
        // author tokens, not on the spelling of the source string.
        assertAuthorResults("\"van dok*, h\"", "1", "222");
        assertAuthorResults("\"^Herrera-Camus\"", "1", "702");
        assertAuthorResults("\"^acco*\"", "2", "705", "706");
        assertAuthorResults("acco*", "2", "705", "706");
        assertAuthorResults("Adamč*", "11",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");

        // A semicolon-separated synonym and the multi-valued author fixture.
        assertAuthorResults("\"wyrzykowskij, l\"", "1", "601");
        assertAuthorResults("\"other, name\"", "1", "600");
        assertAuthorResults("\u8349", "1", "600");
        assertAuthorResults("\"baz, baz\"", "1", "600");
        assertAuthorResults("\"foo, * other, *\"", "0");
        assertAuthorResults("\"foo, *\"", "1", "600");
        assertAuthorResults("\"other, *\"", "1", "600");

        // Long multi-part names must not produce a false positive.
        assertAuthorResults("\"van der Wiel, M. H. D.\"", "0");
        assertAuthorResults("\"van der Wiel, M. Hillary D.\"", "0");

        // Gopal-Krishna is indexed in three first-name forms; all input forms
        // must resolve to the same three records.
        assertAuthorResults("Gopal-Krishna", "3", "300", "301", "302");
        assertAuthorResults("\"Gopal Krishna,\"", "3", "300", "301", "302");
        assertAuthorResults("\"Gopal Krishna\"", "3", "300", "301", "302");

        assertAuthorResults("Maestro\\,\\ V", "1", "709");
        assertAuthorResults("V\\ Maestro", "1", "709");
        assertAuthorResults("Boyajian\\,\\ T", "1", "710");
        assertAuthorResults("T\\ Boyajian", "1", "710");
        assertAuthorResults("first", "0");
        assertAuthorResults("goodman", "0");

        // The parser removes the trailing xxx component while retaining the
        // indexed Accomazzi record.
        assertAuthorResults("\"accomazzi, alberto, xxx.\"", "1", "705");

        // Apostrophe and punctuation normalization must not invent authors.
        assertAuthorResults("\"o' sullivan\"", "0");
        assertAuthorResults("\"o'sullivan\"", "0");
        assertAuthorResults("\"o' sullivan, ji\"", "0");
        assertAuthorResults("Dall\\'oglio", "0");
        assertAuthorResults("Antonella\\ Dall\\'Oglio", "0");
        assertAuthorResults("\"t' Hooft, Sullivan\"", "0");

        assertAuthorResults("\"P'ING-TZU KAO\"", "1", "707");
        assertAuthorResults("\"Kao, P'ing-Tzu\"", "1", "707");
        assertAuthorResults("\"purpose of this review is to bridge the gap between\"", "0");

        // first_author is a separately indexed author field.  Keep the expected
        // match for fixture 230; a zero here indicates that the fixture's
        // first_author value was not indexed, not a valid zero-result contract.
        author_field = "first_author";
        assertAuthorResults("\"Boser, S\"", "1", "230");
        assertAuthorResults("\"Böser, S\"", "1", "230");

        author_field = "author";
        assertAuthorResults("\"Boser, S\"", "2", "231", "232");
        assertAuthorResults("\"Böser, S\"", "2", "231", "232");

        assertAuthorResults("\"Gonzalez-Alfonso, E\"", "4", "500", "502", "503", "505");
        assertAuthorResults("\"Gonzalez Alfonso, E\"", "4", "500", "502", "503", "505");

        assertAuthorResults("\"Moon, Dae-Sik\"", "10",
                "400", "401", "402", "403", "404", "405", "406", "407", "408", "409");
        assertAuthorResults("\"Moon, D. -S.\"", "10",
                "400", "401", "402", "403", "404", "405", "406", "407", "408", "409");


        assertAuthorResults("\"van Dokkum\"", "6", "220", "221", "222", "223", "224", "225");
        assertAuthorResults("\"van Dokkum,\"", "6", "220", "221", "222", "223", "224", "225");
        assertAuthorResults("\"van Dokkum, H\"", "3", "222", "223", "224");
        assertAuthorResults("\"van Dokkum, H.\"", "3", "222", "223", "224");
        assertAuthorResults("\"van Dokkum, Romulus\"", "1", "225");

        assertAuthorResults("Pinilla-Alonso", "6", "210", "211", "212", "213", "214", "215");
        assertAuthorResults("\"Pinilla Alonso\"", "6", "210", "211", "212", "213", "214", "215");
        assertAuthorResults("\"Pinilla Alonso,\"", "6", "210", "211", "212", "213", "214", "215");
        assertAuthorResults("\"Pinilla-Alonso, B\"", "3", "212", "213", "214");
        assertAuthorResults("\"Pinilla Alonso, B.\"", "3", "212", "213", "214");
        assertAuthorResults("\"Pinilla-Alonso, Brava\"", "2", "212", "213");

        // The wildcard must not match the longer Harwin-Costa author.
        assertAuthorResults("\"Lee, H-C\"", "4", "200", "201", "202", "203");
        assertAuthorResults("\"Lee, H C\"", "4", "200", "201", "202", "203");
        assertAuthorResults("\"Lee, Harwin C\"", "4", "200", "201", "202", "203");
        assertAuthorResults("\"Lee, Harwin-*\"", "0");
        assertAuthorResults("\"Lee, Harwin*\"", "2", "202", "203");
        assertAuthorResults("\"Lee, H*\"", "4", "200", "201", "202", "203");
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

    		 	- transliteration: adamčuk, m --> adamcuk, m
         		- synonym expansion for: ADAMŠuk, m;ADAMGuk, m;ADAMČuk, m
         */

        /**
         * <surname>
         *
         * upgraded && transliterated
         * synonym adamšuk IS NOT FOUND because there is no  entry for "adam(č|c)uk" the syn list
         */
        assertAuthorResults("adAMčuk", "23",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "61");
        assertAuthorResults("adAMcuk", "22",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30");
        assertAuthorResults("adAMguk", "12",
                "2", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70");


        assertAuthorResults("adAMchuk", "11",
                "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50");

        assertAuthorResults("adAMczuk", "22",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30");

        assertAuthorResults("adAMšuk", "2",
                "2", "61");


        /**
         * <surname>,
         *
         * upgraded && transliterated
         * synonym adamšuk IS NOT FOUND because there is no  entry for "adam(č|c|ch)uk" the syn list
         */
        assertAuthorResults("\"adamčuk,\"", "23",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "61");
        assertAuthorResults("\"adamcuk,\"", "22",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30");
        assertAuthorResults("\"adamchuk,\"", "11",
                "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50");
        assertAuthorResults("\"adamczuk,\"", "22",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30");
        assertAuthorResults("\"adamguk,\"", "12",
                "2", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70");




        // deactivated: "ADAMCuk, m K=> ADAMČuk, m K"
        // synonyms: "ADAMŠuk, m; ADAMGuk, m;ADAMČuk, m"
        // HOWEVER - synonyms are discovered by the author_short_name_rage processor
        // it always trips me; basically ADS wants to expand array of author names
        // by looking for synonyms that were derived from ascii folding UTF-8 synonyms
        // plus: synonyms are automatically created by stripping first names so that
        // `adamšuk` will find "ADAMŠuk, m; ADAMGuk, m;ADAMČuk, m" -- ufff...
        // but we will use them only if the user was searching for the short form
        // (assuming: ah, they didn't know the first name, let's help 'em...)
        assertAuthorResults("\"adamšuk,\"", "2",
                "2", "61");



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
        // Multipart author input is deliberately precise: surname-only records
        // are not admitted by the initial-based expansion.
        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"adamčuk,    m\""),
                "//*[@numFound='21']",
                "//doc/str[@name='id'][.='2']",
                "//doc/str[@name='id'][.='3']",
                "//doc/str[@name='id'][.='4']",
                "//doc/str[@name='id'][.='5']",
                "//doc/str[@name='id'][.='6']",
                "//doc/str[@name='id'][.='7']",
                "//doc/str[@name='id'][.='8']",
                "//doc/str[@name='id'][.='21']",
                "//doc/str[@name='id'][.='22']",
                "//doc/str[@name='id'][.='23']",
                "//doc/str[@name='id'][.='24']",
                "//doc/str[@name='id'][.='25']",
                "//doc/str[@name='id'][.='26']",
                "//doc/str[@name='id'][.='27']",
                "//doc/str[@name='id'][.='61']",
                "//doc/str[@name='id'][.='62']",
                "//doc/str[@name='id'][.='63']",
                "//doc/str[@name='id'][.='64']",
                "//doc/str[@name='id'][.='65']",
                "//doc/str[@name='id'][.='66']",
                "//doc/str[@name='id'][.='67']"
        );
        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"adamcuk, m\""),
                "//*[@numFound='21']",
                "//doc/str[@name='id'][.='2']",
                "//doc/str[@name='id'][.='3']",
                "//doc/str[@name='id'][.='4']",
                "//doc/str[@name='id'][.='5']",
                "//doc/str[@name='id'][.='6']",
                "//doc/str[@name='id'][.='7']",
                "//doc/str[@name='id'][.='8']",
                "//doc/str[@name='id'][.='21']",
                "//doc/str[@name='id'][.='22']",
                "//doc/str[@name='id'][.='23']",
                "//doc/str[@name='id'][.='24']",
                "//doc/str[@name='id'][.='25']",
                "//doc/str[@name='id'][.='26']",
                "//doc/str[@name='id'][.='27']",
                "//doc/str[@name='id'][.='61']",
                "//doc/str[@name='id'][.='62']",
                "//doc/str[@name='id'][.='63']",
                "//doc/str[@name='id'][.='64']",
                "//doc/str[@name='id'][.='65']",
                "//doc/str[@name='id'][.='66']",
                "//doc/str[@name='id'][.='67']"
        );

        assertAuthorResults("\"adamchuk, m\"", "7",
                "41", "42", "43", "44", "45", "46", "47");
        assertAuthorResults("\"adamczuk, m\"", "21",
                "2", "3", "4", "5", "6", "7", "8",
                "21", "22", "23", "24", "25", "26", "27",
                "61", "62", "63", "64", "65", "66", "67");
        assertAuthorResults("\"adamšuk, m\"", "21",
                "2", "3", "4", "5", "6", "7", "8",
                "21", "22", "23", "24", "25", "26", "27",
                "61", "62", "63", "64", "65", "66", "67");
        assertAuthorResults("\"adamguk, m\"", "21",
                "2", "3", "4", "5", "6", "7", "8",
                "21", "22", "23", "24", "25", "26", "27",
                "61", "62", "63", "64", "65", "66", "67");
        assertAuthorResults("\"AdAmČuk, m\"", "21",
                "2", "3", "4", "5", "6", "7", "8",
                "21", "22", "23", "24", "25", "26", "27",
                "61", "62", "63", "64", "65", "66", "67");
        assertAuthorResults("\"ADAMŠuk, m\"", "21",
                "2", "3", "4", "5", "6", "7", "8",
                "21", "22", "23", "24", "25", "26", "27",
                "61", "62", "63", "64", "65", "66", "67");
        assertAuthorResults("\"AdAmGuk,    M\"", "21",
                "2", "3", "4", "5", "6", "7", "8",
                "21", "22", "23", "24", "25", "26", "27",
                "61", "62", "63", "64", "65", "66", "67");


        /**
         * <surname>, <1name>
         *
         *  upgraded && transliterated && expanded
         *  synonym "adamšuk, m" IS FOUND because of the query variation for "adamčuk, m" the syn list
         */



        assertAuthorResults("\"adamčuk, molja\"", "15",
                "2", "4", "5", "6", "7", "8", "21", "23", "24", "25", "26", "27", "61", "65", "67");

        assertAuthorResults("\"adamcuk, molja\"", "15",
                "2", "4", "5", "6", "7", "8", "21", "23", "24", "25", "26", "27", "61", "65", "67");

        assertAuthorResults("\"adamchuk, molja\"", "6",
                "41", "43", "44", "45", "46", "47");

        assertAuthorResults("\"adamczuk, molja\"", "15",
                "2", "4", "5", "6", "7", "8", "21", "23", "24", "25", "26", "27", "61", "65", "67");

        // "adamčuk, molja" is not there (and cannot be, because it is not in
        // synonym map, but synonym "adamšuk, m" is found correctly)

        assertAuthorResults("\"adamšuk, molja\"", "9",
                "2", "6", "8", "21", "25", "27", "61", "65", "67");


        assertAuthorResults("\"adamguk, molja\"", "12",
                "2", "6", "8", "21", "25", "27", "61", "63", "64", "65", "66", "67");


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
        assertAuthorResults("\"adamčuk, molja k\"", "12",
                "2", "4", "5", "6", "7", "8", "21", "23", "24", "25", "26", "27");

        assertAuthorResults("\"adamcuk, molja k\"", "12",
                "2", "4", "5", "6", "7", "8", "21", "23", "24", "25", "26", "27");

        assertAuthorResults("\"adamchuk, molja k\"", "6",
                "41", "43", "44", "45", "46", "47");

        assertAuthorResults("\"adamczuk, molja k\"", "12",
                "2", "4", "5", "6", "7", "8", "21", "23", "24", "25", "26", "27");

        assertAuthorResults("\"adamšuk, molja k\"", "0");

        assertAuthorResults("\"adamguk, molja k\"", "6",
                "61", "63", "64", "65", "66", "67");


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
        //dumpDoc(null, "id", "author");

        assertAuthorResults("\"adamčuk, molja karel\"", "12",
                "2", "4", "5", "6", "7", "8", "21", "23", "24", "25", "26", "27");

        assertAuthorResults("\"adamcuk, molja karel\"", "12",
                "2", "4", "5", "6", "7", "8",
                "21", "23", "24", "25", "26", "27");

        assertAuthorResults("\"adamchuk, molja karel\"", "6",
                "41", "43", "44", "45", "46", "47");


        assertAuthorResults("\"adamczuk, molja karel\"", "8",
                "2", "4", "7", "8", "21", "23", "26", "27");

        assertAuthorResults("\"adamšuk, molja k\"", "0");


        assertAuthorResults("\"adamguk, molja karel\"", "6",
                "61", "63", "64", "65", "66", "67");


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
        assertAuthorResults("\"adamčuk, m karel\"", "14",
                "2", "3", "4", "5", "6", "7", "8", "21", "22", "23", "24", "25", "26", "27");

        assertAuthorResults("\"adamcuk, m karel\"", "14",
                "2", "3", "4", "5", "6", "7", "8",
                "21", "22", "23", "24", "25", "26", "27");

        assertAuthorResults("\"adamchuk, m karel\"", "7",
                "41", "42", "43", "44", "45", "46", "47");

        assertAuthorResults("\"adamczuk, m karel\"", "0");
        assertAuthorResults("\"adamšuk, m karel\"", "0");
        assertAuthorResults("\"adamguk, m karel\"", "7",
                "61", "62", "63", "64", "65", "66", "67");


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
        assertAuthorResults("\"adamčuk, a b\"", "0");
        assertAuthorResults("\"adamcuk, a b\"", "0");
        assertAuthorResults("\"adamchuk, a b\"", "0");
        assertAuthorResults("\"adamczuk, a b\"", "0");

        assertAuthorResults("\"adamšuk, m k\"", "0");

        assertAuthorResults("\"adamguk, m k\"", "7",
                "61", "62", "63", "64", "65", "66", "67");


        /**
         * <surname>, <2>
         *
         * No expansion, because of the gap. Only transliteration
         *
         */


        assertAuthorResults("\"adamčuk, k\"", "6",
                "9", "10", "11", "28", "29", "30");

        assertAuthorResults("\"adamcuk, k\"", "6",
                "9", "10", "11", "28", "29", "30");

        assertAuthorResults("\"adamchuk, k\"", "3",
                "48", "49", "50");

        assertAuthorResults("\"adamczuk, k\"", "0");

        assertAuthorResults("\"adamšuk, k\"", "0");

        assertAuthorResults("\"adamguk, k\"", "3",
                "68", "69", "70");


        /**
         * <surname>, <2name>
         *
         * No expansion, because of the gap. Only transliteration
         *
         */


        assertAuthorResults("\"adamčuk, karel\"", "6",
                "9", "10", "11", "28", "29", "30");

        assertAuthorResults("\"adamcuk, karel\"", "6",
                "9", "10", "11", "28", "29", "30");

        assertAuthorResults("\"adamchuk, karel\"", "3",
                "48", "49", "50");

        assertAuthorResults("\"adamczuk, karel\"", "0");

        assertAuthorResults("\"adamšuk, karel\"", "0");

        assertAuthorResults("\"adamguk, karel\"", "3",
                "68", "69", "70");


        /**
         * <surname>, <2name> <1>
         *
         * The order is not correct, therefore no expansion. Only transliteration
         *
         */


        assertAuthorResults("\"adamčuk, karel m\"", "6",
                "9", "10", "11", "28", "29", "30");

        assertAuthorResults("\"adamcuk, karel m\"", "6",
                "9", "10", "11", "28", "29", "30");

        assertAuthorResults("\"adamchuk, karel m\"", "3",
                "48", "49", "50");

        assertAuthorResults("\"adamczuk, karel m\"", "0");

        assertAuthorResults("\"adamšuk, karel m\"", "0");

        assertAuthorResults("\"adamguk, karel m\"", "3",
                "68", "69", "70");


        /**
         * <surname>, <2name> <1name>
         *
         * The order is not correct. Only transliteration
         *
         */


        assertAuthorResults("\"adamčuk, karel molja\"", "6",
                "9", "10", "11", "28", "29", "30");

        assertAuthorResults("\"adamcuk, karel molja\"", "6",
                "9", "10", "11", "28", "29", "30");

        assertAuthorResults("\"adamchuk, karel molja\"", "3",
                "48", "49", "50");

        assertAuthorResults("\"adamczuk, karel molja\"", "0");

        assertAuthorResults("\"adamšuk, karel molja\"", "0");

        assertAuthorResults("\"adamguk, karel molja\"", "3",
                "68", "69", "70");


        /**
         * <surname>, <2> <1>
         *
         * The order is not correct, therefore no expansion. Only transliteration
         *
         */

        assertAuthorResults("\"adamčuk, k m\"", "6",
                "9", "10", "11", "28", "29", "30");

        assertAuthorResults("\"adamcuk, k m\"", "6",
                "9", "10", "11", "28", "29", "30");

        assertAuthorResults("\"adamchuk, k m\"", "3",
                "48", "49", "50");
        assertAuthorResults("\"adamczuk, k m\"", "0");
        assertAuthorResults("\"adamšuk, k m\"", "0");

        assertAuthorResults("\"adamguk, k m\"", "3",
                "68", "69", "70");


        /**
         * <surname>, <2> <1name>
         *
         * The order is not correct, therefore no expansion. Only transliteration
         *
         */


        assertAuthorResults("\"adamčuk, k molja\"", "6",
                "9", "10", "11", "28", "29", "30");

        assertAuthorResults("\"adamcuk, k molja\"", "6",
                "9", "10", "11", "28", "29", "30");

        assertAuthorResults("\"adamchuk, k molja\"", "3",
                "48", "49", "50");

        assertAuthorResults("\"adamczuk, k molja\"", "0");

        assertAuthorResults("\"adamšuk, k molja\"", "0");

        assertAuthorResults("\"adamguk, k molja\"", "3",
                "68", "69", "70");

        /**
         * <surname>, <1*>
         * <surname>, <1n*>
         *
         * No expansion should happen if the <part*> has more than 2 characters, otherwise
         * it should work as if <surname>, <1> was specified
         *
         */
        assertAuthorResults("\"adamčuk, m*\"", "21",
                "2", "3", "4", "5", "6", "7", "8", "21", "22", "23", "24", "25", "26", "27", "61", "62", "63", "64", "65", "66", "67");
        assertAuthorResults("\"adamchuk, m*\"", "7",
                "41", "42", "43", "44", "45", "46", "47");
        assertAuthorResults("\"adamcuk, m*\"", "21",
                "2", "3", "4", "5", "6", "7", "8", "21", "22", "23", "24", "25", "26", "27", "61", "62", "63", "64", "65", "66", "67");

        assertAuthorResults("\"adamczuk, m*\"", "21",
                "2", "3", "4", "5", "6", "7", "8", "21", "22", "23", "24", "25", "26", "27", "61", "62", "63", "64", "65", "66", "67");
        assertAuthorResults("\"adamšuk, m*\"", "21",
                "2", "3", "4", "5", "6", "7", "8", "21", "22", "23", "24", "25", "26", "27", "61", "62", "63", "64", "65", "66", "67");
        assertAuthorResults("\"adamguk, m*\"", "21",
                "2", "3", "4", "5", "6", "7", "8", "21", "22", "23", "24", "25", "26", "27", "61", "62", "63", "64", "65", "66", "67");
        assertAuthorResults("\"adamčuk, mo*\"", "3",
                "4", "5", "7");
        assertAuthorResults("\"adamcuk, mo*\"", "6",
                "4", "5", "7", "23", "24", "26");

        assertAuthorResults("\"adamchuk, mo*\"", "3",
                "43", "44", "46");

        assertAuthorResults("\"adamczuk, mo*\"", "0");

        assertAuthorResults("\"adamšuk, mo*\"", "0");

        assertAuthorResults("\"adamguk, mo*\"", "3",
                "63", "64", "66");


        /**
         * <surname>, <2*>
         * <surname>, <2n*>
         *
         * No expansion should happen if the <part*> has more than 2 characters, otherwise
         * it should work only if such a patter is in the synonym list (and there is none)
         *
         */

        assertAuthorResults("\"adamčuk, k*\"", "6",
                "9", "10", "11", "28", "29", "30");
        assertAuthorResults("\"adamcuk, k*\"", "6",
                "9", "10", "11", "28", "29", "30");
        assertAuthorResults("\"adamchuk, k*\"", "3", "48", "49", "50");
        assertAuthorResults("\"adamczuk, k*\"", "0");
        assertAuthorResults("\"adamšuk, k*\"", "0");
        assertAuthorResults("\"adamguk, k*\"", "3", "68", "69", "70");

        assertAuthorResults("\"adamčuk, ka*\"", "2", "9", "10");
        assertAuthorResults("\"adamcuk, ka*\"", "4", "9", "10", "28", "29");
        assertAuthorResults("\"adamchuk, ka*\"", "2", "48", "49");
        assertAuthorResults("\"adamczuk, ka*\"", "0");
        assertAuthorResults("\"adamšuk, ka*\"", "0");
        assertAuthorResults("\"adamguk, ka*\"", "2", "68", "69");


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

        assertAuthorResults("forman", "7", "110", "111", "112", "113", "115", "116", "117");
        assertAuthorResults("jones", "15",
                "110", "111", "112", "113", "114", "115", "117",
                "120", "121", "122", "123", "124", "125", "126", "127");
        assertAuthorResults("\"forman, c\"", "7",
                "110", "111", "112", "113", "115", "116", "117");
        assertAuthorResults("\"jones, c\"", "7",
                "110", "111", "112", "113", "114", "115", "117");
        assertAuthorResults("\"jones, christine\"", "6",
                "110", "111", "112", "113", "115", "117");
        assertAuthorResults("\"forman, christine\"", "6",
                "110", "111", "112", "113", "115", "117");


        // Nickname and transliteration synonyms must preserve consumer-visible
        // recall across the indexed Müller/Mueller fixtures.
        assertAuthorResults("\"Muller, William\"", "3", "100", "101", "102");
        assertAuthorResults("\"Müller, William\"", "3", "100", "101", "102");
        assertAuthorResults("\"Mueller, William\"", "3", "100", "101", "102");
        assertAuthorResults("\"Müller, Bill\"", "3", "100", "101", "102");
        assertAuthorResults("\"Müller, W\"", "3", "100", "101", "102");
        assertAuthorResults("\"Muller, Z\"", "0");
        assertAuthorResults("Bill", "0");

        // A default-field term must remain a required clause, rather than
        // disappearing into the fielded author expression.
        assertQ(req("q", "author:\"Muller, William\" William", "df", "all"),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='100']",
                "//doc/str[@name='id'][.='101']");
        assertQ(req("q", "author:\"Muller, William\" boooo", "df", "all"),
                "//*[@numFound='0']");



    
    
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



    }


    private void assertAuthorResults(String query, String numFound, String... recids) throws Exception {
        String[] checks = new String[recids.length + 1];
        checks[0] = "//*[@numFound='" + numFound + "']";
        for (int i = 0; i < recids.length; i++) {
            checks[i + 1] = "//doc/str[@name='id'][.='" + recids[i] + "']";
        }
        assertQ(req("defType", "aqp", "fl", "id," + author_field, "rows", "100",
                        "q", author_field + ":" + query),
                checks);
    }

    // Uniquely for Junit 3
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeAuthorParsing.class);
    }



}
