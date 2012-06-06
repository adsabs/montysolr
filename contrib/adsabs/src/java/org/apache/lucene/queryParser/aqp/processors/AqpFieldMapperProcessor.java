package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;
import java.util.Map;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.config.AqpFieldMapper;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;

/**
 * Looks at the QueryNode(s) and translates the field name if we have a mapping
 * for it. It is used to change the field names on-the-fly
 * 
 * @see AqpFieldMapper
 * @see QueryConfigHandler
 * @author rchyla
 * 
 */
public class AqpFieldMapperProcessor extends QueryNodeProcessorImpl {

	private Map<String, String> fieldMap;

	public AqpFieldMapperProcessor() {
		// empty constructor
	}

	@Override
	public QueryNode process(QueryNode queryTree) throws QueryNodeException {

		if (getQueryConfigHandler().hasAttribute(AqpFieldMapper.class)) {

			fieldMap = getQueryConfigHandler().getAttribute(
					AqpFieldMapper.class).getMap();

			if (this.fieldMap != null) {
				return super.process(queryTree);
			}
		}

		return queryTree;
	}

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof FieldQueryNode) {
			String field = ((FieldQueryNode) node).getFieldAsString();
			if (fieldMap.containsKey(field)) {
				String newField = fieldMap.get(field);
				if (newField == null) {
					throw new QueryNodeException(new MessageImpl(
							"Server error", "The configuration error, field "
									+ field + " is translated to: null"));
				}
				((FieldQueryNode) node).setField(newField);
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

}
