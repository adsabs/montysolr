package org.apache.solr.handler;

import monty.solr.util.MontySolrSetup;
import monty.solr.util.SolrTestSetup;
import org.apache.solr.SolrTestCaseJ4;
import org.junit.BeforeClass;

public class TestCitationCacheReplication extends SolrTestCaseJ4 {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "solr/collection1/conf/schema-batch-provider.xml";

        configString = "solr/collection1/conf/solrconfig-dump-citation-cache.xml";

        // There's a patch on Solr main branch that makes this unnecessary,
        // but for the last 10 years the SolrTestCaseJ4 class has been setting
        // this property to RAMDirectoryFactory. As of Solr 9 the RAMDirectoryFactory
        // doesn't work here; it requires a locking type that's incompatible with
        // this test.
        // See also: https://github.com/apache/solr/commit/c7630fe13ff9e5475c0c358beb9e92c12826e599
        System.setProperty("solr.directoryFactory", "solr.MockDirectoryFactory");

        SolrTestSetup.initCore(configString, schemaString);
    }


    public String getSolrHome() {
        return MontySolrSetup.getMontySolrHome();
    }


    public void test() throws Exception {

        assertU(adoc("id", "11", "bibcode", "b1", "reference", "b2", "reference", "b3", "reference", "b4", "b", "test"));
        assertU(adoc("id", "12", "bibcode", "b2", "b", "test"));
        assertU(adoc("id", "13", "bibcode", "b3", "reference", "b5", "reference", "b6", "reference", "b99", "b", "test"));
        assertU(adoc("id", "14", "bibcode", "b4", "reference", "b2", "reference", "b1"));
        assertU(adoc("id", "15", "bibcode", "b5"));
        assertU(adoc("id", "16", "bibcode", "b6"));
        assertU(adoc("id", "17", "bibcode", "b7", "reference", "b5"));
        assertU(commit("waitSearcher", "true"));

        assertQ(req("q", "bibcode:b3"),
                "//*[@numFound='1']",
                "//doc/int[@name='id'][.='13']"
        );

        System.out.println(h.query(req("qt", "/replication", "command", "indexversion", "wt", "json")));
        System.out.println(h.query(req("qt", "/replication", "command", "filelist", "generation", "2", "wt", "json", "indent", "true")));
        // There was a change in Solr 9 that causes the filecontent command to fail.
        // In particular, the TextWriter class used to give up on serializing unknown classes,
        // but now it uses reflection. The default security manager will reject the attempt to access
        // private fields via reflection, causing the test to fail.
        //System.out.println(h.query(req("qt", "/replication", "command", "filecontent", "file", "citation_cache", "wt", "json", "indent", "true")));

    }
}
