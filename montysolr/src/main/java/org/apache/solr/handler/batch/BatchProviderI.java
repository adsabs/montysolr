package org.apache.solr.handler.batch;

import org.apache.lucene.search.Query;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.SyntaxError;

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
	
	/**
	 * Override the query to be used by the provider. If set, it will have precedence
	 * over the query constructed from the query parameters.
	 * 
	 * @param query
	 *     query string
	 */
	public void setQuery(Query query);
	public Query getQuery(SolrQueryRequest req) throws SyntaxError;
	
}
