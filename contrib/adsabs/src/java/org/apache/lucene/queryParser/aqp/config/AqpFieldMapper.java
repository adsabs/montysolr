package org.apache.lucene.queryParser.aqp.config;

import java.util.Map;

import org.apache.lucene.util.Attribute;


public interface AqpFieldMapper extends Attribute {
	public void setMap(Map<String, String> map);
	public Map<String, String> getMap();
}
