package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;

/**
 * Converts QNORMAL node into @{link {@link FieldQueryNode}. The field value is
 * the @{link DefaultFieldAttribute} specified in the configuration.
 * 
 * <p>
 * 
 * If the user specified a field, it will be set by the @{link
 * AqpFIELDProcessor} Therefore the {@link AqpQNORMALProcessor} should run
 * before it.
 * 
 * 
 * @see QueryConfigHandler
 * 
 */
public class AqpQNORMALProcessor extends AqpQProcessor {

  public boolean nodeIsWanted(AqpANTLRNode node) {
    if (node.getTokenLabel().equals("QNORMAL")) {
      return true;
    }
    return false;
  }

  public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
    String field = getDefaultFieldName();

    AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);

    return new FieldQueryNode(field,
        EscapeQuerySyntaxImpl.discardEscapeChar(subChild.getTokenInput()),
        subChild.getTokenStart(), subChild.getTokenEnd());
  }

}
