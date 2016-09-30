package org.adsabs.lucene;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.payloads.SpanPayloadCheckQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.LuceneTestCase.SuppressCodecs;
import org.apache.lucene.util.TestUtil;

@SuppressCodecs({"Lucene3x", "SimpleText"})
public class BenchmarkAuthorSearch extends LuceneTestCase{
	private IndexSearcher searcher;
	private IndexReader reader;
	private Directory dir;
	private int numDocs = 10000;
	private int numQueries = 100;
	private boolean store = false;
	private long maxTime = 60*1000; // max time benchmark is allowed to run
	private ArrayList<ArrayList<Object>> timerStack = new ArrayList<ArrayList<Object>>();

	private String[] names = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "x",
			"john", "jay", "giovanni", "alberto", "edwin", "michael"};

	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		reader.close();
		dir.close();

		assertTrue(timerStack.size()==0);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();

		startTimer("Buiding index of " + numDocs + " docs");

		dir = newDirectory();
		RandomIndexWriter writer = new RandomIndexWriter(random(), dir,
				newIndexWriterConfig(new MultiFieldAnalyzer())
				.setMaxBufferedDocs(TestUtil.nextInt(random(), 50, 1000)));

		Document doc = new Document();
		FieldType customType = new FieldType(store ? TextField.TYPE_STORED : TextField.TYPE_NOT_STORED);
		customType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
		customType.setStoreTermVectors(true);
		customType.setStoreTermVectorOffsets(true);
		customType.setStoreTermVectorPayloads(true);
		customType.setStoreTermVectorPositions(true);

		Field id = newField("id", "", store ? StringField.TYPE_STORED : StringField.TYPE_NOT_STORED);
		Field original = newField("original", "", StringField.TYPE_STORED);
		Field regex = newField("regex", "", store ? StringField.TYPE_STORED : StringField.TYPE_NOT_STORED);
		Field wildcard = newField("wildcard", "", store ? StringField.TYPE_STORED : StringField.TYPE_NOT_STORED);
		Field payload = newField("vectrfield", "", customType);
		Field n0 = newField("n0", "", store ? StringField.TYPE_STORED : StringField.TYPE_NOT_STORED);
		Field n1 = newField("n1", "", store ? StringField.TYPE_STORED : StringField.TYPE_NOT_STORED);
		Field n2 = newField("n2", "", store ? StringField.TYPE_STORED : StringField.TYPE_NOT_STORED);
		Field n3 = newField("n3", "", store ? StringField.TYPE_STORED : StringField.TYPE_NOT_STORED);
		Field n4 = newField("n4", "", store ? StringField.TYPE_STORED : StringField.TYPE_NOT_STORED);
		
		
		
		doc.add(id);
		doc.add(original);
		doc.add(regex);
		doc.add(wildcard);
		doc.add(payload);
		doc.add(n0);
		doc.add(n1);
		doc.add(n2);
		doc.add(n3);
		doc.add(n4);

		Field[] nFields = {n0, n1, n2, n3, n4};
		Field[] myFields = {id,original, regex, wildcard, payload, n0, n1, n2, n3, n4};
		String surname;

		for (int i = 0; i < numDocs; i++) {
			for (Field f: myFields) {
				f.setStringValue("");
			}

			StringBuilder name = new StringBuilder();
			StringBuilder wild = new StringBuilder();

			//surname
			do {
				surname = TestUtil.randomSimpleString(random()).toLowerCase().replace(",", "").trim();
			} while (surname.length() == 0);

			name.append(surname);
			name.append(", ");
			wild.append(surname);
			wild.append(", ");
			n0.setStringValue(surname);

			//#initials
			int noi = TestUtil.nextInt(random(), 0, 4);
			for (int j = 0; j < noi; j++) {
				String namePart = names[TestUtil.nextInt(random(), 0, names.length-1)];
				name.append(namePart);
				name.append(" ");
				wild.append(namePart);
				wild.append(j+1);
				wild.append(" ");
				nFields[j+1].setStringValue(namePart);
			}

			original.setStringValue(name.toString());
			regex.setStringValue(name.toString());
			wildcard.setStringValue(wild.toString());
			payload.setStringValue(name.toString());
			id.setStringValue(Integer.toString(i));

			writer.addDocument(doc);
		}


		reader = writer.getReader();
		writer.close();
		searcher = newSearcher(reader);

		stopTimer();
	}


	private void startTimer(String message) {
		ArrayList<Object> l = new ArrayList<Object>();
		l.add(System.currentTimeMillis());
		l.add(message);
		timerStack.add(l);
	}

	private long stopTimer() {
		ArrayList<Object> l = timerStack.remove(timerStack.size()-1);
		long endTime = System.currentTimeMillis();
		long startTime = (Long) l.get(0);
		String msg = (String) l.get(1);

		long resTime = endTime - startTime;

		StringBuilder out = new StringBuilder();
		for (int i=0;i<timerStack.size();i++) {
			out.append("\t");
		}
		out.append(resTime);
		out.append("ms.  " + msg);
		System.out.println(out.toString());

		return resTime;
	}
	
	private void appendToTimer(String msg) {
		timerStack.get(timerStack.size()-1).set(1, (timerStack.get(timerStack.size()-1).get(1) + " -- " + msg));
	}
	
	/**
	 * This Analyzer uses an WhitespaceTokenizer and PayloadFilter, OR KeywordTokenizer for
	 * other queries
	 */
	private static class MultiFieldAnalyzer extends Analyzer {

		public MultiFieldAnalyzer() {
			super(PER_FIELD_REUSE_STRATEGY);
		}

		public MultiFieldAnalyzer(String field, byte[] data, int offset, int length) {
			super(PER_FIELD_REUSE_STRATEGY);
		}


		@Override
		public TokenStreamComponents createComponents(String fieldName) {

			if (fieldName.contains("vectrfield")){
				Tokenizer result = new MockTokenizer(MockTokenizer.SIMPLE, true);
				return new TokenStreamComponents(result, new SimplePayloadFilter(result));
			}

			Tokenizer result = new MockTokenizer(MockTokenizer.KEYWORD, true);
			return new TokenStreamComponents(result, result);

		}

	}


	/**
	 * This Filter adds payloads to the tokens.
	 */
	static final class SimplePayloadFilter extends TokenFilter {
		int pos;
		final PayloadAttribute payloadAttr;
		final CharTermAttribute termAttr;

		public SimplePayloadFilter(TokenStream input) {
			super(input);
			pos = 0;
			payloadAttr = input.addAttribute(PayloadAttribute.class);
			termAttr = input.addAttribute(CharTermAttribute.class);
		}

		@Override
		public boolean incrementToken() throws IOException {
			if (input.incrementToken()) {
				payloadAttr.setPayload(new BytesRef((Integer.toString(pos)).getBytes("UTF-8")));  //save the position 0 = surname,1,2,3,4....
				pos++;
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void reset() throws IOException {
			super.reset();
			pos = 0;
		}
	}
  
	public void testBenchMarkAll() throws Exception {


		int[] randomIds = getRandomIds(100);

		startTimer("Verifying data integrity with " + randomIds.length + " docs");
		verifySearch(randomIds);
		stopTimer();

		startTimer("Preparing " + numQueries + " random queries");
		randomIds = getRandomIds(numQueries);
		List<TestCase> testCases = getIndexData(randomIds);
		stopTimer();

		ArrayList<Integer> totals = new ArrayList<Integer>();

		System.out.println("\nExamples of queries:\n--------------------");
		int e = 0;
		int oldLen = 0;
		for (int ii = 0; ii<10;ii++) {
			if (testCases.get(ii).parts.length > oldLen) {
				e = ii;
				oldLen = testCases.get(ii).parts.length;
			}
		}
		for (Query q: buildQueries(testCases.get(e).parts)) {
			System.out.println(q);
		}
		System.out.println("");


		startTimer("Regexp queries (new style, using \\w*)");
    totals.add(runQueries(testCases, new QueryBuilder() {
      public Query getQuery(TestCase t) throws Exception {
        return getRegexpQuerySameAsRegex(t.parts, t.howMany, t.truncate);
      }
    }));
    stopTimer();
    
		startTimer("Regexp queries (new style, using [^\\s]*)");
		totals.add(runQueries(testCases, new QueryBuilder() {
			public Query getQuery(TestCase t) throws Exception {
				return getRegexpQuery(t.parts, t.howMany, t.truncate);
			}
		}));
		stopTimer();

		startTimer("Wildcard queries");
		totals.add(runQueries(testCases, new QueryBuilder() {
			public Query getQuery(TestCase t) throws Exception {
				return getWildcardQuery(t.parts, t.howMany, t.truncate);
			}
		}));
		stopTimer();

		startTimer("Boolean queries");
		totals.add(runQueries(testCases, new QueryBuilder() {
			public Query getQuery(TestCase t) throws Exception {
				return getBooleanQuery(t.parts, t.howMany, t.truncate);
			}
		}));
		stopTimer();
		
		startTimer("Boolean queries (truncated)");
		totals.add(runQueries(testCases, new QueryBuilder() {
			public Query getQuery(TestCase t) throws Exception {
				return getBooleanQuery(t.parts, t.howMany, true);
			}
		}));
		stopTimer();
		
		startTimer("Span queries");
		totals.add(runQueries(testCases, new QueryBuilder() {
			public Query getQuery(TestCase t) throws Exception {
				return getSpanQuery(t.parts, t.howMany, false);
			}
		}));
		stopTimer();
		
		startTimer("Span queries (truncated)");
		totals.add(runQueries(testCases, new QueryBuilder() {
			public Query getQuery(TestCase t) throws Exception {
				return getSpanQuery(t.parts, t.howMany, true);
			}
		}));
		stopTimer();
		
		
		startTimer("Payload queries");
		totals.add(runQueries(testCases, new QueryBuilder() {
			public Query getQuery(TestCase t) throws Exception {
				return getPayloadQuery(t.parts, t.howMany, false);
			}
		}));
		stopTimer();

		startTimer("Payload queries (truncated)");
		totals.add(runQueries(testCases, new QueryBuilder() {
			public Query getQuery(TestCase t) throws Exception {
				return getPayloadQuery(t.parts, t.howMany, true);
			}
		}));
		stopTimer();

		System.out.println("Totals: " + totals);
	}

	class QueryBuilder {
		public Query getQuery(TestCase t) throws Exception {
			return new TermQuery(new Term("original", t.original));
		}
	}
	
	
	private int runQueries(List<TestCase> testCases, QueryBuilder builder) throws Exception {
		
		long start = System.currentTimeMillis(); // TODO: make smarter
		     
		int total = 0;
		int rounds = 0;
		for (TestCase t: testCases) {
			total  += searcher.search(builder.getQuery(t), 1).totalHits;
			rounds++;
			if (rounds % 50 == 0 && (System.currentTimeMillis() - start) > maxTime) {
				appendToTimer("Stopping execution, # queries finished: " + rounds);
				return total;
			}
		}
		return total;
	}
	

	class TestCase {
		public String original;
		public String[] parts;
		public int howMany;
		public boolean truncate = false;

		TestCase(String original, String[] parts, int howMany) {
			this.original = original;
			this.parts = parts;
			this.howMany = howMany;
		}
	}
	
	
	private List<TestCase> getIndexData(int[] randomIds) throws IOException {
		ArrayList<TestCase> data = new ArrayList<TestCase>(randomIds.length);
		for (int i = 0; i < randomIds.length; i++) {
			TopDocs docs = searcher.search(new TermQuery(new Term("id", Integer.toString(randomIds[i]))), 1);
			Document doc = reader.document(docs.scoreDocs[0].doc);
			String original = doc.get("original").toString();
			String[] parts = original.split("\\,? ");
			int howMany = TestUtil.nextInt(random(), 0, parts.length-1); // how many initials
			data.add(new TestCase(original, parts, howMany));
		}
		return data;
	}

	private void verifySearch(int[] randomIds) throws IOException {
		for (int i = 0; i < randomIds.length; i++) {
			TopDocs docs = searcher.search(new TermQuery(new Term("id", Integer.toString(randomIds[i]))), 1);
			if (docs.totalHits == 1) {
				Document doc = reader.document(docs.scoreDocs[0].doc);
				String original = doc.getField("original").stringValue();
				String[] parts = original.split("\\,? ");
				Query[] queries = buildQueries(parts);
				if (queries == null)
				  continue;
				TermQuery oq = new TermQuery(new Term("original", original));
				int ho = searcher.search(oq, 1).totalHits;
				for (Query q: queries) {
				  if (q == null) continue;
					Builder bq = new BooleanQuery.Builder();
					bq.add(q, Occur.MUST);
					bq.add(new TermQuery(new Term("id", Integer.toString(randomIds[i]))), Occur.MUST);
					if (q != null) {
					  System.out.println(q.toString());
						int no = searcher.search(bq.build(), 1).totalHits;
						if (no != 1) {
							System.out.println("Results differ: " + oq + " <<>> " + q + "   [" + ho + " : " + no + "]");
							if (store == true) {
								System.out.println("wildcard: \"" + doc.getField("wildcard").stringValue()  + "\"");
								System.out.println("regex: \"" + doc.getField("regex").stringValue() + "\"");
								System.out.println("vectrfield: \"" + doc.getField("vectrfield").stringValue() + "\"");
								System.out.println("n0: \"" + doc.getField("n0").stringValue() + "\"");
								System.out.println("n1: \"" + doc.getField("n1").stringValue() + "\"");
								System.out.println("n2: \"" + doc.getField("n2").stringValue() + "\"");
								System.out.println("n3: \"" + doc.getField("n3").stringValue() + "\"");
								System.out.println("n4: \"" + doc.getField("n4").stringValue() + "\"");
							}
						}
						//assertEquals(ho, no);
					}
				}
			}
		}

	}

	private Query[] buildQueries(String[] parts) throws UnsupportedEncodingException {
		int howMany = TestUtil.nextInt(random(), 2, parts.length-1); // how many initials
		if (howMany < 2)
		  return null;
		Query[] queries = new Query[9];
		queries[1] = getRegexpQuery(parts, howMany, false);
		queries[2] = getWildcardQuery(parts, howMany, false);
		queries[3] = getBooleanQuery(parts, howMany, false);
		queries[4] = getBooleanQuery(parts, howMany, true);
		
		queries[5] = getSpanQuery(parts, howMany, false);
		queries[6] = getSpanQuery(parts, howMany, true);
		queries[7] = getPayloadQuery(parts, howMany, false);
		queries[8] = getPayloadQuery(parts, howMany, true);
		
		return queries;
	}
	
	
	private Query getSpanQuery(String[] parts, int howMany, boolean truncate) throws UnsupportedEncodingException {
		SpanQuery[] clauses = new SpanQuery[howMany+1];
		clauses[0] = new SpanTermQuery(new Term("vectrfield", parts[0])); // surname
		for (int i = 0; i < howMany; i++) {
			if (truncate) {
				clauses[i+1] = new SpanMultiTermQueryWrapper<WildcardQuery>(new WildcardQuery(new Term("vectrfield", parts[i+1].substring(0, 1) + "*")));
			}
			else {
				clauses[i+1] = new SpanTermQuery(new Term("vectrfield", parts[i+1]));
			}
		}
		SpanNearQuery sq = new SpanNearQuery(clauses, 0, true); // match in order
		return sq;
	}
	
	private Query getPayloadQuery(String[] parts, int howMany, boolean truncate) throws UnsupportedEncodingException {
		List<BytesRef> payloads = new ArrayList<BytesRef>(howMany+1);
		BytesRef pay = new BytesRef((Integer.toString(0)).getBytes("UTF-8"));
		payloads.add(pay);

		SpanQuery[] clauses = new SpanQuery[howMany+1];
		clauses[0] = new SpanTermQuery(new Term("vectrfield", parts[0])); // surname
		for (int i = 0; i < howMany; i++) {
			if (truncate) {
				//clauses[i+1] = new SpanMultiTermQueryWrapper<WildcardQuery>(new WildcardQuery(new Term("vectrfield", parts[i+1].substring(0, 1) + "*")));
				clauses[i+1] = new SpanMultiTermQueryWrapper<PrefixQuery>(new PrefixQuery(new Term("vectrfield", parts[i+1].substring(0, 1))));
			}
			else {
				clauses[i+1] = new SpanTermQuery(new Term("vectrfield", parts[i+1]));
			}
			payloads.add(new BytesRef((Integer.toString(i+1)).getBytes("UTF-8")));
		}
		SpanNearQuery sq = new SpanNearQuery(clauses, 1, true); // match in order
		return new SpanPayloadCheckQuery(sq, payloads);
	}

	private Query getBooleanQuery(String[] parts, int howMany, boolean truncate) {
		Builder bq = new BooleanQuery.Builder();
		bq.add(new TermQuery(new Term("n0", parts[0])), BooleanClause.Occur.MUST);
		for (int i = 1; i < howMany+1; i++) {
			if (truncate) {
				bq.add(new WildcardQuery(new Term("n"+i, parts[i].substring(0,1) + "*")), BooleanClause.Occur.MUST);
			}
			else {
				bq.add(new TermQuery(new Term("n"+i, parts[i])), BooleanClause.Occur.MUST);
			}
		}
		return bq.build();
	}

	private Query getWildcardQuery(String[] parts, int howMany, boolean truncate) {
		return new WildcardQuery(new Term("wildcard", getWildcardQueryString(parts, howMany, truncate)));
	}

	private String getWildcardQueryString(String[] parts, int howMany, boolean truncate) {
		StringBuilder p = new StringBuilder();
		p.append(parts[0]);
		p.append(", ");
		int i = 0;
		for (; i < howMany && parts.length > i; i++) {
			String x = truncate ? parts[i+1].substring(0, 1) : parts[i+1];
			p.append(x + "*" + (i+1) + " ");
		}
		if (parts.length > i) {
			p.append("*");
		}
		return p.toString();
	}

	private Query getRegexpQuerySameAsRegex(String[] parts, int howMany, boolean truncate) {
    return new RegexpQuery(new Term("regex", getRegexQueryString(parts, howMany, truncate)));
  }
	
	private Query getRegexpQuery(String[] parts, int howMany, boolean truncate) {
		return new RegexpQuery(new Term("regex", getRegexpQueryString(parts, howMany, truncate)));
	}

	
	private String getRegexpQueryString(String[] parts, int howMany, boolean truncate) {
    StringBuilder p = new StringBuilder();
    p.append(parts[0]);
    p.append(", ");
    int i = 0;
    for (; i < howMany && parts.length > i; i++) {
      String x = truncate ? parts[i+1].substring(0, 1) : parts[i+1];
      p.append(x + "[^\\s]* ");
    }
    if (parts.length > i) {
      p.append(".*");
    }
    return p.toString();
  }
	
	private String getRegexQueryString(String[] parts, int howMany, boolean truncate) {
		StringBuilder p = new StringBuilder();
		p.append(parts[0]);
		p.append(", ");
		int i = 0;
		for (; i < howMany && parts.length > i; i++) {
			String x = truncate ? parts[i+1].substring(0, 1) : parts[i+1];
			p.append(x + "\\w* ");
		}
		if (parts.length > i) {
			p.append(".*");
		}
		return p.toString();
	}

	private int[] getRandomIds(int i) {
		int[] randomIds = new int[Math.min(numDocs, i)];
		for (int j = 0; j < randomIds.length; j++) {
			randomIds[j] = TestUtil.nextInt(random(), 0, numDocs-1);
		}
		return randomIds;
	}


}
