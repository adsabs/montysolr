package org.apache.lucene.search;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.util.Bits;

public class SecondOrderWeight extends Weight {

	private static final long serialVersionUID = 1999318155593404879L;
	private final Weight innerWeight;
	private SecondOrderCollector secondOrderCollector;
	private Map<Integer, Integer> docStarts;

	public SecondOrderWeight(Weight weight,
			SecondOrderCollector collector) throws IOException {
		this.innerWeight = weight;
		this.secondOrderCollector = collector;
		
	}
	

	@Override
	public Query getQuery() {
		return innerWeight.getQuery();
	}


	@Override
	public float getValueForNormalization() throws IOException {
		return innerWeight.getValueForNormalization();
	}

	@Override
	public void normalize(float norm, float topLevelBoost) {
		innerWeight.normalize(norm, topLevelBoost);
	}

	@Override
	public Scorer scorer(AtomicReaderContext context, boolean scoreDocsInOrder,
      boolean topScorer, Bits acceptDocs) throws IOException {
	  int docBase = context.docBase;
	  int maxRange = docBase + context.reader().maxDoc();
	  List<CollectorDoc> hits = secondOrderCollector.getSubReaderResults(docBase, maxRange);
		if (hits == null || hits.size() == 0) return null;
		
		
		return new SecondOrderListOfDocsScorer(innerWeight, hits, docBase);
	}

	@Override
	public boolean scoresDocsOutOfOrder() {
		return (innerWeight != null) ? innerWeight.scoresDocsOutOfOrder()
				: false;
	}

	@Override
	public Explanation explain(AtomicReaderContext context, int doc) throws IOException {
	  //TODO: modify
		return innerWeight.explain(context, doc);
	}
}