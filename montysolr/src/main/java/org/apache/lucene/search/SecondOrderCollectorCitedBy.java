package org.apache.lucene.search;

import java.io.IOException;

/**
 * // citations(P) - set of papers that have P in their reference list
 * <p>
 * see: http://labs.adsabs.harvard.edu/trac/ads-invenio/ticket/221
 */
public class SecondOrderCollectorCitedBy extends AbstractSecondOrderCollector {

    private final SolrCacheWrapper cache;

    public SecondOrderCollectorCitedBy(SolrCacheWrapper cache) {
        this(cache, 0.0f);
    }

    public SecondOrderCollectorCitedBy(SolrCacheWrapper cache, float textWeightRatio) {
        super();
        assert cache != null;
        this.cache = cache;
        setTextWeightRatio(textWeightRatio);
    }


    @Override
    public void collect(int doc) throws IOException {
        int[] v = cache.getLuceneDocIds(doc + docBase);
        if (v == null && textWeightRatio == 0.0f) return;
        float s = scorer.score();
        if (textWeightRatio > 0.0f) {
            recordInputScore(s);
        }
        if (v == null) return;
        for (int citingDoc : v) {
            hits.add(new CollectorDoc(citingDoc, s, v.length));
        }

    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(cache:" + cache.toString() + ")";
    }

    /**
     * Returns a hash code value for this object.
     */
    public int hashCode() {
        return 8959545 ^ cache.hashCode()
                ^ (textWeightRatio == 0.0f ? 0 : Float.hashCode(textWeightRatio));
    }

    @Override
    public ScoreMode scoreMode() {
        return ScoreMode.COMPLETE;
    }

    @Override
    public SecondOrderCollector copy() {
        SecondOrderCollectorCitedBy copy = new SecondOrderCollectorCitedBy(cache, textWeightRatio);
        copy.setFinalValueType(compactingType);
        return copy;
    }
}
