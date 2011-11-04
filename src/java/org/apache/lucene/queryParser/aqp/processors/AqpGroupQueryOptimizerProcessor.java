package org.apache.lucene.queryParser.aqp.processors;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.util.AqpUtils.Modifier;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryParser.core.nodes.BoostQueryNode;
import org.apache.lucene.queryParser.core.nodes.GroupQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;


/**
 * Optimizes the query by removing the superfluous GroupQuery
 * nodes. We harvest all parameters from fuzzy, boost, and modifier
 * nodes and apply those that are closest to the actual query.
 * 
 * <br/>
 * Example:
 * 
 * <pre>this (+(-(+(-(that thus))^0.1))^0.3)</pre>
 * 
 * Will be optimized into:
 * 
 * <pre>+field:this -((+field:that +field:thus)^0.1)</pre>
 * 
 * 
 */
public class AqpGroupQueryOptimizerProcessor extends QueryNodeProcessorImpl
		implements QueryNodeProcessor {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof GroupQueryNode) {
			
			ClauseData data = harvestData(node);
			
			if (data.getLastChild()!=null && !node.equals(data.getLastChild())) {
				node = data.getLastChild();
				if (data.getBoost()!=null) {
					node = new BoostQueryNode(node, data.getBoost());
				}
				if (data.getModifier()!=null) {
					List<QueryNode> children = new ArrayList<QueryNode>();
					children.add(new ModifierQueryNode(node, data.getModifier()));
					node = new BooleanQueryNode(children);
				}
				
				
				return node;
			}
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
	private ClauseData harvestData(QueryNode clauseNode) {
		ClauseData data = new ClauseData();
		harvestData(clauseNode, data);
		return data;
	}
	
	private void harvestData(QueryNode node, ClauseData data) {
		
		
		if (node instanceof ModifierQueryNode) {
			data.setModifier(((ModifierQueryNode) node).getModifier());
		}
		else if (node instanceof BoostQueryNode) {
			data.setBoost(((BoostQueryNode) node).getValue());
		}
		else if (node instanceof GroupQueryNode) {
			//pass
		}
		else {
			data.setLastChild(node);
			return; //break processing
		}
		if (!node.isLeaf() && node.getChildren().size()==1) {
			harvestData(node.getChildren().get(0), data);
		}
		
	}
	
	
	class ClauseData {
		private ModifierQueryNode.Modifier modifier;
		private Float boost;
		private QueryNode lastNonClause;
		
		ClauseData() {
			
		}
		ClauseData(ModifierQueryNode.Modifier mod, Float boost) {
			this.modifier = mod;
			this.boost = boost;
			this.lastNonClause = lastNonClause;
		}
		public ModifierQueryNode.Modifier getModifier() {
			return modifier;
		}
		public void setModifier(ModifierQueryNode.Modifier modifier) {
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
