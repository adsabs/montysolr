package org.apache.lucene.queryParser.aqp.builders;

import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.builders.QueryBuilder;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.solr.search.function.FunctionQuery;
import org.apache.solr.search.function.PositionSearchFunction;

public class AqpFunctionQueryNodeBuilder implements QueryBuilder {

	public Object build(QueryNode queryNode) throws QueryNodeException {
		AqpFunctionQueryNode node = (AqpFunctionQueryNode) queryNode;
		
		String funcName = node.getName();
		
		// XXX: tired now, will do better later
		if (funcName.equals("pos")) {
			List<String> rawInput = node.getRawData();
			PositionSearchFunction ps = new PositionSearchFunction(rawInput.get(0), 
					rawInput.get(1), 
					Integer.valueOf(rawInput.get(2)), 
					Integer.valueOf(rawInput.get(3)));
			return new FunctionQuery(ps);
		}
		
		return null;
	}

}
