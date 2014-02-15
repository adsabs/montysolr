package org.apache.solr.handler.batch;

import org.adsabs.solr.AdsConfig.F;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.SortableFloatField;

public class TestBatchProviderDumpIndexFields extends BatchProviderTest {

	public void test() throws Exception {

		// now index some data
		assertU(adoc(F.ID, "1", F.BIBCODE, "xxxxxxxxxxxx1", "title", "green wall", "intf", "1", "floatf", "1.0"));
		assertU(adoc(F.ID, "2", F.BIBCODE, "xxxxxxxxxxxx2", "title", "of trees", "intf", "1", "floatf", "1.0"));
		assertU(adoc(F.ID, "3", F.BIBCODE, "xxxxxxxxxxxx3", "title", "blues sky", "intf", "1", "floatf", "1.0"));
		assertU(adoc(F.ID, "4", F.BIBCODE, "xxxxxxxxxxxx4", "title", "of seas", "intf", "1", "floatf", "1.0"));
		assertU(adoc(F.ID, "5", F.BIBCODE, "xxxxxxxxxxxx5", "title", "no fight", "intf", "1", "floatf", "1.0"));
		assertU(adoc(F.ID, "6", F.BIBCODE, "xxxxxxxxxxxx6", "title", "for peace", "intf", "1", "floatf", "1.0"));
		assertU(commit());
		// this creates another segment
		assertU(adoc(F.ID, "7", F.BIBCODE, "xxxxxxxxxxxx7", "title", "no fight", "intf", "1", "floatf", "1.0"));
		assertU(adoc(F.ID, "8", F.BIBCODE, "xxxxxxxxxxxx8", "title", "for peace", "intf", "1", "floatf", "1.0"));
		assertU(commit());
		
		
		BatchHandlerRequestQueue queue = new BatchHandlerRequestQueue();
		String tmpDir = System.getProperty("java.io.tmpdir");
		SolrQueryRequest req = req("jobid", "00000", "#workdir", tmpDir, "q", "title:wall OR title:peace", 
				"fields", "id,title,floatf");
		
		BatchProviderDumpIndexFields provider = new BatchProviderDumpIndexFields();
		provider.run(req, queue);
		req.close();
		
		checkFile(tmpDir + "/00000", 
				"{\"id\":[\"1\"],\"floatf\":[\"1.0\"],\"title\":[\"green\",\"wall\"]},",
				"{\"id\":[\"6\"],\"floatf\":[\"1.0\"],\"title\":[\"for\",\"peace\"]},",
				"{\"id\":[\"8\"],\"floatf\":[\"1.0\"],\"title\":[\"for\",\"peace\"]}]"
		);
		
		req = req("jobid", "00000", "#workdir", tmpDir, "q", "title:wall OR title:peace", 
				"fields", "id,title", "analyze", "false");
		
		//SortableFloatField f = new SortableFloatField();
		//String v = f.readableToIndexed("1.0");
		
		provider = new BatchProviderDumpIndexFields();
		provider.run(req, queue);
		req.close();
		
		checkFile(tmpDir + "/00000", 
				"{\"id\":[\"1\"],\"title\":[\"green wall\"]},",
				"{\"id\":[\"6\"],\"title\":[\"for peace\"]},",
				"{\"id\":[\"8\"],\"title\":[\"for peace\"]}]"
		);
	}

	
	
}
