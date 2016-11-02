package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.lucene.queryparser.flexible.aqp.ADSEscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFunctionQueryBuilder;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsRegexQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsSynonymQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor.OriginalInput;
import org.apache.lucene.queryparser.flexible.aqp.util.AqpCommonTree;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.BoostQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldableNode;
import org.apache.lucene.queryparser.flexible.core.nodes.OpaqueQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.PathQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.SlopQueryNode;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.processors.AnalyzerQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.MatchAllDocsQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.MultiFieldQueryNodeProcessor;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;

/**
 * This processor wraps fields with the 'null' value into edismax 
 * search. This is the solution for the unfielded search 
 * 
 * @see FieldableNode
 * @see MultiFieldQueryNodeProcessor
 * @see AnalyzerQueryNodeProcessor
 *
 */
public class AqpUnfieldedSearchProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {
  
  ADSEscapeQuerySyntaxImpl escaper = new ADSEscapeQuerySyntaxImpl();
  
	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {

	  if (node instanceof FieldQueryNode && !(node instanceof AqpAdsabsRegexQueryNode)) {
	    
		  QueryConfigHandler config = getQueryConfigHandler();
		  
		  if (!((FieldQueryNode) node).getField().equals(
		      config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.UNFIELDED_SEARCH_FIELD))) {
		    return node;
		  }
	    
	    if (!config.has(AqpAdsabsQueryConfigHandler.ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG)) {
	      throw new QueryNodeException(new MessageImpl(
	          "Invalid configuration",
	          "Missing FunctionQueryBuilder provider"));
	    }
	    
	    String funcName = "edismax_combined_aqp"; //"edismax_always_aqp"; //"edismax_combined_aqp";
	    String subQuery = ((FieldQueryNode) node).getTextAsString();
	    
	    if (node instanceof AqpNonAnalyzedQueryNode) {
	      funcName = "edismax_nonanalyzed";
	    }
	    else {
	      //subQuery = (String) escaper.escape(subQuery, Locale.getDefault(), EscapeQuerySyntax.Type.NORMAL);
	      
	      if (node instanceof QuotedFieldQueryNode) {
	      	subQuery = "\"" + subQuery + "\"";
	      }
	      if (node.getParent() instanceof SlopQueryNode) {
	      	subQuery = subQuery + "~" + ((SlopQueryNode) node.getParent()).getValue();
	      	//if (node.getParent().getParent() instanceof BoostQueryNode) {
		      //	subQuery = subQuery + "^" + ((BoostQueryNode) node.getParent().getParent()).getValue();
		      //}
	      }
	      /*else if (node.getParent() instanceof BoostQueryNode) {
	      	//subQuery = subQuery + "^" + ((BoostQueryNode) node.getParent()).getValue();
	      	if (node.getParent().getParent() != null) {
	      	  QueryNode root = node.getParent().getParent();
	      	  List<QueryNode> children = root.getChildren();
	      	  children.clear();
	      	  children.add(node);
	      	  root.set(children);
	      	}
	      	
	      }*/
	    }
	    node.setTag("subQuery", subQuery);
	    
	    AqpFunctionQueryBuilder builder = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG)
	                    .getBuilder(funcName, (QueryNode) node, config);
	    
	    if (builder == null) {
	      throw new QueryNodeException(new MessageImpl(QueryParserMessages.INVALID_SYNTAX,
	          "Unknown function \"" + funcName +"\"" ));
	    }
	    
	    // let adismax know that we want exact search
	    if (node.getTag("aqp.exact") != null || 
	        (node.getParent() instanceof AqpAdsabsSynonymQueryNode && ((AqpAdsabsSynonymQueryNode) node.getParent()).isActivated() == false)) {
          subQuery = "{!adismax aqp.exact.search=true}" + subQuery;
        }
	    
	    List<OriginalInput> fValues = new ArrayList<OriginalInput>();
	    fValues.add(new OriginalInput(subQuery, -1, -1));
	    return new AqpFunctionQueryNode(funcName, builder, fValues);
		}
		return node;
	}
	
	
	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpAdsabsSynonymQueryNode) {
		  applyTagToAllChildren(((AqpAdsabsSynonymQueryNode) node).getChild());
		}
		return node;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}
	
	private void applyTagToAllChildren(QueryNode node) {

    if (node instanceof FieldQueryNode) {
      node.setTag("aqp.exact", true);
    }
    if (node.getChildren() != null) {
      for (QueryNode child : node.getChildren()) {
        applyTagToAllChildren(child);
      }
    }
  }
	
}
