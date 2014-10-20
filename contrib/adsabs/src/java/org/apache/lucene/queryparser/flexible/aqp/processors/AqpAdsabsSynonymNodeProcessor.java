package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsSynonymQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.solr.request.SolrQueryRequest;

/**
 * If the node is {@link AqpAdsabsSynonymQueryNode} we'll check
 * whether to analyze it. If yes, it will be passed to the normal
 * analyzer for that given field. If not, then it will be processed
 * by analyzers of our choice
 * 
 * @author rchyla
 *
 */
public class AqpAdsabsSynonymNodeProcessor extends QueryNodeProcessorImpl implements
	QueryNodeProcessor  {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpAdsabsSynonymQueryNode) {
			AqpAdsabsSynonymQueryNode synNode = (AqpAdsabsSynonymQueryNode) node;
			if (synNode.isActivated()) { 
				return expandSynonyms(synNode);
				
			}
			else {
				
				QueryNode child = synNode.getChild();
				if (child instanceof FieldQueryNode) {
   				// we may be in situation that this node had a modifier (= or #)
					// which only modifies the analysis, but doesn't turn it off
					
					String field = ((FieldQueryNode) child).getFieldAsString() + "_nosyn";
			    if (hasAnalyzer(field)) {
			    	((FieldQueryNode) child).setField(field); // change the field to use a different analyzer...
			    	return child;
			    }
				}
			  
		    
				return applyNonAnalyzableToAllChildren(synNode.getChild());
			}
		}
		return node;
	}

	protected QueryNode applyNonAnalyzableToAllChildren(QueryNode node) {
		
		if (node instanceof AqpNonAnalyzedQueryNode) {
			return node;
		}
		else if (node instanceof FieldQueryNode) {
			return new AqpNonAnalyzedQueryNode((FieldQueryNode) node); 
		}
		
		List<QueryNode> children = node.getChildren();
		
		if (children!=null) {
			
			for (int i=0; i<children.size();i++) {
				children.set(i, applyNonAnalyzableToAllChildren(children.get(i)));
			}
		}
		return node;
		
	}

	protected QueryNode expandSynonyms(AqpAdsabsSynonymQueryNode synNode) {
		// I believe it is the job of the analyzers to expand the node, but it may depend...
		return synNode.getChild();
	}
	
	// TODO: consider merging with AqpAdsabsCarefulAnalyzerProcessor
	// where this code was copied from
	private boolean hasAnalyzer(String fieldName) {
	  SolrQueryRequest req = this.getQueryConfigHandler()
	  .get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
    .getRequest();
	  if (req != null && req.getSchema().hasExplicitField(fieldName)) {
	    return true;
	  }
	  return false;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}


}
