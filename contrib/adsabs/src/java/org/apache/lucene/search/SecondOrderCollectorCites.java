package org.apache.lucene.search;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;

/*
 *    references(P) - set of papers that are in the reference list of P
 *    see: http://labs.adsabs.harvard.edu/trac/ads-invenio/ticket/221
 *    
 *    This implementation reads data directly from the indexed field
 *    
 *    @see: SecondOrderCollectorCitesRAM for the implementation that
 *          uses un-inverted cache stored in RAM
 *          
 *    The CacheWrapper must provide the method that translates the 
 *    field value into lucene docid.
 *          
 *    
 */

public class SecondOrderCollectorCites extends AbstractSecondOrderCollector {

  Set<String> fieldsToLoad;
	private CacheWrapper cache;
	private IndexReader reader;
	
	public SecondOrderCollectorCites(CacheWrapper cache, String[] referenceFields) {
		super();
		assert cache != null;
		this.cache = cache;
		fieldsToLoad = new HashSet<String>();
	  for (String f: referenceFields) {
	  	fieldsToLoad.add(f);
	  }
	  assert fieldsToLoad.size() > 0;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
  @Override
	public boolean searcherInitialization(IndexSearcher searcher, Weight firstOrderWeight) throws IOException {
		reader = searcher.getIndexReader();
		return super.searcherInitialization(searcher, firstOrderWeight);
	}
	

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;

	}

	@Override
	public void collect(int doc) throws IOException {
		//if (reader.isDeleted(doc)) return;
		
		Document document = reader.document(doc, fieldsToLoad);
		
		float s = scorer.score();
		
		for (String f: fieldsToLoad) {
			String[] vals = document.getValues(f); 
			for (String v: vals) {
				v = v.toLowerCase();
				int docid = cache.getLuceneDocId(doc+docBase, v);
				if (docid == -1)
					continue;
				hits.add(new CollectorDoc(docid, s, -1, vals.length));
			}
		}
	}

	@Override
	public void setNextReader(AtomicReaderContext context)
			throws IOException {
		this.reader = context.reader();
		this.docBase = context.docBase;
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}
	
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(cache:" + cache.toString() + ", field:" + fieldsToLoad + ")";
	}
	
	/** Returns a hash code value for this object. */
	public int hashCode() {
		return 9645127 ^ fieldsToLoad.hashCode() ^ cache.hashCode();
	}
	
	
	
}
