package org.apache.solr.search;

import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.Weight;
import org.apache.lucene.util.ToStringUtils;

import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.jni.MontySolrVM;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.BitSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.lucene.search.DictionaryRecIdCache;



public class CitationQuery extends Query {
	private float boost = 1.0f; // query boost factor
	Query query;
	SolrParams localParams;
	SolrQueryRequest req;
	String idField = "id"; //TODO: make it configurable
	String dictName = null;


	public CitationQuery (Query query, SolrQueryRequest req, SolrParams localParams) {
		this.query = query;
		this.localParams = localParams;
		this.req = req;

		String type = localParams.get("rel");
		if (type.contains("refersto")) {
			dictName = "citationdict";
		}
		else if (type.contains("citedby")) {
			dictName = "reversedict";
		}
		else {
			dictName = "reversedict";
		}
    }

	/**
	 * Sets the boost for this query clause to <code>b</code>. Documents
	 * matching this clause will (in addition to the normal weightings) have
	 * their score multiplied by <code>b</code>.
	 */
	public void setBoost(float b) {
		boost = b;
	}

	/**
	 * Gets the boost for this clause. Documents matching this clause will (in
	 * addition to the normal weightings) have their score multiplied by
	 * <code>b</code>. The boost is 1.0 by default.
	 */
	public float getBoost() {
		return boost + query.getBoost();
	}

	public Map<Integer, int[]> getDictCache() throws IOException {
		try {
			return getDictCache(this.dictName);
		} catch (IOException e) {
			e.printStackTrace();
			// return empty map, that is ok because it will affect only
			// this query, the next will get a new cache
			return new HashMap<Integer, int[]>();
		}
	}

	public Map<Integer, int[]> getDictCache(String dictname) throws IOException {


		Map<Integer, int[]> cache = DictionaryRecIdCache.INSTANCE.getCache(dictname);


		if (cache == null) {



			// Get mapping lucene_id->invenio_recid
			// The simplest would be to load the field with a cache (but the
			// field should be integer - and it is not now). The other reason
			// for doint this is that we don't create unnecessary cache

			/**

			TermDocs td = reader.termDocs(); //FIXME: .termDocs(new Term(idField)) works not?!
			String[] li =  {idField};
			MapFieldSelector fieldSelector = new MapFieldSelector(li);
			**/

			SolrIndexSearcher searcher = req.getSearcher();
			SolrIndexReader reader = searcher.getReader();
		    int[] idMapping = FieldCache.DEFAULT.getInts(reader, idField);

			Map<Integer, Integer> fromValueToDocid = new HashMap<Integer, Integer>(idMapping.length);
			int i = 0;
			for (int value: idMapping) {
				fromValueToDocid.put(value, i);
				i++;
			}

			/**
			//OpenBitSet bitSet = new OpenBitSet(reader.maxDoc());
			int i;
			while (td.next()) {
				i = td.doc();
				// not needed when term is null
				//if (reader.isDeleted(i)) {
				//	continue;
				//}
				Document doc = reader.document(i);

				try {
					//bitSet.set(Integer.parseInt(doc.get(idField)));
					idMap.put(i, Integer.parseInt(doc.get(idField)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			**/

			// now get the citation dictionary from Invenio
			HashMap<Integer, int[]> hm = new HashMap<Integer, int[]>();

			PythonMessage message = MontySolrVM.INSTANCE.createMessage("get_citation_dict")
						.setSender("CitationQuery")
						.setParam("dictname", dictName)
						.setParam("result", hm);
			MontySolrVM.INSTANCE.sendMessage(message);


			Map<Integer, int[]> citationDict = new HashMap<Integer, int[]>(0);
			if (message.containsKey("result")) {

				@SuppressWarnings("unchecked")
				Map<Integer, Object> result = (Map<Integer, Object>) message.getResults();
				citationDict = new HashMap<Integer, int[]>(result.size());
				for (Entry<Integer, Object> e: result.entrySet()) {
					Integer recid = e.getKey();
					if (fromValueToDocid.containsKey(recid)) {
						// translate recids into lucene-ids

			            int[] recIds = (int[]) e.getValue();
			            int[] lucIds = new int[recIds.length];
						for (int x=0;x<recIds.length;x++) {
							if (fromValueToDocid.containsKey(recIds[x]))
								lucIds[x] = fromValueToDocid.get(recIds[x]);
						}
						citationDict.put(recid, (int[]) e.getValue());
					}
				}
			}

			DictionaryRecIdCache.INSTANCE.setCache(dictname, citationDict);
			return citationDict;
		}
		return cache;
	}

