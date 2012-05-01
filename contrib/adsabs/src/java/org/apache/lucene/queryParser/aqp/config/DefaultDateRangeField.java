package org.apache.lucene.queryParser.aqp.config;

import org.apache.lucene.util.Attribute;

public interface DefaultDateRangeField extends Attribute {
	public void setField(String fieldName);
	public String getField();
}

