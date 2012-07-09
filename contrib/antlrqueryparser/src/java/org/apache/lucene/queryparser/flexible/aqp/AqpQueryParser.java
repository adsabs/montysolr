package org.apache.lucene.queryparser.flexible.aqp;

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
import java.util.ListIterator;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.QueryParserHelper;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.parser.StandardSyntaxParser;
import org.apache.lucene.queryparser.flexible.standard.processors.StandardQueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.aqp.AqpDebuggingQueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.aqp.AqpStandardQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.AqpSyntaxParser;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedback;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpStandardQueryConfigHandler;
import org.apache.lucene.search.Query;

/**
 * <p>
 * This class is a helper that enables users to easily use the Lucene query
 * parser written in EBNF grammar using ANTLR
 * </p>
 * <p>
 * To construct a Query object from a query string, use the
 * {@link #parse(String, String)} method:
 * <ul>
 * ANTLRQueryParser queryParserHelper = new ANTLRQueryParser(); <br/>
 * Query query = queryParserHelper.parse("a AND b", "defaultField");
 * </ul>
 * <p>
 * To change any configuration before parsing the query string do, for example:
 * <p/>
 * <ul>
 * // the query config handler returned by {@link StandardQueryParser} is a
 * {@link StandardQueryConfigHandler} <br/>
 * queryParserHelper.getQueryConfigHandler().setAnalyzer(new
 * WhitespaceAnalyzer());
 * </ul>
 * <p>
 * The syntax for query strings is as follows (copied from the old QueryParser
 * javadoc):
 * <ul>
 * A Query is a series of clauses. A clause may be prefixed by:
 * <ul>
 * <li>a plus (<code>+</code>) or a minus (<code>-</code>) sign, indicating that
 * the clause is required or prohibited respectively; or
 * <li>a term followed by a colon, indicating the field to be searched. This
 * enables one to construct queries which search multiple fields.
 * </ul>
 * 
 * A clause may be either:
 * <ul>
 * <li>a term, indicating all the documents that contain this term; or
 * <li>a nested query, enclosed in parentheses. Note that this may be used with
 * a <code>+</code>/<code>-</code> prefix to require any of a set of terms.
 * </ul>
 * 
 * Thus, in BNF, the query grammar is:
 * 
 * <pre>
 *   Query  ::= ( Clause )*
 *   Clause ::= [&quot;+&quot;, &quot;-&quot;] [&lt;TERM&gt; &quot;:&quot;] ( &lt;TERM&gt; | &quot;(&quot; Query &quot;)&quot; )
 * </pre>
 * 
 * <p>
 * Examples of appropriately formatted queries can be found in the <a
 * href="../../../../../../queryparsersyntax.html">query syntax
 * documentation</a>.
 * </p>
 * </ul>
 * <p>
 * The text parser used by this helper is a {@link StandardSyntaxParser}.
 * <p/>
 * <p>
 * The query node processor used by this helper is a
 * {@link StandardQueryNodeProcessorPipeline}.
 * <p/>
 * <p>
 * The builder used by this helper is a {@link StandardQueryTreeBuilder}.
 * <p/>
 * 
 * @see StandardQueryParser
 * @see StandardQueryConfigHandler
 * @see StandardSyntaxParser
 * @see StandardQueryNodeProcessorPipeline
 * @see StandardQueryTreeBuilder
 * 
 * 
 * TODO: add the constructor to the SQP
 * 
 * public StandardQueryParser(QueryConfigHandler config, SyntaxParser parser,
      QueryNodeProcessor processor, QueryBuilder builder) {
    super(config, parser,processor, builder);
  }
 */
public class AqpQueryParser extends StandardQueryParser {
	
	private boolean debugMode = false;
	private String syntaxName = null;
	
	
	public AqpQueryParser(QueryConfigHandler config, AqpSyntaxParser parser,
			QueryNodeProcessorPipeline processor, QueryTreeBuilder builder) {
		
		super(config, parser,processor, builder);
		syntaxName = parser.getClass().getName();
	}
	



	@Override
	public String toString() {
		return "<AqpQueryParser config=\"" + this.getQueryConfigHandler()
				+ "\" grammar=\"" + syntaxName
				+ "\"/>";
	}

