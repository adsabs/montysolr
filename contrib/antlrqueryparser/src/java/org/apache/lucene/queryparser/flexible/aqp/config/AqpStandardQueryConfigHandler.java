package org.apache.lucene.queryparser.flexible.aqp.config;

import org.apache.lucene.queryparser.flexible.core.config.ConfigurationKey;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.processors.PhraseSlopQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpBOOSTProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQNORMALProcessor;

public class AqpStandardQueryConfigHandler extends StandardQueryConfigHandler {
	
	final public static class ConfigurationKeys  {
	    
		/**
		 * This attribute is used by {@link AqpQNORMALProcessor} processor and
		 * must be defined in the {@link QueryConfigHandler}. This attribute tells the
		 * processor what is the default field when no field is defined in a
		 * phrase. <br/>
		 * 
		 */
	    final public static ConfigurationKey<String> DEFAULT_FIELD = ConfigurationKey.newInstance();
	    
	    /**
	     * This attribute is used by {@link PhraseSlopQueryNodeProcessor} processor and
	     * must be defined in the {@link QueryConfigHandler}. This attribute tells the
	     * processor what is the default phrase slop when no slop is defined in a
	     * phrase. <br/>
	     * 
	     */
	    final public static ConfigurationKey<Integer> DEFAULT_PROXIMITY = ConfigurationKey.newInstance();
	    
	    /**
	     * Value of the default boost, to be used when user specified '^' without a value
	     * @see AqpBOOSTProcessor
	     */
	    final public static ConfigurationKey<Float> IMPLICIT_BOOST = ConfigurationKey.newInstance();
	    
	    
	    /**
	     * This attribute is used to collect feedback messages and suggestions
	     * from the query parser
	     * 
	     * @see AqpFeedback#registerEventHandler(AqpFeedbackEventHandler)
	     * @see AqpFeedback#sendEvent(AqpFeedbackEvent)
	     */
	    final public static ConfigurationKey<AqpFeedback> FEEDBACK = ConfigurationKey.newInstance();
	}
	
	
	public AqpStandardQueryConfigHandler() {
		super();
		
	    // Add listener that will build the FieldConfig attributes.
		// None for now

	    // Default Values (besides the standard ones)
	    set(ConfigurationKeys.DEFAULT_FIELD, null);
	    set(ConfigurationKeys.DEFAULT_PROXIMITY, 5);
	    set(ConfigurationKeys.IMPLICIT_BOOST, 1.0f);
	    set(ConfigurationKeys.FEEDBACK, new AqpFeedbackImpl());
	    
	  }

}
