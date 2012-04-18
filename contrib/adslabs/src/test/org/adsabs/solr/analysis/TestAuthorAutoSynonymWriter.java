package org.adsabs.solr.analysis;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.lucene.analysis.KeywordTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.solr.analysis.BaseTokenTestCase;

public class TestAuthorAutoSynonymWriter extends BaseTokenTestCase {
	
	public void testWriteSynonyms() {
		// create the set of synonyms to write
		ArrayList<String> synonyms = new ArrayList<String>();
		synonyms.add("MUELLER, BILL");
		synonyms.add("MÜLLER, BILL");
		synonyms.add("MULLER, BILL");
		// byte array the result will be written to
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		// wrap the byte array w/ a buffered writer
		Writer w = new OutputStreamWriter(result, Charset.forName("UTF-8"));
		BufferedWriter writer = new BufferedWriter(w);
		// create the synonym writer for the test
	  	Tokenizer tokenizer = new KeywordTokenizer(new StringReader(""));
	    AuthorAutoSynonymWriterFactory factory = new AuthorAutoSynonymWriterFactory();
	    AuthorAutoSynonymWriter stream = (AuthorAutoSynonymWriter) factory.create(tokenizer);
	    // the string we expect to be written to the byte array
		String expected = "MUELLER\\,\\ BILL,MÜLLER\\,\\ BILL,MULLER\\,\\ BILL\n";
		// write the synonyms
		stream.writeSynonyms(synonyms, writer);
		// check the result
		assertEquals(expected, result.toString());
	}
	
	public void testSynonymWriter() throws Exception {
		// byte array the result will be written to
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		// wrap the byte array w/ a buffered writer
		Writer w = new OutputStreamWriter(result, Charset.forName("UTF-8"));
		BufferedWriter writer = new BufferedWriter(w);
		
		// create the input to the tokenizer
	  	Reader reader = new StringReader("Müller, Bruce");
	  	
	  	// create the synonym writer for the test
	  	Tokenizer tokenizer = new KeywordTokenizer(reader);
	    AuthorAutoSynonymWriterFactory factory = new AuthorAutoSynonymWriterFactory();
	    factory.setWriter(writer);
	    AuthorAutoSynonymWriter stream = (AuthorAutoSynonymWriter) factory.create(tokenizer);
	    
	    // check stream contents
	    assertTokenStreamContents(stream, new String[] { "Müller, Bruce" });	
	    
	    // check that correct synonyms were written to the result string
		HashSet<String> expected = new HashSet<String>();
		expected.add("Müller\\,\\ Bruce");
		expected.add("Muller\\,\\ Bruce");
		expected.add("Mueller\\,\\ Bruce");
		HashSet<String> actual = new HashSet<String>();
		System.out.println("got result: " + result.toString());
		for (String s : result.toString().trim().split("(?<!\\\\),")) {
			actual.add(s);
		}
		assertEquals(expected, actual);
	}
}
