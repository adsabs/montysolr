package monty.solr.util;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.core.SolrConfig;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.util.TestHarness;

import java.nio.file.Files;
import java.nio.file.Path;

public class SolrTestSetup extends SolrTestCaseJ4 {

    public static void initCore(String config, String schema) throws Exception {
        // Set required system variables
        SolrTestCaseJ4.configString = null;
        SolrTestCaseJ4.initCore();

        // Create a resource loader + config that use our test class' resource loader
        Path solrPath = Files.createTempDirectory("montysolr");
        SolrResourceLoader loader = new SolrResourceLoader(solrPath,
                SolrTestSetup.class.getClassLoader());
        SolrConfig solrConfig = SolrConfig.readFromResourceLoader(loader, config);

        // Perform the rest of createCore
        SolrTestCaseJ4.h = new TestHarness(coreName,
                hdfsDataDir == null ? initCoreDataDir.getAbsolutePath() : hdfsDataDir,
                solrConfig,
                schema);

        SolrTestCaseJ4.lrf = h.getRequestFactory("", 0, 20, CommonParams.VERSION, "2.2");

        // Set remaining variables
        SolrTestCaseJ4.configString = config;
        SolrTestCaseJ4.schemaString = schema;
        SolrTestCaseJ4.testSolrHome = solrPath;
    }
}
