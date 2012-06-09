package org.apache.lucene.search;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.OpenBitSet;

public class CitationQueryFilter extends Filter {

	private static final long serialVersionUID = -4597506988705644683L;

	/**
	 * This method returns a set of documents that are referring (citing)
	 * the set of documents we retrieved in the underlying query
	 */
	  
	  public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
	    final OpenBitSet bitSet = new OpenBitSet(reader.maxDoc());
	    for (int i=0; i < reader.maxDoc(); i++) {
	    	bitSet.set(i);
	    }
	    return bitSet;
	  }


}

