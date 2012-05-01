package org.apache.lucene.queryParser.aqp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.lucene.queryParser.aqp.config.AqpFeedback.TYPE;
import org.apache.lucene.queryParser.aqp.config.AqpFeedbackEvent;

public class AqpAdslabsLoggingHandler implements AqpFeedbackEventHandler {
	
	public static final Logger log = LoggerFactory
		.getLogger(AqpAdslabsLoggingHandler.class); 
	
	public ACTION handle(AqpFeedbackEvent event) {
		TYPE type = event.getType();
		if (type.equals(AqpFeedback.TYPE.INFO)) {
			log.info(event.getMessage());
			log.debug(event.getNode().toString());
		}
		else if (type.equals(AqpFeedback.TYPE.DEBUG)) {
			log.debug(event.getMessage());
			log.debug(event.getNode().toString());
		}
		else if (type.equals(AqpFeedback.TYPE.WARN)) {
			log.warn(event.getMessage());
		}
		return ACTION.STOP;
	}

}
