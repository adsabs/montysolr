package org.apache.solr.analysis;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class WriteableEquivalentSynonymMap extends WriteableSynonymMap {
	
  private HashSet<Integer> alreadySeen;


  public WriteableEquivalentSynonymMap(String outfile) {
		super(outfile);
		alreadySeen = new HashSet<Integer>();
	}
	
	@Override
	public void add(String key, Set<String> values) {
		Set<String> masterSet = null;
    if (containsKey(key)) {
      masterSet = get(key);
    }
    else {
      for (String k: values) {
        if (containsKey(k)) {
          masterSet = get(k);
          break;
        }
      }
      if (masterSet==null) {
        masterSet = new LinkedHashSet<String>();
      }
    }
    masterSet.add(key);
    masterSet.addAll(values);
    
    for (String redo: masterSet) {
      put(redo, masterSet);
    }
	}
	
	
	/*
	 * this parses the non-explicit mapping syntax:
	 * 
	 * tokena, tokenb, tokenc
	 */
	@Override
	public void populateMap(List<String> rules) {
		for (String rule : rules) {
			Set<String> tokens = splitValues(rule);
			for (String t: tokens) {
			  add(t, tokens);
			  break; // we can skip the rest
			}
		}
	}

	
	@Override
  public String formatEntry(String key, Set<String> values) {
	  if (alreadySeen.contains(values.hashCode())) {
	    return "";
	  }
	  alreadySeen.add(values.hashCode());
	  
    StringBuffer out = new StringBuffer();
    boolean notFirst = false;
    for (String s : values) {
      if (notFirst) out.append(","); 
      out.append(s.replace(",", "\\,").replace(" ", "\\ "));
      notFirst=true;
    }
    out.append("\n");
    return out.toString();
  }
	
	@Override
	public void clear() {
	  alreadySeen.clear();
	  super.clear();
	}
	
	@Override
	public boolean persist(boolean append) throws IOException {
	  if (append == false) alreadySeen.clear();
	  return super.persist(append);
	}
}
