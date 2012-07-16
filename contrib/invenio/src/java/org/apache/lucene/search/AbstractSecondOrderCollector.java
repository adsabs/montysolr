package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.ReaderUtil;

public abstract class AbstractSecondOrderCollector 
	extends Collector implements SecondOrderCollector {
	
	protected Scorer scorer;
	protected IndexReader reader;
	protected int docBase;
	protected List<ScoreDoc> hits;
	protected int[][] subReaderRanges;
	protected volatile boolean organized = false;
	protected List<List<ScoreDoc>> subReaderHits;
	protected Lock lock = null;
	
	
	public AbstractSecondOrderCollector() {
		lock = new ReentrantLock();
		hits = new ArrayList<ScoreDoc>();
	}
	
	public void searcherInitialization(Searcher searcher) throws IOException {
		
	}
	
	public void initSubReaderRanges(IndexReader reader) {
		int[][] ranges = getSubReaderRanges(reader);
		setSubReaderRanges(ranges);
	}
	
	public int[][] getSubReaderRanges(IndexReader reader) {
		List<IndexReader> subReadersList = new ArrayList<IndexReader>();
	    ReaderUtil.gatherSubReaders(subReadersList, reader);
	    IndexReader[] subReaders = subReadersList.toArray(new IndexReader[subReadersList.size()]);
	    int[][] docRanges = new int[subReaders.length][];
	    int maxDoc = 0;
	    for (int i = 0; i < subReaders.length; i++) {
	    	docRanges[i] =  new int[]{subReaders[i].hashCode(), maxDoc};
	        maxDoc += subReaders[i].maxDoc();
	    }
	    return docRanges;
	}

	
	public void setSubReaderRanges(int[][] ranges) {
		this.subReaderRanges = ranges;
		subReaderHits = new ArrayList<List<ScoreDoc>>(ranges.length);
		for (int i=0;i<ranges.length;i++) {
			subReaderHits.add(new ArrayList<ScoreDoc>());
		}
	}
	
	
	public int getSubReaderDocBase(IndexReader reader) {
		int hc = reader.hashCode();
		for (int[] range: subReaderRanges) {
			if (range[0] == hc) {
				return range[1];
			}
		}
		// should never happen
		throw new IllegalStateException("There exists no reader: " + reader);
	}

	public List<ScoreDoc> getSubReaderResults(IndexReader reader) {
		
		lock.lock();
        try {
        	if (!organized) {
    			extractRanges();
    			organized = true;
    		}
        } finally {
        	lock.unlock();
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
		throw new IllegalStateException("There are no hits for the reader: " + reader);
	}
	

	private void extractRanges() {
		
		sortHits();
		compactHits();
		
		
		int i = 0;
		int currMax = subReaderRanges.length > i+1 ? subReaderRanges[i+1][1] : DocIdSetIterator.NO_MORE_DOCS;
		List<ScoreDoc> currReader = subReaderHits.get(i);
		for (ScoreDoc d: hits) {
			if (d.doc > currMax) {
				i += 1;
				currMax = subReaderRanges.length > i+1 ? subReaderRanges[i+1][1] : DocIdSetIterator.NO_MORE_DOCS;
				currReader = currReader = subReaderHits.get(i);
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
				if (currDoc == null) currDoc = d;
				continue;
			}
			
			if (seenTimes>1) currDoc.score = score/seenTimes;
			newHits.add(currDoc);
			
			currDoc = d;
			score = d.score;
			seenTimes = 1;
		}
		if (currDoc != null) {
			if (seenTimes>1) currDoc.score = score/seenTimes;
			newHits.add(currDoc);
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
	
	/** Returns true iff <code>o</code> is equal to this. */
	public boolean equals(Object o) {
		if (o instanceof SecondOrderCollector) {
			SecondOrderCollector fq = (SecondOrderCollector) o;
			return hashCode() == fq.hashCode();
		}
		return false;
	}
	
	
}
