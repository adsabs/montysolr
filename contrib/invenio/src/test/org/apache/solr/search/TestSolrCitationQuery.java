package org.apache.solr.search;

import montysolr.util.MontySolrAbstractTestCase;
import montysolr.util.MontySolrSetup;

import org.junit.BeforeClass;


public class TestSolrCitationQuery extends MontySolrAbstractTestCase {
	
	@BeforeClass
	public static void beforeTestSolrCitationQuery() throws Exception {
		MontySolrSetup.addBuildProperties("contrib/invenio");
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() + "/contrib/invenio/src/python");
		MontySolrSetup.addTargetsToHandler("monty_invenio.targets");
	}
	
	public String getSchemaFile() {
		return MontySolrSetup.getMontySolrHome() + 
		"/contrib/invenio/src/test-files/solr/collection1/conf/schema-citation-query.xml";
	}

	
	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome() + 
		"/contrib/invenio/src/test-files/solr/collection1/conf/solrconfig-invenio-query-parser.xml";
	}
	
	
	public void testSearch() throws Exception {
		
		assertU(adoc("id", "A", "references", "B", "references", "C", "references", "D"));
		assertU(adoc("id", "B"));
		assertU(adoc("id", "C", "references", "E", "references", "F"));
		assertU(adoc("id", "D", "references", "B"));
		assertU(adoc("id", "E"));
		assertU(adoc("id", "F"));
		assertU(commit());
		
		assertQ(req("q", "*:*"),
				"//*[@numFound='6']"
				);
		
		assertQ(req("q", "id:A"),
				"//*[@numFound='1']",
				"//result/doc[1]/str[@name='id']='A'"
				);
		
	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestSolrCitationQuery.class);
    }

}
