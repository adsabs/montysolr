package org.apache.solr.handler.dataimport;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.SolrLogFormatter;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
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
  private static final String BACKUP_HANDLER = "/invenio-failed-import";
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
    
    if (processedIds.size()==0 && pargs.ids.size() == 1) { // we didn't even get past the first document
      // we can report failed-document
      core.execute(backupHandler, req("command", "register-failed-doc", "recid", pargs.ids.get(0).toString()), rsp);
    }
    else { // we have to call indexing again
      List<Integer> docIds = pargs.ids;
      for (String docid: processedIds) {
        int d = Integer.parseInt(docid);
        assert docIds.contains(d);
        docIds.remove((int) docIds.indexOf(d));
      }
      if (docIds.size()==0) {
        core.execute(backupHandler, req("command", "register-failed-batch", "recid", pargs.origUrl), rsp);
      }
      else if (docIds.size()==1) {
        core.execute(backupHandler, req("command", "register-failed-doc", "recid", docIds.get(0).toString()), rsp);
      }
      else {
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
    }
    
    log.warn("Rollback was called (and not heeded)!");
    commit(false);
  }

  class ParsedArgs {
    public List<Integer> ids;
    public String origUrl;
    private String tmpl;
    
    public ParsedArgs(SolrParams params) {
      origUrl = (String) params.get("url");
      ids = new ArrayList<Integer>();
      processUrl(origUrl);
    }
    public String getUrl(int[] ids) {
      List<String> parts = InvenioKeepRecidUpdated.getQueryIds(ids.length, ids);
      return String.format(tmpl, parts.get(0));
    }
    
    private void processUrl(String url) {
      Map<String, List<String>> params = new HashMap<String, List<String>>();
      String[] urlParts = url.split("\\?");
      StringBuffer out = new StringBuffer();
      out.append(urlParts[0]);
      out.append("?");
      
      if (urlParts.length > 1) {
        for (String param : urlParts[1].split("&")) {
          String pair[] = param.split("=");
          if (pair[0].equals("p")) {
            out.append("p=%s");
            extractIds(pair[1]);
          }
          else {
            out.append(param);
          }
        }
      }
      else {
        out.append("p=%s");
      }
      tmpl = out.toString();
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
