package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQFUNCProcessor;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

/**
 * Provider gets called from the QueryNodeProcess when QFUNC node
 * is first encountered. 
 * 
 * The provider should return a QueryNodeBuilder that implements
 * the build() method, which returns Query object.
 * 
 * The provider has a chance to modify the node, harvest its elements,
 * save the original input values etc. This should happen inside
 * getBuilder() call
 * 
 * @see AqpFunctionQueryNodeBuilder
 * @see AqpFunctionQueryNode
 * @see AqpQFUNCProcessor
 * 
 */

public interface AqpFunctionQueryBuilderProvider {
	
	/**
	 * Returns the builder for the given function name or null if this
	 * provider has nothing to offer 
	 * 
	 * Care should be taken not to change the total number of childre
	 * because the node will be passed into {@link AqpFunctionQueryNode}
	 * which may modify them
	 * 
	 * @param funcName
	 *     name of the funciton, i.e. pos
	 * @param node
	 *     query node
	 * @param config 
	 *     query config
	 * @return {@link AqpFunctionQueryBuilder}
	 */
	public AqpFunctionQueryBuilder getBuilder(String funcName, QueryNode node, QueryConfigHandler config)
		throws QueryNodeException;
}
