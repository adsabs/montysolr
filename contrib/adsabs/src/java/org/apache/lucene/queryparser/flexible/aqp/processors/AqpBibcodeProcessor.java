package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdslabsIdentifierNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;

public class AqpBibcodeProcessor extends QueryNodeProcessorImpl implements
	QueryNodeProcessor {

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
			// test it is a bibcode (19char long)
			if (input.length() == 19) {
				try {
					Integer.parseInt(input.substring(0, 4));
					return new AqpAdslabsIdentifierNode(n.getFieldAsString(), input, n.getBegin(), n.getEnd());
				} catch(NumberFormatException nfe) {
					// pass
				}
			}
			else if (input.contains("\u2026") && input.length() == 17) { //2003AJ….125..525J  -- bibcode with ellipsis
				return new AqpAdslabsIdentifierNode(n.getFieldAsString(), input.replace("\u2026", "..."), n.getBegin(), n.getEnd());
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
