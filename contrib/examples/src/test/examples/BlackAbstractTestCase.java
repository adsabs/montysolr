package examples;

import java.io.File;
import javax.xml.xpath.XPathExpressionException;
import invenio.montysolr.util.MontySolrAbstractTestCase;
import invenio.montysolr.util.MontySolrSetup;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.servlet.DirectSolrConnection;
import org.junit.BeforeClass;

public abstract class BlackAbstractTestCase extends MontySolrAbstractTestCase {

	static String ename = null;
	static String base = null;
	static String path = "/build/contrib/examples/";
	protected EmbeddedSolrServer embedded;
	protected DirectSolrConnection direct;
	private File persistDir = null;
	private String factoryPropShadow;
	
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		exampleInit();
	}
	
	public void setUp() throws Exception {
		
		if (System.getProperty("blackbox.persist.index") != null) { 
			File d = new File(System.getProperty("blackbox.persist.index"));
			if (!d.isDirectory() && d.mkdir() != true) {
				throw new RuntimeException("blackbox.persist.index is set to: " + d.getAbsolutePath() +
						"But this folder does not exist and could not be created");
			}
			else {
				FileUtils.deleteDirectory(d);
				d.mkdir();
			}
			persistDir = d;
			
			factoryPropShadow = System.getProperty("solr.directoryFactory");
		    if (factoryPropShadow == null) {
		    	System.err.println("Setting solr.directoryFactory to SimpleFSDirectoryFactory to preserve index");
		    	System.setProperty("solr.directoryFactory","solr.SimpleFSDirectoryFactory");
		    }
		    else if (factoryPropShadow.contains("RAM")) {
		    	System.err.println("WARNING: you activated blackbox.persist.index but your index is never written to disk (probably).");
		    	System.err.println("Because solr.directoryFactory=" + factoryPropShadow);
		    }
		}
		
		super.setUp();
		embedded = getEmbeddedServer();
		direct = getDirectServer();
	}
	
	public void tearDown() throws Exception {
		
		if (persistDir != null) {
			File src = new File(h.getCore().getDataDir());
			System.err.print("Moving indexes from : " + src + " to:" + persistDir);
			
			embedded.commit(true, true);
			
			for (File d: src.listFiles()) {
				FileUtils.moveDirectoryToDirectory(d, persistDir, true);
			}
			
			
		}
		
		super.tearDown();
		
		if (factoryPropShadow == null) {
	         System.clearProperty("solr.directoryFactory");
	    }
	}
	
	public static String getExampleHome() {
		return MontySolrSetup.getMontySolrHome() + "/" + base;
	}
	
	// must redifine, because we want to use different python path
	public static void envInit() throws Exception {
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", 
				getExampleHome() + "/python");
	}
	
	public static void exampleInit() throws Exception {
		if (ename == null) {
			throw new IllegalStateException("Please call setEName() first");
		}
		System.setProperty("solr.solr.home", getExampleHome() + "/solr");
		
		envInit();
		
		MontySolrSetup.addToSysPath(getExampleHome() + "/python");
		MontySolrSetup.addBuildProperties(base);
	}
	
	public static String getConf(String conf) {
		File f = new File(getExampleHome() + "/" + conf);
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