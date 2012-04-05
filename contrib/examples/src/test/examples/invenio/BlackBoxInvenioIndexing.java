package examples.invenio;

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
	}
	
	public void testUpdates() throws Exception {
		SolrCore core = h.getCore();
		
		SolrRequestHandler handler = core.getRequestHandler("/invenio/update");
		
		((InvenioKeepRecidUpdated) handler).setAsynchronous(false);
		
		SolrQueryResponse rsp = new SolrQueryResponse();
		
		core.execute(handler, req("last_recid", "-1",
				"commit", "true"), rsp);
		
		Thread.sleep(500);
		String out = direct.request("/select?q=*:*", null);
		
		assert out.contains("numFound=\"87\"");
		
	}
	
	
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(BlackBoxInvenioIndexing.class);
    }
}
