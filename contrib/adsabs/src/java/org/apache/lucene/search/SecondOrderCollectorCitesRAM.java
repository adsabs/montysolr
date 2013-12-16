package org.apache.lucene.search;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;

/*
 *    // references(P) - set of papers that are in the reference list of P
 *    see: http://labs.adsabs.harvard.edu/trac/ads-invenio/ticket/221
 */
public class SecondOrderCollectorCitesRAM extends AbstractSecondOrderCollector {

  private CacheWrapper cache;
	
  public SecondOrderCollectorCitesRAM(CacheWrapper cache) {
    super();
    assert cache != null;
		this.cache = cache;
  }
	
	
	@SuppressWarnings("unchecked")
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
  public void setNextReader(AtomicReaderContext context)
      throws IOException {
    this.docBase = context.docBase;
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
		return 2938572 ^ cache.hashCode();
	}
	
	
	
}
