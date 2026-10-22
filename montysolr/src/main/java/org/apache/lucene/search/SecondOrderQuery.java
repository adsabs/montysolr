package org.apache.lucene.search;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.SecondOrderCollector.FinalValueType;

import java.io.IOException;
import java.util.Set;


/**
 * This is a 2nd attempt on the 2nd order operators, while it works,
 * it will work only for searching inside the boundaries of index
 * segments (ie no distributed search)
 */
public class SecondOrderQuery extends Query {

    private static final long serialVersionUID = -5670377581753190942L;
    Query firstOrderQuery;
    private final SecondOrderCollector secondOrderCollector;
    private boolean collectionComplete;
    private boolean needsScoring;

    /**
     * Constructs a new query which applies a filter to the results of the
     * original query. Filter.getDocIdSet() will be called every time this query
     * is used in a search.
     *
     * @param query        Query to be filtered, cannot be <code>null</code>.
     * @param needsScoring Whether the query should apply scoring algorithm (or is a simple
     *                     boolean, 1/0 - which runs faster; default is True)
     */

    public SecondOrderQuery(Query query, SecondOrderCollector collector, boolean needsScoring) {
        this.firstOrderQuery = query;
        this.secondOrderCollector = collector;
        this.collectionComplete = false;

        if (collector == null) {
            throw new IllegalStateException("Collector must not be null");
        }
    }

    public SecondOrderQuery(Query query, SecondOrderCollector collector) {
        this(query, collector, true);
    }


    /**
     * Collects first-order results before returning the Weight that exposes
     * second-order hits. The collector determines the seed Weight's score mode;
     * the outer boost is applied only when scoring collected hits.
     */
    public Weight createWeight(final IndexSearcher searcher, ScoreMode needsScores, float boost) throws IOException {

        // Build the seed Weight on every call, even when collection is already complete.
        Weight firstOrderWeight = firstOrderQuery.createWeight(
                searcher, secondOrderCollector.initializationScoreMode(), 1f);

        // conduct search only if initialization of necessary caches went well
        if (!collectionComplete && secondOrderCollector.searcherInitialization(searcher, firstOrderWeight)) {
            searcher.search(firstOrderQuery, (Collector) secondOrderCollector);

            collectionComplete = true;
        }

        return new SecondOrderWeight(this, firstOrderWeight, secondOrderCollector, boost);
    }


    /**
     * Rewrites the wrapped query.
     */
    @Override
    public Query rewrite(IndexSearcher searcher) throws IOException {
        Query rewritten = firstOrderQuery.rewrite(searcher);
        if (rewritten != firstOrderQuery) {
            // Rewriting starts with a fresh collector but retains its aggregation policy.
            SecondOrderCollector collectorCopy = secondOrderCollector.copy();
            collectorCopy.setFinalValueType(secondOrderCollector.getFinalValueType());
            return new SecondOrderQuery(rewritten, collectorCopy);
        }
        return this;
    }

    @Override
    public void visit(QueryVisitor visitor) {
        firstOrderQuery.visit(visitor);
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

    /**
     * Prints a user-readable version of this query.
     */
    public String toString(String s) {
        String buffer = "SecondOrderQuery(" +
                firstOrderQuery.toString(s) +
                ", collector=" + (secondOrderCollector != null ? secondOrderCollector.toString() : "null") +
                ")";
        return buffer;
    }

    /**
     * Returns true iff <code>o</code> is equal to this.
     */
    public boolean equals(Object o) {
        if (o instanceof SecondOrderQuery) {
            SecondOrderQuery fq = (SecondOrderQuery) o;
            return (firstOrderQuery.equals(fq.firstOrderQuery)
                    && secondOrderCollector.equals(fq.secondOrderCollector));
        }
        return false;
    }

    /**
     * Returns a hash code value for this object.
     */
    public int hashCode() {
        return firstOrderQuery.hashCode() ^ secondOrderCollector.hashCode();
    }


}
