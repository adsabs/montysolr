package invenio.montysolr.util;

import invenio.montysolr.jni.MontySolrVM;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import invenio.montysolr.util.MontySolrTestCaseJ4;
import invenio.montysolr.util.ProcessUtils;

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
