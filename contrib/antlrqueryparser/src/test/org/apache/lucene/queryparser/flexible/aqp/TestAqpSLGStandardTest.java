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
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.Operator;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.processors.GroupQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpStandardLuceneParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpSyntaxParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.automaton.BasicAutomata;
import org.apache.lucene.util.automaton.CharacterRunAutomaton;

/**
 * This test case is a copy of the core Lucene query parser test, it was adapted
 * to use new QueryParserHelper instead of the old query parser.
 * 
 * TODO: modify the QueryParserHelper so that we can extend it (it is not
 * flexible in getting the parser, otherwise we could use the test methods there
 * for most part)
 * 
 * Tests QueryParser.
 */
public class TestAqpSLGStandardTest extends AqpTestAbstractCase {

  public static Analyzer qpAnalyzer = new QPTestAnalyzer();

  public static final class QPTestFilter extends TokenFilter {
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);

    /**
     * Filter which discards the token 'stop' and which expands the token
     * 'phrase' into 'phrase1 phrase2'
     */
    public QPTestFilter(TokenStream in) {
      super(in);
    }

    boolean inPhrase = false;
    int savedStart = 0, savedEnd = 0;

    @Override
    public boolean incrementToken() throws IOException {
      if (inPhrase) {
        inPhrase = false;
        clearAttributes();
        termAtt.setEmpty().append("phrase2");
        offsetAtt.setOffset(savedStart, savedEnd);
        return true;
      } else
        while (input.incrementToken()) {
          if (termAtt.toString().equals("phrase")) {
            inPhrase = true;
            savedStart = offsetAtt.startOffset();
            savedEnd = offsetAtt.endOffset();
            termAtt.setEmpty().append("phrase1");
            offsetAtt.setOffset(savedStart, savedEnd);
            return true;
          } else if (!termAtt.toString().equals("stop"))
            return true;
        }
      return false;
    }
  }

  public static final class QPTestAnalyzer extends Analyzer {

    /** Filters MockTokenizer with StopFilter. */
    @Override
    public final TokenStreamComponents createComponents(String fieldName,
        Reader reader) {
      Tokenizer tokenizer = new MockTokenizer(reader, MockTokenizer.SIMPLE,
          true);
      return new TokenStreamComponents(tokenizer, new QPTestFilter(tokenizer));
    }
  }

  public static class QPTestParser extends AqpQueryParser {
    public QPTestParser(QueryConfigHandler config, AqpSyntaxParser parser,
        QueryNodeProcessorPipeline processor, QueryTreeBuilder builder) {
      super(config, parser, processor, builder);
      // TODO Auto-generated constructor stub
    }

    public static AqpQueryParser init(Analyzer a) throws Exception {
      AqpQueryParser p = AqpStandardLuceneParser.init();

      ((QueryNodeProcessorPipeline) p.getQueryNodeProcessor())
          .add(new QPTestParserQueryNodeProcessor());
      p.setAnalyzer(a);
      return p;
    }

    private static class QPTestParserQueryNodeProcessor extends
        QueryNodeProcessorImpl {

      @Override
      protected QueryNode postProcessNode(QueryNode node)
          throws QueryNodeException {

        return node;

      }

      @Override
      protected QueryNode preProcessNode(QueryNode node)
          throws QueryNodeException {

        if (node instanceof WildcardQueryNode || node instanceof FuzzyQueryNode) {

          throw new QueryNodeException(new MessageImpl(
              QueryParserMessages.EMPTY_MESSAGE));

        }

        return node;

      }

      @Override
      protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
          throws QueryNodeException {

        return children;

      }

    }

  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    originalMaxClauses = BooleanQuery.getMaxClauseCount();
  }

  public void testConstantScoreAutoRewrite() throws Exception {
    AqpQueryParser qp = getParser();
    qp.setAnalyzer(new WhitespaceAnalyzer(TEST_VERSION_CURRENT));

    Query q = qp.parse("foo*bar", "field");
    assertTrue(q instanceof WildcardQuery);
    assertEquals(MultiTermQuery.CONSTANT_SCORE_AUTO_REWRITE_DEFAULT,
        ((MultiTermQuery) q).getRewriteMethod());

    q = qp.parse("foo*", "field");
    assertTrue(q instanceof PrefixQuery);
    assertEquals(MultiTermQuery.CONSTANT_SCORE_AUTO_REWRITE_DEFAULT,
        ((MultiTermQuery) q).getRewriteMethod());

    q = qp.parse("[a TO z]", "field");
    assertTrue(q instanceof TermRangeQuery);
    assertEquals(MultiTermQuery.CONSTANT_SCORE_AUTO_REWRITE_DEFAULT,
        ((MultiTermQuery) q).getRewriteMethod());
  }

  public void testCJK() throws Exception {
    // Test Ideographic Space - As wide as a CJK character cell (fullwidth)
    // used google to translate the word "term" to japanese -> ??
    assertQueryEquals("term\u3000term\u3000term", null,
        "term\u0020term\u0020term");
    assertQueryEqualsAllowLeadingWildcard("??\u3000??\u3000??", null,
        "??\u0020??\u0020??");
  }

  public void testCJKTerm() throws Exception {
    // individual CJK chars as terms
    StandardAnalyzer analyzer = new StandardAnalyzer(TEST_VERSION_CURRENT);

    BooleanQuery expected = new BooleanQuery();
    expected.add(new TermQuery(new Term("field", "中")),
        BooleanClause.Occur.SHOULD);
    expected.add(new TermQuery(new Term("field", "国")),
        BooleanClause.Occur.SHOULD);

    assertEquals(expected, getQuery("中国", analyzer));
  }

  public void testCJKBoostedTerm() throws Exception {
    // individual CJK chars as terms
    StandardAnalyzer analyzer = new StandardAnalyzer(TEST_VERSION_CURRENT);

    BooleanQuery expected = new BooleanQuery();
    expected.setBoost(0.5f);
    expected.add(new TermQuery(new Term("field", "中")),
        BooleanClause.Occur.SHOULD);
    expected.add(new TermQuery(new Term("field", "国")),
        BooleanClause.Occur.SHOULD);

    assertEquals(expected, getQuery("中国^0.5", analyzer));
  }

  public void testCJKPhrase() throws Exception {
    // individual CJK chars as terms
    StandardAnalyzer analyzer = new StandardAnalyzer(TEST_VERSION_CURRENT);

    PhraseQuery expected = new PhraseQuery();
    expected.add(new Term("field", "中"));
    expected.add(new Term("field", "国"));

    assertEquals(expected, getQuery("\"中国\"", analyzer));
  }

  public void testCJKBoostedPhrase() throws Exception {
    // individual CJK chars as terms
    StandardAnalyzer analyzer = new StandardAnalyzer(TEST_VERSION_CURRENT);

    PhraseQuery expected = new PhraseQuery();
    expected.setBoost(0.5f);
    expected.add(new Term("field", "中"));
    expected.add(new Term("field", "国"));

    assertEquals(expected, getQuery("\"中国\"^0.5", analyzer));
  }

  public void testCJKSloppyPhrase() throws Exception {
    // individual CJK chars as terms
    StandardAnalyzer analyzer = new StandardAnalyzer(TEST_VERSION_CURRENT);

    PhraseQuery expected = new PhraseQuery();
    expected.setSlop(3);
    expected.add(new Term("field", "中"));
    expected.add(new Term("field", "国"));

    assertEquals(expected, getQuery("\"中国\"~3", analyzer));
  }

  public void testSimple() throws Exception {
    assertQueryEquals("\"term germ\"~2", null, "\"term germ\"~2");
    assertQueryEquals("term term term", null, "term term term");
    assertQueryEquals("t�rm term term", new WhitespaceAnalyzer(
        TEST_VERSION_CURRENT), "t�rm term term");
    assertQueryEquals("�mlaut", new WhitespaceAnalyzer(TEST_VERSION_CURRENT),
        "�mlaut");

    // XXX: not allowed, TODO???
    // assertQueryEquals("\"\"", new KeywordAnalyzer(), "");
    // assertQueryEquals("foo:\"\"", new KeywordAnalyzer(), "foo:");

    assertQueryEquals("a AND b", null, "+a +b");
    assertQueryEquals("(a AND b)", null, "+a +b");
    assertQueryEquals("c OR (a AND b)", null, "c (+a +b)");

    assertQueryEquals("a AND NOT b", null, "+a -b");
    assertQueryEquals("a NOT b", null, "+a -b");

    assertQueryEquals("a AND -b", null, "+a -b");

    assertQueryEquals("a AND !b", null, "+a -b");

    assertQueryEquals("a && b", null, "+a +b");

    assertQueryEquals("a && ! b", null, "+a -b");

    assertQueryEquals("a OR b", null, "a b");
    assertQueryEquals("a || b", null, "a b");

    assertQueryEquals("a OR !b", null, "a -b");

    assertQueryEquals("a OR ! b", null, "a -b");

    assertQueryEquals("a OR -b", null, "a -b");

    assertQueryEquals("+term -term term", null, "+term -term term");
    assertQueryEquals("foo:term AND field:anotherTerm", null,
        "+foo:term +anotherterm");
    assertQueryEquals("term AND \"phrase phrase\"", null,
        "+term +\"phrase phrase\"");
    assertQueryEquals("\"hello there\"", null, "\"hello there\"");
    assertTrue(getQuery("a AND b", null) instanceof BooleanQuery);
    assertTrue(getQuery("hello", null) instanceof TermQuery);
    assertTrue(getQuery("\"hello there\"", null) instanceof PhraseQuery);

    assertQueryEquals("germ term^2.0", null, "germ term^2.0");
    assertQueryEquals("(term)^2.0", null, "term^2.0");
    assertQueryEquals("(germ term)^2.0", null, "(germ term)^2.0");
    assertQueryEquals("term^2.0", null, "term^2.0");
    assertQueryEquals("term^2", null, "term^2.0");
    assertQueryEquals("\"germ term\"^2.0", null, "\"germ term\"^2.0");
    assertQueryEquals("\"term germ\"^2", null, "\"term germ\"^2.0");

    assertQueryEquals("(foo OR bar) AND (baz OR boo)", null,
        "+(foo bar) +(baz boo)");

    assertQueryEquals("((a OR b) AND NOT c) OR d", null, "(+(a b) -c) d");
    assertQueryEquals("((a OR b) NOT c) OR d", null, "(+(a b) -c) d");

    assertQueryEquals("+(apple \"steve jobs\") -(foo bar baz)", null,
        "+(apple \"steve jobs\") -(foo bar baz)");
    assertQueryEquals("+title:(dog OR cat) -author:\"bob dole\"", null,
        "+(title:dog title:cat) -author:\"bob dole\"");

    AqpQueryParser qp = getParser();
    qp.setDefaultOperator(Operator.OR);
    assertQueryMatch(qp, "title:(+a -b c)", "text", "+title:a -title:b title:c");

    qp.setDefaultOperator(Operator.AND);
    assertQueryMatch(qp, "title:(+a -b c)", "text",
        "+title:a -title:b +title:c");

  }

  public void testPunct() throws Exception {
    Analyzer a = new WhitespaceAnalyzer(TEST_VERSION_CURRENT);
    assertQueryEquals("a&b", a, "a&b");
    assertQueryEquals("a&&b", a, "a&&b");
    assertQueryEquals(".NET", a, ".NET");
  }

  public void testSlop() throws Exception {

    assertQueryEquals("\"term germ\"~2", null, "\"term germ\"~2");
    assertQueryEquals("\"term germ\"~2 flork", null, "\"term germ\"~2 flork");
    assertQueryEquals("\"term\"~2", null, "term");
    assertQueryEquals("\" \"~2 germ", null, "germ");
    assertQueryEquals("\"term germ\"~2^2", null, "\"term germ\"~2^2.0");
  }

  public void testNumber() throws Exception {
    // The numbers go away because SimpleAnalzyer ignores them
    assertQueryEquals("3", null, "");
    assertQueryEquals("term 1.0 1 2", null, "term");
    assertQueryEquals("term term1 term2", null, "term term term");

    Analyzer a = new StandardAnalyzer(TEST_VERSION_CURRENT);
    assertQueryEquals("3", a, "3");
    assertQueryEquals("term 1.0 1 2", a, "term 1.0 1 2");
    assertQueryEquals("term term1 term2", a, "term term1 term2");
  }

  public void testWildcard() throws Exception {
    assertQueryEquals("term*", null, "term*");
    assertQueryEquals("term*^2", null, "term*^2.0");
    assertQueryEquals("term~", null, "term~2");
    assertQueryEquals("term~0.7", null, "term~1");

    assertQueryEquals("term~^2", null, "term~2^2.0");

    assertQueryEquals("term^2~", null, "term~2^2.0");
    assertQueryEquals("term*germ", null, "term*germ");
    assertQueryEquals("term*germ^3", null, "term*germ^3.0");

    assertTrue(getQuery("term*", null) instanceof PrefixQuery);
    assertTrue(getQuery("term*^2", null) instanceof PrefixQuery);
    assertTrue(getQuery("term~", null) instanceof FuzzyQuery);
    assertTrue(getQuery("term~0.7", null) instanceof FuzzyQuery);

    FuzzyQuery fq = (FuzzyQuery) getQuery("term~0.7", null);
    assertEquals(1, fq.getMaxEdits());
    assertEquals(FuzzyQuery.defaultPrefixLength, fq.getPrefixLength());
    fq = (FuzzyQuery) getQuery("term~", null);
    assertEquals(2, fq.getMaxEdits());
    assertEquals(FuzzyQuery.defaultPrefixLength, fq.getPrefixLength());

    assertTrue(getQuery("term*germ", null) instanceof WildcardQuery);

    /*
     * Tests to see that wild card terms are (or are not) properly lower-cased
     * with propery parser configuration
     */
    // First prefix queries:
    // by default, convert to lowercase:
    assertWildcardQueryEquals("Term*", true, "term*");
    // explicitly set lowercase:
    assertWildcardQueryEquals("term*", true, "term*");
    assertWildcardQueryEquals("Term*", true, "term*");
    assertWildcardQueryEquals("TERM*", true, "term*");
    // explicitly disable lowercase conversion:
    assertWildcardQueryEquals("term*", false, "term*");
    assertWildcardQueryEquals("Term*", false, "Term*");
    assertWildcardQueryEquals("TERM*", false, "TERM*");
    // Then 'full' wildcard queries:
    // by default, convert to lowercase:
    assertWildcardQueryEquals("Te?m", "te?m");
    // explicitly set lowercase:
    assertWildcardQueryEquals("te?m", true, "te?m");
    assertWildcardQueryEquals("Te?m", true, "te?m");
    assertWildcardQueryEquals("TE?M", true, "te?m");
    assertWildcardQueryEquals("Te?m*gerM", true, "te?m*germ");
    // explicitly disable lowercase conversion:
    assertWildcardQueryEquals("te?m", false, "te?m");
    assertWildcardQueryEquals("Te?m", false, "Te?m");
    assertWildcardQueryEquals("TE?M", false, "TE?M");
    assertWildcardQueryEquals("Te?m*gerM", false, "Te?m*gerM");
    // Fuzzy queries:
    assertWildcardQueryEquals("Term~", "term~2");
    assertWildcardQueryEquals("Term~", true, "term~2");
    assertWildcardQueryEquals("Term~", false, "Term~2");
    // Range queries:

    // TODO: implement this on QueryParser
    // Q0002E_INVALID_SYNTAX_CANNOT_PARSE: Syntax Error, cannot parse '[A TO
    // C]': Lexical error at line 1, column 1. Encountered: "[" (91), after
    // : ""
    assertWildcardQueryEquals("[A TO C]", "[a TO c]");
    assertWildcardQueryEquals("[A TO C]", true, "[a TO c]");
    assertWildcardQueryEquals("[A TO C]", false, "[A TO C]");
    // Test suffix queries: first disallow
    try {
      assertWildcardQueryEquals("*Term", true, "*term");
      fail();
    } catch (QueryNodeException pe) {
      // expected exception
    }
    try {
      assertWildcardQueryEquals("?Term", true, "?term");
      fail();
    } catch (QueryNodeException pe) {
      // expected exception
    }
    // Test suffix queries: then allow
    assertWildcardQueryEquals("*Term", true, "*term", true);
    assertWildcardQueryEquals("?Term", true, "?term", true);
  }

  public void testLeadingWildcardType() throws Exception {
    AqpQueryParser qp = getParser(null);
    qp.setAllowLeadingWildcard(true);
    assertEquals(WildcardQuery.class, qp.parse("t*erm*", "field").getClass());
    assertEquals(WildcardQuery.class, qp.parse("?term*", "field").getClass());
    assertEquals(WildcardQuery.class, qp.parse("*term*", "field").getClass());
  }

  public void testQPA() throws Exception {
    assertQueryEquals("term term^3.0 term", qpAnalyzer, "term term^3.0 term");
    assertQueryEquals("term stop^3.0 term", qpAnalyzer, "term term");

    assertQueryEquals("term term term", qpAnalyzer, "term term term");
    assertQueryEquals("term +stop term", qpAnalyzer, "term term");
    assertQueryEquals("term -stop term", qpAnalyzer, "term term");

    assertQueryEquals("drop AND (stop) AND roll", qpAnalyzer, "+drop +roll");
    assertQueryEquals("term +(stop) term", qpAnalyzer, "term term");
    assertQueryEquals("term -(stop) term", qpAnalyzer, "term term");

    assertQueryEquals("drop AND stop AND roll", qpAnalyzer, "+drop +roll");
    // rca TODO: plug the modifier GroupQueryNodeProcessor
    // expected: term phrase1 phrase2 term
    assertQueryEquals("term phrase term", qpAnalyzer,
        "term (phrase1 phrase2) term");

    // TODO: plug the modifier GroupQueryNodeProcessor
    // expected: term phrase1 phrase2 term
    assertQueryEquals("term AND NOT phrase term", qpAnalyzer,
        "(+term -(phrase1 phrase2)) term");

    assertQueryEquals("stop^3", qpAnalyzer, "");
    assertQueryEquals("stop", qpAnalyzer, "");
    assertQueryEquals("(stop)^3", qpAnalyzer, "");
    assertQueryEquals("((stop))^3", qpAnalyzer, "");
    assertQueryEquals("(stop^3)", qpAnalyzer, "");
    assertQueryEquals("((stop)^3)", qpAnalyzer, "");
    assertQueryEquals("(stop)", qpAnalyzer, "");
    assertQueryEquals("((stop))", qpAnalyzer, "");
    assertTrue(getQuery("term term term", qpAnalyzer) instanceof BooleanQuery);
    assertTrue(getQuery("term +stop", qpAnalyzer) instanceof TermQuery);
  }

  public void testRange() throws Exception {
    assertQueryEquals("[ a TO z]", null, "[a TO z]");
    assertEquals(MultiTermQuery.CONSTANT_SCORE_AUTO_REWRITE_DEFAULT,
        ((TermRangeQuery) getQuery("[ a TO z]", null)).getRewriteMethod());

    AqpQueryParser qp = getParser();

    qp.setMultiTermRewriteMethod(MultiTermQuery.SCORING_BOOLEAN_QUERY_REWRITE);
    assertEquals(MultiTermQuery.SCORING_BOOLEAN_QUERY_REWRITE,
        ((TermRangeQuery) qp.parse("[ a TO z]", "field")).getRewriteMethod());

    assertQueryEquals("[ a TO z ]", null, "[a TO z]");
    assertQueryEquals("{ a TO z}", null, "{a TO z}");
    assertQueryEquals("{ a TO z }", null, "{a TO z}");
    assertQueryEquals("{ a TO z }^2.0", null, "{a TO z}^2.0");
    assertQueryEquals("[ a TO z] OR bar", null, "[a TO z] bar");
    assertQueryEquals("[ a TO z] AND bar", null, "+[a TO z] +bar");
    assertQueryEquals("( bar blar { a TO z}) ", null, "bar blar {a TO z}");

    // the original expected value was: gack (bar blar {a TO z})
    assertQueryEquals("gack ( bar blar { a TO z}) ", null,
        "gack bar blar {a TO z}");
  }

  /**
   * removed in lucene-4.0 public void testFarsiRangeCollating() throws
   * Exception { Directory ramDir = newDirectory(); IndexWriter iw = new
   * IndexWriter(ramDir, newIndexWriterConfig(TEST_VERSION_CURRENT, new
   * WhitespaceAnalyzer(TEST_VERSION_CURRENT))); Document doc = new Document();
   * doc.add(newField("content", "\u0633\u0627\u0628", Field.Store.YES,
   * Field.Index.NOT_ANALYZED)); iw.addDocument(doc); iw.close(); IndexSearcher
   * is = new IndexSearcher(ramDir, true);
   * 
   * AqpQueryParser qp = getParser(); qp.setAnalyzer(new
   * WhitespaceAnalyzer(TEST_VERSION_CURRENT));
   * 
   * // Neither Java 1.4.2 nor 1.5.0 has Farsi Locale collation available in //
   * RuleBasedCollator. However, the Arabic Locale seems to order the // Farsi
   * // characters properly. Collator c = Collator.getInstance(new
   * Locale("ar")); qp.setRangeCollator(c);
   * 
   * // Unicode order would include U+0633 in [ U+062F - U+0698 ], but Farsi //
   * orders the U+0698 character before the U+0633 character, so the // single
   * // index Term below should NOT be returned by a ConstantScoreRangeQuery //
   * with a Farsi Collator (or an Arabic one for the case when Farsi is // not
   * // supported).
   * 
   * // Test ConstantScoreRangeQuery
   * qp.setMultiTermRewriteMethod(MultiTermQuery.CONSTANT_SCORE_FILTER_REWRITE);
   * ScoreDoc[] result = is.search(qp.parse("[ \u062F TO \u0698 ]", "content"),
   * null, 1000).scoreDocs;
   * assertEquals("The index Term should not be included.", 0, result.length);
   * 
   * result = is.search(qp.parse("[ \u0633 TO \u0638 ]", "content"), null,
   * 1000).scoreDocs; assertEquals("The index Term should be included.", 1,
   * result.length);
   * 
   * // Test RangeQuery
   * qp.setMultiTermRewriteMethod(MultiTermQuery.SCORING_BOOLEAN_QUERY_REWRITE);
   * result = is.search(qp.parse("[ \u062F TO \u0698 ]", "content"), null,
   * 1000).scoreDocs; assertEquals("The index Term should not be included.", 0,
   * result.length);
   * 
   * result = is.search(qp.parse("[ \u0633 TO \u0638 ]", "content"), null,
   * 1000).scoreDocs; assertEquals("The index Term should be included.", 1,
   * result.length);
   * 
   * is.close(); ramDir.close(); }
   */

  public void testDateRange() throws Exception {
    String startDate = getLocalizedDate(2002, 1, 1);
    String endDate = getLocalizedDate(2002, 1, 4);
    Calendar endDateExpected = new GregorianCalendar();
    endDateExpected.clear();
    endDateExpected.set(2002, 1, 4, 23, 59, 59);
    endDateExpected.set(Calendar.MILLISECOND, 999);
    final String defaultField = "default";
    final String monthField = "month";
    final String hourField = "hour";
    AqpQueryParser qp = getParser();

    Map<CharSequence, DateTools.Resolution> dateRes = new HashMap<CharSequence, DateTools.Resolution>();

    // set a field specific date resolution
    dateRes.put(monthField, DateTools.Resolution.MONTH);
    qp.setDateResolution(dateRes);

    // set default date resolution to MILLISECOND
    qp.setDateResolution(DateTools.Resolution.MILLISECOND);

    // set second field specific date resolution
    dateRes.put(hourField, DateTools.Resolution.HOUR);
    qp.setDateResolution(dateRes);

    // for this field no field specific date resolution has been set,
    // so verify if the default resolution is used
    assertDateRangeQueryEquals(qp, defaultField, startDate, endDate,
        endDateExpected.getTime(), DateTools.Resolution.MILLISECOND);

    // verify if field specific date resolutions are used for these two
    // fields
    assertDateRangeQueryEquals(qp, monthField, startDate, endDate,
        endDateExpected.getTime(), DateTools.Resolution.MONTH);

    assertDateRangeQueryEquals(qp, hourField, startDate, endDate,
        endDateExpected.getTime(), DateTools.Resolution.HOUR);
  }

  public void testEscaped() throws Exception {
    Analyzer a = new WhitespaceAnalyzer(TEST_VERSION_CURRENT);

    /*
     * assertQueryEquals("\\[brackets", a, "\\[brackets");
     * assertQueryEquals("\\[brackets", null, "brackets");
     * assertQueryEquals("\\\\", a, "\\\\"); assertQueryEquals("\\+blah", a,
     * "\\+blah"); assertQueryEquals("\\(blah", a, "\\(blah");
     * 
     * assertQueryEquals("\\-blah", a, "\\-blah"); assertQueryEquals("\\!blah",
     * a, "\\!blah"); assertQueryEquals("\\{blah", a, "\\{blah");
     * assertQueryEquals("\\}blah", a, "\\}blah"); assertQueryEquals("\\:blah",
     * a, "\\:blah"); assertQueryEquals("\\^blah", a, "\\^blah");
     * assertQueryEquals("\\[blah", a, "\\[blah"); assertQueryEquals("\\]blah",
     * a, "\\]blah"); assertQueryEquals("\\\"blah", a, "\\\"blah");
     * assertQueryEquals("\\(blah", a, "\\(blah"); assertQueryEquals("\\)blah",
     * a, "\\)blah"); assertQueryEquals("\\~blah", a, "\\~blah");
     * assertQueryEquals("\\*blah", a, "\\*blah"); assertQueryEquals("\\?blah",
     * a, "\\?blah"); //assertQueryEquals("foo \\&\\& bar", a,
     * "foo \\&\\& bar"); //assertQueryEquals("foo \\|| bar", a,
     * "foo \\|| bar"); //assertQueryEquals("foo \\AND bar", a,
     * "foo \\AND bar");
     */

    assertQueryEquals("\\*", a, "*");

    assertQueryEquals("\\a", a, "a");

    assertQueryEquals("a\\-b:c", a, "a-b:c");
    assertQueryEquals("a\\+b:c", a, "a+b:c");
    assertQueryEquals("a\\:b:c", a, "a:b:c");
    assertQueryEquals("a\\\\b:c", a, "a\\b:c");

    assertQueryEquals("a:b\\-c", a, "a:b-c");
    assertQueryEquals("a:b\\+c", a, "a:b+c");
    assertQueryEquals("a:b\\:c", a, "a:b:c");
    assertQueryEquals("a:b\\\\c", a, "a:b\\c");

    assertQueryEquals("a:b\\-c*", a, "a:b-c*");
    assertQueryEquals("a:b\\+c*", a, "a:b+c*");
    assertQueryEquals("a:b\\:c*", a, "a:b:c*");

    assertQueryEquals("a:b\\\\c*", a, "a:b\\c*");

    assertQueryEquals("a:b\\-?c", a, "a:b-?c");
    assertQueryEquals("a:b\\+?c", a, "a:b+?c");
    assertQueryEquals("a:b\\:?c", a, "a:b:?c");

    assertQueryEquals("a:b\\\\?c", a, "a:b\\?c");

    assertQueryEquals("a:b\\-c~", a, "a:b-c~1");
    assertQueryEquals("a:b\\+c~", a, "a:b+c~1");
    assertQueryEquals("a:b\\:c~", a, "a:b:c~1");
    assertQueryEquals("a:b\\\\c~", a, "a:b\\c~1");

    // TODO: implement Range queries on QueryParser
    assertQueryEquals("[ a\\- TO a\\+ ]", null, "[a- TO a+]");
    assertQueryEquals("[ a\\: TO a\\~ ]", null, "[a: TO a~]");
    assertQueryEquals("[ a\\\\ TO a\\* ]", null, "[a\\ TO a*]");

    assertQueryEquals(
        "[\"c\\:\\\\temp\\\\\\~foo0.txt\" TO \"c\\:\\\\temp\\\\\\~foo9.txt\"]",
        a, "[c:\\temp\\~foo0.txt TO c:\\temp\\~foo9.txt]");

    assertQueryEquals("a\\\\\\+b", a, "a\\+b");

    assertQueryEquals("a \\\"b c\\\" d", a, "a \"b c\" d");
    assertQueryEquals("\"a \\\"b c\\\" d\"", a, "\"a \"b c\" d\"");
    assertQueryEquals("\"a \\+b c d\"", a, "\"a +b c d\"");

    assertQueryEquals("c\\:\\\\temp\\\\\\~foo.txt", a, "c:\\temp\\~foo.txt");

    assertQueryNodeException("XY\\"); // there must be a character after the
    // escape char

    // test unicode escaping
    assertQueryEquals("a\\u0062c", a, "abc");
    assertQueryEquals("XY\\u005a", a, "XYZ");
    assertQueryEquals("XY\\u005A", a, "XYZ");
    assertQueryEquals("\"a \\\\\\u0028\\u0062\\\" c\"", a, "\"a \\(b\" c\"");

    assertQueryNodeException("XY\\u005G"); // test non-hex character in escaped
    // unicode sequence
    assertQueryNodeException("XY\\u005"); // test incomplete escaped unicode
    // sequence

    // Tests bug LUCENE-800
    assertQueryEquals("(item:\\\\ item:ABCD\\\\)", a, "item:\\ item:ABCD\\");
    setDebug(true);
    assertQueryNodeException("(item:\\\\ item:ABCD\\\\))"); // unmatched closing
    setDebug(false);
    // paranthesis
    assertQueryEquals("\\*", a, "*");
    assertQueryEquals("\\\\", a, "\\"); // escaped backslash

    assertQueryNodeException("\\"); // a backslash must always be escaped

    setDebug(true);
    // LUCENE-1189
    assertQueryEquals("(\"a\\\\\") or (\"b\")", a, "a\\ or b");
    setDebug(false);
  }

  public void testQueryStringEscaping() throws Exception {
    Analyzer a = new WhitespaceAnalyzer(TEST_VERSION_CURRENT);

    assertEscapedQueryEquals("a-b:c", a, "a\\-b\\:c");
    assertEscapedQueryEquals("a+b:c", a, "a\\+b\\:c");
    assertEscapedQueryEquals("a:b:c", a, "a\\:b\\:c");
    assertEscapedQueryEquals("a\\b:c", a, "a\\\\b\\:c");

    assertEscapedQueryEquals("a:b-c", a, "a\\:b\\-c");
    assertEscapedQueryEquals("a:b+c", a, "a\\:b\\+c");
    assertEscapedQueryEquals("a:b:c", a, "a\\:b\\:c");
    assertEscapedQueryEquals("a:b\\c", a, "a\\:b\\\\c");

    assertEscapedQueryEquals("a:b-c*", a, "a\\:b\\-c\\*");
    assertEscapedQueryEquals("a:b+c*", a, "a\\:b\\+c\\*");
    assertEscapedQueryEquals("a:b:c*", a, "a\\:b\\:c\\*");

    assertEscapedQueryEquals("a:b\\\\c*", a, "a\\:b\\\\\\\\c\\*");

    assertEscapedQueryEquals("a:b-?c", a, "a\\:b\\-\\?c");
    assertEscapedQueryEquals("a:b+?c", a, "a\\:b\\+\\?c");
    assertEscapedQueryEquals("a:b:?c", a, "a\\:b\\:\\?c");

    assertEscapedQueryEquals("a:b?c", a, "a\\:b\\?c");

    assertEscapedQueryEquals("a:b-c~", a, "a\\:b\\-c\\~");
    assertEscapedQueryEquals("a:b+c~", a, "a\\:b\\+c\\~");
    assertEscapedQueryEquals("a:b:c~", a, "a\\:b\\:c\\~");
    assertEscapedQueryEquals("a:b\\c~", a, "a\\:b\\\\c\\~");

    assertEscapedQueryEquals("[ a - TO a+ ]", null, "\\[ a \\- TO a\\+ \\]");
    assertEscapedQueryEquals("[ a : TO a~ ]", null, "\\[ a \\: TO a\\~ \\]");
    assertEscapedQueryEquals("[ a\\ TO a* ]", null, "\\[ a\\\\ TO a\\* \\]");

    // LUCENE-881
    assertEscapedQueryEquals("|| abc ||", a, "\\|\\| abc \\|\\|");
    assertEscapedQueryEquals("&& abc &&", a, "\\&\\& abc \\&\\&");
  }

  public void testTabNewlineCarriageReturn() throws Exception {
    assertQueryEqualsDOA("+weltbank +worlbank", null, "+weltbank +worlbank");

    assertQueryEqualsDOA("+weltbank\n+worlbank", null, "+weltbank +worlbank");
    assertQueryEqualsDOA("weltbank \n+worlbank", null, "+weltbank +worlbank");
    assertQueryEqualsDOA("weltbank \n +worlbank", null, "+weltbank +worlbank");

    assertQueryEqualsDOA("+weltbank\r+worlbank", null, "+weltbank +worlbank");
    assertQueryEqualsDOA("weltbank \r+worlbank", null, "+weltbank +worlbank");
    assertQueryEqualsDOA("weltbank \r +worlbank", null, "+weltbank +worlbank");

    assertQueryEqualsDOA("+weltbank\r\n+worlbank", null, "+weltbank +worlbank");
    assertQueryEqualsDOA("weltbank \r\n+worlbank", null, "+weltbank +worlbank");
    assertQueryEqualsDOA("weltbank \r\n +worlbank", null, "+weltbank +worlbank");
    assertQueryEqualsDOA("weltbank \r \n +worlbank", null,
        "+weltbank +worlbank");

    assertQueryEqualsDOA("+weltbank\t+worlbank", null, "+weltbank +worlbank");
    assertQueryEqualsDOA("weltbank \t+worlbank", null, "+weltbank +worlbank");
    assertQueryEqualsDOA("weltbank \t +worlbank", null, "+weltbank +worlbank");
  }

  public void testSimpleDAO() throws Exception {
    assertQueryEqualsDOA("term term term", null, "+term +term +term");
    assertQueryEqualsDOA("term +term term", null, "+term +term +term");
    assertQueryEqualsDOA("term term +term", null, "+term +term +term");
    assertQueryEqualsDOA("term +term +term", null, "+term +term +term");
    assertQueryEqualsDOA("-term term term", null, "-term +term +term");
  }

  public void testBoost() throws Exception {
    CharacterRunAutomaton stopSet = new CharacterRunAutomaton(
        BasicAutomata.makeString("on"));
    Analyzer oneStopAnalyzer = new MockAnalyzer(random(), MockTokenizer.SIMPLE,
        true, stopSet, true);
    AqpQueryParser qp = getParser();
    qp.setAnalyzer(oneStopAnalyzer);

    Query q = qp.parse("on^1.0", "field");
    assertNotNull(q);
    q = qp.parse("\"hello\"^2.0", "field");
    assertNotNull(q);
    assertEquals(q.getBoost(), (float) 2.0, (float) 0.5);
    q = qp.parse("hello^2.0", "field");
    assertNotNull(q);
    assertEquals(q.getBoost(), (float) 2.0, (float) 0.5);
    q = qp.parse("\"on\"^1.0", "field");
    assertNotNull(q);

    AqpQueryParser qp2 = getParser();
    qp2.setAnalyzer(new StandardAnalyzer(TEST_VERSION_CURRENT));

    q = qp2.parse("the^3", "field");
    // "the" is a stop word so the result is an empty query:
    assertNotNull(q);
    assertEquals("", q.toString());
    assertEquals(1.0f, q.getBoost(), 0.01f);
  }

  public void testException() throws Exception {
    assertQueryNodeException("*leadingWildcard"); // disallowed by default
    assertQueryNodeException("(foo bar");

    // these exceptions are not thrown
    setDebug(true);
    assertQueryNodeException("\"some phrase");
    assertQueryNodeException("foo bar))");
    assertQueryNodeException("field:term:with:colon some more terms");
    assertQueryNodeException("(sub query)^5.0^2.0 plus more");
    assertQueryNodeException("secret AND illegal) AND access:confidential");
    setDebug(false);
  }

  public void testCustomQueryParserWildcard() throws Exception {
    try {
      QPTestParser.init(new WhitespaceAnalyzer(TEST_VERSION_CURRENT)).parse(
          "a?t", "contents");
      fail("Wildcard queries should not be allowed");
    } catch (QueryNodeException expected) {
      // expected exception
    }
  }

  public void testCustomQueryParserFuzzy() throws Exception {
    try {
      QPTestParser.init(new WhitespaceAnalyzer(TEST_VERSION_CURRENT)).parse(
          "xunit~", "contents");
      fail("Fuzzy queries should not be allowed");
    } catch (QueryNodeException expected) {
      // expected exception
    }
  }

  public void testBooleanQuery() throws Exception {
    BooleanQuery.setMaxClauseCount(2);
    try {
      AqpQueryParser qp = getParser();
      qp.setAnalyzer(new WhitespaceAnalyzer(TEST_VERSION_CURRENT));

      qp.parse("one two three", "field");
      fail("ParseException expected due to too many boolean clauses");
    } catch (QueryNodeException expected) {
      // too many boolean clauses, so ParseException is expected
    }
  }

  /**
   * This test differs from TestPrecedenceQueryParser
   */
  public void testPrecedence() throws Exception {
    AqpQueryParser qp1 = getParser();
    qp1.setAnalyzer(new WhitespaceAnalyzer(TEST_VERSION_CURRENT));

    AqpQueryParser qp2 = getParser();
    qp2.setAnalyzer(new WhitespaceAnalyzer(TEST_VERSION_CURRENT));

    // TODO: to achieve standard lucene behaviour (no operator precedence)
    // modify the GroupQueryNodeProcessor to recognize our new BooleanQN classes
    // then do:
    QueryNodeProcessorPipeline processor = (QueryNodeProcessorPipeline) qp1
        .getQueryNodeProcessor();
    processor.add(new GroupQueryNodeProcessor());

    Query query1 = qp1.parse("A AND B OR C AND D", "field");
    Query query2 = qp2.parse("+A +B +C +D", "field");

    assertEquals(query1, query2);
  }

  public void testLocalDateFormat() throws IOException, QueryNodeException,
      ParseException {
    Directory ramDir = new RAMDirectory();
    IndexWriter iw = new IndexWriter(ramDir, newIndexWriterConfig(
        TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    addDateDoc("a", 2005, 12, 2, 10, 15, 33, iw);
    addDateDoc("b", 2005, 12, 4, 22, 15, 00, iw);
    iw.close();
    IndexSearcher is = new IndexSearcher(DirectoryReader.open(ramDir));

    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);
    Date d1_12 = format.parse("1/12/2005");
    Date d3_12 = format.parse("3/12/2005");
    Date d4_12 = format.parse("4/12/2005");
    Date d28_12 = format.parse("28/12/2005");

    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,
        Locale.getDefault());
    String dec1 = df.format(d1_12);
    String dec2 = df.format(format.parse("2/12/2005"));
    String dec3 = df.format(d3_12);
    String dec4 = df.format(d4_12);
    String dec28 = df.format(d28_12);

    assertHits(2, String.format("[%s TO %s]", dec1, dec28), is);
    assertHits(2, String.format("[%s TO %s]", dec1, dec4), is);

    assertHits(2, String.format("{%s TO %s}", dec1, dec28), is);
    assertHits(1, String.format("{%s TO %s}", dec1, dec4), is);
    assertHits(0, String.format("{%s TO %s}", dec3, dec4), is);

    ramDir.close();
  }

  public void testStopwords() throws Exception {
    AqpQueryParser qp = getParser();
    qp.setAnalyzer(new StopAnalyzer(TEST_VERSION_CURRENT, StopFilter
        .makeStopSet(TEST_VERSION_CURRENT, "the", "foo")));

    Query result = qp.parse("a:the OR a:foo", "a");
    assertNotNull("result is null and it shouldn't be", result);
    assertTrue("result is not a BooleanQuery", result instanceof BooleanQuery);
    assertTrue(((BooleanQuery) result).clauses().size() + " does not equal: "
        + 0, ((BooleanQuery) result).clauses().size() == 0);
    result = qp.parse("a:woo OR a:the", "a");
    assertNotNull("result is null and it shouldn't be", result);
    assertTrue("result is not a TermQuery", result instanceof TermQuery);

    result = qp.parse(
        "(fieldX:xxxxx OR fieldy:xxxxxxxx)^2 AND (fieldx:the OR fieldy:foo)",
        "a");

    assertNotNull("result is null and it shouldn't be", result);
    assertTrue("result is not a BooleanQuery", result instanceof BooleanQuery);
    if (VERBOSE)
      System.out.println("Result: " + result);
    assertTrue(((BooleanQuery) result).clauses().size() + " does not equal: "
        + 2, ((BooleanQuery) result).clauses().size() == 2);
  }

  public void testPositionIncrement() throws Exception {
    AqpQueryParser qp = getParser();
    qp.setAnalyzer(new StopAnalyzer(TEST_VERSION_CURRENT, StopFilter
        .makeStopSet(TEST_VERSION_CURRENT, "the", "in", "are", "this")));

    qp.setEnablePositionIncrements(true);

    String qtxt = "\"the words in poisitions pos02578 are stopped in this phrasequery\"";
    // 0 2 5 7 8
    int expectedPositions[] = { 1, 3, 4, 6, 9 };
    PhraseQuery pq = (PhraseQuery) qp.parse(qtxt, "a");
    // System.out.println("Query text: "+qtxt);
    // System.out.println("Result: "+pq);
    Term t[] = pq.getTerms();
    int pos[] = pq.getPositions();
    for (int i = 0; i < t.length; i++) {
      // System.out.println(i+". "+t[i]+"  pos: "+pos[i]);
      assertEquals("term " + i + " = " + t[i] + " has wrong term-position!",
          expectedPositions[i], pos[i]);
    }
  }

  public void testMatchAllDocs() throws Exception {
    AqpQueryParser qp = getParser();
    qp.setAnalyzer(new WhitespaceAnalyzer(TEST_VERSION_CURRENT));

    assertEquals(new MatchAllDocsQuery(), qp.parse("*:*", "field"));
    assertEquals(new MatchAllDocsQuery(), qp.parse("(*:*)", "field"));
    BooleanQuery bq = (BooleanQuery) qp.parse("+*:* -*:*", "field");
    assertTrue(bq.getClauses()[0].getQuery() instanceof MatchAllDocsQuery);
    assertTrue(bq.getClauses()[1].getQuery() instanceof MatchAllDocsQuery);
  }

  private class CannedTokenizer extends Tokenizer {
    private int upto = 0;
    private final PositionIncrementAttribute posIncr = addAttribute(PositionIncrementAttribute.class);
    private final CharTermAttribute term = addAttribute(CharTermAttribute.class);

    public CannedTokenizer(Reader reader) {
      super(reader);
    }

    @Override
    public boolean incrementToken() {
      clearAttributes();
      if (upto == 4) {
        return false;
      }
      if (upto == 0) {
        posIncr.setPositionIncrement(1);
        term.setEmpty().append("a");
      } else if (upto == 1) {
        posIncr.setPositionIncrement(1);
        term.setEmpty().append("b");
      } else if (upto == 2) {
        posIncr.setPositionIncrement(0);
        term.setEmpty().append("c");
      } else {
        posIncr.setPositionIncrement(0);
        term.setEmpty().append("d");
      }
      upto++;
      return true;
    }

    @Override
    public void reset() throws IOException {
      super.reset();
      this.upto = 0;
    }
  }

  private class CannedAnalyzer extends Analyzer {
    @Override
    public TokenStreamComponents createComponents(String ignored,
        Reader alsoIgnored) {
      return new TokenStreamComponents(new CannedTokenizer(alsoIgnored));
    }
  }

  public void testMultiPhraseQuery() throws Exception {
    Directory dir = newDirectory();
    IndexWriter w = new IndexWriter(dir, newIndexWriterConfig(
        TEST_VERSION_CURRENT, new CannedAnalyzer()));
    Document doc = new Document();
    doc.add(newField("field", "", TextField.TYPE_NOT_STORED));
    w.addDocument(doc);
    w.commit();
    IndexReader r = DirectoryReader.open(w.getDirectory());
    IndexSearcher s = newSearcher(r);

    Query q = QPTestParser.init(new CannedAnalyzer()).parse("\"a\"", "field");
    assertTrue(q instanceof MultiPhraseQuery);
    assertEquals(1, s.search(q, 10).totalHits);
    r.close();
    w.close();
    dir.close();
  }

  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAqpSLGStandardTest.class);
  }

}
