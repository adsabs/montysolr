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

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.QueryParserHelper;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.QueryParserUtil;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.Operator;
import org.apache.lucene.queryparser.flexible.standard.processors.StandardQueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardLuceneParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.util.LuceneTestCase;

/**
 * This test case is a copy of the core Lucene query parser test, it was adapted
 * to use new QueryParserHelper instead of the old query parser.
 * 
 * Tests QueryParser.
 */
public class AqpTestAbstractCase extends LuceneTestCase {

  public int originalMaxClauses;
  public boolean debugParser = false;
  protected String grammarName = "StandardLuceneGrammar";
  protected int noFailures = 0;

  public void setUp() throws Exception {
    super.setUp();
    originalMaxClauses = BooleanQuery.getMaxClauseCount();
  }

  public static void fail(String message) {
    System.err.println(message);
    LuceneTestCase.fail(message);
  }

  public void setDebug(boolean d) {
    debugParser = d;
  }

  public void setGrammarName(String name) {
    grammarName = name;
  }

  public String getGrammarName() {
    return grammarName;
  }

  public AqpQueryParser getParser(Analyzer a) throws Exception {
    if (a == null)
      a = new SimpleAnalyzer(TEST_VERSION_CURRENT);
    AqpQueryParser qp = getParser();
    qp.setAnalyzer(a);
    return qp;

  }

  public QueryParserHelper getParser(Analyzer a, boolean standard)
      throws Exception {
    if (standard) {
      StandardQueryParser sp = new StandardQueryParser(a);
      if (this.debugParser) {
        sp.setQueryNodeProcessor(new DebuggingQueryNodeProcessorPipeline(sp
            .getQueryConfigHandler()));
      }
      return sp;
    } else {
      return getParser(a);
    }
  }

  public AqpQueryParser getParser() throws Exception {
    AqpQueryParser qp = AqpStandardLuceneParser.init(getGrammarName());
    qp.setDebug(this.debugParser);
    return qp;
  }

  public Query getQuery(String query, Analyzer a) throws Exception {
    return getParser(a).parse(query, "field");
  }

  public Query getQueryAllowLeadingWildcard(String query, Analyzer a)
      throws Exception {
    AqpQueryParser parser = getParser(a);
    parser.setAllowLeadingWildcard(true);
    return parser.parse(query, "field");
  }

  public Query assertQueryEquals(String query, Analyzer a, String result)
      throws Exception {
    Query q = getQuery(query, a);
    String s = q.toString("field");
    if (!s.equals(result)) {
      debugFail(q.toString(), result, s);
    }
    return q;
  }

  public Query assertQueryEquals(String query, Analyzer a, String result,
      Class<?> clazz) throws Exception {
    Query q = assertQueryEquals(query, a, result);
    if (!q.getClass().isAssignableFrom(clazz)) {
      debugFail(q.toString(), result,
          "Query is not: " + clazz + " but: " + q.getClass());
    }
    return q;
  }

  public void assertQueryEqualsAllowLeadingWildcard(String query, Analyzer a,
      String result) throws Exception {
    Query q = getQueryAllowLeadingWildcard(query, a);
    String s = q.toString("field");
    if (!s.equals(result)) {
      fail("Query /" + query + "/ yielded /" + s + "/, expecting /" + result
          + "/");
    }
  }

  public void assertQueryEquals(AqpQueryParser qp, String field, String query,
      String result) throws Exception {
    Query q = qp.parse(query, field);
    String s = q.toString(field);
    if (!s.equals(result)) {
      fail("Query /" + query + "/ yielded /" + s + "/, expecting /" + result
          + "/");
    }
  }

  public void assertEscapedQueryEquals(String query, Analyzer a, String result)
      throws Exception {
    String escapedQuery = QueryParserUtil.escape(query);
    if (!escapedQuery.equals(result)) {
      fail("Query /" + query + "/ yielded /" + escapedQuery + "/, expecting /"
          + result + "/");
    }
  }

