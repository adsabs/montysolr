/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lucene.search.join;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryVisitor;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Weight;
import org.apache.lucene.util.Accountable;
import org.apache.lucene.util.BitSetIterator;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefHash;
import org.apache.lucene.util.FixedBitSet;
import org.apache.lucene.util.RamUsageEstimator;

/**
 * Adapted from Lucene's {@code org.apache.lucene.search.join.TermsIncludingScoreQuery}.
 * The terms and scores supplied here are normalized (analyzed) and deduplicated by the caller
 * ({@link MontyNormalizingJoinUtil}), rather than collected directly by Lucene's join collectors.
 * This class performs no analysis itself; it only seeks the given terms against toField's
 * term dictionary.
 *
 * <p>Trimmed relative to the original: only the single-valued-per-document scorer is kept, since
 * montysolr's join call sites (bibcode -&gt; citation/reference) never join on a multivalued
 * "from" field. The no-score ({@code ScoreMode.None}) fast path is dropped for the same reason --
 * montysolr's call sites always need scores (ScoreMode.Avg).
 */
final class MontyNormalizingTermsIncludingScoreQuery extends Query implements Accountable {
    private static final long BASE_RAM_BYTES =
            RamUsageEstimator.shallowSizeOfInstance(MontyNormalizingTermsIncludingScoreQuery.class);

    private final String toField;
    private final BytesRefHash terms;
    private final float[] scores;
    private final int[] sortedTermOrds;

    // These fields are used for equals() and hashcode() only
    private final String fromField;
    private final Query fromQuery;
    // id of the context rather than the context itself in order not to hold references to index
    // readers
    private final Object topReaderContextId;

    private final long ramBytesUsed; // cache

    MontyNormalizingTermsIncludingScoreQuery(
            String toField,
            BytesRefHash terms,
            float[] scores,
            String fromField,
            Query fromQuery,
            Object indexReaderContextId) {
        this.toField = toField;
        this.terms = terms;
        this.scores = scores;
        this.sortedTermOrds = terms.sort();

        this.fromField = fromField;
        this.fromQuery = fromQuery;
        this.topReaderContextId = indexReaderContextId;

        this.ramBytesUsed =
                BASE_RAM_BYTES
                        + RamUsageEstimator.sizeOfObject(fromField)
                        + RamUsageEstimator.sizeOfObject(
                                fromQuery, RamUsageEstimator.QUERY_DEFAULT_RAM_BYTES_USED)
                        + RamUsageEstimator.sizeOfObject(sortedTermOrds)
                        + RamUsageEstimator.sizeOfObject(scores)
                        + RamUsageEstimator.sizeOfObject(terms)
                        + RamUsageEstimator.sizeOfObject(toField);
    }

    @Override
    public String toString(String string) {
        return String.format(
                Locale.ROOT, "MontyNormalizingTermsIncludingScoreQuery{field=%s;fromQuery=%s}", toField, fromQuery);
    }

    @Override
    public void visit(QueryVisitor visitor) {
        if (visitor.acceptField(toField)) {
            visitor.visitLeaf(this);
        }
    }

    @Override
    public boolean equals(Object other) {
        return sameClassAs(other) && equalsTo(getClass().cast(other));
    }

    private boolean equalsTo(MontyNormalizingTermsIncludingScoreQuery other) {
        return Objects.equals(toField, other.toField)
                && Objects.equals(fromField, other.fromField)
                && Objects.equals(fromQuery, other.fromQuery)
                && Objects.equals(topReaderContextId, other.topReaderContextId);
    }

    @Override
    public int hashCode() {
        return classHash() + Objects.hash(toField, fromField, fromQuery, topReaderContextId);
    }

    @Override
    public long ramBytesUsed() {
        return ramBytesUsed;
    }

