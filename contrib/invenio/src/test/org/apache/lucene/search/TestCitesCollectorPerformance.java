package org.apache.lucene.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


import montysolr.util.MontySolrAbstractLuceneTestCase;
import montysolr.util.MontySolrSetup;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.junit.BeforeClass;

public class TestCitesCollectorPerformance extends MontySolrAbstractLuceneTestCase {

	protected String idField;
	private Directory directory;
	private IndexReader reader;
	private IndexSearcher searcher;
	private IndexWriter writer;
	private boolean debug = false;

	@BeforeClass
	public static void beforeClassMontySolrTestCase() throws Exception {
		envInit();
		MontySolrSetup.addBuildProperties("contrib/invenio");
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome()
				+ "/contrib/invenio/src/python");
		MontySolrSetup.addTargetsToHandler("monty_invenio.targets");
		MontySolrSetup
				.addTargetsToHandler("monty_invenio.tests.fake_citation_query");

	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		if (debug) {
			directory = newFSDirectory(new File(TEMP_DIR,"index-citations"));
		}
		else {
			directory = new RAMDirectory();
			directory = newFSDirectory(new File(TEMP_DIR,"index-citations"));
		}
		
		//directory = SimpleFSDirectory.open(new File(TEMP_DIR,"index-citations"));
		//directory = NIOFSDirectory.open(new File(TEMP_DIR,"index-citations"));
		//directory = newDirectory();
		//writer = new RandomIndexWriter(random, directory);
		
		
		reOpenWriter(OpenMode.CREATE);
		writer.deleteAll();
		writer.commit();

