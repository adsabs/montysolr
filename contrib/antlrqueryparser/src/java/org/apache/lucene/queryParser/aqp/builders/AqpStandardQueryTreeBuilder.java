package org.apache.lucene.queryParser.aqp.builders;

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

import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.builders.QueryBuilder;
import org.apache.lucene.queryParser.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryParser.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryParser.core.nodes.BoostQueryNode;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryParser.core.nodes.GroupQueryNode;
import org.apache.lucene.queryParser.core.nodes.MatchAllDocsQueryNode;
import org.apache.lucene.queryParser.core.nodes.MatchNoDocsQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.SlopQueryNode;
import org.apache.lucene.queryParser.core.nodes.TokenizedPhraseQueryNode;
import org.apache.lucene.queryParser.standard.builders.BooleanQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.BoostQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.FuzzyQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.GroupQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.MatchAllDocsQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.MatchNoDocsQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.ModifierQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.MultiPhraseQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.PhraseQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.PrefixWildcardQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.RangeQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.SlopQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.StandardBooleanQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.StandardQueryBuilder;
import org.apache.lucene.queryParser.standard.builders.WildcardQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.nodes.MultiPhraseQueryNode;
import org.apache.lucene.queryParser.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryParser.standard.nodes.RangeQueryNode;
import org.apache.lucene.queryParser.standard.nodes.StandardBooleanQueryNode;
import org.apache.lucene.queryParser.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryParser.standard.processors.StandardQueryNodeProcessorPipeline;
import org.apache.lucene.search.Query;

/**
 * This query tree builder only defines the necessary map to build a
 * {@link Query} tree object. It should be used to generate a {@link Query} tree
 * object from a query node tree processed by a
 * {@link AqpStandardQueryNodeProcessorPipeline}. <br/>
 * 
 * @see QueryTreeBuilder
 * @see StandardQueryNodeProcessorPipeline
 */
public class AqpStandardQueryTreeBuilder extends QueryTreeBuilder implements
		StandardQueryBuilder {

	private boolean debug = false;

	public AqpStandardQueryTreeBuilder(boolean debug) {
		this.debug = debug;
		init();
	}

	public AqpStandardQueryTreeBuilder() {
		init();
	}

	private void init() {
		setBuilder(GroupQueryNode.class, new GroupQueryNodeBuilder());
		setBuilder(FieldQueryNode.class, new AqpFieldQueryNodeBuilder());
		setBuilder(BooleanQueryNode.class, new BooleanQueryNodeBuilder());
		setBuilder(FuzzyQueryNode.class, new FuzzyQueryNodeBuilder());
		setBuilder(BoostQueryNode.class, new BoostQueryNodeBuilder());
		setBuilder(ModifierQueryNode.class, new ModifierQueryNodeBuilder());
		setBuilder(WildcardQueryNode.class, new WildcardQueryNodeBuilder());
		setBuilder(TokenizedPhraseQueryNode.class, new PhraseQueryNodeBuilder());
		setBuilder(MatchNoDocsQueryNode.class,
				new MatchNoDocsQueryNodeBuilder());
		setBuilder(PrefixWildcardQueryNode.class,
				new PrefixWildcardQueryNodeBuilder());
		setBuilder(RangeQueryNode.class, new RangeQueryNodeBuilder());
		setBuilder(SlopQueryNode.class, new SlopQueryNodeBuilder());
		setBuilder(StandardBooleanQueryNode.class,
				new StandardBooleanQueryNodeBuilder());
		setBuilder(MultiPhraseQueryNode.class,
				new MultiPhraseQueryNodeBuilder());
		setBuilder(MatchAllDocsQueryNode.class,
				new MatchAllDocsQueryNodeBuilder());
	}

	@Override
	public Query build(QueryNode queryNode) throws QueryNodeException {
		return (Query) super.build(queryNode);
	}

	public void debug(boolean debug) {
		this.debug = debug;
	}

	public void setBuilder(Class<? extends QueryNode> queryNodeClass,
			QueryBuilder builder) {
		if (this.debug) {
			super.setBuilder(queryNodeClass, new DebuggingNodeBuilder(
					queryNodeClass, builder));
		} else {
			super.setBuilder(queryNodeClass, builder);
		}
	}

	class DebuggingNodeBuilder implements QueryBuilder {
		private Class<? extends QueryNode> clazz = null;
		private QueryBuilder realBuilder = null;

		DebuggingNodeBuilder(Class<? extends QueryNode> queryNodeClass,
				QueryBuilder builder) {
			clazz = queryNodeClass;
			realBuilder = builder;
		}

		public Object build(QueryNode queryNode) throws QueryNodeException {
			System.out.println("     building");
			System.out.println("--------------------------------------------");
			System.out.println(clazz.getName());
			System.out.println(realBuilder.getClass());
			System.out.println("--------------------------------------------");
			System.out.println(queryNode.toString());

			Object result = realBuilder.build(queryNode);
			System.out.println(((Query) result).toString());
			System.out.println("--------------------------------------------");
			System.out.println("--------------------------------------------");
			return result;
		}

	};

}
