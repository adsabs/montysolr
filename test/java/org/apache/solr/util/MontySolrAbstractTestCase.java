package org.apache.solr.util;

import invenio.montysolr.jni.MontySolrVM;

import java.io.File;
import java.io.IOException;

import org.apache.solr.MontySolrTestCaseJ4;


public abstract class MontySolrAbstractTestCase extends AbstractSolrTestCase {
	
	public void setUp() throws Exception {
		super.setUp();
		
		// this is necessary to run in the main thread
		MontySolrVM.INSTANCE.start("python");
		
		//MontySolrVM.INSTANCE
		//		.start("montysolr_java", "-c",
		//				"import sys; sys.path.insert(0, \'/dvt/workspace/montysolr/src/python\')");
		
		
		// has no effect
		// System.setProperty("java.library.path",
		// "/usr/local/lib/python2.7/dist-packages/JCC-2.11-py2.7-linux-x86_64.egg");
		/**
		 * Map<String, String> env = System.getenv();
		 * 
		 * if (env.containsKey("PYTHONPATH")) { env.put("PYTHONPATH", new
		 * File(MontySolrTestCaseJ4.MONTYSOLR_HOME +
		 * "/src/python").getAbsolutePath() + ":" + env.get("PYTHONPATH")); }
		 * else { env.put("PYTHONPATH", new
		 * File(MontySolrTestCaseJ4.MONTYSOLR_HOME +
		 * "/src/python").getAbsolutePath()); }
		 */
	}
	public String getSolrHome() {
		return MontySolrTestCaseJ4.TEST_HOME;
	}

	/** @see MontySolrTestCaseJ4#getFile */
	public static File getFile(String name) throws IOException {
		return MontySolrTestCaseJ4.getFile(name);
	}
}
