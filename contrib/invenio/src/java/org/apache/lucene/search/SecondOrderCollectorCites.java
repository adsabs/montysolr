package org.apache.lucene.search;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;

/*
 *    references(P) - set of papers that are in the reference list of P
 *    see: http://labs.adsabs.harvard.edu/trac/ads-invenio/ticket/221
 *    
 *    This implementation reads data directly from the indexed field
 *    
 *    @see: SecondOrderCollectorCitesRAM for the implementation that
 *          uses un-inverted cache stored in RAM
 */

public class SecondOrderCollectorCites extends AbstractSecondOrderCollector {

  Set<String> fieldsToLoad;
	protected Map<String, Integer> valueToDocidCache = null;
	protected String referenceField;
	protected String uniqueIdField;
	private AtomicReaderContext context;
	private IndexReader reader;
	private CacheGetter cacheGetter;
	
	public SecondOrderCollectorCites(CacheGetter getter, String referenceField) {
		super();
		initFldSelector();
		cacheGetter = getter;
		this.referenceField = referenceField;
		assert referenceField != null;
		initFldSelector();
	}
	
	public SecondOrderCollectorCites(String uniqueIdField, String referenceField) {
		super();
		valueToDocidCache = null;
		this.uniqueIdField = uniqueIdField;
		this.referenceField = referenceField;
		initFldSelector();
	}
	
	private void initFldSelector() {
	  fieldsToLoad = new HashSet<String>();
    fieldsToLoad.add(referenceField);
	}
	
	
	@SuppressWarnings("unchecked")
  @Override
	public void searcherInitialization(IndexSearcher searcher) throws IOException {
		if (valueToDocidCache == null) {
		  if (cacheGetter != null) {
		    valueToDocidCache = (Map<String, Integer>) cacheGetter.getCache();
		  }
		  else {
  			valueToDocidCache = DictionaryRecIdCache.INSTANCE.
  				getTranslationCacheString(searcher.getIndexReader(), uniqueIdField);
		  }
		}
	}
	

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;

	}

	@Override
	public void collect(int doc) throws IOException {
		//if (reader.isDeleted(doc)) return;
		
		Document document = reader.document(doc, fieldsToLoad);
		
		float s = scorer.score();
		
		String[] vals = document.getValues(referenceField); 
		for (String v: vals) {
			v = v.toLowerCase();
			if (valueToDocidCache.containsKey(v)) {
				hits.add(new CollectorDoc(valueToDocidCache.get(v), s, -1, vals.length));
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
		return "references[field:" + referenceField + "]";
	}
	
	/** Returns a hash code value for this object. */
	public int hashCode() {
		return referenceField.hashCode() ^ (valueToDocidCache != null ? valueToDocidCache.size() : 0);
	}
	
	
	
}
