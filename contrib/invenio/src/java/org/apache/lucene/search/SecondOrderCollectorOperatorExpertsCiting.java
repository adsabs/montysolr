package org.apache.lucene.search;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;

/**
 * Finds papers that are cited by our search. And then adjusts the score so that
 * papers with most value from the cited_read_boost field are up 
 *
 */
public class SecondOrderCollectorOperatorExpertsCiting extends AbstractSecondOrderCollector {

  Set<String> fieldsToLoad;
	protected String referenceField;
	protected String[] uniqueIdField;
	protected String boostField;
	private IndexReader reader;
	private CacheWrapper cache;
	
	
	public SecondOrderCollectorOperatorExpertsCiting(CacheWrapper cache, String referenceField, String boostField) {
		super();
		
		assert cache != null;
		this.cache = cache;
		
		this.referenceField = referenceField;
		this.boostField = boostField;
		fieldsToLoad = new HashSet<String>();
    fieldsToLoad.add(referenceField);
    fieldsToLoad.add(boostField);
	}
	
	

	@Override
	public void collect(int doc) throws IOException {
		
		Document document = reader.document(doc, fieldsToLoad);
		
		float s = scorer.score();
		
		// naive implementation (probably slow)
		IndexableField bf = document.getField(boostField);
		//if (bf==null) throw new IOException("Every document must have field: " + boostField);
		
		// TODO: we must find the proper values for this, that means to compute the statistics
		// (find the local minimas, maximas) for this function; this is just guessing....
		
		
    if (bf != null) {
      s = s + (s * bf.numericValue().floatValue());
    }
    else {
      // penalize docs without boost
      s = s * 0.8f;
    }
		
    // find documents that are cited by our doc (references)
		String[] vals = document.getValues(referenceField);
		//s = s / (vals.length + 100); // this would contribute only a part of the score to each citation
		for (String v: vals) {
			v = v.toLowerCase();
			int docid = cache.getLuceneDocId(doc, v);
			if (docid > 0) {
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
		return "expertcites[using:" + referenceField + ",weightedBy:" + boostField + "," + cache.toString() + "]";
	}
	
	/** Returns a hash code value for this object. */
	public int hashCode() {
		return 435878 ^ referenceField.hashCode() ^ boostField.hashCode() ^ cache.hashCode();
	}
	
	
	
}
