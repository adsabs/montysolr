package org.apache.lucene.search;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;

/**
 * This is a toy example for testing the ADS Classic relevance
 * computation. I know it is most likely inefficient and most
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

	private String boostField;
	private float highestLuceneScore;
	private float lucenePart;
	private float adsPart;
	private CacheWrapper cache;
	private LuceneCacheWrapper<NumericDocValues> boostCache;
	private float highestClassicFactor;

	public SecondOrderCollectorAdsClassicScoringFormula(CacheWrapper cache, LuceneCacheWrapper<NumericDocValues> boostCache, float ratio) {
		this.cache = cache;
		this.lucenePart = ratio;
		this.adsPart = 1.0f - ratio;
		this.boostCache = boostCache;
	}
	
	public SecondOrderCollectorAdsClassicScoringFormula(CacheWrapper cache, LuceneCacheWrapper<NumericDocValues> boostCache) {
		this.cache = cache;
		this.lucenePart = 0.5f;
		this.adsPart = 0.5f;
		this.boostCache = boostCache;
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
			
//			float cf = getClassicBoostFactor(doc+docBase);
//			if (cf > highestClassicFactor)
//			  highestClassicFactor = cf;
				
			
			hits.add(new CollectorDoc(doc+docBase, s, -1));

	}

	@Override
	public void doSetNextReader(LeafReaderContext context)
			throws IOException {
		this.docBase = context.docBase;

	}

	
	@Override
	public List<CollectorDoc> getSubReaderResults(int rangeStart, int rangeEnd) {
		
		if (hits.size() == 0)
			return null;
		
		lock.lock();
		try {
			if (!organized) {
				for (CollectorDoc hit: hits) {
					hit.score = (this.lucenePart * hit.score / highestLuceneScore) + (this.adsPart * getClassicBoostFactor(hit.doc));
				}
			}
		}
		finally {
			lock.unlock();
		}
		
		return super.getSubReaderResults(rangeStart, rangeEnd);
		
	  }
		
		private float getClassicBoostFactor(int doc) {
		  return boostCache.getFloat(doc);
	  }
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(cache=" + cache.toString() + ", boost=" + boostCache.toString() 
		+ ", lucene=" + this.lucenePart + ", adsPart=" + this.adsPart + ")";
	}

    @Override
	public boolean needsScores() {
    	return true;
	}

	@Override
	public SecondOrderCollector copy() {	
		return new SecondOrderCollectorAdsClassicScoringFormula(cache, boostCache, lucenePart);
	}

}
