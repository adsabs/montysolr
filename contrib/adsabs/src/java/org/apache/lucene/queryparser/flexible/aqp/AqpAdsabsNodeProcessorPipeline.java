package org.apache.lucene.queryparser.flexible.aqp;

import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.processors.NoChildOptimizationQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.core.processors.RemoveDeletedQueryNodesProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.AllowLeadingWildcardProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.BooleanSingleChildOptimizationQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.BoostQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.DefaultPhraseSlopQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.FuzzyQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.MatchAllDocsQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.MultiFieldQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.MultiTermRewriteMethodProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.PhraseSlopQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.RemoveEmptyNonLeafQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.TermRangeQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.WildcardQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsAnalyzerProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsFixQPOSITIONProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsMODIFIERProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsQPOSITIONProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsRegexNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsSynonymNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpBOOSTProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpBibcodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpCLAUSEProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpCOMMAProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpDEFOPProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpFIELDProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpFUZZYProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpFieldMapperProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpFuzzyModifierProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpGroupQueryOptimizerProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpLowercaseExpandedTermsQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpNullDefaultFieldProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpOPERATORProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpOptimizationProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQANYTHINGProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQDATEProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQFUNCProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQIDENTIFIERProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQNORMALProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQPHRASEProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQPHRASETRUNCProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQRANGEEXProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQRANGEINProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQTRUNCATEDProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpTMODIFIERProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpTreeRewriteProcessor;


public class AqpAdsabsNodeProcessorPipeline extends QueryNodeProcessorPipeline {

	public AqpAdsabsNodeProcessorPipeline(QueryConfigHandler queryConfig) {
		super(queryConfig);
	
		add(new AqpDEFOPProcessor());
		add(new AqpTreeRewriteProcessor());
		
		add(new AqpAdsabsFixQPOSITIONProcessor()); // handles QPHRASE:"^some phrase$" and QNORMAL:word$
		add(new AqpAdsabsQPOSITIONProcessor()); // rewrites ^author$ into a functional form
		add(new AqpQFUNCProcessor()); // prepares function node (may decide which implementation to call)
		
		add(new AqpCOMMAProcessor()); // extends operators with COMMA and SEMICOLON
		add(new AqpAdsabsMODIFIERProcessor()); // extends PLUS and MINUS with # and =
		add(new AqpOPERATORProcessor()); 
		add(new AqpCLAUSEProcessor());
		
		add(new AqpTMODIFIERProcessor()); // changes AST to more manageable form
		
		
		add(new AqpBOOSTProcessor());
		add(new AqpFUZZYProcessor());
	
		add(new AqpQRANGEINProcessor());
		//add(new AqpQRANGEEXProcessor());  // exclusive ranges not used by ADS
		
		add(new AqpQDATEProcessor());
		add(new AqpQPHRASEProcessor());
		add(new AqpQNORMALProcessor());
		add(new AqpQPHRASETRUNCProcessor());
		add(new AqpQTRUNCATEDProcessor());
		add(new AqpQANYTHINGProcessor());
		add(new AqpQIDENTIFIERProcessor());
		add(new AqpFIELDProcessor());
		
	
		add(new AqpFuzzyModifierProcessor());
	
		add(new AqpBibcodeProcessor()); // finds bibcode and converts to AqpAdslabsIdentifier
		add(new WildcardQueryNodeProcessor());
		add(new MultiFieldQueryNodeProcessor()); // expands to multiple fields if field=null
		add(new AqpNullDefaultFieldProcessor());
		add(new FuzzyQueryNodeProcessor());
		add(new MatchAllDocsQueryNodeProcessor());
		
		add(new AqpFieldMapperProcessor()); // translate the field name before we try to find the tokenizer chain
		add(new AqpLowercaseExpandedTermsQueryNodeProcessor()); // use a specific tokenizer chain to modify the terms
		add(new TermRangeQueryNodeProcessor());
		add(new AllowLeadingWildcardProcessor());
		
		add(new AqpAdsabsSynonymNodeProcessor()); //simply wraps the non-synonym QN into NonAnalyzedQueryNode
		
		
		add(new AqpAdsabsRegexNodeProcessor()); // wraps regex QN w/ NonAnalyzedQueryNode
		add(new AqpAdsabsAnalyzerProcessor()); // we prevent analysis to happen inside QFUNC
		
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
