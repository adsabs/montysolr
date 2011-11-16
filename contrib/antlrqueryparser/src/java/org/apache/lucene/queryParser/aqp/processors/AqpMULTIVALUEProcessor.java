package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;

public class AqpMULTIVALUEProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpANTLRNode && ((AqpANTLRNode) node).getTokenLabel().equals("MULTIVALUE")) {
			return node;
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

}
