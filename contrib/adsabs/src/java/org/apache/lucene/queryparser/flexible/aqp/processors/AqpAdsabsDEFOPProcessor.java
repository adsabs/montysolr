package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;

public class AqpAdsabsDEFOPProcessor extends AqpDEFOPProcessor implements
	QueryNodeProcessor {
	
	@Override
	protected StandardQueryConfigHandler.Operator getDefaultOperator()
			throws QueryNodeException {
		QueryConfigHandler queryConfig = getQueryConfigHandler();

		if (queryConfig != null) {

			if (queryConfig
					.has(AqpAdsabsQueryConfigHandler.ConfigurationKeys.DEFAULT_OPERATOR)) {
				return queryConfig
						.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.DEFAULT_OPERATOR);
			}
		}
		throw new QueryNodeException(new MessageImpl(
				QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
				"Configuration error: "
						+ StandardQueryConfigHandler.ConfigurationKeys.class
								.toString() + " is missing"));
	}
}
