package org.apache.lucene.queryparser.flexible.aqp.nodes;

import java.util.List;

import org.apache.lucene.queryparser.flexible.core.QueryNodeError;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;

public class AqpDisjunctionQueryNode extends QueryNodeImpl {

  private Float tieBreaker = null;

  public AqpDisjunctionQueryNode(List<QueryNode> children, float tieBreaker) {
    if (children == null) {
      throw new QueryNodeError(
          new MessageImpl(QueryParserMessages.PARAMETER_VALUE_NOT_SUPPORTED,
              "children", "null"));
    }
    allocate();
    setLeaf(false);
    add(children);
    this.tieBreaker = tieBreaker;
  }

  public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
    if (getChildren().size() == 0)
      return "";

    StringBuilder sb = new StringBuilder();
    sb.append("(");
    boolean notFirst = false;
    for (QueryNode child : getChildren()) {
      if (notFirst) {
        sb.append(" | ");
      }
      sb.append(child.toQueryString(escapeSyntaxParser));
      notFirst = true;
    }
    sb.append(")");
    return sb.toString();

  }

  public String toString() {
    StringBuffer bo = new StringBuffer();
    bo.append("<disjunction tieBreaker=\"");
    bo.append(tieBreaker);
    bo.append("\">\n");
    for (QueryNode child : this.getChildren()) {
      bo.append(child.toString());
      bo.append("\n");
    }
    bo.append("\n</disjunction>");
    return bo.toString();
  }

  public Float getTieBreaker() {
    return tieBreaker;
  }

  public void setTieBreaker(Float tieBreaker) {
    this.tieBreaker = tieBreaker;
  }

}
