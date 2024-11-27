package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAndQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpOrQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.*;
import org.apache.lucene.queryparser.flexible.standard.builders.BooleanQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.queryparser.flexible.standard.nodes.MultiPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.search.*;
import org.python.core.__builtin__;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AqpBooleanQueryNodeBuilder implements StandardQueryBuilder {

    private final BooleanQueryNodeBuilder booleanBuilder;

    public AqpBooleanQueryNodeBuilder() {
        booleanBuilder = new BooleanQueryNodeBuilder();
    }

    public Query build(QueryNode queryNode) throws QueryNodeException {
        if (queryNode.getChildren() != null && canCoalesceSingleField(queryNode, null)) {
            return booleanFieldQueryToRegex(queryNode);
        }

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

    /**
     * Checks whether a given boolean query can be coalesced into a single regular expression query to reduce
     * total number of boolean clauses and improve query performance. Nodes will be checked recursively to
     * verify that the entire subtree applies to the same field.
     *
     * @param queryNode Boolean query node or child node
     * @param fieldName The field all nodes should match, or null if called on the root of the subtree
     * @return Whether the root node and all of its descendents can be converted into a single regex query
     */
    public boolean canCoalesceSingleField(QueryNode queryNode, @Nullable CharSequence fieldName) {
        if (!(queryNode instanceof BooleanQueryNode || queryNode instanceof FieldQueryNode
                || queryNode instanceof MultiPhraseQueryNode)
            || queryNode instanceof AndQueryNode || queryNode instanceof AqpAndQueryNode) {
            return false;
        }

        if (queryNode.getChildren() == null) {
            return true;
        }

        if (queryNode.getChildren().stream()
                .allMatch(subQuery -> subQuery instanceof FieldableNode)) {
            List<CharSequence> distinctFields = queryNode.getChildren().stream()
                    .map(subQuery -> ((FieldableNode)subQuery).getField())
                    .distinct().toList();

            if (distinctFields.size() != 1) {
                return false;
            }

            if (fieldName != null && !distinctFields.get(0).equals(fieldName)) {
                return false;
            } else {
                fieldName = distinctFields.get(0);
            }

            for (QueryNode child : queryNode.getChildren()) {
                if (!canCoalesceSingleField(child, fieldName)) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public Query booleanFieldQueryToRegex(QueryNode queryNode) {
        String regex = collectRegex(queryNode);
        return new RegexpQuery(new Term(getRegexField(queryNode), regex));
    }

    public String getRegexField(QueryNode queryNode) {
        if (queryNode instanceof FieldQueryNode) {
            return ((FieldQueryNode) queryNode).getFieldAsString();
        }

        return queryNode.getChildren().stream().map(this::getRegexField).findFirst().get();
    }

    public String collectRegex(QueryNode queryNode) {
        if (queryNode instanceof WildcardQueryNode) {
            String wildcardText = ((WildcardQueryNode) queryNode).getTextAsString();
            wildcardText = wildcardText.replace(".", "\\.");
            wildcardText = wildcardText.replace("*", ".*");
            wildcardText = wildcardText.replace("?", ".");
            return wildcardText;
        } else if (queryNode instanceof FieldQueryNode) {
            return ((FieldQueryNode) queryNode).getTextAsString();
        }

        return queryNode.getChildren().stream().map(this::collectRegex).collect(Collectors.joining("|"));
    }
}
