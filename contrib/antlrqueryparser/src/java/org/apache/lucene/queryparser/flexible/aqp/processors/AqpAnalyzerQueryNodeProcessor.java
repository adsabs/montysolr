package org.apache.lucene.queryparser.flexible.aqp.processors;

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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.GroupQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.NoTokenFoundQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.RangeQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.TextableQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.TokenizedPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.standard.nodes.MultiPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.RegexpQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.StandardBooleanQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.processors.AnalyzerQueryNodeProcessor;

/**
 * This is an improved version of the {@link AnalyzerQueryNodeProcessor} it is
 * better because it keeps track of the position offset which is absolutely
 * indispensable for proper parsing of expanded queries. And also we save the
 * type attribute name with the node
 * 
 * TODO: send a patch and make them accept it
 * 
 * This processor verifies if {@link ConfigurationKeys#ANALYZER} is defined in
 * the {@link QueryConfigHandler}. If it is and the analyzer is not
 * <code>null</code>, it looks for every {@link FieldQueryNode} that is not
 * {@link WildcardQueryNode}, {@link FuzzyQueryNode} or {@link RangeQueryNode}
 * contained in the query node tree, then it applies the analyzer to that
 * {@link FieldQueryNode} object.
 * <p>
 * If the analyzer return only one term, the returned term is set to the
 * {@link FieldQueryNode} and it's returned.
 * <p>
 * If the analyzer return more than one term, a {@link TokenizedPhraseQueryNode}
 * or {@link MultiPhraseQueryNode} is created, whether there is one or more
 * terms at the same position, and it's returned.
 * <p>
 * If no term is returned by the analyzer a {@link NoTokenFoundQueryNode} object
 * is returned.
 * 
 * 
 * @see ConfigurationKeys#ANALYZER
 * @see Analyzer
 * @see TokenStream
 */

public class AqpAnalyzerQueryNodeProcessor extends QueryNodeProcessorImpl {

  public static String TYPE_ATTRIBUTE = "token_type_attribute";
  private Analyzer analyzer;

  private boolean positionIncrementsEnabled;

  public AqpAnalyzerQueryNodeProcessor() {
    // empty constructor
  }

  @Override
  public QueryNode process(QueryNode queryTree) throws QueryNodeException {
    Analyzer analyzer = getQueryConfigHandler().get(ConfigurationKeys.ANALYZER);

    if (analyzer != null) {
      this.analyzer = analyzer;
      this.positionIncrementsEnabled = false;
      Boolean positionIncrementsEnabled = getQueryConfigHandler().get(
          ConfigurationKeys.ENABLE_POSITION_INCREMENTS);

      if (positionIncrementsEnabled != null) {
        this.positionIncrementsEnabled = positionIncrementsEnabled;
      }

      if (this.analyzer != null) {
        return super.process(queryTree);
      }

    }

    return queryTree;

  }

