package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpAndQueryNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpNearQueryNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.QueryNode;

public class AqpAdslabsOPERATORProcessor extends AqpOPERATORProcessor {
	
	@Override
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		
		String label = node.getTokenLabel();
		
		if (label.equals("COMMA")) {
			List<QueryNode> children = node.getChildren();
			AqpNearQueryNode nearNode = new AqpNearQueryNode(children, 1);
			
			String fChild = getTokenInput(children.get(0));
			String lChild = getTokenInput(children.get(children.size()-1));
			
			if (fChild == null || lChild == null) {
				// we deal with something else than the MODIFIER-TMODIFIER-FIELD...
				// probably it is a nested function or nested clause
				return nearNode;
			}
			
			// test if we deal with J, Name
			if (fChild.length() < 3 || fChild.length() < lChild.length()) {
				nearNode.setInOrder(false);
			}
			return nearNode;
			
		} else if (label.equals("SEMICOLON")) {
			return new AqpAndQueryNode(node.getChildren());
		} else {
			return super.createQNode(node);
		}

	}
	
	/*
	 * Child has a rigid structure (unless I changed it ;))
	 *               COMMA
	 *                 |
	 *              MODIFIER
	 *               /   \
	 *             -|+  TMODIFIER
	 *                     \
	 *                   FIELD
	 *                      \
	 *                     Q-[token]
	 *                        \
	 *                       <user-input>
	 * 
	 * This method is lazy (and may break if the OPERATOR
	 * is called in the later stages; when ATNLR nodes were
	 * already converted)
	 * 
	 */
	protected String getTokenInput(QueryNode child) {
		AqpANTLRNode n = null;
		if (child instanceof AqpANTLRNode) {
			n = (AqpANTLRNode) child;
		}
		else {
			for (QueryNode c: child.getChildren()) {
				return getTokenInput(c);
			}
		}
		AqpANTLRNode fieldNode = n.getChild("FIELD");
		if (fieldNode != null) {
			return ((AqpANTLRNode) (fieldNode.getChildren().get(0)).getChildren().get(0)).getTokenInput();
		}
		
		return null;
	}
}
