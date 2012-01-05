package newseman;

import org.junit.BeforeClass;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.util.MontySolrAbstractLuceneTestCase;
import invenio.montysolr.util.MontySolrSetup;


public class MontySolrBaseTokenStreamTestCase extends MontySolrAbstractLuceneTestCase {
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		MontySolrSetup.init("montysolr.java_bridge.SimpleBridge", 
				MontySolrSetup.getMontySolrHome() + "/src/python");
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() + "/contrib/newseman/src/python");
		MontySolrSetup.addTargetsToHandler("monty_newseman.targets", "monty_newseman.tests.targets");
	}
	
}
