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
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;

/**
 * A {@link AqpDefopQueryNode} represents the default boolean operation performed on a
 * list of nodes. 
 * 
 * This behaves the same way as any AqpBooleanQueryNode but we have the advantage of
 * knowing which tokens were marked by the DEFOP operator and later on we can look at 
 * them and process specially (in the logic that explicit AND is stronger than implicit
 * AND) 
 * 
 * @see AqpBooleanQueryNode
 * @see StandardQueryConfigHandler.Operator
 */
public class AqpDefopQueryNode extends AqpBooleanQueryNode {


	/**
	 * @param clauses
	 *            - the query nodes to be joined
	 */
	public AqpDefopQueryNode(List<QueryNode> clauses, StandardQueryConfigHandler.Operator op) {
		super(clauses);


		if ((clauses == null) || (clauses.size() == 0)) {
			throw new IllegalArgumentException(
					"DEFOP query must have at least one clause");
		}
		
		if (op.equals(StandardQueryConfigHandler.Operator.AND)) {
			operator = "AND";
			applyModifier(clauses, Modifier.MOD_REQ);
		}
		else if (op.equals(StandardQueryConfigHandler.Operator.OR)) {
			operator = "OR";
			applyModifier(clauses, Modifier.MOD_NONE);
		}
		
		
		// unfortunately we have to do it like this (when subclassing from
		// BooleanQueryNode)
		set(clauses);

	}

}
