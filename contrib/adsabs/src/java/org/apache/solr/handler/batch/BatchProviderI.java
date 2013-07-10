package org.apache.solr.handler.batch;

import org.apache.solr.request.SolrQueryRequest;

public interface BatchProviderI {
	public void run(SolrQueryRequest locReq, BatchHandlerRequestQueue queue) throws Exception;
	public void setName(String name);
	public String getName();
	public String getDescription();
}
