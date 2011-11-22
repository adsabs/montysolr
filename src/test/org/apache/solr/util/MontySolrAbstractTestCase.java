package org.apache.solr.util;

import invenio.montysolr.jni.MontySolrVM;

import java.io.File;
import java.io.IOException;

import org.apache.solr.MontySolrTestCaseJ4;


public abstract class MontySolrAbstractTestCase extends AbstractSolrTestCase {
	
	public void setUp() throws Exception {
		super.setUp();
		
		// chaning PYTHONPATH (has no effect on the embedded interpereter)
		//String pythonpath = MontySolrTestCaseJ4.MONTYSOLR_HOME + "/src/python";
		//ProcessUtils.addEnv("PYTHONPATH", pythonpath);
		
		// the path added to sys.path is the parent
		System.setProperty("montysolr.modulepath", getModulePath());
		
		// discover and set -Djava.library.path
		String jccpath = ProcessUtils.getJCCPath();
		ProcessUtils.setLibraryPath(jccpath);
		
		// this is necessary to run in the main thread and because of the 
		// python loads the parent folder and inserts it into the pythonpath
		// we trick it
		MontySolrVM.INSTANCE.start(getChildModulePath());
		
		System.setProperty("montysolr.bridge", getModuleName());

		
	}
	public String getSolrHome() {
		return MontySolrTestCaseJ4.TEST_HOME;
	}

	/** @see MontySolrTestCaseJ4#getFile */
	public static File getFile(String name) throws IOException {
		return MontySolrTestCaseJ4.getFile(name);
	}
	
	public String getModuleName() throws Exception {
		throw new Exception("You must implement this in your class!");
	}
	
	public String getModulePath() {
		return MontySolrTestCaseJ4.MONTYSOLR_HOME + "/src/python/montysolr";
	}
	
	/**
	 * Trick to find any existing folder/file inside the main module path
	 * and return it to be set by python into the PYTHONPATH
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getChildModulePath() throws Exception {
		File f = new File(getModulePath());
		if (f.isFile()) {
			return f.getAbsolutePath().toString();
		}
		else if(f.exists()) {
			for (String child: f.list()) {
				return new File(f.getAbsolutePath() + "/" + child).getAbsolutePath();
			}
		}
		throw new Exception("The module.path must exist: " + getModulePath());
	}
}
