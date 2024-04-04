package org.apache.lucene.search;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.adsabs.solr.AdsConfig;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestNegativePositionQueries extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "deploy/adsabs/server/solr/collection1/conf/schema.xml";

        configString = "deploy/adsabs/server/solr/collection1/conf/solrconfig.xml";

        SolrTestSetup.initCore(configString, schemaString);

        assertU(adoc(AdsConfig.F.ID, "1", AdsConfig.F.BIBCODE, "xxxxxxxxxxxxx",
                AdsConfig.F.AUTHOR, "Author, A",
                AdsConfig.F.AUTHOR, "Author, B",
                AdsConfig.F.AUTHOR, "Author, C"
        ));

        assertU(commit("waitSearcher", "true"));
    }

    @Test
    public void testBasicPositionSearch() {
        assertQ(req("q", "author:\"Author\""),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(author:\"Author, A\", 1)"),
                "//*[@numFound='1']"
        );
    }

    @Test
    public void testExactNegativePositionSearch() {
        assertQ(req("q", "pos(author:\"Author, C\", \"-1\")"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(author:\"Author, C\", -1, -1)"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(author:\"Author, B\", \"-2\")"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(author:\"Author, A\", \"-2\")"),
                "//*[@numFound='0']"
        );
    }

    @Test
    public void testEndOfFieldMatches() {
        assertQ(req("q", "pos(author:\"Author, B\", 0, -10)"),
                "//*[@numFound='0']"
        );

        assertQ(req("q", "pos(author:\"Author, B\", 0, -1)"),
                "//*[@numFound='1']"
        );
    }

    @Test
    public void testStartOfFieldMatches() {
        assertQ(req("q", "pos(author:\"Author, B\", \"-3\", \"-1\")"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(author:\"Author, B\", \"-5\", \"-1\")"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(author:\"Author, B\", -3, -1)"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(author:\"Author, B\", -5, -1)"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(author:\"Author, A\", -2, -1)"),
                "//*[@numFound='0']"
        );

        assertQ(req("q", "pos(author:\"Author, A\", -3, -1)"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(author:\"Author, A\", -10, -1)"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(author:\"Author, C\", 0, -1)"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(author:\"Author, C\", 0, -2)"),
                "//*[@numFound='0']"
        );
    }

    @Test
    public void testCrossedPositionDoesNotMatch() {
        assertQ(req("q", "pos(author:\"Author, C\", -1, -2)"),
                "//*[@numFound='0']"
        );
    }
}
