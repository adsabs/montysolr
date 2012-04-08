package examples;

import java.io.File;

import invenio.montysolr.util.MontySolrAbstractLuceneTestCase;
import invenio.montysolr.util.MontySolrSetup;

import org.junit.BeforeClass;

public abstract class BlackAbstractLuceneTestCase extends MontySolrAbstractLuceneTestCase {

	static String ename = null;
	static String base = null;
	static String path = "/build/contrib/examples/";
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		exampleInit();
	}
	
	public void setUp() throws Exception {
		if (System.getProperty("blackbox.persist.index") != null) {
			throw new RuntimeException("blackbox.persist.index has no meaning for the lucene blackbox tests!");
		}
		super.setUp();
	}
	
	public static void exampleInit() throws Exception {
		if (ename == null) {
			throw new IllegalStateException("Please call setEName() first");
		}
		
		envInit();
		
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() + "/" + base + "/python");
		MontySolrSetup.addBuildProperties(base);
	}
	
	public static String getEName() {
		return ename;
	}
	
	public static void setEName(String name) {
		ename = name;
		base = path + ename;
	}
	
	// must redifine, because we want to use different python path
	public static void envInit() throws Exception {
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", 
				MontySolrSetup.getMontySolrHome() + "/" + base + "/python");
	}
	
	
	
}