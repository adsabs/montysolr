package org.apache.lucene.queryParser.aqp.processors;

import org.apache.lucene.queryParser.aqp.nodes.AqpAdslabsSynonymQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpMODIFIERProcessor;

public class AqpAdslabsMODIFIERProcessor extends AqpMODIFIERProcessor {
	
	@Override
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		
		if (node.getChildren().size() == 1) {
			return node.getChildren().get(0);
		}
		
		AqpANTLRNode subNode = getModifierNode(node);
		
		String sign = subNode.getTokenLabel();
		
		if (sign.equals("=")) {
			return new AqpAdslabsSynonymQueryNode(getValueNode(node), false);
		}
		else if (sign.equals("#")) {
			return new AqpAdslabsSynonymQueryNode(getValueNode(node), true);
		}
		else {
			return super.createQNode(node);
		}

	}
	
}
