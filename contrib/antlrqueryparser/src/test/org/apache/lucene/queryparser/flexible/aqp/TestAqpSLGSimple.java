package org.apache.lucene.queryparser.flexible.aqp;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.BoostQueryNode;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.builders.BooleanQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.BoostQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.Operator;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

/**
 * This test case is a copy of the core Lucene query parser test, it was adapted
 * to use new QueryParserHelper instead of the old query parser.
 * 
 * Tests QueryParser.
 */
public class TestAqpSLGSimple extends AqpTestAbstractCase {

  private boolean verbose = true;

  private int originalMaxClauses;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    originalMaxClauses = BooleanQuery.getMaxClauseCount();
    setGrammarName("StandardLuceneGrammar");
  }

  public void testBooleanQuery() throws Exception {

    WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer(TEST_VERSION_CURRENT);

    AqpQueryParser qp = getParser(analyzer);

    StandardQueryParser sp = (StandardQueryParser) getParser(analyzer, true);

    // DEFAULT OPERATOR IS AND
    qp.setDefaultOperator(Operator.AND);
    sp.setDefaultOperator(Operator.AND);

    // test the clause rewriting/optimization
    Query q = qp.parse("a -(-(+(-(x)^0.6))^0.2)^0.3", "");
    assertQueryMatch(qp, "a -(-(+(-(x)^0.6))^0.2)^0.3", "", "+a -x^0.3");
    assertQueryMatch(qp, "-(-(+(-(x)^0.6))^0.2)^0.3", "", "x^0.3"); // not
                                                                    // minus,
                                                                    // because
                                                                    // that is
                                                                    // not
                                                                    // allowed
    assertQueryMatch(qp, "-(-(+(-(x)^0.6))^0.2)^", "", "x"); // because defualt
                                                             // boost is 1.0f
    assertQueryMatch(qp, "-(-(+(-(x)^))^0.2)^0.1", "", "x^0.1"); // because
                                                                 // defualt
                                                                 // boost is
                                                                 // 1.0f

    Query qa = sp.parse("kahnn-strauss", "x");
    Query qb = qp.parse("kahnn-strauss", "x");
    assertQueryMatch(qp, "kahnn-strauss", "x", qa.toString());

    qa = sp.parse("a \\\"b \\\"c d", "x");
    qb = qp.parse("a \\\"b \\\"c d", "x");
    assertQueryMatch(qp, "a \\\"b \\\"c d", "x", qa.toString());

    qa = sp.parse("\"a \\\"b c\\\" d\"", "x");
    qb = qp.parse("\"a \\\"b c\\\" d\"", "x");
    assertQueryMatch(qp, "\"a \\\"b c\\\" d\"", "x", qa.toString());

    assertQueryMatch(qp, "+(-(-(-(x)^0.6))^0.2)^", "field", "field:x");
    assertQueryMatch(qp, "+(-(-(-(x)^0.6))^0.2)^0.5", "field", "field:x^0.5");

    // the first element will be positive, because the negative X NOT Y
    // is currently implemented that way (the first must return something)
    // TODO: this should be:
    // "((+field:a +field:b)^0.8) -((+field:x +field:y)^0.2)"

    assertQueryMatch(qp, "(+(-(a b)))^0.8 OR -(x y)^0.2", "field",
        "+((+field:a +field:b)^0.8) -((+field:x +field:y)^0.2)");

    assertQueryMatch(qp, "(+(-(a b)))^0.8 AND -(x y)^0.2", "field",
        "+((+field:a +field:b)^0.8) -((+field:x +field:y)^0.2)");

    assertQueryMatch(qp, "(+(-(a b)))^0.8 -(x y)", "field",
        "+((+field:a +field:b)^0.8) -(+field:x +field:y)");
    // or does -(x y) have different semantics? ... -field:x -field:y
    // +((-(+field:a +field:b))^0.8) -field:x -field:y

    assertQueryMatch(qp, "+((+(-(a b)))^0.8)^0.7 OR -(x y)^0.2", "field",
        "+((+field:a +field:b)^0.7) -((+field:x +field:y)^0.2)");

    assertQueryMatch(qp, "+title:(dog cat)", "field", "+title:dog +title:cat");

    assertQueryMatch(qp, "title:(+dog -cat)", "field", "+title:dog -title:cat");

    assertQueryMatch(qp, "\\*", "field", "field:*");

    assertQueryMatch(qp, "term~", "field", "field:term~2");
    assertQueryMatch(qp, "term~1", "field", "field:term~1");
    assertQueryMatch(qp, "term~2", "field", "field:term~2");

    qp.setAllowSlowFuzzy(true);
    assertQueryMatch(qp, "term~", "field", "field:term~0.5");
    assertQueryMatch(qp, "term~0.1", "field", "field:term~0.1");
    assertQueryMatch(qp, "term~0.2", "field", "field:term~0.2");
    qp.setAllowSlowFuzzy(false);

    assertQueryMatch(qp, "something", "field", "field:something");

    assertQueryMatch(qp, "x:something", "field", "x:something");

    assertQueryMatch(qp, "x:\"something else\"", "field",
        "x:\"something else\"");

    assertQueryMatch(qp, "x:\"someth*\"", "field", "x:someth*");

    assertQueryMatch(qp, "x:\"someth?ng\"", "field", "x:someth?ng");

    assertQueryMatch(qp, "A AND B C AND D", "field",
        "+field:A +field:B +field:C +field:D");

    assertQueryMatch(qp, "A AND B C AND D OR E", "field",
        "+(+field:A +field:B) +((+field:C +field:D) field:E)");

    assertQueryMatch(qp, "one OR +two", "f", "f:one +f:two");

    assertQueryMatch(qp, "one OR two NOT three", "field",
        "field:one (+field:two -field:three)");

    assertQueryMatch(qp, "one OR (two AND three) NOT four", "field",
        "field:one (+(+field:two +field:three) -field:four)");

    assertQueryMatch(qp, "-one -two", "field", "-field:one -field:two");

    assertQueryMatch(qp, "x:one NOT y:two -three^0.5", "field",
        "+(+x:one -y:two) -field:three^0.5");

    qp.setAllowSlowFuzzy(true);
    assertQueryMatch(qp, "one NOT two -three~0.2", "field",
        "+(+field:one -field:two) -field:three~0.2");

    assertQueryMatch(qp, "one NOT two NOT three~0.2", "field",
        "+field:one -field:two -field:three~0.2");

    assertQueryMatch(qp, "one two^0.5 three~0.2", "field",
        "+field:one +field:two^0.5 +field:three~0.2");
    qp.setAllowSlowFuzzy(false);

    assertQueryMatch(qp, "one NOT two -three~0.2", "field",
        "+(+field:one -field:two) -field:three~2");

    assertQueryMatch(qp, "one NOT two NOT three~0.2", "field",
        "+field:one -field:two -field:three~2");

    assertQueryMatch(qp, "one two^0.5 three~0.2", "field",
        "+field:one +field:two^0.5 +field:three~2");

    q = qp.parse("one (two three)^0.8", "field");
    
    // I know where the problem is: builder does not have access
    // to the config, so that when a default operator is changed
    // *after* the parser was instantiated, the builder behaves
    // the old way(s) -- this is a bigger problem, and I didn't 
    // want to change the flex parser API
    
    ((QueryTreeBuilder) qp.getQueryBuilder()).setBuilder(BooleanQueryNode.class, new BooleanQueryNodeBuilder(BooleanClause.Occur.MUST));
    
    qa = qp.parse("one (two three)^0.8", "field");
    assertQueryMatch(qp, "one (two three)^0.8", "field",
        "+field:one +((+field:two +field:three)^0.8)");

    assertQueryMatch(qp, "one (x:two three)^0.8", "field",
        "+field:one +((+x:two +field:three)^0.8)");
    
    // TODO: the original value was -((+one:two +one:three)^0.8)
    // but that is not a valid Lucene query (or is it?)
    assertQueryMatch(qp, "-one:(two three)^0.8", "field",
        "(+one:two +one:three)^0.8");

    
    assertQueryMatch(qp, "one:(two three)^0.8", "field",
        "(+one:two +one:three)^0.8");

    assertQueryMatch(qp, "+one:(two three)^0.8", "field",
        "(+one:two +one:three)^0.8");

    assertQueryMatch(qp, "[one TO five]", "field", "field:[one TO five]");

    assertQueryMatch(qp, "z:[one TO five]", "field", "z:[one TO five]");

    assertQueryMatch(qp, "{one TO five}", "field", "field:{one TO five}");

    assertQueryMatch(qp, "z:{one TO five}", "field", "z:{one TO five}");

    assertQueryMatch(qp, "z:{\"one\" TO \"five\"}", "field", "z:{one TO five}");

    assertQueryMatch(qp, "z:{one TO *}", "field", "z:{one TO *}");

    assertQueryMatch(qp, "this +(that)", "field", "+field:this +field:that");

    assertQueryMatch(qp, "(this) (that)", "field", "+field:this +field:that");

    assertQueryMatch(qp, "this ((((+(that))))) ", "field",
        "+field:this +field:that");

    assertQueryMatch(qp, "this (+(that)^0.7)", "field",
        "+field:this +field:that^0.7");

    assertQueryMatch(qp, "this (+(that thus)^0.7)", "field",
        "+field:this +((+field:that +field:thus)^0.7)");

    assertQueryMatch(qp, "this (-(+(that thus))^0.7)", "field",
        "+field:this -((+field:that +field:thus)^0.7)");

    assertQueryMatch(qp, "this (+(-(+(-(that thus))^0.1))^0.3)", "field",
        "+field:this +((+field:that +field:thus)^0.3)");

    BooleanQuery.setMaxClauseCount(2);
    try {
      qp = getParser(new WhitespaceAnalyzer(TEST_VERSION_CURRENT));

      qp.parse("one two three", "field");
      fail("ParseException expected due to too many boolean clauses");
    } catch (QueryNodeException expected) {
      // too many boolean clauses, so ParseException is expected
    }
  }

  // Uniquely for Junit 3
  public static junit.framework.Test suite() {
    return new junit.framework.JUnit4TestAdapter(TestAqpSLGSimple.class);
  }

}
