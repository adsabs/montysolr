package org.apache.solr.search;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.Weight;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;


public class InvenioQueryBitSet extends InvenioQuery {

	private static final long serialVersionUID = -2624111746562481355L;
	private float boost = 1.0f; // query boost factor

	public InvenioQueryBitSet(TermQuery query, SolrQueryRequest req,
			SolrParams localParams, Map<Integer, Integer> recidToDocid) {
		super(query, req, localParams, recidToDocid);

	}

	/**
	 * Expert: Constructs an appropriate Weight implementation for this query.
	 *
	 * <p>
	 * Only implemented by primitive queries, which re-write to themselves.
	 */
	public Weight createWeight(Searcher searcher) throws IOException {

		return new InvenioWeightBitSet(this, localParams, req, recidToDocid);
	}


}


