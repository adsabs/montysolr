package org.apache.lucene.queryParser.aqp.nodes;

import java.util.List;

import org.apache.lucene.queryParser.aqp.AqpCommonTree;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryParser.core.parser.EscapeQuerySyntax;

public class AqpANTLRNode extends QueryNodeImpl {

	private static final long serialVersionUID = 5128762709928473351L;

	private AqpCommonTree tree;

	private int tokenType;

	private String tokenLabel;

	private String tokenName;

	private String tokenInput = null;
	

	/**
	 * @param node
	 *            - AST node
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
			return "(" + getTokenLabel() + getTokenInput() + ")";
		} else {
			return getTokenLabel();
		}
	}

	public String toStringNodeOnly() {
		if (getTokenInput() != null) {
			return "<ast" + getTokenName() + " value=\"" + getTokenInput() + "\" start=\""
					+ getTokenStart() + "\" end=\"" + getTokenEnd()
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
			buf.append("value=\"" + getTokenInput() + "\" start=\""
					+ getTokenStart() + "\" end=\"" + getTokenEnd()
					+ "\"");
		} else {
			buf.append("label=\"" + getTokenLabel() + "\"");
		}
		buf.append(" name=\"" + getTokenName() + "\"" + " type=\""
				+ getTokenType() + "\" ");

		List<QueryNode> children = this.getChildren();

		if (children != null) {
			buf.append(">");
			for (QueryNode child : children) {
				if (child instanceof AqpANTLRNode) {
					buf.append(((AqpANTLRNode) child).toString(level+4));
				}
				else {
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

	public String getTokenLabel() {
		return tokenLabel;
	}

	public void setTokenLabel(String tokenLabel) {
		this.tokenLabel = tokenLabel;
	}

	public String getTokenName() {
		return tokenName;
	}

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
	
	public AqpANTLRNode getChild(String tokenLabel) {
		List<QueryNode> children = getChildren();
		if (children!=null) {
			for (QueryNode child: children) {
				AqpANTLRNode n = (AqpANTLRNode) child;
				if (n.getTokenLabel().equals(tokenLabel)) {
					return n;
				}
			}
			
		}
		return null;
	}
	
	public Float getTokenInputFloat() {
		if (this.tokenInput!=null) {
			return Float.valueOf(this.tokenInput);
		}
		return null;
	}

}
