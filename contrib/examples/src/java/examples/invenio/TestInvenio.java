package examples.invenio;

import invenio.montysolr.util.MontySolrTestCaseJ4;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.BeforeClass;

import examples.MontySolrJettyBase;


public class TestInvenio extends MontySolrJettyBase {

	protected String schema = "http";
	protected String host = "localhost";
	protected String port = "8983";
	protected String context = "/solr";
	private String basePath;
	
	@BeforeClass
	  public static void beforeClass() throws Exception {
	    configString = MontySolrTestCaseJ4.getMontySolrHome() + "/contrib/examples/invenio/solr/conf/solrconfig.xml";
	    schemaString = MontySolrTestCaseJ4.getMontySolrHome() + "/contrib/examples/invenio/solr/conf/schema.xml";
	  }
	
	public void setUp() throws Exception {
		super.setUp();
		createJetty(getSolrHome(), getSolrConfigFile(), context);
		addToSysPath(getMontySolrHome() + "/contrib/invenio/src/python");
		addTargetsToHandler("monty_invenio.targets");
		basePath = schema + "://" + host + ":" + port + "/" + context + "/";
		
	}

	
	public String getMySchemaFile() {
		return getMontySolrHome() + "/contrib/examples/invenio/solr/conf/schema.xml";
	}

	
	public String getMySolrConfigFile() {
		return getMontySolrHome() + "/contrib/examples/invenio/solr/conf/solrconfig.xml";
	}


	public String getModuleName() throws Exception {
		return "montysolr.java_bridge.SimpleBridge";
	}
	
	public void testAll() throws MalformedURLException, IOException, SolrServerException {
		// Currently not an extensive test, but it does make sure the basic functionality works

		SolrServer server = getSolrServer();
	    
	    // Empty the database...
	    server.deleteByQuery( "*:*" );// delete everything!
	    
	    
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
