package org.apache.lucene.search;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;

public class CitesCollector extends Collector {

	private Scorer scorer;
	private IndexReader reader;
	private int docBase;
	private String indexField;
	private HashMap<String, Integer> fieldCache;
	private Set recids = new HashSet();

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;

	}

	@Override
	public void collect(int doc) throws IOException {
		Document document = reader.document(docBase + doc);
		String[] vals = document.getValues(indexField);
		for (String v: vals) {
			if (fieldCache.containsKey(v)) {
				recids.add(fieldCache.get(v));
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

}
