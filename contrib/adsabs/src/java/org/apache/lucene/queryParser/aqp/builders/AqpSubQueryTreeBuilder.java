package org.apache.lucene.queryParser.aqp.builders;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.aqp.AqpSubqueryParser;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.search.Query;
import org.apache.solr.search.AqpFunctionQParser;

public class AqpSubQueryTreeBuilder extends QueryTreeBuilder {
	
	private AqpSubqueryParser vs;
	private AqpFunctionQParser fp;
	
	public AqpSubQueryTreeBuilder(AqpSubqueryParser provider, AqpFunctionQParser parser) {
		vs = provider;
		fp = parser;
	}
	
	public Query build(QueryNode node) throws QueryNodeException {
		try {
			fp.setQueryNode(node);
			return vs.parse(fp);
		} catch (ParseException e) {
			throw new QueryNodeException(new MessageImpl(e.getLocalizedMessage()));
		}
	}
}
