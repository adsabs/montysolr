package org.apache.lucene.search;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.CollectorQuery.CollectorCreator;

public class NewCollectorWeight extends Weight {

	private static final long serialVersionUID = 1999318155593404879L;
	private final Weight innerWeight;
	private final Similarity similarity;
	private Collector collector;
	private Map<Integer, Integer> docStarts;
	private CollectorCreator creator;

	public NewCollectorWeight(Weight weight,
			Similarity similarity, Collector collector, Map<Integer, Integer> docStarts) throws IOException {
		this.similarity = similarity;
		this.innerWeight = weight;
		this.collector = collector;
		this.docStarts = docStarts;
		
	}
	
	public NewCollectorWeight(Weight weight,
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
		
		// wait for the first-order-collector to finish executing
		List<ScoreDoc> hits = collector.getPerReaderScoreDocs(docStarts.get(reader.hashCode()));
		
		
		return new ListOfScoreDocScorer(hits);
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