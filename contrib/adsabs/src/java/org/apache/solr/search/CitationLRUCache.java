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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.NotImplementedException;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.index.SortedNumericDocValues;
import org.apache.lucene.index.SortedSetDocValues;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.FixedBitSet;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.batch.BatchHandlerRequestQueue;
import org.apache.solr.handler.batch.BatchProviderDumpCitationCache;
import org.apache.solr.handler.batch.BatchProviderI;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.StrField;
import org.apache.solr.schema.TextField;
import org.apache.solr.schema.TrieIntField;
import org.apache.solr.uninverting.UninvertingReader;
import org.apache.solr.uninverting.UninvertingReader.Type;
import org.apache.solr.util.RefCounted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
 * network in memory. However, the initial mapping (value&lt;-&gt;lucene id)
 * will always be constructed in its entirety.
 * 
 */
public class CitationLRUCache<K,V> extends SolrCacheBase implements CitationCache<K,V> {
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  
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
  private String description="Citation LRU Cache";

  // the main objects
  private LinkedHashMap<K,V> relationships;
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

	private boolean dumpCache = false;
	


  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Object init(Map args, Object persistence, CacheRegenerator regenerator) {
    super.init(args, regenerator);
    
  	identifierFields  = ((String)args.get("identifierFields")).split(",");
    assert (identifierFields != null && identifierFields.length > 0);
    
    
    
    incremental  = "true".equals(((String)args.get("incremental")));
    reuseCache  = "true".equals(((String)args.get("reuseCache")));
    dumpCache  = "true".equals(((String)args.get("dumpCache")));
    
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

    relationships = new RelationshipLinkedHashMap<K,V>(initialSize, 0.75f, true, 
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
    synchronized(relationships) {
      return relationships.size();
    }
  }

  public boolean treatsIdentifiersAsText() {
  	return treatIdentifiersAsText;
  }
  
  public V put(K key, V value) {
  	//System.out.println("put(" + key + "," + value+")");
    synchronized (relationships) {
      if (getState() == State.LIVE) {
        stats.inserts.incrementAndGet();
      }

      // increment local inserts regardless of state???
      // it does make it more consistent with the current size...
      inserts++;
      return relationships.put(key,value);
    }
  }

  public V get(K key) {
    synchronized (relationships) {
      V val = relationships.get(key);
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
  public  Iterator<int[][]> getCitationGraph() {
  	return ((RelationshipLinkedHashMap<K,V>) relationships).getRelationshipsIterator();
  }
  
  public int getCitationsIteratorSize() {
  	synchronized (relationships) {
  		return ((RelationshipLinkedHashMap<K,V>) relationships).relationshipsDataSize();
  	}
  }
  
  public int[] getCitations(K key) {
  	synchronized (relationships) {
  		V val = relationships.get(key);
  		if (val==null)
  			return null;
  		
      RelationshipLinkedHashMap<K,V> relMap = (RelationshipLinkedHashMap<K,V>) relationships;
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
    synchronized (relationships) {
      RelationshipLinkedHashMap<K,V> relMap = (RelationshipLinkedHashMap<K,V>) relationships;
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
  	synchronized (relationships) {
  		V val = relationships.get(key);
  		if (val==null)
  			return null;
  		
      RelationshipLinkedHashMap<K,V> relMap = (RelationshipLinkedHashMap<K,V>) relationships;
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
    synchronized (relationships) {
      RelationshipLinkedHashMap<K,V> relMap = (RelationshipLinkedHashMap<K,V>) relationships;
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
    synchronized(relationships) {
      relationships.clear();
    }
  }
  
  private boolean isWarming = false;

	private boolean purgeCache;
  public boolean isWarmingOrWarmed() {
  	return isWarming;
  }
  
  public void warm(SolrIndexSearcher searcher, SolrCache<K,V> old) {
    
    long warmingStartTime = System.nanoTime();
    if (isAutowarmingOn()) {
    	isWarming = true;
    	try {
    		log.info("Warming cache (" + name() + "): " + searcher);
  	  	if (this.incremental ) {
  	  		warmIncrementally(searcher, old);
  	  	}
  	  	else {
  	      warmRebuildEverything(searcher, old);
  	  	}
  	  	log.info("Warming cache done (# entries:" + relationships.size() + "): " + searcher);
    	} 
    	catch (IOException e) {
      	throw new SolrException(ErrorCode.SERVER_ERROR, "Failed to generate initial IDMapping", e);
      }
    	sourceReaderHashCode = searcher.hashCode();
    }
    
    if (dumpCache) {
    	try {
    		persistCitationCache(searcher);
    	}
    	catch (IOException e) {
    		throw new SolrException(ErrorCode.SERVER_ERROR, "Failed to generate initial IDMapping", e);
    	}
    }
    
    warmupTime = TimeUnit.MILLISECONDS.convert(System.nanoTime() - warmingStartTime, TimeUnit.NANOSECONDS);
  }
  
	private void persistCitationCache(SolrIndexSearcher searcher) throws IOException {
		
		
		long generation = -1;
		try {
			generation = searcher.getIndexReader().getIndexCommit().getGeneration();
		} catch (IOException e) {
			throw new IOException("Cannot obtain generation and cannot dump the citation cache", e);
		}

		Directory dir = searcher.getIndexReader().directory();
		IndexOutput out = null;
		
		try {
			out = dir.createOutput("citation_cache", new IOContext());
			Iterator<int[][]> it = this.getCitationGraph();
			while (it.hasNext()) {
			      int[][] data = it.next();
			      int[] references = data[0];
				  	if (references != null && references.length > 0) {
				  		for (int luceneDocId: references) {
				  		  if (luceneDocId == -1)
				  		    continue;
				  		  out.writeInt(luceneDocId);
				  		  for (int ref: references) {
				  			  out.writeInt(ref);
				  		  }
				  		  out.writeString("\n");
				  	}
				  }
			  }
			
		} 
		finally {
			if (out != null)
				out.close();
		}
	}
  

private void warmRebuildEverything(SolrIndexSearcher searcher, SolrCache<K,V> old) throws IOException {
  	
  	List<String> fields = getFields(searcher, this.identifierFields);
  	
  	//if (this.referenceFields.length == 0 && this.citationFields.length == 0) {
  	//  return;
  	//}

  	// builds the mapping from document ID's to lucene docids
  	unInvertedTheDamnThing(searcher, fields,
  			new KVSetter() {
	  		@Override
	  		@SuppressWarnings({ "unchecked" })
	      public void set (int docbase, int docid, Object value) {
	  		  if (treatIdentifiersAsText && value instanceof Integer) {
            value = Integer.toString((Integer) value);
	  		  }
	    		put((K) value, (V) (Integer) (docbase+docid));
	    	}
	  	}
  	);
  	
  	
  	if (this.referenceFields.length > 0 || this.citationFields.length > 0) {
	  	@SuppressWarnings("rawtypes")
      final RelationshipLinkedHashMap relMap = (RelationshipLinkedHashMap) relationships;
	  	relMap.initializeCitationCache(searcher.maxDoc()); // TODO: touch only updated fields
	  	
	  	
	  	unInvertedTheDamnThing(searcher, getFields(searcher, this.referenceFields), 
	  			new KVSetter() {
			  		@Override
			      public void set (int docbase, int docid, Object value) {
			  			synchronized (relMap) {
			  				relMap.addReference(docbase+docid, value);
	            }
			    	}
			  	}
	  	);
	  	
	  	unInvertedTheDamnThing(searcher, getFields(searcher, this.citationFields),
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
    
    
    List<String> fields = getFields(searcher, this.identifierFields);
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
    
    Bits liveDocs = searcher.getSlowAtomicReader().getLiveDocs();
    //System.out.println(liveDocs == null ? "liveDocs=" + null : "liveDocs=" + liveDocs.length());
    //System.out.println("numDeletes=" + searcher.getAtomicReader().numDeletedDocs());
    
    
    
    if (liveDocs == null) { // everything is new, this could be fresh index or merged/optimized index too
    	
    	//searcher.getAtomicReader().getContext().children().size()
    	
      //other.map.clear(); // force regeneration
      toRefresh.set(0, toRefresh.length());
      
      // Build the mapping from indexed values into lucene ids
      // this must always be available, so we build it no matter what...
      // XXX: make it update only the necessary IDs (not the whole index)
      unInvertedTheDamnThing(searcher, fields, new KVSetter() {
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
      for (V v: other.relationships.values()) {
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
      synchronized (other.relationships) {
        
        int sz = autowarm.getWarmCount(other.relationships.size());
        
        keys = new Object[sz];
        vals = new Object[sz];

        Iterator<Map.Entry<K, V>> iter = other.relationships.entrySet().iterator();

        // iteration goes from oldest (least recently used) to most recently used,
        // so we need to skip over the oldest entries.
        int skip = other.relationships.size() - sz;
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
  }

  

	private List<String> getFields(SolrIndexSearcher searcher, String[] listOfFields) {
  	
	  List<String> out = new ArrayList<String>();
    
    IndexSchema schema = searcher.getCore().getLatestSchema();
    if (schema.getUniqueKeyField() == null) {
    	throw new SolrException(ErrorCode.FORBIDDEN, "Sorry, your schema is missing unique key and thus you probably have many duplicates. I won't continue");
    }
    
  	for (String f: listOfFields) {
  	  String fName = f.replace(":sorted", "");
  		SchemaField fieldInfo = schema.getField(fName);
  		FieldType type = fieldInfo.getType();
  		
  		if (type.getNumericType() != null) {
        synchronized (relationships) {
          treatIdentifiersAsText  = true;
        }
      }
  		
  		if (!fieldInfo.stored() && type.getDocValuesFormat().equals(DocValuesType.NONE)) {
  		  throw new SolrException(ErrorCode.FORBIDDEN, "The field " + f + " cannot be used to build citation cache!");
  		}
  		out.add(fName);
  	}
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
  
  
  /* 
   * Reads values from the DocValue and/or FieldCache and calls the 
   * setter
   */
  private class Transformer {
    public void process(int docBase, int docid) {
      throw new NotImplementedException();
    }
  }
  private class KVSetter {
    @SuppressWarnings({ "unchecked" })
    public void set (int docbase, int docid, Object value) {
  		throw new NotImplementedException();
  	}
  }
  /*
   * Given the set of fields, we'll look inside them and retrieve (into memory)
   * all values
   */
  private void unInvertedTheDamnThing(
      SolrIndexSearcher searcher, 
      List<String> fields, 
      KVSetter setter) throws IOException {
  	
  	IndexSchema schema = searcher.getCore().getLatestSchema();
  	List<LeafReaderContext> leaves = searcher.getIndexReader().getContext().leaves();
  
  	Bits liveDocs;
  	LeafReader lr;
  	Transformer transformer;
    for (LeafReaderContext leave: leaves) {
  	  int docBase = leave.docBase;
  	  liveDocs = leave.reader().getLiveDocs();
  	  lr = leave.reader();
  	  FieldInfos fInfo = lr.getFieldInfos();
  	  for (String field: fields) {

  	    FieldInfo fi = fInfo.fieldInfo(field);
  	    
  	    if (fi == null) {
  	      log.error("Field " + field + " has no schema entry; skipping it!");
  	      continue;
  	    }
  	    
  	    SchemaField fSchema = schema.getField(field);
  	    DocValuesType fType = fi.getDocValuesType();
  	    Map<String,Type> mapping = new HashMap<String,Type>();
  	    final LeafReader unReader;
  	    
  	    if (fType.equals(DocValuesType.NONE)) {
  	      Class<? extends DocValuesType> c = fType.getClass();
          if (c.isAssignableFrom(TextField.class) || c.isAssignableFrom(StrField.class)) {
            if (fSchema.multiValued()) {
              mapping.put(field, Type.SORTED);
            }
            else {
              mapping.put(field, Type.BINARY);
            }
          }
          else if (c.isAssignableFrom(TrieIntField.class)) {
            if (fSchema.multiValued()) {
              mapping.put(field, Type.SORTED_SET_INTEGER);
            }
            else {
              mapping.put(field, Type.INTEGER_POINT);
            }
          }
          else {
            continue;
          }
          unReader = new UninvertingReader(lr, mapping);
  	    }
  	    else {
  	      unReader = lr;
  	    }
        
        switch(fType) {
  	      case NUMERIC:
  	        transformer = new Transformer() {
  	          NumericDocValues dv = unReader.getNumericDocValues(field);
  	          @Override
              public void process(int docBase, int docId) {
                int v = (int) dv.get(docId);
                setter.set(docBase, docId, v);
              }
  	        };
  	        break;
  	      case SORTED_NUMERIC:
  	        transformer = new Transformer() {
              SortedNumericDocValues dv = unReader.getSortedNumericDocValues(field);
              @Override
              public void process(int docBase, int docId) {
                dv.setDocument(docId);
                int max = dv.count();
                int v;
                for (int i=0; i<max; i++) {
                  v = (int) dv.valueAt(i);
                  setter.set(docBase, docId, v);
                }
              }
            };
  	        break;
  	      case SORTED_SET:
  	        transformer = new Transformer() {
              SortedSetDocValues dv = unReader.getSortedSetDocValues(field);
              int errs = 0;
              @Override
              public void process(int docBase, int docId) {
                if (errs > 5)
                  return;
                dv.setDocument(docId);
                for (long ord = dv.nextOrd(); ord != SortedSetDocValues.NO_MORE_ORDS; ord = dv.nextOrd()) {
                  final BytesRef value = dv.lookupOrd(ord);
                  setter.set(docBase, docId, value.utf8ToString().toLowerCase()); // XXX: even if we apply tokenization, doc values ignore it
                }
              }
            };
  	        break;
  	      case SORTED:
  	        transformer = new Transformer() {
  	          SortedDocValues dv = unReader.getSortedDocValues(field);
              TermsEnum te;
              @Override
              public void process(int docBase, int docId) {
                BytesRef v = dv.get(docId);
                if (v.length == 0)
                  return;
                setter.set(docBase, docId, v.utf8ToString().toLowerCase());
              }
            };
  	        break;
  	      default:
  	        throw new IllegalArgumentException("The field " + field + " is of type that cannot be un-inverted");
  	    }
  	    
  	    int i = 0;
        while(i < lr.maxDoc()) {
          if (liveDocs != null && !(i < liveDocs.length() && liveDocs.get(i))) {
            i++;
            continue;
          }
          transformer.process(docBase, i);
          i++;
        }
  	  }
  	  
  	}
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
    synchronized (relationships) {
      lst.add("lookups", lookups);
      lst.add("hits", hits);
      lst.add("hitratio", calcHitRatio(lookups,hits));
      lst.add("inserts", inserts);
      lst.add("evictions", evictions);
      lst.add("size", relationships.size());
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
   * The main datastructure holding information about the lucene documents.
   * 
   * For speed purposes, the data gets loaded into RAM; we have those pieces
   * 
   *  - mapping: key -> lucene docid
   *  - references: docid -> many other docids
   *  - citations: docid -> many other docids
   *  
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
    	  log.debug("Would like to add reference " + value + " but cannot map it to the lucene id");
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
    		log.debug("Would like to add citation " + value + " but cannot map it to the lucene id");
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
