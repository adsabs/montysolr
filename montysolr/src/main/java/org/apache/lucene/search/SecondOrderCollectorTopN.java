package org.apache.lucene.search;

import org.apache.lucene.index.LeafReaderContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecondOrderCollectorTopN extends AbstractSecondOrderCollector {

    private final TopDocsCollector topCollector;
    private final int topN;
    private String detail = null;
    private Sort sortOrder;

    public SecondOrderCollectorTopN(String detail, int topN, Sort sortOrder) {
        this.topN = topN;
        this.sortOrder = sortOrder;
        this.detail = detail;
        this.topCollector = TopFieldCollector.create(sortOrder, topN, Integer.MAX_VALUE);
    }

    public SecondOrderCollectorTopN(int topN) {
        topCollector = TopScoreDocCollector.create(topN, Integer.MAX_VALUE);
        this.topN = topN;
    }


    @Override
    public List<CollectorDoc> getSubReaderResults(int rangeStart, int rangeEnd) {

        if (topCollector.totalHits == 0)
            return null;

        lock.lock();
        try {
            if (!organized) {
                ((ArrayList) hits).ensureCapacity(topCollector.totalHits);
                for (ScoreDoc d : topCollector.topDocs().scoreDocs) {
                    hits.add(new CollectorDoc(d.doc, d.score));
                }

            }
        } finally {
            lock.unlock();
        }

        return super.getSubReaderResults(rangeStart, rangeEnd);

    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + topN + (detail != null ? ", info=" + detail : "") + ")";
    }

    @Override
    public void collect(int doc) throws IOException {
        throw new UnsupportedOperationException("Must not be called");

    }

    @Override
    public LeafCollector getLeafCollector(LeafReaderContext context) throws IOException {
        LeafCollector c = topCollector.getLeafCollector(context);
        return c;
    }

    @Override
    public ScoreMode scoreMode() {
        return this.topCollector.scoreMode();
    }

    public SecondOrderCollector copy() {
        if (sortOrder != null) {
            return new SecondOrderCollectorTopN(detail, topN, sortOrder);
        } else {
            return new SecondOrderCollectorTopN(topN);
        }
    }
}
