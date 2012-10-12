package org.apache.lucene.queryparser.flexible.aqp;

import java.util.List;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;

/**
 * Joins the nodes below DEFOP QN so that 'weak lensing' becomes
 * one string "weak lensing"
 * 
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
 *            
 * becomes:
 *                   DEFOP
 *                        |
 *                     /     \
 *            MODIFIER      CLAUSE 
 *                   /          \
 *           TMODIFIER       MODIFIER
 *                  /             \
 *             FIELD             .....
 *                /       
 *           QNORMAL   
 *               /        
 *            weak lensing                
 *                    
 * 
 * Care is taken not to join when the fields are different and 
 * when there is operator/clause/modifier inbetween
 * 
 * @author rca
 *
 */

public class AqpDEFOPChildrenCatenator extends AqpQProcessor {

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
		
		Integer previous = null;
		for (int i=0;i<children.size();i++) {
			if (isBareNode(children.get(i))) {
				if (previous == null) {
					previous = i;
				}
				else if (previous+1 == i) {
					joinChildren(children.get(previous), children.get(i));
					children.remove(i);
					i--;
					continue;
					
				}
				else {
					previous = i;
					continue;
				}
				
			}
		}
		
		
		return node;
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
	
	private QueryNode joinChildren(QueryNode first, QueryNode second) {
		AqpANTLRNode firstValueNode = (AqpANTLRNode) getTerminalNode(first);
		AqpANTLRNode secondValueNode = (AqpANTLRNode) getTerminalNode(second);
		
		
		firstValueNode.setTokenInput(firstValueNode.getTokenInput() + " " + secondValueNode.getTokenInput());
		firstValueNode.getTree().setTokenStopIndex(secondValueNode.getTokenEnd());
		return first;
	}

	private QueryNode getTerminalNode(QueryNode node) {
		while (!node.isLeaf()) {
			return getTerminalNode(node.getChildren().get(0));
		}
		return node;
	}
}
