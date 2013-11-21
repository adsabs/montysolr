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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocTermOrds;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldCache.DocTerms;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.FixedBitSet;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.IntField;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.StrField;
import org.apache.solr.schema.TextField;
import org.apache.solr.schema.TrieIntField;


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
 * TODO: I should use DocValues after we move to >= vSolr42
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
  private String description="Citation LRU Cache";

	private String[] identifierFields = null;
	private String[] identifierFieldsMultivalued = null;

	// If we detect that you are mixing int and text fields
	// we'll treat all values (mappings) as text values
	private boolean treatIdentifiersAsText = false;

	private List<String> textClasses;
	private List<String> intClasses;
	private List<String> textClassesMV;
	private List<String> intClassesMV;

  public Object init(Map args, Object persistence, CacheRegenerator regenerator) {
    super.init(args, regenerator);
    
  	identifierFields  = ((String)args.get("identifierFields")).split(",");
    assert (identifierFields != null && identifierFields.length > 0);
    
    textClasses = new ArrayList<String>();
    intClasses = new ArrayList<String>();
    textClassesMV = new ArrayList<String>();
    intClassesMV = new ArrayList<String>();
    
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
    
    //TODO: call createIdentifiersToLuceneIdsMapping
    
    int unknownClasses = 0;
    
    IndexSchema schema = searcher.getCore().getSchema();
  	for (String f: identifierFields) {
  		SchemaField fieldInfo = schema.getField(f);
  		FieldType type = fieldInfo.getType();
  		Class<? extends FieldType> c = type.getClass();
  		if (c.isAssignableFrom(TextField.class) || c.isAssignableFrom(StrField.class)) {
  			if (type.isMultiValued()) {
  				textClassesMV.add(f);
  			}
  			else {
  				textClasses.add(f);
  			}
  		}
  		else if (c.isAssignableFrom(TrieIntField.class) || c.isAssignableFrom(IntField.class)) {
  			if (type.isMultiValued()) {
  				intClassesMV.add(f);
  			}
  			else {
  				intClasses.add(f);
  			}
  		}
  		else {
  			unknownClasses += 1;
  		}
  	}
  	
  	assert unknownClasses == 0;
  	if (textClasses.size() > 0 || textClassesMV.size() > 0) {
  		treatIdentifiersAsText  = true;
  	}
    
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
    if (!liveDocs.get((Integer) cacheKey)) { // doc is deleted
      return true;
    }
    for (Integer luceneId: (Integer[]) cacheValue) {
      if (!liveDocs.get(luceneId) || luceneId == -1) { // some of the linked docs was deleted or unrecognized
        return true;
      }
    }
    return false;
  }

  public void close() {
  }
  
  
  private AtomicReader getAtomicReader(IndexReader reader) throws IOException {
			AtomicReader atomReader = SlowCompositeReaderWrapper.wrap(reader);
			return atomReader;
	}
  
  
  @SuppressWarnings("unchecked")
  private void createIdentifiersToLuceneIdsMapping(AtomicReader reader) throws IOException {
		
  	
  	// load multiple values->idlucene mapping
  	for (String idField: intClassesMV) {
			DocTermOrds unInvertedIndex = new DocTermOrds(reader, idField);
			TermsEnum termsEnum = unInvertedIndex.getOrdTermsEnum(reader);
			if (termsEnum == null) {
				continue;
			}
			DocsEnum docs = null;
			Bits liveDocs = reader.getLiveDocs();
			for (;;) {
				BytesRef term = termsEnum.next();
				if (term == null)
					break;
				
				Integer t = FieldCache.DEFAULT_INT_PARSER.parseInt(term);
				
				docs = termsEnum.docs(liveDocs, docs, 0); // we don't need docFreq
				int i = 0;
				for (;;) {
					Integer d = docs.nextDoc();
					if (d == DocIdSetIterator.NO_MORE_DOCS) {
						break;
					}
					if (treatIdentifiersAsText) {
						put((K)(Integer.toString(t)), (V)d);
					}
					else {
						put((K)t, (V)d);
					}
					i += 1;
					if (i > 1) {
						log.warn("The term {} is used by more than one document; your cache has problems", t);
					}
				}
			}
		}
  	
		for (String idField: textClassesMV) {
			DocTermOrds unInvertedIndex = new DocTermOrds(reader, idField);
			TermsEnum termsEnum = unInvertedIndex.getOrdTermsEnum(reader);
			if (termsEnum == null) {
				continue;
			}
			DocsEnum docs = null;
			Bits liveDocs = reader.getLiveDocs();
			for (;;) {
				BytesRef term = termsEnum.next();
				if (term == null)
					break;
				String t = term.utf8ToString();
				
				docs = termsEnum.docs(liveDocs, docs, 0); // we don't need docFreq
				int i = 0;
				for (;;) {
					Integer d = docs.nextDoc();
					if (d == DocIdSetIterator.NO_MORE_DOCS) {
						break;
					}
					put((K)t, (V)d);
					i += 1;
					if (i > 1) {
						log.warn("The term {} is used by more than one document; your cache has problems", t);
					}
				}
			}
		}
		
  	// load single valued ids into the mapping (should we check if they override
		// already defined values?)
		for (String idField: textClasses) {
			DocTerms idMapping = FieldCache.DEFAULT.getTerms(reader, idField);
			Integer i = 0;
			BytesRef ret = new BytesRef();
			while(i < idMapping.size()) {
			  ret = idMapping.getTerm(i, ret);
			  if (ret.length > 0) {
			    map.put((K)ret.utf8ToString(), (V)i);
			  }
				i++;
			}
		}
		for (String idField: intClasses) {
			int[] idMapping = FieldCache.DEFAULT.getInts(reader, idField, false);
			Integer i = 0;
			while(i < idMapping.length) {
				if (treatIdentifiersAsText) {
					map.put((K)(Integer.toString(idMapping[i])), (V)i);
				}
				else {
					map.put((K)((Integer)idMapping[i]), (V)i);
				}
				i++;
			}
		}

		if (map.containsKey(null))	map.remove(null);
		
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
