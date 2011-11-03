package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.nodes.AqpFuzzyModifierNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.SlopQueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryParser.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryParser.standard.parser.EscapeQuerySyntaxImpl;

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
			
			if (child instanceof FieldQueryNode) {
				if (fuzzy<0.0f || fuzzy>=1.0f) {
					throw new QueryNodeException(new MessageImpl(
						QueryParserMessages.INVALID_SYNTAX,
						node.toString(), "Similarity s must be 0.0 > s < 1.0"));
				}
				
				FieldQueryNode fn = (FieldQueryNode) child;
				
				return new FuzzyQueryNode(fn.getFieldAsString(), fn.getTextAsString(), fuzzy,
						fn.getBegin(), fn.getEnd());
			}
			else if (child instanceof QuotedFieldQueryNode ||
					child instanceof WildcardQueryNode) {
				return new SlopQueryNode(child, fuzzy.intValue());
			}
			else  {
				throw new QueryNodeException(new MessageImpl(
						QueryParserMessages.INVALID_SYNTAX,
						node.toString(), "Use of is ~ not allowed here"));
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
