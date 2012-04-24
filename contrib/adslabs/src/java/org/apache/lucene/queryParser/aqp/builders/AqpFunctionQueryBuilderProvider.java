package org.apache.lucene.queryParser.aqp.builders;

import org.apache.lucene.queryParser.core.nodes.QueryNode;

public interface AqpFunctionQueryBuilderProvider {
	
	/**
	 * Returns the builder for the given function name or null if this
	 * provider has nothing to offer 
	 * 
	 * @param funcName
	 * @param node
	 * @return
	 */
	public AqpFunctionQueryBuilder getBuilder(String funcName, QueryNode node);
}
