package org.adsabs.solr.analysis;

import java.io.Reader;
import java.io.StringReader;
import org.apache.lucene.analysis.KeywordTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenTestCase;

public class TestAuthorVariationFilter extends BaseTokenTestCase {
	public void testAuthorVariations() throws Exception {
		Reader reader = new StringReader("GOMEZ, HECTOR");
		Tokenizer tokenizer = new KeywordTokenizer(reader);
		AuthorVariationFilterFactory factory = new AuthorVariationFilterFactory();
		TokenStream stream = factory.create(tokenizer);
		String[] expected = { "GOMEZ, HECTOR", "GOMEZ, H\\b.*", "GOMEZ, HECTOR\\b.*", "GOMEZ,", "GOMEZ, H" };
		assertTokenStreamContents(stream, expected);
	}
}
