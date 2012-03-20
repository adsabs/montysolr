package org.apache.lucene.queryParser.aqp.nodes;

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

import java.util.List;

import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode.Modifier;
import org.apache.lucene.queryParser.core.nodes.QueryNode;

/**
 * A {@link AqpOrQueryNode} represents an OR boolean operation performed on a
 * list of nodes.
 * 
 * @see AqpBooleanQueryNode
 */
public class AqpOrQueryNode extends AqpBooleanQueryNode {

	private static final long serialVersionUID = 8472252510866053747L;

	/**
	 * @param clauses
	 *            - the query nodes to be or'ed
	 */
	public AqpOrQueryNode(List<QueryNode> clauses) {
		super(clauses);
		
		operator = "OR";
		
		//applyModifier(clauses, Modifier.MOD_NONE);
		
		// unfortunately we have to do it like this (when subclassing from
		// BooleanQueryNode)
		set(clauses);

	}

}
