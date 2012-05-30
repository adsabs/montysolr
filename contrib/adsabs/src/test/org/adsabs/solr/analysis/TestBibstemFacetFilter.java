package org.adsabs.solr.analysis;

import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.util.Version;
import org.apache.solr.analysis.BaseTokenTestCase;


public final class TestBibstemFacetFilter extends BaseTokenTestCase {

	public void testHasVolume() {
		assertTrue(BibstemFacetFilter.hasVolume("1992ApJ...389..510D"));
		assertTrue(BibstemFacetFilter.hasVolume("1999AJ....118.1131W"));
		assertTrue(BibstemFacetFilter.hasVolume("1993NYASA.688..184M"));
		assertFalse(BibstemFacetFilter.hasVolume("1991abe..conf.1276H"));
		assertFalse(BibstemFacetFilter.hasVolume("1989asme.proc..289C"));
	}
	
	public void testIsBibcode() {
		assertTrue(BibstemFacetFilter.isBibcode("1999AJ....118.1131W"));
		assertFalse(BibstemFacetFilter.isBibcode("foo bar"));
		assertFalse(BibstemFacetFilter.isBibcode("1234abe..conf.1276HH"));
	}
	
	public void testExtractBibstem() {
		assertEquals("abe..conf", BibstemFacetFilter.extractBibstem("1234abe..conf.1276H"));
		assertEquals("ApJ", BibstemFacetFilter.extractBibstem("1992ApJ...389..510D"));
	}
	
	  public void testBibcodeWithVolume() throws Exception {
		  	Reader reader = new StringReader("1992ApJ...389..510D 1999AJ....118.1131W 1993NYASA.688..184M 1991abe..conf.1276H 1989asme.proc..289C");
		  	Tokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_31, reader);
		    BibstemFacetFilterFactory factory = new BibstemFacetFilterFactory();
		    
		    TokenStream stream = factory.create(tokenizer);
		    assertTokenStreamContents(stream, new String[] { "ApJ", "AJ", "NYASA", "abe..conf", "asme.proc"} );
	  }
}
