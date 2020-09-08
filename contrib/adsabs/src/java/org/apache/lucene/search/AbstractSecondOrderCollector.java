package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.index.LeafReaderContext;


public abstract class AbstractSecondOrderCollector implements Collector, LeafCollector, SecondOrderCollector {

	
	
	protected Scorer scorer;
	protected int docBase;
	protected List<CollectorDoc> hits;
	protected volatile boolean organized = false;
	protected Lock lock = null;
	private Integer lastPos = null;
	protected float ensureCapacityRatio = 0.25f;
	protected FinalValueType compactingType = FinalValueType.MAX_VALUE;
  protected LeafReaderContext context;
  private float maxScore = 0.0f;

	public AbstractSecondOrderCollector() {
		lock = new ReentrantLock();
		hits = new ArrayList<CollectorDoc>();
	}
	
	/** This method is called before collecting <code>context</code>. */
  protected void doSetNextReader(LeafReaderContext context) throws IOException {
    this.context = context;
    this.docBase = context.docBase;
  }

  @Override
  public void setScorer(Scorer scorer) throws IOException {
    this.scorer = scorer;
  }
  
  @Override
  public LeafCollector getLeafCollector(LeafReaderContext context) throws IOException {
    this.doSetNextReader(context);
    return this;
  }
  
  
	public boolean searcherInitialization(IndexSearcher searcher, Weight firstOrderWeight) throws IOException {
		// this is pretty arbitrary, but 2nd order queries may return many hits...
		((ArrayList<CollectorDoc>) hits).ensureCapacity((int) (searcher.getIndexReader().maxDoc() * ensureCapacityRatio));
		return true;
	}
	
	

