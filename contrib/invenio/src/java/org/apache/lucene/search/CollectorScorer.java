package org.apache.lucene.search;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;


public class CollectorScorer extends Scorer {
	private Set<Integer> hits;
	private Iterator<Integer> iterator = null;
	private int doc = -1;

    public CollectorScorer(Similarity similarity, Weight w, Scorer scorer, Collector collector) throws IOException {
      super(similarity,w);
      
      scorer.score(collector);
      if (collector instanceof SetCollector) {
    	  hits = ((SetCollector)collector).getHits();
    	  iterator = hits.iterator();
      }
      else {
    	  throw new IllegalStateException("The collector must implement getHits() method");
      }
      
    }

    @Override
    public int nextDoc() throws IOException {
    	if (iterator != null && iterator.hasNext()) {
    		return doc = iterator.next();
    	}
    	else {
    		return doc = NO_MORE_DOCS;
    	}
    }
    
    @Override
    public int docID() {
      return doc;
    }

    @Override
    public float score() throws IOException {
    	assert doc != -1;
		return 1.0f; // TODO: implementation of the scoring algorithm
    }

    @Override
    public int advance(int target) throws IOException {
    	while ((doc = nextDoc()) < target) {
		}
		return doc;
    }
    
    
    
  }
