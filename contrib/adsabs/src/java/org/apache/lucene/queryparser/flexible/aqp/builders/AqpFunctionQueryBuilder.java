package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.solr.search.AqpFunctionQParser;
import org.apache.solr.search.ValueSourceParser;


public interface AqpFunctionQueryBuilder extends QueryBuilder {
	
	
	public AqpFunctionQParser getParser(QueryNode node);
	
	public ValueSourceParser getValueSourceParser();
	
	
}
