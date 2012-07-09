package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.aqp.nodes.SlowFuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.sandbox.queries.SlowFuzzyQuery;

@SuppressWarnings("deprecation")
public class AqpSlowFuzzyQueryNodeBuilder implements StandardQueryBuilder {

	public AqpSlowFuzzyQueryNodeBuilder() {
		// empty constructor
	}

	public SlowFuzzyQuery build(QueryNode queryNode) throws QueryNodeException {
		SlowFuzzyQueryNode fuzzyNode = (SlowFuzzyQueryNode) queryNode;

		return new SlowFuzzyQuery(new Term(fuzzyNode.getFieldAsString(),
				fuzzyNode.getTextAsString()), fuzzyNode.getSimilarity(),
				fuzzyNode.getPrefixLength());

	}

}
