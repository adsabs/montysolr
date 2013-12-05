package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.queryparser.flexible.aqp.nodes.PythonQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.PythonQuery;
import org.apache.lucene.search.PythonQueryBitSet;
import org.apache.lucene.search.Query;


/**
 * This builder wraps the query inside {@link PythonQuery} or 
 * {@link PythonQueryBitSet} 
 * 
 * The query is built by the {@link QueryBuilder} - which is a reference
 * to the builder which called {@link PythonQueryNodeBuilder}
 * 
 * 
 */
public class PythonQueryNodeBuilder implements StandardQueryBuilder {

	private QueryBuilder master;

	public PythonQueryNodeBuilder(QueryBuilder parentBuilder) {
		master = parentBuilder;
	}

	public Query build(QueryNode queryNode) throws QueryNodeException {

		PythonQueryNode pq = (PythonQueryNode) queryNode;
		QueryNode q = pq.getChild();

		if (pq.useIntBitSet()) {
			return new PythonQueryBitSet((Query) master.build(q), pq.getCacheWrapper(), false, pq.getPythonFunctionName());
		}
		else {
			return new PythonQuery((Query) master.build(q), pq.getCacheWrapper(), false, pq.getPythonFunctionName());
		}

	}


}
