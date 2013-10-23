package org.apache.lucene.queryparser.flexible.aqp.nodes;

import org.apache.lucene.queryparser.flexible.core.nodes.GroupQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;


public class AqpImmutableGroupQueryNode extends GroupQueryNode {

	public AqpImmutableGroupQueryNode(QueryNode query) {
	  super(query);
  }

}
