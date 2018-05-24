package org.apache.lucene.queryparser.flexible.aqp.nodes;

import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;

public class AqpConstantQueryNode extends QueryNodeImpl implements QueryNode {

  public AqpConstantQueryNode(QueryNode nested) {
    allocate();
    setLeaf(true);
    getChildren().add(nested);
  }

  @Override
  public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
    StringBuffer bo = new StringBuffer();
    bo.append("<constant>\n");
    for (QueryNode child: this.getChildren()) {
      bo.append("<child>" + child.toString() + "</child>\n");
    }
    bo.append("</constant>");
    return bo.toString();
  }

}
