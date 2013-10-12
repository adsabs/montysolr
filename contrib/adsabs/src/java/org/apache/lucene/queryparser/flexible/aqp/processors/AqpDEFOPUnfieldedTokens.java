package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.CharStream;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpOrQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.standard.parser.ParseException;

/**
 * 
 * Looks at the nodes below DEFOP QN and marks the nodes
 * and concatenates them if possible into one tokens.
 * 
 * Using the following example:
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
 * Several options are available:
 * 
 * REPLACE - it will replace the concatenated token; it will also
 *           check whether one of the parts was wildcard search, 
 *           in that case the new token will be marked as QTRUNCATED
 * 
 * <pre>
 *                     DEFOP
 *                        |
 *                     /        \
 *            MODIFIER           CLAUSE 
 *                   /              \
 *           TMODIFIER              MODIFIER
 *                  /                 \
 *             FIELD                 .....
 *                /        
 *           QNORMAL          
 *               /         
 *            weak  lensing
 * </pre>              
 * 
 * ADD - it adds the concatenated token next to the originals
 * 
 * <pre>
 *                     DEFOP
 *                        |\
 *                        | \------------ 
 *                        OR             \
 *                        ----            \
 *                        |    \           \
 *                     DEFOP   MODIFIER     \
 *                     /  |        \         \
 *            MODIFIER   MOD..   TMOD..      CLAUSE 
 *                   /    |         \          \
 *           TMODIFIER   TMODIFIER  FIELD    MODIFIER
 *                  /     |           \          \
 *             FIELD    FIELD        QNORMAL      .....
 *                /       |             \
 *           QNORMAL   QNORMAL        weak lensing
 *               /        |
 *            weak      lensing
 * </pre>     
 *                     
 * <p>
 * Care is taken not to join when the fields are different and 
 * when there is operator/clause/modifier inbetween
 * 
 *
 */

public class AqpDEFOPUnfieldedTokens extends AqpQProcessorPost {
	
	public static String PLAIN_TOKEN = "PLAIN_TOKEN";
	public static String PLAIN_TOKEN_SEPARATOR = " ";
	public static String PLAIN_TOKEN_CONCATENATED = "PLAIN_TOKEN_CONCATENATED";
	private List<String> ignoreModifiers;
	private List<String> ignoreTModifiers;
	private List<String> ignoreFields;
	private List<String> catchQTypes;
	private String operationMode;
	private List<String> wildcardQTypes;
	
	public AqpDEFOPUnfieldedTokens() {
		ignoreModifiers = Arrays.asList("+", "-");
		ignoreTModifiers = Arrays.asList("");
		ignoreFields = Arrays.asList("");
		catchQTypes = Arrays.asList("QNORMAL", "QTRUNCATED");
		wildcardQTypes = Arrays.asList("QTRUNCATED");
		operationMode = "add_replace";
	}
	
