package org.apache.solr.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.FieldCache;

public enum DictionaryCache {
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
