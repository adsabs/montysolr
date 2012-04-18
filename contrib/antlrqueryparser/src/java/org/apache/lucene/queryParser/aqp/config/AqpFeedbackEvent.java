package org.apache.lucene.queryParser.aqp.config;

import org.apache.lucene.queryParser.core.nodes.QueryNode;

public class AqpFeedbackEvent {
	public AqpFeedback.TYPE level = null;
	public QueryNode node = null;
	public String message = null;
	public Object[] args = null;
	
	AqpFeedbackEvent(AqpFeedback.TYPE level, QueryNode node, String message, Object...args) {
		this.level = level;
		this.node = node;
		this.message = message;
		this.args = args;
	}
}
