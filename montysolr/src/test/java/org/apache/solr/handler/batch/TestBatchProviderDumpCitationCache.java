package org.apache.solr.handler.batch;

import org.apache.solr.request.SolrQueryRequest;

import java.io.File;

public class TestBatchProviderDumpCitationCache extends BatchProviderTest {


    public void test() throws Exception {

        assertU(adoc("id", "11", "bibcode", "b1", "reference", "b2", "reference", "b3", "reference", "b4", "b", "test"));
        assertU(adoc("id", "12", "bibcode", "b2", "b", "test"));
        assertU(adoc("id", "13", "bibcode", "b3", "reference", "b5", "reference", "b6", "reference", "b99", "b", "test"));
        assertU(adoc("id", "14", "bibcode", "b4", "reference", "b2", "reference", "b1"));
        assertU(adoc("id", "15", "bibcode", "b5"));
        assertU(adoc("id", "16", "bibcode", "b6"));
        assertU(adoc("id", "17", "bibcode", "b7", "reference", "b5"));
        assertU(commit("waitSearcher", "true"));


        BatchHandlerRequestQueue queue = new BatchHandlerRequestQueue();
        String tmpDir = new File("./temp").getAbsolutePath();
        BatchProviderI provider = new BatchProviderDumpCitationCache();

        SolrQueryRequest req = req("jobid", "00000", "#workdir", tmpDir, "unique_field", "bibcode", "ref_field", "reference");
        provider.run(req, queue);
        req.close();

        checkFile(tmpDir + "/00000",
                "b1\tb2\tb3\tb4",
                "b3\tb5\tb6",
                "b4\tb1\tb2",
                "b7\tb5"
        );

        req = req("jobid", "00000", "#workdir", tmpDir, "unique_field", "bibcode", "ref_field", "reference",
                "return_docids", "true");
        provider.run(req, queue);
        req.close();

        checkFile(tmpDir + "/00000",
                "0\t1\t2\t3",
                "2\t4\t5",
                "3\t0\t1",
                "6\t4"
        );

    }


}
