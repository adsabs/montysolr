package org.apache.solr.handler.dataimport;


public class NoRollbackDataImporter extends DataImporter {
	
  public void runCmd(RequestInfo reqParams, SolrWriter sw) {
	  NoRollbackWriter w = new NoRollbackWriter(null, sw.req);
	  w.setWriter(sw);
	  super.runCmd(reqParams, w);
  }
  
  /* only if the DIH uses its own interface
   */
  public void runCmd(RequestInfo reqParams, DIHWriter sw) {
//    Map<String, Object> params = reqParams.getRawParams();
//    params.put("writerImpl", NoRollbackWriter.class.getName());
//    reqParams = new RequestInfo(params, reqParams.getContentStream());

    NoRollbackWriter w = new NoRollbackWriter(null, ((SolrWriter) sw).req);
    w.setWriter(sw);
    super.runCmd(reqParams, w);
  }
  
}
