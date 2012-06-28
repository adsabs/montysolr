package org.apache.lucene.search;

import java.util.List;
import org.apache.lucene.index.IndexReader;

public interface SecondOrderCollector {

    /**
     * Called from the SecondOrderQuery when the Weight object
     * is being created. The searcher is the top searcher, therefore
     * the collector can (should|must) inspect it and discover the
     * subreaders and how they are partitioned
     * 
     * @param searcher
     */
    public void searcherInitialization(Searcher searcher);

    
    /**
	 * Given the reader instance, returns the docBase from which this
	 * reader starts.
	 * 
	 */
    public int getSubReaderDocBase(IndexReader reader);
    
    
	/**
	 * Returns the sorted list of ScoreDoc hits that belong only
	 * to this particular subreader. The range of subreaders is
	 * known to the collector because we have inspected the searcher
	 * before @see {@link SecondOrderCollector#searcherInitialization(Searcher)}
	 * 
	 * @param reader
	 * @return
	 */
	public List<ScoreDoc> getSubReaderResults(IndexReader reader);
	
	

	/**
	 * If there are two collector instances, but they do the same,
	 * they should return the same hashCode. Usually done as a combination
	 * of the field and the cache
	 * @return
	 */
    public int hashCode();
    public boolean equals(Object o);

}
