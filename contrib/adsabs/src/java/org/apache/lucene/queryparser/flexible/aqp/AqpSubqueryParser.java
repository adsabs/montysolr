package org.apache.lucene.queryparser.flexible.aqp;

import org.apache.commons.lang.NotImplementedException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.solr.search.FunctionQParser;

public class AqpSubqueryParser {

  private boolean canBeAnalyzed = false;

  public boolean canBeAnalyzed() {
    return canBeAnalyzed ;
  }
  
	public Query parse(FunctionQParser fp) throws ParseException {
		throw new NotImplementedException();
	}
	
	public AqpSubqueryParser configure(boolean canBeAnalyzed) {
	  this.canBeAnalyzed=canBeAnalyzed;
	  return this;
	}

}
