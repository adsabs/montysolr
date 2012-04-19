package org.apache.lucene.queryParser.aqp.config;

import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.util.Attribute;


public interface AqpFeedbackEvent extends Attribute {
	
	public AqpFeedback.TYPE getType();
	public Class<? extends QueryNodeProcessor> getCaller();
	public QueryNode getNode();
	public String getMessage();
	public Object[] getArgs();
	
}
