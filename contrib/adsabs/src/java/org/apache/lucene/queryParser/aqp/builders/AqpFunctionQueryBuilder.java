package org.apache.lucene.queryParser.aqp.builders;

import org.apache.lucene.queryParser.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.builders.QueryBuilder;
import org.apache.lucene.queryParser.core.nodes.QueryNode;


public interface AqpFunctionQueryBuilder extends QueryBuilder {
	
	
	/**
	 * Creates the query node that will be passed to the builder.
	 *  
	 * @param node
	 * @return
	 */
	public AqpFunctionQueryNode createQNode(QueryNode node)
		throws QueryNodeException;
	
	
	
}
