package org.apache.solr.analysis;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;


import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.pattern.PatternTokenizer;
import org.apache.solr.analysis.author.AuthorNameVariantsCollectorFactory;
import org.apache.solr.analysis.author.AuthorNameVariantsCollectorFilter;

public class TestWriteableSynonymMap extends BaseTokenStreamTestCase {
	
	public void dontTestMap() throws IOException, InterruptedException {
		// create the set of synonyms to write
		ArrayList<String> expected = new ArrayList<String>();
		//expected.add("MÜLLER, BILL");
		expected.add("MUELLER, BILL");
		expected.add("MULLER, BILL");

		// create the synonym writer for the test
	  	Tokenizer tokenizer = new PatternTokenizer(new StringReader("MÜLLER, BILL"), Pattern.compile(";"), -1);
	  	AuthorNameVariantsCollectorFactory factory = new AuthorNameVariantsCollectorFactory();
	  	
	  	File tmpFile = File.createTempFile("variants", ".tmp");
	  	
	  	
	    AuthorNameVariantsCollectorFilter filter = factory.create(tokenizer);
	    
	    while (filter.incrementToken() != false) {
	    	//pass
	    }
	    
	    // the string we expect to be written to the byte array
	    // XXX: ??? why this is expected for AuthorAutoSynonymWriterFactory?
	    // makes little sense to me
		// String expected = "MUELLER\\,\\ BILL,MÜLLER\\,\\ BILL,MULLER\\,\\ BILL\n";
		
		WriteableSynonymMap synMap = factory.getSynonymMap();
		synMap.setOutput(tmpFile.getAbsolutePath());
		
		assertTrue(synMap.containsKey("MÜLLER, BILL"));
		
		// check the result
		assertEquals(expected, synMap.get("MÜLLER, BILL"));
		
		
		// call it again
		// the tokens are normally normalized by another filter, so this should not happen
		tokenizer = new PatternTokenizer(new StringReader("MÜller, Bill"), Pattern.compile(";"), -1);
		filter = factory.create(tokenizer);
	    while (filter.incrementToken() != false) {
	    	//pass
	    }
	    
	    assertTrue(synMap.containsKey("MÜLLER, BILL"));
	    assertTrue(synMap.containsKey("MÜller, Bill"));
	    assertTrue(synMap.get("MÜLLER, BILL").size() == 2);
	    assertTrue(synMap.get("MÜller, Bill").size() == 2);
	    
	    
	    // now test the map is correctly written to disk
	    
	    synMap.persist();
	    checkOutput(tmpFile, "MÜller\\,\\ Bill=>MUEller\\,\\ Bill,MUller\\,\\ Bill,\nMÜLLER\\,\\ BILL=>MUELLER\\,\\ BILL,MULLER\\,\\ BILL,\n");
	    
	    // now simulate the map was updated and we are closing, it should save itself
	    synMap.put("MÜLLER, BILL", synMap.get("MÜLLER, BILL"));
	    tmpFile.delete();
	    assertFalse(tmpFile.canRead());
	    synMap = null;
	    factory = null;
	    filter = null;
	    System.gc();
	    Thread.sleep(30);
	    
	    checkOutput(tmpFile, "MÜller\\,\\ Bill=>MUEller\\,\\ Bill,MUller\\,\\ Bill,\nMÜLLER\\,\\ BILL=>MUELLER\\,\\ BILL,MULLER\\,\\ BILL,\n");
	    
	    // now load the factory and check the synonyms were loaded properly
	    factory = new AuthorNameVariantsCollectorFactory();
	    synMap = factory.getSynonymMap();
	    synMap.parseRules(synMap.getLines(tmpFile.getAbsolutePath()));
	    
	    
	    assertTrue(synMap.containsKey("MÜLLER, BILL"));
	    assertTrue(synMap.containsKey("MÜller, Bill"));
	    assertEquals(expected, synMap.get("MÜLLER, BILL"));
	    
	}
	
	private void checkOutput(File tmpFile, String expected) throws IOException {
		
		FileInputStream in = new FileInputStream(tmpFile);
	    BufferedReader fi = new BufferedReader(new InputStreamReader(new DataInputStream(in)));
	    StringBuffer out = new StringBuffer();
	    String strLine;
		while ((strLine = fi.readLine()) != null)   {
			out.append(strLine);
			out.append("\n");
		}
	    
	    assertEquals(expected, out.toString());
	}
	
}
