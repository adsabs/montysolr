package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.solr.search.CitationLRUCache;

/**
 * Find the papers that are citing results of the search
 * 
 *   - find docs that are citing us 
 *   - and set their score to the score of the paper they cite
       later it will get turned into arithmetic mean 
 *
 */
public class SecondOrderCollectorCitingTheMostCited extends AbstractSecondOrderCollector {

  
  private CacheWrapper cache;
  private String boostField;
  private boolean initialized;
  private float[] boostCache;


  public SecondOrderCollectorCitingTheMostCited(CacheWrapper cache, String boostField) {
    super();
    assert cache != null;
    this.boostField = boostField;
    initialized = false;
  }
  
  
  @Override
  public boolean searcherInitialization(IndexSearcher searcher, Weight firstOrderWeight) throws IOException {
  	cache.collectorInitialized(searcher, firstOrderWeight);
    if (initialized == false) {
      boostCache = FieldCache.DEFAULT.getFloats(cache.getAtomicReader(), 
          boostField, false);
      initialized = true;
    }
    if (boostCache.length == 0) {
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
  	int[] related = cache.getLuceneDocIds(doc+docBase);
  	
    if (related == null) return;
    float s = scorer.score();
    
    // adjusted score of this paper
    s = s + (s * boostCache[doc+docBase]);
    
    float freq = (float) related.length;
    
    // find docs that are citing us and set their score to the score of the paper they cite
    // later it will get turned into arithmetic mean 
    for (int v: related) {
    	if (v < 0)
    		continue;
      hits.add(new CollectorDoc(v, s, -1, freq));
    }
    
  }


  @Override
  public boolean acceptsDocsOutOfOrder() {
    return true;
  }
  
  
  @Override
  public String toString() {
    //return "citingMostCited[using:" + boostField + "<" + referenceField  + ":" + fieldsToStr(uniqueIdField) + ">]";
  	return "citingMostCited[using:" + boostField + "<" + cache.toString() + ">]";
  }
  
  /** Returns a hash code value for this object. */
  public int hashCode() {
    return cache.hashCode() ^ boostCache.hashCode();
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
     this.docBase = context.docBase;
  }
  
  
}
