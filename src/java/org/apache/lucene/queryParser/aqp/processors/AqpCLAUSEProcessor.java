package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.processors.AqpCLAUSEProcessor.ClauseData;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.BoostQueryNode;
import org.apache.lucene.queryParser.core.nodes.GroupQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;

public class AqpCLAUSEProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpANTLRNode && ((AqpANTLRNode) node).getTokenLabel().equals("CLAUSE")) {
			ClauseData data = harvestData((AqpANTLRNode) node);
			
			node = new GroupQueryNode(data.getLastChild());
			if (data.getBoost()!=null) {
				node = new BoostQueryNode(node, data.getBoost());
			}
			if (data.getModifier()!=null) {
				node = new ModifierQueryNode(node, data.getModifier()==Modifier.PLUS ? ModifierQueryNode.Modifier.MOD_REQ 
																					 : ModifierQueryNode.Modifier.MOD_NOT);
			}
			return node;
		}
		return node;
	}

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}
	
	private ClauseData harvestData(AqpANTLRNode clauseNode) {
		ClauseData data = new ClauseData();
		return harvestData(clauseNode, data);
	}
	
	private ClauseData harvestData(AqpANTLRNode clauseNode, ClauseData data) {
		List<QueryNode> children = clauseNode.getChildren();
		AqpANTLRNode modifierNode = (AqpANTLRNode) children.remove(0);
		AqpANTLRNode boostNode = (AqpANTLRNode) children.remove(0);
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
		
		if (children.size()==1 && ((AqpANTLRNode)children.get(0)).getTokenLabel().equals("CLAUSE")) {
			return harvestData(((AqpANTLRNode)children.get(0)), data);
		}
		else {
			data.setLastChild(children.get(0));
		}
		return data;
		
	}
	
	enum Modifier {
		PLUS,
		MINUS;
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