	/*
	 * De/activates the debugging print of the processed query tree
	 */
	public void setDebug(boolean debug) throws InstantiationException, IllegalAccessException {
		if (debug) {
			QueryNodeProcessor qp = this.getQueryNodeProcessor();
			AqpDebuggingQueryNodeProcessorPipeline np = new AqpDebuggingQueryNodeProcessorPipeline(
					getQueryConfigHandler());
			
			List<QueryNodeProcessor> qnp = (List<QueryNodeProcessor>) qp;
			ListIterator<QueryNodeProcessor> it = qnp.listIterator();
			while (it.hasNext()) {
				np.add(it.next());
			}
			this.setQueryNodeProcessor(np);
			
			QueryBuilder qb = this.getQueryBuilder();
			QueryBuilder newBuilder = qb.getClass().newInstance();
			if (newBuilder instanceof AqpStandardQueryTreeBuilder) {
				((AqpStandardQueryTreeBuilder) newBuilder).debug(debug);
				((AqpStandardQueryTreeBuilder) newBuilder).init();
				this.setQueryBuilder(newBuilder);
			}
			
		}
		else {
			if (debugMode != debug) {
				QueryBuilder qb = this.getQueryBuilder();
				QueryBuilder newBuilder = qb.getClass().newInstance();
				this.setQueryBuilder(newBuilder);
			}
		}
		debugMode = debug;
	}
	
	public boolean getDebug() {
		return debugMode;
	}

	
	
	
	/**
	 * Overrides {@link QueryParserHelper#parse(String, String)} so it casts the
	 * return object to {@link Query}. For more reference about this method,
	 * check {@link QueryParserHelper#parse(String, String)}.
	 * 
	 * @param query
	 *            the query string
	 * @param defaultField
	 *            the default field used by the text parser
	 * 
	 * @return the object built from the query
	 * 
	 * @throws QueryNodeException
	 *             if something wrong happens along the three phases
	 */
	@Override
	public Query parse(String query, String defaultField)
			throws QueryNodeException {
		
		if(defaultField != null) {
			QueryConfigHandler cfg = getQueryConfigHandler();
			setDefaultField(defaultField);
		}
		try {
			return (Query) super.parse(query, defaultField);
		}
		catch (NestedParseException e) {
			throw new QueryNodeException(e);
		}

	}
	
	public String getDefaultField() {
		return getQueryConfigHandler().get(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_FIELD);
	}
	
	public void setDefaultField(String field) {
		getQueryConfigHandler().set(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_FIELD, field);
	}
	
	public Integer getDefaultProximity() {
		return getQueryConfigHandler().get(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_PROXIMITY);
	}
	
	public void setDefaultProximity(Integer value) {
		getQueryConfigHandler().set(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_PROXIMITY, value);
	}
	
	public Float getImplicitBoost() {
		return getQueryConfigHandler().get(AqpStandardQueryConfigHandler.ConfigurationKeys.IMPLICIT_BOOST);
	}
	
	public void setImplicitBoost(Float value) {
		getQueryConfigHandler().set(AqpStandardQueryConfigHandler.ConfigurationKeys.IMPLICIT_BOOST, value);
	}
	
	public AqpFeedback getFeedback() {
		return getQueryConfigHandler().get(AqpStandardQueryConfigHandler.ConfigurationKeys.FEEDBACK);
	}
	
	public void setFeedback(AqpFeedback feedbackInstance) {
		getQueryConfigHandler().set(AqpStandardQueryConfigHandler.ConfigurationKeys.FEEDBACK, feedbackInstance);
	}
	
	public Float getImplicitFuzzy() {
		return getQueryConfigHandler().get(AqpStandardQueryConfigHandler.ConfigurationKeys.IMPLICIT_FUZZY);
	}
	
	public void setImplicitFuzzy(Float value) {
		getQueryConfigHandler().set(AqpStandardQueryConfigHandler.ConfigurationKeys.IMPLICIT_FUZZY, value);
	}
	
	public Boolean getAllowSlowFuzzy() {
		return getQueryConfigHandler().get(AqpStandardQueryConfigHandler.ConfigurationKeys.ALLOW_SLOW_FUZZY);
	}
	
	public void setAllowSlowFuzzy(Boolean value) {
		getQueryConfigHandler().set(AqpStandardQueryConfigHandler.ConfigurationKeys.ALLOW_SLOW_FUZZY, value);
	}
	
}
