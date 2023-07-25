package org.apache.lucene.queryparser.flexible.aqp.builders;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpDisjunctionQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Query;



public class AqpDisjunctQueryNodeBuilder implements StandardQueryBuilder {
	

  public AqpDisjunctQueryNodeBuilder() {
    //empty constructor
  }

	public Query build(QueryNode queryNode) throws QueryNodeException {
	  
	  List<Query> disjuncts = new ArrayList<>();
	  for (QueryNode n: queryNode.getChildren()) {
	    Query q = (Query) n.getTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);
	    if (q != null) {
	      disjuncts.add(q);
	    }
	  }
	  
    float tieBreaker = ((AqpDisjunctionQueryNode) queryNode).getTieBreaker();

    return new DisjunctionMaxQuery(disjuncts, tieBreaker);

	  }

}
