package org.apache.solr.handler;

import java.nio.file.Path;
import java.util.List;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.cloud.MiniSolrCloudCluster;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.Slice;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import monty.solr.util.MontySolrSetup;

public class TestAdsabsSolrCloudReplication extends SolrCloudTestCase {
    private static final String COLLECTION = "adsabs_cloud_replication";
    private static final String CONFIG = "adsabs-production";
    private static final List<String> EXPECTED_IDS = List.of("900001", "900002");
    private static final String JUTE_MAXBUFFER_PROPERTY = "jute.maxbuffer";
    private static final String PREVIOUS_JUTE_MAXBUFFER = System.getProperty(JUTE_MAXBUFFER_PROPERTY);

    static {
        System.setProperty(JUTE_MAXBUFFER_PROPERTY, "4194304");
    }

    @BeforeClass
    public static void setupCloud() throws Exception {
        Path projectRoot = Path.of(MontySolrSetup.getMontySolrHome());
        Path configSet = projectRoot.resolve("deploy/adsabs/server/solr/collection1/conf");

        MiniSolrCloudCluster configuredCluster = configureCluster(2).build();
        try {
            configuredCluster.uploadConfigSet(configSet, CONFIG);
            cluster = configuredCluster;
        } catch (Exception uploadFailure) {
            try {
                configuredCluster.shutdown();
            } catch (Exception shutdownFailure) {
                uploadFailure.addSuppressed(shutdownFailure);
            }
            throw uploadFailure;
        }

        CollectionAdminRequest.createCollection(COLLECTION, CONFIG, 1, 2)
                .process(cluster.getSolrClient());
        cluster.waitForActiveCollection(COLLECTION, 1, 2);
    }

    @AfterClass
    public static void shutdownCloud() throws Exception {
        try {
            shutdownCluster();
        } finally {
            if (PREVIOUS_JUTE_MAXBUFFER == null) {
                System.clearProperty(JUTE_MAXBUFFER_PROPERTY);
            } else {
                System.setProperty(JUTE_MAXBUFFER_PROPERTY, PREVIOUS_JUTE_MAXBUFFER);
            }
        }
    }


    @Test
    public void testDocumentsReachEveryReplica() throws Exception {
        CloudSolrClient cloudClient = cluster.getSolrClient();
        cloudClient.setDefaultCollection(COLLECTION);

        SolrInputDocument first = new SolrInputDocument();
        first.addField("id", "900001");
        first.addField("recid", "900001");
        first.addField("bibcode", "b900001");
        first.addField("citation", "b900002");

        SolrInputDocument second = new SolrInputDocument();
        second.addField("id", "900002");
        second.addField("recid", "900002");
        second.addField("bibcode", "b900002");
        second.addField("reference", "b900001");

        cloudClient.add(List.of(first, second));
        cloudClient.commit();

        DocCollection collection = cluster.getZkStateReader().getClusterState().getCollection(COLLECTION);
        Slice shard = collection.getSlice("shard1");
        SolrTestCaseJ4.assertNotNull(shard);
        SolrTestCaseJ4.assertEquals(2, shard.getReplicas().size());

        for (Replica replica : shard.getReplicas()) {
            try (HttpSolrClient replicaClient = new HttpSolrClient.Builder(replica.getCoreUrl()).build()) {
                SolrQuery query = new SolrQuery("*:*");
                query.set("distrib", "false");
                query.setFields("id");
                query.setRows(EXPECTED_IDS.size());
                query.setSort("id", SolrQuery.ORDER.asc);

                QueryResponse response = replicaClient.query(query);
                List<String> actualIds = response.getResults().stream()
                        .map(document -> (String) document.getFieldValue("id"))
                        .toList();
                SolrTestCaseJ4.assertEquals(EXPECTED_IDS, actualIds);

                SolrTestCaseJ4.assertEquals(List.of("900002"),
                        queryIds(replicaClient, "citations(bibcode:b900001)"));
                SolrTestCaseJ4.assertEquals(List.of("900001"),
                        queryIds(replicaClient, "references(bibcode:b900002)"));
            }
        }
    }

    private static List<String> queryIds(HttpSolrClient client, String queryText) throws Exception {
        SolrQuery query = new SolrQuery(queryText);
        query.set("distrib", "false");
        query.setFields("id");
        query.setRows(EXPECTED_IDS.size());
        query.setSort("id", SolrQuery.ORDER.asc);
        QueryResponse response = client.query(query);
        return response.getResults().stream()
                .map(document -> (String) document.getFieldValue("id"))
                .toList();
    }
}
