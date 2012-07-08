package invenio.montysolr;

import org.apache.solr.client.solrj.embedded.JettySolrRunner;
import java.io.File;
import java.lang.System;


public class SolrRunner {
	
	static File rootDir = new File("/x/dev/workspace/apache-solr-1.4.1/example/");
	static File homeDir = new File(rootDir, "solr");
	static File dataDir = new File(homeDir, "data");
	static File confDir = new File(homeDir, "conf");
	
		
	public static JettySolrRunner createJetty() throws Exception {
	    System.setProperty("solr.solr.home", homeDir.toString());
	    System.setProperty("solr.data.dir", dataDir.toString());
	    JettySolrRunner jetty = new JettySolrRunner("/solr", 0, rootDir.toString() + "/etc/jetty.xml");
	    jetty.start();
	    return jetty;
	  }
	
	public static void main(String[] args) throws Exception {
		JettySolrRunner jetty = createJetty();
		System.out.println(jetty);
		System.out.println(jetty.getLocalPort());
	}

}
