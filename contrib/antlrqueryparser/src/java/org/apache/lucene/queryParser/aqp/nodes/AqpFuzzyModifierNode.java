package org.apache.lucene.queryParser.aqp.nodes;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.core.QueryNodeError;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryParser.core.parser.EscapeQuerySyntax;

public class AqpFuzzyModifierNode extends QueryNodeImpl implements QueryNode {

	private static final long serialVersionUID = -3059874057254791689L;
	private Float fuzzy;

	public AqpFuzzyModifierNode(QueryNode query, Float fuzzy) {
		if (query == null) {
			throw new QueryNodeError(new MessageImpl(
					QueryParserMessages.PARAMETER_VALUE_NOT_SUPPORTED, "query",
					"null"));
		}

		allocate();
		setLeaf(false);
		add(query);
		this.fuzzy = fuzzy;
	}

	
	public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
		if (getChild() == null)
			return "";

		String leftParenthensis = "";
		String rightParenthensis = "";

		if (getChild() != null && getChild() instanceof ModifierQueryNode) {
			leftParenthensis = "(";
			rightParenthensis = ")";
		}

		return leftParenthensis + getChild().toQueryString(escapeSyntaxParser)
				+ rightParenthensis + "~" + this.fuzzy.toString();

	}

	
	public String toString() {
		return "<fuzzy value='" + this.fuzzy.toString() + "'>" + "\n"
				+ getChild().toString() + "\n</fuzzy>";
	}

	public QueryNode getChild() {
		return getChildren().get(0);
	}
	
	public Float getFuzzyValue() {
		return fuzzy;
	}

}
