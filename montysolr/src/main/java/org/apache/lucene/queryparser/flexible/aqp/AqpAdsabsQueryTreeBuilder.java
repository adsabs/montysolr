package org.apache.lucene.queryparser.flexible.aqp;

import org.apache.lucene.queryparser.flexible.aqp.builders.*;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.*;
import org.apache.lucene.queryparser.flexible.core.nodes.*;
import org.apache.lucene.queryparser.flexible.standard.builders.*;
import org.apache.lucene.queryparser.flexible.standard.nodes.*;
import org.apache.solr.search.AqpAdsabsQParser;

/**
 * This is the configuration for the ADS grammar (the last phase: BUILDER)
 * <p>
 * It receives results from the {@link AqpAdsabsNodeProcessorPipeline}
 *
 * @see AqpAdsabsNodeProcessorPipeline
 * @see AqpAdsabsQueryConfigHandler
 * @see AqpAdsabsQueryTreeBuilder
 * @see AqpAdsabsQParser
 */
public class AqpAdsabsQueryTreeBuilder extends AqpQueryTreeBuilder {

    public void init() {

        setBuilder(GroupQueryNode.class, new GroupQueryNodeBuilder());
        setBuilder(AqpAdsabsIdentifierNode.class, new AqpAdsabsIdentifierNodeBuilder());
        setBuilder(FieldQueryNode.class, new AqpFieldQueryNodeBuilder());
        setBuilder(AqpAdsabsRegexQueryNode.class, new AqpFieldQueryNodeRegexBuilder());
        setBuilder(AqpNonAnalyzedQueryNode.class, new AqpFieldQueryNodeBuilder());
        setBuilder(SlowFuzzyQueryNode.class, new AqpSlowFuzzyQueryNodeBuilder());
        setBuilder(FuzzyQueryNode.class, new FuzzyQueryNodeBuilder());
        setBuilder(BoostQueryNode.class, new BoostQueryNodeBuilder());
        setBuilder(ModifierQueryNode.class, new ModifierQueryNodeBuilder());
        setBuilder(WildcardQueryNode.class, new WildcardQueryNodeBuilder());
        setBuilder(TokenizedPhraseQueryNode.class, new PhraseQueryNodeBuilder());
        setBuilder(MatchNoDocsQueryNode.class, new MatchNoDocsQueryNodeBuilder());
        setBuilder(PrefixWildcardQueryNode.class, new PrefixWildcardQueryNodeBuilder());
        setBuilder(PointQueryNode.class, new DummyQueryNodeBuilder());
        setBuilder(PointRangeQueryNode.class, new PointRangeQueryNodeBuilder());
        setBuilder(TermRangeQueryNode.class, new TermRangeQueryNodeBuilder());
        setBuilder(SlopQueryNode.class, new AqpSlopQueryNodeBuilder());
        setBuilder(MultiPhraseQueryNode.class, new MultiPhraseQueryNodeBuilder());
        setBuilder(MatchAllDocsQueryNode.class, new MatchAllDocsQueryNodeBuilder());
        setBuilder(AqpFunctionQueryNode.class, new AqpFunctionQueryNodeBuilder());
        setBuilder(AqpNearQueryNode.class, new AqpNearQueryNodeBuilder());
        setBuilder(OpaqueQueryNode.class, new IgnoreQueryNodeBuilder());
        setBuilder(AqpConstantQueryNode.class, new AqpConstantQueryNodeBuilder(this));
        setBuilder(AqpAdsabsScoringQueryNode.class, new AqpScoringQueryNodeBuilder());
        setBuilder(AqpDisjunctionQueryNode.class, new AqpDisjunctQueryNodeBuilder());
        setBuilder(BooleanQueryNode.class, new AqpBooleanQueryNodeBuilder());
        setBuilder(SynonymQueryNode.class, new SynonymQueryNodeBuilder());

    }

}
