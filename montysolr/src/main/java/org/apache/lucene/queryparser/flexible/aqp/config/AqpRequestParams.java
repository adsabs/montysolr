package org.apache.lucene.queryparser.flexible.aqp.config;

import org.apache.lucene.util.Attribute;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public interface AqpRequestParams extends Attribute {

    SolrQueryRequest getRequest();

    void setRequest(SolrQueryRequest req);

    SolrQueryResponse getResponse();

    void setResponse(SolrQueryResponse rsp);

    void setLocalParams(SolrParams localParams);

    void setParams(SolrParams params);

    SolrParams getLocalParams();

    SolrParams getParams();

    String getQueryString();

    void setQueryString(String query);
}
