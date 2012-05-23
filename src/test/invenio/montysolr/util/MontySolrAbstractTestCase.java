package invenio.montysolr.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import invenio.montysolr.util.MontySolrTestCaseJ4;

import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.servlet.DirectSolrConnection;
import org.apache.solr.util.AbstractSolrTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class MontySolrAbstractTestCase extends AbstractSolrTestCase {

	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		envInit();
	}

	@AfterClass
	public static void afterClassMontySolrTestCase() throws Exception {
	}
	
	/**
	 * This must be called first, so that we make sure the Python 
	 * interpreter is loaded
	 * 
	 * @throws Exception
	 */
	public static void envInit() throws Exception {
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", 
				MontySolrSetup.getMontySolrHome() + "/src/python");
	}
	
	// normally we set this to point to default solr/example config
	// but BlackBox tests should override the method and return only
	// the solr home
	public String getSolrHome() {
		return MontySolrSetup.getSolrHome() + "/example/solr";
	}
	
	/** @see MontySolrTestCaseJ4#getFile */
	public static File getFile(String name) throws IOException {
		return MontySolrTestCaseJ4.getFile(name);
	}
	
	
	/**
	 * A trick to add resources to a classpath so that we can run
	 * tests inside the development dir, but not necessarily install
	 * and compile the whole solr distribution.
	 * 
	 * We cannot guarantee which resource will be loaded first if
	 * it is present in both locations. So a warning is emitted.
	 * Also, we are adding the default Solr example/solr/conf
	 * 
	 * This method, if run by a test, should be called from inside
	 * getSchemaFile() because at that stage the instance already
	 * contains a config
	 * 
	 * @param loader
	 */
	public static void makeResourcesVisible(SolrResourceLoader loader, String...paths) {
		try {
			URLClassLoader innerLoader = (URLClassLoader) loader.getClassLoader();
			Class<?> classLoader = URLClassLoader.class;
			Class[] params = new Class[]{ URL.class };  
			Method method = classLoader.getDeclaredMethod( "addURL", params);
		    method.setAccessible( true );
		    
		    for (String p: paths) {
		    	File f = new File(p);
		    	f = f.isDirectory() ? f : f.getParentFile();
		    	method.invoke( innerLoader, new Object[]{ f.toURI().toURL() } );
		    	System.err.println("MontyDevel warning - adding resource path: " + f);
		    	System.err.println("If you encounter strange errors, then first check for duplicate files!!!");
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EmbeddedSolrServer getEmbeddedServer() {
		return new EmbeddedSolrServer(h.getCoreContainer(), h.getCore().getName());
	}
	
	public DirectSolrConnection getDirectServer() {
		return new DirectSolrConnection(h.getCore());
	}
}
