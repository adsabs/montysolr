package invenio.montysolr.util;

import org.apache.lucene.util.LuceneTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;


public abstract class MontySolrAbstractLuceneTestCase extends LuceneTestCase {
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", MontySolrSetup.getMontySolrHome() + "/src/python");
	}

	@AfterClass
	public static void afterClassMontySolrTestCase() throws Exception {
	}
	
	
}
