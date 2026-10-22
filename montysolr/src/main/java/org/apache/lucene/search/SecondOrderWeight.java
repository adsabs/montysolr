package org.apache.lucene.search;

import org.apache.lucene.index.LeafReaderContext;

import java.io.IOException;
import java.util.List;

public class SecondOrderWeight extends Weight {

    private static final long serialVersionUID = 1999318155593404879L;
    private final Weight firstOrderWeight;
    private final SecondOrderCollector secondOrderCollector;
    private final float scoreBoost;

    public SecondOrderWeight(Query query, Weight weight,
                             SecondOrderCollector collector, float boost) throws IOException {
        super(query);
        this.firstOrderWeight = weight;
        this.secondOrderCollector = collector;
        this.scoreBoost = boost;
    }


    @Override
    public Scorer scorer(LeafReaderContext context) throws IOException {
        int docBase = context.docBase;
        int docBaseEnd = docBase + context.reader().maxDoc();
        List<CollectorDoc> hits = secondOrderCollector.getSubReaderResults(docBase, docBaseEnd);
        if (hits == null || hits.size() == 0) return null;

        return new SecondOrderListOfDocsScorer(this, hits, docBase, scoreBoost);
    }

    @Override
    public Explanation explain(LeafReaderContext context, int doc) throws IOException {
        int docBase = context.docBase;
        List<CollectorDoc> hits = secondOrderCollector.getSubReaderResults(
                docBase, docBase + context.reader().maxDoc());
        if (hits != null) {
            int globalDoc = docBase + doc;
            for (CollectorDoc hit : hits) {
                if (hit.doc == globalDoc) {
                    return Explanation.match(SecondOrderListOfDocsScorer.boostCollectedScore(hit.score, scoreBoost),
                            "score collected by second-order query");
                }
            }
        }

        return Explanation.noMatch("not collected by second-order query");
    }

    @Override
    public boolean isCacheable(LeafReaderContext ctx) {
        return firstOrderWeight.isCacheable(ctx);
    }


}