package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFunctionQueryBuilder;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFunctionQueryBuilderConfig;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessorPost;

/**
 * Processing of functional queries may be more involved than the standard
 * queries. The functions calls can be nested and often we may want to
 * wait until the values are prepared by other processors, before we grab 
 * them and use inside the query.
 * 
 * Also each functional processor can be different, therefore my decision
 * was to register individual function builders inside the config
 * @see AqpFunctionQueryBuilderConfig#getBuilder(String, QueryNode)
 * and then insert this builder into the QNode. It will wait there
 * until it is picked by the {@link AqpFunctionQueryBuilder} which 
 * will call it at the end.
 * 
 * You should check each {@link AqpFunctionQueryBuilder#createQNode(QueryNode)}
 * for details on how are data processed.
 * 
 * @author rchyla
 *
 */
public class AqpQFUNCProcessor extends AqpQProcessorPost {
	
	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("QFUNC")) {
			return true;
		}
		return false;
	}
	
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		List<QueryNode> children = node.getChildren();
		
		String funcName = ((AqpANTLRNode) children.get(0)).getTokenInput();
		if (funcName.endsWith("(")) {
			funcName = funcName.substring(0, funcName.length()-1);
		}
		
		QueryConfigHandler config = getQueryConfigHandler();
		
		if (!config.has(AqpAdsabsQueryConfigHandler.ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG)) {
			throw new QueryNodeException(new MessageImpl(
					"Invalid configuration",
					"Missing FunctionQueryBuilder provider"));
		}
		
		AqpFunctionQueryBuilder builder = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.FUNCTION_QUERY_BUILDER_CONFIG)
										.getBuilder(funcName, (QueryNode) node, config);
		
		if (builder == null) {
			throw new QueryNodeException(new MessageImpl(QueryParserMessages.INVALID_SYNTAX,
					"Unknown function \"" + funcName + "\"" ));
		}
		
	  // HACK-FUNC-to-refactor
   	// FIX: hack, the correct solution is to have QFUNC builder
  	// decide what values it wants, all this code must be simplified
  	// the AqpFunctionQueryNode should only contain the original
  	// user input and all the analysis is directed by the handler
  	if (node.getChildren().size() == 1 && node.getChild("COMMA") != null) {
  		return new AqpFunctionQueryNode(funcName, builder, node.getChild("COMMA"));
  	}
		
		return new AqpFunctionQueryNode(funcName, builder, node);
		
	}


}
