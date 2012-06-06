package org.apache.lucene.queryParser.aqp.config;

import java.util.Map;

import org.apache.lucene.util.AttributeImpl;

public class AqpFieldMapperImpl extends AttributeImpl
	implements AqpFieldMapper {

	private static final long serialVersionUID = -2492592672046008602L;
	private Map<String, String> map = null;
	
	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public Map<String, String> getMap() {
		return this.map;
	}

	@Override
	public void clear() {
		map = null;
	}

	@Override
	public void copyTo(AttributeImpl target) {
		throw new IllegalAccessError();
		
	}

}
