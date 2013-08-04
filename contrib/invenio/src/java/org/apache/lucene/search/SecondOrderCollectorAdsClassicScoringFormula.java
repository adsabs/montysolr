package org.apache.lucene.search;

import java.io.IOException;
import java.util.List;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;

/**
 * This is a toy example for testing the ADS Classic relevance
 * computtation. I know it is most likely inefficient and most
 * likely it relies on a boost factor that needs more work,
 * nevertheless a test can be done...
 *
 * ADS Classic score is implemented as
 * 
 * ADS = (0.5 * norm(LS)) + (0.5 x cite_read_boost)
 * 
 * where norm(LS) = normalized score (in this case it will be a Lucene
 *                  score, normalized to be in the range 1-0, where 
 *                  1 = the first, best hit; LS/MaximumLuceneScore
 *                  
 *       cite_read_boost = score computed independently, it ranges 1-0,
 *                  and I think the formula was st like:
 *                        
 *                  crb = citations * normalized(reads)
 *
 */

public class SecondOrderCollectorAdsClassicScoringFormula extends AbstractSecondOrderCollector {

	private IndexReader reader;
	private CacheGetter cacheGetter;
	private String boostField;
	private float highestLuceneScore;
	private AtomicReaderContext context;
	private float[] boostCache;

	public SecondOrderCollectorAdsClassicScoringFormula(String boostField) {
		this.boostField = boostField;
		
	}
	
	@Override
	public boolean searcherInitialization(IndexSearcher searcher, Weight firstOrderWeight) throws IOException {
		boostCache = FieldCache.DEFAULT.getFloats(DictionaryRecIdCache.INSTANCE.getAtomicReader(searcher.getIndexReader()), 
				boostField, false);
		return super.searcherInitialization(searcher, firstOrderWeight);
	}
	
	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;

	}

	@Override
	public void collect(int doc) throws IOException {
			float s = scorer.score();
			
			// we must collect everything and then we can normalize
			// (we could be collecting only the x-first top hits,
			// but this is a toy example....)
			
			if (s > highestLuceneScore)
				highestLuceneScore = s;
				
			
			hits.add(new CollectorDoc(doc+docBase, s, -1, scorer.freq()));

	}

	@Override
	public void setNextReader(AtomicReaderContext context)
			throws IOException {
		this.context = context;
		this.reader = context.reader();
		this.docBase = context.docBase;

	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return false;
	}
	
	@Override
	public List<CollectorDoc> getSubReaderResults(int rangeStart, int rangeEnd) {

		if (hits.size() == 0)
			return null;
		
		lock.lock();
		try {
			if (!organized) {
				for (CollectorDoc hit: hits) {
					hit.score = (0.5f * hit.score / highestLuceneScore) + (0.5f * getClassicBoostFactor(hit.doc));
				}
			}
		}
		finally {
			lock.unlock();
		}
		
		return super.getSubReaderResults(rangeStart, rangeEnd);
		
  }
	
	private float getClassicBoostFactor(int doc) {
	  return boostCache[doc];
  }

	@Override
	public String toString() {
		return "adsrel[" + boostField + ", outOfOrder=" + this.acceptsDocsOutOfOrder() + "]";
	}

}
