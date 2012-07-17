package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;

public class SecondOrderCollectorCites extends AbstractSecondOrderCollector {

  Set<String> fieldsToLoad;
	protected Map<String, Integer> valueToDocidCache;
	protected String referenceField;
	protected String uniqueIdField;
	private AtomicReaderContext context;
	private IndexReader reader;
	
	public SecondOrderCollectorCites(Map<String, Integer> cache, String uniqueIdField, String referenceField) {
		super();
		valueToDocidCache = cache;
		this.uniqueIdField = uniqueIdField;
		this.referenceField = referenceField;
		docBase = 0;
		initFldSelector();
	}
	
	public SecondOrderCollectorCites(String uniqueIdField, final String referenceField) {
		super();
		valueToDocidCache = null;
		this.uniqueIdField = uniqueIdField;
		this.referenceField = referenceField;
		docBase = 0;
		initFldSelector();
	}
	
	private void initFldSelector() {
	  fieldsToLoad = new HashSet<String>();
    fieldsToLoad.add(referenceField);
	}
	
	
	@Override
	public void searcherInitialization(IndexSearcher searcher) throws IOException {
		if (valueToDocidCache == null) {
			valueToDocidCache = DictionaryRecIdCache.INSTANCE.
				getTranslationCacheString(((IndexSearcher) searcher).getIndexReader(), uniqueIdField);
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
				hits.add(new ScoreDoc((Integer)valueToDocidCache.get(v), s));
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
		return "cites[using:" + referenceField + "]";
	}
	
	/** Returns a hash code value for this object. */
	public int hashCode() {
		return referenceField.hashCode() ^ (valueToDocidCache != null ? valueToDocidCache.hashCode() : 0);
	}
	
	
	
}
