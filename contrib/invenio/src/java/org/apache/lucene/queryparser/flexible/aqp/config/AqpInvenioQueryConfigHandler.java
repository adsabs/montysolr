package org.apache.lucene.queryparser.flexible.aqp.config;

import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.config.ConfigurationKey;

public class AqpInvenioQueryConfigHandler extends AqpStandardQueryConfigHandler {
	final public static class ConfigurationKeys  {
		final public static ConfigurationKey<String> INVENIO_DEFAULT_ID_FIELD = ConfigurationKey.newInstance();
		final public static ConfigurationKey<InvenioQueryAttribute> INVENIO_QUERY = ConfigurationKey.newInstance();
	}
	public AqpInvenioQueryConfigHandler() {
		super();
		
		// Default Values
		set(ConfigurationKeys.INVENIO_DEFAULT_ID_FIELD, "recid");
		set(ConfigurationKeys.INVENIO_QUERY, new InvenioQueryAttributeImpl());
		
	  }
}
