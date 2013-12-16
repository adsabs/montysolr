package org.apache.lucene.search;

import org.apache.lucene.index.AtomicReader;

public interface CacheWrapper {
  
  
  public int getLuceneDocId(int sourceDocid);
  
  public int[] getLuceneDocIds(int sourceDocid);
  
  public int getLuceneDocId(int sourceDocid, Object sourceValue);
  
  public int[] getLuceneDocIds(int sourceDocid, Object sourceValue);
  
  public AtomicReader getAtomicReader();
  
  /*
   * Make sure to return a hashcode that reflect nature of this 
   * cache contents, but it is not too slow to be computed!
   */
  public int internalHashCode();
  
  public String internalToString();
  
}
