package org.apache.lucene.queryParser.aqp.processors;

import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.config.DefaultDateRangeField;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.ParametricQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ParametricRangeQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ParametricQueryNode.CompareOperator;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpFeedback;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessorPost;

public class AqpQDATEProcessor extends AqpQProcessorPost {

	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("QDATE")) {
			return true;
		}
		return false;
	}
	
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		
		QueryConfigHandler queryConfig = getQueryConfigHandler();
		
		if (!queryConfig.hasAttribute(DefaultDateRangeField.class)) {
			throw new QueryNodeException(new MessageImpl(
					"Configuration error",
					"DefaultDateRangeField is missing from configuration"));
		}
		
		String dateField = queryConfig.getAttribute(DefaultDateRangeField.class).getField();
		if (dateField == null) {
			throw new QueryNodeException(new MessageImpl(
					"Configuration error",
					"DefaultDateRangeField is not set"));
		}
		
		
		AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);
		
		String input = subChild.getTokenInput();
		int start_point = subChild.getTokenStart();
		String lower = null;
		String upper = null;
		int lower_start = 0;
		int lower_end = 0;
		int upper_start = 0;
		int upper_end = 0;
		
		
		
		if (input.startsWith("-") || input.endsWith("-")) {
			AqpFeedback feedback = getFeedbackAttr();
			if (input.startsWith("-")) {
				lower = "*";
				upper = input.substring(1);
				
				lower_start = start_point + input.indexOf(upper) - 2;
				lower_end = lower_start + 1;
				
				upper_start = start_point + input.indexOf(upper);
				upper_end = upper_start + upper.length();
				
			}
			else {
				lower = input.substring(0, input.length()-1);
				upper = "*";
				
				lower_start = start_point + input.indexOf(lower);
				lower_end = lower_start + lower.length();
				
				upper_start = lower_end + 1;
				upper_end = upper_start + 1;
			}
			feedback.createEvent(AqpFeedback.TYPE.DEPRECATED, this.getClass(), subChild,
					"The query syntax \"" + input + "\" is deprecated. Please use:" + 
					"{{{" + dateField + ":[" + lower + " TO " + upper + "]}}}");
			
		}
		else {
			String[] parts = input.split("-");
			lower = parts[0];
			lower_start = start_point + input.indexOf(lower);
			lower_end = lower_start + lower.length();
			
			upper = parts[1];
			upper_start = start_point + input.indexOf(upper);
			upper_end = upper_start + upper.length();
		}
		
		ParametricQueryNode lowerBound = new ParametricQueryNode(dateField,
				CompareOperator.GE, EscapeQuerySyntaxImpl.discardEscapeChar(lower),
				lower_start, lower_end);
		ParametricQueryNode upperBound = new ParametricQueryNode(dateField,
				CompareOperator.LE, EscapeQuerySyntaxImpl.discardEscapeChar(upper),
				upper_start, upper_end);
		
		return new ParametricRangeQueryNode(lowerBound, upperBound);
		
		
	}

}