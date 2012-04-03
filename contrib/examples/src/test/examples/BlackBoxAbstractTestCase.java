package examples;

import java.io.File;
import invenio.montysolr.util.MontySolrAbstractTestCase;
import invenio.montysolr.util.MontySolrSetup;

import org.junit.BeforeClass;

public abstract class BlackBoxAbstractTestCase extends MontySolrAbstractTestCase {

	static String ename = null;
	static String base = null;
	static String path = "/build/contrib/examples/";
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		exampleInit();
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

}