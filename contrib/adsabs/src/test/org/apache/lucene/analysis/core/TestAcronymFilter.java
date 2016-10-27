package org.apache.lucene.analysis.core;

import java.io.StringReader;
import java.util.HashMap;

import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.util.Version;
import org.apache.solr.analysis.AcronymTokenFilterFactory;

public class TestAcronymFilter extends BaseTokenStreamTestCase {
	
	  public void testReplace() throws Exception {
		    AcronymTokenFilterFactory factory = new AcronymTokenFilterFactory(new HashMap<String,String>() {{
          put("emitBoth", "false");
          put("prefix", "acr::");
          put("setType", "ACRONYM");
        }});
		    factory.setExplicitLuceneMatchVersion(true);
		    
		    TokenStream stream = factory.create(
		        whitespaceMockTokenizer(new StringReader("mit MIT"))
		        );
		    assertTokenStreamContents(stream, 
		    		new String[] { "mit", "acr::MIT" },
		    		new int[] { 1, 1 }
		    );
		    stream = factory.create(
		        whitespaceMockTokenizer(new StringReader("mit MIT"))
		    );
		    assertTokenStreamContents(stream, 
            new String[] { "mit", "acr::MIT" },
            new String[] { TypeAttribute.DEFAULT_TYPE, "ACRONYM" }
        );
		    
        
	  }
	  
	  
	  public void testAdd() throws Exception {
		    AcronymTokenFilterFactory factory = new AcronymTokenFilterFactory(new HashMap<String,String>() {{
          put("emitBoth", "true");
          put("prefix", "acr::");
          put("setType", "ACRONYM");
        }});
		    factory.setExplicitLuceneMatchVersion(true);
		    
		    TokenStream stream = factory.create(whitespaceMockTokenizer(new StringReader("M MIT")));
		    assertTokenStreamContents(stream, 
		    		new String[] { "M", "MIT", "acr::MIT" },
		    		new int[] { 1, 1, 0 }
		    );
		    stream = factory.create(whitespaceMockTokenizer(new StringReader("M MIT")));
		    assertTokenStreamContents(stream, 
            new String[] { "M", "MIT", "acr::MIT" },
            new String[] { TypeAttribute.DEFAULT_TYPE, TypeAttribute.DEFAULT_TYPE,  "ACRONYM" }
        );
	  }
}