package org.apache.lucene.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queries.mlt.MoreLikeThisQuery;


/**
 * Just a fix for MLTQ - the original is not setting hashCode 
 * properly which means wrong cache hits
 *
 */

public class MoreLikeThisQueryFixed extends MoreLikeThisQuery {

	public MoreLikeThisQueryFixed(String likeText, String[] moreLikeFields,
      Analyzer analyzer, String fieldName) {
	  super(likeText, moreLikeFields, analyzer, fieldName);
  }
	
	@Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Float.floatToIntBits(getBoost()) + getLikeText().hashCode();
    return result;
  }
}
