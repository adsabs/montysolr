package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;

public class AqpNUCLEUSProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpANTLRNode && ((AqpANTLRNode) node).getTokenLabel().equals("NUCLEUS")) {
			List<QueryNode> children = node.getChildren();
			AqpANTLRNode fieldNode = (AqpANTLRNode) children.remove(0);
			String field = getFieldValue(fieldNode);
			QueryNode valueNode = children.get(0);
			if (field!=null) {
				applyFieldToAllChildren(field, valueNode);
			}
			return valueNode;
		}
		return node;
	}

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}
	
	private String getFieldValue(AqpANTLRNode fieldNode) throws QueryNodeException {
		
		if (fieldNode!= null && fieldNode.getTokenInput()!=null) {
			return fieldNode.getTokenInput();
		}
		return null;
		
	}
	
	private void applyFieldToAllChildren(String field, QueryNode node) {
		
		for (QueryNode child : node.getChildren()) {
			if (child instanceof FieldQueryNode) {
				((FieldQueryNode) child).setField(field);
				if (child.getChildren()!=null) {
					applyFieldToAllChildren(field, child);
				}
			}
		}
		
	}

}
