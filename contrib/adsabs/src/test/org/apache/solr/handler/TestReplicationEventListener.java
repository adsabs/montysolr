package org.apache.solr.handler;

import monty.solr.util.MontySolrSetup;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.handler.batch.BatchHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.QueryResponseWriter;
import org.apache.solr.response.SolrQueryResponse;
import org.junit.BeforeClass;

public class TestReplicationEventListener extends SolrTestCaseJ4 {
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		
		System.setProperty("solr.allow.unsafe.resourceloading", "true");
		schemaString = MontySolrSetup.getMontySolrHome()
		+ "/contrib/adsabs/src/test-files/solr/collection1/conf/schema-minimal.xml";
		
		configString = MontySolrSetup.getMontySolrHome()
		+ "/contrib/adsabs/src/test-files/solr/collection1/conf/solrconfig-replication.xml";
		
		initCore(configString, schemaString, MontySolrSetup.getSolrHome() + "/example/solr");
	}
	
	
	public String getSolrHome() {
		return MontySolrSetup.getMontySolrHome();
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();

		assertU(adoc("id", "0", "bibcode", "b0")); 
		assertU(adoc("id", "1", "bibcode", "b1"));
		assertU(adoc("id", "2", "bibcode", "b2"));
		assertU(adoc("id", "3", "bibcode", "b3"));
		assertU(adoc("id", "4", "bibcode", "b4"));
		assertU(commit());
		
		assertU(adoc("id", "5", "bibcode", "b5"));
		assertU(adoc("id", "6", "bibcode", "b6"));		
		assertU(commit());
	}
	
	
	
	
	
	public void test() throws Exception {
		
		assertQ(req("q", "bibcode:b3"), 
        "//*[@numFound='1']",
        "//doc/str[@name='id'][.='3']"
    );
		
		ReplicationCoordinatorHandler handler = new ReplicationCoordinatorHandler();
    NamedList<Object> defaults = new NamedList<Object>();
    handler.init(defaults);
    
    SolrQueryRequest req = req("event", "give-me-delay", "hostid", "foo");
    float[] answers = new float[10];
    for (int i=0; i < answers.length; i++) {
      SolrQueryResponse rsp = getResponse(handler, req);
      answers[i] = (float) rsp.getValues().get("delay");
    }
    // run two full cycles
		assertArrayEquals(new float[] {900.0f, 0.0f, 0.0f, 300.0f, 600.0f, 900.0f, 0.0f, 0.0f, 300.0f, 600.0f}, answers, 0.1f);
	}
	
	private SolrQueryResponse getResponse(ReplicationCoordinatorHandler handler, SolrQueryRequest req) throws IOException, InterruptedException {
    SolrQueryResponse rsp = new SolrQueryResponse();
    try {
      h.getCore().execute(handler, req, rsp);
      return rsp;
    }
    finally {
      req.close();
    }
  }
}
