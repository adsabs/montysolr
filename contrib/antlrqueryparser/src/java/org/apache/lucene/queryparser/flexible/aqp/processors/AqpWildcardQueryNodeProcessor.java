package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.processors.WildcardQueryNodeProcessor;

/** modification to the standard Wildcard processor which treats
 *  field:* queries to be wildcard queries. But it is more efficient
 *  to make them prefix queries. This behaviour has changed by 
 *  LUCENE-7355
 */

public class AqpWildcardQueryNodeProcessor extends WildcardQueryNodeProcessor {

  @Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
    if (node instanceof PrefixWildcardQueryNode) {
      return node; // do nothing
    }
    else if (node instanceof FieldQueryNode || node instanceof FuzzyQueryNode) {
      FieldQueryNode fqn = (FieldQueryNode) node;      
      CharSequence text = fqn.getText();
      if (text.length() == 1 && text.charAt(0) == '*')
        return new PrefixWildcardQueryNode(fqn.getField(), text, fqn.getBegin(), fqn.getEnd());
    }
    return super.postProcessNode(node);
  }
}
