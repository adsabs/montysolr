package org.apache.lucene.queryParser.aqp;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.QueryParserHelper;
import org.apache.lucene.queryParser.standard.StandardQueryParser;
import org.apache.lucene.queryParser.standard.config.DefaultOperatorAttribute.Operator;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.LuceneTestCase;

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
		
		//DEFAULT OPERATOR IS AND
		qp.setDefaultOperator(Operator.AND);
		sp.setDefaultOperator(Operator.AND);
		
		Query qa = sp.parse("kahnn-strauss", "x");
		Query qb = qp.parse("kahnn-strauss", "x");
		
		qa = sp.parse("a \\\"b \\\"c d", "x");
		qb = qp.parse("a \\\"b \\\"c d", "x");
		
		qa = sp.parse("\"a \\\"b c\\\" d\"", "x");
		qb = qp.parse("\"a \\\"b c\\\" d\"", "x");
		
		qp.setDebug(true);
		System.out.println(qp.parse("a -(a x)^0.5", ""));
		assertQueryMatch(qp, "(+(-(a b)))^0.8 OR -(x y)^0.2", "field", 
        "((-(+field:a +field:b))^0.8) ((-(+field:x +field:y))^0.2)");
		
		assertQueryMatch(qp, "(+(-(a b)))^0.8 -(x y)", "field", 
        "+((-(+field:a +field:b))^0.8) +(-(+field:x +field:y))");
		// or does -(x y) have different semantics? ... -field:x -field:y
		// +((-(+field:a +field:b))^0.8) -field:x -field:y
		
		assertQueryMatch(qp, "+((+(-(a b)))^0.8)^0.7 OR -(x y)^0.2", "field", 
        "((-((+field:a +field:b)^0.8))^0.7) ((-(+field:x +field:y))^0.2)");
		
		
		assertQueryMatch(qp, "+title:(dog cat)", "field", 
        "+title:dog +title:cat");
		
		
		assertQueryMatch(qp, "title:(+dog -cat)", "field", 
        "+title:dog -title:cat");
		
		assertQueryMatch(qp, "\\*", "field", 
        "field:*");
		
		assertQueryMatch(qp, "term~", "field", 
        "field:term~0.5");
		
		assertQueryMatch(qp, "something", "field", 
				             "field:something");
		
		assertQueryMatch(qp, "x:something", "field", 
        "x:something");
		
		assertQueryMatch(qp, "x:\"something else\"", "field", 
        "x:\"something else\"");
		
		assertQueryMatch(qp, "x:\"someth*\"", "field", 
        					"x:someth*");
		
		assertQueryMatch(qp, "x:\"someth?ng\"", "field", 
							"x:someth?ng");
		
		assertQueryMatch(qp, "A AND B C AND D", "field", 
				             "+field:A +field:B +field:C +field:D");
		
		assertQueryMatch(qp, "A AND B C AND D OR E", "field", 
        					 "+(+field:A +field:B) +((+field:C +field:D) field:E)");
		
		assertQueryMatch(qp, "one OR +two", "f", 
				             "f:one +f:two");
		
		assertQueryMatch(qp, "one OR two NOT three", "field", 
							 "field:one (+field:two -field:three)");
		
		assertQueryMatch(qp, "one OR (two AND three) NOT four", "field", 
							 "field:one (+(+field:two +field:three) -field:four)");
		
		assertQueryMatch(qp, "-one -two", "field", 
				             "-field:one -field:two");
		
		assertQueryMatch(qp, "x:one NOT y:two -three^0.5", "field", 
                             "+(+x:one -y:two) -field:three^0.5");
		
		assertQueryMatch(qp, "one NOT two -three~0.2", "field", 
        					"+(+field:one -field:two) -field:three~0.2");
		
		assertQueryMatch(qp, "one NOT two NOT three~0.2", "field", 
        					"+field:one -field:two -field:three~0.2");

		assertQueryMatch(qp, "one two^0.5 three~0.2", "field", 
        					"+field:one +field:two^0.5 +field:three~0.2");

		assertQueryMatch(qp, "one (two three)^0.8", "field", 
        					"+field:one +((+field:two +field:three)^0.8)");

		assertQueryMatch(qp, "one (x:two three)^0.8", "field", 
        					"+field:one +((+x:two +field:three)^0.8)");
		
		assertQueryMatch(qp, "one:(two three)^0.8", "field", 
        					"(+one:two +one:three)^0.8");
		
		assertQueryMatch(qp, "-one:(two three)^0.8", "field", 
       						"-((+one:two +one:three)^0.8)");
		
		assertQueryMatch(qp, "+one:(two three)^0.8", "field", 
        					"(+one:two +one:three)^0.8");
		
		assertQueryMatch(qp, "[one TO five]", "field", 
        					"field:[one TO five]");
		
		assertQueryMatch(qp, "z:[one TO five]", "field", 
        					"z:[one TO five]");
		
		assertQueryMatch(qp, "{one TO five}", "field", 
        					"field:{one TO five}");
		
		assertQueryMatch(qp, "z:{one TO five}", "field", 
        					"z:{one TO five}");
		
		assertQueryMatch(qp, "z:{\"one\" TO \"five\"}", "field", 
        					"z:{one TO five}");
		
		assertQueryMatch(qp, "z:{one TO *}", "field", 
        					"z:{one TO *}");
		
		assertQueryMatch(qp, "this +(that)", "field", 
        					"+field:this +field:that");
		
		assertQueryMatch(qp, "(this) (that)", "field", 
							"+field:this +field:that");
		
		assertQueryMatch(qp, "this ((((+(that))))) ", "field", 
							"+field:this +field:that");
		
		assertQueryMatch(qp, "this (+(that)^0.7)", "field", 
							"+field:this +field:that^0.7");
		
		assertQueryMatch(qp, "this (+(that thus)^0.7)", "field", 
							"+field:this +((+field:that +field:thus)^0.7)");
		
		assertQueryMatch(qp, "this (+(-(that thus))^0.7)", "field", 
							"+field:this -((+field:that +field:thus)^0.7)");
		
		assertQueryMatch(qp, "this (+(-(+(-(that thus))^0.1))^0.3)", "field", 
							"+field:this -((+field:that +field:thus)^0.1)");
		
		BooleanQuery.setMaxClauseCount(2);
		try {
			qp = getParser(new WhitespaceAnalyzer(TEST_VERSION_CURRENT));

			qp.parse("one two three", "field");
			fail("ParseException expected due to too many boolean clauses");
		} catch (QueryNodeException expected) {
			// too many boolean clauses, so ParseException is expected
		}
	}


}
