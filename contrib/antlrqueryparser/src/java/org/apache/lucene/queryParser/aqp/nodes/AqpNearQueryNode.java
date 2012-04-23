package org.apache.lucene.queryParser.aqp.nodes;

import java.util.List;
import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.core.QueryNodeError;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryParser.core.parser.EscapeQuerySyntax;

public class AqpNearQueryNode extends QueryNodeImpl implements QueryNode {
	
	private static final long serialVersionUID = 8806759327487974314L;
	private Integer slop = null;
	private boolean inOrder = true;
	
	public AqpNearQueryNode(List<QueryNode> children, int proximity) {
		if (children == null) {
			throw new QueryNodeError(new MessageImpl(
					QueryParserMessages.PARAMETER_VALUE_NOT_SUPPORTED, "children",
					"null"));
		}
		allocate();
		setLeaf(false);
		add(children);
		this.slop = proximity;
	}

	
	public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
		if (getChild() == null)
			return "";

		String leftParenthensis = "";
		String rightParenthensis = "";

		if (getChild() != null && getChild() instanceof AqpNearQueryNode) {
			leftParenthensis = "(";
			rightParenthensis = ")";
		}

		return leftParenthensis + "#" + getChild().toQueryString(escapeSyntaxParser)
				+ rightParenthensis ;

	}

	
	public String toString() {
		StringBuffer bo = new StringBuffer();
		bo.append("<near slop=\"");
		bo.append(slop);
		bo.append("\">\n");
		for (QueryNode child: this.getChildren()) {
			bo.append(child.toString());
			bo.append("\n");
		}
		bo.append("\n</near>");
		return bo.toString();
	}

	public QueryNode getChild() {
		return getChildren().get(0);
	}

	public Integer getSlop() {
		return slop;
	}
	
	public void setSlop(Integer prox) {
		slop = prox;
	}


	public boolean getInOrder() {
		return inOrder;
	}
	
	public void setInOrder( boolean order ) {
		inOrder = order;
	}
}
