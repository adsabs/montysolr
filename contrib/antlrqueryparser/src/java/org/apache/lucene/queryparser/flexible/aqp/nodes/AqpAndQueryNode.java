package org.apache.lucene.queryparser.flexible.aqp.nodes;

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

import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode.Modifier;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

/**
 * A {@link AqpAndQueryNode} represents an AND boolean operation performed on a
 * list of nodes.
 * 
 * @see AqpBooleanQueryNode
 */
public class AqpAndQueryNode extends AqpBooleanQueryNode {

	private static final long serialVersionUID = -4148186404006404927L;

	/**
	 * @param clauses
	 *            - the query nodes to be and'ed
	 */
	public AqpAndQueryNode(List<QueryNode> clauses) {
		super(clauses);

		operator = "AND";

		if ((clauses == null) || (clauses.size() == 0)) {
			throw new IllegalArgumentException(
					"AND query must have at least one clause");
		}
		
		applyModifier(clauses, Modifier.MOD_REQ);
		
		// unfortunately we have to do it like this (when subclassing from
		// BooleanQueryNode)
		set(clauses);

	}

}
