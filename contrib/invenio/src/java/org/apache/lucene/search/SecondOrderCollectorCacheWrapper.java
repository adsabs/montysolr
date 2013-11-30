package org.apache.lucene.search;

import org.apache.commons.lang.NotImplementedException;
import org.apache.lucene.index.AtomicReader;

public class SecondOrderCollectorCacheWrapper implements CacheWrapper {


	@Override
  public int getLuceneDocId(int sourceDocid) {
		throw new NotImplementedException();
  }

	@Override
  public int[] getLuceneDocIds(int sourceDocid) {
		throw new NotImplementedException();
  }


	@Override
  public AtomicReader getAtomicReader() {
	  throw new NotImplementedException();
  }

	@Override
  public void collectorInitialized(IndexSearcher searcher,
      Weight firstOrderWeight) {
		throw new NotImplementedException();
  }

	@Override
  public int getLuceneDocId(int sourceDocid, Object sourceValue) {
		throw new NotImplementedException();
  }

	@Override
  public int[] getLuceneDocIds(int sourceDocid, Object sourceValue) {
		throw new NotImplementedException();
  }



}
