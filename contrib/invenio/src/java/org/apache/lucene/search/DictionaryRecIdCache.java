package org.apache.lucene.search;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.FieldCache;

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
	
	private HashMap<String, Map<Integer, Integer>>
		translation_cache = new HashMap<String, Map<Integer, Integer>>(2);
	
	private HashMap<String, Integer>
		translation_cache_tracker = new HashMap<String, Integer>(2);
		
	public void setCache(String name, Map<Integer, int[]> value) {
		cache.put(name, value);
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
		return fromFieldToLuceneId;
	}
	
	/**
	 * Provides the mapping <b>from</b> the external source ids <b>into</b>
	 * lucene doc ids. This class is thread safe if you read data only.
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
	public Map<Integer, Integer> getTranslationCache(IndexReader reader, String field) throws IOException {
		int[] idMapping = getLuceneCache(reader, field);
		Integer h = idMapping.hashCode();
		Integer old_hash = null;
		if (translation_cache_tracker.containsKey(field))
			old_hash = translation_cache_tracker.get(field);
		if (!h.equals(old_hash)) {
				Map<Integer, Integer> translTable = buildCache(idMapping);
				translation_cache.put(field, translTable);
				translation_cache_tracker.put(field, h);
		}
		return translation_cache.get(field);
	}

}
