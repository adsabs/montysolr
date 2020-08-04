package org.apache.lucene.search;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.NumericDocValues;
import org.apache.solr.uninverting.UninvertingReader;
import org.apache.solr.uninverting.UninvertingReader.Type;

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
	
	public static LuceneCacheWrapper<NumericDocValues> getFloatCache(
	    String fieldName, 
	    Type type, 
	    LeafReader reader)
	    throws IOException  {
		
	  Map<String, UninvertingReader.Type> mapping = new HashMap<String, UninvertingReader.Type>();
    mapping.put(fieldName, type);
    LeafReader uninvertingReader = UninvertingReader.wrap(reader, mapping::get);
    NumericDocValues values = uninvertingReader.getNumericDocValues(fieldName);
    
    if (values == null)
      values = DocValues.emptyNumeric();
    
		final String fName = fieldName;
		LuceneCacheWrapper<NumericDocValues> newCache = new LuceneCacheWrapper<NumericDocValues>(new SoftReference<NumericDocValues>(values)) {
			@Override
		  public String internalToString() {
				return "float[] " + fName;
		  }
			@Override
			public float getFloat(int docid) {
			  NumericDocValues ref = this.cache.get();
				try {
          if (ref.advanceExact(docid)) {
            return (float) ref.longValue();
          }
        } catch (IOException e) {
          // TODO:rca - propagate instead?
          e.printStackTrace();
        }
				return 0.0f;
			}
		};
		
		return newCache;
		
	}

}
