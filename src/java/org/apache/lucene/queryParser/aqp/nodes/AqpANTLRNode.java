package org.apache.lucene.queryParser.aqp.nodes;

import java.util.List;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;
import org.apache.lucene.queryParser.aqp.AqpCommonTree;
import org.apache.lucene.queryParser.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryParser.core.parser.EscapeQuerySyntax;

public class AqpANTLRNode extends QueryNodeImpl {
	
	private static final long serialVersionUID = 5128762709928473351L;
	
	private AqpCommonTree tree;

	protected Token token;
	  
	  private String tokenType;
	  
	  private String tokenInput = null;
	  
	  /**
	   * @param node
	   *          - AST node
	   */
	  public AqpANTLRNode(AqpCommonTree node) {
		  tree = node;
		  tokenInput = node.getTokenInput();
		  if (tokenInput!=null) {
			  tokenType = "INPUT";
		  }
		  else {
			  tokenType = node.getTokenType();
		  }
		  if (node.getChildCount() > 0) {
			  setLeaf(false);
			  allocate();
		  }
		  
	  }

	  @Override
	  public CharSequence toQueryString(EscapeQuerySyntax escaper) {
		  if (this.tokenInput != null) {
	      return "(" + this.tokenType + this.tokenInput + ")";
		  }
		  else {
			  return this.tokenType;
		  }
	  }

	  @Override
	  public String toString() {
		  if (this.tokenInput!=null) {
			  return "<ast value=\"" + this.tokenInput + "\" start=\"" + tree.getStartIndex() + 
    		         "\" end=\"" + tree.getStopIndex() + "\" />";
		  }
		  else {
			  return "<ast type=\"" + this.tokenType + "\" />";
		  }
	  }
	  
	  public String toStringRecursive() {
		  return toStringRecursive(0);
	  }
	  
	  public String toStringRecursive(int level) {
		  StringBuffer buf = new StringBuffer();
		  for (int i=0;i<level;i++) {
			  buf.append(" ");
		  }
		  
		  buf.append("<ast ");
		  
		  if (this.tokenInput!=null) {
			  buf.append( "value=\"" + this.tokenInput + "\" start=\"" + tree.getStartIndex() + 
    		         "\" end=\"" + tree.getStopIndex() + "\"");
		  }
		  else {
			  buf.append("type=\"" + this.tokenType + "\"");
		  }
		  
		  List<QueryNode> children = this.getChildren();
		  
		  if (children!=null) {
			  buf.append(">\n");
			  for (QueryNode child: children) {
				  buf.append(((AqpANTLRNode)child).toStringRecursive(level+2));
			  }
		  }
		  
		  if (isLeaf()) {
			  buf.append(" />");
		  }
		  else {
			  for (int i=0;i<level;i++) {
				  buf.append(" ");
			  }
			  buf.append("</ast>");
		  }
		  buf.append("\n");
		  
		  return buf.toString();
	  }
	  
}
