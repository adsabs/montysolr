package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

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
	  dmp = new DateMathParser(DateMathParser.UTC);
    
    sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT);
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
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
      // ie. rewrite (pub)date fieldquery into termrange query
      
      if (field.equals("pubdate")){
      	
        // first parse the date with the appropriate analyzer
        String value = fieldNode.getTextAsString();
        boolean dateGuessed = false;
        
        if (value.equals("*") && node.getParent() instanceof TermRangeQueryNode) {
        	QueryNode theOtherNode = null;
        	for (QueryNode ch: node.getParent().getChildren()) {
        		if (ch != fieldNode) {
        			theOtherNode = ch;
        			break;
        		}
        	}
        	if (theOtherNode != null) {
        		String theOtherValue = ((FieldQueryNode) theOtherNode).getTextAsString();
        		value = theOtherValue;
        		dateGuessed = true;
        	}
        }
        
        Analyzer analyzer = getQueryConfigHandler().get(ConfigurationKeys.ANALYZER);
        TokenStream source = null;
        try {
          source = analyzer.tokenStream(field, new StringReader(value));
          source.reset();
          source.incrementToken();
        } catch (IOException e1) {
          throw new RuntimeException(e1);
        }
        finally {
        	if (source != null) {
        		try {
        			source.close();
        		} catch (IOException e) {
        			// ignore
        		}
        	}
        }
        
        Date dateWithoutOffset = null;
        String normalizedDate= source.getAttribute(CharTermAttribute.class).toString();
        try {
          dateWithoutOffset = sdf.parse(normalizedDate);
          dmp.setNow(dateWithoutOffset);
          
        } catch (ParseException e) {
          throw new QueryNodeException(new MessageImpl(e.getMessage()));
        }
        
        // if we are already inside TermRangeQuery, we just need
        // to change the field and value
        if (node.getParent() instanceof TermRangeQueryNode) {
        	fieldNode.setField("date");
        	if (node.getParent().getChildren().get(0) == fieldNode) { // lower bound
        		if (dateGuessed) {
        			if (fieldNode.getBegin() > 0) { // user typed '*'
        				fieldNode.setValue(moveDate(value, dateWithoutOffset, 
	        				"/YEAR-1000YEAR+1SECOND", "/MONTH-1000YEAR+1SECOND", "/DAY-1000YEAR+1SECOND"));
        			}
        			else {
	        			fieldNode.setValue(moveDate(value, dateWithoutOffset, 
	        				"/YEAR-1YEAR+1SECOND", "/MONTH-1MONTH+1SECOND", "/DAY-1DAY+1SECOND"));
        			}
        		}
        		else {
        			fieldNode.setValue(normalizedDate);
        		}
        	}
        	else { // upper bound
        		if (dateGuessed) {
        			if (fieldNode.getBegin() > 0) { // user typed '*'
        				fieldNode.setValue(moveDate(value, dateWithoutOffset, 
	        				"/YEAR+1000YEAR-1SECOND", "/MONTH+1000YEAR-1SECOND", "/DAY+1000YEAR-1SECOND"));
        			}
        			else {
	        			fieldNode.setValue(moveDate(value, dateWithoutOffset, 
	        				"/YEAR+1YEAR-1SECOND", "/MONTH+1MONTH-1SECOND", "/DAY+1DAY-1SECOND"));
        			}
        		}
        		else {
        			fieldNode.setValue(moveDate(value, dateWithoutOffset, 
        				"/YEAR+1YEAR-1SECOND", "/MONTH+1MONTH-1SECOND", "/DAY+1DAY-1SECOND"));
        		}
        		
        	}
          
        	return new AqpNonAnalyzedQueryNode(fieldNode);
        }

        // make a copy of the pubdate node
        FieldQueryNode upperBound;
        try {
          upperBound = fieldNode.cloneTree();
        } catch (CloneNotSupportedException e) {
          throw new QueryNodeException(new MessageImpl(e.getMessage()));
        }

        FieldQueryNode lowerBound = fieldNode;
        lowerBound.setField("date");
        lowerBound.setValue(normalizedDate);
        
        upperBound.setField("date");
        upperBound.setValue(moveDate(value, dateWithoutOffset, 
        		"/YEAR+1YEAR", "/MONTH+1MONTH", "/DAY+1DAY"));
        
        
        
        return new TermRangeQueryNode(
        		new AqpNonAnalyzedQueryNode(lowerBound), 
            new AqpNonAnalyzedQueryNode(upperBound), 
            true, false); // upper bound non-inclusive
      }
        
    }
	  return node;
	}

	@SuppressWarnings("deprecation")
  private String moveDate(
			String originalDate, 
			Date parsedDate,
			String...moveBy) throws QueryNodeException {
		String[] dateParts = originalDate.split("-|/");
		Date dateWithOffset = (Date) parsedDate.clone();
		
		try {
			if (dateParts.length == 1) { // just a year
				assert moveBy.length >= 1;
				dateWithOffset = dmp.parseMath(moveBy[0]); // "+1YEAR" = move to the next year
			}
			else if (dateParts.length == 2) {
				assert moveBy.length >= 2;
				dateWithOffset = dmp.parseMath(moveBy[1]); // "+1MONTH"
			}
			else {
				assert moveBy.length == 3;
				dateWithOffset = dmp.parseMath(moveBy[2]); // "+1DAY"
			}
		} catch (ParseException e) {
			throw new QueryNodeException(new MessageImpl(e.getMessage()));
		}
		
		
    return sdf.format(dateWithOffset);
    
	}
	
	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}

}
