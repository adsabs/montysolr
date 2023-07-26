package org.apache.lucene.queryparser.flexible.aqp.config;

import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.util.Attribute;

public interface AqpFeedbackEvent extends Attribute {

  public AqpFeedback.TYPE getType();

  public Class<? extends QueryNodeProcessor> getCaller();

  public QueryNode getNode();

  public String getMessage();

  public Object[] getArgs();

}
