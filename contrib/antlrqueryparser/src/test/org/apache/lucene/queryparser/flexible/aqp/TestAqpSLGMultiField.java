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

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.EmptyTokenizer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Analyzer.PerFieldReuseStrategy;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.QueryParserUtil;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.TestQPHelper;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.Operator;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.util.AqpQueryParserUtil;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * This test case is a copy of the core Lucene query parser test, it was adapted
 * to use new QueryParserHelper instead of the old query parser.
 * 
 * Tests QueryParser.
 */
public class TestAqpSLGMultiField extends AqpTestAbstractCase {

  /**
   * test stop words parsing for both the non static form, and for the
   * corresponding static form (qtxt, fields[]).
   */
  public void testStopwordsParsing() throws Exception {
    assertStopQueryEquals("one", "b:one t:one");
    assertStopQueryEquals("one stop", "b:one t:one");
    assertStopQueryEquals("one (stop)", "b:one t:one");
    assertStopQueryEquals("one ((stop))", "b:one t:one");
    assertStopQueryEquals("stop", "");
    assertStopQueryEquals("(stop)", "");
    assertStopQueryEquals("((stop))", "");
  }

  // verify parsing of query using a stopping analyzer
  private void assertStopQueryEquals(String qtxt, String expectedRes)
      throws Exception {
    String[] fields = { "b", "t" };
    Occur occur[] = { Occur.SHOULD, Occur.SHOULD };
    TestQPHelper.QPTestAnalyzer a = new TestQPHelper.QPTestAnalyzer();
    AqpQueryParser mfqp = getParser();
    mfqp.setMultiFields(fields);
    mfqp.setAnalyzer(a);

    Query q = mfqp.parse(qtxt, null);
    assertEquals(expectedRes, q.toString());

    q = QueryParserUtil.parse(qtxt, fields, occur, a);
    assertEquals(expectedRes, q.toString());
  }

  public void testSimple() throws Exception {
    String[] fields = { "b", "t" };
    AqpQueryParser mfqp = getParser();
    mfqp.setMultiFields(fields);
    mfqp.setAnalyzer(new StandardAnalyzer(TEST_VERSION_CURRENT));

    Query q = mfqp.parse("one", null);
    assertEquals("b:one t:one", q.toString());

    q = mfqp.parse("one two", null);
    assertEquals("(b:one t:one) (b:two t:two)", q.toString());

    q = mfqp.parse("+one +two", null);
    assertEquals("+(b:one t:one) +(b:two t:two)", q.toString());

    q = mfqp.parse("+one -two -three", null);
    assertEquals("+(b:one t:one) -(b:two t:two) -(b:three t:three)",
        q.toString());

    q = mfqp.parse("one^2 two", null);
    assertEquals("((b:one t:one)^2.0) (b:two t:two)", q.toString());

    mfqp.setAllowSlowFuzzy(true);
    q = mfqp.parse("one~ two", null);
    assertEquals("(b:one~0.5 t:one~0.5) (b:two t:two)", q.toString());

    q = mfqp.parse("one~0.8 two^2", null);
    assertEquals("(b:one~0.8 t:one~0.8) ((b:two t:two)^2.0)", q.toString());

    q = mfqp.parse("one* two*", null);
    assertEquals("(b:one* t:one*) (b:two* t:two*)", q.toString());

    q = mfqp.parse("[a TO c] two", null);
    assertEquals("(b:[a TO c] t:[a TO c]) (b:two t:two)", q.toString());

    q = mfqp.parse("w?ldcard", null);
    assertEquals("b:w?ldcard t:w?ldcard", q.toString());

    q = mfqp.parse("\"foo bar\"", null);
    assertEquals("b:\"foo bar\" t:\"foo bar\"", q.toString());

    q = mfqp.parse("\"aa bb cc\" \"dd ee\"", null);
    assertEquals("(b:\"aa bb cc\" t:\"aa bb cc\") (b:\"dd ee\" t:\"dd ee\")",
        q.toString());

    q = mfqp.parse("\"foo bar\"~4", null);
    assertEquals("b:\"foo bar\"~4 t:\"foo bar\"~4", q.toString());

    // LUCENE-1213: QueryParser was ignoring slop when phrase
    // had a field.
    q = mfqp.parse("b:\"foo bar\"~4", null);
    assertEquals("b:\"foo bar\"~4", q.toString());

    // make sure that terms which have a field are not touched:
    q = mfqp.parse("one f:two", null);
    assertEquals("(b:one t:one) f:two", q.toString());

    // AND mode:
    mfqp.setDefaultOperator(Operator.AND);
    q = mfqp.parse("one two", null);
    assertEquals("+(b:one t:one) +(b:two t:two)", q.toString());
    q = mfqp.parse("\"aa bb cc\" \"dd ee\"", null);
    assertEquals("+(b:\"aa bb cc\" t:\"aa bb cc\") +(b:\"dd ee\" t:\"dd ee\")",
        q.toString());

  }

