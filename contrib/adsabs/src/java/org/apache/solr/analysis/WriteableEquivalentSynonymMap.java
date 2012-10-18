package org.apache.solr.analysis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WriteableEquivalentSynonymMap extends WriteableExplicitSynonymMap {
	
	private static final String OUTPUT_KEY_SEPARATOR = ",";

	public WriteableEquivalentSynonymMap(String outfile) {
		super(outfile);
	}
	
	@Override
	public void put(String key, Set<String> synonyms) {
		//log.trace("setting " + k + " to " + v);
		numUpdates++;
		synonyms.add(key);
		for (String syn : synonyms) {
			Set<String> values = new HashSet<String>(synonyms);
			values.remove(syn);
			this.map.put(syn, values);
		}
	}
	
	@Override
	protected String getOutputKeySeparator() {
		return OUTPUT_KEY_SEPARATOR;
	}
	
	/*
	 * this parses the non-explicit mapping syntax:
	 * 
	 * tokena, tokenb, tokenc
	 */
	@Override
	public void parseRules(List<String> rules) {
		for (String rule : rules) {
			Set<String> tokens = getSynList(rule);
			for (String key : tokens) {
				Set<String> values = new HashSet<String>(tokens);
				values.remove(key);
			    this.map.put(key.replace("\\,", ",").replace("\\ ", " "), values);
			}
		}
	}
}
