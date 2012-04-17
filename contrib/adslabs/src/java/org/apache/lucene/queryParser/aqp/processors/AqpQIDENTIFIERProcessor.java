package org.apache.lucene.queryParser.aqp.processors;

import org.apache.lucene.queryParser.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.aqp.nodes.AqpAdslabsIdentifierNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QuotedFieldQueryNode;
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
		String field = getDefaultFieldName();
		
		AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);
		
		return new AqpAdslabsIdentifierNode(field,
				EscapeQuerySyntaxImpl.discardEscapeChar(subChild
						.getTokenInput()),
						subChild.getTokenStart(),
						subChild.getTokenEnd());
		
	}

}
