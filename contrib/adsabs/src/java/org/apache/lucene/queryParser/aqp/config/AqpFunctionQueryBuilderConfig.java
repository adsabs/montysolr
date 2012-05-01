package org.apache.lucene.queryParser.aqp.config;

import org.apache.lucene.queryParser.aqp.builders.AqpFunctionQueryBuilder;
import org.apache.lucene.queryParser.aqp.builders.AqpFunctionQueryBuilderProvider;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.util.Attribute;

public interface AqpFunctionQueryBuilderConfig extends Attribute {
	
	
	public void addProvider(AqpFunctionQueryBuilderProvider provider);
	
	public void setBuilder(String funcName, AqpFunctionQueryBuilder builder);
	
	public AqpFunctionQueryBuilder getBuilder(String funcName, QueryNode node);
	
}
