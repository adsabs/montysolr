package examples;

import java.io.File;


import monty.solr.util.MontySolrAbstractLuceneTestCase;
import monty.solr.util.MontySolrSetup;

import org.junit.BeforeClass;

public abstract class BlackAbstractLuceneTestCase extends MontySolrAbstractLuceneTestCase {

	static String ename = null;
	static String base = null;
	static String path = "/build/contrib/examples/";
	
	@BeforeClass
	public static void beforeBlackAbstractLuceneTestCase() throws Exception {
		//exampleInit();
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
		
	}
	
	public static String getEName() {
		return ename;
	}
	
	public static void setEName(String name) {
		ename = name;
		base = path + ename;
	}
	
	
}