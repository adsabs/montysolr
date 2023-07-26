package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.queryparser.flexible.aqp.ADSEscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFunctionQueryBuilder;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsRegexQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsSynonymQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor.OriginalInput;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldableNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.SlopQueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.processors.AnalyzerQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.MultiFieldQueryNodeProcessor;

/**
 * This processor wraps fields with the 'null' value into edismax 
 * search. This is the solution for the unfielded search 
 * 
 * @see FieldableNode
 * @see MultiFieldQueryNodeProcessor
 * @see AnalyzerQueryNodeProcessor
 *
 */
public class AqpUnfieldedSearchProcessor extends AqpQueryNodeProcessorImpl implements
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
	    List<String> local = new ArrayList<String>();
	    
	    String funcName = "edismax_combined_aqp"; //"edismax_always_aqp"; //"edismax_combined_aqp";
	    String subQuery = ((FieldQueryNode) node).getTextAsString();
	    
	    if (node instanceof AqpNonAnalyzedQueryNode) {
	      funcName = "edismax_nonanalyzed";
	    }
	    else {
	      //subQuery = (String) escaper.escape(subQuery, Locale.getDefault(), EscapeQuerySyntax.Type.NORMAL);
	      
	      if (node instanceof QuotedFieldQueryNode) {
	      	subQuery = "\"" + subQuery + "\"";
	      	//local.add("sow=false");
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
          local.add("aqp.exact.search=true ");
        }
	    else if (getConfigVal("aqp.maxPhraseLength", null) != null) {
	      subQuery = "{!adismax aqp.maxPhraseLength=" + getConfigVal("aqp.maxPhraseLength") + "}" + subQuery;
	    }
	    
	    List<OriginalInput> fValues = new ArrayList<OriginalInput>();
	    if (local.size() > 0) {
	      subQuery = "{!adismax " + String.join(" ", local).trim() + "}" + subQuery;
	    }
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
