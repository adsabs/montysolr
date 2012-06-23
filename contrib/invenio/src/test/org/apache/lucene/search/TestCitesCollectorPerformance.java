package org.apache.lucene.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import invenio.montysolr.util.MontySolrAbstractLuceneTestCase;
import invenio.montysolr.util.MontySolrSetup;

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
import org.junit.BeforeClass;

public class TestCitesCollectorPerformance extends MontySolrAbstractLuceneTestCase {

	protected String idField;
	private Directory directory;
	private IndexReader reader;
	private IndexSearcher searcher;
	private IndexWriter writer;

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
		directory = newFSDirectory(new File(TEMP_DIR,"index-citations")); 
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

	public int[][] createRandomDocs(int start, int numDocs) throws IOException {
		Random randomSeed = new Random();
		
		int[] randData = new int[numDocs/10];
		for (int i=0; i<randData.length; i++) {
			randData[i] = randomSeed.nextInt(numDocs) - start;
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
		
		Document doc;
		for (int k=0;k<randi.length;k++) {
			doc = new Document();
			doc.add(newField("id",  String.valueOf(k+start), Field.Store.YES,	Field.Index.NOT_ANALYZED));
			doc.add(newField("bibcode",  "b" + (k+start), Field.Store.YES,	Field.Index.NOT_ANALYZED));
			for (int v: randi[k]) {
				doc.add(newField("reference",  String.valueOf(v+start), Field.Store.YES,	Field.Index.NOT_ANALYZED));
				doc.add(newField("breference",  "b" + (v+start), Field.Store.YES,	Field.Index.NOT_ANALYZED));
			}
			writer.addDocument(doc);
		}
		
		System.out.println("Created random docs: " + start + " - " + numDocs);
		return randi;
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
	private void adoc(String... fields) throws IOException {
		Document doc = new Document();
		for (int i = 0; i < fields.length; i = i + 2) {
			doc.add(newField(fields[i], fields[i + 1], Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		writer.addDocument(doc);
	}
	
	
	public void testCitesCollector() throws Exception {
		
		
		
		int[][] data = createRandomDocs(0, 3000);
		
		
		writer.commit();
		reOpenWriter(OpenMode.APPEND); // close the writer, create a new segment
		createRandomDocs(4000, 7000);
		
		
		writer.commit();
		reOpenWriter(OpenMode.APPEND); // close the writer, create a new segment
		createRandomDocs(7000, 10000);
		
		writer.commit();
		reOpenWriter(OpenMode.APPEND); // close the writer, create a new segment
		
		
		/* this doesn't help
		writer.commit();
		reOpenWriter(OpenMode.APPEND);
		Thread.sleep(1000);
		*/
		
		// without this, you should see errors in the CitesCollector (occassionally, not always)
		//writer.optimize(true);
		
		reOpenSearcher();
		
		int docId = 0;
		int[] citesIds = null;
		for (int i=0; i<data.length;i++) {
			if (data[i].length > 2) {
				docId = i;
				citesIds = data[i];
				break;
			}
		}
		
		Map<String, Integer> refCache = DictionaryRecIdCache.INSTANCE.getTranslationCacheString(
				searcher.getIndexReader(), "breference");
		
		int maxHits = 10000;
		//new CitesCollectorString(refCache, "breference")), maxHits).scoreDocs;
		ScoreDoc[] hits = searcher.search(new CollectorQuery(new TermQuery(new Term("id", String.valueOf(docId))),
				reader,
				CollectorQuery.createCollector(CitesCollectorString.class, refCache, "breference")), maxHits).scoreDocs; 
		
		
		Map<Integer, Integer> histogram = new HashMap<Integer, Integer>();
		
		for (int i=0; i<9000; i++) {
			//System.err.println("search: " + i + " ------");
			//new CitesCollectorString(refCache, "breference")
			hits = searcher.search(new CollectorQuery(new TermQuery(new Term("id", String.valueOf(i))), reader, 
					CollectorQuery.createCollector(CitesCollectorString.class, refCache, "breference")), maxHits).scoreDocs;
			if (!histogram.containsKey(hits.length)) {
				histogram.put(hits.length, 0);
			}
			histogram.put(hits.length, histogram.get(hits.length) + 1);
			
			if (i % 1000 == 0) {
				System.out.println("Done: " + i);
			}
		}
		
		for ( Entry<Integer, Integer> x : histogram.entrySet()) {
			System.out.println(x.getKey() + " : " + x.getValue());
		}
		
		
		
	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestCitesCollectorPerformance.class);
    }
}