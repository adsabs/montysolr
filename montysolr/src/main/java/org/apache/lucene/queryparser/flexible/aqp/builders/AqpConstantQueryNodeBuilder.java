package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.Query;

import java.util.List;

public class AqpConstantQueryNodeBuilder implements StandardQueryBuilder {

    private final QueryBuilder root;

    public AqpConstantQueryNodeBuilder(QueryBuilder root) {
        this.root = root;
    }

    public Query build(QueryNode queryNode) throws QueryNodeException {
        List<QueryNode> children = queryNode.getChildren();
        if (children.size() != 1)
            throw new QueryNodeException(new MessageImpl("The query node must have only one child: " + queryNode));

        QueryNode child = children.get(0);
        Query q = (Query) root.build(child);

        return new ConstantScoreQuery(q);

    }

}
