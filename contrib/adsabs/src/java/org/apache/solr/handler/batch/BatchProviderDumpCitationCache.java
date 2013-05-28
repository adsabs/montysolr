package org.apache.solr.handler.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import org.apache.lucene.search.DictionaryRecIdCache;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldCache.DocTerms;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.JSONDumper;

/**
 * This dumps a CITEDBY data structure to disk
 *
 */
public class BatchProviderDumpCitationCache extends BatchProvider {
	
	
	public void run(SolrQueryRequest req, BatchHandlerRequestQueue queue) throws Exception {
		
		SolrParams params = req.getParams();
	  String jobid = params.get("jobid");
	  String workDir = params.get("#workdir");
	  String uniqueField = params.get("unique_field");
	  String refField = params.get("ref_field");
	  boolean returnDocids = params.getBool("return_docids", false);
	  
	  assert jobid != null && new File(workDir).canWrite();
	  
	  File jobFile = new File(workDir + "/" + jobid);
		final BufferedWriter out = new BufferedWriter(new FileWriter(jobFile), 1024*256);
		
	  DocTerms uniqueValueCache = FieldCache.DEFAULT.getTerms(req.getSearcher().getAtomicReader(), uniqueField);
	  
	  int[][] invertedIndex = DictionaryRecIdCache.INSTANCE.
				getUnInvertedDocidsStrField(req.getSearcher().getIndexReader(), 
				uniqueField, refField);

	  BytesRef ret = new BytesRef();
	  boolean first = true;
	  int i=0;
	  
	  if (!returnDocids) {
		  for (int[] referencedPapers: invertedIndex) {
		  	if (referencedPapers!= null && referencedPapers.length > 0) {
		  		uniqueValueCache.getTerm(i, ret);
		  		out.write(ret.utf8ToString());
		  		out.write("\t");
		  		first=true;
		  		for (int luceneDocId: referencedPapers) {
			  		ret = uniqueValueCache.getTerm(luceneDocId, ret);
					  if (ret.length > 0) {
					  	if (!first) {
					  		out.write("\t");
					  	}
					    out.write(ret.utf8ToString());
					    first = false;
					  }
		  		}
		  		out.write("\n");
		  	}
		  	i++;
		  }
	  }
	  else {
	  	for (int[] referencedPapers: invertedIndex) {
		  	if (referencedPapers!= null && referencedPapers.length > 0) {
		  		out.write(Integer.toString(i));
		  		out.write("\t");
		  		first=true;
		  		for (int luceneDocId: referencedPapers) {
				  	if (!first) {
				  		out.write("\t");
				  	}
				    out.write(Integer.toString(luceneDocId));
				    first = false;
		  		}
		  		out.write("\n");
		  	}
		  	i++;
		  }
	  }
	  
	  out.close();
	}

}
