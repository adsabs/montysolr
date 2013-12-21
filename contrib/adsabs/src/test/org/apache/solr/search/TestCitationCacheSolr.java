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
import java.util.Iterator;
import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.solr.request.SolrQueryRequest;
import org.junit.Test;

@SuppressWarnings({"rawtypes", "unchecked"})
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
		
		/*
		 	0 refs: [3, 4, 2] cits: []
			1 refs: [2, 3, 4] cits: []
			2 refs: [2, 3, 4] cits: [0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10]
			3 refs: [2, 3, 4] cits: [0, 1, 2, 3, 4, 5, 6, 7, 9, 10]
			4 refs: [2, 3, 4] cits: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
			5 refs: [3, 4, 2] cits: []
			6 refs: [2, 3, 4] cits: []
			7 refs: [2, 3, 4] cits: []
			8 refs: [4, 2, 2] cits: []
			9 refs: [2, 3, 4] cits: []
			10 refs: [2, 3, 4] cits: []
		 */
		
		assertU(adoc("id", "0", "bibcode", "b0", 
				"reference", "x2", "reference", "b3", "reference", "b4"
		));
		assertU(adoc("id", "1", "bibcode", "b1", 
				"reference", "b2", "reference", "b3", "reference", "b4"
		));
		assertU(adoc("id", "2", "bibcode", "b2", "alternate_bibcode", "x2", "alternate_bibcode", "x22",
				"reference", "b2", "reference", "b3", "reference", "b4"
				, "citation", "b0", "citation", "b1", "citation", "x2"
				, "citation", "b3", "citation", "b4", "citation", "b5"
				, "citation", "b6", "citation", "b7", "citation", "b8", "citation", "x8"
				, "citation", "b9", "citation", "b10"
		));
		assertU(adoc("id", "3", "bibcode", "b3", 
				"reference", "b2", "reference", "b3", "reference", "b4"
				, "citation", "b0", "citation", "b1", "citation", "x2"
				, "citation", "b3", "citation", "b4", "citation", "b5"
				, "citation", "b6", "citation", "b7"
				, "citation", "b9", "citation", "b10"
				));
		assertU(adoc("id", "4", "bibcode", "b4", 
				"reference", "b2", "reference", "b3", "reference", "b4"
				, "citation", "b0", "citation", "b1", "citation", "x2"
				, "citation", "b3", "citation", "b4", "citation", "b5"
				, "citation", "b6", "citation", "b7", "citation", "b8"
				, "citation", "b9", "citation", "b10"
				));
		
		assertU(commit()); // closes the writer, create a new segment
		
		assertU(adoc("id", "5", "bibcode", "b5", "alternate_bibcode", "x5",
				"reference", "x22", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "6", "bibcode", "b6", 
				"reference", "b2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "7", "bibcode", "b7", 
				"reference", "b2", "reference", "b3", "reference", "b4"));
		assertU(adoc("id", "8", "bibcode", "b8", "alternate_bibcode", "x8",
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
	

  @Test
	public void test() throws IOException, Exception {
		
		SolrQueryRequest r = req("test");
		SolrIndexSearcher searcher = r.getSearcher();
		
		
		String cacheName;
		
		if (random().nextInt(2) == 0) {
			cacheName = "citations-cache-from-citations";
		}
		else {
			cacheName = "citations-cache-from-references";
		}
		
		
		System.out.println("We are testing: " + cacheName);
		
		try {
			
			
			SolrCache cache2 = searcher.getCache(cacheName);
			CitationLRUCache cache = (CitationLRUCache) searcher.getCache(cacheName);
			
			
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
			
			compare("References", new int[]{3, 4, 2}, cache.getReferences(0)); // the order seems to be lexicographic (as the tokens were indexed)
			compare("References", new int[]{2, 3, 4}, cache.getReferences(1));
			compare("References", new int[]{2, 3, 4}, cache.getReferences(2));
			compare("References", new int[]{2, 3, 4}, cache.getReferences(3));
			compare("References", new int[]{2, 3, 4}, cache.getReferences(4));
			compare("References", new int[]{3, 4, 2}, cache.getReferences(5));
			compare("References", new int[]{2, 3, 4}, cache.getReferences(6));
			compare("References", new int[]{2, 3, 4}, cache.getReferences(7));
			compare("References", new int[]{4, 2, 2}, cache.getReferences(8)); // note the duplicate
			compare("References", new int[]{2, 3, 4}, cache.getReferences(9));
			compare("References", new int[]{2, 3, 4}, cache.getReferences(10));
			
			compare("References", new int[]{3, 4, 2}, cache.getReferences("b0")); // the order seems to be lexicographic (as the tokens were indexed)
			compare("References", new int[]{2, 3, 4}, cache.getReferences("b1"));
			compare("References", new int[]{2, 3, 4}, cache.getReferences("x2"));
			compare("References", new int[]{2, 3, 4}, cache.getReferences("x22"));
			compare("References", new int[]{2, 3, 4}, cache.getReferences("b3"));
			compare("References", new int[]{2, 3, 4}, cache.getReferences("b4"));
			compare("References", new int[]{3, 4, 2}, cache.getReferences("b5"));
			compare("References", new int[]{3, 4, 2}, cache.getReferences("x5"));
			compare("References", new int[]{2, 3, 4}, cache.getReferences("b6"));
			compare("References", new int[]{2, 3, 4}, cache.getReferences("b7"));
			compare("References", new int[]{4, 2, 2}, cache.getReferences("b8")); // note the duplicate
			compare("References", new int[]{2, 3, 4}, cache.getReferences("b9"));
			compare("References", new int[]{2, 3, 4}, cache.getReferences("b10"));
			
			
			// test citations
			compare( "Citations", null, cache.getCitations(0));
			compare( "Citations", null, cache.getCitations(1));
			compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10}, cache.getCitations(2)); // note the duplicate
			compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 10}, cache.getCitations(3));
			compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, cache.getCitations(4));
			compare( "Citations", null, cache.getCitations(5));
			compare( "Citations", null, cache.getCitations(6));
			compare( "Citations", null, cache.getCitations(7));
			compare( "Citations", null, cache.getCitations(8));
			compare( "Citations", null, cache.getCitations(9));
			
			compare( "Citations", null, cache.getCitations("b0"));
			compare( "Citations", null, cache.getCitations("b1"));
			compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10}, cache.getCitations("b2")); // note the duplicate
			compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10}, cache.getCitations("x2")); // note the duplicate
			compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10}, cache.getCitations("x22")); // note the duplicate
			compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 10}, cache.getCitations("b3"));
			compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, cache.getCitations("b4"));
			compare( "Citations", null, cache.getCitations("b5"));
			compare( "Citations", null, cache.getCitations("x5"));
			compare( "Citations", null, cache.getCitations("b6"));
			compare( "Citations", null, cache.getCitations("b7"));
			compare( "Citations", null, cache.getCitations("b8"));
			compare( "Citations", null, cache.getCitations("b9"));
			compare( "Citations", null, cache.getCitations("b10"));
			
			
			
			/*
			 * this will delete the old document (recid=10) and create a new one (recid=11)
			 */
			assertU(adoc("id", "10", "bibcode", "b10",
					"reference", "b2", "reference", "b3", "reference", "b4", "reference", "b44"));
			assertU(commit());
			
			r.close();
			r = req("test");
			searcher = r.getSearcher();
			cache = (CitationLRUCache) searcher.getCache(cacheName);
			
			
			//printCache(cache);
			
			assertTrue( cache.get(10) == null);
			assertTrue( cache.get("b10").equals(11));
			
			if (cacheName.contains("from-references")) {
				
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b2"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b3"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b4"));
				compare("References", new int[]{2, 3, 4, -1}, cache.getReferences("b10"));
				compare("References", new int[]{2, 3, 4, -1}, cache.getReferences(11));
				
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 11}, cache.getCitations("b2"));
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 11}, cache.getCitations("b3"));
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11}, cache.getCitations("b4"));
				compare( "Citations", null, cache.getCitations("b10")); // b44 cannot be resolved
				compare( "Citations", null, cache.getCitations(11));
				
			}
			else if (cacheName.contains("from-citations")) {
				
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b2"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b3"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b4"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b10"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences(11));
				
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 11}, cache.getCitations("b2"));
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 11}, cache.getCitations("b3"));
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11}, cache.getCitations("b4"));
				compare( "Citations", null, cache.getCitations("b10"));
				compare( "Citations", null, cache.getCitations(11)); 
				
			}
			
			
			
			/*
			 * Replace one doc (change its id)
			 * 
			 * this, however, keeps the old document and just deletes 
			 * the field - recycling the docid (wtf?!)
			 */
			
			assertU(adoc("id", "10", 
					"reference", "b2", "reference", "b3", "reference", "b4", "reference", "b44"
					));
			assertU(commit());
			//System.out.println(h.query(req("q", "*:*", "indent", "true", "wt", "json", "rows", "20", "fl", "[docid],*")));
			
			
			r.close();
			r = req("test");
			searcher = r.getSearcher();
			cache = (CitationLRUCache) searcher.getCache(cacheName);
			
			//printCache(cache);
			
			
			
			/*
			 * this might seem confusing, but it is OK, the citation cache for 'b10'
			 * cannot be retrieved, because 'b10' key is not in the cache; however
			 * its recid (11) will be present in the *derived* reference structures,
			 * because for multi-valued terms, we are reading all terms, and for each
			 * term all documents that contain them and thus the recid 11 makes its
			 * way into citations (when inferred from refernces) BUT IT DOES NOT make
			 * it when inferred from citations (because b2,b3,b4 are citing b10 and 
			 * that doesn't make it; hence no references can be inferred from nonexisting
			 * doc)
			 * 
			 * note: i could check for the existence of the document id, but that would
			 * add additional cost and it is not necessary (i think)
			 */
			assertTrue( cache.get(10) == null);
			assertTrue( cache.get(11) == null);
			assertTrue( cache.get("b10") == null);
			assertTrue( cache.get("b11") == null);
			
			
			if (cacheName.contains("from-references")) {
				
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b2"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b3"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b4"));
				compare("References", null, cache.getReferences("b10"));
				compare("References", new int[]{2, 3, 4, -1}, cache.getReferences(11)); // b44 cannot be resolved
				
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 11}, cache.getCitations("b2"));
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 11}, cache.getCitations("b3"));
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11}, cache.getCitations("b4"));
				compare( "Citations", null, cache.getCitations("b10"));
				compare( "Citations", null, cache.getCitations(11)); 
				
			}
			else if (cacheName.contains("from-citations")) {
				
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b2"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b3"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b4"));
				compare("References", null, cache.getReferences("b10"));
				
				compare( "Citations", new int[]{0, 1, -1, 3, 4, 5, 6, 7, 8, 9, 2, 8}, cache.getCitations("b2"));
				compare( "Citations", new int[]{0, 1, -1, 3, 4, 5, 6, 7, 9, 2}, cache.getCitations("b3"));
				compare( "Citations", new int[]{0, 1, -1, 3, 4, 5, 6, 7, 8, 9, 2}, cache.getCitations("b4"));
				compare( "Citations", null, cache.getCitations("b10"));
				compare( "Citations", null, cache.getReferences(11));
				
			}
			
			
			
			/* 
			 * Add b10 again 
			 */
			
			assertU(adoc("id", "10", "bibcode", "b10",
					"reference", "b2", "reference", "b3", "reference", "b4", "reference", "b444"));
			assertU(commit());
			
			r.close();
			r = req("test");
			searcher = r.getSearcher();
			cache = (CitationLRUCache) searcher.getCache(cacheName);
			
			//printCache(cache);
			
			assertTrue( cache.get(10) == null);
			assertTrue( cache.get("b10").equals(11));
			
			if (cacheName.contains("from-references")) {
				
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b2"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b3"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b4"));
				compare("References", new int[]{2, 3, 4, -1}, cache.getReferences("b10"));
				compare("References", new int[]{2, 3, 4, -1}, cache.getReferences(11));
				
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 11}, cache.getCitations("b2"));
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 11}, cache.getCitations("b3"));
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11}, cache.getCitations("b4"));
				compare( "Citations", null, cache.getCitations("b10")); 
				compare( "Citations", null, cache.getCitations(11));
				
			}
			else if (cacheName.contains("from-citations")) {
				
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b2"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b3"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b4"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b10"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences(11));
				
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 11}, cache.getCitations("b2"));
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 11}, cache.getCitations("b3"));
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11}, cache.getCitations("b4"));
				compare( "Citations", null, cache.getCitations("b10"));
				compare( "Citations", null, cache.getCitations(11));
				
			}
			
			
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
			cache = (CitationLRUCache) searcher.getCache(cacheName);
			
			
			//printCache(cache);
			
			assertTrue( cache.get("b8") == null);
			assertTrue( cache.get("b9") == null);
			assertTrue( cache.get("b10").equals(9));
			
			
			if (cacheName.contains("from-references")) {
				
				compare("References", null, cache.getReferences("b8"));
				compare("References", null, cache.getReferences("b9"));
				compare("References", new int[]{2, 3, 4, -1}, cache.getReferences("b10"));
				
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9}, cache.getCitations("b2"));
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9}, cache.getCitations("b3"));
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9}, cache.getCitations("b4"));
				compare( "Citations", null, cache.getCitations("b10")); 
				
			}
			else if (cacheName.contains("from-citations")) {
				
				compare("References", null, cache.getReferences("b8"));
				compare("References", null, cache.getReferences("b9"));
				compare("References", new int[]{2, 3, 4}, cache.getReferences("b10"));
				
				compare( "Citations", new int[]{0, 1, 9, 3, 4, 5, 6, 7, -1, -1, 2, -1}, cache.getCitations("b2"));
				compare( "Citations", new int[]{0, 1, 9, 3, 4, 5, 6, 7, -1, 2}, cache.getCitations("b3"));
				compare( "Citations", new int[]{0, 1, 9, 3, 4, 5, 6, 7, -1, -1, 2}, cache.getCitations("b4"));
				compare( "Citations", null, cache.getCitations("b10")); 
				
			}
	
			
			
			
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
			cache = (CitationLRUCache) searcher.getCache(cacheName);
			
			//printCache(cache);
			
			assertTrue( cache.get("b8").equals(10));
			assertTrue( cache.get("b9").equals(11));
			assertTrue( cache.get("b10").equals(9));
			
			if (cacheName.contains("from-references")) {
				
				compare( "References", new int[]{2, 3, 4}, cache.getReferences("b2"));
				compare( "References", new int[]{4, 2, 2}, cache.getReferences("b8"));
				compare( "References", new int[]{2, 3, 4, -1}, cache.getReferences("b9"));
				compare( "References", new int[]{2, 3, 4, -1}, cache.getReferences("b10"));
	
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 10, 11}, cache.getCitations("b2"));
				compare( "Citations", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 9, 11}, cache.getCitations("b3"));
				compare( "Citations", null, cache.getCitations("b8"));
				compare( "Citations", null, cache.getCitations("b9"));
				compare( "Citations", null, cache.getCitations("b10")); 
				
			}
			else if (cacheName.contains("from-citations")) {
				
				compare( "References", new int[]{2, 3, 4}, cache.getReferences("b2"));
				compare( "References", new int[]{2, 4}, cache.getReferences("b8"));
				compare( "References", new int[]{2, 3, 4}, cache.getReferences("b9"));
				compare( "References", new int[]{2, 3, 4}, cache.getReferences("b10"));
				
				// -1 == x8, the alternate bibcode is missing in the newly added doc
				compare( "Citations", new int[]{0, 1, 9, 3, 4, 5, 6, 7, 10, 11, 2, -1}, cache.getCitations("b2"));
				compare( "Citations", new int[]{0, 1, 9, 3, 4, 5, 6, 7, 11, 2}, cache.getCitations("b3"));
				compare( "Citations", null, cache.getCitations("b8"));
				compare( "Citations", null, cache.getCitations("b9"));
				compare( "Citations", null, cache.getCitations("b10")); 
				
			}
			
			
			if (cacheName.contains("from-references")) {
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
				
				assertArrayEquals(expected, getCache(cache));
			}
			else if (cacheName.contains("from-citations")) {
				int[][][] expected = new int[][][] {
						new int[][] {	new int[]{2, 3, 4}, new int[0] },
						new int[][] {	new int[]{2, 3, 4}, new int[0] },
						new int[][] {	new int[]{2, 3, 4}, new int[]{0, 1, 9, 3, 4, 5, 6, 7, 10, 11, 2, -1} },
						new int[][] {	new int[]{2, 3, 4}, new int[]{0, 1, 9, 3, 4, 5, 6, 7, 11, 2} },
						new int[][] {	new int[]{2, 3, 4}, new int[]{0, 1, 9, 3, 4, 5, 6, 7, 10, 11, 2} },
						new int[][] {	new int[]{2, 3, 4}, new int[0] },
						new int[][] {	new int[]{2, 3, 4}, new int[0] },
						new int[][] {	new int[]{2, 3, 4}, new int[0] },
						new int[][] {	new int[0], new int[0] },
						new int[][] {	new int[]{2, 3, 4}, new int[0] },
						new int[][] {	new int[]{2, 4}, new int[0] },
						new int[][] {	new int[]{2, 3, 4}, new int[0] },
					};
					
				assertArrayEquals(expected, getCache(cache));
			}
			
		}
		finally {
			r.close();
		}

		
	}

  private int[][][] getCache(CitationLRUCache cache) {
  	int[][][] results = new int[cache.getCitationsIteratorSize()][2][];
		Iterator<int[][]> it = cache.getCitationsIterator();
		
		int j = 0;
		while (it.hasNext()) {
			int[][] data = it.next();
			results[j] = data;
			j += 1;
		}
		return results;
  }

	private void compare(String msg, int[] exp, int[] res) {
  	if (exp != null)
	  	Arrays.sort(exp);
  	if (res != null)
	  	Arrays.sort(res);
  	
  	assertArrayEquals(msg,  exp, res);
  }
  
  private void printCache(CitationLRUCache cache) {
  	Iterator cci = cache.getCitationsIterator();
		int ii = 0;
		while (cci.hasNext()) {
			int[][] dta = (int[][]) cci.next();
			System.out.println(ii + " refs: " + Arrays.toString(dta[0]) + " cits: " + Arrays.toString(dta[1]));
			ii++;
		}
  }
  
  


}


