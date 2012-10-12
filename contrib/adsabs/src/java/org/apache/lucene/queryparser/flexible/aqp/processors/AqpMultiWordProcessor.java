package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.NumericTokenStream.NumericTermAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryparser.flexible.aqp.AqpDEFOPMarkPlainNodes;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldableNode;
import org.apache.lucene.queryparser.flexible.core.nodes.GroupQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.TextableQueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.core.util.UnescapedCharSequence;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;

public class AqpMultiWordProcessor extends QueryNodeProcessorImpl {


	public AqpMultiWordProcessor() {
		// empty constructor
	}

	@Override
	public QueryNode process(QueryNode queryTree) throws QueryNodeException {
		QueryConfigHandler config = this.getQueryConfigHandler();

		if (config.has(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
				&& config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
				.getRequest() != null) {
			return super.process(queryTree);
		}

		return queryTree;
	}

	@Override
	protected QueryNode preProcessNode(QueryNode node)
	throws QueryNodeException {
		if (node instanceof FieldQueryNode) {
			if (!node.containsTag(AqpDEFOPMarkPlainNodes.NODE_TYPE)) {
				return node;
			}

			String multiToken = (String) node.getTag(AqpDEFOPMarkPlainNodes.TOKEN_CONCATENATED);
			if (multiToken == null)
				return node;

			QueryConfigHandler config = this.getQueryConfigHandler();

			Locale locale = getQueryConfigHandler().get(ConfigurationKeys.LOCALE);
			if (locale == null) {
				locale = Locale.getDefault();
			}

			FieldableNode fieldNode = (FieldableNode) node;
			TextableQueryNode txtNode = (TextableQueryNode) node;

			CharSequence text = txtNode.getText();
			CharSequence field = fieldNode.getField();

			Analyzer analyzer = config.get(StandardQueryConfigHandler.ConfigurationKeys.ANALYZER);

			TokenStream source = null;
			try {
				source = analyzer.tokenStream(field.toString(),
						new StringReader(multiToken));
				source.reset();
			} catch (IOException e1) {
				txtNode.setText(text != null ? UnescapedCharSequence.toLowerCase(text,
						locale) : null);
				return node;
			}

			CachingTokenFilter buffer = new CachingTokenFilter(source);

			int numTokens = 0;

			try {
				while (buffer.incrementToken()) {
					numTokens++;
				}
			} catch (IOException e) {
				// pass
			}

			try {
				// rewind the buffer stream
				buffer.reset();

				// close original stream - all tokens buffered
				source.close();
			} catch (IOException e) {
				// ignore
			}


			if (numTokens == 0) {
				// do nothing, change nothing
			} else if (numTokens == 1) {
				try {
					buffer.incrementToken();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (buffer.hasAttribute(CharTermAttribute.class)) {
					CharTermAttribute termAtt = buffer.getAttribute(CharTermAttribute.class);
					txtNode.setText(termAtt.toString());
				}
				else {
					NumericTermAttribute numAtt = buffer.getAttribute(NumericTermAttribute.class);
					txtNode.setText(new Long(numAtt.getRawValue()).toString());
				}

			} else {
				return expandNode(node, field, text, buffer);
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

	protected QueryNode expandNode(QueryNode node, CharSequence field, 
			CharSequence text, CachingTokenFilter buffer) throws QueryNodeException {

		FieldableNode fieldNode = (FieldableNode) node;


		LinkedList<QueryNode> children = new LinkedList<QueryNode>();
		children.add(new AqpNonAnalyzedQueryNode((FieldQueryNode) fieldNode)); // original input

		buffer.reset();
		CharTermAttribute termAtt;
		NumericTermAttribute numAtt;

		try {
			while (buffer.incrementToken()) {
				FieldableNode newNode = (FieldableNode) fieldNode.cloneTree();

				if (buffer.hasAttribute(CharTermAttribute.class)) {
					termAtt = buffer.getAttribute(CharTermAttribute.class);
					((TextableQueryNode) newNode).setText(termAtt.toString());
				}
				else {
					numAtt = buffer.getAttribute(NumericTermAttribute.class);
					((TextableQueryNode) newNode).setText(new Long(numAtt.getRawValue()).toString());
				}

				children.add(new AqpNonAnalyzedQueryNode((FieldQueryNode) newNode));
			}
		} catch (IOException e) {
			getQueryConfigHandler().get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_LOGGER).error(e.getLocalizedMessage());
		} catch (CloneNotSupportedException e) {
			getQueryConfigHandler().get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_LOGGER).error(e.getLocalizedMessage());
		}



		if (children.size() < 1) {
			throw new QueryNodeException(new MessageImpl(
					QueryParserMessages.PARAMETER_VALUE_NOT_SUPPORTED,
					"The solr analyzer returned more than one value when processing "
					+ field + ":" + text + ". However, all in vain"));
		}


		/*
		 * TODO: add a flexible factory that knows how to handle the 
		 * children or raise error
		 */

		return new GroupQueryNode(new BooleanQueryNode(children));

	}
}