package org.apache.lucene.search;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.CollectorQuery.CollectorCreator;

public class SecondOrderWeight extends Weight {

	private static final long serialVersionUID = 1999318155593404879L;
	private final Weight innerWeight;
	private final Similarity similarity;
	private SecondOrderCollector secondOrderCollector;
	private Map<Integer, Integer> docStarts;
	private CollectorCreator creator;

	public SecondOrderWeight(Weight weight,
			Similarity similarity, SecondOrderCollector collector) throws IOException {
		this.similarity = similarity;
		this.innerWeight = weight;
		this.secondOrderCollector = collector;
		
	}
	
	public SecondOrderWeight(Weight weight,
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
		
		List<ScoreDoc> hits = secondOrderCollector.getSubReaderScoreDocs(reader);
		if (hits.size() == 0) return null;
		return new ListOfScoreDocScorer(hits, secondOrderCollector.getSubReaderDocBase(reader));
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