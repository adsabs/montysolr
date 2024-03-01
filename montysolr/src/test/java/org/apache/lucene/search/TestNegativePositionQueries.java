package org.apache.lucene.search;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.adsabs.solr.AdsConfig;
import org.junit.BeforeClass;

import java.io.IOException;

public class TestNegativePositionQueries extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "deploy/adsabs/server/solr/collection1/conf/schema.xml";

        configString = "deploy/adsabs/server/solr/collection1/conf/solrconfig.xml";

        SolrTestSetup.initCore(configString, schemaString);
    }

    public void test() {
        assertU(adoc(AdsConfig.F.ID, "1", AdsConfig.F.BIBCODE, "xxxxxxxxxxxxx",
                AdsConfig.F.AUTHOR, "Author, A",
                AdsConfig.F.AUTHOR, "Author, B",
                AdsConfig.F.AUTHOR, "Author, C"
        ));

        assertU(commit("waitSearcher", "true"));

        assertQ(req("q", "author:\"Author\""),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(author:\"Author, A\", 1)"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(author:\"Author, C\", \"-1\")"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(author:\"Author, C\", -1)"),
                "//*[@numFound='1']"
        );
    }
}
