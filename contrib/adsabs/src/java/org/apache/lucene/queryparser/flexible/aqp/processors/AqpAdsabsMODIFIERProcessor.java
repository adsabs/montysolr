package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpSolrFunctionProvider;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsSynonymQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpMODIFIERProcessor;
import org.apache.solr.common.params.SolrParams;

/**
 * When QNode is prefixed with '='/'#' we wrap it into
 * {@link AqpAdsabsSynonymQueryNode} and then further down
 * the line, the processing will be different. Look at
 * {@link AqpAdsabsSynonymNodeProcessor} for an idea
 * 
 * @author rchyla
 *
 */

public class AqpAdsabsMODIFIERProcessor extends AqpMODIFIERProcessor {
	
	@Override
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		
		if (node.getChildren().size() == 1) {
			return node.getChildren().get(0);
		}
		
		AqpANTLRNode subNode = getModifierNode(node);
		
		String sign = subNode.getTokenLabel();
		
		if (sign.equals("=")) {
			return new AqpAdsabsSynonymQueryNode(getValueNode(node), false);
		}
		else if (sign.equals("#")) {
			return new AqpAdsabsSynonymQueryNode(getValueNode(node), true);
		}
		else {
			return super.createQNode(node);
		}

	}
	
}
