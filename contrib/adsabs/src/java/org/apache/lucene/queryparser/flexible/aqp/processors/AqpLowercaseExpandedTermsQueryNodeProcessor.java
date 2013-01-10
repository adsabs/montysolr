package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.processors.LowercaseExpandedTermsQueryNodeProcessor;

/**
 * A modified version of the {@link LowercaseExpandedTermsQueryNodeProcessor}
 * 
 * We prevent lowercasing for {@link AqpNonAnalyzedQueryNode}
 * 
 * LUCENE-4679
 * 
 * @author rchyla
 * 
 */
public class AqpLowercaseExpandedTermsQueryNodeProcessor extends
LowercaseExpandedTermsQueryNodeProcessor {

	
	@Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
	  if (node instanceof AqpNonAnalyzedQueryNode) {
	    return node;
	  }
	  return super.postProcessNode(node);
	}
}
