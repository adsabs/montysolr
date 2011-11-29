package org.apache.solr.search;

import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.jni.MontySolrVM;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.Weight;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.handler.InvenioHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InvenioWeight extends Weight {

	public static final Logger log = LoggerFactory
		.getLogger(InvenioWeight.class);

	protected Weight weight;
	protected Similarity similarity;
	protected InvenioQuery query;
	protected TermQuery innerQuery;
	protected SolrParams localParams;
	protected Map<Integer, Integer> recidToDocid;
	protected float value;

	private int searcherCounter;

	public InvenioWeight(InvenioQuery query, SolrParams localParams,
			SolrQueryRequest req, Map<Integer, Integer> recidToDocid)
			throws IOException {
		SolrIndexSearcher searcher = req.getSearcher();
		this.innerQuery = (TermQuery) query.query;
		this.weight = innerQuery.createWeight(searcher);
		this.similarity = innerQuery.getSimilarity(searcher);
		this.query = query;
		this.localParams = localParams;
		this.recidToDocid = recidToDocid;
		this.searcherCounter = 0;
	}
	public Scorer scorer(IndexReader indexReader, boolean scoreDocsInOrder,
			boolean topScorer) throws IOException {

		if (searcherCounter > 0) {
			return null;
		}
		searcherCounter++;

		// we override the Scorer for the InvenioQuery
		return new Scorer(similarity) {

			private int doc = -1;
			private int[] recids = null;
			private int recids_counter = -1;
			private int max_counter = -1;

			public void score(Collector collector) throws IOException {
				collector.setScorer(this);

				int d;
				while ((d = nextDoc()) != NO_MORE_DOCS) {
					collector.collect(d);
				}
			}

			private void searchInvenio() {
				// ask Invenio to give us recids
				String qval = query.getInvenioQuery();

				PythonMessage message = MontySolrVM.INSTANCE
						.createMessage("perform_request_search_ints")
						.setSender("InvenioQuery").setParam("query", qval);
				
				MontySolrVM.INSTANCE.sendMessage(message);

				Object result = message.getResults();
				if (result != null) {
					recids = (int[]) result;
					max_counter = recids.length - 1;
					log.info("Invenio returned: " + recids.length + " hits");
				}
				else {
					log.info("Invenio returned: null");
				}
			}

			public int nextDoc() throws IOException {
				// this is called only once
				if (this.doc == -1) {
					searchInvenio();
					if (recids == null || recids.length == 0) {
						return doc = NO_MORE_DOCS;
					}
				}

				recids_counter += 1;
				if (recids_counter > max_counter) {
					return doc = NO_MORE_DOCS;
				}

				try {
					doc = recidToDocid.get(recids[recids_counter]);
				}
				catch (NullPointerException e) {
					log.error("Doc with recid=" + recids[recids_counter] + " missing. You should update Invenio recids!");
					throw e;
				}

				return doc;
			}

			public int docID() {
				return doc;
			}

			public int advance(int target) throws IOException {
				while ((doc = nextDoc()) < target) {
				}
				return doc;
			}

			public float score() throws IOException {
				assert doc != -1;
				return innerQuery.getBoost() * 1.0f; // TODO: implementation of the
												// scoring algorithm
			}
		};// Scorer
	}// scorer

	// pass these methods through to enclosed query's weight
	public float getValue() {
		return value;
	}

	public float sumOfSquaredWeights() throws IOException {
		return weight.sumOfSquaredWeights() * innerQuery.getBoost()
				* innerQuery.getBoost();
	}

	public void normalize(float v) {
		weight.normalize(v);
		value = weight.getValue() * innerQuery.getBoost();
	}

	public Explanation explain(IndexReader ir, int i) throws IOException {
		Explanation inner = weight.explain(ir, i);
		if (innerQuery.getBoost() != 1) {
			Explanation preBoost = inner;
			inner = new Explanation(inner.getValue() * innerQuery.getBoost(),
					"product of:");
			inner.addDetail(new Explanation(innerQuery.getBoost(), "boost"));
			inner.addDetail(preBoost);
		}
		inner.addDetail(new Explanation(0.0f, "TODO: add formula details"));
		return inner;
	}

	// return this query
	public Query getQuery() {
		return query;
	}

}; // Weight

