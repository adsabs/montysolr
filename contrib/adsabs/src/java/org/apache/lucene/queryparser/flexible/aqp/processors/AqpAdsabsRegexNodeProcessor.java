package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsRegexQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsSynonymQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;

public class AqpAdsabsRegexNodeProcessor extends QueryNodeProcessorImpl implements
	QueryNodeProcessor  {

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected QueryNode preProcessNode(QueryNode node)
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
				return new AqpAdsabsRegexQueryNode(n.getFieldAsString(), input, n.getBegin(), n.getEnd());
			} else if (!(node instanceof PrefixWildcardQueryNode) && input.endsWith(".*")) {
				input = input.substring(0, input.length() - 2) + "*";
				return new PrefixWildcardQueryNode(n.getFieldAsString(), input, n.getBegin(), n.getEnd());
			}
			else if (input.contains(".*")) { // TODO: this is not fail-proof
				return new WildcardQueryNode(n.getFieldAsString(), input.replace(".*", "*").replace(".?", "?"),
						n.getBegin(), n.getEnd());
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
