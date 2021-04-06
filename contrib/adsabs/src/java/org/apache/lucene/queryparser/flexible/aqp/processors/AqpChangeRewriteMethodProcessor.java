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
import java.util.Set;
import java.util.TreeMap;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsScoringQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpOrQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.FieldConfig;
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
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.handler.AdsConfigHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.util.RefCounted;

public class AqpChangeRewriteMethodProcessor extends
  AqpQueryNodeProcessorImpl {
  boolean first = true;
  private Set<String> types = null;
  private Set<String> fields = null;
  private Set<String> ignoredFields = null;
  
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
        
        if (getConfigVal("aqp.multiphrase.keep_one.ignore.fields", null) != null) {
          
          if (ignoredFields == null) {
            ignoredFields = new HashSet<String>();
            for (String s: getConfigVal("aqp.multiphrase.keep_one.ignore.fields").split(",")) {
              ignoredFields.add(s);
            }
          }
          
          if (ignoredFields.contains((String)((MultiPhraseQueryNode) node).getField())) {
            
            // for ignored fields, we don't want to do proximity search
            for (QueryNode child: node.getChildren()) {
              child.setTag(AqpAnalyzerQueryNodeProcessor.MAX_MULTI_TOKEN_SIZE, 0);
            }
            
            return node;
          }
        }
        

        AqpRequestParams reqAttr = this.getQueryConfigHandler().get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST);
        if (reqAttr != null) {
          IndexSchema schema = reqAttr.getRequest().getSchema();
          FieldType fType = schema.getFieldType((String)((MultiPhraseQueryNode) node).getField());
          if (fType != null) {
            node.setTag("field.is.tokenized", fType.isTokenized());
          }
        }
        
        
        try {
          node = simplifyMultiphrase(node, types);
        } catch (IOException e) {
          throw new QueryNodeException(e);
        }
      }
    }
    else if (node instanceof AqpOrQueryNode && 
        node.getTag(AqpQueryTreeBuilder.SYNONYMS) != null &&
            (Boolean) node.getTag(AqpQueryTreeBuilder.SYNONYMS) == true
        ) {
      List<QueryNode> children = node.getChildren();
      // be definition, all tokens must be from the same field
      QueryNode fNode = children.get(0);
      if (fNode instanceof FieldQueryNode) {
        String f = ((FieldQueryNode) fNode).getFieldAsString();
        
        if (getFields().contains(f)) {
          LinkedList<QueryNode> newList = new LinkedList<QueryNode>();
          
          try {
            pickSynonyms(children, newList, getTypes());
            node.set(newList);
          } catch (IOException e) {
            throw new QueryNodeException(e);
          }
        }        
      }
    }
    
    return node;
  }
  
  private Set<String> getTypes() {
    if (types == null) {
      types = new HashSet<String>();
      for (String s: getConfigVal("aqp.multiphrase.keep_one", "").split(",")) {
        types.add(s);
      }
    }
    return types;
  }
  
  private Set<String> getFields() {
    if (fields == null) {
      fields = new HashSet<String>();
      for (String s: getConfigVal("aqp.multiphrase.fields", "").split(",")) {
        fields.add(s);
      }
    }
    return fields ;
  }
  

  private QueryNode simplifyMultiphrase(QueryNode node, Set<String> typesToKeep) throws IOException {
    
    // will inspect multiphrase children, discover those that fall on the same
    // position and will only keep one of them (so that we avoid double scoring)
    
    
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
      

      List newList = new LinkedList<QueryNode>();
      for (int positionIncrement : positionTermMap.keySet()) {
        
        List<QueryNode> termList = positionTermMap.get(positionIncrement);
        if (termList.size() > 1) {
          pickSynonyms(termList, newList, getTypes());
        }
        else {
          newList.add(termList.get(0));
        }
      }
      
      // it's guaranteed to be a simple phrase
      node.set(newList);
      
    }
    
    return node;
  }

  private void pickSynonyms(List<QueryNode> termList, List<QueryNode> newList, Set<String> typesToKeep) throws IOException {
    
    SolrQueryRequest req = this.getQueryConfigHandler()
        .get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
        .getRequest();
    SolrIndexSearcher searcher;
    RefCounted<SolrIndexSearcher> s = req.getCore().getRegisteredSearcher();
    
    try {
      searcher = s.get();
      
      // there exists two situations:
      //   1. user input was short and resulted in multi-token synonym
      //   2. user input was series of tokens that were identified as multi-token synonym
      // here we have to decide what scenario it is and for
      //   1. pick the most-frequent synonym
      //   2. pick the least-frequent synonym
      // we'll use the information about the original input token begin and end
      // positions, to guess what situation we are in
      
      int equalLength = 0;
      int userInputLen = 0;
      int numTokens = 0;
      int tokenLongerThanInput = 0;
      int tokenShorterThanInput = 0;
      int begin = 0;
      int end = 0;
      int len = 0;
      String text;
      FieldQueryNode maxFreqTerm = null;
      FieldQueryNode minFreqTerm = null;
      FieldQueryNode closestLenTerm = null;
      int termFreq;
      int minFreq = Integer.MAX_VALUE;
      int maxFreq = Integer.MIN_VALUE;
      Integer closestLen = null;
      int oldSize = newList.size();
      
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
        len = text.length() - (text.indexOf("::") + 2);
        userInputLen += len;
        numTokens++;
        
        // how many times the current token fits into the user input
        // anything below 1.0 means the current token is longer than 
        // what user typed
        
        float ratio = (float)(end-begin) / (float)len;
        
        if (ratio == 1.0f) {
          equalLength++;
        }
        else if (ratio < 1.2f) { // we give it bit of slack
          tokenLongerThanInput++;
        }
        else {
          tokenShorterThanInput++;
        }
        
        if (closestLen == null || Math.abs((end-begin)-len) < closestLen) {
          closestLen = Math.abs((end-begin)-len);
          closestLenTerm = termNode;
        }
        
        
        // careful, 0 means the term does not exist
        termFreq = searcher.docFreq(new Term(termNode.getFieldAsString(), text));
        
        // we'll ignore unknown terms
        if (termFreq > 0) {
          if (termFreq < minFreq) {
            minFreqTerm = termNode;
            minFreq = termFreq;
          }
          else if (termFreq == minFreq && text.length() > minFreqTerm.getValue().length()) {
            minFreqTerm = termNode; // if same docfreq, pick longer ones
          }
          
          if (termFreq > maxFreq) {
            maxFreqTerm = termNode;
            maxFreq = termFreq;
          }
          else if (termFreq == maxFreq && text.length() < minFreqTerm.getValue().length()) {
            maxFreqTerm = termNode; // if same frequency, pick shorter one
          }
        }
        
      }
      
      String strategy = null;
      if (tokenLongerThanInput > tokenShorterThanInput) {
        strategy = "mostFrequent"; // most tokens are longer than input (i.e. user typed acronym)
                                   // pick the shortest - i.e. more frequent term
      }
      else if (tokenShorterThanInput > tokenLongerThanInput) {
        strategy = "leastFrequent"; // most tokens were equal or shorter than the user's input
                                    // pick the longest - i.e. more specific term
      }
      else { // they were equal lengths
        strategy = "cantDecide"; 
        if (minFreqTerm != null && maxFreqTerm != null) {
          float diffMax = Math.abs((float)userInputLen/numTokens - ((FieldQueryNode) maxFreqTerm).getTextAsString().length());
          float diffMin = Math.abs((float)userInputLen/numTokens - ((FieldQueryNode) minFreqTerm).getTextAsString().length());
          
          if (diffMax < diffMin) { // longer term is closer to input
            strategy = "leastFrequent";
          }
          else if (diffMin < diffMax) { // shorter term is closer to the user input length
            strategy = "mostFrequent";
          }
        }
      }
      
      
      
      if (strategy.equals("mostFrequent") && maxFreqTerm != null) {
        newList.add(maxFreqTerm);
      }
      else if (strategy.equals("leastFrequent") && minFreqTerm != null) {
        newList.add(minFreqTerm);
      }
      else if (strategy.equals("cantDecide") && closestLenTerm != null) {
        newList.add(closestLenTerm);
      }
      
      
      if (newList.size() == oldSize) { // we didn't find any type that would satisfy the condition
        QueryNode picked = termList.get(0);
        // pick the longest if you can
        int x = 0;
        for (QueryNode t: termList) {
          int l = ((FieldQueryNode) t).getTextAsString().length();
          if (l > x) {
            x = l;
            picked = t;
          }
        }
        newList.add(picked);                
      }
      
    }
    finally  {
      s.decref();
    }
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
