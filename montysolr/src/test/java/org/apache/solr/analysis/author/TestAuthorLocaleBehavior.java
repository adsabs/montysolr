package org.apache.solr.analysis.author;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.adsabs.solr.AdsConfig.F;
import org.apache.solr.handler.batch.BatchHandlerRequestQueue;
import org.apache.solr.handler.batch.BatchProviderDumpAuthorNames;
import org.apache.solr.request.SolrQueryRequest;
import org.junit.BeforeClass;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class TestAuthorLocaleBehavior extends MontySolrQueryTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "schema.xml";
        configString = "solrconfig.xml";
        SolrTestSetup.initCore(configString, schemaString);
    }

    public void testProviderAndAuthorQueriesAreLocaleIndependent() throws Exception {
        Locale previous = Locale.getDefault();
        Path rootDump = Files.createTempDirectory("author-locale-root");
        Path turkishDump = Files.createTempDirectory("author-locale-turkish");
        try {
            assertU(adoc(F.ID, "1631", F.BIBCODE, "1631test", F.AUTHOR, "KIEV, Ivan"));
            assertU(adoc(F.ID, "1632", F.BIBCODE, "1632test", F.AUTHOR, "KIEV, Alice"));
            assertU(commit());

            Locale.setDefault(Locale.ROOT);
            String rootOutput = dumpAuthors(rootDump);
            assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100", "q", "author:\"KIEV, Ivan\""),
                    "//*[@numFound='1']",
                    "//doc/str[@name='id'][.='1631']",
                    "not(//doc/str[@name='id'][.='1632'])");

            Locale.setDefault(new Locale("tr", "TR"));
            String turkishOutput = dumpAuthors(turkishDump);
            assertQ(req("defType", "aqp", "fl", "id,author", "rows", "100", "q", "author:\"KIEV, Ivan\""),
                    "//*[@numFound='1']",
                    "//doc/str[@name='id'][.='1631']",
                    "not(//doc/str[@name='id'][.='1632'])");

            assertEquals(rootOutput, turkishOutput);
        } finally {
            Locale.setDefault(previous);
            assertU(delQ("*:*"));
            assertU(commit());
        }
    }

    private String dumpAuthors(Path workDir) throws Exception {
        BatchProviderDumpAuthorNames provider = new BatchProviderDumpAuthorNames();
        BatchHandlerRequestQueue queue = new BatchHandlerRequestQueue();
        SolrQueryRequest request = req("jobid", "00000", "#workdir", workDir.toString(),
                "sourceField", "author", "analyzerField", "author_collector");
        try {
            provider.run(request, queue);
        } finally {
            request.close();
        }
        return Files.readString(workDir.resolve("00000"));
    }
}
