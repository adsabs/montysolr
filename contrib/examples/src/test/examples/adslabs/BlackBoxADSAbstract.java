package examples.adslabs;

import invenio.montysolr.util.MontySolrAbstractTestCase;
import invenio.montysolr.util.MontySolrSetup;

import org.junit.BeforeClass;

public abstract class BlackBoxADSAbstract extends MontySolrAbstractTestCase {

	static String ename = "adslabs";
	static String base = "/build/contrib/examples/" + ename; 
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		
		System.setProperty("solr.solr.home", MontySolrSetup.getMontySolrHome() + base + "/solr");
		
		System.err.println("Remember, BlackBox tests are to be run with example assembled!");
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", 
				MontySolrSetup.getMontySolrHome() + base + "/python");
		MontySolrSetup.addBuildProperties(base);
		
		
	}
	
	
	public String getSchemaFile() {
		return MontySolrSetup.getMontySolrHome() + base + 
		"/solr/conf/schema.xml";
	}

	
	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome() + base + 
		"/solr/conf/solrconfig.xml";
	}
}