package org.apache.solr.handler.component;

import org.apache.lucene.search.Query;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.QueryParsing;
import org.apache.solr.search.SyntaxError;

import java.io.IOException;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * When the request parameter {@code parseOnly=true} is set, this component
 * parses the query - running the full aqp expansion pipeline (synonym, virtual
 * field, author expansion, etc.) - and adds the fully expanded query to the
 * response under {@code parsedquery}. It is rendered with
 * {@link QueryParsing#toString(Query, org.apache.solr.schema.IndexSchema)}, the
 * exact serialization Solr uses for {@code debug.parsedquery}.
 * <p>
 * The actual search is then skipped: the stock {@code QueryComponent} (and the
 * {@code wordcloud}/{@code facet} components, which need a results object) are
 * turned off via request params, and debug is disabled on the ResponseBuilder
 * so {@code DebugComponent} does not dereference the (never populated) query.
 * <p>
 * This must be registered as a <em>first-component</em> so that it runs before
 * QueryComponent and DebugComponent in both the prepare and process phases.
 */
public class AqpExpandedQueryComponent extends SearchComponent {

    public static final String COMPONENT_NAME = "parseOnly";
    public static final String RESPONSE_KEY = "parsedquery";
    public static final String ERROR_KEY = "parsedqueryError";

    @Override
    public void prepare(ResponseBuilder rb) throws IOException {
        SolrQueryRequest req = rb.req;
        SolrParams params = req.getParams();
        if (!params.getBool(COMPONENT_NAME, false)) {
            return;
        }
        SolrQueryResponse rsp = rb.rsp;

        String defType = params.get(QueryParsing.DEFTYPE, QParserPlugin.DEFAULT_QTYPE);

        // Get it from the response builder to give a different component a chance
        // to set it; fall back to the raw q param (mirrors QueryComponent).
        String queryString = rb.getQueryString();
        if (queryString == null) {
            queryString = params.get(CommonParams.Q);
        }

        try {
            QParser parser = QParser.getParser(queryString, defType, req);
            Query q = parser.getQuery(); // triggers the full aqp expansion pipeline
            // Render exactly as debug.parsedquery does (NOT plain q.toString()).
            rsp.add(RESPONSE_KEY, QueryParsing.toString(q, req.getSchema()));
        } catch (SyntaxError | RuntimeException e) {
            rsp.add(ERROR_KEY, e.getMessage());
        } finally {
            // Skip the search: neutralize QueryComponent and the components that
            // need a results object. Done even on the error path so the "no
            // search" contract holds regardless.
            ModifiableSolrParams mp = new ModifiableSolrParams(params);
            mp.set(QueryComponent.COMPONENT_NAME, false);
            mp.set(WordCloudComponent.COMPONENT_NAME, false);
            mp.set(FacetComponent.COMPONENT_NAME, false);
            req.setParams(mp);

            // The debug flags are already read from params into the
            // ResponseBuilder before the prepare loop runs, so disable debug on
            // rb directly; otherwise DebugComponent NPEs on the null rb.getQuery().
            rb.setDebug(false);
        }
    }

    @Override
    public void process(ResponseBuilder rb) throws IOException {
        // no-op; everything happens in prepare()
    }

    @Override
    public String getDescription() {
        return "Returns the expanded (parsed) query without executing the search";
    }
}
