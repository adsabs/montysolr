package org.apache.solr.handler;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.security.AuthorizationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdsConfigHandler extends RequestHandlerBase {

    public static final Logger log = LoggerFactory
            .getLogger(AdsConfigHandler.class);

    private final Map<String, SolrParams> components = new HashMap<String, SolrParams>();
    private SolrParams emptyParams = null;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void init(NamedList args) {
        super.init(args);
        for (int i = 0; i < args.size(); i++) {
            String n = args.getName(i);
            Object v = args.getVal(i);
            if (v != null) {
                if (v instanceof NamedList) {
                    components.put(n, SolrParams.toSolrParams((NamedList) v));
                } else if (v instanceof String) {
                    NamedList nl = new NamedList();
                    nl.add(n, v);
                    components.put(n, SolrParams.toSolrParams(nl));
                } else if (v instanceof List) {
                    ArrayList<String> vv = (ArrayList<String>) v;
                    NamedList nl = new NamedList();
                    for (int i1 = 0; i1 < vv.size(); i1++) {
                        nl.add(Integer.toString(i1), vv.get(i1));
                    }
                    components.put(n, SolrParams.toSolrParams(nl));
                }
            }
        }
        emptyParams = SolrParams.toSolrParams(new NamedList());
    }

    public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
            throws Exception {

        SolrParams params = req.getParams();
        String command = params.get("command", "info");

        if (command.equals("reopenSearcher")) {
            req.getCore().getSearcher(true, false, null, false);
        }

    }

    public boolean hasParams(String key) {
        return components.containsKey(key);
    }

    public SolrParams getParams(String key) {
        if (components.containsKey(key)) {
            return components.get(key);
        }
        return emptyParams;
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Current config: ");
        if (components != null) {
            for (String c : components.keySet()) {
                sb.append(components.get(c).toString());
                sb.append(",");
            }
        }
        return sb.toString();
    }

    @Override
    public Name getPermissionName(AuthorizationContext request) {
        return Name.CORE_EDIT_PERM;
    }
}
