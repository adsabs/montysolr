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
import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.DictionaryRecIdCache;
import org.apache.lucene.search.DictionaryRecIdCache.Str2LuceneId;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.SecondOrderCollectorTopN;
import org.apache.lucene.search.SecondOrderQuery;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.update.DirectUpdateHandler2;
import org.apache.solr.update.UpdateHandler;
import org.apache.solr.util.RefCounted;
import org.junit.Test;

public class TestCitationCacheSolr extends MontySolrAbstractTestCase {

	public String getSchemaFile() {
		return MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1/conf/" + 
			"schema-citations-transformer.xml";
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1/conf/" + 
			"citation-cache-solrconfig.xml";
	}

	public void createIndex() throws Exception {
		
		assertU(adoc("id", "0", "bibcode", "b0", 
				"reference", "x2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "1", "bibcode", "b1", 
				"reference", "b2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "2", "bibcode", "b2", "alternate_bibcode", "x2", "alternate_bibcode", "x22",
				"reference", "b2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "3", "bibcode", "b3", 
				"reference", "b2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "4", "bibcode", "b4", 
				"reference", "b2", "reference", "b3", "reference", "b4"));
		
		assertU(commit()); // closes the writer, create a new segment
		
		assertU(adoc("id", "5", "bibcode", "b5", "alternate_bibcode", "x5",
				"reference", "x22", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "6", "bibcode", "b6", 
				"reference", "b2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "7", "bibcode", "b7", 
				"reference", "b2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "8", "bibcode", "b8", 
				"reference", "x2", "reference", "x22", "reference", "b4"));

		assertU(commit()); // closes the writer, create a new segment
		

		assertU(adoc("id", "9", "bibcode", "b9",
				"reference", "b2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "10", "bibcode", "b10",
				"reference", "b2", "reference", "b3", "reference", "b4"));
		
		
		assertU(commit());
	}

	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		createIndex();
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		
	}
	

	@SuppressWarnings("unchecked")
  @Test
	public void test() throws IOException, Exception {
		
		SolrQueryRequest r = req("test");
		SolrIndexSearcher searcher = r.getSearcher();
		
		try {
			
		
			
		SolrCache cache2 = searcher.getCache("citations-cache");
		CitationLRUCache cache = (CitationLRUCache) searcher.getCache("citations-cache");
		
		
		assertTrue( cache.equals(cache2));
		assertTrue( cache2 == cache2 );
		
		// test ID mapping function
		assertTrue( cache.get("b0").equals(0));
		assertTrue( cache.get("b1").equals(1));
		assertTrue( cache.get("b2").equals(2));
		assertTrue( cache.get("x2").equals(2));
		assertTrue( cache.get("x22").equals(2));
		assertTrue( cache.get("b3").equals(3));
		assertTrue( cache.get("b4").equals(4));
		assertTrue( cache.get("b5").equals(5));
		assertTrue( cache.get("x5").equals(5));
		assertTrue( cache.get("b6").equals(6));
		assertTrue( cache.get("b7").equals(7));
		assertTrue( cache.get("b8").equals(8));
		assertTrue( cache.get("b9").equals(9));
		assertTrue( cache.get("b10").equals(10));
		
		// test references
		
		assertArrayEquals("Wrong references", new int[]{3, 4, 2}, cache.getReferences(0)); // the order seems to be lexicographic (as the tokens were indexed)
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences(1));
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences(2));
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences(3));
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences(4));
		assertArrayEquals("Wrong references", new int[]{3, 4, 2}, cache.getReferences(5));
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences(6));
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences(7));
		assertArrayEquals("Wrong references", new int[]{4, 2, 2}, cache.getReferences(8)); // note the duplicate
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences(9));
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences(10));
		
		assertArrayEquals("Wrong references", new int[]{3, 4, 2}, cache.getReferences("b0")); // the order seems to be lexicographic (as the tokens were indexed)
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences("b1"));
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences("x2"));
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences("x22"));
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences("b3"));
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences("b4"));
		assertArrayEquals("Wrong references", new int[]{3, 4, 2}, cache.getReferences("b5"));
		assertArrayEquals("Wrong references", new int[]{3, 4, 2}, cache.getReferences("x5"));
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences("b6"));
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences("b7"));
		assertArrayEquals("Wrong references", new int[]{4, 2, 2}, cache.getReferences("b8")); // note the duplicate
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences("b9"));
		assertArrayEquals("Wrong references", new int[]{2, 3, 4}, cache.getReferences("b10"));
		
		
		// test citations
		assertTrue( cache.getCitations(0) == null);
		assertTrue( cache.getCitations(1) == null);
		assertArrayEquals( "Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10}, cache.getCitations(2)); // note the duplicate
		assertArrayEquals( "Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 10}, cache.getCitations(3));
		assertArrayEquals( "Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, cache.getCitations(4));
		assertTrue( cache.getCitations(5) == null);
		assertTrue( cache.getCitations(6) == null);
		assertTrue( cache.getCitations(7) == null);
		assertTrue( cache.getCitations(8) == null);
		assertTrue( cache.getCitations(9) == null);
		
		assertTrue( cache.getCitations("b0") == null);
		assertTrue( cache.getCitations("b1") == null);
		assertArrayEquals( "Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10}, cache.getCitations("b2")); // note the duplicate
		assertArrayEquals( "Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10}, cache.getCitations("x2")); // note the duplicate
		assertArrayEquals( "Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10}, cache.getCitations("x22")); // note the duplicate
		assertArrayEquals( "Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 10}, cache.getCitations("b3"));
		assertArrayEquals( "Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, cache.getCitations("b4"));
		assertTrue( cache.getCitations("b5") == null);
		assertTrue( cache.getCitations("x5") == null);
		assertTrue( cache.getCitations("b6") == null);
		assertTrue( cache.getCitations("b7") == null);
		assertTrue( cache.getCitations("b8") == null);
		assertTrue( cache.getCitations("b9") == null);
		assertTrue( cache.getCitations("b10") == null);
		
		
		//System.out.println(h.query(req("q", "*:*", "indent", "true", "wt", "json", "rows", "20", "fl", "[docid],*")));
		
		/*
		 * this will delete the old document and create a new one
		 */
		//System.out.println("Replace one doc");
		assertU(adoc("id", "10", "bibcode", "b10",
				"reference", "b2", "reference", "b3", "reference", "b4", "reference", "b44"));
		assertU(commit());
		//System.out.println(h.query(req("q", "*:*", "indent", "true", "wt", "json", "rows", "20", "fl", "[docid],*")));

		
		// close to get the new cache
		r.close();
		r = req("test");
		searcher = r.getSearcher();
		
		
		cache = (CitationLRUCache) searcher.getCache("citations-cache");
		assertTrue( cache.get("b10").equals(11));
		assertArrayEquals("Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 11}, cache.getCitations("b2"));
		assertArrayEquals("Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 11}, cache.getCitations("b3"));
		assertArrayEquals("Wrong references", new int[]{2, 3, 4, -1}, cache.getReferences("b10")); // b44 cannot be resolved		
		assertArrayEquals( "Wrong citations", new int[]{2, 3, 4, -1}, cache.getReferences(11));
		assertArrayEquals( "Wrong citations", null, cache.getCitations(11));
		
		/*
		 * Replace one doc (change its id)
		 * 
		 * this, however, keeps the old document and just deletes 
		 * the field - recycling the docid (wtf?!)
		 */
		
		assertU(adoc("id", "10", 
				"reference", "b2", "reference", "b3", "reference", "b4", "reference", "b44"));
		assertU(commit());
		//System.out.println(h.query(req("q", "*:*", "indent", "true", "wt", "json", "rows", "20", "fl", "[docid],*")));
		
		
		r.close();
		r = req("test");
		searcher = r.getSearcher();
		cache = (CitationLRUCache) searcher.getCache("citations-cache");
		
		
		assertTrue( cache.get("b10") == null);
		assertArrayEquals("Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 11}, cache.getCitations("b2"));
		assertArrayEquals("Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 11}, cache.getCitations("b3"));
		assertArrayEquals("Wrong references", null, cache.getReferences("b10"));		
		assertArrayEquals( "Wrong citations", new int[]{2, 3, 4, -1}, cache.getReferences(11));
		
		
		/* 
		 * Add b10 again 
		 */
		
		assertU(adoc("id", "10", "bibcode", "b10",
				"reference", "b2", "reference", "b3", "reference", "b4", "reference", "b444"));
		assertU(commit());
		
		r.close();
		r = req("test");
		searcher = r.getSearcher();
		cache = (CitationLRUCache) searcher.getCache("citations-cache");
		
		assertTrue( cache.get("b8").equals(8));
		assertTrue( cache.get("b9").equals(9));
		assertTrue( cache.get("b10").equals(11));
		assertArrayEquals("Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 11}, cache.getCitations("b2"));
		assertArrayEquals("Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 11}, cache.getCitations("b3"));
		assertArrayEquals("Wrong references", new int[]{2, 3, 4, -1}, cache.getReferences("b10"));		
		assertArrayEquals( "Wrong citations", new int[]{2, 3, 4, -1}, cache.getReferences(11));
		assertArrayEquals( "Wrong citations", null, cache.getCitations(11));
		
		
		//System.out.println(h.query(req("q", "*:*", "indent", "true", "wt", "json", "rows", "20", "fl", "[docid],*")));
		
		/*
		 * Delete two docs
		 * 
		 * The WEIRD thing is that the last document (b10) has docid=9
		 * as if the segments were automatically merged (and deleted
		 * docs removed)
		 */
		
		assertU(delQ("id:8 OR id:9"));
		assertU(commit());
		
		r.close();
		r = req("test");
		searcher = r.getSearcher();
		cache = (CitationLRUCache) searcher.getCache("citations-cache");
		
		assertTrue( cache.get("b8") == null);
		assertTrue( cache.get("b9") == null);
		assertTrue( cache.get("b10").equals(9));
		assertArrayEquals("Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9}, cache.getCitations("b2"));
		assertArrayEquals("Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9}, cache.getCitations("b3"));
		assertArrayEquals("Wrong references", null, cache.getReferences("b8"));
		assertArrayEquals("Wrong references", null, cache.getReferences(8));
		assertArrayEquals( "Wrong citations", null, cache.getCitations("b9"));
		assertArrayEquals( "Wrong citations", null, cache.getCitations(9));
		
		
		
		/*
		 * Add 8, 9 again
		 */
		assertU(adoc("id", "8", "bibcode", "b8", 
				"reference", "x2", "reference", "x22", "reference", "b4"));
		assertU(adoc("id", "9", "bibcode", "b9",
				"reference", "b2", "reference", "b3", "reference", "b4", "reference", "b44"));
		assertU(commit());
		//System.out.println(h.query(req("q", "*:*", "indent", "true", "wt", "json", "rows", "20", "fl", "[docid],*")));
		
		
		r.close();
		r = req("test");
		searcher = r.getSearcher();
		cache = (CitationLRUCache) searcher.getCache("citations-cache");
		
		assertTrue( cache.get("b8").equals(10));
		assertTrue( cache.get("b9").equals(11));
		assertTrue( cache.get("b10").equals(9));
		int[] xx = cache.getCitations("b8");
		assertArrayEquals("Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 10, 11}, cache.getCitations("b2"));
		assertArrayEquals("Wrong citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 11}, cache.getCitations("b3"));
		assertArrayEquals("Wrong references", new int[]{4, 2, 2}, cache.getReferences("b8"));		
		assertArrayEquals( "Wrong citations", new int[]{2, 3, 4, -1}, cache.getReferences("b9"));
		assertArrayEquals( "Wrong citations", null, cache.getCitations("b8"));
		assertArrayEquals( "Wrong citations", null, cache.getCitations("b9"));
		
		
		CitationLRUCache<Object,Integer> cache3 = (CitationLRUCache<Object,Integer>) searcher.getCache("citations-cache");
		
		int[][][] expected = new int[][][] {
			new int[][] {	new int[]{3,4,2}, new int[0] },
			new int[][] {	new int[]{2,3,4}, new int[0] },
			new int[][] {	new int[]{2,3,4}, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 10, 11} },
			new int[][] {	new int[]{2,3,4}, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 11} },
			new int[][] {	new int[]{2,3,4}, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 11} },
			new int[][] {	new int[]{3,4,2}, new int[0] },
			new int[][] {	new int[]{2,3,4}, new int[0] },
			new int[][] {	new int[]{2,3,4}, new int[0] },
			new int[][] {	new int[0], new int[0] },
			new int[][] {	new int[]{2,3,4,-1}, new int[0] },
			new int[][] {	new int[]{4,2,2}, new int[0] },
			new int[][] {	new int[]{2,3,4,-1}, new int[0] },
		};
		
		int[][][] results = new int[cache3.getCitationsIteratorSize()][2][];
		Iterator<int[][]> it = cache3.getCitationsIterator();
		
		int j = 0;
		while (it.hasNext()) {
			int[][] data = it.next();
			results[j] = data;
			j += 1;
		}
		
		//for (int i=0;i>results.length;i++) {
		//	System.out.println(i);
		//	System.out.println(Arrays.toString(expected[i][0]) + " == " + Arrays.toString(results[i][0]));
		//	System.out.println(Arrays.toString(expected[i][1]) + " == " + Arrays.toString(results[i][1]));
		//}
		
		assertArrayEquals(expected, results);
		
		}
		finally {
			r.close();
		}

		
	}




}


