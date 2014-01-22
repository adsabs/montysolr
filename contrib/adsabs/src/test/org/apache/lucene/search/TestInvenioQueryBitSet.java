package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.antlr.runtime.BitSet;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TestInvenioQuery.InvenioQuery;
import org.apache.lucene.store.Directory;
import org.apache.solr.search.CitationLRUCache;

/**
 * This is the mirror of {@link TestInvenioQuery} but 
 * we use intbitset for communicating with the remote
 * process
 * 
 * @author rca
 *
 */
public class TestInvenioQueryBitSet extends TestInvenioQuery {
	
	private BitSet getBitSet(TopDocs hits) {
		BitSet out = new BitSet();
		ScoreDoc[] dd = hits.scoreDocs;
		for (ScoreDoc d: dd) {
			out.add(d.doc);
		}
		return out;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
  public void testInvenioQuery() throws IOException {
		
		IndexedDocs iDocs = indexDocsPython(10);
		Directory ramdir = indexDocsLucene(iDocs);
		IndexReader reader = DirectoryReader.open(ramdir);
		IndexSearcher searcher = new IndexSearcher(reader);
		
		String[] words = iDocs.words;
		HashMap<String, ArrayList<String>> index = iDocs.index;
		
		final int[] docidCache = FieldCache.DEFAULT.getInts(SlowCompositeReaderWrapper.wrap(reader), "recid", false);
		
		SolrCacheWrapper cache = new SolrCacheWrapper(new CitationLRUCache()) {
			@Override
		  public int getLuceneDocId(int sourceDocid) {
			  return docidCache[sourceDocid];
		  }
			@Override
      public int internalHashCode() {
        return docidCache.hashCode();
      }
			@Override
      public String internalToString() {
        return "~~recid~~";
      }
		};
		
		String rWord = words[new Random().nextInt(words.length)];
		
		int moreThanOne = 0;
		
		for (String word: words) {
			TermQuery tq = new TermQuery(new Term("text", word));
			PythonQueryBitSet iq = new PythonQueryBitSet(tq, cache, true, "fake_search_intbitset");
			//assertEquals("<(intbitset,recid)text:" + word + ">", iq.toString());
			
			TopDocs hits2 = searcher.search(tq, 100);
			TopDocs hits = searcher.search(iq, 100);
			
			assertTrue(hits.totalHits == hits2.totalHits);
			assertTrue(getBitSet(hits).equals(getBitSet(hits2)));
			
			if (hits.totalHits > 0)
				moreThanOne++;
		}
		
		assertTrue(moreThanOne > 0);
		reader.close();
		ramdir.close();
	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestInvenioQueryBitSet.class);
    }
	
}
