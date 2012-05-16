package org.apache.lucene.queryParser.aqp.builders;

import org.apache.lucene.queryParser.aqp.config.AqpSolrRequestHandlerParams;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.builders.QueryBuilder;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.AqpFunctionQParser;
import org.apache.solr.search.ValueSourceParser;


/**
 * Returns a special FunctionQParser provider for the functions implemented
 * by Solr: http://wiki.apache.org/solr/FunctionQuery
 * 
 * @author rchyla
 *
 */
public class AqpSolrFunctionProvider implements AqpFunctionQueryBuilderProvider {

	public QueryBuilder getBuilder(String funcName, QueryNode node, QueryConfigHandler config) 
		throws QueryNodeException {
		
		AqpSolrRequestHandlerParams reqAttr = config.getAttribute(AqpSolrRequestHandlerParams.class);
		
		SolrQueryRequest req = reqAttr.getRequest();
		if (req == null)
			return null;
		
		ValueSourceParser provider = req.getCore().getValueSourceParser(funcName);
		
		if (provider == null)
			return null;
		
			
		AqpFunctionQParser fParser = new AqpFunctionQParser(null, reqAttr.getLocalParams(), 
				reqAttr.getParams(), req);
		
		AqpFunctionQueryTreeBuilder.flattenChildren(node);
		
		return new AqpFunctionQueryTreeBuilder(provider, fParser);
		
	}

}
