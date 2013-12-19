package org.apache.solr.search;

import java.util.HashMap;
import java.util.Map;

public class SolrParserConfigParams {
	public Map<String, Map<String, Float>> virtualFields;
	public SolrParserConfigParams() {
		virtualFields = new HashMap<String, Map<String, Float>>();
	}
}
