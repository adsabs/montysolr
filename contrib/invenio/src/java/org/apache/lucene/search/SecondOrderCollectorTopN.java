package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.AtomicReaderContext;

public class SecondOrderCollectorTopN extends AbstractSecondOrderCollector {

	private TopScoreDocCollector topCollector;
	private int topN;

	public SecondOrderCollectorTopN(int topN, boolean docsScoredInOrder) {
		topCollector = TopScoreDocCollector.create(topN, docsScoredInOrder);
		this.topN = topN;
	}
	
	public SecondOrderCollectorTopN(int topN) {
		topCollector = TopScoreDocCollector.create(topN, !firstOrderScorerOutOfOrder);
		this.topN = topN;
	}
	
	@Override
	public void setScorer(Scorer scorer) throws IOException {
		topCollector.setScorer(scorer);

	}

	@Override
	public void collect(int doc) throws IOException {
		topCollector.collect(doc);

	}

	@Override
	public void setNextReader(AtomicReaderContext context) throws IOException {
		topCollector.setNextReader(context);

	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return topCollector.acceptsDocsOutOfOrder();
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
		return "topn[" + topN + ", outOfOrder=" + this.acceptsDocsOutOfOrder() + "]";
	}

}
