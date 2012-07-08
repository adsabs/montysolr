package org.apache.lucene.queryParser.aqp;

import org.apache.lucene.queryParser.aqp.builders.InvenioQueryNodeBuilder;
import org.apache.lucene.queryParser.aqp.config.InvenioDefaultIdFieldAttribute;
import org.apache.lucene.queryParser.aqp.config.InvenioQueryAttribute;
import org.apache.lucene.queryParser.aqp.nodes.InvenioQueryNode;
import org.apache.lucene.queryParser.aqp.processors.AqpInvenioMODIFIERProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpInvenioQPHRASEProcessor;
import org.apache.lucene.queryparser.flexible.core.QueryNodeParseException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.BoostQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.GroupQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.MatchAllDocsQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.MatchNoDocsQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.SlopQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.TokenizedPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.NoChildOptimizationQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.core.processors.RemoveDeletedQueryNodesProcessor;
import org.apache.lucene.queryparser.flexible.standard.builders.BooleanQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.BoostQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.FuzzyQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.GroupQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.MatchAllDocsQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.MatchNoDocsQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.ModifierQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.MultiPhraseQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.PhraseQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.PrefixWildcardQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.RangeQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.SlopQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardBooleanQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.WildcardQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.config.AllowLeadingWildcardAttribute;
import org.apache.lucene.queryparser.flexible.standard.config.AnalyzerAttribute;
import org.apache.lucene.queryparser.flexible.standard.config.DefaultOperatorAttribute;
import org.apache.lucene.queryparser.flexible.standard.config.DefaultPhraseSlopAttribute;
import org.apache.lucene.queryparser.flexible.standard.config.FieldBoostMapFCListener;
import org.apache.lucene.queryparser.flexible.standard.config.FieldDateResolutionFCListener;
import org.apache.lucene.queryparser.flexible.standard.config.FuzzyAttribute;
import org.apache.lucene.queryparser.flexible.standard.config.LocaleAttribute;
import org.apache.lucene.queryparser.flexible.standard.config.LowercaseExpandedTermsAttribute;
import org.apache.lucene.queryparser.flexible.standard.config.MultiFieldAttribute;
import org.apache.lucene.queryparser.flexible.standard.config.MultiTermRewriteMethodAttribute;
import org.apache.lucene.queryparser.flexible.standard.config.PositionIncrementsAttribute;
import org.apache.lucene.queryparser.flexible.standard.config.RangeCollatorAttribute;
import org.apache.lucene.queryparser.flexible.standard.nodes.MultiPhraseQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.RangeQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.StandardBooleanQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.processors.AllowLeadingWildcardProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.AnalyzerQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.BooleanSingleChildOptimizationQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.BoostQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.DefaultPhraseSlopQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.FuzzyQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.LowercaseExpandedTermsQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.MatchAllDocsQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.MultiFieldQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.MultiTermRewriteMethodProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.ParametricRangeQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.PhraseSlopQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.RemoveEmptyNonLeafQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.WildcardQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.AqpQueryParser;
import org.apache.lucene.queryparser.flexible.aqp.AqpStandardQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.AqpSyntaxParser;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFieldQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpBOOSTProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpCLAUSEProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpDEFOPProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpFIELDProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpFUZZYProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpFuzzyModifierProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpGroupQueryOptimizerProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpNullDefaultFieldProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpOPERATORProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpOptimizationProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQANYTHINGProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQNORMALProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQPHRASETRUNCProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQRANGEEXProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQRANGEINProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQTRUNCATEDProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpTMODIFIERProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpTreeRewriteProcessor;


/**
 * The complete configuration for the Invenio query 
 * parser, this is the main object which parses the 
 * query, from the string, until the query object.
 * 
 * @author rca
 *
 */

public class AqpInvenioQueryParser extends AqpQueryParser {

	public static AqpInvenioQueryParser init () 
		throws QueryNodeParseException {
		return new AqpInvenioQueryParser(new InvenioQueryConfigHandler(), 
				new AqpInvenioSyntaxParser(), //.initializeGrammar("Invenio"),
				new InvenioNodeProcessorPipeline(null),
				new InvenioQueryTreeBuilder());
	}

	
	public static AqpInvenioQueryParser init(String grammarName) throws Exception {
		throw new IllegalArgumentException(
				"Invenio query parser does not support loadable grammars");
	}
	
	public AqpInvenioQueryParser(QueryConfigHandler config,
			AqpSyntaxParser parser,
			QueryNodeProcessorPipeline processor,
			QueryTreeBuilder builder) {
		super(config, parser, processor, builder);
	}

	
	static class InvenioQueryConfigHandler extends QueryConfigHandler {
		public InvenioQueryConfigHandler() {
			// Add listener that will build the FieldConfig attributes.
			addFieldConfigListener(new FieldBoostMapFCListener(this));
			addFieldConfigListener(new FieldDateResolutionFCListener(this));

			// Default Values
			addAttribute(DefaultFieldAttribute.class);
			addAttribute(InvenioDefaultIdFieldAttribute.class);
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
			addAttribute(InvenioQueryAttribute.class);
			addAttribute(MultiFieldAttribute.class);
			
			getAttribute(InvenioDefaultIdFieldAttribute.class).setDefaultIdField("recid");
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
			add(new MultiFieldQueryNodeProcessor()); // expands to multiple fields if field=null
			add(new AqpNullDefaultFieldProcessor());
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
	
	
	static class InvenioQueryTreeBuilder extends AqpStandardQueryTreeBuilder {

		public void init() {
			setBuilder(GroupQueryNode.class, new GroupQueryNodeBuilder());
			setBuilder(FieldQueryNode.class, new AqpFieldQueryNodeBuilder());
			setBuilder(AqpNonAnalyzedQueryNode.class, new AqpFieldQueryNodeBuilder());
			setBuilder(InvenioQueryNode.class,
					new InvenioQueryNodeBuilder(this));
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


	}

}
