package org.apache.solr.search;

import org.apache.lucene.queryParser.aqp.AqpInvenioSyntaxParser;
import org.apache.lucene.queryParser.aqp.AqpQueryParser;
import org.apache.lucene.queryParser.aqp.builders.AqpFieldQueryNodeBuilder;
import org.apache.lucene.queryParser.aqp.builders.InvenioQueryNodeBuilder;
import org.apache.lucene.queryParser.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryParser.aqp.config.DefaultIdFieldAttribute;
import org.apache.lucene.queryParser.aqp.config.InvenioQueryAttribute;
import org.apache.lucene.queryParser.aqp.nodes.InvenioQueryNode;
import org.apache.lucene.queryParser.aqp.nodes.NonAnalyzedQueryNode;
import org.apache.lucene.queryParser.aqp.processors.AqpBOOSTProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpCLAUSEProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpDEFOPProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpFIELDProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpFUZZYProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpFuzzyModifierProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpGroupQueryOptimizerProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpInvenioMODIFIERProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpInvenioQPHRASEProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpOPERATORProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpOptimizationProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQANYTHINGProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQNORMALProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQPHRASETRUNCProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQRANGEEXProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQRANGEINProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQTRUNCATEDProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpTMODIFIERProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpTreeRewriteProcessor;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.QueryNodeParseException;
import org.apache.lucene.queryParser.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryParser.core.nodes.BoostQueryNode;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryParser.core.nodes.GroupQueryNode;
import org.apache.lucene.queryParser.core.nodes.MatchAllDocsQueryNode;
import org.apache.lucene.queryParser.core.nodes.MatchNoDocsQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.SlopQueryNode;
import org.apache.lucene.queryParser.core.nodes.TokenizedPhraseQueryNode;
import org.apache.lucene.queryParser.core.processors.NoChildOptimizationQueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryParser.core.processors.RemoveDeletedQueryNodesProcessor;
import org.apache.lucene.queryParser.standard.builders.BooleanQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.BoostQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.FuzzyQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.GroupQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.MatchAllDocsQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.MatchNoDocsQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.ModifierQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.MultiPhraseQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.PhraseQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.PrefixWildcardQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.RangeQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.SlopQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.StandardBooleanQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.builders.StandardQueryBuilder;
import org.apache.lucene.queryParser.standard.builders.WildcardQueryNodeBuilder;
import org.apache.lucene.queryParser.standard.config.AllowLeadingWildcardAttribute;
import org.apache.lucene.queryParser.standard.config.AnalyzerAttribute;
import org.apache.lucene.queryParser.standard.config.DefaultOperatorAttribute;
import org.apache.lucene.queryParser.standard.config.DefaultPhraseSlopAttribute;
import org.apache.lucene.queryParser.standard.config.FieldBoostMapFCListener;
import org.apache.lucene.queryParser.standard.config.FieldDateResolutionFCListener;
import org.apache.lucene.queryParser.standard.config.FuzzyAttribute;
import org.apache.lucene.queryParser.standard.config.LocaleAttribute;
import org.apache.lucene.queryParser.standard.config.LowercaseExpandedTermsAttribute;
import org.apache.lucene.queryParser.standard.config.MultiTermRewriteMethodAttribute;
import org.apache.lucene.queryParser.standard.config.PositionIncrementsAttribute;
import org.apache.lucene.queryParser.standard.config.RangeCollatorAttribute;
import org.apache.lucene.queryParser.standard.nodes.MultiPhraseQueryNode;
import org.apache.lucene.queryParser.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryParser.standard.nodes.RangeQueryNode;
import org.apache.lucene.queryParser.standard.nodes.StandardBooleanQueryNode;
import org.apache.lucene.queryParser.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryParser.standard.processors.AllowLeadingWildcardProcessor;
import org.apache.lucene.queryParser.standard.processors.AnalyzerQueryNodeProcessor;
import org.apache.lucene.queryParser.standard.processors.BooleanSingleChildOptimizationQueryNodeProcessor;
import org.apache.lucene.queryParser.standard.processors.BoostQueryNodeProcessor;
import org.apache.lucene.queryParser.standard.processors.DefaultPhraseSlopQueryNodeProcessor;
import org.apache.lucene.queryParser.standard.processors.FuzzyQueryNodeProcessor;
import org.apache.lucene.queryParser.standard.processors.LowercaseExpandedTermsQueryNodeProcessor;
import org.apache.lucene.queryParser.standard.processors.MatchAllDocsQueryNodeProcessor;
import org.apache.lucene.queryParser.standard.processors.MultiFieldQueryNodeProcessor;
import org.apache.lucene.queryParser.standard.processors.MultiTermRewriteMethodProcessor;
import org.apache.lucene.queryParser.standard.processors.ParametricRangeQueryNodeProcessor;
import org.apache.lucene.queryParser.standard.processors.PhraseSlopQueryNodeProcessor;
import org.apache.lucene.queryParser.standard.processors.RemoveEmptyNonLeafQueryNodeProcessor;
import org.apache.lucene.queryParser.standard.processors.WildcardQueryNodeProcessor;
import org.apache.lucene.search.Query;


/**
 * Class that can be used inside Lucene, without knowledge of Solr
 * schema etc
 * 
 * @author rca
 *
 */

public class AqpInvenioQueryParserSolr extends AqpQueryParser {

