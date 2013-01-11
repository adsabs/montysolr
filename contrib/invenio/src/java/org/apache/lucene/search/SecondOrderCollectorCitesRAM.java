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

	protected Map<Integer, List<Integer>> docToDocidsCache = null;
	protected String referenceField;
	protected String uniqueIdField;
  private AtomicReaderContext context;
  private AtomicReader reader;
  private CacheGetter cacheGetter;
	
  public SecondOrderCollectorCitesRAM(CacheGetter getter) {
    super();
    cacheGetter = getter;
  }
	
	public SecondOrderCollectorCitesRAM(String uniqueIdField, final String referenceField) {
		super();
		this.uniqueIdField = uniqueIdField;
		this.referenceField = referenceField;
	}
	
	
	
	@SuppressWarnings("unchecked")
  @Override
	public void searcherInitialization(IndexSearcher searcher) throws IOException {
		if (docToDocidsCache == null) {
		  if (cacheGetter != null) {
		    docToDocidsCache = (Map<Integer, List<Integer>>) cacheGetter.getCache();
		  }
		  else {
  			docToDocidsCache = DictionaryRecIdCache.INSTANCE.
  				getCacheTranslatedMultiValuesString(searcher.getIndexReader(), 
  				uniqueIdField, referenceField);
		  }
		}
	}
	

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;

	}

	@Override
	public void collect(int doc) throws IOException {
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
