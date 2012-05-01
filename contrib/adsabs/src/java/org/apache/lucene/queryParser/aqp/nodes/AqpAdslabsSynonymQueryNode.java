package org.apache.lucene.queryParser.aqp.nodes;

import java.util.List;
import java.util.Map;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.core.QueryNodeError;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryParser.core.parser.EscapeQuerySyntax;

public class AqpAdslabsSynonymQueryNode extends QueryNodeImpl implements QueryNode {

	private static final long serialVersionUID = 6262993993478292390L;
	private boolean allowed = false;
	
	
	public AqpAdslabsSynonymQueryNode(QueryNode child, boolean activated) {
		if (child == null) {
			throw new QueryNodeError(new MessageImpl(
					QueryParserMessages.PARAMETER_VALUE_NOT_SUPPORTED, "child",
					"null"));
		}
		allocate();
		setLeaf(false);
		add(child);
		this.allowed = activated;
	}
	
	public String toString() {
		return "<synonymNode activated='" + this.isActivated() + "'>" + "\n"
				+ getChild().toString() + "\n</synonymNode>";
	}

	public QueryNode getChild() {
		return getChildren().get(0);
	}
	
	public boolean isActivated() {
		return allowed;
	}
	
	public void setActivated(boolean val) {
		allowed = val;
	}

	public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
		if (isActivated()) {
			return "#" + getChild().toQueryString(escapeSyntaxParser);
		}
		else {
			return "=" + getChild().toQueryString(escapeSyntaxParser);
		}
	}

}
