package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryParser.standard.parser.EscapeQuerySyntaxImpl;

/**
 * Converts QPHRASE node into @{link {@link QuotedFieldQueryNode}. 
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
public class AqpQPHRASEProcessor extends AqpQProcessor {

	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("QPHRASE")) {
			return true;
		}
		return false;
	}
	
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		String field = getDefaultFieldName();
		
		AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);
		
		return new QuotedFieldQueryNode(field,
				EscapeQuerySyntaxImpl.discardEscapeChar(subChild
						.getTokenInput().substring(1, subChild.getTokenInput().length()-1)), 
						subChild.getTokenStart()+1,
				subChild.getTokenEnd()-1);
		
	}

}
