package invenio.montysolr.util;

import invenio.montysolr.jni.MontySolrVM;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import invenio.montysolr.util.MontySolrTestCaseJ4;
import invenio.montysolr.util.ProcessUtils;
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
		return MontySolrTestCaseJ4.EXAMPLE_HOME;
	}
	
	/** @see MontySolrTestCaseJ4#getFile */
	public static File getFile(String name) throws IOException {
		return MontySolrTestCaseJ4.getFile(name);
	}


}
