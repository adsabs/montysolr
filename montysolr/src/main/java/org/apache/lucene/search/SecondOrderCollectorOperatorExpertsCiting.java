package org.apache.lucene.search;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.solr.search.CitationCache;

import java.io.IOException;
import java.util.Set;

/**
 * Finds papers that are cited by our search. And then adjusts the score so that
 * papers with most value from the cited_read_boost field are up
 */
public class SecondOrderCollectorOperatorExpertsCiting extends AbstractSecondOrderCollector {

    Set<String> fieldsToLoad;
    protected String referenceField;
    protected String[] uniqueIdField;
    protected String boostField;
    private final SolrCacheWrapper<CitationCache<Object, Integer>> cache;
    private final LuceneCacheWrapper<NumericDocValues> boostCache;
    private int hashCode;

    public SecondOrderCollectorOperatorExpertsCiting(
            SolrCacheWrapper<CitationCache<Object, Integer>> cache,
            LuceneCacheWrapper<NumericDocValues> boostWrapper) {
        super();

        assert cache != null;
        this.cache = cache;
        this.boostCache = boostWrapper;
        setFinalValueType(FinalValueType.ARITHM_MEAN);
    }


    @Override
    public void collect(int doc) throws IOException {

        // find references froum our doc
        int[] references = cache.getLuceneDocIds(doc + docBase);

        if (references == null || references.length == 0) {
            return;
        }

        //Document document = reader.document(doc, fieldsToLoad);

        float s = 0.0f; // lucene score doesn't make sense for us;
		
		/*
		// naive implementation (probably slow)
		Document document = this.context.reader().document(doc);
		IndexableField bf = document.getField("boost_1");
		if (bf != null) {
      s = s + (s * bf.numericValue().floatValue());
    }
    else {
      // penalize docs without boost
      s = s * 0.8f;
    }
    */


        //if (bf==null) throw new IOException("Every document must have field: " + boostField);

        // TODO: we must find the proper values for this, that means to compute the statistics
        // (find the local minimas, maximas) for this function; this is just guessing....

        float bc = 0.0f;
        if ((bc = boostCache.getFloat(doc + docBase)) > 0.0f) {
            s = bc / references.length;
        }


        //s = s / (vals.length + 100); // this would contribute only a part of the score to each citation
        for (int docid : references) {
            if (docid > 0) {
                //System.out.println("expert: doc=" + (doc+docBase) + "(boost:" + bc + ") adding=" + docid + " (score:" + (s) + ")" + " freq=" + references.length) ;
                hits.add(new CollectorDoc(docid, s, references.length));
            }
        }
    }

    @Override
    public void doSetNextReader(LeafReaderContext context)
            throws IOException {
        this.docBase = context.docBase;
        this.context = null;
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(cache=" + cache.toString() + ", boost=" + boostCache.toString() + ")";
    }

    /**
     * Returns a hash code value for this object.
     */
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = computeHashCode();
            assert hashCode != 0;
        }
        assert hashCode == computeHashCode();
        return hashCode;
    }


    private int computeHashCode() {
        if (boostField != null)
            return 1301081 ^ boostField.hashCode() ^ cache.hashCode();
        return 1301081 ^ cache.hashCode();
    }


    @Override
    public boolean needsScores() {
        return true;
    }


    @Override
    public SecondOrderCollector copy() {
        return new SecondOrderCollectorOperatorExpertsCiting(cache, boostCache);
    }


}
