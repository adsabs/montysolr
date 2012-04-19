package org.apache.lucene.queryParser.aqp.config;

import org.apache.lucene.util.AttributeImpl;

public class DefaultDateRangeFieldImpl extends AttributeImpl 
	implements DefaultDateRangeField {
	
	private String dateField = null;
	
	public void setField(String fieldName) {
		dateField = fieldName;
	}

	public String getField() {
		return dateField;
	}

	@Override
	public void clear() {
		dateField = null;
	}

	@Override
	public void copyTo(AttributeImpl target) {
		throw new UnsupportedOperationException();
	}

}
