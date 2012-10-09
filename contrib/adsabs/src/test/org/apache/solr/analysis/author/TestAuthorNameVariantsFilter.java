package org.apache.solr.analysis.author;

import java.io.Reader;
import java.io.StringReader;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.solr.analysis.author.AuthorNameVariantsCollectorFactory;


public class TestAuthorNameVariantsFilter extends BaseTokenStreamTestCase {
	public void testAuthorSynonyms() throws Exception {
		Reader reader = new StringReader("Müller, Bill");
		Tokenizer tokenizer = new KeywordTokenizer(reader);
		AuthorNameVariantsCollectorFactory factory = new AuthorNameVariantsCollectorFactory();
		TokenStream stream = factory.create(tokenizer);
		String[] expected = { "Müller, Bill", "Mueller, Bill", "Muller, Bill" };
		assertTokenStreamContents(stream, expected);
	}
}
