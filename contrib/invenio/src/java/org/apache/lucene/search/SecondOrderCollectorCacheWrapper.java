package org.apache.lucene.search;

import org.apache.lucene.index.AtomicReader;

public class SecondOrderCollectorCacheWrapper implements CacheWrapper {


	@Override
  public int getLuceneDocId(int sourceDocid) {
	  return 0;
  }

	@Override
  public int[] getLuceneDocIds(int sourceDocid) {
	  return null;
  }


	@Override
  public AtomicReader getAtomicReader() {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  public void collectorInitialized(IndexSearcher searcher,
      Weight firstOrderWeight) {
	  // TODO Auto-generated method stub
	  
  }

	@Override
  public int getLuceneDocId(int sourceDocid, Object sourceValue) {
	  // TODO Auto-generated method stub
	  return 0;
  }

	@Override
  public int[] getLuceneDocIds(int sourceDocid, Object sourceValue) {
	  // TODO Auto-generated method stub
	  return null;
  }



}
