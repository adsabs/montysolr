package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

public class AqpAdslabsIdentifierNodeBuilder implements StandardQueryBuilder {
	
	public AqpAdslabsIdentifierNodeBuilder() {
		// empty constructor
	}

	public Query build(QueryNode queryNode) throws QueryNodeException {
	    FieldQueryNode fieldNode = (FieldQueryNode) queryNode;
	    
        
	    return new TermQuery(new Term(fieldNode.getFieldAsString(), 
	    		fieldNode.getTextAsString().toLowerCase()));

	  }

}
