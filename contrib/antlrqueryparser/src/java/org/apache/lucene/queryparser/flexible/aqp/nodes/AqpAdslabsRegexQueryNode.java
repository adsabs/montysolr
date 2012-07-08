package org.apache.lucene.queryparser.flexible.aqp.nodes;

import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;

public class AqpAdslabsRegexQueryNode extends AqpNonAnalyzedQueryNode {

	public AqpAdslabsRegexQueryNode(CharSequence field, CharSequence text,
			int begin, int end) {
		super(field, text, begin, end);
	}

	public AqpAdslabsRegexQueryNode(FieldQueryNode fqn) {
	    this(fqn.getField(), fqn.getText(), fqn.getBegin(), fqn.getEnd());
	}

	  @Override
	  public String toString() {
	    return "<regex field='" + this.field + "' term='" + this.text + "'/>";
	  }

	  @Override
	  public AqpAdslabsRegexQueryNode cloneTree() throws CloneNotSupportedException {
	    AqpAdslabsRegexQueryNode clone = (AqpAdslabsRegexQueryNode) super.cloneTree();

	    // nothing to do here

	    return clone;
	  }

}
