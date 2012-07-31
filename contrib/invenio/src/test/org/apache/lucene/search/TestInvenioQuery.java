package org.apache.lucene.search;

import monty.solr.jni.MontySolrVM;
import monty.solr.jni.PythonMessage;
import monty.solr.util.MontySolrAbstractLuceneTestCase;
import monty.solr.util.MontySolrSetup;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;



public class TestInvenioQuery extends MontySolrAbstractLuceneTestCase {
	
	protected String idField;
	
	@BeforeClass
	public static void beforeTestInvenioQuery() throws Exception {
		MontySolrSetup.addBuildProperties("contrib/invenio");
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome()
				+ "/contrib/invenio/src/python");
		MontySolrSetup.addTargetsToHandler("monty_invenio.targets");
		MontySolrSetup.addTargetsToHandler("monty_invenio.tests.fake_query.targets");
		
	}


	protected IndexedDocs indexDocsPython(int no_docs) {
		PythonMessage message = MontySolrVM.INSTANCE.createMessage("index_docs")
			.setParam("no_docs", no_docs);
		MontySolrVM.INSTANCE.sendMessage(message);
		
		String[] words = (String[]) message.getParam("text");
		int[] recids = (int[]) message.getParam("recid");
		String[] docs = (String[]) message.getParam("docs");
		
		
		return new IndexedDocs(recids, words, docs);
	}
	
	protected Directory indexDocsLucene(IndexedDocs docs) throws IOException {
		Directory ramdir = newDirectory();
	    Analyzer analyzer = new WhitespaceAnalyzer(TEST_VERSION_CURRENT);
	    IndexWriter writer = new IndexWriter(ramdir,
	                                         new IndexWriterConfig(TEST_VERSION_CURRENT, analyzer));
	    
	    HashMap<Integer, ArrayList<String>> ds = docs.docs;
	    Set<Integer> recids = ds.keySet();
	    
	    for (Integer r: recids) {
	    	ArrayList<String> vals = ds.get(r);
	    	Document doc = new Document();
		    Field field1 = newField("recid", r.toString(), TextField.TYPE_STORED);
		    StringBuffer sb = new StringBuffer();
		    for (String v: vals) {
		    	sb.append(v);
		    	sb.append(" ");
		    }
		    Field field2 = newField("text", sb.toString(), TextField.TYPE_STORED);
		    doc.add(field1);
		    doc.add(field2);
		    writer.addDocument(doc);
	    }
	    writer.commit();
	    writer.close();
	    return ramdir;
	}
	
	public void testInvenioQuery() throws IOException {
		
		IndexedDocs iDocs = indexDocsPython(10);
		Directory ramdir = indexDocsLucene(iDocs);
		DirectoryReader reader = DirectoryReader.open(ramdir);
		IndexSearcher searcher = new IndexSearcher(reader);
		
		String[] words = iDocs.words;
		HashMap<String, ArrayList<String>> index = iDocs.index;
		
		String rWord = words[new Random().nextInt(words.length)];
		
		for (String word: words) {
			TermQuery tq = new TermQuery(new Term("text", word));
			InvenioQuery iq = new InvenioQuery(tq, "recid", "text", "fake_search");
			assertEquals(iq.toString(), "<(ints,recid)text:" + word + ">");
			
			TopDocs hits = searcher.search(iq, 100);
			TopDocs hits2 = searcher.search(tq, 100);
			
			//System.out.println("tq=" + hits2.totalHits + ", iq=" + hits.totalHits);
			assertTrue(hits.totalHits == hits2.totalHits);
		}
		
		reader.close();
		ramdir.close();
	}

	
	public class IndexedDocs {
		public int[] recids;
		public String[] words;
		public HashMap<Integer, ArrayList<String>> docs;
		public HashMap<String, ArrayList<String>> index;

		IndexedDocs(int[] recids, String[] words, String[] docs) {
			this.recids = recids;
			this.words = words;
			this.docs = new HashMap<Integer, ArrayList<String>>();
			this.index = new HashMap<String, ArrayList<String>>();
			
			for (String d: docs) {
				String[] parts = d.split(" ");
				Integer recid = Integer.valueOf(parts[0]);
				ArrayList<String> ws = new ArrayList<String>();
				for (int i=1;i<parts.length;i++) {
					ws.add(parts[i]);
					if (!index.containsKey(parts[i])) {
						index.put(parts[i], new ArrayList<String>());
					}
					index.get(parts[i]).add(parts[0]);
				}
				this.docs.put(recid, ws);
			}
		}
	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestInvenioQuery.class);
    }
}