		reader = writer.getReader();
		searcher = newSearcher(reader);
	}
	

	@Override
	public void tearDown() throws Exception {
		writer.close();
		searcher.close();
		reader.close();
		directory.close();
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
		Document doc;
		for (int k=0;k<randi.length;k++) {
			doc = new Document();
			doc.add(newField("id",  String.valueOf(k+start), Field.Store.YES,	Field.Index.NOT_ANALYZED));
			doc.add(newField("bibcode",  "b" + (k+start), Field.Store.YES,	Field.Index.NOT_ANALYZED));
			int[] row = new int[randi[k].length];
			x = 0;
			for (int v: randi[k]) {
				doc.add(newField("reference",  String.valueOf(v+start), Field.Store.YES,	Field.Index.NOT_ANALYZED));
				doc.add(newField("breference",  "b" + (v+start), Field.Store.YES,	Field.Index.NOT_ANALYZED));
				row[x] = v+start;
				x++;
			}
			
			writer.deleteDocuments(new TermQuery(new Term("id", String.valueOf(k+start))));
			writer.addDocument(doc);
			data.put(k+start, row);
		}
		
		if (debug) System.out.println("Created random docs: " + start + " - " + numDocs);
		
		return data;
	}
	
	private void reOpenWriter(OpenMode mode) throws CorruptIndexException, LockObtainFailedException, IOException {
		if (writer != null) writer.close();
		writer = new IndexWriter(directory, newIndexWriterConfig(TEST_VERSION_CURRENT, 
				new WhitespaceAnalyzer(TEST_VERSION_CURRENT)).setOpenMode(mode)
				//.setRAMBufferSizeMB(0.1f)
				//.setMaxBufferedDocs(500)
				);
	}
	
	private void reOpenSearcher() throws IOException {
		searcher.close();
		reader.close();
		reader = writer.getReader();
		searcher = newSearcher(reader);
		
	}
	
	
	public void testCitesCollector() throws Exception {
		
		
		int maxHits = 1000;
		int maxHitsFound = 300;
		HashMap<Integer, int[]> cites = createRandomDocs(0, new Float(maxHits * 0.4f).intValue());
		
		
		writer.commit();
		reOpenWriter(OpenMode.APPEND); // close the writer, create a new segment
		cites.putAll(createRandomDocs(new Float(maxHits * 0.3f).intValue(), new Float(maxHits * 0.7f).intValue()));
		
		
		writer.commit();
		reOpenWriter(OpenMode.APPEND); // close the writer, create a new segment
		cites.putAll(createRandomDocs(new Float(maxHits * 0.71f).intValue(), new Float(maxHits * 1.0f).intValue()));
		
		writer.commit();
		reOpenWriter(OpenMode.APPEND); // close the writer, create a new segment
		
		
		reOpenSearcher();
		writer.close();
		
		
		HashMap<Integer, int[]> citedBy = invert(cites);
		
		Map<String, Integer> refCache = DictionaryRecIdCache.INSTANCE.getTranslationCacheString(
				reader, "bibcode");
		
		int[][] invertedCache = DictionaryRecIdCache.INSTANCE.
				getUnInvertedDocidsStrField(((IndexSearcher) searcher).getIndexReader(), 
				"bibcode", "breference");
		
		
		
		assertTrue(refCache.size() > maxHits/2);
		assertTrue(invertedCache.length > maxHits/2);
		
		
		
		
		Map<Integer, Integer> histogram = new HashMap<Integer, Integer>();
		
		SecondOrderCollectorCites coll = new SecondOrderCollectorCites(refCache, "bibcode", "breference");
		searcher.search(new MatchAllDocsQuery(), coll); // run it through the whole index (no IO error should happen)
		
		for (int[] x: coll.getSubReaderRanges(reader)) {
			if (debug) System.err.println("reader: " + x[0] + " - docbase: " + x[1] );
		}
		
		
		
		
		ScoreDoc[] hits;
		for (int i=0; i<maxHits; i++) {
			//System.err.println("search: " + i + " ------");
			//new CitesCollectorString(refCache, "breference")
			
			
			// X->{papers}
			// let's toggle implementataions
			if (i % 3 == 0) {
				hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", String.valueOf(i))), null,
					new SecondOrderCollectorCites(refCache, "bibcode", "breference"), false), maxHitsFound).scoreDocs;
			}
			else if (i % 3 == 1) {
				hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", String.valueOf(i))), null,
						new SecondOrderCollectorCites("bibcode", "breference"), false), maxHitsFound).scoreDocs;
			}
			else {
				hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", String.valueOf(i))), null,
						new SecondOrderCollectorCitesRAM("bibcode", "breference"), false), maxHitsFound).scoreDocs;
			}
			
			if (!histogram.containsKey(hits.length)) {
				histogram.put(hits.length, 0);
			}
			histogram.put(hits.length, histogram.get(hits.length) + 1);
			
			
			if (debug) {
				if(!hitsEquals(cites.get(i), cites, hits)) {
					hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", String.valueOf(i))), null,
					new SecondOrderCollectorCites(refCache, "bibcode", "breference"), false), maxHitsFound).scoreDocs;
					hitsEquals(cites.get(i), cites, hits);
				}
			}
			else {
				assertTrue(hitsEquals(cites.get(i), cites, hits));
			}
			
			
			// {papers} -> X
			if (i % 2 == 0) {
				hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", String.valueOf(i))), null,
					new SecondOrderCollectorCitedBy(invertedCache, "bibcode", "breference"), false), maxHitsFound).scoreDocs;
			}
			else {
				hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", String.valueOf(i))), null,
						new SecondOrderCollectorCitedBy("bibcode", "breference"), false), maxHitsFound).scoreDocs;
			}
			
			if (debug) {
				if(!citedByEquals(citedBy.get(i), citedBy, hits)) {
					hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", String.valueOf(i))), null,
					new SecondOrderCollectorCitedBy(invertedCache, "bibcode", "breference"), false), maxHitsFound).scoreDocs;
					citedByEquals(citedBy.get(i), citedBy, hits);
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
			//new CitesCollectorString(refCache, "breference")
			hits = searcher.search(new CollectorQuery(new TermQuery(new Term("id", String.valueOf(i))), reader, 
					CollectorQuery.createCollector(CitesCollectorString.class, refCache, "breference")), maxHits).scoreDocs;
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
							CollectorQuery.createCollector(CitesCollectorString.class, refCache, "breference")), maxHits).scoreDocs;
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