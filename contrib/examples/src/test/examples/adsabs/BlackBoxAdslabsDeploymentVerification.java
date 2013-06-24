package examples.adsabs;

import java.io.File;
import java.net.URLEncoder;


import monty.solr.util.MontySolrSetup;

import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.dataimport.WaitingDataImportHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.InvenioKeepRecidUpdated;
import org.junit.BeforeClass;

import examples.BlackAbstractTestCase;


public class BlackBoxAdslabsDeploymentVerification extends BlackAbstractTestCase{
	
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
		
		//System.out.println(direct.request("/select?q=*:*", null));
		assertQ(req("q", "*:*"), "//*[@numFound='0']");

		
		embedded.commit();
		
		String data;
		//data = direct.request("/invenio/import?last_recid=-1&url=python%3A%2F%2Fsearch%3Fp%3Drecid%3A1->104", null);
		//data = direct.request("/invenio/update?last_recid=-1", null);
		
		// have to do it this way to avoid async requests
		WaitingDataImportHandler updater = (WaitingDataImportHandler) h.getCore().getRequestHandler("/invenio/import");
		SolrQueryRequest req = req("command", "full-import",
				"commit", "true",
				"url", "python://search?p=" + URLEncoder.encode("recid:1->10", "UTF-8")
				);
		core.execute(updater, req, rsp);
		
		
		assertU(commit());
		assertQ(req("q", "*:*", "fl", "recid,title"), "//*[@numFound='10']");
		
		// check we have gotten data from mongo
		
		assertQ(req("q", "full:a*"), "//*[@numFound>0]");
		assertQ(req("q", "read_count:[0.0 TO 100.0]"), "//*[@numFound>0]");
		
		// these queries will not find anything, but we can test the proper
		// search behaviour (the extensive tests are in separate classes,
		// here we just make sure that handful of queries is expanded/transformed
		// properly (that all synonym files are at place etc)
		data = direct.request("/select?q=MÜLLER&debugQuery=true&wt=json", null);
		
		data.contains("(abstract:acr::müller abstract:acr::muller)^1.3)");
		data.contains("(author:müller, author:müller,* author:mueller, author:mueller,* author:muller, author:muller,*)^2.0");
		data.contains("DisjunctionMaxQuery(");
		
		data = direct.request("/select?q=accomazzi&debugQuery=true&wt=json", null);
		data.contains("abstract:accomazzi");
		data.contains("title:accomazzi");
		data.contains("full:accomazzi");
		data.contains("abstract:accomazzi");
		data.contains("keyword:accomazzi");
		data.contains("acc:accomazzi");
		
		
		// #231 - use 'aqp' as a default parser also for filter queries
		assert direct.request("/select?q=*:*&fq={!aqp}author:\"Civano, F\"&debugQuery=true&wt=json", null)
    	.contains("author:civano, f author:civano, f* author:civano,");
		
		// check we are using synonym for translation
		data = direct.request("/select?q=AAS&debugQuery=true&wt=json", null);
		assert data.contains("title:syn::american astronomical society");
		assert data.contains("title:acr::aas");
		
		data = direct.request("/select?q=aborigines&debugQuery=true&wt=json", null);
		assert data.contains("title:syn::aboriginal");
		
		data = direct.request("/select?q=\"STERN, CAROLYN P\"&debugQuery=true&wt=json", null);
		assert data.contains("author:stern grant, c");
		
		data = direct.request("/select?q=author:\"Boser, S\"&debugQuery=true&wt=json", null);
		assert data.contains("author:böser, s");
				
	}
	
	
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(BlackBoxAdslabsDeploymentVerification.class);
    }
}
