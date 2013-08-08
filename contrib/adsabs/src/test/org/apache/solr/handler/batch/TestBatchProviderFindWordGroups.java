package org.apache.solr.handler.batch;

import org.adsabs.solr.AdsConfig.F;
import org.apache.solr.request.SolrQueryRequest;

public class TestBatchProviderFindWordGroups extends BatchProviderTest {


	public void test() throws Exception {

		// now index some data
		assertU(adoc(F.ID, "1", F.BIBCODE, "xxxxxxxxxxxx1", F.TYPE_ADS_TEXT, "green wall"));
		assertU(adoc(F.ID, "2", F.BIBCODE, "xxxxxxxxxxxx2", F.TYPE_ADS_TEXT, "of trees"));
		assertU(adoc(F.ID, "3", F.BIBCODE, "xxxxxxxxxxxx3", F.TYPE_ADS_TEXT, "blues sky"));
		assertU(adoc(F.ID, "4", F.BIBCODE, "xxxxxxxxxxxx4", F.TYPE_ADS_TEXT, "of seas"));
		assertU(adoc(F.ID, "5", F.BIBCODE, "xxxxxxxxxxxx5", F.TYPE_ADS_TEXT, "no fight"));
		assertU(adoc(F.ID, "6", F.BIBCODE, "xxxxxxxxxxxx6", F.TYPE_ADS_TEXT, "for peace"));
		assertU(commit());
		// this creates another segment
		assertU(adoc(F.ID, "7", F.BIBCODE, "xxxxxxxxxxxx7", F.TYPE_ADS_TEXT, "no fight"));
		assertU(adoc(F.ID, "8", F.BIBCODE, "xxxxxxxxxxxx8", F.TYPE_ADS_TEXT, "for peace"));
		assertU(commit());
		
		
		BatchHandlerRequestQueue queue = new BatchHandlerRequestQueue();
		String tmpDir = System.getProperty("java.io.tmpdir");
		SolrQueryRequest req = req("jobid", "00000", 
				"#workdir", tmpDir,
				"fields", F.TYPE_ADS_TEXT,
				"maxDistance", "2"
				);
		
		BatchProviderDumpIndex provider = new BatchProviderDumpIndex();
		provider.run(req, queue);
		req.close();
		
		checkFile(tmpDir + "/00000", 
				"{\"id\":1.0,\"bibcode\":\"xxxxxxxxxxxx1\",\"title\":\"green wall\"}",
				",{\"id\":5.0,\"bibcode\":\"xxxxxxxxxxxx5\",\"title\":\"no fight\"}",
				",{\"id\":6.0,\"bibcode\":\"xxxxxxxxxxxx6\",\"title\":\"for peace\"}",
				",{\"id\":7.0,\"bibcode\":\"xxxxxxxxxxxx7\",\"title\":\"no fight\"}",
				",{\"id\":8.0,\"bibcode\":\"xxxxxxxxxxxx8\",\"title\":\"for peace\"}"
		);
	}
	
	
}