  public void testBoostsSimple() throws Exception {
    Map<String, Float> boosts = new HashMap<String, Float>();
    boosts.put("b", Float.valueOf(5));
    boosts.put("t", Float.valueOf(10));
    String[] fields = { "b", "t" };
    AqpQueryParser mfqp = getParser();
    mfqp.setMultiFields(fields);
    mfqp.setFieldsBoost(boosts);
    mfqp.setAnalyzer(new StandardAnalyzer(TEST_VERSION_CURRENT));

    // Check for simple
    Query q = mfqp.parse("one", null);
    assertEquals("b:one^5.0 t:one^10.0", q.toString());

    // Check for AND
    q = mfqp.parse("one AND two", null);
    assertEquals("+(b:one^5.0 t:one^10.0) +(b:two^5.0 t:two^10.0)",
        q.toString());

    // Check for OR
    q = mfqp.parse("one OR two", null);
    assertEquals("(b:one^5.0 t:one^10.0) (b:two^5.0 t:two^10.0)", q.toString());

    // Check for AND and a field
    q = mfqp.parse("one AND two AND foo:test", null);
    assertEquals("+(b:one^5.0 t:one^10.0) +(b:two^5.0 t:two^10.0) +foo:test",
        q.toString());

    q = mfqp.parse("one^3 AND two^4", null);
    assertEquals("+((b:one^5.0 t:one^10.0)^3.0) +((b:two^5.0 t:two^10.0)^4.0)",
        q.toString());
  }

  public void testStaticMethod1() throws Exception {
    String[] fields = { "b", "t" };
    String[] queries = { "one", "two" };
    AqpQueryParser qp = getParser();
    qp.setAnalyzer(new StandardAnalyzer(TEST_VERSION_CURRENT));
    Query q = AqpQueryParserUtil.parse(qp, queries, fields);
    assertEquals("b:one t:two", q.toString());

    String[] queries2 = { "+one", "+two" };
    q = AqpQueryParserUtil.parse(qp, queries2, fields);
    assertEquals("b:one t:two", q.toString());

    String[] queries3 = { "one", "+two" };
    q = AqpQueryParserUtil.parse(qp, queries3, fields);
    assertEquals("b:one t:two", q.toString());

    String[] queries4 = { "one +more", "+two" };
    q = AqpQueryParserUtil.parse(qp, queries4, fields);
    assertEquals("(b:one +b:more) t:two", q.toString());

    String[] queries5 = { "blah" };
    try {
      q = AqpQueryParserUtil.parse(qp, queries5, fields);
      fail();
    } catch (IllegalArgumentException e) {
      // expected exception, array length differs
    }

    // check also with stop words for this static form (qtxts[], fields[]).
    TestQPHelper.QPTestAnalyzer stopA = new TestQPHelper.QPTestAnalyzer();
    qp.setAnalyzer(stopA);

    String[] queries6 = { "((+stop))", "+((stop))" };
    q = AqpQueryParserUtil.parse(qp, queries6, fields);
    assertEquals("", q.toString());

    String[] queries7 = { "one ((+stop)) +more", "+((stop)) +two" };
    q = AqpQueryParserUtil.parse(qp, queries7, fields);
    // well, aqp is better in removing the parens from top-level,
    // so this is the correct result (the AqpQueryUtils has fundamental flaw
    // anyway)
    // original was: (b:one +b:more) (+t:two)
    assertEquals("(b:one +b:more) t:two", q.toString());

  }

  public void testStaticMethod2() throws QueryNodeException {
    String[] fields = { "b", "t" };
    BooleanClause.Occur[] flags = { BooleanClause.Occur.MUST,
        BooleanClause.Occur.MUST_NOT };
    Query q = QueryParserUtil.parse("one", fields, flags, new StandardAnalyzer(
        TEST_VERSION_CURRENT));
    assertEquals("+b:one -t:one", q.toString());

    q = QueryParserUtil.parse("one two", fields, flags, new StandardAnalyzer(
        TEST_VERSION_CURRENT));
    assertEquals("+(b:one b:two) -(t:one t:two)", q.toString());

    try {
      BooleanClause.Occur[] flags2 = { BooleanClause.Occur.MUST };
      q = QueryParserUtil.parse("blah", fields, flags2, new StandardAnalyzer(
          TEST_VERSION_CURRENT));
      fail();
    } catch (IllegalArgumentException e) {
      // expected exception, array length differs
    }
  }

