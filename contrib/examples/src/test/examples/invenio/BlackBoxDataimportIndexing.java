package examples.invenio;

import invenio.montysolr.util.MontySolrSetup;

import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.junit.BeforeClass;

import examples.BlackBoxAbstractTestCase;


public class BlackBoxDataimportIndexing extends BlackBoxAbstractTestCase{
	
	String mainUrl = "http://inspirebeta.net/search";
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		setEName("invenio");
		exampleInit();
		MontySolrSetup.addTargetsToHandler("monty_invenio.targets");
		MontySolrSetup.addTargetsToHandler("monty_invenio.schema.targets");
	}
	
	public void testUpdates() throws InterruptedException {
		SolrCore core = h.getCore();
		
		SolrRequestHandler handler = core.getRequestHandler("/invenio/import");
		
		SolrQueryResponse rsp = new SolrQueryResponse();
		
		core.execute(handler, req("command", "full-import",
				"url", mainUrl + "?p=recid%3A50-%3E95&of=xm&rg=200",
				"commit", "true"), rsp);
		
		while (isIdle(handler) == false) {
			Thread.sleep(50);
		}
		
		Thread.sleep(100);
		assertQ(req("q", "*:*"),
	            "//*[@numFound='46']"
	            );
	}
	
	private boolean isIdle(SolrRequestHandler handler, String ... vals) {
		SolrQueryResponse rsp = new SolrQueryResponse();
		h.getCore().execute(handler, req(vals), rsp);
		String status = (String) rsp.getValues().get("status");
		if (status == "idle") {
			return true;
		}
		return false;
	}
	
	
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(BlackBoxDataimportIndexing.class);
    }
}
