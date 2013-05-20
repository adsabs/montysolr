package org.apache.lucene.queryparser.flexible.aqp.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    List<QueryNode> children = nearNode.getChildren();

    if (children != null) {
      SpanQuery[] clauses = new SpanQuery[children.size()];

      int i = 0;
      for (QueryNode child : children) {
        Object obj = child.getTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);
        if (obj != null) {
          clauses[i++] = getSpanQuery(obj, nearNode);
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

  protected SpanQuery getSpanQuery(Object obj, AqpNearQueryNode nearNode)
      throws QueryNodeException {
    Query q = (Query) obj;
    if (q instanceof SpanQuery) {
      return (SpanQuery) q;
    } else if (q instanceof TermQuery) {
      return new SpanTermQuery(((TermQuery) q).getTerm());
    } else if (q instanceof WildcardQuery) {
      return new SpanMultiTermQueryWrapper<WildcardQuery>((WildcardQuery) q);
    } else if (q instanceof PrefixQuery) {
      return new SpanMultiTermQueryWrapper<PrefixQuery>((PrefixQuery) q);
    } else if (q instanceof BooleanQuery) {
      return convertBooleanToSpan((BooleanQuery) q, nearNode);
    } else {
      throw new QueryNodeException(new MessageImpl(
          QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR, q.toString(),
          "(yet) Unsupported clause inside span query: "
              + q.getClass().getName()));
    }
  }

  /*
   * Silly convertor for now it can handle only boolean queries of the same type
   * (ie not mixed cases). To do that, I have to build a graph (tree) and maybe
   * of only pairs (?)
   */
  protected SpanQuery convertBooleanToSpan(BooleanQuery q,
      AqpNearQueryNode nearNode) throws QueryNodeException {
    BooleanClause[] clauses = q.getClauses();
    SpanQuery[] spanClauses = new SpanQuery[clauses.length];
    Occur o = null;
    int i = 0;
    for (BooleanClause c : clauses) {
      if (o != null && !o.equals(c.getOccur())) {
        throw new QueryNodeException(new MessageImpl(
            QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR, q.toString(),
            "(yet) Unsupported clause inside span query: "
                + q.getClass().getName()));
      }
      o = c.getOccur();
      spanClauses[i] = getSpanQuery(c.getQuery(), nearNode);
      i++;
    }

    if (o.equals(Occur.MUST)) {
      return new SpanNearQuery(spanClauses, nearNode.getSlop(),
          nearNode.getInOrder());
    } else if (o.equals(Occur.SHOULD)) {
      return new SpanOrQuery(spanClauses);
    } else if (o.equals(Occur.MUST_NOT)) {
      SpanQuery[] exclude = new SpanQuery[spanClauses.length - 1];
      for (int j = 1; j < spanClauses.length; j++) {
        exclude[j - 1] = spanClauses[j];
      }
      return new SpanNotQuery(spanClauses[0], new SpanOrQuery(exclude));
    }

    throw new QueryNodeException(new MessageImpl(
        QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR, q.toString(),
        "Congratulations! You have hit (yet) unsupported case: "
            + q.getClass().getName()));
  }

  class Leaf {
    public List<BooleanClause> members = new ArrayList<BooleanClause>();
    public BooleanClause left;
    public Leaf right;

    public Leaf(BooleanClause left, Leaf right) {
      this.left = left;
      this.right = right;
    }
  }

  /*
   * Creates a tree of the clauses, according to operator precedence:
   * 
   * Thus: D +C -A -B becomes:
   * 
   * - / \ A - / \ B + / \ C D
   */
  private Leaf constructTree(BooleanClause[] clauses) {
    List<BooleanClause> toProcess = Arrays.asList(clauses);
    Leaf leaf = new Leaf(null, null);
    leaf.members = toProcess;

    // from highest priority
    // findNots(leaf);
    // findAnds(leaf);
    // findOrs(leaf);
    return leaf;
  }

  private void findNots(Leaf leaf) {

    for (BooleanClause m : leaf.members) {
      if (m.getOccur().equals(Occur.MUST_NOT)) {
        leaf.members.remove(m);
        leaf.left = m;
      }
    }

  }

}
