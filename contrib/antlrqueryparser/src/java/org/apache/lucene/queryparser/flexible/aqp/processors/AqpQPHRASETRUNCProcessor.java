package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;

/**
 * Converts QPHRASETRUNC node into @{link {@link WildcardQueryNode}. The field
 * value used is the @{link DefaultFieldAttribute} specified in the
 * configuration.
 * 
 * <br/>
 * 
 * If the user specified a field, it will be set by the @{link
 * AqpFIELDProcessor} Therefore the {@link AqpQTRUNCATEDProcessor} should run
 * before it.
 * 
 * 
 * @see QueryConfigHandler
 * @see DefaultFieldAttribute
 * 
 */
public class AqpQPHRASETRUNCProcessor extends AqpQProcessor {

  public boolean nodeIsWanted(AqpANTLRNode node) {
    if (node.getTokenLabel().equals("QPHRASETRUNC")) {
      return true;
    }
    return false;
  }

  public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
    String field = getDefaultFieldName();

    AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);

    return new WildcardQueryNode(field,
        EscapeQuerySyntaxImpl.discardEscapeChar(subChild.getTokenInput()
            .substring(1, subChild.getTokenInput().length() - 1)),
        subChild.getTokenStart() + 1, subChild.getTokenEnd() - 1);

  }

}
