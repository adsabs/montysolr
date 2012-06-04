package org.apache.lucene.queryParser.aqp.processors;

import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.config.AqpRequestParams;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryParser.core.nodes.ParametricQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.util.UnescapedCharSequence;
import org.apache.lucene.queryParser.standard.config.AnalyzerAttribute;
import org.apache.lucene.queryParser.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryParser.standard.processors.LowercaseExpandedTermsQueryNodeProcessor;

public class AqpLowercaseExpandedTermsQueryNodeProcessor extends
		LowercaseExpandedTermsQueryNodeProcessor {

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {

		if (node instanceof WildcardQueryNode || node instanceof FuzzyQueryNode
				|| node instanceof ParametricQueryNode) {

			FieldQueryNode fieldNode = (FieldQueryNode) node;
			
			
			// if we have the SOLR analyzer, we pass it to it 
			// and get the analyzed value - otherwise we use the default
			// lowercasing
			QueryConfigHandler config = this.getQueryConfigHandler();
			if (config.hasAttribute(AqpRequestParams.class)
					&& config.getAttribute(AqpRequestParams.class).getRequest() != null) {
				Analyzer analyzer = config
						.getAttribute(AnalyzerAttribute.class).getAnalyzer();

				TokenStream source = analyzer.tokenStream(fieldNode
						.getFieldAsString(),
						new StringReader(fieldNode.getTextAsString()));

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

				CharTermAttribute termAtt = buffer
						.getAttribute(CharTermAttribute.class);
				if (numTokens == 0) {
					// do nothing, change nothing
				}
				else if (numTokens == 1) {
					try {
						buffer.incrementToken();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					fieldNode.setText(termAtt.toString());

				} else {
					throw new QueryNodeException(new MessageImpl(
							QueryParserMessages.PARAMETER_VALUE_NOT_SUPPORTED,
							"The solr analyzer returned more than one value when processing "
									+ fieldNode.getFieldAsString() + ":"
									+ fieldNode.getTextAsString()));
				}

			}
			else {

				fieldNode.setText(UnescapedCharSequence.toLowerCase(fieldNode
					.getText()));
			}
		}

		return node;

	}
}
