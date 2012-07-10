package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.search.Weight;


public class InvenioQueryBitSet extends InvenioQuery {

	private static final long serialVersionUID = -2624111746562481355L;
	private float boost = 1.0f; // query boost factor

	public InvenioQueryBitSet(Query query, String idField, String searchField) {
		super(query, idField, searchField);
	}
	
	public InvenioQueryBitSet(Query query, String idField, String searchField, 
			String pythonResponder) {
		super(query, idField, searchField, pythonResponder);
	}

	/**
	 * Expert: Constructs an appropriate Weight implementation for this query.
	 *
	 * <p>
	 * Only implemented by primitive queries, which re-write to themselves.
	 */
	public Weight createWeight(IndexSearcher searcher) throws IOException {

		InvenioWeightBitSet w = new InvenioWeightBitSet((IndexSearcher) searcher, this, this.idField);
		if (pythonResponder != null) {
			w.setPythonResponder(pythonResponder);
		}
		return w;
	}

	public String toString(String s) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<(");
		buffer.append("intbitset,");
		buffer.append(idField);
		buffer.append(")");
		//buffer.append(query.toString());
		buffer.append(getInvenioQuery());
		buffer.append(">");
		return buffer.toString();
	}


}


