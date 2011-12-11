package org.apache.solr.search;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.InvenioQuery;
import org.apache.lucene.search.InvenioWeightBitSet;
import org.apache.lucene.search.InvenioWeight;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Weight;
import java.io.IOException;
import java.util.Map;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrInvenioQuery extends InvenioQuery {


	public static final Logger log = LoggerFactory
			.getLogger(SolrInvenioQuery.class);

	private float boost = 1.0f; // query boost factor
	Query query;
	SolrParams localParams;
	SolrQueryRequest req;
	Map<Integer, Integer> recidToDocid = null;

	public SolrInvenioQuery(Query query, SolrQueryRequest req,
			SolrParams localParams, String idField) {
		super(query, idField);
		this.query = query;
		this.localParams = localParams;
		this.req = req;

	}


	/**
	 * Expert: Constructs an appropriate Weight implementation for this query.
	 *
	 * <p>
	 * Only implemented by primitive queries, which re-write to themselves.
	 */
	public Weight createWeight(IndexSearcher searcher) throws IOException {
		
		if (localParams.get("iq.channel").equals("bitset")) {
			return new InvenioWeight(searcher, this, idField);
		}
		else {
			return new InvenioWeightBitSet(searcher, this, idField);
		}
	}

}

