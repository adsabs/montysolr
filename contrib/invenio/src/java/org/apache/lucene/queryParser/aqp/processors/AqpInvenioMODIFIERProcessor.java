package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryParser.aqp.config.DefaultIdFieldAttribute;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.nodes.InvenioQueryNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.QueryNodeParseException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryParser.core.nodes.BoostQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode.Modifier;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryParser.standard.config.FuzzyAttribute;
import org.apache.lucene.search.InvenioQuery;

/**
 * Creates a {@link ModifierQueryNode} from the MODIFIER node 
 * last child
 *  
 * If MODIFIER node contains only one child, we return that child and do 
 * nothing.
 * <br/>
 * 
 * If BOOST node contains two children, we take the first and check its
 * input, eg.
 * <pre>
 *               MODIFIER
 *                  /  \
 *                 #  rest
 * </pre>
 * 
 * We create an {@link InvenioQuery} node for nodes that have the modifier
 * with value '#'
 * <br/>
 * 
 * This processor should run late enough to be sure that the query was already 
 * translated.
 * 
 * @see Modifier
 * @see AqpBooleanQueryNode
 */
public class AqpInvenioMODIFIERProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpANTLRNode && ((AqpANTLRNode) node).getTokenLabel().equals("MODIFIER")) {
			
			if (node.getChildren().size()==1) {
				return node.getChildren().get(0);
			}
			
			
			String modifier = ((AqpANTLRNode) node.getChildren().get(0)).getTokenName();
			node = node.getChildren().get(node.getChildren().size()-1);
			if (modifier.equals("BAR")) {
				return new InvenioQueryNode(node, getIdField());
			}
			else {
				throw new QueryNodeParseException(new MessageImpl("Unknown modifier: " + modifier));
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
	
	private String getIdField() throws QueryNodeException {
		QueryConfigHandler queryConfig = getQueryConfigHandler();
		if (queryConfig == null || !queryConfig.hasAttribute(DefaultIdFieldAttribute.class)) {
			throw new QueryNodeException(new MessageImpl(
	                QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
	                "Configuration error: " + DefaultIdFieldAttribute.class.toString() + " is missing"));
		}
		return queryConfig.getAttribute(
				DefaultIdFieldAttribute.class).getDefaultIdField();
	}

}

