package org.apache.lucene.search;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;

public class SecondOrderWeight extends Weight {

	public SecondOrderWeight(Weight firstOrderWeight,
			Similarity firstOrderSimilarity,
			SecondOrderCollector secondOrderCollector) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Explanation explain(IndexReader reader, int doc) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query getQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void normalize(float norm) {
		// TODO Auto-generated method stub

	}

	@Override
	public Scorer scorer(IndexReader reader, boolean scoreDocsInOrder,
			boolean topScorer) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float sumOfSquaredWeights() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
