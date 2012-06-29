package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.document.FieldSelectorResult;
import org.apache.lucene.index.IndexReader;

public class SecondOrderCollectorCites extends AbstractSecondOrderCollector {

	protected FieldSelector fldSel;
	protected Map<String, Integer> valueToDocidCache;
	protected String referenceField;
	protected String uniqueIdField;
	
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
		fldSel = new FieldSelector() {
		      public FieldSelectorResult accept(String fieldName) {
		        return fieldName.equals(referenceField) ? 
		            FieldSelectorResult.LOAD:
		              FieldSelectorResult.NO_LOAD;
		      }
		    };
	}
	
	
	@Override
	public void searcherInitialization(Searcher searcher) throws IOException {
		if (valueToDocidCache == null) {
			valueToDocidCache = DictionaryRecIdCache.INSTANCE.
				getTranslationCacheString(((IndexSearcher) searcher).getIndexReader(), uniqueIdField);
			
		}
		initSubReaderRanges(((IndexSearcher) searcher).getIndexReader());
	}
	

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;

	}

	@Override
	public void collect(int doc) throws IOException {
		//if (reader.isDeleted(doc)) return;
		
		Document document = reader.document(doc, fldSel);
		
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
	public void setNextReader(IndexReader reader, int docBase)
			throws IOException {
		this.reader = reader;
		this.docBase = docBase;

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
