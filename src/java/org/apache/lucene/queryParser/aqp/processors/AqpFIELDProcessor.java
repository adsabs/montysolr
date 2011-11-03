package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.FieldableNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;

/**
 * This processor applies the user-submitted value to all
 * {@link FieldableNode} nodes which are below it. The FIELD node
 * itself will be discarded.
 * <br/>
 * If the FIELD has only one child, the child will be returned
 * 
 * @see FieldableNode
 * @see AqpQNORMALProcessor and similar
 *
 */
public class AqpFIELDProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpANTLRNode && ((AqpANTLRNode) node).getTokenLabel().equals("FIELD")) {
			if (node.getChildren().size()==1) {
				return node.getChildren().get(0);
			}
			
			
			String field = getFieldValue(node);
			node = node.getChildren().get(node.getChildren().size()-1);
			if (field!=null) {
				applyFieldToAllChildren(field, node);
			}
		}
		return node;
	}

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}
	
	private String getFieldValue(QueryNode fieldNode) throws QueryNodeException {
		
		if (fieldNode!= null && fieldNode.getChildren()!=null) {
			return ((AqpANTLRNode) fieldNode.getChildren().get(0)).getTokenInput();
		}
		return null;
	}
	
	private void applyFieldToAllChildren(String field, QueryNode node) {
		
		if (node instanceof FieldQueryNode) {
			((FieldQueryNode) node).setField(field);
		}
		if (node.getChildren()!=null) {
			for (QueryNode child : node.getChildren()) {
				applyFieldToAllChildren(field, child);
			}
		}
	}

}
