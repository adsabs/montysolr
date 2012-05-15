package invenio.montysolr.util;

import invenio.montysolr.jni.MontySolrVM;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;


/**
 * Automatic setup for Test classes, do not use this in a production
 * classes!
 * 
 * @author rchyla
 *
 */


public class MontySolrSetup {
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
	
	
	
	public static void checkJCCPath() throws Exception {
		
		// first check if we have the build/build.properties
		try {
			Properties p = loadProperties(getMontySolrHome(), "build");
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
	
	/**
	 * Load the properties from the file (build.properties)
	 * This is used only by the tests that need a working 
	 * MontySolr instance
	 * 
	 * By default, this returns the Properties for the core
	 * 
	 * @return
	 */
	public static Properties getProperties(String projectName) {
		return getProperties(getMontySolrHome(), projectName); // the default MontySolr main project
	}

	public static Properties getProperties(String base, String project) {
		project = project.trim();
		while (project.indexOf("/") == 0) {
			project = project.substring(1);
		}
		while (project.indexOf("/") == project.length()) {
			project = project.substring(0, project.length()-1);
		}
		if (prop.containsKey(project)) {
			return prop.get(project);
		}
		
		Properties p = loadProperties(base, project);
		prop.put(project, p);
		return p;
	}

	/**
	 * MontySolr depends on the properties file for unittesting, as it was too
	 * much hassle to load interpolated strings from the build.properties file
	 * (without adding more jars) and the discovery of files using file
	 * structure crawling is ugly.
	 * 
	 * @return
	 * @throws IOException
	 */
	
	private static Properties loadProperties(String base, String project) throws IllegalStateException {
		base = base + "/" + project;
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File(base
					+ "/build.properties")));
		} catch (IOException e) {
			throw new IllegalStateException("Your montysolr installation does not have "
					+ base + "/build.properties file! "
					+ "You should fix this (by running ant build-all for the core)");
		}
		return prop;
	}

	public static String getSolrHome() {
		String base = getMontySolrHome();
		
		String home = System.getProperty("solr.solr.home");
		if (home!= null ) {
			if ((new File(home)).exists()) {
				System.err.println("MontySolrSetup: solr.solr.home is set to \'" + home + "\'\n" +
						           "...ignoring the test setup");
				return home;
			}
			else {
				System.err.println("MontySolrSetup: solr.solr.home is set to \'" + home + "\'\n" +
		           "...but this path does not exist");
			}
		}
		
		String solr_home = null;
		
		Properties p = getProperties("build");
		if (p != null && p.containsKey("solr.home")) {
			solr_home = p.getProperty("solr.home");
		}

		if (solr_home == null || !(new File(solr_home).exists())) {
			throw new IllegalStateException(
					"Your montysolr build.properties file has incorrect value for solr.home: "
							+ solr_home);
		}

		File s = new File(solr_home);
		File s_level_down = new File(s.getAbsolutePath() + "/solr");

		if (s_level_down.exists())
			return s_level_down.getAbsolutePath();

		if (s.exists())
			return s.getAbsolutePath();

		throw new IllegalStateException(
				"Cannot determine the folder with solr installation");
	}

	/**
	 * Gets a resource from the context classloader as {@link File}. This method
	 * should only be used, if a real file is needed. To get a stream, code
	 * should prefer {@link Class#getResourceAsStream} using
	 * {@code this.getClass()}.
	 */
	public static File getFile(String name) {
		try {
			File file = new File(name);
			if (!file.exists()) {
				file = new File(Thread.currentThread().getContextClassLoader()
						.getResource(name).toURI());
			}
			return file;
		} catch (Exception e) {
			/* more friendly than NPE */
			throw new RuntimeException("Cannot find resource: " + name);
		}
	}

	static String determineMontySourceHome() {
		File base = getFile("examples/README.txt").getAbsoluteFile();
		return new File(base.getParentFile().getParentFile(), "test-files/")
				.getAbsolutePath();
	}

	/**
	 * Returns the root folder of montysolr
	 * 
	 * @return
	 */
	public static String getMontySolrHome() {
		File base = new File(System.getProperty("user.dir"));
		// File base = getFile("solr/conf").getAbsoluteFile();
		while (!new File(base, "src/python").exists()) {
			base = base.getParentFile();
		}
		return base.getAbsolutePath();
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
	 * @return
	 * @throws Exception
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
