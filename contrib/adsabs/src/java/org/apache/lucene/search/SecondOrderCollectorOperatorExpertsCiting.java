package org.apache.lucene.search;

import java.io.IOException;
import java.util.Set;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.solr.search.CitationLRUCache;

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
	private SolrCacheWrapper<CitationLRUCache<Object, Integer>> cache;
	private LuceneCacheWrapper<NumericDocValues> boostCache;
  private int hashCode;
	
	public SecondOrderCollectorOperatorExpertsCiting(
	      SolrCacheWrapper<CitationLRUCache<Object, Integer>> cache, 
	      LuceneCacheWrapper<NumericDocValues> boostWrapper) {
		super();
		
		assert cache != null;
		this.cache = cache;
		this.boostCache = boostWrapper;
		setFinalValueType(FinalValueType.AGRESTI_COULL);
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
		
		float bc = 0.0f;
		if ((bc = boostCache.getFloat(doc+docBase)) >  0.0f) {
			s = s + bc;
		}
		

		//s = s / (vals.length + 100); // this would contribute only a part of the score to each citation
		for (int docid: references) {
			if (docid > 0) {
				//System.out.println("expert: doc=" + (doc+docBase) + "(score:" + s + ") adding=" + docid + " (score:" + (s + boostCache[docid]) + ")" + " freq=" + references.length) ;
				hits.add(new CollectorDoc(docid, s + boostCache.getFloat(docid), -1, 1));
			}
		}
	}

	@Override
	public void doSetNextReader(LeafReaderContext context)
			throws IOException {
		this.docBase = context.docBase;
	}

	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(cache=" + cache.toString() + ", boost=" + boostCache.toString() + ")";
	}
	
	/** Returns a hash code value for this object. */
	public int hashCode() {
	  if (hashCode == 0) {
      hashCode = computeHashCode();
      assert hashCode != 0;
    }
    assert hashCode == computeHashCode();
    return hashCode;
	}


  private int computeHashCode() {
    if (boostField != null)
      return 1301081 ^ boostField.hashCode() ^ cache.hashCode();
    return 1301081 ^ cache.hashCode();
  }


  @Override
  public boolean needsScores() {
    return true;
  }
	
	
	
}
