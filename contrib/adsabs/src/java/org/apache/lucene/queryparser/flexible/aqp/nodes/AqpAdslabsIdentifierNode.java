package org.apache.lucene.queryparser.flexible.aqp.nodes;

import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;

public class AqpAdslabsIdentifierNode extends FieldQueryNode {


	private static final long serialVersionUID = 2818797741887759988L;

	/**
	   * @param field
	   *          - field name
	   * @param text
	   *          - value
	   * @param begin
	   *          - position in the query string
	   * @param end
	   *          - position in the query string
	   */
	  public AqpAdslabsIdentifierNode(CharSequence field, CharSequence text, int begin,
	      int end) {
	    super(field, text, begin, end);
	  }

	  @Override
	  public CharSequence toQueryString(EscapeQuerySyntax escaper) {
	    if (isDefaultField(this.field)) {
	      return "\"" + getTermEscapeQuoted(escaper) + "\"";
	    } else {
	      return this.field + ":" + "\"" + getTermEscapeQuoted(escaper) + "\"";
	    }
	  }

	  @Override
	  public String toString() {
	    return "<identifierfield start='" + this.begin + "' end='" + this.end
	        + "' field='" + this.field + "' term='" + this.text + "'/>";
	  }

	  @Override
	  public QuotedFieldQueryNode cloneTree() throws CloneNotSupportedException {
	    QuotedFieldQueryNode clone = (QuotedFieldQueryNode) super.cloneTree();
	    // nothing to do here
	    return clone;
	  }

	}
