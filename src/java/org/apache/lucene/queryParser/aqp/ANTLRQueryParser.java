package org.apache.lucene.queryParser.aqp;

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

import java.util.Locale;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.aqp.config.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryParser.aqp.parser.ANTLRSyntaxParser;
import org.apache.lucene.queryParser.aqp.processors.ANTLRQueryNodeProcessorPipeline;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.QueryParserHelper;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.standard.builders.StandardQueryTreeBuilder;
import org.apache.lucene.queryParser.standard.config.AnalyzerAttribute;
import org.apache.lucene.queryParser.standard.config.FieldBoostMapAttribute;
import org.apache.lucene.queryParser.standard.config.LocaleAttribute;
import org.apache.lucene.queryParser.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryParser.standard.parser.StandardSyntaxParser;
import org.apache.lucene.queryParser.standard.processors.StandardQueryNodeProcessorPipeline;
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
 */
public class ANTLRQueryParser extends QueryParserHelper {

  /**
   * Constructs a {@link StandardQueryParser} object.
   */
  public ANTLRQueryParser() {
    super(new AqpStandardQueryConfigHandler(), 
    	new ANTLRSyntaxParser(),
        new ANTLRQueryNodeProcessorPipeline(null),
        new StandardQueryTreeBuilder());
  }

  /**
   * Constructs a {@link StandardQueryParser} object and sets an
   * {@link Analyzer} to it. The same as:
   *
   * <ul>
   * StandardQueryParser qp = new StandardQueryParser();
   * qp.getQueryConfigHandler().setAnalyzer(analyzer);
   * </ul>
   *
   * @param analyzer
   *          the analyzer to be used by this query parser helper
   */
  public ANTLRQueryParser(Analyzer analyzer) {
    this();

    this.setAnalyzer(analyzer);
  }

  @Override
  public String toString(){
    return "<ANTLRQueryParser config=\"" + this.getQueryConfigHandler() + "\"/>";
  }





  public void setAnalyzer(Analyzer analyzer) {
    AnalyzerAttribute attr = getQueryConfigHandler().getAttribute(AnalyzerAttribute.class);
    attr.setAnalyzer(analyzer);
  }

  public Analyzer getAnalyzer() {
    QueryConfigHandler config = this.getQueryConfigHandler();

    if ( config.hasAttribute(AnalyzerAttribute.class)) {
      AnalyzerAttribute attr = config.getAttribute(AnalyzerAttribute.class);
      return attr.getAnalyzer();
    }

    return null;
  }


  public void setFieldsBoost(Map<String, Float> boosts) {
    FieldBoostMapAttribute attr = getQueryConfigHandler().addAttribute(FieldBoostMapAttribute.class);
    attr.setFieldBoostMap(boosts);
  }

  /**
   * Set locale used by date range parsing.
   */
  public void setLocale(Locale locale) {
    LocaleAttribute attr = getQueryConfigHandler().addAttribute(LocaleAttribute.class);
    attr.setLocale(locale);
  }

  /**
   * Returns current locale, allowing access by subclasses.
   */
  public Locale getLocale() {
    LocaleAttribute attr = getQueryConfigHandler().addAttribute(LocaleAttribute.class);
    return attr.getLocale();
  }


  /**
   * Overrides {@link QueryParserHelper#parse(String, String)} so it casts the
   * return object to {@link Query}. For more reference about this method, check
   * {@link QueryParserHelper#parse(String, String)}.
   *
   * @param query
   *          the query string
   * @param defaultField
   *          the default field used by the text parser
   *
   * @return the object built from the query
   *
   * @throws QueryNodeException
   *           if something wrong happens along the three phases
   */
  @Override
  public Query parse(String query, String defaultField)
      throws QueryNodeException {

    return (Query) super.parse(query, defaultField);

  }


}
