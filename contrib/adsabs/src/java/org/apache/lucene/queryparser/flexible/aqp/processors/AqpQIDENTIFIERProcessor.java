package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.MatchAllDocsQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsIdentifierNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQPHRASEProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor;
import org.apache.lucene.search.PrefixQuery;

/**
 * Converts QIDENTIFIER node into @{link {@link QuotedFieldQueryNode}. 
 * The field value is the @{link DefaultFieldAttribute} 
 * specified in the configuration.
 * 
 * <br/>
 * 
 * If the user specified a field, it will be set by the @{link AqpFIELDProcessor}
 * Therefore the {@link AqpQPHRASEProcessor} should run before it.
 * 
 * 
 * @see QueryConfigHandler
 * @see DefaultFieldAttribute
 *
 */
public class AqpQIDENTIFIERProcessor extends AqpQProcessor {

	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("QIDENTIFIER")) {
			return true;
		}
		return false;
	}
	
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		//String field = getDefaultFieldName();
		
		String field = getQueryConfigHandler().get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.DEFAULT_IDENTIFIER_FIELD);
		String input = null;
		int start = 0;
		int end = 0;
		
		QueryNode sc = null;
		if (node.getChildren().size() == 1) {
			sc = node.getChildren().get(0);
      if (sc instanceof AqpANTLRNode) {
        input = EscapeQuerySyntaxImpl.discardEscapeChar(((AqpANTLRNode) sc).getTokenInput()).toString();
        start = ((AqpANTLRNode) sc).getTokenStart();
        end = ((AqpANTLRNode) sc).getTokenEnd();
      }
      else if (sc instanceof MatchAllDocsQueryNode) {
				// XXX: we are missing the info about the token position, which may hurt us later
				return new PrefixWildcardQueryNode(field, "*", -1, -1);
			}
      else {
        input = ((FieldQueryNode) sc).getTextAsString();
        start = ((FieldQueryNode) sc).getBegin();
        end = ((FieldQueryNode) sc).getEnd();
      }
		}
		else {
			field = ((AqpANTLRNode) node.getChildren().get(0)).getTokenLabel();
			sc = node.getChildren().get(1);
			if (sc instanceof AqpANTLRNode) {
				input = EscapeQuerySyntaxImpl.discardEscapeChar(((AqpANTLRNode) sc).getTokenInput()).toString();
				start = ((AqpANTLRNode) sc).getTokenStart();
				end = ((AqpANTLRNode) sc).getTokenEnd();
			}
			else if (sc instanceof MatchAllDocsQueryNode) {
				// XXX: we are missing the info about the token position, which may hurt us later
				return new PrefixWildcardQueryNode(field, "*", -1, -1);
			}
			else {
				input = ((FieldQueryNode) sc).getTextAsString();
				start = ((FieldQueryNode) sc).getBegin();
				end = ((FieldQueryNode) sc).getEnd();
			}
		}
		
		
		
		/*
		if (input.contains(":")) {
			String[] vals = input.split("\\:", 2);
			String f = vals[0].toLowerCase();
			if (f.equals("doi")) {
				field = "doi";
			}
			else if (f.equals("identifier")) {
				field = "identifier";
			}
			
		}
		*/
		
		if (sc instanceof FieldQueryNode) {
			((FieldQueryNode) sc).setField(field);
			((FieldQueryNode) sc).setText(input);
			((FieldQueryNode) sc).setBegin(start);
			((FieldQueryNode) sc).setEnd(end);
			return sc;
		}
		else {
			return new AqpAdsabsIdentifierNode(field, input, start, end);
		}
		
	}

}
