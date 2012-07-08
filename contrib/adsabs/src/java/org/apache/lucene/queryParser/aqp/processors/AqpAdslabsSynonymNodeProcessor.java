package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpAdslabsSynonymQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;

public class AqpAdslabsSynonymNodeProcessor extends QueryNodeProcessorImpl implements
	QueryNodeProcessor  {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpAdslabsSynonymQueryNode) {
			AqpAdslabsSynonymQueryNode synNode = (AqpAdslabsSynonymQueryNode) node;
			if (synNode.isActivated()) { 
				return expandSynonyms(synNode);
				
			}
			else {
				return applyNonAnalyzableToAllChildren(synNode.getChild());
			}
		}
		return node;
	}

	protected QueryNode applyNonAnalyzableToAllChildren(QueryNode node) {
		
		if (node instanceof AqpNonAnalyzedQueryNode) {
			return node;
		}
		else if (node instanceof FieldQueryNode) {
			return new AqpNonAnalyzedQueryNode((FieldQueryNode) node); 
		}
		
		List<QueryNode> children = node.getChildren();
		
		if (children!=null) {
			
			for (int i=0; i<children.size();i++) {
				children.set(i, applyNonAnalyzableToAllChildren(children.get(i)));
			}
		}
		return node;
		
	}

	protected QueryNode expandSynonyms(AqpAdslabsSynonymQueryNode synNode) {
		// I believe it is the job of the analyzers to expand the node, but it may depend...
		return synNode.getChild();
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}


}
