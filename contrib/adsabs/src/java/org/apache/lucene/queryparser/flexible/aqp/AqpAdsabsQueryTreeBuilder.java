package org.apache.lucene.queryparser.flexible.aqp;

import org.apache.lucene.queryparser.flexible.aqp.builders.AqpAdsabsIdentifierNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpConstantQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFieldQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFieldQueryNodeRegexBuilder;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFunctionQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpNearQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpSlopQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpSlowFuzzyQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.builders.IgnoreQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsIdentifierNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsRegexQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpConstantQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNearQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.SlowFuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.BoostQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.GroupQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.MatchAllDocsQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.MatchNoDocsQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.OpaqueQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.SlopQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.TokenizedPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.BooleanQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.BoostQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.DummyQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.FuzzyQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.GroupQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.LegacyNumericRangeQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.MatchAllDocsQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.MatchNoDocsQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.ModifierQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.MultiPhraseQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.PhraseQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.PrefixWildcardQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardBooleanQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.TermRangeQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.WildcardQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.nodes.LegacyNumericQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.LegacyNumericRangeQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.MultiPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.StandardBooleanQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.TermRangeQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.solr.search.AqpAdsabsQParser;

/**
 * This is the configuration for the ADS grammar (the last phase: BUILDER)
 * 
 * It receives results from the {@link AqpAdsabsNodeProcessorPipeline}
 * 
 * @see AqpAdsabsNodeProcessorPipeline
 * @see AqpAdsabsQueryConfigHandler
 * @see AqpAdsabsQueryTreeBuilder
 * @see AqpAdsabsQParser
 *
 */
public class AqpAdsabsQueryTreeBuilder extends AqpQueryTreeBuilder {

	public void init() {
		
		setBuilder(GroupQueryNode.class, new GroupQueryNodeBuilder());
		setBuilder(AqpAdsabsIdentifierNode.class, new AqpAdsabsIdentifierNodeBuilder());
		setBuilder(FieldQueryNode.class, new AqpFieldQueryNodeBuilder());
		setBuilder(AqpAdsabsRegexQueryNode.class, new AqpFieldQueryNodeRegexBuilder());
		setBuilder(AqpNonAnalyzedQueryNode.class, new AqpFieldQueryNodeBuilder());
		setBuilder(BooleanQueryNode.class, new BooleanQueryNodeBuilder());
		setBuilder(SlowFuzzyQueryNode.class, new AqpSlowFuzzyQueryNodeBuilder());
		setBuilder(FuzzyQueryNode.class, new FuzzyQueryNodeBuilder());
		setBuilder(BoostQueryNode.class, new BoostQueryNodeBuilder());
		setBuilder(ModifierQueryNode.class, new ModifierQueryNodeBuilder());
		setBuilder(WildcardQueryNode.class, new WildcardQueryNodeBuilder());
		setBuilder(TokenizedPhraseQueryNode.class, new PhraseQueryNodeBuilder());
		setBuilder(MatchNoDocsQueryNode.class, new MatchNoDocsQueryNodeBuilder());
		setBuilder(PrefixWildcardQueryNode.class, new PrefixWildcardQueryNodeBuilder());
		setBuilder(LegacyNumericQueryNode.class, new DummyQueryNodeBuilder()); //TODO: convert to new
		setBuilder(LegacyNumericRangeQueryNode.class, new LegacyNumericRangeQueryNodeBuilder());
		setBuilder(TermRangeQueryNode.class, new TermRangeQueryNodeBuilder());
		setBuilder(SlopQueryNode.class, new AqpSlopQueryNodeBuilder());
		setBuilder(StandardBooleanQueryNode.class, new StandardBooleanQueryNodeBuilder());
		setBuilder(MultiPhraseQueryNode.class, new MultiPhraseQueryNodeBuilder());
		setBuilder(MatchAllDocsQueryNode.class,	new MatchAllDocsQueryNodeBuilder());
		setBuilder(AqpFunctionQueryNode.class,	new AqpFunctionQueryNodeBuilder());
		setBuilder(AqpNearQueryNode.class,	new AqpNearQueryNodeBuilder());
		setBuilder(OpaqueQueryNode.class,	new IgnoreQueryNodeBuilder());
		setBuilder(AqpConstantQueryNode.class, new AqpConstantQueryNodeBuilder(this));
		
	}

}
