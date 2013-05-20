package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;

public class AqpNUCLEUSProcessor extends QueryNodeProcessorImpl implements
    QueryNodeProcessor {

  @Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
    if (node instanceof AqpANTLRNode
        && ((AqpANTLRNode) node).getTokenLabel().equals("NUCLEUS")) {
      List<QueryNode> children = node.getChildren();
      AqpANTLRNode fieldNode = (AqpANTLRNode) children.remove(0);
      String field = getFieldValue(fieldNode);
      QueryNode valueNode = children.get(0);
      if (field != null) {
        applyFieldToAllChildren(EscapeQuerySyntaxImpl.discardEscapeChar(field)
            .toString(), valueNode);
      }
      return valueNode;
    }
    return node;
  }

  @Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
    return node;
  }

  @Override
  protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
      throws QueryNodeException {
    return children;
  }

  private String getFieldValue(AqpANTLRNode fieldNode)
      throws QueryNodeException {

    if (fieldNode != null && fieldNode.getChildren() != null) {
      return ((AqpANTLRNode) fieldNode.getChildren().get(0)).getTokenInput();
    }
    return null;

  }

  private void applyFieldToAllChildren(String field, QueryNode node) {

    if (node instanceof FieldQueryNode) {
      ((FieldQueryNode) node).setField(field);
    }
    if (node.getChildren() != null) {
      for (QueryNode child : node.getChildren()) {
        applyFieldToAllChildren(field, child);
      }
    }
  }

}
