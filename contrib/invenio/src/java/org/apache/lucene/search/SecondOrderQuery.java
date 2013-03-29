package org.apache.lucene.search;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.ToStringUtils;


/**
 * 
 * This is a 2nd attempt on the 2nd order operators, while it works, 
 * it will work only for searching inside the boundaries of index
 * segments.
 * 
 */
public class SecondOrderQuery extends Query {

	private static final long serialVersionUID = -5670377581753190942L;
	Query firstOrderQuery;
	Filter filter = null;
	private SecondOrderCollector secondOrderCollector;
	private boolean threaded;

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
	
	public SecondOrderQuery(Query query, Filter filter, SecondOrderCollector collector, boolean threaded) {
		this.firstOrderQuery = query;
		this.filter = filter;
		this.secondOrderCollector = collector;
		this.threaded = threaded;
		
		if (collector == null) {
			throw new IllegalStateException("Collector must not be null");
		}
	}
	
	public SecondOrderQuery(Query query, Filter filter, SecondOrderCollector collector) {
    this.firstOrderQuery = query;
    this.filter = filter;
    this.secondOrderCollector = collector;
    this.threaded = false;
    
    if (collector == null) {
      throw new IllegalStateException("Collector must not be null");
    }
  }
	
	/*
	public CollectorQuery(Query query, IndexReader reader, Collector collector) {
		this.query = query;
		this.filter = null;
		this.collector = collector;
		
		if (collector == null) {
			throw new IllegalStateException("Collector must not be null");
		}
		initDocStarts(reader);
		
	}
	*/
	
	
	
	/**
	 * Returns a Weight that applies the filter to the enclosed query's Weight.
	 * This is accomplished by overriding the Scorer returned by the Weight.
	 */
	public Weight createWeight(final IndexSearcher searcher) throws IOException {
	    
		if (threaded) {
			return null;
		}
		else {
			
			Weight firstOrderWeight = firstOrderQuery.createWeight(searcher);
			
			// conduct search only if initialization of necessary caches went well
			if (secondOrderCollector.searcherInitialization(searcher)) {
				searcher.search(firstOrderQuery, filter, (Collector) secondOrderCollector);
			}
			
			// no logging, we are basic lucene class
			return new SecondOrderWeight(firstOrderWeight, secondOrderCollector);
		}
	}
	

	/** Rewrites the wrapped query. */
	public Query rewrite(IndexReader reader) throws IOException {
		Query rewritten = firstOrderQuery.rewrite(reader);
		if (rewritten != firstOrderQuery) {
			SecondOrderQuery clone = (SecondOrderQuery) this.clone();
			clone.firstOrderQuery = rewritten;
			return clone;
		} else {
			return this;
		}
	}

	public Query getQuery() {
		return firstOrderQuery;
	}

	public Filter getFilter() {
		return filter;
	}
	
	public SecondOrderCollector getcollector() {
		return secondOrderCollector;
	}

	// inherit javadoc
	public void extractTerms(Set<Term> terms) {
		getQuery().extractTerms(terms);
	}

	/** Prints a user-readable version of this query. */
	public String toString(String s) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SecondOrderQuery(");
		buffer.append(firstOrderQuery.toString(s));
		buffer.append(", filter=" + (filter!=null ? filter.toString() : "null"));
		buffer.append(", collector=" + (secondOrderCollector!=null ? secondOrderCollector.toString() : "null"));
		buffer.append(")");
		buffer.append(ToStringUtils.boost(getBoost()));
		return buffer.toString();
	}

	/** Returns true iff <code>o</code> is equal to this. */
	public boolean equals(Object o) {
		if (o instanceof SecondOrderQuery) {
			SecondOrderQuery fq = (SecondOrderQuery) o;
			return (firstOrderQuery.equals(fq.firstOrderQuery) 
					&& (filter != null ? filter.equals(fq.filter) : true)
					&& secondOrderCollector.equals(fq.secondOrderCollector)
					&& getBoost() == fq.getBoost());
		}
		return false;
	}

	/** Returns a hash code value for this object. */
	public int hashCode() {
		if (filter != null) {
			return firstOrderQuery.hashCode() ^ filter.hashCode() ^ secondOrderCollector.hashCode()
				+ Float.floatToRawIntBits(getBoost());
		}
		
		return firstOrderQuery.hashCode() ^ secondOrderCollector.hashCode()
				+ Float.floatToRawIntBits(getBoost());
	}
	
	
	
}
