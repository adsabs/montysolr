package org.apache.solr.handler.batch;

import org.apache.solr.request.SolrQueryRequest;

import java.io.File;

public class TestBatchProviderDumpDocsByQuery extends BatchProviderTest {


    public void test() throws Exception {

        createIndex();

        BatchHandlerRequestQueue queue = new BatchHandlerRequestQueue();
        String tmpDir = createTempDir().toAbsolutePath().toString();
        SolrQueryRequest req = req("jobid", "00000", "#workdir", tmpDir, "q", "id:1");

        BatchProviderDumpDocsByQuery provider = new BatchProviderDumpDocsByQuery();
        provider.run(req, queue);
        req.close();

        checkFile(tmpDir + "/00000",
                "{\"id\":1,\"bibcode\":\"xxxxxxxxxxxx1\",\"title\":\"green wall\"}",
                "!,{\"id\":5,\"bibcode\":\"xxxxxxxxxxxx5\",\"title\":\"no fight\"}",
                "!,{\"id\":6,\"bibcode\":\"xxxxxxxxxxxx6\",\"title\":\"for peace\"}",
                "!,{\"id\":7,\"bibcode\":\"xxxxxxxxxxxx7\",\"title\":\"no fight\"}",
                "!,{\"id\":8,\"bibcode\":\"xxxxxxxxxxxx8\",\"title\":\"for peace\"}"
        );

        // request set of fields
        req = req("jobid", "00000", "#workdir", tmpDir, "q", "id:1", "fl", "id,bibcode");
        provider = new BatchProviderDumpDocsByQuery();
        provider.run(req, queue);
        req.close();

        checkFile(tmpDir + "/00000",
                "{\"id\":1,\"bibcode\":\"xxxxxxxxxxxx1\"}",
                "!,{\"id\":5,\"bibcode\":\"xxxxxxxxxxxx5\"}",
                "!,{\"id\":6,\"bibcode\":\"xxxxxxxxxxxx6\"}",
                "!,{\"id\":7,\"bibcode\":\"xxxxxxxxxxxx7\"}",
                "!,{\"id\":8,\"bibcode\":\"xxxxxxxxxxxx8\"}"
        );

    }


}
