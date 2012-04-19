package org.apache.lucene.queryParser.aqp.nodes;

import java.util.List;

import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryParser.core.parser.EscapeQuerySyntax;

public class AqpFunctionQueryNode extends QueryNodeImpl implements QueryNode {

	private static final long serialVersionUID = 751068795564006998L;
	private String name = null;
	private List<String> rawData = null;
	
	public AqpFunctionQueryNode(String name, QueryNode child, List<String> rawData) {
		allocate();
		setLeaf(false);
		add(child);
		this.name = name;
		this.rawData = rawData;
	}

	public String toString() {
		StringBuffer bo = new StringBuffer();
		bo.append("<function name=\"");
		bo.append(getName());
		bo.append("\">\n");
		for (QueryNode child: this.getChildren()) {
			bo.append(child.toString());
		}
		bo.append("\n</function>");
		return bo.toString();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public QueryNode getChild() {
		return getChildren().get(0);
	}

	public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
		return getName() + "(" + getChild().toQueryString(escapeSyntaxParser) + ")";
	}
	
	public List<String> getRawData() {
		return rawData;
	}

}
