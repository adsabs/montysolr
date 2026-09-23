package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNearQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpPostAnalysisProcessor;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;

import org.apache.lucene.queries.spans.SpanNearQuery;
import org.apache.lucene.queries.spans.SpanQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.MatchNoDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryVisitor;
import org.apache.lucene.util.automaton.ByteRunAutomaton;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * The builder for the {@link AqpNearQueryNode}, example query:
 *
 * <pre>
 *   dog NEAR/5 cat
 *  </pre>
 *
 * <p>
 * After the AST tree was parsed, and synonyms were found,
 * we may have the following tree:
 *
 *
 * <pre>
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
 * <p>
 * <p>
 * This builder does not know (yet) how to handle cases of
 * mixed boolean operators, eg.
 *
 * <pre>
 * (dog AND (cat OR fat)) NEAR/5 batman
 * </pre>
 *
 * @see AqpNearQueryNode
 */
public class AqpNearQueryNodeBuilder implements QueryBuilder {
    private final java.util.function.Predicate<String> fieldEligible;

    public AqpNearQueryNodeBuilder() {
        this(field -> true);
    }

    public AqpNearQueryNodeBuilder(java.util.function.Predicate<String> fieldEligible) {
        this.fieldEligible = fieldEligible;
    }


    public Object build(QueryNode queryNode) throws QueryNodeException {
        AqpNearQueryNode nearNode = (AqpNearQueryNode) queryNode;
        List<QueryNode> children = nearNode.getChildren();
        if (children == null) {
            throw new QueryNodeException(new MessageImpl(
                    QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                    "Illegal state for: " + nearNode));
        }
        if (children.isEmpty()) {
            return new MatchNoDocsQuery();
        }
        if (children.size() == 1) {
            Object obj = children.get(0).getTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);
            if (obj instanceof Query) {
                return obj;
            }
            throw new QueryNodeException(new MessageImpl(
                    QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                    "One of the clauses inside AqpNearQueryNode is null"));
        }

        List<Query> queries = new ArrayList<>(children.size());
        Set<String> commonFields = null;
        boolean allFieldsKnown = true;

        for (QueryNode child : children) {
            Object obj = child.getTag(QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);
            if (!(obj instanceof Query query)) {
                throw new QueryNodeException(new MessageImpl(
                        QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                        "One of the clauses inside AqpNearQueryNode is null"));
            }

            queries.add(query);
            Set<String> fields = fieldsOf(query);
            if (fields.isEmpty()) {
                allFieldsKnown = false;
                continue;
            }
            if (commonFields == null) {
                commonFields = new LinkedHashSet<>(fields);
            } else {
                commonFields.retainAll(fields);
            }
        }
        if (commonFields != null) {
            commonFields.removeIf(field -> !fieldEligible.test(field));
        }

        if (allFieldsKnown && commonFields != null && !commonFields.isEmpty()) {
            boolean needsAlignment = commonFields.size() != 1;
            if (!needsAlignment) {
                for (Query query : queries) {
                    if (!fieldsOf(query).equals(commonFields)) {
                        needsAlignment = true;
                        break;
                    }
                }
            }

            if (!needsAlignment) {
                return buildSpanNear(queries, nearNode);
            }

            // Unfielded operands are expanded into field disjunctions. Build
            // one proximity query per field and combine those queries at the
            // Boolean-query level: SpanOrQuery itself cannot mix fields.
            List<SpanQuery> aligned = new ArrayList<>(commonFields.size());
            for (String field : commonFields) {
                List<Query> perField = new ArrayList<>(queries.size());
                boolean complete = true;
                for (Query query : queries) {
                    Query restricted = restrictToField(query, field);
                    if (restricted == null) {
                        complete = false;
                        break;
                    }
                    perField.add(restricted);
                }
                if (complete) {
                    aligned.add((SpanQuery) buildSpanNear(perField, nearNode));
                }
            }

            if (!aligned.isEmpty()) {
                if (aligned.size() == 1) {
                    return aligned.get(0);
                }
                BooleanQuery.Builder disjunction = new BooleanQuery.Builder();
                for (SpanQuery clause : aligned) {
                    disjunction.add(clause, BooleanClause.Occur.SHOULD);
                }
                return disjunction.build();
            }
        }

