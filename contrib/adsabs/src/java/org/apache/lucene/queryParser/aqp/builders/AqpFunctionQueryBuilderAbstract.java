package org.apache.lucene.queryParser.aqp.builders;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.builders.QueryBuilder;
import org.apache.lucene.queryParser.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.queryParser.standard.parser.ParseException;

public abstract class AqpFunctionQueryBuilderAbstract extends QueryTreeBuilder 
	implements AqpFunctionQueryBuilder {
	
	/**
	 * Creates the query node that will be passed to the builder.
	 *  
	 * @param node
	 * @return
	 */
	public AqpFunctionQueryNode createQNode(QueryNode node)
		throws QueryNodeException {
		throw new IllegalStateException("So late!");
	}
	
	/**
	 * get the raw input from the children, we do not go 
	 * into nested QFUNCs, that is intentional,
	 * we see only the immediate level
	 */
	protected List<String> harvestInput(QueryNode node) {
		List<String> rawInput = new ArrayList<String>();
		swimDeep(rawInput, node);
		return rawInput;
	}

	private void swimDeep(List<String> rawInput, QueryNode node) {
		if (node instanceof AqpANTLRNode) {
			AqpANTLRNode a = (AqpANTLRNode) node;
			if (a.getTokenInput() != null) {
				try {
					rawInput.add(
						EscapeQuerySyntaxImpl.discardEscapeChar(a.getTokenInput()).toString()
								);
				} catch (ParseException e) {
					rawInput.add(a.getTokenInput());
				}
			}
		}
		else if (node instanceof AqpFunctionQueryNode) {
			rawInput.add("QFUNC");
			return;
		}
		if (!node.isLeaf()) {
			for (QueryNode child: node.getChildren()) {
				swimDeep(rawInput, child);
			}
		}
		
	}
}
