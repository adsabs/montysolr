package org.apache.lucene.search;

import java.io.IOException;
import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.ToStringUtils;

public class CitationQueryCitedBy extends Query {

	private static final long serialVersionUID = -5670377581753190942L;
	Query query;
	Filter filter;

	/**
	 * Constructs a new query which applies a filter to the results of the
	 * original query. Filter.getDocIdSet() will be called every time this query
	 * is used in a search.
	 * 
	 * @param query
	 *            Query to be filtered, cannot be <code>null</code>.
	 * @param filter
	 *            Filter to apply to query results, cannot be <code>null</code>.
	 */
	public CitationQueryCitedBy(Query query, Filter filter) {
		this.query = query;
		this.filter = filter;
	}

	/**
	 * Returns a Weight that applies the filter to the enclosed query's Weight.
	 * This is accomplished by overriding the Scorer returned by the Weight.
	 */
	public Weight createWeight(final Searcher searcher) throws IOException {
		final Weight weight = query.createWeight(searcher);
		final Similarity similarity = query.getSimilarity(searcher);
		return new Weight() {
			private float value;

			// pass these methods through to enclosed query's weight
			public float getValue() {
				return value;
			}

			public float sumOfSquaredWeights() throws IOException {
				return weight.sumOfSquaredWeights() * getBoost() * getBoost();
			}

			public void normalize(float v) {
				weight.normalize(v);
				value = weight.getValue() * getBoost();
			}

			public Explanation explain(IndexReader ir, int i)
					throws IOException {
				Explanation inner = weight.explain(ir, i);
				if (getBoost() != 1) {
					Explanation preBoost = inner;
					inner = new Explanation(inner.getValue() * getBoost(),
							"product of:");
					inner.addDetail(new Explanation(getBoost(), "boost"));
					inner.addDetail(preBoost);
				}
				Filter f = CitationQueryCitedBy.this.filter;
				DocIdSet docIdSet = f.getDocIdSet(ir);
				DocIdSetIterator docIdSetIterator = docIdSet == null ? DocIdSet.EMPTY_DOCIDSET
						.iterator() : docIdSet.iterator();
				if (docIdSetIterator == null) {
					docIdSetIterator = DocIdSet.EMPTY_DOCIDSET.iterator();
				}
				if (docIdSetIterator.advance(i) == i) {
					return inner;
				} else {
					Explanation result = new Explanation(0.0f,
							"failure to match filter: " + f.toString());
					result.addDetail(inner);
					return result;
				}
			}

			// return this query
			public Query getQuery() {
				return CitationQueryCitedBy.this;
			}

			// return a filtering scorer
			public Scorer scorer(IndexReader indexReader,
					boolean scoreDocsInOrder, boolean topScorer)
					throws IOException {
				final Scorer scorer = weight.scorer(indexReader, true, false);
				if (scorer == null) {
					return null;
				}
				DocIdSet docIdSet = filter.getDocIdSet(indexReader);
				if (docIdSet == null) {
					return null;
				}
				final DocIdSetIterator docIdSetIterator = docIdSet.iterator();
				if (docIdSetIterator == null) {
					return null;
				}

				return new Scorer(similarity) {

					private int doc = -1;

					private int advanceToCommon(int scorerDoc, int disiDoc)
							throws IOException {
						while (scorerDoc != disiDoc) {
							if (scorerDoc < disiDoc) {
								scorerDoc = scorer.advance(disiDoc);
							} else {
								disiDoc = docIdSetIterator.advance(scorerDoc);
							}
						}
						return scorerDoc;
					}

					public void score(Collector collector) throws IOException {
						collector.setScorer(this);
						int doc;
						while ((doc = nextDoc()) != NO_MORE_DOCS) {
							collector.collect(doc);
						}
					}

					/** @deprecated use {@link #nextDoc()} instead. */
					@Deprecated
					public boolean next() throws IOException {
						return nextDoc() != NO_MORE_DOCS;
					}

					public int nextDoc() throws IOException {
						int scorerDoc, disiDoc;
						return doc = (disiDoc = docIdSetIterator.nextDoc()) != NO_MORE_DOCS
								&& (scorerDoc = scorer.nextDoc()) != NO_MORE_DOCS
								&& advanceToCommon(scorerDoc, disiDoc) != NO_MORE_DOCS ? scorer
								.docID() : NO_MORE_DOCS;
					}

					public int docID() {
						return doc;
					}

					/** @deprecated use {@link #advance(int)} instead. */
					@Deprecated
					public boolean skipTo(int i) throws IOException {
						return advance(i) != NO_MORE_DOCS;
					}

					public int advance(int target) throws IOException {
						int disiDoc, scorerDoc;
						return doc = (disiDoc = docIdSetIterator
								.advance(target)) != NO_MORE_DOCS
								&& (scorerDoc = scorer.advance(disiDoc)) != NO_MORE_DOCS
								&& advanceToCommon(scorerDoc, disiDoc) != NO_MORE_DOCS ? scorer
								.docID() : NO_MORE_DOCS;
					}

					public float score() throws IOException {
						return getBoost() * scorer.score();
					}

					// add an explanation about whether the document was
					// filtered
					public Explanation explain(int i) throws IOException {
						Explanation exp = new Explanation();
						// FIX: use the IndexReader.explain()
						// Weight.explain(indexReader, i);
						// Explanation exp = scorer.explain(i);

						if (docIdSetIterator.advance(i) == i) {
							exp.setDescription("allowed by filter: "
									+ exp.getDescription());
							exp.setValue(getBoost() * exp.getValue());
						} else {
							exp.setDescription("removed by filter: "
									+ exp.getDescription());
							exp.setValue(0.0f);
						}
						return exp;
					}
				};
			}
		};
	}

	/** Rewrites the wrapped query. */
	public Query rewrite(IndexReader reader) throws IOException {
		Query rewritten = query.rewrite(reader);
		if (rewritten != query) {
			CitationQueryCitedBy clone = (CitationQueryCitedBy) this.clone();
			clone.query = rewritten;
			return clone;
		} else {
			return this;
		}
	}

	public Query getQuery() {
		return query;
	}

	public Filter getFilter() {
		return filter;
	}

	// inherit javadoc
	public void extractTerms(Set<Term> terms) {
		getQuery().extractTerms(terms);
	}

	/** Prints a user-readable version of this query. */
	public String toString(String s) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("filtered(");
		buffer.append(query.toString(s));
		buffer.append(")->");
		buffer.append(filter);
		buffer.append(ToStringUtils.boost(getBoost()));
		return buffer.toString();
	}

	/** Returns true iff <code>o</code> is equal to this. */
	public boolean equals(Object o) {
		if (o instanceof CitationQueryCitedBy) {
			CitationQueryCitedBy fq = (CitationQueryCitedBy) o;
			return (query.equals(fq.query) && filter.equals(fq.filter) && getBoost() == fq
					.getBoost());
		}
		return false;
	}

	/** Returns a hash code value for this object. */
	public int hashCode() {
		return query.hashCode() ^ filter.hashCode()
				+ Float.floatToRawIntBits(getBoost());
	}
}
