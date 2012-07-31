package newseman;

import monty.solr.util.MontySolrAbstractLuceneTestCase;
import monty.solr.util.MontySolrSetup;

import org.junit.BeforeClass;



public class MontySolrBaseTokenStreamTestCase extends MontySolrAbstractLuceneTestCase {
	
	@BeforeClass
	public static void beforeMontySolrBaseTokenStreamTestCase() throws Exception {
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() + "/contrib/newseman/src/python");
		MontySolrSetup.addTargetsToHandler("monty_newseman.targets", "monty_newseman.tests.targets");
	}
	
}
