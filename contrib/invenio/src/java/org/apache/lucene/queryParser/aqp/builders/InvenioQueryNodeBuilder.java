package org.apache.lucene.queryParser.aqp.builders;

import org.apache.lucene.queryParser.aqp.nodes.InvenioQueryNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.builders.QueryBuilder;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.InvenioQuery;
import org.apache.lucene.search.Query;

public class InvenioQueryNodeBuilder implements StandardQueryBuilder {
	
	private QueryBuilder master;
	
	public InvenioQueryNodeBuilder(QueryBuilder parentBuilder) {
		master = parentBuilder;
	}
	
	@Override
	public Query build(QueryNode queryNode) throws QueryNodeException {
		QueryNode q = ((InvenioQueryNode) queryNode).getChild();
		String idField = ((InvenioQueryNode) queryNode).getIdField();
		
		return new InvenioQuery((Query)master.build(q), "idField");
		

	  }

}
