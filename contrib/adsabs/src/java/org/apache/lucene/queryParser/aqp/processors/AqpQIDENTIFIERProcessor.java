package org.apache.lucene.queryParser.aqp.processors;

import org.apache.lucene.queryParser.aqp.nodes.AqpAdslabsIdentifierNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.util.UnescapedCharSequence;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQPHRASEProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor;

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
		
		AqpANTLRNode subChild;
		String field;
		String input = null;
		int start = 0;
		int end = 0;
		
		if (node.getChildren().size() == 1) {
			field = "identifier";
			subChild = (AqpANTLRNode) node.getChildren().get(0);
		}
		else {
			field = ((AqpANTLRNode) node.getChildren().get(0)).getTokenLabel();
			QueryNode sc = node.getChildren().get(1);
			if (sc instanceof AqpANTLRNode) {
				input = EscapeQuerySyntaxImpl.discardEscapeChar(((AqpANTLRNode) sc).getTokenInput()).toString();
				start = ((AqpANTLRNode) sc).getTokenStart();
				end = ((AqpANTLRNode) sc).getTokenEnd();
			}
			else {
				input = ((FieldQueryNode) sc).getTextAsString();
				start = ((FieldQueryNode) sc).getBegin();
				end = ((FieldQueryNode) sc).getEnd();
			}
		}
		
		
		
		
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
		
		return new AqpAdslabsIdentifierNode(field, input, start, end);
		
	}

}
