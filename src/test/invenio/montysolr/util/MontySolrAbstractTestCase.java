package invenio.montysolr.util;

import java.io.File;
import java.io.IOException;
import invenio.montysolr.util.MontySolrTestCaseJ4;
import org.apache.solr.util.AbstractSolrTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class MontySolrAbstractTestCase extends AbstractSolrTestCase {

	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		envInit();
	}

	@AfterClass
	public static void afterClassMontySolrTestCase() throws Exception {
	}
	
	/**
	 * This must be called first, so that we make sure the Python 
	 * interpreter is loaded
	 * 
	 * @throws Exception
	 */
	public static void envInit() throws Exception {
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", 
				MontySolrSetup.getMontySolrHome() + "/src/python");
	}

	public String getSolrHome() {
		return MontySolrSetup.getSolrHome();
	}
	
	/** @see MontySolrTestCaseJ4#getFile */
	public static File getFile(String name) throws IOException {
		return MontySolrTestCaseJ4.getFile(name);
	}
	
	

}
