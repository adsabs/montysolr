package org.apache.lucene.search;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;

public class CitesCollectorString extends Collector implements SetCollector {

	protected Scorer scorer;
	protected IndexReader reader;
	protected int docBase;
	protected String indexField;
	protected Map<String, Integer> fieldCache = null;
	protected Set<Integer> recids;
	
	public CitesCollectorString(Map<String, Integer> cache, String field) {
		super();
		fieldCache = cache;
		indexField = field;
		recids = new HashSet<Integer>();
		docBase = 0;
	}
	
	

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;

	}

	@Override
	public void collect(int doc) throws IOException {
		try {
			Document document = reader.document(docBase + doc);
			String[] vals = document.getValues(indexField); //TODO: optimize, read only one value
			for (String v: vals) {
				v = v.toLowerCase();
				if (fieldCache.containsKey(v)) {
					recids.add(fieldCache.get(v));
				}
			}
		}
		catch (IOException e) {
			System.err.print("Error doc: " + doc + " docBase: " + docBase + " reader: " + reader.maxDoc() + ". ");
			throw e;
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
