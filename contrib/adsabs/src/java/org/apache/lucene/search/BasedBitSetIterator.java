package org.apache.lucene.search;

import org.apache.lucene.util.BitSet;

/**
 * Iterator which works with the knowledge of index segment boundaries.
 * It returns un-based document ids.
 * 
 * Internal bitset iterator works with bounded ranges. We remove/add 
 * bases before returning the results.
 * 
 */
public class BasedBitSetIterator extends DocIdSetIterator {
  private int base;
  private int doc = -1;
  private int upperBound;
  private BitSet bits;
  private int length;
  private long cost;
  
  public BasedBitSetIterator(BitSet bits, long cost, int base, int size) {
    if (cost < 0) {
      throw new IllegalArgumentException("cost must be >= 0, got " + cost);
    }
    this.bits = bits;
    this.length = bits.length();
    this.cost = cost;
    this.base = base;
    this.upperBound = base + size;
    if (upperBound > length)
      upperBound = length;
  }

  @Override
  public int docID() {
    return doc;
  }

  @Override
  public int nextDoc() {
    return advance(doc + 1);
  }

  @Override
  public int advance(int target) {
    if (target+base >= upperBound) {
      return doc = NO_MORE_DOCS;
    }
    doc = bits.nextSetBit(target+base);
    if (doc >= upperBound)
      return doc = NO_MORE_DOCS;
    return doc = doc - base;
  }

  @Override
  public long cost() {
    return cost;
  }

}