package org.apache.solr.search;

import org.junit.BeforeClass;


public class TestCitationsSearchCloudMapDBMultiShard extends AbstractCitationsSearchCloudTest {

    private static final String CONFIG_DIR = "src/test/resources/solr/cloud-MapDBCache-conf";
    public static final int NUM_SHARDS = 2;
    public static final int NUM_REPLICAS = 1;

    @BeforeClass
    public static void setupCluster() throws Exception { setupCluster(NUM_SHARDS, NUM_REPLICAS, CONFIG_DIR); }

    @Override
    protected int getNumShards() { return NUM_SHARDS; }

    @Override
    protected int getNumReplicas() { return NUM_REPLICAS; }

    @Override
    protected String getConfigDir() { return CONFIG_DIR; }

    @Override
    protected boolean skipCacheTests() { return false; }
}
