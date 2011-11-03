package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.config.DefaultProximityAttribute;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpAndQueryNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpNearQueryNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpNotQueryNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpOrQueryNode;
import org.apache.lucene.queryParser.aqp.util.AqpUtils.Operator;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.AndQueryNode;
import org.apache.lucene.queryParser.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryParser.core.nodes.OrQueryNode;
import org.apache.lucene.queryParser.core.nodes.ProximityQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryParser.standard.config.FuzzyAttribute;
import org.apache.lucene.queryParser.standard.nodes.BooleanModifierNode;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode.Modifier;

/**
 * This processor operates on OPERATOR nodes, these are the nodes with labels
 * AND, OR, NOT, DEFOP, NEAR and possibly others.
 * <br/>
 * I have decided to create special QueryNodes for each type of the operator,
 * because the default implementation ({@link AndQueryNode}, {@link OrQueryNode})
 * was confusing.
 * <pre>
 *   AND
 *   	- creates {@link AqpAndQueryNode}
 *   OR
 *   	- creates {@link AqpOrQueryNode}
 *   NOT
 *   	- creates {@link AqpNotQueryNode}
 *   WITH
 *   	- not implemented yet
 *   PARAGRAPH
 *   	- not implemented yet
 * </pre>
 *   
 * This processor should run after {@link AqpDEFOPProcessor}, but before 
 * {@link AqpMODIFIERProcessor} because modifiers should
 * have precedence over operators. Like in the query: "this OR +that" 
 *
 */
public class AqpOPERATORProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {
	

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}
	
	
	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		
		if (node instanceof AqpANTLRNode && ((AqpANTLRNode) node).getTokenName().equals("OPERATOR")) {
			AqpANTLRNode n = (AqpANTLRNode) node;
			
			if (n.getTokenLabel().equals("AND")) {
				return new AqpAndQueryNode(node.getChildren());
			}
			else if(n.getTokenLabel().equals("OR")) {
				return new AqpOrQueryNode(node.getChildren());
			}
			else if(n.getTokenLabel().equals("NOT")) {
				return new AqpNotQueryNode(node.getChildren());
			}
			else if(n.getTokenLabel().contains("NEAR")) {
				String[] parts = n.getTokenLabel().split(":");
				if (parts.length > 1) {
					return new AqpNearQueryNode(node.getChildren(), Integer.valueOf(parts[1]));
				}
				else {
					return new AqpNearQueryNode(node.getChildren(), getDefaultProximityValue());
				}
				
			}
			else {
				throw new QueryNodeException(new MessageImpl(
		                QueryParserMessages.INVALID_SYNTAX,
		                "Unknown operator " + n.getTokenLabel()));
			}
			
		}
		return node;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}
	
	
	
	private Integer getDefaultProximityValue() throws QueryNodeException {
		QueryConfigHandler queryConfig = getQueryConfigHandler();
		if (queryConfig == null || !queryConfig.hasAttribute(DefaultProximityAttribute.class)) {
			throw new QueryNodeException(new MessageImpl(
	                QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
	                "Configuration error: " + DefaultProximityAttribute.class.toString() + " is missing"));
		}
		return queryConfig.getAttribute(DefaultProximityAttribute.class).getDefaultProximity();
	}

}
