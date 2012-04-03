package examples;

import java.io.File;

import invenio.montysolr.util.MontySolrAbstractLuceneTestCase;
import invenio.montysolr.util.MontySolrSetup;

import org.junit.BeforeClass;

public abstract class BlackBoxAbstractLuceneTestCase extends MontySolrAbstractLuceneTestCase {

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
		
		System.err.println("Remember, BlackBox tests are to be run with the example assembled!");
		
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", 
				MontySolrSetup.getMontySolrHome() + base + "/python");
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
	
}