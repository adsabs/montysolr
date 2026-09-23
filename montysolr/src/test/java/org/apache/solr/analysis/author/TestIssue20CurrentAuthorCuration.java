package org.apache.solr.analysis.author;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.adsabs.solr.AdsConfig.F;
import org.junit.BeforeClass;

import java.nio.file.Paths;

/**
 * Consumer regression for the named issue-20 curation rows. This test loads
 * the repository's actual schema and synonym resources rather than a copied
 * fixture, so a fresh foundation fails before the curated data change.
 */
public class TestIssue20CurrentAuthorCuration extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = SolrTestSetup.getRepoUrl(Paths.get(
                "deploy/adsabs/server/solr/collection1/conf/schema.xml")).getFile();
        configString = "solrconfig.xml";
        SolrTestSetup.initCore(configString, schemaString);
    }

    public void testIssue20CurrentCuratedAuthorConsumers() throws Exception {
        assertU(adoc(F.ID, "800", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Berta, Zachory"));
        assertU(adoc(F.ID, "801", F.BIBCODE, "2014arXiv1409.0891I", F.AUTHOR, "Berta-Thompson, Zachory K"));
        assertU(adoc(F.ID, "802", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Berta-York, Zachory K"));

        assertU(adoc(F.ID, "810", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Boesgaard, Ann M"));
        assertU(adoc(F.ID, "811", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Merchant Boesgaard, Ann M"));
        assertU(adoc(F.ID, "812", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Boesgaard, Ann"));
        assertU(adoc(F.ID, "813", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Merchant Boesgaard, Ann"));

        assertU(adoc(F.ID, "820", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Pustilnik, S"));
        assertU(adoc(F.ID, "821", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Pustil'nik, S"));
        assertU(adoc(F.ID, "822", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "O'Brian, S"));
        assertU(adoc(F.ID, "823", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Obrian, S"));
        assertU(commit());

        assertQ(req("defType", "aqp", "debugQuery", "true", "fl", "id,author", "rows", "100",
                        "q", "author:\"Berta, Z\""),
                ids("800", "801"));
        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"Berta, Zachory\""),
                ids("800", "801"));
        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"Berta, Zachory\" AND bibcode:2014arXiv1409.0891I"),
                ids("801"));
        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"Berta, Zachory K\""),
                ids("800", "801"));

        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"Merchant Boesgaard, Ann M\""),
                ids("810", "811", "812", "813"));
        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"Merchant Boesgaard, Ann\""),
                ids("810", "811", "812", "813"));
        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"Merchant Boesgaard, A\""),
                ids("810", "811", "812", "813"));

        assertU(adoc(F.ID, "816", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Boesgaard, Alice"));
        assertU(adoc(F.ID, "817", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Merchant Boesgaard, Alice"));
        assertU(commit());

        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"Merchant Boesgaard, Ann M\""),
                ids("810", "811", "812", "813"));
        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"Merchant Boesgaard, Ann\""),
                ids("810", "811", "812", "813"));
        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"Boesgaard, Ann M\""),
                ids("810", "811", "812", "813"));
        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"Merchant Boesgaard, A\""),
                ids("810", "811", "812", "813", "817"));

        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"Pustilnik, S\""),
                ids("820", "821"));
        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"Pustil'nik, S\""),
                ids("820", "821"));
        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"O'Brian, S\""),
                ids("822"));
        assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100",
                        "q", "author:\"Obrian, S\""),
                ids("823"));

    }

    private String[] ids(String... ids) {
        String[] checks = new String[ids.length + 1];
        checks[0] = "//*[@numFound='" + ids.length + "']";
        for (int i = 0; i < ids.length; i++) {
            checks[i + 1] = "//doc/str[@name='id'][.='" + ids[i] + "']";
        }
        return checks;
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestIssue20CurrentAuthorCuration.class);
    }
}
