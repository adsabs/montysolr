package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.BoostQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryParser.standard.config.BoostAttribute;
import org.apache.lucene.queryParser.standard.config.FuzzyAttribute;

/**
 * Sets the node into the BoostQueryNode, this processor requires that
 * {@link AqpTMODIFIERProcessor} ran before. Because we depend on the proper
 * tree shape.
 * 
 * <br/>
 * 
 * If BOOST node contains only one child, we return that child and do 
 * nothing.
 * 
 * <br/>
 * 
 * If BOOST node contains two children, we take the first and check its
 * input, eg.
 * <pre>
 *                  FUZZY
 *                  /  \
 *               ~0.1  rest
 * </pre>
 * 
 * We create a new node  {@@link AqpFuzzyModifierNode} (rest, 0.1) and return that node.
 * 
 * <br/>
 * 
 * Presence of the BOOST node child means user specified at least "^"
 * We'll use the default from the configuration {@link BoostAttribute}
 * 
 * @see AqpTMODIFIERProcessor
 * @see AqpFUZZYProcessor
 */
public class AqpFUZZYProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpANTLRNode && ((AqpANTLRNode) node).getTokenLabel().equals("BOOST")) {
			
			if (node.getChildren().size()==1) {
				return node.getChildren().get(0);
			}
			
			Float fuzzy= getFuzzyValue(node);
			
			if (fuzzy==null) {
				return node.getChildren().get(node.getChildren().size());
			}
			
			return new AqpFuzzyModifierNode(node.getChildren().get(node.getChildren().size()), fuzzy);
			
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
	
	private Float getFuzzyValue(QueryNode fuzzyNode) throws QueryNodeException {
		if (fuzzyNode.getChildren()!=null) {
			
			AqpANTLRNode child = ((AqpANTLRNode) fuzzyNode.getChildren().get(0));
			String input = child.getTokenInput();
			float fuzzy;
			
			if (input.equals("~")) {
				QueryConfigHandler queryConfig = getQueryConfigHandler();
				if (queryConfig == null || !queryConfig.hasAttribute(FuzzyAttribute.class)) {
					throw new QueryNodeException(new MessageImpl(
			                QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
			                "Configuration error: " + FuzzyAttribute.class.toString() + " is missing"));
				}
				fuzzy = queryConfig.getAttribute(FuzzyAttribute.class).getFuzzyMinSimilarity();
			}
			else {
				fuzzy = Float.valueOf(input.replace("^", ""));
			}
			
			return fuzzy;
			
		}
		return null;
	}

}
