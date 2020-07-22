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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.TooManyListenersException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.QueryParserHelper;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.standard.config.FuzzyConfig;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.Operator;
import org.apache.lucene.queryparser.flexible.standard.parser.StandardSyntaxParser;
import org.apache.lucene.queryparser.flexible.standard.processors.StandardQueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.aqp.AqpSyntaxParser;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedback;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.util.AqpDebuggingQueryNodeProcessorPipeline;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.Query;

/**
 * <p>
 * This class is a helper that enables users to easily use the Lucene query
 * parser written in EBNF grammar using ANTLR
 * <p>
 * To construct a Query object from a query string, use the
 * {@link #parse(String, String)} method:
 * <pre>
 * ANTLRQueryParser queryParserHelper = new ANTLRQueryParser();
 * Query query = queryParserHelper.parse("a AND b", "defaultField");
 * </pre>
 * <p>
 * To change any configuration before parsing the query string do, for example:
 * <code>
 * // the query config handler returned by {@link StandardQueryParser} is a
 * {@link StandardQueryConfigHandler}
 * queryParserHelper.getQueryConfigHandler().setAnalyzer(new
 * WhitespaceAnalyzer());
 * </code>
 * <p>
 * The syntax for query strings is as follows (copied from the old QueryParser
 * javadoc):
 * <p>
 * A Query is a series of clauses. A clause may be prefixed by:
 * <p>
 * a plus (<code>+</code>) or a minus (<code>-</code>) sign, indicating that
 * the clause is required or prohibited respectively; or
 * <p>
 * a term followed by a colon, indicating the field to be searched. This
 * enables one to construct queries which search multiple fields.
 * 
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
 * <p>
 * The text parser used by this helper is a {@link StandardSyntaxParser}.
 * <p>
 * The query node processor used by this helper is a
 * {@link StandardQueryNodeProcessorPipeline}.
 * <p>
 * The builder used by this helper is a {@link StandardQueryTreeBuilder}.
 * <p>
 * 
 * @see StandardQueryParser
 * @see StandardQueryConfigHandler
 * @see StandardSyntaxParser
 * @see StandardQueryNodeProcessorPipeline
 * @see StandardQueryTreeBuilder
 * 
 * 
 *      TODO: add the constructor to the SQP and remove the duplicated code
 * 
 *      public StandardQueryParser(QueryConfigHandler config, SyntaxParser
 *      parser, QueryNodeProcessor processor, QueryBuilder builder) {
 *      super(config, parser,processor, builder); }
 */
public class AqpQueryParser extends QueryParserHelper {

  private boolean debugMode = false;
  private String syntaxName = null;

  public AqpQueryParser(QueryConfigHandler config, AqpSyntaxParser parser,
      QueryNodeProcessorPipeline processor, QueryTreeBuilder builder) {

    super(config, parser, processor, builder);
    syntaxName = parser.getClass().getName();
  }

  @Override
  public String toString() {
    return "<AqpQueryParser config=\"" + this.getQueryConfigHandler()
        + "\" grammar=\"" + syntaxName + "\"/>";
  }

  /**
   * De/activates the debugging output of the query parser
   * 
   * It works by wrapping the processor pipeline into a debugging
   * class and by calling setDebug on the underlying builder.
   * 
   * @see AqpDebuggingQueryNodeProcessorPipeline
   * @see AqpQueryTreeBuilder
   */
  @SuppressWarnings("unchecked")
  public void setDebug(boolean debug) throws InstantiationException,
      IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
  	
