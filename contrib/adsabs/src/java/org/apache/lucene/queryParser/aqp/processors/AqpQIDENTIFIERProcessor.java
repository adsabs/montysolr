package org.apache.lucene.queryParser.aqp.processors;

import org.apache.lucene.queryParser.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpAdslabsIdentifierNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryParser.core.util.UnescapedCharSequence;
import org.apache.lucene.queryParser.standard.parser.EscapeQuerySyntaxImpl;

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
		String field = "identifier";
		
		AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);
		
		String input = EscapeQuerySyntaxImpl.discardEscapeChar(subChild.getTokenInput()).toString();
		int start = subChild.getTokenStart();
		int end = subChild.getTokenEnd();
		
		if (input.contains(":")) {
			String[] vals = input.split("\\:", 2);
			String f = vals[0].toLowerCase();
			if (f.equals("doi")) {
				field = "doi";
				input = vals[1];
			}
			else if (f.equals("identifier")) {
				field = "identifier";
				input = vals[1];
			}
			
		}
		
		return new AqpAdslabsIdentifierNode(field, input, start, end);
		
	}

}
