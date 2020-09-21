package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor;

public class AqpAdsabsFixQPOSITIONProcessor extends AqpQProcessor {

	@Override
	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("QNORMAL") || node.getTokenLabel().startsWith("QPHRASE")) {
			return true;
		}
		return false;
	}

	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		
		AqpANTLRNode subChild = ((AqpANTLRNode) node.getChildren().get(0)); 
		String input = subChild.getTokenInput();
		
		// TODO: emit warnings
		
		if (node.getTokenLabel().equals("QNORMAL")) {
			if (input.endsWith("$")) {
				node.setTokenName("QPOSITION");
				node.setTokenLabel("QPOSITION");
			}
		}
		else {
			String testInput = input.substring(1, input.length()-1);
			if (testInput.startsWith("^") || testInput.endsWith("$")) {
				node.setTokenName("QPOSITION");
				node.setTokenLabel("QPOSITION");
				subChild.setTokenInput(testInput);
			}
		}
		
		return node;
	}

}
