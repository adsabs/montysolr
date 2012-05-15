package org.adsabs.solr.analysis;

import java.io.Reader;
import java.io.StringReader;
import org.apache.lucene.analysis.KeywordTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenTestCase;

public class TestAuthorNameVariantsFilterFactory extends BaseTokenTestCase {
	public void testAuthorSynonyms() throws Exception {
		Reader reader = new StringReader("Müller, Bill");
		Tokenizer tokenizer = new KeywordTokenizer(reader);
		AuthorNameVariantsFilterFactory factory = new AuthorNameVariantsFilterFactory();
		TokenStream stream = factory.create(tokenizer);
		String[] expected = { "Müller, Bill", "Mueller, Bill", "Muller, Bill" };
		assertTokenStreamContents(stream, expected);
	}
}
