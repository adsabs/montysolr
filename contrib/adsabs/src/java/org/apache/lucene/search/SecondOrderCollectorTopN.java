package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.LeafReaderContext;

public class SecondOrderCollectorTopN extends AbstractSecondOrderCollector {

	private TopDocsCollector topCollector;
	private int topN;
	private String detail = null;

	public SecondOrderCollectorTopN(String detail, int topN, TopDocsCollector collector) {
		this.topN = topN;
		topCollector = collector;
		this.detail = detail;
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
    return topCollector.needsScores();
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
}
