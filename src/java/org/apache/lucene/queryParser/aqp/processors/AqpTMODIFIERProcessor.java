package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;

/**
 * TMODIFIER node contains FUZZY, BOOST, FIELD nodes.
 * This processor changes the tree from this shape:
 * 
 * <pre>
 *               TMODIFIER
 *               /   |   \
 *           FUZZY BOOST FIELD
 *             /           \
 *            ~1           ...
 * </pre>
 *           
 *  To this shape:
 * <pre>              
 *               FUZZY
 *                / \
 *              ~1  BOOST
 *                    |
 *                    FIELD
 *                      \
 *                     ...
 * </pre>                    
 *  
 *  After the processor ran, the TMODIFIER node is removed and we return
 *  the FUZZY node
 *  <br/>
 *  
 *  If TMODIFIER contains only single child, we return that child
 *  (thus remove the TMODIFIER node from the tree).
 *  <br/>
 *  
 *  @see AqpFUZZYProcessor
 *  @see AqpBOOSTProcessor
 *  @see AqpFIELDProcessor
 */
public class AqpTMODIFIERProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		
		if (node instanceof AqpANTLRNode
				&& ((AqpANTLRNode) node).getTokenLabel().equals("TMODIFIER")) {
			
			List<QueryNode> children = node.getChildren();
			
			if (children.size()==0) {
				return children.get(0);
			}
			
			QueryNode masterChild = children.get(0);
			for (int i=1;i<children.size();i++) {
				List<QueryNode> masterChildren = masterChild.getChildren();
				masterChildren.add(children.get(i));
				masterChild = children.get(i);
			}
			
			return masterChild;
			
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
