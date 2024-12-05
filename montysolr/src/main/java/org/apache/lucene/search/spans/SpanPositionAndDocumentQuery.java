package org.apache.lucene.search.spans;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermStates;
import org.apache.lucene.queries.spans.FilterSpans;
import org.apache.lucene.queries.spans.SpanQuery;
import org.apache.lucene.queries.spans.SpanWeight;
import org.apache.lucene.queries.spans.Spans;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreMode;


/**
 * Base class for filtering a SpanQuery based on the position of a match.
 **/
public abstract class SpanPositionAndDocumentQuery extends SpanQuery implements Cloneable {
    protected SpanQuery match;

    public SpanPositionAndDocumentQuery(SpanQuery match) {
        this.match = Objects.requireNonNull(match);
    }

    /**
     * @return the SpanQuery whose matches are filtered.
     *
     * */
    public SpanQuery getMatch() { return match; }

    @Override
    public String getField() { return match.getField(); }

    /**
     * Implementing classes are required to return whether the current position is a match for the passed in
     * "match" {@link SpanQuery}.
     *
     * This is only called if the underlying last {@link Spans#nextStartPosition()} for the
     * match indicated a valid start position.
     *
     * @param spans The {@link Spans} instance, positioned at the spot to check
     *
     * @return whether the match is accepted, rejected, or rejected and should move to the next doc.
     *
     * @see Spans#nextDoc()
     *
     */
    protected abstract FilterSpans.AcceptStatus acceptPosition(Spans spans, Document currentDocument) throws IOException;

    @Override
    public SpanWeight createWeight(IndexSearcher searcher, ScoreMode scoreMode, float boost) throws IOException {
        SpanWeight matchWeight = match.createWeight(searcher, scoreMode, boost);
        return new SpanPositionCheckWeight(matchWeight, searcher, scoreMode.needsScores() ? getTermStates(matchWeight) : null, boost);
    }

    public class SpanPositionCheckWeight extends SpanWeight {

        final SpanWeight matchWeight;

        public SpanPositionCheckWeight(SpanWeight matchWeight, IndexSearcher searcher, Map<Term, TermStates> terms, float boost) throws IOException {
            super(SpanPositionAndDocumentQuery.this, searcher, terms, boost);
            this.matchWeight = matchWeight;
        }

        @Override
        public void extractTermStates(Map<Term, TermStates> terms) {
            matchWeight.extractTermStates(terms);
        }

        @Override
        public boolean isCacheable(LeafReaderContext ctx) {
            return matchWeight.isCacheable(ctx);
        }

        @Override
        public Spans getSpans(final LeafReaderContext context, Postings requiredPostings) throws IOException {
            Spans matchSpans = matchWeight.getSpans(context, requiredPostings);
            return (matchSpans == null) ? null : new FilterSpans(matchSpans) {
                @Override
                protected AcceptStatus accept(Spans candidate) throws IOException {
                    Document currentDocument = context.reader().document(candidate.docID());

                    return acceptPosition(candidate, currentDocument);
                }
            };
        }

    }

    @Override
    public Query rewrite(IndexReader reader) throws IOException {
        SpanQuery rewritten = (SpanQuery) match.rewrite(reader);
        if (rewritten != match) {
            try {
                SpanPositionAndDocumentQuery clone = (SpanPositionAndDocumentQuery) this.clone();
                clone.match = rewritten;
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }

        return super.rewrite(reader);
    }

    /** Returns true iff <code>other</code> is equal to this. */
    @Override
    public boolean equals(Object other) {
        return sameClassAs(other) &&
                match.equals(((SpanPositionAndDocumentQuery) other).match);
    }

    @Override
    public int hashCode() {
        return classHash() ^ match.hashCode();
    }
}

