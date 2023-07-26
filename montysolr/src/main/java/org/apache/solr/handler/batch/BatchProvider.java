package org.apache.solr.handler.batch;
import org.apache.lucene.search.Query;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.QueryParsing;
import org.apache.solr.search.SyntaxError;

public abstract class BatchProvider implements BatchProviderI {
	
  private Query query = null; 
	private String name;
	
	public BatchProvider() {
		this.name = "<not-set>";
	}
	
	public BatchProvider(String name) {
		this.name = name;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setQuery(Query query) {
	  this.query = query;
	}
	
	public Query getQuery(SolrQueryRequest req) throws SyntaxError {
	  if (query != null)
	    return query;
	  SolrParams params = req.getParams();
	  String defType = params.get(QueryParsing.DEFTYPE,QParserPlugin.DEFAULT_QTYPE);
	  String q = params.get(CommonParams.Q, null);
    if (q == null) {
      throw new SolrException(ErrorCode.BAD_REQUEST, "The 'q' parameter is missing, but we must know how to select docs");
    }
    QParser parser = QParser.getParser(q, defType, req);
    return parser.getQuery();
	}
}