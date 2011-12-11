package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.antlr.runtime.BitSet;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;

/**
 * This is the mirror of {@link TestInvenioQuery} but 
 * we use intbitset for communicating with the remote
 * process
 * 
 * @author rca
 *
 */
public class TestInvenioQueryBitSet extends TestInvenioQuery {
	
	public void setUp() throws Exception {
		super.setUp();
		// let's play dirty
		addToSysPath("/opt/invenio/lib/python");
	}
	
	private BitSet getBitSet(TopDocs hits) {
		BitSet out = new BitSet();
		ScoreDoc[] dd = hits.scoreDocs;
		for (ScoreDoc d: dd) {
			out.add(d.doc);
		}
		return out;
	}
	public void testInvenioQuery() throws IOException {
		
		IndexedDocs iDocs = indexDocsPython(10);
		Directory ramdir = indexDocsLucene(iDocs);
		IndexSearcher searcher = new IndexSearcher(ramdir);
		
		String[] words = iDocs.words;
		HashMap<String, ArrayList<String>> index = iDocs.index;
		
		
		String rWord = words[new Random().nextInt(words.length)];
		
		int moreThanOne = 0;
		
		for (String word: words) {
			TermQuery tq = new TermQuery(new Term("text", word));
			InvenioQuery iq = new InvenioQueryBitSet(tq, "recid", "fake_search_intbitset");
			assertEquals(iq.toString(), "<" + tq.toString() + ">");
			
			TopDocs hits2 = searcher.search(tq, 100);
			TopDocs hits = searcher.search(iq, 100);
			
			assertTrue(hits.totalHits == hits2.totalHits);
			assertTrue(getBitSet(hits).equals(getBitSet(hits2)));
			
			if (hits.totalHits > 0)
				moreThanOne++;
		}
		
		assertTrue(moreThanOne > 0);
		searcher.close();
		ramdir.close();
	}
	
}
