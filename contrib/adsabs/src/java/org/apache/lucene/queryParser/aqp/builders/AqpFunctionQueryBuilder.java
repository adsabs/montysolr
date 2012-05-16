package org.apache.lucene.queryParser.aqp.builders;

import org.apache.lucene.queryParser.core.builders.QueryBuilder;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.solr.search.AqpFunctionQParser;
import org.apache.solr.search.ValueSourceParser;


public interface AqpFunctionQueryBuilder extends QueryBuilder {
	
	
	public AqpFunctionQParser getParser(QueryNode node);
	
	public ValueSourceParser getValueSourceParser();
	
	
}
