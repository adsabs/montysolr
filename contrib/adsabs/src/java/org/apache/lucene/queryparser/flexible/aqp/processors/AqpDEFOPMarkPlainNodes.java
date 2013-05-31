package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.CharStream;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

/**
 * 
 * Looks at the nodes below DEFOP QN and marks the nodes
 * that can be concatenated during analysis, eg. weak lensing
 * can be used as one token
 * 
 * <pre>
 *                     DEFOP
 *                        |
 *                     /  |     \
 *            MODIFIER   MOD..   CLAUSE 
 *                   /    |         \
 *           TMODIFIER   TMODIFIER  MODIFIER
 *                  /     |           \
 *             FIELD    FIELD        .....
 *                /       |
 *           QNORMAL   QNORMAL
 *               /        |
 *            weak      lensing
 * </pre>           
 *                    
 * <p>
 * Care is taken not to join when the fields are different and 
 * when there is operator/clause/modifier inbetween
 * 
 * @author rca
 *
 */

public class AqpDEFOPMarkPlainNodes extends AqpQProcessor {
	
	public static String PLAIN_TOKEN = "PLAIN_TOKEN";
	public static String PLAIN_TOKEN_SEPARATOR = " ";
	public static String PLAIN_TOKEN_CONCATENATED = "PLAIN_TOKEN_CONCATENATED";
	private boolean modifyTree = false;
	private List<String> firstChildAllowedModifiers;
	
	public AqpDEFOPMarkPlainNodes() {
		modifyTree = false;
		firstChildAllowedModifiers = Arrays.asList("+", "-");
	}
	
	public AqpDEFOPMarkPlainNodes(boolean modifyTree, List<String> firstChildAllowedModifiers) {
		this.modifyTree = modifyTree;
		this.firstChildAllowedModifiers = firstChildAllowedModifiers;
	}
	
	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("DEFOP")) {
			
  		// refuse processing: '=(this that token)'
			// but this is not the ideal place for it (it is kind of arbitrary)
			if (node.getParent() != null && node.getParent().getParent() != null) {
				QueryNode p = node.getParent().getParent();
				if (p.getChildren().size() > 1 &&
						!firstChildAllowedModifiers.contains(((AqpANTLRNode) p.getChildren().get(0)).getTokenInput())) {
					return false; 
				}
			}
				
			return true;
		}
		return false;
	}
	
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		
		// only one child, do nothing
		if (node.getChildren().size() == 1) {
			return node;
		}
		
		List<QueryNode> children = node.getChildren();
		List<QueryNode> forMarking = new ArrayList<QueryNode>();
		List<QueryNode> newChildren = new ArrayList<QueryNode>();
		
		
		Integer previous = -1;
		for (int i=0;i<children.size();i++) {
			if (isBareNode(children.get(i), forMarking.size() == 0)) {
				if (forMarking.size() == 0) {
					previous = i;
					forMarking.add(children.get(i));
				}
				else if (previous+1 == i) {
					forMarking.add(children.get(i));
					previous = i;
					continue;
				}
				else {
					previous = -1;
					tagPlainNodes(forMarking, newChildren);
					continue;
				}
			}
			else if (modifyTree){
				if (forMarking.size() > 0)
					tagPlainNodes(forMarking, newChildren);
				newChildren.add(children.get(i));
			}
		}
		
		if (forMarking.size() > 0)
			tagPlainNodes(forMarking, newChildren);
		
		if (modifyTree) 
			node.set(newChildren);
		
		return node;
	}
	
	private void tagPlainNodes(List<QueryNode> forMarking, List<QueryNode> newChildren) {
		
		int startPos = -1;
		int endPos = -1;
		StringBuffer concatenated = new StringBuffer();	
		AqpANTLRNode terminal;
		
		if (forMarking.size() > 1) {
			int tag = concatenated.hashCode();
			for (int i=0;i<forMarking.size();i++) {
				concatenated.append(markChild(tag, forMarking.get(i)));
				if (i+1 != forMarking.size())
					concatenated.append(PLAIN_TOKEN_SEPARATOR);
				if (modifyTree) {
					terminal = (AqpANTLRNode) getTerminalNode(forMarking.get(i));
					if (startPos == -1)
						startPos = ((AqpANTLRNode)terminal).getInputTokenStart();
					endPos = ((AqpANTLRNode)terminal).getInputTokenEnd();
				}
			}
		}
		if (modifyTree ) { // keep the first node in the group
			QueryNode first = forMarking.get(0);
			if (forMarking.size() > 1) {
				terminal = (AqpANTLRNode) getTerminalNode(first);
				CharStream is = AqpQProcessor.getInputStream(terminal);
				String val = is.substring(startPos, endPos);
				terminal.setInputTokenStart(startPos);
				terminal.setInputTokenEnd(endPos);
				terminal.setTokenInput(val);
				terminal.setTag(PLAIN_TOKEN_CONCATENATED, concatenated.toString());
			}
			newChildren.add(first);
		}
		forMarking.clear();
	}
	
	
	private boolean isBareNode(QueryNode node, boolean isPotentiallyFirst) {
		StringBuffer sb = new StringBuffer();
		harvestLabels(node, sb, 5);
		if (sb.toString().equals("/MODIFIER/TMODIFIER/FIELD/QNORMAL")) {
			ArrayList<String> vals = new ArrayList<String>();
			harvestValues(node, vals, 4);
			if (vals.size()>0 && vals.size()<=1) {
				if (isPotentiallyFirst && firstChildAllowedModifiers.contains(vals.get(0))) {// we allow modifiers for the first child
					return true;
				}
				return false;
			}
			return true;
		}
		return false;
	}
		
	private void harvestLabels(QueryNode node, StringBuffer data, int maxDepth) {
		if (maxDepth == 0)
			return;
		
		if (node instanceof AqpANTLRNode) {
			if (node.isLeaf()) {
				return; // avoid the terminal node
			}
			data.append("/");
			data.append(((AqpANTLRNode) node).getTokenLabel());
			for (QueryNode child: node.getChildren()) {
				harvestLabels(child, data, maxDepth-1);
			}
		}
		else {
			data.append("/?");
		}
	}
	
	private void harvestValues(QueryNode node, List<String> data, int maxDepth) {
		if (maxDepth == 0)
			return;
		
		if (node instanceof AqpANTLRNode) {
			if (node.isLeaf()) {
				data.add(((AqpANTLRNode) node).getTokenLabel());
				return;
			}
			for (QueryNode child: node.getChildren()) {
				harvestValues(child, data, maxDepth-1);
			}
		}
		else {
			data.add("?");
		}
	}
	
	private String markChild(int tag, QueryNode node) {
		AqpANTLRNode terminal = (AqpANTLRNode) getTerminalNode(node);
		terminal.setTag(PLAIN_TOKEN, tag);
		return terminal.getTokenInput();
	}

}
