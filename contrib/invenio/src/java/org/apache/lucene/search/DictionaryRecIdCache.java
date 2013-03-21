package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.DocTermOrds;
import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldCache.DocTerms;
import org.apache.lucene.search.FieldCache.IntParser;
import org.apache.lucene.search.FieldCacheImpl.Entry;
import org.apache.lucene.search.FieldCacheImpl.StopFillCacheException;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;

/**
 * This class helps to translate from external source ids to lucene ids.
 * It is assumed, that you index the field (the field is unique for each
 * documement). And the external source is returned the values of the 
 * field in the place of lucene docids. 
 * 
 * So, for example, Invenio has records with recids: 0...5,8,10
 * 
 * You are indexing these records with Lucene, and they get assigned these
 * lucene docids: 0..7 (0=0,...,10=7)
 * 
 * The search returned recids: 2,10 --> they will be translated automatically
 * into lucene docids 2,7
 * 
 * Wheneven you remove/update/delete document, we'll automatically update the
 * recid Cache.
 * 
 * @author rchyla
 *
 */
public enum DictionaryRecIdCache {
	INSTANCE;
	//Map<String, Map<Integer, int[]>> cache = null;
	
	private HashMap<String, Map<Integer, int[]>> 
		cache = new HashMap<String, Map<Integer, int[]>>(4);
	
	private HashMap<String, Object>
		translation_cache = new HashMap<String, Object>(2);
	
	private HashMap<String, Integer>
		translation_cache_tracker = new HashMap<String, Integer>(2);
		
	public void setCache(String name, Map<Integer, int[]> value) {
		synchronized(cache) {
			cache.put(name, value);
		}
	}
	
	public Map<Integer, int[]> getCache(String name) {
		return cache.get(name);
	}
	
	public int[] getLuceneCache(AtomicReader reader, String field) throws IOException {
		return FieldCache.DEFAULT.getInts(reader, field, false);
	}
	
	public Map<Integer, Integer> buildCache(int[] idMapping) throws IOException {
		
		Map<Integer, Integer> fromFieldToLuceneId = new HashMap<Integer, Integer>(idMapping.length);
		int i = 0;
		for (int value: idMapping) {
			fromFieldToLuceneId.put(value, i);
			i++;
		}
		if (fromFieldToLuceneId.containsKey(null))	fromFieldToLuceneId.remove(null);
		return fromFieldToLuceneId;
	}
	
	public Map<String, Integer> buildCacheStr(DocTerms idMapping) throws IOException {
		
		Map<String, Integer> fromFieldToLuceneId = new HashMap<String, Integer>(idMapping.size());
		int i = 0;
		BytesRef ret = new BytesRef();
		while(i < idMapping.size()) {
		  ret = idMapping.getTerm(i, ret);
		  if (ret.length > 0) {
		    fromFieldToLuceneId.put(ret.utf8ToString(), i);
		  }
			i++;
		}
		if (fromFieldToLuceneId.containsKey(null))	fromFieldToLuceneId.remove(null);
		return fromFieldToLuceneId;
	}
	
	
	private boolean indexUnchanged(AtomicReader reader, String field, Boolean isString) throws IOException {
		// first check that the index wasn't updated
		Integer old_hash = null;
		if (translation_cache_tracker.containsKey(field)) {
			old_hash = translation_cache_tracker.get(field);
		}
		else {
			return false;
		}
		if (isString) {
			return old_hash.equals(FieldCache.DEFAULT.getTerms(reader, field).hashCode());
		}
		else {
			return old_hash.equals(FieldCache.DEFAULT.getInts(reader, field, false).hashCode());
		}
	}
	
	
	/*
	 * TODO: several threads may be trying to get the data at once, there will be no clash,
	 * but likely useless computation. I should add some lock to each of the main methods
	 * (that gets separate cache type) 
	 */
	
