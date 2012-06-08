package org.apache.lucene.queryParser.aqp;

import org.apache.lucene.queryParser.aqp.processors.AqpAdslabsAnalyzerProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpAdslabsFixQPOSITIONProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpAdslabsMODIFIERProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpAdslabsQPOSITIONProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpAdslabsRegexNodeProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpAdslabsSynonymNodeProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpBOOSTProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpBibcodeProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpCLAUSEProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpCOMMAProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpDEFOPProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpFIELDProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpFUZZYProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpFieldMapperProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpFuzzyModifierProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpGroupQueryOptimizerProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpLowercaseExpandedTermsQueryNodeProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpNullDefaultFieldProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpOPERATORProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpOptimizationProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQANYTHINGProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQDATEProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQFUNCProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQIDENTIFIERProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQNORMALProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQPHRASEProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQPHRASETRUNCProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQRANGEEXProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQRANGEINProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQTRUNCATEDProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpTMODIFIERProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpTreeRewriteProcessor;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.processors.NoChildOptimizationQueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryParser.core.processors.RemoveDeletedQueryNodesProcessor;
import org.apache.lucene.queryParser.standard.processors.AllowLeadingWildcardProcessor;
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


public class AqpAdslabsNodeProcessorPipeline extends QueryNodeProcessorPipeline {

	public AqpAdslabsNodeProcessorPipeline(QueryConfigHandler queryConfig) {
		super(queryConfig);
	
		add(new AqpDEFOPProcessor());
		add(new AqpTreeRewriteProcessor());
		
		add(new AqpAdslabsFixQPOSITIONProcessor()); // handles QPHRASE:"^some phrase$" and QNORMAL:word$
		add(new AqpAdslabsQPOSITIONProcessor()); // rewrites ^author$ into a functional form
		add(new AqpQFUNCProcessor()); // prepares function node (may decide which implementation to call)
		
		add(new AqpCOMMAProcessor()); // extends with COMMA and SEMICOLON
		add(new AqpAdslabsMODIFIERProcessor()); // extends PLUS and MINUS with # and =
		add(new AqpOPERATORProcessor()); 
		add(new AqpCLAUSEProcessor());
		
		add(new AqpTMODIFIERProcessor()); // changes AST to more manageable form
		
		
		add(new AqpBOOSTProcessor());
		add(new AqpFUZZYProcessor());
	
		add(new AqpQRANGEINProcessor());
		//add(new AqpQRANGEEXProcessor());
		
		add(new AqpQDATEProcessor());
		add(new AqpQPHRASEProcessor());
		add(new AqpQNORMALProcessor());
		add(new AqpQPHRASETRUNCProcessor());
		add(new AqpQTRUNCATEDProcessor());
		add(new AqpQRANGEINProcessor());
		add(new AqpQRANGEEXProcessor());
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
		
		add(new AqpLowercaseExpandedTermsQueryNodeProcessor());
		add(new ParametricRangeQueryNodeProcessor());
		add(new AllowLeadingWildcardProcessor());
		
		add(new AqpAdslabsSynonymNodeProcessor()); //simply wraps the non-synonym QN into NonAnalyzedQueryNode
		
		add(new AqpFieldMapperProcessor());
		add(new AqpAdslabsAnalyzerProcessor()); // we prevent analysis to happen inside QFUNC
		add(new AqpAdslabsRegexNodeProcessor()); // wraps regex QN w/ NonAnalyzedQueryNode
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
