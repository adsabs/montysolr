package org.apache.lucene.queryParser.aqp;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.parser.SyntaxParser;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryParser.standard.QueryParserUtil;
import org.apache.lucene.queryParser.standard.config.DefaultOperatorAttribute.Operator;
import org.apache.lucene.queryParser.standard.nodes.WildcardQueryNode;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.LuceneTestCase;

/**
 * This test case is a copy of the core Lucene query parser test, it was adapted
 * to use new QueryParserHelper instead of the old query parser.
 * 
 * Tests QueryParser.
 */
public class TestAqpSimple extends LuceneTestCase {
	
	private boolean verbose = true;

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

		/** Filters LowerCaseTokenizer with StopFilter. */
		@Override
		public final TokenStream tokenStream(String fieldName, Reader reader) {
			return new QPTestFilter(new LowerCaseTokenizer(
					TEST_VERSION_CURRENT, reader));
		}
	}

	public static class QPTestParser extends AqpQueryParser {
		public QPTestParser(Analyzer a) {
			((QueryNodeProcessorPipeline) getQueryNodeProcessor())
					.add(new QPTestParserQueryNodeProcessor());
			this.setAnalyzer(a);

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

				if (node instanceof WildcardQueryNode
						|| node instanceof FuzzyQueryNode) {

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

	private int originalMaxClauses;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		originalMaxClauses = BooleanQuery.getMaxClauseCount();
	}

	@Override
	public void tearDown() throws Exception {
		BooleanQuery.setMaxClauseCount(originalMaxClauses);
		super.tearDown();
	}

	
	public AqpQueryParser getParser(Analyzer a) throws Exception {
		if (a == null)
			a = new SimpleAnalyzer(TEST_VERSION_CURRENT);
		AqpQueryParser qp = new AqpQueryParser();
		qp.setAnalyzer(a);
		return qp;
	}

	public Query getQuery(String query, Analyzer a) throws Exception {
		return getParser(a).parse(query, "field");
	}


	public void assertQueryEquals(String query, Analyzer a, String result)
			throws Exception {
		Query q = getQuery(query, a);
		String s = q.toString("field");
		if (!s.equals(result)) {
			fail("Query /" + query + "/ yielded /" + s + "/, expecting /"
					+ result + "/");
		}
	}


	public void assertQueryEquals(AqpQueryParser qp, String field,
			String query, String result) throws Exception {
		Query q = qp.parse(query, field);
		String s = q.toString(field);
		if (!s.equals(result)) {
			fail("Query /" + query + "/ yielded /" + s + "/, expecting /"
					+ result + "/");
		}
	}

	public void assertEscapedQueryEquals(String query, Analyzer a, String result)
			throws Exception {
		String escapedQuery = QueryParserUtil.escape(query);
		if (!escapedQuery.equals(result)) {
			fail("Query /" + query + "/ yielded /" + escapedQuery
					+ "/, expecting /" + result + "/");
		}
	}
	

	private void assertQueryMatch(AqpQueryParser qp, String queryString,
			String defaultField, String expectedResult) throws Exception {
		
		try {
			Query query = qp.parse(queryString, defaultField);
			String queryParsed = query.toString();
			
			if (!queryParsed.equals(expectedResult)) {
				
				if (this.verbose) {
					
					SyntaxParser parser = qp.getSyntaxParser();
					QueryNode queryTree = parser.parse(queryString, defaultField);
					
					System.out.println(" ----- AST QNTRee ----");
					System.out.print(queryTree);
					QueryNodeProcessor processor = qp.getQueryNodeProcessor();
				    if (processor != null) {
				      queryTree = processor.process(queryTree);
				    }
				    System.out.println(" ----- ProcessedQN ----");
				    System.out.println(queryTree);
				    System.out.println("query:\t\t" + queryString);
					System.out.println("result:\t\t" + queryParsed);
				}
				
				String msg = "Query /" + queryString + "/ with field: " + defaultField
				+ "/ yielded /" + queryParsed
				+ "/, expecting /" + expectedResult + "/";
				
				if (this.verbose) {
					System.err.println(msg);
				}
				else {
					fail(msg);
				}
			}
			else {
				if (this.verbose) {
					System.out.println("OK \"" + queryString + "\" --->  " + queryParsed );
				}
			}
		} catch (Exception e) {
			if (this.verbose) {
				System.err.println(queryString);
				e.printStackTrace();
			}
			else {
				throw e;
			}
		}
		
		
		
		
	}

	public void testBooleanQuery() throws Exception {
		
		WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer(TEST_VERSION_CURRENT);
		
		AqpQueryParser qp = getParser(analyzer);
		
		//DEFAULT OPERATOR IS AND
		qp.setDefaultOperator(Operator.AND);
		
		assertQueryMatch(qp, "something", "field", 
				             "field:something");
		
		assertQueryMatch(qp, "x:something", "field", 
        "x:something");
		
		assertQueryMatch(qp, "x:\"something else\"", "field", 
        "x:\"something else\"");
		
		assertQueryMatch(qp, "x:\"someth*\"", "field", 
        "x:someth*");
		
		assertQueryMatch(qp, "x:\"someth?ng\"", "field", 
        "x:something");
		
		assertQueryMatch(qp, "A AND B C AND D", "field", 
				             "+field:A +field:B +field:C +field:D");
		
		assertQueryMatch(qp, "A AND B C AND D OR E", "field", 
        					 "(+field:A +field:B) ((+field:C +field:D) field:E)");
		
		assertQueryMatch(qp, "one OR +two", "f", 
				             "f:one +f:two");
		
		assertQueryMatch(qp, "one OR two NOT three", "field", 
							 "field:one (+field:two -field:three)");
		
		assertQueryMatch(qp, "one OR (two AND three) NOT four", "field", 
							 "field:one ((+field:two +field:three) -field:four)");
		
		assertQueryMatch(qp, "-one -two", "field", 
				             "-field:one -field:two");
		
		assertQueryMatch(qp, "x:one NOT y:two -three^0.5", "field", 
                             "(+x:one -y:two) -field:three^0.5");
		
		assertQueryMatch(qp, "one NOT two -three~0.2", "field", 
        "(+field:one -field:two) -field:three~0.2");
		
		assertQueryMatch(qp, "one NOT two NOT three~0.2", "field", 
        "+field:one -field:two -field:three~0.2");

		assertQueryMatch(qp, "one two^0.5 three~0.2", "field", 
        "+field:one +field:two^0.5 +field:three~0.2");

		assertQueryMatch(qp, "one (two three)^0.8", "field", 
        "+field:one +((+field:two +field:three)^0.8)");

		assertQueryMatch(qp, "one (x:two three)^0.8", "field", 
        "+field:one +((+x:two +field:three)^0.8)");
		
		assertQueryMatch(qp, "one:(two three)^0.8", "field", 
        "+((one:two one:three)^0.8)");
		
		assertQueryMatch(qp, "-one:(two three)^0.8", "field", 
        "-((one:two one:three)^0.8)");
		
		assertQueryMatch(qp, "+one:(two three)^0.8", "field", 
        "+((one:two one:three)^0.8)");
		
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
        "z:{one TO five}");
		
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




	private class CannedTokenStream extends TokenStream {
		private int upto = 0;
		final PositionIncrementAttribute posIncr = addAttribute(PositionIncrementAttribute.class);
		final CharTermAttribute term = addAttribute(CharTermAttribute.class);

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
	}

	private class CannedAnalyzer extends Analyzer {
		@Override
		public TokenStream tokenStream(String ignored, Reader alsoIgnored) {
			return new CannedTokenStream();
		}
	}

}
