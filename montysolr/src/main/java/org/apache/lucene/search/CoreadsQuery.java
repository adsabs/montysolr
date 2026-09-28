package org.apache.lucene.search;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Scores documents by the number of reader terms shared with a seed query.
 *
 * <p>The matching terms are accumulated directly from postings rather than
 * represented as BooleanQuery clauses. This keeps coreads independent of
 * Lucene's Boolean clause limit while retaining an exact overlap score.</p>
 */
public final class CoreadsQuery extends Query {
    private final String fieldName;
    private final Set<String> readerTerms;

    public CoreadsQuery(String fieldName, Collection<String> readerTerms) {
        this.fieldName = Objects.requireNonNull(fieldName);
        this.readerTerms = Collections.unmodifiableSet(new HashSet<String>(readerTerms));
    }

    @Override
    public Weight createWeight(IndexSearcher searcher, ScoreMode scoreMode, float boost) {
        return new CoreadsWeight(this, boost);
    }
    @Override
    public void visit(QueryVisitor visitor) {
        if (visitor.acceptField(fieldName)) {
            for (String readerTerm : readerTerms) {
                visitor.consumeTerms(this, new Term(fieldName, readerTerm));
            }
        }
    }


    @Override
    public String toString(String field) {
        return "coreads(" + fieldName + ", terms=" + readerTerms.size() + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (sameClassAs(other) == false) {
            return false;
        }
        CoreadsQuery that = (CoreadsQuery) other;
        return fieldName.equals(that.fieldName) && readerTerms.equals(that.readerTerms);
    }

    @Override
    public int hashCode() {
        return 31 * classHash() + fieldName.hashCode() * 31 + readerTerms.hashCode();
    }

    private static final class CoreadsWeight extends Weight {
        private final CoreadsQuery query;
        private final float boost;

        private CoreadsWeight(CoreadsQuery query, float boost) {
            super(query);
            this.query = query;
            this.boost = boost;
        }

        @Override
        public Scorer scorer(LeafReaderContext context) throws IOException {
            int[] overlap = new int[context.reader().maxDoc()];
            Bits liveDocs = context.reader().getLiveDocs();
            Terms terms = context.reader().terms(query.fieldName);
            if (terms == null) {
                return null;
            }

            TermsEnum termsEnum = terms.iterator();
            for (String readerTerm : query.readerTerms) {
                if (termsEnum.seekExact(new BytesRef(readerTerm)) == false) {
                    continue;
                }
                PostingsEnum postings = termsEnum.postings(null, PostingsEnum.NONE);
                for (int doc = postings.nextDoc(); doc != DocIdSetIterator.NO_MORE_DOCS;
                     doc = postings.nextDoc()) {
                    if (liveDocs == null || liveDocs.get(doc)) {
                        overlap[doc]++;
                    }
                }
            }

            boolean hasMatches = false;
            for (int count : overlap) {
                if (count > 0) {
                    hasMatches = true;
                    break;
                }
            }
            return hasMatches ? new CoreadsScorer(this, overlap, boost) : null;
        }

        @Override
        public Explanation explain(LeafReaderContext context, int doc) throws IOException {
            Scorer scorer = scorer(context);
            if (scorer == null || scorer.iterator().advance(doc) != doc) {
                return Explanation.noMatch("No shared reader");
            }
            int count = Math.round(scorer.score() / boost);
            return Explanation.match(scorer.score(), "shared readers: " + count);
        }

        @Override
        public boolean isCacheable(LeafReaderContext context) {
            return true;
        }
    }

    private static final class CoreadsScorer extends Scorer {
        private final int[] overlap;
        private final float boost;
        private final DocIdSetIterator iterator;

        private CoreadsScorer(Weight weight, int[] overlap, float boost) {
            super(weight);
            this.overlap = overlap;
            this.boost = boost;
            this.iterator = new DocIdSetIterator() {
                private int doc = -1;

                @Override
                public int docID() {
                    return doc;
                }

                @Override
                public int nextDoc() {
                    return advance(doc + 1);
                }

                @Override
                public int advance(int target) {
                    if (doc == NO_MORE_DOCS) {
                        return doc;
                    }
                    doc = Math.max(target, doc + 1);
                    while (doc < CoreadsScorer.this.overlap.length
                            && CoreadsScorer.this.overlap[doc] == 0) {
                        doc++;
                    }
                    if (doc >= CoreadsScorer.this.overlap.length) {
                        doc = NO_MORE_DOCS;
                    }
                    return doc;
                }

                @Override
                public long cost() {
                    return CoreadsScorer.this.overlap.length;
                }
            };
        }

        @Override
        public DocIdSetIterator iterator() {
            return iterator;
        }

        @Override
        public int docID() {
            return iterator.docID();
        }

        @Override
        public float score() {
            return overlap[docID()] * boost;
        }

        @Override
        public float getMaxScore(int upTo) {
            return Float.POSITIVE_INFINITY;
        }
    }
}
