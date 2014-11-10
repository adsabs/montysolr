package org.apache.solr.handler.component;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.parser.SyntaxParser;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.search.AqpAdsabsQParser;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.QueryParsing;
import org.apache.solr.search.SyntaxError;

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
 * Returns the JSON representation of the query syntax tree
 * useful for parsing query (providing feedback to the user;
 * used by UI Bumblebee)
 * 
 */
public class AqpQueryTree extends SearchComponent {
  
  public static final String COMPONENT_NAME = "qtree";
  
  @Override
  public void prepare(ResponseBuilder rb) throws IOException {
    SolrQueryRequest req = rb.req;
    SolrParams params = req.getParams();
    if (!params.getBool(COMPONENT_NAME, true)) {
      return;
    }
    SolrQueryResponse rsp = rb.rsp;
    
    
    String defType = params.get(QueryParsing.DEFTYPE,QParserPlugin.DEFAULT_QTYPE);
    
    // get it from the response builder to give a different component a chance
    // to set it.
    String queryString = rb.getQueryString();
    if (queryString == null) {
      // this is the normal way it's set.
      queryString = params.get( CommonParams.Q );
    }
    
    QParser parser;
    try {
      parser = QParser.getParser(queryString, defType, req);
      
      if (parser instanceof AqpAdsabsQParser) {
        AqpQueryParser aqpParser = ((AqpAdsabsQParser) parser).getParser();
        SyntaxParser syntaxParser = aqpParser.getSyntaxParser();
        QueryNode queryTree = syntaxParser.parse(queryString, null);
        if (queryTree instanceof AqpANTLRNode) {
          if (params.get(CommonParams.WT, "json") == "json") {
            //System.err.println(((AqpANTLRNode) queryTree).toJson());
            rsp.add("qtree", ((AqpANTLRNode) queryTree).toJson());
          }
          else {
            //System.err.println(((AqpANTLRNode) queryTree).toString());
            rsp.add("qtree", ((AqpANTLRNode) queryTree).toString());
          }
        }
      }
      
    } catch (QueryNodeParseException e) {
      rsp.add("qtreeError", e.getMessage());
    } catch (SyntaxError e) {
	    rsp.add("qtreeError", e.getMessage());
    }
    
    
  }
  
  @Override
  public void process(ResponseBuilder rb) throws IOException {
    // do nothing
  }
  
  @Override
  public String getDescription() {
    return null;
  }
  
  @Override
  public String getSource() {
    return null;
  }
  
}
