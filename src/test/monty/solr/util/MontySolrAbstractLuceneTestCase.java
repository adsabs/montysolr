package monty.solr.util;


import org.apache.lucene.util.LuceneTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;


public abstract class MontySolrAbstractLuceneTestCase extends LuceneTestCase {
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		envInit();
	}

	@AfterClass
	public static void afterClassMontySolrTestCase() throws Exception {
	  System.clearProperty("montysolr.bridge");
	  System.clearProperty("montysolr.home");
	  System.clearProperty("montysolr.modulepath");
	  System.clearProperty("solr.test.sys.prop1");
	  System.clearProperty("solr.test.sys.prop2");
	}
	
	/**
	 * Must be called first, so that we make sure the Python 
	 * interpreter is loaded
	 * 
	 * @throws Exception
	 */
	public static void envInit() throws Exception {
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", 
				MontySolrSetup.getMontySolrHome() + "/src/python");
	}
	
	
}
