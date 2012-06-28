package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;

public class SecondOrderCollectorCites extends AbstractSecondOrderCollector {

	
	public SecondOrderCollectorCites(Map<String, Integer> cache, String field) {
		super();
		fieldCache = cache;
		indexField = field;
		hits = new ArrayList<ScoreDoc>();
		docBase = 0;
	}
	
	

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;

	}

	@Override
	public void collect(int doc) throws IOException {
		if (reader.isDeleted(doc)) return;
		
		Document document = reader.document(doc); //TODO: optimize, read only one value
		
		String[] vals = document.getValues(indexField); 
		for (String v: vals) {
			v = v.toLowerCase();
			if (fieldCache.containsKey(v)) {
				hits.add(new ScoreDoc(fieldCache.get(v), scorer.score()));
			}
		}
	}

	@Override
	public void setNextReader(IndexReader reader, int docBase)
			throws IOException {
		this.reader = reader;
		this.docBase = docBase;

	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}
	
	
	@Override
	public String toString() {
		return "cites[using:" + indexField + "]";
	}
	
	
}
