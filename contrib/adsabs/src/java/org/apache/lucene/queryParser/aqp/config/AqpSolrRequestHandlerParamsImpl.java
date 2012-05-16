package org.apache.lucene.queryParser.aqp.config;

import org.apache.lucene.util.AttributeImpl;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public class AqpSolrRequestHandlerParamsImpl extends AttributeImpl 
	implements AqpSolrRequestHandlerParams {
	
	private static final long serialVersionUID = -1243402092650568132L;
	
	private SolrQueryRequest req;
	private SolrQueryResponse rsp;
	private SolrParams localParams;
	private SolrParams params;
	
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
		throw new UnsupportedOperationException();
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
	
}
