package org.apache.solr.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.component.SearchHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.solr.util.WebUtils;


/**
 * The following handler simply takes the arguments inside inv.params
 * and puts them into the request object, so that they can be retrieved
 * inside the response paramater later on. This is needed for communication
 * with Invenio and also it may be needed by the processors that format
 * the query output.
 * 
 * @author rchyla
 *
 */
public class InvenioRequestHandler extends RequestHandlerBase {
  
  private String parentHandler = "standard";
  
	public static final Logger log = LoggerFactory
			.getLogger(InvenioRequestHandler.class);


	public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
			throws Exception {
		SolrParams params = req.getParams();
		String q = params.get(CommonParams.Q);
		
		// get the invenio parameters and set them into the request
		String invParams = params.get("inv.params");
		Map<String, String> qs = null;
		if (invParams != null) {
			qs = WebUtils.parseQueryString(invParams);
		}
		else {
			log.warn("Received no parameters from Invenio (inv.params)");
			qs = new HashMap<String, String>();
		}
		Map<Object, Object> context = req.getContext();
		context.put("inv.params", qs);

		SolrRequestHandler stdh = getParentHandler(req);
		stdh.handleRequest(req, rsp);
		
	}


  @Override
  public String getDescription() {
    return "InvenioRequestHandler makes sure inv.params are set and forward the request to: " + parentHandler;
  }


  @Override
  public String getSource() {
    return "$URL: http://github.com/romanchyla/montysolr/contrib/invenio/src/java/org/apache/solr/handler/InvenioRequestHandler.java $";
  }
  
  private SolrRequestHandler getParentHandler(SolrQueryRequest req) {
    SolrRequestHandler stdh = req.getCore().getRequestHandler(parentHandler);
    if (stdh == null) {
      throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Wrong configuration, the handler with name: " + parentHandler + " is missing");
    }
    return stdh;
  }

}
