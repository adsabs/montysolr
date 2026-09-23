package org.apache.solr.analysis.author;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.adsabs.solr.AdsConfig.F;
import org.junit.BeforeClass;

import java.io.File;
import java.nio.file.Paths;

/**
 * Adding only the explicitly curated initial-form row retrieves the
 * K-bearing target through the unchanged production analyzer pipeline.
 */
public class TestIssue20AuthorSynonymFullPlusShort extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = schemaWithRows(new String[]{
                "berta thompson, zachory;berta, zachory",
                "berta thompson, z;berta, z"
        });
        configString = "solrconfig.xml";
        SolrTestSetup.initCore(configString, schemaString);
    }

    private static String schemaWithRows(String[] rows) throws Exception {
        File schema = new File(SolrTestSetup.getRepoUrl(
                Paths.get("deploy/adsabs/server/solr/collection1/conf/schema.xml")).getFile());
        File curated = createTempFile(rows);
        File generated = createTempFile(new String[]{""});
        File copy = duplicateModify(schema,
                "synonyms=\"author_curated.synonyms\"",
                "synonyms=\"" + curated.getAbsolutePath().replace('\\', '/') + "\"",
                "synonyms=\"author_generated.translit\"",
                "synonyms=\"" + generated.getAbsolutePath().replace('\\', '/') + "\"");
        return copy.getAbsolutePath();
    }

    public void testIssue20InitialQueryUsesExplicitShortRow() throws Exception {
        assertU(adoc(F.ID, "800", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Berta, Zachory"));
        assertU(adoc(F.ID, "801", F.BIBCODE, "2014arXiv1409.0891I", F.AUTHOR, "Berta-Thompson, Zachory K"));
        assertU(adoc(F.ID, "802", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Berta-York, Zachory K"));
        assertU(commit());

        assertQ(req("defType", "aqp", "debugQuery", "true", "fl", "id,author", "rows", "100",
                        "q", "author:\"Berta, Z\""),
                "//*[@numFound='2']",
                "//doc/str[@name='id'][.='800']",
                "//doc/str[@name='id'][.='801']");
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestIssue20AuthorSynonymFullPlusShort.class);
    }
}
