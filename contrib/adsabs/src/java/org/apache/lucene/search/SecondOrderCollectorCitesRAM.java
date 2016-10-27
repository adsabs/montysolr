package org.apache.lucene.search;

import java.io.IOException;

/*
 *    // references(P) - set of papers that are in the reference list of P
 *    see: http://labs.adsabs.harvard.edu/trac/ads-invenio/ticket/221
 */
public class SecondOrderCollectorCitesRAM extends AbstractSecondOrderCollector {

  private SolrCacheWrapper cache;
	
  public SecondOrderCollectorCitesRAM(SolrCacheWrapper cache) {
    super();
    assert cache != null;
		this.cache = cache;
  }
	
	
	@Override
	public void collect(int doc) throws IOException {
		int[] citations = cache.getLuceneDocIds(doc+docBase);
		if (citations == null) {
			return;
		}
		float freq = citations.length;
		float s = scorer.score();
		for (int docid: citations) {
			if (docid == -1)
				continue;
			hits.add(new CollectorDoc(docid, s, -1, freq));
		}
		
	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(cache:" + cache.toString() + ")";
	}
	
	/** Returns a hash code value for this object. */
	public int hashCode() {
		return 2938572 ^ cache.hashCode();
	}


  @Override
  public boolean needsScores() {
    return true;
  }
}
