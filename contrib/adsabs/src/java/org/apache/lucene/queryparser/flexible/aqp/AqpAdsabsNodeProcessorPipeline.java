package org.apache.lucene.queryparser.flexible.aqp;

import java.util.Arrays;

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
import org.apache.lucene.queryparser.flexible.standard.processors.NumericQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.NumericRangeQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.PhraseSlopQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.RemoveEmptyNonLeafQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.TermRangeQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.WildcardQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsAnalyzerProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsAuthorPreProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsCarefulAnalyzerProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsExpandAuthorSearchProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsExtractMultisynonymsProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsFieldNodePreAnalysisProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsFixQPOSITIONProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsMODIFIERProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsQDELIMITERProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsQTRUNCATEDProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsFieldMapperProcessorPostAnalysis;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpDEFOPMarkPlainNodes;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpDEFOPUnfieldedTokens;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpPostAnalysisProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQREGEXProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpUnfieldedSearchProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsQNORMALProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsQPOSITIONProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsRegexNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpAdsabsSynonymNodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpBOOSTProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpBibcodeProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpCLAUSEProcessor;
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
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQPHRASEProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQPHRASETRUNCProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQRANGEINProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpTMODIFIERProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpTreeRewriteProcessor;
import org.apache.solr.search.AqpAdsabsQParser;

/**
 * This is the MAIN PIPELINE - it sets out how to build query,
 * deal with exceptions and stuff for ADS language
 * 
 * @see AqpAdsabsQueryConfigHandler
 * @see AqpAdsabsQueryTreeBuilder
 * @see AqpAdsabsQParser
 *
 */
public class AqpAdsabsNodeProcessorPipeline extends QueryNodeProcessorPipeline {

	public AqpAdsabsNodeProcessorPipeline(QueryConfigHandler queryConfig) {
		super(queryConfig);
		
		QueryConfigHandler config = getQueryConfigHandler();
		
		// function queries are handled first, because they will be parsed
		// again (during resolution)
		add(new AqpAdsabsFixQPOSITIONProcessor()); // handles QPHRASE:"^some phrase$" and QNORMAL:word$
		add(new AqpAdsabsQPOSITIONProcessor()); // rewrites ^author$ into a functional form
		add(new AqpQFUNCProcessor()); // prepares function node (may decide which implementation to call)
		
	
		// find separate tokens and join them into one 'string'
		// true=modify the parse tree, otherwise values are 'added'
		// into the first token
	  // this was the original behaviour, you can still activate:
		// add(new AqpDEFOPMarkPlainNodes());
		// this was the 2nd strategy
		// add(new AqpDEFOPMarkPlainNodes(true, Arrays.asList("+", "-"),
		//		Arrays.asList("author", "first_author")));
		
		add(new AqpDEFOPUnfieldedTokens());
		

		/**
		 * Most of the processors immediately below can be seen as
		 * 'transducers' from ANTLR to QueryNode. Because ANTLR is
		 * producing Abstract Syntax Trees (AST). You can view these
		 * processors as a layer between 'pure syntax parsing' (ANTLR)
		 * and query building. If you need to check for 'semantics'
		 * (e.g. invalid combinations, for example: this NEAR5 wildca*)
		 * this is the right place to do it
		 */
		
		add(new AqpDEFOPProcessor()); // sets DEFOP to be AND|OR....
		add(new AqpTreeRewriteProcessor()); // makes (AND(AND(AND... to be (AND...
		
		
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
		add(new AqpAdsabsQNORMALProcessor()); // keeps the tag information (AqpDEFOPMarkPlainNodes)
		add(new AqpQPHRASETRUNCProcessor());
		add(new AqpAdsabsQTRUNCATEDProcessor());
		add(new AqpQANYTHINGProcessor());
		add(new AqpQIDENTIFIERProcessor());
		add(new AqpFIELDProcessor()); // sets the field name (if user specified one, or there is a default)
		
		
		/**
		 * After this point, the AST tree usually does not contain ANTLR
		 * specific nodes, but it is made entirely from the transformed
		 * QueryNode(s). The tree is also much simpler and 'flatter' 
		 */
		
		
		add(new AqpBibcodeProcessor()); // finds bibcode and converts to AqpAdslabsIdentifier		
		add(new AqpFuzzyModifierProcessor());
		
		//this
		//add(new WildcardQueryNodeProcessor());
		
		
	  // expands to multiple fields if field=null (ie. unfielded search)
		// however, this solution is the old-style lucene mechanism which 
		// was superseded by edismax() function calls; it works when
		// ConfigurationKeys.MULTI_FIELDS are not empty. It is kept here 
		// because of unittest compatibility
	  add(new MultiFieldQueryNodeProcessor()); 
		add(new AqpNullDefaultFieldProcessor());
		
		
		add(new FuzzyQueryNodeProcessor());
		add(new MatchAllDocsQueryNodeProcessor());
		
		
		
		
		/**
		 * Analysis block: here we use the Solr/Lucene analyzers
		 * This is the most complex part of the analysis chain
		 * it can generate synonyms, multi-token synonyms,
		 * call functions() etc...we are changing AST tree and 
		 * doing some other acrobatics
		 */
		
	  // ADS specific modification of the tree before the analysis
		// helps with syntactic sugar 
		add(new AqpAdsabsFieldNodePreAnalysisProcessor()); 
		
	  // translate the field name before we try to find the tokenizer chain
		// useful when you want to use particular analyzer for several fields
		// but you don't want to define these fields as separate indexes
		add(new AqpFieldMapperProcessor()); 
		
		// was the old-time behaviour, before the AqpDEFOPMarkPlainNodes() was
		// modifying the AST; now this is obsolete, we solve it elsewhere
	  // find synonyms if we have 'plain word token group', this processor
		// add(new AqpMultiWordProcessor()); 
		
		
		// this block applies only when query parser is ran inside SOLR
		if (config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_READY) == true) {
			
			// take the 'unfielded search' values and wrap them into edismax('xxxxx') call
			// it will be executed/built in the 'build' phase (after processors finished)
      add(new AqpUnfieldedSearchProcessor());
      
      // TEMPORARY solution for the unfielded multi-token searches, edismax
      // does not know how to handle properly token expansions spanning several
      // positions; so we extract these and add them next to the edismax generated query
      add(new AqpAdsabsExtractMultisynonymsProcessor());
      
    }
		
