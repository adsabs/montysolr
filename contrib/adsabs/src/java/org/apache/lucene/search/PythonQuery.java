package org.apache.lucene.search;

import monty.solr.jni.MontySolrVM;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.Weight;
import org.apache.lucene.util.BytesRef;


import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;


/**
 * {@link InvenioQuery} extends {@link Query} by adding ability 
 * to query and retrieve data from the Python process using
 * {@link MontySolrVM}
 * 
 * The real work is done by the {@link PythonQueryWeight} class,
 * which queries the underlying Python process and returns
 * the doc ids (they will be translated from the Invenio recids
 * into lucene docids) on the fly.
 * 
 */

public class PythonQuery extends Query {


	private static final long serialVersionUID = -5151676153419588281L;
	
	private float boost = 1.0f; // query boost factor
	protected Query query;
	protected String pythonResponder = null;
	
	boolean dieOnMissingIds;
	SolrCacheWrapper cache;
	
	public PythonQuery(Query query, SolrCacheWrapper cache, boolean dieOnMissingIds) {
		this(query, cache, dieOnMissingIds, null);
	}
	
	public PythonQuery(Query query, SolrCacheWrapper cache, boolean dieOnMissingIds, String pythonResponder) {
		super();
		if (pythonResponder != null) {
			this.pythonResponder = pythonResponder;
		}
		this.dieOnMissingIds = dieOnMissingIds;
		assert cache != null;
		this.cache = cache;
		this.query = query;
	}

	/**
	 * Sets the boost for this query clause to <code>b</code>. Documents
	 * matching this clause will (in addition to the normal weightings) have
	 * their score multiplied by <code>b</code>.
	 */
	public void setBoost(float b) {
		query.setBoost(b);
	}

	/**
	 * Gets the boost for this clause. Documents matching this clause will (in
	 * addition to the normal weightings) have their score multiplied by
	 * <code>b</code>. The boost is 1.0 by default.
	 */
	public float getBoost() {
		return query.getBoost();
	}

	/**
	 * Expert: Constructs an appropriate Weight implementation for this query.
	 *
	 * <p>
	 * Only implemented by primitive queries, which re-write to themselves.
	 */
	public Weight createWeight(IndexSearcher searcher) throws IOException {

		PythonQueryWeight w = new PythonQueryWeight((IndexSearcher) searcher, this, cache, dieOnMissingIds);
		if (pythonResponder != null) {
			w.setPythonFunctionName(pythonResponder);
		}
		return w;
	}


	/**
	 * Expert: called to re-write queries into primitive queries. For example, a
	 * PrefixQuery will be rewritten into a BooleanQuery that consists of
	 * TermQuerys.
	 */
	public Query rewrite(IndexReader reader) throws IOException {
		Query rewritten = query.rewrite(reader);
		if (rewritten != query) {
			PythonQuery clone = (PythonQuery) this.clone();
			clone.query = rewritten;
			return clone;
		} else {
			return this;
		}
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


	/** Prints a user-readable version of this query. */
	public String toString(String s) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("PythonQuery(");
		buffer.append(getQueryAsString());
		buffer.append(")");
		return buffer.toString();
	}

	/** Returns true iff <code>o</code> is equal to this. */
	public boolean equals(Object o) {
		if (o instanceof PythonQuery) {
			PythonQuery fq = (PythonQuery) o;
			return (query.equals(fq.query) && getBoost() == fq.getBoost());
		}
		return false;
	}

	/** Returns a hash code value for this object. */
	public int hashCode() {
		return query.hashCode() ^ Float.floatToRawIntBits(getBoost());
	}

	
	public Query getInnerQuery() {
		return query;
	}

	/* you can provide a specific logic here that translates the 
	 * query into a syntax of the target system; we'll send them
	 * the query as a string andd the collector will retrieve
	 * results from them
	 */
	public String getQueryAsString() {
	  
	  return query.toString();
  }
}

