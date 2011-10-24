package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;

/**
 * Takes {@link AqpANTLRNode} node with label QPHRASETRUNC and removes
 * the enclosing phrase characters from the child node input. We expect
 * that QPHRASETRUNC has always only one child.
 * 
 * @see AqpQPHRASEProcessor
 */

public class AqpQPHRASETRUNCProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpANTLRNode && ((AqpANTLRNode) node).getTokenLabel().equals("QPHRASETRUNC")) {
			AqpANTLRNode n = (AqpANTLRNode) node.getChildren().get(0);
			String input = n.getTokenInput();
			n.setTokenInput(input.substring(1, input.length()-1));
		}
		return node;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}

}
