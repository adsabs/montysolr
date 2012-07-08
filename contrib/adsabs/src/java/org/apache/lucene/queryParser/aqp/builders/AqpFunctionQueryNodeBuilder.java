package org.apache.lucene.queryParser.aqp.builders;

import org.apache.lucene.queryParser.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.Query;

public class AqpFunctionQueryNodeBuilder implements StandardQueryBuilder {

	public AqpFunctionQueryNodeBuilder() {
		// empty constructor
	}
	
	public Query build(QueryNode queryNode) throws QueryNodeException {
		AqpFunctionQueryNode node = (AqpFunctionQueryNode) queryNode;
		
		QueryBuilder builder = node.getBuilder();
		return (Query) builder.build(node);
	}


}
