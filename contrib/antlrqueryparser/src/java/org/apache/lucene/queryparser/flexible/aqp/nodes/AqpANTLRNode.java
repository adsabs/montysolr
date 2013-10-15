package org.apache.lucene.queryparser.flexible.aqp.nodes;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.CommonToken;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;
import org.apache.lucene.queryparser.flexible.aqp.util.AqpCommonTree;

/**
 * When Aqp parser starts processing the AST (abstract syntax tree)
 * every node in the tree is made of {@link AqpANTLRNode} and it wraps
 * the {@link AqpCommonTree} through which you can access information
 * about the string, its position, type etc... these are courtesy of 
 * ANTLR.
 * 
 * We provide a few utility methods for setting different attributes
 * of the original ANTLR object.
 *
 */
public class AqpANTLRNode extends QueryNodeImpl {

  private static final long serialVersionUID = 5128762709928473351L;

  private AqpCommonTree tree;

  private int tokenType;

  private String tokenLabel;

  private String tokenName;

  private String tokenInput = null;

	private boolean isCloning;

  /**
   * @param node
   *          - AST node
   */
  public AqpANTLRNode(AqpCommonTree node) {

    tree = node;
    String input = node.getTokenInput();

    if (input != null) {
      setTokenInput(input);
    }

    setTokenLabel(node.getTokenLabel());

    setTokenType(node.getTokenType());

    setTokenName(node.getTypeLabel());

    if (node.getChildCount() > 0) {
      setLeaf(false);
      allocate();
    }
  }

  public CharSequence toQueryString(EscapeQuerySyntax escaper) {
    if (getTokenInput() != null) {
      return "(" + getTokenLabel() + ":" + getTokenInput() + ")";
    } else {
      return getTokenLabel();
    }
  }

  public String toStringNodeOnly() {
    if (getTokenInput() != null) {
      return "<ast" + getTokenName() + " value=\"" + getTokenInput()
          + "\" start=\"" + getTokenStart() + "\" end=\"" + getTokenEnd()
          + "\" />";
    } else {
      return "<ast" + getTokenName() + " type=\"" + getTokenLabel() + "\" />";
    }
  }

  public String toString() {
    return toString(0);
  }

  public String toString(int level) {
    StringBuffer buf = new StringBuffer();
    buf.append("\n");
    for (int i = 0; i < level; i++) {
      buf.append(" ");
    }

    buf.append("<ast" + getTokenName() + " ");

    if (getTokenInput() != null) {
      buf.append("value=\"" + getTokenInput() + "\" start=\"" + getTokenStart()
          + "\" end=\"" + getTokenEnd() + "\"");
    } else {
      buf.append("label=\"" + getTokenLabel() + "\"");
    }
    buf.append(" name=\"" + getTokenName() + "\"" + " type=\"" + getTokenType()
        + "\" ");

    List<QueryNode> children = this.getChildren();

    if (children != null) {
      buf.append(">");
      for (QueryNode child : children) {
        if (child instanceof AqpANTLRNode) {
          buf.append(((AqpANTLRNode) child).toString(level + 4));
        } else {
          buf.append(child.toString());
        }
      }
    }

    if (isLeaf()) {
      buf.append("/>");
    } else {
      buf.append("\n");
      for (int i = 0; i < level; i++) {
        buf.append(" ");
      }
      buf.append("</ast" + getTokenName() + ">");
    }

    return buf.toString();
  }

  public int getTokenType() {
    return tokenType;
  }

  public void setTokenType(int tokenType) {
    this.tokenType = tokenType;
  }

  /**
   * Label is what is displayed in the AST tree, for example and, And, AND will
   * all have label=AND
   * 
   * (But their internal name is an 'OPERATOR')
   * 
   * @return
   */
  public String getTokenLabel() {
    return tokenLabel;
  }

  /**
   * Label is what is displayed in the AST tree, for example and, And, AND will
   * all have label=AND
   * 
   * (But their internal name is an 'OPERATOR')
   * 
   */
  public void setTokenLabel(String tokenLabel) {
    this.tokenLabel = tokenLabel;
  }

  /**
   * Name is the internal token name, for example and, And, AND will
   * all have name=OPERATOR
   */
  public String getTokenName() {
    return tokenName;
  }

  /**
   * Name is the name of the group, ie. 'AND' is an OPERATOR (but its label
   * says: 'AND')
   * 
   * @param tokenName
   */
  public void setTokenName(String tokenName) {
    this.tokenName = tokenName;
  }

  public String getTokenInput() {
    return tokenInput;
  }

  public void setTokenInput(String tokenInput) {
    this.tokenInput = tokenInput;
  }

  public int getTokenStart() {
    return tree.getStartIndex();
  }

  public int getTokenEnd() {
    return tree.getStopIndex();
  }

  public AqpCommonTree getTree() {
    return tree;
  }

  public int getInputTokenStart() {
    return ((CommonToken) tree.getToken()).getCharPositionInLine();// getStartIndex();
  }

  public int getInputTokenEnd() {
    return ((CommonToken) tree.getToken()).getStopIndex();
  }

  public void setInputTokenEnd(int stop) {
    ((CommonToken) tree.getToken()).setStopIndex(stop);
  }

  public void setInputTokenStart(int start) {
    ((CommonToken) tree.getToken()).setStartIndex(start);
  }

  public AqpANTLRNode getChild(String tokenLabel) {
    List<QueryNode> children = getChildren();
    if (children != null) {
      for (QueryNode child : children) {
        AqpANTLRNode n = (AqpANTLRNode) child;
        if (n.getTokenLabel().equals(tokenLabel)) {
          return n;
        }
      }

    }
    return null;
  }
  
  public int hasTokenName(String tokenName, int level) {
    List<QueryNode> children = getChildren();
    if (children != null) {
      for (QueryNode child : children) {
        AqpANTLRNode n = (AqpANTLRNode) child;
        if (n.getTokenLabel().equals(tokenName)) {
          return level+1;
        }
        int nl = n.hasTokenName(tokenName, level+1);
        if (nl > level+1)
        	return nl;
      }
    }
    return level;
  }

  public AqpANTLRNode findChild(String tokenLabel) {
    ArrayList<QueryNode> lst = new ArrayList<QueryNode>();
    findChild(this, tokenLabel, lst);

    if (lst.size() == 1) {
      return (AqpANTLRNode) lst.get(0);
    } else if (lst.size() > 1) {
      throw new RuntimeException(
          "This method is not meant to search for n>1 nodes");
    }
    return null;
  }

  private void findChild(QueryNode node, String tokenLabel,
      ArrayList<QueryNode> lst) {
    if (((AqpANTLRNode) node).getTokenLabel().equals(tokenLabel)) {
      lst.add(node);
    } else {
      if (!node.isLeaf()) {
        for (QueryNode child : node.getChildren()) {
          findChild(child, tokenLabel, lst);
        }
      }
    }
  }

  public Float getTokenInputFloat() {
    if (this.tokenInput != null) {
      return Float.valueOf(this.tokenInput);
    }
    return null;
  }
  

}
