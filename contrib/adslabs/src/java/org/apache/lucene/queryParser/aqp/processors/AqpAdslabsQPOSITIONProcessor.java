package org.apache.lucene.queryParser.aqp.processors;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;

public class AqpAdslabsQPOSITIONProcessor extends AqpQProcessorPost implements
		QueryNodeProcessor {
	
	@Override
	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("QPOSITION")) {
			return true;
		}
		return false;
	}
	
	@Override
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);
		String input = subChild.getTokenInput();
		
		Integer start = null;
		Integer end = null;
		
		if (input.startsWith("^")) {
			input = input.substring(1, input.length());
			start = 0;
		}
		
		if (input.endsWith("$")) {
			input = input.substring(1, input.length());
			end = -1;
		}
		
	}
}
