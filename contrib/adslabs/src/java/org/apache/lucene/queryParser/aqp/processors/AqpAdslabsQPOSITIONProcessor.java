package org.apache.lucene.queryParser.aqp.processors;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.standard.config.BoostAttribute;

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
		
		if (input.equals("^~")) {
			throw new QueryNodeException(new MessageImpl(
	                QueryParserMessages.INVALID_SYNTAX,
	                "It is not allowed to use ^~, especially the carat (^) must be accompanied by a number, eg. word^0.5~"));
		}
		
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
		return node;
	}
}
