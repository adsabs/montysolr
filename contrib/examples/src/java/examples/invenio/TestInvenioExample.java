package examples.invenio;

import invenio.montysolr.JettyRunnerPythonVM;
import invenio.montysolr.util.MontySolrSetup;
import invenio.montysolr.util.MontySolrTestCaseJ4;
import invenio.montysolr.util.ProcessUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import examples.MontySolrJettyBase;


public class TestInvenioExample extends MontySolrJettyBase {

	static String schema = "http";
	static String host = "localhost";
	static String port = "8983";
	static String context = "/solr";
	static JettyRunnerPythonVM jr = null;
	private String basePath;
	
	@BeforeClass
	  public static void beforeClass() throws Exception {
		
		// discover and set -Djava.library.path
		String jccpath = ProcessUtils.getJCCPath();
		ProcessUtils.setLibraryPath(jccpath);
		
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", 
				MontySolrSetup.getMontySolrHome() + "/src/python");
		
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() + "/contrib/invenio/src/python");
		MontySolrSetup.addTargetsToHandler("monty_invenio.targets");
	  }
	
	
	public void setUp() throws Exception {
		
		basePath = schema + "://" + host + ":" + port + "/" + context + "/";
		
		jr = JettyRunnerPythonVM.init();
		String base = MontySolrSetup.getMontySolrHome();
		String exampleHome = base + "/build/contrib/examples/invenio";
		
		System.setProperty("solr.solr.home", exampleHome + "/solr");
		jr.configure(new String[]{"--webroot", exampleHome,
				"--port", port, "--context", context});
		jr.start();
	}
	
	public void tearDown() throws Exception {
		jr.stop();
	}
	
	public void testAll() throws MalformedURLException, IOException, SolrServerException {
		// Currently not an extensive test, but it does make sure the basic functionality works

	    String queryPath = basePath + "/invenio";
	    String adminPath = basePath + "/admin/";

	    String html = IOUtils.toString( new URL(queryPath).openStream() );
	    assert html.contains("<body"); // real error will be an exception

	    html = IOUtils.toString( new URL(adminPath).openStream() );
	    assert html.contains("Solr Admin"); // real error will be an exception
	    

	    // analysis
	    html = IOUtils.toString( new URL(adminPath+"analysis.jsp").openStream() );
	    assert html.contains("Field Analysis"); // real error will be an exception

	    // schema browser
	    html = IOUtils.toString( new URL(adminPath+"schema.jsp").openStream() );
	    assert html.contains("Schema"); // real error will be an exception

	    // schema browser
	    html = IOUtils.toString( new URL(adminPath+"threaddump.jsp").openStream() );
	    assert html.contains("org.apache.solr"); // real error will be an exception

	    
	    // special caching query
	    html = IOUtils.toString( new URL(queryPath+"select/?q=*%3A*&version=2.2&start=0&rows=10&indent=on&qt=iq").openStream());
	    System.out.println(html);

	    // special caching query
	    html = IOUtils.toString( new URL(queryPath+"select/?q=*%3A*&version=2.2&start=0&rows=10&indent=on&qt=iq").openStream());
	    System.out.println(html);
	}
}
