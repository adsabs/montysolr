package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import monty.solr.util.MontySolrAbstractLuceneTestCase;
import monty.solr.util.MontySolrSetup;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.junit.BeforeClass;

public class TestSecondOrderQueryTypesAds extends MontySolrAbstractLuceneTestCase {

	protected String idField;
	private Directory directory;
	private IndexReader reader;
	private IndexSearcher searcher;
	private RandomIndexWriter writer;

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
		addDocs(new float[]{0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,});
	}
	
	private void addDocs(float[] b) throws IOException {
	  assert b.length == 10;
		writer = new RandomIndexWriter(random(), directory);

		int i=0;
		adoc("id", "1", "bibcode",   "b1", "const_boost", "1.0f", "boost", "0.5f", "references", "b2,b3,b4,b5");
		adoc("id", "2", "bibcode",   "b2", "const_boost", "1.0f", "boost", "0.2f");
		adoc("id", "3", "bibcode",   "b3", "const_boost", "1.0f", "boost", "0.3f", "references", "b9");
		adoc("id", "4", "bibcode",   "b4", "const_boost", "1.0f", "boost", "0.1f", "references", "b100");
		adoc("id", "5", "bibcode",   "b5", "const_boost", "1.0f", "boost", "0.8f", "references", "b10");
		adoc("id", "6", "bibcode",   "b6", "const_boost", "1.0f", "boost", "0.1f", "references", "b5");
		adoc("id", "7", "bibcode",   "b7", "const_boost", "1.0f", "boost", "0.1f", "references", "b5");
		adoc("id", "8", "bibcode",   "b8", "const_boost", "1.0f", "boost", "0.1f", "references", "b5");
		adoc("id", "9", "bibcode",   "b9", "const_boost", "1.0f", "boost", "0.1f", "references", "b2,b3,b4,b10");
		adoc("id", "10","bibcode",  "b10", "const_boost", "1.0f", "boost", "0.5f", "references", "b3,b4");
		
		reader = writer.getReader();
		searcher = newSearcher(reader);
		writer.close();
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
		  String f = fields[i];
		  if (f.contains("boost")) {
		    doc.add(new FloatField(f, Float.parseFloat(fields[i + 1]), Field.Store.YES));
		  }
		  else {
  		  for (String v: fields[i + 1].split(",")) {
  		    doc.add(newField(fields[i], v, StringField.TYPE_STORED));
  		  }
		  }
		}
		for (String v: "words are all the same".split(" ")) {
  		doc.add(newField("text", v, StringField.TYPE_STORED));
  		doc.add(newField("title", v, StringField.TYPE_STORED));
		}
		writer.addDocument(doc);
	}
	
	
	public void testADSOperators() throws Exception {
		
		
		String refField = "references";
		String idField = "id";
		
		
		// for the queries that use the String values
		// ------------------------------------------
		
		refField = "references";
		idField = "bibcode";
		int idPrefix = 0;
		
		Query d1 = new TermQuery(new Term("id", String.valueOf(idPrefix + 1)));
		Query d2 = new TermQuery(new Term("id", String.valueOf(idPrefix + 2)));
		Query d3 = new TermQuery(new Term("id", String.valueOf(idPrefix + 3)));
		Query d4 = new TermQuery(new Term("id", String.valueOf(idPrefix + 4)));
		Query d5 = new TermQuery(new Term("id", String.valueOf(idPrefix + 5)));
		Query d6 = new TermQuery(new Term("id", String.valueOf(idPrefix + 6)));
		Query d7 = new TermQuery(new Term("id", String.valueOf(idPrefix + 7)));
		Query d8 = new TermQuery(new Term("id", String.valueOf(idPrefix + 8)));
		Query d9 = new TermQuery(new Term("id", String.valueOf(idPrefix + 9)));
		Query d10 = new TermQuery(new Term("id", String.valueOf(idPrefix + 10)));
		Query d99 = new TermQuery(new Term("id", String.valueOf(idPrefix + 99)));
		
		Query r1 = new TermQuery(new Term("references", "b" + String.valueOf(idPrefix + 1)));
    Query r2 = new TermQuery(new Term("references", "b" + String.valueOf(idPrefix + 2)));
    Query r3 = new TermQuery(new Term("references", "b" + String.valueOf(idPrefix + 3)));
    Query r4 = new TermQuery(new Term("references", "b" + String.valueOf(idPrefix + 4)));
    Query r5 = new TermQuery(new Term("references", "b" + String.valueOf(idPrefix + 5)));
    Query r6 = new TermQuery(new Term("references", "b" + String.valueOf(idPrefix + 6)));
    Query r7 = new TermQuery(new Term("references", "b" + String.valueOf(idPrefix + 7)));
    Query r8 = new TermQuery(new Term("references", "b" + String.valueOf(idPrefix + 8)));
    Query r9 = new TermQuery(new Term("references", "b" + String.valueOf(idPrefix + 9)));
    Query r10 = new TermQuery(new Term("references", "b" + String.valueOf(idPrefix + 10)));
    Query r99 = new TermQuery(new Term("references", "b" + String.valueOf(idPrefix + 99)));
		
		
		BooleanQuery bq234 = new BooleanQuery();
		bq234.add(d2, Occur.SHOULD);
		bq234.add(d3, Occur.SHOULD);
		bq234.add(d4, Occur.SHOULD);
		
		BooleanQuery bq19 = new BooleanQuery();
		bq19.add(d1, Occur.SHOULD);
		bq19.add(d9, Occur.SHOULD);
		
		BooleanQuery bq1910 = new BooleanQuery();
    bq1910.add(d1, Occur.SHOULD);
    bq1910.add(d9, Occur.SHOULD);
    bq1910.add(d10, Occur.SHOULD);
    
		BooleanQuery bq26 = new BooleanQuery();
    bq26.add(d2, Occur.SHOULD);
    bq26.add(d6, Occur.SHOULD);
    
    Query all = new MatchAllDocsQuery();
		
		// just a test that index is OK
		assertEquals(1, searcher.search(d1, 10).totalHits);
		assertEquals(1, searcher.search(d2, 10).totalHits);
		assertEquals(1, searcher.search(d3, 10).totalHits);
		assertEquals(0, searcher.search(d99, 10).totalHits);
		assertEquals(3, searcher.search(bq234, 10).totalHits);
		assertEquals(2, searcher.search(bq19, 10).totalHits);
		assertEquals(2, searcher.search(bq26, 10).totalHits);
		
		
		// now test of references ( X --> (x))
		Map<String, Integer> scache = DictionaryRecIdCache.INSTANCE.getTranslationCacheString(searcher.getIndexReader(), idField);
		Map<String, Integer> scache2 = DictionaryRecIdCache.INSTANCE.getTranslationCacheString(searcher.getIndexReader(), idField);
		assertTrue(scache.hashCode() == scache2.hashCode());
		assertTrue(scache == scache2);
		
		
		assertEquals(4, searcher.search(new SecondOrderQuery(d1, null, new SecondOrderCollectorCites(idField, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new SecondOrderQuery(d2, null, new SecondOrderCollectorCites(idField, refField)), 10).totalHits);
		assertEquals(1, searcher.search(new SecondOrderQuery(d3, null, new SecondOrderCollectorCites(idField, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new SecondOrderQuery(d4, null, new SecondOrderCollectorCites(idField, refField)), 10).totalHits);
		assertEquals(1, searcher.search(new SecondOrderQuery(d5, null, new SecondOrderCollectorCites(idField, refField)), 10).totalHits);
		assertEquals(1, searcher.search(new SecondOrderQuery(d6, null, new SecondOrderCollectorCites(idField, refField)), 10).totalHits);
		assertEquals(1, searcher.search(new SecondOrderQuery(d7, null, new SecondOrderCollectorCites(idField, refField)), 10).totalHits);
		assertEquals(1, searcher.search(new SecondOrderQuery(d8, null, new SecondOrderCollectorCites(idField, refField)), 10).totalHits);
		assertEquals(4, searcher.search(new SecondOrderQuery(d9, null, new SecondOrderCollectorCites(idField, refField)), 10).totalHits);
		assertEquals(2, searcher.search(new SecondOrderQuery(d10, null, new SecondOrderCollectorCites(idField, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new SecondOrderQuery(d99, null, new SecondOrderCollectorCites(idField, refField)), 10).totalHits);
		
		
		
		int[][] invCache = DictionaryRecIdCache.INSTANCE.getUnInvertedDocidsStrField(reader, idField, refField);
		int[][] invCache2 = DictionaryRecIdCache.INSTANCE.getUnInvertedDocidsStrField(reader, idField, refField);
		assertTrue(invCache.equals(invCache2));
		assertTrue(invCache == invCache2);
		
		
		assertEquals(0, searcher.search(new SecondOrderQuery(d1, null, new SecondOrderCollectorCitedBy(idField, refField)), 10).totalHits);
		assertEquals(2, searcher.search(new SecondOrderQuery(d2, null, new SecondOrderCollectorCitedBy(idField, refField)), 10).totalHits);
		assertEquals(3, searcher.search(new SecondOrderQuery(d3, null, new SecondOrderCollectorCitedBy(idField, refField)), 10).totalHits);
		assertEquals(3, searcher.search(new SecondOrderQuery(d4, null, new SecondOrderCollectorCitedBy(idField, refField)), 10).totalHits);
		assertEquals(4, searcher.search(new SecondOrderQuery(d5, null, new SecondOrderCollectorCitedBy(idField, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new SecondOrderQuery(d6, null, new SecondOrderCollectorCitedBy(idField, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new SecondOrderQuery(d7, null, new SecondOrderCollectorCitedBy(idField, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new SecondOrderQuery(d8, null, new SecondOrderCollectorCitedBy(idField, refField)), 10).totalHits);
		assertEquals(1, searcher.search(new SecondOrderQuery(d9, null, new SecondOrderCollectorCitedBy(idField, refField)), 10).totalHits);
		assertEquals(2, searcher.search(new SecondOrderQuery(d10, null, new SecondOrderCollectorCitedBy(idField, refField)), 10).totalHits);
		assertEquals(0, searcher.search(new SecondOrderQuery(d99, null, new SecondOrderCollectorCitedBy(idField, refField)), 10).totalHits);
		
		
		FieldCache.DEFAULT.purgeAllCaches();
		
		String boostField = "boost";
		String constBoost = "const_boost";
		assertEquals(4, searcher.search(new SecondOrderQuery(d1, null, new SecondOrderCollectorOperatorExpertsCiting(idField, refField, boostField)), 10).totalHits);
		assertEquals(4, searcher.search(new SecondOrderQuery(d1, null, new SecondOrderCollectorOperatorExpertsCiting(idField, refField, constBoost)), 10).totalHits);
		
		TopDocs normalSet = searcher.search(new SecondOrderQuery(d1, null, new SecondOrderCollectorOperatorExpertsCiting(idField, refField, boostField)), 10);
		TopDocs constSet = searcher.search(new SecondOrderQuery(d1, null, new SecondOrderCollectorOperatorExpertsCiting(idField, refField, constBoost)), 10);
		
		
		// because the query finds only one document, the order is basically unchanged (because it is the order of the 'source' 
		// paper that contributes to the weight of the found hits)
		assert normalSet.totalHits == constSet.totalHits;
		assertArrayEquals(getIds(normalSet.scoreDocs), getIds(normalSet.scoreDocs));
		assertArrayEquals(new int[]{1, 2, 3, 4}, getIds(normalSet.scoreDocs)); // 1,2,3,4 == b2,b3,b4,b5
		assertNotSame("The scores should be different", normalSet.getMaxScore(), constSet.getMaxScore());
		
		
		normalSet = searcher.search(new SecondOrderQuery(bq19, null, new SecondOrderCollectorOperatorExpertsCiting(idField, refField, boostField)), 10);
    constSet = searcher.search(new SecondOrderQuery(bq19, null, new SecondOrderCollectorOperatorExpertsCiting(idField, refField, constBoost)), 10);
    
    // expected order: 5, 2, 3, 4, 10
    assert normalSet.totalHits == constSet.totalHits;
    assertArrayEquals(new int[]{1, 2, 3, 4, 9}, getIds(constSet.scoreDocs));
    assertArrayEquals(new int[]{4, 1, 2, 3, 9}, getIds(normalSet.scoreDocs));
    assertNotSame("The scores should be different", normalSet.getMaxScore(), constSet.getMaxScore());
    
    
    normalSet = searcher.search(new SecondOrderQuery(bq1910, null, new SecondOrderCollectorOperatorExpertsCiting(idField, refField, boostField)), 10);
    constSet = searcher.search(new SecondOrderQuery(bq1910, null, new SecondOrderCollectorOperatorExpertsCiting(idField, refField, constBoost)), 10);
    
    // now we should find the same number of papers, but their order is to be different
    // because they are referenced (multiple times) and the new doc10 should raise up 
    // the score of docs 3,4
    
    // expected order: 5, 3, 4, 2, 10
    assert normalSet.totalHits == constSet.totalHits;
    assertArrayEquals(new int[]{1, 2, 3, 4, 9}, getIds(constSet.scoreDocs));
    assertArrayEquals(new int[]{4, 2, 3, 1, 9}, getIds(normalSet.scoreDocs));
    assertNotSame("The scores should be different", normalSet.getMaxScore(), constSet.getMaxScore());
		
    
    normalSet = searcher.search(new SecondOrderQuery(d5, null, new SecondOrderCollectorCitingTheMostCited(idField, refField, boostField)), 10);
    constSet = searcher.search(new SecondOrderQuery(d5, null, new SecondOrderCollectorCitingTheMostCited(idField, refField, constBoost)), 10);
    
    normalSet = searcher.search(new SecondOrderQuery(all, null, new SecondOrderCollectorCitingTheMostCited(idField, refField, boostField)), 10);
    constSet = searcher.search(new SecondOrderQuery(all, null, new SecondOrderCollectorCitingTheMostCited(idField, refField, constBoost)), 10);
    
    System.out.println(normalSet);
    System.out.println(normalSet);
	}
	
	private int[] getIds(ScoreDoc[] docs) {
	  int[] out = new int[docs.length];
	  int i = 0;
	  for (ScoreDoc d: docs) {
	    out[i++] = d.doc;
	  }
	  return out;
	}
	
	private float[] getScores(ScoreDoc[] docs) {
    float[] out = new float[docs.length];
    int i = 0;
    for (ScoreDoc d: docs) {
      out[i++] = d.score;
    }
    return out;
  }
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestSecondOrderQueryTypesAds.class);
    }
}