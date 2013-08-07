package org.apache.solr.handler.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.DictionaryRecIdCache;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.FixedBitSet;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;


public class BatchProviderDumpBibcodes extends BatchProvider {
	
	private BatchProviderDumpIndexFields internalWorker = new BatchProviderDumpIndexFields();

	@Override
  public void run(SolrQueryRequest locReq, BatchHandlerRequestQueue queue) throws Exception {
	  SolrParams params = locReq.getParams();
	  String jobid = params.get("jobid");
	  String workDir = params.get("#workdir");
	  File input = new File(workDir + "/" + jobid + ".input");
	  if (!input.canRead()) {
	  	throw new SolrException(ErrorCode.BAD_REQUEST, "No input data available, bark bark - " + input);
	  }
	  
	  // we must harvest lucene docids
	  AtomicReader reader = locReq.getSearcher().getAtomicReader();
	  FixedBitSet bits = new FixedBitSet(reader.maxDoc());
	  
	  // normally, we should load the requested list of bibcodes
	  // because it will be always smaller than the whole index
	  // but since we are using the bibcodes in the second-order
	  // search, the lookup cache is already available
	  
	  Map<String, Integer> bibcodes = DictionaryRecIdCache.INSTANCE.getTranslationCacheString(locReq.getSearcher(), 
	  		new String[]{"bibcode", "alternate_bibcode"});
	  
	  // construct a filter
	  BufferedReader br = new BufferedReader(new FileReader(input));
	  String line;
	  while ((line = br.readLine()) != null) {
	     line = line.toLowerCase().trim();
	     if (bibcodes.containsKey(line)) {
	    	 bits.set(bibcodes.get(line));
	     }
	  }
	  br.close();
	  
	  if (bits.cardinality() <= 0) {
	  	return; // nothing to do
	  }
	  
	  
	  ModifiableSolrParams mParams = new ModifiableSolrParams( locReq.getParams() );
	  mParams.set(CommonParams.Q, "*:*");
	  locReq.setParams(mParams);
	  internalWorker.setFilter(new BitSetFilter(bits));
	  
	  internalWorker.run(locReq, queue);
	  
	  
  }
	
	public static final class BitSetFilter extends Filter {
		private FixedBitSet docs;
    
    public BitSetFilter(FixedBitSet docs) {
      this.docs = docs;
    }

    @Override
    public DocIdSet getDocIdSet(AtomicReaderContext context, Bits acceptDocs) {
      final FixedBitSet set = new FixedBitSet(context.reader().maxDoc());
      int docBase = context.docBase;
      final int limit = docBase+context.reader().maxDoc();
      int docId = docBase;
      while ((docId = docs.nextSetBit(docId)) != -1 && docId < limit) {
      	set.set(docId-docBase);
      	docId++;
      }
      return set.nextSetBit(0) == -1 ? null:set;
    }
    
  }
	
	@Override
  public String getDescription() {
	  return "Receives bibcodes and dumps selected fields to disk in JSON format";
  }
}
