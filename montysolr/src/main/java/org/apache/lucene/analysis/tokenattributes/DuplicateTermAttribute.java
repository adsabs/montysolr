package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.util.Attribute;

public interface DuplicateTermAttribute extends Attribute {
	
	public String getValue();
	public void setValue(String val);
	public void clear();
}
