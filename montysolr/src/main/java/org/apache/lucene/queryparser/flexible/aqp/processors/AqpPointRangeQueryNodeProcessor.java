package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.config.PointsConfig;
import org.apache.lucene.queryparser.flexible.standard.nodes.PointQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.PointRangeQueryNode;
import org.apache.lucene.queryparser.flexible.standard.processors.PointRangeQueryNodeProcessor;

/**
 * Replaces null numeric endpoints created by Lucene's point-range processor
 * with the same canonical bounds used by PointRangeQueryNodeBuilder.
 *
 * <p>The standard processor represents an open endpoint as a PointQueryNode
 * whose value is null. That is semantically valid for the builder, but its
 * {@code toString()} is not null-safe and the later AQP optimization pass
 * serializes point ranges while deduplicating boolean children.</p>
 */
public class AqpPointRangeQueryNodeProcessor extends PointRangeQueryNodeProcessor {

    @Override
    protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
        QueryNode processed = super.postProcessNode(node);
        if (!(processed instanceof PointRangeQueryNode range)) {
            return processed;
        }

        PointsConfig config = range.getPointsConfig();
        Class<?> type = config.getType();
        PointQueryNode lower = (PointQueryNode) range.getLowerBound();
        PointQueryNode upper = (PointQueryNode) range.getUpperBound();

        if (lower.getValue() == null) {
            lower.setValue(canonicalBound(type, true));
        }
        if (upper.getValue() == null) {
            upper.setValue(canonicalBound(type, false));
        }
        return range;
    }

    private Number canonicalBound(Class<?> type, boolean lower) throws QueryNodeException {
        if (Integer.class.equals(type)) {
            return lower ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        }
        if (Long.class.equals(type)) {
            return lower ? Long.MIN_VALUE : Long.MAX_VALUE;
        }
        if (Float.class.equals(type)) {
            return lower ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
        }
        if (Double.class.equals(type)) {
            return lower ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        }
        throw new QueryNodeException(new MessageImpl(
                "Unsupported numeric range type", type.getName()));
    }
}
