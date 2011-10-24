package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.hamcrest.core.IsInstanceOf;

public class AqpTreeRewriteProcessor extends QueryNodeProcessorImpl {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		
		if (node instanceof AqpANTLRNode && node.getChildren()!= null) {
			List<QueryNode> children = node.getChildren();
			AqpANTLRNode parent = (AqpANTLRNode)node;
			AqpANTLRNode child;
			
			// turn (AND (AND (CLAUSE...))) into (AND (CLAUSE...))
			// also (AND (ATOM ....)) into (ATOM...)
			if (parent.getTokenName().equals("OPERATOR") && children.size() == 1) {
				child = (AqpANTLRNode) children.get(0);
				if (child.getTokenName().equals("OPERATOR") || child.getTokenName().equals("ATOM")) {
					return child;
				}
			}
			
			if (node.getParent()==null && children.size() > 1) { // it is a root mode
				String last = ((AqpANTLRNode)children.get(0)).getTokenLabel();
				boolean rewriteSafe = true;
				// check all children nodes are of the same type
				for (int i=1;i<children.size();i++) {
					AqpANTLRNode t = (AqpANTLRNode) children.get(i);
					String tt = t.getTokenLabel();
					if (!(tt.equals(last) && t.getTokenName().equals("OPERATOR"))) {
						rewriteSafe = false;
						break;
					}
				}
				if (rewriteSafe==true) {
					
					QueryNode firstChild = children.get(0);
					List<QueryNode> childrenList = firstChild.getChildren();
					
					for (int i=1;i<children.size();i++) {
						QueryNode otherChild = children.get(i);
						for (QueryNode n: otherChild.getChildren()) {
							childrenList.add(n);
						}
					}
					
					children.clear();
					parent.set(childrenList);
				}
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