  	if (debugMode != debug) {
  		
  		QueryNodeProcessorPipeline processor = (QueryNodeProcessorPipeline) this.getQueryNodeProcessor();
  		QueryBuilder builder = this.getQueryBuilder();
  		
  		QueryNodeProcessorPipeline newPipeline;
  		
  		QueryConfigHandler configHandler = this.getQueryConfigHandler();
  		
	    if (debug) {
	      newPipeline = new AqpDebuggingQueryNodeProcessorPipeline(
	      		this.getQueryConfigHandler(), processor.getClass());
	    }
	    else {
	    	// can't use the simple form because parser pipelines may be using config to adjust themselves
	    	// newPipeline = clazz.newInstance();
	    	
	    	Class<? extends QueryNodeProcessorPipeline> clazz = ((AqpDebuggingQueryNodeProcessorPipeline) processor)
    								.getOriginalProcessorClass();
	    	Constructor<? extends QueryNodeProcessorPipeline> constructor = clazz.getConstructor(QueryConfigHandler.class);
	    	newPipeline = constructor.newInstance(new Object[]{configHandler});
	    }
	    

      List<QueryNodeProcessor> listOfProcessors = (List<QueryNodeProcessor>) processor;
      ListIterator<QueryNodeProcessor> it = listOfProcessors.listIterator();
      while (it.hasNext()) {
        newPipeline.add(it.next());
      }
      this.setQueryNodeProcessor(newPipeline);
	
	      
      QueryBuilder newBuilder = builder.getClass().newInstance();
      if (newBuilder instanceof AqpQueryTreeBuilder) {
        ((AqpQueryTreeBuilder) newBuilder).setDebug(debug);
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

    if (defaultField != null) {
      setDefaultField(defaultField);
    }
    try {
      return (Query) super.parse(query, defaultField);
    } catch (NestedParseException e) {
      throw new QueryNodeException(e);
    }

  }

  public String getDefaultField() {
    return getQueryConfigHandler().get(
        AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_FIELD);
  }

  public void setDefaultField(String field) {
    getQueryConfigHandler().set(
        AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_FIELD, field);
  }

  public Integer getDefaultProximity() {
    return getQueryConfigHandler().get(
        AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_PROXIMITY);
  }

  public void setDefaultProximity(Integer value) {
    getQueryConfigHandler().set(
        AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_PROXIMITY,
        value);
  }

  public Float getImplicitBoost() {
    return getQueryConfigHandler().get(
        AqpStandardQueryConfigHandler.ConfigurationKeys.IMPLICIT_BOOST);
  }

  public void setImplicitBoost(Float value) {
    getQueryConfigHandler().set(
        AqpStandardQueryConfigHandler.ConfigurationKeys.IMPLICIT_BOOST, value);
  }

  public AqpFeedback getFeedback() {
    return getQueryConfigHandler().get(
        AqpStandardQueryConfigHandler.ConfigurationKeys.FEEDBACK);
  }

  public void setFeedback(AqpFeedback feedbackInstance) {
    getQueryConfigHandler().set(
        AqpStandardQueryConfigHandler.ConfigurationKeys.FEEDBACK,
        feedbackInstance);
  }

  public Float getImplicitFuzzy() {
    return getQueryConfigHandler().get(
        AqpStandardQueryConfigHandler.ConfigurationKeys.IMPLICIT_FUZZY);
  }

  public void setImplicitFuzzy(Float value) {
    getQueryConfigHandler().set(
        AqpStandardQueryConfigHandler.ConfigurationKeys.IMPLICIT_FUZZY, value);
  }

  public Boolean getAllowSlowFuzzy() {
    return getQueryConfigHandler().get(
        AqpStandardQueryConfigHandler.ConfigurationKeys.ALLOW_SLOW_FUZZY);
  }

  public void setAllowSlowFuzzy(Boolean value) {
    getQueryConfigHandler()
        .set(AqpStandardQueryConfigHandler.ConfigurationKeys.ALLOW_SLOW_FUZZY,
            value);
  }

  /********************************************************************
   * Everything below is simpy copy of the StandardQueryParser *
   *******************************************************************/

  /**
   * Gets implicit operator setting, which will be either {@link Operator#AND}
   * or {@link Operator#OR}.
   */
  public StandardQueryConfigHandler.Operator getDefaultOperator() {
  	return getQueryConfigHandler().get(ConfigurationKeys.DEFAULT_OPERATOR);
    
  }

  /**
   * Sets the boolean operator of the QueryParser. In default mode (
   * {@link Operator#OR}) terms without any modifiers are considered optional:
   * for example <code>capital of Hungary</code> is equal to
   * <code>capital OR of OR Hungary</code>.
   * 
   * <p>
   * 
   * In {@link Operator#AND} mode terms are considered to be in conjunction: the
   * above mentioned query is parsed as <code>capital AND of AND Hungary</code>
   */
  public void setDefaultOperator(StandardQueryConfigHandler.Operator operator) {
    getQueryConfigHandler().set(ConfigurationKeys.DEFAULT_OPERATOR, operator);
  }


  /**
   * Set to <code>true</code> to allow leading wildcard characters.
   * <p>
   * When set, <code>*</code> or <code>?</code> are allowed as the first
   * character of a PrefixQuery and WildcardQuery. Note that this can produce
   * very slow queries on big indexes.
   * <p>
   * Default: false.
   */
  public void setAllowLeadingWildcard(boolean allowLeadingWildcard) {
    getQueryConfigHandler().set(ConfigurationKeys.ALLOW_LEADING_WILDCARD,
        allowLeadingWildcard);
  }

  /**
   * Set to <code>true</code> to enable position increments in result query.
   * <p>
   * When set, result phrase and multi-phrase queries will be aware of position
   * increments. Useful when e.g. a StopFilter increases the position increment
   * of the token that follows an omitted token.
   * <p>
   * Default: false.
   */
  public void setEnablePositionIncrements(boolean enabled) {
    getQueryConfigHandler().set(ConfigurationKeys.ENABLE_POSITION_INCREMENTS,
        enabled);
  }

  /**
   * @see #setEnablePositionIncrements(boolean)
   */
  public boolean getEnablePositionIncrements() {
    Boolean enablePositionsIncrements = getQueryConfigHandler().get(
        ConfigurationKeys.ENABLE_POSITION_INCREMENTS);

    if (enablePositionsIncrements == null) {
      return false;

    } else {
      return enablePositionsIncrements;
    }

  }

  /**
   * By default, it uses
   * {@link MultiTermQuery#CONSTANT_SCORE_BOOLEAN_REWRITE} when creating a
   * prefix, wildcard and range queries. This implementation is generally
   * preferable because it a) Runs faster b) Does not have the scarcity of terms
   * unduly influence score c) avoids any {@link TooManyListenersException}
   * exception. However, if your application really needs to use the
   * old-fashioned boolean queries expansion rewriting and the above points are
   * not relevant then use this change the rewrite method.
   */
  public void setMultiTermRewriteMethod(MultiTermQuery.RewriteMethod method) {
    getQueryConfigHandler().set(ConfigurationKeys.MULTI_TERM_REWRITE_METHOD,
        method);
  }

  /**
   * @see #setMultiTermRewriteMethod(org.apache.lucene.search.MultiTermQuery.RewriteMethod)
   */
  public MultiTermQuery.RewriteMethod getMultiTermRewriteMethod() {
    return getQueryConfigHandler().get(
        ConfigurationKeys.MULTI_TERM_REWRITE_METHOD);
  }

  /**
   * Set the fields a query should be expanded to when the field is
   * <code>null</code>
   * 
   * @param fields
   *          the fields used to expand the query
   */
  public void setMultiFields(CharSequence[] fields) {

    if (fields == null) {
      fields = new CharSequence[0];
    }

    getQueryConfigHandler().set(ConfigurationKeys.MULTI_FIELDS, fields);

  }

  /**
   * Returns the fields used to expand the query when the field for a certain
   * query is <code>null</code>
   * 
   * @param fields
   *          the fields used to expand the query
   */
  public void getMultiFields(CharSequence[] fields) {
    getQueryConfigHandler().get(ConfigurationKeys.MULTI_FIELDS);
  }

  /**
   * Set the prefix length for fuzzy queries. Default is 0.
   * 
   * @param fuzzyPrefixLength
   *          The fuzzyPrefixLength to set.
   */
  public void setFuzzyPrefixLength(int fuzzyPrefixLength) {
    QueryConfigHandler config = getQueryConfigHandler();
    FuzzyConfig fuzzyConfig = config.get(ConfigurationKeys.FUZZY_CONFIG);

    if (fuzzyConfig == null) {
      fuzzyConfig = new FuzzyConfig();
      config.set(ConfigurationKeys.FUZZY_CONFIG, fuzzyConfig);
    }

    fuzzyConfig.setPrefixLength(fuzzyPrefixLength);

  }


  /**
   * Set locale used by date range parsing.
   */
  public void setLocale(Locale locale) {
    getQueryConfigHandler().set(ConfigurationKeys.LOCALE, locale);
  }

  /**
   * Returns current locale, allowing access by subclasses.
   */
  public Locale getLocale() {
    return getQueryConfigHandler().get(ConfigurationKeys.LOCALE);
  }

  /**
   * Sets the default slop for phrases. If zero, then exact phrase matches are
   * required. Default value is zero.
   * 
   * @deprecated renamed to {@link #setPhraseSlop(int)}
   */
  @Deprecated
  public void setDefaultPhraseSlop(int defaultPhraseSlop) {
    getQueryConfigHandler().set(ConfigurationKeys.PHRASE_SLOP,
        defaultPhraseSlop);
  }

  /**
   * Sets the default slop for phrases. If zero, then exact phrase matches are
   * required. Default value is zero.
   */
  public void setPhraseSlop(int defaultPhraseSlop) {
    getQueryConfigHandler().set(ConfigurationKeys.PHRASE_SLOP,
        defaultPhraseSlop);
  }

  public void setAnalyzer(Analyzer analyzer) {
    getQueryConfigHandler().set(ConfigurationKeys.ANALYZER, analyzer);
  }

  public Analyzer getAnalyzer() {
    return getQueryConfigHandler().get(ConfigurationKeys.ANALYZER);
  }

  /**
   * @see #setAllowLeadingWildcard(boolean)
   */
  public boolean getAllowLeadingWildcard() {
    Boolean allowLeadingWildcard = getQueryConfigHandler().get(
        ConfigurationKeys.ALLOW_LEADING_WILDCARD);

    if (allowLeadingWildcard == null) {
      return false;

    } else {
      return allowLeadingWildcard;
    }
  }

  /**
   * Get the minimal similarity for fuzzy queries.
   */
  public float getFuzzyMinSim() {
    FuzzyConfig fuzzyConfig = getQueryConfigHandler().get(
        ConfigurationKeys.FUZZY_CONFIG);

    if (fuzzyConfig == null) {
      return FuzzyQuery.defaultMinSimilarity;
    } else {
      return fuzzyConfig.getMinSimilarity();
    }
  }

  /**
   * Get the prefix length for fuzzy queries.
   * 
   * @return Returns the fuzzyPrefixLength.
   */
  public int getFuzzyPrefixLength() {
    FuzzyConfig fuzzyConfig = getQueryConfigHandler().get(
        ConfigurationKeys.FUZZY_CONFIG);

    if (fuzzyConfig == null) {
      return FuzzyQuery.defaultPrefixLength;
    } else {
      return fuzzyConfig.getPrefixLength();
    }
  }

  /**
   * Gets the default slop for phrases.
   */
  public int getPhraseSlop() {
    Integer phraseSlop = getQueryConfigHandler().get(
        ConfigurationKeys.PHRASE_SLOP);

    if (phraseSlop == null) {
      return 0;

    } else {
      return phraseSlop;
    }
  }

  /**
   * Set the minimum similarity for fuzzy queries. Default is defined on
   * {@link FuzzyQuery#defaultMinSimilarity}.
   */
  public void setFuzzyMinSim(float fuzzyMinSim) {
    QueryConfigHandler config = getQueryConfigHandler();
    FuzzyConfig fuzzyConfig = config.get(ConfigurationKeys.FUZZY_CONFIG);

    if (fuzzyConfig == null) {
      fuzzyConfig = new FuzzyConfig();
      config.set(ConfigurationKeys.FUZZY_CONFIG, fuzzyConfig);
    }

    fuzzyConfig.setMinSimilarity(fuzzyMinSim);
  }

  /**
   * Sets the boost used for each field.
   * 
   * @param boosts
   *          a collection that maps a field to its boost
   */
  public void setFieldsBoost(Map<String, Float> boosts) {
    getQueryConfigHandler().set(ConfigurationKeys.FIELD_BOOST_MAP, boosts);
  }

  /**
   * Returns the field to boost map used to set boost for each field.
   * 
   * @return the field to boost map
   */
  public Map<String, Float> getFieldsBoost() {
    return getQueryConfigHandler().get(ConfigurationKeys.FIELD_BOOST_MAP);
  }

  /**
   * Sets the default {@link Resolution} used for certain field when no
   * {@link Resolution} is defined for this field.
   * 
   * @param dateResolution
   *          the default {@link Resolution}
   */
  public void setDateResolution(DateTools.Resolution dateResolution) {
    getQueryConfigHandler().set(ConfigurationKeys.DATE_RESOLUTION,
        dateResolution);
  }

  /**
   * Returns the default {@link Resolution} used for certain field when no
   * {@link Resolution} is defined for this field.
   * 
   * @return the default {@link Resolution}
   */
  public DateTools.Resolution getDateResolution() {
    return getQueryConfigHandler().get(ConfigurationKeys.DATE_RESOLUTION);
  }

  /**
   * Sets the {@link Resolution} used for each field
   * 
   * @param dateRes
   *          a collection that maps a field to its {@link Resolution}
   * 
   * @deprecated this method was renamed to {@link #setDateResolutionMap(Map)}
   */
  @Deprecated
  public void setDateResolution(Map<CharSequence, DateTools.Resolution> dateRes) {
    setDateResolutionMap(dateRes);
  }

  /**
   * Returns the field to {@link Resolution} map used to normalize each date
   * field.
   * 
   * @return the field to {@link Resolution} map
   */
  public Map<CharSequence, DateTools.Resolution> getDateResolutionMap() {
    return getQueryConfigHandler().get(
        ConfigurationKeys.FIELD_DATE_RESOLUTION_MAP);
  }

  /**
   * Sets the {@link Resolution} used for each field
   * 
   * @param dateRes
   *          a collection that maps a field to its {@link Resolution}
   */
  public void setDateResolutionMap(
      Map<CharSequence, DateTools.Resolution> dateRes) {
    getQueryConfigHandler().set(ConfigurationKeys.FIELD_DATE_RESOLUTION_MAP,
        dateRes);
  }
  
  
  /**
   * Returns the string value of the NAMED_PARAMETER if set or
   * null if nothing is there
   * 
   * @return the NAMED_PARAMETER value for the given key
   */
  public String getNamedParameter(String key) {
    Map<String, String> map = getQueryConfigHandler().get(
    		AqpStandardQueryConfigHandler.ConfigurationKeys.NAMED_PARAMETER);
    if (map.containsKey(key)) {
    	return map.get(key);
    }
    return null;
  }

  /**
   * Sets NAMED_PARAMETER
   * 
   * @param key
   *          name of the parameter; the value will depend on what 
   *          processors are using and what they accept
   * @param value
   *          string value
   */
  public void setNamedParameter(String key, String value) {
  	Map<String, String> map = getQueryConfigHandler().get(
    		AqpStandardQueryConfigHandler.ConfigurationKeys.NAMED_PARAMETER);
  	map.put(key, value);
  }
}
