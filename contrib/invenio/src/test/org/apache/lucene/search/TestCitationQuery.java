package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
		adoc("id", "3", "references", "5", "references", "6");
		adoc("id", "4", "references", "2");
		adoc("id", "5");
		adoc("id", "6");

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
		TermQuery q3 = new TermQuery(new Term("id", "3"));
		BooleanQuery bq = new BooleanQuery();
		bq.add(q1, Occur.SHOULD);
		bq.add(q3, Occur.SHOULD);
		
		assertEquals(1, searcher.search(q1, 10).totalHits);
		assertEquals(1, searcher.search(q3, 10).totalHits);
		assertEquals(2, searcher.search(bq, 10).totalHits);
		
		
//		collector = new CitationCollectorCites();
//		CitationQuery cq = new CitationQuery(bq, null, collector);
//		
//		
//		assertEquals(5, searcher.search(bq, 10).totalHits);
//		ScoreDoc[] docs = searcher.search(bq, 10).scoreDocs;
//		
//		ArrayList<Integer> ar = new ArrayList<Integer>();
//		for (ScoreDoc d: docs) {
//			ar.add(d.doc);
//		}
//		assertTrue(ar.containsAll(Arrays.asList(new Integer[]{2,3,4,5,6})));
		
	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestCitationQuery.class);
    }
}