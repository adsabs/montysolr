package org.apache.solr.search;

import java.util.Iterator;

/**
 * Main API to deal with citation graph. Implementations are free to use lucene's
 * inverted index, docvalues or other ways of getting data.
 *
 * @param <K>
 * @param <V>
 */
public interface CitationCache<K,V> extends SolrCache<K, V> {
  
  /**
   * Retrieve list of documents that *cite* our
   * lucene docid
   * 
   * @param docid
   * @return list of docids
   */
  public int[] getCitations(int docid);
  
  /**
   * Retrieve list of documents that *cite* our
   * value (e.g. bibcode)
   * 
   * @param key
   * @return list of docids
   */
  public int[] getCitations(K key);
  
  /**
   * Retrieve list of documents that are cited by our
   * lucene docid
   * 
   * @param docid
   * @return list of docids
   */
  public int[] getReferences(int docid);
  
  /**
   * Retrieve list of documents that are cited by our
   * value (e.g. bibcode)
   * 
   * @param key
   * @return list of docids
   */
  public int[] getReferences(K key);
  
  /**
   * Returns false only when the cache is 'cold'; this flag
   * is used to synchronize search threads to avoid sitution
   * when several of them would like to start loading data.
   * 
   * @return True when the first thread starts doing its
   *    work
   */
  public boolean isWarmingOrWarmed();
  
  /**
   * This method should be used only for very specific purposes of
   * dumping the citation cache (or accessing all elements of 
   * the cache). Access to the map is not synchronized, but you
   * are iterating over a copy of data - so yo cannot change it
   * 
   * The first comes references, the second are citations
   * @return iterator over list of ints[]
   */
  public  Iterator<int[][]> getCitationGraph();
}
