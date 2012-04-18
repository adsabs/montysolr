package org.adsabs.solr.analysis;

import java.util.*;
import java.util.regex.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorSynonymMap {
    public static final Logger log = LoggerFactory.getLogger(AuthorSynonymFilter.class);
	private HashMap<String, List<String>> map;
	
	public AuthorSynonymMap() {
		this.map = new HashMap<String, List<String>>();
	}
	
	public List<String> put(String k, List<String> v) {
		log.debug("setting " + k + " to " + v);
		return this.map.put(k, v);
	}
	
	public List<String> get(String k) {
		return this.map.get(k);
	}
	
	public List<String> get(Pattern p) {
		for (String k : this.map.keySet()) {
			Matcher m = p.matcher(k);
			if (m.matches()) {
				return this.map.get(k);
			}
		}
		return null;
	}
}
