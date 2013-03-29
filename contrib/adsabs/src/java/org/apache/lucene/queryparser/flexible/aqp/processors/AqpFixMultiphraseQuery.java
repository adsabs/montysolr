package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.MultiPhraseQueryNode;

public class AqpFixMultiphraseQuery extends QueryNodeProcessorImpl {

	@Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
	  return node;
  }

	@Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
		if (node instanceof MultiPhraseQueryNode) {
			List<QueryNode> children = node.getChildren();
			for (QueryNode child : children) {
				System.out.println(((FieldQueryNode) child).getPositionIncrement() + child.toString());
				((FieldQueryNode) child).setPositionIncrement(0);
			}
		}
	  return node;
  }

	@Override
  protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
      throws QueryNodeException {
	  return children;
  }


}
