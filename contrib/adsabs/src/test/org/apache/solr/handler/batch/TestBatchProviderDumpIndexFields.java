package org.apache.solr.handler.batch;

import org.adsabs.solr.AdsConfig.F;
import org.apache.solr.request.SolrQueryRequest;

public class TestBatchProviderDumpIndexFields extends BatchProviderTest {

	public void test() throws Exception {

		// now index some data
		assertU(adoc(F.ID, "1", F.BIBCODE, "xxxxxxxxxxxx1", "title", "green wall"));
		assertU(adoc(F.ID, "2", F.BIBCODE, "xxxxxxxxxxxx2", "title", "of trees"));
		assertU(adoc(F.ID, "3", F.BIBCODE, "xxxxxxxxxxxx3", "title", "blues sky"));
		assertU(adoc(F.ID, "4", F.BIBCODE, "xxxxxxxxxxxx4", "title", "of seas"));
		assertU(adoc(F.ID, "5", F.BIBCODE, "xxxxxxxxxxxx5", "title", "no fight"));
		assertU(adoc(F.ID, "6", F.BIBCODE, "xxxxxxxxxxxx6", "title", "for peace"));
		assertU(commit());
		// this creates another segment
		assertU(adoc(F.ID, "7", F.BIBCODE, "xxxxxxxxxxxx7", "title", "no fight"));
		assertU(adoc(F.ID, "8", F.BIBCODE, "xxxxxxxxxxxx8", "title", "for peace"));
		assertU(commit());
		
		
		BatchHandlerRequestQueue queue = new BatchHandlerRequestQueue();
		String tmpDir = System.getProperty("java.io.tmpdir");
		SolrQueryRequest req = req("jobid", "00000", "#workdir", tmpDir, "q", "title:wall OR title:peace", "fields", "id,title");
		
		BatchProviderDumpIndexFields provider = new BatchProviderDumpIndexFields();
		provider.run(req, queue);
		req.close();
		
		checkFile(tmpDir + "/00000", 
				"{\"id\":[\"1.0\"],\"title\":[\"green\",\"wall\"]},",
				"{\"id\":[\"6.0\"],\"title\":[\"for\",\"peace\"]},",
				"{\"id\":[\"8.0\"],\"title\":[\"for\",\"peace\"]}]"
		);
	}

	
	
}
