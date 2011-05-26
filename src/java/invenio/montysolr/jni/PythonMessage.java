package invenio.montysolr.jni;

import java.util.AbstractCollection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PythonMessage extends HashMap<String, Object>{

	/**
	 *
	 */
	private static final long serialVersionUID = -3744935985066647405L;
	public static final Logger log = LoggerFactory.getLogger(PythonMessage.class);


	public PythonMessage(String receiver) {
		this.put("receiver", receiver);
	}

	public String getSender() {
		return (String) this.get("sender");
	}

	public PythonMessage setSender(String sender) {
		this.put("sender", sender);
		return this;
	}

	public String getReceiver() {
		return (String) this.get("receiver");
	}

	public PythonMessage setReceiver(String receiver) {
		this.put("receiver", receiver);
		return this;
	}


	public SolrQueryRequest getSolrQueryRequest() {
		return (SolrQueryRequest) this.get("SolrQueryRequest");
	}
	public PythonMessage setSolrQueryRequest(SolrQueryRequest sqr) {
		this.put("SolrQueryRequest", sqr);
		return this;
	}

	public SolrQueryResponse getSolrQueryResponse() {
		return (SolrQueryResponse) this.get("SolrQueryResponse");
	}
	public PythonMessage setSolrQueryResponse(SolrQueryResponse srp) {
		this.put("SolrQueryResponse", srp);
		return this;
	}

	public PythonMessage setParam(String name, Object value) {
		this.put(name, value);
		return this;
	}

	public Object getParam(String name) {
		return this.get(name);
	}

	public int[] getParamArray_int(String name) {
		return (int[]) this.get(name);
	}
	public String[] getParamArray_str(String name) {
		return (String[]) this.get(name);
	}

	public Object getResults() {
		return this.get("#result");
	}

	public void setResults(Object result) {
		this.put("#result", result);
	}


	public String toString() {
		Set<Entry<String, Object>> s = this.entrySet();
		StringBuilder out = new StringBuilder();
		for (Entry<String, Object> e: s) {
			out.append(e.getKey());
			out.append("=");
			Object v = e.getValue();
			if (v instanceof AbstractCollection) {
				if (((AbstractCollection) v).size() > 10) {
					out.append("@" + v.getClass());
				}
				else {
					out.append(v);
				}
			}
			else {
				out.append(v);
			}
			out.append(",");
		}
		return out.toString();
	}

	public void threadInfo(String s) {
		log.info("[Python] " + s + this.getInfo());
	}

	private String getInfo() {
		return "  [Thread=" + Thread.currentThread().getName() + " id=" + Thread.currentThread().getId() +
				" time=" + System.currentTimeMillis() + "]";
	}

}
