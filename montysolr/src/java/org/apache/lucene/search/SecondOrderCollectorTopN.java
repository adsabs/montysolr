package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.LeafReaderContext;

public class SecondOrderCollectorTopN extends AbstractSecondOrderCollector {

	private TopDocsCollector topCollector;
	private int topN;
	private String detail = null;
	private Sort sortOrder;

	public SecondOrderCollectorTopN(String detail, int topN, Sort sortOrder) {
		this.topN = topN;
		this.sortOrder = sortOrder;
		this.detail = detail;
		this.topCollector = TopFieldCollector.create(sortOrder, topN, false, true, true, true);
	}
	
	public SecondOrderCollectorTopN(int topN) {
		topCollector = TopScoreDocCollector.create(topN);
		this.topN = topN;
	}
	
	
	@Override
	public List<CollectorDoc> getSubReaderResults(int rangeStart, int rangeEnd) {

		if (topCollector.totalHits == 0)
			return null;
		
		lock.lock();
		try {
			if (!organized) {
				((ArrayList) hits).ensureCapacity(topCollector.totalHits);
				for (ScoreDoc d: topCollector.topDocs().scoreDocs) {
					hits.add(new CollectorDoc(d.doc, d.score));
				}
					
			}
		}
		finally {
			lock.unlock();
		}
		
		return super.getSubReaderResults(rangeStart, rangeEnd);
		
  }
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + topN + (detail!=null ? ", info=" + detail : "") + ")";
	}

  @Override
  public boolean needsScores() {
	  return this.topCollector.needsScores();
  }

  @Override
  public void collect(int doc) throws IOException {
    throw new UnsupportedOperationException("Must not be called");
    
  }
  
  @Override
  public LeafCollector getLeafCollector(LeafReaderContext context) throws IOException {
    LeafCollector c = topCollector.getLeafCollector(context);
    return c;
  }
  
  public SecondOrderCollector copy() {
	  if (sortOrder != null) {
		  return new SecondOrderCollectorTopN(detail, topN, sortOrder);
	  }
	  else {
		  return new SecondOrderCollectorTopN(topN);		  
	  }
  }
}
