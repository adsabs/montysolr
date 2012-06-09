package org.apache.lucene.search;

import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.Weight;
import org.apache.lucene.util.ToStringUtils;

import java.io.IOException;

import java.util.Map;
import java.util.Set;
import java.util.BitSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;



public class CitationQueryCites extends Query {
	private static final long serialVersionUID = 5816756548145074263L;
	private float boost = 1.0f; // query boost factor
	Query query = null;
	Map<Integer, int[]> citationMap = null;


	public CitationQueryCites (Query query, Map<Integer, int[]> citationMap) {
		this.query = query;
		this.citationMap = citationMap;
    }

	/**
	 * Sets the boost for this query clause to <code>b</code>. Documents
	 * matching this clause will (in addition to the normal weightings) have
	 * their score multiplied by <code>b</code>.
	 */
	public void setBoost(float b) {
		boost = b;
	}

	/**
	 * Gets the boost for this clause. Documents matching this clause will (in
	 * addition to the normal weightings) have their score multiplied by
	 * <code>b</code>. The boost is 1.0 by default.
	 */
	public float getBoost() {
		return boost + query.getBoost();
	}


	/**
	 * Expert: Constructs an appropriate Weight implementation for this query.
	 *
	 * <p>
	 * Only implemented by primitive queries, which re-write to themselves.
	 */
	public Weight createWeight(Searcher searcher) throws IOException {

		final Weight weight = query.createWeight (searcher);
	    final Similarity similarity = query.getSimilarity(searcher);



	    return new Weight() {
	      private float value;


	      // return a filtering scorer
	      public Scorer scorer(IndexReader indexReader, boolean scoreDocsInOrder, boolean topScorer)
	          throws IOException {

	        final Scorer scorer = weight.scorer(indexReader, true, false);
	        final IndexReader reader = indexReader;

	        if (scorer == null) {
	          return null;
	        }


	        // we override the Scorer for the CitationQuery
	        return new Scorer(similarity) {

	          private int doc = -1;

	          public void getCache() {
	        	  System.out.println(reader);

	          }

	          // here is the core of the processing
	          public void score(Collector collector) throws IOException {
	        	    collector.setScorer(this);
	        	    int doc;

	        	    //TODO: we could as well collect the first matching documents
	        	    //and based on them retrieve all the citations. But probably
	        	    //that is not correct, because the citation search wants to
	        	    //retrieve the documents that are most cited/referred, therefore
	        	    //we have to search the whole space

	        	    BitSet aHitSet = new BitSet(reader.maxDoc());

	        	    if (citationMap.size() == 0)
	        	    	return;

	        	    // retrieve documents that matched the query and while we go
	        	    // collect the documents referenced by/from those docs
	        	    while ((doc = nextDoc()) != NO_MORE_DOCS) {
	        	    	if (citationMap.containsKey(doc)) {
	        	    		int[] v = citationMap.get(doc);
	        	    		for (int i: v)
	        	    			aHitSet.set(i);
	        	    	}
	        	    }

	        	    // now collect the big set of citing relations
	        	    doc = 0;
	        	    while ((doc = aHitSet.nextSetBit(doc)) != -1) {
	        	      collector.collect(doc);
	        	      doc += 1;
	        	    }
	        	  }

	          public int nextDoc() throws IOException {
	        	  return scorer.nextDoc();
	          }

	          public int docID() { return doc; }

	          /** @deprecated use {@link #advance(int)} instead. */
	          @Deprecated
	          public boolean skipTo(int i) throws IOException {
	            return advance(i) != NO_MORE_DOCS;
	          }

	          public int advance(int target) throws IOException {
	        	  return scorer.advance(target);
	          }

	          //public float score() throws IOException { return getBoost() * scorer.score(); }
	          public float score() throws IOException { return getBoost() * 1.0f; }
	        };// Scorer
	      }// scorer

	      // pass these methods through to enclosed query's weight
	      public float getValue() { return value; }

	      public float sumOfSquaredWeights() throws IOException {
	        return weight.sumOfSquaredWeights() * getBoost() * getBoost();
	      }

	      public void normalize (float v) {
	        weight.normalize(v);
	        value = weight.getValue() * getBoost();
	      }

	      public Explanation explain (IndexReader ir, int i) throws IOException {
	        Explanation inner = weight.explain (ir, i);
	        if (getBoost()!=1) {
	          Explanation preBoost = inner;
	          inner = new Explanation(inner.getValue()*getBoost(),"product of:");
	          inner.addDetail(new Explanation(getBoost(),"boost"));
	          inner.addDetail(preBoost);
	        }
	        inner.addDetail(new Explanation(0.0f, "TODO: add citation formula details"));
	        return inner;
	      }

	      // return this query
	      public Query getQuery() { return CitationQueryCites.this; }

	    }; //Weight
	  }

