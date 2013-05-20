package org.apache.lucene.queryparser.flexible.aqp.config;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedbackEvent;

public interface AqpFeedbackEventHandler {

  public enum ACTION {
    STOP, SAVE_EVENT
  };

  /**
   * Handles the {@link AqpFeedbackEvent}
   * 
   * If it returns false, the next registered event handler will not get a
   * chance to handle the event.
   * 
   * @param event
   * @return
   */
  public ACTION handle(AqpFeedbackEvent event);
}
