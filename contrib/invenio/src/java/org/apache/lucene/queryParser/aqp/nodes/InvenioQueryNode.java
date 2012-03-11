package org.apache.lucene.queryParser.aqp.nodes;


import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.config.InvenioQueryAttribute.Channel;
import org.apache.lucene.queryParser.core.QueryNodeError;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryParser.core.parser.EscapeQuerySyntax;

public class InvenioQueryNode extends QueryNodeImpl implements QueryNode {

	private static final long serialVersionUID = 3935454544149998076L;
	private String idField = null;
	private Channel channel = Channel.DEFAULT;
	private String searchField = null;
	
	public InvenioQueryNode(QueryNode query, String idField) {
		if (query == null) {
			throw new QueryNodeError(new MessageImpl(
					QueryParserMessages.PARAMETER_VALUE_NOT_SUPPORTED, "query",
					"null"));
		}

		allocate();
		setLeaf(false);
		add(query);
		this.idField = idField;
	}

	
	public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
		if (getChild() == null)
			return "";

		String leftParenthensis = "";
		String rightParenthensis = "";

		if (getChild() != null && getChild() instanceof InvenioQueryNode) {
			leftParenthensis = "(";
			rightParenthensis = ")";
		}

		return leftParenthensis + "#" + getChild().toQueryString(escapeSyntaxParser)
				+ rightParenthensis ;

	}

	
	public String toString() {
		return "<invenio>\n"  + getChild().toString() + "\n</invenio>";
	}

	public QueryNode getChild() {
		return getChildren().get(0);
	}

	public String getIdField() {
		return idField;
	}
	
	public String getSearchField() {
		return searchField;
	}
	
	public void setSeatchField(String field) {
		searchField = field;
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public void setChannel(Channel ch) {
		channel = ch;
	}

}
