package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.CharStream;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpDisjunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpImmutableGroupQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpOrQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpWhiteSpacedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
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

public class AqpDEFOPUnfieldedTokens extends AqpQProcessor {
	
	public static String PLAIN_TOKEN = "PLAIN_TOKEN";
	public static String PLAIN_TOKEN_SEPARATOR = " ";
	public static String PLAIN_TOKEN_CONCATENATED = "PLAIN_TOKEN_CONCATENATED";
	
	/*
	 * Nodes will be considered 'bare' even if they have any of these modifiers
	 * e.g. +foo will be effectively treated as if it was 'foo'
	 */
	private List<String> ignoreModifiers;

	/*
	 * Dtto as above 
	 */
	private List<String> ignoreTModifiers;
	private List<String> ignoreFields;
	private List<String> catchQTypes;
	private String operationMode;
	private List<String> wildcardQTypes;
	
	/*
	 * Default constructor with sensible defaults
	 */
	public AqpDEFOPUnfieldedTokens() {
		ignoreModifiers = Arrays.asList("PLUS", "MINUS");
		ignoreTModifiers = Arrays.asList("");
		ignoreFields = null; //Arrays.asList("pubdate");
		catchQTypes = Arrays.asList("QNORMAL", "QTRUNCATED", "QDELIMITER");
		wildcardQTypes = Arrays.asList("QTRUNCATED");
		operationMode = null; // null == wait for the request 
	}
	
