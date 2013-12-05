package org.apache.solr.search;

import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;

import org.junit.BeforeClass;


public class TestSolrCitationQuery extends MontySolrAbstractTestCase {

	@BeforeClass
	public static void beforeTestSolrCitationQuery() throws Exception {
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome() 
				+ "/contrib/adsabs/src/python");
		MontySolrSetup.addTargetsToHandler("adsabs.targets");
	}

	public String getSchemaFile() {
		makeResourcesVisible(this.solrConfig.getResourceLoader(),
				new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/collection1/conf",
			MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf"
		});
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/examples/adsabs/solr/collection1/conf/schema.xml";
	}


	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/examples/adsabs/solr/collection1/conf/solrconfig.xml";
	}


	public void testSearch() throws Exception {

		assertU(adoc("id", "0", "bibcode", "A", "reference", "B", "reference", "C", "reference", "D"));
		assertU(adoc("id", "1", "bibcode", "B", "reference", "X"));
		assertU(adoc("id", "2", "bibcode", "C", "reference", "E", "reference", "F"));
		assertU(adoc("id", "3", "bibcode", "D", "reference", "B"));
		assertU(adoc("id", "4", "bibcode", "E"));
		assertU(adoc("id", "5", "bibcode", "F"));
		assertU(commit());

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
