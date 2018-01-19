package org.apache.solr.handler;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrConfig;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrEventListener;
import org.apache.solr.search.SolrIndexSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.apache.solr.client.solrj.impl.HttpSolrClient.Builder;
import org.apache.solr.client.solrj.request.QueryRequest;


public class ReplicationEventListener implements SolrEventListener {
  
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private AtomicInteger numberOfTimesCalled;
  private CountDownLatch latch;
  private CyclicBarrier barrier;
  private NamedList args;
  private SolrCore core;
  private String masterUrl = null;
  private String coordinatorEndpoint = "/coordinator";
  private HttpSolrClient httpClient;
  private int randHostId = new Random().nextInt(Integer.MAX_VALUE);
  
  public ReplicationEventListener(SolrCore core) {
    this.core = core;
  }
  
  public void init(NamedList args) {
    this.args = args.clone();
    masterUrl  = findMasterUrl(core.getSolrConfig());
    String foo = (String) args.get("endpoint");
    if (foo != null)
      coordinatorEndpoint = foo;
    latch = new CountDownLatch(1);
    barrier = new CyclicBarrier(1);
    numberOfTimesCalled = new AtomicInteger(0);
    httpClient = new Builder(masterUrl).build();
  }
  
  @Override
  public void postCommit() {
    notifyCoordinator("index-loaded");
  }

  @Override
  public void postSoftCommit() {}

  @Override
  public void newSearcher(SolrIndexSearcher newSearcher,
      SolrIndexSearcher currentSearcher) {
    if (currentSearcher == null || masterUrl == null)
      return; // initialization, don't do anything
    
    int delayInSecs = 0;
    try {
      // contact the remote master url, it should tell us how long to wait before activating the new index
      delayInSecs = contactReplicatorCoordinator("give-me-delay", "delay");
      
      notifyCoordinator("delaying-index", Integer.toString(delayInSecs));
      
      // simulate a slow searcher listener
      barrier.await(delayInSecs, TimeUnit.SECONDS);
      
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.warn("Delaying interrupted");
      notifyCoordinator("delay-interrupted", e.toString());
      throw new RuntimeException(e);
    } catch (BrokenBarrierException e) {
      log.error("Broken barrier, we are not delaying index deployment any more.");
      notifyCoordinator("congestion-problem", "we have waited too long, another thread tried to open a searcher"); // this will never happe IFF there is only one write thread
    } catch (TimeoutException e) {
      log.error("Time barrier timed out");
      notifyCoordinator("activating-new-index", "timeout :" + delayInSecs);
    }
    
    notifyCoordinator("activating-index", Integer.toString(delayInSecs));
    barrier.reset();
    numberOfTimesCalled.incrementAndGet();
    log.info("Delaying action of (" + delayInSecs + "s.) passed. We have interfered x=" + numberOfTimesCalled + " times. Good luck!");
  }
  
  private Integer contactReplicatorCoordinator(String verb, String retrieve) {
    NamedList<Object> r = request(verb, null);
    String out = (String) r.get(retrieve);
    if (out == null) {
      log.error("Error contacting/getting data from coordinator.");
      return 0;
    }
    try {
      return Integer.parseInt(out);
    }
    catch (Exception e ) {
      return 0;
    }
  }
  
  private void notifyCoordinator(String event) {
    request(event, null);
  }
  private void notifyCoordinator(String event, String value) {
    request(event, value);
  }
  
  private NamedList<Object> request(String event, String value) {
    ModifiableSolrParams params = new ModifiableSolrParams();
    
    params.set("q", "foo");
    params.set("event", event);
    if (value != null)
      params.set("value", value);
    try {
      params.set("ip", InetAddress.getLocalHost().toString());
    } catch (UnknownHostException e) {
      params.set("ip", "0.0.0.0");
    }
    try {
      params.set("hostname", InetAddress.getLocalHost().getHostName().toString());
    } catch (UnknownHostException e) {
      params.set("hostname", "unknown");
    }
    params.set("hostid", randHostId);
    
    QueryRequest request = new QueryRequest(params);
    request.setPath(coordinatorEndpoint);
    
    log.info("Contacting coordinator: ", request);
    
    try {
      return httpClient.request(request);
    } catch (SolrServerException | IOException e) {
      log.error(e.toString());
      return null;
    }
  }
  private String findMasterUrl(SolrConfig config) {

    Node a = config.getNode("//config/requestHandler[@name='/replication']/lst[@name='slave']/str[@name='masterUrl']", false);
    if (a == null)
      return null;
          
    String url = a.getTextContent();
    return url.trim();
    
  }
  
}