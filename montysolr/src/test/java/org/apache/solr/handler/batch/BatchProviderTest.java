package org.apache.solr.handler.batch;

import monty.solr.util.MontySolrSetup;
import monty.solr.util.SolrTestSetup;
import org.apache.solr.SolrTestCaseJ4;
import org.junit.BeforeClass;

import java.io.IOException;

public class BatchProviderTest extends SolrTestCaseJ4 {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "solr/collection1/conf/schema-batch-provider.xml";

        configString = "solr/collection1/conf/solrconfig-batch-provider.xml";

        SolrTestSetup.initCore(configString, schemaString);
    }


    public String getSolrHome() {
        return MontySolrSetup.getMontySolrHome();
    }

    public void createIndex() {
        // now index some data
        assertU(adoc("id", "1", "bibcode", "xxxxxxxxxxxx1", "title", "green wall"));
        assertU(adoc("id", "2", "bibcode", "xxxxxxxxxxxx2", "title", "of trees"));
        assertU(adoc("id", "3", "bibcode", "xxxxxxxxxxxx3", "title", "blues sky"));
        assertU(adoc("id", "4", "bibcode", "xxxxxxxxxxxx4", "title", "of seas"));
        assertU(adoc("id", "5", "bibcode", "xxxxxxxxxxxx5", "title", "no fight"));
        assertU(adoc("id", "6", "bibcode", "xxxxxxxxxxxx6", "title", "for peace"));
        assertU(commit());
        // this creates another segment
        assertU(adoc("id", "7", "bibcode", "xxxxxxxxxxxx7", "title", "no fight"));
        assertU(adoc("id", "8", "bibcode", "xxxxxxxxxxxx8", "title", "for peace"));
        assertU(commit());
    }

    protected void checkFile(String file, String... expected) throws IOException {
        StringBuffer input = new StringBuffer();
        for (String l : h.getCore().getResourceLoader().getLines(file)) {
//      if (l.startsWith("{")) {
//        input.append("\n");
//      }
            input.append(l);
        }
        String lines = input.toString();
//    List<String> lines =  h.getCore().getResourceLoader().getLines(file);
//    for (String l : lines) {
//    	System.out.println(l);
//    }
        for (String t : expected) {
            if (t.charAt(0) == '!') {
                assertFalse("Present: " + t + "\n" + lines, lines.contains(t.substring(1)));
            } else {
                assertTrue("Missing: " + t + "\n" + lines, lines.contains(t));
            }
        }
    }
}
