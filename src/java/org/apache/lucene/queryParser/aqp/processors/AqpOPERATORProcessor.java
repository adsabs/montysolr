package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.nodes.NotQueryNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.AndQueryNode;
import org.apache.lucene.queryParser.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryParser.core.nodes.OrQueryNode;
import org.apache.lucene.queryParser.core.nodes.ProximityQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryParser.standard.nodes.BooleanModifierNode;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode.Modifier;

public class AqpOPERATORProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {
	
	public enum OPERATOR {
		AND,
		OR,
		NOT,
		WITH,
		PARAGRAPH;
	}

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}
	
	
	/*
	 * Returns the QueryNode with the children being converted to {@see ModifierQueryNode}
	 *   AND
	 *   	- applies {@see ModifierQueryNode.Modifier.MOD_REQ} to all children
	 *   OR
	 *   	- applies {@see ModifierQueryNode.Modifier.MOD_NONE} to all children
	 *   NOT
	 *   	- applies {@see ModifierQueryNode.Modifier.MOD_REQ} to the first child
	 *        and {@see ModifierQueryNode.Modifier.MOD_NOT} to the rest. This query
	 *        means "x AND NOT y"
	 *   WITH
	 *   	- not implemented yet
	 *   PARAGRAPH
	 *   	- not implemented yet
	 *   
	 * This processor should be before {@AqpMODIFIERProcessor} because modifiers should
	 * have precedence over operators. Like in the query: "this OR +that" 
	 * 
	 * @see AqpMODIFIERProcessor
	 * @see org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl#preProcessNode(org.apache.lucene.queryParser.core.nodes.QueryNode)
	 */
	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		
		if (node instanceof AqpANTLRNode && ((AqpANTLRNode) node).getTokenName().equals("OPERATOR")) {
			AqpANTLRNode n = (AqpANTLRNode) node;
			QueryNode ret = null;
			
			if (n.getTokenLabel().equals("AND")) {
				ret = new AndQueryNode(getChildren(node, OPERATOR.AND));
			}
			else if(n.getTokenLabel().equals("OR")) {
				ret = new OrQueryNode(getChildren(node, OPERATOR.OR));
			}
			else if(n.getTokenLabel().equals("NOT")) {
				ret = new NotQueryNode(getChildren(node, OPERATOR.NOT));
			}
			//else if(n.getTokenLabel().equals("WITH")) {
			//	new ProximityQueryNode(clauses, field, type, inorder)
			//}
			return ret;
		}
		return node;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}
	
	/*
	 * Apply the ModifierQueryNode to each of the children
	 * (unless it is already of the type ModifierQueryNode)
	 */
	private List<QueryNode> getChildren(QueryNode node, AqpOPERATORProcessor.OPERATOR operator) {
		Modifier mod = null;
		List<QueryNode> children = node.getChildren();
		int index = 0;
		
		switch (operator) {
		case AND:
			mod = Modifier.MOD_REQ;
			break;
		case NOT:
			mod = Modifier.MOD_NOT;
			index = 1; //skip the first
			if (!(children.get(0) instanceof ModifierQueryNode
					|| children.get(0) instanceof BooleanQueryNode)) {
				children.set(0, new ModifierQueryNode(children.get(0), Modifier.MOD_REQ));
			}
			break;
		case OR:
			mod = Modifier.MOD_NONE;
			break;
		default:
			throw new IllegalArgumentException("This call accepts only standard Boolean operators");
		}
		
		
		for (int i=index;i<children.size();i++) {
			QueryNode child = children.get(i);
			if (child instanceof ModifierQueryNode || child instanceof BooleanQueryNode) {
				// do nothing, modifiers have precedence
				//children.set(i, new ModifierQueryNode(child.getChildren().get(0), 
				//		i==0&&firstMod!=null ? firstMod : mod));
			} 
			else {
				children.set(i, new ModifierQueryNode(children.get(i), mod));
			}
		}
		return children;
		
	}

}
