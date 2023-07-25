package org.apache.solr.search;

import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.BeforeClass;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.MontySolrSetup;


public class TestSolrCitationQuery extends MontySolrQueryTestCase {


	@BeforeClass
	public static void beforeClass() throws Exception {
		
		makeResourcesVisible(Thread.currentThread().getContextClassLoader(), new String[] {
			    MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/server/solr/collection1/conf",
		      MontySolrSetup.getSolrHome() + "/example/solr/collection1"
		    });
				
		System.setProperty("solr.allow.unsafe.resourceloading", "true");
		schemaString = MontySolrSetup.getMontySolrHome()
					+ "/contrib/examples/adsabs/server/solr/collection1/conf/schema.xml";
		
		configString = MontySolrSetup.getMontySolrHome()
					+ "/contrib/examples/adsabs/server/solr/collection1/conf/solrconfig.xml";
		
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
		assertU(adoc("id", "0", "bibcode", "a", 
				"reference", "b", "reference", "c", "reference", "b"
				));
		assertU(adoc("id", "1", "bibcode", "b", 
				"reference", "X",
				"citation", "A", "citation", "D"
				));
		assertU(commit("waitSearcher", "true"));
		assertU(adoc("id", "2", "bibcode", "c", 
				"reference", "E", "reference", "F",
				"citation", "A"
				));
		assertU(adoc("id", "3", "bibcode", "d", 
				"reference", "B",
				"citation", "A"
				));
		assertU(commit("waitSearcher", "true"));
		assertU(adoc("id", "4", "bibcode", "e",
				"citation", "C"
				));
		assertU(adoc("id", "5", "bibcode", "f",
				"citation", "C"
				));
		assertU(commit("waitSearcher", "true"));
		
		
		
		assertQ(req("q", "*:*"),
				"//*[@numFound='6']"
		);

		assertQ(req("q", "bibcode:A", "fl", "bibcode"),
				"//*[@numFound='1']",
				"//result/doc[1]/str[@name='bibcode']='a'"
		);

		assertQ(req("q", "citations(bibcode:A)"),
				"//*[@numFound='0']"
		);

		assertQ(req("q", "citations(bibcode:b)", "fl", "bibcode"),
				"//*[@numFound='2']",
				"//result/doc/str[@name='bibcode']='a'",
				"//result/doc/str[@name='bibcode']='d'"
		);
		
//		ModifiableSolrParams p = params("sort","id asc");
//		assertJQ(req(p, "q","{!join from=bibcode to=reference}bibcode:b", "fl","id", "debugQuery", "true")
//        ,"/response=={'numFound':2,'start':0,'docs':[{'id':'0'},{'id':'1'}]}"
//    );
		
		assertQ(req("q", "joincitations(bibcode:B)", "fl", "bibcode"),
		    "//*[@numFound='2']",
		    "//result/doc/str[@name='bibcode']='a'",
		    "//result/doc/str[@name='bibcode']='d'"
		    );


		assertQ(req("q", "references(bibcode:A)", "fl", "bibcode"),
				"//*[@numFound='3']",
				"//result/doc/str[@name='bibcode']='b'",
				"//result/doc/str[@name='bibcode']='c'",
				"//result/doc/str[@name='bibcode']='d'"
		);
		assertQ(req("q", "joinreferences(bibcode:A)", "fl", "bibcode"),
		    "//*[@numFound='3']",
		    "//result/doc/str[@name='bibcode']='b'",
		    "//result/doc/str[@name='bibcode']='c'",
		    "//result/doc/str[@name='bibcode']='d'"
		    );

		

	}

	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(TestSolrCitationQuery.class);
	}

}
