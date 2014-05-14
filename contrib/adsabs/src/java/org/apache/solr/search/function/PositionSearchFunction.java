package org.apache.solr.search.function;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermContext;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.Spans;

public class PositionSearchFunction extends ValueSource {
	private static final long serialVersionUID = -998231529379681734L;

	protected String field;
	protected String value;
	protected int start = 0;
	protected int end = 9999;
	protected HashMap<Integer, Float> subQueryHits;
	protected SpanTermQuery subQuery;

	public PositionSearchFunction(String field, String value, int start, int end) {
		this.field = field;
		this.value = value;
		this.start = start;
		this.end = end;
		subQuery = new SpanTermQuery(new Term(this.field, this.value));
	}
	
	public PositionSearchFunction(Term term, int start, int end) {
		this.field = term.field();
		this.value = term.text();
		this.start = start;
		this.end = end;
		subQuery = new SpanTermQuery(term);
	}

	protected String name() {
		return "author";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof PositionSearchFunction))
			return false;

		PositionSearchFunction that = (PositionSearchFunction) o;
		if (this.hashCode() == that.hashCode())
			return true;

		return false;
	}

	@Override
	public int hashCode() {
		int result = value.hashCode();
		result = 31 * result + start;
		result = 31 * result + end;
		return result;
	}

	@Override
	public String description() {
		StringBuilder sb = new StringBuilder();
		sb.append("pos").append('(');
		sb.append(field).append(",");
		sb.append(value).append(',');
		sb.append(start).append(",").append(end);
		sb.append(')');
		return sb.toString();
	}

	@Override
	public void createWeight(Map context, IndexSearcher searcher)
			throws IOException {
		// collect the docs that match
		subQueryHits = new HashMap<Integer, Float>(); // XXX: probably
														// inefficient, should
														// do better
		IndexReaderContext ctx = searcher.getTopReaderContext();

		Map<Term, TermContext> termContexts = new HashMap<Term, TermContext>();
		TreeSet<Term> extractedTerms = new TreeSet<Term>();
		subQuery.extractTerms(extractedTerms);
		for (Term term : extractedTerms) {
			termContexts.put(term, TermContext.build(ctx, term));
		}

		List<AtomicReaderContext> leaves = ctx.leaves();
		for (AtomicReaderContext leave : leaves) {
			Spans spans = subQuery.getSpans(leave,
					leave.reader().getLiveDocs(), termContexts);
			while (spans.next() == true) {
				int s = spans.start();
				int e = spans.end();
				if (s >= this.start && e <= this.end) { // all inclusive, shall
														// it
														// be overlap?
					subQueryHits.put(spans.doc(), (float) s);
					// spans.skipTo(spans.doc());
				}
			}
		}

		/**
		 * searcher.search(subQuery, new Collector() { private int base = 0;
		 * private Scorer scorer;
		 * 
		 * @Override public void setScorer(Scorer scorer) throws IOException {
		 *           this.scorer = scorer; }
		 * @Override public final void collect(int doc) throws IOException {
		 *           subQueryHits.put(doc + base, scorer.score()); }
		 * @Override public void setNextReader(IndexReader reader, int docBase)
		 *           { base = docBase; }
		 * @Override public boolean acceptsDocsOutOfOrder() { return true; } });
		 **/

	}

	@Override
	public FunctionValues getValues(Map context,
			AtomicReaderContext readerContext) throws IOException {
		return new FunctionValues() {

			public float floatVal(int doc) {
				// XXX: is docBase already included in the doc? or shall we do
				// st with context???
				if (subQueryHits.containsKey(doc)) {
					return 1.0f; // this could do nice weighting/scoring as well
				} else {
					return -1.0f;
					// return -Float.MAX_VALUE;
				}
			}

			public int intVal(int doc) {
				return (int) doubleVal(doc);
			}

			public long longVal(int doc) {
				return (long) doubleVal(doc);
			}

			public double doubleVal(int doc) {
				return (double) floatVal(doc);
			}

			public String toString(int doc) {
				StringBuilder sb = new StringBuilder();
				sb.append("pos").append('(');
				sb.append(subQuery.toString());
				sb.append(" * ").append(floatVal(doc));
				sb.append(')');
				return sb.toString();
			}
		};
	}

}
