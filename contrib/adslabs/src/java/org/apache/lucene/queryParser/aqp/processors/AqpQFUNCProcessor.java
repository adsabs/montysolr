package org.apache.lucene.queryParser.aqp.processors;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.queryParser.standard.parser.ParseException;

public class AqpQFUNCProcessor extends AqpQProcessorPost {
	
	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("QFUNC")) {
			return true;
		}
		return false;
	}
	
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		List<QueryNode> children = node.getChildren();
		
		String funcName = ((AqpANTLRNode) children.get(0)).getTokenInput();
		if (funcName.endsWith("(")) {
			funcName = funcName.substring(0, funcName.length()-1);
		}
		
		List<String> rawData = harvestChildrenData(children.get(1));
		return new AqpFunctionQueryNode(funcName, children.get(1), rawData);
		
	}

	// get the raw input from the children, we do not go 
	// into nested QFUNCs, that is intentional,
	// we see only the immediate level
	private List<String> harvestChildrenData(QueryNode node) {
		List<String> rawInput = new ArrayList<String>();
		swimDeep(rawInput, node);
		return rawInput;
	}

	private void swimDeep(List<String> rawInput, QueryNode node) {
		if (node instanceof AqpANTLRNode) {
			AqpANTLRNode a = (AqpANTLRNode) node;
			if (a.getTokenInput() != null) {
				try {
					rawInput.add(
						EscapeQuerySyntaxImpl.discardEscapeChar(a.getTokenInput()).toString()
								);
				} catch (ParseException e) {
					rawInput.add(a.getTokenInput());
				}
			}
		}
		else if (node instanceof AqpFunctionQueryNode) {
			rawInput.add("QFUNC");
			return;
		}
		if (!node.isLeaf()) {
			for (QueryNode child: node.getChildren()) {
				swimDeep(rawInput, child);
			}
		}
		
	}


}
