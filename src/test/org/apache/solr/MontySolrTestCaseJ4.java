package org.apache.solr;

import java.io.File;

public abstract class MontySolrTestCaseJ4 extends SolrTestCaseJ4 {
	
	private static final String SOURCE_SOLR_HOME = determineSourceHome();
	private static final String SOURCE_HOME = getMontySolrHome() + "/src/test-files";
	public static String MONTYSOLR_HOME = getMontySolrHome();
	public static String TEST_HOME = SOURCE_HOME + "/solr"; //getFile("solr/conf").getParent();
	public static String WEBAPP_HOME = new File(SOURCE_SOLR_HOME, "src/webapp/web")
			.getAbsolutePath();
	public static String EXAMPLE_HOME = new File(SOURCE_SOLR_HOME, "example/solr")
			.getAbsolutePath();
	public static String EXAMPLE_MULTICORE_HOME = new File(SOURCE_SOLR_HOME,
			"example/multicore").getAbsolutePath();
	public static String EXAMPLE_SCHEMA = EXAMPLE_HOME + "/conf/schema.xml";
	public static String EXAMPLE_CONFIG = EXAMPLE_HOME + "/conf/solrconfig.xml";
	
	static String determineMontySourceHome() {
	    // ugly, ugly hack to determine the example home without depending on the CWD
	    // this is needed for example/multicore tests which reside outside the classpath
	    File base = getFile("examples/README.txt").getAbsoluteFile();
	    return new File(base.getParentFile().getParentFile(), "test-files/").getAbsolutePath();
	  }
	
	/**
	 * Returns the root folder with montysolr code/files
	 * @return
	 */
	public static String getMontySolrHome() {
		File base = getFile("src/python/").getAbsoluteFile();
	    while (!new File(base, "README").exists()) {
	      base = base.getParentFile();
	    }
	    return base.getAbsolutePath();
	    
	}
}
