package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedback;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;

/**
 * A generic class that is used by other query processors, eg.
 * {@link AqpQNORMALProcessor}
 * 
 * 
 * 
 */
public class AqpQProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {

	public String defaultField = null;

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpANTLRNode) {
			AqpANTLRNode n = (AqpANTLRNode) node;
			if (nodeIsWanted(n)) {
				return createQNode(n);
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

	public boolean nodeIsWanted(AqpANTLRNode node) {
		throw new UnsupportedOperationException();
	}

	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		throw new UnsupportedOperationException();
	}

	public String getDefaultFieldName() throws QueryNodeException {
		QueryConfigHandler queryConfig = getQueryConfigHandler();

		String defaultField = null;
		if (queryConfig != null) {

			if (queryConfig
					.has(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_FIELD)) {
				defaultField = queryConfig
						.get(AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_FIELD);
				return defaultField;
			}
		}
		throw new QueryNodeException(
				new MessageImpl(
						QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
						"Configuration error: "
								+ AqpStandardQueryConfigHandler.ConfigurationKeys.DEFAULT_FIELD
										.toString() + " is missing"));
	}

	public AqpFeedback getFeedbackAttr() throws QueryNodeException {
		QueryConfigHandler config = getQueryConfigHandler();
		if (config
				.has(AqpStandardQueryConfigHandler.ConfigurationKeys.FEEDBACK)) {
			return config
					.get(AqpStandardQueryConfigHandler.ConfigurationKeys.FEEDBACK);
		} else {
			throw new QueryNodeException(
					new MessageImpl(
							QueryParserMessages.NODE_ACTION_NOT_SUPPORTED,
							"Configuration error, missing AqpFeedback.class in the config!"));
		}
	}

}
