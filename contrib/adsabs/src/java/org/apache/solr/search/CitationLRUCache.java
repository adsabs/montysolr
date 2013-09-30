/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.search;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.FixedBitSet;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;


/**
 * Implementation of a cache for second order operations. This cache
 * will first construct a mapping from identifiers to lucene ids.
 * Next, it will read all values from a document field and build
 * in-memory data structure that can be used to tell what documents
 * are related.
 * 
 * For the time being, we read whole index into memory to create 
 * a citation network, but this implementation should also be
 * capable of holding only partial (the most accessed) citation 
 * network in memory. However, the initial mapping (value<->lucene id)
 * will always be constructed in its entirety.
 * 
 */
public class CitationLRUCache<K,V> extends SolrCacheBase implements SolrCache<K,V> {

  /* An instance of this class will be shared across multiple instances
   * of an LRUCache at the same time.  Make sure everything is thread safe.
   */
  private static class CumulativeStats {
    AtomicLong lookups = new AtomicLong();
    AtomicLong hits = new AtomicLong();
    AtomicLong inserts = new AtomicLong();
    AtomicLong evictions = new AtomicLong();
  }

  private CumulativeStats stats;

  // per instance stats.  The synchronization used for the map will also be
  // used for updating these statistics (and hence they are not AtomicLongs
  private long lookups;
  private long hits;
  private long inserts;
  private long evictions;

  private long warmupTime = 0;

  private Map<K,V> map;
  private String description="LRU Cache";

  public Object init(Map args, Object persistence, CacheRegenerator regenerator) {
    super.init(args, regenerator);
    String str = (String)args.get("size");
    final int limit = str==null ? 1024 : Integer.parseInt(str);
    str = (String)args.get("initialSize");
    final int initialSize = Math.min(str==null ? 1024 : Integer.parseInt(str), limit);
    description = generateDescription(limit, initialSize);

    map = new LinkedHashMap<K,V>(initialSize, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
          if (size() > limit) {
            // increment evictions regardless of state.
            // this doesn't need to be synchronized because it will
            // only be called in the context of a higher level synchronized block.
            evictions++;
            stats.evictions.incrementAndGet();
            return true;
          }
          return false;
        }
      };

    if (persistence==null) {
      // must be the first time a cache of this type is being created
      persistence = new CumulativeStats();
    }

    stats = (CumulativeStats)persistence;

