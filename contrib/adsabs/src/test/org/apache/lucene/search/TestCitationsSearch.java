package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.join.JoinUtil;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.NumericUtils;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.CitationLRUCache;
import org.apache.solr.search.SolrIndexSearcher;
import org.junit.BeforeClass;

@SuppressWarnings({"rawtypes", "unchecked"})
public class TestCitationsSearch extends MontySolrAbstractTestCase {

	private boolean debug = false;
	private SolrQueryRequest tempReq;
	
	@BeforeClass
  public static void beforeClass() throws Exception {
    System.setProperty("solr.allow.unsafe.resourceloading", "true");
    schemaString = MontySolrSetup.getMontySolrHome()
        + "/contrib/adsabs/src/test-files/solr/collection1/conf/"
        + "schema-citations-transformer.xml";
      
    configString = MontySolrSetup.getMontySolrHome()
          + "/contrib/adsabs/src/test-files/solr/collection1/conf/"
          + "citation-cache-solrconfig.xml";
    initCore(configString, schemaString);
  }
	

	@Override
	public void setUp() throws Exception {
		if (debug) {
			// TODO: set the codec new File(TEMP_DIR,"index-citations")
		}
		super.setUp();
	}
	
	@Override 
	public void tearDown() throws Exception {
		if (tempReq != null) {
			tempReq.close();
		}
		super.tearDown();
	}

	public HashMap<Integer, int[]> createRandomDocs(int start, int numDocs) throws IOException {
		Random randomSeed = new Random();
		
		int[] randData = new int[numDocs/10];
		for (int i=0; i<randData.length; i++) {
			randData[i] = Math.abs(randomSeed.nextInt(numDocs) - start);
		}
		
		
		int x = 0;
		int[][] randi = new int[numDocs-start][];
		for (int i=0; i<numDocs-start; i++) {
			int howMany = randomSeed.nextInt(6);
			randi[i] = new int[howMany];
			for (int j=0; j<howMany; j++) {
				if (x>=randData.length) {
					x = 0;
				}
				randi[i][j] = randData[x++];
			}
		}
		
		HashMap<Integer, int[]> data = new HashMap<Integer, int[]>(randi.length);
		List<String> thisDoc = new ArrayList<String>();
		
		for (int k=0;k<randi.length;k++) {
			thisDoc.clear();
			thisDoc.add("id");
			thisDoc.add(String.valueOf(k+start));
			thisDoc.add("bibcode");
			thisDoc.add("b" + (k+start));
			
			int[] row = new int[randi[k].length];
			
			x = 0;
			for (int v: randi[k]) {
				row[x] = v+start;
				thisDoc.add("reference");
				thisDoc.add("b" + String.valueOf(v+start));
				thisDoc.add("ireference");
				thisDoc.add(String.valueOf(v+start));
				x++;
			}
			assertU(adoc(thisDoc.toArray(new String[thisDoc.size()])));
			data.put(k+start, row);
			if (debug) System.out.println(thisDoc);
		}
		
		
		if (debug) System.out.println("Created random docs: " + start + " - " + numDocs);
		return data;
	}
	
	
	
	
	
