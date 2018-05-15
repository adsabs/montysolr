package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;

/**
 * 
 * Helper class to make it easier to retrieve config values from the aqp parser
 * config/url params.
 * 
 * @see org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl
 */
public abstract class AqpQueryNodeProcessorImpl extends QueryNodeProcessorImpl {
  
  public boolean hasConfigMap() {
    return getQueryConfigHandler().get(
        AqpStandardQueryConfigHandler.ConfigurationKeys.NAMED_PARAMETER) != null;
  }
  /**
   * @return map with config values (all is string)
   */
  public Map<String, String> getConfigMap() {
    Map<String, String> args = getQueryConfigHandler().get(
        AqpStandardQueryConfigHandler.ConfigurationKeys.NAMED_PARAMETER);
    if (args == null)
      return new HashMap<String, String>();
    return args;
  }
  
  public String getConfigVal(String key) {
    Map<String, String> args = getConfigMap();
    if (args.containsKey(key)) {
      return args.get(key);
    }
    return null;
  }
  
  public String getConfigVal(String key, String defaultVal) {
    Map<String, String> args = getConfigMap();
    if (args.containsKey(key)) {
      return args.get(key);
    }
    return defaultVal;
  }

}
