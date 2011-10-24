package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryParser.standard.config.DefaultPhraseSlopAttribute;
import org.apache.lucene.queryParser.standard.parser.EscapeQuerySyntaxImpl;

public class AqpQNORMALProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpANTLRNode
				&& ((AqpANTLRNode) node).getTokenLabel().equals("QNORMAL")) {
			
			QueryConfigHandler queryConfig = getQueryConfigHandler();

			if (queryConfig != null) {
				
				AqpANTLRNode child = (AqpANTLRNode) node.getChildren().get(0);
				System.out.println(child.toString());
				if (queryConfig.hasAttribute(DefaultFieldAttribute.class)) {
					String defaultField = queryConfig.getAttribute(
							DefaultFieldAttribute.class).getDefaultField();

					return new FieldQueryNode(defaultField,
							EscapeQuerySyntaxImpl.discardEscapeChar(child
									.getTokenInput()), child.getTokenStart(),
							child.getTokenEnd());

				}
			}

		}
		return node;
	}

	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}

}
