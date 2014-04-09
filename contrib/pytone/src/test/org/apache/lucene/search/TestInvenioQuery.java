package org.apache.lucene.search;

import monty.solr.jni.MontySolrVM;
import monty.solr.jni.PythonMessage;
import monty.solr.util.MontySolrAbstractLuceneTestCase;
import monty.solr.util.MontySolrSetup;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.search.CitationLRUCache;



public class TestInvenioQuery extends MontySolrAbstractLuceneTestCase {
	
	protected String idField;
	
	@BeforeClass
	public static void beforeTestInvenioQuery() throws Exception {
		//MontySolrSetup.addBuildProperties("contrib/adsabs");
		MontySolrSetup.addToSysPath(MontySolrSetup.getMontySolrHome()
				+ "/contrib/adsabs/src/python");
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
  public void testInvenioQuery() throws IOException {
		
		IndexedDocs iDocs = indexDocsPython(10);
		Directory ramdir = indexDocsLucene(iDocs);
		DirectoryReader reader = DirectoryReader.open(ramdir);
		IndexSearcher searcher = new IndexSearcher(reader);
		
		String[] words = iDocs.words;
		HashMap<String, ArrayList<String>> index = iDocs.index;
		
		String rWord = words[new Random().nextInt(words.length)];
		
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
		
		for (String word: words) {
			TermQuery tq = new TermQuery(new Term("text", word));
			InvenioQuery iq = new InvenioQuery(tq, cache, true, "fake_search");
			assertEquals("PythonQuery(text:"+word+")", iq.toString());
			
			TopDocs hits = searcher.search(iq, 100);
			TopDocs hits2 = searcher.search(tq, 100);
			
			//System.out.println("tq=" + hits2.totalHits + ", iq=" + hits.totalHits + " lucene=" + tq + " inv:" + iq);
			//System.out.println(Arrays.toString(hits2.scoreDocs));
			//System.out.println(Arrays.toString(hits.scoreDocs));
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
		
	
	public class InvenioQuery extends PythonQuery {

			
			public InvenioQuery(Query query, SolrCacheWrapper cache,
          boolean dieOnMissingIds, String fName) {
	      super(query, cache, dieOnMissingIds, fName);
      }
			
			@Override
			public String getQueryAsString() {
				return getInvenioQuery();
			}
			
			public String getInvenioQuery() {
				String qval = getInvenioQueryValue(new StringBuffer(), query).toString();
				return qval;
			}
			
			public String getField(String luceneField) {
				return luceneField != null ? luceneField + ":" : "";
			}
			
			public StringBuffer getInvenioQueryValue(StringBuffer out, Query query) {
				if (query instanceof TermQuery) {
					out.append(getField(((TermQuery) query).getTerm().field()));
					out.append(((TermQuery) query).getTerm().text());
				}
				else if (query instanceof MatchAllDocsQuery) {
					out.append("");
				}
				else if (query instanceof TermRangeQuery) {
					TermRangeQuery q = (TermRangeQuery) query;
					out.append(getField(q.getField()));
					
					//out.append(q.includesLower() ? '[' : '{'); // invenio doesn't understand these
					BytesRef lt = q.getLowerTerm();
					BytesRef ut = q.getUpperTerm();
					if (lt == null) {
						out.append('*');
					} else {
						out.append(lt.utf8ToString());
					}

					out.append("->");

					if (ut == null) {
						out.append('*');
					} else {
						out.append(ut.utf8ToString());
					}
				}
				else if (query instanceof NumericRangeQuery) {
					NumericRangeQuery q = (NumericRangeQuery) query;
					out.append(getField(q.getField()));
					
					//out.append(q.includesLower() ? '[' : '{'); // invenio doesn't understand these
					//TODO: verify Invneio is using int ranges only
					Number lt = q.getMin();
					Number ut = q.getMax();
					if (lt == null) {
						out.append("-999999");
					} else {
						out.append(lt.intValue());
					}

					out.append("->");

					if (ut == null) {
						out.append("999999");
					} else {
						out.append(ut.intValue());
					}
				} 
				else if (query instanceof BooleanQuery) {
					BooleanQuery q = (BooleanQuery) query;
					List<BooleanClause>clauses = q.clauses();
					out.append("(");
					for (int i=0;i<clauses.size();i++) {
						BooleanClause c = clauses.get(i);
						Query qq = c.getQuery();
						out.append(c.getOccur().toString());
						out.append(" ");
						getInvenioQueryValue(out, qq);
					}
					out.append(")");
				} 
				else if (query instanceof PrefixQuery) {
					PrefixQuery q = (PrefixQuery) query;
					Term prefix = q.getPrefix();
					out.append(getField(q.getField()));
					out.append(prefix.text());
					out.append('*');
				} 
				else if (query instanceof WildcardQuery) {
					WildcardQuery q = (WildcardQuery) query;
					Term t = q.getTerm();
					out.append(getField(q.getField()));
					out.append(t.text());
				} 
				else if (query instanceof PhraseQuery) {
					PhraseQuery q = (PhraseQuery) query;
					Term[] terms = q.getTerms();
					String slop = q.getSlop() > 0 ? "'" : "\"";
					out.append(slop);
					for (int i=0;i<terms.length;i++) {
						if (i != 0) {
							out.append(" ");
						}
						out.append(getField(terms[i].field()));
						out.append(terms[i].text());
					}
					out.append(slop);
				} 
				else if (query instanceof FuzzyQuery) {
					// do nothing
				} 
				else if (query instanceof ConstantScoreQuery) {
					// do nothing
				} else {
					throw new IllegalStateException(query.toString());
				}
				
				return out;
			}
			
	}
	
	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestInvenioQuery.class);
    }
}
