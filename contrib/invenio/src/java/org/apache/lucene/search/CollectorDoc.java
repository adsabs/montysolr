package org.apache.lucene.search;

public class CollectorDoc extends ScoreDoc {
  
  public float freq = 1.0f;
  
  public CollectorDoc(int doc, float score) {
    super(doc, score, -1);
  }
  
  public CollectorDoc(int doc, float score, int shardIndex, float freq) {
    super(doc, score, shardIndex);
    this.freq = freq;
  }
  
}
