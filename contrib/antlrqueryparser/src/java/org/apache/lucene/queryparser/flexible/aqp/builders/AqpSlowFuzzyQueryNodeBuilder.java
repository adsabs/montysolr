package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.aqp.nodes.SlowFuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.FuzzyQuery;

@SuppressWarnings("deprecation")
public class AqpSlowFuzzyQueryNodeBuilder implements StandardQueryBuilder {

  public AqpSlowFuzzyQueryNodeBuilder() {
    // empty constructor
  }

  public FuzzyQuery build(QueryNode queryNode) throws QueryNodeException {
    SlowFuzzyQueryNode fuzzyNode = (SlowFuzzyQueryNode) queryNode;

    // TODO:rca -- SlowFuzzyQuery was removed in LUCENE-7439
    // may have to re-add?
    int editDistance = (int) (fuzzyNode.getTextAsString().length() * fuzzyNode.getSimilarity());
    return new FuzzyQuery(new Term(fuzzyNode.getFieldAsString(),
        fuzzyNode.getTextAsString()), editDistance,
        fuzzyNode.getPrefixLength());

  }

}
