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
package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;
import java.util.Map;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsScoringQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.RegexpQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.processors.MultiTermRewriteMethodProcessor;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;

public class AqpChangeRewriteMethodProcessor extends
  AqpQueryNodeProcessorImpl {
  boolean first = true;

  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
    
    if (first && getConfigVal("aqp.classic_scoring.modifier", "") != "") {
      // TODO: i don't want to make the source field be changed with URL params
      // but i'd like it to be configurable
      
      SolrQueryRequest req = this.getQueryConfigHandler()
          .get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
          .getRequest();
      
      ModifiableSolrParams params = new ModifiableSolrParams(req.getParams());
      params.remove("aqp.classic_scoring.modifier");
      req.setParams(params);
      
      
      node = new AqpAdsabsScoringQueryNode(node, "cite_read_boost", 
          Float.parseFloat(getConfigVal("aqp.classic_scoring.modifier")));
      
    }
    first = false;
    return node;
    
  }

  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
    String key;
    String method;
    if (node instanceof PrefixWildcardQueryNode) { 
      key = "aqp.qprefix.scoring." + ((FieldQueryNode) node).getFieldAsString();
      if ((method = getConfigVal(key, "")) != "") {
        setRewriteMethod(node, method);
      }
    }
    else if (node instanceof WildcardQueryNode) {
      key = "aqp.qwildcard.scoring." + ((FieldQueryNode) node).getFieldAsString();
      if ((method = getConfigVal(key, "")) != "") {
        setRewriteMethod(node, method);
      }
    }
    else if (node instanceof RegexpQueryNode) {
      key = "aqp.qregex.scoring." + ((FieldQueryNode) node).getFieldAsString();
      if ((method = getConfigVal(key, "")) != "") {
        setRewriteMethod(node, method);
      }
    }
    
    return node;
  }

  private void setRewriteMethod(QueryNode node, String method) throws QueryNodeException {
    if ("constant".equals(method)) {      
      node.setTag(MultiTermRewriteMethodProcessor.TAG_ID, MultiTermQuery.CONSTANT_SCORE_REWRITE);
    }
    else if ("constant_boolean".equals(method)) {
      node.setTag(MultiTermRewriteMethodProcessor.TAG_ID, MultiTermQuery.CONSTANT_SCORE_BOOLEAN_REWRITE);
    }
    else if ("boolean".equals(method)) {
      node.setTag(MultiTermRewriteMethodProcessor.TAG_ID, MultiTermQuery.SCORING_BOOLEAN_REWRITE);
    }
    else if ("topterms_blended".equals(method)) {
      node.setTag(MultiTermRewriteMethodProcessor.TAG_ID, new MultiTermQuery.TopTermsBlendedFreqScoringRewrite(1024));
    }
    else if ("topterms".equals(method)) {
        node.setTag(MultiTermRewriteMethodProcessor.TAG_ID, new MultiTermQuery.TopTermsScoringBooleanQueryRewrite(1024));
    }
    else if ("topterms_boosted".equals(method)) {
        node.setTag(MultiTermRewriteMethodProcessor.TAG_ID, new MultiTermQuery.TopTermsBoostOnlyBooleanQueryRewrite(1024));
    }
    else {
      throw new QueryNodeException(new MessageImpl(
          QueryParserMessages.PARAMETER_VALUE_NOT_SUPPORTED, "Unknown rewrite method: \"" + method + "\""));
    }
  }

  @Override
  protected List<QueryNode> setChildrenOrder(List<QueryNode> children) throws QueryNodeException {
    return children;
  }

}
