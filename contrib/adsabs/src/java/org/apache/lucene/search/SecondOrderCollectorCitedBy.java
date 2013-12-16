package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.AtomicReaderContext;

/**
 * // citations(P) - set of papers that have P in their reference list
 * 
 * see: http://labs.adsabs.harvard.edu/trac/ads-invenio/ticket/221
 * 
 */
public class SecondOrderCollectorCitedBy extends AbstractSecondOrderCollector {

	private CacheWrapper cache;


	public SecondOrderCollectorCitedBy(CacheWrapper cache) {
		super();
		assert cache != null;
		this.cache = cache;
	}
	
	
	@Override
	public boolean searcherInitialization(IndexSearcher searcher, Weight firstOrderWeight) throws IOException {
		return super.searcherInitialization(searcher, firstOrderWeight);
	}
	

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;

	}

	@Override
	public void collect(int doc) throws IOException {
		int[] v = cache.getLuceneDocIds(doc+docBase);
		if (v == null) return;
		float s = scorer.score();
		float freq = (float) v.length;
		for (int citingDoc: v) {
			hits.add(new CollectorDoc(citingDoc, s, -1, freq));
		}
		
	}


	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}
	
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(cache:" + cache.toString() + ")";
	}
	
	/** Returns a hash code value for this object. */
	public int hashCode() {
		return 8959545 ^ cache.hashCode();
	}
	
	/** Returns true iff <code>o</code> is equal to this. */
	public boolean equals(Object o) {
		if (o instanceof SecondOrderCollector) {
			SecondOrderCollector fq = (SecondOrderCollector) o;
			return hashCode() == fq.hashCode();
		}
		return false;
	}

  @Override
  public void setNextReader(AtomicReaderContext context) throws IOException {
     this.docBase = context.docBase;
  }
	
	
}
