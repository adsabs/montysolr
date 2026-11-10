package org.apache.lucene.search;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class SecondOrderWeight extends Weight {

    private static final long serialVersionUID = 1999318155593404879L;
    private final Weight innerWeight;
    private final SecondOrderCollector secondOrderCollector;
    private final float boost;

    public SecondOrderWeight(Query query, Weight weight,
                             SecondOrderCollector collector, float boost) throws IOException {
        super(query);
        this.innerWeight = weight;
        this.secondOrderCollector = collector;
        this.boost = boost;
    }


    @Override
    public Scorer scorer(LeafReaderContext context) throws IOException {
        int docBase = context.docBase;
        int maxRange = docBase + context.reader().maxDoc();
        List<CollectorDoc> hits = secondOrderCollector.getSubReaderResults(docBase, maxRange);
        if (hits == null || hits.size() == 0) return null;
        return new SecondOrderListOfDocsScorer(this, hits, docBase, boost);
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
                    float score = hit.score * boost;
                    return Explanation.match(score >= 0f ? score : 0f,
                            "score collected by second-order query");
                }
            }
        }
        return Explanation.noMatch("not collected by second-order query");
    }

    @Override
    public boolean isCacheable(LeafReaderContext ctx) {
        return innerWeight.isCacheable(ctx);
    }


}