package org.adsabs.solr.analysis;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.util.Version;
import org.apache.solr.analysis.BaseTokenTestCase;

public class TestAcronymFilter extends BaseTokenTestCase {
	
	  public void testIndexing() throws Exception {
		  	Reader reader = new StringReader("mit MIT");
		  	Tokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_31, reader);
		    AcronymTokenFilterFactory factory = new AcronymTokenFilterFactory();
		    factory.init(new HashMap<String,String>() {{
		    	put("luceneMatchVersion", "LUCENE_31");
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
		  	Tokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_31, reader);
		    AcronymTokenFilterFactory factory = new AcronymTokenFilterFactory();
		    factory.init(new HashMap<String,String>() {{
		    	put("luceneMatchVersion", "LUCENE_31");
		    	put("emitBoth", "false");
		    	put("lowercaseAcronyms", "false");
		    }});
		    
		    TokenStream stream = factory.create(tokenizer);
		    assertTokenStreamContents(stream, new String[] { "MIT" } );
	  }
	  
	  public void testAcronymLength() throws Exception {
		  	Reader reader = new StringReader("M MIT");
		  	Tokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_31, reader);
		    AcronymTokenFilterFactory factory = new AcronymTokenFilterFactory();
		    factory.init(new HashMap<String,String>() {{
		    	put("luceneMatchVersion", "LUCENE_31");
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