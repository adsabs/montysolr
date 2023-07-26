package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.DeletedQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

/*
 * Just deletes the comma node, because if it made until here, 
 * it is no longer needed
 */
public class AqpAdsabsQDELIMITERProcessor extends AqpQProcessorPost {
  
	@Override
  public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenName().equals("QDELIMITER")) {
			return true;
		}
		return false;
  }
	
  @Override
  public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
    return new DeletedQueryNode(); 
  }

}
