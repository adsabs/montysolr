package examples.adsabs;

import java.io.File;


import montysolr.util.MontySolrSetup;

import org.apache.solr.core.SolrCore;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.InvenioKeepRecidUpdated;
import org.junit.BeforeClass;

import examples.BlackAbstractTestCase;


public class BlackBoxAdslabsIndexing extends BlackAbstractTestCase{
	
	@BeforeClass
	public static void beforeBlackBoxAdslabsIndexing() throws Exception {
		setEName("adsabs");
		exampleInit();
		MontySolrSetup.addTargetsToHandler("monty_invenio.targets");
		MontySolrSetup.addTargetsToHandler("monty_invenio.schema.targets");
		//MontySolrSetup.addTargetsToHandler("monty_invenio.tests.demotest_updating");
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		// to have always the demo records as a fresh site
		//PythonMessage message = MontySolrVM.INSTANCE.createMessage("reset_records");
		//MontySolrVM.INSTANCE.sendMessage(message);
		
	}
	
	public void testUpdates() throws Exception {
		SolrCore core = h.getCore();
		SolrQueryResponse rsp = new SolrQueryResponse();
		String out = "";
		
		System.out.println(direct.request("/select?q=*:*", null));
		embedded.deleteByQuery("*:*");
		embedded.commit();
		
		
		InvenioKeepRecidUpdated uHandler = (InvenioKeepRecidUpdated) core.getRequestHandler("/invenio/update");
		uHandler.setAsynchronous(false);
		
		// remove the update file (if exists)
		File f = uHandler.getPropertyFile();
		if (f.exists()) {
			f.delete();
		}
		
		core.execute(uHandler, req("last_recid", "-1", "inveniourl", "python://search",
				"importurl", "/invenio/import?command=full-import&amp;dirs=",
				"updateurl", "/invenio/import?command=full-import&amp;dirs=",
				"deleteurl", "blankrecords",
				"maximport", "200"
				), rsp);
		
		assertTrue(rsp.getException() == null);
		
		embedded.commit(true, true);
				
	}
	
	
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(BlackBoxAdslabsIndexing.class);
    }
}
