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
import org.apache.lucene.queryParser.standard.nodes.PrefixWildcardQueryNode;

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
			
			// TODO: should we consider using something more explicit, 
			// e.g. creating author tokens like "/Kurtz, M.*/" ?
			if (input.contains("\\b") || input.contains("\\w")) {
				return new AqpAdslabsRegexQueryNode(n.getFieldAsString(), input, n.getBegin(), n.getEnd());
			} else if (!(node instanceof PrefixWildcardQueryNode) && input.endsWith(".*")) {
				input = input.substring(0, input.length() - 2) + "*";
				return new PrefixWildcardQueryNode(n.getFieldAsString(), input, n.getBegin(), n.getEnd());
			}
		}
		return node;
	}
	
	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}


}
