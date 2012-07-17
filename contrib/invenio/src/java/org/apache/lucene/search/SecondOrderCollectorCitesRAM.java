package org.apache.lucene.search;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;

public class SecondOrderCollectorCitesRAM extends AbstractSecondOrderCollector {

	protected Map<Integer, List<Integer>> docToDocidsCache;
	protected String referenceField;
	protected String uniqueIdField;
  private AtomicReaderContext context;
  private AtomicReader reader;
	
	public SecondOrderCollectorCitesRAM(Map<Integer, List<Integer>> cache, String uniqueIdField, String referenceField) {
		super();
		docToDocidsCache = cache;
		this.uniqueIdField = uniqueIdField;
		this.referenceField = referenceField;
		docBase = 0;
	}
	
	public SecondOrderCollectorCitesRAM(String uniqueIdField, final String referenceField) {
		super();
		docToDocidsCache = null;
		this.uniqueIdField = uniqueIdField;
		this.referenceField = referenceField;
		docBase = 0;
	}
	
	
	
	@Override
	public void searcherInitialization(IndexSearcher searcher) throws IOException {
		if (docToDocidsCache == null) {
			docToDocidsCache = DictionaryRecIdCache.INSTANCE.
				getCacheTranslatedMultiValuesString(((IndexSearcher) searcher).getIndexReader(), 
				uniqueIdField, referenceField);
			
		}
	}
	

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;

	}

	@Override
	public void collect(int doc) throws IOException {
		if (docToDocidsCache.containsKey(doc+docBase)) {
			float s = scorer.score();
			for (int i: docToDocidsCache.get(doc+docBase)) {
				hits.add(new ScoreDoc(i, s));
			}
		}
		
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
		return true;
	}
	
	
	@Override
	public String toString() {
		return "cites[using_cache:" + referenceField + "]";
	}
	
	/** Returns a hash code value for this object. */
	public int hashCode() {
		return referenceField.hashCode() ^ (docToDocidsCache!=null ? docToDocidsCache.hashCode() : 0);
	}
	
	
	
}
