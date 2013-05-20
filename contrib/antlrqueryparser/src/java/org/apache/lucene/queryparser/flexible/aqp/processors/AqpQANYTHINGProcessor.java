package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.MatchAllDocsQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;

/**
 * Converts QANYTHING node into @{link {@link MatchAllDocsQueryNode}. The field
 * value is the @{link DefaultFieldAttribute} specified in the configuration.
 * 
 */
public class AqpQANYTHINGProcessor extends AqpQProcessor {

  public boolean nodeIsWanted(AqpANTLRNode node) {
    if (node.getTokenLabel().equals("QANYTHING")) {
      return true;
    }
    return false;
  }

  public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
    return new MatchAllDocsQueryNode();
  }

}
