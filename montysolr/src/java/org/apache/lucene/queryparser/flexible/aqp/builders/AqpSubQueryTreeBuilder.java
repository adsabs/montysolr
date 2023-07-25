package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpSubqueryParser;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.search.Query;
import org.apache.solr.search.AqpFunctionQParser;
import org.apache.solr.search.SyntaxError;

public class AqpSubQueryTreeBuilder extends QueryTreeBuilder
  implements AqpFunctionQueryBuilder {
	
	private AqpSubqueryParser aqpValueSourceParser;
	private AqpFunctionQParser functionQueryParser;
	
	public AqpSubQueryTreeBuilder(AqpSubqueryParser provider, AqpFunctionQParser parser) {
		aqpValueSourceParser = provider;
		functionQueryParser = parser;
	}
	
	public Query build(QueryNode node) throws QueryNodeException {
		try {
			functionQueryParser.setQueryNode((AqpFunctionQueryNode) node);
			if (((AqpFunctionQueryNode) node).getOriginalInput() != null) {
				functionQueryParser.setString(((AqpFunctionQueryNode) node).getOriginalInput().value);
			}
			return aqpValueSourceParser.parse(functionQueryParser);
		} catch (SyntaxError e) {
			QueryNodeException ex = new QueryNodeException(new MessageImpl(e.getMessage()));
			ex.setStackTrace(e.getStackTrace());
			throw ex;
		} finally {
			functionQueryParser.getReq().close();
		}
	}

}
