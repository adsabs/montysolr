package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpRequestParams;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.SlowFuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldableNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.RangeQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.TextableQueryNode;
import org.apache.lucene.queryparser.flexible.core.util.UnescapedCharSequence;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.standard.nodes.RegexpQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.TermRangeQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.processors.LowercaseExpandedTermsQueryNodeProcessor;

/**
 * A modified version of the {@link LowercaseExpandedTermsQueryNodeProcessor} We
 * will try to use the Tokenizer chain of the given index field (if possible)
 * 
 * @author rchyla
 * 
 */
public class AqpLowercaseExpandedTermsQueryNodeProcessor extends
    LowercaseExpandedTermsQueryNodeProcessor {

  @Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {

    QueryConfigHandler config = this.getQueryConfigHandler();

    // if we have the SOLR analyzer, we pass it to it
    // and get the analyzed value - otherwise we use the default
    // lowercasing
    if (node instanceof FieldableNode
        && node instanceof TextableQueryNode
        && config
            .has(AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
        && config.get(
            AqpAdsabsQueryConfigHandler.ConfigurationKeys.SOLR_REQUEST)
            .getRequest() != null) {

      Locale locale = getQueryConfigHandler().get(ConfigurationKeys.LOCALE);
      if (locale == null) {
        locale = Locale.getDefault();
      }

      FieldableNode fieldNode = (FieldableNode) node;
      TextableQueryNode txtNode = (TextableQueryNode) node;

      CharSequence text = txtNode.getText();
      CharSequence field = fieldNode.getField();

      Analyzer analyzer = config
          .get(StandardQueryConfigHandler.ConfigurationKeys.ANALYZER);

      // TODO: the analyzer chain changed, so maybe we are not doing the right
      // thing here
      TokenStream source = null;
      try {
        source = analyzer.tokenStream(field.toString(),
            new StringReader(text.toString()));
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

      CharTermAttribute termAtt = buffer.getAttribute(CharTermAttribute.class);
      if (numTokens == 0) {
        // do nothing, change nothing
      } else if (numTokens == 1) {
        try {
          buffer.incrementToken();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        txtNode.setText(termAtt.toString());

      } else {
        throw new QueryNodeException(new MessageImpl(
            QueryParserMessages.PARAMETER_VALUE_NOT_SUPPORTED,
            "The solr analyzer returned more than one value when processing "
                + field + ":" + text));
      }

    } else {
      return super.postProcessNode(node);
    }

    return node;

  }
}
