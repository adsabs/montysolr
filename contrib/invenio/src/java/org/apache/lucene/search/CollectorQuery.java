package org.apache.lucene.search;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.ToStringUtils;

public class CollectorQuery extends Query {

	private static final long serialVersionUID = -5670377581753190942L;
	Query query;
	Filter filter = null;
	Collector collector;

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
	public CollectorQuery(Query query, Filter filter, Collector collector) {
		this.query = query;
		this.filter = filter;
		this.collector = collector;
		
		if (collector == null) {
			throw new IllegalStateException("Collector must not be null");
		}
	}
	
	public CollectorQuery(Query query, Collector collector) {
		this.query = query;
		this.filter = null;
		this.collector = collector;
		
		if (collector == null) {
			throw new IllegalStateException("Collector must not be null");
		}
	}
	
	/**
	 * Returns a Weight that applies the filter to the enclosed query's Weight.
	 * This is accomplished by overriding the Scorer returned by the Weight.
	 */
	public Weight createWeight(final Searcher searcher) throws IOException {
		Weight weight = query.createWeight(searcher);
		Similarity similarity = query.getSimilarity(searcher);
		
		Weight w = new CollectorWeight(weight, similarity, collector);
		
		return w;
	}
	
//	public Weight createWeight(final Searcher searcher) throws IOException {
//		final Weight weight = query.createWeight(searcher);
//		final Similarity similarity = query.getSimilarity(searcher);
//		return new Weight() {
//			private float value;
//
//			// pass these methods through to enclosed query's weight
//			public float getValue() {
//				return value;
//			}
//
//			public float sumOfSquaredWeights() throws IOException {
//				return weight.sumOfSquaredWeights() * getBoost() * getBoost();
//			}
//
//			public void normalize(float v) {
//				weight.normalize(v);
//				value = weight.getValue() * getBoost();
//			}
//
//			public Explanation explain(IndexReader ir, int i)
//					throws IOException {
//				Explanation inner = weight.explain(ir, i);
//				if (getBoost() != 1) {
//					Explanation preBoost = inner;
//					inner = new Explanation(inner.getValue() * getBoost(),
//							"product of:");
//					inner.addDetail(new Explanation(getBoost(), "boost"));
//					inner.addDetail(preBoost);
//				}
//				Filter f = CollectorQuery.this.filter;
//				if (f != null) {
//					DocIdSet docIdSet = f.getDocIdSet(ir);
//					DocIdSetIterator docIdSetIterator = docIdSet == null ? DocIdSet.EMPTY_DOCIDSET
//							.iterator() : docIdSet.iterator();
//					if (docIdSetIterator == null) {
//						docIdSetIterator = DocIdSet.EMPTY_DOCIDSET.iterator();
//					}
//					if (docIdSetIterator.advance(i) == i) {
//						return inner;
//					} else {
//						Explanation result = new Explanation(0.0f,
//								"failure to match filter: " + f.toString());
//						result.addDetail(inner);
//						return result;
//					}
//				}
//				else {
//					Explanation result = new Explanation(0.0f,
//							"Filter is empty");
//					result.addDetail(inner);
//					return result;
//				}
//			}
//
//			// return this query
//			public Query getQuery() {
//				return CollectorQuery.this;
//			}
//
//			int searcherCounter;
//			Collector innerCollector = CollectorQuery.this.collector;
//			
//			
//			public Scorer scorer(IndexReader indexReader, boolean scoreDocsInOrder,
//					boolean topScorer) throws IOException {
//				
//				
//				if (searcherCounter > 0) {
//					return null;
//				}
//				searcherCounter++;
//				
//				
//				// we override the Scorer for the InvenioQuery
//				return new Scorer(similarity) {
//
//					private int doc = -1;
//					private int[] recids = null;
//					private int recids_counter = -1;
//					private int max_counter = -1;
//					private HashMap<String, Integer> recidToDocid;
//
//					public void score(Collector collector) throws IOException {
//						collector.setScorer(this);
//
//						int d;
//						while ((d = nextDoc()) != NO_MORE_DOCS) {
//							innerCollector.collect(d);
//						}
//					}
//
//
//					public int nextDoc() throws IOException {
//						// this is called only once
//						if (this.doc == -1) {
//							//internalCollector. TODO
//							if (recids == null || recids.length == 0) {
//								return doc = NO_MORE_DOCS;
//							}
//						}
//
//						recids_counter += 1;
//						if (recids_counter > max_counter) {
//							return doc = NO_MORE_DOCS;
//						}
//
//						try {
//							doc = recidToDocid.get(recids[recids_counter]);
//						}
//						catch (NullPointerException e) {
//							throw new IOException("Doc with recid=" + recids[recids_counter] + " is unknown to Lucene. You should reindex!");
//						}
//
//						return doc;
//					}
//
//					public int docID() {
//						return doc;
//					}
//
//					public int advance(int target) throws IOException {
//						while ((doc = nextDoc()) < target) {
//						}
//						return doc;
//					}
//
//					public float score() throws IOException {
//						assert doc != -1;
//						return CollectorQuery.this.getBoost() * 1.0f; // TODO: implementation of the
//														// scoring algorithm
//					}
//				};// Scorer
//			}// scorer
//			}
//		};
//	}

	/** Rewrites the wrapped query. */
	public Query rewrite(IndexReader reader) throws IOException {
		Query rewritten = query.rewrite(reader);
		if (rewritten != query) {
			CollectorQuery clone = (CollectorQuery) this.clone();
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
	
	public Collector getCollector() {
		return collector;
	}

	// inherit javadoc
	public void extractTerms(Set<Term> terms) {
		getQuery().extractTerms(terms);
	}

	/** Prints a user-readable version of this query. */
	public String toString(String s) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("CollectorQuery(");
		buffer.append(query.toString(s));
		buffer.append(", filter=" + (filter!=null ? filter.toString() : "null"));
		buffer.append(", collector=" + (collector!=null ? collector.toString() : "null"));
		buffer.append(")");
		buffer.append(ToStringUtils.boost(getBoost()));
		return buffer.toString();
	}

	/** Returns true iff <code>o</code> is equal to this. */
	public boolean equals(Object o) {
		if (o instanceof CollectorQuery) {
			CollectorQuery fq = (CollectorQuery) o;
			return (query.equals(fq.query) 
					&& (filter != null ? filter.equals(fq.filter) : true)
					&& collector.equals(fq.collector)
					&& getBoost() == fq.getBoost());
		}
		return false;
	}

	/** Returns a hash code value for this object. */
	public int hashCode() {
		if (filter != null) {
			return query.hashCode() ^ filter.hashCode() ^ collector.hashCode()
				+ Float.floatToRawIntBits(getBoost());
		}
		
		return query.hashCode() ^ collector.hashCode()
				+ Float.floatToRawIntBits(getBoost());
	}
}
