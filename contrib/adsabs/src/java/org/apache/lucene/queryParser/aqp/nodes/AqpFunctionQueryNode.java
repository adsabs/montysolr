package org.apache.lucene.queryParser.aqp.nodes;

import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;

/**
 * This QNode receives all the children of the QFUNC node, so typically
 *                            QFUNC
 *                              |
 *                            /   \
 *                        fName   DEFOP
 *                                  |
 *                                COMMA
 *                                  |
 *                             /    |     \
 *                          ...  MODIFIER  ...
 *                                  |
 *                              TMODIFIER
 *                                  |
 *                                FIELD
 *                                  |
 *                                Q<node>
 *                                  |
 *                                <value>
 * @author rchyla
 *
 */
public class AqpFunctionQueryNode extends QueryNodeImpl implements QueryNode {

	private static final long serialVersionUID = 751068795564006998L;
	private QueryBuilder builder = null;
	private String name = null;
	
	public AqpFunctionQueryNode(String name, QueryBuilder builder, QueryNode node) {
		allocate();
		setLeaf(false);
		//add(node.getChildren().get(1).getChildren()); // we keep only the values
		//add(node.getChildren().get(1));
		add(node.getChildren());
		this.builder = builder;
		this.name = name;
	}

	public String toString() {
		StringBuffer bo = new StringBuffer();
		bo.append("<function name=\"");
		bo.append(this.name == null ? getBuilder().getClass() : this.name);
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
	
	public QueryBuilder getBuilder() {
		return builder;
	}
	
	public String getName() {
		return name;
	}
	
}
