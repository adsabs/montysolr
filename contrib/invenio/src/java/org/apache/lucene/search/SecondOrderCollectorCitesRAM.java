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
		citations = cache.getLuceneDocIds(sourceDocid)
		if (docToDocidsCache.containsKey(doc+docBase)) {
			float s = scorer.score();
			float freq = docToDocidsCache.get(doc+docBase).size();
			for (int i: docToDocidsCache.get(doc+docBase)) {
				hits.add(new CollectorDoc(i, s, -1, freq));
			}
		}
		
	}

	@Override
  public void setNextReader(AtomicReaderContext context)
      throws IOException {
    this.context = context;
    this.reader = context.reader();
    this.docBase = context.docBase;

  }

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}
	
	
	@Override
	public String toString() {
		return "references[cache:" + referenceField + "]";
	}
	
	/** Returns a hash code value for this object. */
	public int hashCode() {
	  // when using docToDocidsCache.hashCode() java tries to compute hashCode of all objects inside
		return referenceField.hashCode() ^ (docToDocidsCache!=null ? docToDocidsCache.size() : 0);
	}
	
	
	
}