	public List<CollectorDoc> getSubReaderResults(int rangeStart, int rangeEnd) {

		if (hits.size() == 0)
			return null;

		int i = -1;
		//TODO: prevent locking, test for !orgnized
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
	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
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
	
	
	public void setFinalValueType(FinalValueType type) {
		compactingType = type;
	}
	
	protected void compactHits() {
		switch (compactingType) {
			case GEOM_MEAN_NORM:
				compactHitsGeomMean();
				normalizeScores();
				break;
			case ARITHM_MEAN_NORM:
			  compactHitsArithmMean();
			  normalizeScores();
				break;
			case GEOM_MEAN:
				compactHitsGeomMean();
				break;
			case ARITHM_MEAN:
				compactHitsArithmMean();
				break;
			case ABS_COUNT:
				compactHitsAbsCount();
				break;
			case ABS_COUNT_NORM:
				compactHitsAbsCount();
				normalizeScores();
				break;
			case MAX_VALUE:
				compactHitsMaxValue();
				break;
			case MIN_VALUE:
				compactHitsMinValue();
				break;
			case AGRESTI_COULL:
				compactHitsArithmMean();
				normalizeScores();
				applyAgrestiCoull();
				break;
			default:
				compactHitsMaxValue();
				break;
		}
	}
	
	

	protected void applyAgrestiCoull() {
		if (hits.size() < 1)
			return;
		// add 2 upvotes and 2 downvotes to each result
		// plus penalize the ones that have only few
		// hits - we are 'misusing' shardIndex for counting
		// frequency since lucene 7.x removed freq fields
		// (but second order queries can only run in 
		// non-sharded environment, so it's dirty but innocent ;-))
		for (CollectorDoc d: hits) {
			d.score = ((d.score + 2.001f) / (d.shardIndex + 4)) - (1.0f/(d.shardIndex+0.001f));
		}
  }

	protected void normalizeScores() {
		if (hits.size() < 1)
			return;
		// find the max value
		float maxV = 1.0f;
		if (this.maxScore > 0.0f) {
		  maxV = maxScore;
		}
		else {
		  maxV = hits.get(0).score;
		  for (CollectorDoc d: hits) {
		    if (d.score > maxV)
		      maxV = d.score;
		  }		  
		}
		if (maxV == 1.0f)
		  return;
		// normalize the scores
		for (CollectorDoc d: hits) {
			d.score = d.score / maxV;
		}
	}
	
	protected void compactHitsMinValue() {
		ArrayList<CollectorDoc> newHits = new ArrayList<CollectorDoc>(new Float(
				(hits.size() * 0.75f)).intValue());

		if (hits.size() < 1)
			return;
		
		CollectorDoc currDoc = hits.get(0);
		
		int seenTimes = 1;
		for (CollectorDoc d : hits) {
			if (d.doc == currDoc.doc) {
				if (d.score < currDoc.score) {
					currDoc.score = d.score;
				}
				currDoc.shardIndex = seenTimes++;
				continue;
			}
			newHits.add(currDoc);
			currDoc = d;
			seenTimes = 1;
		}
		
		if (newHits.size() == 0 || newHits.get(newHits.size()-1).doc != currDoc.doc) {
			newHits.add(currDoc);
		}
		hits = newHits;
	}
	
	protected void compactHitsMaxValue() {
		ArrayList<CollectorDoc> newHits = new ArrayList<CollectorDoc>(new Float(
				(hits.size() * 0.75f)).intValue());

		if (hits.size() < 1)
			return;
		
		CollectorDoc currDoc = hits.get(0);
		
		int seenTimes = 1;
		for (CollectorDoc d : hits) {
			if (d.doc == currDoc.doc) {
				if (d.score > currDoc.score) {
					currDoc.score = d.score;
				}
				currDoc.shardIndex = seenTimes++;
				continue;
			}
			newHits.add(currDoc);
			currDoc = d;
			seenTimes = 1;
		}
		
		if (newHits.size() == 0 || newHits.get(newHits.size()-1).doc != currDoc.doc) {
			newHits.add(currDoc);
		}
		hits = newHits;
	}
	
	protected void compactHitsAbsCount() {
		ArrayList<CollectorDoc> newHits = new ArrayList<CollectorDoc>(new Float(
				(hits.size() * 0.75f)).intValue());

		if (hits.size() < 1)
			return;
		
		CollectorDoc currDoc = hits.get(0);
		int seenTimes = 0;

		for (CollectorDoc d : hits) {
			if (d.doc == currDoc.doc) {
				seenTimes += 1.0f;
				continue;
			}
			currDoc.shardIndex = seenTimes;
			currDoc.score = seenTimes;
			newHits.add(currDoc);
			currDoc = d;
			seenTimes = 1;
		}
		
		if (newHits.size() == 0 || newHits.get(newHits.size()-1).doc != currDoc.doc) {
			currDoc.score = seenTimes;
			currDoc.shardIndex = seenTimes;
			newHits.add(currDoc);
		}
		hits = newHits;
	}
	
	protected void compactHitsArithmMean() {
		ArrayList<CollectorDoc> newHits = new ArrayList<CollectorDoc>(new Float(
				(hits.size() * 0.75f)).intValue());

		if (hits.size() < 1)
			return;
		
		CollectorDoc currDoc = hits.get(0);
		int seenTimes = 0;
		float score = 0.0f;

		for (CollectorDoc d : hits) {
			if (d.doc == currDoc.doc) {
				score += d.score;
				seenTimes += 1;
				continue;
			}
			if (seenTimes > 1) {
				currDoc.score = (float) score/seenTimes;
				currDoc.shardIndex = seenTimes;
			}
			newHits.add(currDoc);
			currDoc = d;
			seenTimes = 1;
			score = currDoc.score;
		}

		if (newHits.size() == 0 || newHits.get(newHits.size()-1).doc != currDoc.doc) {
			currDoc.score = (float) score/ (float) seenTimes;
			currDoc.shardIndex = seenTimes;
		}
		if (currDoc.score > maxScore )
		  maxScore = currDoc.score;
		
		newHits.add(currDoc);
		hits = newHits;
	}
	
	protected void compactHitsGeomMean() {
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
				seenTimes += 1f;
				continue;
			}
			// compute geometric mean (not arithmetic, as that is not good for comparison
			// of normalized values

			if (seenTimes > 1) {
				currDoc.score = (float) Math.pow(score, 1.0f/seenTimes);
				currDoc.shardIndex = seenTimes;
			}
			//System.out.println("adding " + currDoc.toString());
			newHits.add(currDoc);
			currDoc = d;
			seenTimes = 1;
			score = currDoc.score;
		}

		if (newHits.size() == 0 || newHits.get(newHits.size()-1).doc != currDoc.doc) {
			currDoc.score = (float) Math.pow(score, 1.0f/seenTimes);
			currDoc.shardIndex = seenTimes;
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
