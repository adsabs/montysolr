package org.apache.lucene.analysis.core;

import java.io.Reader;
import java.io.StringReader;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.solr.analysis.AuthorVariationFilterFactory;

public class TestAuthorVariationFilter extends BaseTokenStreamTestCase {
	public void testAuthorVariations() throws Exception {
		Reader reader = new StringReader("GOMEZ, HECTOR");
		Tokenizer tokenizer = new KeywordTokenizer(reader);
		AuthorVariationFilterFactory factory = new AuthorVariationFilterFactory();
		TokenStream stream = factory.create(tokenizer);
		String[] expected = { "GOMEZ, HECTOR", "GOMEZ, H\\b.*", "GOMEZ, HECTOR\\b.*", "GOMEZ,", "GOMEZ, H" };
		assertTokenStreamContents(stream, expected);
	}
}
