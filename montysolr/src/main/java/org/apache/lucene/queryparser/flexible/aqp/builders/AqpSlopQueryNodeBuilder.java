package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAnalyzerQueryNodeProcessor;
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
 * IFF the default value is zero, we'll check positions of the
 * terms and adjust the slope, eg. positions 0,1,5 will get slope of
 * 3 because there are three empty tokens inbetween (2,3,4)
 */
public class AqpSlopQueryNodeBuilder implements StandardQueryBuilder {

    public AqpSlopQueryNodeBuilder() {
        // empty constructor
    }

    public Query build(QueryNode queryNode) throws QueryNodeException {
        SlopQueryNode phraseSlopNode = (SlopQueryNode) queryNode;

        Query query = (Query) phraseSlopNode.getChild().getTag(
                QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID);

        int defaultValue = phraseSlopNode.getValue();


        if (query instanceof PhraseQuery) {

            if (defaultValue == 0) {
                int[] pos = ((PhraseQuery) query).getPositions();
                defaultValue = (pos[pos.length - 1] - pos[0]) - (pos.length - 1);
            }

            if (defaultValue <= 1) return query;

            Builder builder = new PhraseQuery.Builder();
            builder.setSlop(defaultValue);
            for (Term t : ((PhraseQuery) query).getTerms()) {
                builder.add(t);
            }
            query = builder.build();

        } else {

            int maxBranchSize = 0;
            int gap = 0;
            // examine terms that made the multi-phrase query
            for (QueryNode child : queryNode.getChildren().get(0).getChildren()) {
                if (child.getTag(AqpAnalyzerQueryNodeProcessor.MAX_MULTI_TOKEN_SIZE) != null) {
                    gap = (Integer) child.getTag(AqpAnalyzerQueryNodeProcessor.MAX_MULTI_TOKEN_SIZE);
                    if (gap > defaultValue)
                        defaultValue = gap;
                }
                if (child.getTag(AqpPostAnalysisProcessor.QUERY_BRANCH_SIZE) != null) {
                    maxBranchSize = (Integer) child.getTag(AqpPostAnalysisProcessor.QUERY_BRANCH_SIZE);
                }
            }

            if (maxBranchSize > 0 && maxBranchSize == ((MultiPhraseQuery) query).getPositions().length) {
                // return query; // do nothing
            }

            // fallback
            if (defaultValue == 0) {
                int[] pos = ((MultiPhraseQuery) query).getPositions();
                defaultValue = (pos[pos.length - 1] - pos[0]) - (pos.length - 1);
            }

            if (defaultValue <= 1) return query;

            MultiPhraseQuery.Builder builder = new MultiPhraseQuery.Builder((MultiPhraseQuery) query);
            builder.setSlop(defaultValue);

            query = builder.build();
        }

        return query;

    }

}
