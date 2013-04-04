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

import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAnalyzerQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryparser.flexible.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;

/**
 * A {@link NonAnalyzedQueryNode} represents a query that was already
 * be processed by an analyzer. The child (typically only one) is the
 * result of a call to another analyzer.
 * 
 * @see AqpAnalyzerQueryNodeProcessor
 */
public class AqpAnalyzedQueryNode extends QueryNodeImpl {


	/**
	 * @param node
	 *          - query node
	 */
	public AqpAnalyzedQueryNode(QueryNode node) {
		allocate();
		setLeaf(false);
		this.add(node);
	}



	@Override
	public String toString() {
		return "<analyzed>" + this.getChild() + "</analyzed>";
	}

	@Override
	public AqpAnalyzedQueryNode cloneTree() throws CloneNotSupportedException {
		AqpAnalyzedQueryNode clone = (AqpAnalyzedQueryNode) super.cloneTree();
		// nothing to do here
		return clone;
	}


	@Override
	public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
		return this.getChildren().get(0).toQueryString(escapeSyntaxParser);
	}
	
	public QueryNode getChild() {
		return this.getChildren().get(0);
	}
}