    @Override
    public Weight createWeight(
            IndexSearcher searcher, org.apache.lucene.search.ScoreMode scoreMode, float boost)
            throws IOException {
        return new Weight(MontyNormalizingTermsIncludingScoreQuery.this) {

            @Override
            public Explanation explain(LeafReaderContext context, int doc) throws IOException {
                Terms segmentTerms = context.reader().terms(toField);
                if (segmentTerms != null) {
                    TermsEnum segmentTermsEnum = segmentTerms.iterator();
                    BytesRef spare = new BytesRef();
                    PostingsEnum postingsEnum = null;
                    for (int i = 0; i < MontyNormalizingTermsIncludingScoreQuery.this.terms.size(); i++) {
                        if (segmentTermsEnum.seekExact(
                                MontyNormalizingTermsIncludingScoreQuery.this.terms.get(sortedTermOrds[i], spare))) {
                            postingsEnum = segmentTermsEnum.postings(postingsEnum, PostingsEnum.NONE);
                            if (postingsEnum.advance(doc) == doc) {
                                final float score = MontyNormalizingTermsIncludingScoreQuery.this.scores[sortedTermOrds[i]];
                                if (boost == 1.0f) {
                                    return Explanation.match(
                                            score, "Score based on join value " + segmentTermsEnum.term().utf8ToString());
                                } else {
                                    return Explanation.match(
                                            score * boost,
                                            "Score based on join value "
                                                    + segmentTermsEnum.term().utf8ToString()
                                                    + "^"
                                                    + boost);
                                }
                            }
                        }
                    }
                }
                return Explanation.noMatch("Not a match");
            }

            @Override
            public Scorer scorer(LeafReaderContext context) throws IOException {
                Terms segmentTerms = context.reader().terms(toField);
                if (segmentTerms == null) {
                    return null;
                }

                final long cost = context.reader().maxDoc() * segmentTerms.size();

                TermsEnum segmentTermsEnum = segmentTerms.iterator();
                return new SingleValueInOrderScorer(this, segmentTermsEnum, context.reader().maxDoc(), cost, boost);
            }

            @Override
            public boolean isCacheable(LeafReaderContext ctx) {
                return true;
            }
        };
    }

    private class SingleValueInOrderScorer extends Scorer {

        final DocIdSetIterator matchingDocsIterator;
        final float[] documentScores;
        final long cost;
        final float boost;

        SingleValueInOrderScorer(Weight weight, TermsEnum termsEnum, int maxDoc, long cost, float boost)
                throws IOException {
            super(weight);
            FixedBitSet matchingDocs = new FixedBitSet(maxDoc);
            this.documentScores = new float[maxDoc];
            fillDocsAndScores(matchingDocs, termsEnum);
            this.matchingDocsIterator = new BitSetIterator(matchingDocs, cost);
            this.cost = cost;
            this.boost = boost;
        }

        private void fillDocsAndScores(FixedBitSet matchingDocs, TermsEnum termsEnum)
                throws IOException {
            BytesRef spare = new BytesRef();
            PostingsEnum postingsEnum = null;
            for (int i = 0; i < terms.size(); i++) {
                if (termsEnum.seekExact(terms.get(sortedTermOrds[i], spare))) {
                    postingsEnum = termsEnum.postings(postingsEnum, PostingsEnum.NONE);
                    float score = MontyNormalizingTermsIncludingScoreQuery.this.scores[sortedTermOrds[i]];
                    for (int doc = postingsEnum.nextDoc();
                         doc != DocIdSetIterator.NO_MORE_DOCS;
                         doc = postingsEnum.nextDoc()) {
                        matchingDocs.set(doc);
                        // In the case the same doc is also related to another doc, a score might be
                        // overwritten. This can only happen in a many-to-many relation.
                        documentScores[doc] = score;
                    }
                }
            }
        }

        @Override
        public float score() throws IOException {
            return documentScores[docID()] * boost;
        }

        @Override
        public float getMaxScore(int upTo) throws IOException {
            return Float.POSITIVE_INFINITY;
        }

        @Override
        public int docID() {
            return matchingDocsIterator.docID();
        }

        @Override
        public DocIdSetIterator iterator() {
            return matchingDocsIterator;
        }
    }
}
