package org.apache.solr.analysis.author;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.adsabs.solr.AdsConfig.F;
import org.junit.BeforeClass;

import java.io.File;
import java.nio.file.Paths;

/**
 * Full-name-only curation does not authorize an initial-form alias to
 * expand to a different, K-bearing full name.
 */
public class TestIssue20AuthorSynonymFullOnly extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = schemaWithRows(new String[]{
                "berta thompson, zachory;berta, zachory"
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

    public void testIssue20InitialQueryNeedsExplicitShortRow() throws Exception {
        assertU(adoc(F.ID, "800", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Berta, Zachory"));
        assertU(adoc(F.ID, "801", F.BIBCODE, "2014arXiv1409.0891I", F.AUTHOR, "Berta-Thompson, Zachory K"));
        assertU(adoc(F.ID, "802", F.BIBCODE, "xxxxxxxxxxxxx", F.AUTHOR, "Berta-York, Zachory K"));
        assertU(commit());

        assertQ(req("defType", "aqp", "debugQuery", "true", "fl", "id,author", "rows", "100",
                        "q", "author:\"Berta, Z\""),
                "//*[@numFound='1']",
                "//doc/str[@name='id'][.='800']");
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestIssue20AuthorSynonymFullOnly.class);
    }
}