	/**
	 * Provides the mapping <b>from</b> the external source ids <b>into</b>
	 * lucene doc ids. This class is thread safe.
	 * 
	 * The externalIdsField must contain integers
	 * 
	 * The cache is always checked for the updates before the mapping is 
	 * returned, so you are sure to obtain the latest stage at the moment
	 * when you ask for it.
	 * 
	 * @param reader
	 * @param field
	 * @return
	 * @throws IOException
	 */
	public Map<Integer, Integer> getTranslationCache(IndexReader reader, String externalIdsField) throws IOException {
		AtomicReader atomReader = getAtomicReader(reader);
		
		if (!(translation_cache_tracker.containsKey(externalIdsField) && indexUnchanged(atomReader, externalIdsField, false))) {
			synchronized(translation_cache_tracker) {
				int[] idMapping = FieldCache.DEFAULT.getInts(atomReader, externalIdsField, false);
				Map<Integer, Integer> translTable = buildCache(idMapping);
				translation_cache.put(externalIdsField, translTable);
				translation_cache_tracker.put(externalIdsField, idMapping.hashCode());
				return translTable;
			}
		}
		return (Map<Integer, Integer>) translation_cache.get(externalIdsField);
	}
	
	
	/**
	 * Provides mapping from the string value (stored in refField) onto the 
	 * lucene ids via idField. 
	 * 
	 * The externalIdsField must contain unique value! I.e. bibcode
	 * 
	 * I.e. refField=bibcode
	 * bibcode contains unique value XYZ and this value (XYZ) 
	 * corresponds to the lucene-id=5
	 * 
	 * WARNING: the keys come out processed! Ie. usually lowercased
	 * 
	 * @param reader
	 * @param externalIdsField
	 * @return
	 * @throws IOException
	 */
	public Map<String, Integer> getTranslationCacheString(IndexReader reader, String externalIdsField) throws IOException {
		
	  AtomicReader atomReader = getAtomicReader(reader);
	  
		if (!(translation_cache_tracker.containsKey(externalIdsField) && indexUnchanged(atomReader, externalIdsField, true))) {
			synchronized(translation_cache_tracker) {
				DocTerms idMapping = FieldCache.DEFAULT.getTerms(atomReader, externalIdsField);
				Map<String, Integer> translTable = buildCacheStr(idMapping);
				translation_cache.put(externalIdsField, translTable);
				translation_cache_tracker.put(externalIdsField, idMapping.hashCode());
				return translTable;
			}
		}
		return (Map<String, Integer>) translation_cache.get(externalIdsField);
	}
	
	
	
	/**
	 * This cache alleviates the need to read the data from the index, we
	 * basically load the field into memory (but because lucene-3 cache
	 * cannot yet load the multiple values, we do it here manually, and we 
	 * translate the values into docids as we proceed, just to be superfast
	 * on the search side)
	 */
	private HashMap<String, Object>multiValuesCache = new HashMap<String, Object >(2);
	
	public Map<Integer, List<Integer>> getCacheTranslatedMultiValuesString(IndexReader reader, String externalIds, String refField) 
		throws IOException {
	  
	  AtomicReader atomReader = getAtomicReader(reader);
		
		if (multiValuesCache.containsKey(refField) && indexUnchanged(atomReader, externalIds, true)) {
			return (Map<Integer, List<Integer>>) multiValuesCache.get(refField);
		}
		
		// since most likely this will be needed anyway, i don't bother with reading just the values from the index
		int[][] invCache = getUnInvertedDocidsStrField(atomReader, externalIds, refField);
		
		HashMap<Integer, List<Integer>> docsPointingToDocId = new HashMap<Integer, List<Integer>>();
		Integer i = -1;
		for (int[] docIds: invCache) {
			i++;
			if (docIds == null) continue;
			for (int docId: docIds) {
				if (!docsPointingToDocId.containsKey(docId)) {
					docsPointingToDocId.put(docId, new ArrayList<Integer>());
				}
				docsPointingToDocId.get(docId).add(i);
			}
			
		}
		
		synchronized(multiValuesCache) {
			multiValuesCache.put(refField, docsPointingToDocId);
		}
		return docsPointingToDocId;
		
	}

	/**
	 * Uninverts the lucene index, it grabs all the values from the index and discovers what 
	 * documents they are used in. This can be used for references, citations etc. Example:
	 * 
	 * Documents:
	 *    id=4, recid=90, references=91, references=92
	 *    id=5, recid=91, references=92
	 *    id=6, recid=92, references=999
	 *    
	 * Will result in an array of size x (maxdoc)
	 * 
	 *   [[...],
	 *    ...,
	 *    [5, 6],  <-- position #4
	 *    [6],
	 *    [],      <-- position #6, 999 could not be resolved to a valid lucene docid
	 *    ....]
	 *   
	 *   
	 *  TODO: in Solr4.0 there is a much better solution, see
	 *  https://issues.apache.org/jira/browse/LUCENE-3354
	 * 
	 * 
	 * @param reader
	 *    - top-leve index reader
	 * @param externalIds
	 * 	  - name of the index that contains the external system ids (eg. recids)
	 * @param refField
	 *    - field to uninvert (we expect multiple values)

	 * @return
	 *    
	 * @throws IOException
	 */
	
	private HashMap<String, Object>invertedCache = new HashMap<String, Object >(2);
	
	public int[][] getUnInvertedDocids(IndexReader reader, String externalIds, String refField) throws IOException {
		
		AtomicReader atomReader = getAtomicReader(reader);
		
		if (invertedCache.containsKey(refField) && indexUnchanged(atomReader, externalIds, false)) {
			return (int[][]) invertedCache.get(refField);
		}
		
		final Map<?, Integer> idMapping = getTranslationCache(reader, externalIds);
		
		Object val = unInvertField(reader, new Entry(refField, new FieldCache.IntParser() {
		    public int parseInt(BytesRef value) {
		        int v = Integer.parseInt(value.utf8ToString());
		        if (idMapping.containsKey(v)) {
		        	return idMapping.get(v);
		        }
		        else {
		        	return -1;
		        }
		      }
		      protected Object readResolve() {
		        return FieldCache.DEFAULT_INT_PARSER;
		      }
		      @Override
		      public String toString() { 
		        return FieldCache.class.getName()+".UNINVERTING_INT_PARSER"; 
		      }
		    }));
		synchronized(invertedCache) {
			invertedCache.put(refField, val);
		}
		
		return (int[][]) val;
		
	}
	
