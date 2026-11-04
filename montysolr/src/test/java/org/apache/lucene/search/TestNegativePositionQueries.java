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
                AdsConfig.F.AUTHOR, "Author, C",
                "aff", "aff1",
                "aff", "aff2",
                "aff", "aff3",
                "institution", "InstX",
                "orcid_pub", "1111-2222-3333-4444"
        ));
        assertU(adoc(AdsConfig.F.ID, "2", AdsConfig.F.BIBCODE, "yyyyyyyyyyyyy",
                "orcid_user", "0000-0002-4110-3999"
        ));
        assertU(adoc(AdsConfig.F.ID, "3", AdsConfig.F.BIBCODE, "zzzzzzzzzzzzz",
                "orcid_other", "0000-0002-4110-4999"
        ));
        assertU(adoc(AdsConfig.F.ID, "4", AdsConfig.F.BIBCODE, "aaaaaaaaaaaaa",
                "orcid_pub", "scorebranch"
        ));
        assertU(adoc(AdsConfig.F.ID, "5", AdsConfig.F.BIBCODE, "bbbbbbbbbbbbb",
                "orcid_other", "scorebranch"
        ));
        assertU(adoc(AdsConfig.F.ID, "6", AdsConfig.F.BIBCODE, "ccccccccccccc",
                "orcid_pub", "phrase value"
        ));

        String[] longDocument = new String[4 + 4 * 102];
        longDocument[0] = AdsConfig.F.ID;
        longDocument[1] = "8";
        longDocument[2] = AdsConfig.F.BIBCODE;
        longDocument[3] = "ddddddddddddd";
        for (int i = 0; i < 102; i++) {
            longDocument[4 + 4 * i] = "aff";
            longDocument[5 + 4 * i] = i == 101 ? "sentinel omega" : "alpha beta";
            longDocument[6 + 4 * i] = "title";
            longDocument[7 + 4 * i] = i == 101 ? "singlecontrol" : "filler";
        }
        assertU(adoc(longDocument));

        assertU(adoc(AdsConfig.F.ID, "9", AdsConfig.F.BIBCODE, "eeeeeeeeeeeee",
                "aff", "edge",
                "aff", " ",
                "aff", "emptyend",
                "title", "alpha",
                "title", "the",
                "title", "omega"
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
    public void testAffiliationSearch() {
        assertQ(req("q", "pos(aff:\"aff2\", -2)"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(aff:\"aff2\", 0, -1)"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(aff:\"aff3\", 0, -2)"),
                "//*[@numFound='0']"
        );
    }

    @Test
    public void testLongMultiTokenValuesUseActualBoundaries() {
        assertQ(req("q", "pos(aff:sentinel, -1)"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(aff:\"sentinel omega\", -1)"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(aff:alpha, -1)"),
                "//*[@numFound='0']"
        );

        assertQ(req("q", "pos(aff:alpha, -2)"),
                "//*[@numFound='1']"
        );
    }

    @Test
    public void testSingleTokenVirtualFieldKeepsLongGapBoundaries() {
        assertQ(req("q", "pos(abs:singlecontrol, -1)"),
                "//*[@numFound='1']"
        );
    }

    @Test
    public void testEmptyAndStopwordOnlyValuesCountAsBoundaries() {
        assertQ(req("q", "pos(aff:emptyend, -1)"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(aff:edge, -3)"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(aff:edge, -3, -1)"),
                "//*[@numFound='1']"
        );

        assertQ(req("q", "pos(aff:emptyend, -2)"),
                "//*[@numFound='0']"
        );

        assertQ(req("q", "pos(title:omega, -1)"),
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
        assertQ(req("q", "pos(institution:\"InstX\", -1)"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='1']"
        );

        assertQ(req("q", "pos(orcid:\"1111-2222-3333-4444\", -1)"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='1']"
        );
        assertQ(req("q", "pos(orcid:\"0000-0002-4110-3999\", -1)"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='2']"
        );
        assertQ(req("q", "pos(orcid:\"0000-0002-4110-4999\", -1)"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='3']"
        );
        assertQ(req("q", "pos(orcid:scorebranch, 1)", "rows", "2", "sort", "score desc", "fl", "id,score"),
                "//*[@numFound='2']",
                "//result/doc[1]/str[@name='id'][.='4']",
                "//result/doc[2]/str[@name='id'][.='5']"
        );
        assertQ(req("q", "pos(orcid:\"phrase value\", 1, 2)"),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='6']"
        );
        assertU(adoc(AdsConfig.F.ID, "7", AdsConfig.F.BIBCODE, "ddddddddddddd",
                AdsConfig.F.TYPE_ADS_TEXT, "Hubble Space Telescope"
        ));
        assertU(commit("waitSearcher", "true"));
        try {
            assertQ(req("q", "pos(abs:HST,1)"),
                    "//*[@numFound='1']",
                    "//doc/str[@name='id'][.='7']");
        } finally {
            assertU(delI("7"));
            assertU(commit("waitSearcher", "true"));
        }
        assertQ(req("q", "pos(orcid:(\"0000-0002-4110-3999\" AND missing), -1)"),
                "//*[@numFound='0']"
        );
        assertQ(req("q", "pos(orcid:(\"0000-0002-4110-3999\" NOT \"0000-0002-4110-3999\"), -1)"),
                "//*[@numFound='0']"
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
