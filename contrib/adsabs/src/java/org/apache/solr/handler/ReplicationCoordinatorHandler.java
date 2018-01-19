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

  private int slaveCounters = 0;
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void init(NamedList args) {
    super.init(args);
  }

  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
      throws Exception {
    
    long gen = getIndexGeneration(req.getCore());
    if (gen != latestGeneration) {
      counters.clear();
      slaveCounters = 0;
    }
    
    SolrParams params = req.getParams();
    String event = params.get("event","info");
    String slaveid = params.get("hostid");
    
    if (slaveid == null) {
      log.error("Slave id must be present");
      return;
    }
    
    if (!counters.containsKey(slaveid))
      counters.put(slaveid, slaveCounters++);
    
    if (event.equals("give-me-delay")) {
      int order = counters.get(slaveid);
      
      if (order % 2 == 0) {
        rsp.add("delay", 0); // half of the slaves should start commits immediately
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

