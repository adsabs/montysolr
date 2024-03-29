package org.apache.lucene.queryparser.flexible.aqp.config;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedback.TYPE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AqpAdsabsLoggingHandler implements AqpFeedbackEventHandler {

    public static final Logger log = LoggerFactory
            .getLogger(AqpAdsabsLoggingHandler.class);

    public ACTION handle(AqpFeedbackEvent event) {
        TYPE type = event.getType();
        if (type.equals(AqpFeedback.TYPE.INFO)) {
            log.info(event.getMessage());
            log.debug(event.getNode().toString());
        } else if (type.equals(AqpFeedback.TYPE.DEBUG)) {
            log.debug(event.getMessage());
            log.debug(event.getNode().toString());
        } else if (type.equals(AqpFeedback.TYPE.WARN)) {
            log.warn(event.getMessage());
        }
        return ACTION.STOP;
    }

}
