package monty.solr.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import monty.solr.jni.MontySolrVM;
import monty.solr.util.ProcessUtils;


/**
 * Automatic setup for Test classes, do not use this in a production
 * environment! (unless you are really too lazy ;))
 * 
 * @author rchyla
 *
 */


public class PytoneSolrSetup extends MontySolrSetup {
	private static HashMap<String, Properties> prop = new HashMap<String, Properties>();
	
	
	/**
	 * Initializes the environment so that tests that expect JCC 
	 * environment can run properly. 
	 * 
	 * @param mainModuleName
	 * @param mainModulePath
	 * @throws Exception
	 */
	public static void init(String mainModuleName, String mainModulePath)
			throws Exception {


		// the path added to sys.path is the parent
		System.setProperty("montysolr.modulepath",
				getChildModulePath(mainModulePath));

		// discover and set -Djava.library.path
		checkJCCPath();
		

		// this is necessary to run in the main thread and because of the
		// python loads the parent folder and inserts it into the pythonpath
		// we trick it to add the folder to sys.path
		MontySolrVM.INSTANCE.start(getChildModulePath(mainModulePath));
		
		
		// used by MontySolrVM instance to recognize the handler
		System.setProperty("montysolr.bridge", mainModuleName);
		
		
		
		// Now add to PYTHONPATH the core (you can call it repeatedly, eg.
		// for contrib modules)
		
		// chaning PYTHONPATH (has no effect on the embedded interpereter)
		// String pythonpath = MontySolrTestCaseJ4.MONTYSOLR_HOME + "/src/python";
		// ProcessUtils.addEnv("PYTHONPATH", pythonpath);
		
		// so we must do it using python
		addBuildProperties("build");

		// other methods like starting a jetty instance need these too
		System.setProperty("solr.test.sys.prop1", "propone");
		System.setProperty("solr.test.sys.prop2", "proptwo");
		System.setProperty("montysolr.home", getMontySolrHome());
		
	}
	
	
	/*
	 * Should be called by classes that manually called init()
	 * If you get property violation exception from Lucene
	 */
	public static void deinit() {
		System.clearProperty("montysolr.bridge");
		System.clearProperty("montysolr.home");
		System.clearProperty("montysolr.modulepath");
		System.clearProperty("solr.test.sys.prop1");
		System.clearProperty("solr.test.sys.prop2");
  }
	
	public static void checkJCCPath() throws Exception {
		
		// first check if we have the build/build.properties
		try {
			Properties p = MontySolrSetup.loadProperties(getMontySolrHome(), "build");
			if (p.containsKey("python")) {
				ProcessUtils.checkJCCPath(p.getProperty("python").trim());
				return;
			}
			
		} catch (IllegalStateException e) {
			// pass
		}
		
		ProcessUtils.checkJCCPath(null);
		
	}


	/**
   * Will find the build.properties for the project and 
   * set some interesting variables into PYTHON
   */
  
  public static void addBuildProperties(String projectName) {
    Properties p = loadProperties(getMontySolrHome(), projectName);
    if (p.containsKey("python_path")) {
      String pp = p.getProperty("python_path");
      addToSysPath(pp);
    }
    
  }

	public static void addToSysPath(String... paths) {
		for (String path : paths) {
			MontySolrVM.INSTANCE.evalCommand("import sys;\'" + path
					+ "\' in sys.path or sys.path.append(\'" + path + "\')");
		}
	}

	public static void addTargetsToHandler(String... modules) {
		for (String path : modules) {
			// this is a quick hack, i should make the handler to have a defined
			// place (or find some better way of adding)
			MontySolrVM.INSTANCE
					.evalCommand("self._handler.discover_targets([\'" + path
							+ "\'])");
		}
	}
	
	public static void evalCommand(String... commands) {
		for (String command : commands) {
			System.out.println(command);
			MontySolrVM.INSTANCE.evalCommand(command);
		}
	}
	
	/**
	 * Trick to find any existing folder/file inside the main module path and
	 * return it to be set by python into the PYTHONPATH
	 * 
	 * @return String
	 */
	public static String getChildModulePath(String modulePath) throws Exception {
		File f = new File(modulePath);
		if (f.isFile()) {
			return f.getAbsolutePath().toString();
		} else if (f.exists()) {
			for (String child : f.list()) {
				return new File(f.getAbsolutePath() + "/" + child)
						.getAbsolutePath();
			}
		}
		throw new Exception("The module.path must exist: " + modulePath);
	}

	
}
