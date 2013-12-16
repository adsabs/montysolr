package org.apache.solr.handler.dataimport;

import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoRollbackWriter extends SolrWriter {
  private static final Logger log = LoggerFactory.getLogger(NoRollbackWriter.class);
  
  
  public NoRollbackWriter(UpdateRequestProcessor processor, SolrQueryRequest req) {
    super(processor, req);
  }
  
  
  public void rollback() {
    log.error("Rollback was called (but we ignore it and commit)!");
    commit(false);
  }


}
