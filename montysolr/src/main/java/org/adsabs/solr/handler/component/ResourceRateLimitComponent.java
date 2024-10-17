package org.adsabs.solr.handler.component;

import com.codahale.metrics.Counter;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.PointRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryVisitor;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.handler.component.SearchComponent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Computes a "resource utilization score" per user query and determines whether the query
 * would take us over the maximum resource units allotted to this Solr instance.
 */
public class ResourceRateLimitComponent extends SearchComponent {
    public static final String RESOURCE_RATE_LIMIT_PREFIX = "resourceRatelimit.";

    /**
     * int - sets the maximum number of resource units the searcher will handle before rejecting requests
     */
    public static final String TOTAL_ALLOTTED_UNITS = RESOURCE_RATE_LIMIT_PREFIX + "totalAllottedUnits";

    /**
     * Default max allotment of resource units per Solr instance.
     */
    public static final int DEFAULT_ALLOTMENT = 10_000;

    /**
     * float - controls the resource unit multiplier applied by second order operators when doing query calculations
     */
    public static final String SECOND_ORDER_MULTIPLIER = RESOURCE_RATE_LIMIT_PREFIX + "secondOrderMultiplier";

    /**
     * Default second order multiplier value.
     */
    public static final float DEFAULT_SECOND_ORDER_MULTIPLIER = 10.0f;

    /**
     * int - once the rate limit has been triggered wait until total use is below this threshold before allowing
     *       further requests. Set to the maximum or 0 to disable this behavior.
     */
    public static final String SAFE_THRESHOLD = RESOURCE_RATE_LIMIT_PREFIX + "safeThreshold";

    /**
     * Default safe threshold.
     */
    public static final int DEFAULT_SAFE_THRESHOLD = DEFAULT_ALLOTMENT;

    private NamedList<?> initArgs;

    protected int totalAllottedUnits;
    protected int safeThreshold;
    protected float secondOrderMultiplier;

    protected AtomicInteger activeResourceUnits = new AtomicInteger(0);

    @Override
    public void init(NamedList<?> args) {
        super.init(args);
        this.initArgs = args.clone();

        if (initArgs != null) {
            totalAllottedUnits = initArgs.toSolrParams().getInt(TOTAL_ALLOTTED_UNITS, DEFAULT_ALLOTMENT);
            safeThreshold = initArgs.toSolrParams().getInt(SAFE_THRESHOLD, DEFAULT_SAFE_THRESHOLD);
            secondOrderMultiplier = initArgs.toSolrParams().getFloat(SECOND_ORDER_MULTIPLIER,
                    DEFAULT_SECOND_ORDER_MULTIPLIER);
        } else {
            totalAllottedUnits = DEFAULT_ALLOTMENT;
            safeThreshold = DEFAULT_SAFE_THRESHOLD;
            secondOrderMultiplier = DEFAULT_SECOND_ORDER_MULTIPLIER;
        }
    }

    @Override
    public void prepare(ResponseBuilder rb) throws IOException {
        int unitsUsed = calculateResourceUnits(rb.getQuery());
        rb.rsp.add("resources-used", unitsUsed);

        while(true) {
            int currentUnits = activeResourceUnits.intValue();
            if (currentUnits + unitsUsed >= totalAllottedUnits) {
                throw new SolrException(SolrException.ErrorCode.TOO_MANY_REQUESTS,
                        "Server over global request ratelimit." +
                                " This is not a per-user ratelimit; please try your request again later.");
            }

            // Only continue once/if we're up-to-date
            if (activeResourceUnits.compareAndSet(currentUnits, currentUnits + unitsUsed)) {
                break;
            }
        }
    }

    protected int calculateResourceUnits(Query q) {
        ResourceCalculator calc = new ResourceCalculator(q);
        q.visit(calc);
        return calc.getUnits();
    }

    @Override
    public void process(ResponseBuilder rb) throws IOException {
        Integer unitsUsed = (Integer) rb.rsp.getValues().get("resources-used");
        int finalUnits = activeResourceUnits.updateAndGet(value -> value - unitsUsed);

        rb.rsp.add("total-resource-count", finalUnits);
    }

    @Override
    public String getDescription() {
        return "Rate limits requests based on resources used per query type.";
    }

    public static class ResourceCalculator extends QueryVisitor {
        private Optional<Query> rootQuery;
        private Optional<ResourceCalculator> currentSubCalc;
        private int units = 1;

        protected static final Map<Class<?>, Integer> baseQueryValues;

        static {
            baseQueryValues = new HashMap<>();
            baseQueryValues.put(PointRangeQuery.class, 1);
        }

        public ResourceCalculator(Query rootQuery) {
            this.rootQuery = Optional.of(rootQuery);
            this.currentSubCalc = Optional.empty();
        }

        public int getUnits() {
            if (currentSubCalc.isPresent()) {
                currentSubCalc.ifPresent(resourceCalculator -> units += resourceCalculator.getUnits());
                currentSubCalc = Optional.empty();
            }

            return units;
        }

        @Override
        public QueryVisitor getSubVisitor(BooleanClause.Occur occur, Query parent) {
            currentSubCalc.ifPresent(resourceCalculator -> units += resourceCalculator.getUnits());
            currentSubCalc = Optional.of(new ResourceCalculator(parent));

            return currentSubCalc.get();
        }

        @Override
        public void visitLeaf(Query query) {
            currentSubCalc.ifPresent(resourceCalculator -> units += resourceCalculator.getUnits());
            currentSubCalc = Optional.empty();

            units += getBaseQueryUnitValue(query);
        }

        protected int getBaseQueryUnitValue(Query query) {
            if (query == null)
                return 0;

            Class<?> c = query.getClass();
            while (c != null && !c.equals(Object.class)) {
                if (baseQueryValues.containsKey(c))
                    return baseQueryValues.get(c);

                c = c.getSuperclass();
            }

            return 1;
        }
    }
}
