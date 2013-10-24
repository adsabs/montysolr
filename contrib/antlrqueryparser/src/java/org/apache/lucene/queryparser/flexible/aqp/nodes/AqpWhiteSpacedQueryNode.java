package org.apache.lucene.queryparser.flexible.aqp.nodes;

import org.apache.lucene.queryparser.flexible.aqp.processors.AqpDEFOPUnfieldedTokens;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpUnfieldedSearchProcessor;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;

/**
 * This node will be turned into the {@link AqpFunctionQueryNode} by 
 * {@link AqpUnfieldedSearchProcessor}. This node is created by 
 * {@link AqpDEFOPUnfieldedTokens} processor from a group of words
 * separated by spaces, these should be ideally reparsed again
 * 
 * The field, if present, means that the first token in the group
 * had a field, eg. author:lee, h c
 */
public class AqpWhiteSpacedQueryNode extends FieldQueryNode {

  public AqpWhiteSpacedQueryNode(CharSequence field, CharSequence text,
      int begin, int end) {
    super(field, text, begin, end);
  }

  public AqpWhiteSpacedQueryNode(FieldQueryNode fqn) {
    this(fqn.getField(), fqn.getText(), fqn.getBegin(), fqn.getEnd());
  }

  @Override
  public String toString() {
    return "<whitespace field='" + this.field + "' term='" + this.text + "' start=\"" + this.begin + "\" end=\"" + this.end + "\"/>";
  }

  @Override
  public AqpWhiteSpacedQueryNode cloneTree() throws CloneNotSupportedException {
    AqpWhiteSpacedQueryNode clone = (AqpWhiteSpacedQueryNode) super.cloneTree();
    // nothing to do here
    return clone;
  }

}
