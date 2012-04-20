package org.apache.lucene.queryParser.aqp.processors;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpAndQueryNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpNearQueryNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.GroupQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.standard.parser.EscapeQuerySyntaxImpl;


/**
 * this processor transforms the AST tree, and it must run before the
 * {@link AqpOPERATORProcessor} because we also work with the OPERATOR
 * nodes 
 * 
 * @author rchyla
 *
 */
public class AqpCOMMAProcessor extends AqpQProcessorPost {
	
	@Override
	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("COMMA") || node.getTokenLabel().equals("SEMICOLON")) {
			return true;
		}
		return false;
	}
	
	@Override
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		
		String label = node.getTokenLabel();
		
		if (label.equals("COMMA")) {
			AqpANTLRNode modifierNode = transformCommaNode(node);
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
			
			if (modifierNode != null) {
				modifierNode.add(nearNode);
				return modifierNode;
			}
			else {
				return nearNode;
			}
			
		} else {
			List<QueryNode> children = node.getChildren();
			for (int i=0;i<children.size();i++) {
				children.set(i, new GroupQueryNode(children.get(i)));
			}
			return new AqpAndQueryNode(children);
		} 

	}
	
	/*
	 * Child has a rigid structure (unless I changed it ;))
	 *                COMMA
	 *                 |   \
	 *                 |    \
	 *                 |     \
	 *             MODIFIER  MODIFIER
	 *               /   \          \
	 *             -|+  TMODIFIER   ....
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
	
	/*
	 * Removes the modifiers from the children
	 */
	protected AqpANTLRNode transformCommaNode(AqpANTLRNode commaNode) throws QueryNodeException {
		ArrayList<QueryNode> newChildren = new ArrayList<QueryNode>();
		
		AqpANTLRNode realModifier = null;
		boolean changed = false;
		
		int i = 0;
		for (QueryNode child: commaNode.getChildren()) {
			if (child instanceof AqpANTLRNode) {
				AqpANTLRNode modifierNode = (AqpANTLRNode) child;
				// we want to safeguard against really weird usage such as:
				// -Eichhorn, (G. 2000)^5
				// while we could still handle this
				// -Eichhorn, (G. 2000)
				// they produce a different tree
				if (modifierNode.getTokenLabel().equals("CLAUSE")) {
					throw new QueryNodeException(new MessageImpl(
			                QueryParserMessages.INVALID_SYNTAX_CANNOT_PARSE, commaNode
			                    .toQueryString(new EscapeQuerySyntaxImpl())
			                    )); 
				}
				else if (modifierNode.getTokenLabel().equals("MODIFIER")) {
					if (i==0 && modifierNode.getChildren().size() > 1) { // only for the first node, we grab the modifier
						realModifier = modifierNode;
					}
					// remove the modifier
					newChildren.add(modifierNode.getChildren().get(modifierNode.getChildren().size()-1));
					changed = true;
				}
				else {
					newChildren.add(modifierNode);
				}
			}
			else {
				throw new QueryNodeException(new MessageImpl(
		                QueryParserMessages.NODE_ACTION_NOT_SUPPORTED, 
		                "Hmmm, something is wroooong... you are not supposed to have stranger nodes below COMMA!"
		                    ));
			}
			i++;
		}
		
		if (changed)		commaNode.set(newChildren);
		
		if (realModifier != null) {
			List<QueryNode> tobeChildren = realModifier.getChildren();
			tobeChildren.remove(1);
			return realModifier;
		}
		
		return null;
	}
}
