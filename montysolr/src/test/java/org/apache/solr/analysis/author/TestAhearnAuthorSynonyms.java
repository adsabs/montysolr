package org.apache.solr.analysis.author;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.adsabs.solr.AdsConfig.F;
import org.junit.BeforeClass;

public class TestAhearnAuthorSynonyms extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "schema.xml";
        configString = "solrconfig.xml";
        SolrTestSetup.initCore(configString, schemaString);
    }

    public void testApostropheQueryIncludesAhearnSpelling() throws Exception {
        assertU(adoc(F.ID, "1551", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "A'Hearn, M"));
        assertU(adoc(F.ID, "1552", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Ahearn, M"));
        assertU(adoc(F.ID, "1553", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Ahearn, A"));
        assertU(commit());

        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"A'Hearn, M\""),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='1551']",
                "//doc/str[@name='id'][.='1552']",
                "not(//doc/str[@name='id'][.='1553'])");
        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"Ahearn, M\""),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='1551']",
                "//doc/str[@name='id'][.='1552']",
                "not(//doc/str[@name='id'][.='1553'])");
    }
}
