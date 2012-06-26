package org.apache.lucene.search;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.CollectorQuery.CollectorCreator;

public class CollectorWeightSearcher extends Weight {

	private static final long serialVersionUID = 6340251541629951246L;
	private final Weight innerWeight;
	private final Similarity similarity;
	private Collector collector;
	private Map<Integer, Integer> docStarts;
	private CollectorCreator creator;

	public CollectorWeightSearcher(Weight weight,
			Similarity similarity, Collector collector, Map<Integer, Integer> docStarts) throws IOException {
		this.similarity = similarity;
		this.innerWeight = weight;
		this.collector = collector;
		this.docStarts = docStarts;
		
	}
	
	public CollectorWeightSearcher(Weight weight,
			Similarity similarity, CollectorCreator creator, Map<Integer, Integer> docStarts) throws IOException {
		this.similarity = similarity;
		this.innerWeight = weight;
		this.creator = creator;
		this.docStarts = docStarts;
		
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
		// we require the CollectorQuery to be initialized with the reader
		// instance. But since the IndexReader may have changed between the
		// query creation and invocation, this could lead to inconsistency
		// This check should prevent the problems (it will fail if other
		// Index reader is used for searching
		
		assert docStarts.get(reader.hashCode()) != null;
		

		Scorer innerScorer = innerWeight.scorer(reader, scoreDocsInOrder, topScorer);
		if (innerScorer == null) { //when there are no hits
			return null;
		}
		
		if (collector == null) {
			try {
				Collector c = creator.create();
				c.setNextReader(reader, docStarts.get(reader.hashCode()));
				return new CollectorScorer(similarity, innerWeight, innerScorer, c);
			} catch (Exception e) {
				throw new IOException(e);
			}
			
		}
		
		collector.setNextReader(reader, docStarts.get(reader.hashCode()));
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