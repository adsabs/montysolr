package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * A generic class that is used by other query processors, eg.
 * {@link AqpQNORMALProcessor}
 * 
 * 
 *
 */
public class AqpQProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {
	
	public String defaultField = null;
	
	
	@Override
	protected QueryNode preProcessNode(QueryNode node)
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
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}
	
	
	public boolean nodeIsWanted(AqpANTLRNode node) {
		throw new NotImplementedException();
	}
	
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		throw new NotImplementedException();
	}
	
	public String getDefaultFieldName() throws QueryNodeException {
		QueryConfigHandler queryConfig = getQueryConfigHandler();
		
		String defaultField = null;
		if (queryConfig != null) {
			
			if (queryConfig.hasAttribute(DefaultFieldAttribute.class)) {
				defaultField = queryConfig.getAttribute(
						DefaultFieldAttribute.class).getDefaultField();
				return defaultField;
			}
		}
		throw new QueryNodeException(new MessageImpl(
                QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                "Configuration error: " + DefaultFieldAttribute.class.toString() + " is missing"));
	}

}
