package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

import java.util.Map.Entry;

public class AqpAdsabsQNORMALProcessor extends AqpQNORMALProcessor {
    static final String LITERAL_LINE_TAG = "aqp.literal_forbidden_line";

    public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {

        AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);

        QueryNode newNode = super.createQNode(node);
        for (Entry<String, Object> e : subChild.getTagMap().entrySet()) {
            newNode.setTag(e.getKey(), e.getValue());
        }
        if ("FORBIDDEN_LINE".equals(subChild.getTokenName())) {
            newNode.setTag(LITERAL_LINE_TAG, Boolean.TRUE);
        }
        return newNode;
    }

}
