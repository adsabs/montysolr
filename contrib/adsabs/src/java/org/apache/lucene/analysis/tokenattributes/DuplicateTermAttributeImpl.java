package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.util.AttributeImpl;

public class DuplicateTermAttributeImpl extends AttributeImpl implements DuplicateTermAttribute {
	
	private String v;
	

	public String getValue() {
		return v;
	}

	public void setValue(String val) {
		v = val;
	}

	@Override
	public void clear() {
		v = null;
	}
	
	@Override
	public void copyTo(AttributeImpl target) {
		DuplicateTermAttributeImpl t = (DuplicateTermAttributeImpl) target;
		t.setValue(v);
	}
	
}
