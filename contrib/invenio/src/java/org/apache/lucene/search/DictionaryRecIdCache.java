package org.apache.lucene.search;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldCache.IntParser;
import org.apache.lucene.search.FieldCacheImpl.Cache;
import org.apache.lucene.search.FieldCacheImpl.Entry;
import org.apache.lucene.search.FieldCacheImpl.StopFillCacheException;
import org.omg.CORBA.NO_MEMORY;

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
	
	public int[] getLuceneCache(IndexReader reader, String field) throws IOException {
		return FieldCache.DEFAULT.getInts(reader, field);
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
	
	public Map<String, Integer> buildCacheStr(String[] idMapping) throws IOException {
		
		Map<String, Integer> fromFieldToLuceneId = new HashMap<String, Integer>(idMapping.length);
		int i = 0;
		for (String value: idMapping) {
			fromFieldToLuceneId.put(value, i);
			i++;
		}
		if (fromFieldToLuceneId.containsKey(null))	fromFieldToLuceneId.remove(null);
		return fromFieldToLuceneId;
	}
	
	
	private boolean indexUnchanged(IndexReader reader, String field, Boolean isString) throws IOException {
		// first check that the index wasn't updated
		Integer old_hash = null;
		if (translation_cache_tracker.containsKey(field)) {
			old_hash = translation_cache_tracker.get(field);
		}
		else {
			return false;
		}
		if (isString) {
			return old_hash.equals(FieldCache.DEFAULT.getStrings(reader, field).hashCode());
		}
		else {
			return old_hash.equals(FieldCache.DEFAULT.getInts(reader, field).hashCode());
		}
	}
	
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
		if (!(translation_cache_tracker.containsKey(externalIdsField) && indexUnchanged(reader, externalIdsField, false))) {
			synchronized(translation_cache_tracker) {
				int[] idMapping = FieldCache.DEFAULT.getInts(reader, externalIdsField);
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
		
		if (!(translation_cache_tracker.containsKey(externalIdsField) && indexUnchanged(reader, externalIdsField, true))) {
			synchronized(translation_cache_tracker) {
				String[] idMapping = FieldCache.DEFAULT.getStrings(reader, externalIdsField);
				Map<String, Integer> translTable = buildCacheStr(idMapping);
				translation_cache.put(externalIdsField, translTable);
				translation_cache_tracker.put(externalIdsField, idMapping.hashCode());
				return translTable;
			}
		}
		return (Map<String, Integer>) translation_cache.get(externalIdsField);
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
		
		if (invertedCache.containsKey(refField) && indexUnchanged(reader, externalIds, false)) {
			return (int[][]) invertedCache.get(refField);
		}
		
		final Map<?, Integer> idMapping = getTranslationCache(reader, externalIds);
		
		Object val = unInvertField(reader, new Entry(refField, new FieldCache.IntParser() {
		    public int parseInt(String value) {
		        int v = Integer.parseInt(value);
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
	
	public int[][] getUnInvertedDocidsStrField(IndexReader reader, String externalIds, String refField) throws IOException {
		
		if (invertedCache.containsKey(refField) && indexUnchanged(reader, externalIds, true)) {
			return (int[][]) invertedCache.get(refField);
		}
		
		final Map<?, Integer> idMapping = getTranslationCacheString(reader, externalIds);
		
		Object val = unInvertField(reader, new Entry(refField, new FieldCache.IntParser() {
		    public int parseInt(String value) {
		        if (idMapping.containsKey(value)) {
		        	return idMapping.get(value);
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
	
	/*
	 * A temporary hack to get uninverted values from the index
	 * the solr-4.0 already has a solution for the problem of 
	 * multivalued cache entries, see:
	 * https://issues.apache.org/jira/browse/LUCENE-3354
	 */
	
	protected Object unInvertField(IndexReader reader, Entry entryKey)
	throws IOException {
		Entry entry = entryKey;
		String field = entry.field;
		IntParser parser = (IntParser) entry.custom;

		int[][] retArray = new int[reader.maxDoc()][];
		TermDocs termDocs = reader.termDocs();
		TermEnum termEnum = reader.terms (new Term (field));
		try {
			do {
				Term term = termEnum.term();

				if (term==null || term.field() != field) break;
				int[] val = new int[termEnum.docFreq()];

				int i = 0;
				int luceneDocId = parser.parseInt(term.text());
				
				if (luceneDocId < 0) continue; // skip terms that are not mapped onto lucene ids
				
				termDocs.seek (termEnum);
						while (termDocs.next()) {
							
							val[i] = termDocs.doc();
							i++;
						}
						retArray[luceneDocId] = val;
			} while (termEnum.next());
		} catch (StopFillCacheException stop) {
		} finally {
			termDocs.close();
			termEnum.close();
		}
		if (retArray == null) // no values
			retArray = new int[reader.maxDoc()][];
		return retArray;
	}
}
