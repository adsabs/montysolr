package org.apache.solr.handler.batch;

import org.apache.solr.request.SolrQueryRequest;

public interface BatchProviderI {
	
	/*
	 * TODO: make the class always return the container
	 * that already has description we can display in 
	 * the info (web help)
	 */
	public class BatchProviderContainer {
		public BatchProviderI provider;
		public String description; 
	}
	
	
	public void run(SolrQueryRequest locReq, BatchHandlerRequestQueue queue) throws Exception;
	public void setName(String name);
	public String getName();
	
	public String getDescription();
}
