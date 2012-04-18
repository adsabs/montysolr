package org.apache.lucene.queryParser.aqp.processors;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode.Modifier;
import org.apache.lucene.queryParser.core.nodes.QueryNode;

/**
 * Creates a {@link ModifierQueryNode} from the MODIFIER node last child
 * 
 * If MODIFIER node contains only one child, we return that child and do
 * nothing. <br/>
 * 
 * If BOOST node contains two children, we take the first and check its input,
 * eg.
 * 
 * <pre>
 *               MODIFIER
 *                  /  \
 *                 +  rest
 * </pre>
 * 
 * We create a new node ModifierQueryNode(rest, Modifier) and return that node. <br/>
 * 
 * This processor should run before {@link AqpOPERATORProcessor} to ensure that
 * local modifiers have precedence over the boolean operations. For example:
 * 
 * <pre>
 * title:(+a -b c)
 * </pre>
 * 
 * Should produce (when OR is a default operator):
 * 
 * <pre>
 *  +title:a -title:b title:c
 * </pre>
 * 
 * and when AND is the default operator:
 * 
 * <pre>
 *  +title:a -title:b +title:c
 * </pre>
 * 
 * @see Modifier
 * @see AqpBooleanQueryNode
 */
public class AqpMODIFIERProcessor extends AqpQProcessor {

	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("MODIFIER")) {
			return true;
		}
		return false;
	}
	
	public AqpANTLRNode getModifierNode(QueryNode node) {
		return ((AqpANTLRNode) node.getChildren().get(0));
	}
	
	public QueryNode getValueNode(QueryNode node) {
		return node.getChildren().get(node.getChildren().size() - 1);
	}

	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {

		if (node.getChildren().size() == 1) {
			return node.getChildren().get(0);
		}

		AqpANTLRNode modifierNode = getModifierNode(node);
		String modifier = modifierNode.getTokenName();

		QueryNode childNode = getValueNode(node);
		
		if (modifier.equals("PLUS")) {
			return new ModifierQueryNode(childNode,
					ModifierQueryNode.Modifier.MOD_REQ);
		} else if (modifier.equals("MINUS")) {
			return new ModifierQueryNode(childNode,
					ModifierQueryNode.Modifier.MOD_NOT);
		} else {
			throw new QueryNodeException(new MessageImpl(
					QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
					"Unknown modifier: " + modifier + "\n" + node.toString()));
		}

	}

}
