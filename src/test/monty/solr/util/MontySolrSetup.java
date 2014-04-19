package monty.solr.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;



/**
 * Automatic setup for Test classes, do not use this in a production
 * environment! (unless you are really too lazy ;))
 * 
 * @author rchyla
 *
 */


public class MontySolrSetup {
	private static HashMap<String, Properties> prop = new HashMap<String, Properties>();
	
	
	
	
	/**
	 * Load the properties from the file (build.properties)
	 * This is used only by the tests that need a working 
	 * MontySolr instance
	 * 
	 * By default, this returns the Properties for the core
	 * 
	 * @return Properties
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
	 * @return Properties
	 */
	
	public static Properties loadProperties(String base, String project) throws IllegalStateException {
		base = base + "/" + project;
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File(base
					+ "/build.properties")));
		} catch (IOException e) {
			throw new IllegalStateException("Your montysolr installation does not have "
					+ base + "/build.properties file! "
					+ "You should fix this (by running 'ant build-all', or 'ant write-properties')");
		}
		return prop;
	}

	public static String getSolrHome() {
		String base = getMontySolrHome();
		
		String home = System.getProperty("solr.solr.home");
		if (home!= null ) {
			if ((new File(home)).exists() && System.getProperty("montysolr.ignore.home") != null) {
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
					"solr.home is not accessible - you should run ant write-properties: "
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
	 * @return String
	 */
	public static String getMontySolrHome() {
		File base = new File(System.getProperty("user.dir"));
		// File base = getFile("solr/conf").getAbsoluteFile();
		while (!new File(base, "contrib/adsabs").exists()) {
			base = base.getParentFile();
		}
		return base.getAbsolutePath();
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
