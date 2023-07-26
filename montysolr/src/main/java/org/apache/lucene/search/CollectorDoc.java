package org.apache.lucene.search;

public class CollectorDoc extends ScoreDoc {
  
  public CollectorDoc(int doc, float score) {
    super(doc, score, -1);
  }
  
  public CollectorDoc(int doc, float score, int shardIndex) {
    super(doc, score, shardIndex);
  }
  
}
