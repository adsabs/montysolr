package org.apache.solr.handler.batch;

import java.io.File;
import org.apache.lucene.util.Bits;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.JSONDumper;

/**
 * Provider which saves documents from the index in
 * JSON format on disk. 
 *
 */
public class BatchProviderDumpIndex extends BatchProvider {
	
	private Bits docsToCollect = null;

	public void setDocsToCollect(Bits docsToCollect) {
		this.docsToCollect = docsToCollect;
	}
	
	public void run(SolrQueryRequest req, BatchHandlerRequestQueue queue) throws Exception {
		
		SolrParams params = req.getParams();
	  String jobid = params.get("jobid");
	  String workDir = params.get("#workdir");
	  
	  assert jobid != null && new File(workDir).canWrite();
	  
	  File jobFile = new File(workDir + "/" + jobid);
    JSONDumper dumper = JSONDumper.create(req, jobFile, this.docsToCollect);
		dumper.writeResponse();
	  
	}
	
	@Override
  public String getDescription() {
	  return "Generic provider which dumps (selected docs) to disk in JSON format";
  }

}
