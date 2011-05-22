package org.apache.solr.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.handler.component.SearchHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.solr.util.WebUtils;



public class InvenioHandler extends SearchHandler {

	public static final Logger log = LoggerFactory
			.getLogger(InvenioHandler.class);


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


		super.handleRequestBody(req, rsp);
	}


}
