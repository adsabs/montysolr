package org.apache.lucene.queryparser.flexible.aqp.config;

public interface AqpFeedbackEventHandler {

    enum ACTION {
        STOP, SAVE_EVENT
    }

    /**
     * Handles the {@link AqpFeedbackEvent}
     * <p>
     * If it returns false, the next registered event handler will not get a
     * chance to handle the event.
     *
     * @return STOP or SAVE_EVENT action
     */
    ACTION handle(AqpFeedbackEvent event);
}
