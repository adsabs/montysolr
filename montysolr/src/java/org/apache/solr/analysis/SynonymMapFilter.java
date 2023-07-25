package org.apache.solr.analysis;

import org.apache.lucene.analysis.synonym.SynonymMap;

public interface SynonymMapFilter {
	public SynonymMap getSynonymMap();
	public void setSynonymMap(WriteableSynonymMap synMap);
}
