package org.apache.lucene.queryParser.aqp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.lucene.queryParser.aqp.config.AqpFeedbackEvent;

public class AqpAdslabsLoggingHandler implements AqpFeedbackEventHandler {
	
	public static final Logger log = LoggerFactory
		.getLogger(AqpAdslabsLoggingHandler.class); 
	
	public ACTION handle(AqpFeedbackEvent event) {
		if (event.level.equals(AqpFeedback.TYPE.INFO)) {
			log.info(event.message);
			log.debug(event.node.toString());
		}
		else if (event.level.equals(AqpFeedback.TYPE.DEBUG)) {
			log.debug(event.message);
			log.debug(event.node.toString());
		}
		else if (event.level.equals(AqpFeedback.TYPE.WARN)) {
			log.warn(event.message);
		}
		return ACTION.STOP;
	}

}
