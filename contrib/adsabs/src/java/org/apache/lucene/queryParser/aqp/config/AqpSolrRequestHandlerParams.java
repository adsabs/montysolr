package org.apache.lucene.queryParser.aqp.config;

import org.apache.lucene.util.Attribute;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public interface AqpSolrRequestHandlerParams extends Attribute {
	
	public SolrQueryRequest getRequest();
	public void setRequest(SolrQueryRequest req);
	
	public SolrQueryResponse getResponse();
	public void setResponse(SolrQueryResponse  rsp);
	
	public void setLocalParams(SolrParams localParams);
	public void setParams(SolrParams params);

	public SolrParams getLocalParams();
	public SolrParams getParams();
}
