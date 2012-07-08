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

import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode.Modifier;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

/**
 * A {@link AqpNotQueryNode} represents an NOT boolean operation performed on a
 * list of nodes.
 * 
 * <br/>
 * 
 * The first node is set to be required {@link ModifierQueryNode.Modifier#MOD_REQ}
 * and the rest of the clauses will have a {@link ModifierQueryNode.Modifier#MOD_NOT} 
 * 
 * @see AqpBooleanQueryNode
 */
public class AqpNotQueryNode extends AqpBooleanQueryNode {


	private static final long serialVersionUID = 4054514488434283069L;

	/**
	 * @param clauses
	 *            - the query nodes to be and'ed
	 */
	public AqpNotQueryNode(List<QueryNode> clauses) {
		super(clauses);

		operator = "NOT";

		if ((clauses == null) || (clauses.size() < 2)) {
			throw new IllegalArgumentException(
					"NOT query must have at least two clauses");
		}
		
		QueryNode firstNode = clauses.get(0);
		applyModifier(clauses, Modifier.MOD_NOT);
		// reset the first node (if it was wrapped, ie not already having user specified MODIFIER)
		if (!firstNode.equals(clauses.get(0))) {
			clauses.set(0, new ModifierQueryNode(((ModifierQueryNode) clauses.get(0)).getChild(), Modifier.MOD_REQ));
		}
		
		set(clauses);

	}

}
