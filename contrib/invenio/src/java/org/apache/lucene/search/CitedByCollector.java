package org.apache.lucene.search;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.index.IndexReader;

/**
 * Implements the (x) --> X relation
 * For the 'refersto' queries
 * 
 * @author rchyla
 *
 */
public class CitedByCollector extends Collector implements SetCollector {

	private Scorer scorer;
	private IndexReader reader;
	private int docBase;
	private String indexField;
	private int[][] invertedCache;
	private Set<Integer> recids = new HashSet<Integer>();
	
	public CitedByCollector(int[][] cache, String field) {
		super();
		invertedCache = cache;
		indexField = field;
	}

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;

	}

	@Override
	public void collect(int doc) throws IOException {
		if (invertedCache[doc] == null) return;
		for (int v: invertedCache[doc]) {
			recids.add(v);
		}

	}

	@Override
	public void setNextReader(IndexReader reader, int docBase)
			throws IOException {
		this.reader = reader;
		this.docBase = docBase;

	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}
	
	public Set<Integer> getHits() {
		return recids;
	}
	
	@Override
	public String toString() {
		return "refersto[using:" + indexField + "]";
	}
	
	/** Returns a hash code value for this object. */
	public int hashCode() {
		return indexField.hashCode() ^ invertedCache.hashCode();
	}
	
	/** Returns true iff <code>o</code> is equal to this. */
	public boolean equals(Object o) {
		if (o instanceof CitedByCollector) {
			CitedByCollector fq = (CitedByCollector) o;
			return hashCode() == fq.hashCode();
		}
		return false;
	}

}