        throw new QueryNodeException(new MessageImpl(
                QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                "NEAR operands do not share a concrete field: " + nearNode));
    }

    private Query buildSpanNear(List<Query> queries, AqpNearQueryNode nearNode)
            throws QueryNodeException {
        if (queries.isEmpty()) {
            return new MatchNoDocsQuery();
        }
        if (queries.size() == 1) {
            return queries.get(0);
        }
        SpanConverter converter = new SpanConverter();
        SpanQuery[] clauses = new SpanQuery[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            Query query = queries.get(i);
            float boost = query instanceof BoostQuery
                    ? ((BoostQuery) query).getBoost() : 1.0f;
            clauses[i] = converter.getSpanQuery(new SpanConverterContainer(
                    query, nearNode.getSlop(), nearNode.getInOrder(), boost));
        }

        int[] rawGaps = (int[]) nearNode.getTag(
                AqpPostAnalysisProcessor.RAW_POSITIONAL_GAPS);
        if (rawGaps != null) {
            if (rawGaps.length != clauses.length || rawGaps.length == 0
                    || rawGaps[0] != 0) {
                throw new QueryNodeException(new MessageImpl(
                        QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                        "Invalid fixed-gap positions for AqpNearQueryNode"));
            }
            SpanNearQuery.Builder builder = new SpanNearQuery.Builder(
                    clauses[0].getField(), nearNode.getInOrder());
            for (int i = 0; i < clauses.length; i++) {
                if (rawGaps[i] < 0) {
                    throw new QueryNodeException(new MessageImpl(
                            QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                            "Negative fixed gap in AqpNearQueryNode"));
                }
                if (rawGaps[i] > 0) {
                    builder.addGap(rawGaps[i]);
                }
                builder.addClause(clauses[i]);
            }
            builder.setSlop(nearNode.getSlop());
            return builder.build();
        }
        return new SpanNearQuery(clauses, nearNode.getSlop(), nearNode.getInOrder());
    }

    private Set<String> fieldsOf(Query query) {
        Set<String> fields = new LinkedHashSet<>();
        if (query instanceof SpanNearQuery spanNear) {
            for (SpanQuery clause : spanNear.getClauses()) {
                fields.addAll(fieldsOf(clause));
            }
            return fields;
        }
        query.visit(new QueryVisitor() {
            @Override
            public void consumeTerms(Query q, Term... terms) {
                for (Term term : terms) {
                    fields.add(term.field());
                }
            }

            @Override
            public void consumeTermsMatching(Query q, String field,
                                              Supplier<ByteRunAutomaton> automaton) {
                fields.add(field);
            }

            @Override
            public QueryVisitor getSubVisitor(BooleanClause.Occur occur, Query parent) {
                return this;
            }
        });
        return fields;
    }

    private Query restrictToField(Query query, String field) {
        if (query instanceof BoostQuery boost) {
            Query restricted = restrictToField(boost.getQuery(), field);
            return restricted == null ? null : new BoostQuery(restricted, boost.getBoost());
        }
        if (query instanceof ConstantScoreQuery constant) {
            Query restricted = restrictToField(constant.getQuery(), field);
            return restricted == null ? null : new ConstantScoreQuery(restricted);
        }
        if (query instanceof DisjunctionMaxQuery disjunction) {
            List<Query> restricted = new ArrayList<>();
            for (Query clause : disjunction.getDisjuncts()) {
                Query selected = restrictToField(clause, field);
                if (selected != null) {
                    restricted.add(selected);
                }
            }
            if (restricted.isEmpty()) {
                return null;
            }
            if (restricted.size() == 1) {
                return restricted.get(0);
            }
            return new DisjunctionMaxQuery(restricted,
                    disjunction.getTieBreakerMultiplier());
        }
        if (query instanceof SpanNearQuery) {
            Set<String> fields = fieldsOf(query);
            return fields.size() == 1 && fields.contains(field) ? query : null;
        }
        if (query instanceof BooleanQuery booleanQuery) {
            BooleanQuery.Builder restricted = new BooleanQuery.Builder();
            int retained = 0;
            int retainedShould = 0;
            for (BooleanClause clause : booleanQuery.clauses()) {
                Query selected = restrictToField(clause.getQuery(), field);
                if (selected == null) {
                    // Dropping a required or prohibited clause changes the
                    // grouped operand's meaning, so this field is ineligible.
                    if (clause.isRequired() || clause.isProhibited()) {
                        return null;
                    }
                    continue;
                }
                restricted.add(selected, clause.getOccur());
                retained++;
                if (clause.getOccur() == BooleanClause.Occur.SHOULD) {
                    retainedShould++;
                }
            }
            if (retained == 0
                    || booleanQuery.getMinimumNumberShouldMatch() > retainedShould) {
                return null;
            }
            restricted.setMinimumNumberShouldMatch(
                    booleanQuery.getMinimumNumberShouldMatch());
            return restricted.build();
        }

        Set<String> fields = fieldsOf(query);
        return fields.size() == 1 && fields.contains(field) ? query : null;
    }
}
