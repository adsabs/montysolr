package examples;

import java.io.File;

import javax.xml.xpath.XPathExpressionException;

import invenio.montysolr.util.MontySolrAbstractTestCase;
import invenio.montysolr.util.MontySolrSetup;

import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.servlet.DirectSolrConnection;
import org.junit.BeforeClass;

public abstract class BlackBoxAbstractTestCase extends MontySolrAbstractTestCase {

	static String ename = null;
	static String base = null;
	static String path = "/build/contrib/examples/";
	protected EmbeddedSolrServer embedded;
	protected DirectSolrConnection direct;
	
	
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		exampleInit();
	}
	
	public void setUp() throws Exception {
		super.setUp();
		embedded = getServer();
		direct = getDirectConnection();
	}
	
	public EmbeddedSolrServer getServer() {
		return new EmbeddedSolrServer(h.getCoreContainer(), h.getCore().getName());
	}
	
	public DirectSolrConnection getDirectConnection() {
		return new DirectSolrConnection(h.getCore());
	}
	
	public static void exampleInit() throws Exception {
		if (ename == null) {
			throw new IllegalStateException("Please call setEName() first");
		}
		System.setProperty("solr.solr.home", MontySolrSetup.getMontySolrHome() + base + "/solr");
		
		System.err.println("Remember, BlackBox tests are to be run with the example assembled!");
		
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", 
				MontySolrSetup.getMontySolrHome() + base + "/python");
		MontySolrSetup.addBuildProperties(base);
	}
	
	public static String getConf(String conf) {
		File f = new File(MontySolrSetup.getMontySolrHome() + base + "/" + conf);
		if (!f.exists()) {
			throw new IllegalStateException("Not exists: " + f.toString());
		}
		return f.toString();
	}
	
	public static String getEName() {
		return ename;
	}
	
	public static void setEName(String name) {
		ename = name;
		base = path + ename;
	}
	
	public String getSchemaFile() {
		return getConf("solr/conf/schema.xml");
	}

	
	public String getSolrConfigFile() {
		return getConf("solr/conf/solrconfig.xml");
	}
	
	public void assertQDirect(String query, String body, String expected) {
		try {
	      String response = direct.request(query, body);
	      String results = h.validateXPath(response, expected);
	      if (null != results) {
	        fail("query failed XPath: " + results +
	             "\n xml response was: " + response +
	             "\n request was: " + query + 
	             "\n body was: " + body);
	      }
	    } catch (XPathExpressionException e1) {
	      throw new RuntimeException("XPath is invalid", e1);
	    } catch (Exception e2) {
	      throw new RuntimeException("Exception during query", e2);
	    }
	}

}