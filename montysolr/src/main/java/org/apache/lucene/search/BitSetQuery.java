package org.apache.lucene.search;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.util.FixedBitSet;

import java.io.IOException;

/**
 * A query that matches all documents and filters them using a bitset filter
 */
public class BitSetQuery extends Query {

    private String uuid;
    private final FixedBitSet docs;

    public BitSetQuery(FixedBitSet docs) {
        this.docs = docs;
    }

    @Override
    public Weight createWeight(IndexSearcher searcher, ScoreMode needsScores, float boost) throws IOException {
        return new ConstantScoreWeight(this, boost) {
            @Override
            public Scorer scorer(LeafReaderContext context) throws IOException {
                return new ConstantScoreScorer(this, score(), needsScores,
                        new BasedBitSetIterator(docs, docs.approximateCardinality(),
                                context.docBase, context.reader().maxDoc()));
            }

            @Override
            public boolean isCacheable(LeafReaderContext ctx) {
                return true;
            }
        };
    }

    @Override
    public String toString(String field) {
        return "BitSetQuery(" + docs.approximateCardinality() + ")";
    }

    @Override
    public void visit(QueryVisitor visitor) {
        visitor.visitLeaf(this);
    }

    @Override
    public boolean equals(Object other) {
        return sameClassAs(other) &&
                docs.equals(((BitSetQuery) other).docs);
    }

    @Override
    public int hashCode() {
        if (uuid != null) {
            return uuid.hashCode();
        }
        return 31 * classHash() + docs.hashCode();
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public String getUUID(String uuid) {
        return uuid;
    }
}
