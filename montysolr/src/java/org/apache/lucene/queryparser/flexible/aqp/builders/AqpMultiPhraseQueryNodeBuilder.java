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
package org.apache.lucene.queryparser.flexible.aqp.builders;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.queryparser.flexible.standard.nodes.MultiPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.MultiPhraseQueryNodeBuilder;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.TermQuery;

/**
 * Builds a {@link MultiPhraseQuery} object from a {@link MultiPhraseQueryNode}
 * object.
 * 
 * Modified {@link MultiPhraseQueryNodeBuilder} - when we encounter a token
 * with position increment=0 we assume that it should be placed into the same
 * position as the previous token (i.e. it is a synonym). Make sure that
 * the tokens submitted are in proper order!
 */
public class AqpMultiPhraseQueryNodeBuilder implements StandardQueryBuilder {

  public AqpMultiPhraseQueryNodeBuilder() {
    // empty constructor
  }

  @Override
  public MultiPhraseQuery build(QueryNode queryNode) throws QueryNodeException {
    MultiPhraseQueryNode phraseNode = (MultiPhraseQueryNode) queryNode;

    MultiPhraseQuery.Builder phraseQueryBuilder = new MultiPhraseQuery.Builder();

    List<QueryNode> children = phraseNode.getChildren();

    if (children != null) {
      TreeMap<Integer, List<Term>> positionTermMap = new TreeMap<>();
      Integer lastPos = null;

      for (QueryNode child : children) {
        FieldQueryNode termNode = (FieldQueryNode) child;
        TermQuery termQuery = (TermQuery) termNode
            .getTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);
        
        int pos = termNode.getPositionIncrement();
        if (pos == 0 && lastPos != null) {
          pos = lastPos;
        }
        else {
          lastPos = pos;
        }
        
        List<Term> termList = positionTermMap.get(pos);

        if (termList == null) {
          termList = new LinkedList<>();
          positionTermMap.put(pos, termList);

        }

        termList.add(termQuery.getTerm());

      }

      for (int positionIncrement : positionTermMap.keySet()) {
        List<Term> termList = positionTermMap.get(positionIncrement);

        phraseQueryBuilder.add(termList.toArray(new Term[termList.size()]),
            positionIncrement);

      }

    }

    return phraseQueryBuilder.build();

  }

}
