package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import montysolr.util.MontySolrAbstractLuceneTestCase;
import montysolr.util.MontySolrSetup;

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
		
		// the same thing, but using bibcode-like data
		
		adoc("id", "11", "bibcode", "b1", "breferences", "b2", "breferences", "b3", "breferences", "b4");
		adoc("id", "12", "bibcode", "b2");
		adoc("id", "13", "bibcode", "b3", "breferences", "b5", "breferences", "b6", "breferences", "b99");
		adoc("id", "14", "bibcode", "b4", "breferences", "b2", "breferences", "b1");
		adoc("id", "15", "bibcode", "b5");
		adoc("id", "16", "bibcode", "b6");
		adoc("id", "17", "bibcode", "b7", "breferences", "b5");

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
	
	
	public void testCitationQueries() throws Exception {
		
		
		// for the queries that use the Integer values
		// -------------------------------------------
		
		String refField = "references";
		String idField = "id";
		int idPrefix = 0;
		
		TermQuery q1 = new TermQuery(new Term("id", String.valueOf(idPrefix + 1)));
		TermQuery q2 = new TermQuery(new Term("id", String.valueOf(idPrefix + 2)));
		TermQuery q3 = new TermQuery(new Term("id", String.valueOf(idPrefix + 3)));
		TermQuery q4 = new TermQuery(new Term("id", String.valueOf(idPrefix + 4)));
		TermQuery q5 = new TermQuery(new Term("id", String.valueOf(idPrefix + 5)));
		TermQuery q6 = new TermQuery(new Term("id", String.valueOf(idPrefix + 6)));
		TermQuery q7 = new TermQuery(new Term("id", String.valueOf(idPrefix + 7)));
		TermQuery q99 = new TermQuery(new Term("id", String.valueOf(idPrefix + 99)));
		
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
		Map<Integer, Integer> cache = DictionaryRecIdCache.INSTANCE.getTranslationCache(searcher.getIndexReader(), idField);
		Map<Integer, Integer> cache2 = DictionaryRecIdCache.INSTANCE.getTranslationCache(searcher.getIndexReader(), idField);
		assertTrue(cache.hashCode() == cache2.hashCode());
		assertTrue(cache == cache2);
		
		
		assertEquals(3, searcher.search(new CollectorQuery(q1, reader, CollectorQuery.createCollector(CitesCollector.class, cache, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new CollectorQuery(q2, reader, CollectorQuery.createCollector(CitesCollector.class, cache, refField)), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(q3, reader, CollectorQuery.createCollector(CitesCollector.class, cache, refField)), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(q4, reader, CollectorQuery.createCollector(CitesCollector.class, cache, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new CollectorQuery(q5, reader, CollectorQuery.createCollector(CitesCollector.class, cache, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new CollectorQuery(q6, reader, CollectorQuery.createCollector(CitesCollector.class, cache, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new CollectorQuery(q99, reader, CollectorQuery.createCollector(CitesCollector.class, cache, refField)), 10).totalHits);
		assertEquals(5, searcher.search(new CollectorQuery(bq13, reader, CollectorQuery.createCollector(CitesCollector.class, cache, refField)), 10).totalHits);
		
		ScoreDoc[] docs = searcher.search(new CollectorQuery(bq13, reader, CollectorQuery.createCollector(CitesCollector.class, cache, refField)), 10).scoreDocs;
		
		ArrayList<Integer> ar = new ArrayList<Integer>();
		for (ScoreDoc d: docs) {
			Document doc = reader.document(d.doc);
			ar.add(Integer.valueOf(doc.get("id")));
		}
		List<Integer> er = Arrays.asList(idPrefix + 2, idPrefix + 3, idPrefix + 4, idPrefix + 5, idPrefix + 6);
		assertTrue(ar.containsAll(er));
		
		
		int[][] invCache = DictionaryRecIdCache.INSTANCE.getUnInvertedDocids(reader, idField, refField);
		int[][] invCache2 = DictionaryRecIdCache.INSTANCE.getUnInvertedDocids(reader, idField, refField);
		
		assertTrue(invCache == invCache2);
		assertTrue(invCache.hashCode() == invCache.hashCode());
		
		assertEquals(1, searcher.search(new CollectorQuery(q1, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(q2, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(1, searcher.search(new CollectorQuery(q3, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(1, searcher.search(new CollectorQuery(q4, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(q5, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(1, searcher.search(new CollectorQuery(q6, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new CollectorQuery(q99, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		
		assertEquals(2, searcher.search(new CollectorQuery(bq13, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(bq123, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(bq1234, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(3, searcher.search(new CollectorQuery(bq15, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		
		
		ar = new ArrayList<Integer>();
		for (ScoreDoc d: searcher.search(new CollectorQuery(bq15, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).scoreDocs) {
			Document doc = reader.document(d.doc);
			ar.add(Integer.valueOf(doc.get("id")));
		}
		er = Arrays.asList(idPrefix + 3, idPrefix + 4, idPrefix + 7);
		assertTrue(ar.containsAll(er));
		
		
		
		CollectorQuery c1 = new CollectorQuery(bq15, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField));
		CollectorQuery c2 = new CollectorQuery(bq15, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField));
		assertTrue(c1.equals(c2));
		
		CollectorQuery c3 = new CollectorQuery(bq13, reader, CollectorQuery.createCollector(CitesCollector.class, cache, refField));
		CollectorQuery c4 = new CollectorQuery(bq13, reader, CollectorQuery.createCollector(CitesCollector.class, cache, refField));
		assertTrue(c3.equals(c4));
		
		
		// for the queries that use the String values
		// ------------------------------------------
		
		refField = "breferences";
		idField = "bibcode";
		idPrefix = 10;
		
		q1 = new TermQuery(new Term("id", String.valueOf(idPrefix + 1)));
		q2 = new TermQuery(new Term("id", String.valueOf(idPrefix + 2)));
		q3 = new TermQuery(new Term("id", String.valueOf(idPrefix + 3)));
		q4 = new TermQuery(new Term("id", String.valueOf(idPrefix + 4)));
		q5 = new TermQuery(new Term("id", String.valueOf(idPrefix + 5)));
		q6 = new TermQuery(new Term("id", String.valueOf(idPrefix + 6)));
		q7 = new TermQuery(new Term("id", String.valueOf(idPrefix + 7)));
		q99 = new TermQuery(new Term("id", String.valueOf(idPrefix + 99)));
		
		bq13 = new BooleanQuery();
		bq13.add(q1, Occur.SHOULD);
		bq13.add(q3, Occur.SHOULD);
		
		bq123 = new BooleanQuery();
		bq123.add(q1, Occur.SHOULD);
		bq123.add(q2, Occur.SHOULD);
		bq123.add(q3, Occur.SHOULD);
		
		bq1234 = new BooleanQuery();
		bq1234.add(q1, Occur.SHOULD);
		bq1234.add(q2, Occur.SHOULD);
		bq1234.add(q3, Occur.SHOULD);
		bq1234.add(q4, Occur.SHOULD);
		
		bq15 = new BooleanQuery();
		bq15.add(q1, Occur.SHOULD);
		bq15.add(q5, Occur.SHOULD);
		
		// just a test that index is OK
		assertEquals(1, searcher.search(q1, 10).totalHits);
		assertEquals(1, searcher.search(q2, 10).totalHits);
		assertEquals(1, searcher.search(q3, 10).totalHits);
		assertEquals(0, searcher.search(q99, 10).totalHits);
		assertEquals(2, searcher.search(bq13, 10).totalHits);
		
		
		// now test of references ( X --> (x))
		Map<String, Integer> scache = DictionaryRecIdCache.INSTANCE.getTranslationCacheString(searcher.getIndexReader(), idField);
		Map<String, Integer> scache2 = DictionaryRecIdCache.INSTANCE.getTranslationCacheString(searcher.getIndexReader(), idField);
		assertTrue(scache.hashCode() == scache2.hashCode());
		assertTrue(scache == scache2);
		
		
		assertEquals(3, searcher.search(new CollectorQuery(q1, reader, CollectorQuery.createCollector(CitesCollectorString.class, scache, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new CollectorQuery(q2, reader, CollectorQuery.createCollector(CitesCollectorString.class, scache, refField)), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(q3, reader, CollectorQuery.createCollector(CitesCollectorString.class, scache, refField)), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(q4, reader, CollectorQuery.createCollector(CitesCollectorString.class, scache, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new CollectorQuery(q5, reader, CollectorQuery.createCollector(CitesCollectorString.class, scache, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new CollectorQuery(q6, reader, CollectorQuery.createCollector(CitesCollectorString.class, scache, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new CollectorQuery(q99, reader, CollectorQuery.createCollector(CitesCollectorString.class, scache, refField)), 10).totalHits);
		assertEquals(5, searcher.search(new CollectorQuery(bq13, reader, CollectorQuery.createCollector(CitesCollectorString.class, scache, refField)), 10).totalHits);
		
		docs = searcher.search(new CollectorQuery(bq13, reader, CollectorQuery.createCollector(CitesCollectorString.class, scache, refField)), 10).scoreDocs;
		
		ar = new ArrayList<Integer>();
		for (ScoreDoc d: docs) {
			Document doc = reader.document(d.doc);
			ar.add(Integer.valueOf(doc.get("id")));
		}
		er = Arrays.asList(idPrefix + 2, idPrefix + 3, idPrefix + 4, idPrefix + 5, idPrefix + 6);
		assertTrue(ar.containsAll(er));
		
		
		invCache = DictionaryRecIdCache.INSTANCE.getUnInvertedDocidsStrField(reader, idField, refField);
		invCache2 = DictionaryRecIdCache.INSTANCE.getUnInvertedDocidsStrField(reader, idField, refField);
		
		assertTrue(invCache.equals(invCache2));
		assertTrue(invCache == invCache2);
		
		
		assertEquals(1, searcher.search(new CollectorQuery(q1, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(q2, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(1, searcher.search(new CollectorQuery(q3, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(1, searcher.search(new CollectorQuery(q4, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(q5, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(1, searcher.search(new CollectorQuery(q6, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new CollectorQuery(q99, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		
		assertEquals(2, searcher.search(new CollectorQuery(bq13, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(bq123, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(2, searcher.search(new CollectorQuery(bq1234, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		assertEquals(3, searcher.search(new CollectorQuery(bq15, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).totalHits);
		
		
		ar = new ArrayList<Integer>();
		for (ScoreDoc d: searcher.search(new CollectorQuery(bq15, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField)), 10).scoreDocs) {
			Document doc = reader.document(d.doc);
			ar.add(Integer.valueOf(doc.get("id")));
		}
		er = Arrays.asList(idPrefix + 3, idPrefix + 4, idPrefix + 7);
		assertTrue(ar.containsAll(er));
		
		
		
		c1 = new CollectorQuery(bq15, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField));
		c2 = new CollectorQuery(bq15, reader, CollectorQuery.createCollector(CitedByCollector.class, invCache, refField));
		assertTrue(c1.equals(c2));
		
		c3 = new CollectorQuery(bq13, reader, CollectorQuery.createCollector(CitesCollector.class, cache, refField));
		c4 = new CollectorQuery(bq13, reader, CollectorQuery.createCollector(CitesCollector.class, cache, refField));
		assertTrue(c3.equals(c4));
		
		
	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestCitationQuery.class);
    }
}