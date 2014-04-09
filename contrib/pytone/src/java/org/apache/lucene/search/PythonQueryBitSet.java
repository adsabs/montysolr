package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.search.Weight;


public class PythonQueryBitSet extends PythonQuery {

	private static final long serialVersionUID = -2624111746562481355L;
	private float boost = 1.0f; // query boost factor

	public PythonQueryBitSet(Query query, SolrCacheWrapper cache, boolean dieOnMissingIds) {
		super(query, cache, dieOnMissingIds);
	}
	
	public PythonQueryBitSet(Query query, SolrCacheWrapper cache, boolean dieOnMissingIds, String pythonFunctionName) {
		super(query, cache, dieOnMissingIds, pythonFunctionName);
	}
	

	/**
	 * Expert: Constructs an appropriate Weight implementation for this query.
	 *
	 * <p>
	 * Only implemented by primitive queries, which re-write to themselves.
	 */
	public Weight createWeight(IndexSearcher searcher) throws IOException {

		PythonQueryWeight w = new PythonQueryWeightBitSet((IndexSearcher) searcher, this, cache, dieOnMissingIds);
		if (pythonResponder != null) {
			w.setPythonFunctionName(pythonResponder);
		}
		return w;
	}

	public String toString(String s) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("PythonQueryBitSet(");
		buffer.append(getQueryAsString());
		buffer.append(")");
		return buffer.toString();
	}


}


