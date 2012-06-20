package org.apache.lucene.search;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;

public class CitesCollector extends Collector implements SetCollector {

	private Scorer scorer;
	private IndexReader reader;
	private int docBase;
	private String indexField;
	private Map<Integer, Integer> fieldCache;
	private Set<Integer> recids;
	
	public CitesCollector(Map<Integer, Integer> cache, String field) {
		super();
		fieldCache = cache;
		indexField = field;
		recids = new HashSet<Integer>();
	}

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;

	}

	@Override
	public void collect(int doc) throws IOException {
		Document document = reader.document(docBase + doc);
		String[] vals = document.getValues(indexField);
		Integer va;
		for (String v: vals) {
			va = Integer.valueOf(v);
			if (fieldCache.containsKey(va)) {
				recids.add(fieldCache.get(va));
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
	
	public Set<Integer> getHits() {
		return recids;
	}
	
	@Override
	public String toString() {
		return "cites[using:" + indexField + "]";
	}
	
	/** Returns a hash code value for this object. */
	public int hashCode() {
		return indexField.hashCode() ^ fieldCache.hashCode();
	}
	
	/** Returns true iff <code>o</code> is equal to this. */
	public boolean equals(Object o) {
		if (o instanceof CitesCollector) {
			CitesCollector fq = (CitesCollector) o;
			return hashCode() == fq.hashCode();
		}
		return false;
	}

}
