package org.apache.solr.analysis;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;


import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.pattern.PatternTokenizer;
import org.apache.solr.analysis.author.AuthorTransliterationsCollectorFactory;
import org.apache.solr.analysis.author.AuthorTransliterationsCollectorFilter;
import org.apache.solr.analysis.author.AuthorNormalizeFilterFactory;
import org.apache.solr.analysis.author.AuthorTransliterationFactory;

public class TestWriteableSynonymMap extends BaseTokenStreamTestCase {

  public void testMap() throws IOException, InterruptedException {
    // create the set of synonyms to write
    Set<String> expected = new HashSet<String>();
    //expected.add("MÜLLER, BILL");
    expected.add("MUELLER, BILL");
    expected.add("MULLER, BILL");

    // create the synonym writer for the test MÜLLER, BILL 
    TokenStream stream = new PatternTokenizer(new StringReader("MÜLLER, BILL"), Pattern.compile(";"), -1);

    AuthorNormalizeFilterFactory normFactory = new AuthorNormalizeFilterFactory();
    AuthorTransliterationsCollectorFactory collectorFactory = new AuthorTransliterationsCollectorFactory();
    AuthorTransliterationFactory transliteratorFactory = new AuthorTransliterationFactory();

    File tmpFile = File.createTempFile("variants", ".tmp");

    collectorFactory.setSynonymMap(new WriteableExplicitSynonymMap(null));
    AuthorTransliterationsCollectorFilter filter = collectorFactory.create(transliteratorFactory.create(normFactory.create(stream)));

    filter.reset();
    while (filter.incrementToken() != false) {
      //pass
    }

    // the string we expect to be written to the byte array
    // XXX: ??? why this is expected for AuthorAutoSynonymWriterFactory?
    // makes little sense to me
    // String expected = "MUELLER\\,\\ BILL,MÜLLER\\,\\ BILL,MULLER\\,\\ BILL\n";

    WriteableSynonymMap synMap = collectorFactory.getSynonymMap();
    synMap.setOutput(tmpFile.getAbsolutePath());

    assertTrue(synMap.containsKey("MÜLLER, BILL"));

    // check the result
    assertEquals(expected, synMap.get("MÜLLER, BILL"));


    // call it again
    // the tokens should be normalized by another filter
    stream = new PatternTokenizer(new StringReader("MÜller, Bill"), Pattern.compile(";"), -1);
    filter = collectorFactory.create(transliteratorFactory.create(normFactory.create(stream)));
    filter.reset();

    synMap.clear();
    assertFalse(synMap.containsKey("MÜLLER, BILL"));

    while (filter.incrementToken() != false) {
      //pass
    }

    assertTrue(synMap.containsKey("MÜLLER, BILL"));
    assertFalse(synMap.containsKey("MÜller, Bill"));
    assertEquals(expected, synMap.get("MÜLLER, BILL"));


    // now test the map is correctly written to disk

    synMap.persist();
    checkOutput(tmpFile, "MÜLLER\\,\\ BILL=>MUELLER\\,\\ BILL,MULLER\\,\\ BILL,\n");

    // now simulate the map was updated and we are closing, it should save itself
    synMap.put("M\u00dcLLER, BILL", synMap.get("M\u00dcLLER, BILL"));
    tmpFile.delete();
    assertFalse(tmpFile.canRead());
    synMap = null;
    collectorFactory = null;
    filter = null;
    System.gc();
    Thread.sleep(30);

    checkOutput(tmpFile, "MÜLLER\\,\\ BILL=>MUELLER\\,\\ BILL,MULLER\\,\\ BILL,\n");

    // now load the factory and check the synonyms were loaded properly
    collectorFactory = new AuthorTransliterationsCollectorFactory();
    collectorFactory.setSynonymMap(new WriteableExplicitSynonymMap(null));
    synMap = collectorFactory.getSynonymMap();
    synMap.clear();
    synMap.parseRules(synMap.getLines(tmpFile.getAbsolutePath()));


    assertTrue(synMap.containsKey("MÜLLER, BILL"));
    assertEquals(expected, synMap.get("MÜLLER, BILL"));

  }

  public void testEquivalentMap() throws IOException, InterruptedException {

    File tmpFile = File.createTempFile("variants", ".tmp");

    // create a synonym map
    WriteableSynonymMap synMap = new WriteableEquivalentSynonymMap(tmpFile.getAbsolutePath());

    // add some synonyms
    synMap.put("Foo, Bär", new HashSet<String>() {{ add("Foo, Bar"); add("Foo, Baer"); }});
    synMap.persist();

    String expected = "Foo\\,\\ Bar,Foo\\,\\ Baer,Foo\\,\\ Bär,\n"
      + "Foo\\,\\ Baer,Foo\\,\\ Bar,Foo\\,\\ Bär,\n"
      + "Foo\\,\\ Bär,Foo\\,\\ Bar,Foo\\,\\ Baer,\n";

    checkOutput(tmpFile, expected);

  }

  private void checkOutput(File tmpFile, String expected) throws IOException {

    FileInputStream in = new FileInputStream(tmpFile);
    BufferedReader fi = new BufferedReader(new InputStreamReader(new DataInputStream(in), "UTF-8"));
    StringBuffer out = new StringBuffer();
    String strLine;
    while ((strLine = fi.readLine()) != null)   {
      out.append(strLine);
      out.append("\n");
    }

    assertEquals(expected, out.toString());
  }

}
