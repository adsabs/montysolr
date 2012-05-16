package org.apache.lucene.queryParser.aqp.builders;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryParser.core.nodes.OpaqueQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.search.Query;
import org.apache.solr.search.AqpFunctionQParser;
import org.apache.solr.search.ValueSourceParser;
import org.apache.solr.search.function.FunctionQuery;
import org.apache.lucene.queryParser.ParseException;

public class AqpFunctionQueryTreeBuilder extends QueryTreeBuilder 
	implements AqpFunctionQueryBuilder {
	
	private ValueSourceParser vs;
	private AqpFunctionQParser fp;
	
	public AqpFunctionQueryTreeBuilder(ValueSourceParser provider,
			AqpFunctionQParser fParser) {
		vs = provider;
		fp = fParser;
	}

	public Query build(QueryNode node) throws QueryNodeException {
		try {
			return new FunctionQuery(getValueSourceParser().parse(getParser(node)));
		} catch (ParseException e) {
			throw new QueryNodeException(new MessageImpl(e.getLocalizedMessage()));
		}
	}
	
	
	public static void flattenChildren(QueryNode node) throws QueryNodeException {
		
		// we know that for solr functions, the literal values are enough (so we can safely replace
		// nodes with their literal values - just the nested functions will remain as functions)
		
		QueryNode valueNode = node.getChildren().get(1);
		
		try {
			List<String> inputVals = AqpFunctionQueryTreeBuilder.harvestInput(valueNode);
			List<QueryNode> children = valueNode.getChildren();
			
			for (int i=0; i<inputVals.size();i++) {
				if (!inputVals.get(i).equals("#QFUNC")) {
					children.set(i, new OpaqueQueryNode("child#" + i, inputVals.get(i)));
				}
			}
			}
		catch (Exception e) {
			throw new QueryNodeException(e);
		}
		
		//valueNode.set(children);
	}
	
	
	/**
	 * get the raw input from the children, we do not go 
	 * into nested QFUNCs, that is intentional,
	 * we see only the immediate level
	 */
	public static List<String> harvestInput(QueryNode node) {
		List<String> rawInput = new ArrayList<String>();
		swimDeep(rawInput, node);
		return rawInput;
	}

	public static void swimDeep(List<String> rawInput, QueryNode node) {
		if (node instanceof AqpANTLRNode) {
			AqpANTLRNode a = (AqpANTLRNode) node;
			if (a.getTokenInput() != null) {
				try {
					rawInput.add(
						EscapeQuerySyntaxImpl.discardEscapeChar(a.getTokenInput()).toString()
								);
				} catch (Exception e) {
					rawInput.add(a.getTokenInput());
				}
			}
		}
		else if (node instanceof AqpFunctionQueryNode) {
			rawInput.add("#QFUNC");
			return;
		}
		if (!node.isLeaf()) {
			for (QueryNode child: node.getChildren()) {
				swimDeep(rawInput, child);
			}
		}
		
	}

	public AqpFunctionQParser getParser(QueryNode node) {
		fp.setQueryNode(node);
		return fp;
	}


	public ValueSourceParser getValueSourceParser() {
		return vs;
	}


}
