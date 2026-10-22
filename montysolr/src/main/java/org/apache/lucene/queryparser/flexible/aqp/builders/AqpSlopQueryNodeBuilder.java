package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.index.Term;
import org.apache.lucene.queries.spans.SpanQuery;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAnalyzerQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpFuzzyModifierProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpPostAnalysisProcessor;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.SlopQueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PhraseQuery.Builder;
import org.apache.lucene.search.Query;

/**
 * This builder basically reads the {@link Query} object set on the
 * {@link SlopQueryNode} child using
 * {@link QueryTreeBuilder#QUERY_TREE_BUILDER_TAGID} and applies the slop value
 * defined in the {@link SlopQueryNode}.
 * <p>
 * For implicit slop on phrases without preserved raw or graph positions, a
 * zero value falls back to the position holes. Positions 0,1,5 contribute
 * three empty positions (2,3,4). Explicit zero slop leaves the query unchanged.
 */
public class AqpSlopQueryNodeBuilder implements StandardQueryBuilder {

    public AqpSlopQueryNodeBuilder() {
        // empty constructor
    }

    public Query build(QueryNode queryNode) throws QueryNodeException {
        SlopQueryNode phraseSlopNode = (SlopQueryNode) queryNode;

        Query query = (Query) phraseSlopNode.getChild().getTag(
                QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);

        if (query instanceof SpanQuery) {
            return query;
        }

        int effectiveSlop = phraseSlopNode.getValue();
        boolean explicitSlop = Boolean.TRUE.equals(
                phraseSlopNode.getTag(AqpFuzzyModifierProcessor.EXPLICIT_SLOP));
        if (explicitSlop && effectiveSlop == 0) {
            return query;
        }

        boolean preserveGraphPositions =
                phraseSlopNode.getTag(AqpPostAnalysisProcessor.EXACT_GRAPH_PATH) != null
                        || phraseSlopNode.getChild().getTag(AqpPostAnalysisProcessor.EXACT_GRAPH_PATH) != null;

        if (query instanceof PhraseQuery) {
            boolean rawPositions = hasRawPositions(phraseSlopNode.getChild());
            if (rawPositions || preserveGraphPositions) {
                PhraseQuery phrase = (PhraseQuery) query;
                if (phrase.getSlop() == effectiveSlop) {
                    return phrase;
                }

                Builder builder = new PhraseQuery.Builder().setSlop(effectiveSlop);
                Term[] terms = phrase.getTerms();
                int[] positions = phrase.getPositions();
                for (int i = 0; i < terms.length; i++) {
                    builder.add(terms[i], positions[i]);
                }
                return builder.build();
            }

            if (effectiveSlop == 0) {
                int[] positions = ((PhraseQuery) query).getPositions();
                effectiveSlop = (positions[positions.length - 1] - positions[0]) - (positions.length - 1);
            }

            if (explicitSlop ? effectiveSlop == 0 : effectiveSlop <= 1) {
                return query;
            }

            Builder builder = new PhraseQuery.Builder();
            builder.setSlop(effectiveSlop);
            for (Term term : ((PhraseQuery) query).getTerms()) {
                builder.add(term);
            }
            query = builder.build();
        } else {
            boolean rawPositions = hasRawPositions(phraseSlopNode.getChild());
            if (rawPositions || preserveGraphPositions) {
                MultiPhraseQuery phrase = (MultiPhraseQuery) query;
                if (phrase.getSlop() == effectiveSlop) {
                    return phrase;
                }

                return new MultiPhraseQuery.Builder(phrase).setSlop(effectiveSlop).build();
            }

            int maxBranchSize = 0;
            int gap = 0;

            // Examine terms that made the multi-phrase query.
            if (!explicitSlop) {
                for (QueryNode child : queryNode.getChildren().get(0).getChildren()) {
                    if (child.getTag(AqpAnalyzerQueryNodeProcessor.MAX_MULTI_TOKEN_SIZE) != null) {
                        gap = (Integer) child.getTag(AqpAnalyzerQueryNodeProcessor.MAX_MULTI_TOKEN_SIZE);
                        if (gap > effectiveSlop) {
                            effectiveSlop = gap;
                        }
                    }
                    if (child.getTag(AqpPostAnalysisProcessor.QUERY_BRANCH_SIZE) != null) {
                        maxBranchSize = (Integer) child.getTag(AqpPostAnalysisProcessor.QUERY_BRANCH_SIZE);
                    }
                }
            }

            if (maxBranchSize > 0 && maxBranchSize == ((MultiPhraseQuery) query).getPositions().length) {
                // return query; // do nothing
            }

            // Fall back to position holes when no implicit slop was inferred.
            if (!explicitSlop && effectiveSlop == 0) {
                int[] positions = ((MultiPhraseQuery) query).getPositions();
                effectiveSlop = (positions[positions.length - 1] - positions[0]) - (positions.length - 1);
            }

            if (explicitSlop ? effectiveSlop == 0 : effectiveSlop <= 1) {
                return query;
            }

            MultiPhraseQuery.Builder builder = new MultiPhraseQuery.Builder((MultiPhraseQuery) query);
            builder.setSlop(effectiveSlop);
            query = builder.build();
        }

        return query;
    }

    private boolean hasRawPositions(QueryNode node) {
        if (Boolean.TRUE.equals(node.getTag(
                AqpPostAnalysisProcessor.RAW_POSITIONAL_PATH))) {
            return true;
        }
        if (node.getChildren() != null) {
            for (QueryNode child : node.getChildren()) {
                if (hasRawPositions(child)) {
                    return true;
                }
            }
        }
        return false;
    }

}
