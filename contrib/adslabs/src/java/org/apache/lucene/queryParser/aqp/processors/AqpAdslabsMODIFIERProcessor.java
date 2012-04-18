package org.apache.lucene.queryParser.aqp.processors;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpAdslabsSynonymQueryNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.QueryNode;

public class AqpAdslabsMODIFIERProcessor extends AqpMODIFIERProcessor {
	
	@Override
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		
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
