package org.apache.lucene.queryparser.flexible.aqp;

import org.apache.lucene.queryparser.flexible.core.config.ConfigurationKey;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.FieldBoostMapFCListener;
import org.apache.lucene.queryparser.flexible.standard.config.FieldDateResolutionFCListener;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpAdslabsFunctionProvider;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpAdslabsSubSueryProvider;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpSolrFunctionProvider;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdslabsLoggingHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedback;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFunctionQueryBuilderConfig;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFunctionQueryBuilderConfigImpl;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpInvenioQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.InvenioQueryAttribute;
import org.apache.lucene.queryparser.flexible.aqp.config.InvenioQueryAttributeImpl;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpInvenioQueryConfigHandler.ConfigurationKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AqpAdslabsQueryConfigHandler extends AqpStandardQueryConfigHandler {
	public static final Logger log = LoggerFactory
			.getLogger(AqpAdslabsQueryConfigHandler.class);
	
	final public static class ConfigurationKeys  {
		
		/**
	     * Here one can store AqpRequest object
	     */
	    final public static ConfigurationKey<AqpRequestParams> SOLR_REQUEST = ConfigurationKey.newInstance();
	    
	    final public static ConfigurationKey<AqpFunctionQueryBuilderConfig> FUNCTION_QUERY_BUILDER_CONFIG = ConfigurationKey.newInstance();
	    
	    final public static ConfigurationKey<String> DEFAULT_DATE_RANGE_FIELD = ConfigurationKey.newInstance();
	}
	
	public AqpAdslabsQueryConfigHandler() {
		
		super();
		
		// Add listener that will build the FieldConfig attributes.
		addFieldConfigListener(new FieldBoostMapFCListener(this));
		addFieldConfigListener(new FieldDateResolutionFCListener(this));

		// Default Values
		set(AqpInvenioQueryConfigHandler.ConfigurationKeys.INVENIO_QUERY, new InvenioQueryAttributeImpl());
		set(ConfigurationKeys.SOLR_REQUEST, null);
		set(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG, new AqpFunctionQueryBuilderConfigImpl());
		
		get(AqpStandardQueryConfigHandler.ConfigurationKeys.FEEDBACK).registerEventHandler(new AqpAdslabsLoggingHandler());
		set(ConfigurationKeys.DEFAULT_DATE_RANGE_FIELD, "date");
		
		set(StandardQueryConfigHandler.ConfigurationKeys.ALLOW_LEADING_WILDCARD, true);
		
		get(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG).addProvider(new AqpAdslabsFunctionProvider());
		
		//XXX: must find a better way to signal presence of solr
		if (true) {
			get(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG).addProvider(new AqpSolrFunctionProvider());
			get(ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG).addProvider(new AqpAdslabsSubSueryProvider());
		}
	}
}
