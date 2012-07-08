package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQPHRASEProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor;

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
public class AqpInvenioQPHRASEProcessor extends AqpQProcessor {

	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("QPHRASE")) {
			return true;
		}
		return false;
	}
	
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		String field = getDefaultFieldName();
		
		AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);
		
		String phraseType = subChild.getTokenInput().substring(0, 1);
		
		if (phraseType.equals("\"")) { // exact phrase
			return new QuotedFieldQueryNode(field,
				EscapeQuerySyntaxImpl.discardEscapeChar(subChild
						.getTokenInput().substring(1, subChild.getTokenInput().length()-1)), 
						subChild.getTokenStart()+1,
				subChild.getTokenEnd()-1);
		}
		else if (phraseType.equals("\'")) { // the same position query
			throw new QueryNodeException(new MessageImpl("Not implemented yet!"));
		}
		else if (phraseType.equals("\u300c")) { // non-analyzed field
			return new AqpNonAnalyzedQueryNode(field,
					EscapeQuerySyntaxImpl.discardEscapeChar(subChild
							.getTokenInput().substring(1, subChild.getTokenInput().length()-1)), 
							subChild.getTokenStart()+1,
					subChild.getTokenEnd()-1);
		}
		return node;
	}

}
