package org.apache.lucene.queryParser.aqp.processors;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.nodes.QueryNode;


/**
 * The class that is used for ANTLR query nodes, it is the
 * same as {@link AqpQPHRASEProcessor}, the only difference is
 * that it works in postProcessor phase (instead of pre-process
 * phase)
 * 
 * @author rchyla
 *
 */

public class AqpQProcessorPost extends AqpQProcessor {
	
	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpANTLRNode) {
			AqpANTLRNode n = (AqpANTLRNode) node;
			if (nodeIsWanted(n)) {
				return createQNode(n);
			}
		}
		return node;
	}

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}
	
	@Override
	public boolean nodeIsWanted(AqpANTLRNode node) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		throw new UnsupportedOperationException();
	}
}
