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
	
	private HashMap<String, Map<Integer, Integer>>
		translation_cache = new HashMap<String, Map<Integer, Integer>>(2);
	
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
			synchronized(translation_cache_tracker) {
				Map<Integer, Integer> translTable = buildCache(idMapping);
				translation_cache.put(field, translTable);
				translation_cache_tracker.put(field, h);
			}
		}
		return translation_cache.get(field);
	}
	
	

	/**
	 * Returns the lucene docids 
	 * @param reader
	 * @param field
	 * @return
	 * @throws IOException
	 */
	public int[][] getUnInvertedDocids(IndexReader reader, String field, String externalIds) throws IOException {
		
		final Map<Integer, Integer> idMapping = getTranslationCache(reader, externalIds);
		
		Object val = unInvertField(reader, new Entry(field, new FieldCache.IntParser() {
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
