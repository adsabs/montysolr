package org.apache.solr.search;

import org.junit.BeforeClass;

import invenio.montysolr.util.MontySolrAbstractTestCase;
import invenio.montysolr.util.MontySolrSetup;

public class TestSolrCitationQuery extends MontySolrAbstractTestCase {
	
	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		envInit();
		MontySolrSetup.addBuildProperties("contrib/invenio");
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() + "/contrib/invenio/src/python");
		MontySolrSetup.addTargetsToHandler("monty_invenio.targets");
	}
	
	public String getSchemaFile() {
		return MontySolrSetup.getMontySolrHome() + 
		"/contrib/invenio/src/test-files/solr/conf/schema-minimal.xml";
	}

	
	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome() + 
		"/contrib/invenio/src/test-files/solr/conf/solrconfig-invenio-query-parser.xml";
	}
	
	
	public void testSearch() throws Exception {
		
		assertU(adoc("id", "A", "references", "B", "references", "C", "references", "D"));
		assertU(adoc("id", "B"));
		assertU(adoc("id", "C", "references", "E", "references", "F"));
		assertU(adoc("id", "D", "references", "B"));
		assertU(adoc("id", "E"));
		assertU(adoc("id", "F"));
		assertU(commit("waitFlush", "true"));
		
		assertQ(req("q", "*:*"),
				"//*[@numFound='6']"
				);
		
		assertQ(req("q", "id:A"),
				"//*[@numFound='1']",
				"//result/doc[1]/str[@name='id']='A'"
				);
		
	}
	

}
