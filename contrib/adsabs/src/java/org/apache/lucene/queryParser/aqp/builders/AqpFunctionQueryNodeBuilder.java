package org.apache.lucene.queryParser.aqp.builders;

import org.apache.lucene.queryParser.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.Query;

public class AqpFunctionQueryNodeBuilder implements StandardQueryBuilder {

	public AqpFunctionQueryNodeBuilder() {
		// empty constructor
	}
	
	public Query build(QueryNode queryNode) throws QueryNodeException {
		AqpFunctionQueryNode node = (AqpFunctionQueryNode) queryNode;
		
		AqpFunctionQueryBuilder builder = node.getBuilder();
		return (Query) builder.build(node);
	}


}
