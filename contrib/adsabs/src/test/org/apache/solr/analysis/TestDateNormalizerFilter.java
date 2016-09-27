package org.apache.solr.analysis;

import java.io.StringReader;
import java.util.HashMap;

import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public class TestDateNormalizerFilter extends BaseTokenStreamTestCase {
	
	public void test() throws Exception {
	  HashMap<String, String> config = new HashMap<String, String>();
	  config.put("format", "yyyy-MM-dd|yy-MM-dd|yy-MM");
		DateNormalizerTokenFilterFactory factory = new DateNormalizerTokenFilterFactory(config);
		
		TokenStream stream;
		stream = factory.create(whitespaceMockTokenizer(new StringReader("2014-12-00")));
    assertTokenStreamContents(stream, 
        new String[] {"2014-12-01"} 
        
    );
	}
}
