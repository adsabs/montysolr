package org.apache.lucene.queryparser.flexible.aqp.builders;


import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.queries.function.FunctionQuery;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.valuesource.ConstValueSource;
import org.apache.lucene.queries.function.valuesource.FloatFieldSource;
import org.apache.lucene.queries.function.valuesource.SumFloatFunction;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsScoringQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.Query;

/**
 * @author rchyla
 *
 * Will produce a CustomScoreQuery which combines scores computed by
 * lucene with the value indexed in an index, using formula:
 * 
 *  score = lucene_score * ( classic_factor + modifier ) 
 */
public class AqpScoringQueryNodeBuilder implements StandardQueryBuilder {


	public Query build(QueryNode queryNode) throws QueryNodeException {

		AqpAdsabsScoringQueryNode q = (AqpAdsabsScoringQueryNode) queryNode;

		Query query = (Query) queryNode.getChildren().get(0).getTag(
				QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);

		return wrapQuery(query, q.getSource(), q.getModifier());

	}

	public static Query wrapQuery(Query q, String source, float modifier) {
		ValueSource vs = new SumFloatFunction(new ValueSource[] {
				new FloatFieldSource(source), // classic score 
				new ConstValueSource(modifier) // modifier of how much lucene score to use
		});


		FunctionQuery functionQuery = new FunctionQuery(vs);
		return new CustomScoreQuery(q, functionQuery);
	}

}
