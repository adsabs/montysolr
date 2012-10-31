package org.apache.solr.handler.dataimport;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.MultiMapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.NamedList.NamedListEntry;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.servlet.SolrRequestParsers;
import org.apache.solr.update.InvenioKeepRecidUpdated;
import org.apache.solr.update.processor.UpdateRequestProcessor;
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
      else if (pargs.ids.size()==1) { // the last doc is erroneous
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
    
    log.error("Rollback was called (but we ignore it and commit)!");
    commit(false); // anything bad happens if we don't call commit?
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
    
    int half = Math.max(docIds.size()/2, 1);
    int[] ids = new int[half];
    for (int i=0;i<half;i++) {
      ids[i] = docIds.get(i);
    }
    core.execute(backupHandler, req("command", "register-new-batch", 
        "url", pargs.getUrl(ids)), rsp);
    
    ids = new int[docIds.size()-half];
    
    if (ids.length < 1) return;
    
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
    private String queryParams;
    
    public ParsedArgs(SolrParams params) {
      ids = new ArrayList<Integer>();
      processUrl(params);
    }
    public String getUrl(int[] ids) {
      List<String> parts = InvenioKeepRecidUpdated.getQueryIds(ids.length, ids);
      try {
        String recids = URLEncoder.encode( parts.get(0), "UTF-8" );
        return tmpl.replace("_____", URLEncoder.encode(queryParams.replace("_____", recids), "UTF-8"));
        //return URLEncoder.encode(url, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, e);
      }
    }
    
    private void processUrl(SolrParams params) {
      ModifiableSolrParams modPar = new ModifiableSolrParams(params);
      
      assert modPar.get("url", null) != null;
      assert modPar.get("url", "").contains("?");
      
      String[] pair = modPar.get("url").split("\\?");
      ModifiableSolrParams q = new ModifiableSolrParams(SolrRequestParsers.parseQueryString(pair[1]));
      if (q.get("p", null) != null) {
        extractIds(q.get("p"));
        q.set("p", "_____");
      }
      
      origUrl = modPar.toString();
      modPar.set("url", "_____");
      this.tmpl = modPar.toString();
      this.queryParams = pair[0] + "?" + q.toString();
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
  
  @SuppressWarnings("unchecked")
  public SolrQueryRequest req(String ... q) {
    if (q.length%2 != 0) { 
      throw new RuntimeException("The length of the string array (query arguments) needs to be even");
    }
    Map.Entry<String, String>[] entries = new NamedListEntry[q.length / 2];
    for (int i = 0; i < q.length; i += 2) {
      entries[i/2] = new NamedListEntry<String>(q[i], q[i+1]);
    }
    return new LocalSolrQueryRequest(req.getCore(), new NamedList(entries));
  }

}
