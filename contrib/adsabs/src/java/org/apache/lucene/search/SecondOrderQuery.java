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
import org.apache.lucene.search.SecondOrderCollector.FinalValueType;
import org.apache.lucene.util.ToStringUtils;


/**
 * 
 * This is a 2nd attempt on the 2nd order operators, while it works, 
 * it will work only for searching inside the boundaries of index
 * segments (ie no distributed search)
 * 
 */
public class SecondOrderQuery extends Query {

	private static final long serialVersionUID = -5670377581753190942L;
	Query firstOrderQuery;
	Filter filter = null;
	private SecondOrderCollector secondOrderCollector;
	private boolean threaded;
	private boolean alreadyExecuted;

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
		this.alreadyExecuted = false;
		
		if (collector == null) {
			throw new IllegalStateException("Collector must not be null");
		}
	}
	
	public SecondOrderQuery(Query query, Filter filter, SecondOrderCollector collector) {
		this(query, filter, collector, false);
  }
	
	public SecondOrderQuery(Query query, SecondOrderCollector collector) {
		this(query, null, collector, false);
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
	 * 
	 * The Weight is prepared before the search begins, but since we are inside
	 * the second order query, we start searching, collect results and then
	 * return the Weight object, which carries with itself the results of the
	 * first-order query
	 */
	public Weight createWeight(final IndexSearcher searcher) throws IOException {
	    
		if (threaded) {
			return null;
		}
		else {
			
			Weight firstOrderWeight = firstOrderQuery.createWeight(searcher);
			
			//System.out.println("preparing: " + this.secondOrderCollector);
			
			// conduct search only if initialization of necessary caches went well
			if (!alreadyExecuted && secondOrderCollector.searcherInitialization(searcher, firstOrderWeight)) {
				//System.out.println("Executing: " + firstOrderQuery.toString());
				searcher.search(firstOrderQuery, filter, (Collector) secondOrderCollector);
				
				//System.out.println("  searching: " + this.secondOrderCollector);
				
				// TODO: can we avoid being called (initialized) in a loop?
				// this looks like a bad design (on my side) if it happens
				// it happens when 2nd order operators are nested
				alreadyExecuted = true;
				
			}
			
			//System.out.println("done:" + this.secondOrderCollector);
			
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
	
	/**
	 * Recursively sets the implemantation type of the
	 * final score (down the wrapped queries, if they
	 * are of the SecondOrderQuery
	 */
	public void setFinalValueType(FinalValueType type) {
		secondOrderCollector.setFinalValueType(type);
		if (firstOrderQuery instanceof SecondOrderQuery) {
			((SecondOrderQuery) firstOrderQuery).setFinalValueType(type);
		}
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
