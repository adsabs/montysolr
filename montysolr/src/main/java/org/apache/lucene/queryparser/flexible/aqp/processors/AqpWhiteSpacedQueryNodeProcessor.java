package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFunctionQueryBuilder;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpWhiteSpacedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor.OriginalInput;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.BoostQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.SlopQueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * This processor looks for {@link AqpWhiteSpacedQueryNode}
 * and builds a {@link AqpFunctionQueryNode} out of it.
 * This is the solution for unfielded search.
 * <p>
 * Your config should specify:
 * <p>
 * aqp.unfielded.tokens.function.name - name of the function
 * that receives the input  {@link AqpFunctionQueryNode}
 */
public class AqpWhiteSpacedQueryNodeProcessor extends AqpQueryNodeProcessorImpl implements
        QueryNodeProcessor {


    @Override
    protected QueryNode postProcessNode(QueryNode node)
            throws QueryNodeException {

        if (node instanceof AqpWhiteSpacedQueryNode) {

            QueryConfigHandler config = getQueryConfigHandler();

            if (!config.has(AqpAdsabsQueryConfigHandler.ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG)) {
                throw new QueryNodeException(new MessageImpl(
                        "Invalid configuration",
                        "Missing FunctionQueryBuilder provider"));
            }

            String funcName = getConfigVal("aqp.unfielded.tokens.function.name", "edismax_combined_aqp");
            String subQuery = ((FieldQueryNode) node).getTextAsString();
            String field = ((FieldQueryNode) node).getFieldAsString();
            if (field.equals(config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.UNFIELDED_SEARCH_FIELD))) {
                field = null;
            }

            if (field != null) {
                subQuery = field + ":" + subQuery;
            }

            if (node.getParent() instanceof SlopQueryNode) {
                subQuery = "(" + subQuery + ")";
                subQuery = subQuery + "~" + ((SlopQueryNode) node.getParent()).getValue();
                if (node.getParent().getParent() instanceof BoostQueryNode) {
                    subQuery = subQuery + "^" + ((BoostQueryNode) node.getParent().getParent()).getValue();
                }
            } else if (node.getParent() instanceof BoostQueryNode) {
                subQuery = "(" + subQuery + ")";
                subQuery = subQuery + "^" + ((BoostQueryNode) node.getParent()).getValue();
            }

            node.setTag("subQuery", subQuery);

            AqpFunctionQueryBuilder builder = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG)
                    .getBuilder(funcName, node, config);

            if (builder == null) {
                throw new QueryNodeException(new MessageImpl(QueryParserMessages.INVALID_SYNTAX,
                        "Unknown function: \"" + funcName + "\""));
            }


            List<OriginalInput> fValues = new ArrayList<OriginalInput>();
            fValues.add(new OriginalInput(subQuery, ((AqpWhiteSpacedQueryNode) node).getBegin(), ((AqpWhiteSpacedQueryNode) node).getEnd()));
            return new AqpFunctionQueryNode(funcName, builder, fValues);
        }
        return node;
    }

    @Override
    protected QueryNode preProcessNode(QueryNode node)
            throws QueryNodeException {
        return node;
    }

    @Override
    protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
            throws QueryNodeException {
        return children;
    }

}
