package org.apache.solr.handler.batch;

import java.io.File;
import java.io.PrintWriter;

import org.apache.solr.request.SolrQueryRequest;

public class TestBatchProviderDumpBibcodes extends BatchProviderTest {


	public void test() throws Exception {

		createIndex();
		
		BatchHandlerRequestQueue queue = new BatchHandlerRequestQueue();
		String tmpDir = System.getProperty("java.io.tmpdir");
		String jobid = "00009";
		
		File jobFile = new File(tmpDir + "/" + jobid + ".input");
		PrintWriter fo = new PrintWriter(jobFile);
		fo.println("xxxxxxxxxxxx1");
		fo.println("xxxxxxxxxxxx5");
		fo.close();
		
		SolrQueryRequest req = req("jobid", jobid, "#workdir", tmpDir, 
				"fields", "bibcode,title", "analyze", "false");
		
		BatchProviderDumpBibcodes provider = new BatchProviderDumpBibcodes();
		provider.run(req, queue);
		req.close();
		
		checkFile(tmpDir + "/" + jobid, 
				"{\"bibcode\":[\"xxxxxxxxxxxx1\"],\"title\":[\"green wall\"]},",
				"{\"bibcode\":[\"xxxxxxxxxxxx5\"],\"title\":[\"no fight\"]}]",
				"!{\"bibcode\":\"xxxxxxxxxxxx6\",\"title\":[\"for peace\"]},",
				"!{\"bibcode\":\"xxxxxxxxxxxx7\",\"title\":[\"no fight\"]},",
				"!{\"bibcode\":\"xxxxxxxxxxxx8\",\"title\":[\"for peace\"]},"
		);

		
	}
	
	
}
