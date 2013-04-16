package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.standard.nodes.TermRangeQueryNode;
import org.apache.solr.schema.DateField;
import org.apache.solr.util.DateMathParser;

/**
 * Hand-made modifications that catch the exceptions or enhancements to
 * the AQP ADSABS parsing
 * 
 * @see AqpFieldMapper
 * @see QueryConfigHandler
 * @author rchyla
 * 
 */
public class AqpAdsabsFieldNodePreAnalysisProcessor extends QueryNodeProcessorImpl {

	private DateMathParser dmp;
  private SimpleDateFormat sdf;

  public AqpAdsabsFieldNodePreAnalysisProcessor() {
	  super();
	  dmp = new DateMathParser(DateField.UTC, Locale.ROOT);
	  sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT);
    sdf.setTimeZone(DateField.UTC);
	}

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
	  
	  
	  if (node instanceof FieldQueryNode) {
	    FieldQueryNode fieldNode = (FieldQueryNode) node;
      String field = ((FieldQueryNode) node).getFieldAsString();
      
      // we must detect pubdate:YYYY(-MM-DD) queries, and turn them into range query if necessary
      if (field.equals("pubdate")){  
        // first parse the date with the appropriate analyzer
        String value = fieldNode.getTextAsString();
        Analyzer analyzer = getQueryConfigHandler().get(ConfigurationKeys.ANALYZER);
        TokenStream source;
        try {
          source = analyzer.tokenStream(field, new StringReader(value));
          source.reset();
          source.incrementToken();
        } catch (IOException e1) {
          throw new RuntimeException(e1);
        }
        
        Date date = null;
        String normalizedDate=null;
        try {
          // get the result, then apply appropriate offset
          normalizedDate = source.getAttribute(CharTermAttribute.class).toString();
          date = sdf.parse(normalizedDate);
          dmp.setNow(date);
          
          String[] parts = value.split("-|/");
          if (parts.length == 1) { // just a year
            date = dmp.parseMath("/YEAR");
            date = dmp.parseMath("+1YEAR");
          }
          else if (parts.length == 2) {
            date = dmp.parseMath("/MONTH");
            date = dmp.parseMath("+1MONTH");
          }
          else {
            date = dmp.parseMath("/DAY");
            date = dmp.parseMath("+1DAY");
          }
        } catch (ParseException e) {
          throw new QueryNodeException(new MessageImpl(e.getMessage()));
        }
        
        // when we are called to parse values that are already inside range QNode
        if (node.getParent() instanceof TermRangeQueryNode) {
        	fieldNode.setField("date");
          fieldNode.setValue(normalizedDate);
        	return new AqpNonAnalyzedQueryNode(fieldNode);
        }

        // make a copy of the pubdate node
        FieldQueryNode upperBound;
        try {
          upperBound = fieldNode.cloneTree();
        } catch (CloneNotSupportedException e) {
          throw new QueryNodeException(new MessageImpl(e.getMessage()));
        }
        
        upperBound.setField("date");
        upperBound.setValue(DateField.formatExternal(date));
        
        fieldNode.setField("date");
        fieldNode.setValue(normalizedDate);
        
        
        return new TermRangeQueryNode(new AqpNonAnalyzedQueryNode(fieldNode), 
            new AqpNonAnalyzedQueryNode(upperBound), true, false); // not include the lowerBound
      }
        
    }
	  return node;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}

}
