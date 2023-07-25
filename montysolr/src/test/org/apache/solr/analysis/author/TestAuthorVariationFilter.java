package org.apache.solr.analysis.author;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.solr.analysis.author.AuthorQueryVariationsFilterFactory;

public class TestAuthorVariationFilter extends BaseTokenStreamTestCase {
	public void testAuthorVariations() throws Exception {
		Reader reader = new StringReader("GOMEZ, HECTOR");
		Tokenizer tokenizer = new KeywordTokenizer();
		tokenizer.setReader(reader);
		AuthorQueryVariationsFilterFactory factory = new AuthorQueryVariationsFilterFactory(new HashMap<String,String>());
		TokenStream stream = factory.create(tokenizer);
		String[] expected = { "GOMEZ, HECTOR", "GOMEZ,", "GOMEZ, HECTOR\\b.*", "GOMEZ, H", "GOMEZ, H\\b.*",  };
		assertTokenStreamContents(stream, expected);
	}
}
