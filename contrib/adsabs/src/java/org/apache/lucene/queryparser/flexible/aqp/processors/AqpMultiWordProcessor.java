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
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.queryparser.flexible.aqp.AqpDEFOPMarkPlainNodes;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpDefopQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpOrQueryNode;
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

	private CachingTokenFilter buffer;
	private CharTermAttribute termAtt;
	private NumericTermAttribute numAtt;
	private TypeAttribute typeAtt;
	
	public AqpMultiWordProcessor() {
		// empty
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
	protected QueryNode postProcessNode(QueryNode node)
	throws QueryNodeException {
		
		if (node instanceof AqpDefopQueryNode) {
			LinkedList<QueryNode> newChildren = new LinkedList<QueryNode>();
			List<QueryNode> children = node.getChildren();
			
			String multiToken;
			Integer groupId;
			Integer grpReplaced = -1;
			
			for (QueryNode child: children) {
				QueryNode terminalNode = getTerminalNode(child);
				
				if (terminalNode == null) {
					newChildren.add(child);
					continue;
				}
				
				groupId = (Integer) terminalNode.getTag(AqpDEFOPMarkPlainNodes.PLAIN_TOKEN);
				
				if (groupId.equals(grpReplaced)) {
					continue;
				}
				
				multiToken = (String) terminalNode.getTag(AqpDEFOPMarkPlainNodes.PLAIN_TOKEN_CONCATENATED);
				if (multiToken != null) {
					
					if (analyzeMultiToken(((FieldableNode) terminalNode).getField(), multiToken) > 0) {
						newChildren.add(expandMultiToken(terminalNode));
						grpReplaced = groupId;
					}
					else {
						newChildren.add(child);
					}
					
				}
				else {
					newChildren.add(child);
				}
			}
			node.set(newChildren);
		}
		return node;
		
	}


	private QueryNode getTerminalNode(QueryNode node) {
		if (node.isLeaf()) {
			return null;
		}
		for (QueryNode child: node.getChildren()) {
			if (child.containsTag(AqpDEFOPMarkPlainNodes.PLAIN_TOKEN)) {
				return child;
			}
			QueryNode nn = getTerminalNode(child);
			if (nn != null) 
				return nn;
		}
		return null;
	}
	
	private int analyzeMultiToken(CharSequence field, String multiToken) {
			

		QueryConfigHandler config = this.getQueryConfigHandler();

		Locale locale = getQueryConfigHandler().get(ConfigurationKeys.LOCALE);
		if (locale == null) {
			locale = Locale.getDefault();
		}
		Analyzer analyzer = config.get(StandardQueryConfigHandler.ConfigurationKeys.ANALYZER);

		TokenStream source = null;
		try {
			source = analyzer.tokenStream(field.toString(),
					new StringReader(multiToken));
			source.reset();
		} catch (IOException e1) {
			return -1;
		}

		buffer = new CachingTokenFilter(source);
		int numSynonyms = 0;

		try {
			while (buffer.incrementToken()) {
				typeAtt = buffer.getAttribute(TypeAttribute.class);
				if (typeAtt.type().equals(SynonymFilter.TYPE_SYNONYM)) {
					numSynonyms++;
				}
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
		
		return numSynonyms;
	}

	@Override
	protected QueryNode preProcessNode(QueryNode node)
	throws QueryNodeException {
		return node;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
	throws QueryNodeException {
		return children;
	}

	protected QueryNode expandMultiToken(QueryNode node) throws QueryNodeException {

		FieldableNode fieldNode = (FieldableNode) node;


		LinkedList<QueryNode> children = new LinkedList<QueryNode>();
		//children.add(new AqpNonAnalyzedQueryNode((FieldQueryNode) fieldNode)); // original input

		buffer.reset();
		

		try {
			while (buffer.incrementToken()) {
				
				typeAtt = buffer.getAttribute(TypeAttribute.class);
				if (!typeAtt.type().equals(SynonymFilter.TYPE_SYNONYM)) {
					continue;
				}
				
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
					"This should never hapeeeeennnn! Error expanding synonyms for: "
					+ node.toString() + ""));
		}

		return new GroupQueryNode(new AqpOrQueryNode(children));

	}
}