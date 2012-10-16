package org.apache.solr.analysis;

import java.io.Reader;
import java.io.StringReader;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.util.Version;

public class TestASCIIDuplicatingFilter extends BaseTokenStreamTestCase {
	
	public void test() throws Exception {
		ASCIIDuplicatingFilterFactory factory = new ASCIIDuplicatingFilterFactory();
		
		Reader reader = new StringReader("čtyřista čtyřicet čtyři");
		Tokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_50, reader);
		TokenStream stream = factory.create(tokenizer);
		String[] expected = { "čtyřista", "ctyrista", "čtyřicet", "ctyricet", "čtyři", "ctyri" };
		int[] increments = {1, 0, 1, 0, 1, 0};
		assertTokenStreamContents(stream, expected, increments);
	}
}