	public void testCitesCollector() throws Exception {
		
		
		int maxHits = 1000;
		int maxHitsFound = new Float(maxHits * 0.3f).intValue();
		HashMap<Integer, int[]> references = createRandomDocs(0, new Float(maxHits * 0.4f).intValue());
		
		
		assertU(commit()); // closes the writer, create a new segment
		
		references.putAll(createRandomDocs(new Float(maxHits * 0.3f).intValue(), new Float(maxHits * 0.7f).intValue()));
		
		
		assertU(commit()); // closes the writer, create a new segment
		references.putAll(createRandomDocs(new Float(maxHits * 0.71f).intValue(), new Float(maxHits * 1.0f).intValue()));
		
		assertU(commit("waitSearcher", "true")); // closes the writer, create a new segment
		
		
		// invert ourselves - this is what we expect to find
		HashMap<Integer, int[]> citations = invert(references);
		
		
		// get the cache
		tempReq = req("test");
		SolrIndexSearcher searcher = tempReq.getSearcher();
		
		final CitationLRUCache cache = (CitationLRUCache) searcher.getCache("citations-cache-from-references");
		
		assert cache != null;
		
		@SuppressWarnings("rawtypes")
		SolrCacheWrapper citationsWrapper = new SolrCacheWrapper.CitationsCache(cache);
		
		@SuppressWarnings("rawtypes")
		SolrCacheWrapper referencesWrapper = new SolrCacheWrapper.ReferencesCache(cache);
		
		
		
		
		// to collect the measurements data
		Map<Integer, Integer> histogram = new HashMap<Integer, Integer>();
		
		SecondOrderCollectorCites coll = new SecondOrderCollectorCites(referencesWrapper, new String[]{"reference"});
		coll.searcherInitialization(searcher, null);
		
		
		
  	// verify we have all docs in the index
		for (Integer docid: references.keySet()) {
			assertQ(req("q", "id:" + docid), "//*[@numFound='1']");
		}
		assertQ(req("*:*"), "//*[@numFound='" + references.size() + "']");
		int numWithReferences = 0;
		for (Entry<Integer, int[]> entry: references.entrySet()) {
			if (entry.getValue().length > 0) {
				numWithReferences += 1;
			}
		}
		assertQ(req("reference:*"), "//*[@numFound='" + numWithReferences + "']");
  	// run 2nd order through the whole index (no IO error should happen)
		searcher.search(new SecondOrderQuery(new MatchAllDocsQuery(), coll), 10); 
		
		
		
		
		
		
		ScoreDoc[] hits;
		for (Integer i=0; i<maxHits; i++) {
			
			//assertQ(req("q", "id:" + i), "//*[@numFound='1']");
			
			// int field types must be searched with bytes value (not strings)
			BytesRef br = new BytesRef();
			NumericUtils.intToPrefixCoded(i, 0, br);
			
			// references
			if (i % 2 == 1) {
				hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", br.utf8ToString())), null,
						new SecondOrderCollectorCites(referencesWrapper, new String[] {"reference"})), maxHitsFound).scoreDocs;
			}
			else {
				hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", br.utf8ToString())), null,
						new SecondOrderCollectorCitesRAM(referencesWrapper), false), maxHitsFound).scoreDocs;
			}
			
			if (!histogram.containsKey(hits.length)) {
				histogram.put(hits.length, 0);
			}
			histogram.put(hits.length, histogram.get(hits.length) + 1);
			
			
			if (debug) {
				System.out.println("references(id:"+i+")");
				System.out.println("Expecting:" + Arrays.toString(references.get(i)));
				System.out.println("Got:" + Arrays.toString(hits));
				if(!hitsEquals(references.get(i), references, hits)) {
					hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", String.valueOf(i))), null,
					new SecondOrderCollectorCites(referencesWrapper, new String[] {"reference"}), false), maxHitsFound).scoreDocs;
					hitsEquals(references.get(i), references, hits);
				}
			}
			else {
				assertTrue(hitsEquals(references.get(i), references, hits));
			}
			
			
			// citations: {papers} -> X
			if (i % 2 == 0) {
				hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", String.valueOf(i))), null,
					new SecondOrderCollectorCitedBy(citationsWrapper), false), maxHitsFound).scoreDocs;
			}
			else {
				hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", String.valueOf(i))), null,
						new SecondOrderCollectorCitedBy(citationsWrapper), false), maxHitsFound).scoreDocs;
			}
			
			if (debug) {
				if(!citedByEquals(citations.get(i), citations, hits)) {
					hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", String.valueOf(i))), null,
					    new SecondOrderCollectorCitedBy(citationsWrapper), false), maxHitsFound).scoreDocs;
					
					citedByEquals(citations.get(i), citations, hits);
				}
			}
			else {
				//assertTrue(citedByEquals(citedBy.get(i), citedBy, hits));
			}
			
			
			if (i % 5000 == 0 && debug) {
				System.out.println("Done: " + i);
			}
		}
		
		
		
		int sum = 0;
		for ( Entry<Integer, Integer> x : histogram.entrySet()) {
			if (debug) System.out.println(x.getKey() + " : " + x.getValue());
			sum += x.getValue();
		}
		if (debug) System.out.println(sum);
		
		
		
	}
	
	private void compareCitations(Query query, SecondOrderCollector collector) throws IOException {
		SolrQueryRequest r = req("test");
		SolrIndexSearcher searcher = r.getSearcher();
		ScoreDoc[] r1 = searcher.search(new SecondOrderQuery(query, null, collector), 100).scoreDocs;
		ScoreDoc[] r2 = searcher.search(JoinUtil.createJoinQuery("id", false, "reference", query, searcher, ScoreMode.Max), 100).scoreDocs;
		compareResults(r1, r2);
  }
	
	private void compareResults(ScoreDoc[] r1, ScoreDoc[] r2) {
		ArrayList<Integer> a1 = new ArrayList<Integer>();
		ArrayList<Integer> a2 = new ArrayList<Integer>();
		for (int i=0;i<r1.length;i++) {
			a1.add(r1[i].doc);
			a2.add(r2[i].doc);
		}
		Collections.sort(a1);
		Collections.sort(a2);
		assertEquals("The implementations return different results", a1, a2);
	}
	
	private void compareReferences(Query query, SecondOrderCollector collector) throws IOException {
		SolrQueryRequest r = req("test");
		SolrIndexSearcher searcher = r.getSearcher();
		ScoreDoc[] r1 = searcher.search(new SecondOrderQuery(query, null, collector), 100).scoreDocs;
		ScoreDoc[] r2 = searcher.search(JoinUtil.createJoinQuery("reference", true, "id", query, searcher, ScoreMode.Max), 100).scoreDocs;
		compareResults(r1, r2);
  }
	
	private HashMap<Integer, int[]> invert(HashMap<Integer, int[]> cites) {
		HashMap<Integer, List<Integer>> result = new HashMap<Integer, List<Integer>>(cites.size());
		for (Entry<Integer, int[]> e: cites.entrySet()) {
			for (int paperId: e.getValue()) {
				if (!result.containsKey(paperId)) {
					result.put(paperId, new ArrayList<Integer>());
				}
				result.get(paperId).add(e.getKey());
			}
		}
		HashMap<Integer, int[]> out = new HashMap<Integer, int[]>();
		for (Entry<Integer, List<Integer>> e: result.entrySet()) {
			List<Integer> list = e.getValue();
			int[] ret = new int[list.size()];
			for(int i = 0;i < ret.length;i++)
			    ret[i] = list.get(i);
			out.put(e.getKey(), ret);
		}
		return out;
	}

	private boolean hitsEquals(int[] thisDocCites, HashMap<Integer, int[]> cites, ScoreDoc[] hits) throws CorruptIndexException, IOException {
		DirectoryReader reader = tempReq.getSearcher().getIndexReader();
		if (thisDocCites == null && hits.length == 0) return true;
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (ScoreDoc d: hits) {
			try {
				Document doc = reader.document(d.doc);
				result.add(Integer.valueOf(doc.get("id")));
			}
			catch (IOException e) {
				return false;
			}
		}
		ArrayList<Integer> expected = new ArrayList<Integer>();
		for (int r: thisDocCites) {
			if (cites.containsKey(r)) {
				expected.add(r);
			}
		}
		if (!(result.containsAll(expected) && expected.containsAll(result))) {
			System.err.println("expected: " + expected.toString() + " actual: " + result.toString());
		}
		return result.containsAll(expected) && expected.containsAll(result);
	}
	
	private boolean citedByEquals(int[] thisDocCites, HashMap<Integer, int[]> cites, ScoreDoc[] hits) throws CorruptIndexException, IOException {
		DirectoryReader reader = tempReq.getSearcher().getIndexReader();
		if (thisDocCites == null && hits.length == 0) return true;
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (ScoreDoc d: hits) {
			try {
				Document doc = reader.document(d.doc);
				result.add(Integer.valueOf(doc.get("id")));
			}
			catch (IOException e) {
				return false;
			}
		}
		ArrayList<Integer> expected = new ArrayList<Integer>();
		for (int r: thisDocCites) {
			expected.add(r);
		}
		if (!(result.containsAll(expected) && expected.containsAll(result))) {
			System.err.println("expected: " + expected.toString() + " actual: " + result.toString());
		}
		return result.containsAll(expected) && expected.containsAll(result);
	}


	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestCitationsSearch.class);
    }
}