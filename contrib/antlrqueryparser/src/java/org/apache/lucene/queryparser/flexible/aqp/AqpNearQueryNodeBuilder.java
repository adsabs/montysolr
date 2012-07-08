package org.apache.lucene.queryparser.flexible.aqp;

import java.util.List;

import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNearQueryNode;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
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
		} else {
			throw new QueryNodeException(new MessageImpl(
					QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
					"(yet) Unsupported clause inside span query: "
							+ q.toString()));
		}
	}

}
