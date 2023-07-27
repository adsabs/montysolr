package org.apache.solr.search;

import java.util.HashMap;
import java.util.Map;

public class SolrParserConfigParams {
    public Map<String, Map<String, Float>> virtualFields;
    public HashMap<String, String> params;

    public SolrParserConfigParams() {
        virtualFields = new HashMap<String, Map<String, Float>>();
        params = new HashMap<String, String>();
    }
}
