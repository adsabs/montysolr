package org.apache.lucene.queryParser.aqp.builders;

import org.apache.lucene.queryParser.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryParser.aqp.processors.AqpQFUNCProcessor;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.builders.QueryBuilder;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.QueryNode;

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
 * @author rchyla
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
	 * @param node
	 * @param config 
	 * @return
	 */
	public QueryBuilder getBuilder(String funcName, QueryNode node, QueryConfigHandler config)
		throws QueryNodeException;
}