	public AqpDEFOPUnfieldedTokens(boolean modifyTree, 
			List<String> firstChildAllowedModifiers,
			List<String> firstChildAllowedFields) {
		this.ignoreModifiers = firstChildAllowedModifiers;
		this.ignoreTModifiers = firstChildAllowedFields;
	}
	
	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("DEFOP")) {
  		// this will refuse processing of: '=(this that token)'
			// but the '=' modifier must not be in the list of modifiers to ignore
			if (node.getParent() != null && node.getParent().getParent() != null) {
				QueryNode p = node.getParent().getParent();
				if (p.getChildren().size() > 1 &&
						!ignoreModifiers.contains(((AqpANTLRNode) p.getChildren().get(0)).getTokenInput())) {
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
		
		// harvest node info into a manageable form
		List<NodeInfo> nodeInfos = harvestNodeChildren(node);
		
		List<NodeInfo> newGroup = new ArrayList<NodeInfo>();
		List<QueryNode> newChildren = new ArrayList<QueryNode>();
		
		// go through the list of nodes, decide what to do with them
		// insert the results into 'newChildren'
		try {
			for (NodeInfo ninfo: nodeInfos) {
				if (ninfo.isBareNode(newGroup.size() == 0)) {
					newGroup.add(ninfo);
				}
				else {
					if (newGroup.size() > 1) {
						decideInsertChild(newChildren, newGroup);
					}
					else if (newGroup.size() == 1) {
						newChildren.add(newGroup.get(0).getOriginalNode());
					}
					newChildren.add(ninfo.getOriginalNode());
					newGroup.clear();
				}
			}
			
			if (newGroup.size() > 1) {
				decideInsertChild(newChildren, newGroup);
			}
			else if (newGroup.size() == 1) {
				newChildren.add(newGroup.get(0).getOriginalNode());
			}
		}
		catch (CloneNotSupportedException e) {
			throw new QueryNodeException(e);
		}
		
		// set the modifications back into the parent
		node.set(newChildren);
		
		return node;
	}
	
	private void decideInsertChild(List<QueryNode> newChildren,
      List<NodeInfo> newGroup) throws CloneNotSupportedException, ParseException {
	  
		if (operationMode.equals("tag")) {
			tagChildren(newGroup);
			for (NodeInfo ninfo: newGroup) {
				newChildren.add(ninfo.getOriginalNode());
			}
		}
		else if (operationMode.equals("replace")) {
			newChildren.add(createReplacementNode(newGroup));
		}
		else if (operationMode.equals("add_replace")) {
			QueryNode replacementNode = createReplacementNode(newGroup);
			QueryNode defopNode = cloneNode(newGroup.get(0).getOriginalNode().getParent());
			
			
			ArrayList<QueryNode> defopChildren = new ArrayList<QueryNode>();
			defopNode.set(defopChildren);
			for (NodeInfo n: newGroup) {
				defopChildren.add(n.getOriginalNode()); // shall we clone?
			}
			defopNode.set(defopChildren);
			
			ArrayList<QueryNode> orClauses = new ArrayList<QueryNode>();
			orClauses.add(defopNode);
			orClauses.add(replacementNode);
			AqpOrQueryNode orNode = new AqpOrQueryNode(orClauses);
			newChildren.add(orNode);
		}
	  
  }

	private QueryNode createReplacementNode(List<NodeInfo> newGroup) throws CloneNotSupportedException, ParseException {
	  String newValue = getConcatenatedValue(newGroup);
	  boolean isWildcard = false;
	  for (NodeInfo ninfo: newGroup) {
			if (wildcardQTypes.contains(ninfo.getQType())) {
				isWildcard = true;
				break;
			}
		}
	  
	  
	  // we'll reuse the first node (but make its copy)
	  QueryNode firstNode = cloneNode(newGroup.get(0).getOriginalNode());
	  
	  // inject the new value
	  QueryNode terminalNode = AqpQProcessor.getTerminalNode(firstNode);
	  ((AqpANTLRNode) terminalNode).setTokenInput(newValue);
	  ((AqpANTLRNode) terminalNode).setTokenName("INJECTED");
	  
	  // change parent's type 
	  // TODO: change the position information?
	  
	  QueryNode terminalParent = terminalNode.getParent();
	  if (isWildcard) {
	  	((AqpANTLRNode) terminalParent).setTokenName("QTRUNCATED");
	  	((AqpANTLRNode) terminalParent).setTokenLabel("QTRUNCATED");
	  }
	  else {
	  	((AqpANTLRNode) terminalNode).setTokenInput("\"" + newValue + "\"");
	  	((AqpANTLRNode) terminalParent).setTokenName("QPHRASE");
	  	((AqpANTLRNode) terminalParent).setTokenLabel("QPHRASE");
	  }
	  return firstNode;
  }

	/*
	 * Ufff....this is necessary, because the QueryNodeImpl is NOT
	 * resetting the parent. sooooo stupid....
	 */
	private QueryNode cloneNode(QueryNode node) throws CloneNotSupportedException {
		QueryNode n = node.cloneTree();
		fixClone(n);
		return n;
	}
	private void fixClone(QueryNode node) {
	  ArrayList<QueryNode> newChildren = new ArrayList<QueryNode>();
	  newChildren.addAll(node.getChildren());
	  node.set(newChildren);
	  for (QueryNode qn: node.getChildren()) {
	  	if (!qn.isLeaf()) {
	  		fixClone(qn);
	  	}
	  }
  }


	private String getConcatenatedValue(List<NodeInfo> newGroup) throws ParseException {
		
		boolean allIsAqp = true;
		for (NodeInfo ninfo: newGroup) {
			if (!(ninfo.getTerminalNode() instanceof AqpANTLRNode)) {
				allIsAqp = false;
				break;
			}
		}
		
		if (allIsAqp) {
			AqpANTLRNode first = (AqpANTLRNode) newGroup.get(0).getTerminalNode();
			AqpANTLRNode last = (AqpANTLRNode) newGroup.get(newGroup.size()-1).getTerminalNode();
			int start = first.getInputTokenStart();
			int end = last.getInputTokenEnd();
			CharStream is = AqpQProcessor.getInputStream(first);
			return is.substring(start, end);
		}
		else {
			StringBuffer concatenated = new StringBuffer();
			boolean first = false;
			for (NodeInfo ninfo: newGroup) {
				if (first) {
					concatenated.append(PLAIN_TOKEN_SEPARATOR);
				}
				concatenated.append(ninfo.getInput());
				first = true;
			}
			return concatenated.toString();
		}
		
		
	}
	
	private void tagChildren(List<NodeInfo> newGroup) throws ParseException {
		
		String value = getConcatenatedValue(newGroup);
		for (NodeInfo ninfo: newGroup) {
			ninfo.getTerminalNode().setTag(PLAIN_TOKEN_CONCATENATED, value);
		}
  }
	

	private List<NodeInfo> harvestNodeChildren(AqpANTLRNode node) {
		ArrayList<NodeInfo> out = new ArrayList<NodeInfo>();
	  for (QueryNode child: node.getChildren()) {
	  	out.add(new NodeInfo(child));
	  }
	  return out;
  }

	private class NodeInfo {
		private QueryNode originalNode;
		private String qType;
		private String field;
		private String tModifier;
		private String modifier;
		private String input;
		
		public NodeInfo(QueryNode node) {
			originalNode = node;
			qType = null;
			field = null;
			tModifier = null;
			modifier = null;
			initValues();
		}
		
		public String getInput() throws ParseException {
	    
	    if (originalNode instanceof AqpANTLRNode) {
	    	return AqpQProcessor.getOriginalInput((AqpANTLRNode) originalNode, new HashSet()).value;
	    }
	    else {
	    	QueryNode terminal = getTerminalNode();
	    	if (terminal instanceof FieldQueryNode) {
	    		return ((FieldQueryNode) terminal).getTextAsString();
	    	}
	    	else {
	    		if (input != null && input != "") {
	    			return input;
	    		}
	    		throw new ParseException(new MessageImpl("Hmmm, should never happen that we don't know how to get user input. Error is ours, not yours though!"));
	    	}
	    }
	    
    }
		public QueryNode getOriginalNode() {
	    return originalNode;
    }
		public boolean isBareNode(boolean isFirstInGroup) {
			// we allow only the following:
			// /MODIFIER/TMODIFIER/FIELD/QNORMAL
			// and all elements must be either empty
			// or have values that can be ignored
			
	    if (modifier == "" || (isFirstInGroup && ignoreModifiers.contains(modifier))) {
	    	if (tModifier == "" || (isFirstInGroup && ignoreTModifiers.contains(tModifier))) {
	    		if (field == "" || (isFirstInGroup && ignoreFields.contains(field))) {
	    			if (catchQTypes.contains(qType)) {
	    				return true;
	    			}
	    		}
	    	}
	    }
	    return false;
    }
		
		public String getQType() {
			return qType;
		}
		
		private void initValues() {
			Map<Integer, String> labels = new HashMap<Integer, String>();
			harvestLabels(originalNode, labels, 5, 0);
			
			// check the node has correct structure, otherwise pull out
			// that leaves the values to be null, which means 'get out!'
			if (!(labels.containsKey(0) && labels.get(0).equals("MODIFIER") &&
					labels.containsKey(1) && labels.get(1).equals("TMODIFIER") &&
					labels.containsKey(2) && labels.get(2).equals("FIELD"))) {
				return;
			}
			
			if (labels.containsKey(3)) {
				qType = labels.get(3);
			}
			
			Map<Integer, String> values = new HashMap<Integer, String>();
			harvestValues(originalNode, values, 5, 0);
			
			modifier = values.containsKey(1) ? values.get(1) : "";
			tModifier = values.containsKey(2) ? values.get(2) : "";
			field = values.containsKey(3) ? values.get(3) : "";
			input = values.containsKey(4) ? values.get(4) : "";
    }
		
		private void harvestValues(QueryNode node, Map<Integer, String> data, int maxDepth, int level) {
			
			assert maxDepth >= level;
			if (maxDepth == level)
				return;
			
			if (node.isLeaf()) {
				if (node instanceof AqpANTLRNode) {
					data.put(level, ((AqpANTLRNode) node).getTokenInput()); 
				}
				else {
					data.put(level, "???");
				}
			}
			else {
				for (QueryNode n: node.getChildren()) {
					harvestValues(n, data, maxDepth, level+1);
				}
			}
		}
		
		private void harvestLabels(QueryNode node, Map<Integer, String> data, int maxDepth, int level) {
			
			assert maxDepth >= level;
			//System.out.println(data);
			if (maxDepth == level)
				return;

			if (!node.isLeaf()) {

				if (!data.containsKey(level)) {
					//System.out.println("checking=" + level + "node=" + node);
					//QueryNode parent = node.getParent();
					if (node instanceof AqpANTLRNode) {
						data.put(level, ((AqpANTLRNode) node).getTokenLabel()); 
					}
					else {
						data.put(level, "???");
					}
				}

				for (QueryNode n: node.getChildren()) {
					harvestLabels(n, data, maxDepth, level+1);
				}
			}
			
		}
		
		public QueryNode getTerminalNode() {
			return AqpQProcessor.getTerminalNode(originalNode);
		}
	}

	
	
	

}
