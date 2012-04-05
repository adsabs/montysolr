package examples.invenio;

import java.io.File;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.util.MontySolrSetup;

import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.InvenioKeepRecidUpdated;
import org.junit.BeforeClass;

import examples.BlackBoxAbstractTestCase;


public class BlackBoxInvenioIndexing extends BlackBoxAbstractTestCase{
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		setEName("invenio");
		exampleInit();
		MontySolrSetup.addTargetsToHandler("monty_invenio.targets");
		MontySolrSetup.addTargetsToHandler("monty_invenio.schema.targets");
		MontySolrSetup.addTargetsToHandler("monty_invenio.tests.demotest_updating");
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
		
		embedded.deleteByQuery("*:*");
		embedded.commit();
		
		
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
		assertQDirect("/select?q=*:*", null,
				"//*[@numFound='96']");
		
		// now do the same, but because the handler 
		assertQDirect("/invenio-updater?command=full-import&inveniourl=python://search&" +
				"importurl=/invenio-importer?command=full-import&amp;dirs%3D&" +
				"updateurl=/invenio-importer?command=full-import&amp;dirs%3D&" +
				"deleteurl=blankrecords",
				null,
				"//str[@name='importStatus']/text()='idle'");
		
		assertQDirect("/invenio-updater", null, "//str[@name='importStatus']/text()='idle'");
		
		
		
	}
	
	
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(BlackBoxInvenioIndexing.class);
    }
}
