package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsIdentifierNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;

import java.util.List;

/**
 * Special case processor for SciX IDs
 * <br/>
 * We'd like "identifier:scix:XXX-XXX-XXX" queries to work, but the other elements of the
 * query processing pipeline remove the "scix:" prefix from the query. This adds the prefix
 * back in to ensure the query term can match the indexed term.
 */
public class AqpScixIDProcessor extends QueryNodeProcessorImpl {


    @Override
    protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
        if (node instanceof AqpAdsabsIdentifierNode identifierNode) {
            if (identifierNode.getFieldAsString().equals("scix")) {
                return new AqpAdsabsIdentifierNode(
                        identifierNode.getField(),
                        identifierNode.getFieldAsString() + ":" + identifierNode.getTextAsString(),
                        identifierNode.getBegin(),
                        identifierNode.getEnd());
            }
        }

        return node;
    }

    @Override
    protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
        return node;
    }

    @Override
    protected List<QueryNode> setChildrenOrder(List<QueryNode> children) throws QueryNodeException {
        return children;
    }
}
