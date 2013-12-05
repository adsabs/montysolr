package org.apache.lucene.search;

import java.io.IOException;
import java.util.List;

public interface SecondOrderCollector {

	public enum FinalValueType {
		ARITHM_MEAN, GEOM_MEAN, ABS_COUNT, MAX_VALUE, MIN_VALUE,
		GEOM_MEAN_NORM, ARITHM_MEAN_NORM, ABS_COUNT_NORM,
		AGRESTI_COULL;
	}

	/**
	 * Called from the SecondOrderQuery when the Weight object
	 * is being created. The searcher is the top searcher, therefore
	 * the collector can (should|must) inspect it and discover the
	 * subreaders and how they are partitioned
	 * 
	 * Must return true if the initialization succeeded, otherwise
	 * the query will not be executed
	 * 
	 * @param searcher
	 * @param firstOrderWeight 
	 * @throws IOException 
	 */
	public boolean searcherInitialization(IndexSearcher searcher, Weight firstOrderWeight) throws IOException;




	/**
	 * Returns the sorted list of ScoreDoc hits that belong only
	 * to this particular subreader. The range of subreaders is
	 * known to the collector because we have inspected the searcher
	 * before @see {@link SecondOrderCollector#searcherInitialization(Searcher)}
	 * 
	 * @param reader
	 * @return
	 */
	public List<CollectorDoc> getSubReaderResults(int docBase, int docBaseEnd);



	/**
	 * If there are two collector instances, but they do the same,
	 * they should return the same hashCode. Usually done as a combination
	 * of the field and the cache
	 * @return
	 */
	public int hashCode();

	public boolean equals(Object o);

	/**
	 * Called by the Second Order Query before starting the search. 
	 * The collector should reset its data (it will collect the same
	 * data, from the same query; so in theory it could just refuse
	 * to collect new ones - which might be faster
	 * 
	 */
	public void reset();


	/**
	 * Sets how the implementation should compute the final 
	 * score (as an average of votes, maximum, minimum, etc...)
	 * 
	 */
	public void setFinalValueType(FinalValueType type);

}
