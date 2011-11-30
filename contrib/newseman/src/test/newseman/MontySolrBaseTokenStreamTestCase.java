package newseman;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.util.MontySolrAbstractLuceneTestCase;


public class MontySolrBaseTokenStreamTestCase extends MontySolrAbstractLuceneTestCase {
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		addToSysPath(getMontySolrHome() + "/contrib/newseman/src/python");
		
		addTargetsToHandler("monty_newseman.targets", "monty_newseman.tests.targets");
		
	}
	
}
