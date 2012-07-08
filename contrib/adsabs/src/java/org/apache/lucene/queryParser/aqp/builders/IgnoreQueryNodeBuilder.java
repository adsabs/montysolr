package org.apache.lucene.queryParser.aqp.builders;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.Query;

public class IgnoreQueryNodeBuilder implements StandardQueryBuilder {

	public Query build(QueryNode queryNode) throws QueryNodeException {
		return null;
	}

}
