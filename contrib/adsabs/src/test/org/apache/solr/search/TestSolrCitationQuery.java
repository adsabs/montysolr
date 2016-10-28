package org.apache.solr.search;

import org.junit.BeforeClass;

import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;


public class TestSolrCitationQuery extends MontySolrQueryTestCase {


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
		//FieldCache.DEFAULT.purgeAllCaches();
		super.tearDown();
	}

	public void testSearch() throws Exception {
		
		//assertU(delQ("*:*"));
		//assertU(commit()); // if i remove this, the test will sometimes fail (i don't understand...)
		assertU(adoc("id", "0", "bibcode", "A", 
				"reference", "B", "reference", "C", "reference", "D"
				));
		assertU(adoc("id", "1", "bibcode", "B", 
				"reference", "X",
				"citation", "A", "citation", "D"
				));
		assertU(adoc("id", "2", "bibcode", "C", 
				"reference", "E", "reference", "F",
				"citation", "A"
				));
		assertU(adoc("id", "3", "bibcode", "D", 
				"reference", "B",
				"citation", "A"
				));
		assertU(adoc("id", "4", "bibcode", "E",
				"citation", "C"
				));
		assertU(adoc("id", "5", "bibcode", "F",
				"citation", "C"
				));
		assertU(commit("waitSearcher", "true")); // very weird, it is not waiting
		
		
		
		assertQ(req("q", "*:*"),
				"//*[@numFound='6']"
		);

		assertQ(req("q", "bibcode:A"),
				"//*[@numFound='1']",
				"//result/doc[1]/str[@name='bibcode']='A'"
		);

		assertQ(req("q", "citations(bibcode:A)"),
				"//*[@numFound='0']"
		);

		assertQ(req("q", "citations(bibcode:B)"),
				"//*[@numFound='2']",
				"//result/doc/str[@name='bibcode']='A'",
				"//result/doc/str[@name='bibcode']='D'"
		);

		assertQ(req("q", "joincitations(bibcode:B)"),
				"//*[@numFound='2']",
				"//result/doc/str[@name='bibcode']='A'",
				"//result/doc/str[@name='bibcode']='D'"
		);

		assertQ(req("q", "references(bibcode:A)"),
				"//*[@numFound='3']",
				"//result/doc/str[@name='bibcode']='B'",
				"//result/doc/str[@name='bibcode']='C'",
				"//result/doc/str[@name='bibcode']='D'"
		);

		assertQ(req("q", "joinreferences(bibcode:A)"),
				"//*[@numFound='3']",
				"//result/doc/str[@name='bibcode']='B'",
				"//result/doc/str[@name='bibcode']='C'",
				"//result/doc/str[@name='bibcode']='D'"
		);
		

	}

	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(TestSolrCitationQuery.class);
	}

}
