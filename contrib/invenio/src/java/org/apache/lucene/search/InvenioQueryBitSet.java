package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.search.Weight;


public class InvenioQueryBitSet extends InvenioQuery {

	private static final long serialVersionUID = -2624111746562481355L;
	private float boost = 1.0f; // query boost factor

	public InvenioQueryBitSet(Query query, String idField) {
		super(query, idField);

	}

	/**
	 * Expert: Constructs an appropriate Weight implementation for this query.
	 *
	 * <p>
	 * Only implemented by primitive queries, which re-write to themselves.
	 */
	public Weight createWeight(IndexSearcher searcher) throws IOException {

		return new InvenioWeightBitSet(searcher, this, this.idField);
	}


}


