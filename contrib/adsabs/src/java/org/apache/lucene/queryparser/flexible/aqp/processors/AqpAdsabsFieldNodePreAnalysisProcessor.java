package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
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
 * @see AqpFieldMapperProcessor
 * @see QueryConfigHandler
 * 
 */
public class AqpAdsabsFieldNodePreAnalysisProcessor extends AqpQueryNodeProcessorImpl {

	private DateMathParser dmp;
  private Map<String, TargetField> dehumnzdDates;

  public AqpAdsabsFieldNodePreAnalysisProcessor() {
	  super();
	  dmp = new DateMathParser(DateMathParser.UTC);
    
    

	}
  
  private Map<String, TargetField> getFields() {
    if (dehumnzdDates != null)
      return dehumnzdDates;
    
    dehumnzdDates = new HashMap<String, TargetField>();
    Object pairs = getConfigVal("aqp.humanized.dates");
    if (pairs != null) {
      
      String datef = (String) getConfigVal("aqp.dateFormat");
      String timef = (String) getConfigVal("aqp.timestampFormat");
      
      SimpleDateFormat ddf = new SimpleDateFormat(datef, Locale.US);
      ddf.setTimeZone(TimeZone.getTimeZone("UTC"));
      
      SimpleDateFormat tdf = new SimpleDateFormat(timef, Locale.US);
      tdf.setTimeZone(TimeZone.getTimeZone("UTC"));
      
      for (String pair: ((String) pairs).split(",")) {
        String[] kv = pair.split(":");
        if (kv.length == 2) {
          dehumnzdDates.put(kv[0], new TargetField(kv[1], ddf, ddf));
        }
        else if(kv.length == 3) {
          dehumnzdDates.put(kv[0], new TargetField(kv[1], ddf, kv[2].equals("timestamp") ? tdf : ddf));
        }
        else {
          throw new RuntimeException("Misconfiguratin in aqp.humanized.dates: " + pair);
        }
      }
    }
    return dehumnzdDates;
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
	    
	    Map<String, TargetField> hFields = getFields();
	    
	    FieldQueryNode fieldNode = (FieldQueryNode) node;
      String field = ((FieldQueryNode) node).getFieldAsString();
      
      // we must detect pubdate:YYYY(-MM-DD) queries, and turn them into range query if necessary
      // ie. rewrite (pub)date fieldquery into termrange query
      
      if (hFields.containsKey(field)){
      	TargetField targetDateField = hFields.get(field);
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
          dateWithoutOffset = targetDateField.parse(normalizedDate);
          dmp.setNow(dateWithoutOffset);
          
        } catch (ParseException e) {
          throw new QueryNodeException(new MessageImpl(e.getMessage()));
        }
        
        // if we are already inside TermRangeQuery, we just need
        // to change the field and value
        if (node.getParent() instanceof TermRangeQueryNode) {
        	fieldNode.setField(targetDateField.fieldname);
        	if (node.getParent().getChildren().get(0) == fieldNode) { // lower bound
        		if (dateGuessed) {
        			if (fieldNode.getBegin() > 0) { // user typed '*'
        				fieldNode.setValue(moveDate(targetDateField, value, dateWithoutOffset, 
	        				"/YEAR-1000YEAR+1SECOND", "/MONTH-1000YEAR+1SECOND", "/DAY-1000YEAR+1SECOND"));
        			}
        			else {
	        			fieldNode.setValue(moveDate(targetDateField, value, dateWithoutOffset, 
	        				"/YEAR-1YEAR+1SECOND", "/MONTH-1MONTH+1SECOND", "/DAY-1DAY+1SECOND"));
        			}
        		}
        		else {
        			fieldNode.setValue(moveDate(targetDateField, normalizedDate, dateWithoutOffset, 
        			    "", "", "+0SECOND"));
        		}
        	}
        	else { // upper bound
        		if (dateGuessed) {
        			if (fieldNode.getBegin() > 0) { // user typed '*'
        				fieldNode.setValue(moveDate(targetDateField, value, dateWithoutOffset, 
	        				"/YEAR+1000YEAR-1SECOND", "/MONTH+1000YEAR-1SECOND", "/DAY+1000YEAR-1SECOND"));
        			}
        			else {
	        			fieldNode.setValue(moveDate(targetDateField, value, dateWithoutOffset, 
	        				"/YEAR+1YEAR-1SECOND", "/MONTH+1MONTH-1SECOND", "/DAY+1DAY-1SECOND"));
        			}
        		}
        		else {
        			fieldNode.setValue(moveDate(targetDateField, value, dateWithoutOffset, 
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
        lowerBound.setField(targetDateField.fieldname);
        lowerBound.setValue(targetDateField.format.format(dateWithoutOffset));
        
        upperBound.setField(targetDateField.fieldname);
        upperBound.setValue(moveDate(targetDateField, value, dateWithoutOffset, 
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
      TargetField targetField,
			String originalDate, 
			Date parsedDate,
			String...moveBy) throws QueryNodeException {
		String[] dateParts = originalDate.split("-|/");
		Date dateWithOffset = (Date) parsedDate.clone();
		dmp.setNow(parsedDate);
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
		
		
    return targetField.format(dateWithOffset);
    
	}
	
	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}
	
	
	
	class TargetField {
	  public String fieldname;
	  private SimpleDateFormat format;
	  private SimpleDateFormat parser;
	  public TargetField(String n, SimpleDateFormat p, SimpleDateFormat f) {
	    fieldname = n;
	    parser = p; // this is a parser that understand output from the tokenization
	    format = f; // this is a parser that knows how to properly output 'fieldname'
	  }
	  public Date parse(String source) throws ParseException {
	    return parser.parse(source);
	  }
	  public String format(Date source) {
	    return format.format(source);
	  }
	}

}
