package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;

/*
 * Processor which massages the AST tree before other processors work
 * with it.
 * 	- replaces chain of OPERATORs with the lowest ie. (AND (AND (AND..)))
 *    becomes (AND ...); this happens only if the OPERATOR has one 
 *    single child of type: OPERATOR, ATOM, CLAUSE
 *    
 *    Useful mostly for the DEFOP operator as our ANTLR grammars
 *    usually group same clauses under one operator
 *    
 *  - 
 */
public class AqpTreeRewriteProcessor extends QueryNodeProcessorImpl {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		
		if (node instanceof AqpANTLRNode && node.getChildren()!= null) {
			List<QueryNode> children = node.getChildren();
			AqpANTLRNode n = (AqpANTLRNode)node;
			AqpANTLRNode child;
			
			// turn (AND (AND (CLAUSE...))) into (AND (CLAUSE...))
			// also (AND (ATOM ....)) into (ATOM...)
			if (n.getTokenName().equals("OPERATOR") && children.size() == 1) {
				child = (AqpANTLRNode) children.get(0);
				if (child.getTokenName().equals("OPERATOR") 
						|| child.getTokenName().equals("ATOM")
						|| child.getTokenName().equals("CLAUSE")) {
					return child;
				}
			}
			
			/*
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
						for (QueryNode nod: otherChild.getChildren()) {
							childrenList.add(nod);
						}
					}
					
					children.clear();
					n.set(childrenList);
				}
			}
			*/
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
