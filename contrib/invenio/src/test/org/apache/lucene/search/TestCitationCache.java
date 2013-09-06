package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


import monty.solr.util.MontySolrAbstractLuceneTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MockIndexWriter;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.DictionaryRecIdCache.UnInvertedMap;
import org.apache.lucene.search.join.JoinUtil;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.BeforeClass;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestCitationCache extends MontySolrAbstractLuceneTestCase {

	protected String idField;
	private Directory directory;
	private IndexReader reader;
	private IndexSearcher searcher;
	private MockIndexWriter writer;

	@BeforeClass
	public static void beforeTestCitationQuery() throws Exception {
		MontySolrSetup.addBuildProperties("contrib/invenio");
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome()
				+ "/contrib/invenio/src/python");

	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		directory = newDirectory();
		reOpenWriter(OpenMode.CREATE);

		adoc("id", "0", "bibcode", "b0", 
				"reference", "b2", "reference", "b3", "reference", "b4");
		adoc("id", "1", "bibcode", "b1", 
				"reference", "b2", "reference", "b3", "reference", "b4");
		adoc("id", "2", "bibcode", "b2", "alternate_bibcode", "x2", "alternate_bibcode", "x22",
				"reference", "b2", "reference", "b3", "reference", "b4");
		adoc("id", "3", "bibcode", "b3", 
				"reference", "b2", "reference", "b3", "reference", "b4");
		adoc("id", "4", "bibcode", "b4", 
				"reference", "b2", "reference", "b3", "reference", "b4");
		
		writer.commit();
		reOpenWriter(OpenMode.APPEND); // close the writer, create a new segment
		
		adoc("id", "5", "bibcode", "b5", "alternate_bibcode", "x5",
				"reference", "b2", "reference", "b3", "reference", "b4");
		adoc("id", "6", "bibcode", "b6", 
				"reference", "b2", "reference", "b3", "reference", "b4");
		adoc("id", "7", "bibcode", "b7", 
				"reference", "b2", "reference", "b3", "reference", "b4");
		adoc("id", "8", "bibcode", "b8", 
				"reference", "x2", "reference", "x22", "reference", "b4");

		writer.commit();
		reOpenWriter(OpenMode.APPEND); // close the writer, create a new segment

		adoc("id", "9", "bibcode", "b9",
				"reference", "b2", "reference", "b3", "reference", "b4");
		adoc("id", "10", "bibcode", "b10",
				"reference", "b2", "reference", "b3", "reference", "b4");
		
		
		writer.commit();
		reader = writer.getReader();
		searcher = newSearcher(reader);
		writer.close();
	}
	
	private void reOpenWriter(OpenMode mode) throws CorruptIndexException, LockObtainFailedException, IOException {
		if (writer != null) writer.close();
		writer = new MockIndexWriter(directory, newIndexWriterConfig(TEST_VERSION_CURRENT, 
				new WhitespaceAnalyzer(TEST_VERSION_CURRENT)).setOpenMode(mode)
				//.setRAMBufferSizeMB(0.1f)
				//.setMaxBufferedDocs(500)
				);
	}
	
	
	@Override
	public void tearDown() throws Exception {
		reader.close();
		directory.close();
		super.tearDown();
	}

	private void adoc(String... fields) throws IOException {
		Document doc = new Document();
		for (int i = 0; i < fields.length; i = i + 2) {
			doc.add(newField(fields[i], fields[i + 1], StringField.TYPE_STORED));
		}
		writer.addDocument(doc);
	}
	
	
	public void testCitationCache() throws Exception {
		
		// first make sure
		
		
		// what papers are referenced by each paper
		Map<Integer, List<Integer>> cache1 = DictionaryRecIdCache.INSTANCE.getCache(UnInvertedMap.MULTIVALUED, 
				searcher, 
				new String[] {"bibcode", "alternate_bibcode"}, 
				"reference");
		
		Map<Integer, List<Integer>> cache2 = DictionaryRecIdCache.INSTANCE.getCache(UnInvertedMap.MULTIVALUED, 
				searcher, 
				new String[] {"bibcode", "alternate_bibcode"}, 
				"reference");
		
		assertTrue( cache1.equals(cache2));
		assertTrue( cache1 == cache2 );
		
		// TODO: finish
//		assertTrue(cache1.get(0).equals(Arrays.asList(2, 3, 4)));
//		assertTrue(cache1.get(1).equals(Arrays.asList(2, 3, 4)));
//		assertTrue(cache1.get(2).equals(Arrays.asList(2, 3, 4)));
//		assertTrue(cache1.get(3).equals(Arrays.asList(2, 3, 4)));
//		assertTrue(cache1.get(4).equals(Arrays.asList(2, 3, 4)));
//		assertTrue(cache1.get(5).equals(Arrays.asList(2, 3, 4)));
//		assertTrue(cache1.get(6).equals(Arrays.asList(2, 3, 4)));
//		assertTrue(cache1.get(7).equals(Arrays.asList(2, 3, 4)));
//		assertTrue(cache1.get(8).equals(Arrays.asList(2, 4)));
//		assertTrue(cache1.get(9).equals(Arrays.asList(2, 3, 4)));
//		assertTrue(cache1.get(10).equals(Arrays.asList(2, 3, 4)));
		
		
	}
	
	

}