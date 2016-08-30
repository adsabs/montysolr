package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.index.Term;

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

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.SlopQueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PhraseQuery.Builder;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

/**
 * This builder basically reads the {@link Query} object set on the
 * {@link SlopQueryNode} child using
 * {@link QueryTreeBuilder#QUERY_TREE_BUILDER_TAGID} and applies the slop value
 * defined in the {@link SlopQueryNode}.
 * 
 * IFF the default value is zero, we'll check positions of the 
 * terms and adjust the slope, eg. positions 0,1,5 will get slope of
 * 3 because there are three empty tokens inbetween (2,3,4)
 */
public class AqpSlopQueryNodeBuilder implements StandardQueryBuilder {

  public AqpSlopQueryNodeBuilder() {
    // empty constructor
  }

  public Query build(QueryNode queryNode) throws QueryNodeException {
    SlopQueryNode phraseSlopNode = (SlopQueryNode) queryNode;

    Query query = (Query) phraseSlopNode.getChild().getTag(
        QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);

    int defaultValue = phraseSlopNode.getValue();
    
    
    if (query instanceof PhraseQuery) {
    	if (defaultValue == 0) {
	    	int[] pos = ((PhraseQuery) query).getPositions();
	    	if (pos[pos.length-1] > pos.length) {
	    		defaultValue = pos[pos.length-1] - pos.length + 1;
	    	}
    	}
    	
    	if (defaultValue <= 1) return query;
    	
    	Builder builder = new PhraseQuery.Builder();
    	builder.setSlop(defaultValue);
    	for (Term t: ((PhraseQuery) query).getTerms()) {
    	  builder.add(t);
    	}
    	query = builder.build();

    } else {
    	if (defaultValue == 0) {
	    	int[] pos = ((MultiPhraseQuery) query).getPositions();
	    	if (pos[pos.length-1] > pos.length) {
	    		defaultValue = pos[pos.length-1] - pos.length + 1;
	    	}
    	}
    	
    	if (defaultValue <= 1) return query;
    	
      MultiPhraseQuery.Builder builder = new MultiPhraseQuery.Builder();
      builder.setSlop(defaultValue);
      Term[][] terms = ((MultiPhraseQuery) query).getTermArrays(); 
      for (int i=0; i < terms.length; i++) {
        builder.add(terms[i]);
      }
      query = builder.build();
    }

    return query;

  }

}
