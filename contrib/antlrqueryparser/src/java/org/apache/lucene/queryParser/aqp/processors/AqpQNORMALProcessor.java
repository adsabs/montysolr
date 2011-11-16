package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryParser.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.standard.parser.EscapeQuerySyntaxImpl;

/**
 * Converts QNORMAL node into @{link {@link FieldQueryNode}. 
 * The field value is the @{link DefaultFieldAttribute} 
 * specified in the configuration.
 * 
 * <br/>
 * 
 * If the user specified a field, it will be set by the @{link AqpFIELDProcessor}
 * Therefore the {@link AqpQNORMALProcessor} should run before it.
 * 
 * 
 * @see QueryConfigHandler
 * @see DefaultFieldAttribute
 *
 */
public class AqpQNORMALProcessor extends AqpQProcessor {

	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("QNORMAL")) {
			return true;
		}
		return false;
	}
	
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		String field = getDefaultFieldName();
		
		AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);
		
		return new FieldQueryNode(field,
				EscapeQuerySyntaxImpl.discardEscapeChar(subChild
						.getTokenInput()), subChild.getTokenStart(),
				subChild.getTokenEnd());
	}

}
