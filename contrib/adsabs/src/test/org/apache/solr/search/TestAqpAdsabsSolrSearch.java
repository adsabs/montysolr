package org.apache.solr.search;

import invenio.montysolr.util.MontySolrAbstractTestCase;
import invenio.montysolr.util.MontySolrSetup;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.aqp.AqpTestAbstractCase;
import org.apache.lucene.queryParser.aqp.TestAqpAdsabs;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;


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
 * for now I resigned, and I think of contrib/examples
 * as a dependency for adsabs
 * 
 * contrib/examples should contain only a code for the
 * live site (setup), but we are developing components
 * for it here and we'll test it here 
 * (inside contrib/adsabs)
 * 
 * Let's do it pragmatically (not as code puritans)
 *
 */
public class TestAqpAdsabsSolrSearch extends MontySolrAbstractTestCase {

	public String getSchemaFile() {
		makeResourcesVisible(this.solrConfig.getResourceLoader(),
	    		new String[] {MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/solr/conf",
	    				      MontySolrSetup.getSolrHome() + "/example/solr/conf"
	    	});
		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/examples/adsabs/solr/conf/schema.xml";
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome()
				+ "/contrib/examples/adsabs/solr/conf/solrconfig.xml";
	}
	
	private AqpTestAbstractCase tp = null;
	
	public void setUp() throws Exception {
		super.setUp();
		
		final TestAqpAdsabsSolrSearch that = this;
		
		tp = new AqpTestAbstractCase() {
			
			@Override
			public void setUp() throws Exception {
				super.setUp();
			}
			
			
		};
		tp.setUp();
	}
	
	public void tearDown() throws Exception {
		tp.tearDown();
		super.tearDown();
	}
	
	
	public void test() throws Exception {
		
		//tp.setDebug(true);
		
		assertQueryEquals(req("qt", "aqp", "q", "edismax(dog OR cat)"), 
				"+((all:dog) (all:cat))", BooleanQuery.class);
		assertQueryEquals(req("qt", "aqp", "q", "edismax(dog AND cat)"), 
				"+(+(all:dog) +(all:cat))", BooleanQuery.class);
		assertQueryEquals(req("qt", "aqp", "q", "edismax(frank bank)"), 
				"+(((all:frank) (all:bank))~2)", BooleanQuery.class);
		
		assertQueryEquals(req("qt", "aqp", "q", "edismax(dog OR cat) OR bat"), 
				"(+((all:dog) (all:cat))) all:bat", BooleanQuery.class);
		assertQueryEquals(req("qt", "aqp", "q", "edismax(dog AND cat) AND bat"), 
				"+(+(+(all:dog) +(all:cat))) +all:bat", BooleanQuery.class);
		assertQueryEquals(req("qt", "aqp", "q", "edismax(frank bank) bat"), 
				"(+(((all:frank) (all:bank))~2)) all:bat", BooleanQuery.class);
		
		//  {!raw f=myfield}Foo Bar creates TermQuery(Term("myfield","Foo Bar"))
		assertQueryEquals(req("qt", "aqp", "f", "myfield", "q", "raw({!f=myfield}Foo Bar)"), "myfield:Foo Bar", TermQuery.class);
		assertQueryEquals(req("qt", "aqp", "f", "myfield", "q", "raw({!f=x}\"Foo Bar\")"), "x:\"Foo Bar\"", TermQuery.class);
		
		assertQueryParseException(req("qt", "aqp", "f", "myfield", "q", "raw(Foo Bar)"));
	}
	
	
	public QParser getParser(SolrQueryRequest req) throws ParseException, InstantiationException, IllegalAccessException {
		SolrParams params = req.getParams();
		String query = params.get(CommonParams.Q);
		String qt = params.get(CommonParams.QT);
		QParser qParser = QParser.getParser(query, qt, req);
		
		if (qParser instanceof AqpAdsabsQParser) {
			((AqpAdsabsQParser) qParser).getParser().setDebug(tp.debugParser);
		}
		return qParser;
		
	}
	
	public Query assertQueryEquals(SolrQueryRequest req, String result, Class<?> clazz)
		throws Exception {
		
		QParser qParser = getParser(req);
		
		Query q = qParser.parse();
		
		String s = q.toString("field");
		if (!s.equals(result)) {
			tp.debugFail(q.toString(), result, s);
		}
		
		if (clazz != null) {
			if (!q.getClass().isAssignableFrom(clazz)) {
				tp.debugFail(q.toString(), result, "Query is not: " + clazz + " but: " + q.getClass());
			}
		}
		
		return q;
	}
	
	public void assertQueryParseException(SolrQueryRequest req) throws Exception {
		try {
			getParser(req).parse();
		} catch (ParseException expected) {
			return;
		}
		tp.debugFail("ParseException expected, not thrown");
	} 
	
}
