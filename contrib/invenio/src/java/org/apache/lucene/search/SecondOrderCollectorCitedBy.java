package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;

public class SecondOrderCollectorCitedBy extends AbstractSecondOrderCollector {

	
	private int[][] invertedIndex;
	private String referenceField;
	private String uniqueIdField;
  private AtomicReaderContext context;


	public SecondOrderCollectorCitedBy(int[][] invertedIndex, String uniqueIdField, String referenceField) {
		super();
		this.invertedIndex = invertedIndex;
		this.uniqueIdField = uniqueIdField;
		hits = new ArrayList<ScoreDoc>();
		docBase = 0;
		this.referenceField = referenceField;
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
	public SecondOrderCollectorCitedBy(String uniqueIdField, String referenceField) {
		super();
		this.invertedIndex = null;
		this.uniqueIdField = uniqueIdField;
		hits = new ArrayList<ScoreDoc>();
		docBase = 0;
		this.referenceField = referenceField;
	}
	
	@Override
	public void searcherInitialization(IndexSearcher searcher) throws IOException {
		if (invertedIndex == null) {
			invertedIndex = DictionaryRecIdCache.INSTANCE.
				getUnInvertedDocidsStrField(((IndexSearcher) searcher).getIndexReader(), 
				uniqueIdField, referenceField);
		}
	}
	

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;

	}

	@Override
	public void collect(int doc) throws IOException {
		if (invertedIndex[doc+docBase] == null) return;
		float s = scorer.score();
		for (int v: invertedIndex[doc+docBase]) {
			hits.add(new ScoreDoc(v, s));
		}
		
	}


	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}
	
	
	@Override
	public String toString() {
		return "citedby[using:" + referenceField + "<" + uniqueIdField + ">]";
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
