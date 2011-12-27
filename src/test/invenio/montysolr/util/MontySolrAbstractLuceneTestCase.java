package invenio.montysolr.util;

import invenio.montysolr.jni.MontySolrVM;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import invenio.montysolr.util.MontySolrTestCaseJ4;
import invenio.montysolr.util.ProcessUtils;

import org.apache.lucene.util.LuceneTestCase;


public abstract class MontySolrAbstractLuceneTestCase extends LuceneTestCase {
	
	public void setUp() throws Exception {
		super.setUp();
		
		// chaning PYTHONPATH (has no effect on the embedded interpereter)
		//String pythonpath = MontySolrTestCaseJ4.MONTYSOLR_HOME + "/src/python";
		//ProcessUtils.addEnv("PYTHONPATH", pythonpath);
		
		// the path added to sys.path is the parent
		System.setProperty("montysolr.modulepath", getChildModulePath());
		
		
		// discover and set -Djava.library.path
		String jccpath = ProcessUtils.getJCCPath();
		ProcessUtils.setLibraryPath(jccpath);
		
		// this is necessary to run in the main thread and because of the 
		// python loads the parent folder and inserts it into the pythonpath
		// we trick it
		MontySolrVM.INSTANCE.start(getModulePath());
		
		System.setProperty("montysolr.bridge", getModuleName());

		// for testing purposes add what is in the pythonpath
		File f = new File(getMontySolrHome() + "/build/build.properties");
		if (f.exists()) {
			Properties p = new Properties();
			p.load(new FileInputStream(f));
			if (p.containsKey("python_path")) {
				String pp = p.getProperty("python_path");
				addToSysPath(pp);
			}
		}
	}
	
	public String getMontySolrHome() {
		return MontySolrTestCaseJ4.MONTYSOLR_HOME;
	}
	
	
	/**
	 * A fully qualified name of the python module that will be loaded
	 * eg: package.module.ClassName
	 * 
	 * @return
	 * @throws IllegalStateException
	 */
	public String getModuleName() throws IllegalStateException {
		throw new IllegalStateException("You must implement this in your class!");
	}
	
	/**
	 * Folder that should be added to sys.path upon startup. It must 
	 * exist and be a valid path (of one folder)
	 * @return
	 */
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
	
	
	public void addToSysPath(String... paths) {
		for (String path: paths) {
			MontySolrVM.INSTANCE.evalCommand("import sys;\'" + path + "\' in sys.path or sys.path.insert(0, \'" + path + "\')");
		}
	}
	
	public void addTargetsToHandler(String... modules) {
		for (String path: modules) {
			// this is a quick hack, i should make the handler to have a defined place (or find some better way of adding)
			MontySolrVM.INSTANCE.evalCommand("self._handler.discover_targets([\'" + path + "\'])");
		}
	}
}
