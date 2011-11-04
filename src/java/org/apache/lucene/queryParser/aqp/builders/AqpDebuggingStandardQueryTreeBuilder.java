package org.apache.lucene.queryParser.aqp.builders;

import java.util.List;

import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.builders.QueryBuilder;
import org.apache.lucene.queryParser.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.search.Query;


/**
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
 * This query tree builder only defines the necessary map to build a
 * {@link Query} tree object. It should be used to generate a {@link Query} tree
 * object from a query node tree processed by a
 * {@link AqpStandardQueryNodeProcessorPipeline}. <br/>
 * 
 * @see QueryTreeBuilder
 * @see StandardQueryNodeProcessorPipeline
 */
public class AqpDebuggingStandardQueryTreeBuilder extends AqpStandardQueryTreeBuilder {

  public AqpDebuggingStandardQueryTreeBuilder() {
	  super();
  }

  @Override
  public Query build(QueryNode queryNode) throws QueryNodeException {

	process(queryNode);
    
    return (Query) queryNode.getTag(QUERY_TREE_BUILDER_TAGID);
  }
  
  // TODO: use reflection to get at the private methods
  private void process(QueryNode node) throws QueryNodeException {

	    if (node != null) {
	      QueryBuilder builder = getBuilder(node);

	      if (!(builder instanceof QueryTreeBuilder)) {
	        List<QueryNode> children = node.getChildren();

	        if (children != null) {

	          for (QueryNode child : children) {
	            process(child);
	          }

	        }

	      }

	      super.processNode(node, builder);

	    }

	  }

}
