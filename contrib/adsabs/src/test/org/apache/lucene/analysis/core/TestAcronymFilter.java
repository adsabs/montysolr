package org.apache.lucene.analysis.core;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.solr.analysis.AcronymTokenFilterFactory;

public class TestAcronymFilter extends BaseTokenStreamTestCase {
	
	  public void testIndexing() throws Exception {
		  	Reader reader = new StringReader("mit MIT");
		  	Tokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_40, reader);
		    AcronymTokenFilterFactory factory = new AcronymTokenFilterFactory();
		    factory.setLuceneMatchVersion(TEST_VERSION_CURRENT);
		    factory.init(new HashMap<String,String>() {{
		    	put("emitBoth", "true");
		    	put("lowercaseAcronyms", "true");
		    }});
		    
		    TokenStream stream = factory.create(tokenizer);
		    assertTokenStreamContents(stream, 
		    		new String[] { "mit", "mit", "MIT" },
		    		new int[] { 1, 1, 0 }
		    );
	  }
	  
	  public void testQuerying() throws Exception {
		  	Reader reader = new StringReader("MIT");
		  	Tokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_40, reader);
		    AcronymTokenFilterFactory factory = new AcronymTokenFilterFactory();
		    factory.setLuceneMatchVersion(TEST_VERSION_CURRENT);
		    factory.init(new HashMap<String,String>() {{
		    	put("emitBoth", "false");
		    	put("lowercaseAcronyms", "false");
		    }});
		    
		    TokenStream stream = factory.create(tokenizer);
		    assertTokenStreamContents(stream, new String[] { "MIT" } );
	  }
	  
	  public void testAcronymLength() throws Exception {
		  	Reader reader = new StringReader("M MIT");
		  	Tokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_40, reader);
		    AcronymTokenFilterFactory factory = new AcronymTokenFilterFactory();
		    factory.setLuceneMatchVersion(TEST_VERSION_CURRENT);
		    factory.init(new HashMap<String,String>() {{
		    	put("emitBoth", "true");
		    	put("lowercaseAcronyms", "true");
		    }});
		    
		    TokenStream stream = factory.create(tokenizer);
		    assertTokenStreamContents(stream, 
		    		new String[] { "m", "mit", "MIT" },
		    		new int[] { 1, 1, 0 }
		    );
	  }
}