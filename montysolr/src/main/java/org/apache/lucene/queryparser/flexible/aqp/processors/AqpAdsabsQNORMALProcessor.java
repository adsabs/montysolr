package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

import java.util.Map.Entry;

public class AqpAdsabsQNORMALProcessor extends AqpQNORMALProcessor {
    public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {

        AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);

        QueryNode newNode = super.createQNode(node);
        for (Entry<String, Object> e : subChild.getTagMap().entrySet()) {
            newNode.setTag(e.getKey(), e.getValue());
        }
        return newNode;
    }

}
