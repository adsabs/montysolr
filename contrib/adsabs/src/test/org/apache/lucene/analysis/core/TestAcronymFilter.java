package org.apache.lucene.analysis.core;

import java.io.StringReader;
import java.util.HashMap;

import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.solr.analysis.AcronymTokenFilterFactory;

public class TestAcronymFilter extends BaseTokenStreamTestCase {
	
	  public void testReplace() throws Exception {
		    AcronymTokenFilterFactory factory = new AcronymTokenFilterFactory();
		    factory.setLuceneMatchVersion(TEST_VERSION_CURRENT);
		    factory.init(new HashMap<String,String>() {{
		    	put("emitBoth", "false");
		    }});
		    
		    TokenStream stream = factory.create(new MockTokenizer(new StringReader("mit MIT"), MockTokenizer.WHITESPACE, false));
		    assertTokenStreamContents(stream, 
		    		new String[] { "mit", "acr::MIT" },
		    		new int[] { 1, 1 }
		    );
		    stream = factory.create(new MockTokenizer(new StringReader("mit MIT"), MockTokenizer.WHITESPACE, false));
		    assertTokenStreamContents(stream, 
            new String[] { "mit", "acr::MIT" },
            new String[] { TypeAttribute.DEFAULT_TYPE, AcronymTokenFilter.TOKEN_TYPE_ACRONYM }
        );
		    
        
	  }
	  
	  
	  public void testAdd() throws Exception {
		    AcronymTokenFilterFactory factory = new AcronymTokenFilterFactory();
		    factory.setLuceneMatchVersion(TEST_VERSION_CURRENT);
		    factory.init(new HashMap<String,String>() {{
		    	put("emitBoth", "true");
		    }});
		    
		    TokenStream stream = factory.create(new MockTokenizer(new StringReader("M MIT"), MockTokenizer.WHITESPACE, false));
		    assertTokenStreamContents(stream, 
		    		new String[] { "M", "MIT", "acr::MIT" },
		    		new int[] { 1, 1, 0 }
		    );
		    stream = factory.create(new MockTokenizer(new StringReader("M MIT"), MockTokenizer.WHITESPACE, false));
		    assertTokenStreamContents(stream, 
            new String[] { "M", "MIT", "acr::MIT" },
            new String[] { TypeAttribute.DEFAULT_TYPE, TypeAttribute.DEFAULT_TYPE,  AcronymTokenFilter.TOKEN_TYPE_ACRONYM }
        );
	  }
}