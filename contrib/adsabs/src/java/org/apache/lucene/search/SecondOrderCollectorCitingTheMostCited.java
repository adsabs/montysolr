package org.apache.lucene.search;

import java.io.IOException;

/**
 * Find the papers that are citing results of the search
 * 
 *   - find docs that are citing us 
 *   - and set their score to the score of the paper they cite
       later it will get turned into arithmetic mean 
 *
 */
public class SecondOrderCollectorCitingTheMostCited extends AbstractSecondOrderCollector {

  
  private SolrCacheWrapper cache;
	private LuceneCacheWrapper boostCache;


  public SecondOrderCollectorCitingTheMostCited(SolrCacheWrapper cache, LuceneCacheWrapper boostCache) {
    super();
    assert cache != null;
    this.cache = cache;
    this.boostCache = boostCache;
  }
  
  

  @Override
  public void collect(int doc) throws IOException {
  	int[] related = cache.getLuceneDocIds(doc+docBase);
  	
  	//System.out.println("Collecting: doc=" + doc + "docBase:" +docBase);
    if (related == null) return;
    //float s = scorer.score();
    float s = 0.5f; // lucene score doesn't make sense for us;
    
    // adjusted score of this paper
    // s = s + (s * boostCache[doc+docBase]);
    
    float bc = 0.0f;
    if ((bc = boostCache.getFloat(doc+docBase)) >  0.0f) {
			s = s + bc;
		}
    
    // find docs that are citing us and set their score to the score of the paper they cite
    // later it will get turned into arithmetic mean 
    for (int docid: related) {
    	if (docid < 0)
    		continue;
    	//System.out.println("expert: doc=" + (doc+docBase) + "(score:" + s + ") adding=" + docid + " (score:" + (s + boostCache[docid]) + ")" + " freq=" + related.length) ;
      hits.add(new CollectorDoc(docid, s, related.length));
    }
    
  }


  
  	@Override
  	public String toString() {
  		return this.getClass().getSimpleName() + "(cache=" + cache.toString() + ", boost=" + boostCache.toString() + ")";
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
  	public boolean needsScores() {
  		return true;
  	}

	@Override
	public SecondOrderCollector copy() {
		return new SecondOrderCollectorCitingTheMostCited(cache, boostCache);
	}
	
}
