package org.apache.lucene.queryParser.aqp.nodes;

import java.util.List;

import org.apache.lucene.queryParser.aqp.builders.AqpFunctionQueryBuilder;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryParser.core.parser.EscapeQuerySyntax;

public class AqpFunctionQueryNode extends QueryNodeImpl implements QueryNode {

	private static final long serialVersionUID = 751068795564006998L;
	private AqpFunctionQueryBuilder builder = null;
	
	public AqpFunctionQueryNode(AqpFunctionQueryBuilder builder, List<QueryNode> children) {
		allocate();
		setLeaf(false);
		add(children);
		this.builder = builder;
	}

	public String toString() {
		StringBuffer bo = new StringBuffer();
		bo.append("<function name=\"");
		bo.append(getBuilder().getClass());
		bo.append("\">\n");
		for (QueryNode child: this.getChildren()) {
			bo.append(child.toString());
		}
		bo.append("\n</function>");
		return bo.toString();
	}
	
	
	public QueryNode getChild() {
		return getChildren().get(0);
	}

	public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
		return getBuilder().toString();
	}
	
	public AqpFunctionQueryBuilder getBuilder() {
		return builder;
	}
	
}
