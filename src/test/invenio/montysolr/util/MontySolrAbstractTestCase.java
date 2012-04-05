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
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", MontySolrSetup.getMontySolrHome() + "/src/python");
	}

	@AfterClass
	public static void afterClassMontySolrTestCase() throws Exception {
	}

	public String getSolrHome() {
		return MontySolrSetup.getSolrHome();
	}
	
	/** @see MontySolrTestCaseJ4#getFile */
	public static File getFile(String name) throws IOException {
		return MontySolrTestCaseJ4.getFile(name);
	}
	
	

}
