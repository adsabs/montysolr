package org.apache.solr.handler.dataimport;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.solr.SolrLogFormatter;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.NamedList.NamedListEntry;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.InvenioKeepRecidUpdated;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.apache.solr.util.TestHarness;
import org.apache.solr.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FailSafeInvenioNoRollbackWriter extends SolrWriter {
  private static final Logger log = LoggerFactory.getLogger(FailSafeInvenioNoRollbackWriter.class);
  private static final String INVENIO_ID = "id";
  private static final String BACKUP_HANDLER = "/invenio-doctor";
  private List<String> processedIds = new ArrayList<String>();
  
  public FailSafeInvenioNoRollbackWriter(UpdateRequestProcessor processor, SolrQueryRequest req) {
    super(processor, req);
    processedIds.clear();
  }
  
  @Override
  public boolean upload(SolrInputDocument d) {
    boolean val = super.upload(d);
    SolrInputField f = d.getField(INVENIO_ID);
    processedIds.add((String) f.getFirstValue());
    return val;
  }
  
  /*
   * We must decide whether we'll invoke ourselves with a different
   * set of parameters. 
   * 
   * @see org.apache.solr.handler.dataimport.SolrWriter#rollback()
   */
  public void rollback() {
    
    SolrCore core = req.getCore();
    SolrParams params = req.getOriginalParams();
    SolrRequestHandler backupHandler = req.getCore().getRequestHandler(BACKUP_HANDLER);
    
    ParsedArgs pargs = new ParsedArgs(params);
    SolrQueryResponse rsp = new SolrQueryResponse();
    
    if (processedIds.size()==0 ) { // we didn't even get past the first document
      if (pargs.ids.size() == 1) {
        // we'll report only if the recid contains one doc
        core.execute(backupHandler, req("command", "register-failed-doc", "recid", pargs.ids.get(0).toString()), rsp);
      }
      else {
        // but likely, the first failed is erroneous, so let's create three batches
        core.execute(backupHandler, req("command", "register-new-batch", 
            "url", pargs.getUrl(new int[]{pargs.ids.get(0)})), rsp);
        pargs.ids.remove(0);
        callProcessingAgain(pargs);
      }
    }
    else { // we have to call indexing again
      removeProcessed(pargs.ids);
      if (pargs.ids.size()==0) {
        core.execute(backupHandler, req("command", "register-failed-batch", "recid", pargs.origUrl), rsp);
      }
      else if (pargs.ids.size()==1) {
        core.execute(backupHandler, req("command", "register-failed-doc", "recid", pargs.ids.get(0).toString()), rsp);
      }
      else {
        callProcessingAgain(pargs);
      }
    }
    
    log.warn("Rollback was called (and not heeded)!");
    commit(false);
  }
  
  protected SolrQueryRequest getReq() {
    return req;
  }
  
  private void removeProcessed(List<Integer> docIds) {
    for (String docid: processedIds) {
      int d = Integer.parseInt(docid);
      assert docIds.contains(d);
      docIds.remove((int) docIds.indexOf(d));
    }
  }
  
  private void callProcessingAgain(ParsedArgs pargs) {
    SolrCore core = req.getCore();
    SolrRequestHandler backupHandler = req.getCore().getRequestHandler(BACKUP_HANDLER);
    SolrQueryResponse rsp = new SolrQueryResponse();
    
    List<Integer> docIds = pargs.ids;
    
    int half = docIds.size()/2;
    int[] ids = new int[half];
    for (int i=0;i<half;i++) {
      ids[i] = docIds.get(i);
    }
    core.execute(backupHandler, req("command", "register-new-batch", 
        "url", pargs.getUrl(ids)), rsp);
    
    ids = new int[docIds.size()-half];
    int j = 0;
    for (int i=half;i<docIds.size();i++,j++) {
      ids[j] = docIds.get(i);
    }
    core.execute(backupHandler, req("command", "register-new-batch", 
        "url", pargs.getUrl(ids)), rsp);
  }

  class ParsedArgs {
    public List<Integer> ids;
    public String origUrl;
    private String tmpl;
    
    public ParsedArgs(SolrParams params) {
      ids = new ArrayList<Integer>();
      processUrl(params);
    }
    public String getUrl(int[] ids) {
      List<String> parts = InvenioKeepRecidUpdated.getQueryIds(ids.length, ids);
      return tmpl.replace("_____", parts.get(0));
    }
    
    private void processUrl(SolrParams params) {
      
      ModifiableSolrParams modPar = new ModifiableSolrParams(params);
      origUrl = modPar.toString();
      
      StringBuilder template = new StringBuilder();
      
      String[] pair = params.get("url").split("\\?");
      if (pair.length>1) {
        template.append(pair[0] + "?");
        boolean first=true;
        for (String pp: pair[1].split("&")) {
          if (!first) template.append("&");
          first=false;
          String[] vals = pp.split("=");
          if (vals[0].equals("p")) {
            template.append("p=_____");
            extractIds(vals[1]);
          }
        }
      }
      else {
        template.append(pair[0] + "&p=%s");
      }
      modPar.set("url", template.toString());
      this.tmpl = modPar.toString();
    }
    
    private void extractIds(String pValue) {
      String[] orClauses = pValue.split(" OR ");
      for (String s: orClauses) {
        s = s.replace("recid:", "");
        if (s.indexOf("->") > -1) {
          String[] range = s.split("->");
          int max = Integer.parseInt(range[1]);
          for (int i=Integer.parseInt(range[0]);i<=max;i++) {
            ids.add(i);
          }
        }
        else {
          ids.add(Integer.parseInt(s));
        }
      }
    }
  }
  
  public SolrQueryRequest req(String ... q) {
    if (q.length%2 != 0) { 
      throw new RuntimeException("The length of the string array (query arguments) needs to be even");
    }
    Map.Entry<String, String> [] entries = new NamedListEntry[q.length / 2];
    for (int i = 0; i < q.length; i += 2) {
      entries[i/2] = new NamedListEntry<String>(q[i], q[i+1]);
    }
    return new LocalSolrQueryRequest(req.getCore(), new NamedList(entries));
  }

}
