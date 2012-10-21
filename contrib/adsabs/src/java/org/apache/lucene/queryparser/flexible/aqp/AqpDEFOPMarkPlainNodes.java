package org.apache.lucene.queryparser.flexible.aqp;

import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

/**
 * 
 * Looks at the nodes below DEFOP QN and marks the nodes
 * that can be concatenated during analysis, eg. weak lensing
 * can be used as one token
 * 
 * <pre>
 *                     DEFOP
 *                        |
 *                     /  |     \
 *            MODIFIER   MOD..   CLAUSE 
 *                   /    |         \
 *           TMODIFIER   TMODIFIER  MODIFIER
 *                  /     |           \
 *             FIELD    FIELD        .....
 *                /       |
 *           QNORMAL   QNORMAL
 *               /        |
 *            weak      lensing
 * </pre>           
 *                    
 * <p>
 * Care is taken not to join when the fields are different and 
 * when there is operator/clause/modifier inbetween
 * 
 * @author rca
 *
 */

public class AqpDEFOPMarkPlainNodes extends AqpQProcessor {
	
	public static String PLAIN_TOKEN = "PLAIN_TOKEN";
	public static String PLAIN_TOKEN_SEPARATOR = " ";
	public static String PLAIN_TOKEN_CONCATENATED = "PLAIN_TOKEN_CONCATENATED";
	
	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("DEFOP")) {
			return true;
		}
		return false;
	}
	
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		
		// only one child, do nothing
		if (node.getChildren().size() == 1) {
			return node;
		}
		
		List<QueryNode> children = node.getChildren();
		List<QueryNode> forMarking = new ArrayList<QueryNode>();
		
		Integer previous = -1;
		for (int i=0;i<children.size();i++) {
			if (isBareNode(children.get(i))) {
				if (forMarking.size() == 0) {
					previous = i;
					forMarking.add(children.get(i));
				}
				else if (previous+1 == i) {
					forMarking.add(children.get(i));
					previous = i;
					continue;
					
				}
				else {
					previous = -1;
					tagPlainNodes(forMarking);
					continue;
				}
			}
		}
		
		if (forMarking.size() > 0)
			tagPlainNodes(forMarking);
		
		return node;
	}
	
	private void tagPlainNodes(List<QueryNode> forMarking) {
		
		if (forMarking.size() > 1) {
			StringBuffer concatenated = new StringBuffer();
			int tag = concatenated.hashCode();
			for (int i=0;i<forMarking.size();i++) {
				concatenated.append(markChild(tag, forMarking.get(i)));
				if (i+1 != forMarking.size())
					concatenated.append(PLAIN_TOKEN_SEPARATOR);
			}
			QueryNode terminal = getTerminalNode(forMarking.get(0));
			terminal.setTag(PLAIN_TOKEN_CONCATENATED, concatenated.toString());
		}
		forMarking.clear();
	}
	
	
	private boolean isBareNode(QueryNode node) {
		StringBuffer sb = new StringBuffer();
		harvestLabels(node, sb, 5);
		if (sb.toString().equals("/MODIFIER/TMODIFIER/FIELD/QNORMAL")) {
			return true;
		}
		return false;
	}
		
	private void harvestLabels(QueryNode node, StringBuffer data, int maxDepth) {
		if (maxDepth > 0 && node instanceof AqpANTLRNode) {
			if (node.isLeaf()) {
				return; // avoid the terminal node
			}
			data.append("/");
			data.append(((AqpANTLRNode) node).getTokenLabel());
			for (QueryNode child: node.getChildren()) {
				harvestLabels(child, data, maxDepth-1);
			}
		}
	}
	
	private String markChild(int tag, QueryNode node) {
		AqpANTLRNode terminal = (AqpANTLRNode) getTerminalNode(node);
		terminal.setTag(PLAIN_TOKEN, tag);
		return terminal.getTokenInput();
	}

	private QueryNode getTerminalNode(QueryNode node) {
		while (!node.isLeaf()) {
			return getTerminalNode(node.getChildren().get(0));
		}
		return node;
	}
}
