package org.apache.lucene.search;


public interface CacheWrapper {
  
  /*
  public int getLuceneDocId(int sourceDocid);
  
  public int[] getLuceneDocIds(int sourceDocid);
  
  public int getLuceneDocId(int sourceDocid, Object sourceValue);
  
  public int[] getLuceneDocIds(int sourceDocid, Object sourceValue);
  */

    /*
     * Make sure to return a hashcode that reflect nature of this
     * cache contents, but it is not too slow to be computed!
     */
    int internalHashCode();

    String internalToString();


    /*
     * Will be called before the search is executed; here you can
     * initialize the cache
     */
    void init();

    /*
     * Normally, 2nd order queries will call this method to signalize
     * that the search is done and the resources can be freed. Failure
     * to do so may result in very high memory allocation and consequent
     * OOM errors (e.g. the queries can be cached and if they carry with
     * themselves references to a cache; the cache cannot be reclaimed)
     */
    void close();


}
