package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.config.AqpFeedback;
import org.apache.lucene.queryParser.aqp.nodes.AqpFuzzyModifierNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.SlopQueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryParser.standard.nodes.WildcardQueryNode;

/**
 * Rewrites the query node which is below the {@link AqpFuzzyModifierNode}
 * 
 * The actions are:
 * 		FieldQueryNode 
 * 			- query is turned into FuzzyQueryNode
 * 			- invalid syntax is raised if not 0.0 > fuzzy < 1.0
 *  	WildcardQueryNode
 *  	QuotedFieldQueryNode
 *  		- wrapped with SlopQueryNode
 * 			
 *
 */
public class AqpFuzzyModifierProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpFuzzyModifierNode) {
			QueryNode child = ((AqpFuzzyModifierNode) node).getChild();
			Float fuzzy = ((AqpFuzzyModifierNode) node).getFuzzyValue();
			
			if (child instanceof QuotedFieldQueryNode ||
					child instanceof WildcardQueryNode) {
				
				if (fuzzy.intValue() < fuzzy) {
					QueryConfigHandler config = getQueryConfigHandler();
					if (config.hasAttribute(AqpFeedback.class)) {
						AqpFeedback feedback = config.getAttribute(AqpFeedback.class);
						feedback.sendEvent(feedback.createEvent(AqpFeedback.TYPE.WARN, 
								this.getClass(), node, 
								"For phrases and wildcard queries the float attribute " + fuzzy + 
								" is automatically converted to: " + fuzzy.intValue()));
					}
				}
				return new SlopQueryNode(child, fuzzy.intValue());
			}
			else if (child instanceof FieldQueryNode) {
				
				if (fuzzy<0.0f || fuzzy>=1.0f) {
					throw new QueryNodeException(new MessageImpl(
						QueryParserMessages.INVALID_SYNTAX,
						node.toString() + "\nSimilarity s must be 0.0 > s < 1.0"));
				}
				
				FieldQueryNode fn = (FieldQueryNode) child;
				
				return new FuzzyQueryNode(fn.getFieldAsString(), fn.getTextAsString(), fuzzy,
						fn.getBegin(), fn.getEnd());
			}
			else  {
				throw new QueryNodeException(new MessageImpl(
						QueryParserMessages.INVALID_SYNTAX,
						node.toString() + "\nUse of ~ is not allowed here"));
			}
			
		}
		return node;
	}

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}

}
