package examples.adsabs;

import org.junit.BeforeClass;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;


public class TestPivotQuery extends MontySolrQueryTestCase {


	@BeforeClass
	public static void beforeClass() throws Exception {
		
		makeResourcesVisible(Thread.currentThread().getContextClassLoader(), new String[] {
			    MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/server/solr/collection1/",
		      MontySolrSetup.getSolrHome() + "/example/solr/collection1"
		    });
				
		System.setProperty("solr.allow.unsafe.resourceloading", "true");
		schemaString = MontySolrSetup.getMontySolrHome()
					+ "/contrib/examples/adsabs/server/solr/collection1/schema.xml";
		
		configString = MontySolrSetup.getMontySolrHome()
					+ "/contrib/examples/adsabs/server/solr/collection1/solrconfig.xml";
		
		initCore(configString, schemaString, MontySolrSetup.getSolrHome() + "/example/solr");
	}
	

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSearch() throws Exception {
		
		//assertU(delQ("*:*"));
		//assertU(commit()); // if i remove this, the test will sometimes fail (i don't understand...)
		assertU(adoc("id", "0", "bibcode", "0", "year", "2011", 
				"property", "refereed"
				));
		assertU(adoc("id", "1", "bibcode", "1", "year", "2011", 
				"property", "Not Refereed"
				));
		assertU(adoc("id", "2", "bibcode", "2", "year", "2011", 
        "property", "Not Refereed"
        ));
		assertU(adoc("id", "3", "bibcode", "3", "year", "2012", 
        "property", "Not Refereed"
        ));
		assertU(adoc("id", "4", "bibcode", "4", "year", "2012", 
        "property", "Refereed"
        ));
		assertU(adoc("id", "5", "bibcode", "5", "year", "2012", 
        "property", "Not Refereed"
        ));
		assertU(commit("waitSearcher", "true"));
		
		
		
		assertQ(req("q", "*:*"),
				"//*[@numFound='6']"
		);

		assertQ(req("q", "*:*", "facet", "true", "facet.pivot", "property,year", "fl", "bibcode", "indent", "true"),
		    
				"/response/lst[2]/lst[6]/arr[@name=\"property,year\"]/lst[1]//str[2]/text()='notrefereed'",
				"/response/lst[2]/lst[6]/arr[@name=\"property,year\"]/lst[1]/int[@name=\"count\"]='4'",
				
				"/response/lst[2]/lst[6]/arr[@name=\"property,year\"]/lst[1]/arr[@name=\"pivot\"]/lst[1]//str[1]/text()='year'",
				"/response/lst[2]/lst[6]/arr[@name=\"property,year\"]/lst[1]/arr[@name=\"pivot\"]/lst[1]//str[2]/text()='2011'",
				"/response/lst[2]/lst[6]/arr[@name=\"property,year\"]/lst[1]/arr[@name=\"pivot\"]/lst[1]/int[@name=\"count\"]=2",
				
				"/response/lst[2]/lst[6]/arr[@name=\"property,year\"]/lst[1]/arr[@name=\"pivot\"]/lst[2]//str[1]/text()='year'",
        "/response/lst[2]/lst[6]/arr[@name=\"property,year\"]/lst[1]/arr[@name=\"pivot\"]/lst[2]//str[2]/text()='2012'",
        "/response/lst[2]/lst[6]/arr[@name=\"property,year\"]/lst[1]/arr[@name=\"pivot\"]/lst[2]/int[@name=\"count\"]=2",
        
        "/response/lst[2]/lst[6]/arr[@name=\"property,year\"]/lst[2]//str[2]/text()='refereed'",
        "/response/lst[2]/lst[6]/arr[@name=\"property,year\"]/lst[2]/int[@name=\"count\"]='2'",
        
        "/response/lst[2]/lst[6]/arr[@name=\"property,year\"]/lst[2]/arr[@name=\"pivot\"]/lst[1]//str[2]/text()='2011'",
        "/response/lst[2]/lst[6]/arr[@name=\"property,year\"]/lst[2]/arr[@name=\"pivot\"]/lst[2]/int[@name=\"count\"]=1"
		);


	}

	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(TestPivotQuery.class);
	}

}
