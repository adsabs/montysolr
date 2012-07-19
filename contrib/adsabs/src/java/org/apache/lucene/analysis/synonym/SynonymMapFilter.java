package org.apache.lucene.analysis.synonym;

import org.apache.solr.analysis.WriteableSynonymMap;

public interface SynonymMapFilter {
	public SynonymMap getSynonymMap();
	public void setSynonymMap(WriteableSynonymMap synMap);
}
