package org.apache.lucene.queryParser.aqp.config;

import org.apache.lucene.queryParser.aqp.config.AqpFeedbackEvent;

public interface AqpFeedbackEventHandler {
	
	public enum ACTION {STOP,CONTINUE, BREAK};  
	
	/**
	 * Handles the {@link AqpFeedbackEvent}
	 * 
	 * If it returns false, the next registered event handler 
	 * will not get a chance to handle the event.
	 * 
	 * @param event
	 * @return
	 */
	public ACTION handle(AqpFeedbackEvent event);
}
