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

	
	private int[][] invertedIndex = null;
	private String referenceField = null;
	private String[] uniqueIdField = null;
  private AtomicReaderContext context;
  private CacheGetter cacheGetter;


	public SecondOrderCollectorCitedBy(CacheGetter getter) {
		super();
		assert getter != null;
		cacheGetter = getter;
	}
	
	/**
	 * If you use this constructor, then we'll construct the inverted
	 * cache for you - using the uniqueField to identify translation
	 * from the string value into lucene ids. And then from there we'll
	 * find out which documents are pointing at these other documents
	 * (referenceField must contain uniqueField values)
	 * 
	 * @param field
	 * @param cacheField
	 */
	public SecondOrderCollectorCitedBy(String[] uniqueIdField, String referenceField) {
		super();
		this.referenceField = referenceField;
		this.uniqueIdField = uniqueIdField;
		assert this.referenceField != null && this.uniqueIdField != null;
	}
	
	@Override
	public boolean searcherInitialization(IndexSearcher searcher, Weight firstOrderWeight) throws IOException {
		if (invertedIndex == null) {
		  if (cacheGetter != null) {
		    invertedIndex = (int[][]) cacheGetter.getCache();
		  }
		  else {
			invertedIndex = DictionaryRecIdCache.INSTANCE.
				getUnInvertedDocidsStrField(searcher, 
				uniqueIdField, referenceField);
		  }
		}
		
		if (invertedIndex == null || invertedIndex.length == 0) {
			return false;
		}
		return super.searcherInitialization(searcher, firstOrderWeight);
	}
	

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;

	}

	@Override
	public void collect(int doc) throws IOException {
		if (invertedIndex[doc+docBase] == null) return;
		float s = scorer.score();
		float freq = (float) invertedIndex[doc+docBase].length;
		for (int v: invertedIndex[doc+docBase]) {
			hits.add(new CollectorDoc(v, s, -1, freq));
		}
		
	}


	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}
	
	
	@Override
	public String toString() {
		return "citations[cache:" + referenceField + "<" + fieldsToStr(uniqueIdField) + ">]";
	}
	
	/** Returns a hash code value for this object. */
	public int hashCode() {
		return uniqueIdField.hashCode() ^ (invertedIndex !=null ? invertedIndex.hashCode() : 0) 
			^ referenceField.hashCode();
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
     this.context = context;
     this.docBase = context.docBase;
  }
	
	
}
