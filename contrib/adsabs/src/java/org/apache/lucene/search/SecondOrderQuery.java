package org.apache.lucene.search;

import java.io.IOException;
import java.util.Set;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.SecondOrderCollector.FinalValueType;


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
	private SecondOrderCollector secondOrderCollector;
	private boolean alreadyExecuted;
	private boolean needsScoring;

	/**
	 * Constructs a new query which applies a filter to the results of the
	 * original query. Filter.getDocIdSet() will be called every time this query
	 * is used in a search.
	 * 
	 * @param query
	 *            Query to be filtered, cannot be <code>null</code>.
	 * @param needsScoring
	 *            Whether the query should apply scoring algorithm (or is a simple
	 *            boolean, 1/0 - which runs faster; default is True)
	 */
	
	public SecondOrderQuery(Query query, SecondOrderCollector collector, boolean needsScoring) {
		this.firstOrderQuery = query;
		this.secondOrderCollector = collector;
		this.alreadyExecuted = false;
		
		if (collector == null) {
			throw new IllegalStateException("Collector must not be null");
		}
	}
	
	public SecondOrderQuery(Query query, SecondOrderCollector collector) {
    this(query, collector, true);
  }
	
	
	/**
	 * Returns a Weight that applies the filter to the enclosed query's Weight.
	 * This is accomplished by overriding the Scorer returned by the Weight.
	 * 
	 * The Weight is prepared before the search begins, but since we are inside
	 * the second order query, we start searching, collect results and then
	 * return the Weight object, which carries with itself the results of the
	 * first-order query
	 */
	public Weight createWeight(final IndexSearcher searcher, boolean needsScores) throws IOException {
	    
		Weight firstOrderWeight = firstOrderQuery.createWeight(searcher, needsScores);
		
		//System.out.println("preparing: " + this.secondOrderCollector);
		
		// conduct search only if initialization of necessary caches went well
		if (!alreadyExecuted && secondOrderCollector.searcherInitialization(searcher, firstOrderWeight)) {
			//System.out.println("Executing: " + firstOrderQuery.toString());
			searcher.search(firstOrderQuery, (Collector) secondOrderCollector);
			
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
	

	/** Rewrites the wrapped query. */
	public Query rewrite(IndexReader reader) throws IOException {
		Query rewritten = firstOrderQuery.rewrite(reader);
		if (rewritten != firstOrderQuery) {
			SecondOrderQuery clone = new SecondOrderQuery(rewritten, this.secondOrderCollector);
			return clone;
		} else {
			return this;
		}
	}

	public Query getQuery() {
		return firstOrderQuery;
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
		((SecondOrderQuery) getQuery()).extractTerms(terms);
	}

	/** Prints a user-readable version of this query. */
	public String toString(String s) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SecondOrderQuery(");
		buffer.append(firstOrderQuery.toString(s));
		buffer.append(", collector=" + (secondOrderCollector!=null ? secondOrderCollector.toString() : "null"));
		buffer.append(")");
		return buffer.toString();
	}

	/** Returns true iff <code>o</code> is equal to this. */
	public boolean equals(Object o) {
		if (o instanceof SecondOrderQuery) {
			SecondOrderQuery fq = (SecondOrderQuery) o;
			return (firstOrderQuery.equals(fq.firstOrderQuery) 
					&& secondOrderCollector.equals(fq.secondOrderCollector));
		}
		return false;
	}

	/** Returns a hash code value for this object. */
	public int hashCode() {
		return firstOrderQuery.hashCode() ^ secondOrderCollector.hashCode();
	}
	
	
	
}
