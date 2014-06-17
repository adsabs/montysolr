package org.apache.solr.handler.batch;

import java.io.IOException;
import java.util.List;

import monty.solr.util.MontySolrSetup;

import org.apache.solr.util.AbstractSolrTestCase;
import org.junit.BeforeClass;

public class BatchProviderTest extends AbstractSolrTestCase {
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		
		System.setProperty("solr.allow.unsafe.resourceloading", "true");
		schemaString = MontySolrSetup.getMontySolrHome()
		+ "/contrib/adsabs/src/test-files/solr/collection1/conf/schema-batch-provider.xml";
		
		configString = MontySolrSetup.getMontySolrHome()
		+ "/contrib/adsabs/src/test-files/solr/collection1/conf/solrconfig-batch-provider.xml";
		
		initCore(configString, schemaString, MontySolrSetup.getSolrHome() + "/example/solr");
	}
	
	
	@Override
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
    List<String> lines = h.getCore().getResourceLoader().getLines(file);
    for (String t: expected) {
      if (t.substring(0,1).equals("!")) {
        assertFalse("Present: " + t + "\n" + lines, lines.contains(t.substring(1)));
      }
      else {
        assertTrue("Missing: " + t + "\n" + lines, lines.contains(t));
      }
    }
  }
}
