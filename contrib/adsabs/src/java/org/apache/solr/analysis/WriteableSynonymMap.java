package org.apache.solr.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WriteableSynonymMap {

  public static final Logger log = LoggerFactory.getLogger(WriteableSynonymMap.class);

  public abstract void add(String key, Set<String> values);
  public abstract void populateMap(List<String> rules);
  public abstract String formatEntry(String key, Set<String> values);

  protected Map<String, Set<String>> map;
  protected Map<String, Set<String>> regexMap;
  protected int numUpdates = 0;
  protected String outFile = null;

  public WriteableSynonymMap() {
    this.map = Collections.synchronizedMap(new HashMap<String, Set<String>>());
    this.regexMap = Collections.synchronizedMap(new HashMap<String, Set<String>>());
  }
  
  public void clear() {
    this.map = new HashMap<String, Set<String>>();
    this.regexMap = new HashMap<String, Set<String>>();
  }

  public void setOutput(String out) {
    this.outFile = out;
  }

  public Set<String> get(String k) {
    return this.map.get(k);
  }
  
  public void put(String k, Set<String> v) {
  	if (outFile == null)
  		return;
    numUpdates++;
    this.map.put(k, v);
  }

  /*
   * Tries hard to find all keys that match
   * the given pattern, the first call is expensive
   * subsequent calls will be relative fast.
   * However, avoid this method if you can!
   */
  public Set<String> get(Pattern p) {

    Set<String> synonyms = new HashSet<String>();
    String pKey = p.toString();
    if (regexMap.containsKey(pKey)) {
      if (regexMap.get(pKey) != null) {
        for (String k : regexMap.get(pKey)) {
          synonyms.addAll(this.map.get(k));
          synonyms.add(k);
        }
        return synonyms;
      }
      return null;
    }

    Set<String> matchedKeys = new HashSet<String>();
    for (String mapKey : this.map.keySet()) {
      Matcher m = p.matcher(mapKey);
      if (m.matches()) {
        matchedKeys.add(mapKey);
        synonyms.addAll(this.map.get(mapKey));
        synonyms.add(mapKey);
      }
    }

    if (synonyms.size() > 0) { 
      regexMap.put(pKey, matchedKeys);
      return synonyms;
    } else {
      regexMap.put(pKey, null);
      return null;
    }
  }


  public boolean containsKey(String key) {
    return this.map.containsKey(key);
  }

  public boolean isDirty() {
    if (numUpdates > 0) 
      return true;
    return false;
  }

  public boolean persist() throws IOException {
    return persist(false);
  }

  public boolean persist(boolean append) throws IOException {
    Writer writer = getWriter(append);
    if (writer == null) {
      //log.error("Cannot write synonyms, writer object is null.");
      return false;
    }

    writeSynonyms(map, writer);
    numUpdates = 0;
    writer.close();
    return true;
  }

  private Writer getWriter(boolean append) {
    if (outFile == null)
      return null;

    log.info("Creating new Writer for " + outFile);
    Writer w;

    Charset UTF_8 = Charset.forName("UTF-8");
    try {
      w = new OutputStreamWriter(new FileOutputStream(this.outFile, append), UTF_8);
      //w = new BufferedWriter(w);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } 
    return w;
  }

  public void writeSynonyms(Map<String, Set<String>> map, Writer writer) {
    StringBuffer out = new StringBuffer();
    int max = 1000;
    int i = 0;
    for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
      out.append(formatEntry(entry.getKey(), entry.getValue()));
      i++;
      if (i > max) {
        i = 0;
        write(writer, out.toString(), false);
        out = new StringBuffer();
      }
    }
    write(writer, out.toString(), true);
  }

  private void write(Writer writer, String out, boolean flush) {
    try {
      synchronized(writer) {
        writer.write(out);
        if (flush) writer.flush();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void finalize() {
    if (isDirty())
      try {
        this.persist();
      } catch (IOException e) {
        log.error(e.getLocalizedMessage());
      }
  }

  public static Set<String> splitValues(String synonyms) {
    Set<String> list = new LinkedHashSet<String>(); // preserve order
    for (String s : StrUtils.splitSmart(synonyms, ",", false)) {
      list.add(s.trim().replace("\\,", ",").replace("\\ ", " "));
    }
    return list;
  }

  /**
   * This is just a helper method, you should be using SolrResouceLoader#getLines()
   * instead
   * 
   * @param inputFile
   * @return
   * @throws IOException
   */
  public List<String> getLines(String inputFile) throws IOException {
    ArrayList<String> lines;
    BufferedReader input = null;
    try {
      input = new BufferedReader(new InputStreamReader(
          new FileInputStream(new File(inputFile)),
          Charset.forName("UTF-8")));

      lines = new ArrayList<String>();
      for (String word = null; (word = input.readLine()) != null;) {
        // skip initial bom marker
        if (lines.isEmpty() && word.length() > 0
            && word.charAt(0) == '\uFEFF')
          word = word.substring(1);
        // skip comments
        if (word.startsWith("#"))
          continue;
        word = word.trim();
        // skip blank lines
        if (word.length() == 0)
          continue;
        lines.add(word);
      }
    } catch (CharacterCodingException ex) {
      throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,
          "Error loading resource (wrong encoding?): " + inputFile,
          ex);
    } finally {
      if (input != null)
        input.close();
    }
    return lines;

  }

}