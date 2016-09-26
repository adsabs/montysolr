package org.apache.lucene.search;

import org.apache.lucene.util.BitSet;
import org.apache.lucene.util.BitSetIterator;

/**
 * Iterator which works with the knowledge of index segment boundaries.
 * It returns un-based document ids.
 * 
 */
public class BasedBitSetIterator extends BitSetIterator {
  private int base;
  private int v;
  
  public BasedBitSetIterator(BitSet bits, long cost, int base) {
    super(bits, cost);
    this.base = base;
  }

  @Override
  public int docID() {
    v = super.docID();
    if (v != NO_MORE_DOCS)
      return v - base;
    return v;
  }

  @Override
  public int nextDoc() {
    v = super.nextDoc();
    if (v != NO_MORE_DOCS)
      return v - base;
    return v;
  }

  @Override
  public int advance(int target) {
    v = super.advance(target + base);
    if (v != NO_MORE_DOCS)
      return v - base;
    return v;
  }
  
  
  
}
