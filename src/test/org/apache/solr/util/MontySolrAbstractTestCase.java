package org.apache.solr.util;

import invenio.montysolr.jni.MontySolrVM;

import java.io.File;
import java.io.IOException;

import org.apache.solr.MontySolrTestCaseJ4;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public abstract class MontySolrAbstractTestCase extends AbstractSolrTestCase {
	
	public void setUp() throws Exception {
		super.setUp();
		
		// fix PYTHONPATH (has no effect on the interpereter)
		//String pythonpath = MontySolrTestCaseJ4.MONTYSOLR_HOME + "/src/python";
		//ProcessUtils.addEnv("PYTHONPATH", pythonpath);
		
		// the path added to sys.path is the parent
		System.setProperty("montysolr.modulepath", MontySolrTestCaseJ4.MONTYSOLR_HOME + "/src/python/montysolr");
		
		// set -Djava.library.path
		String jccpath = ProcessUtils.getJCCPath();
		ProcessUtils.setLibraryPath(jccpath);
		
		// this is necessary to run in the main thread
		MontySolrVM.INSTANCE.start("python");
		
		System.setProperty("montysolr.bridge", getBridgeName());

		
	}
	public String getSolrHome() {
		return MontySolrTestCaseJ4.TEST_HOME;
	}

	/** @see MontySolrTestCaseJ4#getFile */
	public static File getFile(String name) throws IOException {
		return MontySolrTestCaseJ4.getFile(name);
	}
	
	public String getBridgeName() {
		throw new NotImplementedException();
	}
}
