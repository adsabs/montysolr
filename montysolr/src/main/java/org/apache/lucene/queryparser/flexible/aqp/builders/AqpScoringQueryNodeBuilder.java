package org.apache.lucene.queryparser.flexible.aqp.builders;


import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.valuesource.ConstValueSource;
import org.apache.lucene.queries.function.valuesource.FloatFieldSource;
import org.apache.lucene.queries.function.valuesource.SumFloatFunction;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsScoringQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SecondOrderQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author rchyla
 * <p>
 * Will produce a FunctionScoreQuery which combines scores computed by
 * lucene with the value indexed in an index, using formula:
 * <p>
 * score = lucene_score * ( classic_factor + modifier )
 */
public class AqpScoringQueryNodeBuilder implements StandardQueryBuilder {


    public Query build(QueryNode queryNode) throws QueryNodeException {

        AqpAdsabsScoringQueryNode q = (AqpAdsabsScoringQueryNode) queryNode;

        Query query = (Query) queryNode.getChildren().get(0).getTag(
                QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);

        Query pushed = pushClassicScore(query, q.getSource(), q.getModifier(), true);
        return pushed == null ? wrapQuery(query, q.getSource(), q.getModifier()) : pushed;
    }

    private Query pushClassicScore(Query query, String source, float modifier, boolean scoreOrdinaryBranches) {
        if (query instanceof SecondOrderQuery secondOrderQuery) {
            Query seed = pushClassicScore(secondOrderQuery.getQuery(), source, modifier, true);
            if (seed == null) {
                seed = wrapQuery(secondOrderQuery.getQuery(), source, modifier);
            }
            return new SecondOrderQuery(seed, secondOrderQuery.getcollector());
        }
        if (query instanceof BoostQuery boostQuery) {
            Query wrapped = pushClassicScore(boostQuery.getQuery(), source, modifier, scoreOrdinaryBranches);
            return wrapped == null ? null : new BoostQuery(wrapped, boostQuery.getBoost());
        }
        if (query instanceof FunctionScoreQuery functionScoreQuery) {
            Query wrapped = pushClassicScore(functionScoreQuery.getWrappedQuery(), source, modifier,
                    scoreOrdinaryBranches);
            return wrapped == null ? null : new FunctionScoreQuery(wrapped, functionScoreQuery.getSource());
        }
        if (query instanceof ConstantScoreQuery constantScoreQuery) {
            Query wrapped = pushClassicScore(constantScoreQuery.getQuery(), source, modifier, false);
            return wrapped == null ? null : new ConstantScoreQuery(wrapped);
        }
        if (query instanceof BooleanQuery booleanQuery) {
            List<BooleanClause> clauses = booleanQuery.clauses();
            BooleanQuery.Builder builder = null;
            for (int i = 0; i < clauses.size(); i++) {
                BooleanClause clause = clauses.get(i);
                Query wrapped = pushClassicScore(clause.getQuery(), source, modifier,
                        scoreOrdinaryBranches && clause.isScoring());
                if (builder == null && wrapped != null) {
                    builder = new BooleanQuery.Builder()
                            .setMinimumNumberShouldMatch(booleanQuery.getMinimumNumberShouldMatch());
                    for (int previous = 0; previous < i; previous++) {
                        BooleanClause previousClause = clauses.get(previous);
                        if (scoreOrdinaryBranches && previousClause.isScoring()) {
                            builder.add(wrapQuery(previousClause.getQuery(), source, modifier),
                                    previousClause.getOccur());
                        } else {
                            builder.add(previousClause);
                        }
                    }
                }
                if (builder != null) {
                    if (wrapped != null) {
                        builder.add(wrapped, clause.getOccur());
                    } else if (scoreOrdinaryBranches && clause.isScoring()) {
                        builder.add(wrapQuery(clause.getQuery(), source, modifier), clause.getOccur());
                    } else {
                        builder.add(clause);
                    }
                }
            }
            return builder == null ? null : builder.build();
        }
        if (query instanceof DisjunctionMaxQuery disjunctionMaxQuery) {
            Collection<Query> disjuncts = disjunctionMaxQuery.getDisjuncts();
            List<Query> wrappedDisjuncts = null;
            int index = 0;
            for (Query disjunct : disjuncts) {
                Query wrapped = pushClassicScore(disjunct, source, modifier, scoreOrdinaryBranches);
                if (wrapped != null) {
                    if (wrappedDisjuncts == null) {
                        wrappedDisjuncts = new ArrayList<>(disjuncts);
                        if (scoreOrdinaryBranches) {
                            for (int previous = 0; previous < index; previous++) {
                                wrappedDisjuncts.set(previous,
                                        wrapQuery(wrappedDisjuncts.get(previous), source, modifier));
                            }
                        }
                    }
                    wrappedDisjuncts.set(index, wrapped);
                } else if (wrappedDisjuncts != null && scoreOrdinaryBranches) {
                    wrappedDisjuncts.set(index, wrapQuery(disjunct, source, modifier));
                }
                index++;
            }
            return wrappedDisjuncts == null ? null
                    : new DisjunctionMaxQuery(wrappedDisjuncts, disjunctionMaxQuery.getTieBreakerMultiplier());
        }
        return null;
    }

    public static Query wrapQuery(Query q, String source, float modifier) {
        ValueSource vs = new SumFloatFunction(new ValueSource[]{
                new FloatFieldSource(source), // classic score
                new ConstValueSource(modifier) // modifier of how much lucene score to use
        });

        return FunctionScoreQuery.boostByValue(q, vs.asDoubleValuesSource());
    }

}

