package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;

/**
 * Converts QTRUNCATED node into @{link {@link WildcardQueryNode}. The field
 * value used is the @{link DefaultFieldAttribute} specified in the
 * configuration.
 * 
 * <p>
 * 
 * If the user specified a field, it will be set by the @{link
 * AqpFIELDProcessor} Therefore the {@link AqpQTRUNCATEDProcessor} should run
 * before it.
 * 
 * 
 * @see QueryConfigHandler
 * 
 */
public class AqpQTRUNCATEDProcessor extends AqpQProcessor {

  public boolean nodeIsWanted(AqpANTLRNode node) {
    if (node.getTokenLabel().equals("QTRUNCATED")) {
      return true;
    }
    return false;
  }

  public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
    String field = getDefaultFieldName();

    AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);
    String input = subChild.getTokenInput();

    if (input.contains("*?") || input.contains("?*")) {
      throw new QueryNodeException(new MessageImpl(
          QueryParserMessages.INVALID_SYNTAX,
          "It is not allowed to put * next to ?" + input));
    }

    return new WildcardQueryNode(field,
        EscapeQuerySyntaxImpl.discardEscapeChar(input),
        subChild.getTokenStart(), subChild.getTokenEnd());

  }

}
