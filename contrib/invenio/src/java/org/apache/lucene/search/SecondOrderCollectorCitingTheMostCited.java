package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.index.AtomicReaderContext;

/**
 * Find the papers that are citing results of the search
 * 
 *   - find docs that are citing us 
 *   - and set their score to the score of the paper they cite
       later it will get turned into arithmetic mean 
 *
 */
public class SecondOrderCollectorCitingTheMostCited extends AbstractSecondOrderCollector {

  
  private int[][] invertedIndex = null;
  private String referenceField = null;
  private String[] uniqueIdField = null;
  private AtomicReaderContext context;
  private CacheGetter cacheGetter;
  private String boostField;
  private float[] boostCache;


  public SecondOrderCollectorCitingTheMostCited(CacheGetter getter) {
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
  public SecondOrderCollectorCitingTheMostCited(String[] uniqueIdField, String referenceField, String boostField) {
    super();
    this.referenceField = referenceField;
    this.uniqueIdField = uniqueIdField;
    this.boostField = boostField;
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
      
      boostCache = FieldCache.DEFAULT.getFloats(DictionaryRecIdCache.INSTANCE.getAtomicReader(((IndexSearcher) searcher).getIndexReader()), 
          boostField, false);
    }
    if (invertedIndex == null || invertedIndex.length == 0 || boostCache.length == 0) {
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
    
    // adjusted score of this paper
    s = s + (s * boostCache[doc+docBase]);
    
    float freq = (float) invertedIndex[doc+docBase].length;
    
    // find docs that are citing us and set their score to the score of the paper they cite
    // later it will get turned into arithmetic mean 
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
    return "citingMostCited[using:" + boostField + "<" + referenceField  + ":" + uniqueIdField + ">]";
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
