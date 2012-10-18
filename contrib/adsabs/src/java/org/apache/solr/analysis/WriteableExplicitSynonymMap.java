package org.apache.solr.analysis;

import java.util.*;
import java.util.regex.*;

import org.apache.solr.common.util.StrUtils;



public class WriteableExplicitSynonymMap extends WriteableSynonymMap {
	
	private static final String OUTPUT_KEY_SEPARATOR = "=>";
	
    public WriteableExplicitSynonymMap(String outFile) {
    	super(outFile);
	}
    
	@Override
	public void put(String k, Set<String> v) {
		//log.trace("setting " + k + " to " + v);
		numUpdates++;
		this.map.put(k, v);
	}
	
	@Override
	protected String getOutputKeySeparator() {
		return OUTPUT_KEY_SEPARATOR;
	}
	
	/*
	 * this is much simplified version of synonym rules that
	 * supports:
	 * 
	 * token=>token,token\\ tokenb,token
	 */
	@Override
	public void parseRules(List<String> rules) {
		
		for (String rule : rules) {
			List<String> mapping = StrUtils.splitSmart(rule, "=>", false);
		    if (mapping.size() != 2) 
		    	log.error("Invalid Synonym Rule:" + rule);
		    String key = mapping.get(0).trim();
		    Set<String> values = getSynList(mapping.get(1));
		    this.map.put(key.replace("\\,", ",").replace("\\ ", " "), values);
		}
	}
	
}