  public void assertWildcardQueryEquals(String query, boolean lowercase,
      String result, boolean allowLeadingWildcard) throws Exception {
    AqpQueryParser qp = getParser(null);
    qp.setLowercaseExpandedTerms(lowercase);
    qp.setAllowLeadingWildcard(allowLeadingWildcard);
    Query q = qp.parse(query, "field");
    String s = q.toString("field");
    if (!s.equals(result)) {
      fail("WildcardQuery /" + query + "/ yielded /" + s + "/, expecting /"
          + result + "/");
    }
  }

  public void assertWildcardQueryEquals(String query, boolean lowercase,
      String result) throws Exception {
    assertWildcardQueryEquals(query, lowercase, result, false);
  }

  public void assertWildcardQueryEquals(String query, String result)
      throws Exception {
    AqpQueryParser qp = getParser(null);
    Query q = qp.parse(query, "field");
    String s = q.toString("field");
    if (!s.equals(result)) {
      fail("WildcardQuery /" + query + "/ yielded /" + s + "/, expecting /"
          + result + "/");
    }
  }

  public Query getQueryDOA(String query, Analyzer a) throws Exception {
    if (a == null)
      a = new SimpleAnalyzer(TEST_VERSION_CURRENT);
    AqpQueryParser qp = getParser();
    qp.setAnalyzer(a);
    qp.setDefaultOperator(Operator.AND);

    return qp.parse(query, "field");

  }

  public void assertQueryEqualsDOA(String query, Analyzer a, String result)
      throws Exception {
    Query q = getQueryDOA(query, a);
    String s = q.toString("field");
    if (!s.equals(result)) {
      fail("Query /" + query + "/ yielded /" + s + "/, expecting /" + result
          + "/");
    }
  }

  /** for testing DateTools support */
  private String getDate(String s, DateTools.Resolution resolution)
      throws Exception {
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
    return getDate(df.parse(s), resolution);
  }

  /** for testing DateTools support */
  private String getDate(Date d, DateTools.Resolution resolution) {
    return DateTools.dateToString(d, resolution);
  }

  public String escapeDateString(String s) {
    if (s.contains(" ")) {
      return "\"" + s + "\"";
    } else {
      return s;
    }
  }

