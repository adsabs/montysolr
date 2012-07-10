package org.apache.lucene.queryparser.flexible.aqp.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.util.Attribute;

public interface InvenioQueryAttribute extends Attribute {
	
	public static enum Channel {
		INTBITSET, DEFAULT
	};
	public static enum QMode {
		MAXINV, MAXSOLR
	}

	void setChannel(String string);
	Channel getChannel();
	
	void setOverridenFields(String[] xfields);
	void setOverridenFields(ArrayList<String> xfields);
	List<String> getOverridenFields();
	
	
	void setMode(String mode, Boolean schemaHasExplicitAsteriskField);
	QMode getMode();

}
