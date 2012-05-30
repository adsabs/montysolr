package org.adsabs.solr.analysis;

import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.util.Version;
import org.apache.solr.analysis.BaseTokenTestCase;

public class TestPubdateSortFilter extends BaseTokenTestCase {
	
	  public void testFilter() throws Exception {
		  	Reader reader = new StringReader("2005-11-00 2010-00-00 1998-01-05");
		  	Tokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_31, reader);
		    PubdateSortFilterFactory factory = new PubdateSortFilterFactory();
		    
		    TokenStream stream = factory.create(tokenizer);
		    assertTokenStreamContents(stream, new String[] { "20051100", "20100000", "19980105"} );
	  }
	  
}
