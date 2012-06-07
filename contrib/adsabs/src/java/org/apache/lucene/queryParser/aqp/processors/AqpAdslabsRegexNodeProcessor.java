package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpAdslabsRegexQueryNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpAdslabsSynonymQueryNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;

public class AqpAdslabsRegexNodeProcessor extends QueryNodeProcessorImpl implements
	QueryNodeProcessor  {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof FieldQueryNode) {
			FieldQueryNode n = (FieldQueryNode) node;
			String input = n.getTextAsString();
			if (input == null) {
				return node;
			}
			if (hasRegexSyntax(input)) {
				return new AqpAdslabsRegexQueryNode(n.getFieldAsString(), input, n.getBegin(), n.getEnd());
			}
		}
		return node;
	}
	
	protected boolean hasRegexSyntax(String input) {
		// TODO: match other regex syntax variations that will be present
		return input.endsWith(".*");
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}


}
