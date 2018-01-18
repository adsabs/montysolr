package org.apache.solr.handler;

import monty.solr.util.MontySolrSetup;

import org.apache.solr.util.AbstractSolrTestCase;
import org.junit.BeforeClass;

public class TestReplicationEventListener extends AbstractSolrTestCase {
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		
		System.setProperty("solr.allow.unsafe.resourceloading", "true");
		schemaString = MontySolrSetup.getMontySolrHome()
		+ "/contrib/adsabs/src/test-files/solr/collection1/conf/schema-minimal.xml";
		
		configString = MontySolrSetup.getMontySolrHome()
		+ "/contrib/adsabs/src/test-files/solr/collection1/conf/solrconfig-replication.xml";
		
		initCore(configString, schemaString, MontySolrSetup.getSolrHome() + "/example/solr");
	}
	
	
	@Override
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
				
	}
}
