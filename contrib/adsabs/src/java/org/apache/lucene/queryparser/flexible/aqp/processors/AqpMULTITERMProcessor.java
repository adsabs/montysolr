package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.BoostQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.OrQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.util.AqpUtils;
import org.apache.lucene.queryparser.flexible.aqp.util.AqpUtils.Modifier;

public class AqpMULTITERMProcessor extends QueryNodeProcessorImpl implements
    QueryNodeProcessor {

  @Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
    return node;
  }

  @Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
    if (node instanceof AqpANTLRNode
        && ((AqpANTLRNode) node).getTokenLabel().equals("MULTITERM")) {
      AqpANTLRNode mNode = (AqpANTLRNode) node;
      AqpANTLRNode modifierNode = (AqpANTLRNode) mNode.getChild("MODIFIER");
      AqpANTLRNode fieldNode = (AqpANTLRNode) mNode.getChild("FIELD");
      AqpANTLRNode multiNode = (AqpANTLRNode) mNode.getChild("MULTIATOM");
      AqpANTLRNode tModifierNode = (AqpANTLRNode) mNode.getChild("TMODIFIER");

      String field = AqpUtils.getFirstChildInputString(fieldNode);
      Modifier modifier = AqpUtils.getFirstChildInputModifier(modifierNode);

      if (field != null) {
        AqpUtils.applyFieldToAllChildren(EscapeQuerySyntaxImpl
            .discardEscapeChar(field).toString(), multiNode);
      }

      node = new OrQueryNode(multiNode.getChildren());

      if (tModifierNode != null) {
        AqpANTLRNode boostNode = tModifierNode.getChild("BOOST");
        Float boost = AqpUtils.getFirstChildInputFloat(boostNode);
        if (boost != null) {
          node = new BoostQueryNode(node, boost);
        }
      }

      if (modifier != null) {
        node = new ModifierQueryNode(node,
            modifier == Modifier.PLUS ? ModifierQueryNode.Modifier.MOD_REQ
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

  private String getFieldValue(AqpANTLRNode fieldNode)
      throws QueryNodeException {

    if (fieldNode != null && fieldNode.getChildren() != null) {
      return ((AqpANTLRNode) fieldNode.getChildren().get(0)).getTokenInput();
    }
    return null;

  }

}
