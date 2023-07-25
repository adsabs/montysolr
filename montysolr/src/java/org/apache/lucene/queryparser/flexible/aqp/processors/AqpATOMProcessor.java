package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;

public class AqpATOMProcessor extends QueryNodeProcessorImpl implements
    QueryNodeProcessor {

  @Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
    return node;
  }

  @Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {

    if (node instanceof AqpANTLRNode
        && ((AqpANTLRNode) node).getTokenLabel().equals("ATOM")) {

      AqpANTLRNode atomNode = (AqpANTLRNode) node;

      List<QueryNode> children = node.getChildren();
      QueryNode modifierNode = children.get(0); // MODIFIER
      node = children.get(1); // NUCLEUS

      if (modifierNode.getChildren() != null) {
        String modifier = ((AqpANTLRNode) modifierNode.getChildren().get(0))
            .getTokenName();
        node = new ModifierQueryNode(node,
            modifier.equals("PLUS") ? ModifierQueryNode.Modifier.MOD_REQ
                : ModifierQueryNode.Modifier.MOD_NOT);
      }
      return node;
    }
    return node;
  }

  @Override
  protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
      throws QueryNodeException {
    return children;
  }

}
