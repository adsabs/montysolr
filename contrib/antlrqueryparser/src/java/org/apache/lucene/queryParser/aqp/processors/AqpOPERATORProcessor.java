package org.apache.lucene.queryParser.aqp.processors;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.config.DefaultProximityAttribute;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpAndQueryNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpNearQueryNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpNotQueryNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpOrQueryNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.AndQueryNode;
import org.apache.lucene.queryParser.core.nodes.OrQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;

/**
 * This processor operates on OPERATOR nodes, these are the nodes with labels
 * AND, OR, NOT, DEFOP, NEAR and possibly others. <br/>
 * I have decided to create special QueryNodes for each type of the operator,
 * because the default implementation ({@link AndQueryNode}, {@link OrQueryNode}
 * ) was confusing.
 * 
 * <pre>
 *   AND
 *   	- creates {@link AqpAndQueryNode}
 *   OR
 *   	- creates {@link AqpOrQueryNode}
 *   NOT
 *   	- creates {@link AqpNotQueryNode}
 *   NEAR
 *   	- creates {@link AqpNearQueryNode}
 *   WITH
 *   	- not implemented yet
 *   PARAGRAPH
 *   	- not implemented yet
 * </pre>
 * 
 * This processor should run after {@link AqpDEFOPProcessor}, and also after
 * {@link AqpMODIFIERProcessor} because modifiers should have precedence over
 * operators. Like in the query: "this OR +that"
 * 
 */
public class AqpOPERATORProcessor extends AqpQProcessorPost {

	@Override
	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenName().equals("OPERATOR")) {
			return true;
		}
		return false;
	}

	@Override
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		
		String label = node.getTokenLabel();
		
		if (label.equals("AND")) {
			return new AqpAndQueryNode(node.getChildren());
		} else if (label.equals("OR")) {
			return new AqpOrQueryNode(node.getChildren());
		} else if (label.equals("NOT")) {
			return new AqpNotQueryNode(node.getChildren());
		} else if (label.contains("NEAR")) {
			String[] parts = label.split(":");
			if (parts.length > 1) {
				return new AqpNearQueryNode(node.getChildren(),
						Integer.valueOf(parts[1]));
			} else {
				return new AqpNearQueryNode(node.getChildren(),
						getDefaultProximityValue());
			}

		} else {
			throw new QueryNodeException(new MessageImpl(
					QueryParserMessages.INVALID_SYNTAX, "Unknown operator "
							+ label));
		}

	}

	private Integer getDefaultProximityValue() throws QueryNodeException {
		QueryConfigHandler queryConfig = getQueryConfigHandler();
		if (queryConfig == null
				|| !queryConfig.hasAttribute(DefaultProximityAttribute.class)) {
			throw new QueryNodeException(new MessageImpl(
					QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
					"Configuration error: "
							+ DefaultProximityAttribute.class.toString()
							+ " is missing"));
		}
		return queryConfig.getAttribute(DefaultProximityAttribute.class)
				.getDefaultProximity();
	}

}
