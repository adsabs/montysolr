package org.apache.solr;

import monty.solr.util.MontySolrSetup;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;


public class MontySolrTestCaseJ4 extends SolrTestCaseJ4 {


    public static String TEST_HOME() {
        return MontySolrSetup.getSolrHome();
    }

    public static String MONTYSOLR_HOME = MontySolrSetup.getMontySolrHome();
    private static final String SOLR_HOME = MontySolrSetup.getSolrHome();
    private static final String SOURCE_HOME = MONTYSOLR_HOME
            + "/src/test-files";
    public static String TEST_HOME = SOURCE_HOME + "/solr";
    public static String WEBAPP_HOME = new File(SOLR_HOME, "src/webapp/web")
            .getAbsolutePath();
    public static String EXAMPLE_HOME = new File(SOLR_HOME, "example/solr")
            .getAbsolutePath();
    public static String EXAMPLE_MULTICORE_HOME = new File(SOLR_HOME,
            "example/multicore").getAbsolutePath();
    public static String EXAMPLE_SCHEMA = EXAMPLE_HOME + "/conf/schema.xml";
    public static String EXAMPLE_CONFIG = EXAMPLE_HOME + "/conf/solrconfig.xml";


    /**
     * A trick to add resources to a classpath so that we can run
     * tests inside the development dir, but not necessarily install
     * and compile the whole solr distribution.
     * <p>
     * We cannot guarantee which resource will be loaded first if
     * it is present in both locations. So a warning is emitted.
     * Also, we are adding the default Solr example/solr/conf
     * <p>
     * This method, if run by a test, should be called from inside
     * getSchemaFile() because at that stage the instance already
     * contains a config
     */
    public static void makeResourcesVisible(ClassLoader loader, String... paths) {
        try {
            URLClassLoader innerLoader = (URLClassLoader) loader;
            Class<?> classLoader = URLClassLoader.class;
            Class[] params = new Class[]{URL.class};
            Method method = classLoader.getDeclaredMethod("addURL", params);
            method.setAccessible(true);

            for (String p : paths) {
                File f = new File(p);
                f = f.isDirectory() ? f : f.getParentFile();
                method.invoke(innerLoader, f.toURI().toURL());
                System.err.println("MontyDevel warning - adding resource path: " + f);
                System.err.println("If you encounter strange errors, then first check for duplicate files!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