		add(new NumericQueryNodeProcessor());
    add(new NumericRangeQueryNodeProcessor());
		add(new TermRangeQueryNodeProcessor());
		add(new AqpAdsabsRegexNodeProcessor()); // wraps regex QN w/ NonAnalyzedQueryNode
		
    // simply wraps into non-analyzed node; possibly to remove because we now use 
		// AqpAdsabsCarefulAnalyzerProcessor below
		add(new AqpAdsabsSynonymNodeProcessor());  
		

	  // ADS is doing lots of things with regard to parsing author searches
		// here we clean up author searches before analysis stage
		add(new AqpAdsabsAuthorPreProcessor()); 
		
		
  	// the main analysis happens here (but not for wildcard nodes and similar
		// non-analyzed nodes), these will stay untouched
		add(new AqpAdsabsAnalyzerProcessor()); 
		
		
		// here we analyze input that was not analyzed by the previous step
		// this applies mostly to wildcard, regex, fuzzy searches; the ADS 
		// convention is that if a '<field>_wildcard' will be used to analyze
		// and create Wildcard node etc
		add(new AqpAdsabsCarefulAnalyzerProcessor());
		
	  
		// lowercase everything else which wasn't caught by the previous steps
		// a special case are non-analyzed nodes - these are left =UnTouchEd
		add(new AqpLowercaseExpandedTermsQueryNodeProcessor());

		
		// author search: 'kurtz, michael' is expanded with "kurtz, michael *" ...
	  // ADS has a 'very special' requirement for expanding the author search
		// this expansion cannot be solved inside the analysis chain (because
		// it depends on the context [knowing the original input]), so it is 
		add(new AqpAdsabsExpandAuthorSearchProcessor()); 
		
		
    // translate the field names back into their index-name variants
		add(new AqpAdsabsFieldMapperProcessorPostAnalysis()); 
		
		
	  // deals with the the-same-position tokens: 
		// "(word | synonym) phrase query" -> "word phrase query" | synonym
		add(new AqpPostAnalysisProcessor()); 
		
		
		/**
		 * Analysis-phase is over, these are the standard flex guys
		 * massaging remaining query elements
		 */
		add(new AqpAdsabsQDELIMITERProcessor());
		add(new PhraseSlopQueryNodeProcessor());
		add(new AllowLeadingWildcardProcessor());
		// add(new GroupQueryNodeProcessor()); // this removes the boolean opearator precedence
		add(new NoChildOptimizationQueryNodeProcessor());
		add(new RemoveDeletedQueryNodesProcessor());
		add(new RemoveEmptyNonLeafQueryNodeProcessor());
		add(new BooleanSingleChildOptimizationQueryNodeProcessor());
		add(new DefaultPhraseSlopQueryNodeProcessor());
		add(new BoostQueryNodeProcessor());
		add(new MultiTermRewriteMethodProcessor());
	
		
		// purely aestetical, final touches to the query
		// we are rewriting the tree, for example:
		// +(this that) +what' becomes '+this +that +what'
		add(new AqpGroupQueryOptimizerProcessor());
		add(new AqpOptimizationProcessor());
		
		
	}

}