	public Map<Integer, BitSet> getDictCacheX() {
    	HashMap<Integer, BitSet> hm = new HashMap<Integer, BitSet>();

    	int Min = 1;
    	int Max = 5000;
    	int r;
    	for (int i=0;i<5000; i++) {
    		r = Min + (int)(Math.random() * ((Max - Min) + 1));
    		BitSet bs = new BitSet(r);
    		int ii = 0;
    		while (ii < 20) {
    			r = Min + (int)(Math.random() * ((Max - Min) + 1));
    			bs.set(r);
    			ii += 1;
    		}
    		hm.put(i, bs);
    	}
    	return hm;
    }


	/**
	 * Expert: Constructs an appropriate Weight implementation for this query.
	 *
	 * <p>
	 * Only implemented by primitive queries, which re-write to themselves.
	 */
	public Weight createWeight(Searcher searcher) throws IOException {

		final Weight weight = query.createWeight (searcher);
	    final Similarity similarity = query.getSimilarity(searcher);



	    return new Weight() {
	      private float value;


	      // return a filtering scorer
	      public Scorer scorer(IndexReader indexReader, boolean scoreDocsInOrder, boolean topScorer)
	          throws IOException {

	        final Scorer scorer = weight.scorer(indexReader, true, false);
	        final IndexReader reader = indexReader;

	        if (scorer == null) {
	          return null;
	        }


	        // we override the Scorer for the CitationQuery
	        return new Scorer(similarity) {

	          private int doc = -1;

	          public void getCache() {
	        	  System.out.println(reader);

	          }

	          // here is the core of the processing
	          public void score(Collector collector) throws IOException {
	        	    collector.setScorer(this);
	        	    int doc;

	        	    //TODO: we could as well collect the first matching documents
	        	    //and based on them retrieve all the citations. But probably
	        	    //that is not correct, because the citation search wants to
	        	    //retrieve the documents that are most cited/referred, therefore
	        	    //we have to search the whole space

	        	    // get the respective dictionary
	        	    Map<Integer, int[]> cache = getDictCache();
	        	    BitSet aHitSet = new BitSet(reader.maxDoc());

	        	    if (cache.size() == 0)
	        	    	return;

	        	    // retrieve documents that matched the query and while we go
	        	    // collect the documents referenced by/from those docs
	        	    while ((doc = nextDoc()) != NO_MORE_DOCS) {
	        	    	if (cache.containsKey(doc)) {
	        	    		int[] v = cache.get(doc);
	        	    		for (int i: v)
	        	    			aHitSet.set(i);
	        	    	}
	        	    }

	        	    // now collect the big set of citing relations
	        	    doc = 0;
	        	    while ((doc = aHitSet.nextSetBit(doc)) != -1) {
	        	      collector.collect(doc);
	        	      doc += 1;
	        	    }
	        	  }

	          public int nextDoc() throws IOException {
	        	  return scorer.nextDoc();
	          }

	          public int docID() { return doc; }

	          /** @deprecated use {@link #advance(int)} instead. */
	          @Deprecated
	          public boolean skipTo(int i) throws IOException {
	            return advance(i) != NO_MORE_DOCS;
	          }

	          public int advance(int target) throws IOException {
	        	  return scorer.advance(target);
	          }

	          //public float score() throws IOException { return getBoost() * scorer.score(); }
	          public float score() throws IOException { return getBoost() * 1.0f; }
	        };// Scorer
	      }// scorer

	      // pass these methods through to enclosed query's weight
	      public float getValue() { return value; }

	      public float sumOfSquaredWeights() throws IOException {
	        return weight.sumOfSquaredWeights() * getBoost() * getBoost();
	      }

	      public void normalize (float v) {
	        weight.normalize(v);
	        value = weight.getValue() * getBoost();
	      }

	      public Explanation explain (IndexReader ir, int i) throws IOException {
	        Explanation inner = weight.explain (ir, i);
	        if (getBoost()!=1) {
	          Explanation preBoost = inner;
	          inner = new Explanation(inner.getValue()*getBoost(),"product of:");
	          inner.addDetail(new Explanation(getBoost(),"boost"));
	          inner.addDetail(preBoost);
	        }
	        inner.addDetail(new Explanation(0.0f, "TODO: add citation formula details"));
	        return inner;
	      }

	      // return this query
	      public Query getQuery() { return CitationQuery.this; }

	    }; //Weight
	  }

