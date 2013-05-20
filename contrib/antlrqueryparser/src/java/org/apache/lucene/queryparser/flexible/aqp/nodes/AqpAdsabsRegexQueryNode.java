package org.apache.lucene.queryparser.flexible.aqp.nodes;

import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFieldQueryNodeRegexBuilder;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.search.RegexpQuery;

/**
 * This node will be turned into the {@link RegexpQuery} by 
 * {@link AqpFieldQueryNodeRegexBuilder}. But the appropriate 
 * builder must be configured. 
 * 
 * @see instances of {@link QueryTreeBuilder}
 *
 */
public class AqpAdsabsRegexQueryNode extends AqpNonAnalyzedQueryNode {

  public AqpAdsabsRegexQueryNode(CharSequence field, CharSequence text,
      int begin, int end) {
    super(field, text, begin, end);
  }

  public AqpAdsabsRegexQueryNode(FieldQueryNode fqn) {
    this(fqn.getField(), fqn.getText(), fqn.getBegin(), fqn.getEnd());
  }

  @Override
  public String toString() {
    return "<regex field='" + this.field + "' term='" + this.text + "'/>";
  }

  @Override
  public AqpAdsabsRegexQueryNode cloneTree() throws CloneNotSupportedException {
    AqpAdsabsRegexQueryNode clone = (AqpAdsabsRegexQueryNode) super.cloneTree();

    // nothing to do here

    return clone;
  }

}
