package org.apache.solr.analysis.author;

import org.apache.lucene.tests.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

public class TestAuthorVariationFilter extends BaseTokenStreamTestCase {
    public void testAuthorVariations() throws Exception {
        Reader reader = new StringReader("GOMEZ, HECTOR");
        Tokenizer tokenizer = new KeywordTokenizer();
        tokenizer.setReader(reader);
        AuthorQueryVariationsFilterFactory factory = new AuthorQueryVariationsFilterFactory(new HashMap<String, String>());
        TokenStream stream = factory.create(tokenizer);
        String[] expected = {"GOMEZ, HECTOR", "GOMEZ,", "GOMEZ, HECTOR\\b.*", "GOMEZ, H", "GOMEZ, H\\b.*",};
        assertTokenStreamContents(stream, expected);
    }
}
