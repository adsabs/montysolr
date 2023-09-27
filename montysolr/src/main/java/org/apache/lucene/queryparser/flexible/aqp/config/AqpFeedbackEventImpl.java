package org.apache.lucene.queryparser.flexible.aqp.config;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedback.TYPE;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;

public class AqpFeedbackEventImpl implements AqpFeedbackEvent {

    private AqpFeedback.TYPE type = null;
    private Class<? extends QueryNodeProcessor> caller = null;
    private QueryNode node = null;
    private String message = null;
    private Object[] args = null;

    AqpFeedbackEventImpl(AqpFeedback.TYPE type,
                         Class<? extends QueryNodeProcessor> processorClass, QueryNode node,
                         String message, Object... args) {

        this.type = type;
        this.caller = processorClass;
        this.node = node;
        this.message = message;
        this.args = args;

    }

    public TYPE getType() {
        return type;
    }

    public Class<? extends QueryNodeProcessor> getCaller() {
        return caller;
    }

    public QueryNode getNode() {
        return node;
    }

    public String getMessage() {
        return message;
    }

    public Object[] getArgs() {
        return args;
    }
}
