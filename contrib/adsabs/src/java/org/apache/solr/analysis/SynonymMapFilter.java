package org.apache.solr.analysis;

import org.adsabs.solr.analysis.WriteableSynonymMap;

public interface SynonymMapFilter {
	public SynonymMap getSynonymMap();
	public void setSynonymMap(WriteableSynonymMap synMap);
}
