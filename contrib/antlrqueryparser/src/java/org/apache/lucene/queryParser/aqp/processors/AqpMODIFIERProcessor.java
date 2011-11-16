package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryParser.core.nodes.BoostQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode.Modifier;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;

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
 *                 +  rest
 * </pre>
 * 
 * We create a new node  ModifierQueryNode(rest, Modifier) and return that node.
 * <br/>
 * 
 * This processor should run before {@link AqpOPERATORProcessor} to ensure
 * that local modifiers have precedence over the boolean operations. For example:
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
 * @see Modifier
 * @see AqpBooleanQueryNode
 */
public class AqpMODIFIERProcessor extends QueryNodeProcessorImpl implements
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
			return new ModifierQueryNode(node, modifier.equals("PLUS") ?  ModifierQueryNode.Modifier.MOD_REQ : ModifierQueryNode.Modifier.MOD_NOT);
			
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
