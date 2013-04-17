package examples.adsabs;

import java.io.File;


import monty.solr.util.MontySolrSetup;

import org.apache.solr.core.SolrCore;
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
		embedded.deleteByQuery("*:*");
		embedded.commit();
		
		// these queries will not find anything, but we can test the proper
		// search behaviour (the extensive tests are in separate classes,
		// here we just make sure that handful of queries is expanded/transformed
		// properly (that all synonym files are at place etc)
		assert direct.request("/select?q=MÜLLER&debugQuery=true&wt=json", null)
		  .contains("(((abstract:acr::müller abstract:acr::muller)^1.3) " +
		  		"| ((author:müller, author:müller,* author:mueller, author:mueller,* author:muller, author:muller,*)^2.0) " +
		  		"| ((title:acr::müller title:acr::muller)^1.4) " +
		  		"| full:acr::MULLER^0.7 | keyword:muller^1.4 " +
		  		"| keyword_norm:muller^1.4 " +
		  		"| (all:acr::müller all:acr::muller))");
		assert direct.request("/select?q=accomazzi&debugQuery=true&wt=json", null)
      .contains("(abstract:accomazzi^1.3 " +
      		"| ((author:accomazzi, author:accomazzi,*)^2.0) " +
      		"| title:accomazzi^1.4 " +
      		"| full:accomazzi^0.7 " +
      		"| keyword:accomazzi^1.4 " +
      		"| keyword_norm:accomazzi^1.4 " +
      		"| all:accomazzi)");
		
		// #231 - use 'aqp' as a default parser also for filter queries
		assert direct.request("/select?q=*:*&fq={!aqp}author:\"Civano, F\"&debugQuery=true&wt=json", null)
    	.contains("author:civano, f author:civano, f* author:civano,");
		
		
		
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
        return new junit.framework.JUnit4TestAdapter(BlackBoxAdslabsDeploymentVerification.class);
    }
}
