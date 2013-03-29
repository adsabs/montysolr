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
import org.apache.lucene.queryparser.flexible.standard.processors.LowercaseExpandedTermsQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.MatchAllDocsQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.MultiFieldQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.MultiTermRewriteMethodProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.NumericQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.NumericRangeQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.PhraseSlopQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.RemoveEmptyNonLeafQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.TermRangeQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.WildcardQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsAnalyzerProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsAuthorPreProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsCOMMAProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsCarefulAnalyzerProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsExpandAuthorSearchProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsFieldNodePreAnalysisProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsFixQPOSITIONProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsMODIFIERProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsQTRUNCATEDProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsFieldMapperProcessorPostAnalysis;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpDEFOPMarkPlainNodes;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpFixMultiphraseQuery;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQREGEXProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpUnfieldedSearchProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsQNORMALProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsQPOSITIONProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsRegexNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsSynonymNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAnalysisQueryNodeProcessor;
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
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpMultiWordProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpNullDefaultFieldProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpOPERATORProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpOptimizationProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQANYTHINGProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQDATEProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQFUNCProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQIDENTIFIERProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQPHRASEProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQPHRASETRUNCProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQRANGEINProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQTRUNCATEDProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpTMODIFIERProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpTreeRewriteProcessor;


public class AqpAdsabsNodeProcessorPipeline extends QueryNodeProcessorPipeline {

	public AqpAdsabsNodeProcessorPipeline(QueryConfigHandler queryConfig) {
		super(queryConfig);
		
		QueryConfigHandler config = getQueryConfigHandler();
	
		add(new AqpDEFOPMarkPlainNodes());
		add(new AqpDEFOPProcessor());
		add(new AqpTreeRewriteProcessor());
		
		add(new AqpAdsabsFixQPOSITIONProcessor()); // handles QPHRASE:"^some phrase$" and QNORMAL:word$
		add(new AqpAdsabsQPOSITIONProcessor()); // rewrites ^author$ into a functional form
		add(new AqpQFUNCProcessor()); // prepares function node (may decide which implementation to call)
		
		add(new AqpAdsabsCOMMAProcessor()); // extends operators with COMMA and SEMICOLON
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
		add(new AqpQREGEXProcessor());
		add(new AqpAdsabsQNORMALProcessor()); // keeps the tag information
		add(new AqpQPHRASETRUNCProcessor());
		add(new AqpAdsabsQTRUNCATEDProcessor());
		add(new AqpQANYTHINGProcessor());
		add(new AqpQIDENTIFIERProcessor());
		add(new AqpFIELDProcessor());
		add(new AqpBibcodeProcessor()); // finds bibcode and converts to AqpAdslabsIdentifier		
	
		add(new AqpFuzzyModifierProcessor());
		add(new WildcardQueryNodeProcessor());
		
		
	  add(new MultiFieldQueryNodeProcessor()); // expands to multiple fields if field=null
	    
		
		add(new AqpNullDefaultFieldProcessor());
		add(new FuzzyQueryNodeProcessor());
		add(new MatchAllDocsQueryNodeProcessor());
		
		
		// Analysis block: here we use the Solr/Lucene analyzers
		add(new AqpAdsabsFieldNodePreAnalysisProcessor()); // ADS specific modification of the tree before the analysis
		
		add(new AqpFieldMapperProcessor()); // translate the field name before we try to find the tokenizer chain
		add(new AqpMultiWordProcessor()); // find synonyms if we have 'plain word token group'
		
		if (config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_READY) == true) {
      add(new AqpUnfieldedSearchProcessor()); // use edismax to wrap unfielded searches
    }
		
		add(new NumericQueryNodeProcessor());
    add(new NumericRangeQueryNodeProcessor());
		add(new TermRangeQueryNodeProcessor());
		add(new AqpAdsabsRegexNodeProcessor()); // wraps regex QN w/ NonAnalyzedQueryNode
		add(new AqpAdsabsSynonymNodeProcessor()); //XXX: to remove? -- simply wraps into non-analyzed node 
		
		
		add(new AqpAdsabsAuthorPreProcessor()); // must happen before analysis
		add(new AqpAdsabsAnalyzerProcessor()); // the main analysis happens here (but not for wildcard nodes and co)
		add(new AqpFixMultiphraseQuery()); // "(word | synonym) phrase query" -> "word phrase query" | synonym 
		add(new AqpLowercaseExpandedTermsQueryNodeProcessor()); // lowercase ASTRO* -> astro* (we index everything lowercase)
		add(new AqpAdsabsCarefulAnalyzerProcessor()); //XXX should we remove LowercaseExpandedTermsQueryNodeProcessor? -- massages wildcard, regex, fuzzy fields 
		add(new AqpAdsabsExpandAuthorSearchProcessor()); // kurtz, michael +> "kurtz, michael *" and stuff... 
				
		add(new AqpAdsabsFieldMapperProcessorPostAnalysis()); // translate the field name into their final name
		
		add(new PhraseSlopQueryNodeProcessor());
		add(new AllowLeadingWildcardProcessor());
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
