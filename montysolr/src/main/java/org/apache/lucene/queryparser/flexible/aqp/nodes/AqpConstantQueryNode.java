package org.apache.lucene.queryparser.flexible.aqp.nodes;

import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;

public class AqpConstantQueryNode extends QueryNodeImpl implements QueryNode {

    public AqpConstantQueryNode(QueryNode nested) {
        setLeaf(false);
        allocate();
        add(nested);
    }

    @Override
    public String toString() {
        StringBuffer bo = new StringBuffer();
        bo.append("<constant>\n");
        for (QueryNode child : this.getChildren()) {
            bo.append("<child>" + child.toString() + "</child>\n");
        }
        bo.append("</constant>");
        return bo.toString();
    }

    @Override
    public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
        return "constant(" + this.getChildren().get(0).toQueryString(escapeSyntaxParser) + ")";
    }

}
