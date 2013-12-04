package org.apache.lucene.search;


import java.io.IOException;
import java.util.Map;

import monty.solr.jni.MontySolrVM;
import monty.solr.jni.PythonCall;
import monty.solr.jni.PythonMessage;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Weight;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.util.Bits;


public class PythonQueryWeight extends Weight implements PythonCall {


	private static final long serialVersionUID = 1090438140913506853L;
	
	protected String pythonFunctionName = "perform_request_search_ints";
	
	protected Weight weight;
	protected Similarity similarity;
	protected PythonQuery query;
	protected Query innerQuery;
	protected float value;
	protected String idField;
	private int searcherCounter;
	
	CacheWrapper cache;
	boolean dieOnMissingIds;


	public PythonQueryWeight(IndexSearcher searcher, PythonQuery query, CacheWrapper cache, boolean dieOnMissingIds)
			throws IOException {
		this.innerQuery = query.getInnerQuery();
		this.weight = innerQuery.createWeight(searcher);
		this.similarity = searcher.getSimilarity();
		this.query = query;
		this.cache = cache;
		this.searcherCounter = 0;
		this.dieOnMissingIds = dieOnMissingIds;
	}
	
	@Override
  public void setPythonFunctionName(String name) {
		pythonFunctionName = name;
  }

	@Override
  public String getPythonFunctionName() {
	  return pythonFunctionName;
  }
	
	
	public Scorer scorer(AtomicReaderContext context, boolean scoreDocsInOrder,
			boolean topScorer, Bits acceptDocs) throws IOException {

		if (searcherCounter > 0) {
			return null;
		}
		searcherCounter++;

		// we override the Scorer for the PythonQuery
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

			private void searchPython() {
				
				PythonMessage message = MontySolrVM.INSTANCE
						.createMessage(pythonFunctionName)
						.setSender("PythonQuery")
						.setParam("query", query.getQueryAsString());
				
				MontySolrVM.INSTANCE.sendMessage(message);

				Object result = message.getResults();
				if (result != null) {
					recids = (int[]) result;
					max_counter = recids.length - 1;
				}
				else {
					throw new NullPointerException("Python process returned null for search: " + query.getQueryAsString());
				}
			}

			public int nextDoc() throws IOException {
				// this is called only once
				if (this.doc == -1) {
					searchPython();
					if (recids == null || recids.length == 0) {
						return doc = NO_MORE_DOCS;
					}
				}

				recids_counter += 1;
				while(recids_counter < max_counter) {
					
					doc = cache.getLuceneDocId(recids[recids_counter]);
					
					if (doc == -1) {
						if (dieOnMissingIds) {
							throw new IOException("Doc with recid=" + recids[recids_counter] + " is unknown to Lucene." + "" +
							" You should reindex and make sure we can translate the remote system docids into lucene ids!");
						}
						else {
							recids_counter += 1;
						}
					}
					else {
						return doc;
					}
					
				}
				
				return doc = NO_MORE_DOCS;
				
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

