package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import invenio.montysolr.util.MontySolrAbstractLuceneTestCase;
import invenio.montysolr.util.MontySolrSetup;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.junit.BeforeClass;

public class TestCitationQuery extends MontySolrAbstractLuceneTestCase {

	protected String idField;
	private Directory directory;
	private IndexReader reader;
	private IndexSearcher searcher;
	private RandomIndexWriter writer;

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
		directory = newDirectory();
		writer = new RandomIndexWriter(random, directory);

		adoc("id", "1", "references", "2", "references", "3", "references", "4");
		adoc("id", "2");
		adoc("id", "3", "references", "5", "references", "6", "references", "99");
		adoc("id", "4", "references", "2", "references", "1");
		adoc("id", "5");
		adoc("id", "6");
		adoc("id", "7", "references", "5");

		reader = writer.getReader();
		searcher = newSearcher(reader);
		writer.close();
	}

	@Override
	public void tearDown() throws Exception {
		searcher.close();
		reader.close();
		directory.close();
		super.tearDown();
	}

	private void adoc(String... fields) throws IOException {
		Document doc = new Document();
		for (int i = 0; i < fields.length; i = i + 2) {
			doc.add(newField(fields[i], fields[i + 1], Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		writer.addDocument(doc);
	}
	
	
	public void testCitedBy() throws Exception {
		TermQuery q1 = new TermQuery(new Term("id", "1"));
		TermQuery q2 = new TermQuery(new Term("id", "2"));
		TermQuery q3 = new TermQuery(new Term("id", "3"));
		TermQuery q4 = new TermQuery(new Term("id", "4"));
		TermQuery q5 = new TermQuery(new Term("id", "5"));
		TermQuery q6 = new TermQuery(new Term("id", "6"));
		TermQuery q7 = new TermQuery(new Term("id", "7"));
		TermQuery q99 = new TermQuery(new Term("id", "99"));
		
		BooleanQuery bq13 = new BooleanQuery();
		bq13.add(q1, Occur.SHOULD);
		bq13.add(q3, Occur.SHOULD);
		
		BooleanQuery bq123 = new BooleanQuery();
		bq123.add(q1, Occur.SHOULD);
		bq123.add(q2, Occur.SHOULD);
		bq123.add(q3, Occur.SHOULD);
		
		BooleanQuery bq1234 = new BooleanQuery();
		bq1234.add(q1, Occur.SHOULD);
		bq1234.add(q2, Occur.SHOULD);
		bq1234.add(q3, Occur.SHOULD);
		bq1234.add(q4, Occur.SHOULD);
		
		BooleanQuery bq15 = new BooleanQuery();
		bq15.add(q1, Occur.SHOULD);
		bq15.add(q5, Occur.SHOULD);
		
		// just a test that index is OK
		assertEquals(1, searcher.search(q1, 10).totalHits);
		assertEquals(1, searcher.search(q2, 10).totalHits);
		assertEquals(1, searcher.search(q3, 10).totalHits);
		assertEquals(0, searcher.search(q99, 10).totalHits);
		assertEquals(2, searcher.search(bq13, 10).totalHits);
		
		
		// now test of references ( X --> (x))
		Map<Integer, Integer> cache = DictionaryRecIdCache.INSTANCE.getTranslationCache(searcher.getIndexReader(), "id");
		
		assertEquals(3, searcher.search(new CollectorQuery(q1, new CitesCollector(cache, "references")), 10).totalHits);
		assertEquals(0, searcher.search(new CollectorQuery(q2, new CitesCollector(cache, "references")), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(q3, new CitesCollector(cache, "references")), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(q4, new CitesCollector(cache, "references")), 10).totalHits);
		assertEquals(0, searcher.search(new CollectorQuery(q5, new CitesCollector(cache, "references")), 10).totalHits);
		assertEquals(0, searcher.search(new CollectorQuery(q6, new CitesCollector(cache, "references")), 10).totalHits);
		assertEquals(0, searcher.search(new CollectorQuery(q99, new CitesCollector(cache, "references")), 10).totalHits);
		assertEquals(5, searcher.search(new CollectorQuery(bq13, new CitesCollector(cache, "references")), 10).totalHits);
		
		ScoreDoc[] docs = searcher.search(new CollectorQuery(bq13, new CitesCollector(cache, "references")), 10).scoreDocs;
		
		ArrayList<Integer> ar = new ArrayList<Integer>();
		for (ScoreDoc d: docs) {
			Document doc = reader.document(d.doc);
			ar.add(Integer.valueOf(doc.get("id")));
		}
		List<Integer> er = Arrays.asList(2,3,4,5,6);
		assertTrue(ar.containsAll(er));
		
		
		int[][] invCache = DictionaryRecIdCache.INSTANCE.getUnInvertedDocids(reader, "references", "id");
		assertEquals(1, searcher.search(new CollectorQuery(q1, new CitedByCollector(invCache, "references")), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(q2, new CitedByCollector(invCache, "references")), 10).totalHits);
		assertEquals(1, searcher.search(new CollectorQuery(q3, new CitedByCollector(invCache, "references")), 10).totalHits);
		assertEquals(1, searcher.search(new CollectorQuery(q4, new CitedByCollector(invCache, "references")), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(q5, new CitedByCollector(invCache, "references")), 10).totalHits);
		assertEquals(1, searcher.search(new CollectorQuery(q6, new CitedByCollector(invCache, "references")), 10).totalHits);
		assertEquals(0, searcher.search(new CollectorQuery(q99, new CitedByCollector(invCache, "references")), 10).totalHits);
		
		assertEquals(2, searcher.search(new CollectorQuery(bq13, new CitedByCollector(invCache, "references")), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(bq123, new CitedByCollector(invCache, "references")), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(bq1234, new CitedByCollector(invCache, "references")), 10).totalHits);
		assertEquals(3, searcher.search(new CollectorQuery(bq15, new CitedByCollector(invCache, "references")), 10).totalHits);
		
		
		ar = new ArrayList<Integer>();
		for (ScoreDoc d: searcher.search(new CollectorQuery(bq15, new CitedByCollector(invCache, "references")), 10).scoreDocs) {
			Document doc = reader.document(d.doc);
			ar.add(Integer.valueOf(doc.get("id")));
		}
		er = Arrays.asList(3, 4,7);
		assertTrue(ar.containsAll(er));
		
	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestCitationQuery.class);
    }
}