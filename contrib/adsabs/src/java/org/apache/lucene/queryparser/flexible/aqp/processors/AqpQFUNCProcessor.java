package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.CharStream;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFunctionQueryBuilder;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFunctionQueryBuilderConfig;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;

/**
 * Processing of functional queries may be more involved than the standard
 * queries. The functions calls can be nested.
 * 
 * Also each functional processor can be different, therefore my decision
 * was to register individual function builders inside the config
 * @see AqpFunctionQueryBuilderConfig
 * and then insert this builder into the QNode. It will wait there
 * until it is picked by the {@link AqpFunctionQueryBuilder} which 
 * will decide how it wants to parse the query (ie. it may just want
 * the literals, or it will request a full query object to be built, etc)
 * 
 * You should check each {@link AqpFunctionQueryBuilder}
 * for details on how are data processed.
 * 
 */
public class AqpQFUNCProcessor extends AqpQProcessor {
	
	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("QFUNC")) {
			return true;
		}
		return false;
	}
	
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		List<QueryNode> children = node.getChildren();
		
		AqpANTLRNode funcHead = ((AqpANTLRNode) children.get(0));
		
		String funcName = funcHead.getTokenInput();
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
		
		if (((AqpANTLRNode) children.get(children.size()-1)).getTokenName().equals("RPAREN")) {
			CharStream inputStream = AqpQProcessor.getInputStream(node);
			AqpANTLRNode funcTail = (AqpANTLRNode) children.get(children.size()-1);
			OriginalInput originalInput = new OriginalInput(inputStream.substring(funcHead.getInputTokenEnd(), funcTail.getInputTokenStart()), 
					funcHead.getInputTokenStart(),
					funcHead.getInputTokenEnd());
			
			ArrayList<OriginalInput> values = new ArrayList<OriginalInput>();
			int start = funcHead.getInputTokenEnd()+1;
			int stop = start;
			
			for (QueryNode n: children.get(1).getChildren()) {
				AqpANTLRNode a = (AqpANTLRNode) n;
  			int l = a.hasTokenName("QDELIMITER", 0);
  			if (l > 0 && l < 4) { // MODIFIER/TMODIFIER/FIELD/QDELIMITER
  				AqpANTLRNode delimiter = (AqpANTLRNode) AqpQProcessor.getTerminalNode(a);
  				stop = delimiter.getInputTokenStart()-1;
  				values.add(new OriginalInput(inputStream.substring(start, stop), start, stop));
  				start = delimiter.getInputTokenEnd()+1;
  			}
			}
			stop = funcTail.getInputTokenEnd()-1;
			values.add(new OriginalInput(inputStream.substring(start, stop), start, stop));
			return new AqpFunctionQueryNode(funcName, builder, originalInput, values);
		}
		else { // the old semantics when we try hard to discover elements of the function
			return new AqpFunctionQueryNode(funcName, builder, (AqpANTLRNode) children.get(1));
		}
		
	}


}
