package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.parser.ParseException;

public class AqpAdsabsCOMMAProcessor extends AqpCOMMAProcessor {
  
  @Override
  public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
    
    String label = node.getTokenLabel();
    
    if (label.equals("COMMA")) {
      List<QueryNode> children = node.getChildren();
      ArrayList<String> vals = new ArrayList<String>();
      QueryConfigHandler config = getQueryConfigHandler();
      String dummy = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.DUMMY_VALUE);
      QueryNode targetNode = null;
      QueryNode valueNode = null;
      for (QueryNode child: children) {
        // we need only the value, excluding field 
        AqpANTLRNode fldNode = ((AqpANTLRNode) child).findChild("FIELD");
        QueryNode valNode = fldNode.getChildren().get(fldNode.getChildren().size()-1);
        
        String input = getOriginalInput((AqpANTLRNode) valNode);
        if (!input.equals(dummy)) {
          vals.add(input);
          if (targetNode == null) {
            targetNode = child;
            valueNode = valNode;
          }
        }
      }
      StringBuilder sb = new StringBuilder();
      boolean first = true;
      for (String v: vals) {
        if (!first) sb.append(",");
        sb.append(v);
        first = false;
      }
      if (targetNode == null) {
        throw new ParseException(new MessageImpl("There is no user input"));
      }
      
      modifyTargetNode(valueNode, sb.toString());
      return targetNode;
    }
    return super.createQNode(node);
  }

  private void modifyTargetNode(QueryNode node, String string) {
    if (node.isLeaf()) {
      if (((AqpANTLRNode) node).getTokenName().equals("TERM_NORMAL")) {
        ((AqpANTLRNode) node).setTokenInput(string);
      }
    }
    else {
      for (QueryNode child: node.getChildren()) {
        modifyTargetNode(child, string);
      }
    }
  }
}
