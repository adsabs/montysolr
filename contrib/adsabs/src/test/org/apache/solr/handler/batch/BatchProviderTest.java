package org.apache.solr.handler.batch;

import java.io.IOException;
import java.util.List;

import monty.solr.util.MontySolrSetup;

import org.apache.solr.util.AbstractSolrTestCase;

public class BatchProviderTest extends AbstractSolrTestCase {
	public String getSchemaFile() {
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/adsabs/src/test-files/solr/collection1/conf/schema-fieldpos.xml";
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/adsabs/src/test-files/solr/collection1/conf/solrconfig-fieldpos.xml";
	}
	
	protected void checkFile(String file, String... expected) throws IOException {
    List<String> lines = h.getCore().getResourceLoader().getLines(file);
    for (String t: expected) {
      if (t.substring(0,1).equals("!")) {
        assertFalse("Present: " + t, lines.contains(t.substring(1)));
      }
      else {
        assertTrue("Missing: " + t, lines.contains(t));
      }
    }
  }
}
