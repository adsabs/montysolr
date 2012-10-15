package monty.solr.util;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.aqp.AqpTestAbstractCase;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.AqpAdsabsQParser;
import org.apache.solr.search.QParser;


public class MontySolrQueryTestCase extends MontySolrAbstractTestCase {

	private AqpTestAbstractCase tp = null;
	
	@Override
	public String getSchemaFile() {
		throw new IllegalAccessError("You must override this method");
	}

	@Override
	public String getSolrConfigFile() {
		throw new IllegalAccessError("You must override this method");
	} 
	
	public void setUp() throws Exception {
		super.setUp();
		
		final MontySolrQueryTestCase that = this;
		
		tp = new AqpTestAbstractCase() {
			
			@Override
			public void setUp() throws Exception {
				super.setUp();
			}
			
			@Override
			public void tearDown() throws Exception {
				super.tearDown();
			}
			
			
		};
		tp.setUp();
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
		
		tp.tearDown();
		tp = null;
		
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
	
	public Query assertQueryEquals(SolrQueryRequest req, String expected, Class<?> clazz)
		throws Exception {
		
		QParser qParser = getParser(req);
		String query = req.getParams().get(CommonParams.Q);
		Query q = qParser.parse();
		
		String actual = q.toString("field");
		if (!actual.equals(expected)) {
			tp.debugFail(query, expected, actual);
		}
		
		if (clazz != null) {
			if (!q.getClass().isAssignableFrom(clazz)) {
				tp.debugFail("Query is not: " + clazz + " but: " + q.getClass(), expected, "-->" + q.toString());
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
	
	public void setDebug(boolean v) {
		tp.setDebug(v);
	}

}
