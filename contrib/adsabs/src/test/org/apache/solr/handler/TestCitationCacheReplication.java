package org.apache.solr.handler;

import monty.solr.util.MontySolrSetup;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.solr.common.util.NamedList;
import org.apache.solr.handler.batch.BatchHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.QueryResponseWriter;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.util.AbstractSolrTestCase;
import org.junit.BeforeClass;

public class TestCitationCacheReplication extends AbstractSolrTestCase {
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		
		System.setProperty("solr.allow.unsafe.resourceloading", "true");
		schemaString = MontySolrSetup.getMontySolrHome()
				+ "/contrib/adsabs/src/test-files/solr/collection1/conf/schema-batch-provider.xml";
		
		configString = MontySolrSetup.getMontySolrHome()
		+ "/contrib/adsabs/src/test-files/solr/collection1/conf/solrconfig-dump-citation-cache.xml";
		
		initCore(configString, schemaString, MontySolrSetup.getSolrHome() + "/example/solr");
	}
	
	
	@Override
	public String getSolrHome() {
		return MontySolrSetup.getMontySolrHome();
	}
	
	
	public void test() throws Exception {
		
		assertU(adoc("id", "11", "bibcode", "b1", "reference", "b2", "reference", "b3", "reference", "b4", "b", "test"));
		assertU(adoc("id", "12", "bibcode", "b2", "b", "test"));
		assertU(adoc("id", "13", "bibcode", "b3", "reference", "b5", "reference", "b6", "reference", "b99", "b", "test"));
		assertU(adoc("id", "14", "bibcode", "b4", "reference", "b2", "reference", "b1"));
		assertU(adoc("id", "15", "bibcode", "b5"));
		assertU(adoc("id", "16", "bibcode", "b6"));
		assertU(adoc("id", "17", "bibcode", "b7", "reference", "b5"));
		assertU(commit("waitSearcher", "true"));
		
		assertQ(req("q", "bibcode:b3"), 
        "//*[@numFound='1']",
        "//doc/int[@name='id'][.='13']"
	    );
		
		System.out.println(h.query(req("qt", "/replication", "command", "indexversion", "wt", "json")));
		System.out.println(h.query(req("qt", "/replication", "command", "filelist", "generation", "2", "wt", "json", "indent", "true")));
		System.out.println(h.query(req("qt", "/replication", "command", "filecontent", "file", "citation_cache", "wt", "json", "indent", "true")));
			
	  }
}
