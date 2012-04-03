package org.apache.solr.handler.dataimport;

import invenio.montysolr.PythonCall;
import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonMessage;

import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import org.apache.solr.common.SolrException;
import org.apache.solr.util.WebUtils;

public class InvenioDataSource extends URLDataSource implements PythonCall {
	
	
	private String pyFuncName = "invenio_search";

	@Override
	public Reader getData(String query) {
		if (query.substring(0, 9).equals("python://")) {
			URI uri;
			try {
				uri = new URI(query);
			} catch (URISyntaxException e1) {
				throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
						e1.getMessage());
			}
			
			String q = uri.getQuery();
			Map<String, List<String>> params;
			try {
				params = WebUtils.parseQuery(q);
				
			} catch (UnsupportedEncodingException e) {
				throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
						e.getMessage());
			}
			PythonMessage message = MontySolrVM.INSTANCE
				.createMessage(getPythonFunctionName())
				//.setSender(this.getClass().getSimpleName())
				.setParam("kwargs", params);
			
			MontySolrVM.INSTANCE.sendMessage(message);

			Object results = message.getResults();
			if (results == null) {
				LOG.info("No new/updated/deleted records inside Invenio.");
				return null;
			}
			else {
				return new StringReader((String) results);
			}
		}
		else {
			return super.getData(query);
		}
	}

	public void setPythonFunctionName(String name) {
		pyFuncName = name;
		
	}

	public String getPythonFunctionName() {
		return pyFuncName;
	}
}
