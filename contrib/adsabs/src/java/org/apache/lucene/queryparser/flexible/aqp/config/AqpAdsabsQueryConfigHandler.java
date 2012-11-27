package org.apache.lucene.queryparser.flexible.aqp.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.queryparser.flexible.core.config.ConfigurationKey;
import org.apache.lucene.queryparser.flexible.standard.config.FieldBoostMapFCListener;
import org.apache.lucene.queryparser.flexible.standard.config.FieldDateResolutionFCListener;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpAdsabsFunctionProvider;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpInvenioQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.InvenioQueryAttributeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AqpAdsabsQueryConfigHandler extends AqpStandardQueryConfigHandler {
	public static final Logger log = LoggerFactory
			.getLogger(AqpAdsabsQueryConfigHandler.class);
	
	final public static class ConfigurationKeys  {
		
		/**
	     * Here one can store AqpRequest object
	     */
	    final public static ConfigurationKey<AqpRequestParams> SOLR_REQUEST = ConfigurationKey.newInstance();
	    
	    final public static ConfigurationKey<AqpFunctionQueryBuilderConfig> FUNCTION_QUERY_BUILDER_CONFIG = ConfigurationKey.newInstance();
	    
	    final public static ConfigurationKey<String> DEFAULT_DATE_RANGE_FIELD = ConfigurationKey.newInstance();
	    
	    final public static ConfigurationKey<String> DEFAULT_IDENTIFIER_FIELD = ConfigurationKey.newInstance();
	    
	    final public static ConfigurationKey<Logger> SOLR_LOGGER = ConfigurationKey.newInstance();
	    
	    final public static ConfigurationKey<Boolean> SOLR_READY = ConfigurationKey.newInstance();
	    
	    final public static ConfigurationKey<Map<String, int[]>> AUTHOR_FIELDS = ConfigurationKey.newInstance();
	    
	}
	
	public AqpAdsabsQueryConfigHandler() {
		
		super();
		
		// Add listener that will build the FieldConfig attributes.
		addFieldConfigListener(new FieldBoostMapFCListener(this));
		addFieldConfigListener(new FieldDateResolutionFCListener(this));

		// Default Values
		set(AqpInvenioQueryConfigHandler.ConfigurationKeys.INVENIO_QUERY, new InvenioQueryAttributeImpl());
		set(ConfigurationKeys.SOLR_REQUEST, new AqpRequestParamsImpl());
		set(ConfigurationKeys.SOLR_LOGGER, log);
		set(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG, new AqpFunctionQueryBuilderConfigImpl());
		
		get(AqpStandardQueryConfigHandler.ConfigurationKeys.FEEDBACK).registerEventHandler(new AqpAdsabsLoggingHandler());
		set(ConfigurationKeys.DEFAULT_DATE_RANGE_FIELD, "date");
		set(ConfigurationKeys.DEFAULT_IDENTIFIER_FIELD, "identifier");
		
		set(StandardQueryConfigHandler.ConfigurationKeys.ALLOW_LEADING_WILDCARD, true);
		
		get(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG).addProvider(new AqpAdsabsFunctionProvider());
		
		
		set(AqpStandardQueryConfigHandler.ConfigurationKeys.ALLOW_SLOW_FUZZY, true);
		set(AqpStandardQueryConfigHandler.ConfigurationKeys.IMPLICIT_FUZZY, 2.0f);
		
		// Now inside the solrconfig.xml
		//Map<String, String> fieldMap = get(AqpStandardQueryConfigHandler.ConfigurationKeys.FIELD_MAPPER);
		//fieldMap.put("arxiv", "identifier");
		
		/*
		 * Warning: some of these values can be set also inside solrconfig.xml
		 * Check the ads-config section (applies to solr QParsers only)
		 */
    set(StandardQueryConfigHandler.ConfigurationKeys.DEFAULT_OPERATOR, Operator.AND);
    set(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_READY, true);
    
    set(AqpAdsabsQueryConfigHandler.ConfigurationKeys.AUTHOR_FIELDS, new HashMap<String, int[]>());
	    
	}
}
