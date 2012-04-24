package org.apache.lucene.queryParser.aqp.builders;

import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.builders.QueryBuilder;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.Query;

public class IgnoreQueryNodeBuilder implements StandardQueryBuilder {

	public Query build(QueryNode queryNode) throws QueryNodeException {
		return null;
	}

}