  public void testStaticMethod2Old() throws Exception {
    String[] fields = { "b", "t" };
    BooleanClause.Occur[] flags = { BooleanClause.Occur.MUST,
        BooleanClause.Occur.MUST_NOT };
    AqpQueryParser parser = getParser();
    parser.setMultiFields(fields);
    parser.setAnalyzer(new StandardAnalyzer(TEST_VERSION_CURRENT));

    Query q = QueryParserUtil.parse("one", fields, flags, new StandardAnalyzer(
        TEST_VERSION_CURRENT));// , fields, flags, new
    // StandardAnalyzer());
    assertEquals("+b:one -t:one", q.toString());

    q = QueryParserUtil.parse("one two", fields, flags, new StandardAnalyzer(
        TEST_VERSION_CURRENT));
    assertEquals("+(b:one b:two) -(t:one t:two)", q.toString());

    try {
      BooleanClause.Occur[] flags2 = { BooleanClause.Occur.MUST };
      q = QueryParserUtil.parse("blah", fields, flags2, new StandardAnalyzer(
          TEST_VERSION_CURRENT));
      fail();
    } catch (IllegalArgumentException e) {
      // expected exception, array length differs
    }
  }

  public void testStaticMethod3() throws QueryNodeException {
    String[] queries = { "one", "two", "three" };
    String[] fields = { "f1", "f2", "f3" };
    BooleanClause.Occur[] flags = { BooleanClause.Occur.MUST,
        BooleanClause.Occur.MUST_NOT, BooleanClause.Occur.SHOULD };
    Query q = QueryParserUtil.parse(queries, fields, flags,
        new StandardAnalyzer(TEST_VERSION_CURRENT));
    assertEquals("+f1:one -f2:two f3:three", q.toString());

    try {
      BooleanClause.Occur[] flags2 = { BooleanClause.Occur.MUST };
      q = QueryParserUtil.parse(queries, fields, flags2, new StandardAnalyzer(
          TEST_VERSION_CURRENT));
      fail();
    } catch (IllegalArgumentException e) {
      // expected exception, array length differs
    }
  }

  public void testStaticMethod3Old() throws QueryNodeException {
    String[] queries = { "one", "two" };
    String[] fields = { "b", "t" };
    BooleanClause.Occur[] flags = { BooleanClause.Occur.MUST,
        BooleanClause.Occur.MUST_NOT };
    Query q = QueryParserUtil.parse(queries, fields, flags,
        new StandardAnalyzer(TEST_VERSION_CURRENT));
    assertEquals("+b:one -t:two", q.toString());

    try {
      BooleanClause.Occur[] flags2 = { BooleanClause.Occur.MUST };
      q = QueryParserUtil.parse(queries, fields, flags2, new StandardAnalyzer(
          TEST_VERSION_CURRENT));
      fail();
    } catch (IllegalArgumentException e) {
      // expected exception, array length differs
    }
  }

  public void testAnalyzerReturningNull() throws Exception {
    String[] fields = new String[] { "f1", "f2", "f3" };
    AqpQueryParser parser = getParser();
    parser.setMultiFields(fields);
    parser.setAnalyzer(new AnalyzerReturningNull());

    Query q = parser.parse("bla AND blo", null);
    assertEquals("+(f2:bla f3:bla) +(f2:blo f3:blo)", q.toString());
    // the following queries are not affected as their terms are not
    // analyzed anyway:
    q = parser.parse("bla*", null);
    assertEquals("f1:bla* f2:bla* f3:bla*", q.toString());
    q = parser.parse("bla~", null);
    assertEquals("f1:bla~1 f2:bla~1 f3:bla~1", q.toString());
    q = parser.parse("[a TO c]", null);
    assertEquals("f1:[a TO c] f2:[a TO c] f3:[a TO c]", q.toString());
  }

  public void testStopWordSearching() throws Exception {
    Analyzer analyzer = new StandardAnalyzer(TEST_VERSION_CURRENT);
    Directory ramDir = new RAMDirectory();
    IndexWriter iw = new IndexWriter(ramDir, newIndexWriterConfig(
        TEST_VERSION_CURRENT, analyzer));
    Document doc = new Document();
    doc.add(newField("body", "blah the footest blah", TextField.TYPE_NOT_STORED));
    iw.addDocument(doc);
    iw.close();

    AqpQueryParser mfqp = getParser();

    mfqp.setMultiFields(new String[] { "body" });
    mfqp.setAnalyzer(analyzer);
    mfqp.setDefaultOperator(Operator.AND);
    Query q = mfqp.parse("the footest", null);
    IndexSearcher is = new IndexSearcher(DirectoryReader.open(ramDir));
    ScoreDoc[] hits = is.search(q, null, 1000).scoreDocs;
    assertEquals(1, hits.length);
    ramDir.close();
  }

  /**
   * Return empty tokens for field "f1".
   */
  private static final class AnalyzerReturningNull extends Analyzer {
    MockAnalyzer stdAnalyzer = new MockAnalyzer(random());

    public AnalyzerReturningNull() {
      super(new PerFieldReuseStrategy());
    }

    @Override
    public TokenStreamComponents createComponents(String fieldName,
        Reader reader) {
      if ("f1".equals(fieldName)) {
        return new TokenStreamComponents(new EmptyTokenizer(reader));
      } else {
        return stdAnalyzer.createComponents(fieldName, reader);
      }
    }
  }

  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAqpSLGMultiField.class);
  }

}
