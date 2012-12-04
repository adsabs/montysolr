package org.apache.solr.analysis.author;

import java.io.Reader;
import java.io.StringReader;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.solr.analysis.author.AuthorNormalizeFilterFactory;

public class TestAuthorNormalizeFilter extends BaseTokenStreamTestCase {

	  public void testUppercase() throws Exception {
		  	Reader reader = new StringReader("Gómez, Hector Q");
		  	Tokenizer tokenizer = new KeywordTokenizer(reader);
		    AuthorNormalizeFilterFactory factory = new AuthorNormalizeFilterFactory();
		    TokenStream stream = factory.create(tokenizer);
		    assertTokenStreamContents(stream, new String[] { "Gómez, Hector Q" });
	  }
	  public void testWhitespace() throws Exception {
		  	Reader reader = new StringReader("  Gómez,\n Hector    Q ");
		  	Tokenizer tokenizer = new KeywordTokenizer(reader);
		    AuthorNormalizeFilterFactory factory = new AuthorNormalizeFilterFactory();
		    TokenStream stream = factory.create(tokenizer);
		    assertTokenStreamContents(stream, new String[] { "Gómez, Hector Q" });
	  }
	  public void testPuncuation() throws Exception {
		  	Reader reader = new StringReader("%$Gómez_Foo, He-ctor;  29Q.");
		  	Tokenizer tokenizer = new KeywordTokenizer(reader);
		    AuthorNormalizeFilterFactory factory = new AuthorNormalizeFilterFactory();
		    TokenStream stream = factory.create(tokenizer);
		    assertTokenStreamContents(stream, new String[] { "Gómez_Foo, He-ctor 29Q" });
	  }
}
