package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.DeletedQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

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
