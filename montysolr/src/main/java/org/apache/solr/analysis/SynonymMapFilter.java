package org.apache.solr.analysis;

import org.apache.lucene.analysis.synonym.SynonymMap;

public interface SynonymMapFilter {
    SynonymMap getSynonymMap();

    void setSynonymMap(WriteableSynonymMap synMap);
}
