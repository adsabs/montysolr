package org.apache.lucene.queryparser.flexible.aqp.parser;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.queryparser.flexible.core.config.ConfigurationKey;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.processors.PhraseSlopQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedback;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedbackEvent;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedbackEventHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedbackImpl;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFuzzyModifierNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpBOOSTProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpDEFOPProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpFUZZYProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpFuzzyModifierProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQNORMALProcessor;

public class AqpStandardQueryConfigHandler extends StandardQueryConfigHandler {

  final public static class ConfigurationKeys {

    /**
     * This attribute is used by {@link AqpQNORMALProcessor} processor and must
     * be defined in the {@link QueryConfigHandler}. This attribute tells the
     * processor what is the default field when no field is defined in a phrase.
     * 
     */
    final public static ConfigurationKey<String> DEFAULT_FIELD = ConfigurationKey
        .newInstance();

    /**
     * This attribute is used by {@link PhraseSlopQueryNodeProcessor} processor
     * and must be defined in the {@link QueryConfigHandler}. This attribute
     * tells the processor what is the default phrase slop when no slop is
     * defined in a phrase.
     * 
     */
    final public static ConfigurationKey<Integer> DEFAULT_PROXIMITY = ConfigurationKey
        .newInstance();

    /**
     * This attribute is used by {@link AqpDEFOPProcessor} processor and must be
     * defined in the {@link QueryConfigHandler}. This attribute tells the
     * processor what are the allowed values when user submits them.
     * 
     */
    final public static ConfigurationKey<int[]> ALLOWED_PROXIMITY_RANGE = ConfigurationKey
        .newInstance();

    /**
     * Value of the default boost, to be used when user specified '^' without a
     * value
     * 
     * @see AqpBOOSTProcessor
     */
    final public static ConfigurationKey<Float> IMPLICIT_BOOST = ConfigurationKey
        .newInstance();

    /**
     * This attribute is used to collect feedback messages and suggestions from
     * the query parser
     * 
     * @see AqpFeedback#registerEventHandler(AqpFeedbackEventHandler)
     * @see AqpFeedback#sendEvent(AqpFeedbackEvent)
     */
    final public static ConfigurationKey<AqpFeedback> FEEDBACK = ConfigurationKey
        .newInstance();

    /**
     * Default fuzzy value when user specified only 'term~'
     * 
     * @see AqpFUZZYProcessor
     * @see AqpFuzzyModifierNode
     * @see AqpFuzzyModifierProcessor
     */
    final public static ConfigurationKey<Float> IMPLICIT_FUZZY = ConfigurationKey
        .newInstance();

    /**
     * Allow to use the old-style 0.0-1.0f fuzzy value and let it be handled by
     * the SlowFuzzyQuery
     * 
     * @see AqpFuzzyModifierProcessor
     * 
     */
    final public static ConfigurationKey<Boolean> ALLOW_SLOW_FUZZY = ConfigurationKey
        .newInstance();

    /**
     * Translation mapping for index names
     */
    final public static ConfigurationKey<Map<String, String>> FIELD_MAPPER = ConfigurationKey
        .newInstance();

    /**
     * Translation mapping for index names (after analysis was done)
     */
    final public static ConfigurationKey<Map<String, String>> FIELD_MAPPER_POST_ANALYSIS = ConfigurationKey
        .newInstance();
    
    
    /**
     * Named arguments that can be used in place of non-existant SOLR request
     */
    final public static ConfigurationKey<Map<String, String>> NAMED_PARAMETER = ConfigurationKey
    		.newInstance();
    
  }

  public AqpStandardQueryConfigHandler() {
    super();

    // Add listener that will build the FieldConfig attributes.
    // None for now

    // Default Values (besides the standard ones)
    set(ConfigurationKeys.DEFAULT_FIELD, null);
    set(ConfigurationKeys.DEFAULT_PROXIMITY, 5);
    set(ConfigurationKeys.ALLOWED_PROXIMITY_RANGE, new int[] { 1, 5 });
    set(ConfigurationKeys.IMPLICIT_BOOST, 1.0f);
    set(ConfigurationKeys.IMPLICIT_FUZZY, 0.5f);
    set(ConfigurationKeys.FEEDBACK, new AqpFeedbackImpl());
    set(ConfigurationKeys.FIELD_MAPPER, new HashMap<String, String>());
    set(ConfigurationKeys.FIELD_MAPPER_POST_ANALYSIS,
        new HashMap<String, String>());
    set(ConfigurationKeys.NAMED_PARAMETER, new HashMap<String, String>());
  }

}
