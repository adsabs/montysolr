package org.apache.lucene.search;

public class CacheGetter {
  
  public Object getCache() {
    throw new IllegalAccessError("This method must be overriden");
  }
  
  
}
