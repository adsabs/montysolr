package org.apache.solr.handler.batch;

import org.adsabs.solr.AdsConfig.F;
import org.apache.solr.request.SolrQueryRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestBatchProviderFindWordGroups extends BatchProviderTest {


    public void test() throws Exception {

        String field = "text_sw";
        String field2 = "text";

        // now index some data
        assertU(adoc(F.ID, "1", F.BIBCODE, "xxxxxxxxxxxx1", field, "green wall for the blue sky", field2, "green wall for the blue sky"));
        assertU(adoc(F.ID, "2", F.BIBCODE, "xxxxxxxxxxxx2", field, "of trees angels edens", field2, "of trees angels edens"));
        assertU(adoc(F.ID, "3", F.BIBCODE, "xxxxxxxxxxxx3", field, "blue sky no blues dye", field2, "blue sky no blues dye"));
        assertU(adoc(F.ID, "4", F.BIBCODE, "xxxxxxxxxxxx4", field, "of high seas and low", field2, "of high seas and low"));
        assertU(adoc(F.ID, "5", F.BIBCODE, "xxxxxxxxxxxx5", field, "flights of the heroes", field2, "flights of the heroes"));
        assertU(adoc(F.ID, "6", F.BIBCODE, "xxxxxxxxxxxx6", field, "fo the race past race for peace", field2, "fo the race past race for peace"));
        assertU(commit());
        // this creates another segment
        assertU(adoc(F.ID, "7", F.BIBCODE, "xxxxxxxxxxxx7", field, "no fight, no plight", field2, "no fight, no plight"));
        assertU(adoc(F.ID, "8", F.BIBCODE, "xxxxxxxxxxxx8", field, "no peace, think twice", field2, "no peace, think twice"));
        assertU(commit());


        BatchHandlerRequestQueue queue = new BatchHandlerRequestQueue();
        String tmpDir = new File("./temp").getAbsolutePath();

        writeData(tmpDir + "/00001.input", "blue\nseas\nrace");


        BatchProviderFindWordGroups provider = new BatchProviderFindWordGroups();

        SolrQueryRequest req = req(
                "jobid", "00001",
                "#workdir", tmpDir,
                "fields", field,
                "maxlen", "2",
                "lowerLimit", "0.0"
        );
        provider.run(req, queue);
        req.close();

        checkFile(tmpDir + "/00001",
                "blue|sky\t2",
                "high|seas\t1",
                "fo|race\t1",
                "past|race\t1",
                "race|past\t1",
                "race|peace\t1",
                "seas|low\t1",
                "wall|blue\t1"
        );


        req = req(
                "jobid", "00001",
                "#workdir", tmpDir,
                "fields", field,
                "maxlen", "2",
                "lowerLimit", "0.5"
        );
        provider.run(req, queue);
        req.close();

        checkFile(tmpDir + "/00001",
                "blue|sky\t2",
                "high|seas\t1",
                "fo|race\t1",
                "past|race\t1",
                "!race|past\t1",
                "!race|peace\t1",
                "!seas|low\t1",
                "!wall|blue\t1"
        );

        req = req(
                "jobid", "00001",
                "#workdir", tmpDir,
                "fields", field,
                "maxlen", "2",
                "upperLimit", "0.9",
                "lowerLimit", "0.4"
        );
        provider.run(req, queue);
        req.close();

        checkFile(tmpDir + "/00001",
                "!blue|sky\t2",
                "high|seas\t1",
                "fo|race\t1",
                "past|race\t1",
                "race|past\t1",
                "!race|peace\t1",
                "!seas|low\t1",
                "!wall|blue\t1"
        );

        // use the limit on the number of collected results
        req = req(
                "jobid", "00001",
                "#workdir", tmpDir,
                "fields", field,
                "maxlen", "2",
                "lowerLimit", "0.0",
                "stopAfterReaching", "2"
        );
        provider.run(req, queue);
        req.close();

        checkFile(tmpDir + "/00001",
                "blue|sky\t2",
                "sky|blues\t1",
                "wall|blue\t1",
                "!fo|race\t1",
                "!past|race\t1",
                "!race|past\t1",
                "!race|peace\t1",
                "!seas|low\t1"
        );


        req = req(
                "jobid", "00001",
                "#workdir", tmpDir,
                "fields", field,
                "maxlen", "4",
                "lowerLimit", "0.0"
        );
        provider.run(req, queue);
        req.close();

        checkFile(tmpDir + "/00001",
                "blue|sky|blues|dye\t1",
                "fo|race|past|race\t1",
                "green|wall|blue|sky\t1",
                "race|past|race|peace\t1"
        );


    }

    private void writeData(String targetFile, String targetData) throws IOException {
        File fo = new File(targetFile);
        FileWriter fw = new FileWriter(fo);
        fw.write(targetData);
        fw.close();
    }


}
