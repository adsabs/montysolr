package org.apache.lucene.search;


import java.io.IOException;
import java.util.Map;

import montysolr.jni.MontySolrVM;
import montysolr.jni.PythonMessage;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Weight;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.util.Bits;


public class InvenioWeight extends Weight {


	private static final long serialVersionUID = 1090438140913506853L;
	
	protected String pythonFunctionName = "perform_request_search_ints";
	
	protected Weight weight;
	protected Similarity similarity;
	protected InvenioQuery query;
	protected Query innerQuery;
	protected Map<Integer, Integer> recidToDocid;
	protected float value;
	protected String idField;

	private int searcherCounter;

	public InvenioWeight(IndexSearcher searcher, InvenioQuery query, String idField)
			throws IOException {
		this.innerQuery = query.getInnerQuery();
		this.weight = innerQuery.createWeight(searcher);
		this.similarity = searcher.getSimilarity();
		this.query = query;
		this.recidToDocid = DictionaryRecIdCache.INSTANCE.getTranslationCache(
				searcher.getIndexReader(), idField);
		this.searcherCounter = 0;
		this.idField = idField;
		
	}
	
	public void setPythonResponder(String functionName) {
		pythonFunctionName = functionName;
	}
	
	public Scorer scorer(AtomicReaderContext context, boolean scoreDocsInOrder,
			boolean topScorer, Bits acceptDocs) throws IOException {

		if (searcherCounter > 0) {
			return null;
		}
		searcherCounter++;

		// we override the Scorer for the InvenioQuery
		return new Scorer(weight) {

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
						.createMessage(pythonFunctionName)
						.setSender("InvenioQuery").setParam("query", qval);
				
				MontySolrVM.INSTANCE.sendMessage(message);

				Object result = message.getResults();
				if (result != null) {
					recids = (int[]) result;
					max_counter = recids.length - 1;
				}
				else {
					throw new NullPointerException("Invenio returned: null");
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
					throw new IOException("Doc with recid=" + recids[recids_counter] + " is unknown to Lucene. You should reindex!");
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
				return innerQuery.getBoost(); // TODO: implementation of the
												// scoring algorithm
			}

      @Override
      public float freq() throws IOException {
        return 1.0f; // because Invenio doesn't know this info, we stick to 1
      }
		};// Scorer
	}// scorer
	

	public float getValueForNormalization() throws IOException {
		return weight.getValueForNormalization();
	}
	
	@Override
    public void normalize(float queryNorm, float topLevelBoost) {
      weight.normalize(queryNorm, topLevelBoost);
    }
	
	@Override
    public Explanation explain(AtomicReaderContext context, int doc) throws IOException {
		return weight.explain(context, doc);
    }


	// return this query
	public Query getQuery() {
		return query;
	}


}; // Weight

