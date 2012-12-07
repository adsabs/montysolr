package org.apache.solr.analysis.author;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.pattern.PatternTokenizer;
import org.apache.lucene.analysis.util.ClasspathResourceLoader;
import org.apache.solr.analysis.WriteableSynonymMap;

public class TestAuthorCollectorFactory extends BaseTokenStreamTestCase {
  
  public void testCollector() throws IOException, InterruptedException {
    
    AuthorCollectorFactory factory = new AuthorCollectorFactory();
    
    File tmpFile = File.createTempFile("variants", ".tmp");
    Map<String,String> args = new HashMap<String,String>();
    args.put("outFile", tmpFile.getAbsolutePath());
    args.put("tokenTypes", AuthorUtils.AUTHOR_TRANSLITERATED);
    args.put("emitTokens", "false");
    
    factory.setLuceneMatchVersion(TEST_VERSION_CURRENT);
    factory.init(args);
    factory.inform(new ClasspathResourceLoader(getClass()));

    AuthorNormalizeFilterFactory normFactory = new AuthorNormalizeFilterFactory();
    AuthorTransliterationFactory transliteratorFactory = new AuthorTransliterationFactory();
    
    //create the synonym writer for the test MÜLLER, BILL 
    TokenStream stream = new PatternTokenizer(new StringReader("MÜLLER, BILL"), Pattern.compile(";"), -1);
    TokenStream ts = factory.create(transliteratorFactory.create(normFactory.create(stream)));
    assertTrue(ts instanceof AuthorCollectorFilter);
    ts.reset();
    while (ts.incrementToken() != false) {
      //pass
    }
    ts.reset();

    WriteableSynonymMap synMap = factory.getSynonymMap();
    assertTrue(synMap.containsKey("MULLER, BILL"));
    assertTrue(synMap.containsKey("MUELLER, BILL"));
    assertSame(synMap.get("MUELLER, BILL"), synMap.get("MULLER, BILL"));
    
    assertFalse(synMap.containsKey("MÜLLER, BILL"));
    assertFalse(synMap.containsKey("MÜLLER, B"));
    assertFalse(synMap.containsKey("MULLER, B"));
    assertFalse(synMap.containsKey("MUELLER, B"));
    assertFalse(synMap.containsKey("MÜLLER,"));
    assertFalse(synMap.containsKey("MUELLER,"));
    
    
    synMap.persist();
    checkOutput(tmpFile, 
        "MULLER\\,\\ BILL=>MÜLLER\\,\\ BILL",
        "MULLER\\,\\ B=>MÜLLER\\,\\ B",
        "MUELLER\\,\\ BILL=>MÜLLER\\,\\ BILL",
        "MUELLER\\,\\ B=>MÜLLER\\,\\ B",
        "MUELLER\\,=>MÜLLER\\,",
        "MULLER\\,=>MÜLLER\\,"
        );
    

    // call it again
    // the tokens should not be normalized by our filter
    stream = new PatternTokenizer(new StringReader("MÜller, Bill"), Pattern.compile(";"), -1);
    ts = factory.create(transliteratorFactory.create(normFactory.create(stream)));
    

    synMap.clear();
    assertFalse(synMap.containsKey("MULLER, BILL"));

    ts.reset();
    while (ts.incrementToken() != false) {
      //pass
    }
    ts.reset();

    assertFalse(synMap.containsKey("MÜLLER, BILL"));
    assertFalse(synMap.containsKey("MÜller, Bill"));
    assertTrue(synMap.containsKey("MUEller, Bill"));
    assertTrue(synMap.containsKey("MUller, Bill"));
    
    
    //clean and redo
    synMap.clear();
    stream = new PatternTokenizer(new StringReader("MÜLLER, BILL;MÜLLER, WILLIAM BILL;ASCII, NAME"), Pattern.compile(";"), -1);
    ts = factory.create(transliteratorFactory.create(normFactory.create(stream)));
    ts.reset();
    while (ts.incrementToken() != false) {
      //pass
    }

    // now test the map is correctly written to disk
    synMap.persist();
    checkOutput(tmpFile, 
        "MULLER\\,\\ BILL=>MÜLLER\\,\\ BILL",
        "MULLER\\,\\ B=>MÜLLER\\,\\ B",
        "MUELLER\\,\\ BILL=>MÜLLER\\,\\ BILL",
        "MUELLER\\,\\ B=>MÜLLER\\,\\ B",
        "MUELLER\\,=>MÜLLER\\,",
        "MULLER\\,=>MÜLLER\\,",
        "MUELLER\\,\\ WILLIAM=>MÜLLER\\,\\ WILLIAM",
        "MULLER\\,\\ WILLIAM=>MÜLLER\\,\\ WILLIAM",
        "MUELLER\\,\\ WILLIAM\\ BILL=>MÜLLER\\,\\ WILLIAM\\ BILL",
        "MUELLER\\,\\ WILLIAM\\ B=>MÜLLER\\,\\ WILLIAM\\ B",
        "MUELLER\\,\\ W\\ B=>MÜLLER\\,\\ W\\ B",
        "MULLER\\,\\ WILLIAM\\ BILL=>MÜLLER\\,\\ WILLIAM\\ BILL",
        "MULLER\\,\\ WILLIAM\\ B=>MÜLLER\\,\\ WILLIAM\\ B",
        "MULLER\\,\\ W\\ B=>MÜLLER\\,\\ W\\ B",
        "MULLER\\,\\ WILLIAM=>MÜLLER\\,\\ WILLIAM",
        "!ASCII"
        );

    
    // now simulate the map was updated and we are closing, it should save itself
    synMap.put("MULLER, BILL", synMap.get("MULLER, BILL"));
    tmpFile.delete();
    
    /* works not since the new refactoring
    tmpFile.delete();
    assertFalse(tmpFile.canRead());
    ts = null;
    synMap = null;
    factory = null;
    System.gc();
    */
    
    // trick the filter into persisting itself
    ts.reset();
    ts.reset();
    ts.reset();
    
    checkOutput(tmpFile, 
        "MULLER\\,\\ BILL=>MÜLLER\\,\\ BILL",
        "MULLER\\,\\ B=>MÜLLER\\,\\ B",
        "MUELLER\\,\\ BILL=>MÜLLER\\,\\ BILL",
        "MUELLER\\,\\ B=>MÜLLER\\,\\ B",
        "MUELLER\\,=>MÜLLER\\,",
        "MULLER\\,=>MÜLLER\\,"
        );
    
    // now load the factory and check the synonyms were loaded properly
    //factory = new AuthorCollectorFactory();
    //args.put("synonyms", tmpFile.getAbsolutePath());
    //factory.init(args);
    //factory.inform(new ClasspathResourceLoader(getClass()));
    
    synMap = factory.getSynonymMap();
    synMap.populateMap(Arrays.asList(readFile(tmpFile).split("\n")));
    assertTrue(synMap.containsKey("MULLER, WILLIAM BILL"));
    assertTrue(synMap.containsKey("MULLER, WILLIAM"));
    assertTrue(synMap.containsKey("MULLER, BILL"));
    assertTrue(synMap.containsKey("MUELLER, BILL"));
    assertTrue(synMap.containsKey("MUELLER, B"));
    assertTrue(synMap.containsKey("MUELLER, B"));
    assertEquals(synMap.get("MULLER, BILL"), new LinkedHashSet(){{add("MÜLLER, BILL");}});
    assertEquals(synMap.get("MUELLER, BILL"), new LinkedHashSet(){{add("MÜLLER, BILL");}});
    assertEquals(synMap.get("MULLER,"), new LinkedHashSet(){{add("MÜLLER,");}});
    
    assertSame(synMap.get("MUELLER, BILL"), synMap.get("MULLER, BILL"));
    assertSame(synMap.get("MUELLER,"), synMap.get("MUELLER,"));
  }
  
  private void checkOutput(File tmpFile, String... expected) throws IOException {
    String fc = readFile(tmpFile);
    for (String t: expected) {
      if (t.substring(0,1).equals("!")) {
        assertFalse("Present: " + t, fc.contains(t.substring(1)));
      }
      else {
        assertTrue("Missing: " + t, fc.contains(t));
      }
    }
  }
  private String readFile(File tmpFile) throws IOException {
    FileInputStream in = new FileInputStream(tmpFile);
    BufferedReader fi = new BufferedReader(new InputStreamReader(new DataInputStream(in), "UTF-8"));
    StringBuffer out = new StringBuffer();
    String strLine;
    while ((strLine = fi.readLine()) != null)   {
      out.append(strLine);
      out.append("\n");
    }
    return out.toString();
  }

}
