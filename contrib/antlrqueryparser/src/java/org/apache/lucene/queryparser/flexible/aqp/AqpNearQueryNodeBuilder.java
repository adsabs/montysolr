package org.apache.lucene.queryparser.flexible.aqp;

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
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;

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
				Object obj = child
						.getTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);
				if (obj != null) {
					clauses[i++] = getSpanQuery(obj);
				} else {
					throw new QueryNodeException(
							new MessageImpl(
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

	protected SpanQuery getSpanQuery(Object obj) throws QueryNodeException {
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
      return convertBooleanToSpan((BooleanQuery) q);
		} else {
			throw new QueryNodeException(new MessageImpl(
					QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
					q.toString(),
					"(yet) Unsupported clause inside span query: " + q.getClass().getName()));
		}
	}
	
	protected SpanQuery convertBooleanToSpan(BooleanQuery q) {
	  BooleanClause[] clauses = q.getClauses();
	  for (BooleanClause c: clauses) {
	    Occur o = c.getOccur();
	    if (o.equals(Occur.MUST)) {
	      
	    }
	  }
	  return null;
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
	 * Creates a tree of the clauses, according to 
	 * operator precedence:
	 * 
	 *  Thus: D +C -A -B becomes:
	 *    
	 *         -
	 *       /   \
	 *      A     -
	 *          /   \
	 *         B     +
	 *             /   \
	 *            C     D 
	 */ 
	private Leaf constructTree(BooleanClause[] clauses) {
	  List<BooleanClause> toProcess = Arrays.asList(clauses);
	  Leaf leaf = new Leaf(null, null);
	  leaf.members = toProcess;
	  
	  // from highest priority
	  //findNots(leaf);
	  //findAnds(leaf);
	  //findOrs(leaf);
	  return leaf;
	}

  private void findNots(Leaf leaf) {
    
    for (BooleanClause m: leaf.members) {
      if (m.getOccur().equals(Occur.MUST_NOT)) {
        leaf.members.remove(m);
        leaf.left = m;
      }
    }
    
  }
	
	

}
