package org.apache.lucene.queryparser.flexible.aqp.config;

import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.AttributeReflector;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public class AqpRequestParamsImpl extends AttributeImpl
	implements AqpRequestParams {
	
	private static final long serialVersionUID = -1243402092650568132L;
	
	private SolrQueryRequest req;
	private SolrQueryResponse rsp;
	private SolrParams localParams;
	private SolrParams params;
	private String query;
	
	public SolrQueryRequest getRequest() {
		return req;
	}

	public void setRequest(SolrQueryRequest req) {
		this.req = req;
	}

	public SolrQueryResponse getResponse() {
		return rsp;
	}

	public void setResponse(SolrQueryResponse rsp) {
		this.rsp = rsp;
	}

	@Override
	public void clear() {
		req = null;
		rsp = null;
		params = null;
		localParams = null;
	}

	@Override
	public void copyTo(AttributeImpl target) {
		AqpRequestParams t = (AqpRequestParams) target;
		t.setLocalParams(localParams);
		t.setParams(params);
		t.setQueryString(query);
		t.setRequest(req);
		t.setResponse(rsp);
	}

	public void setLocalParams(SolrParams localParams) {
		this.localParams = localParams;
	}

	public void setParams(SolrParams params) {
		this.params = params;
	}

	public SolrParams getLocalParams() {
		return localParams;
	}

	public SolrParams getParams() {
		return params;
	}

	public String getQueryString() {
		return query;
	}

	public void setQueryString(String query) {
		this.query = query;
	}

  @Override
  public void reflectWith(AttributeReflector reflector) {
    reflector.reflect(AqpRequestParams.class, "req", req);
    reflector.reflect(AqpRequestParams.class, "rsp", rsp);
    reflector.reflect(AqpRequestParams.class, "localParams", localParams);
    reflector.reflect(AqpRequestParams.class, "params", params);
    reflector.reflect(AqpRequestParams.class, "query", query);
    
  }
	
}
