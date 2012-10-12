package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpBooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode.Modifier;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;

/**
 * Optimizes the query tree
 * 		- on root node
 * 			- turns +whathever into whatever if there is only one child
 *            (but only if Modifier is positive, MOD_REQ or MOD_NONE)
 * @author rchyla
 *
 */
public class AqpOptimizationProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node.getParent()==null && node.getChildren()!=null && node.getChildren().size()==1) {
			if (node instanceof BooleanQueryNode ) {
				QueryNode c = node.getChildren().get(0);
				if (c instanceof ModifierQueryNode && 
						((ModifierQueryNode)c).getModifier()!=Modifier.MOD_NOT) {
					return ((ModifierQueryNode)c).getChild();
				}
			}
		}
		else if (node instanceof AqpBooleanQueryNode) {
				
			List<QueryNode> children = node.getChildren();
			String thisOp = ((AqpBooleanQueryNode) node).getOperator();
			boolean rewriteSafe = true;

			QueryNode modifier;
			QueryNode subClause;
			for (int i = 0; i < children.size(); i++) {
				modifier=children.get(i);
				if (modifier.isLeaf()) {
					rewriteSafe=false;
					break;
				}
				subClause = modifier.getChildren().get(0);
				if (!(subClause instanceof AqpBooleanQueryNode &&
						((AqpBooleanQueryNode)subClause).getOperator().equals(thisOp) )) {
					rewriteSafe = false;
					break;
				}
			}

			if (rewriteSafe == true) {
				List<QueryNode> childrenList = new ArrayList<QueryNode>();

				for (int i = 0; i < children.size(); i++) {
					subClause = children.get(i).getChildren().get(0);
					for (QueryNode nod : subClause.getChildren()) {
						childrenList.add(nod);
					}
				}

				children.clear();
				node.set(childrenList);
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
