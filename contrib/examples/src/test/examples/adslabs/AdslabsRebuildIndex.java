package examples.adslabs;

import invenio.montysolr.util.MontySolrSetup;

import org.apache.solr.core.SolrCore;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.InvenioKeepRecidUpdated;
import org.junit.BeforeClass;

import examples.BlackAbstractTestCase;


public class AdslabsRebuildIndex extends BlackAbstractTestCase{
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		setEName("adslabs");
		exampleInit();
		MontySolrSetup.addTargetsToHandler("monty_invenio.targets");
		MontySolrSetup.addTargetsToHandler("monty_invenio.schema.targets");
		
		System.setProperty("blackbox.persist.index", getExampleHome() + "/solr/data");
		System.setProperty("java.util.logging.config.file", getExampleHome() + "/etc/logging.properties");
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
		
		long start = System.currentTimeMillis();
		long timeNow = System.currentTimeMillis();
		long lastGo = System.currentTimeMillis();
		int maximport = 200;
		int batchSize =  10000;
		int maxRecords = 10000000;
		long maxTime = 48 * 3600 * 1000;
		int recsSoFar = 0;
		long minTimeSpentIndexing = 2000;
		int i = 0;
		
		InvenioKeepRecidUpdated uHandler = (InvenioKeepRecidUpdated) core.getRequestHandler("/invenio/update");
		uHandler.setAsynchronous(false);
		
		// initialization (from the beginning)
		core.execute(uHandler, req("last_recid", "-1", "inveniourl", "python://search",
				"importurl", "/invenio/import?command=full-import&amp;dirs=",
				"updateurl", "/invenio/import?command=full-import&amp;dirs=",
				"deleteurl", "blankrecords",
				"maximport", String.valueOf(maximport),
				"batchsize", String.valueOf(batchSize)
				), rsp);
		
		
		recsSoFar = recsSoFar+batchSize;
		timeNow = System.currentTimeMillis();
		
		System.out.println("Indexing (round/recs/last-round-ms/total-s): " + 
				i + "./" 
				+ recsSoFar + "/" 
				+ (timeNow-lastGo) + "ms./" 
				+ ((timeNow-start)/1000) + "s.");
		
		// now run in a loop x times or x hours, whatever comes earlier
		Integer loops = Integer.valueOf(maxRecords / batchSize);
		
		while (i < loops && (timeNow - start) < maxTime) {
			rsp = new SolrQueryResponse();
			lastGo = System.currentTimeMillis();
			core.execute(uHandler, req("inveniourl", "python://search",
					"importurl", "/invenio/import?command=full-import&amp;dirs=",
					"updateurl", "/invenio/import?command=full-import&amp;dirs=",
					"deleteurl", "blankrecords",
					"maximport", String.valueOf(maximport),
					"batchsize", String.valueOf(batchSize)
					), rsp);
			timeNow = System.currentTimeMillis();
			i++;
			recsSoFar = recsSoFar+batchSize;
			
			System.out.println("Indexing (round/recs/last-round-ms/total-s): " + 
					i + "./" 
					+ recsSoFar + "/" 
					+ (timeNow-lastGo) + "ms./" 
					+ ((timeNow-start)/1000) + "s.");
			
			if ((timeNow-lastGo) < minTimeSpentIndexing) {
				System.out.println("Seems we have finished, stopping...");
				break;
			}
		}
		
		System.out.println("Committing index...");
		embedded.commit(true, true);
		
		System.out.println(direct.request("/select?q=*:*&fl=id", null));
				
	}
	
	
	
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(AdslabsRebuildIndex.class);
    }
}
