package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.builders.AqpFunctionQueryBuilder;
import org.apache.lucene.queryParser.aqp.config.AqpFunctionQueryBuilderConfig;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.QueryNode;

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
		
		if (!config.hasAttribute(AqpFunctionQueryBuilderConfig.class)) {
			throw new QueryNodeException(new MessageImpl(
					"Invalid configuration",
					"Missing FunctionQueryBuilder provider"));
		}
		
		AqpFunctionQueryBuilder builder = config.getAttribute(AqpFunctionQueryBuilderConfig.class)
										.getBuilder(funcName, node);
		
		if (builder == null) {
			throw new QueryNodeException(new MessageImpl(QueryParserMessages.INVALID_SYNTAX,
					"Unknown function \"" + funcName + "\"" ));
		}
		
		return builder.createQNode(children.get(1));
		
	}


}