	/**
	 * Expert: Constructs and initializes a Weight for a top-level query.
	 */
	public Weight weight(Searcher searcher) throws IOException {
		Query query = searcher.rewrite(this);
		Weight weight = query.createWeight(searcher);
		float sum = weight.sumOfSquaredWeights();
		float norm = getSimilarity(searcher).queryNorm(sum);
		if (Float.isInfinite(norm) || Float.isNaN(norm))
			norm = 1.0f;
		weight.normalize(norm);
		return weight;
	}

	/**
	 * Expert: called to re-write queries into primitive queries. For example, a
	 * PrefixQuery will be rewritten into a BooleanQuery that consists of
	 * TermQuerys.
	 */
	public Query rewrite(IndexReader reader) throws IOException {
	    Query rewritten = query.rewrite(reader);
	    if (rewritten != query) {
	      CitationQueryCites clone = (CitationQueryCites)this.clone();
	      clone.query = rewritten;
	      return clone;
	    } else {
	      return this;
	    }
	  }

	/**
	 * Expert: called when re-writing queries under MultiSearcher.
	 *
	 * Create a single query suitable for use by all subsearchers (in 1-1
	 * correspondence with queries). This is an optimization of the OR of all
	 * queries. We handle the common optimization cases of equal queries and
	 * overlapping clauses of boolean OR queries (as generated by
	 * MultiTermQuery.rewrite()). Be careful overriding this method as
	 * queries[0] determines which method will be called and is not necessarily
	 * of the same type as the other queries.
	 */
	public Query combine(Query[] queries) {
		return query.combine(queries);

	}

	/**
	 * Expert: adds all terms occurring in this query to the terms set. Only
	 * works if this query is in its {@link #rewrite rewritten} form.
	 *
	 * @throws UnsupportedOperationException
	 *             if this query is not yet rewritten
	 */
	public void extractTerms(Set<Term> terms) {
		query.extractTerms(terms);
	}


	/**
	 * Expert: Returns the Similarity implementation to be used for this query.
	 * Subclasses may override this method to specify their own Similarity
	 * implementation, perhaps one that delegates through that of the Searcher.
	 * By default the Searcher's Similarity implementation is returned.
	 */
	public Similarity getSimilarity(Searcher searcher) {
		return searcher.getSimilarity();
	}



	/** Prints a user-readable version of this query. */
	  public String toString (String s) {
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("CitationQuery(");
	    buffer.append(query.toString(s));
	    buffer.append(")->");
	    buffer.append(ToStringUtils.boost(getBoost()));
	    return buffer.toString();
	  }

	  /** Returns true iff <code>o</code> is equal to this. */
	  public boolean equals(Object o) {
	    if (o instanceof CitationQueryCites) {
	    	CitationQueryCites fq = (CitationQueryCites) o;
	      return (query.equals(fq.query) && getBoost()==fq.getBoost());
	    }
	    return false;
	  }

	  /** Returns a hash code value for this object. */
	  public int hashCode() {
	    return query.hashCode() ^ Float.floatToRawIntBits(getBoost());
	  }
}
