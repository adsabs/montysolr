package monty.solr.util;

import org.apache.lucene.search.IndexSearcher;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.impl.SolrZkClientTimeout;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.core.*;
import org.apache.solr.metrics.reporters.SolrJmxReporter;
import org.apache.solr.update.UpdateShardHandlerConfig;
import org.apache.solr.util.TestHarness;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

public class SolrTestSetup extends SolrTestCaseJ4 {

    public static void initCore(String config, String schema) throws Exception {
        // Set required system variables
        SolrTestCaseJ4.configString = null;
        SolrTestCaseJ4.initCore();

        // Create a resource loader + config that use our test class' resource loader
        System.setProperty("solr.allow.unsafe.resourceloading", "true");
        Path solrPath = Files.createTempDirectory("montysolr");

        // Add repo root + ADS solr conf to classpath
        Path solrConfPath = Paths.get("deploy", "adsabs", "server", "solr", "collection1", "conf");
        URLClassLoader extendedLoader = URLClassLoader.newInstance(
                new URL[] {getRepoUrl(), getRepoUrl(solrConfPath)},
                SolrTestSetup.class.getClassLoader());

        SolrResourceLoader loader = new SolrResourceLoader(solrPath,
                extendedLoader);

        // Perform the rest of createCore
        SolrTestCaseJ4.h = new TestHarness(
                buildTestNodeConfig(solrPath, loader),
                new TestHarness.TestCoresLocator(SolrTestCaseJ4.DEFAULT_TEST_CORENAME,
                        solrPath.toString(), config, schema)
        );
        SolrTestCaseJ4.h.coreName = coreName;
        /*
        SolrTestCaseJ4.h = new TestHarness(coreName,
                hdfsDataDir == null ? initCoreDataDir.getAbsolutePath() : hdfsDataDir,
                solrConfig,
                schema);
         */

        SolrTestCaseJ4.lrf = h.getRequestFactory("", 0, 20, CommonParams.VERSION, "2.2");

        // Set remaining variables
        SolrTestCaseJ4.configString = config;
        SolrTestCaseJ4.schemaString = schema;
        SolrTestCaseJ4.testSolrHome = solrPath;

        IndexSearcher.setMaxClauseCount(2048);
    }

    public static NodeConfig buildTestNodeConfig(Path solrHome, SolrResourceLoader loader) {
        CloudConfig cloudConfig = null == System.getProperty("zkHost") ? null : (new CloudConfig.CloudConfigBuilder(System.getProperty("host"), Integer.getInteger("hostPort", 8983), System.getProperty("hostContext", ""))).setZkClientTimeout(SolrZkClientTimeout.DEFAULT_ZK_CLIENT_TIMEOUT).setZkHost(System.getProperty("zkHost")).build();
        Map<String, Object> attributes = new HashMap();
        attributes.put("name", "default");
        attributes.put("class", SolrJmxReporter.class.getName());
        PluginInfo defaultPlugin = new PluginInfo("reporter", attributes);
        MetricsConfig metricsConfig = (new MetricsConfig.MetricsConfigBuilder()).setMetricReporterPlugins(new PluginInfo[]{defaultPlugin}).build();
        return (new NodeConfig.NodeConfigBuilder("testNode", solrHome))
                .setUseSchemaCache(Boolean.getBoolean("shareSchema"))
                .setCloudConfig(cloudConfig)
                .setUpdateShardHandlerConfig(UpdateShardHandlerConfig.TEST_DEFAULT)
                .setMetricsConfig(metricsConfig)
                .setSolrResourceLoader(loader)
                .build();
    }

    public static URL getRepoUrl() throws URISyntaxException, MalformedURLException {
        Class<SolrTestSetup> clazz = SolrTestSetup.class;
        ProtectionDomain protectionDomain = clazz.getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();

        Path filePath = Paths.get(codeSource.getLocation().toURI());
        while (!filePath.resolve(".gitignore").toFile().exists()) {
            filePath = filePath.getParent();
        }

        return filePath.toUri().toURL();
    }

    public static URL getRepoUrl(Path subpath) throws URISyntaxException, MalformedURLException {
        return Paths.get(getRepoUrl().toURI()).resolve(subpath).toUri().toURL();
    }
}
