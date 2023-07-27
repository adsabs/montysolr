package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.util.AqpUtils;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldableNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;

import java.util.List;

/**
 * This processor applies the user-submitted value to all {@link FieldableNode}
 * nodes which are below it. The FIELD node itself will be discarded.
 *
 * <p>
 * <p>
 * If the FIELD has only one child, the child will be returned
 *
 * @see FieldableNode
 * @see AqpQNORMALProcessor and similar
 */
public class AqpFIELDProcessor extends QueryNodeProcessorImpl implements
        QueryNodeProcessor {

    @Override
    protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
        if (node instanceof AqpANTLRNode
                && ((AqpANTLRNode) node).getTokenLabel().equals("FIELD")) {
            if (node.getChildren().size() == 1) {
                return node.getChildren().get(0);
            }

            String field = getFieldValue(node);
            node = node.getChildren().get(node.getChildren().size() - 1);
            if (field != null) {
                AqpUtils.applyFieldToAllChildren(EscapeQuerySyntaxImpl.discardEscapeChar(field)
                        .toString(), node);
            }
        }
        return node;
    }

    @Override
    protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
        return node;
    }

    @Override
    protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
            throws QueryNodeException {
        return children;
    }

    private String getFieldValue(QueryNode fieldNode) throws QueryNodeException {

        if (fieldNode != null && fieldNode.getChildren() != null) {
            return ((AqpANTLRNode) fieldNode.getChildren().get(0)).getTokenInput();
        }
        return null;
    }


}
