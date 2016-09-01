package org.apache.solr.analysis;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public class TestASCIIDuplicatingFilter extends BaseTokenStreamTestCase {
	
	public void test() throws Exception {
		ASCIIDuplicatingFilterFactory factory = new ASCIIDuplicatingFilterFactory(new HashMap<String,String>());
		
		TokenStream stream = factory.create(whitespaceMockTokenizer(new StringReader("čtyřista čtyřicet čtyři")));
		String[] expected = new String[] { "čtyřista", "ctyrista", "čtyřicet", "ctyricet", "čtyři", "ctyri" };
		int[] increments = new int[] {1, 0, 1, 0, 1, 0};
		String W = TypeAttribute.DEFAULT_TYPE;
		String D = OnChangeDuplicatingFilter.DUPLICATE;
		String[] types = new String[] { W, D, W, D, W, D};
		assertTokenStreamContents(stream, expected, increments);
		
		stream = factory.create(whitespaceMockTokenizer(new StringReader("čtyřista čtyřicet čtyři")));
		assertTokenStreamContents(stream, expected, types);
		
		
		
		// test it doesnt interfere
		stream = factory.create(whitespaceMockTokenizer(new StringReader("Cyril Methood")));
    assertTokenStreamContents(stream, 
        new String[] {"cyril", "methood"}, 
        new int[] {1, 1});
	}
}
