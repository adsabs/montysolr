package org.apache.lucene.search;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;

/**
 * Finds papers that are cited by our search. And then adjusts the score so that
 * papers with most value from the cited_read_boost field are up 
 *
 */
public class SecondOrderCollectorOperatorExpertsCiting extends AbstractSecondOrderCollector {

  Set<String> fieldsToLoad;
	protected Map<String, Integer> valueToDocidCache = null;
	protected String referenceField;
	protected String uniqueIdField;
	protected String boostField;
	private AtomicReaderContext context;
	private IndexReader reader;
	private CacheGetter cacheGetter;
	
	public SecondOrderCollectorOperatorExpertsCiting(CacheGetter getter, String referenceField) {
		super();
		initFldSelector();
		cacheGetter = getter;
		this.referenceField = referenceField;
		assert referenceField != null;
		initFldSelector();
	}
	
	public SecondOrderCollectorOperatorExpertsCiting(String uniqueIdField, String referenceField, String boostField) {
		super();
		valueToDocidCache = null;
		this.uniqueIdField = uniqueIdField;
		this.referenceField = referenceField;
		this.boostField = boostField;
		initFldSelector();
	}
	
	private void initFldSelector() {
	  fieldsToLoad = new HashSet<String>();
    fieldsToLoad.add(referenceField);
    fieldsToLoad.add(boostField);
	}
	
	
	@SuppressWarnings("unchecked")
  @Override
	public boolean searcherInitialization(IndexSearcher searcher) throws IOException {
		if (valueToDocidCache == null) {
		  if (cacheGetter != null) {
		    valueToDocidCache = (Map<String, Integer>) cacheGetter.getCache();
		  }
		  else {
  			valueToDocidCache = DictionaryRecIdCache.INSTANCE.
  				getTranslationCacheString(searcher.getIndexReader(), uniqueIdField);
		  }
		}
		if (valueToDocidCache == null || valueToDocidCache.size() == 0) {
			return false;
		}
		return super.searcherInitialization(searcher);
	}
	

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;

	}

	@Override
	public void collect(int doc) throws IOException {
		
		Document document = reader.document(doc, fieldsToLoad);
		
		float s = scorer.score();
		
		// naive implementation (probably slow)
		IndexableField bf = document.getField(boostField);
		//if (bf==null) throw new IOException("Every document must have field: " + boostField);
		
		// TODO: we must find the proper values for this, that means to compute the statistics
		// (find the local minimas, maximas) for this function; this is just guessing....
		
		
    if (bf != null) {
      s = s + (s * bf.numericValue().floatValue());
    }
    else {
      // penalize docs without boost
      s = s * 0.8f;
    }
		
    // find documents that are cited by our doc (references)
		String[] vals = document.getValues(referenceField);
		//s = s / (vals.length + 100); // this would contribute only a part of the score to each citation
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
		return "cites[using:" + referenceField + "<weightedBy:" + boostField + ">]";
	}
	
	/** Returns a hash code value for this object. */
	public int hashCode() {
		return referenceField.hashCode() ^ (valueToDocidCache != null ? valueToDocidCache.size() : 0);
	}
	
	
	
}
