package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;

public abstract class AbstractSecondOrderCollector extends Collector implements
    SecondOrderCollector {

  protected Scorer scorer;
  protected int docBase;
  protected List<ScoreDoc> hits;
  protected volatile boolean organized = false;
  protected Lock lock = null;
  private Integer lastPos = null;

  public AbstractSecondOrderCollector() {
    lock = new ReentrantLock();
    hits = new ArrayList<ScoreDoc>();
  }

  public void searcherInitialization(IndexSearcher searcher) throws IOException {

  }

  public List<ScoreDoc> getSubReaderResults(int rangeStart, int rangeEnd) {
	  
	if (hits.size() == 0)
		return null;
	
    int i = -1;
    
    lock.lock();
    try {
      if (!organized) {
        organizeResults();
        organized = true;
      }
      i = findClosestIndex(rangeStart, rangeEnd, 0, hits.size()-1);
      if (i == -1 || i+1 > hits.size())
    	  return null;
      
    } finally {
      lock.unlock();
    }
    
    
    ArrayList<ScoreDoc> results = new ArrayList<ScoreDoc>();
    for (;i<hits.size() && hits.get(i).doc < rangeEnd;i++) {
    	results.add(hits.get(i));
    }
    return results;
    
  }
  
  private int findClosestIndex(int rangeStart, int rangeEnd, int low, int high) {
	  
	  int latest = 0;
    while (low <= high) {
        int mid = (low + high) >>> 1;
        int midVal = hits.get(mid).doc;
  
        if (midVal < rangeStart) {
            low = mid + 1;
            latest = high;
        } else if (midVal > rangeStart) {
            high = mid - 1;
            latest = low;
        } else {
            return mid; // key found
        }
    }
    if (hits.get(latest).doc >= rangeStart && hits.get(latest).doc <= rangeEnd)
    	return latest;
    
    return -1;  // key not found
  }

  // a very naive implementation (TODO: search faster)
  private Integer[] findRange(int startDoc, Integer lastDoc) {
    int low = 0;
    int high = hits.size();
    Integer[] out = new Integer[]{0,0};
    
    if (lastPos  != null) {
      if (lastPos+1 <= hits.size()) {
        return out;
      }
      ScoreDoc previous = hits.get(lastPos);
      ScoreDoc next = hits.get(lastPos+1);
      if (previous.doc < startDoc && next.doc >= startDoc) {
        low = lastPos+1;
      }
    }
    
    int i=low;
    while (i<high) {
      int d = hits.get(i).doc;
      if (d >= startDoc && d<=lastDoc) {
        i++;
      }
      else {
        break;
      }
    }
    
    lastPos = i;
    
    out[0] = low;
    out[1] = lastPos;
    
    return out;
    
  }
  
  private int findClosestInclusive(int low, int high, int valToSearch) {
    int i;
    for (i=low;i<high;i++) {
      int d = hits.get(i).doc;
      if (d == valToSearch) {
        return i;
      }
      else if (d > valToSearch) {
        return i-1;
      }
    }
    return -1;
  }
  
  private int binarySearch(int low, int high, int valToSearch) {
    while (low <= high) {
        int mid = (low + high) >>> 1;
        int midVal = hits.get(mid).doc;
  
        if (midVal < valToSearch)
            low = mid + 1;
        else if (midVal > valToSearch)
            high = mid - 1;
        else
            return mid; // key found
    }
    return low-1;  // key not found, get closest
  }

  private void organizeResults() {

    sortHits();
    compactHits();

  }

  private void compactHits() {
    ArrayList<ScoreDoc> newHits = new ArrayList<ScoreDoc>(new Float(
        (hits.size() * 0.75f)).intValue());

    ScoreDoc currDoc = null;
    int seenTimes = 0;
    float score = 0.0f;

    for (ScoreDoc d : hits) {
      if (currDoc == null || d.doc == currDoc.doc) {
        score += d.score;
        seenTimes += 1;
        if (currDoc == null)
          currDoc = d;
        continue;
      }

      if (seenTimes > 1)
        currDoc.score = score / seenTimes;
      newHits.add(currDoc);

      currDoc = d;
      score = d.score;
      seenTimes = 1;
    }
    if (currDoc != null) {
      if (seenTimes > 1)
        currDoc.score = score / seenTimes;
      newHits.add(currDoc);
    }

    hits = newHits;
  }

  private void sortHits() {

    Collections.sort(hits, new Comparator<ScoreDoc>() {
      public int compare(ScoreDoc o1, ScoreDoc o2) {
        return o1.doc - o2.doc;
      }
    });

  }

  /** Returns true iff <code>o</code> is equal to this. */
  public boolean equals(Object o) {
    if (o instanceof SecondOrderCollector) {
      SecondOrderCollector fq = (SecondOrderCollector) o;
      return hashCode() == fq.hashCode();
    }
    return false;
  }

}
