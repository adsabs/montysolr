package org.apache.lucene.queryParser.aqp.builders;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.regex.RegexQuery;

public class AqpFieldQueryNodeRegexBuilder implements StandardQueryBuilder {
	
	public AqpFieldQueryNodeRegexBuilder() {
		// empty constructor
	}

	public Query build(QueryNode queryNode) throws QueryNodeException {
	    FieldQueryNode fieldNode = (FieldQueryNode) queryNode;
	    
	    return new RegexQuery(new Term(fieldNode.getFieldAsString(), fieldNode
	        .getTextAsString()));

	  }

}
