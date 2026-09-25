package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNearQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpPostAnalysisProcessor;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.MatchNoDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.queries.spans.SpanNearQuery;
import org.apache.lucene.queries.spans.SpanQuery;

import java.util.List;

/**
 * The builder for the {@link AqpNearQueryNode}, example query:
 *
 * <pre>
 *   dog NEAR/5 cat
 *  </pre>
 *
 * <p>
 * After the AST tree was parsed, and synonyms were found,
 * we may have the following tree:
 *
 *
 * <pre>
 *        AqpNearQueryNode(5)
 *                |
 *            ------------------------------
 *           /                              \
 *         OR                         QueryNode(cat)
 *          |
 *       -----------------
 *      /                 \
 *   QueryNode(dog)     QueryNode(canin)
 *
 *  </pre>
 *
 *
 * <p>
 * Since Lucene cannot handle these queries, the flex builder
 * must rewrite them, effectively producing
 *
 * <pre>
 * SpanNear(SpanOr(dog | cat), SpanTerm(cat), 5)
 * </pre>
 * <p>
 * <p>
 * This builder does not know (yet) how to handle cases of
 * mixed boolean operators, eg.
 *
 * <pre>
 * (dog AND (cat OR fat)) NEAR/5 batman
 * </pre>
 *
 * @see AqpNearQueryNode
 */
public class AqpNearQueryNodeBuilder implements QueryBuilder {

    public AqpNearQueryNodeBuilder() {
        // empty constructor
    }

    public Object build(QueryNode queryNode) throws QueryNodeException {
        AqpNearQueryNode nearNode = (AqpNearQueryNode) queryNode;

        SpanConverter converter = new SpanConverter();
        List<QueryNode> children = nearNode.getChildren();

        if (children == null) {
            throw new QueryNodeException(new MessageImpl(
                    QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                    "Illegal state for: " + nearNode));
        }

        if (children.size() <= 1) {
            if (children.isEmpty()) {
                return new MatchNoDocsQuery();
            }

            Object obj = children.get(0).getTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);
            if (obj instanceof Query) {
                return (Query) obj;
            }

            throw new QueryNodeException(new MessageImpl(
                    QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                    "One of the clauses inside AqpNearQueryNode is null"));
        }

        SpanQuery[] clauses = new SpanQuery[children.size()];
        int i = 0;
        for (QueryNode child : children) {
            Object obj = child.getTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);
            if (obj != null) {
                float boost = 1.0f;
                if (obj instanceof BoostQuery) {
                    boost = ((BoostQuery) obj).getBoost();
                }
                SpanQuery result = converter.getSpanQuery(new SpanConverterContainer(
                        (Query) obj, nearNode.getSlop(), nearNode.getInOrder(), boost));
                clauses[i++] = result;
            } else {
                throw new QueryNodeException(new MessageImpl(
                        QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                        "One of the clauses inside AqpNearQueryNode is null"));
            }
        }

        int[] rawGaps = (int[]) nearNode.getTag(
                AqpPostAnalysisProcessor.RAW_POSITIONAL_GAPS);
        if (rawGaps != null) {
            if (rawGaps.length != clauses.length || rawGaps.length == 0
                    || rawGaps[0] != 0) {
                throw new QueryNodeException(new MessageImpl(
                        QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                        "Invalid fixed-gap positions for AqpNearQueryNode"));
            }
            SpanNearQuery.Builder builder = new SpanNearQuery.Builder(
                    clauses[0].getField(), nearNode.getInOrder());
            for (i = 0; i < clauses.length; i++) {
                if (rawGaps[i] < 0) {
                    throw new QueryNodeException(new MessageImpl(
                            QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                            "Negative fixed gap in AqpNearQueryNode"));
                }
                if (rawGaps[i] > 0) {
                    builder.addGap(rawGaps[i]);
                }
                builder.addClause(clauses[i]);
            }
            builder.setSlop(nearNode.getSlop());
            return builder.build();
        }

        return new SpanNearQuery(clauses, nearNode.getSlop(),
                nearNode.getInOrder());

    }


}
