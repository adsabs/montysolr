package org.apache.solr.response.transform;

import monty.solr.util.MontySolrSetup;

import org.apache.solr.util.AbstractSolrTestCase;

public class TestCitationsTransformer extends AbstractSolrTestCase {
	public String getSchemaFile() {
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/adsabs/src/test-files/solr/collection1/conf/schema-citations-transformer.xml";
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
		+ "/contrib/adsabs/src/test-files/solr/collection1/conf/solrconfig-citations-transformer.xml";
	}
	
	@Override
	public String getSolrHome() {
		return MontySolrSetup.getMontySolrHome();
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();

		assertU(adoc("id", "0", "bibcode", "b0", 
				"reference", "x2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "1", "bibcode", "b1", 
				"reference", "b2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "2", "bibcode", "b2", "alternate_bibcode", "x2", "alternate_bibcode", "x22",
				"reference", "b2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "3", "bibcode", "b3", 
				"reference", "b2", "reference", "b3", "reference", "b4", "reference", "foo"));
		assertU(adoc("id", "4", "bibcode", "b4", 
				"reference", "b2", "reference", "b3", "reference", "b4"));
		
		
		assertU(adoc("id", "5", "bibcode", "b5", "alternate_bibcode", "x5",
				"reference", "x22", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "6", "bibcode", "b6", 
				"reference", "b2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "7", "bibcode", "b7", 
				"reference", "b2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "8", "bibcode", "b8", 
				"reference", "x2", "reference", "x22", "reference", "b4"));


		assertU(adoc("id", "9", "bibcode", "b9",
				"reference", "b2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "10", "bibcode", "b10",
				"reference", "b2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "11", "bibcode", "b11"));		
		assertU(commit());
	}
	
	
	
	
	
	public void test() throws Exception {
		
		assertQ(req("q", "bibcode:b3"), 
        "//*[@numFound='1']",
        "//doc/int[@name='id'][.='3']"
    );
		
		assertQ(req("q", "bibcode:b3", 
				"fl", "id,[citations]",
				"indent", "true"), 
        "//*[@numFound='1']",
        "//doc/int[@name='id'][.='3']",
        "//doc/lst[@name='[citations]']/int[@name='num_citations'][.='10']",
        "//doc/lst[@name='[citations]']/int[@name='num_references'][.='3']"
    );
		
		assertQ(req("q", "bibcode:b3", 
				"fl", "id,[citations values=citations,references]",
				"indent", "true"), 
        "//*[@numFound='1']",
        "//doc/int[@name='id'][.='3']",
        "//doc/lst[@name='[citations]']/int[@name='num_citations'][.='10']",
        "//doc/lst[@name='[citations]']/int[@name='num_references'][.='3']",
        "//doc/lst[@name='[citations]']/arr[@name='references']/str[1][.='2']",
        "//doc/lst[@name='[citations]']/arr[@name='references']/str[2][.='3']",
        "//doc/lst[@name='[citations]']/arr[@name='references']/str[3][.='4']",
        "//doc/lst[@name='[citations]']/arr[@name='citations']/str[1][.='0']",
        "//doc/lst[@name='[citations]']/arr[@name='citations']/str[10][.='10']"
    );
		
		assertQ(req("q", "bibcode:b3", 
				"fl", "id,[citations values=citations,references resolve=true]",
				"indent", "true"), 
        "//*[@numFound='1']",
        "//doc/int[@name='id'][.='3']",
        "//doc/lst[@name='[citations]']/int[@name='num_citations'][.='10']",
        "//doc/lst[@name='[citations]']/int[@name='num_references'][.='3']",
        "//doc/lst[@name='[citations]']/arr[@name='references']/str[1][.='b2']",
        "//doc/lst[@name='[citations]']/arr[@name='references']/str[2][.='b3']",
        "//doc/lst[@name='[citations]']/arr[@name='references']/str[3][.='b4']",
        "//doc/lst[@name='[citations]']/arr[@name='citations']/str[1][.='b0']",
        "//doc/lst[@name='[citations]']/arr[@name='citations']/str[10][.='b10']"
    );
		
		assertQ(req("q", "bibcode:b11", 
				"fl", "id,[citations values=citations,references resolve=true]",
				"indent", "true"), 
        "//*[@numFound='1']",
        "//doc/int[@name='id'][.='11']",
        "//doc/lst[@name='[citations]']/int[@name='num_citations'][.='0']",
        "//doc/lst[@name='[citations]']/int[@name='num_references'][.='0']"
        );
		
	}
}