  @Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {

    if (node instanceof TextableQueryNode
        && !(node instanceof WildcardQueryNode)
        && !(node instanceof FuzzyQueryNode)
        && !(node instanceof RegexpQueryNode)
        && !(node.getParent() instanceof RangeQueryNode)) {

      FieldQueryNode fieldNode = ((FieldQueryNode) node);
      int queryStart = Math.max(fieldNode.getBegin(), 0); // could be -1
      String text = fieldNode.getTextAsString();
      String field = fieldNode.getFieldAsString();

      TokenStream source = null;
      CachingTokenFilter buffer = null;
      
      try {
        source = this.analyzer.tokenStream(field, new StringReader(text));
        source.reset();
        buffer = new CachingTokenFilter(source); //TODO: use reusable strategy?
  
        PositionIncrementAttribute posIncrAtt = null;
        int numTokens = 0;
        int positionCount = 0;
        boolean severalTokensAtSamePosition = false;
  
        if (buffer.hasAttribute(PositionIncrementAttribute.class)) {
          posIncrAtt = buffer.getAttribute(PositionIncrementAttribute.class);
        }
  
        TypeAttribute typeAtt = null;
        if (buffer.hasAttribute(TypeAttribute.class)) {
          typeAtt = buffer.getAttribute(TypeAttribute.class);
        }
  
        
        while (buffer.incrementToken()) {
          numTokens++;
          int positionIncrement = (posIncrAtt != null) ? posIncrAtt
              .getPositionIncrement() : 1;
          if (positionIncrement != 0) {
            positionCount += positionIncrement;
  
          } else {
            severalTokensAtSamePosition = true;
          }
  
        }
  
        
  
        // rewind the buffer stream
        buffer.reset();
  
        // close original stream - all tokens buffered
        source.close();
        source = null;
  
        if (!buffer.hasAttribute(CharTermAttribute.class)) {
          return new NoTokenFoundQueryNode();
        }
  
        CharTermAttribute termAtt = buffer.getAttribute(CharTermAttribute.class);
  
        int offsetStart = -1;
        int offsetEnd = -1;
        OffsetAttribute offsetAtt;
        if (buffer.hasAttribute(OffsetAttribute.class)) {
          offsetAtt = buffer.getAttribute(OffsetAttribute.class);
        } else {
          offsetAtt = null;
        }
  
        if (numTokens == 0) {
          return new NoTokenFoundQueryNode();
  
        } else if (numTokens == 1) {
          String term = null;
          try {
            boolean hasNext;
            hasNext = buffer.incrementToken();
            assert hasNext == true;
            term = termAtt.toString();
  
          } catch (IOException e) {
            // safe to ignore, because we know the number of tokens
          }
  
          fieldNode.setText(term);
          if (offsetAtt != null) {
            fieldNode.setBegin(queryStart + offsetAtt.startOffset());
            fieldNode.setEnd(queryStart + offsetAtt.endOffset());
          }
          if (typeAtt != null)
            fieldNode.setTag(TYPE_ATTRIBUTE, typeAtt.type());
          return fieldNode;
  
        } else if (severalTokensAtSamePosition
            || !(node instanceof QuotedFieldQueryNode)) {
          if (positionCount == 1 || !(node instanceof QuotedFieldQueryNode)) {
            // no phrase query:
            LinkedList<QueryNode> children = new LinkedList<QueryNode>();
  
            for (int i = 0; i < numTokens; i++) {
              String term = null;
              offsetStart = offsetEnd = -1;
              try {
                boolean hasNext = buffer.incrementToken();
                assert hasNext == true;
                term = termAtt.toString();
                if (offsetAtt != null) {
                  offsetStart = queryStart + offsetAtt.startOffset();
                  offsetEnd = queryStart + offsetAtt.endOffset();
                }
  
              } catch (IOException e) {
                // safe to ignore, because we know the number of tokens
              }
  
              FieldQueryNode fq = new FieldQueryNode(field, term, offsetStart,
                  offsetEnd);
              if (typeAtt != null)
                fq.setTag(TYPE_ATTRIBUTE, typeAtt.type());
              children.add(fq);
  
            }
            return new GroupQueryNode(new StandardBooleanQueryNode(children,
                positionCount == 1));
          } else {
            // phrase query:
            MultiPhraseQueryNode mpq = new MultiPhraseQueryNode();
  
            List<FieldQueryNode> multiTerms = new ArrayList<FieldQueryNode>();
            int position = -1;
            int i = 0;
            int termGroupCount = 0;
  
            for (; i < numTokens; i++) {
              String term = null;
              offsetStart = offsetEnd = -1;
              int positionIncrement = 1;
              String tokenType = null;
              try {
                boolean hasNext = buffer.incrementToken();
                assert hasNext == true;
                term = termAtt.toString();
                if (posIncrAtt != null) {
                  positionIncrement = posIncrAtt.getPositionIncrement();
                }
                if (offsetAtt != null) {
                  offsetStart = queryStart + offsetAtt.startOffset();
                  offsetEnd = queryStart + offsetAtt.endOffset();
                }
                if (typeAtt != null)
                  tokenType = typeAtt.type();
              } catch (IOException e) {
                // safe to ignore, because we know the number of tokens
              }
  
              if (positionIncrement > 0 && multiTerms.size() > 0) {
  
                for (FieldQueryNode termNode : multiTerms) {
  
                  if (this.positionIncrementsEnabled) {
                    termNode.setPositionIncrement(position);
                  } else {
                    termNode.setPositionIncrement(termGroupCount);
                  }
  
                  mpq.add(termNode);
  
                }
  
                // Only increment once for each "group" of
                // terms that were in the same position:
                termGroupCount++;
  
                multiTerms.clear();
  
              }
  
              position += positionIncrement;
              FieldQueryNode fq = new FieldQueryNode(field, term, offsetStart,
                  offsetEnd);
              fq.setTag(TYPE_ATTRIBUTE, tokenType);
              multiTerms.add(fq);
  
            }
  
            for (FieldQueryNode termNode : multiTerms) {
  
              if (this.positionIncrementsEnabled) {
                termNode.setPositionIncrement(position);
  
              } else {
                termNode.setPositionIncrement(termGroupCount);
              }
  
              mpq.add(termNode);
  
            }
  
            return mpq;
  
          }
  
        } else {
  
          TokenizedPhraseQueryNode pq = new TokenizedPhraseQueryNode();
  
          int position = -1;
  
          for (int i = 0; i < numTokens; i++) {
            String term = null;
            int positionIncrement = 1;
            offsetStart = offsetEnd = -1;
  
            try {
              boolean hasNext = buffer.incrementToken();
              assert hasNext == true;
              term = termAtt.toString();
  
              if (posIncrAtt != null) {
                positionIncrement = posIncrAtt.getPositionIncrement();
              }
  
              if (offsetAtt != null) {
                offsetStart = queryStart + offsetAtt.startOffset();
                offsetEnd = queryStart + offsetAtt.endOffset();
              }
  
            } catch (IOException e) {
              // safe to ignore, because we know the number of tokens
            }
  
            FieldQueryNode newFieldNode = new FieldQueryNode(field, term,
                offsetStart, offsetEnd);
            if (typeAtt != null)
              newFieldNode.setTag(TYPE_ATTRIBUTE, typeAtt.type());
  
            if (this.positionIncrementsEnabled) {
              position += positionIncrement;
              newFieldNode.setPositionIncrement(position);
  
            } else {
              newFieldNode.setPositionIncrement(i);
            }
  
            pq.add(newFieldNode);
  
          }
  
          return pq;
  
        }
      } catch (IOException e1) {
        throw new RuntimeException(e1);
      } finally {
        if (source != null) {
          try {
            source.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        if (buffer != null) {
          try {
            buffer.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

    }

    return node;

  }

  @Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {

    return node;

  }

  @Override
  protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
      throws QueryNodeException {

    return children;

  }

}
