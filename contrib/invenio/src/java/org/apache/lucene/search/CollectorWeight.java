package org.apache.lucene.search;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;

public class CollectorWeight extends Weight {

	private static final long serialVersionUID = 6340251541629951246L;
	private final Weight innerWeight;
	private final Similarity similarity;
	private Collector collector;
	private int docBase;
	private int lastReaderId = 0;

	public CollectorWeight(Weight weight,
			Similarity similarity, Collector collector) throws IOException {
		this.similarity = similarity;
		this.innerWeight = weight;
		this.collector = collector;
		docBase = 0;
		
	}

	@Override
	public Query getQuery() {
		return innerWeight.getQuery();
	}

	@Override
	public float getValue() {
		return innerWeight.getValue();
	}

	@Override
	public float sumOfSquaredWeights() throws IOException {
		return innerWeight.sumOfSquaredWeights();
	}

	@Override
	public void normalize(float norm) {
		innerWeight.normalize(norm);
	}

	@Override
	public Scorer scorer(IndexReader reader, boolean scoreDocsInOrder,
			boolean topScorer) throws IOException {
		// Because there is no way to get access to the docBase information
		// we relly on the fact that scorer is called with each reader
		// in sequence
		collector.setNextReader(reader, docBase);
		//System.err.println(reader);
		//System.err.println(collector);
		if (lastReaderId != reader.hashCode()) {
			lastReaderId  = reader.hashCode();
			docBase += reader.maxDoc();
		}
		else {
			System.err.println("wtf?!");
		}
		Scorer innerScorer = innerWeight.scorer(reader, scoreDocsInOrder, topScorer);
		if (innerScorer == null) { //when there are no hits
			return null;
		}
		return new CollectorScorer(similarity, innerWeight, innerScorer, collector);
	}

	@Override
	public boolean scoresDocsOutOfOrder() {
		return (innerWeight != null) ? innerWeight.scoresDocsOutOfOrder()
				: false;
	}

	@Override
	public Explanation explain(IndexReader reader, int doc) throws IOException {
		return innerWeight.explain(reader, doc);
	}
}