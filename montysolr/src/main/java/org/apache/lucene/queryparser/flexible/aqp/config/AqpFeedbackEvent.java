package org.apache.lucene.queryparser.flexible.aqp.config;

import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.util.Attribute;

public interface AqpFeedbackEvent extends Attribute {

    AqpFeedback.TYPE getType();

    Class<? extends QueryNodeProcessor> getCaller();

    QueryNode getNode();

    String getMessage();

    Object[] getArgs();

}
