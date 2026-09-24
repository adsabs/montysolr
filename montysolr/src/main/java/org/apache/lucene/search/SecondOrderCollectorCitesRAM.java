package org.apache.lucene.search;

import java.io.IOException;

/*
 *    // references(P) - set of papers that are in the reference list of P
 *    see: http://labs.adsabs.harvard.edu/trac/ads-invenio/ticket/221
 */
public class SecondOrderCollectorCitesRAM extends AbstractSecondOrderCollector {

    private final SolrCacheWrapper cache;

    public SecondOrderCollectorCitesRAM(SolrCacheWrapper cache) {
        this(cache, 0.0f);
    }

    public SecondOrderCollectorCitesRAM(SolrCacheWrapper cache, float textWeightRatio) {
        super();
        assert cache != null;
        this.cache = cache;
        setTextWeightRatio(textWeightRatio);
    }


    @Override
    public void collect(int doc) throws IOException {
        int[] citations = cache.getLuceneDocIds(doc + docBase);
        if (citations == null && textWeightRatio == 0.0f) {
            return;
        }
        float s = scorer.score();
        if (textWeightRatio > 0.0f) {
            recordInputScore(s);
        }
        if (citations == null) {
            return;
        }
        for (int docid : citations) {
            if (docid == -1) {
                continue;
            }
            hits.add(new CollectorDoc(docid, s, citations.length));
        }

    }


    public String toString() {
        return this.getClass().getSimpleName() + "(cache:" + cache.toString() + ")";
    }

    /**
     * Returns a hash code value for this object.
     */
    public int hashCode() {
        return 2938572 ^ cache.hashCode()
                ^ (textWeightRatio == 0.0f ? 0 : Float.hashCode(textWeightRatio));
    }


    @Override
    public ScoreMode scoreMode() {
        return ScoreMode.COMPLETE;
    }


    @Override
    public SecondOrderCollector copy() {
        SecondOrderCollectorCitesRAM copy = new SecondOrderCollectorCitesRAM(cache, textWeightRatio);
        copy.setFinalValueType(compactingType);
        return copy;
    }
}
