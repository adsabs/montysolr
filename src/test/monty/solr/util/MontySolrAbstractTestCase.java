package monty.solr.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import monty.solr.util.MontySolrTestCaseJ4;

import org.apache.commons.io.FileUtils;
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
	
	protected List<File> tempFiles;
	
	@Override
	  public void setUp() throws Exception {
		super.setUp();
		tempFiles = new ArrayList<File>();
	}
	
	@Override
	  public void tearDown() throws Exception {
		super.tearDown();
		for (File f: tempFiles) {
			recurseDelete(f);
		}
	}
	
	/**
	 * This must be called first, so that we make sure the Python 
	 * interpreter is loaded
	 * 
	 * @throws Exception
	 */
	public static void envInit() throws Exception {
		//System.setProperty("storeAll", "true");
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", 
				MontySolrSetup.getMontySolrHome() + "/src/python");
	}
	
	// normally we set this to point to default solr/example config
	// but BlackBox tests should override the method and return only
	// the solr home
	public String getSolrHome() {
		System.clearProperty("solr.solr.home"); // always force recomputing the solr.home
		return MontySolrSetup.getSolrHome() + "/example/solr";
	}
	
	/** @see MontySolrTestCaseJ4#getFile */
	public static File getFile(String name) {
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
	
	public File createTempFile(String...lines) throws IOException {
		File tmpFile = File.createTempFile("montySolr-unittest", null);
		if (lines.length > 0) {
			FileOutputStream fi = FileUtils.openOutputStream(tmpFile);
			StringBuffer out = new StringBuffer();
			for (String l: lines) {
				out.append(l + "\n");
			}
			FileUtils.writeStringToFile(tmpFile, out.toString(), "UTF-8");
		}
		return tmpFile;
	}
	
	public File duplicateFile(File origFile) throws IOException {
		File tmpFile = createTempFile();
		FileUtils.copyFile(origFile, tmpFile);
		return tmpFile;
	}
	
	public int replaceInFile(File target, String toFind, String replacement) throws IOException {
		return replaceInFile(target, Pattern.compile(toFind), replacement);
	}

	public int replaceInFile(File target, Pattern toFind, String replacement) throws IOException {
		int matches = 0;
		String contents = FileUtils.readFileToString(target);
		Matcher matcher = toFind.matcher(contents);
		while (matcher.find()) {
			matches++;
		}
		matcher.reset();
		contents = matcher.replaceAll(replacement);
		FileUtils.writeStringToFile(target, contents);
		return matches;
	}
	

}
