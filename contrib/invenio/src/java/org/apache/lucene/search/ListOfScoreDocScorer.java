package org.apache.lucene.search;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class ListOfScoreDocScorer extends Scorer {
	private List<ScoreDoc> hits;
	private Iterator<ScoreDoc> iterator = null;
	private int doc = -1;
	private float score;
	private int docBase = 0;

    public ListOfScoreDocScorer(List<ScoreDoc> hits, int docBase) throws IOException {
      super(null, null);
      if (hits.size()>0) {
    	  this.hits = hits;
    	  iterator = hits.iterator();
      }
      this.docBase = docBase;
    }

    @Override
    public int nextDoc() throws IOException {
    	if (iterator != null && iterator.hasNext()) {
    		ScoreDoc hit = iterator.next();
    		score = hit.score;
    		return doc = hit.doc - docBase;
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
		return score;
    }

    @Override
    public int advance(int target) throws IOException {
    	while ((doc = nextDoc()) < target) {
		}
		return doc;
    }
    
    
    
  }