	private AtomicReader _atom;
	private Integer _readerHashCode = 0;
	public AtomicReader getAtomicReader(IndexReader reader) throws IOException {
		synchronized (_readerHashCode) {
			if (_readerHashCode.equals(reader.hashCode())) {
				return _atom;
			}
			AtomicReader atomReader = SlowCompositeReaderWrapper.wrap(reader);
			_atom = atomReader;
			_readerHashCode = reader.hashCode();
		}
		
		return _atom;
	}

	public int[][] getUnInvertedDocidsStrField(IndexReader reader, String externalIds, String refField) throws IOException {
		
	  AtomicReader atomReader = getAtomicReader(reader);
	  
		if (invertedCache.containsKey(refField) && indexUnchanged(atomReader, externalIds, true)) {
			return (int[][]) invertedCache.get(refField);
		}
		
		final Map<?, Integer> idMapping = getTranslationCacheString(reader, externalIds);
		
		Object val = unInvertField(reader, new Entry(refField, new FieldCache.IntParser() {
		    public int parseInt(BytesRef value) {
		        if (idMapping.containsKey(value.utf8ToString())) {
		        	return idMapping.get(value.utf8ToString());
		        }
		        else {
		        	return -1;
		        }
		      }
		      protected Object readResolve() {
		        return FieldCache.DEFAULT_INT_PARSER;
		      }
		      @Override
		      public String toString() { 
		        return FieldCache.class.getName()+".UNINVERTING_INT_PARSER"; 
		      }
		    }));
		
		synchronized(invertedCache) {
			invertedCache.put(refField, val);
		}
		return (int[][]) val;
	}
	
	
	// XXX: should go away and be replaced by a smarter subclass of DocTermOrds
	// which can read the terms on-the-fly
	protected Object unInvertField(IndexReader reader, Entry entry)
		throws IOException {
		AtomicReader atom = getAtomicReader(reader);
		IntParser parser = (IntParser) entry.custom;
		Bits liveDocs = atom.getLiveDocs();
		
		DocTermOrds unInvertedIndex = new DocTermOrds(atom, entry.field);
		TermsEnum termsEnum = unInvertedIndex.getOrdTermsEnum(atom);
		if (termsEnum == null) {
			return new int[reader.maxDoc()][];
		}
		int[][] retArray = new int[reader.maxDoc()][];
		DocsEnum docs = null;
		
		ArrayList<Integer> docIds = new ArrayList<Integer>();
		for (;;) {
			BytesRef term = termsEnum.next();
			if (term == null)
				break;
			
			int luceneDocId = parser.parseInt(term);
			if (luceneDocId < 0) continue; // skip terms that are not mapped onto lucene ids
			
			int i = 0;
			//int[] val = new int[termsEnum.docFreq()]; // also contains deleted documents
			//System.out.println(term.utf8ToString() + " docFreq: " + termsEnum.docFreq() + " totalTermFreq: " + termsEnum.totalTermFreq());
			docIds.clear();
			docs = termsEnum.docs(liveDocs, docs, 0); // we don't need docFreq
			for (;;) {
				int d = docs.nextDoc();
				if (d == DocIdSetIterator.NO_MORE_DOCS) {
					break;
				}
				//val[i] = d;
				docIds.add(d);
				//i++;
			}
			int[] val = new int[docIds.size()];
			for (int x: docIds) {
				val[i] = x;
				i++;
			}
			retArray[luceneDocId] = val;
		}
		return retArray;
	}
	
	/*
	 * A temporary hack to get uninverted values from the index
	 * the solr-4.0 already has a solution for the problem of 
	 * multivalued cache entries, see:
	 * https://issues.apache.org/jira/browse/LUCENE-3354
	 */
	
	protected Object unInvertFieldx(IndexReader reader, Entry entryKey)
	throws IOException {
		AtomicReader atom = getAtomicReader(reader);
		Entry entry = entryKey;
		String field = entry.field;
		IntParser parser = (IntParser) entry.custom;
		
		
		
		int[][] retArray = new int[reader.maxDoc()][];
		
		final Terms terms = MultiFields.getTerms(reader, field);
	    if (terms == null) {
	      return retArray;
	    }
	    final TermsEnum termsEnum = terms.iterator(null);
	    Bits liveDocs = atom.getLiveDocs();
	    DocsEnum reuse = null;
	    
	    
	    while (termsEnum.next() != null) {
			BytesRef term = termsEnum.term();

			if (term==null) break;

			int luceneDocId = parser.parseInt(term);
			if (luceneDocId < 0) continue; // skip terms that are not mapped onto lucene ids
			
			int i = 0;
			int[] val = new int[termsEnum.docFreq()];
			DocsEnum docs = termsEnum.docs(liveDocs, reuse, 0); // we do not need docFreq
			docs.nextDoc();
			while (docs.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
				val[i] = docs.docID();
				i++;
			}
			retArray[luceneDocId] = val;
		};
			
		return retArray;
	}
}
