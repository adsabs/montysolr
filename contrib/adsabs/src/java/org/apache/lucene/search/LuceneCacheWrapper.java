package org.apache.lucene.search;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.apache.commons.lang.NotImplementedException;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.search.FieldCache.Floats;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;

public class LuceneCacheWrapper<T> implements CacheWrapper {
	
	String str;
	int hashCode;
	public Reference<T> cache;

	public LuceneCacheWrapper(Reference<T> cache) {
		this.cache = cache;
	  this.str = internalToString(); // we cache these values because cache may disappear
	  this.hashCode = internalHashCode();
  }
	
	@Override
	public void init() {
		// do nothing
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
  public void close() {
    cache.clear();
  }
	
	@Override
  public String internalToString() {
		throw new NotImplementedException();
  }
	
	public float getFloat(int docid) {
		throw new NotImplementedException();
	}
	
	public static LuceneCacheWrapper<Floats> getFloatCache(String fieldName, AtomicReader reader)  {
		
		Floats data;
    try {
      data = FieldCache.DEFAULT.getFloats(reader, fieldName, false);
    } catch (IOException e) {
      throw new SolrException(ErrorCode.SERVER_ERROR, e);
    }
    
		final String fName = fieldName;
		LuceneCacheWrapper<Floats> newCache = new LuceneCacheWrapper<Floats>(new SoftReference<Floats>(data)) {
			@Override
		  public String internalToString() {
				return "float[] " + fName;
		  }
			@Override
			public float getFloat(int docid) {
				Floats c = cache.get();
				// XXX: rca - this was the old logic; what happens if docid is bigger than current index?
				//if (docid < c.length) {
				//	return c[docid]; 
				//}
				//return -1.0f;
				return c.get(docid);
			}
		};
		
		return newCache;
		
	}

	public static LuceneCacheWrapper<Floats> getFloatCache(String fieldName, Floats data)  {
		
		final String fName = fieldName;
		LuceneCacheWrapper<Floats> newCache = new LuceneCacheWrapper<Floats>(new SoftReference<Floats>(data)) {
			@Override
		  public String internalToString() {
				return "float[] " + fName;
		  }
			@Override
			public float getFloat(int docid) {
			  Floats c = cache.get();
        // XXX: rca - this was the old logic; what happens if docid is bigger than current index?
        //if (docid < c.length) {
        //  return c[docid]; 
        //}
        //return -1.0f;
        return c.get(docid);
			}
		};
		
		return newCache;
		
	}
	
	
}
