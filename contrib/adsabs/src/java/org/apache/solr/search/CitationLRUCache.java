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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.NotImplementedException;
import org.apache.lucene.document.SortedBytesDocValuesField;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.index.DocTermOrds;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.SegmentCommitInfo;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldCache.CacheEntry;
import org.apache.lucene.search.FieldCache.Ints;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.FixedBitSet;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
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

  private LinkedHashMap<K,V> map;
  private String description="Citation LRU Cache";

	private String[] referenceFields;
	private String[] citationFields;
	private String[] identifierFields = null;
	
	private int sourceReaderHashCode = 0;

	// If we detect that you are mixing int and text fields
	// we'll treat all values (mappings) as text values
	private boolean treatIdentifiersAsText = false;
	
	// TODO: i'm planning to add the ability to build the cache
	// incrementally (ie per index segment), but it may
	// not be necessary as we are going to denormalize 
	// citation data outside solr and prepare everything there...
	private boolean incremental = false;

	private boolean reuseCache;
	


  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Object init(Map args, Object persistence, CacheRegenerator regenerator) {
    super.init(args, regenerator);
    
  	identifierFields  = ((String)args.get("identifierFields")).split(",");
    assert (identifierFields != null && identifierFields.length > 0);
    
    
    
    incremental  = "true".equals(((String)args.get("incremental")));
    reuseCache  = "true".equals(((String)args.get("reuseCache")));
    
    citationFields = new String[0];
    referenceFields = new String[0];
    
    if (args.containsKey("referenceFields") && ((String)args.get("referenceFields")).trim().length() > 0) {
    	referenceFields = ((String)args.get("referenceFields")).split(",");
    }
    if (args.containsKey("citationFields") && ((String)args.get("citationFields")).trim().length() > 0) {
    	citationFields = ((String)args.get("citationFields")).split(",");
    }
    
  	Float sizeInPercent = null;
    
    String str = (String)args.get("size");
    if (str != null && str.endsWith("%")) {
    	str = str.substring(0, str.length()-1);
    	sizeInPercent = Integer.parseInt(str) / 100f;
    }
    
    final int limit = str==null ? 1024 : Integer.parseInt(str);
    str = (String)args.get("initialSize");
    
    final int initialSize = Math.min(str==null ? 1024 : Integer.parseInt(str), limit);
    description = generateDescription(limit, initialSize);

    map = new RelationshipLinkedHashMap<K,V>(initialSize, 0.75f, true, 
    		limit, sizeInPercent);

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

  public boolean treatsIdentifiersAsText() {
  	return treatIdentifiersAsText;
  }
  
  public V put(K key, V value) {
  	//System.out.println("put(" + key + "," + value+")");
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
  
  /*
   * This method should be used only for very specific purposes of
   * dumping the citation cache (or accessing all elements of 
   * the cache). Access to the map is not synchronized, but you
   * are iterating over a copy of data - so yo cannot change it
   * 
   * The first comes references, the second are citations
   */
  public  Iterator<int[][]> getCitationsIterator() {
  	return ((RelationshipLinkedHashMap<K,V>) map).getRelationshipsIterator();
  }
  
  public int getCitationsIteratorSize() {
  	synchronized (map) {
  		return ((RelationshipLinkedHashMap<K,V>) map).relationshipsDataSize();
  	}
  }
  
  public int[] getCitations(K key) {
  	synchronized (map) {
  		V val = map.get(key);
  		if (val==null)
  			return null;
  		
      RelationshipLinkedHashMap<K,V> relMap = (RelationshipLinkedHashMap<K,V>) map;
      int[] values = relMap.getCitations((Integer)val);
      
      if (getState() == State.LIVE) {
        // only increment lookups and hits if we are live.
        lookups++;
        stats.lookups.incrementAndGet();
        if (values!=null) {
          hits++;
          stats.hits.incrementAndGet();
        }
      }
      return values;
    }
  }
  
  /*
   * This is a helper method allowing you to retrieve
   * what we have directly using lucene docid
   */
  public int[] getCitations(int docid) {
    synchronized (map) {
      RelationshipLinkedHashMap<K,V> relMap = (RelationshipLinkedHashMap<K,V>) map;
      int[] val = relMap.getCitations(docid);
      
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
  
  public int[] getReferences(K key) {
  	synchronized (map) {
  		V val = map.get(key);
  		if (val==null)
  			return null;
  		
      RelationshipLinkedHashMap<K,V> relMap = (RelationshipLinkedHashMap<K,V>) map;
      int[] values = relMap.getReferences((Integer)val);
      
      if (getState() == State.LIVE) {
        // only increment lookups and hits if we are live.
        lookups++;
        stats.lookups.incrementAndGet();
        if (values!=null) {
          hits++;
          stats.hits.incrementAndGet();
        }
      }
      return values;
    }
  }
  
  /*
   * This is a helper method allowing you to retrieve
   * what we have directly using lucene docid
   */
  public int[] getReferences(int docid) {
    synchronized (map) {
      RelationshipLinkedHashMap<K,V> relMap = (RelationshipLinkedHashMap<K,V>) map;
      int[] val = relMap.getReferences(docid);
      
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
  
  private boolean isWarming = false;

	private boolean purgeCache;
  public boolean isWarmingOrWarmed() {
  	return isWarming;
  }
  
  public void warm(SolrIndexSearcher searcher, SolrCache<K,V> old) {
  	isWarming = true;
  	try {
  		log.info("Warming cache (" + name() + "): " + searcher);
	  	if (this.incremental ) {
	  		warmIncrementally(searcher, old);
	  	}
	  	else {
	      warmRebuildEverything(searcher, old);
	  	}
	  	log.info("Warming cache done (# entries:" + map.size() + "): " + searcher);
  	} 
  	catch (IOException e) {
    	throw new SolrException(ErrorCode.SERVER_ERROR, "Failed to generate initial IDMapping", e);
    }
  	sourceReaderHashCode = searcher.hashCode();
  }
  
  private void warmRebuildEverything(SolrIndexSearcher searcher, SolrCache<K,V> old) throws IOException {
  	
  	Map<String, List<String>> fields = getFields(searcher, this.identifierFields);
  	if (fields.get("textFields").size() > 0 || fields.get("textFieldsMV").size() > 0) {
  		synchronized (map) {
  			treatIdentifiersAsText  = true;
      }
  	}

  	// builds the mapping from document ID's to lucene docids
  	unInvertedTheDamnThing(searcher.getAtomicReader(), fields,
  			null,
  			new KVSetter() {
	  		@Override
	  		@SuppressWarnings({ "unchecked" })
	      public void set (int docbase, int docid, Object value) {
	    		put((K) value, (V) (Integer) (docbase+docid));
	    	}
	  	}
  	);
  	
  	if (this.referenceFields.length == 0 && this.citationFields.length == 0) {
  		return;
  	}
  	
  	if (this.referenceFields.length > 0 || this.citationFields.length > 0) {
	  	@SuppressWarnings("rawtypes")
      final RelationshipLinkedHashMap relMap = (RelationshipLinkedHashMap) map;
	  	relMap.initializeCitationCache(searcher.maxDoc());
	  	
	  	
	  	unInvertedTheDamnThing(searcher.getAtomicReader(), getFields(searcher, this.referenceFields), 
	  			null,
	  			new KVSetter() {
			  		@Override
			      public void set (int docbase, int docid, Object value) {
			  			synchronized (relMap) {
			  				relMap.addReference(docbase+docid, value);
	            }
			    	}
			  	}
	  	);
	  	
	  	unInvertedTheDamnThing(searcher.getAtomicReader(), getFields(searcher, this.citationFields),
	  			null,
	  			new KVSetter() {
			  		@Override
			      public void set (int docbase, int docid, Object value) {
			  			synchronized (relMap) {
			  				relMap.addCitation(docbase+docid, value);
	            }
			    	}
			  	}
	  	);
	  	
	  	if (this.citationFields.length == 0 && this.referenceFields.length > 0) {
	  		relMap.inferCitationsFromReferences();
	  	}
	  	else if (this.citationFields.length > 0  && this.referenceFields.length == 0) {
	  		relMap.inferReferencesFromCitations();
	  	}
  	}
  	
  	
  }
  
  private void warmIncrementally(SolrIndexSearcher searcher, SolrCache<K,V> old) throws IOException {
    if (regenerator==null) return;
    
    //System.out.println("regenerator: " + regenerator);
    
    Map<String, List<String>> fields = getFields(searcher, this.identifierFields);
    if (fields.get("textClasses").size() > 0 || fields.get("textClassesMV").size() > 0) {
  		synchronized (map) {
  			treatIdentifiersAsText  = true;
      }
  	}

    
    long warmingStartTime = System.currentTimeMillis();
    CitationLRUCache<K,V> other = (CitationLRUCache<K,V>)old;

    // collect ids of documents that need to be reloaded/regenerated during this
    // warmup run
    //System.out.println("searcher: " + searcher.toString());
    //System.out.println("maxDoc: " + searcher.getIndexReader().maxDoc());
    FixedBitSet toRefresh = new FixedBitSet(searcher.getIndexReader().maxDoc());
    
    //System.out.println("version=" + searcher.getIndexReader().getVersion());
    //try {
	    //System.out.println("commit=" + searcher.getIndexReader().getIndexCommit());
    //} catch (IOException e2) {
	    // TODO Auto-generated catch block
	    //e2.printStackTrace();
    //}
    
//    for (IndexReaderContext c : searcher.getTopReaderContext().children()) {
//    	//System.out.println("context=" + c.reader().getCombinedCoreAndDeletesKey());
//    }
    
//    for (IndexReaderContext l : searcher.getIndexReader().leaves()) {
//    	//System.out.println(l);
//    }
    
    Bits liveDocs = searcher.getAtomicReader().getLiveDocs();
    //System.out.println(liveDocs == null ? "liveDocs=" + null : "liveDocs=" + liveDocs.length());
    //System.out.println("numDeletes=" + searcher.getAtomicReader().numDeletedDocs());
    
    
    
    if (liveDocs == null) { // everything is new, this could be fresh index or merged/optimized index too
    	
    	//searcher.getAtomicReader().getContext().children().size()
    	
      //other.map.clear(); // force regeneration
      toRefresh.set(0, toRefresh.length());
      
      // Build the mapping from indexed values into lucene ids
      // this must always be available, so we build it no matter what...
      // XXX: make it update only the necessary IDs (not the whole index)
      unInvertedTheDamnThing(searcher.getAtomicReader(), fields, liveDocs, new KVSetter() {
	  		@SuppressWarnings("unchecked")
        @Override
	      public void set (int docbase, int docid, Object value) {
	    		put((K) value, (V) (Integer) (docbase+docid));
	    	}
	  	}
  	);
      
    }
    else if (liveDocs != null) {
    	
    	Integer luceneId;
      for (V v: other.map.values()) {
      	luceneId = ((Integer) v);
      	if (luceneId <= liveDocs.length() && !liveDocs.get(luceneId)) { // doc was either deleted or updated
      		//System.out.println("Found deleted: " + luceneId);
      		// retrieve all citations/references for this luceneId and mark these docs to be refreshed
      	}
      }
      
      for (int i = 0; i < toRefresh.length(); i++) {
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

  

	private Map<String, List<String>> getFields(SolrIndexSearcher searcher, String[] listOfFields) {
  	
  	HashMap<String, List<String>> out = new HashMap<String, List<String>>();
  	out.put("textFields", new ArrayList<String>());
  	out.put("textFieldsMV", new ArrayList<String>());
  	out.put("intFields", new ArrayList<String>());
  	out.put("intFieldsMV", new ArrayList<String>());
  	
  	
    int unknownClasses = 0;
    //boolean foundRequired = false;
    
    IndexSchema schema = searcher.getCore().getLatestSchema();
    
    if (schema.getUniqueKeyField() == null) {
    	throw new SolrException(ErrorCode.FORBIDDEN, "Sorry, your schema is missing unique key and thus you probably have many duplicates. I won't continue");
    }
    
    //String unique = schema.getUniqueKeyField().getName();
    
  	for (String f: listOfFields) {
  		SchemaField fieldInfo = schema.getField(f.replace(":sorted", ""));
  		FieldType type = fieldInfo.getType();
  		
  		//if (fieldInfo.isRequired()) {
  		//	foundRequired = true;
  		//}
  		
  		assert fieldInfo.stored() == true;
  		
  		Class<? extends FieldType> c = type.getClass();
  		if (c.isAssignableFrom(TextField.class) || c.isAssignableFrom(StrField.class)) {
  			if (fieldInfo.multiValued() || type.isMultiValued()) {
  				out.get("textFieldsMV").add(f);
  			}
  			else {
  				out.get("textFields").add(f);
  			}
  		}
  		else if (c.isAssignableFrom(TrieIntField.class) || c.isAssignableFrom(IntField.class)) {
  			if (fieldInfo.multiValued() || type.isMultiValued()) {
  				out.get("intFieldsMV").add(f);
  			}
  			else {
  				out.get("textFields").add(f);
  			}
  		}
  		else {
  			unknownClasses += 1;
  		}
  	}
  	
  	if (unknownClasses > 0 ) {
  		throw new SolrException(ErrorCode.FORBIDDEN, "Cache can be built only from text/numeric fields");
  	}
  	
  	//if (!foundRequired) {
  	//	throw new SolrException(ErrorCode.FORBIDDEN, "At least one of the identifier fields must be type 'required'.");
  	//}
  	
  	return out;
	  
  }

	/*
   * Checks whether the cache needs to be rebuilt for this 
   * document, eg. if the key points to a deleted document
   * or if one of the values point at a deleted document
   */
  private boolean isModified(Bits liveDocs, Object cacheKey, Object cacheValue) {
  	/*
  	if (!liveDocs.get((Integer) get((K)cacheKey))) { // doc is deleted
      return true;
    }
    
    for (Integer luceneId: (Integer[]) cacheValue) {
      if (!liveDocs.get(luceneId) || luceneId == -1) { // some of the linked docs was deleted or unrecognized
        return true;
      }
    }
    */
    return false;
  }

  public void close() {
  }
  
  
  
  
  private class KVSetter {
    public void set (int docbase, int docid, Object value) {
  		throw new NotImplementedException();
  	}
  }
  /*
   * Given the set of fields, we'll look inside them and retrieve (into memory)
   * all values
   */
  private void unInvertedTheDamnThing(AtomicReader reader, Map<String, 
  		List<String>> fields, Bits liveDocs, KVSetter setter) throws IOException {
  	
  	if (liveDocs == null) {
  		liveDocs = reader.getLiveDocs();
  	}
  	
  	int docBase = reader.getContext().docBase;
  	//System.out.println("***REBUILDING***");
  	//System.out.println("Generating mapping from: " + reader.toString() + " docBase=" + docBase);
  	
  	// load multiple values->idlucene mapping
  	for (String idField: fields.get("intFieldsMV")) {
			DocTermOrds unInvertedIndex = new DocTermOrds(reader, liveDocs, idField);
			TermsEnum termsEnum = unInvertedIndex.getOrdTermsEnum(reader);
			if (termsEnum == null) {
				continue;
			}
			DocsEnum docs = null;
			for (;;) {
				BytesRef term = termsEnum.next();
				if (term == null)
					break;
				
				Integer t = FieldCache.DEFAULT_INT_PARSER.parseInt(term);
				
				docs = termsEnum.docs(liveDocs, docs, 0); // we don't need docFreq
				int i = 0;
				for (;;) {
					int d = docs.nextDoc();
					if (d == DocIdSetIterator.NO_MORE_DOCS) {
						break;
					}
					
					setter.set(docBase, d, treatIdentifiersAsText ? Integer.toString(t) : t);
					
					i += 1;
					//if (i > 1) {
					//	log.warn("The term {} is used by more than one document {} ; your cache has problems", t, d+docBase);
					//}
				}
			}
		}
  	
  	/*
  	 * Read every term
  	 *    - for each term get all live documents
  	 *    	- and do something with the pair: (docid, term)
  	 */
		for (String idField: fields.get("textFieldsMV")) {
			DocTermOrds unInvertedIndex = new DocTermOrds(reader, liveDocs, idField);
			TermsEnum termsEnum = unInvertedIndex.getOrdTermsEnum(reader);
			if (termsEnum == null) {
				continue;
			}
			DocsEnum docs = null;
			for (;;) {
				BytesRef term = termsEnum.next();
				if (term == null)
					break;
				String t = term.utf8ToString();
				
				docs = termsEnum.docs(liveDocs, docs, 0); // we don't need docFreq
				for (;;) {
					int d = docs.nextDoc();
					if (d == DocIdSetIterator.NO_MORE_DOCS) {
						break;
					}
					
					setter.set(docBase, d, t);
					
					//if (i > 1) {
					//	log.warn("The term {} is used by more than one document {} ; your cache has problems", t, d+docBase);
					//}
				}
			}
		}
		
		
  	// load single valued ids 
		for (String idField: fields.get("textFields")) {
			BinaryDocValues idMapping = getCacheReuseExisting(reader, idField);
			
			Integer i = 0;
			BytesRef ret = new BytesRef();
			while(i < reader.maxDoc()) {
				if (liveDocs != null && !(i < liveDocs.length() && liveDocs.get(i))) {
					//System.out.println("skipping: " + i);
					i++;
					continue;
				}
			  ret = idMapping.get(i);
			  if (ret.length > 0) {
			    setter.set(docBase, i, ret.utf8ToString()); // in this case, docbase will always be 0
			  }
				i++;
			}
			if (purgeCache)
				FieldCache.DEFAULT.purgeByCacheKey(reader.getCoreCacheKey());
		}
		for (String idField: fields.get("intFields")) {
			Ints idMapping = FieldCache.DEFAULT.getInts(reader, idField, false);
			Integer i = 0;
			while(i < reader.maxDoc()) {
				if (liveDocs != null && !(i < liveDocs.length() && liveDocs.get(i))) {
					//System.out.println("skipping: " + i);
					i++;
					continue;
				}
				setter.set(docBase, i, treatIdentifiersAsText ? Integer.toString(idMapping.get(i)) : idMapping.get(i));
				i++;
			}
		}

	}

  private BinaryDocValues getCacheReuseExisting(AtomicReader reader, String idField) throws IOException {
  	
  	purgeCache = false;
  	
  	Boolean sorted = false;
  	if (idField.indexOf(':') > -1) {
  		String[] parts = idField.split(":");
  		idField = parts[0];
  		if (parts[1].indexOf("sort") > -1)
  			sorted = true;
  	}
  	
  	// first try discover if there exists a cache already that we can reuse
  	// be careful, the cache needs to be populated properly - ie. when a new
  	// searcher is opened, the warming query should generate these caches;
  	// otherwise it could happen we grab the old searcher's cache
  	if (reuseCache) {
	  	CacheEntry[] caches = FieldCache.DEFAULT.getCacheEntries();
			if (caches != null) {
				ArrayList<CacheEntry> potentialCandidates = new ArrayList<CacheEntry>();
				
				for (int i=0; i < caches.length; i++) {
					
					CacheEntry c = caches[i];
					String key = c.getFieldName();
					String readerKey = c.getReaderKey().toString();
					String segmentCode = readerKey.substring(readerKey.indexOf("(")+1, readerKey.length()-1);
					
					if (idField.equals(key) && 
							(reader.getCoreCacheKey().toString().contains(segmentCode)
									|| readerKey.contains("SegmentCoreReaders"))) {
						if (sorted) {
							if (c.getValue() instanceof SortedDocValues)	potentialCandidates.add(c);
						}
						else {
							potentialCandidates.add(c);
						}
					}
					
				}
				
				if (potentialCandidates.size() == 0) {
					// pass
				}
				else if (potentialCandidates.size() == 1) {
					CacheEntry ce = potentialCandidates.get(0);
					Object v = ce.getValue();
					if (v instanceof BinaryDocValues) {
						return (BinaryDocValues) v;
					}
				}
				else {
					log.warn("We cannot unambiguously identify cache entry for: {}, {}", idField, reader.getCoreCacheKey());
				}
				
			}
  	}
		
		BinaryDocValues idMapping;
		purgeCache = true;
		
		// because sorting components will create the cache anyway; we can avoid duplicating data
		// if we create a cache duplicate, the tests will complain about cache insanity (and rightly so)
		if (sorted) {
			idMapping = FieldCache.DEFAULT.getTermsIndex(reader, idField);
			//System.out.println("creating new sorted: " + idField);
			//System.out.println("created: " + idMapping);
		}
		else {
		  idMapping = FieldCache.DEFAULT.getTerms(reader, idField, false); // XXX:rca - should we use 'true'?
		  //System.out.println("creating new: " + idField);
		  //System.out.println("created: " + idMapping);
		}
		return idMapping;
		
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

  @SuppressWarnings({ "rawtypes", "unchecked" })
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
  
  @Override
  public int hashCode() {
  	return referenceFields.hashCode() ^ identifierFields.hashCode() ^ sourceReaderHashCode;
  }
  
  public String identifierString() {
  	StringBuffer out = new StringBuffer();
  	out.append("CitationLRUCache(");
  	out.append("idfields:");
  	out.append(Arrays.toString(identifierFields));
  	if (referenceFields.length > 0) {
  		out.append(", valfields:");
  		out.append(Arrays.toString(referenceFields));
  	}
  	out.append(")");
  	return out.toString();
  }
  
  public static class SimpleRegenerator implements CacheRegenerator {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean regenerateItem(SolrIndexSearcher newSearcher,
                                  SolrCache newCache,
                                  SolrCache oldCache,
                                  Object oldKey,
                                  Object oldVal)
      throws IOException {

      newCache.put(oldKey,oldVal);
      return true;
    }
  };
  
  /**
   * Efficient resizable auto-expanding list holding <code>int</code> elements;
   * implemented with arrays.
   */
  private static final class ArrayIntList {

    private int[] elements;
    private int size = 0;
      
    public ArrayIntList(int initialCapacity) {
      elements = new int[initialCapacity];
    }

    public void add(int elem) {
      if (size == elements.length) ensureCapacity(size + 1);
      elements[size++] = elem;
    }

    public int[] getElements() {
    	int[] out = new int[size];
    	System.arraycopy(elements, 0, out, 0, size);
    	return out;
    }
    
    public int get(int index) {
      if (index >= size) throwIndex(index);
      return elements[index];
    }
    
    public int size() {
      return size;
    }
    
    private void ensureCapacity(int minCapacity) {
      int newCapacity = Math.max(minCapacity, (elements.length * 3) / 2 + 1);
      int[] newElements = new int[newCapacity];
      System.arraycopy(elements, 0, newElements, 0, size);
      elements = newElements;
    }

    private void throwIndex(int index) {
      throw new IndexOutOfBoundsException("index: " + index
            + ", size: " + size);
    }
    
    public String toString() {
    	return Arrays.toString(elements);
    }
    
    /** returns the first few positions (without offsets); debug only */
    @SuppressWarnings("unused")
    public String toString(int stride) {
      int s = size() / stride;
      int len = Math.min(10, s); // avoid printing huge lists
      StringBuilder buf = new StringBuilder(4*len);
      buf.append("[");
      for (int i = 0; i < len; i++) {
        buf.append(get(i*stride));
        if (i < len-1) buf.append(", ");
      }
      if (len != s) buf.append(", ..."); // and some more...
      buf.append("]");
      return buf.toString();
    }   
  }
  

  
  /*
   * Until I implement dynamic loading of data, this cache 
   * will always grow to the maxdoc size, so that no 
   * evictions happen
   */
	@SuppressWarnings("hiding")
  public class RelationshipLinkedHashMap<K,V> extends LinkedHashMap<K,V> {
    private static final long serialVersionUID = -356203002886265188L;
		int slimit;
		List<ArrayIntList> references;
		List<ArrayIntList> citations;
  	
		public RelationshipLinkedHashMap (int initialSize, float ratio, boolean accessOrder, 
				int limit, Float sizeInPercent) {
			super(initialSize, ratio, accessOrder);
			slimit = limit;
			references = new ArrayList<ArrayIntList>(0); // just to prevent NPE - normally, is
    	citations = new ArrayList<ArrayIntList>(0);  // initialized in initializeCitationCache 
		}
		
		
    @SuppressWarnings("rawtypes")
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
    	return false;
    	/*
      if (size() > slimit) {
        // increment evictions regardless of state.
        // this doesn't need to be synchronized because it will
        // only be called in the context of a higher level synchronized block.
      	evictions++;
      	stats.evictions.incrementAndGet();
        return true;
      }
      return false;
      */
    }
    
    
    public int[] getReferences(int docid) {
    	if (docid < references.size() && references.get(docid) != null) {
    		ArrayIntList c = references.get(docid);
    		if (c != null)
    			return c.getElements();
    	}
    	return null;
    }
    
    
    public Iterator<int[][]> getRelationshipsIterator() {
    	return new CitationDataIterator();
    }
    
    public int relationshipsDataSize() {
    	return citations.size();
    }
    
    public int[] getCitations(int docid) {
    	if (docid < citations.size() && citations.get(docid) != null) {
    		ArrayIntList c = citations.get(docid);
    		if (c != null)
    			return c.getElements();
    	}
    	return null;
    }
    
    public void initializeCitationCache(int maxDocSize) {
    	references = new ArrayList<ArrayIntList>(maxDocSize);
    	citations = new ArrayList<ArrayIntList>(maxDocSize);
    	
    	// i was hoping thi sis not necessary, but set(index, value)
    	// throws errors otherwise
    	for (int i=0;i<maxDocSize;i++) {
    		references.add(null);
    		citations.add(null);
    	}
    	
    }
    
    public void addReference(int sourceDocid, Object value) {
    	//System.out.println("addReference(" + sourceDocid + ", " + value + ")");
    	if (this.containsKey(value)) {
    		addReference(sourceDocid, (Integer) this.get(value));
    	}
    	else {
    		addReference(sourceDocid, -1);
    	}
    }
    public void addReference(int sourceDocid, Integer targetDocid) {
    	_add(references, sourceDocid, targetDocid);
    }

    public void addCitation(int sourceDocid, Object value) {
    	//System.out.println("addCitation(" + sourceDocid + ", " + value + ")");
    	if (this.containsKey(value)) {
    		addCitation(sourceDocid, (Integer) this.get(value));
    	}
    	else {
    		addCitation(sourceDocid, -1);
    	}
    }
    
    public void addCitation(int sourceDocid, Integer targetDocid) {
    	//System.out.println("addCitation(" + sourceDocid + "," + targetDocid+")");
    	_add(citations, sourceDocid, targetDocid);
    }
        
    private void _add(List<ArrayIntList> target, int sourceDocid, int targetDocid) {
    	
    	//System.out.println("_add(" + sourceDocid + "," + targetDocid+")");
    	
    	if (target.get(sourceDocid) == null) {
    		ArrayIntList pointer = new ArrayIntList(1);
    		pointer.add(targetDocid);
    		target.set(sourceDocid, pointer);
    	}
    	else {
    		target.get(sourceDocid).add(targetDocid);
    	}
    }
    
    public void inferCitationsFromReferences() {
    	int i = -1;
    	for (ArrayIntList refs : references) {
    		i += 1;
    		if (refs == null) {
    			continue;
    		}
    		for (int j=0; j<refs.size();j++) {
    			if (refs.get(j) == -1)
    				continue;
    			addCitation(refs.get(j),i);
    		}
    	}
    }
    
    public void inferReferencesFromCitations() {
    	int i = -1;
    	for (ArrayIntList refs : citations) {
    		i += 1;
    		if (refs == null) {
    			continue;
    		}
    		for (int j=0; j<refs.size();j++) {
    			if (refs.get(j) == -1)
    				continue;
    			addReference(refs.get(j),i);
    		}
    	}
    }
    
    private class CitationDataIterator implements Iterator<int[][]> {
	    int cursor = 0;       // index of next element to return
	    
	    public boolean hasNext() {
        return cursor != citations.size();
	    }
	    
	    public int[][] next() {
	        int i = cursor;
	        if (i >= citations.size())
	            throw new NoSuchElementException();
	        int[][] out = new int[2][];
	        
	        ArrayIntList v1 = references.get(cursor);
	        ArrayIntList v2 = citations.get(cursor);
	        
	        out[0] = v1 != null ? v1.getElements() : new int[0];
	        out[1] = v2 != null ? v2.getElements() : new int[0];
	        
	        cursor = i + 1;
	        return out;
	    }
	
	    public void remove() {
          throw new UnsupportedOperationException();
	    }
	
    }
  };
}
