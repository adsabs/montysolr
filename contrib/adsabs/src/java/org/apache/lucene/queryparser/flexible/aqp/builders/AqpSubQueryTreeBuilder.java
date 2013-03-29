package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpSubqueryParser;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.search.Query;
import org.apache.solr.search.AqpFunctionQParser;

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
			functionQueryParser.setQueryNode(node);
			return aqpValueSourceParser.parse(functionQueryParser);
		} catch (ParseException e) {
			throw new QueryNodeException(new MessageImpl(e.getLocalizedMessage()));
		}
	}

  public boolean canBeAnalyzed() {
    if (aqpValueSourceParser instanceof AqpSubqueryParser) {
      return ((AqpSubqueryParser) aqpValueSourceParser).canBeAnalyzed();
    }
    return false;
  }

}