	public AqpDEFOPUnfieldedTokens( 
			List<String> firstChildAllowedModifiers,
			List<String> firstChildAllowedFields,
			List<String> ignoreFields,
			List<String> catchQTypes,
			List<String> wildcardQTypes,
			String strategy
			) {
		this.ignoreModifiers = firstChildAllowedModifiers;
		this.ignoreTModifiers = firstChildAllowedFields;
		this.ignoreFields = ignoreFields;
		this.catchQTypes = catchQTypes;
		this.wildcardQTypes = wildcardQTypes;
		this.operationMode = strategy;
	}
	
	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("DEFOP")) {
		  if (node.getParent() != null && node.getParent().getParent() != null) {
		    QueryNode immediateParent = node.getParent();
		    QueryNode distantParent = node.getParent().getParent();
		    
		    if (!(immediateParent instanceof AqpANTLRNode)) return false;
		    
		    AqpANTLRNode distantP = (AqpANTLRNode) distantParent;
		    AqpANTLRNode immediateP = (AqpANTLRNode) immediateParent;
		    
		    if (immediateP.getTokenName().equals("CLAUSE")
		        && distantP.getTokenName().equals("FIELD") 
		        && distantP.getChildren().size() == 2
		        && ((AqpANTLRNode) (distantP.getChildren().get(0))).getTokenInput() != null) {
		      return false;
		    }
		    
		    if (immediateP.getTokenName().equals("TMODIFIER")
            && distantP.getTokenName().equals("MODIFIER") 
            && distantP.getChildren().size() == 2) {
		      AqpANTLRNode modifier = ((AqpANTLRNode) (distantP.getChildren().get(0)));
          if (modifier.getTokenName() != null && !ignoreModifiers.contains(modifier.getTokenName()))
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
						newGroup.clear();
						if (ninfo.isBareNode(true)) {
							newGroup.add(ninfo);
						}
						else {
							newChildren.add(ninfo.getOriginalNode());
						}
						
					}
					else {
						if (newGroup.size() == 1) {
							newChildren.add(newGroup.remove(0).getOriginalNode());
						}
						if (ninfo.isBareNode(true)) {
							newGroup.add(ninfo);
						}
						else {
							newChildren.add(ninfo.getOriginalNode());
						}
					}
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
      List<NodeInfo> newGroup) throws CloneNotSupportedException, QueryNodeException {
		
		if (operationMode == null) {
			operationMode = getStrategy();
		}
	  
		if (operationMode.equals("tag")) {
			tagChildren(newGroup);
			for (NodeInfo ninfo: newGroup) {
				newChildren.add(ninfo.getOriginalNode());
			}
		}
		else if (operationMode.equals("join")) { // concatenates into one single node
			newChildren.add(createReplacementNode(newGroup, null));
		}
		else if (operationMode.equals("add")) { // (original original...) OR (single node)
			QueryNode replacementNode = createReplacementNode(newGroup, null);
			
			QueryNode defopNode = cloneNode(newGroup.get(0).getOriginalNode().getParent());
			
			
			ArrayList<QueryNode> defopChildren = new ArrayList<QueryNode>();
			defopNode.set(defopChildren);
			for (NodeInfo n: newGroup) {
				defopChildren.add(n.getOriginalNode()); // shall we clone?
			}
			defopNode.set(defopChildren);
			
			ArrayList<QueryNode> orClauses = new ArrayList<QueryNode>();
			orClauses.add(new AqpImmutableGroupQueryNode(defopNode));
			orClauses.add(new AqpImmutableGroupQueryNode(replacementNode));
			AqpOrQueryNode orNode = new AqpOrQueryNode(orClauses);
			newChildren.add(orNode);
		}
		else if (operationMode.equals("multiply")) { // (single node) OR ("single node")
			// this strategy is best for ADS as we want to support
			// multi-token synonym replacement, edismax, and also 
			// non-quoted strings should be searched in sensible field
			AqpWhiteSpacedQueryNode normalNode = (AqpWhiteSpacedQueryNode) createReplacementNode(newGroup, "simple");
			AqpWhiteSpacedQueryNode phraseNode = normalNode.cloneTree();
			
			phraseNode.setValue("\"" + phraseNode.getValue() + "\"");
			
			ArrayList<QueryNode> orClauses = new ArrayList<QueryNode>();
			orClauses.add(normalNode);
			orClauses.add(phraseNode);
			AqpOrQueryNode orNode = new AqpOrQueryNode(orClauses);
			newChildren.add(orNode);
		}
		else if (operationMode.equals("disjuncts")) {
		  // the same as above, but the score will be only the maximum of one of the branches
		  AqpWhiteSpacedQueryNode normalNode = (AqpWhiteSpacedQueryNode) createReplacementNode(newGroup, "simple");
      AqpWhiteSpacedQueryNode phraseNode = normalNode.cloneTree();
      
      phraseNode.setValue("\"" + phraseNode.getValue() + "\"");
      
      ArrayList<QueryNode> orClauses = new ArrayList<QueryNode>();
      orClauses.add(normalNode);
      orClauses.add(phraseNode);
      AqpDisjunctionQueryNode disjNode = new AqpDisjunctionQueryNode(orClauses, getDisjunctTieBreaker());
      newChildren.add(disjNode);
		}
		else {
			throw new ParseException(new MessageImpl("Unknown strategy: " + operationMode));
		}
	  
  }

	private void fixTheFieldProblem(List<NodeInfo> newGroup) {
	  NodeInfo firstNode = newGroup.get(0);
	  String f = firstNode.getField();
	  if (f != null) {
	  	QueryNode n = firstNode.getOriginalNode();
	  	removeField(n);
	  }
  }

	private void removeField(QueryNode n) {
	  if (!n.isLeaf() && n instanceof AqpANTLRNode) {
	  	if (((AqpANTLRNode) n).getTokenLabel().equals("FIELD")) {
		  	List<QueryNode> children = n.getChildren();
		  	if (children.size() > 1) {
		  		AqpANTLRNode f = (AqpANTLRNode) children.remove(0);
		  		AqpANTLRNode c = (AqpANTLRNode) AqpQProcessor.getTerminalNode(children.get(0));
		  		c.setTokenInput(f.getTokenInput() + ":" + c.getTokenInput());
		  		c.setInputTokenStart(f.getInputTokenStart());
		  		c.setTokenStart(f.getInputTokenStart());
		  	}
	  	}
	  	for (QueryNode child: n.getChildren()) {
	  		removeField(child);
	  	}
	  }
	  
  }

	private QueryNode createReplacementNode(List<NodeInfo> newGroup, String tt) throws CloneNotSupportedException, QueryNodeException {
	  String newValue = getConcatenatedValue(newGroup);
	  String field = "";
	  if (newGroup.get(0).getField() != null && newGroup.get(0).getField().length() > 0) 
	  	field = newGroup.get(0).getField() + ":";
	  
	  boolean isWildcard = false;
	  for (NodeInfo ninfo: newGroup) {
			if (wildcardQTypes.contains(ninfo.getQType())) {
				isWildcard = true;
				break;
			}
		}
	  
	  if (tt == null)
	  	tt = getNewTokenType();
	  
	  if (tt.equals("simple")) {
	  	return new AqpWhiteSpacedQueryNode(
	  			field != "" ?	newGroup.get(0).getField() : null , newValue, -1, -1);
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
	  	if (tt.contains("QPHRASE")) {
	  		((AqpANTLRNode) terminalNode).setTokenInput("\"" + newValue + "\"");
	  	}
	  	((AqpANTLRNode) terminalParent).setTokenName(tt);
	  	((AqpANTLRNode) terminalParent).setTokenLabel(tt);
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
	
	private Boolean isFieldIgnored(String fld) {
	  return getIgnoredFields().contains(fld);
	}
	
	private Object _getConfigVal(String key) {
	  Map<String, String> args = getQueryConfigHandler().get(
	      AqpStandardQueryConfigHandler.ConfigurationKeys.NAMED_PARAMETER);
	  if (args.containsKey(key)) {
	    return args.get(key);
	  }
	  return null;
	}
	
	private String getStrategy() {
		Object obj = _getConfigVal("aqp.unfielded.tokens.strategy");
		if (obj == null)
		  return "tag";
		return (String) obj;
	}
	
	private Float getDisjunctTieBreaker() {
    Object obj = _getConfigVal("aqp.unfielded.tokens.tiebreaker");
    if (obj == null)
      return 0.0f;
    return Float.valueOf((String) obj);
  }
	
	private Set<String> aqpIgnorableFields = null;
	private Set<String> getIgnoredFields() {
	  if (aqpIgnorableFields != null)
	    return aqpIgnorableFields;
    Object obj = _getConfigVal("aqp.unfielded.ignore.fields");

    aqpIgnorableFields = new HashSet<String>();
    if (obj != null) {
      String[] vals = ((String) obj).split("\\s+");
      for (String v: vals) {
        aqpIgnorableFields.add(v);
      }
    }
    if (ignoreFields != null) {
      for (String v: ignoreFields) {
        aqpIgnorableFields.add(v);
      }
    }
    return aqpIgnorableFields;
  }
	
	private String getNewTokenType() {
	  Object obj = _getConfigVal("aqp.unfielded.tokens.new.type");
		if (obj == null)
		  return "simple";
		
		String m = ((String) obj).toLowerCase();
		
		if (m.contains("phrase")) {
			return "QPHRASE";
		}
		else if (m.contains("simple")) {
			return "simple";
		}
		else {
			return "QNORMAL";
		}
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
		public String getField() {
			return field;
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
	    		if (field == "" || (isFirstInGroup && !isFieldIgnored(field))) {
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
					data.put(level, 
							((AqpANTLRNode) node).getTokenInput() 
							!= null ?
							((AqpANTLRNode) node).getTokenInput()
							:
							((AqpANTLRNode) node).getTokenName()); 
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
