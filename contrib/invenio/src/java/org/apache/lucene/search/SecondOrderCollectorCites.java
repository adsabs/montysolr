package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;

public class SecondOrderCollectorCites extends Collector implements SecondOrderCollector {

	protected Scorer scorer;
	protected IndexReader reader;
	protected int docBase;
	protected String indexField;
	protected Map<String, Integer> fieldCache = null;
	private List<ScoreDoc> hits;
	private int[][] subReaderRanges;
	volatile boolean sorted = false;
	private List<List<ScoreDoc>> subReaderHits;
	
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
		try {
			Document document = reader.document(docBase + doc);
			String[] vals = document.getValues(indexField); //TODO: optimize, read only one value
			for (String v: vals) {
				v = v.toLowerCase();
				if (fieldCache.containsKey(v)) {
					hits.add(new ScoreDoc(fieldCache.get(v), scorer.score()));
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
		if (o instanceof SecondOrderCollectorCites) {
			SecondOrderCollectorCites fq = (SecondOrderCollectorCites) o;
			return hashCode() == fq.hashCode();
		}
		return false;
	}



	public void setSubReaderRanges(int[][] ranges) {
		this.subReaderRanges = ranges;
		subReaderHits = new ArrayList<List<ScoreDoc>>(ranges.length);
	}


	public List<ScoreDoc> getSubReaderScoreDocs(IndexReader reader) {
		
		if (subReaderHits == null) {
			extractRanges();
		}
		int hc = reader.hashCode();
		int i = 0;
		for (int[] range: subReaderRanges) {
			if (range[0] == hc) {
				return subReaderHits.get(i);
			}
			i++;
		}
		// should never happen
		throw new IllegalStateException("There are no hits for the reader with id: " + reader);
	}



	private void extractRanges() {
		if (!sorted) {
			sortHits();
			compactHits();
		}
		
		
		int i = 0;
		int currMax = subReaderRanges[i][1];
		List<ScoreDoc> currReader = subReaderHits.get(i);
		for (ScoreDoc d: hits) {
			if (d.doc > currMax) {
				i += 1;
				currMax = subReaderRanges[i][1];
				currReader = subReaderHits.get(i);
			}
			currReader.add(d);
		}
		
		
	}


	private void compactHits() {
		ArrayList<ScoreDoc> newHits = new ArrayList<ScoreDoc>(new Float((hits.size() * 0.75f)).intValue());
		
		ScoreDoc currDoc = null;
		int seenTimes = 0;
		float score = 0.0f;
		
		for (ScoreDoc d: hits) {
			if (currDoc == null || d.doc == currDoc.doc) {
				score += d.score;
				seenTimes += 1;
				continue;
			}
			
			currDoc.score = score/seenTimes;
			newHits.add(currDoc);
			
			currDoc = d;
			score = d.score;
			seenTimes = 1;
		}
		
		hits = newHits;
	}

	private void sortHits() {
		
		Collections.sort(hits, new Comparator<ScoreDoc>() {
		    public int compare(ScoreDoc o1, ScoreDoc o2) {
		        return o1.doc - o2.doc;
		    }
		}); 
		
	}

}
