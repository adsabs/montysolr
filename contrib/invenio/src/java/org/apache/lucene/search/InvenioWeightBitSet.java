package org.apache.lucene.search;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import monty.solr.jni.MontySolrVM;
import monty.solr.jni.PythonMessage;

import org.ads.solr.InvenioBitSet;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.util.Bits;


import com.jcraft.jzlib.ZInputStream;

public class InvenioWeightBitSet extends InvenioWeight {

	private static final long serialVersionUID = -4934126443764572711L;
	
	
	public InvenioWeightBitSet(IndexSearcher searcher, InvenioQuery query, String idField)
			throws IOException {
		super(searcher, query, idField);
		pythonFunctionName = "perform_request_search_bitset";

	}

	public Scorer scorer(AtomicReaderContext context, boolean scoreDocsInOrder,
			boolean topScorer, Bits acceptDocs) throws IOException {

		// we override the Scorer for the InvenioQuery
		return new Scorer(weight) {

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

			private void searchInvenio() throws IOException {
				// ask Invenio to give us recids
				String qval = query.getInvenioQuery();

				PythonMessage message = MontySolrVM.INSTANCE
						.createMessage(pythonFunctionName)
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
					recid=0;
				}

				if ((recid = bitSet.nextSetBit(recid)) == -1) {
					return doc = NO_MORE_DOCS;
				}
				try {
					doc = recidToDocid.get(recid);
					recid++;
				}
				catch (NullPointerException e) {
					throw new NullPointerException("Doc with recid=" + recid + " missing. You should update Invenio recids!");
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

			@Override
      public float freq() throws IOException {
        return 1.0f; // because Invenio doesn't know this info, we stick to 1
      }
			
		};// Scorer
	}// scorer

}; // Weight

