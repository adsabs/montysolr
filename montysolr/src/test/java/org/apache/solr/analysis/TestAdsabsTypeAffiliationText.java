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
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.PhraseQuery;
import org.junit.BeforeClass;

/**
 * Test for the affiliation_text type
 *
 */
public class TestAdsabsTypeAffiliationText extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {

        makeResourcesVisible(Thread.currentThread().getContextClassLoader(), MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/server/solr/collection1/conf",
                MontySolrSetup.getSolrHome() + "/example/solr/collection1");

        System.setProperty("solr.allow.unsafe.resourceloading", "true");


        schemaString = MontySolrSetup.getMontySolrHome()
                + "/contrib/examples/adsabs/server/solr/collection1/conf/schema.xml";

        configString = MontySolrSetup.getMontySolrHome()
                + "/contrib/examples/adsabs/server/solr/collection1/conf/solrconfig.xml";

        initCore(configString, schemaString, MontySolrSetup.getSolrHome()
                + "/example/solr");
    }


    public void test() throws Exception {

        assertU(addDocs("aff", "W.K. Kellogg Radiation Laboratory, California Institute of Technology, Pasadena, CA 91125, USA"));
        assertU(addDocs("aff", "W.K. Kellogg <xfoo> Radiation Laboratory, California Institute of Technology, Pasadena, CA(91125), USA"));
        assertU(addDocs("aff", "IMCCE/Observatoire de Paris"));
        assertU(addDocs("aff", "INAF - Osservatorio Astronomico di Brera, via E. Bianchi 46, I-23807 Merate, Italy;",
                "aff", "INAF - IASF Milano, via E. Bassini 15, I-20133 Milano, Italy"));
        assertU(addDocs("aff", "Instituto de Astrofísica de Andalucía (IAA-CSIC) foo:doo"));
        assertU(addDocs("aff", "foo1", "aff", "foo2", "aff", "-", "aff", "foo4"));

        assertU(commit());

        //dumpDoc(null, "aff");
        //System.err.println(h.query(req("q", "aff:foo1")));

        assertQ(req("q", "*:*"), "//*[@numFound>='2']");

        assertQueryEquals(req("q", "aff:\"Institut d’Astrophysique\"", "aqp.multiphrase.keep_one", "SYNONYM", "qt", "aqp"),
                "(aff:\"institut d'astrophysique\" | aff:\"institut d astrophysique\")",
                DisjunctionMaxQuery.class
        );

        assertQ(req("q", "aff:xfoo"), "//*[@numFound='0']");

        assertQueryEquals(req("q", "aff:\"Pasadena, CA 91125\"", "qt", "aqp"),
                "aff:\"pasadena acr::ca 91125\"",
                PhraseQuery.class
        );
        assertQueryEquals(req("q", "aff:\"Pasadena, CA(91125)\"", "qt", "aqp"),
                "aff:\"pasadena acr::ca 91125\"",
                PhraseQuery.class
        );

        assertQ(req("q", "aff:\"Pasadena, CA 91125\""),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='0']",
                "//doc/str[@name='id'][.='1']"
        );

        assertQ(req("q", "aff:IMCCE"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='2']"
        );

        assertQ(req("q", "aff:imcce"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='2']"
        );

        assertQ(req("q", "aff:IASF"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='3']"
        );

        assertQ(req("q", "aff:iasf"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='3']"
        );

        assertQ(req("q", "aff:IAA-CSIC"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='4']"
        );

        assertQ(req("q", "aff:IAACSIC"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='4']"
        );
        assertQ(req("q", "aff:iaa-csic"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='4']"
        );

        assertQ(req("q", "aff:\"INAF - IASF\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='3']"
        );
        assertQ(req("q", "aff:\"inaf - iasf\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='3']"
        );

        assert h.query(req("q", "aff:foo1", "fl", "aff", "indent", "false"))
                .contains("<arr name=\"aff\">" +
                        "<str>foo1</str>" +
                        "<str>foo2</str>" +
                        "<str>-</str>" +
                        "<str>foo4</str>"
                );

    }


    // Uniquely for Junit 3
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeAffiliationText.class);
    }
}
