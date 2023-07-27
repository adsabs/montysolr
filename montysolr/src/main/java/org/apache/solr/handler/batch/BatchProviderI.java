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
    class BatchProviderContainer {
        public BatchProviderI provider;
        public String description;
    }


    void run(SolrQueryRequest locReq, BatchHandlerRequestQueue queue) throws Exception;

    void setName(String name);

    String getName();

    String getDescription();

    /**
     * Override the query to be used by the provider. If set, it will have precedence
     * over the query constructed from the query parameters.
     *
     * @param query query string
     */
    void setQuery(Query query);

    Query getQuery(SolrQueryRequest req) throws SyntaxError;

}
