package org.apache.solr.search;

import invenio.montysolr.jni.PythonMessage;
import invenio.montysolr.jni.MontySolrVM;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.ads.solr.InvenioBitSet;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TermQuery;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;


import com.jcraft.jzlib.ZInputStream;

public class InvenioWeightBitSet extends InvenioWeight {

	public InvenioWeightBitSet(InvenioQuery query, SolrParams localParams,
			SolrQueryRequest req, Map<Integer, Integer> recidToDocid)
			throws IOException {
		super(query, localParams, req, recidToDocid);

	}

	public Scorer scorer(IndexReader indexReader, boolean scoreDocsInOrder,
			boolean topScorer) throws IOException {

		// we override the Scorer for the InvenioQuery
		return new Scorer(similarity) {

			private int doc = -1;
			private int recid = -1;
			private InvenioBitSet bitSet = null;

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
						.createMessage("perform_request_search_bitset")
						.setSender("InvenioQuery").setParam("query", qval);
				
				MontySolrVM.INSTANCE.sendMessage(message);

				Object result = message.getResults();

				if (result != null) {
					// use zlib to read in the data
					InputStream is = new ByteArrayInputStream((byte[]) result);
					ByteArrayOutputStream bOut = new ByteArrayOutputStream();
					ZInputStream zIn = new ZInputStream(is);

					int bytesCopied = IOUtils.copy(zIn, bOut);
					byte[] bitset_bytes = bOut.toByteArray();
					bitSet = new InvenioBitSet(bitset_bytes);
				}
			}

			public int nextDoc() throws IOException {
				// this is called only once
				if (this.recid == -1) {
					searchInvenio();
					if (bitSet == null || bitSet.isEmpty()) {
						return doc = NO_MORE_DOCS;
					}
				}

				if ((recid = bitSet.nextSetBit(recid)) == -1) {
					return doc = NO_MORE_DOCS;
				}

				try {
					doc = recidToDocid.get(recid);
				}
				catch (NullPointerException e) {
					log.error("Doc with recid=" + recid + " missing. You should update Invenio recids!");
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
				return query.getBoost() * 1.0f; // TODO: implementation of the
												// scoring algorithm
			}
		};// Scorer
	}// scorer

}; // Weight

