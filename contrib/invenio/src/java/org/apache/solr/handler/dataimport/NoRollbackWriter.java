package org.apache.solr.handler.dataimport;

import java.util.Map;
import java.util.Set;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoRollbackWriter extends SolrWriter {
  private static final Logger log = LoggerFactory.getLogger(NoRollbackWriter.class);
  private DIHWriter writer = null;
  
  public NoRollbackWriter() {
    super(null, null);
    writer = this;
  }
  
  public NoRollbackWriter(UpdateRequestProcessor processor, SolrQueryRequest req) {
    super(processor, req);
    writer = this;
  }
  
  public void setWriter(DIHWriter writer) {
    this.writer = writer;
  }
  

  public void commit(boolean optimize) {
    if (this.equals(writer)) {
      super.commit(optimize);
    }
    else {
      writer.commit(optimize);
    }
    
  }

  public void close() {
    if (this.equals(writer)) {
      super.close();
    }
    else {
      writer.close();
    }
  }
  
  public void rollback() {
    log.warn("Rollback was called (and not heeded)!");
    commit(false);
  }

  public void deleteByQuery(String q) {
    if (this.equals(writer)) {
      super.deleteByQuery(q);
    }
    else {
      writer.deleteByQuery(q);
    }
  }

  public void doDeleteAll() {
    if (this.equals(writer)) {
      super.doDeleteAll();
    }
    else {
      writer.doDeleteAll();
    }
  }

  public void deleteDoc(Object key) {
    if (this.equals(writer)) {
      super.deleteDoc(key);
    }
    else {
      writer.deleteDoc(key);
    }
  }

  public boolean upload(SolrInputDocument doc) {
    if (this.equals(writer)) {
      return super.upload(doc);
    }
    else {
      return writer.upload(doc);
    }
  }

  public void init(Context context) {
    if (this.equals(writer)) {
      super.init(context);
    }
    else {
      writer.init(context);
    }
  }

  public void setDeltaKeys(Set<Map<String, Object>> deltaKeys) {
    if (this.equals(writer)) {
      super.setDeltaKeys(deltaKeys);
    }
    else {
      writer.setDeltaKeys(deltaKeys);
    }
  }
}
