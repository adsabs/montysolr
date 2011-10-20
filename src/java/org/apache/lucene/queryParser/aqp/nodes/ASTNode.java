package org.apache.lucene.queryParser.aqp.nodes;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;
import org.apache.lucene.queryParser.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryParser.core.parser.EscapeQuerySyntax;

public class ASTNode extends QueryNodeImpl {
	
	  //private static final long serialVersionUID =  ;

	  protected Token token;
	  
	  private int start;
	  
	  private int end;
	  
	  private String type;
	  
	  private String value = null;
	  
	  /**
	   * @param node
	   *          - AST node
	   */
	  public ASTNode(Tree node) {
		  type = "t" + node.getType();
		  value = node.getText();
		  start = node.getTokenStartIndex();
		  end = node.getTokenStopIndex();
		  if (node.getChildCount() > 0) {
			  setLeaf(false);
			  allocate();
		  }
		  
	  }

	  @Override
	  public CharSequence toQueryString(EscapeQuerySyntax escaper) {
		  if (this.value != null) {
	      return "(" + this.type + this.value + ")";
		  }
		  else {
			  return this.type;
		  }
	  }

	  @Override
	  public String toString() {
	    return "<ast type='" + this.type + "' value='" + this.value
	        + "' start='" + this.start + "' end='" + this.end +"'/>";
	  }
	  
}
