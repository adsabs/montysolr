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
	protected String referenceField;
	protected String[] uniqueIdField;
	protected String boostField;
	private IndexReader reader;
	private CacheWrapper cache;
	private float[] boostCache;
	
	public SecondOrderCollectorOperatorExpertsCiting(CacheWrapper cache, String boostField) {
		super();
		
		assert cache != null;
		this.cache = cache;
		this.boostField = boostField;
		setFinalValueType(FinalValueType.AGRESTI_COULL);
	}
	
	@Override
  public boolean searcherInitialization(IndexSearcher searcher, Weight firstOrderWeight) throws IOException {
    boostCache = FieldCache.DEFAULT.getFloats(cache.getAtomicReader(), 
        boostField, false);
    if (boostCache.length == 0) {
    	return false;
    }
    return super.searcherInitialization(searcher, firstOrderWeight);
  }
	

	@Override
	public void collect(int doc) throws IOException {
		
    // find references froum our doc 
		int[] references = cache.getLuceneDocIds(doc+docBase);
		
		if (references == null) {
			return;
		}
		
		//Document document = reader.document(doc, fieldsToLoad);
		
		float s = 0.5f; // lucene score doesn't make sense for us;
		
		/*
		// naive implementation (probably slow)
		// IndexableField bf = document.getField(boostField);
		if (bf != null) {
      s = s + (s * bf.numericValue().floatValue());
    }
    else {
      // penalize docs without boost
      s = s * 0.8f;
    }
    */
		
		
		           
		//if (bf==null) throw new IOException("Every document must have field: " + boostField);
		
		// TODO: we must find the proper values for this, that means to compute the statistics
		// (find the local minimas, maximas) for this function; this is just guessing....
		
		
		if (boostCache[doc+docBase] >  0.0f) {
			s = s + boostCache[doc+docBase];
		}
		

		//s = s / (vals.length + 100); // this would contribute only a part of the score to each citation
		for (int docid: references) {
			if (docid > 0) {
				//System.out.println("expert: doc=" + (doc+docBase) + "(score:" + s + ") adding=" + docid + " (score:" + (s + boostCache[docid]) + ")" + " freq=" + references.length) ;
				hits.add(new CollectorDoc(docid, s + boostCache[docid], -1, 1));
			}
		}
	}

	@Override
	public void setNextReader(AtomicReaderContext context)
			throws IOException {
		this.reader = context.reader();
		this.docBase = context.docBase;
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}
	
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(cache:" + cache.toString() + ", field:" + boostField + ")";
	}
	
	/** Returns a hash code value for this object. */
	public int hashCode() {
		return 435878 ^ boostField.hashCode() ^ cache.hashCode();
	}
	
	
	
}
