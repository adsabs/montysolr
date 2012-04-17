package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryParser.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryParser.standard.parser.EscapeQuerySyntaxImpl;

/**
 * Converts QTRUNCATED node into @{link {@link WildcardQueryNode}. 
 * The field value used is the @{link DefaultFieldAttribute} 
 * specified in the configuration.
 * 
 * <br/>
 * 
 * If the user specified a field, it will be set by the @{link AqpFIELDProcessor}
 * Therefore the {@link AqpQTRUNCATEDProcessor} should run before it.
 * 
 * 
 * @see QueryConfigHandler
 * @see DefaultFieldAttribute
 *
 */
public class AqpQTRUNCATEDProcessor extends AqpQProcessor {

	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("QTRUNCATED")) {
			return true;
		}
		return false;
	}
	
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		String field = getDefaultFieldName();
		
		AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);
		String input = subChild.getTokenInput();
		
		if (input.contains("*?") || input.contains("?*")) {
			throw new QueryNodeException(new MessageImpl(
					QueryParserMessages.INVALID_SYNTAX, "It is not allowed to put * next to ?"
							+ input));
		}
		
		return new WildcardQueryNode(field,
				EscapeQuerySyntaxImpl.discardEscapeChar(input), subChild.getTokenStart(),
				subChild.getTokenEnd());
		
	}

}
