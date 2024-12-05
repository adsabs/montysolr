package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpOrQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.BooleanQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.*;
import org.python.core.__builtin__;

import java.util.ArrayList;
import java.util.List;

public class AqpBooleanQueryNodeBuilder implements StandardQueryBuilder {

    private final BooleanQueryNodeBuilder booleanBuilder;

    public AqpBooleanQueryNodeBuilder() {
        booleanBuilder = new BooleanQueryNodeBuilder();
    }

    public Query build(QueryNode queryNode) throws QueryNodeException {
        if (queryNode instanceof AqpOrQueryNode) {
            Object s = queryNode.getTag(AqpQueryTreeBuilder.SYNONYMS);
            if (s != null && (Boolean) s) {

                final String[] queryField = {null};
                List<QueryNode> children = queryNode.getChildren();
                ArrayList<Query> disjuncts = new ArrayList<Query>(children.size());
                final boolean[] simpleCase = {true};

                if (children != null) {
                    for (QueryNode child : children) {
                        Object obj = child.getTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);

                        if (obj != null) {
                            Query query = (Query) obj;

                            query.visit(new QueryVisitor() {
                                @Override
                                public boolean acceptField(String field) {
                                    if (queryField[0] == null) {
                                        queryField[0] = field;
                                    } else if (!queryField[0].equals(field)) {
                                        simpleCase[0] = false;
                                    }

                                    return super.acceptField(field);
                                }
                            });

                            disjuncts.add(query);
                            if (!(query instanceof TermQuery))
                                simpleCase[0] = false;
                        }
                    }
                }

                if (simpleCase[0]) {
                    SynonymQuery.Builder builder = new SynonymQuery.Builder(queryField[0]);

                    for (Query qu : disjuncts) {
                        builder.addTerm(((TermQuery) qu).getTerm());
                    }

                    return builder.build();
                } else {
                    return new DisjunctionMaxQuery(disjuncts, 0.0f);
                }

            }

        }
        return booleanBuilder.build(queryNode);
    }
}