	/**
	 * Expert: Constructs and initializes a Weight for a top-level query.
	 */
	public Weight weight(Searcher searcher) throws IOException {
		Query query = searcher.rewrite(this);
		Weight weight = query.createWeight(searcher);
		float sum = weight.sumOfSquaredWeights();
		float norm = getSimilarity(searcher).queryNorm(sum);
		if (Float.isInfinite(norm) || Float.isNaN(norm))
			norm = 1.0f;
		weight.normalize(norm);
		return weight;
	}

	/**
	 * Expert: called to re-write queries into primitive queries. For example, a
	 * PrefixQuery will be rewritten into a BooleanQuery that consists of
	 * TermQuerys.
	 */
	public Query rewrite(IndexReader reader) throws IOException {
	    Query rewritten = query.rewrite(reader);
	    if (rewritten != query) {
	      CitationQuery clone = (CitationQuery)this.clone();
	      clone.query = rewritten;
	      return clone;
	    } else {
	      return this;
	    }
	  }

	/**
	 * Expert: called when re-writing queries under MultiSearcher.
	 *
	 * Create a single query suitable for use by all subsearchers (in 1-1
	 * correspondence with queries). This is an optimization of the OR of all
	 * queries. We handle the common optimization cases of equal queries and
	 * overlapping clauses of boolean OR queries (as generated by
	 * MultiTermQuery.rewrite()). Be careful overriding this method as
	 * queries[0] determines which method will be called and is not necessarily
	 * of the same type as the other queries.
	 */
	public Query combine(Query[] queries) {
		return query.combine(queries);

	}

	/**
	 * Expert: adds all terms occurring in this query to the terms set. Only
	 * works if this query is in its {@link #rewrite rewritten} form.
	 *
	 * @throws UnsupportedOperationException
	 *             if this query is not yet rewritten
	 */
	public void extractTerms(Set<Term> terms) {
		query.extractTerms(terms);
	}


	/**
	 * Expert: Returns the Similarity implementation to be used for this query.
	 * Subclasses may override this method to specify their own Similarity
	 * implementation, perhaps one that delegates through that of the Searcher.
	 * By default the Searcher's Similarity implementation is returned.
	 */
	public Similarity getSimilarity(Searcher searcher) {
		return searcher.getSimilarity();
	}



	/** Prints a user-readable version of this query. */
	  public String toString (String s) {
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("CitationQuery(");
	    buffer.append(query.toString(s));
	    buffer.append(")->");
	    buffer.append(ToStringUtils.boost(getBoost()));
	    return buffer.toString();
	  }

	  /** Returns true iff <code>o</code> is equal to this. */
	  public boolean equals(Object o) {
	    if (o instanceof CitationRefersToQuery) {
	    	CitationRefersToQuery fq = (CitationRefersToQuery) o;
	      return (query.equals(fq.query) && getBoost()==fq.getBoost());
	    }
	    return false;
	  }

	  /** Returns a hash code value for this object. */
	  public int hashCode() {
	    return query.hashCode() ^ Float.floatToRawIntBits(getBoost());
	  }
}
