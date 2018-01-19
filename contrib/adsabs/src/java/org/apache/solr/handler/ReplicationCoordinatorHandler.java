package org.apache.solr.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.ReplicationHandler.CommitVersionInfo;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.util.RefCounted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReplicationCoordinatorHandler extends RequestHandlerBase {

  public static final Logger log = LoggerFactory
      .getLogger(ReplicationCoordinatorHandler.class);
  
  private Map<String, Integer> counters = new HashMap<String, Integer>();
  private Long latestGeneration = null;
  private int maxDelay = 15 * 60;
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void init(NamedList args) {
    super.init(args);
  }

  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
      throws Exception {
    
    long gen = getIndexGeneration(req.getCore());
    if (gen != latestGeneration) {
      counters.clear();
    }
    
    SolrParams params = req.getParams();
    String event = params.get("event","info");
    String slaveid = params.get("hostid");
    
    if (slaveid == null) {
      log.error("Slave id must be present");
      return;
    }
    
    
    
    if (event.equals("give-me-delay")) {
      if (counters.containsKey(slaveid)) {
        if (counters.get(slaveid))
      }
      else {
        if (counters.size() % 2 == 0) { // every even request receives zero delay
          counters.put(info.id, 0);
          rsp.add("delay", 0);
        }
        else {
          int d = calculateDelay(counters.size()+1);
          counters.put(info.id, d);
          rsp.add("delay", d);
        }
      }
    }
    
  }
  
  
  /**
   * returns the CommitVersionInfo for the current searcher, or null on error.
   */
  private long getIndexGeneration(SolrCore core) {
    
    RefCounted<SolrIndexSearcher> searcher = core.getSearcher();
    try {
      return searcher.get().getIndexReader().getIndexCommit().getGeneration();
    } catch (IOException e) {
      return -1;
    } finally {
      searcher.decref();
    }
  }



  @Override
  public String getSource() {
    return "";
  }


  @Override
  public String getVersion() {
    return "";
  }

  

}

