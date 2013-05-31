package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpBooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldableNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode.Modifier;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;

/**
 * Optimizes the query tree - on root node - turns +whathever into whatever if
 * there is only one child (but only if Modifier is positive, MOD_REQ or
 * MOD_NONE)
 * 
 */
public class AqpOptimizationProcessor extends QueryNodeProcessorImpl implements
    QueryNodeProcessor {

  @Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
    if (node.getParent() == null && node.getChildren() != null
        && node.getChildren().size() == 1) {
      if (node instanceof BooleanQueryNode) {
        QueryNode c = node.getChildren().get(0);
        if (c instanceof ModifierQueryNode
            && ((ModifierQueryNode) c).getModifier() != Modifier.MOD_NOT) {
          return ((ModifierQueryNode) c).getChild();
        }
      }
    } else if (node instanceof AqpBooleanQueryNode) {

      List<QueryNode> children = node.getChildren();
      String thisOp = ((AqpBooleanQueryNode) node).getOperator();
      boolean rewriteSafe = true;
      	
      QueryNode subClause;
      QueryNode child;
      int counterOfElements = 0;
      
      for (int i = 0; i < children.size(); i++) {
        child = children.get(i);
        if (child.isLeaf()) {
          rewriteSafe = false;
          break;
        }
        
        subClause = getClauseIgnoreModifiers(child); // skips multiple modifier nodes
        if (subClause instanceof FieldableNode) {
        	counterOfElements++;
        }
        else if (subClause instanceof AqpBooleanQueryNode && ((AqpBooleanQueryNode) subClause)
	            .getOperator().equals(thisOp)) {
        	counterOfElements = counterOfElements + subClause.getChildren().size() + 1;
        }
        else {
        	rewriteSafe = false;
        	break;
        }
        
      }

      if (rewriteSafe && counterOfElements > children.size()) {
        List<QueryNode> childrenList = new ArrayList<QueryNode>();

        for (QueryNode ch: children) {
        	subClause = getClauseIgnoreModifiers(ch);
        	if (subClause instanceof AqpBooleanQueryNode) {
	          for (QueryNode nod : subClause.getChildren()) {
	            childrenList.add(nod);
	          }
        	}
        	else {
        		childrenList.add(ch);
        	}
        }

        children.clear();
        node.set(childrenList);
      }
    }

    return node;
  }

  private QueryNode getClauseIgnoreModifiers(QueryNode node) {
  	if (node instanceof ModifierQueryNode 
  			&& node.getChildren().get(0) instanceof ModifierQueryNode 
  			&& ((ModifierQueryNode) node.getChildren().get(0)).getModifier()
  			.equals(((ModifierQueryNode) node).getModifier())) {
  		return getClauseIgnoreModifiers(node.getChildren().get(0));
  	}
  	return node.getChildren().get(0);
  }
  
  private boolean oneOfChildrenBoolean(QueryNode node) {
	  for (QueryNode n: node.getChildren()) {
	  	if (n instanceof AqpBooleanQueryNode) {
	  		return true;
	  	}
	  }
	  return false;
  }

	@Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
    return node;
  }

  @Override
  protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
      throws QueryNodeException {
    return children;
  }

}
