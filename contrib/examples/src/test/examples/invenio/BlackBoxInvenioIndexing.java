package examples.invenio;

import java.io.File;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.util.MontySolrSetup;

import org.apache.solr.core.SolrCore;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.InvenioKeepRecidUpdated;
import org.junit.BeforeClass;



import examples.BlackAbstractTestCase;


public class BlackBoxInvenioIndexing extends BlackAbstractTestCase{
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		setEName("invenio");
		exampleInit();
		MontySolrSetup.addTargetsToHandler("monty_invenio.targets");
		MontySolrSetup.addTargetsToHandler("monty_invenio.schema.targets");
		MontySolrSetup.addTargetsToHandler("monty_invenio.tests.demotest_updating");
		//System.setProperty("blackbox.persist.index", getExampleHome() + "/solr/data");
		}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		// to have always the demo records as a fresh site
		PythonMessage message = MontySolrVM.INSTANCE.createMessage("reset_records");
		MontySolrVM.INSTANCE.sendMessage(message);
		
	}
	
	public void testUpdates() throws Exception {
		SolrCore core = h.getCore();
		SolrQueryResponse rsp = new SolrQueryResponse();
		String out = "";
		
		
		InvenioKeepRecidUpdated uHandler = (InvenioKeepRecidUpdated) core.getRequestHandler("/invenio-updater");
		uHandler.setAsynchronous(false);
		
		// remove the update file (if exists)
		File f = uHandler.getPropertyFile();
		if (f.exists()) {
			f.delete();
		}
		
		core.execute(uHandler, req("last_recid", "-1", "inveniourl", "python://search",
				"importurl", "/invenio-importer?command=full-import&amp;dirs=",
				"updateurl", "/invenio-importer?command=full-import&amp;dirs=",
				"deleteurl", "blankrecords"
				), rsp);
		
		assertQDirect("/invenio-updater?command=full-import&inveniourl=python://search&", null,
				"//str[@name='importStatus']/text()='idle'");
		
		assertQDirect("/select?q=*:*", null,
				"//*[@numFound='0']");
		
		
		embedded.commit(true, true);
		assertQDirect("/select?q=*:*&fl=id", null,
			"//*[@numFound='104']");
		
		
		// now do the same, but because the handler 
		assertQDirect("/invenio-updater?command=full-import&inveniourl=python://search&" +
				"importurl=/invenio-importer?command=full-import&amp;dirs%3D&" +
				"updateurl=/invenio-importer?command=full-import&amp;dirs%3D&" +
				"deleteurl=blankrecords",
				null,
				"//str[@name='importStatus']/text()='idle'");
		
		assertQDirect("/invenio-updater", null, "//str[@name='importStatus']/text()='idle'");
		
		
		uHandler.setAsynchronous(true);
		core.execute(uHandler, req("last_recid", "-1", "inveniourl", "python://search",
				"importurl", "/invenio-importer?command=full-import&amp;dirs=",
				"updateurl", "/invenio-importer?command=full-import&amp;dirs=",
				"deleteurl", "blankrecords"
				), rsp);
		assertQDirect("/invenio-updater", null, "//str[@name='importStatus']/text()='busy'");
		
		try {
			while (true) {
				Thread.sleep(50);
				assertQDirect("/invenio-updater", null, "//str[@name='importStatus']/text()='busy'");
			}
		}
		catch (AssertionError r) {
			assertQDirect("/invenio-updater", null, "//str[@name='importStatus']/text()='idle'");
		}
		
		embedded.deleteByQuery("*:*");
		embedded.commit(true, true);
		
		uHandler.setAsynchronous(false);
		// now import in batches per 20 recs
		core.execute(uHandler, req("last_recid", "-1", "inveniourl", "python://search",
				"importurl", "/invenio-importer?command=full-import&amp;dirs=",
				"updateurl", "/invenio-importer?command=full-import&amp;dirs=",
				"deleteurl", "blankrecords",
				"maximport", "20"
				), rsp);
		
		
		embedded.commit(true, true);
		assertQDirect("/select?q=*:*&fl=id", null,
			"//*[@numFound='104']");
		
	}
	
	
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(BlackBoxInvenioIndexing.class);
    }
}
