package org.apache.solr.analysis;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.util.LuceneTestCase;

public class TestWriteableSynonymMap extends LuceneTestCase {

  
  public void testExplicitMap() throws IOException, InterruptedException {

    String expected = "Foo\\,\\ Bär=>Foo\\,\\ Bar,Foo\\,\\ Baer\n" +
    "Boo\\,\\ Bär=>Boo\\,\\ Bar,Boo\\,\\ Baer";
    
    File tmpFile = createTempFile(expected);
    
    // create a synonym map
    WriteableSynonymMap synMap = new WriteableExplicitSynonymMap();
    synMap.setOutput(tmpFile.getAbsolutePath());
    synMap.populateMap(synMap.getLines(tmpFile.getAbsolutePath()));
    
    
    
    assertTrue(synMap.containsKey("Foo, Bär"));
    assertTrue(!synMap.containsKey("Foo, Bar"));
    assertTrue(!synMap.containsKey("Foo, Baer"));
    assertTrue(synMap.get("Foo, Bär").contains("Foo, Bar"));
    assertTrue(synMap.get("Foo, Bär").contains("Foo, Baer"));

    assertTrue(synMap.containsKey("Boo, Bär"));
    assertTrue(!synMap.containsKey("Boo, Bar"));
    assertTrue(!synMap.containsKey("Boo, Baer"));
    assertTrue(synMap.get("Boo, Bär").contains("Boo, Bar"));
    assertTrue(synMap.get("Boo, Bär").contains("Boo, Baer"));
    
    
    synMap.persist();
    String fc = readFile(tmpFile);
    
    assertTrue(fc.contains("Foo\\,\\ Bär=>Foo\\,\\ Bar,Foo\\,\\ Baer\n"));
    assertTrue(fc.contains("Boo\\,\\ Bär=>Boo\\,\\ Bar,Boo\\,\\ Baer"));
    
    
    // add some synonyms
    synMap.add("Foo, Bär", new LinkedHashSet<String>() {{ add("Fuu, Bar"); add("Foo, Baer"); }});
    synMap.add("Fuu, Bar", new LinkedHashSet<String>() {{ add("Fuj, Taj");}});
    
    assertTrue(synMap.containsKey("Fuu, Bar"));
    assertTrue(synMap.get("Fuu, Bar").contains("Fuj, Taj"));
    assertTrue(!synMap.get("Foo, Bär").contains("Fuj, Taj"));
    assertTrue(synMap.get("Foo, Bär").contains("Fuu, Bar"));
    
    
    synMap.persist();
    fc = readFile(tmpFile);

    assertTrue(fc.contains("Foo\\,\\ Bär=>Foo\\,\\ Bar,Foo\\,\\ Baer,Fuu\\,\\ Bar\n"));
    assertTrue(fc.contains("Boo\\,\\ Bär=>Boo\\,\\ Bar,Boo\\,\\ Baer"));
    
    FileUtils.deleteQuietly(tmpFile);
  }
  
  public void testEquivalentMap() throws IOException, InterruptedException {

    String expected = "Foo\\,\\ Bär,Foo\\,\\ Bar,Foo\\,\\ Baer\n" +
    "Boo\\,\\ Bär,Boo\\,\\ Bar,Boo\\,\\ Baer";
    
    File tmpFile = createTempFile(expected);
    
    // create a synonym map
    WriteableSynonymMap synMap = new WriteableEquivalentSynonymMap();
    synMap.setOutput(tmpFile.getAbsolutePath());
    synMap.populateMap(synMap.getLines(tmpFile.getAbsolutePath()));
    
    
    
    // values are stored inside the same set
    assertTrue(synMap.containsKey("Foo, Bär"));
    assertEquals(synMap.get("Foo, Bär"), synMap.get("Foo, Bar"));
    assertEquals(synMap.get("Foo, Bär"), synMap.get("Foo, Baer"));
    assertSame(synMap.get("Foo, Bär"), synMap.get("Foo, Bar"));
    assertSame(synMap.get("Foo, Bär"), synMap.get("Foo, Baer"));
    assertSame(synMap.get("Foo, Bar"), synMap.get("Foo, Baer"));
    
    assertTrue(synMap.containsKey("Boo, Bär"));
    assertEquals(synMap.get("Boo, Bär"), synMap.get("Boo, Bar"));
    assertEquals(synMap.get("Boo, Bär"), synMap.get("Boo, Baer"));
    assertSame(synMap.get("Boo, Bär"), synMap.get("Boo, Bar"));
    assertSame(synMap.get("Boo, Bar"), synMap.get("Boo, Baer"));
    
    synMap.persist();
    String fc = readFile(tmpFile);
    
    assertTrue(fc.contains("Foo\\,\\ Bär,Foo\\,\\ Bar,Foo\\,\\ Baer\n"));
    assertTrue(fc.contains("Boo\\,\\ Bär,Boo\\,\\ Bar,Boo\\,\\ Baer"));
    
    
    // add some synonyms
    synMap.add("Foo, Bär", new HashSet<String>() {{ add("Fuu, Bar"); add("Foo, Baer"); }});
    
    assertTrue(synMap.containsKey("Fuu, Bar"));
    assertEquals(synMap.get("Fuu, Bar"), synMap.get("Foo, Bär"));
    assertEquals(synMap.get("Fuu, Bar"), synMap.get("Foo, Bar"));
    assertEquals(synMap.get("Fuu, Bar"), synMap.get("Foo, Baer"));
    assertSame(synMap.get("Fuu, Bar"), synMap.get("Foo, Bär"));
    assertSame(synMap.get("Fuu, Bar"), synMap.get("Foo, Bar"));
    assertSame(synMap.get("Fuu, Bar"), synMap.get("Foo, Baer"));
    
    synMap.persist();
    fc = readFile(tmpFile);

    assertTrue(fc.contains("Foo\\,\\ Bär,Foo\\,\\ Bar,Foo\\,\\ Baer,Fuu\\,\\ Bar\n"));
    assertTrue(fc.contains("Boo\\,\\ Bär,Boo\\,\\ Bar,Boo\\,\\ Baer"));
    
    FileUtils.deleteQuietly(tmpFile);

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
  private void checkOutput(File tmpFile, String expected) throws IOException {

    assertEquals(expected, readFile(tmpFile));
  }
  
  private File createTempFile(String...lines) throws IOException {
    File tmpFile = File.createTempFile("montySolr-unittest", null);
    if (lines.length > 0) {
      FileOutputStream fi = FileUtils.openOutputStream(tmpFile);
      StringBuffer out = new StringBuffer();
      for (String l: lines) {
        out.append(l + "\n");
      }
      FileUtils.writeStringToFile(tmpFile, out.toString(), "UTF-8");
    }
    return tmpFile;
  }

}
