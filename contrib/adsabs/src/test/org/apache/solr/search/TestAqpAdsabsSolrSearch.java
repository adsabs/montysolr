package org.apache.solr.search;

import invenio.montysolr.util.MontySolrAbstractTestCase;
import invenio.montysolr.util.MontySolrSetup;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.aqp.AqpQueryParser;
import org.apache.lucene.queryParser.aqp.AqpTestAbstractCase;
import org.apache.lucene.queryParser.aqp.TestAqpAdsabs;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.function.FunctionQuery;


/**
 * This unittest is for queries that require solr core
 * 
 * @see TestAqpAdsabs for the other tests
 * @author rchyla
 * 
 * XXX: I was uneasy about the family of these tests
 * because they depend on the settings from the other 
 * project (contrib/examples) - on the other hand, I 
 * don't want to duplicate the code/config files. So, 
 * for now I rezigned, and I think of contrib/examples
 * as a dependency for adsabs
 * 
 * contrib/examples should contain only a code for the
 * live site (setup), but we are developing components
 * for it here and we'll test it here 
 * (inside contrib/adsabs)
 * 
 * Let's call it pragmatic resignation (non-puritanism)
 *
 */
public class TestAqpAdsabsSolrSearch extends AqpTestAbstractCase {

	public String getSchemaFile() {
		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/examples/adsabs/solr/conf/schema.xml";
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/examples/adsabs/solr/conf/solrconfig.xml";
	}
	
	private MontySolrAbstractTestCase solr = null;
	
	public SolrCore core = null; 
	public void setUp() throws Exception {
		super.setUp();
		
		final TestAqpAdsabsSolrSearch that = this;
		
		solr = new MontySolrAbstractTestCase() {
			
			@Override
			public String getSolrConfigFile() {
				return that.getSolrConfigFile();
			}
			
			@Override
			public String getSchemaFile() {
				makeResourcesVisible(this.solrConfig.getResourceLoader(),
			    		new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/conf",
			    				      MontySolrSetup.getSolrHome() + "/example/solr/conf"
			    	});
				return that.getSchemaFile();
			}
			@Override
			public void setUp() throws Exception {
				super.setUp();
				that.core = h.getCore();
			}
		};
		solr.setUp();
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
		solr.tearDown();
	}
	
	public AqpQueryParser getParser() throws Exception {
		AqpQueryParser qp = new AqpQueryParser(getGrammarName());
		qp.setDebug(this.debugParser);
		return qp;
	}
	
	public void test() throws Exception {
		
		setDebug(true);
		assertQueryEquals(solr.req("qt", "aqp", "q", "edismax(this OR that)"), "", null);
		
	}
	
	public Query assertQueryEquals(SolrQueryRequest req, String result, Class<?> clazz)
		throws Exception {
		
		SolrParams params = req.getParams();
		String query = params.get(CommonParams.Q);
		String qt = params.get(CommonParams.QT);
		QParser qParser = QParser.getParser(query, qt, req);
		
		if (qParser instanceof AqpAdsabsQParser) {
			((AqpAdsabsQParser) qParser).getParser().setDebug(this.debugParser);
		}
		
		Query q = qParser.parse();
		
		String s = q.toString("field");
		if (!s.equals(result)) {
			debugFail(q.toString(), result, s);
		}
		
		if (clazz != null) {
			if (!q.getClass().isAssignableFrom(clazz)) {
				debugFail(q.toString(), result, "Query is not: " + clazz + " but: " + q.getClass());
			}
		}
		
		return q;
	}
}
