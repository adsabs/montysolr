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
			setTokenInput(tokenInput);
		}

		setTokenLabel(node.getTokenLabel());

		setTokenType(node.getTokenType());

		setTokenName(node.getTypeLabel());

		if (node.getChildCount() > 0) {
			setLeaf(false);
			allocate();
		}
	}

	@Override
	public CharSequence toQueryString(EscapeQuerySyntax escaper) {
		if (getTokenInput() != null) {
			return "(" + getTokenLabel() + getTokenInput() + ")";
		} else {
			return getTokenLabel();
		}
	}

	public String toStringNodeOnly() {
		if (getTokenInput() != null) {
			return "<ast value=\"" + getTokenInput() + "\" start=\""
					+ tree.getStartIndex() + "\" end=\"" + tree.getStopIndex()
					+ "\" />";
		} else {
			return "<ast type=\"" + getTokenLabel() + "\" />";
		}
	}

	@Override
	public String toString() {
		return toStringRecursive(0);
	}

	public String toStringRecursive(int level) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < level; i++) {
			buf.append(" ");
		}

		buf.append("<ast ");

		if (this.tokenInput != null) {
			buf.append("value=\"" + getTokenInput() + "\" start=\""
					+ tree.getStartIndex() + "\" end=\"" + tree.getStopIndex()
					+ "\"");
		} else {
			buf.append("label=\"" + getTokenLabel() + "\"");
		}
		buf.append(" name=\"" + getTokenName() + "\"" + " type=\""
				+ getTokenType() + "\" ");

		List<QueryNode> children = this.getChildren();

		if (children != null) {
			buf.append(">\n");
			for (QueryNode child : children) {
				buf.append(((AqpANTLRNode) child).toStringRecursive(level + 4));
			}
		}

		if (isLeaf()) {
			buf.append("/>");
		} else {
			for (int i = 0; i < level; i++) {
				buf.append(" ");
			}
			buf.append("</ast>");
		}
		buf.append("\n");

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

}
