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
import org.apache.lucene.search.TermQuery;
import org.junit.BeforeClass;

/**
 * Test for the normalized_string_ascii type
 *
 */
public class TestAdsabsTypeNormalizedStringAscii extends MontySolrQueryTestCase {


    @BeforeClass
    public static void beforeClass() throws Exception {
        //System.setProperty("solr.directoryFactory", "solr.StandardDirectoryFactory");
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

        String[] fs = new String[]{"bibcode", "identifier", "title"}; // single-val-string, multi-val-string, text

        assertU(addDocs(fs, "Bílá kobyla skočila přes čtyřista"));
        assertU(addDocs(fs, "třicet-tři stříbrných střech"));
        assertU(addDocs(fs, "A ještě TřistaTřicetTři stříbrných stovek"));
        assertU(addDocs(fs, "one two three"));
        assertU(addDocs(fs, "este-c'est que"));
        assertU(addDocs(fs, "568"));

        assertU(commit("waitSearcher", "true"));

        assertQ(req("q", "*:*"), "//*[@numFound='6']");

        assertQueryEquals(req("q", "bibcode:Bílá", "qt", "aqp"), "bibcode:bila", TermQuery.class);
        assertQueryEquals(req("q", "bibcode:Bila-bila", "qt", "aqp"), "bibcode:bilabila", TermQuery.class);

        assertQ(req("q", "bibcode:Bílá*"),
                "//*[@numFound='1']",
                "//doc[1]/str[@name='id'][.='0']");
        assertQ(req("q", "identifier:Bílá*"),
                "//*[@numFound='1']",
                "//doc[1]/str[@name='id'][.='0']");
        assertQ(req("q", "title:Bílá*"),
                "//*[@numFound='1']",
                "//doc[1]/str[@name='id'][.='0']");

        assertQ(req("q", "bibcode:kobyla"),
                "//*[@numFound='0']");
        assertQ(req("q", "identifier:kobyla"),
                "//*[@numFound='0']");
        assertQ(req("q", "title:kobyla"),
                "//*[@numFound='1']",
                "//doc[1]/str[@name='id'][.='0']");


        assertQ(req("q", "bibcode:Bílá-kobyla*"),
                "//*[@numFound='1']",
                "//doc[1]/str[@name='id'][.='0']");
        assertQ(req("q", "identifier:Bílá-kobyla*"),
                "//*[@numFound='1']",
                "//doc[1]/str[@name='id'][.='0']");
        assertQ(req("q", "title:Bílá-kobyla*"),
                "//*[@numFound='0']");

        assertQ(req("q", "bibcode:Bílá-kobyla"),
                "//*[@numFound='0']");
        assertQ(req("q", "identifier:Bílá-kobyla"),
                "//*[@numFound='0']");
        assertQ(req("q", "title:Bílá-kobyla"),
                "//*[@numFound='1']");

        assertQ(req("q", "bibcode:\"one two three\""),
                "//*[@numFound='1']",
                "//doc[1]/str[@name='id'][.='3']");
        assertQ(req("q", "bibcode:\"este-c'est que\""),
                "//*[@numFound='1']",
                "//doc[1]/str[@name='id'][.='4']");
        assertQ(req("q", "bibcode:568"),
                "//*[@numFound='1']",
                "//doc[1]/str[@name='id'][.='5']");
    }


    // Uniquely for Junit 3
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAdsabsTypeNormalizedStringAscii.class);
    }
}
