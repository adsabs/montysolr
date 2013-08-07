package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public abstract class AbstractSecondOrderCollector extends Collector implements
SecondOrderCollector {

	protected Scorer scorer;
	protected int docBase;
	protected List<CollectorDoc> hits;
	protected volatile boolean organized = false;
	protected Lock lock = null;
	private Integer lastPos = null;
	protected float ensureCapacityRatio = 0.25f;
	protected boolean firstOrderScorerOutOfOrder = false;

	public AbstractSecondOrderCollector() {
		lock = new ReentrantLock();
		hits = new ArrayList<CollectorDoc>();
	}

	public boolean searcherInitialization(IndexSearcher searcher, Weight firstOrderWeight) throws IOException {
		// this is pretty arbitrary, but 2nd order queries may return many hits...
		((ArrayList<CollectorDoc>) hits).ensureCapacity((int) (searcher.getIndexReader().maxDoc() * ensureCapacityRatio));
		if (firstOrderWeight != null)
			firstOrderScorerOutOfOrder = firstOrderWeight.scoresDocsOutOfOrder();
		return true;
	}

	public List<CollectorDoc> getSubReaderResults(int rangeStart, int rangeEnd) {

		if (hits.size() == 0)
			return null;

		int i = -1;

		lock.lock();
		try {
			if (!organized) {
				organizeResults();
				organized = true;
			}
		} finally {
			lock.unlock();
		}

		i = findClosestIndex(rangeStart, rangeEnd, 0, hits.size()-1);
		if (i == -1 || i+1 > hits.size())
			return null;


		ArrayList<CollectorDoc> results = new ArrayList<CollectorDoc>();
		for (;i<hits.size() && hits.get(i).doc < rangeEnd;i++) {
			results.add(hits.get(i));
		}
		return results;

	}

	private int findClosestIndex(int rangeStart, int rangeEnd, int low, int high) {

		int latest = -1;
		while (low <= high) {
			int mid = (low + high) >>> 1;
		int midVal = hits.get(mid).doc;

		if (midVal < rangeStart) {
			low = mid + 1;
			if (midVal >= rangeStart && midVal <= rangeEnd)
				latest = mid;
		} else if (midVal > rangeStart) {
			high = mid - 1;
			if (midVal >= rangeStart && midVal <= rangeEnd)
				latest = mid;
		} else {
			return mid; // key found
		}
		}
		//if (low > 0 && low < hits.size() && hits.get(low).doc >= rangeStart && hits.get(low).doc <= rangeEnd)
		//	return low;
		if (latest > -1) return latest;

		return -1;  // key not found
	}

	// a very naive implementation (TODO: search faster)
	private Integer[] findRange(int startDoc, Integer lastDoc) {
		int low = 0;
		int high = hits.size();
		Integer[] out = new Integer[]{0,0};

		if (lastPos  != null) {
			if (lastPos+1 <= hits.size()) {
				return out;
			}
			ScoreDoc previous = hits.get(lastPos);
			ScoreDoc next = hits.get(lastPos+1);
			if (previous.doc < startDoc && next.doc >= startDoc) {
				low = lastPos+1;
			}
		}

		int i=low;
		while (i<high) {
			int d = hits.get(i).doc;
			if (d >= startDoc && d<=lastDoc) {
				i++;
			}
			else {
				break;
			}
		}

		lastPos = i;

		out[0] = low;
		out[1] = lastPos;

		return out;

	}

	private int findClosestInclusive(int low, int high, int valToSearch) {
		int i;
		for (i=low;i<high;i++) {
			int d = hits.get(i).doc;
			if (d == valToSearch) {
				return i;
			}
			else if (d > valToSearch) {
				return i-1;
			}
		}
		return -1;
	}

	private int binarySearch(int low, int high, int valToSearch) {
		while (low <= high) {
			int mid = (low + high) >>> 1;
			int midVal = hits.get(mid).doc;

			if (midVal < valToSearch)
				low = mid + 1;
			else if (midVal > valToSearch)
				high = mid - 1;
			else
				return mid; // key found
		}
		return low-1;  // key not found, get closest
	}

	protected void organizeResults() {

		sortHits();
		compactHits();

	}

	protected void compactHits() {
		ArrayList<CollectorDoc> newHits = new ArrayList<CollectorDoc>(new Float(
				(hits.size() * 0.75f)).intValue());

		if (hits.size() < 1)
			return;

		CollectorDoc currDoc = hits.get(0);
		int seenTimes = 0;
		float score = 1.0f;

		for (CollectorDoc d : hits) {
			//System.out.println("seen=" + seenTimes + " score=" + score + " currDoc " + currDoc.toString() + " d " + d.toString());
			if (d.doc == currDoc.doc) {
				score *= d.score;
				seenTimes += 1;
				continue;
			}
			// compute geometric mean (not arithmetic, as that is not good for comparison
			// of normalized values

			if (seenTimes > 1)
				currDoc.score = (float) Math.pow(score, 1.0f/seenTimes);
			//System.out.println("adding " + currDoc.toString());
			newHits.add(currDoc);
			currDoc = d;
			seenTimes = 1;
			score = currDoc.score;
		}

		if (seenTimes > 0) {
			currDoc.score = (float) Math.pow(score, 1.0f/seenTimes);
		}
		newHits.add(currDoc);

		hits = newHits;
	}

	protected void sortHits() {

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

	public void reset() {
		hits.clear();
		organized = false;
	}

	protected String fieldsToStr(String[] fields) {
		StringBuilder out = new StringBuilder();
		boolean first = false;
		for (String f: fields) {
			if (first) {
				out.append(",");
			}
			first = true;
			out.append(f);
		}
		return out.toString();
	}

}
