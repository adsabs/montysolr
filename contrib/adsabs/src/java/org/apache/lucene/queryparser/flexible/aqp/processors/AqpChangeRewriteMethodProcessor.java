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

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsScoringQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.MultiPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.RegexpQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.processors.MultiTermRewriteMethodProcessor;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.util.RefCounted;

public class AqpChangeRewriteMethodProcessor extends
  AqpQueryNodeProcessorImpl {
  boolean first = true;
  private Set<String> types = null;
  
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
    else if (node instanceof MultiPhraseQueryNode) {
      if (getConfigVal("aqp.multiphrase.keep_one", null) != null) {
        
        if (types == null) {
          types = new HashSet<String>();
          for (String s: getConfigVal("aqp.multiphrase.keep_one").split(",")) {
            types.add(s);
          }
        }
        
        try {
          node = simplifyMultiphrase(node, types);
        } catch (IOException e) {
          throw new QueryNodeException(e);
        }
      }
    }
    
    return node;
  }

  private QueryNode simplifyMultiphrase(QueryNode node, Set<String> typesToKeep) throws IOException {
    
    // will inspect multiphrase children, discover those that fall on the same
    // position and will only keep one of them (so that we avoid double scoring)
    
    SolrQueryRequest req = this.getQueryConfigHandler()
        .get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
        .getRequest();
    SolrIndexSearcher searcher;
    RefCounted<SolrIndexSearcher> s = req.getCore().getRegisteredSearcher();
    
    try {
      searcher = s.get();
      

      List<QueryNode> children = node.getChildren();
      if (children != null) {
        TreeMap<Integer, List<QueryNode>> positionTermMap = new TreeMap<>();
        
        // first group nodes into positions
        for (QueryNode child : children) {
          FieldQueryNode termNode = (FieldQueryNode) child;
          
          List<QueryNode> termList = positionTermMap.get(termNode
              .getPositionIncrement());
  
          if (termList == null) {
            termList = new LinkedList<>();
            positionTermMap.put(termNode.getPositionIncrement(), termList);
          }
  
          termList.add(termNode);
        }
        
  
        LinkedList newList = new LinkedList<>();
        for (int positionIncrement : positionTermMap.keySet()) {
          
          List<QueryNode> termList = positionTermMap.get(positionIncrement);
          if (termList.size() > 1) {
            
            // there exists two situations:
            //   1. user input was short and resulted in multi-token synonym
            //   2. user input was series of tokens that were identified as multi-token synonym
            // here we have to decide what scenario it is and for
            //   1. pick the most-frequent synonym
            //   2. pick the least-frequent synonym
            // we'll use the information about the original input token begin and end
            // positions, to guess what situation we are in
            
            int equalLength = 0;
            int fromShortToLongForm = 0;
            int fromLongToShort = 0;
            int begin = 0;
            int end = 0;
            int len = 0;
            String text;
            FieldQueryNode maxTerm = null;
            FieldQueryNode minTerm = null;
            int termFreq;
            int minFreqTerm = Integer.MAX_VALUE;
            int maxFreqTerm = Integer.MIN_VALUE;
            
            // first decide one scenarios 1. xor 2.
            for (QueryNode n: termList) {
              
              String t = (String) n.getTag(AqpAnalyzerQueryNodeProcessor.TYPE_ATTRIBUTE);
              if (t != null && !typesToKeep.contains(t)) {
                continue;
              }
              
              FieldQueryNode termNode = (FieldQueryNode) n;
              begin = termNode.getBegin();
              end = termNode.getEnd();
              text = termNode.getTextAsString();
              len = text.length() - 5;
              
              if (len > (end - begin)) {
                fromShortToLongForm++;
              }
              else if (len < (end - begin)) {
                fromLongToShort++;
              }
              else {
                equalLength++;
              }
              
              // careful, 0 means the term does not exist
              termFreq = searcher.docFreq(new Term(termNode.getFieldAsString(), text));
              
              // we'll ignore unknown terms
              if (termFreq > 0) {
                if (termFreq < minFreqTerm) {
                  minTerm = termNode;
                  minFreqTerm = termFreq;
                }
                else if (termFreq == minFreqTerm && text.length() > minTerm.getValue().length()) {
                  minTerm = termNode;
                }
                
                if (termFreq > maxFreqTerm) {
                  maxTerm = termNode;
                  maxFreqTerm = termFreq;
                }
                else if (termFreq == maxFreqTerm && text.length() < minTerm.getValue().length()) {
                  maxTerm = termNode;
                }
              }
              
            }
            
            String strategy = null;
            if (fromShortToLongForm > fromLongToShort) {
              strategy = "mostFrequent";
            }
            else if (fromLongToShort > fromShortToLongForm) {
              strategy = "leastFrequent"; 
            }
            else {
              strategy = "cantDecide"; // they were equal lengths
            }
            
            
            
            int added = 0;
            for (QueryNode n: termList) {
              String t = (String) n.getTag(AqpAnalyzerQueryNodeProcessor.TYPE_ATTRIBUTE);
              if (t != null && typesToKeep.contains(t)) {
                if (strategy.equals("mostFrequent") && n.equals(maxTerm)) {
                  newList.add(n);
                  added += 1;
                  break;
                }
                else if (strategy.equals("leastFrequent") && n.equals(minTerm)) {
                  newList.add(n);
                  added += 1;
                  break;
                }
                else if (strategy.equals("cantDecide")) {
                  newList.add(n);
                  added += 1;
                  break;                
                }
              }
            }
            
            if (added == 0) { // we didn't find any type that would satisfy the condition
              if (strategy.equals("mostFrequent") && maxTerm != null) {
                newList.add(maxTerm);
              }
              else if (strategy.equals("leastFrequent") && minTerm != null) {
                newList.add(minTerm);
              }
              else if (strategy.equals("cantDecide")) {
                newList.add(termList.get(0));                
              }
            }
            
          }
          else {
            newList.add(termList.get(0));
          }
        }
        
        // it's guaranteed to be a simple phrase
        node.set(newList);
        
      }
    }
    finally  {
      s.decref();
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
