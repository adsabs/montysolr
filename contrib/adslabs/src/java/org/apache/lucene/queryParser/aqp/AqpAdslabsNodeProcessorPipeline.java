package org.apache.lucene.queryParser.aqp;

import org.apache.lucene.queryParser.aqp.processors.AqpBOOSTProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpCLAUSEProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpDEFOPProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpFIELDProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpFUZZYProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpFuzzyModifierProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpGroupQueryOptimizerProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpInvenioMODIFIERProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpInvenioQPHRASEProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpMODIFIERProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpNullDefaultFieldProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpOPERATORProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpOptimizationProcessor;
import org.apache.lucene.queryParser.aqp.processors.AqpQANYTHINGProcessor;
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


public class AqpAdslabsNodeProcessorPipeline extends QueryNodeProcessorPipeline {

	public AqpAdslabsNodeProcessorPipeline(QueryConfigHandler queryConfig) {
		super(queryConfig);
	
		add(new AqpDEFOPProcessor());
		add(new AqpTreeRewriteProcessor());
	
		add(new AqpMODIFIERProcessor());
		add(new AqpOPERATORProcessor());
		add(new AqpCLAUSEProcessor());
		add(new AqpTMODIFIERProcessor());
		add(new AqpBOOSTProcessor());
		add(new AqpFUZZYProcessor());
	
		add(new AqpQRANGEINProcessor());
		add(new AqpQRANGEEXProcessor());
		add(new AqpQPHRASEProcessor());
		add(new AqpQNORMALProcessor());
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
