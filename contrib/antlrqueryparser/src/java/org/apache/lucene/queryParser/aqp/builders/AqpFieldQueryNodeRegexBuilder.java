package org.apache.lucene.queryParser.aqp.builders;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.standard.builders.StandardQueryBuilder;
<<<<<<< HEAD
import org.apache.lucene.search.Query;
import org.apache.lucene.search.regex.RegexQuery;
=======
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
>>>>>>> d3589f4... initial commit with new processor and builder classes

public class AqpFieldQueryNodeRegexBuilder implements StandardQueryBuilder {
	
	public AqpFieldQueryNodeRegexBuilder() {
		// empty constructor
	}

	public Query build(QueryNode queryNode) throws QueryNodeException {
	    FieldQueryNode fieldNode = (FieldQueryNode) queryNode;
	    
<<<<<<< HEAD
	    return new RegexQuery(new Term(fieldNode.getFieldAsString(), fieldNode
=======
	    if (fieldNode.getFieldAsString().equals("*") && fieldNode.getTextAsString().equals("*")) {
	    	return new MatchAllDocsQuery();
	    }
        
	    return new TermQuery(new Term(fieldNode.getFieldAsString(), fieldNode
>>>>>>> d3589f4... initial commit with new processor and builder classes
	        .getTextAsString()));

	  }

}
