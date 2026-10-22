package org.apache.lucene.search;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.ReaderUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SecondOrderCollectorTopN extends AbstractSecondOrderCollector {

    private final TopDocsCollector topCollector;
    private final int topN;
    private String detail = null;
    private Sort sortOrder;
    private IndexSearcher searcher;
    private Weight firstOrderScoreWeight;

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
    public boolean searcherInitialization(IndexSearcher searcher, Weight firstOrderWeight)
            throws IOException {
        if (sortOrder != null) {
            this.searcher = searcher;
            this.firstOrderScoreWeight = firstOrderWeight;
        }
        return super.searcherInitialization(searcher, firstOrderWeight);
    }

    @Override
    public ScoreMode initializationScoreMode() {
        // No-score weights can discard scoring wrappers needed to rescore selected hits.
        return sortOrder == null ? scoreMode() : ScoreMode.COMPLETE;
    }

    @Override
    public List<CollectorDoc> getSubReaderResults(int rangeStart, int rangeEnd) throws IOException {
        if (topCollector.totalHits == 0)
            return null;

        lock.lock();
        try {
            if (!organized) {
                ScoreDoc[] scoreDocs = topCollector.topDocs().scoreDocs;
                ((ArrayList) hits).ensureCapacity(scoreDocs.length);
                if (sortOrder != null) {
                    populateSeedScores(scoreDocs);
                }
                for (ScoreDoc scoreDoc : scoreDocs) {
                    hits.add(new CollectorDoc(scoreDoc.doc, scoreDoc.score));
                }
            }
        } finally {
            lock.unlock();
        }

        return super.getSubReaderResults(rangeStart, rangeEnd);
    }

    private void populateSeedScores(ScoreDoc[] scoreDocs) throws IOException {
        // Selected hits must be visited in doc-id order by the forward-only seed scorers.
        Arrays.sort(scoreDocs, Comparator.comparingInt(scoreDoc -> scoreDoc.doc));
        List<LeafReaderContext> leaves = searcher.getLeafContexts();
        LeafReaderContext currentLeaf = null;
        Scorer currentScorer = null;

        for (ScoreDoc scoreDoc : scoreDocs) {
            if (currentLeaf == null
                    || scoreDoc.doc >= currentLeaf.docBase + currentLeaf.reader().maxDoc()) {
                currentLeaf = leaves.get(ReaderUtil.subIndex(scoreDoc.doc, leaves));
                ScorerSupplier scorerSupplier = firstOrderScoreWeight.scorerSupplier(currentLeaf);
                if (scorerSupplier == null) {
                    throw new IllegalStateException("Selected top-n document does not match the seed query");
                }
                currentScorer = scorerSupplier.get(1);
            }

            int leafDocId = scoreDoc.doc - currentLeaf.docBase;
            int advancedDocId = currentScorer.iterator().advance(leafDocId);
            if (advancedDocId != leafDocId) {
                throw new IllegalStateException("Selected top-n document does not match the seed query");
            }
            scoreDoc.score = currentScorer.score();
        }
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
        return topCollector.getLeafCollector(context);
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
