package org.apache.solr.handler;


import java.io.IOException;

import montysolr.jni.MontySolrVM;
import montysolr.jni.PythonMessage;

import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class PythonDiagnosticHandler extends RequestHandlerBase {

	public static final Logger log = LoggerFactory
			.getLogger(PythonDiagnosticHandler.class);


	public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws IOException
			 {
		SolrParams params = req.getParams();
		String q = params.get(CommonParams.Q);

		log.info("======= start diagnostics =======");
		
		PythonMessage message = MontySolrVM.INSTANCE
			.createMessage("diagnostic_test")
			.setParam("query", q);

		MontySolrVM.INSTANCE.sendMessage(message);

		Object result = message.getResults();
		if (result != null) {
			String res = (String) result;
			rsp.add("diagnostic_message", res);
			log.info("Diagnostic message: \n" + res);
		}
		else {
			log.info("Diagnostic message: null");
		}

		
		log.info("======== end diagnostics ========");

	}




	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}


}