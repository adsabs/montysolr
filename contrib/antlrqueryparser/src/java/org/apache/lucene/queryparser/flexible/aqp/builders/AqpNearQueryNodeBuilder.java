package org.apache.lucene.queryparser.flexible.aqp.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNearQueryNode;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanNotQuery;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;

/**
 *  The builder for the {@link AqpNearQueryNode}, example query:
 *  
 *  <pre>
 *   dog NEAR/5 cat
 *  </pre>
 *  
 *  <p>
 *  After the AST tree was parsed, and synonyms were found, 
 *  we may have the following tree:
 *  
 *  <p>
 *  <pre>
 *        AqpNearQueryNode(5)
 *                |
 *            ------------------------------    
 *           /                              \
 *         OR                         QueryNode(cat)
 *          |
 *       -----------------   
 *      /                 \ 
 *   QueryNode(dog)     QueryNode(canin)
 *   
 *  </pre>     
 *  
 *  
 * <p>
 * Since Lucene cannot handle these queries, the flex builder 
 * must rewrite them, effectively producing
 *
 * <pre>
 * SpanNear(SpanOr(dog | cat), SpanTerm(cat), 5)
 * </pre>
 * 
 * 
 * This builder does not know (yet) how to handle cases of
 * mixed boolean operators, eg.
 * 
 * <pre>
 * (dog AND (cat OR fat)) NEAR/5 batman
 * </pre>
 * 
 * @see QueryNodeProcessorPipeline
 * @see AqpNearQueryNode
 *
 */
public class AqpNearQueryNodeBuilder implements QueryBuilder {

  public AqpNearQueryNodeBuilder() {
    // empty constructor
  }

  public Object build(QueryNode queryNode) throws QueryNodeException {
    AqpNearQueryNode nearNode = (AqpNearQueryNode) queryNode;

    SpanConverter converter = new SpanConverter();
    List<QueryNode> children = nearNode.getChildren();

    if (children != null) {
      SpanQuery[] clauses = new SpanQuery[children.size()];

      int i = 0;
      for (QueryNode child : children) {
        Object obj = child.getTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);
        if (obj != null) {
        	SpanQuery result = converter.getSpanQuery(new SpanConverterContainer((Query) obj, nearNode.getSlop(), nearNode.getInOrder()));
          clauses[i++] = result;
          
          //TODO: v6 - boost is gone, have to move it converter
          //if (obj instanceof BoostQuery) {
          //  result = new BoostQuery(result, ((BoostQuery) obj).getBoost());
          //}
          
        } else {
          throw new QueryNodeException(new MessageImpl(
              QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
              "One of the clauses inside AqpNearQueryNode is null"));
        }
      }

      return new SpanNearQuery(clauses, nearNode.getSlop(),
          nearNode.getInOrder());
    }

    throw new QueryNodeException(new MessageImpl(
        QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
        "Illegal state for: " + nearNode.toString()));
  }

  
}
