package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    Modifier om = null;
    if (node instanceof ModifierQueryNode) {
      om = ((ModifierQueryNode) node).getModifier();
    }
    else {
      return node;
    }
    
    QueryNode ret = node;
    while (ret instanceof ModifierQueryNode) {
      if (ret.getChildren().size() != 1)
        break;
      QueryNode ch = ret.getChildren().get(0);
      if (ch.getChildren() == null)
        break;
      boolean safe = true;
      for (QueryNode child: ch.getChildren()) {
        if (child instanceof ModifierQueryNode 
            && ((ModifierQueryNode)child).getModifier().equals(om)) {
          // pass
        }
        else {
          safe = false;
          break;
        }
      }
      if (safe) {
        ret = ch;
      }
      else {
        break;
      }
    }
  	return ret;
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
	  if (node instanceof AqpBooleanQueryNode) {
	    Set<String> seen = new HashSet<String>();
      List<QueryNode> children = node.getChildren();
      int i = 0;
      boolean changed = false;
      while (i < children.size()) {
        if (seen.contains(children.get(i).toString())) {
          children.remove(i);
          changed = true;
        }
        else {
          seen.add(children.get(i).toString());
          i += 1;
        }
      }
      if (changed)
        node.set(children);
	  }
	  return node;
  }

  @Override
  protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
      throws QueryNodeException {
    return children;
  }

}
