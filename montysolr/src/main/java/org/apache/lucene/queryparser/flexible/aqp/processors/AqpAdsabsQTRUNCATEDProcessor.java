package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.messages.QueryParserMessages;
import org.apache.lucene.queryparser.flexible.core.nodes.MatchAllDocsQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpANTLRNode;

/**
 * Converts QTRUNCATED node into @{link {@link WildcardQueryNode}. 
 * The field value used is the @{link DefaultFieldAttribute} 
 * specified in the configuration.
 * 
 * <p>
 * 
 * If the user specified a field, it will be set by the @{link AqpFIELDProcessor}
 * Therefore the {@link AqpQTRUNCATEDProcessor} should run before it.
 * 
 * 
 * @see QueryConfigHandler
 *
 */
public class AqpAdsabsQTRUNCATEDProcessor extends AqpQProcessor {
  
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
          QueryParserMessages.INVALID_SYNTAX, "It is not allowed to put '*' next to '?'"
              + input));
    }
    
    
    // special exception, we don't want query '*' to be passed
    // further on, it would be grabbed by edismax; and also
    // if it was a fielded query 'field:*', we make of it a prefix query
    QueryConfigHandler config = getQueryConfigHandler();
    if (input.equals("*")) {
    	if (node.getParent().getChildren().size() == 1) { // no field
    		return new MatchAllDocsQueryNode();
    	}
    	QueryNode fieldNode = node.getParent().getChildren().get(0);
    	String unfieldedName = config.get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.UNFIELDED_SEARCH_FIELD);
    	if (fieldNode instanceof AqpANTLRNode) {
	    	if (((AqpANTLRNode) fieldNode).getTokenInput() != null &&
	    			!((AqpANTLRNode) fieldNode).getTokenInput().equals(unfieldedName)) {
	    		return new PrefixWildcardQueryNode(((AqpANTLRNode) fieldNode).getTokenInput(),
	          "*", subChild.getTokenStart(),
	          subChild.getTokenEnd());
	    	}
    	}
    	else {
    		return new MatchAllDocsQueryNode();
    	}
      
    }
    
    
    return new WildcardQueryNode(field,
        EscapeQuerySyntaxImpl.discardEscapeChar(input), subChild.getTokenStart(),
        subChild.getTokenEnd());
    
  }

}
