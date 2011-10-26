package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.util.AqpUtils;
import org.apache.lucene.queryParser.aqp.util.AqpUtils.Modifier;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.BoostQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryParser.core.nodes.OrQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;

public class AqpMULTIATOMProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpANTLRNode && ((AqpANTLRNode) node).getTokenLabel().equals("MULTIATOM")) {
			AqpANTLRNode mNode = (AqpANTLRNode) node;
			AqpANTLRNode modifierNode = (AqpANTLRNode) mNode.getChild("MODIFIER");
			AqpANTLRNode fieldNode = (AqpANTLRNode) mNode.getChild("FIELD");
			AqpANTLRNode multiNode = (AqpANTLRNode) mNode.getChild("MULTIVALUE");
			AqpANTLRNode tModifierNode = (AqpANTLRNode) mNode.getChild("TMODIFIER");
			
			String field = AqpUtils.getFirstChildInputString(fieldNode);
			Modifier modifier = AqpUtils.getFirstChildInputModifier(modifierNode);
			
			
			if (field!=null) {
				AqpUtils.applyFieldToAllChildren(field, multiNode);
			}
			
			node = new OrQueryNode(multiNode.getChildren());
			
			if (tModifierNode!=null) {
				AqpANTLRNode boostNode = tModifierNode.getChild("BOOST");
				Float boost = AqpUtils.getFirstChildInputFloat(boostNode);
				if (boost!=null) {
					node = new BoostQueryNode(node, boost);
				}
			}
			
			if (modifier!=null) {
				node = new ModifierQueryNode(node, modifier==Modifier.PLUS ? ModifierQueryNode.Modifier.MOD_REQ 
																		   : ModifierQueryNode.Modifier.MOD_NOT);
			}
			
			
			return node;
			
		}
		return node;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}
	
	private String getFieldValue(AqpANTLRNode fieldNode) throws QueryNodeException {
		
		if (fieldNode!= null && fieldNode.getChildren()!=null) {
			return ((AqpANTLRNode) fieldNode.getChildren().get(0)).getTokenInput();
		}
		return null;
		
	}

}
