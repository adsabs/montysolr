package newseman;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.aqp.TestAqpSLGStandardTest.QPTestFilter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import monty.solr.jni.MontySolrVM;
import monty.solr.jni.PythonMessage;
import newseman.SemanticTaggerTokenFilter;
import newseman.SemanticTagger;
import newseman.MontySolrBaseTokenStreamTestCase;


import java.io.Reader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Just a basic tests that verifies the tokens are translated and the query is
 * searching for both words and semes. There should be much more elaborate test
 * suite in future (time, time...)
 * 
 * @author rca
 * 
 */
public class TestSemanticTaggerIndexing extends
		MontySolrBaseTokenStreamTestCase {

	private String url;
	private SemanticTagger tagger;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		url = "sqlite:///:memory:";

		tagger = new SemanticTagger(url);

		// fill the db with test data, must happen before we configure seman to
		// have data
		PythonMessage message = MontySolrVM.INSTANCE.createMessage(
				"fill_newseman_dictionary").setParam("url", tagger.getName());
		MontySolrVM.INSTANCE.sendMessage(message);

		tagger.configureTagger("czech", 2, "rewrite", "purge");

	}

	public void testSemanticTokenFilter() throws IOException, ParseException {
		String text = "velká světová revoluce byla velká říjnová revoluce "
				+ "s velkou extra říjnovou revolucí";

		Directory ramdir = new RAMDirectory();
		Analyzer analyzer = new TestSemanticAnalyzer();
		IndexWriter writer = new IndexWriter(ramdir, new IndexWriterConfig(
				TEST_VERSION_CURRENT, analyzer));
		Document doc = new Document();
		Field field1 = newField("foo", text, TextField.TYPE_STORED);
		Field field2 = newField("foox", text, TextField.TYPE_STORED);

		doc.add(field1);
		doc.add(field2);
		writer.addDocument(doc);
		writer.close();

		IndexSearcher ram = new IndexSearcher(DirectoryReader.open(ramdir));
		QueryParser qp1 = new QueryParser(TEST_VERSION_CURRENT, "foo", analyzer);
		QueryParser qp2 = new QueryParser(TEST_VERSION_CURRENT, "foox",
				analyzer);

		TopDocs hits;

		hits = ram.search(qp1.parse("foo:XXX"), 10);
		assertTrue(hits.totalHits == 1);

		hits = ram.search(qp1.parse("foox:XXX"), 10);
		assertTrue(hits.totalHits == 0);

		// currently, each token is tokenized by the qparser
		// so we don't see them together
		Query q1 = qp1.parse("\"velká říjnová revoluce\"");
		Query q2 = qp2.parse("\"velká říjnová revoluce\"");

		assertTrue(!q1.equals(q2));

		assertTrue(q1 instanceof MultiPhraseQuery);
		assertTrue(q2 instanceof PhraseQuery);

		MultiPhraseQuery mq = (MultiPhraseQuery) q1;
		List<Term[]> ta = mq.getTermArrays();
		StringBuffer o = new StringBuffer();
		for (int i = 0; i < ta.size(); i++) {
			for (Term t : ta.get(i)) {
				o.append(t.toString());
				o.append(" ");
			}
			o.append("|");
		}
		assertTrue(o
				.toString()
				.equals("foo:velká foo:velká říjnová revoluce foo:XXX |foo:říjnová |foo:revoluce |"));

		assertTrue(q1.toString().equals(
				"foo:\"(velká velká říjnová revoluce XXX) říjnová revoluce\""));
		assertTrue(q2.toString().equals("foox:\"velká říjnová revoluce\""));

		Set<Term> terms = new HashSet<Term>();
		q1.extractTerms(terms);

		// extract only the 2nd (semantic) element
		q1 = qp1.parse("revoluce");

		terms = new HashSet<Term>();
		q1.extractTerms(terms);

		Term semQ = (Term) terms.toArray()[1];
		String sem = semQ.text();

		hits = ram.search(qp1.parse(sem), 10);
		assertTrue(hits.totalHits == 1);
		hits = ram.search(qp1.parse(semQ.toString()), 10);
		assertTrue(hits.totalHits == 1);
		hits = ram.search(qp2.parse(sem), 10);
		assertTrue(hits.totalHits == 0);

		ramdir.close();
	}

	private class TestSemanticAnalyzer extends Analyzer {

		public TestSemanticAnalyzer() {
		}


    @Override
    protected TokenStreamComponents createComponents(String fieldName,
        Reader reader) {
      if (fieldName.endsWith("x")) {
        return new TokenStreamComponents(new StandardTokenizer(TEST_VERSION_CURRENT, reader));
      }
      else {
        Tokenizer source = new StandardTokenizer(TEST_VERSION_CURRENT, reader);
        TokenStream result = new LowerCaseFilter(TEST_VERSION_CURRENT, source);
        CharArraySet stopWords = new CharArraySet(TEST_VERSION_CURRENT, 2, false);
        stopWords.add("s");
        stopWords.add("a");
        result = new StopFilter(TEST_VERSION_CURRENT, result, stopWords);
        result = new SemanticTaggerTokenFilter(result, tagger);
        return new TokenStreamComponents(source, result);
      }
      
    }

	}

	// Uniquely for Junit 3
	public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(
				TestSemanticTaggerIndexing.class);
	}

}