    return persistence;
  }

  /**
   * 
   * @return Returns the description of this cache. 
   */
  private String generateDescription(int limit, int initialSize) {
    String description = "CitationLRU Cache(maxSize=" + limit + ", initialSize=" + initialSize;
    if (isAutowarmingOn()) {
      description += ", " + getAutowarmDescription();
    }
    description += ')';
    return description;
  }

  public int size() {
    synchronized(map) {
      return map.size();
    }
  }

  public V put(K key, V value) {
    synchronized (map) {
      if (getState() == State.LIVE) {
        stats.inserts.incrementAndGet();
      }

      // increment local inserts regardless of state???
      // it does make it more consistent with the current size...
      inserts++;
      return map.put(key,value);
    }
  }

  public V get(K key) {
    synchronized (map) {
      V val = map.get(key);
      if (getState() == State.LIVE) {
        // only increment lookups and hits if we are live.
        lookups++;
        stats.lookups.incrementAndGet();
        if (val!=null) {
          hits++;
          stats.hits.incrementAndGet();
        }
      }
      return val;
    }
  }

  public void clear() {
    synchronized(map) {
      map.clear();
    }
  }

  public void warm(SolrIndexSearcher searcher, SolrCache<K,V> old) {
    if (regenerator==null) return;
    long warmingStartTime = System.currentTimeMillis();
    CitationLRUCache<K,V> other = (CitationLRUCache<K,V>)old;

    // collect ids of documents that need to be reloaded/regenerated during this
    // warmup run
    FixedBitSet toRefresh = new FixedBitSet(searcher.getIndexReader().maxDoc());
    
    
    Bits liveDocs = searcher.getAtomicReader().getLiveDocs();
    if (liveDocs == null && other.map.size() > 0) { // everything is new, this could be fresh index or merged/optimized index too
      other.map.clear(); // force regeneration
      toRefresh.set(0, toRefresh.length());
    }
    else if (liveDocs != null) {
      for (int i = 0; i < liveDocs.length(); i++) {
        if (liveDocs.get(i)) {
          toRefresh.set(i);
        }
      }
    }
    
    
    // warm entries
    if (isAutowarmingOn()) {
      Object[] keys,vals = null;

      // Don't do the autowarming in the synchronized block, just pull out the keys and values.
      synchronized (other.map) {
        
        int sz = autowarm.getWarmCount(other.map.size());
        
        keys = new Object[sz];
        vals = new Object[sz];

        Iterator<Map.Entry<K, V>> iter = other.map.entrySet().iterator();

        // iteration goes from oldest (least recently used) to most recently used,
        // so we need to skip over the oldest entries.
        int skip = other.map.size() - sz;
        for (int i=0; i<skip; i++) iter.next();


        for (int i=0; i<sz; i++) {
          Map.Entry<K,V> entry = iter.next();
          keys[i]=entry.getKey();
          vals[i]=entry.getValue();
        }
      }

      
      
      // autowarm from the oldest to the newest entries so that the ordering will be
      // correct in the new cache.
      for (int i=0; i<keys.length; i++) {
        try {
          boolean continueRegen = true;
          if (isModified(liveDocs, keys[i], vals[i])) {
            toRefresh.set((Integer) keys[i]);
          }
          else {
            continueRegen = regenerator.regenerateItem(searcher, this, old, keys[i], vals[i]);
          }
          if (!continueRegen) break;
        }
        catch (Throwable e) {
          SolrException.log(log,"Error during auto-warming of key:" + keys[i], e);
        }
      }
    }

    warmupTime = System.currentTimeMillis() - warmingStartTime;
  }

  /*
   * Checks whether the cache needs to be rebuilt for this 
   * document, eg. if the key points to a deleted document
   * or if one of the values point at a deleted document
   */
  private boolean isModified(Bits liveDocs, Object cacheKey, Object cacheValue) {
    if (liveDocs.get((Integer) cacheKey)) {
      return true;
    }
    for (Integer luceneId: (Integer[]) cacheValue) {
      if (!liveDocs.get(luceneId)) {
        return true;
      }
    }
    return false;
  }

  public void close() {
  }


  //////////////////////// SolrInfoMBeans methods //////////////////////


  public String getName() {
    return CitationLRUCache.class.getName();
  }

  public String getDescription() {
     return description;
  }

  public String getSource() {
    return "$URL: http://svn.apache.org/repos/asf/lucene/dev/branches/lucene_solr_4_0/solr/core/src/java/org/apache/solr/search/LRUCache.java $";
  }

  public NamedList getStatistics() {
    NamedList lst = new SimpleOrderedMap();
    synchronized (map) {
      lst.add("lookups", lookups);
      lst.add("hits", hits);
      lst.add("hitratio", calcHitRatio(lookups,hits));
      lst.add("inserts", inserts);
      lst.add("evictions", evictions);
      lst.add("size", map.size());
    }
    lst.add("warmupTime", warmupTime);
    
    long clookups = stats.lookups.get();
    long chits = stats.hits.get();
    lst.add("cumulative_lookups", clookups);
    lst.add("cumulative_hits", chits);
    lst.add("cumulative_hitratio", calcHitRatio(clookups,chits));
    lst.add("cumulative_inserts", stats.inserts.get());
    lst.add("cumulative_evictions", stats.evictions.get());
    
    return lst;
  }

  @Override
  public String toString() {
    return name() + getStatistics().toString();
  }
}