  public String getLocalizedDate(int year, int month, int day) {
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
    Calendar calendar = new GregorianCalendar();
    calendar.clear();
    calendar.set(year, month, day);
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);
    return df.format(calendar.getTime());
  }

  public void assertDateRangeQueryEquals(AqpQueryParser qp, String field,
      String startDate, String endDate, Date endDateInclusive,
      DateTools.Resolution resolution) throws Exception {
    assertQueryEquals(
        qp,
        field,
        field + ":[" + escapeDateString(startDate) + " TO "
            + escapeDateString(endDate) + "]",
        "[" + getDate(startDate, resolution) + " TO "
            + getDate(endDateInclusive, resolution) + "]");
    assertQueryEquals(
        qp,
        field,
        field + ":{" + escapeDateString(startDate) + " TO "
            + escapeDateString(endDate) + "}",
        "{" + getDate(startDate, resolution) + " TO "
            + getDate(endDate, resolution) + "}");
  }

  public void assertHits(int expected, String query, IndexSearcher is)
      throws IOException, QueryNodeException {
    AqpQueryParser qp;
    try {
      qp = getParser();
    } catch (Exception e) {
      e.printStackTrace();
      throw new QueryNodeException(e);
    }
    qp.setAnalyzer(new WhitespaceAnalyzer(TEST_VERSION_CURRENT));
    // qp.setLocale(Locale.ENGLISH);
    qp.setDateResolution(DateTools.Resolution.DAY);

    Query q = qp.parse(query, "date");
    ScoreDoc[] hits = is.search(q, null, 1000).scoreDocs;
    assertEquals(expected, hits.length);
  }

  public void assertQueryNodeException(String queryString) throws Exception {
    try {
      getQuery(queryString, null);
    } catch (QueryNodeException expected) {
      return;
    }
    debugFail("ParseException expected, not thrown");
  }

  public void addDateDoc(String content, int year, int month, int day,
      int hour, int minute, int second, IndexWriter iw) throws IOException {
    Document d = new Document();
    d.add(new Field("f", content, TextField.TYPE_STORED));
    Calendar cal = Calendar.getInstance(Locale.ROOT);
    cal.set(year, month - 1, day, hour, minute, second);
    d.add(new Field("date", getDate(cal.getTime(), DateTools.Resolution.DAY),
        StringField.TYPE_NOT_STORED));
    iw.addDocument(d);
  }

  public void assertQueryMatch(AqpQueryParser qp, String queryString,
      String defaultField, String expectedResult) throws Exception {

    try {
      Query query = qp.parse(queryString, defaultField);
      String queryParsed = query.toString();

      if (!queryParsed.equals(expectedResult)) {

        if (this.debugParser) {

          System.out.println("query:\t\t" + queryString);

          if (qp.getDebug() != true) { // it already printed debug
            qp.setDebug(true);
            qp.parse(queryString, defaultField);
            qp.setDebug(false);
          }
          System.out.println("");
          System.out.println("query:\t\t" + queryString);
          System.out.println("result:\t\t" + queryParsed);

        }

        String msg = "Query /" + queryString + "/ with field: " + defaultField
            + "/ yielded /" + queryParsed + "/, expecting /" + expectedResult
            + "/";

        debugFail(queryString, expectedResult, queryParsed);

      } else {
        if (this.debugParser) {
          System.out.println("OK \"" + queryString + "\" --->  " + queryParsed);
        }
      }
    } catch (Exception e) {
      if (this.debugParser) {
        System.err.println(queryString);
        e.printStackTrace();
      } else {
        throw e;
      }
    }

  }

  public void debugFail(String message) {
    if (this.debugParser) {
      System.err.println("Number of failures: " + ++noFailures);
      System.err.println(message);
    } else {
      fail(message);
    }
  }

  public void debugFail(String query, String expected, String actual) {
    if (this.debugParser) {
      System.err.println("Number of failures: " + ++noFailures);
      System.err.println("query:/" + query + "/ expected:/" + expected
          + "/ actual:/" + actual + "/");
    } else {
      assertEquals(expected, actual);
    }
  }

  @Override
  public void tearDown() throws Exception {
    BooleanQuery.setMaxClauseCount(originalMaxClauses);
    super.tearDown();
  }

  class DebuggingQueryNodeProcessorPipeline extends
      StandardQueryNodeProcessorPipeline {
    DebuggingQueryNodeProcessorPipeline(QueryConfigHandler queryConfig) {
      super(queryConfig);
    }

    public QueryNode process(QueryNode queryTree) throws QueryNodeException {
      String oldVal = null;
      String newVal = null;

      oldVal = queryTree.toString();
      int i = 1;
      System.out.println("     0. starting");
      System.out.println("--------------------------------------------");
      System.out.println(oldVal);

      Iterator<QueryNodeProcessor> it = this.iterator();

      QueryNodeProcessor processor;
      while (it.hasNext()) {
        processor = it.next();

        System.out.println("     " + i + ". step "
            + processor.getClass().toString());
        queryTree = processor.process(queryTree);
        newVal = queryTree.toString();
        System.out.println("     Tree changed: "
            + (newVal.equals(oldVal) ? "NO" : "YES"));
        System.out.println("--------------------------------------------");
        System.out.println(newVal);
        oldVal = newVal;
        i += 1;
      }

      System.out.println("");
      System.out.println("final result:");
      System.out.println("--------------------------------------------");
      System.out.println(queryTree.toString());
      return queryTree;

    }
  }

}
