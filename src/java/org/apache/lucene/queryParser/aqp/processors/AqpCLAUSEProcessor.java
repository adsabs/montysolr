package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.GroupQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryParser.aqp.util.AqpUtils.Modifier;

/**
 * Converts CLAUSE node into @{link {@link GroupQueryNode}
 * 
 *
 */
public class AqpCLAUSEProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {
	
	
	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		
		if (node instanceof AqpANTLRNode && ((AqpANTLRNode) node).getTokenLabel().equals("CLAUSE")) {
			return new GroupQueryNode(node.getChildren().get(0));			
		}
		return node;
	}

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}
	
	
	/*
	 * methods below not used now, but might be added - it tries to
	 * compact consecutive CLAUSE nodes into one clause, taking only
	 * the last modifier/tmodifier values
	 */
	private ClauseData harvestData(AqpANTLRNode clauseNode) {
		ClauseData data = new ClauseData();
		return harvestData(clauseNode, data);
	}
	
	private ClauseData harvestData(AqpANTLRNode clauseNode, ClauseData data) {
		List<QueryNode> children = clauseNode.getChildren();
		AqpANTLRNode modifierNode = (AqpANTLRNode) children.remove(0); //MODIFIER
		AqpANTLRNode boostNode = (AqpANTLRNode) children.remove(0); //BOOST
		String modifier = null;
		Float boost = null;
		
		if (modifierNode!=null && modifierNode.getChildren()!=null) {
			modifier = ((AqpANTLRNode) modifierNode.getChildren().get(0)).getTokenName();
			if (modifier!=null) {
				data.setModifier(modifier.equals("PLUS") ? Modifier.PLUS : Modifier.MINUS);
			}
		}
		if (boostNode!=null && boostNode.getChildren()!=null) {
			boost = ((AqpANTLRNode) boostNode.getChildren().get(0)).getTokenInputFloat();
			if (boost!=null) {
				data.setBoost(boost);
			}
		}
		
		if (children.size()==1 &&
				children.get(0) instanceof AqpANTLRNode &&
				((AqpANTLRNode)children.get(0)).getTokenLabel().equals("CLAUSE")) {
			return harvestData(((AqpANTLRNode)children.get(0)), data);
		}
		else {
			data.setLastChild(children.get(0));
		}
		return data;
		
	}
	
	
	class ClauseData {
		private Modifier modifier;
		private Float boost;
		private QueryNode lastNonClause;
		
		ClauseData() {
			
		}
		ClauseData(Modifier mod, Float boost) {
			this.modifier = mod;
			this.boost = boost;
			this.lastNonClause = lastNonClause;
		}
		public Modifier getModifier() {
			return modifier;
		}
		public void setModifier(Modifier modifier) {
			this.modifier = modifier;
		}
		public Float getBoost() {
			return boost;
		}
		public void setBoost(Float boost) {
			this.boost = boost;
		}
		public QueryNode getLastChild() {
			return lastNonClause;
		}
		public void setLastChild(QueryNode lastNonClause) {
			this.lastNonClause = lastNonClause;
		}
		
	}

}
