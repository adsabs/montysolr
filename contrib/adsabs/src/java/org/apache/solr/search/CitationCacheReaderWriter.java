package org.apache.solr.search;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.lucene.store.DataInput;
import org.apache.lucene.store.OutputStreamDataOutput;

public class CitationCacheReaderWriter {
  private static String GENERATION = "citation_cache.gen";
  private static String DICTIONARY= "citation_cache.dict";
  private static String REFERENCES = "citation_cache.refs";
  private static String CITATIONS = "citation_cache.cit";
  private File targetDir;
  
  public CitationCacheReaderWriter(File targetDir) {
    this.targetDir = targetDir;
    
    if (! targetDir.exists())
      targetDir.mkdirs();
    
    assert targetDir.isDirectory();
    assert targetDir.canWrite();
  }
  
  public String getTargetDir() {
    return targetDir.toString();
  }
  
  @SuppressWarnings("unchecked")
  public void persist(CitationCache cache, long generation) throws IOException {
    
    OutputStreamDataOutput gs = getOutputStream(GENERATION);
    OutputStreamDataOutput ds = getOutputStream(DICTIONARY);
    OutputStreamDataOutput rs = getOutputStream(REFERENCES);
    OutputStreamDataOutput cs = getOutputStream(CITATIONS);
    
    // write number of entries - important for efficiently reading stuff back
    gs.writeLong(generation);
    gs.writeInt(cache.getHighestDocid());
    
    Iterator<int[][]> it = cache.getCitationGraph();
    int i = 0;
    int[] citations;
    int[] references;
    while (it.hasNext()) {
      int[][] data = it.next();
      references = data[0];
      citations = data[1];
      
      if (references != null && references.length > 0) {
        for (int r: references)
          rs.writeInt(r);
      }
      
      if (citations != null && citations.length > 0) {
        for (int c: citations)
          cs.writeInt(c);
      }
      
      rs.writeByte((byte) '\n');
      cs.writeByte((byte) '\n');
      i += 1;
    }
    
    gs.writeInt(i);
    gs.close();
    
    
    // memory hog, TODO: make it more efficient?
    Map<Integer, Set<String>> out = new HashMap<Integer, Set<String>>(i);
    Entry<String, Integer> en;
    String k;
    Integer v;
    Integer maxV = 0;
    Iterator<Entry<String, Integer>> di = cache.getDictionary();
    while (di.hasNext()) {
      en = di.next();
      k = en.getKey();
      v = en.getValue();
      
      if (v > maxV)
    	  maxV = v;
      
      if (out.get(v) == null) {
        HashSet<String> x = new HashSet<String>();
        x.add(k);
        out.put(v, x);
      }
      else {
        out.get(v).add(k);
      }
    }
    
    HashSet<String> empty = new HashSet<String>();
    for ( i=0; i<=cache.getHighestDocid(); i++) {
      if (out.containsKey(i)) {
        ds.writeSetOfStrings(out.get(i));
      }
      else {
        ds.writeSetOfStrings(empty);
      }
    }
    
    gs.close();
    ds.close();
    cs.close();
    rs.close();
  }
  
  /**
   * Load/unpack data from the files stored on disk.
   * @throws IOException - when we fail loading
   */
  @SuppressWarnings("unchecked")
  public void load(CitationCache cache) throws IOException {
    Metadata m = loadMetadata();
    
    cache.initializeCitationCache(m.maxDocs+1);
    
    BufferedDataInput ci = openInputStream(CITATIONS);
    BufferedDataInput ri = openInputStream(REFERENCES);
    BufferedDataInput di = openInputStream(DICTIONARY);
    
    Set<String> s;
    int i = 0;
    while (i <= m.maxDocs && (s = di.readSetOfStrings()) != null) {
      if (s.size() > 0) {
        for (String k: s)
          cache.put(k, i);
      }
      i += 1;
    }
    di.close();
    
    int j = 0;
    while (j <= m.maxDocs) {
      while (ci.peek() != '\n') {
        cache.insertCitation(j, ci.readInt());
      }
      while (ri.peek() != '\n') {
        cache.insertReference(j, ri.readInt());
      }
      ci.readByte();
      ri.readByte();
      j += 1;
    }
    ci.close();
    ri.close();
    
  }

  private OutputStreamDataOutput getOutputStream(String fname) throws FileNotFoundException {
    File f = new File(targetDir + File.separator + fname);
    if (f.exists())
      f.delete();
    return new OutputStreamDataOutput(new FileOutputStream(f));
  }
  
  
  public static long getIndexGeneration(SolrIndexSearcher searcher) {
    long generation = -1;
    try {
      generation = searcher.getIndexReader().getIndexCommit().getGeneration();
    } catch (IOException e) {
      // pass
    }
    return generation;
  }
  
	public static long getCacheGeneration(File cacheDir) {
		long generation = -2;
		File f = new File(cacheDir.getAbsolutePath() + File.separator + GENERATION);

		if (f.exists()) {
			try {
				BufferedDataInput gi = new BufferedDataInput(new FileInputStream(f));
				generation = gi.readLong();
				gi.close();
			} catch (IOException e) {
				// NOOP
			}
		}
		return generation;
	}
  
  private BufferedDataInput openInputStream(String fname) throws IOException {
    File f = new File(targetDir + File.separator + fname);
    if (!f.exists())
      throw new IOException(f.getAbsolutePath() + " does not exist");
    return new BufferedDataInput(new FileInputStream(f));
  }
  
  public Metadata loadMetadata() throws IOException {
    File f = new File(targetDir + File.separator + GENERATION);
    long generation = -1;
    int maxDocs = 0;
    
    if (f.exists()) {
      BufferedDataInput gi = openInputStream(GENERATION);
      generation = gi.readLong();
      maxDocs = gi.readInt();
    }       
    else { // generation was not sync'ed - we can go the hard route
      f = new File(targetDir + File.separator + REFERENCES);
      
      if (!f.exists())
        f = new File(targetDir + File.separator + CITATIONS);
        if (! f.exists())
          throw new IOException("Cannot find .gen or .cit or .ref input files");
       
      BufferedInputStream tmp = new BufferedInputStream(new FileInputStream(f));
      int x;
      while ((x = tmp.read()) != -1) {
        if (x == '\n')
          maxDocs += 1;
      }
      
      tmp.close();
    }
    Metadata g = new Metadata();
    g.generation = generation;
    g.maxDocs = maxDocs;
    return g;
  }
  
  public static class BufferedDataInput extends DataInput {
    
    private FileInputStream is;

    public BufferedDataInput(FileInputStream is) {
      this.is = is;
    }
    @Override
    public byte readByte() throws IOException {
      return (byte) is.read();
    }

    @Override
    public void readBytes(byte[] b, int offset, int len) throws IOException {
      is.read(b, offset, len);
    }
    
    public void close() throws IOException {
      is.close();
    }
    
    public int peek() throws IOException {
      long p = is.getChannel().position();
      int b = is.read();
      is.getChannel().position(p);
      return b;
    }
    
  }
  
  public static class Metadata {
    public long generation = -1;
    public int maxDocs = 0;
  }
  
}


