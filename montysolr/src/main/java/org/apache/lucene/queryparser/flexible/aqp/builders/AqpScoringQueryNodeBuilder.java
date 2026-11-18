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
 * For second-order queries, apply this factor to their seed queries while
 * retaining their collectors and surrounding query structure.
 */
public class AqpScoringQueryNodeBuilder implements StandardQueryBuilder {

    public Query build(QueryNode queryNode) throws QueryNodeException {
        AqpAdsabsScoringQueryNode scoringNode = (AqpAdsabsScoringQueryNode) queryNode;

        Query query = (Query) queryNode.getChildren().get(0).getTag(
                QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);

        Query scoredQuery = scoreSecondOrderSeeds(query, scoringNode.getSource(), scoringNode.getModifier(), true);
        return scoredQuery == null ? wrapQuery(query, scoringNode.getSource(), scoringNode.getModifier()) : scoredQuery;
    }

    /**
     * Returns a rebuilt query with scored second-order seeds, or null if none are found.
     * Ordinary scoring branches are wrapped only when a sibling seed requires rebuilding.
     */
    private Query scoreSecondOrderSeeds(Query query, String source, float modifier, boolean scoreOrdinaryBranches) {
        if (query instanceof SecondOrderQuery secondOrderQuery) {
            Query scoredSeed = scoreSecondOrderSeeds(secondOrderQuery.getQuery(), source, modifier, true);
            if (scoredSeed == null) {
                scoredSeed = wrapQuery(secondOrderQuery.getQuery(), source, modifier);
            }
            return new SecondOrderQuery(scoredSeed, secondOrderQuery.getcollector());
        }

        if (query instanceof BoostQuery boostQuery) {
            Query scoredQuery = scoreSecondOrderSeeds(boostQuery.getQuery(), source, modifier, scoreOrdinaryBranches);
            return scoredQuery == null ? null : new BoostQuery(scoredQuery, boostQuery.getBoost());
        }

        if (query instanceof FunctionScoreQuery functionScoreQuery) {
            Query scoredQuery = scoreSecondOrderSeeds(functionScoreQuery.getWrappedQuery(), source, modifier,
                    scoreOrdinaryBranches);
            return scoredQuery == null ? null : new FunctionScoreQuery(scoredQuery, functionScoreQuery.getSource());
        }

        if (query instanceof ConstantScoreQuery constantScoreQuery) {
            Query scoredQuery = scoreSecondOrderSeeds(constantScoreQuery.getQuery(), source, modifier, false);
            return scoredQuery == null ? null : new ConstantScoreQuery(scoredQuery);
        }

        if (query instanceof BooleanQuery booleanQuery) {
            return scoreBooleanSeeds(booleanQuery, source, modifier, scoreOrdinaryBranches);
        }

        if (query instanceof DisjunctionMaxQuery disjunctionMaxQuery) {
            return scoreDisjunctionSeeds(disjunctionMaxQuery, source, modifier, scoreOrdinaryBranches);
        }

        return null;
    }

    private Query scoreBooleanSeeds(BooleanQuery booleanQuery, String source, float modifier,
                                    boolean scoreOrdinaryBranches) {
        List<BooleanClause> clauses = booleanQuery.clauses();
        BooleanQuery.Builder builder = null;
        for (int i = 0; i < clauses.size(); i++) {
            BooleanClause clause = clauses.get(i);
            Query scoredClause = scoreSecondOrderSeeds(clause.getQuery(), source, modifier,
                    scoreOrdinaryBranches && clause.isScoring());
            if (builder == null && scoredClause != null) {
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
                if (scoredClause != null) {
                    builder.add(scoredClause, clause.getOccur());
                } else if (scoreOrdinaryBranches && clause.isScoring()) {
                    builder.add(wrapQuery(clause.getQuery(), source, modifier), clause.getOccur());
                } else {
                    builder.add(clause);
                }
            }
        }

        return builder == null ? null : builder.build();
    }

    private Query scoreDisjunctionSeeds(DisjunctionMaxQuery disjunctionMaxQuery, String source, float modifier,
                                        boolean scoreOrdinaryBranches) {
        Collection<Query> disjuncts = disjunctionMaxQuery.getDisjuncts();
        List<Query> scoredDisjuncts = null;
        int index = 0;
        for (Query disjunct : disjuncts) {
            Query scoredDisjunct = scoreSecondOrderSeeds(disjunct, source, modifier, scoreOrdinaryBranches);
            if (scoredDisjunct != null) {
                if (scoredDisjuncts == null) {
                    scoredDisjuncts = new ArrayList<>(disjuncts);
                    if (scoreOrdinaryBranches) {
                        for (int previous = 0; previous < index; previous++) {
                            scoredDisjuncts.set(previous,
                                    wrapQuery(scoredDisjuncts.get(previous), source, modifier));
                        }
                    }
                }
                scoredDisjuncts.set(index, scoredDisjunct);
            } else if (scoredDisjuncts != null && scoreOrdinaryBranches) {
                scoredDisjuncts.set(index, wrapQuery(disjunct, source, modifier));
            }
            index++;
        }

        return scoredDisjuncts == null ? null
                : new DisjunctionMaxQuery(scoredDisjuncts, disjunctionMaxQuery.getTieBreakerMultiplier());
    }

    public static Query wrapQuery(Query q, String source, float modifier) {
        ValueSource boostFactor = new SumFloatFunction(new ValueSource[]{
                new FloatFieldSource(source), // classic score
                new ConstValueSource(modifier) // modifier of how much lucene score to use
        });

        return FunctionScoreQuery.boostByValue(q, boostFactor.asDoubleValuesSource());
    }

}
