package org.apache.solr.handler.batch;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.FixedBitSet;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.JSONDumper;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.QueryParsing;
import org.apache.solr.search.SolrIndexSearcher;

/**
 * Provider which saves documents from the index in
 * JSON format on disk. 
 *
 */
public class BatchProviderDumpDocsByQuery extends BatchProvider {
	
	
	public void run(SolrQueryRequest req, BatchHandlerRequestQueue queue) throws Exception {
		
		SolrParams params = req.getParams();
	  String jobid = params.get("jobid");
	  String workDir = params.get("#workdir");
	  
	  assert jobid != null && new File(workDir).canWrite();
	  
	  String q = params.get(CommonParams.Q, null);
		if (q == null) {
			throw new SolrException(ErrorCode.BAD_REQUEST, "The 'q' parameter is missing, but we must know how to select docs");
		}
		
		String defType = params.get(QueryParsing.DEFTYPE,QParserPlugin.DEFAULT_QTYPE);
		QParser parser = QParser.getParser(q, defType, req);
		Query query = parser.getQuery();
		
		
		SolrIndexSearcher searcher = req.getSearcher();
		final FixedBitSet bits = new FixedBitSet(searcher.getIndexReader().maxDoc());
		
		// collect ids of docs we want to dump
		searcher.search(query, new Collector() {
			private int docBase;

			@Override
			public boolean acceptsDocsOutOfOrder() {
				return true;
			}

			@Override
			public void collect(int i) throws IOException {
				bits.set(i+docBase);
			}

			@Override
      public void setScorer(Scorer scorer) throws IOException {
	      // TODO Auto-generated method stub
      }

			@Override
      public void setNextReader(AtomicReaderContext context) throws IOException {
	      docBase = context.docBase;
      }
		});
		
		if (bits.nextSetBit(0) > -1) {
			BatchProviderDumpIndex worker = new BatchProviderDumpIndex();
			worker.setDocsToCollect(bits);
			worker.run(req, queue);
		}
		else {
			throw new SolrException(ErrorCode.BAD_REQUEST, "The query " + query + " found nothing");
		}
	  
	}
	
	@Override
  public String getDescription() {
	  return "Generic provider which takes a query and dumps to disk in JSON format all retrieved docs";
  }

}
