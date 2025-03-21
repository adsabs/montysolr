package org.apache.lucene.queryparser.flexible.aqp;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.processors.*;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.processors.NoChildOptimizationQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.core.processors.RemoveDeletedQueryNodesProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.*;
import org.apache.solr.search.AqpAdsabsQParser;

/**
 * This is the MAIN PIPELINE - it sets out how to build query,
 * deal with exceptions and stuff for ADS language
 *
 * @see AqpAdsabsQueryConfigHandler
 * @see AqpAdsabsQueryTreeBuilder
 * @see AqpAdsabsQParser
 */
public class AqpAdsabsNodeProcessorPipeline extends QueryNodeProcessorPipeline {

    public AqpAdsabsNodeProcessorPipeline(QueryConfigHandler queryConfig) {
        super(queryConfig);

        QueryConfigHandler config = getQueryConfigHandler();

        // function queries are handled first, because they will be parsed
        // again (during resolution)
        add(new AqpAdsabsFixQPOSITIONProcessor()); // handles QPHRASE:"^some phrase$" and QNORMAL:word$
        add(new AqpFirstAuthorMappingProcessor()); // remap first author queries to the first_author field
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
        // and this is the best so far, it can nicely extend the query
        // and is configurable through url params
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

        // expands virtual fields into real fields, that can be analyzed
        // normal ways (it also adds boosts, if necessary)
        add(new AqpVirtualFieldsQueryNodeProcessor());

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
        if (config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_READY)) {

            // take the 'unfielded search' values and wrap them into edismax('xxxxx') call
            // it will be executed/built in the 'build' phase (after processors finished)
            add(new AqpUnfieldedSearchProcessor());

            // this takes the unfielded search (multi-token group)
            add(new AqpWhiteSpacedQueryNodeProcessor());

            // TEMPORARY solution for the unfielded multi-token searches, edismax
            // does not know how to handle properly token expansions spanning several
            // positions; so we extract these and add them next to the edismax generated query
            // 28/10/13 - I've finally modified edismax (to call our aqp) when building
            // a query, so this is being taken care off by us
            // 02/04/2018 - leaving the component here as it might be useful for *some*
            // situations; but deactivating it
            // 02/04/2019 - activating it again, as I spent 1-2 hours trying to figure out
            // why i had a mistake in unittests :) - will only be used when
            // aqp.unfielded.phrase.edismax.synonym.workaround=true
            add(new AqpAdsabsExtractMultisynonymsProcessor());

        }

        add(new PointQueryNodeProcessor());
        add(new PointRangeQueryNodeProcessor());
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


        // normalize values
        // a special case are non-analyzed nodes - these are left =UnTouchEd
        add(new AqpNormalizeFieldQueryNodeProcessor());
        // add(new AqpWildcardQueryNodeProcessor());

        add(new RegexpQueryNodeProcessor());


        // author search: 'kurtz, michael' is expanded with "kurtz, michael *" ...
        // ADS has a 'very special' requirement for expanding the author search
        // this expansion cannot be solved inside the analysis chain (because
        // it depends on the context [knowing the original input]), so it is here
        add(new AqpAdsabsExpandAuthorSearchProcessor());


        // translate the field names back into their index-name variants
        add(new AqpAdsabsFieldMapperProcessorPostAnalysis());


        // deals with the the-same-position tokens:
        // "(word | synonym) phrase query" becomes "word phrase query" | synonym
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
        // +(+this +that) +what' becomes '+this +that +what'
        add(new AqpGroupQueryOptimizerProcessor());
        add(new AqpOptimizationProcessor());


        /**
         * Allow us to apply different scoring methods to particular
         * combination of query types and fields
         */

        add(new AqpChangeRewriteMethodProcessor());
    }

}
