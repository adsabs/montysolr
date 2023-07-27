package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpOrQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.BooleanQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SynonymQuery;
import org.apache.lucene.search.TermQuery;

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

                List<QueryNode> children = queryNode.getChildren();
                ArrayList<Query> disjuncts = new ArrayList<Query>(children.size());
                boolean simpleCase = true;

                if (children != null) {
                    for (QueryNode child : children) {
                        Object obj = child.getTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);

                        if (obj != null) {
                            Query query = (Query) obj;
                            disjuncts.add(query);
                            if (!(query instanceof TermQuery))
                                simpleCase = false;
                        }
                    }
                }

                if (simpleCase) {
                    Term[] terms = new Term[disjuncts.size()];
                    int i = 0;
                    for (Query qu : disjuncts) {
                        terms[i] = ((TermQuery) qu).getTerm();
                        i++;
                    }
                    return new SynonymQuery(terms);
                } else {
                    return new DisjunctionMaxQuery(disjuncts, 0.0f);
                }

            }

        }
        return booleanBuilder.build(queryNode);
    }
}
