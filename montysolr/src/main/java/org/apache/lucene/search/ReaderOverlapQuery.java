package org.apache.lucene.search;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Matches documents containing at least one reader from a supplied set and
 * scores each match by the number of distinct readers it shares with that set.
 *
 * <p>The reader set is constructed by the trending_overlap operator from the
 * initial top-N documents.  Reader values are terms in an indexed string field,
 * so counting postings gives set intersection semantics: duplicate values in a
 * source or candidate document cannot inflate the score.</p>
 */
public final class ReaderOverlapQuery extends Query {
    private final String field;
    private final Set<String> readers;

    public ReaderOverlapQuery(String field, Collection<String> readers) {
        if (field == null || field.isEmpty()) {
            throw new IllegalArgumentException("field must not be empty");
        }
        this.field = field;
        TreeSet<String> uniqueReaders = new TreeSet<String>();
        if (readers != null) {
            for (String reader : readers) {
                if (reader != null && !reader.isEmpty()) {
                    uniqueReaders.add(reader);
                }
            }
        }
        this.readers = Collections.unmodifiableSet(uniqueReaders);
    }

    public Set<String> getReaders() {
        return readers;
    }

    @Override
    public Weight createWeight(final IndexSearcher searcher, final ScoreMode scoreMode,
                               final float boost) {
        return new Weight(this) {
            @Override
            public Scorer scorer(LeafReaderContext context) throws IOException {
                int[] counts = overlapCounts(context);
                int matched = 0;
                for (int count : counts) {
                    if (count > 0) {
                        matched++;
                    }
                }
                if (matched == 0) {
                    return null;
                }
                return new ReaderOverlapScorer(this, counts, boost, matched);
            }

            @Override
            public Explanation explain(LeafReaderContext context, int doc) throws IOException {
                int[] counts = overlapCounts(context);
                int count = doc >= 0 && doc < counts.length ? counts[doc] : 0;
                if (count == 0) {
                    return Explanation.noMatch("document has no reader overlap");
                }
                return Explanation.match(count * boost,
                        "reader overlap count (" + count + ")");
            }

            @Override
            public boolean isCacheable(LeafReaderContext context) {
                return true;
            }
        };
    }

    private int[] overlapCounts(LeafReaderContext context) throws IOException {
        int[] counts = new int[context.reader().maxDoc()];
        if (readers.isEmpty()) {
            return counts;
        }
        Terms terms = context.reader().terms(field);
        if (terms == null) {
            return counts;
        }
        TermsEnum termsEnum = terms.iterator();
        Bits liveDocs = context.reader().getLiveDocs();
        for (String reader : readers) {
            if (!termsEnum.seekExact(new BytesRef(reader))) {
                continue;
            }
            PostingsEnum postings = termsEnum.postings(null, PostingsEnum.NONE);
            for (int doc = postings.nextDoc(); doc != DocIdSetIterator.NO_MORE_DOCS;
                 doc = postings.nextDoc()) {
                if (liveDocs == null || liveDocs.get(doc)) {
                    counts[doc]++;
                }
            }
        }
        return counts;
    }

    @Override
    public String toString(String field) {
        return "ReaderOverlapQuery(" + this.field + ", readers=" + readers + ")";
    }

    @Override
    public void visit(QueryVisitor visitor) {
        visitor.visitLeaf(this);
    }

    @Override
    public boolean equals(Object other) {
        return sameClassAs(other)
                && field.equals(((ReaderOverlapQuery) other).field)
                && readers.equals(((ReaderOverlapQuery) other).readers);
    }

    @Override
    public int hashCode() {
        return 31 * classHash() + field.hashCode() * 31 + readers.hashCode();
    }

    private static final class ReaderOverlapScorer extends Scorer {
        private final int[] counts;
        private final float boost;
        private final int matched;
        private int doc = -1;
        private final DocIdSetIterator iterator = new DocIdSetIterator() {
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
                if (target == NO_MORE_DOCS) {
                    return doc = NO_MORE_DOCS;
                }
                for (int candidate = target; candidate < counts.length; candidate++) {
                    if (counts[candidate] > 0) {
                        return doc = candidate;
                    }
                }
                return doc = NO_MORE_DOCS;
            }

            @Override
            public long cost() {
                return matched;
            }
        };

        private ReaderOverlapScorer(Weight weight, int[] counts, float boost, int matched) {
            super(weight);
            this.counts = counts;
            this.boost = boost;
            this.matched = matched;
        }

        @Override
        public int docID() {
            return doc;
        }

        @Override
        public float score() {
            return counts[doc] * boost;
        }

        @Override
        public DocIdSetIterator iterator() {
            return iterator;
        }

        @Override
        public float getMaxScore(int upTo) {
            int max = 0;
            int limit = Math.min(upTo, counts.length - 1);
            for (int candidate = 0; candidate <= limit; candidate++) {
                max = Math.max(max, counts[candidate]);
            }
            return max * boost;
        }
    }
}
