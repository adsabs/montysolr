package org.apache.lucene.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;


import monty.solr.util.MontySolrAbstractLuceneTestCase;
import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MockIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.CacheRegenerator;
import org.apache.solr.search.CitationLRUCache;
import org.apache.solr.search.SolrCache;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.update.AddUpdateCommand;
import org.apache.solr.update.CommitUpdateCommand;
import org.apache.solr.update.DeleteUpdateCommand;
import org.apache.solr.update.UpdateHandler;
import org.junit.BeforeClass;

public class TestCitesCollectorPerformance extends MontySolrAbstractTestCase {

	private boolean debug = false;
	private SolrQueryRequest tempReq;
	
	public String getSchemaFile() {
		return MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1/conf/" + 
			"schema-citations-transformer.xml";
	}

	public String getSolrConfigFile() {
		return MontySolrSetup.getMontySolrHome() + "/contrib/adsabs/src/test-files/solr/collection1/conf/" + 
			"citation-cache-solrconfig.xml";
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

	Set<Object>tmpX = new HashSet<Object>();
	public HashMap<Integer, int[]> createRandomDocs(int start, int numDocs) throws IOException {
		Random randomSeed = new Random();
		
		int[] randData = new int[numDocs/10];
		for (int i=0; i<randData.length; i++) {
			randData[i] = Math.abs(randomSeed.nextInt(numDocs) - start);
		}
		
		UpdateHandler updater = h.getCore().getUpdateHandler();
    SolrQueryRequest req = req();
    AddUpdateCommand cmd = new AddUpdateCommand(req);
    DeleteUpdateCommand delCmd = new DeleteUpdateCommand(req);
		
		
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
		Document doc;
		for (int k=0;k<randi.length;k++) {
			cmd.solrDoc = new SolrInputDocument();
      cmd.solrDoc.addField("id", String.valueOf(k+start));
      cmd.solrDoc.addField("bibcode", "b" + (k+start));
      
			int[] row = new int[randi[k].length];
			String[] references = new String[row.length];
			String[] breferences = new String[row.length];
			
			x = 0;
			for (int v: randi[k]) {
				row[x] = v+start;
				references[x] = String.valueOf(v+start);
				breferences[x] = "b" + String.valueOf(v+start);
				x++;
			}
			cmd.solrDoc.addField("ireference", references);
			cmd.solrDoc.addField("reference", breferences);
			
			
			delCmd.setQuery("id:" + String.valueOf(k+start));
			updater.deleteByQuery(delCmd);
			updater.addDoc(cmd);
			data.put(k+start, row);
			tmpX.add(k+start);
		}
		
		
		CommitUpdateCommand cmtCmd = new CommitUpdateCommand(req, false);
    updater.commit(cmtCmd);
    
    System.out.println("tmpX=" + tmpX.size());
		if (debug) System.out.println("Created random docs: " + start + " - " + numDocs);
		
		return data;
	}
	
	
	
	
	private CacheRegenerator createCodeRegenerator() {
    CacheRegenerator cr = new CacheRegenerator() {
        public boolean regenerateItem(SolrIndexSearcher newSearcher, SolrCache newCache,
                                      SolrCache oldCache, Object oldKey, Object oldVal) {
          newCache.put(oldKey, oldVal);
          return true;
        }
      };
    return cr;
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
		final CitationLRUCache cache = (CitationLRUCache) searcher.getCache("citations-cache");
		
		
		
		CacheWrapper citationsWrapper = new SecondOrderCollectorCacheWrapper() {
			@Override
		  public int[] getLuceneDocIds(int sourceDocid) {
			  return cache.getCitations(sourceDocid);
		  }
			
			@Override
			public int getLuceneDocId(int sourceDocid, Object sourceValue) {
			  return (Integer) cache.get(sourceValue);
		  }
		};
		
		CacheWrapper referencesWrapper = new SecondOrderCollectorCacheWrapper() {
			@Override
		  public int[] getLuceneDocIds(int sourceDocid) {
			  return cache.getReferences(sourceDocid);
		  }
			@Override
			public int getLuceneDocId(int sourceDocid, Object sourceValue) {
			  Object v = cache.get(sourceValue);
			  if (v == null) {
			  	return -1;
			  }
			  return (Integer) v;
		  }
		};
		
		
		
		
		// to collect the measurements data
		Map<Integer, Integer> histogram = new HashMap<Integer, Integer>();
		
		SecondOrderCollectorCites coll = new SecondOrderCollectorCites(referencesWrapper, new String[]{"reference"});
		coll.searcherInitialization(searcher, null);
		
		
		assertQ(req("reference:*"), "//*[@numFound='" + references.size() + "']");
		assertQ(req("*:*"), "//*[@numFound='" + references.size() + "']");
		searcher.search(new MatchAllDocsQuery(), coll); // run it through the whole index (no IO error should happen)
		
		
		
		
		
		
		ScoreDoc[] hits;
		for (int i=0; i<maxHits; i++) {
			//System.err.println("search: " + i + " ------");
			//new CitesCollectorString(refCache, "reference")
			
			assertQ(req("q", "id:" + i), "//*[@numFound='1']");
			
			// X->{papers}
			// let's toggle implementataions
			if (i % 2 == 1) {
				hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", String.valueOf(i))), null,
						new SecondOrderCollectorCites(referencesWrapper, new String[] {"reference"})), maxHitsFound).scoreDocs;
			}
			else {
				hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", String.valueOf(i))), null,
						new SecondOrderCollectorCitesRAM(referencesWrapper), false), maxHitsFound).scoreDocs;
			}
			
			if (!histogram.containsKey(hits.length)) {
				histogram.put(hits.length, 0);
			}
			histogram.put(hits.length, histogram.get(hits.length) + 1);
			
			
			if (debug) {
				if(!hitsEquals(references.get(i), references, hits)) {
					hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", String.valueOf(i))), null,
					new SecondOrderCollectorCites(referencesWrapper, new String[] {"reference"}), false), maxHitsFound).scoreDocs;
					hitsEquals(references.get(i), references, hits);
				}
			}
			else {
				assertTrue(hitsEquals(references.get(i), references, hits));
			}
			
			
			// {papers} -> X
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
		
		
		/*
		HashMap<Integer, Integer> histogram2 = new HashMap<Integer, Integer>();
		for (int i=0; i<maxHits; i++) {
			//System.err.println("search: " + i + " ------");
			//new CitesCollectorString(refCache, "reference")
			hits = searcher.search(new CollectorQuery(new TermQuery(new Term("id", String.valueOf(i))), reader, 
					CollectorQuery.createCollector(CitesCollectorString.class, refCache, "reference")), maxHits).scoreDocs;
			if (!histogram2.containsKey(hits.length)) {
				histogram2.put(hits.length, 0);
			}
			histogram2.put(hits.length, histogram.get(hits.length) + 1);
			
			if (i % 5000 == 0) {
				if (debug) System.out.println("Done: " + i);
			}
			
			if (debug) {
				if(!hitsEquals(cites.get(i), cites, hits)) {
					hits = searcher.search(new CollectorQuery(new TermQuery(new Term("id", String.valueOf(i))), reader, 
							CollectorQuery.createCollector(CitesCollectorString.class, refCache, "reference")), maxHits).scoreDocs;
					hitsEquals(cites.get(i), cites, hits);
				}
			}
			else {
				assertTrue(hitsEquals(cites.get(i), cites, hits));
			}
		}
		
		sum = 0;
		for ( Entry<Integer, Integer> x : histogram2.entrySet()) {
			if (debug) System.out.println(x.getKey() + " : " + x.getValue());
			sum += x.getValue();
		}
		if (debug) System.out.println(sum);
		
		for (Entry e: histogram.entrySet()) {
			//assertTrue(e.getValue().equals(histogram2.get(e.getKey())));
		}
		
		*/
		
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

	private void merge(HashMap<Integer, int[]> data,
			HashMap<Integer, int[]> additionalData) {
		for (Entry<Integer, int[]> e: additionalData.entrySet()) {
			if (data.containsKey(e.getKey())) {
				int[] row = new int[data.get(e.getKey()).length+ (e.getValue().length)];
				int i = 0;
				for (int r: data.get(e.getKey())) {
					row[i] = r;
					i++;
				}
				for (int r: e.getValue()) {
					row[i] = r;
					i++;
				}
				data.put(e.getKey(), row);
			}
			else {
				data.put(e.getKey(), e.getValue());
			}
		}
		
	}

	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestCitesCollectorPerformance.class);
    }
}