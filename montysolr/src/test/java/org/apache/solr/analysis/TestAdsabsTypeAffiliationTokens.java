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
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.SynonymQuery;
import org.apache.lucene.search.TermQuery;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Paths;


/**
 * Test for the affiliation_text type
 *
 */
public class TestAdsabsTypeAffiliationTokens extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = getSchemaFile();

        configString = "solrconfig.xml";

        SolrTestSetup.initCore(configString, schemaString);
    }

    public static String getSchemaFile() {

        /*
         * For purposes of the test, we make a copy of the schema.xml, and create
         * our own synonym files
         */

        String configFile;
        try {
            configFile = SolrTestSetup.getRepoUrl(
                    Paths.get("deploy/adsabs/server/solr/collection1/conf/schema.xml")).getFile();
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }

        File newConfig;
        try {

            newConfig = duplicateFile(new File(configFile));


            File simpleTokenSynonymsFile = createTempFile(
                    "id1,id2\n"
                            + "ror.1;foo;bar\n"
                            + "A00001;Aalborg U;Aalborg University;RID1004;04m5j1k67;000000010742471X;Q601956;grid.5117.2;\n\n"
                            + "A00002;Aarhus U;Aarhus University;RID1006;01aj84f44;0000000119562722;Q924265;grid.7048.b;\n"
                            //+ "A01400;SI/CfA;Center for Astrophysics | Harvard and Smithsonian;Harvard Smithsonian Center for Astrophysics;RID61814;03c3r2d17;Q1133697;grid.455754.2\n"
                            + "AX;SI\n"
                            + "AB=>CfA\n"
                            + "A01400;CfA;SI/CfA;Harvard U/CfA;Center for Astrophysics Harvard and Smithsonian;Harvard Smithsonian Center for Astrophysics;RID61814;03c3r2d17;Q1133697;grid.455754.2\n"
                            + "A01397;SI;Smithsonian Institution;RID8264;01pp8nd67;0000000087163312;Q131626;grid.1214.6");

            replaceInFile(newConfig, "synonyms=\"aff_id.synonyms\"",
                    "synonyms=\"" + simpleTokenSynonymsFile.getAbsolutePath() + "\"");

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }

        return newConfig.getAbsolutePath();
    }

    public void test() throws Exception {

        assertU(addDocs("institution", "foo bar", "institution", "bar baz/hey"));
        assertU(addDocs("institution", "Kavli Institute/Dept of Physics"));
        assertU(addDocs("institution", "U Catania/Dep Phy Ast; -",
                "institution", "U Catania/Dep Phy Ast; -; -; INFN/Catania",
                "institution", "U Catania/Dep Phy Ast; -"
        ));
        assertU(addDocs(
                "institution", "SI/CfA; Harvard U/CfA",
                "institution", "Harvard U/Phys; Brown U/Ast",
                "aff", "SI/CfA")
        );
        assertU(addDocs(
                "institution", "Harvard U/Law; -",
                "institution", "SI/CfA; Harvard U/CfA",
                "aff", "SI/CfA")
        );
        assertU(commit());

        assertQueryEquals(req("q", "aff_id:https\\://ror.org/ror.1"),
                "Synonym(aff_id:bar aff_id:foo aff_id:ror.1)",
                SynonymQuery.class
        );
        assertQueryEquals(req("q", "aff_id:\"https://ror.org/ror.1\""),
                "Synonym(aff_id:bar aff_id:foo aff_id:ror.1)",
                SynonymQuery.class
        );

        // test synonyms
        assertQueryEquals(req("q", "aff_id:\"ror.1\""),
                "Synonym(aff_id:bar aff_id:foo aff_id:ror.1)",
                SynonymQuery.class
        );
        assertQueryEquals(req("q", "aff_id:\"ROR.1\""),
                "Synonym(aff_id:bar aff_id:foo aff_id:ror.1)",
                SynonymQuery.class
        );
        assertQueryEquals(req("q", "aff_id:\"A00001\""),
                "Synonym(aff_id:000000010742471x aff_id:04m5j1k67 aff_id:a00001 aff_id:aalborg u aff_id:aalborg university aff_id:grid.5117.2 aff_id:q601956 aff_id:rid1004)",
                SynonymQuery.class
        );
        assertQueryEquals(req("q", "aff_id:\"a00001\""),
                "Synonym(aff_id:000000010742471x aff_id:04m5j1k67 aff_id:a00001 aff_id:aalborg u aff_id:aalborg university aff_id:grid.5117.2 aff_id:q601956 aff_id:rid1004)",
                SynonymQuery.class
        );
        assertQueryEquals(req("q", "aff_id:\"04m5j1k67\""),
                "Synonym(aff_id:000000010742471x aff_id:04m5j1k67 aff_id:a00001 aff_id:aalborg u aff_id:aalborg university aff_id:grid.5117.2 aff_id:q601956 aff_id:rid1004)",
                SynonymQuery.class
        );
        assertQueryEquals(req("q", "aff_id:\"Aalborg U\""),
                "Synonym(aff_id:000000010742471x aff_id:04m5j1k67 aff_id:a00001 aff_id:aalborg u aff_id:aalborg university aff_id:grid.5117.2 aff_id:q601956 aff_id:rid1004)",
                SynonymQuery.class
        );

        // make sure docs are there
        assertQ(req("q", "*:*"), "//*[@numFound>='2']");

        // query parsing tests
        assertQueryEquals(req("q", "institution:\"Foo Bar\""),
                "institution:foo bar",
                TermQuery.class
        );

        // test matches
        assertQ(req("q", "institution:\"foo bar\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='0']"
        );
        assertQ(req("q", "institution:\"bar baz\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='0']"
        );
        assertQ(req("q", "institution:HEY"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='0']"
        );
        assertQ(req("q", "institution:\"bar BAZ/heY\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='0']"
        );

        // only match full tokens
        assertQ(req("q", "institution:foo"), "//*[@numFound='0']");
        assertQ(req("q", "institution:\"baz/hey\""), "//*[@numFound='0']");


        // check the affiliation is there stored as one string
        assert h.query(req("q", "institution:\"Kavli Institute/Dept of Physics\"", "fl", "institution"))
                .contains("<str>Kavli Institute/Dept of Physics</str>"
                );

        //"U Catania/Dep Phy Ast; -; -; INFN/Catania"
        assertQ(req("q", "institution:\"U Catania\""), "//*[@numFound='1']");
        assertQ(req("q", "institution:\"Dep Phy Ast\""), "//*[@numFound='1']");
        assertQ(req("q", "institution:\"U Catania/Dep Phy Ast\""), "//*[@numFound='1']");

        // must not happen if positionIncrementGap > 0
        assertQ(req("q", "institution:\"Catania/U Catania\""), "//*[@numFound='0']");

        // this is ok, it's expected
        assertQ(req("q", "institution:\"-;INFN\""), "//*[@numFound='1']");
        assertQ(req("q", "institution:\"-;-\""), "//*[@numFound='1']");

        assertQ(req("q", "pos(institution:\"U Catania/Dep Phy Ast\", 1)"), "//*[@numFound='1']");
        assertQ(req("q", "pos(institution:\"U Catania\", 1)"), "//*[@numFound='1']");
        assertQ(req("q", "pos(institution:\"Dep Phy Ast\", 1)"), "//*[@numFound='1']");
        assertQ(req("q", "pos(institution:\"-\", 1)"), "//*[@numFound='2']");
        assertQ(req("q", "pos(institution:\"INFN/Catania\", 1)"), "//*[@numFound='0']");
        assertQ(req("q", "pos(institution:\"INFN\", 1)"), "//*[@numFound='0']");
        assertQ(req("q", "pos(institution:\"Catania\", 1)"), "//*[@numFound='0']");

        assertQ(req("q", "pos(institution:\"INFN/Catania\", 2)"), "//*[@numFound='1']");
        assertQ(req("q", "pos(institution:\"INFN\", 2)"), "//*[@numFound='1']");
        assertQ(req("q", "pos(institution:\"Catania\", 2)"), "//*[@numFound='1']");

        // search parts of the affiliation
        assertQ(req("q", "institution:\"SI\""), "//*[@numFound='2']");
        assertQ(req("q", "institution:\"CfA\""), "//*[@numFound='2']");
        assertQ(req("q", "institution:\"Harvard U\""), "//*[@numFound='2']");
        assertQ(req("q", "institution:\"Law\""), "//*[@numFound='1']");
        assertQ(req("q", "institution:\"Phys\""), "//*[@numFound='1']");

        // search parts (but honour position)
        assertQ(req("q", "pos(institution:\"SI\", 1)"), "//*[@numFound='1']");
        assertQ(req("q", "pos(institution:\"CfA\", 1)"), "//*[@numFound='1']");
        assertQ(req("q", "pos(institution:\"Harvard U\", 2)"), "//*[@numFound='2']");
        assertQ(req("q", "pos(institution:\"CfA\", 2)"), "//*[@numFound='1']");

        // search parent/child
        assertQ(req("q", "institution:\"SI/CfA\""), "//*[@numFound='2']");

        // do the same but as phrase; it should fail because the parser WILL NOT
        // treat empty space as a delimiter; it considers it part of the token
        // like 'Harvard U'
        assertQ(req("q", "institution:\"SI CfA\""), "//*[@numFound='0']");

        // proximity operator however should yield the record
        assertQ(req("q", "institution:\"SI\" NEAR1 institution:\"CfA\""), "//*[@numFound='2']");

        // but not mix up insitutions that were separated by ';' (those affiliations
        // belong to different authors/persons)
        assertQ(req("q", "institution:\"Law\" NEAR5 institution:\"CfA\""), "//*[@numFound='0']");

        // one person however can have multiple affiliations; and they can be searched via proximity
        assertQ(req("q", "institution:\"Phys\" NEAR5 institution:\"Ast\""), "//*[@numFound='1']");

        // institution uses only normalization; identifiers remain aff_id synonyms.
        assertQ(req("q", "institution:\"A01400\""), "//*[@numFound='0']");
        // what is the meaning of the pipe? (|) -- it forces our parser to treat the query
        // as a regex; to not do that we have to set aqp.regex.disallowed.fields
        //assertQ(req("q", "institution:\"Center for Astrophysics | Harvard and Smithsonian\"",
        //    "aqp.regex.disallowed.fields", "institution"), "//*[@numFound='2']");
        assertQ(req("q", "institution:\"Center for Astrophysics Harvard and Smithsonian\"",
                "aqp.regex.disallowed.fields", "institution"), "//*[@numFound='0']");

        assertQ(req("q", "institution:\"AX/AB\""), "//*[@numFound='0']");

        // and check we still retrieve the same docs
        assertQ(req("q", "institution:\"SI/CfA\"",
                        "aqp.multiphrase.keep_one", "SYNONYM",
                        "aqp.multiphrase.keep_one.ignore.fields", "aff_id,aff_raw,institution"),
                "//*[@numFound='2']");






    }



    // Uniquely for Junit 3
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeAffiliationTokens.class);
    }

    public void testInstitutionHierarchyLookup() throws Exception {
        assertU(delQ("*:*"));
        assertU(commit("waitSearcher", "true", "expungeDeletes", "true"));
        try {
            assertU(addDocs("institution", "foo bar", "institution", "bar baz/hey"));
            assertU(addDocs("institution", "Kavli Institute/Dept of Physics"));
            assertU(addDocs("institution", "U Catania/Dep Phy Ast; -",
                    "institution", "U Catania/Dep Phy Ast; -; -; INFN/Catania",
                    "institution", "U Catania/Dep Phy Ast; -"
            ));
            assertU(addDocs(
                    "institution", "SI/CfA; Harvard U/CfA",
                    "institution", "Harvard U/Phys; Brown U/Ast",
                    "aff", "SI/CfA")
            );
            assertU(addDocs(
                    "institution", "Harvard U/Law; -",
                    "institution", "SI/CfA; Harvard U/CfA",
                    "aff", "SI/CfA")
            );
            assertU(addDocs("institution", "Université de Montréal/Phys Dépt"));
            assertU(addDocs("institution", "IMCCE/Observatoire de Paris"));
            assertU(adoc("id", "1261", "bibcode", "b1261", "institution", "Harvard U/Math"));
            assertU(adoc("id", "1262", "bibcode", "b1262", "institution", "Other U/CfA"));
            assertU(commit());


            assertQ(req("q", "institution:\"Universite de Montreal\""),
                    "//*[@numFound='1']",
                    "//doc/str[@name='id'][.='5']");
            assertQ(req("q", "institution:\"Phys Dept\""),
                    "//*[@numFound='1']",
                    "//doc/str[@name='id'][.='5']");
            assertQ(req("q", "institution:\"IMCCE\""),
                    "//*[@numFound='1']",
                    "//doc/str[@name='id'][.='6']");
            // Full hierarchy lookup is exact: siblings sharing only Parent or Child
            // must not satisfy the query.
            assertQ(req("q", "institution:\"Harvard U/CfA\""),
                    "//*[@numFound='2']",
                    "//doc/str[@name='id'][.='3']",
                    "//doc/str[@name='id'][.='4']",
                    "not(//doc/str[@name='id'][.='1261' or .='1262'])"
            );
            assertQ(req("q", "institution:observatoire"), "//*[@numFound='0']");
            // Full, parent, and child alternatives share one position; a phrase
            // must not treat the hierarchy components as successive terms.
            assertQ(req("q", "institution:\"IMCCE Observatoire\""), "//*[@numFound='0']");
        } finally {
            assertU(delQ("*:*"));
            assertU(commit("waitSearcher", "true", "expungeDeletes", "true"));
        }
    }
}
