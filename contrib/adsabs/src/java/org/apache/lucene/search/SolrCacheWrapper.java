package org.apache.lucene.search;

import java.lang.ref.SoftReference;

import org.apache.commons.lang.NotImplementedException;
import org.apache.solr.search.CitationLRUCache;
import org.apache.solr.search.SolrCache;

public abstract class SolrCacheWrapper<T extends SolrCache> implements CacheWrapper {

	@SuppressWarnings("rawtypes")
  protected SoftReference<T> cache;
	private String str;
	private int hashCode;
	
	@SuppressWarnings("rawtypes")
	public SolrCacheWrapper( T cache) {
		assert cache != null;
	  this.cache = new SoftReference<T>(cache);
	  this.str = internalToString(); // we cache these values because cache may disappear
	  this.hashCode = internalHashCode();
  }

	@Override
	public void init() {
		// do nothing
	}
	
  public int getLuceneDocId(int sourceDocid) {
		throw new NotImplementedException();
  }

  public int[] getLuceneDocIds(int sourceDocid) {
		throw new NotImplementedException();
  }


  public int getLuceneDocId(int sourceDocid, Object sourceValue) {
		throw new NotImplementedException();
  }

  public int[] getLuceneDocIds(int sourceDocid, Object sourceValue) {
		throw new NotImplementedException();
  }
	
	@Override
	public String toString() {
		return this.str;
	}
	
	@Override
	public int hashCode() {
		return this.hashCode;
	}
	@Override
  public int internalHashCode() {
    return cache.get().hashCode();
  }
	@Override
  public String internalToString() {
    return cache.get().name();
  }
	
	@Override
  public void close() {
    cache.clear();
  }
	
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static class ReferencesCache extends SolrCacheWrapper {

		public ReferencesCache(CitationLRUCache cache) {
	    super(cache);
	    
    }
		
		@Override
	  public int[] getLuceneDocIds(int sourceDocid) {
		  return ((CitationLRUCache) cache.get()).getReferences(sourceDocid);
	  }
		
    @Override
		public int getLuceneDocId(int sourceDocid, Object sourceValue) {
    	Object v = ((CitationLRUCache) cache.get()).get(sourceValue);
    	if (v == null)
    		return -1;
    	return (Integer) v;
	  }
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static class CitationsCache extends SolrCacheWrapper {

		public CitationsCache(CitationLRUCache cache) {
	    super(cache);
    }
		
		@Override
	  public int[] getLuceneDocIds(int sourceDocid) {
		  return ((CitationLRUCache) cache.get()).getCitations(sourceDocid);
	  }
		
		@Override
		public int getLuceneDocId(int sourceDocid, Object sourceValue) {
    	Object v = ((CitationLRUCache) cache.get()).get(sourceValue);
    	if (v == null)
    		return -1;
    	return (Integer) v;
	  }
	}
}
