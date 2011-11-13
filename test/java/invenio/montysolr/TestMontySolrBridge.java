package invenio.montysolr;

import org.apache.solr.util.MontySolrAbstractTestCase;

public class TestMontySolrBridge extends MontySolrAbstractTestCase {
	
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	public String getSchemaFile() {
		return "schema-authorpos.xml";
	}

	@Override
	public String getSolrConfigFile() {
		return "solrconfig.xml";
	}
	
	public void testBridge() {
		System.out.println(1);
	}

}
