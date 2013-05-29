package org.apache.solr.handler.batch;

import org.apache.solr.request.SolrQueryRequest;

public class TestBatchProviderDumpCitationCache extends BatchProviderTest {


	public void test() throws Exception {

		assertU(adoc("id", "1", "references", "2", "references", "3", "references", "4", "x", "test"));
		assertU(adoc("id", "2", "x", "test"));
		assertU(adoc("id", "3", "references", "5", "references", "6", "references", "99", "x", "test"));
		assertU(adoc("id", "4", "references", "2", "references", "1"));
		assertU(adoc("id", "5"));
		assertU(adoc("id", "6"));
		assertU(adoc("id", "7", "references", "5"));
		assertU(commit());
		// the same thing, but using bibcode-like data
		
		assertU(adoc("id", "11", "bibcode", "b1", "breferences", "b2", "breferences", "b3", "breferences", "b4", "b", "test"));
		assertU(adoc("id", "12", "bibcode", "b2", "b", "test"));
		assertU(adoc("id", "13", "bibcode", "b3", "breferences", "b5", "breferences", "b6", "breferences", "b99", "b", "test"));
		assertU(adoc("id", "14", "bibcode", "b4", "breferences", "b2", "breferences", "b1"));
		assertU(adoc("id", "15", "bibcode", "b5"));
		assertU(adoc("id", "16", "bibcode", "b6"));
		assertU(adoc("id", "17", "bibcode", "b7", "breferences", "b5"));
		assertU(commit());
		
		
		BatchHandlerRequestQueue queue = new BatchHandlerRequestQueue();
		String tmpDir = System.getProperty("java.io.tmpdir");
		BatchProviderI provider = new BatchProviderDumpCitationCache();
		
		SolrQueryRequest req = req("jobid", "00000", "#workdir", tmpDir, "unique_field", "bibcode", "ref_field", "breferences");
		provider.run(req, queue);
		req.close();
		
		checkFile(tmpDir + "/00000", 
				"b1\tb4",
				"b2\tb1\tb4",
				"b3\tb1",
				"b4\tb1",
				"b5\tb3\tb7",
				"b6\tb3"
		);
		
		req = req("jobid", "00000", "#workdir", tmpDir, "unique_field", "bibcode", "ref_field", "breferences", 
				"return_docids", "true");
		provider.run(req, queue);
		req.close();
		
		checkFile(tmpDir + "/00000", 
				"7\t10",
				"8\t7\t10",
				"9\t7",
				"10\t7",
				"11\t9\t13",
				"12\t9"
		);
		
	}
	
	
}
