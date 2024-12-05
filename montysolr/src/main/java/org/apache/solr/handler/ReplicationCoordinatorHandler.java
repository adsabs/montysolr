package org.apache.solr.handler;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.security.AuthorizationContext;
import org.apache.solr.security.PermissionNameProvider;
import org.apache.solr.util.RefCounted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This component implements the saw tooth rising/failing response.
 * i.e. contacting components will sleep before proceeding to re-opening
 * the new index.
 * <p>
 * 100%|   --      --      --
 * |         -       -
 * 50%|       -       -
 * |     -       -
 * 0% ----------------------
 */

public class ReplicationCoordinatorHandler extends RequestHandlerBase {

    public static final Logger log = LoggerFactory
            .getLogger(ReplicationCoordinatorHandler.class);

    private final Map<String, Integer> counters = new HashMap<String, Integer>();
    private Long latestGeneration = 0L;
    private int maxDelay = 15 * 60;
    private int currentPosition = 0;
    private int numInstances = 5;

    private float[] cycles;


    @SuppressWarnings({"rawtypes", "unchecked"})
    public void init(NamedList args) {
        super.init(args);
        int numTop = 1;
        int numBottom = 2;

        if (args.get("numInstances") != null)
            numInstances = Integer.parseInt((String) args.remove("numInstances"));
        if (args.get("numTop") != null)
            numTop = Integer.parseInt((String) args.remove("numTop"));
        if (args.get("numBottom") != null)
            numBottom = Integer.parseInt((String) args.remove("numBottom"));
        if (args.get("maxDelay") != null)
            maxDelay = Integer.parseInt((String) args.remove("maxDelay"));

        assert (numInstances - (numTop + numBottom) >= 1);
        int numRest = Math.max(numInstances - (numTop + numBottom) + 1, 2);

        if (args.get("startingPosition") != null)
            currentPosition = Integer.parseInt((String) args.remove("startingPosition"));

        cycles = new float[numInstances];
        int i = 0;
        while (numTop > 0) {
            cycles[i] = 1.0f;
            numTop--;
            i++;
        }

        while (numBottom > 0) {
            cycles[i] = 0.0f;
            numBottom--;
            i++;
        }

        float step = 1.0f / (float) numRest;
        float s = step;
        while (numRest > 0 && i < cycles.length) {
            cycles[i] = s;
            s += step;
            numRest--;
            i++;
        }


    }

    public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
            throws Exception {

        long gen = getIndexGeneration(req.getCore());
        if (gen != latestGeneration) {
            counters.clear();
            latestGeneration = gen;
        }

        SolrParams params = req.getParams();
        String event = params.get("event", "info");
        String slaveid = params.get("hostid");

        if (slaveid == null) {
            log.error("Slave id must be present");
            throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "slaveid is missing");
        }

        if (!counters.containsKey(slaveid))
            counters.put(slaveid, 0);
        else
            counters.put(slaveid, counters.get(slaveid) + 1);

        if (event.equals("give-me-delay")) {
            if (currentPosition == numInstances)
                currentPosition = 0;
            rsp.add("delay", cycles[currentPosition] * maxDelay);
            currentPosition++;
        } else {
            rsp.add("instances", counters);
            rsp.add("numInstances", numInstances);
            StringBuilder sb = new StringBuilder();
            for (float f : cycles) {
                sb.append(f * maxDelay);
                sb.append(" ");
            }
            rsp.add("cycles", sb.toString());
            rsp.add("maxDelay", maxDelay);
            rsp.add("currentPosition", currentPosition);
        }

    }


    /**
     * returns the CommitVersionInfo for the current searcher, or null on error.
     */
    private long getIndexGeneration(SolrCore core) {

        RefCounted<SolrIndexSearcher> searcher = core.getSearcher();
        try {
            return searcher.get().getIndexReader().getIndexCommit().getGeneration();
        } catch (IOException e) {
            return -1;
        } finally {
            searcher.decref();
        }
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Name getPermissionName(AuthorizationContext authorizationContext) {
        return Name.READ_PERM;
    }
}

