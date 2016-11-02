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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.BoostQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldableNode;
import org.apache.lucene.queryparser.flexible.core.nodes.GroupQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.OrQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;

/**
 * 
 * A modified copy of the MultiFieldQueryNodeProcessor - ADS is using it
 * to provide virtual fields that map into real indexes, eg.
 * 
 * <p>
 * 
 * 		'full' is a virtual field for 'body', 'title', 'abstract'
 * 
 * <p>
 * 
 * This processor is used to expand terms so the query looks for the same term
 * in different fields. It also boosts a query based on its field.
 * <p>
 * This processor looks for every {@link FieldableNode} contained in the query
 * node tree. If a {@link FieldableNode} is found, it checks if there is a
 * {@link ConfigurationKeys#MULTI_FIELDS} defined in the {@link QueryConfigHandler}. If
 * there is, the {@link FieldableNode} is cloned N times and the clones are
 * added to a {@link BooleanQueryNode} together with the original node. N is
 * defined by the number of fields that it will be expanded to. The
 * {@link BooleanQueryNode} is returned.
 * 
 * @see ConfigurationKeys#MULTI_FIELDS
 */
public class AqpVirtualFieldsQueryNodeProcessor extends QueryNodeProcessorImpl {

  private boolean processChildren = true;

  public AqpVirtualFieldsQueryNodeProcessor() {
    // empty constructor
  }

  @Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {

    return node;

  }

  @Override
  protected void processChildren(QueryNode queryTree) throws QueryNodeException {

    if (this.processChildren) {
      super.processChildren(queryTree);

    } else {
      this.processChildren = true;
    }

  }

  @Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {

    if (node instanceof FieldableNode) {
      this.processChildren = false;
      FieldableNode fieldNode = (FieldableNode) node;

      Map<String, Map<String, Float>> fields = getQueryConfigHandler().get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.VIRTUAL_FIELDS);
      
      if (fields == null) {
        throw new IllegalArgumentException(
            "AqpAdsabsQueryConfigHandler.ConfigurationKeys.VIRTUAL_FIELDS should be set on the QueryConfigHandler");
      }
      
      if (fieldNode.getField() != null && fields.containsKey(fieldNode.getField())) {
        
      	Map<String, Float> realFields = fields.get(fieldNode.getField());

        if (realFields != null && realFields.size() > 0) {
        	
          if (fields.size() == 1) {
          	for (Entry<String, Float> en: realFields.entrySet()) {
	          	fieldNode.setField(en.getKey());
	          	if (en.getValue() != null && en.getValue() != 1.0f) {
	          		return new BoostQueryNode(fieldNode, en.getValue());
	          	}
	          	else {
	          		return fieldNode;
	          	}
          	}

          } else {
            LinkedList<QueryNode> children = new LinkedList<QueryNode>();

            for (Entry<String, Float> en: realFields.entrySet()) {
              try {
                FieldableNode newNode = (FieldableNode) fieldNode.cloneTree();
                newNode.setField(en.getKey());
                
                if (en.getValue() != null && en.getValue() != 1.0f) {
                	children.add(new BoostQueryNode(newNode, en.getValue()));
  	          	}
  	          	else {
  	          		children.add(newNode);
  	          	}

              } catch (CloneNotSupportedException e) {
                // should never happen
              }

            }

            return new GroupQueryNode(new OrQueryNode(children));

          }

        }

      }

    }

    return node;

  }


	@Override
  protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
      throws QueryNodeException {

    return children;

  }

}
