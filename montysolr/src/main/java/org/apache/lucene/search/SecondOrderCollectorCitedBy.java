package org.apache.lucene.search;

import java.io.IOException;

/**
 * // citations(P) - set of papers that have P in their reference list
 * 
 * see: http://labs.adsabs.harvard.edu/trac/ads-invenio/ticket/221
 * 
 */
public class SecondOrderCollectorCitedBy extends AbstractSecondOrderCollector {

	private SolrCacheWrapper cache;


	public SecondOrderCollectorCitedBy(SolrCacheWrapper cache) {
		super();
		assert cache != null;
		this.cache = cache;
	}
	
	
	@Override
  public void collect(int doc) throws IOException {
		int[] v = cache.getLuceneDocIds(doc+docBase);
		if (v == null) return;
		float s = scorer.score();
		for (int citingDoc: v) {
			hits.add(new CollectorDoc(citingDoc, s, v.length));
		}
		
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(cache:" + cache.toString() + ")";
	}
	
	/** Returns a hash code value for this object. */
	public int hashCode() {
		return 8959545 ^ cache.hashCode();
	}


  @Override
  public boolean needsScores() {
    return true;
  }


  @Override
  public SecondOrderCollector copy() {
  	return new SecondOrderCollectorCitedBy(cache);
  }

}
