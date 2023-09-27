package org.apache.solr.handler.batch;

import org.apache.lucene.util.LuceneTestCase.SuppressCodecs;
import org.apache.solr.request.SolrQueryRequest;

import java.io.File;

@SuppressCodecs({"Lucene3x", "SimpleText"})
public class TestBatchProviderDumpTermFreqs extends BatchProviderTest {


    public void test() throws Exception {

        createIndex();

        BatchHandlerRequestQueue queue = new BatchHandlerRequestQueue();
        String tmpDir = createTempDir().toAbsolutePath().toString();
        SolrQueryRequest req = req("jobid", "00000", "#workdir", tmpDir, "fields", "bibcode,title");

        BatchProviderDumpTermFreqs provider = new BatchProviderDumpTermFreqs();
        provider.run(req, queue);
        req.close();

        checkFile(tmpDir + "/00000",
                "term\ttermFreq\tdocFreq",
                "trees\t1\t1",
                "peace\t2\t2"
        );

    }


}
