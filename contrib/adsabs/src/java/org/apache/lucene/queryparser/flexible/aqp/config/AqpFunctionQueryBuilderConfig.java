package org.apache.lucene.queryparser.flexible.aqp.config;

import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFunctionQueryBuilderProvider;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.util.Attribute;

public interface AqpFunctionQueryBuilderConfig extends Attribute {
	
	
	public void addProvider(AqpFunctionQueryBuilderProvider provider);
	
	public void setBuilder(String funcName, QueryBuilder builder);
	
	public QueryBuilder getBuilder(String funcName, QueryNode node, QueryConfigHandler config)
		throws QueryNodeException;
	
}
