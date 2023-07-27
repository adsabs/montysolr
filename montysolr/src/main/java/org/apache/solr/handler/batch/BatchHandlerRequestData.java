package org.apache.solr.handler.batch;

import org.apache.solr.common.params.MultiMapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.servlet.SolrRequestParsers;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class BatchHandlerRequestData {
    public String url;
    public int count;
    private final SolrParams params;
    public BatchProvider handler;
    private String msg = null;

    public BatchHandlerRequestData(BatchProvider handler, SolrParams params) {
        this.url = params.toString();
        this.params = params;
        this.handler = handler;
    }

    public BatchHandlerRequestData(BatchProvider handler, String url) throws UnsupportedEncodingException {
        this.url = url;
        this.params = SolrRequestParsers.parseQueryString(this.url);
        this.handler = handler;
    }

    public SolrParams getReqParams() {
        return this.params;
    }

    public String toString() {
        return handler + "::" + url + (msg != null ? msg : "");
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    /*
     * we cannot use: SolrRequestParsers.parseQueryString(this.url);
     * because it will unencode the params, which is bad for us
     */
    private MultiMapSolrParams parseQueryString(String queryString) {
        Map<String, String[]> map = new HashMap<String, String[]>();
        if (queryString != null && queryString.length() > 0) {
            for (String kv : queryString.split("&")) {
                int idx = kv.indexOf('=');
                if (idx > 0) {
                    String name = kv.substring(0, idx);
                    String value = kv.substring(idx + 1);
                    MultiMapSolrParams.addParam(name, value, map);
                } else {
                    MultiMapSolrParams.addParam(kv, "", map);
                }
            }
        }
        return new MultiMapSolrParams(map);
    }

    public BatchProvider getProvider() {
        return handler;
    }
}