	public AqpInvenioQueryParserSolr() throws QueryNodeParseException {
		super(new InvenioQueryConfigHandler(), new AqpInvenioSyntaxParser()
				.initializeGrammar("Invenio"),
				new InvenioNodeProcessorPipeline(null),
				new InvenioQueryTreeBuilder());
	}

	
	public AqpInvenioQueryParserSolr(String grammarName) throws Exception {
		throw new IllegalArgumentException(
				"Invenio query parser does not use reflection");
	}

	
	static class InvenioQueryConfigHandler extends QueryConfigHandler {
		public InvenioQueryConfigHandler() {
			// Add listener that will build the FieldConfig attributes.
			addFieldConfigListener(new FieldBoostMapFCListener(this));
			addFieldConfigListener(new FieldDateResolutionFCListener(this));

			// Default Values
			addAttribute(DefaultFieldAttribute.class);
			addAttribute(DefaultIdFieldAttribute.class);
			addAttribute(RangeCollatorAttribute.class);
			addAttribute(DefaultOperatorAttribute.class);
			addAttribute(AnalyzerAttribute.class);
			addAttribute(FuzzyAttribute.class);
			addAttribute(LowercaseExpandedTermsAttribute.class);
			addAttribute(MultiTermRewriteMethodAttribute.class);
			addAttribute(AllowLeadingWildcardAttribute.class);
			addAttribute(PositionIncrementsAttribute.class);
			addAttribute(LocaleAttribute.class);
			addAttribute(DefaultPhraseSlopAttribute.class);
			addAttribute(MultiTermRewriteMethodAttribute.class);
			
			// Special attributes
			addAttribute(InvenioQueryAttribute.class);
			
		}
	}

	static class InvenioNodeProcessorPipeline extends
			QueryNodeProcessorPipeline {

		public InvenioNodeProcessorPipeline(QueryConfigHandler queryConfig) {
			super(queryConfig);

			add(new AqpDEFOPProcessor());
			add(new AqpTreeRewriteProcessor());

			add(new AqpInvenioMODIFIERProcessor());
			add(new AqpOPERATORProcessor());
			add(new AqpCLAUSEProcessor());
			add(new AqpTMODIFIERProcessor());
			add(new AqpBOOSTProcessor());
			add(new AqpFUZZYProcessor());

			add(new AqpQRANGEINProcessor());
			add(new AqpQRANGEEXProcessor());
			add(new AqpQNORMALProcessor());
			add(new AqpInvenioQPHRASEProcessor());
			add(new AqpQPHRASETRUNCProcessor());
			add(new AqpQTRUNCATEDProcessor());
			add(new AqpQRANGEINProcessor());
			add(new AqpQRANGEEXProcessor());
			add(new AqpQANYTHINGProcessor());
			add(new AqpFIELDProcessor());

			add(new AqpFuzzyModifierProcessor());

			add(new WildcardQueryNodeProcessor());
			add(new MultiFieldQueryNodeProcessor());
			add(new FuzzyQueryNodeProcessor());
			add(new MatchAllDocsQueryNodeProcessor());
			add(new LowercaseExpandedTermsQueryNodeProcessor());
			add(new ParametricRangeQueryNodeProcessor());
			add(new AllowLeadingWildcardProcessor());
			add(new AnalyzerQueryNodeProcessor());
			add(new PhraseSlopQueryNodeProcessor());

			// add(new GroupQueryNodeProcessor());
			add(new NoChildOptimizationQueryNodeProcessor());
			add(new RemoveDeletedQueryNodesProcessor());
			add(new RemoveEmptyNonLeafQueryNodeProcessor());
			add(new BooleanSingleChildOptimizationQueryNodeProcessor());
			add(new DefaultPhraseSlopQueryNodeProcessor());
			add(new BoostQueryNodeProcessor());
			add(new MultiTermRewriteMethodProcessor());

			add(new AqpGroupQueryOptimizerProcessor());
			add(new AqpOptimizationProcessor());
		}

	}

	static class InvenioQueryTreeBuilder extends QueryTreeBuilder implements
			StandardQueryBuilder {

		public InvenioQueryTreeBuilder() {
			setBuilder(GroupQueryNode.class, new GroupQueryNodeBuilder());
			setBuilder(FieldQueryNode.class, new AqpFieldQueryNodeBuilder());
			setBuilder(NonAnalyzedQueryNode.class, new AqpFieldQueryNodeBuilder());
			setBuilder(InvenioQueryNode.class,	new InvenioQueryNodeBuilder(this));
			setBuilder(BooleanQueryNode.class, new BooleanQueryNodeBuilder());
			setBuilder(FuzzyQueryNode.class, new FuzzyQueryNodeBuilder());
			setBuilder(BoostQueryNode.class, new BoostQueryNodeBuilder());
			setBuilder(ModifierQueryNode.class, new ModifierQueryNodeBuilder());
			setBuilder(WildcardQueryNode.class, new WildcardQueryNodeBuilder());
			setBuilder(TokenizedPhraseQueryNode.class,
					new PhraseQueryNodeBuilder());
			setBuilder(MatchNoDocsQueryNode.class,
					new MatchNoDocsQueryNodeBuilder());
			setBuilder(PrefixWildcardQueryNode.class,
					new PrefixWildcardQueryNodeBuilder());
			setBuilder(RangeQueryNode.class, new RangeQueryNodeBuilder());
			setBuilder(SlopQueryNode.class, new SlopQueryNodeBuilder());
			setBuilder(StandardBooleanQueryNode.class,
					new StandardBooleanQueryNodeBuilder());
			setBuilder(MultiPhraseQueryNode.class,
					new MultiPhraseQueryNodeBuilder());
			setBuilder(MatchAllDocsQueryNode.class,
					new MatchAllDocsQueryNodeBuilder());

		}

		@Override
		public Query build(QueryNode queryNode) throws QueryNodeException {
			return (Query) super.build(queryNode);
		}

	}
	
	public Query parse(String query) throws QueryNodeException {
		return (Query) super.parse(query, null);
	}

}
